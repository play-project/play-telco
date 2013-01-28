package com.orange.play.eventData.events.outEvents;


import org.apache.commons.lang3.StringEscapeUtils2;


import com.orange.play.eventData.events.outEvent;

//ok
public class Click2CallEvent extends outEvent {
	
	protected String message;
	protected String callerPhoneNumber;
	protected String calleePhoneNumber;

	public Click2CallEvent() {
	}

	public Click2CallEvent(String timeStamp, String uniqueId, String sequenceNumber) {
		super(timeStamp, uniqueId, sequenceNumber);
		// TODO Auto-generated constructor stub
	}
	
	public Click2CallEvent(String timeStamp, String uniqueId, String sequenceNumber, 
			 String message, String callerPhoneNumber,String calleePhoneNumber) {
		super(timeStamp, uniqueId, sequenceNumber);
		
		this.message = message;
		this.callerPhoneNumber = callerPhoneNumber;
		this.calleePhoneNumber = calleePhoneNumber;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCallerPhoneNumber() {
		return callerPhoneNumber;
	}

	public void setCallerPhoneNumber(String callerPhoneNumber) {
		this.callerPhoneNumber = callerPhoneNumber;
	}

	public String getCalleePhoneNumber() {
		return calleePhoneNumber;
	}

	public void setCalleePhoneNumber(String calleePhoneNumber) {
		this.calleePhoneNumber = calleePhoneNumber;
	}
	
	public String toRDF(String AppID) {
		String pattern05 = AppID;
		String pattern07 = "TaxiUCClic2Call";
		String pattern09 = "http://streams.event-processing.org/ids/TaxiUCClic2Call#stream" ;
		
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
			"uctelco:message \"#10#\" ;\n"+
			"uctelco:callerPhoneNumber \"#11#\" ;\n"+
			"uctelco:calleePhoneNumber \"#12#\" ;\n"+
			"# but also some other data according to event format\n"+
			"a :#7# ;\n"+
			":source <http://sources.event-processing.org/ids/#5##source> ;\n"+
			":stream <#9#> . \n"+
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
		avaTemplate = avaTemplate.replaceFirst("#10#", this.message);
		avaTemplate = avaTemplate.replaceFirst("#11#", this.callerPhoneNumber);
		avaTemplate = avaTemplate.replaceFirst("#12#", this.calleePhoneNumber);
		
		if (AppID == "Android")
			return    StringEscapeUtils2.escapeXml(avaTemplate) ;
		else 
			return Header + StringEscapeUtils2.escapeXml(avaTemplate)  + Footer;
	}


	public String toXML() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <CallEvent xmlns=\"http://www.orange.org/TaxiEventType\" "+
		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
		"xsi:schemaLocation=\"http://www.orange.org/TaxiEventType TaxiEventType.xsd\">"+
		"<uniqueId>"+uniqueId+"</uniqueId>"+
		"<sequenceNumber>"+sequenceNumber+"</sequenceNumber>"+
		"<timeStamp>"+timeStamp+"</timeStamp>"+
		"<message>"+message+"</message>"+
		"<callerPhoneNumber>"+callerPhoneNumber+"</callerPhoneNumber>"+
		"<calleePhoneNumber>"+calleePhoneNumber+"</calleePhoneNumber>"+
	
		"</CallEvent>";
	}
	public String toWSN() {
		return "<OrangeLabsTaxiUC:Clic2CallEvent xmlns=\"http://www.orangelabs.org/DefTaxiEventType\" "+
		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
		"xmlns:OrangeLabsTaxiUC=\"http://www.orangelabs.org/taxiEventTypes\" " +
		"xsi:schemaLocation=\"http://www.orangelabs.org/TaxiEventType TaxiEventType.xsd\">"+
		"<uniqueId>"+uniqueId+"</uniqueId>"+
		"<sequenceNumber>"+sequenceNumber+"</sequenceNumber>"+
		"<timeStamp>"+timeStamp+"</timeStamp>"+
		"<message>"+message+"</message>"+
		"<callerPhoneNumber>"+callerPhoneNumber+"</callerPhoneNumber>"+
		"<calleePhoneNumber>"+calleePhoneNumber+"</calleePhoneNumber>"+
		"</OrangeLabsTaxiUC:Clic2CallEvent>";
	}

	
	
	
	

	
	

	

	

	
}
