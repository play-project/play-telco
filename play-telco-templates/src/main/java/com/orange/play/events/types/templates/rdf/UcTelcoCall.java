package com.orange.play.events.types.templates.rdf;

public class UcTelcoCall extends Telco {
	private static final long serialVersionUID = -6058054023347508529L;
	protected String uctelco_calleePhoneNumber = null;
	protected String uctelco_callerPhoneNumber = null;
	protected String uctelco_direction = null;
	
	public UcTelcoCall() {
	}
	
	@Override
	public void getRDFProperties(StringBuilder result) {
		super.getRDFProperties(result);
		
		addRDFPropertyString(result, "uctelco:calleePhoneNumber", this.uctelco_calleePhoneNumber);
		addRDFPropertyString(result, "uctelco:callerPhoneNumber", this.uctelco_callerPhoneNumber);
		addRDFPropertyString(result, "uctelco:direction", this.uctelco_direction);
	}

	public String getUctelco_calleePhoneNumber() {
		return uctelco_calleePhoneNumber;
	}

	public void setUctelco_calleePhoneNumber(String uctelco_calleePhoneNumber) {
		this.uctelco_calleePhoneNumber = uctelco_calleePhoneNumber;
	}

	public String getUctelco_callerPhoneNumber() {
		return uctelco_callerPhoneNumber;
	}

	public void setUctelco_callerPhoneNumber(String uctelco_callerPhoneNumber) {
		this.uctelco_callerPhoneNumber = uctelco_callerPhoneNumber;
	}

	public String getUctelco_direction() {
		return uctelco_direction;
	}

	public void setUctelco_direction(String uctelco_direction) {
		this.uctelco_direction = uctelco_direction;
	}
}
