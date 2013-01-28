package de.nec.nle.siafu.output;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;
import de.nec.nle.siafu.model.Agent;
import de.nec.nle.siafu.model.Overlay;
import de.nec.nle.siafu.model.Position;
import de.nec.nle.siafu.model.World;
import de.nec.nle.siafu.simulationParis.Constants;

/**
 * This class is used for sending all events types from Siafu simulator to
 * Smart-Taxi Web services layer. Just output to console now  PG 29/07
 */
public class EventsSenderOld implements SimulatorOutputPrinter {


	private static int geoLocationEventsSequence = 0;
	private static int customerAlertsSequence = 0;
	private Integer iterpg;
	// The last events sending time
	private long lastEventsSendingTime;
	// The simulation world
	private World world;
	// Event sender constructor
	public EventsSenderOld(final World world) {
		this.iterpg =0;
		this.world = world;
	}
	public void notifyIterationConcluded() {
		iterpg++;
		System.out.println("######### iteration:"+iterpg);
		// Get the simulation events sending last time
		long lastEventsSendingAge = world.getTime().getTimeInMillis()
		- lastEventsSendingTime;
		// Compare the simulation events sending last time with events sending
		// frequence constant
		if (lastEventsSendingAge > Constants.EVENTS_SENDING_FREQUENCY) {
			System.out.println("######### Yes superieur");
			// Update the simulation events sending last time
			lastEventsSendingTime = world.getTime().getTimeInMillis();
			// Loop over all simulation agents
			for (Agent agent : world.getPeople()) {
				// Get the name of current agent
				String agentName = agent.getName();

				// Get the position of current agent
				Position position = agent.getPos();
				// Get current agent position coordinates
				double[] agentPosition = position.getCoordinates();
				// If current agent is the first customer or the second one move
				// to the next agent
				if (agentName.equals("First Customer") || agentName.equals("Second Customer"))
					continue;
				// If current agent is the traffic infos agent
				else if (agentName.equals("Traffic Infos Agent")) {
					continue;
					// If current agent is  a Taxi
				} else if (agentName.startsWith("Taxi")) {
					System.out.println("agentName:"+agentName);
					// Get system date
					Date eventDate = new Date();
					Timestamp timestamp = new Timestamp(eventDate.getTime());
					// Create location event string according to event
					// string format expected by CEP
					String geoLocationEventString = "stream=GeolocationEvent,timestamp="
						+ timestamp.toString() 
						+ ",uniqueId="
						+ "taxiUC:loc-" 
						+ geoLocationEventsSequence
						+ ",sequenceNumber=" 
						+ geoLocationEventsSequence++
						+ ",latitude=" 
						+ agentPosition[0] 
						                + ",longitude="
						                + agentPosition[1]                             
						                                + ",userType=Driver"
						                                + ",phoneNumber=+336000000"+agentName.substring(4);
					//  to find the Number of the taxi  --> substring
					// Print location string on the console
					System.out.println(geoLocationEventString);


				}
			}
		}
	}


	public void cleanup() {

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
		try {
			// Create open customer alert socket
			Socket cutomerAlertsSocket = new Socket(
					Constants.SMART_PHONE_EVENTS_SERVICE_HOST,
					Constants.CUSTOMER_ALERTS_PORT);

			// Get system date
			Date eventDate = new Date();
			Timestamp timestamp = new Timestamp(eventDate.getTime());

			// Create customer alert string in CEP format
			String customerAlertString = "stream=SMSCustomerAlert,timestamp="
				+ timestamp.toString() + ",uniqueId=" + "taxiUC:cus-"
				+ customerAlertsSequence + ",sequenceNumber="
				+ customerAlertsSequence++ + ",customerNumber="
				+ customerNumber + ",latitude=" + coordinates[0]
				                                              + ",longitude=" + coordinates[1];

			// Get a print writer on customer alert socket
			PrintWriter out = new PrintWriter(
					cutomerAlertsSocket.getOutputStream(), true);
			// Print the customer alert string on the print writer
			out.print(customerAlertString);
			// Close the print writer
			out.close();
			// Close the socket
			cutomerAlertsSocket.close();

			// Print the customer alert string on the consol
			System.out.println(customerAlertString);
		} catch (IOException e) {
			// Print stack trace
			e.printStackTrace();
		}
	}

}
