package com.orange.play.events.types.templates.rdf.esrActions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class esr_ShowFriendGeolocation extends esr_Action {
	private static final long serialVersionUID = -4870783128047076670L;
	protected String uctelco_phoneNumber;
	protected Double geo_lat = null;
	protected Double geo_long = null;
	
	@Override
	public void loadFromJSON(JsonObject jsonObject) throws IllegalArgumentException {
		super.loadFromJSON(jsonObject);
		
		JsonElement uctelco_phoneNumberJson = jsonObject.get("uctelco_phoneNumber");
		if (uctelco_phoneNumberJson != null)
			uctelco_phoneNumber = uctelco_phoneNumberJson.getAsString();
		
		JsonElement geo_latJson = jsonObject.get("geo_lat");
		if (geo_latJson != null)
			geo_lat = geo_latJson.getAsDouble();
		
		JsonElement geo_longJson = jsonObject.get("geo_long");
		if (geo_longJson != null)
			geo_long = geo_longJson.getAsDouble();		
	}
	
	@Override
	public void getRDFProperties(StringBuilder result) {
		addRDFPropertyString(result, "uctelco:phoneNumber", uctelco_phoneNumber);
		addRDFPropertyDouble(result, "geo:lat", geo_lat);
		addRDFPropertyDouble(result, "geo:lat", geo_long);
	}
	
	public String getUctelco_phoneNumber() {
		return uctelco_phoneNumber;
	}
	
	public void setUctelco_phoneNumber(String uctelco_phoneNumber) {
		this.uctelco_phoneNumber = uctelco_phoneNumber;
	}

	public Double getGeo_lat() {
		return geo_lat;
	}

	public void setGeo_lat(Double geo_lat) {
		this.geo_lat = geo_lat;
	}

	public Double getGeo_long() {
		return geo_long;
	}

	public void setGeo_long(Double geo_long) {
		this.geo_long = geo_long;
	}
}
