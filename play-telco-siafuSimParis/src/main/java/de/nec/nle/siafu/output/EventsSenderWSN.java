package de.nec.nle.siafu.output;

import java.io.IOException;
import java.io.PrintWriter;
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

public class EventsSenderWSN implements SimulatorOutputPrinter {


	private Socket trafficInfosSocket;
	private Socket callEventsSocket;
	private Socket locationEventsSocket;
	private Socket presenceEventsSocket;

	private static int unexpectedEventsSequence = 0;
	private static int availabilityEventsSequence = 0;
	private static int presenceEventsSequence = 0;
	private static int geoLocationEventsSequence = 0;
	private static int customerAlertsSequence = 0;
	private static int trafficEventsSequence = 0;
	private static int callEventsSequence = 0;
	private boolean trafficSocketIsOpen = false;
	private boolean locationSocketIsOpen = false;
	private boolean callEventSocketIsOpen = false;
	private boolean availibilitySocketIsOpen = false;
	private boolean presenceSocketIsOpen = false;

	private String lastTrafficStatus = "Normal";
	private String lastFirstTaxiUnexpectedEvent = "No";
	private String lastFirstTaxiAvailability = "True";
	private String lastThirdTaxiPresence = "On";

	// PG Begin #0  - constants for outputting events on the network
	static String prefixUC = "taxiUC:";
	private String prefixLOC = "loc-";
	private String prefixUNE = "une-";
	static String prefixTRA = "tra-";
	private String prefixCUS = "cus-";
	private String prefixPOI = "poi-";
	private String prefixAVA = "ava-";
	private String prefixPRE = "pre-";
	private String prefixCAL = "cal-";
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
	public EventsSenderWSN(final World world) {
		this.world = world;
		// Begin #1 PG 
		// HashTable for  Tel numbers for taxi drivers from 10 to 99
		driverNumbers = new Hashtable< Integer,String>();
		customerNumbers = new Hashtable< Integer,String>();
		for (int i = 10; i < 10 + Constants.TAXINUMBER; i++) {
			driverNumbers.put(i,"336000000"+i);
		}
		for (int j = 10; j < 10 + Constants.CUSTOMERNUMBER; j++) {
			customerNumbers.put(j,"336999999"+j);
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

		HTTPConsumerClient client = new HTTPConsumerClient(endpoint);
		Document document = null;
		long lastEventsSendingAge = world.getTime().getTimeInMillis() - lastEventsSendingTime;
		if (lastEventsSendingAge > Constants.EVENTS_SENDING_FREQUENCY) {
			lastEventsSendingTime = world.getTime().getTimeInMillis();
			for (Agent agent : world.getPeople()) {
				String agentName = agent.getName();
				Position position = agent.getPos();
				double[] agentPosition = position.getCoordinates();
				if (agentName.equals("Traffic Infos Agent")) {
					for (Overlay overlay : world.getOverlays().values()) {
						String overlayName = overlay.getName();
						if (overlayName.equals("TrafficStatus")) {
							Date eventDate = new Date();
							Timestamp timestamp = new Timestamp(eventDate.getTime());
							String overlayValue = overlay.getValue(position).toString();
							//	if (!lastTrafficStatus.equals(overlayValue)) {
							lastTrafficStatus = overlayValue;
							TrafficJamEvent trafficJamEvent = new TrafficJamEvent(timestamp.toString(),
									prefixUC +prefixTRA + trafficEventsSequence,
									String.valueOf(trafficEventsSequence++),
									overlayValue,agentPosition[0],agentPosition[1]);
							PrintWriter out = null;
							if (send2DSB){
								// send on the network - to WSN	
								// TrafficJamEvent
								try {document = XMLHelper.createDocumentFromString(trafficJamEvent.toWSN());
								} catch (Exception e1) {}
								localPart = "TaxiUCTrafficJam";
								topic = new QName(namespaceURI, localPart, prefix);
								//System.out.println("Topic-taxiUC: "+topic);
								try {client.notify(document, topic);
								} catch (NotificationException e) {	
									System.out.println("Connection pb to DSB WSN : "+endpoint+" "+topic);
									e.printStackTrace();
								}
							} 
							//just print  no network sending
							if (traceOn)
								System.out.println( "Traffic   Event : " + trafficJamEvent.toString() );
						}
					}
				} else if (agentName.startsWith("Taxi")|| agentName.startsWith("User")  ) {

					boolean taxiOK = true;
					int K = Integer.parseInt(agentName.substring(4));
					String driverProperty1="", driverProperty2 = "";
					String customerProperty1="", customerProperty2 ="";
					if (agentName.startsWith("Taxi")) {
						taxiOK = true;
						driverProperty1 = ",userType=Driver";
						driverProperty2 = ",phoneNumber="+driverNumbers.get(K);
					} else {
						customerProperty1 = ",userType=Customer";
						customerProperty2 = ",phoneNumber="+customerNumbers.get(K);
						taxiOK = false;
					}
					//  if agent is customer and at destination do not send location event
					//						if (agentName.startsWith("User") && agent.isAtDestination()){
					//							continue;
					//						}
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
						try {document = XMLHelper.createDocumentFromString(geolocationEvent.toWSN());
						} catch (Exception e1) {}
						localPart = "TaxiUCGeoLocation";
						topic = new QName(namespaceURI, localPart, prefix);
						//System.out.println("Topic-taxiUC: "+topic);
						try {client.notify(document, topic);
						} catch (NotificationException e) {	
							System.out.println("Connection pb to DSB WSN : "+endpoint+" "+topic);
							e.printStackTrace();
						}
					} 
					// not sending send on the network
					if (traceOn)
						System.out.println("Location Event : " + geolocationEvent.toString() );

					for (Overlay overlay : world.getOverlays().values()) {
						String overlayName = overlay.getName();
						if (overlayName.equals("Available")) {
							String overlayValue = overlay.getValue(position)
							.toString();
							if (!lastFirstTaxiAvailability.equals(overlayValue)) {
								lastFirstTaxiAvailability = overlayValue;
								if (overlayValue.equals("False")) {
									eventDate = new Date();
									timestamp = new Timestamp( eventDate.getTime());
									Random random = new Random();
									int randomValue = random.nextInt(3);
									AvailabilityEvent availabilityEvent;
									switch (randomValue) {
									case 0:
										availabilityEvent = new AvailabilityEvent(
												timestamp.toString(),prefixUC+prefixAVA+availabilityEventsSequence,
												String.valueOf(availabilityEventsSequence++) ,CalendarStatus.WORKING,"Driver",driverNumbers.get(K));
										break;
									case 1:
										availabilityEvent = new AvailabilityEvent(
												timestamp.toString(),prefixUC+prefixAVA+availabilityEventsSequence,
												String.valueOf(availabilityEventsSequence++) ,CalendarStatus.HOLIDAY,"Driver",driverNumbers.get(K));
										break;
									default:
										availabilityEvent = new AvailabilityEvent(
												timestamp.toString(),prefixUC+prefixAVA+availabilityEventsSequence,
												String.valueOf(availabilityEventsSequence++) ,CalendarStatus.MEETING,"Driver",driverNumbers.get(K));
										break;	
									}
									if (send2DSB){
										// send on the network - to WSN	
										// AvailabilityEvent
										try {document = XMLHelper.createDocumentFromString(availabilityEvent.toWSN());
										} catch (Exception e1) {}
										localPart = "TaxiUCAvailability";
										topic = new QName(namespaceURI, localPart, prefix);
										//System.out.println("Topic-taxiUC: "+topic);
										try {client.notify(document, topic);
										} catch (NotificationException e) {	
											System.out.println("Connection pb to DSB WSN : "+endpoint+" "+topic);
											e.printStackTrace();
										}
									} 
									if (traceOn)
										System.out.println( "AvailibilityEvent : " + availabilityEvent.toString() );
								}
							}
						} else if (overlayName.equals("Presence")) {
							String overlayValue = overlay.getValue(position)
							.toString();
							if (!lastThirdTaxiPresence.equals(overlayValue)) {
								lastThirdTaxiPresence = overlayValue;
								if (overlayValue.equals("Off")) {

									eventDate = new Date();
									timestamp = new Timestamp( eventDate.getTime());
									Random random = new Random();
									int randomValue = random.nextInt(2);
									PresenceEvent presenceEvent;
									switch (randomValue) {
									case 0:
										presenceEvent = new PresenceEvent(timestamp.toString(),
												prefixUC+prefixPRE + presenceEventsSequence,
												String.valueOf(presenceEventsSequence++),
												true,"Driver",driverNumbers.get(K));
										break;
									default:
										presenceEvent = new PresenceEvent(timestamp.toString(),
												prefixUC+prefixPRE + presenceEventsSequence,
												String.valueOf(presenceEventsSequence++),
												false,"Driver",driverNumbers.get(K));
										break;
									}
									if (send2DSB){
										// send on the network - to WSN	
										// PresenceJamEvent
										try {document = XMLHelper.createDocumentFromString(presenceEvent.toWSN());
										} catch (Exception e1) {}
										localPart = "TaxiUCPresence";
										topic = new QName(namespaceURI, localPart, prefix);
										//System.out.println("Topic-taxiUC: "+topic);
										try {client.notify(document, topic);
										} catch (NotificationException e) {	
											System.out.println("Connection pb to DSB WSN : "+endpoint+" "+topic);
											e.printStackTrace();
										}
									}  
									if (traceOn)
									System.out.println("Presence    Event : " + presenceEvent.toString() );
								}
							}
						} else if (overlayName.equals("UnexpectedPb")) {
							String overlayValue = overlay.getValue(position).toString();
							if (!lastFirstTaxiUnexpectedEvent
									.equals(overlayValue)) {
								lastFirstTaxiUnexpectedEvent = overlayValue;
								if (overlayValue.equals("Yes")) {
									eventDate = new Date();
									timestamp = new Timestamp(eventDate.getTime());
									Random random = new Random();
									int randomValue = random.nextInt(3);
									UnexpectedEvent unexpectedEvent ;
									switch (randomValue) {
									case 0:
										unexpectedEvent = new UnexpectedEvent(timestamp.toString(),prefixUC+prefixUNE+ unexpectedEventsSequence,
												String.valueOf(unexpectedEventsSequence++),"TYREFLAT",
												"xmlpayloadTYREFLAT","Taxi",driverNumbers.get(K));
										break;
									case 1:
										unexpectedEvent = new UnexpectedEvent(timestamp.toString(),prefixUC+prefixUNE+ unexpectedEventsSequence,
												String.valueOf(unexpectedEventsSequence++),"LATE",
												"xmlpayloadLATE","Taxi",driverNumbers.get(K));

										break;
									default:
										unexpectedEvent = new UnexpectedEvent(timestamp.toString(),prefixUC+prefixUNE+ unexpectedEventsSequence,
												String.valueOf(unexpectedEventsSequence++),"ACCIDENT",
												"xmlpayloadACCIDENT","Taxi",driverNumbers.get(K));
										break;
									}
									if (send2DSB){
										// send on the network - to WSN	
										// PresenceJamEvent
										try {document = XMLHelper.createDocumentFromString(unexpectedEvent.toWSN());
										} catch (Exception e1) {}
										localPart = "TaxiUCUnexpected";
										topic = new QName(namespaceURI, localPart, prefix);
										//System.out.println("Topic-taxiUC: "+topic);
										try {client.notify(document, topic);
										} catch (NotificationException e) {	
											System.out.println("Connection pb to DSB WSN : "+endpoint+" "+topic);
											e.printStackTrace();
										}
									}  
									if (traceOn)
										System.out.println("Unexpecte Event : " + unexpectedEvent.toString());
								}
							}
						}else if (overlayName.equals("CellularNetwork")) {
							// do nothing in this case because event may come from Phones not form Simulator 
							//							String overlayValue = overlay.getValue(position).toString();
							//							if (overlayValue.equals("Strong")) {
							//								eventDate = new Date();
							//								timestamp = new Timestamp(eventDate.getTime());
							//								CallEvent callEvent = new CallEvent( timestamp.toString(),prefixUC+prefixCAL+ callEventsSequence,
							//										String.valueOf(callEventsSequence++),"outgoing",
							//										"message",customerNumbers.get(K),driverNumbers.get(K),1.1,2.2);
							//								//New UC  port and host must be checked  PG 
							//								if (send2DSB){
							//									// send on the network - to WSN	
							//									// callEvent
							//									try {document = XMLHelper.createDocumentFromString(callEvent.toWSN());
							//									} catch (Exception e1) {}
							//									localPart = "TaxiUCCall";
							//									topic = new QName(namespaceURI, localPart, prefix);
							//									//System.out.println("Topic-taxiUC: "+topic);
							//									try {client.notify(document, topic);
							//									} catch (NotificationException e) {	
							//										System.out.println("Connection pb to DSB WSN : "+endpoint+" "+topic);
							//										e.printStackTrace();
							//									}
							//								} 
						//	if (traceOn)
							//						System.out.println( "CallEvent Event : " + callEvent.toString());
							//							}
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
	public void sendCustomerAlert(String customerNumber, double[] coordinates) {


		String namespaceURI = "http://www.petalslink.org/dsb/topicsns/";
		String prefix = "dsb";
		String  localPart ;
		QName topic ;
		HTTPConsumerClient client = new HTTPConsumerClient(endpoint);
		Document document = null;
		// Get system date
		Date eventDate = new Date();
		Timestamp timestamp = new Timestamp(eventDate.getTime());
		// Create customer alert string in CEP format
		SMSCustomerAlert  smsCustomerAlert =  new SMSCustomerAlert (timestamp.toString(),prefixUC+prefixCUS+customerAlertsSequence,
				String.valueOf(customerAlertsSequence++),"ok",customerNumber, 
				coordinates[0],coordinates[1]);
		if (send2DSB){
			// send on the network - to WSN	

			try {document = XMLHelper.createDocumentFromString(smsCustomerAlert.toWSN());
			} catch (Exception e1) {}
			localPart = "TaxiUCSMSCustomerAlert";
			topic = new QName(namespaceURI, localPart, prefix);
			//System.out.println("Topic-taxiUC: "+topic);
			try {client.notify(document, topic);
			} catch (NotificationException e) {	
				System.out.println("Connection pb to DSB WSN : "+endpoint+" "+topic);
				e.printStackTrace();
			}
		}  
		if (traceOn)
			System.out.println( "SMSCusto Event : "+ smsCustomerAlert.toString());
	}

	public void cleanup() {

	}
}
