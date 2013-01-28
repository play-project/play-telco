package de.nec.nle.siafu.output;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Hashtable;

/*************************************************************************/
/**

 * 
 * PLAY project - Siafu Simulation for ICC V1 - 2 users moving 
 *  to use - switch this type with EventsSender in AgentModel.java
 * @author orange labs  - December 2011
 */

//How to  start ..
//Prerequisites
//1)  java jvm should be installed  ( tested with 1.6)
//2)  The Siafu simulator sends directly  to the internet ( Host 161.105.138.99)
//    so ( firewall issues should be resolved)
//Then .............
//2)  on Windows just run runWin32.bat  
//3)  the Siafu GUI is displayed 
//4)  load from GUI Menu ( Simulation --> Open packed simulation) 
//     the simulations: 
//		- SiafuSimulationParisICCS<N>.jar ( print and send to network)
//		- SiafuSimulationParisICCSPrint.jar(just print no sending to network)
//5)   simulation should start sending and logging  events  for the 2  USers ( user10 and User11)
//6) if you click a User (10 or 11)  and then move the pointer on a nearby position on a road
//   the selected user moves ...
//7) 2 taxis and traffic agent are moving but are not sending events 
//8) output on  console
//Please for this first try do not speed up the simulator ( do not use the potentiometer on the gui)
//if the OrangeLabs backend  is ok  + network reachable 
//you  will see 
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//Sent to Orange Labs WS backend - Location Event : 
//stream=GeolocationEvent,timestamp=2011-12-01.........."
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//if the Orange labs backend is stopped or not reachable for any network problem
//you will see this message 
//------------------------------------------------------------------------
//Connection problem to backend socket Location: 161.105.138.99:8088
//Orange WS backend not reachable! Event Location  NOT SENT : 
//stream=GeolocationEvent,timestamp=2011-12-01 ................................
//------------------------------------------------------------------------
//
//9) Events reaching the PLAY DSB are observable with the Console supplied by EBM- Christophe
//here http://46.105.181.221:9000/last 
//( Location event ID starting with <uniqueId>taxiUC-ESR:........)
//Orange Labs  December 2011

import com.orange.play.eventData.events.inEvents.GeolocationEvent;


import de.nec.nle.siafu.model.Agent;
import de.nec.nle.siafu.model.Position;
import de.nec.nle.siafu.model.World;
import de.nec.nle.siafu.simulationParis.Constants;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Hashtable;
import java.util.Random;
import javax.xml.namespace.QName;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.petalslink.dsb.notification.client.http.simple.HTTPConsumerClient;
import org.petalslink.dsb.notification.commons.NotificationException;
import org.w3c.dom.Document;

import com.orange.play.eventData.events.inEvents.AvailabilityEvent;
import com.orange.play.eventData.events.inEvents.CallEvent;
import com.orange.play.eventData.events.inEvents.GeolocationEvent;
import com.orange.play.eventData.events.inEvents.OutNetworkEvent;
import com.orange.play.eventData.events.inEvents.PresenceEvent;
import com.orange.play.eventData.events.inEvents.SMSCustomerAlert;
import com.orange.play.eventData.events.inEvents.TrafficJamEvent;
import com.orange.play.eventData.events.inEvents.AvailabilityEvent.CalendarStatus;
import com.orange.play.eventData.events.inEvents.UnexpectedEvent;

import de.nec.nle.siafu.model.Agent;
import de.nec.nle.siafu.model.Overlay;
import de.nec.nle.siafu.model.Position;
import de.nec.nle.siafu.model.World;
import de.nec.nle.siafu.simulationParis.Constants;
import com.ebmwebsourcing.easycommons.xml.XMLHelper;
import com.ebmwebsourcing.wsstar.basefaults.datatypes.impl.impl.WsrfbfModelFactoryImpl;
import com.ebmwebsourcing.wsstar.basenotification.datatypes.impl.impl.WsnbModelFactoryImpl;
import com.ebmwebsourcing.wsstar.resource.datatypes.impl.impl.WsrfrModelFactoryImpl;
import com.ebmwebsourcing.wsstar.resourcelifetime.datatypes.impl.impl.WsrfrlModelFactoryImpl;
import com.ebmwebsourcing.wsstar.resourceproperties.datatypes.impl.impl.WsrfrpModelFactoryImpl;
import com.ebmwebsourcing.wsstar.topics.datatypes.impl.impl.WstopModelFactoryImpl;
import com.ebmwebsourcing.wsstar.wsnb.services.impl.util.Wsnb4ServUtils;
public class EventsSenderNewICCSRDF implements SimulatorOutputPrinter {
	private Socket locationEventsSocket;
	private boolean locationSocketIsOpen = false;
	private static int geoLocationEventsSequence = 0;
	// PG Begin #0  - constants for outputting events on the network
	static String prefixUC = "taxiUC:";
	private String prefixLOC = "loc-";
	private static int outNetworkEventsSequence = 0;
	private String prefixOUT = "out-";
	private String endpoint ;
	private Boolean traceOn ;
	private Boolean send2DSB ;
	private Boolean WSHere ;
	// PG Begin #0
	private long lastEventsSendingTime;

	private XMLConfiguration pgConfig;
	Hashtable<Integer, String > driverNumbers;
	Hashtable<Integer, String > customerNumbers;
	private World world;
	static {
		Wsnb4ServUtils.initModelFactories(new WsrfbfModelFactoryImpl(),
				new WsrfrModelFactoryImpl(), new WsrfrlModelFactoryImpl(),
				new WsrfrpModelFactoryImpl(), new WstopModelFactoryImpl(),
				new WsnbModelFactoryImpl());
	}
	public EventsSenderNewICCSRDF(final World world) {
		this.world = world;
		// Begin #1 PG 
		// HashTable for  Tel numbers for taxi drivers from 10 to 99
		driverNumbers = new Hashtable< Integer,String>();
		customerNumbers = new Hashtable< Integer,String>();
		for (int i = 10; i < 10 + Constants.TAXINUMBER; i++) {
			driverNumbers.put(i,"306000000"+i);
		}
		for (int j = 10; j < 10 + Constants.CUSTOMERNUMBER; j++) {
			customerNumbers.put(j,"306999999"+j);
		}
		try {
			pgConfig = new XMLConfiguration("etc/pgConfig.properties");
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		endpoint = pgConfig.getString("endpoint");
		traceOn = pgConfig.getBoolean("traceOn");
		send2DSB = pgConfig.getBoolean("send2DSB");
		WSHere = pgConfig.getBoolean("WSHere");
		// End #1 PG  
	}
	public void notifyIterationConcluded() {

		String namespaceURI = "http://www.petalslink.org/dsb/topicsns/";
		String prefix = "dsb";
		String  localPart ;
		QName topic ;
		int K = 0;
		HTTPConsumerClient client = new HTTPConsumerClient(endpoint);
		Document document = null;
		long lastEventsSendingAge = world.getTime().getTimeInMillis() - lastEventsSendingTime;
		if (lastEventsSendingAge > Constants.EVENTS_SENDING_FREQUENCY) {
			lastEventsSendingTime = world.getTime().getTimeInMillis();
			for (Agent agent : world.getPeople()) {
				String agentName = agent.getName();
				Position position = agent.getPos();
				double[] agentPosition = position.getCoordinates();
				if (agentName.startsWith("User")  ) {

					boolean taxiOK = true;
					K = Integer.parseInt(agentName.substring(4));
					String driverProperty1="", driverProperty2 = "";
					String customerProperty1="", customerProperty2 ="";
					if (agentName.startsWith("Taxi")) {
						taxiOK = true;
						driverProperty1 = "Driver";
						driverProperty2 = driverNumbers.get(K);
					} else {
						customerProperty1 = "Customer";
						customerProperty2 = customerNumbers.get(K);
						taxiOK = false;
					}	
					
					//  End  #2 PG 
					Date eventDate = new Date();
					Timestamp timestamp = new Timestamp(eventDate.getTime());

					GeolocationEvent geolocationEvent = new GeolocationEvent(timestamp.toString(),prefixUC+prefixLOC+geoLocationEventsSequence,
							String.valueOf(geoLocationEventsSequence++),agentPosition[0],agentPosition[1],
							(taxiOK ? driverProperty1 : customerProperty1),
							(taxiOK ? driverProperty2 : customerProperty2));


					if (send2DSB){
						// send on the network - to WSN	
						// geoLocationEvent
						try {document = XMLHelper.createDocumentFromString(geolocationEvent.toRDF("Siafu"));
						} catch (Exception e1) {}
						localPart = "TaxiUCGeoLocation";
						topic = new QName(namespaceURI, localPart, prefix);

						try {client.notify(document, topic);

						} catch (NotificationException e) {	
							System.out.println("------------------------------------------------------------------------");
							System.out.println("Connection pb to DSB WSN : "+endpoint+" "+topic);
							System.out.println("------------------------------------------------------------------------");
							e.printStackTrace();
						}	
					} else System.out.println(" Not sending to DSB");
					if (traceOn) {
						System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
						System.out.println("Sent1 to WSN Endpoint   - Location Event : " + geolocationEvent.toString() );
						System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
					}
					
				}
				for (Overlay overlay : world.getOverlays().values()) {
					OutNetworkEvent outNetworkEvent = null;
					String overlayName = overlay.getName();
					if (overlayName.equals("CellularNetwork")) {
						String overlayValue = overlay.getValue(position).toString();
						if (overlayValue.equals("Strong")) {
							Date eventDate = new Date();
							Timestamp timestamp = new Timestamp(eventDate.getTime());
							outNetworkEvent = new OutNetworkEvent( timestamp.toString(),prefixUC+prefixOUT+ outNetworkEventsSequence,
									String.valueOf(outNetworkEventsSequence++),false,
									"Strong",customerNumbers.get(K),driverNumbers.get(K),1.1,2.2);

							if (send2DSB){
								try {document = XMLHelper.createDocumentFromString(outNetworkEvent.toRDF("Siafu"));
								} catch (Exception e1) {}
								localPart = "TaxiUCCall";
								topic = new QName(namespaceURI, localPart, prefix);
								//System.out.println("Topic-taxiUC: "+topic);
								try {client.notify(document, topic);
								} catch (NotificationException e) {	
									System.out.println("Connection pb to DSB WSN : "+endpoint+" "+topic);
									e.printStackTrace();
								}
							} 
							if (traceOn) System.out.println( "outNetworkEvent Event : " + outNetworkEvent.toRDF("Siafu"));
						} else 
							if (overlayValue.equals("Weak")) {
								Date eventDate = new Date();
								Timestamp timestamp = new Timestamp(eventDate.getTime());
								outNetworkEvent = new OutNetworkEvent( timestamp.toString(),prefixUC+prefixOUT+ outNetworkEventsSequence,
										String.valueOf(outNetworkEventsSequence++),true,
										"OutOfRange",customerNumbers.get(K),driverNumbers.get(K),1.1,2.2);


								if (send2DSB){
									try {document = XMLHelper.createDocumentFromString(outNetworkEvent.toRDF("Siafu"));
									} catch (Exception e1) {}
									localPart = "TaxiUCCall";
									topic = new QName(namespaceURI, localPart, prefix);
									//System.out.println("Topic-taxiUC: "+topic);
									try {client.notify(document, topic);
									} catch (NotificationException e) {	
										System.out.println("Connection pb to DSB WSN : "+endpoint+" "+topic);
										e.printStackTrace();
									}
								} 
								if (traceOn) System.out.println( "outNetworkEvent Event : " + outNetworkEvent.toRDF("Siafu"));
							}
					}
				}
			} 
		}
	}
	/**
	 * This method is used for sending customer alerts from Siafu to smart phone
	 * events Web service
	 * 
	 * @param customerNumber
	 *            the customer phone number
	 * @param coordinates
	 *            the customer location
	 */
	public void sendCustomerAlert(String customerNumber, double[] coordinates) {}
	public void cleanup() {}

}
