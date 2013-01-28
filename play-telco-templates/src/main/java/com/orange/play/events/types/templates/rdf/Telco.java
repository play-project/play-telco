package com.orange.play.events.types.templates.rdf;

import java.net.URI;
import java.net.URISyntaxException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public abstract class Telco extends Event {
	private static final long serialVersionUID = -4342933704279293576L;
	protected Integer uctelco_sequenceNumber = null;
	protected String uctelco_uniqueId = null;
	
	protected URI uctelco_mailAddress = null;
	protected String screenNumber = null;
	protected String user_id = null;
	
	protected Telco() {
	}
	
	@Override
	public void loadFromJSON(JsonObject jsonObject) throws IllegalArgumentException {
		super.loadFromJSON(jsonObject);
		
		JsonElement uctelco_sequenceNumberJson = jsonObject.get("uctelco_sequenceNumber");
		if (uctelco_sequenceNumberJson != null)
			uctelco_sequenceNumber = uctelco_sequenceNumberJson.getAsInt();

		JsonElement uctelco_uniqueIdJson = jsonObject.get("uctelco_uniqueId");
		if (uctelco_uniqueIdJson != null)
			uctelco_uniqueId = uctelco_uniqueIdJson.getAsString();		
		
		JsonElement uctelco_mailAddressJson = jsonObject.get("uctelco_mailAddress");
		if (uctelco_mailAddressJson != null)
			try {
				uctelco_mailAddress = new URI(uctelco_mailAddressJson.getAsString());
			} catch (URISyntaxException e) {
				throw new IllegalArgumentException(e);
			}
		
		JsonElement screenNumberJson = jsonObject.get("screenNumber");
		if (screenNumberJson != null)
			screenNumber = screenNumberJson.getAsString();
		
		JsonElement user_idJson = jsonObject.get("user_id");
		if (user_idJson != null)
			user_id = user_idJson.getAsString();
	}
	
	@Override
	public void getRDFProperties(StringBuilder result) {
		super.getRDFProperties(result);
		
		addRDFPropertyInt(result, "uctelco:sequenceNumber", this.uctelco_sequenceNumber);
		addRDFPropertyString(result, "uctelco:uniqueId", this.uctelco_uniqueId);
		
		addRDFPropertyURI(result, "uctelco:mailAddress", this.uctelco_mailAddress);
		addRDFPropertyString(result, ":screenName", this.screenNumber);
		addRDFPropertyString(result, "user:id", this.user_id);
	}

	public Integer getUctelco_sequenceNumber() {
		return uctelco_sequenceNumber;
	}

	public void setUctelco_sequenceNumber(Integer uctelco_sequenceNumber) {
		this.uctelco_sequenceNumber = uctelco_sequenceNumber;
	}

	public String getUctelco_uniqueId() {
		return uctelco_uniqueId;
	}

	public void setUctelco_uniqueId(String uctelco_uniqueId) {
		this.uctelco_uniqueId = uctelco_uniqueId;
	}

	public URI getUctelco_mailAddress() {
		return uctelco_mailAddress;
	}
	
	public void setUctelco_mailAddress(URI uctelco_mailAddress) {
		this.uctelco_mailAddress = uctelco_mailAddress;
	}
	
	public String getScreenNumber() {
		return screenNumber;
	}

	public void setScreenNumber(String screenNumber) {
		this.screenNumber = screenNumber;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
}
