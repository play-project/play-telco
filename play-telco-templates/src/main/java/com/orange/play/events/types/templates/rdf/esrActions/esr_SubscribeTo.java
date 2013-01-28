package com.orange.play.events.types.templates.rdf.esrActions;

import java.net.URI;
import java.net.URISyntaxException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class esr_SubscribeTo extends esr_Action {
	private static final long serialVersionUID = 2343513068092880189L;
	protected URI esr_toStream = null;
	
	@Override
	public void loadFromJSON(JsonObject jsonObject) throws IllegalArgumentException {
		super.loadFromJSON(jsonObject);
	
		JsonElement esr_toStreamJson = jsonObject.get("esr_toStream");
		if (esr_toStreamJson != null)
			try {
				esr_toStream = new URI(esr_toStreamJson.getAsString());
			} catch (URISyntaxException e) {
				throw new IllegalArgumentException(e);
			}
	}
	
	@Override
	public void getRDFProperties(StringBuilder result) {
		addRDFPropertyURI(result, "esr:toStream", esr_toStream);
	}
	
	public URI getEsr_toStream() {
		return esr_toStream;
	}
	
	public void setEsr_toStream(URI esr_toStream) {
		this.esr_toStream = esr_toStream;
	}
}
