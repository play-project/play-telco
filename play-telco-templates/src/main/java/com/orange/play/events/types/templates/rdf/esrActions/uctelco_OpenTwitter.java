package com.orange.play.events.types.templates.rdf.esrActions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class uctelco_OpenTwitter extends esr_Action {
	private static final long serialVersionUID = 5514870407351238541L;
	protected String screenName = null;
	
	@Override
	public void loadFromJSON(JsonObject jsonObject) throws IllegalArgumentException {
		super.loadFromJSON(jsonObject);
	
		JsonElement screenNameJson = jsonObject.get("screenName");
		if (screenNameJson != null)
			screenName = screenNameJson.getAsString();	
	}
	
	@Override
	public void getRDFProperties(StringBuilder result) {
		addRDFPropertyString(result, ":screenName", screenName);
	}
	
	public String getScreenName() {
		return screenName;
	}
	
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
}
