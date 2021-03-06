package de.nec.nle.siafu.output;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Hashtable;
import java.util.Random;

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

public class EventsSenderNew implements SimulatorOutputPrinter {


	private Socket trafficInfosSocket;
	private Socket callEventsSocket;
	private Socket availabilityEventsSocket;
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

	// PG Begin #0
	private long lastEventsSendingTime;

	Hashtable<Integer, String > driverNumbers;
	Hashtable<Integer, String > customerNumbers;

	private World world;

	public EventsSenderNew(final World world) {
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
		// End #1 PG  
	}


	public void notifyIterationConcluded() {

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
							if (Constants.sendingOnNetwork){
								// send on the network
								try {
									if (trafficSocketIsOpen == false
											|| trafficInfosSocket.isClosed()) {
										InetAddress host = InetAddress
										.getLocalHost();
										trafficInfosSocket = new Socket(
												//host for local run of siafu
												// otherwise TRAFIC_SERVICE_HOST
												Constants.siafuOnWSMachine  ? host.getHostName() :
													Constants.TRAFIC_SERVICE_HOST,
													Constants.TRAFIC_INFOS_PORT);
										trafficSocketIsOpen = true;
									}
									out = new PrintWriter(trafficInfosSocket
											.getOutputStream(), true);
									out.print(trafficJamEvent.toString());
									out.close();
								} catch (IOException e) {
									System.out.println("Connection pb to backend socket Traffic: "+
											//"localhost"+":"+
											Constants.TRAFIC_SERVICE_HOST + ":" +
											Constants.TRAFIC_INFOS_PORT);
									e.printStackTrace();
								}
							} 
							//just print  no network sending
							System.out.println(Constants.printOK ? "Traffic   Event : " +
									trafficJamEvent.toString() : "");
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
					if (Constants.sendingOnNetwork){
						try {
							// send on the network
							if (locationSocketIsOpen == false
									|| locationEventsSocket.isClosed()) {
								locationEventsSocket = new Socket(
										Constants.LOCATION_SERVICE_HOST,
										Constants.LOCATION_EVENTS_PORT);
								locationSocketIsOpen = true;
							}
							PrintWriter out = new PrintWriter(locationEventsSocket
									.getOutputStream(), true);
							out.print(geolocationEvent.toString());
							out.close();
						} catch (IOException e) {
							System.out.println("Connection pb to backend socket Location: "+
									Constants.LOCATION_SERVICE_HOST+":"+
									Constants.LOCATION_EVENTS_PORT);
							e.printStackTrace();
						}
					} 
					// not sending send on the network
					System.out.println(Constants.printOK ? "Location Event : " + geolocationEvent.toString() : "");
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
									if (Constants.sendingOnNetwork){
										// send on the network	
										try {
											if (availibilitySocketIsOpen == false
													|| availabilityEventsSocket
													.isClosed()) {
												availabilityEventsSocket = new Socket(
														Constants.IM_SERVICE_HOST,
														Constants.AVAILABILITY_EVENTS_PORT);
												availibilitySocketIsOpen = true;
											}
											PrintWriter out = new PrintWriter(
													availabilityEventsSocket
													.getOutputStream(),
													true);
											out.print(availabilityEvent.toString());
											out.close();
										} catch (IOException e) {
											System.out.println("Connection pb to backend socket Availibility: "+
													Constants.IM_SERVICE_HOST+":"+
													Constants.AVAILABILITY_EVENTS_PORT);
											e.printStackTrace();
										}
									} 
									System.out.println(Constants.printOK ? "AvailibiEvent : " + availabilityEvent.toString() : "");
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
									if (Constants.sendingOnNetwork){
										// send on the network	
										try {
											if (presenceSocketIsOpen == false
													|| presenceEventsSocket
													.isClosed()) {
												presenceEventsSocket = new Socket(
														Constants.IM_SERVICE_HOST,
														Constants.PRESENCE_EVENTS_PORT);
												presenceSocketIsOpen = true;
											}
											PrintWriter out = new PrintWriter(
													presenceEventsSocket.getOutputStream(),true);

											out.print(presenceEvent.toString());
											out.close();
										} catch (IOException e) {
											System.out.println("Connection pb to backend socket Presence: "+
													Constants.IM_SERVICE_HOST+":"+
													Constants.PRESENCE_EVENTS_PORT);
											e.printStackTrace();
										}
									} 
									System.out.println(Constants.printOK ? "Presence    Event : " +
											presenceEvent.toString() : "");
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
									if (Constants.sendingOnNetwork){
										// send on the network	
										try {
											if (callEventSocketIsOpen == false
													|| callEventsSocket
													.isClosed()) {
												InetAddress host = InetAddress.getLocalHost();
												callEventsSocket = new Socket(
														//host.getHostName(),
														Constants.siafuOnWSMachine  ? host.getHostName() :
															Constants.TRAFIC_SERVICE_HOST,
															Constants.UNEXPECTED_EVENTS_PORT);
												callEventSocketIsOpen = true;
											}
											PrintWriter out = new PrintWriter(
													callEventsSocket.getOutputStream(), true);
											out.print(unexpectedEvent.toString());
											out.close();

										} catch (IOException e) {
											System.out.println("Connection pb to backend socket Unexpected: "+
													//"LocalHost"+":"+
													Constants.TRAFIC_SERVICE_HOST + ":"+
													Constants.UNEXPECTED_EVENTS_PORT);
											e.printStackTrace();
										}
									} 
									System.out.println(Constants.printOK ? "Unexpecte Event : " + unexpectedEvent.toString() : "");
								}
							}
						}else if (overlayName.equals("CellularNetwork")) {
							String overlayValue = overlay.getValue(position).toString();
								if (overlayValue.equals("Strong")) {
									eventDate = new Date();
									timestamp = new Timestamp(eventDate.getTime());
										CallEvent callEvent = new CallEvent( timestamp.toString(),prefixUC+prefixCAL+ callEventsSequence,
												String.valueOf(callEventsSequence++),"outgoing",
												"message",customerNumbers.get(K),driverNumbers.get(K),1.1,2.2);
										//New UC  port and host must be checked  PG 
									if (Constants.sendingOnNetwork){
										// send on the network	
										try {
											if (callEventSocketIsOpen == false
													|| callEventsSocket
													.isClosed()) {
												InetAddress host = InetAddress.getLocalHost();
												callEventsSocket = new Socket(
														//host.getHostName(),
														Constants.siafuOnWSMachine  ? host.getHostName() :
															Constants.SMART_PHONE_EVENTS_SERVICE_HOST,
															Constants.CALL_EVENTS_PORT);
												callEventSocketIsOpen = true;
											}
											PrintWriter out = new PrintWriter(
													callEventsSocket.getOutputStream(), true);
											out.print(callEvent.toString());
											out.close();

										} catch (IOException e) {
											System.out.println("Connection pb to backend socket Unexpected: "+
													//"LocalHost"+":"+
													Constants.SMART_PHONE_EVENTS_SERVICE_HOST + ":"+
													Constants.CALL_EVENTS_PORT);
											e.printStackTrace();
										}
									} 
									System.out.println(Constants.printOK ? "CallEvent Event : " + callEvent.toString() : "");
								}
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


		// Get system date
		Date eventDate = new Date();
		Timestamp timestamp = new Timestamp(eventDate.getTime());
		// Create customer alert string in CEP format
		SMSCustomerAlert  smsCustomerAlert =  new SMSCustomerAlert (timestamp.toString(),prefixUC+prefixCUS+customerAlertsSequence,
				String.valueOf(customerAlertsSequence++),"ok",customerNumber, 
				coordinates[0],coordinates[1]);
		if (Constants.sendingOnNetwork){
			try {
				// Create open customer alert socket
				Socket cutomerAlertsSocket = new Socket(
						Constants.SMART_PHONE_EVENTS_SERVICE_HOST,
						Constants.CUSTOMER_ALERTS_PORT);
				// Get a print writer on customer alert socket
				PrintWriter out = new PrintWriter(cutomerAlertsSocket.getOutputStream(), true);
				// Print the customer alert string on the print writer
				out.print(smsCustomerAlert.toString());
				// Close the print writer
				out.close();
				// Close the socket
				cutomerAlertsSocket.close();

			} catch (IOException e) {
				// Print stack trace
				System.out.println("Connection pb to backend socket SMS customerAlert: "+
						Constants.SMART_PHONE_EVENTS_SERVICE_HOST + ":"+
						Constants.CUSTOMER_ALERTS_PORT);
				e.printStackTrace();
			}
		} 
		System.out.println(Constants.printOK ? "SMSCusto Event : "+ smsCustomerAlert.toString() : "");
	}
	
	public void cleanup() {

	}

}
