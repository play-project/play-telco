package com.orange.play.events.types.templates.rdf;

public class UcTelcoGeoLocation extends Telco {
	private static final long serialVersionUID = 408702411895504968L;
	protected String uctelco_phoneNumber = null;
	
	public UcTelcoGeoLocation() {
	}
	
	@Override
	public void getRDFProperties(StringBuilder result) {
		super.getRDFProperties(result);
		
		addRDFPropertyString(result, "uctelco:phoneNumber", this.uctelco_phoneNumber);
	}
	
	public String getUctelco_phoneNumber() {
		return uctelco_phoneNumber;
	}
	
	public void setUctelco_phoneNumber(String uctelco_phoneNumber) {
		this.uctelco_phoneNumber = uctelco_phoneNumber;
	}
}
