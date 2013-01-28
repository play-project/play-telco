package testTaxiTypes;

import com.orange.play.eventData.events.inEvents.AvailabilityEvent;
import com.orange.play.eventData.events.inEvents.CallEvent;
import com.orange.play.eventData.events.inEvents.GeolocationEvent;
import com.orange.play.eventData.events.inEvents.PresenceEvent;
import com.orange.play.eventData.events.inEvents.SMSCustomerAlert;
import com.orange.play.eventData.events.inEvents.TrafficJamEvent;
import com.orange.play.eventData.events.inEvents.UnexpectedEvent;
import com.orange.play.eventData.events.inEvents.AvailabilityEvent.CalendarStatus;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


//import org.petalslink.dsb.notification.light.*;
public class ClientTCPTest  extends TestCase {

	public ClientTCPTest(String testName) {
		super(testName);
//		try {
//			System.out.println("starting1");
//			InetAddress host;
//			host = InetAddress.getLocalHost();
//			Socket skt;
//			try {
//					AvailabilityEvent av1 = new AvailabilityEvent("2011-12-06 18:33:36.681","taxiUC:ava-13","13",CalendarStatus.WORKING,"Driver","33600000011");
//				GeolocationEvent gl1 = new GeolocationEvent("2011-12-06 18:33:36.681","taxiUC:loc-14","11",1.1,2.2,"Driver","33600000011");
//				
//				GeolocationEvent gl2 = new GeolocationEvent("2011-12-06 18:33:36.681","taxiUC:loc-15","12",3.3,4.4,"Driver","33600000012");
//				GeolocationEvent gl3 = new GeolocationEvent("2011-12-06 18:33:36.681","taxiUC:loc-15","13",3.3,4.4,"Driver","33600000013");
//				GeolocationEvent gl4 = new GeolocationEvent("2011-12-06 18:33:36.681","taxiUC:loc-15","14",3.3,4.4,"Driver","33600000014");
//				
//				PresenceEvent pr1 = new PresenceEvent("2011-12-06 18:33:36.681","taxiUC:pre-15","15",true,"Driver","33600000011");
//				SMSCustomerAlert sm1 = new SMSCustomerAlert("2011-12-06 18:33:36.681","taxiUC:cus-16","16","ok","33600000010",1.1,2.2);
//				
//				TrafficJamEvent tr1 = new TrafficJamEvent("2011-12-06 18:33:36.681","taxiUC:tra-17","17","ACCIDENT",1.1,2.2);
//				TrafficJamEvent tr2 = new TrafficJamEvent("2011-12-06 18:33:36.681","taxiUC:tra-18","18","TYREFLAT",1.1,2.2);
//				TrafficJamEvent tr3 = new TrafficJamEvent("2011-12-06 18:33:36.681","taxiUC:tra-19","19","TYREFLAT",1.1,2.2);
//				TrafficJamEvent tr4 = new TrafficJamEvent("2011-12-06 18:33:36.681","taxiUC:tra-20","20","TYREFLAT",1.1,2.2);
//				
//				UnexpectedEvent un1 = new UnexpectedEvent( "2011-12-06D18:33:36.681","taxiUC:une-18","18","bad","um","Driver","33600000011");
//				UnexpectedEvent un2 = new UnexpectedEvent( "2011-12-06D18:33:36.681","taxiUC:une-19","19","bad","um","Driver","33600000011");
//				UnexpectedEvent un3 = new UnexpectedEvent( "2011-12-06D18:33:36.681","taxiUC:une-20","20","bad","um","Driver","33600000011");
//				UnexpectedEvent un4 = new UnexpectedEvent( "2011-12-06D18:33:36.681","taxiUC:une-21","21","bad","um","Driver","33600000011");
//				
//				CallEvent ca1  = new CallEvent( "2011-12-06 18:33:36.681","taxiUC:call-19","19","outgoing","message","33600000011","33600000010",1.1,2.2);
//				System.out.print("ca1.get: "+ca1.getCalleePhoneNumber());
//				System.out.print("ca1.get: "+ca1.getCallerPhoneNumber());
//				System.out.print("ca1.toStringCEP1: "+ca1.toStringCEP1());
//				skt = new Socket(host.getHostName(), 1112);
//				PrintWriter out = new PrintWriter(skt.getOutputStream(), true);
//				
//				out.print(gl1.toStringCEP1()+"\n");
//				out.print(pr1.toStringCEP1()+"\n");
//				out.print(sm1.toStringCEP1()+"\n");
//				out.print(tr1.toStringCEP1()+"\n");
//				out.print(un1.toStringCEP1()+"\n");
//				out.print(av1.toStringCEP1()+"\n");
			
		//		out.print(ca1.toStringCEP1()+"\n");
				
//				out.print(un2.toStringCEP1()+"\n");
//				out.print(un3.toStringCEP1()+"\n");
//				out.print(un4.toStringCEP1()+"\n");
//				
			//	out.print(gl1.toStringCEP1()+"\n");
//				out.print(gl2.toStringCEP1()+"\n");
//				out.print(gl3.toStringCEP1()+"\n");
//				out.print(gl4.toStringCEP1()+"\n");
//			
		//		out.print(tr1.toStringCEP1()+"\n");
//				out.print(tr2.toStringCEP1()+"\n");
//				out.print(tr3.toStringCEP1()+"\n");
//				out.print(tr4.toStringCEP1()+"\n");
				
			
//				
//				out.close();
//				skt.close();
//			} catch (UnknownHostException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} catch (UnknownHostException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}	
	}

	private void fillStrs ()
	{
		//AvailabilityEvent
		AvailabilityEvent av1 = new AvailabilityEvent(
				"2011-12-06 18:33:36.681","taxiUC:ava-13","13",CalendarStatus.WORKING,"Driver","33600000011");
		System.out.println("+++++++++");
		System.out.print("av1.toStringCEP1: "+av1.toStringCEP1());

		// GeoLocationEvent
		GeolocationEvent gl1 = new GeolocationEvent("2011-12-06 18:33:36.681","taxiUC:loc-14","14",1.1,2.2,"Driver","33600000011");
		System.out.println("+++++++++");
		System.out.println("gl1.toRDF\n"+gl1.toRDF("testTaxi"));

		//		// PresenceEvent
		PresenceEvent pr1 = new PresenceEvent("2011-12-06 18:33:36.681","taxiUC:pre-15","15",true,"Driver","33600000011");
		System.out.println("+++++++++");
		System.out.println("pr1.toRDF\n"+pr1.toRDF("testTaxi"));

		//		// SMSCustomerAlert

		SMSCustomerAlert sm1 = new SMSCustomerAlert("2011-12-06 18:33:36.681","taxiUC:cus-16","16","ok","33600000010",1.1,2.2);
		System.out.println("+++++++++");
		System.out.println("sm1.toRDF\n"+sm1.toRDF("testTaxi"));

		//		// TrafficJamEvent
		TrafficJamEvent tr1 = new TrafficJamEvent("2011-12-06 18:33:36.681","taxiUC:tra-17","17","ACCIDENT",1.1,2.2);
		System.out.println("+++++++++");
		System.out.println("tr1.toRDF\n"+tr1.toRDF("testTaxi"));

		//		// UnexpectedEvent
		UnexpectedEvent un1 = new UnexpectedEvent( "2011-12-06 18:33:36.681","taxiUC:une-18","18","bad","um","Driver","33600000011");
		System.out.println("+++++++++");
		System.out.println("un1.toRDF\n"+un1.toRDF("testTaxi"));

		// CallEvent
		CallEvent ca1  = new CallEvent("2011-12-06 18:33:36.681","taxiUC:call-19","19","outgoing","message","33600000011","33600000010",1.1,2.2);
		System.out.println("++++++++++");
		System.out.println("ca1.toRDF\n"+ca1.toRDF("testTaxi"));

	}


	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(ClientTCPTest.class);
	}

	public void testEventSending()
	{
		assertTrue( true );
	}
}