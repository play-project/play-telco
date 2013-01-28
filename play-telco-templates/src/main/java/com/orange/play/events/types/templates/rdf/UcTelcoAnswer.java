package com.orange.play.events.types.templates.rdf;

import java.net.URI;
import java.util.Set;

public class UcTelcoAnswer extends Telco {
	private static final long serialVersionUID = 8857399569814656949L;
	protected String uctelco_phoneNumber = null;
	protected String uctelco_userType = null;
	protected Boolean uctelco_ackResult = null;
	protected String uctelco_relUniqueId = null;
	protected URI uctelco_relRecommendation = null;
	protected Set<URI> uctelco_relAction = null;
	
	public UcTelcoAnswer() {
	}
	
	@Override
	public void getRDFProperties(StringBuilder result) {
		super.getRDFProperties(result);
		
		addRDFPropertyString(result, "uctelco:phoneNumber", this.uctelco_phoneNumber);
		addRDFPropertyString(result, "uctelco:userType", this.uctelco_userType);
		addRDFPropertyBoolean(result, "uctelco:ackResult", this.uctelco_ackResult);
		addRDFPropertyString(result, "uctelco:relUniqueId", this.uctelco_relUniqueId);
		addRDFPropertyURI(result, "uctelco:relRecommendation", this.uctelco_relRecommendation);
		addRDFPropertyRDFPrimitiveSet(result, "uctelco:relAction", this.uctelco_relAction);
	}

	public String getUctelco_phoneNumber() {
		return uctelco_phoneNumber;
	}

	public void setUctelco_phoneNumber(String uctelco_phoneNumber) {
		this.uctelco_phoneNumber = uctelco_phoneNumber;
	}

	public String getUctelco_userType() {
		return uctelco_userType;
	}

	public void setUctelco_userType(String uctelco_userType) {
		this.uctelco_userType = uctelco_userType;
	}

	public Boolean getUctelco_ackResult() {
		return uctelco_ackResult;
	}

	public void setUctelco_ackResult(Boolean uctelco_ackResult) {
		this.uctelco_ackResult = uctelco_ackResult;
	}

	public String getUctelco_relUniqueId() {
		return uctelco_relUniqueId;
	}

	public void setUctelco_relUniqueId(String uctelco_relUniqueId) {
		this.uctelco_relUniqueId = uctelco_relUniqueId;
	}

	public URI getUctelco_relRecommendation() {
		return uctelco_relRecommendation;
	}

	public void setUctelco_relRecommendation(URI uctelco_relRecommendation) {
		this.uctelco_relRecommendation = uctelco_relRecommendation;
	}

	public Set<URI> getUctelco_relAction() {
		return uctelco_relAction;
	}

	public void setUctelco_relAction(Set<URI> uctelco_relAction) {
		this.uctelco_relAction = uctelco_relAction;
	}
}
