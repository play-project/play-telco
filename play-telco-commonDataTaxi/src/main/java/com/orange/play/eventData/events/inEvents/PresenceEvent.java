package com.orange.play.eventData.events.inEvents;


import org.apache.commons.lang3.StringEscapeUtils2;

import com.orange.play.eventData.events.inEvent;

/*************************************************************************/
/**
 * <p>
 * This class defines Presence event.
 * </p>
 * <p>
 * for input and output event
 * <p>
 * <p>
 * event defines several properties, that defines an event type :
 * <ul>
 * <li>A IMStatus</li>
 * </ul>
 * </p>
 * 
 * PLAY project - Presence set by the taxi driver indicating his current IM
 * status
 * 
 * @author Philippe Gibert BIZZ/DIAM/EMB
 */

public class PresenceEvent extends inEvent {

	public enum NetworkStatus {
		ON, OFF
	}

	private boolean status;
	private String userType; /* Customer or Driver */
	private String phoneNumber;

	

	public PresenceEvent() {
	};
	public PresenceEvent(String timeStamp, String uniqueId,
			String sequenceNumber) {
		super(timeStamp, uniqueId, sequenceNumber);
		// TODO Auto-generated constructor stub
	}
	
	
	public PresenceEvent(String timeStamp, String uniqueId,
			String sequenceNumber,boolean status, String userType, String phoneNumber) {
		super(timeStamp, uniqueId, sequenceNumber);
		this.status = status;
		this.userType = userType;
		this.phoneNumber = phoneNumber;
	}
	
	public String toStringCEP1() {
		return "stream=PresenceEvent,uniqueId=" + uniqueId + ",sequenceNumber="
				+ sequenceNumber + ",timeStamp=" + timeStamp + ",status="
				+ status + ",userType=" + userType + ",phoneNumber="
				+ phoneNumber ;
	}

	public String toRDF(String AppID) {
		String pattern05 = AppID;
		String pattern07 = "TaxiUCPresence";
		String pattern09 = "http://streams.event-processing.org/ids/TaxiUCPresence#stream" ;
		
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
			"uctelco:userType \"#10#\" ;\n"+
			"uctelco:status \"#11#\" ;\n"+
			"uctelco:phoneNumber \"#12#\" ;\n"+
			//"uctelco:twId \"#13#\" ;\n"+
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
		avaTemplate = avaTemplate.replaceFirst("#9#", pattern09 );
		// local part of the event
		
		avaTemplate = avaTemplate.replaceFirst("#10#", Boolean.toString(this.status));
		avaTemplate = avaTemplate.replaceFirst("#11#", this.userType);
		avaTemplate = avaTemplate.replaceFirst("#12#", this.phoneNumber);
		if (AppID == "Android")
			return    StringEscapeUtils2.escapeXml(avaTemplate) ;
		else 
			return Header + StringEscapeUtils2.escapeXml(avaTemplate)  + Footer;
	}
	
	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
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

	// facility to send directly to CEP for Sprint 0 whatever the transport is
	public String toStringCEP() {
		return "stream=PresenceEvent,phoneNumber=" + phoneNumber + ",status="
				+ status + ",userType=" + userType + ", timeStamp=" + timeStamp
				+ ",uniqueId=" + uniqueId + ",sequenceNumber=" + sequenceNumber;
	}
	@Override
	public String toString() {
		return "PresenceEvent [uniqueId=" + uniqueId + ", sequenceNumber="
				+ sequenceNumber + ", timeStamp=" + timeStamp + ", status="
				+ status + ", userType=" + userType + ", phoneNumber="
				+ phoneNumber + "]";
	}

	public String toXML() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <PresenceEvent xmlns=\"http://www.orange.org/TaxiEventType\" "+
		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
		"xsi:schemaLocation=\"http://www.orange.org/TaxiEventType TaxiEventType.xsd\">"+
		"<uniqueId>"+uniqueId+"</uniqueId>"+
		"<sequenceNumber>"+sequenceNumber+"</sequenceNumber>"+
		"<timeStamp>"+timeStamp+"</timeStamp>"+
		"<status>"+status+"</status>"+
		"<userType>"+userType+"</userType>"+
		"<phoneNumber>"+phoneNumber+"</phoneNumber>"+
		"</PresenceEvent>";
	}
	
	public String toWSN() {
		return "<OrangeLabsTaxiUC:PresenceEvent xmlns=\"http://www.orangelabs.org/DefTaxiEventType\" "+
		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
		"xmlns:OrangeLabsTaxiUC=\"http://www.orangelabs.org/taxiEventTypes\" " +
		"xsi:schemaLocation=\"http://www.orangelabs.org/TaxiEventType TaxiEventType.xsd\">"+
		"<uniqueId>"+uniqueId+"</uniqueId>"+
		"<sequenceNumber>"+sequenceNumber+"</sequenceNumber>"+
		"<timeStamp>"+timeStamp+"</timeStamp>"+
		"<status>"+status+"</status>"+
		"<userType>"+userType+"</userType>"+
		"<phoneNumber>"+phoneNumber+"</phoneNumber>"+
		"</OrangeLabsTaxiUC:PresenceEvent>";
}


}
