package de.nec.nle.siafu.simulationParis;

import java.util.ArrayList;
import java.util.Map;

import de.nec.nle.siafu.behaviormodels.BaseContextModel;
import de.nec.nle.siafu.model.Overlay;
import de.nec.nle.siafu.model.World;

public class ContextModel extends BaseContextModel {

	public ContextModel(World world) {
		super(world);
	}

	@Override
	public void createOverlays(ArrayList<Overlay> overlaysList) {
	}

	@Override
	public void doIteration(Map<String, Overlay> map) {
	}

}
