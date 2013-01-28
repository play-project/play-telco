package com.orange.play.events.types.templates.rdf;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.orange.play.events.types.templates.RDFObject;

public class geo_Point extends RDFObject {
	private static final long serialVersionUID = 5526172440242871784L;
	protected Double geo_lat = null;
	protected Double geo_long = null;
	
	@Override
	public void loadFromJSON(JsonObject jsonObject) throws IllegalArgumentException {
		super.loadFromJSON(jsonObject);
		
		JsonElement geo_latJson = jsonObject.get("geo_lat");
		if (geo_latJson != null)
			geo_lat = geo_latJson.getAsDouble();
		
		JsonElement geo_longJson = jsonObject.get("geo_long");
		if (geo_longJson != null)
			geo_long = geo_longJson.getAsDouble();		
	}
	
	@Override
	public void getRDFProperties(StringBuilder result) {
		addRDFPropertyDouble(result, "geo:lat", geo_lat);
		addRDFPropertyDouble(result, "geo:long", geo_long);
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
