package com.orange.play.events.types.templates.rdf.esrActions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class uctelco_OpenFacebook extends esr_Action {
	private static final long serialVersionUID = -4754829745474596959L;
	protected String user_id = null;
	
	@Override
	public void loadFromJSON(JsonObject jsonObject) throws IllegalArgumentException {
		super.loadFromJSON(jsonObject);
		
		JsonElement user_idJson = jsonObject.get("user_id");
		if (user_idJson != null)
			user_id = user_idJson.getAsString();	
	}
	
	@Override
	public void getRDFProperties(StringBuilder result) {
		addRDFPropertyString(result, "user:id", user_id);
	}
	
	public String getUser_id() {
		return user_id;
	}
	
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

}
