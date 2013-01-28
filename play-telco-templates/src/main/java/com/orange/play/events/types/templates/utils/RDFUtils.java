package com.orange.play.events.types.templates.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import eu.play_project.play_commons.constants.Event;

public final class RDFUtils {
	public static final String RDF_TYPE_SHORT = "a";
	public static final String RDF_TYPE_LONG = "rdf_type";
	
	private static final String DEFAULT_SCHEMA = ":";
	private static final String RDF_URI_SIMPLE_FORMAT = "%s";
	private static final String RDF_URI_WITH_BRACKETS_FORMAT = "<%s>";
	private static final SimpleDateFormat formatDateTimeUTC;
	private static final SimpleDateFormat formatDateUTC;
	private static final SimpleDateFormat formatTimeUTC;
	
	static {
		formatDateTimeUTC = new SimpleDateFormat(Event.DATE_FORMAT_8601);
		formatDateUTC = new SimpleDateFormat("yyyy-MM-dd");
		formatTimeUTC = new SimpleDateFormat("HH:mm:ss");
		formatDateTimeUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
		formatDateUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
		formatTimeUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	
	private RDFUtils() {
	}

	public static String formatDateTime(Date date){
		if (date == null)
			return null;
		return formatDateTimeUTC.format(date);
	}
	
	public static String formatDate(Date date){
		if (date == null)
			return null;
		return formatDateUTC.format(date);
	}
	
	public static String formatTime(Date date){
		if (date == null)
			return null;
		return formatTimeUTC.format(date);
	}
	
	public static URI resolveURI(String uriStr, Map<String, URI> prefixes) {
		int schemeSeparator = uriStr.indexOf(DEFAULT_SCHEMA);
		if (schemeSeparator < 0)
			throw new IllegalArgumentException("invalid schema");
		String scheme = uriStr.substring(0, schemeSeparator);
		String remaining = uriStr.substring(schemeSeparator + 1);
		if (remaining.isEmpty())
			throw new IllegalArgumentException("invalid uri format");
		URI uri;
		try {
			if (scheme.isEmpty())
				uri = new URI("default:" + remaining);
			else
				uri = new URI(uriStr);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
		URI baseURI = prefixes.get(scheme);
		if (baseURI != null) {
			uriStr = baseURI.toString() + uri.getSchemeSpecificPart() + (uri.getFragment() != null ? "#" + uri.getFragment() : "");
		}
		try {
			return new URI(uriStr);
		} catch (URISyntaxException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public static String formatURI(URI uri, Map<String, URI> prefixes, boolean forceBrackets, boolean brackets) {
		String format = RDF_URI_WITH_BRACKETS_FORMAT;
		String uriStr = uri.toString();
		if (prefixes != null) {
			URI baseURI = prefixes.get(uri.getScheme());
			if (baseURI != null) {
				uriStr = baseURI.toString() + uri.getSchemeSpecificPart() + (uri.getFragment() != null ? "#" + uri.getFragment() : "");
			}
		}
		if (forceBrackets) {
			format = brackets ? RDF_URI_WITH_BRACKETS_FORMAT : RDF_URI_SIMPLE_FORMAT;
		}
		return String.format(format, uriStr);
	}
	
	public static String formatURI(URI uri, Map<String, URI> prefixes) {
		return formatURI(uri, prefixes, false, false);
	}
	
	public static URI getAndCheckRdf_typeFromJson(JsonObject jsonObject, Map<String, URI> prefixes) {
		JsonElement rdf_type = jsonObject.get(RDFUtils.RDF_TYPE_LONG);
		if (rdf_type == null)
			rdf_type = jsonObject.get(RDFUtils.RDF_TYPE_SHORT);
		if (rdf_type == null)
			throw new IllegalArgumentException("rdf_type undefined");
		return resolveURI(rdf_type.getAsString(), prefixes);
	}
}
