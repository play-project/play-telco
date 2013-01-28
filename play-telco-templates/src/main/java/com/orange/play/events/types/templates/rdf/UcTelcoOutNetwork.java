package com.orange.play.events.types.templates.rdf;

public class UcTelcoOutNetwork extends Telco {
	private static final long serialVersionUID = 5714047208265542964L;
	protected Boolean uctelco_outOfNetwork = null;
	protected String uctelco_signalStrength = null;
	
	public UcTelcoOutNetwork() {
	}
	
	@Override
	public void getRDFProperties(StringBuilder result) {
		super.getRDFProperties(result);

		addRDFPropertyBoolean(result, "uctelco_outOfNetwork", this.uctelco_outOfNetwork);
		addRDFPropertyString(result, "uctelco_signalStrength", this.uctelco_signalStrength);
	}

	public Boolean getUctelco_outOfNetwork() {
		return uctelco_outOfNetwork;
	}

	public void setUctelco_outOfNetwork(Boolean uctelco_outOfNetwork) {
		this.uctelco_outOfNetwork = uctelco_outOfNetwork;
	}

	public String getUctelco_signalStrength() {
		return uctelco_signalStrength;
	}

	public void setUctelco_signalStrength(String uctelco_signalStrength) {
		this.uctelco_signalStrength = uctelco_signalStrength;
	}
}
