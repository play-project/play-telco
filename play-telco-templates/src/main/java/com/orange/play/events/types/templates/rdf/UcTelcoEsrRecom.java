package com.orange.play.events.types.templates.rdf;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.orange.play.events.types.templates.rdf.esrActions.esr_Action;

public class UcTelcoEsrRecom extends Telco {
	private static final long serialVersionUID = -5084983280582352299L;
	protected String uctelco_calleePhoneNumber = null;
	protected String uctelco_callerPhoneNumber = null;
	protected Boolean uctelco_ackRequired = null;
	protected Boolean uctelco_answerRequired = null;
	
	protected URI esr_recommendation = null;
	
	protected Set<esr_Action> uctelco_action = null;
	
	public UcTelcoEsrRecom() {
	}
	
	@Override
	public void loadFromJSON(JsonObject jsonObject) throws IllegalArgumentException {
		super.loadFromJSON(jsonObject);
	
		JsonElement uctelco_calleePhoneNumberJson = jsonObject.get("uctelco_calleePhoneNumber");
		if (uctelco_calleePhoneNumberJson != null)
			uctelco_calleePhoneNumber = uctelco_calleePhoneNumberJson.getAsString();
		
		JsonElement uctelco_callerPhoneNumberJson = jsonObject.get("uctelco_callerPhoneNumber");
		if (uctelco_callerPhoneNumberJson != null)
			uctelco_callerPhoneNumber = uctelco_callerPhoneNumberJson.getAsString();
		
		JsonElement uctelco_ackRequiredJson = jsonObject.get("uctelco_ackRequired");
		if (uctelco_ackRequiredJson != null)
			uctelco_ackRequired = uctelco_ackRequiredJson.getAsBoolean();
		
		JsonElement uctelco_answerRequiredJson = jsonObject.get("uctelco_answerRequired");
		if (uctelco_answerRequiredJson != null)
			uctelco_answerRequired = uctelco_answerRequiredJson.getAsBoolean();
		
		JsonElement esr_recommendationJson = jsonObject.get("esr_recommendation");
		if (esr_recommendationJson != null)
			try {
				esr_recommendation = new URI(esr_recommendationJson.getAsString());
			} catch (URISyntaxException e) {
				throw new IllegalArgumentException(e);
			}
		
		uctelco_action = new HashSet<esr_Action>();
		JsonElement uctelco_actionJson = jsonObject.get("uctelco_action");
		if (uctelco_actionJson != null) {
			JsonArray uctelco_actionJsonArray = uctelco_actionJson.getAsJsonArray();
			for (JsonElement esr_actionJson : uctelco_actionJsonArray) {
				esr_Action esr_action = esr_Action.createFromJSON(esr_actionJson.getAsJsonObject(), prefixes);
				uctelco_action.add(esr_action);
			}
		}
	}
	
	@Override
	public void getRDFProperties(StringBuilder result) {
		super.getRDFProperties(result);
		
		addRDFPropertyString(result, "uctelco:calleePhoneNumber", this.uctelco_calleePhoneNumber);
		addRDFPropertyString(result, "uctelco:callerPhoneNumber", this.uctelco_callerPhoneNumber);
		addRDFPropertyBoolean(result, "uctelco:ackRequired", this.uctelco_ackRequired);
		addRDFPropertyBoolean(result, "uctelco:answerRequired", this.uctelco_answerRequired);

		addRDFPropertyURI(result, "esr:recommendation", this.esr_recommendation);

		addRDFPropertyRDFObjectSet(result, "uctelco:action", "blank", uctelco_action);
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

	public Boolean getUctelco_ackRequired() {
		return uctelco_ackRequired;
	}

	public void setUctelco_ackRequired(Boolean uctelco_ackRequired) {
		this.uctelco_ackRequired = uctelco_ackRequired;
	}

	public Boolean getUctelco_answerRequired() {
		return uctelco_answerRequired;
	}

	public void setUctelco_answerRequired(Boolean uctelco_answerRequired) {
		this.uctelco_answerRequired = uctelco_answerRequired;
	}

	public URI getEsr_recommendation() {
		return esr_recommendation;
	}

	public void setEsr_recommendation(URI esr_recommendation) {
		this.esr_recommendation = esr_recommendation;
	}

	public Set<esr_Action> getUctelco_action() {
		return uctelco_action;
	}
	
	public void setUctelco_action(Set<esr_Action> uctelco_action) {
		this.uctelco_action = uctelco_action;
	}
}
