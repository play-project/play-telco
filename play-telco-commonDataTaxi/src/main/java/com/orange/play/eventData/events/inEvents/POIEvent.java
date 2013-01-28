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
 * PLAY project - This POIEvent message is sent by Customer
 * 
 * @author Philippe Gibert BIZZ/DIAM/EMB
 */

public class POIEvent extends inEvent {

	protected String verb ; /* ACK , ... */
	protected double longitude; /* where the ACK occurs */
	protected double latitude; /* where the ACK occurs */
	protected String userType; /* Customer  */
	protected String phoneNumber; /* phoneNumber */
	protected String URLAcknoledged; /* which POI have been Acknowledged */

	public POIEvent() {
	}
	
	
	public POIEvent(String timeStamp, String uniqueId, String sequenceNumber) {
		super(timeStamp, uniqueId, sequenceNumber);
		// TODO Auto-generated constructor stub
	}


	public POIEvent(String timeStamp, String uniqueId, String sequenceNumber,
			String verb, double longitude, double latitude,
			String userType, String phoneNumber, String uRLAcknoledged) {
		super(timeStamp, uniqueId, sequenceNumber);
		this.verb = verb;
		this.longitude = longitude;
		this.latitude = latitude;
		this.userType = userType;
		this.phoneNumber = phoneNumber;
		URLAcknoledged = uRLAcknoledged;
	}

	public String toRDF(String AppID) {
		String pattern05 = AppID;
		String pattern1 = "UCIM";
		String pattern2 = "http://SERVICE.ws.play.orange.com/" ;
		String pattern10 = "http://ws.play.orange.com/Taxi.xsd";
		String pattern12 = "http://ws.play.orange.com/Stream" ;
		String Header = "<mt:nativeMessage xmlns:mt=\"http://www.event-processing.org/wsn/msgtype/\" mt:syntax=\"application/x-trig\">\n";
		String Footer= "</mt:nativeMessage>\n";	
		//File testFile = new File("templateRDF-ava.txt");
		String str1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		"<mt:nativeMessage xmlns:mt=\"http://www.event-processing.org/wsn/msgtype\" mt:syntax=\"application/x-trig\">"+
		"@prefix :        <http://events.event-processing.org/types/> .\n"+
		"@prefix e:       <http://events.event-processing.org/ids/> .\n"+
		"@prefix dsb:     <http://www.petalslink.org/dsb/topicsns/> .\n"+
		"@prefix xsd:     <http://www.w3.org/2001/XMLSchema#> .\n"+
		"@prefix #1#:    <#2#> .\n"+
		"e:#3# {\n"+
		"e:#3##event ucim:sequenceNumber \"#4#\"^^xsd:integer ;\n"+
		"ucim:timestamp \"#5#\"^^xsd:datetime ;\n"+
		"ucim:uniqueId \"#6#\" ;\n"+
		"ucim:phoneNumber \"#7#\" ;\n"+
		"ucim:status \"#8#\" ;\n"+
		"ucim:userType \"#9#\" ;\n"+
		"# but also some other data according to event format\n"+
		"a <#10#> ;\n"+
		":endTime \"#11#\"^^xsd:dateTime ;\n"+
		":source <jbi://Endpoint#source> ;\n"+
		":stream <#12#> .\n"+
		"}\n"+
		"</mt:nativeMessage>";
		// start from the template and  replace pattern with values
		//String avaTemplate=  getContents(testFile);
		String avaTemplate=  str1;
		int indexSep = this.uniqueId.indexOf(':');

		// common part 
		avaTemplate = avaTemplate.replaceFirst("#1#",pattern1 );
		avaTemplate = avaTemplate.replaceFirst("#2#",pattern2 );

		avaTemplate = avaTemplate.replaceFirst("#3#", this.uniqueId.substring(indexSep+1));
		avaTemplate = avaTemplate.replaceFirst("#3#", this.uniqueId.substring(indexSep+1));
		avaTemplate = avaTemplate.replaceFirst("#4#", this.sequenceNumber);
		avaTemplate = avaTemplate.replaceFirst("#5#", this.timeStamp);
		avaTemplate = avaTemplate.replaceFirst("#6#", this.uniqueId);
		avaTemplate = avaTemplate.replaceFirst("#10#", pattern10);
		avaTemplate = avaTemplate.replaceFirst("#11#", this.timeStamp.replace(' ','T'));
		avaTemplate = avaTemplate.replaceFirst("#12#",pattern12 );
		if (AppID == "Android")
			return    StringEscapeUtils2.escapeXml(avaTemplate) ;
		else 
			return Header + StringEscapeUtils2.escapeXml(avaTemplate)  + Footer;
	}
	
	public String getVerb() {
		return verb;
	}
	public void setVerb(String verb) {
		this.verb = verb;
	}
	
	public double getLongitude() {
		return longitude;
	}


	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}


	public double getLatitude() {
		return latitude;
	}


	public void setLatitude(double latitude) {
		this.latitude = latitude;
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
	public String getURLAcknoledged() {
		return URLAcknoledged;
	}
	public void setURLAcknoledged(String uRLAcknoledged) {
		URLAcknoledged = uRLAcknoledged;
	}
//	@Override
//	public String toString() {
//		return "POIEvent [verb=" + verb + ", longitude=" + longitude
//				+ ", latitude=" + latitude + ", userType=" + userType
//				+ ", phoneNumber=" + phoneNumber + ", URLAcknoledged="
//				+ URLAcknoledged + "]";
//	};
	
	
	
	public String toStringCEP() {
		return "stream=POIEvent,verb=" + verb + ",longitude=" + longitude
					+ ",latitude=" + latitude + ",userType=" + userType
					+ ",phoneNumber=" + phoneNumber + ",URLAcknoledged="
					+ URLAcknoledged;
	}


	@Override
	public String toString() {
		return "POIEvent [uniqueId=" + uniqueId + ", sequenceNumber="
				+ sequenceNumber + ", timeStamp=" + timeStamp + ", verb="
				+ verb + ", longitude=" + longitude + ", latitude=" + latitude
				+ ", userType=" + userType + ", phoneNumber=" + phoneNumber
				+ ", URLAcknoledged=" + URLAcknoledged + "]";
	}
	public String toXML() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <POIEvent xmlns=\"http://www.orange.org/TaxiEventType\" "+
		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
		"xsi:schemaLocation=\"http://www.orange.org/TaxiEventType TaxiEventType.xsd\">"+
		"<uniqueId>"+uniqueId+"</uniqueId>"+
		"<sequenceNumber>"+sequenceNumber+"</sequenceNumber>"+
		"<timeStamp>"+timeStamp+"</timeStamp>"+
		"<verb>"+timeStamp+"</verb>"+
		"<latitude>"+latitude+"</latitude>"+
		"<longitude>"+longitude+"</longitude>"+
		"<userType>"+userType+"</userType>"+
		"<phoneNumber>"+phoneNumber+"</phoneNumber>"+
		"<URLAcknoledged>"+phoneNumber+"</URLAcknoledged>"+
		"</POIEvent>";
	}
	public String toWSN() {
		return "<OrangeLabsTaxiUC:POIEvent xmlns=\"http://www.orangelabs.org/DefTaxiEventType\" "+
		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
		"xmlns:OrangeLabsTaxiUC=\"http://www.orangelabs.org/taxiEventTypes\" " +
		"xsi:schemaLocation=\"http://www.orangelabs.org/TaxiEventType TaxiEventType.xsd\">"+
		"<uniqueId>"+uniqueId+"</uniqueId>"+
		"<sequenceNumber>"+sequenceNumber+"</sequenceNumber>"+
		"<timeStamp>"+timeStamp+"</timeStamp>"+
		"<verb>"+timeStamp+"</verb>"+
		"<latitude>"+latitude+"</latitude>"+
		"<longitude>"+longitude+"</longitude>"+
		"<userType>"+userType+"</userType>"+
		"<phoneNumber>"+phoneNumber+"</phoneNumber>"+
		"<URLAcknoledged>"+phoneNumber+"</URLAcknoledged>"+
		"</OrangeLabsTaxiUC:POIEvent>";
}
	
	
}
