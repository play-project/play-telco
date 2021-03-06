package com.orange.play.eventData.events.inEvents;

import org.apache.commons.lang3.StringEscapeUtils2;

import com.orange.play.eventData.events.inEvent;

public class FaceBookEvent extends inEvent{
	protected String fbId;
	protected String message;
	protected String status;
	protected double latitude;
	protected double longitude;
	
	public FaceBookEvent(String timeStamp, String uniqueId, String sequenceNumber,
			String fbId, String message, String status,
			double latitude, double longitude ) {
		super(timeStamp, uniqueId, sequenceNumber);
		this.fbId = fbId;
		this.message = message;
		this.status = status;
		this.latitude = latitude;
		this.longitude = longitude;
		// TODO Auto-generated constructor stub
	}

	public FaceBookEvent(String message) {
		super();
		this.message = message;
	}
	public String toRDF(String AppID) {
		String pattern05 = AppID;
		String pattern07 = "TaxiUCFaceBook";
		String pattern09 = "http://streams.event-processing.org/ids/TaxiUCFaceBook#stream" ;
		String Header = "<mt:nativeMessage xmlns:mt=\"http://www.event-processing.org/wsn/msgtype/\" mt:syntax=\"application/x-trig\">\n";
		String Footer= "</mt:nativeMessage>\n";
		String str1 = 
			"@prefix :        <http://events.event-processing.org/types/> .\n"+
			"@prefix e:       <http://events.event-processing.org/ids/> .\n"+
			"@prefix dsb:     <http://www.petalslink.org/dsb/topicsns/> .\n"+
			"@prefix xsd:     <http://www.w3.org/2001/XMLSchema#> .\n"+
			"@prefix uctelco: <http://events.event-processing.org/uc/telco/> .\n"+
			"@prefix geo:     <http://www.w3.org/2003/01/geo/wgs84_pos#> .\n"+
			"e:#3# {\n"+
			"e:#3##event uctelco:sequenceNumber \"#4#\"^^xsd:integer ;\n"+
			"uctelco:uniqueId \"#6#\" ;\n"+
			"uctelco:fbId \"#10#\" ;\n"+
			"uctelco:message \"#13#\" ;\n"+
			"uctelco:status \"#14#\" ;\n"+
			"# but also some other data according to event format\n"+
			"a :#7# ;\n"+
			":endTime \"#8#\"^^xsd:dateTime ;\n"+
			":source <http://sources.event-processing.org/ids/#5##source> ;\n"+
			":stream <#9#> ; \n"+
			":location <blank://#3#> .\n" +
			"<blank://#3#> geo:lat \"#11#\"^^xsd:double .\n" +
			"<blank://#3#> geo:long \"#12#\"^^xsd:double .\n" +
			"}\n";
		String avaTemplate=  str1;
		int indexSep = this.uniqueId.indexOf(':');
	
		avaTemplate = avaTemplate.replaceFirst("#3#", this.uniqueId.substring(indexSep+1));
		avaTemplate = avaTemplate.replaceFirst("#3#", this.uniqueId.substring(indexSep+1));
		avaTemplate = avaTemplate.replaceFirst("#4#", this.sequenceNumber);
		avaTemplate = avaTemplate.replaceFirst("#5#", pattern05);
		avaTemplate = avaTemplate.replaceFirst("#6#", this.uniqueId);
		avaTemplate = avaTemplate.replaceFirst("#7#", pattern07);
		avaTemplate = avaTemplate.replaceFirst("#8#", this.timeStamp.replace(' ','T'));
		avaTemplate = avaTemplate.replaceFirst("#9#",pattern09 );
		// local part of the event
		avaTemplate = avaTemplate.replaceFirst("#10#", this.fbId);
		avaTemplate = avaTemplate.replaceFirst("#11#", Double.toString(this.latitude));
		avaTemplate = avaTemplate.replaceFirst("#12#", Double.toString(this.longitude));
		avaTemplate = avaTemplate.replaceFirst("#13#", this.message);
		avaTemplate = avaTemplate.replaceFirst("#14#", this.status);
		if (AppID == "Android")
			return    StringEscapeUtils2.escapeXml(avaTemplate) ;
		else 
			return Header + StringEscapeUtils2.escapeXml(avaTemplate)  + Footer;
		
	}
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

//	@Override
//	public String toString() {
//		return "FaceBookEvent [message=" + message + ", getMessage()="
//				+ getMessage() + ", getClass()=" + getClass() + ", hashCode()="
//				+ hashCode() + ", toString()=" + super.toString() + "]";
//	}
	
	@Override
	public String toString() {
		return "FaceBookEvent [uniqueId=" + uniqueId + ", sequenceNumber="
				+ sequenceNumber + ", timeStamp=" + timeStamp + ", fbId="
				+ fbId + ", message=" + message + ", status=" + status
				+ ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}

	
	public String toXML() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <FaceBookEvent xmlns=\"http://www.orange.org/TaxiEventType\" "+
		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
		"xsi:schemaLocation=\"http://www.orange.org/TaxiEventType TaxiEventType.xsd\">"+
		"<uniqueId>"+uniqueId+"</uniqueId>"+
		"<sequenceNumber>"+sequenceNumber+"</sequenceNumber>"+
		"<timeStamp>"+timeStamp+"</timeStamp>"+
		"<fbId>"+fbId+"</fbId>"+
		"<message>"+message+"</message>"+
		"<status>"+status+"</status>"+
		"<latitude>"+latitude+"</latitude>"+
		"<longitude>"+longitude+"</longitude>"+
		"</FaceBookEvent>";
	}
	public String toWSN() {
		return "<OrangeLabsTaxiUC:FaceBookEvent xmlns=\"http://www.orangelabs.org/DefTaxiEventType\" "+
		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
		"xmlns:OrangeLabsTaxiUC=\"http://www.orangelabs.org/taxiEventTypes\" " +
		"xsi:schemaLocation=\"http://www.orangelabs.org/TaxiEventType TaxiEventType.xsd\">"+
		"<uniqueId>"+uniqueId+"</uniqueId>"+
		"<sequenceNumber>"+sequenceNumber+"</sequenceNumber>"+
		"<timeStamp>"+timeStamp+"</timeStamp>"+
		"<fbId>"+fbId+"</fbId>"+
		"<message>"+message+"</message>"+
		"<status>"+status+"</status>"+
		"<latitude>"+latitude+"</latitude>"+
		"<longitude>"+longitude+"</longitude>"+
		"</OrangeLabsTaxiUC:FaceBookEvent>";
	}
}
