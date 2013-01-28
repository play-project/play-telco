package com.orange.play.eventData.events.inEvents;

import org.apache.commons.lang3.StringEscapeUtils2;

import com.orange.play.eventData.events.inEvent;


/*************************************************************************/
/**
 * <p>
 * This class defines IncomingCall event.
 * </p>
 * <p>
 * for input  event
 * <p>
 * <p>
 * event defines several properties, that defines an event type :
 * <ul>
 
 * </ul>
 * </p>
 * 
 * PLAY project - This IncomingCallEvent m
 * 
 * @author Philippe Gibert BIZZ/DIAM/EMB
 */

public class IncomingCallEvent extends inEvent {
	
	
	protected String direction;
	protected String message;
	protected String callerPhoneNumber;
	protected String calleePhoneNumber;
	private double latitude;
	private double longitude;

	public IncomingCallEvent() {
	}
	
	public IncomingCallEvent(String timeStamp, String uniqueId, String sequenceNumber) {
		super(timeStamp, uniqueId, sequenceNumber);
		// TODO Auto-generated constructor stub
	}
	
	public IncomingCallEvent(String timeStamp, String uniqueId, String sequenceNumber, 
			String direction, String message, String callerPhoneNumber,String calleePhoneNumber,
			 double latitude, double longitude) {
		super(timeStamp, uniqueId, sequenceNumber);
		this.direction = direction;
		this.message = message;
		this.callerPhoneNumber = callerPhoneNumber;
		this.calleePhoneNumber = calleePhoneNumber;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String toRDF(String AppID) {
		String pattern05 = AppID;
		String pattern07 = "TaxiUCIncomingCall";
		String pattern09 = "http://streams.event-processing.org/ids/TaxiUCIncomingCall#stream" ;
		
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
			"uctelco:direction \"#10#\" ;\n"+
			"uctelco:message \"#11#\" ;\n"+
			"uctelco:callerPhoneNumber \"#12#\" ;\n"+
			"uctelco:calleePhoneNumber \"#13#\" ;\n"+
			"# but also some other data according to event format\n"+
			"a :#7# ;\n"+
			":endTime \"#8#\"^^xsd:dateTime ;\n"+
			":source <http://sources.event-processing.org/ids/#5##source> ;\n"+
			":stream <#9#> ; \n"+
			":location <blank://#3#> .\n" +
			"<blank://#3#> geo:lat \"#14#\"^^xsd:double .\n" +
			"<blank://#3#> geo:long \"#15#\"^^xsd:double .\n" +
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
		avaTemplate = avaTemplate.replaceFirst("#10#", this.direction);
		avaTemplate = avaTemplate.replaceFirst("#11#", this.message);
		avaTemplate = avaTemplate.replaceFirst("#12#", this.callerPhoneNumber);
		avaTemplate = avaTemplate.replaceFirst("#13#", this.calleePhoneNumber);
		avaTemplate = avaTemplate.replaceFirst("#14#", Double.toString(this.latitude));
		avaTemplate = avaTemplate.replaceFirst("#15#", Double.toString(this.longitude));
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
	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getCalleePhoneNumber() {
		return calleePhoneNumber;
	}

	public void setCalleePhoneNumber(String calleePhoneNumber) {
		this.calleePhoneNumber = calleePhoneNumber;
	}

	public String getCallerPhoneNumber() {
		return callerPhoneNumber;
	}

	public void setCallerPhoneNumber(String callerPhoneNumber) {
		this.callerPhoneNumber = callerPhoneNumber;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}




	@Override
	public String toString() {
		return "IncomingCallEvent [uniqueId=" + uniqueId + ", sequenceNumber="
				+ sequenceNumber + ", timeStamp=" + timeStamp + ", direction="
				+ direction + ", message=" + message + ", callerPhoneNumber="
				+ callerPhoneNumber + ", calleePhoneNumber="
				+ calleePhoneNumber + ", latitude=" + latitude + ", longitude="
		+ longitude + "]";
	}


	public String toXML() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <IncomingCallEvent xmlns=\"http://www.orange.org/TaxiEventType\" "+
		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
		"xsi:schemaLocation=\"http://www.orange.org/TaxiEventType TaxiEventType.xsd\">"+
		"<uniqueId>"+uniqueId+"</uniqueId>"+
		"<sequenceNumber>"+sequenceNumber+"</sequenceNumber>"+
		"<timeStamp>"+timeStamp+"</timeStamp>"+
		"<direction>"+direction+"</direction>"+
		"<message>"+message+"</message>"+
		"<callerPhoneNumber>"+callerPhoneNumber+"</callerPhoneNumber>"+
		"<calleePhoneNumber>"+calleePhoneNumber+"</calleePhoneNumber>"+
		"<latitude>"+latitude+"</latitude>"+
		"<longitude>"+longitude+"</longitude>"+
		"</IncomingCallEvent>";
	}
	public String toWSN() {
		return "<OrangeLabsTaxiUC:IncomingCallEvent xmlns=\"http://www.orangelabs.org/DefTaxiEventType\" "+
		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
		"xmlns:OrangeLabsTaxiUC=\"http://www.orangelabs.org/taxiEventTypes\" " +
		"xsi:schemaLocation=\"http://www.orangelabs.org/TaxiEventType TaxiEventType.xsd\">"+
		"<uniqueId>"+uniqueId+"</uniqueId>"+
		"<sequenceNumber>"+sequenceNumber+"</sequenceNumber>"+
		"<timeStamp>"+timeStamp+"</timeStamp>"+
		"<direction>"+direction+"</direction>"+
		"<message>"+message+"</message>"+
		"<callerPhoneNumber>"+callerPhoneNumber+"</callerPhoneNumber>"+
		"<calleePhoneNumber>"+calleePhoneNumber+"</calleePhoneNumber>"+
		"<latitude>"+latitude+"</latitude>"+
		"<longitude>"+longitude+"</longitude>"+
		"</OrangeLabsTaxiUC:IncomingCallEvent>";
	}
//	
//	public String toString() {
//		return "IncomingCallEvent [direction=" + direction + ", message=" + message
//				+ ", CalleePhoneNumber=" + CalleePhoneNumber
//				+ ", callerPhoneNumber=" + callerPhoneNumber + ", latitude="
//				+ latitude + ", longitude=" + longitude + ", timeStamp="
//				+ timeStamp + ", uniqueId=" + uniqueId + ", sequenceNumber="
//				+ sequenceNumber + "]";
//	}
	
	

	
	

	

	

	
}
