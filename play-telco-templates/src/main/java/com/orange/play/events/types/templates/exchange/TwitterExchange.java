package com.orange.play.events.types.templates.exchange;

import java.io.Serializable;

public class TwitterExchange implements Serializable {
	private static final long serialVersionUID = -7473150027051287609L;
	
	private final String twId;
	private final String message;
	private final String status;
	
	public TwitterExchange(String twId, String message, String status) {
		super();
		this.twId = twId;
		this.message = message;
		this.status = status;
	}

	public String getTwId() {
		return twId;
	}

	public String getMessage() {
		return message;
	}

	public String getStatus() {
		return status;
	}
}
