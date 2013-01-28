package de.nec.nle.siafu.simulationParis;

import de.nec.nle.siafu.types.FlatData;
import de.nec.nle.siafu.types.Publishable;
import de.nec.nle.siafu.types.Text;


/**
 * All constants used in the simulation are defined here
 */
public class Constants {
	
	/**
	 * Population size, that is, how many agents should inhabit this simulation.
	 */
	public static final int POPULATION = 25;
	public static final int TAXINUMBER = 22;
	public static final int CUSTOMERNUMBER = 2;
	public static final int TRAFFICAGENTNUMBER = 1;
	// Events sending frequency
	public static final int EVENTS_SENDING_FREQUENCY = 1000000;

	// Traffic service host and traffic infos and unexpected events ports
	public static final String TRAFIC_SERVICE_HOST = "161.105.138.98";
	public static final int TRAFIC_INFOS_PORT = 8088;
	public static final int UNEXPECTED_EVENTS_PORT = 8089;

	// Smart phone events host and customers alerts port
	public static final String SMART_PHONE_EVENTS_SERVICE_HOST = "161.105.138.98";
	public static final int CUSTOMER_ALERTS_PORT = 8086;
	// PG added for New UC
	public static final int CALL_EVENTS_PORT = 8085;
	
	// IM service host and availability and presence events ports
	public static final String IM_SERVICE_HOST = "161.105.138.99";
	public static final int AVAILABILITY_EVENTS_PORT = 8087;
	public static final int PRESENCE_EVENTS_PORT = 8089;

	// Location service host and location events port
	public static final String LOCATION_SERVICE_HOST = "161.105.138.99";
	public static final int LOCATION_EVENTS_PORT = 8088;

	// send on the network on  not
	public static boolean  sendingOnNetwork = false;
	// print on the console traces
	public static final boolean printOK = true;
	// siafu simulation running on the same  host than the WS
	public static final boolean siafuOnWSMachine = true;
	/**
	 * Agents speed in this simulation.
	 */
	public static final int SPEED = 3;
	

	/**
	 * The names of the fields in each agent object.
	 */
	static class Fields {
		/** The agent's current activity. */
		public static final String ACTIVITY = "Activity";
	}

	/* FIXME the activity doesn't show the actual description */
	/**
	 * List of possible activies. This is implemented as an enum because it
	 * helps us in switch statements. Like the rest of the constants in this
	 * class, they could also have been coded directly in the model
	 */
	enum Activity implements Publishable {

		/** The agent's waiting. */
		WAITING("Waiting"),

		/** The agent's walking. */
		WALKING("Walking"),

		/** The agent's walking. */
		MOVING("Moving");

		/** Human readable desription of the activity. */
		private String description;

		/**
		 * Get the description of the activity.
		 * 
		 * @return a string describing the activity
		 */
		public String toString() {
			return description;
		}

		/**
		 * Build an instance of Activity which keeps a human readable
		 * description for when it's flattened.
		 * 
		 * @param description
		 *            the humanreadable description of the activity
		 */
		private Activity(final String description) {
			this.description = description;
		}

		/**
		 * Flatten the description of the activity.
		 * 
		 * @return a flatenned text with the description of the activity
		 */
		public FlatData flatten() {
			return new Text(description).flatten();
		}
	}
}
