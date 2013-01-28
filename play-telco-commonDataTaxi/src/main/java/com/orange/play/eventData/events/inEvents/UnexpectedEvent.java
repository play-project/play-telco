package com.orange.play.eventData.events.inEvents;

import org.apache.commons.lang3.StringEscapeUtils2;

import com.orange.play.eventData.events.inEvent;

/*************************************************************************/
/**
 * <p>
 * This class defines TyreFlatEvent event.
 * </p>
 * <p>
 * for input and output event
 * <p>
 * <p>
 * event defines several properties, that defines an event type :
 * <ul>
 * <li>A payloadJamEvent</li>
 * </ul>
 * </p>
 * 
 * PLAY project - This UnexpectedEvent message is sent by the taxi Driver
 * 
 * @author Philippe Gibert BIZZ/DIAM/EMB
 */

public class UnexpectedEvent extends inEvent {


	protected String status/* TYREFLAT, ACCIDENT, LATE*/;
	protected String unexpectedMessage;
	protected String userType; /* Customer or Driver */
	protected String phoneNumber;

	public UnexpectedEvent() {
	};

	public UnexpectedEvent(String timeStamp, String uniqueId,
			String sequenceNumber) {
		super(timeStamp, uniqueId, sequenceNumber);
	}

	public UnexpectedEvent(String timeStamp, String uniqueId,
			String sequenceNumber,String status, String unexpectedMessage,
			String userType, String phoneNumber) {
		super(timeStamp, uniqueId, sequenceNumber);
		this.status = status;
		this.unexpectedMessage = unexpectedMessage;
		this.userType = userType;
		this.phoneNumber = phoneNumber;
	}
	public String toStringCEP1() {
		return "stream=UnexpectedEvent,uniqueId=" + uniqueId + ",sequenceNumber="
				+ sequenceNumber + ",status=" + status +
				",timeStamp=" + timeStamp + ",unexpectedMessage="
				+ unexpectedMessage + ",userType=" + userType + ",phoneNumber="
				+ phoneNumber ;
	}

	
	public String toRDF(String AppID) {
		String pattern05 = AppID;
		String pattern07 = "TaxiUCUnexpected";
		String pattern09 = "http://streams.event-processing.org/ids/TaxiUCUnexpected#stream" ;
		
		String Header = "<mt:nativeMessage xmlns:mt=\"http://www.event-processing.org/wsn/msgtype/\" mt:syntax=\"application/x-trig\">\n";
		String Footer= "</mt:nativeMessage>\n";
		String str1 = 
			"@prefix :        <http://events.event-processing.org/types/> .\n"+
			"@prefix e:       <http://events.event-processing.org/ids/> .\n"+
			"@prefix xsd:     <http://www.w3.org/2001/XMLSchema#> .\n"+
			"@prefix uctelco: <http://events.event-processing.org/uc/telco/> .\n"+
			"@prefix geo:     <http://www.w3.org/2003/01/geo/wgs84_pos#> .\n"+
			"@prefix user:    <http://graph.facebook.com/schema/user#> .\n"+
			"e:#3# {\n"+
			"e:#3##event uctelco:sequenceNumber \"#4#\"^^xsd:integer ;\n"+
			"uctelco:uniqueId \"#6#\" ;\n"+
			"uctelco:status \"#10#\" ;\n"+
			"uctelco:unexpectedMessage \"#11#\" ;\n"+
			"uctelco:userType \"#12#\" ;\n"+
			"uctelco:phoneNumber \"#13#\" ;\n"+
			//"uctelco:twId \"#14#\" ;\n"+
			//"user:id \"#15#\" ;\n"+
			"# but also some other data according to event format\n"+
			"a :#7# ;\n"+
			":endTime \"#8#\"^^xsd:dateTime ;\n"+
			":source <http://sources.event-processing.org/ids/#5##source> ;\n"+
			":stream <#9#> . \n"+
			"}\n";
		String avaTemplate=  str1;
		int indexSep = this.uniqueId.indexOf(':');
		// common part 
		//	System.out.print("avaTemplate\n"+avaTemplate);
	
		avaTemplate = avaTemplate.replaceFirst("#3#", this.uniqueId.substring(indexSep+1));
		avaTemplate = avaTemplate.replaceFirst("#3#", this.uniqueId.substring(indexSep+1));
		avaTemplate = avaTemplate.replaceFirst("#4#", this.sequenceNumber);
		avaTemplate = avaTemplate.replaceFirst("#5#", pattern05);
		avaTemplate = avaTemplate.replaceFirst("#6#", this.uniqueId);
		avaTemplate = avaTemplate.replaceFirst("#7#", pattern07);
		avaTemplate = avaTemplate.replaceFirst("#8#", this.timeStamp.replace(' ','T'));
		avaTemplate = avaTemplate.replaceFirst("#9#",pattern09 );
		// local part of the event

		avaTemplate = avaTemplate.replaceFirst("#10#", this.status);
		avaTemplate = avaTemplate.replaceFirst("#11#", this.unexpectedMessage);
		avaTemplate = avaTemplate.replaceFirst("#12#", this.userType);
		avaTemplate = avaTemplate.replaceFirst("#13#", this.phoneNumber);
		if (AppID == "Android")
			return    StringEscapeUtils2.escapeXml(avaTemplate) ;
		else 
			return Header + StringEscapeUtils2.escapeXml(avaTemplate)  + Footer;
	}
	/**
	 * @return the unexpectedMessage
	 */
	public String getUnexpectedMessage() {
		return unexpectedMessage;
	}

	/**
	 * @param unexpectedMessage
	 *            the unexpectedMessage to set
	 */
	public void setUnexpectedMessage(String unexpectedMessage) {
		this.unexpectedMessage = unexpectedMessage;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus (String status) {
		this.status = status;
	}

	//	public String toString() {
	//		return "UnexpectedEvent [status=" + status + ", unexpectedMessage="
	//				+ unexpectedMessage + ", userType=" + userType
	//				+ ", phoneNumber=" + phoneNumber + ", timeStamp=" + timeStamp
	//				+ ", uniqueId=" + uniqueId + ", sequenceNumber="
	//				+ sequenceNumber + "]";
	//	}



	// facility to send directly to CEP for Sprint 0 whatever the transport is
	public String toStringCEP() {
		return "stream=UnexpectedEvent,status=" + status
		+ ",unexpectedMessage=" + unexpectedMessage + ",userType="
		+ userType + ",phoneNumber=" + phoneNumber + ",timeStamp="
		+ timeStamp + ",uniqueId=" + uniqueId + ",sequenceNumber="
		+ sequenceNumber;
	}

	@Override
	public String toString() {
		return "UnexpectedEvent [uniqueId=" + uniqueId + ", sequenceNumber="
		+ sequenceNumber + ", timeStamp=" + timeStamp + ", status="
		+ status + ", unexpectedMessage=" + unexpectedMessage
		+ ", userType=" + userType + ", phoneNumber=" + phoneNumber
		+ "]";
	}
	public String toXML() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <UnexpectedEventEvent xmlns=\"http://www.orange.org/TaxiEventType\" "+
		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
		"xsi:schemaLocation=\"http://www.orange.org/TaxiEventType TaxiEventType.xsd\">"+
		"<uniqueId>"+uniqueId+"</uniqueId>"+
		"<sequenceNumber>"+sequenceNumber+"</sequenceNumber>"+
		"<timeStamp>"+timeStamp+"</timeStamp>"+
		"<status>"+status+"</status>"+
		"<unexpectedMessage>"+unexpectedMessage+"</unexpectedMessage>"+
		"<userType>"+userType+"</userType>"+
		"<phoneNumber>"+phoneNumber+"</phoneNumber>"+
		"</UnexpectedEventEvent>";
	}
	public String toWSN() {
		return "<OrangeLabsTaxiUC:UnexpectedEvent xmlns=\"http://www.orangelabs.org/DefTaxiEventType\" "+
		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
		"xmlns:OrangeLabsTaxiUC=\"http://www.orangelabs.org/taxiEventTypes\" " +
		"xsi:schemaLocation=\"http://www.orangelabs.org/TaxiEventType TaxiEventType.xsd\">"+
		"<uniqueId>"+uniqueId+"</uniqueId>"+
		"<sequenceNumber>"+sequenceNumber+"</sequenceNumber>"+
		"<timeStamp>"+timeStamp+"</timeStamp>"+
		"<status>"+status+"</status>"+
		"<unexpectedMessage>"+unexpectedMessage+"</unexpectedMessage>"+
		"<userType>"+userType+"</userType>"+
		"<phoneNumber>"+phoneNumber+"</phoneNumber>"+
		"</OrangeLabsTaxiUC:UnexpectedEvent>";
	}


	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
