package de.nec.nle.siafu.simulationParis;

import static de.nec.nle.siafu.simulationParis.Constants.Fields.ACTIVITY;



import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import de.nec.nle.siafu.behaviormodels.BaseAgentModel;
import de.nec.nle.siafu.exceptions.PlaceNotFoundException;
import de.nec.nle.siafu.exceptions.PlaceTypeUndefinedException;
import de.nec.nle.siafu.model.Agent;
import de.nec.nle.siafu.model.Place;
import de.nec.nle.siafu.model.World;
import de.nec.nle.siafu.output.EventsSender;
import de.nec.nle.siafu.output.EventsSenderNew;
import de.nec.nle.siafu.output.EventsSenderNewCEP1;
import de.nec.nle.siafu.output.EventsSenderNewICCS;
import de.nec.nle.siafu.output.EventsSenderNewICCSRDF;
import de.nec.nle.siafu.output.EventsSenderRDF;
import de.nec.nle.siafu.output.EventsSenderWSN;
import de.nec.nle.siafu.simulationParis.Constants.*;


/**
 * This class is used to program all simualtion's agents behavor
 */
public class AgentModel extends BaseAgentModel {
	private Agent [] taxiTab = new Agent[Constants.TAXINUMBER];
	private Agent [] customerTab = new Agent[Constants.CUSTOMERNUMBER];
	private Agent [] trafficAgentTab = new Agent[Constants.TRAFFICAGENTNUMBER];
	private Place firstTaxiStartPlace;
	private Place firstTaxiTransitPlace;
	private Place firstCustomerStartPlace;
	private Place thirdTaxiStartPlace ;
	private Place thirdTaxiTransitPlace ;
	private Place trafficAgentStartPos;
	private Place trafficAgentDest;
	private Place random1Place;
	private Place random2Place;
	private Place random3Place;

	// Use to send SMS customer alert once only for fisrt and second customer
	private boolean isFirstIterationForFirstCustomer = true;
	private boolean isFirstIterationForSecondCustomer = true;

	// Use to send events on Web services back-ends
	//private EventsSenderNew eventsSender;
	//private EventsSenderWSN eventsSender;
//	private EventsSenderNewICCS eventsSender;
	private EventsSenderNewICCSRDF eventsSender;
	//private EventsSenderNewCEP1 eventsSender;
//	private EventsSenderRDF eventsSender;
	// Class constructor
	public AgentModel(World world) {
		super(world);
		// switch between old and new EventSender
	//	eventsSender = new EventsSenderNewICCS(this.world);
		eventsSender = new EventsSenderNewICCSRDF(this.world);
		//eventsSender = new EventsSenderRDF(this.world);
		//eventsSender = new EventsSenderNewCEP1(this.world);
		System.out.println("Loaded..eventSender : "+eventsSender);
		//}
	}
	public ArrayList<Agent> createAgents() {
		// Generate simulation's agents list
		ArrayList<Agent> people = AgentGenerator.createRandomPopulation(
				Constants.POPULATION, world);
		// All simulation's places types
		Iterator<Place> places = null;
		Iterator<Place> placesPOis = null;
		// PG to test places
		Iterator<Place> placespg = null;
		Iterator<Place> placespg1 = null;

		String[] poisNames = {"Tuileries Garden","Branly Museum", "Orsay Museum", "National Assembly", "Invalides",
				"Rodin Museum",  "Science PO", "Bir-Hakeim",  "Motte Piquet", "Champs de Mars",
				"Grenelle","Military School", "Unesco", "Necker Hospital", "Faculty" }; 
		String[] pgsNames = { "Estrees", "Bon Marché", "Cambronne" }; 
		try {
			// Get places list for each places types
			System.out.println("PeopleNumber: "+world.getPeople().size());
			int placesNumber = world.getPlacesOfType("places").size();
			System.out.println("placesNumber: "+placesNumber);


			placesPOis = world.getPlacesOfType("places").iterator();
			int i = 0;
			while(placesPOis.hasNext()) {
				Place element = placesPOis.next(); 
				// PG 14/09
				if (i == 7) trafficAgentStartPos = element;
				if (i == 14) trafficAgentDest = element;
				element.setName(poisNames[i]);
				System.out.println("POI11: "+ i+ " :"+ element.getName() + "#"
						+ ((String)element.getPos().flatten().toString()).substring(32) );
				i++;
			} 
			places = world.getPlacesOfType("places").iterator();

			int placesNumberpg = world.getPlacesOfType("placespg").size();
			System.out.println("placesNumberpg: "+placesNumberpg);

			placespg1 = world.getPlacesOfType("placespg").iterator();
			int j = 0;
			while(placespg1.hasNext()) {
				Place element = placespg1.next(); 
				element.setName(pgsNames[j]);
				System.out.println("Pg: "+ element.getName() + "#"
						+ ((String)element.getPos().flatten().toString()).substring(32) );
				j++;
			} 
			placespg = world.getPlacesOfType("placespg").iterator();


		} catch (PlaceTypeUndefinedException e) {
			// Print exception stack trace
			e.printStackTrace();
		}
		firstTaxiStartPlace = placespg.next();
		firstTaxiTransitPlace = placespg.next();
		thirdTaxiStartPlace = places.next();
		thirdTaxiTransitPlace = places.next();
		random1Place = places.next();
		random2Place = places.next();
		random3Place = places.next();
		// PG 14/09
		//	trafficAgentStartPos = places.next();
		firstCustomerStartPlace = placespg.next();
		// Get an instance of each simulation agent
		Iterator<Agent> peopleIt = people.iterator();

		// Fix first Taxi properties - others take  random routes 
		taxiTab[0] = peopleIt.next();
		taxiTab[0].setName("Taxi10");
		taxiTab[0].setPos(firstTaxiStartPlace.getPos());
		taxiTab[0].setDestination(firstTaxiTransitPlace);
		taxiTab[0].setImage("CarRed");
		taxiTab[0].set(ACTIVITY, Activity.MOVING);

		// Set   customer properties
		for (int i=0; i < Constants.CUSTOMERNUMBER; i++) {
			customerTab[i] = peopleIt.next();
			customerTab[i].setName("User"+(i+10));
			customerTab[i].setPos(places.next().getPos());
			customerTab[i].set(ACTIVITY, Activity.WALKING);
			if (i% 5 == 0) {
				customerTab[i].setImage("HumanBrown");
			}
			if (i% 5 == 1){
				customerTab[i].setImage("HumanGreen");
			}
			if (i% 5 == 2) {
				customerTab[i].setImage("HumanMagenta");
			}
			if (i% 5 == 3) {
				customerTab[i].setImage("HumanBlue");

			}
			if (i% 5 == 4) {
				customerTab[i].setImage("HumanGreen");
			}
			if (i% 5 == 4) {
				customerTab[i].setImage("HumanYellow");
			}

		}
		// Set traffic infos agent properties
		trafficAgentTab[0] = peopleIt.next();
		trafficAgentTab[0].setName("Traffic Infos Agent");
		trafficAgentTab[0].setPos(trafficAgentStartPos.getPos());
		trafficAgentTab[0].setImage("TrafficInfos");
		//trafficAgentTab[0].setDestination(firstCustomerStartPlace);
		trafficAgentTab[0].setDestination(trafficAgentDest);
		trafficAgentTab[0].setSpeed(Constants.SPEED);
		trafficAgentTab[0].set(ACTIVITY, Activity.WALKING);

		for (int i=1; i<Constants.TAXINUMBER; i++) {

			taxiTab[i] = peopleIt.next();
			taxiTab[i].setName("Taxi"+(i+10));
			taxiTab[i].setPos(thirdTaxiStartPlace.getPos());

			if (i% 5 == 0) {
				taxiTab[i].setImage("CarPink");
				taxiTab[i].setDestination(trafficAgentStartPos);
			}
			if (i% 5 == 1){
				taxiTab[i].setImage("CarRed");
				taxiTab[i].setDestination(thirdTaxiTransitPlace);
			}
			if (i% 5 == 2) {
				taxiTab[i].setImage("CarYellow");
				taxiTab[i].setDestination(random1Place);
			}
			if (i% 5 == 3) {
				taxiTab[i].setImage("CarBlue");
				taxiTab[i].setDestination(random2Place);
			}
			if (i% 5 == 4) {
				taxiTab[i].setImage("CarGreen");
				taxiTab[i].setDestination(random3Place);
			}
			taxiTab[i].set(ACTIVITY, Activity.MOVING);
		}
		return people;
	}

	public void doIteration(Collection<Agent> agentsCollection) {
		// Handle first customer only at the end of first iteration
		if (isFirstIterationForFirstCustomer == true) {
			eventsSender.sendCustomerAlert("0656588996", customerTab[0].getPos()
					.getCoordinates());
			isFirstIterationForFirstCustomer = false;
		}
		// Handle second customer only at the end of first iteration
		if (isFirstIterationForSecondCustomer == true) {
			eventsSender.sendCustomerAlert("0645788475", customerTab[1].getPos()
					.getCoordinates());
			isFirstIterationForSecondCustomer = false;
		}
		handleFirstTaxi();
		handleTaxis();
		handleOthersAgents();
		// Send all events at the end of iteration
		eventsSender.notifyIterationConcluded();
	}
	private void handleTaxis() {
		// Test if taxi is at its destination
		for (int i=1; i<Constants.TAXINUMBER; i++) {
			if (taxiTab[i].isAtDestination()) {
				// Next taxi destination
				Place nextPlace = null;
				try {
					// Get a random place of type randomPlaces
					nextPlace = world.getRandomPlaceOfType("places");
					// Ensure that third current position is different to its next destination
					if (taxiTab[i].getPos().equals(nextPlace.getPos())) {
						// Get a new random place
						nextPlace = world.getRandomPlaceOfType("places");
					} else {
						// Fix third taxi destination
						taxiTab[i].setDestination(nextPlace);
					}
				} catch (PlaceNotFoundException e) {
					// Throw new runtime exception
					throw new RuntimeException(
							"You didn't define randomPlaces place types", e);
				}
			}
		}

		//		if (!customerTab[1].isAtDestination())
		//			System.out.println("Try to make an outgoing Call!!!");
	}
	private void handleFirstTaxi() {

		// TODO Auto-generated method stub
		// Test if taxi is at its destination
		if (taxiTab[0].isAtDestination()) {

			// Test if taxi is at its transit destination
			if (taxiTab[0].getPos().equals(firstTaxiTransitPlace.getPos())) {
				// Set taxi destination to first customer start location
				taxiTab[0].setDestination(firstTaxiStartPlace);
				// Test if taxi is at its start location
			} else if (taxiTab[0].getPos().equals(firstTaxiStartPlace.getPos())) {
				// Set taxi destination to its transit location
				taxiTab[0].setDestination(firstTaxiTransitPlace);
			} else {
				// Set taxi destination to its start location
				taxiTab[0].setDestination(firstTaxiStartPlace);
			}
		}
	}
	/**
	 * This method is used to handle traffic inos agent
	 */
	private void handleOthersAgents() {
		// Test if traffic infos agent is at its destionation

		if (trafficAgentTab[0].isAtDestination()) {
			// Traffic infos agent next destination
			Place nextPlace = null;
			try {
				// Get a place of type random place
				nextPlace = world.getRandomPlaceOfType("places");
				// Verify that traffic infos agent current position is different from its next destionation
				if (trafficAgentTab[0].getPos().equals(nextPlace.getPos())) {
					// Get another random place
					nextPlace = world.getRandomPlaceOfType("places");
				} else {
					// Set traffic infos agent destination
					trafficAgentTab[0].setDestination(nextPlace);
				}
			} catch (PlaceNotFoundException e) {
				// Throw new runtime exception
				throw new RuntimeException(
						"You didn't define randomPlaces place types", e);
			}
		}

	}

}


