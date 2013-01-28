package de.nec.nle.siafu.output;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Hashtable;
import java.util.Random;

import de.nec.nle.siafu.model.Agent;
import de.nec.nle.siafu.model.Overlay;
import de.nec.nle.siafu.model.Position;
import de.nec.nle.siafu.model.World;
import de.nec.nle.siafu.simulationParis.Constants;

public class EventsSender implements SimulatorOutputPrinter {


	private Socket trafficInfosSocket;
	private Socket unexpectedEventsSocket;
	private Socket availabilityEventsSocket;
	private Socket locationEventsSocket;
	private Socket presenceEventsSocket;
	private static int unexpectedEventsSequence = 0;
	private static int availabilityEventsSequence = 0;
	private static int presenceEventsSequence = 0;
	private static int geoLocationEventsSequence = 0;
	private static int customerAlertsSequence = 0;
	private boolean trafficSocketIsOpen = false;
	private boolean locationSocketIsOpen = false;
	private boolean unexpectedSocketIsOpen = false;
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
	
	
	// PG Begin #0
	private long lastEventsSendingTime;
	
	Hashtable<Integer, String > driverNumbers;
	Hashtable<Integer, String > customerNumbers;
	
	private World world;

	public EventsSender(final World world) {
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
//		System.out.println("#lastEventsSendingAge:"+lastEventsSendingAge);
//		System.out.println("#EVENTS_SENDING_FREQUENCY:"+Constants.EVENTS_SENDING_FREQUENCY);
		if (lastEventsSendingAge > Constants.EVENTS_SENDING_FREQUENCY) {
			
			lastEventsSendingTime = world.getTime().getTimeInMillis();
			for (Agent agent : world.getPeople()) {
				String agentName = agent.getName();
				Position position = agent.getPos();
				double[] agentPosition = position.getCoordinates();
					if (agentName.equals("Traffic Infos Agent")) {
						for (Overlay overlay : world.getOverlays().values()) {
							String overlayName = overlay.getName();
//							if (overlayName.equals("TrafficStatus")) {
//								String overlayValue = overlay.getValue(position).toString();
//							//	if (!lastTrafficStatus.equals(overlayValue)) {
//									lastTrafficStatus = overlayValue;
//									TrafficInfosXMLCreator trafficInfosXMLCreator = new TrafficInfosXMLCreator(		
//											"trafficInfo", overlayValue, ""
//											+ agentPosition[0], ""
//											+ agentPosition[1]);
//									PrintWriter out = null;
//									if (Constants.sendingOnNetwork){
//										// send on the network
//										try {
//											if (trafficSocketIsOpen == false
//													|| trafficInfosSocket.isClosed()) {
//												InetAddress host = InetAddress
//												.getLocalHost();
//												trafficInfosSocket = new Socket(
//														//host for local run of siafu
//														// otherwise TRAFIC_SERVICE_HOST
//														Constants.siafuOnWSMachine  ? host.getHostName() :
//															Constants.TRAFIC_SERVICE_HOST,
//															Constants.TRAFIC_INFOS_PORT);
//												trafficSocketIsOpen = true;
//											}
//											out = new PrintWriter(trafficInfosSocket
//													.getOutputStream(), true);
//											out.print(trafficInfosXMLCreator.createTrafficInfosXML());
//											out.close();
//										} catch (IOException e) {
//											System.out.println("Connection pb to backend socket Traffic: "+
//													//"localhost"+":"+
//													Constants.TRAFIC_SERVICE_HOST + ":" +
//													Constants.TRAFIC_INFOS_PORT);
//											e.printStackTrace();
//										}
//									} 
//									System.out.println( "TrafficJam Event : " +  "--> status="+overlayValue );
//									//just print  no network sending
//									System.out.println(Constants.printOK ? "TrafficJam Event : " +
//									 "--> status="+overlayValue : "");
////									System.out.println(Constants.printOK ? "TrafficJam Event : " +
////											"TrafficJam Event : stream=TrafficJam,timestamp=.........." : "");
//							//	}
//							}
						}
					} else if (agentName.startsWith("Taxi")|| agentName.startsWith("User")  ) {
					
						// Begin #2 PG 
						//  add  location move  from Customers with the GUI 
						//  true if agentName = customer and he is at destination 
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
						if (agentName.startsWith("User") && agent.isAtDestination()){
							continue;
						}
							//  End  #2 PG 
							Date eventDate = new Date();
						Timestamp timestamp = new Timestamp(eventDate.getTime());
						// Begin #3 PG
						// tous les print de string d'evnetIn vers les backend des WS devraient
						// construire un eventIn ( commondatataxi) puis faire .toString
						//avant les envois... Argh mal codé par Youssoufa - A faire
						// End #3 PG
						String geoLocationEventString = "stream=GeolocationEvent,timestamp="
							+ timestamp.toString().replace(' ', 'T')
							+ ",uniqueId="+ prefixUC+prefixLOC
							+ geoLocationEventsSequence
							+ ",sequenceNumber="
							+ geoLocationEventsSequence++
							+ ",latitude="
							+ agentPosition[0]
							                + ",longitude="
							                + agentPosition[1]
							                                + (taxiOK ? driverProperty1 : customerProperty1)
							                                + (taxiOK ? driverProperty2 : customerProperty2) ;
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
								out.print(geoLocationEventString);

								out.close();
							} catch (IOException e) {
								System.out.println("Connection pb to backend socket Location: "+
										Constants.LOCATION_SERVICE_HOST+":"+
										Constants.LOCATION_EVENTS_PORT);
								e.printStackTrace();
							}
						} 
						// not sending send on the network
						System.out.println(Constants.printOK ? "Location Event : " + geoLocationEventString : "");

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
										String availabilityEventString = null;
										switch (randomValue) {
										case 0:
											availabilityEventString = "Availability [status=WORKING";
											break;
										case 1:
											availabilityEventString = "Availability [status=HOLIDAY";
											break;
										default:
											availabilityEventString = "Availability [status=MEETING";
											break;	
										}
										availabilityEventString = availabilityEventString 
										+ ", userType=Driver"
										+ ", phoneNumber="+driverNumbers.get(K)
										+ ", timeStamp="+ timestamp.toString().replace(' ', 'T')
										+ ", uniqueId="+ prefixUC+prefixAVA+ availabilityEventsSequence
										+ ", sequenceNumber="+ availabilityEventsSequence++ + "]";
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
												out.print(availabilityEventString);
												out.close();
											} catch (IOException e) {
												System.out.println("Connection pb to backend socket Availibility: "+
														Constants.IM_SERVICE_HOST+":"+
														Constants.AVAILABILITY_EVENTS_PORT);
												e.printStackTrace();
											}
										} 
										System.out.println(Constants.printOK ? "Availibility  Event : " + availabilityEventString : "");
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
										String presenceEventString = null;
										presenceEventString = "stream=PresenceEvent,"
											+ "phoneNumber="+driverNumbers.get(K);
										switch (randomValue) {
										case 0:
											presenceEventString =presenceEventString + ",status=true";
											break;
										default:
											presenceEventString = presenceEventString + ",status=false";
											break;
										}
										presenceEventString= presenceEventString
										+ ",userType=Driver"
					
										+ ", timeStamp="+ timestamp.toString().replace(' ', 'T')
										+ ",uniqueId="+ prefixUC+prefixPRE + presenceEventsSequence
										+ ",sequenceNumber=" + presenceEventsSequence++;

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

												out.print(presenceEventString);
												out.close();
											} catch (IOException e) {
												System.out.println("Connection pb to backend socket Presence: "+
														Constants.IM_SERVICE_HOST+":"+
														Constants.PRESENCE_EVENTS_PORT);
												e.printStackTrace();
											}
										} 
										System.out.println(Constants.printOK ? "PresenceEvent  Event : " + presenceEventString : "");
									}
								}
							} else if (overlayName.equals("UnexpectedPb")) {
								String overlayValue = overlay.getValue(position).toString();
								if (!lastFirstTaxiUnexpectedEvent
										.equals(overlayValue)) {
									lastFirstTaxiUnexpectedEvent = overlayValue;
									if (overlayValue.equals("Yes")) {

										eventDate = new Date();
										timestamp = new Timestamp(
												eventDate.getTime());
										Random random = new Random();
										int randomValue = random.nextInt(3);
										String unexpectedEventString = null;
										switch (randomValue) {
										case 0:
											unexpectedEventString = "UnexpectedEvent [status=TYREFLAT"
												+ ", UnexpectedMessage=xmlpayloadTYREFLAT";
											break;
										case 1:
											unexpectedEventString = "UnexpectedEvent [status=LATE"
												+ ", UnexpectedMessage=xmlpayloadLATE";
											break;
										default:
											unexpectedEventString = "UnexpectedEvent [status=ACCIDENT"
												+ ", UnexpectedMessage=xmlpayloadACCIDENT";

											break;
										}
										unexpectedEventString =unexpectedEventString + ", userType=Taxi"
										+ ", phoneNumber="+driverNumbers.get(K)
										
										+ ", timeStamp="+ timestamp.toString().replace(' ', 'T')
										+ ", uniqueId="+ prefixUC+prefixUNE+ unexpectedEventsSequence
										+ ", sequenceNumber=" + unexpectedEventsSequence++ + "]";

										if (Constants.sendingOnNetwork){
											// send on the network	
											try {
												if (unexpectedSocketIsOpen == false
														|| unexpectedEventsSocket
														.isClosed()) {
													InetAddress host = InetAddress
													.getLocalHost();
													unexpectedEventsSocket = new Socket(
															//host.getHostName(),
															Constants.siafuOnWSMachine  ? host.getHostName() :
																Constants.TRAFIC_SERVICE_HOST,
																Constants.UNEXPECTED_EVENTS_PORT);
													unexpectedSocketIsOpen = true;
												}
												PrintWriter out = new PrintWriter(
														unexpectedEventsSocket.getOutputStream(), true);
												out.print(unexpectedEventString);
												out.close();

											} catch (IOException e) {
												System.out.println("Connection pb to backend socket Unexpected: "+
														//"LocalHost"+":"+
														Constants.TRAFIC_SERVICE_HOST + ":"+
														Constants.UNEXPECTED_EVENTS_PORT);
												e.printStackTrace();

											}
										} 
										System.out.println(Constants.printOK ? "Unexpected  Event : " + unexpectedEventString : "");
									}
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
		String customerAlertString = "stream=SMSCustomerAlert,timestamp="
			+ timestamp.toString().replace(' ', 'T') + 
			",uniqueId=" +  prefixUC+prefixCUS
			+ customerAlertsSequence + ",sequenceNumber="
			+ customerAlertsSequence++ + ",customerNumber="
			+ customerNumber + ",latitude=" + coordinates[0]
			                                              + ",longitude=" + coordinates[1];
		// Print the customer alert string on the console

		if (Constants.sendingOnNetwork){
			try {
				// Create open customer alert socket
				Socket cutomerAlertsSocket = new Socket(
						Constants.SMART_PHONE_EVENTS_SERVICE_HOST,
						Constants.CUSTOMER_ALERTS_PORT);
				// Get a print writer on customer alert socket
				PrintWriter out = new PrintWriter(cutomerAlertsSocket.getOutputStream(), true);
				// Print the customer alert string on the print writer
				out.print(customerAlertString);
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
		System.out.println(Constants.printOK ? "CustomerResquest   Event : "+ customerAlertString : "");
	}
	
	public void cleanup() {

	}

}
