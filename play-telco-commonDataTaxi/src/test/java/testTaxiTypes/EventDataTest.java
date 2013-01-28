package testTaxiTypes;

import java.io.BufferedWriter;
import java.io.FileWriter;

import org.apache.commons.lang3.StringEscapeUtils2;
import org.petalslink.dsb.notification.light.NotificationGenerator;
import org.petalslink.dsb.notification.light.Topic;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.orange.play.eventData.events.inEvents.AvailabilityEvent;

import com.orange.play.eventData.events.inEvents.AvailabilityEvent.CalendarStatus;
import com.orange.play.eventData.events.inEvents.FaceBookEvent;
import com.orange.play.eventData.events.inEvents.GeolocationEvent;
import com.orange.play.eventData.events.inEvents.IncomingCallEvent;
import com.orange.play.eventData.events.inEvents.OutNetworkEvent;

import com.orange.play.eventData.events.inEvents.PresenceEvent;
import com.orange.play.eventData.events.inEvents.SMSCustomerAlert;
import com.orange.play.eventData.events.inEvents.TrafficJamEvent;
import com.orange.play.eventData.events.inEvents.TwitterEvent;
import com.orange.play.eventData.events.inEvents.UnexpectedEvent;
import com.orange.play.eventData.events.inEvents.CallEvent;

public class EventDataTest  extends TestCase {

	static void toFile( String filename, String content, String suffix){
		try{
			// Create file 
			FileWriter fstream = new FileWriter("target/"+filename+ suffix);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(content);
			//Close the output stream
			out.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

	};
	public EventDataTest(String testName) {
		super(testName);
		String srdf = ".rdf";
		String sxml = ".xml";
		Topic tc = new Topic();
		String namespaceURI = "http://www.petalslink.org/dsb/topicsns/";
		tc.prefix = "dsb";
		tc.ns = namespaceURI;

		//AvailabilityEvent
		AvailabilityEvent av1 = new AvailabilityEvent(
				"2011-12-06 18:33:36.681","taxiUC:ava-13","13",CalendarStatus.WORKING,"Driver","33600000011");
		System.out.println("+++++++++");
		System.out.print("av1.toXML\n"+av1.toXML());
		System.out.println("+++++++++");
		System.out.println("+++++++++");
		System.out.print("Unescaping: \n"+org.apache.commons.lang3.StringEscapeUtils2.unescapeXml(av1.toXML()));
		System.out.println("+++++++++");
		//AndroidWSNNotification
		tc.name = "TaxiUCAvailability";
		System.out.println("WSNAndroid-Availability:"+NotificationGenerator.generate(av1.toXML(),tc));
		//	toFile(tc.name+"-LWSN", NotificationGenerator.generate(av1.toXML(),tc));
		toFile(tc.name+"-HWSN", av1.toWSN(),sxml);
		toFile(tc.name+"-HWSN", av1.toRDF("testTaxi"),srdf);
		// GeoLocationEvent
		GeolocationEvent gl1 = new GeolocationEvent("2011-12-06 18:33:36.681","taxiUC:loc-14","14",1.1,2.2,"Driver","33600000011");
		System.out.println("+++++++++");
		System.out.println("gl1.toXML\n"+gl1.toXML());
		System.out.println("+++++++++");
		System.out.print("Unescaping: \n"+org.apache.commons.lang3.StringEscapeUtils2.unescapeXml(gl1.toXML()));
		//AndroidWSNNotification
		tc.name = "TaxiUCGeoLocation";
		System.out.println("WSNAndroid-Location:"+NotificationGenerator.generate(gl1.toXML(),tc));
		//toFile(tc.name+"-LWSN", NotificationGenerator.generate(gl1.toXML(),tc));
		toFile(tc.name+"-HWSN", gl1.toWSN(), sxml);
		toFile(tc.name+"-HWSN", gl1.toRDF("testTaxi"), srdf);
		//		// PresenceEvent
		PresenceEvent pr1 = new PresenceEvent("2011-12-06 18:33:36.681","taxiUC:pre-15","15",true,"Driver","33600000011");
		System.out.println("+++++++++");
		System.out.println("pr1.toXML\n"+pr1.toXML());
		System.out.println("+++++++++");
		System.out.print("Unescaping: \n"+org.apache.commons.lang3.StringEscapeUtils2.unescapeXml(pr1.toXML()));
		//AndroidWSNNotification
		tc.name = "TaxiUCPresence";
		System.out.println("WSNAndroid-Presence:"+NotificationGenerator.generate(pr1.toXML(),tc));
		//toFile(tc.name+"-LWSN", NotificationGenerator.generate(pr1.toXML(),tc));
		toFile(tc.name+"-HWSN", pr1.toWSN(), sxml);
		toFile(tc.name+"-HWSN", pr1.toRDF("testTaxi"), srdf);
		//		// SMSCustomerAlert
		SMSCustomerAlert sm1 = new SMSCustomerAlert("2011-12-06 18:33:36.681","taxiUC:cus-16","16","ok","33600000010",1.1,2.2);
		System.out.println("+++++++++");
		System.out.println("sm1.toXML\n"+sm1.toXML());
		System.out.println("+++++++++");
		System.out.print("Unescaping: \n"+org.apache.commons.lang3.StringEscapeUtils2.unescapeXml(sm1.toXML()));
		//AndroidWSNNotification
		tc.name = "TaxiUCSMSCustomerAlert";
		System.out.println("WSNAndroid-SMSCustomerAlert:"+NotificationGenerator.generate(sm1.toXML(),tc));
		//toFile(tc.name+"-LWSN",NotificationGenerator.generate(sm1.toXML(),tc));
		toFile(tc.name+"-HWSN", sm1.toWSN(), sxml);
		toFile(tc.name+"-HWSN", sm1.toRDF("testTaxi"), srdf);
		//		// TrafficJamEvent
		TrafficJamEvent tr1 = new TrafficJamEvent("2011-12-06 18:33:36.681","taxiUC:tra-17","17","ACCIDENT",1.1,2.2);
		System.out.println("+++++++++");
		System.out.println("tr1.toXML\n"+tr1.toXML());
		System.out.println("+++++++++");
		System.out.print("Unescaping: \n"+org.apache.commons.lang3.StringEscapeUtils2.unescapeXml(tr1.toXML()));
		//AndroidWSNNotification
		tc.name = "TaxiUCTrafficJam";
		System.out.println("WSNAndroid-TrafficJam:"+NotificationGenerator.generate(tr1.toXML(),tc));
		//toFile(tc.name+"-LWSN", NotificationGenerator.generate(tr1.toXML(),tc));
		toFile(tc.name+"-HWSN", tr1.toWSN(), sxml);
		toFile(tc.name+"-HWSN", tr1.toRDF("testTaxi"), srdf);
		//		// UnexpectedEvent
		UnexpectedEvent un1 = new UnexpectedEvent( "2011-12-06 18:33:36.681","taxiUC:une-18","18","bad","um","Driver","33600000011");
		System.out.println("+++++++++");
		System.out.println("un1.toXML\n"+un1.toXML());
		System.out.println("+++++++++");
		System.out.print("Unescaping: \n"+org.apache.commons.lang3.StringEscapeUtils2.unescapeXml(un1.toXML()));
		//AndroidWSNNotification
		tc.name = "TaxiUCUnexpected";
		System.out.println("WSNAndroid-TaxiUCUnexpected:"+NotificationGenerator.generate(un1.toXML(),tc));
		//	toFile(tc.name+"-LWSN", NotificationGenerator.generate(un1.toXML(),tc));
		toFile(tc.name+"-HWSN", un1.toWSN(), sxml);
		toFile(tc.name+"-HWSN", un1.toRDF("testTaxi"), srdf);
		// CallEvent
		CallEvent ca1  = new CallEvent("2011-12-06 18:33:36.681","taxiUC:call-19","19","outgoing","message","33600000011","33600000010",1.1,2.2);
		System.out.println("++++++++++");
		System.out.println("ca1.toXML\n"+ca1.toXML());
		System.out.println("+++++++++");
		System.out.print("Unescaping: \n"+org.apache.commons.lang3.StringEscapeUtils2.unescapeXml(ca1.toXML()));
		//AndroidWSNNotification
		tc.name = "TaxiUCCall";
		System.out.println("WSNAndroid-TaxiUCCall:"+NotificationGenerator.generate(ca1.toXML(),tc));
		//	toFile(tc.name+"-LWSN", NotificationGenerator.generate(ca1.toXML(),tc));
		toFile(tc.name+"-HWSN", ca1.toWSN(), sxml);
		toFile(tc.name+"-HWSN", ca1.toRDF("testTaxi"), srdf);
		// OutNetworkEvent
		OutNetworkEvent ou1  = new OutNetworkEvent("2011-12-06 18:33:36.681","taxiUC:out-20","20",true,"weak","33600000011","33600000010",1.1,2.2);
		System.out.println("++++++++++");
		System.out.println("ou1.toXML\n"+ou1.toXML());
		System.out.println("+++++++++");
		System.out.println("++++++++++");
		System.out.println("ou1.toRDF\n"+ou1.toRDF("testTaxi"));
		System.out.println("+++++++++");
		System.out.print("Unescaping: \n"+org.apache.commons.lang3.StringEscapeUtils2.unescapeXml(ou1.toXML()));
		System.out.print("Unescaping: \n"+org.apache.commons.lang3.StringEscapeUtils2.unescapeXml(ou1.toRDF("testTaxi")));
		//AndroidWSNNotification
		tc.name = "TaxiUCOutNetwork";
		System.out.println("WSNAndroid-TaxiUCOutNetwork:"+NotificationGenerator.generate(ou1.toRDF("testTaxi"),tc));
		//	toFile(tc.name+"-LWSN", NotificationGenerator.generate(ou1.toRDF("testTaxi"),tc));
		toFile(tc.name+"-HWSN", ou1.toWSN(), sxml);
		toFile(tc.name+"-HWSN", ou1.toRDF("testTaxi"), srdf);
		// IncomingCallEvent
		IncomingCallEvent ia1  = new IncomingCallEvent( "2011-12-06 18:33:36.681","taxiUC:inc-21","21","incoming","Ok message","33600000011","33600000010",1.1,2.2);
		System.out.println("+++++++++");
		System.out.println("ia1.toString"+ ia1.toString());
		System.out.println("+++++++++");
		System.out.println("ia1.toXML"+ia1.toXML());
		System.out.println("++++++++++");
		System.out.println("ia1.toXML"+ia1.toXML());
		System.out.println("+++++++++");
		//AndroidWSNNotification
		tc.name = "TaxiUCIncomingCall";
		System.out.println("WSNAndroid-TaxiUCIncomingCall:"+NotificationGenerator.generate(ia1.toXML(),tc));
		toFile(tc.name+"-HWSN", ia1.toWSN(), sxml);
		toFile(tc.name+"-HWSN", ia1.toRDF("testTaxi"), srdf);
		//		// FaceBookEvent
		FaceBookEvent fb1 = new FaceBookEvent( "2011-12-06 18:33:36.681","taxiUC:fac-22","22","fbid1","mess1","online",1.1,2.2);
		System.out.println("+++++++++");
		System.out.println("fb1.toString"+ fb1.toString());
		System.out.println("+++++++++");
		System.out.println("fb1.toXML"+fb1.toXML());
		System.out.println("+++++++++");
		System.out.println("fb1.toXML"+fb1.toXML());
		System.out.println("+++++++++");
		//AndroidWSNNotification
		tc.name = "TaxiUCFaceBook";
		System.out.println("WSNAndroid-TaxiUCFaceBook:"+NotificationGenerator.generate(fb1.toXML(),tc));
		//		toFile(tc.name+"-LWSN", NotificationGenerator.generate(fb1.toXML(),tc));
		toFile(tc.name+"-HWSN", fb1.toWSN(), sxml);
		toFile(tc.name+"-HWSN", fb1.toRDF("testTaxi"), srdf);
		//		// TwitterEvent
		TwitterEvent tw1 = new TwitterEvent( "2011-12-06 18:33:36.681","taxiUC:tw1-23","23","tw1","mess1","online",1.1,2.2);
		System.out.println("+++++++++");
		System.out.println("tw1.toString"+ tw1.toString());
		System.out.println("+++++++++");
		System.out.println("tw1.toXML"+tw1.toXML());
		System.out.println("+++++++++");
		System.out.println("tw1.toXML"+tw1.toXML());
		System.out.println("+++++++++");
		//AndroidWSNNotification
		tc.name = "TaxiUCTwitter";
		System.out.println("WSNAndroid-TaxiUCTwitter:"+NotificationGenerator.generate(tw1.toXML(),tc));
		//		toFile(tc.name+"-LWSN", NotificationGenerator.generate(tw1.toXML(),tc));
		toFile(tc.name+"-HWSN", tw1.toWSN(), sxml);
		toFile(tc.name+"-HWSN", tw1.toRDF("testTaxi"), srdf);
	
	}
	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(EventDataTest.class);
	}
	public void testEventData()
	{
		assertTrue( true );
	}
}