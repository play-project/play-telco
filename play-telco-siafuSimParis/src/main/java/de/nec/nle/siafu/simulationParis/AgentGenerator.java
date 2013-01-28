package de.nec.nle.siafu.simulationParis;

import java.util.ArrayList;

import de.nec.nle.siafu.exceptions.PlaceNotFoundException;
import de.nec.nle.siafu.model.Agent;
import de.nec.nle.siafu.model.World;
import de.nec.nle.siafu.types.Text;

/**
 * This class is used to generate all agents needed by the simulation
 */
public final class AgentGenerator {

	/**
	 * The class constructor
	 */
	public AgentGenerator() {
	}

	/**
	 * This method is used to create an agent
	 * 
	 * @param world
	 *            the simulation world
	 * @return the created agent
	 */
	public static Agent createRandomAgent(final World world) {
		try {
			// Create the agent
			Agent agent = new Agent(world.getRandomPlaceOfType("places")
					.getPos(), "HumanBrown", world);
			// Fix some agent attributs
			agent.setVisible(true);
			agent.setSpeed(Constants.SPEED);
			agent.getControl();
			agent.set("Language", new Text("English"));
			// Return the created agent
			return agent;
		} catch (PlaceNotFoundException e) {
			// Throw a run time exception
			throw new RuntimeException(
					"You didn't define the \"others\" type of places", e);
		}
	}

	/**
	 * This method is used to create a population of agents
	 * 
	 * @param amount
	 *            number of agents to create
	 * @param world
	 *            the simulation world
	 * @return a list of created agents
	 */
	public static ArrayList<Agent> createRandomPopulation(final int amount,
			final World world) {
		// Create an array list of agents
		ArrayList<Agent> population = new ArrayList<Agent>(amount);
		// Create agents and push them in the array list
		for (int i = 0; i < amount; i++) {
			population.add(createRandomAgent(world));
		}
		// Return the list of agents
		return population;
	}

}
