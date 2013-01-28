package com.orange.play.events.types.templates.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public final class CEPUtils {
	private static final SimpleDateFormat formatDateTimeUTC;
	
	static {
		formatDateTimeUTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		formatDateTimeUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	
	private CEPUtils() {
	}

	public static String formatDateTime(Date date){
		if (date == null)
			return null;
		return formatDateTimeUTC.format(date);
	}
	
	public static String escapeField(String fieldString) {
		return fieldString;
	}
}
