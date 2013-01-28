package com.orange.play.events.types.templates.rdf.esrActions;

import java.net.URI;
import java.net.URISyntaxException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class esr_UnsubscribeFrom extends esr_Action {
	private static final long serialVersionUID = -3627143836870191583L;
	protected URI esr_fromStream = null;
	
	@Override
	public void loadFromJSON(JsonObject jsonObject) throws IllegalArgumentException {
		super.loadFromJSON(jsonObject);
		
		JsonElement esr_fromStreamJson = jsonObject.get("esr_fromStream");
		if (esr_fromStreamJson != null)
			try {
				esr_fromStream = new URI(esr_fromStreamJson.getAsString());
			} catch (URISyntaxException e) {
				throw new IllegalArgumentException(e);
			}
	}
	
	@Override
	public void getRDFProperties(StringBuilder result) {
		addRDFPropertyURI(result, "esr:fromStream", esr_fromStream);
	}
	
	public URI getEsr_fromStream() {
		return esr_fromStream;
	}
	
	public void setEsr_fromStream(URI esr_fromStream) {
		this.esr_fromStream = esr_fromStream;
	}
}
