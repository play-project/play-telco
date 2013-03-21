package com.orange.play.gcmAPI;

import eu.play_project.play_commons.constants.Constants;

public class Configuration {
    public static final String API_KEY = Constants.getProperties("play-mobilesub.properties").getProperty("google.gcm.apikey");
	public static final String PAYLOAD_KEY = Constants.getProperties("play-mobilesub.properties").getProperty("google.gcm.payloadkey");
	public static final int MAX_ATTEMPS = 5;
}