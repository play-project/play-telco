package com.orange.play.events.types.templates.rdf;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.orange.play.events.types.templates.RDFObject;
import com.orange.play.events.types.templates.utils.PlayRDFUtils;

public abstract class Event extends RDFObject {	
	private static final long serialVersionUID = -1077707230194960891L;

	protected URI eventId = null;
	
	protected Date startTime = null;
	protected Date endTime = new Date();
	protected URI source = null;
	protected URI stream = null;
	protected geo_Point location = null;
	protected String message = null;
	
	protected Event() {
		PlayRDFUtils.populateWithPlayPrefixes(prefixes);
		setStreamName(PlayRDFUtils.getDefaultStreamNameForEvent(this));
	}
	
	public String toRDF(String eventName) {
		if (eventName != null)
			setEventName(eventName);
		if (eventId == null)
			throw new IllegalArgumentException("eventId == null");
		return super.toRDF(getEventId());
	}
	
	@Override
	public void loadFromJSON(JsonObject jsonObject) throws IllegalArgumentException {
		super.loadFromJSON(jsonObject);
		
		JsonElement startTimeJson = jsonObject.get("startTime");
		if (startTimeJson != null)
			startTime = new Date(startTimeJson.getAsLong());
		
		JsonElement endTimeJson = jsonObject.get("endTime");
		if (endTimeJson != null)
			endTime = new Date(endTimeJson.getAsLong());
		
		JsonElement sourceJson = jsonObject.get("source");
		if (sourceJson != null)
			try {
				source = new URI(sourceJson.getAsString());
			} catch (URISyntaxException e) {
				throw new IllegalArgumentException(e);
			}
		
		JsonElement streamJson = jsonObject.get("stream");
		if (streamJson != null)
			try {
				stream = new URI(streamJson.getAsString());
			} catch (URISyntaxException e) {
				throw new IllegalArgumentException(e);
			}
		
		JsonElement locationJson = jsonObject.get("location");
		if (locationJson != null) {
			location = new geo_Point();
			location.loadFromJSON(locationJson.getAsJsonObject());
		}
		
		JsonElement messageJson = jsonObject.get("message");
		if (messageJson != null)
			message = messageJson.getAsString();			
	}
	
	@Override
	public void getRDFProperties(StringBuilder result) {		
		addRDFPropertyDateTime(result, ":startTime", this.startTime);
		addRDFPropertyDateTime(result, ":endTime", this.endTime);
		addRDFPropertyURI(result, ":source", this.source);
		addRDFPropertyURI(result, ":stream", this.stream);
		addRDFPropertyObject(result, ":location", "blank", this.location);
		addRDFPropertyString(result, ":message", this.message);
	}

	public URI getEventId() {
		return eventId;
	}
	
	public URI getEventIdWithoutFragment() {
		try {
			URI result = new URI(eventId.getScheme(), eventId.getSchemeSpecificPart(), null);
			return result;
		} catch (URISyntaxException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public void setEventId(URI eventId) {
		this.eventId = eventId;
	}
	
	public void setEventName(String eventName) {
		if (eventName == null) {
			setEventId(null);
			return;
		}
		try {
			this.eventId = new URI(String.format(PlayRDFUtils.EVENT_URI_FORMAT_LONG, PlayRDFUtils.EVENT_PREFIX, eventName));
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public URI getSource() {
		return source;
	}

	public void setSource(URI source) {
		this.source = source;
	}

	public URI getStream() {
		return stream;
	}
	
	public URI getStreamWithoutFragment() {
		try {
			URI result = new URI(stream.getScheme(), stream.getSchemeSpecificPart(), null);
			return result;
		} catch (URISyntaxException e) {
			throw new IllegalStateException(e);
		}
	}

	public void setStream(URI stream) {
		this.stream = stream;
	}
	
	public void setStreamName(String streamName) {
		if (streamName == null) {
			setStream(null);
			return;
		}
		try {
			this.stream = new URI(String.format(PlayRDFUtils.STREAM_URI_FORMAT_LONG, PlayRDFUtils.STREAM_PREFIX, streamName));
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public geo_Point getLocation() {
		return location;
	}

	public void setLocation(geo_Point location) {
		this.location = location;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
