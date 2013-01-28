package com.orange.play.events.types.templates.rdf.esrActions;

import java.net.URI;
import java.net.URISyntaxException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class uctelco_ComposeMail extends esr_Action {
	private static final long serialVersionUID = -3509622673345988462L;
	protected URI uctelco_mailAddress = null;
	protected String uctelco_mailSubject = null;
	protected String uctelco_mailContent = null;
	
	@Override
	public void loadFromJSON(JsonObject jsonObject) throws IllegalArgumentException {
		super.loadFromJSON(jsonObject);
		
		JsonElement uctelco_mailAddressJson = jsonObject.get("uctelco_mailAddress");
		if (uctelco_mailAddressJson != null)
			try {
				uctelco_mailAddress = new URI(uctelco_mailAddressJson.getAsString());
			} catch (URISyntaxException e) {
				throw new IllegalArgumentException(e);
			}
		
		JsonElement uctelco_mailSubjectJson = jsonObject.get("uctelco_mailSubject");
		if (uctelco_mailSubjectJson != null)
			uctelco_mailSubject = uctelco_mailSubjectJson.getAsString();		
		
		JsonElement uctelco_mailContentJson = jsonObject.get("uctelco_mailContent");
		if (uctelco_mailContentJson != null)
			uctelco_mailContent = uctelco_mailContentJson.getAsString();
	}
	
	@Override
	public void getRDFProperties(StringBuilder result) {
		addRDFPropertyURI(result, "uctelco:mailAddress", uctelco_mailAddress);
		addRDFPropertyString(result, "uctelco:mailSubject", uctelco_mailSubject);
		addRDFPropertyString(result, "uctelco:mailContent", uctelco_mailContent);
	}

	public URI getUctelco_mailAddress() {
		return uctelco_mailAddress;
	}
	
	public void setUctelco_mailAddress(URI uctelco_mailAddress) {
		this.uctelco_mailAddress = uctelco_mailAddress;
	}
	
	public String getUctelco_mailSubject() {
		return uctelco_mailSubject;
	}
	
	public void setUctelco_mailSubject(String uctelco_mailSubject) {
		this.uctelco_mailSubject = uctelco_mailSubject;
	}
	
	public String getUctelco_mailContent() {
		return uctelco_mailContent;
	}

	public void setUctelco_mailContent(String uctelco_mailContent) {
		this.uctelco_mailContent = uctelco_mailContent;
	}
}
