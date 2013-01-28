package testTaxiTypes;


import org.petalslink.dsb.notification.light.NotificationGenerator;
import org.petalslink.dsb.notification.light.Topic;

import com.orange.play.eventData.events.inEvents.CallEvent;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

//import org.petalslink.dsb.notification.light.*;
public class CallEventWSNTest  extends TestCase {

	public CallEventWSNTest(String testName) {
		super(testName);
		// for testing AndroidNotification  WSN
		Topic tc = new Topic();
		String namespaceURI = "http://www.petalslink.org/dsb/topicsns/";
		tc.prefix = "dsb";
		tc.ns = namespaceURI;
		tc.name = "TaxiUCCall";
		// CallEvent
		CallEvent ca1  = new CallEvent( "ts","uId","sn","outgoing","message","canum","cenum",1.1,2.2);
		System.out.println("+++++++++");
		System.out.println("ca1.toWSN"+ca1.toWSN());
		System.out.println("+++++++++");
		//AndroidWSNNotification
		System.out.println("WSNAndroid-TaxiUCCall:"+NotificationGenerator.generate(ca1.toWSN(),tc));
		//this String should be sent to  CEP processing Input 
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(CallEventWSNTest.class);
	}
	/**
	 * Rigourous Test :-)
	 */
	public void testCallEventWSN()
	{
		assertTrue( true );
	}
}