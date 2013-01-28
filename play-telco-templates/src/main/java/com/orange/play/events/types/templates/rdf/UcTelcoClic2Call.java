package com.orange.play.events.types.templates.rdf;

public class UcTelcoClic2Call extends Telco {
	private static final long serialVersionUID = -3096888141507691996L;
	protected String uctelco_calleePhoneNumber = null;
	protected String uctelco_callerPhoneNumber = null;
	
	public UcTelcoClic2Call() {
	}
	
	@Override
	public void getRDFProperties(StringBuilder result) {
		super.getRDFProperties(result);
		
		addRDFPropertyString(result, "uctelco:calleePhoneNumber", this.uctelco_calleePhoneNumber);
		addRDFPropertyString(result, "uctelco:callerPhoneNumber", this.uctelco_callerPhoneNumber);
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
}
