package com.orange.play.events.types.templates.utils;

import com.orange.play.events.types.templates.rdf.Event;

public class PlayLogger {
	public static final String LOG_FORMAT = "%s %s %s";
	public static final String APPLICATION_UC_TELCO = "SIAFU";
	public static final String DIRECTION_ENTRY = "Entry";
	public static final String DIRECTION_EXIT = "Exit";
	
	public static String lineUcTelco(Event event, String application, String direction) {
		return String.format(
			LOG_FORMAT, 
			application, 
			direction,
			RDFUtils.formatURI(event.getEventIdWithoutFragment(), event.getPrefixes(), true, false));
	}
}
