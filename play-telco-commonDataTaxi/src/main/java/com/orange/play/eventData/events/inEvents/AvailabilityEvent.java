package com.orange.play.eventData.events.inEvents;



import org.apache.commons.lang3.StringEscapeUtils2;

import com.orange.play.eventData.events.inEvent;


/*************************************************************************/
/**
 * <p>This class defines Availability  event.</p>
 * <p> for input and output event<p>
 * <p>
 * event defines several properties, that defines an 
 * event type : 
 * <ul>
 *   <li>A CalendarStatus</li>
 * </ul>
 * </p>
 *
 * PLAY project - Availability Event 
 *  the taxi driver's status  from the IS taxi company point of view from  his calendar
 * @author Philippe Gibert BIZZ/DIAM/EMB
 */
public class AvailabilityEvent extends inEvent {
	/**  Availability  status events  for the  taxi Driver  */
	public enum CalendarStatus { WORKING, HOLIDAY, MEETING }
	protected CalendarStatus status;
	protected String userType;  /* Customer or Driver */
	protected String phoneNumber;


	public AvailabilityEvent() {};	
	public void setStatus(CalendarStatus status) {
		this.status = status;
	}
	public CalendarStatus getStatus() {
		return status;
	}
	public AvailabilityEvent(CalendarStatus status, String userType,
			String phoneNumber) {
		super();
		this.status = status;
		this.userType = userType;
		this.phoneNumber = phoneNumber;
	}
	public AvailabilityEvent(String timeStamp, String uniqueId,
			String sequenceNumber,CalendarStatus status, String userType,
			String phoneNumber) {
		super(timeStamp, uniqueId, sequenceNumber);
		this.status = status;
		this.userType = userType;
		this.phoneNumber = phoneNumber;
		// TODO Auto-generated constructor stub
	}
	
	
	public String toStringCEP1() {
		return "stream=AvailabilityEvent,uniqueId=" + uniqueId + ",sequenceNumber="
		+ sequenceNumber + ",timeStamp=" + timeStamp + ",status="
		+ status + ",userType=" + userType + ",phoneNumber="
		+ phoneNumber ; 
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
	/** facility to send directly to  CEP  for Sprint 0 whatever the transport is */
	public String toStringCEP() {
		return "Availability=timestamp=" + timeStamp + ", uniqueId="
		+ uniqueId + ", sequenceNumber=" + sequenceNumber + ", status="
		+ status ;
	}

	public String toString() {
		return "AvailabilityEvent [uniqueId=" + uniqueId + ", sequenceNumber="
		+ sequenceNumber + ", timeStamp=" + timeStamp + ", status="
		+ status + ", userType=" + userType + ", phoneNumber="
		+ phoneNumber + "]";
	}
	public String toXML() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <AvailabilityEvent xmlns=\"http://www.orange.org/TaxiEventType\" "+
		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
		"xsi:schemaLocation=\"http://www.orange.org/TaxiEventType TaxiEventType.xsd\">"+
		"<uniqueId>"+uniqueId+"</uniqueId>"+
		"<sequenceNumber>"+sequenceNumber+"</sequenceNumber>"+
		"<timeStamp>"+timeStamp+"</timeStamp>"+
		"<status>"+status+"</status>"+
		"<userType>"+userType+"</userType>"+
		"<phoneNumber>"+phoneNumber+"</phoneNumber>"+
		"</AvailabilityEvent>";
	}
	public String toWSN() {
		return "<OrangeLabsTaxiUC:AvailabilityEvent xmlns=\"http://www.orangelabs.org/DefTaxiEventType\" "+
		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
		"xmlns:OrangeLabsTaxiUC=\"http://www.orangelabs.org/taxiEventTypes\" " +
		"xsi:schemaLocation=\"http://www.orangelabs.org/TaxiEventType TaxiEventType.xsd\">"+
		"<uniqueId>"+uniqueId+"</uniqueId>"+
		"<sequenceNumber>"+sequenceNumber+"</sequenceNumber>"+
		"<timeStamp>"+timeStamp+"</timeStamp>"+
		"<status>"+status+"</status>"+
		"<userType>"+userType+"</userType>"+
		"<phoneNumber>"+phoneNumber+"</phoneNumber>"+
		"</OrangeLabsTaxiUC:AvailabilityEvent>";
	}
	public String toRDF(String AppID) {
		
		String pattern07 = "TaxiUCAvailability";
		String pattern09 = "http://streams.event-processing.org/ids/TaxiUCAvailability#stream" ;
		String pattern05 = AppID;
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
			"uctelco:phoneNumber \"#10#\" ;\n"+
			"uctelco:status \"#11#\" ;\n"+
			"uctelco:userType \"#12#\" ;\n"+
			//"uctelco:twId \"#13#\" ;\n"+
			//"user:id \"#14#\" ;\n"+
			"# but also some other data according to event format\n"+
			"a :#7# ;\n"+
			":endTime \"#8#\"^^xsd:dateTime ;\n"+
			":source <http://sources.event-processing.org/ids/#5##source> ;\n"+
			":stream <#9#> .\n"+
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
		avaTemplate = avaTemplate.replaceFirst("#10#", this.phoneNumber);
		avaTemplate = avaTemplate.replaceFirst("#11#", this.status.toString());
		avaTemplate = avaTemplate.replaceFirst("#12#", this.userType);
		if (AppID == "Android")
			return    StringEscapeUtils2.escapeXml(avaTemplate) ;
		else 
			return Header + StringEscapeUtils2.escapeXml(avaTemplate)  + Footer;
		
	}
}
//String avaTemplate=  getContents(testFile);
//File testFile = new File("templateRDF-ava.txt");



