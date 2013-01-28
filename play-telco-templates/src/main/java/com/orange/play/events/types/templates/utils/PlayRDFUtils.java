package com.orange.play.events.types.templates.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import com.orange.play.events.types.templates.rdf.Event;
import com.orange.play.events.types.templates.rdf.FacebookStatusFeedEvent;
import com.orange.play.events.types.templates.rdf.TwitterEvent;
import com.orange.play.events.types.templates.rdf.UcTelcoAnswer;
import com.orange.play.events.types.templates.rdf.UcTelcoCall;
import com.orange.play.events.types.templates.rdf.UcTelcoClic2Call;
import com.orange.play.events.types.templates.rdf.UcTelcoEsrRecom;
import com.orange.play.events.types.templates.rdf.UcTelcoGeoLocation;

import eu.play_project.play_commons.constants.Namespace;
import eu.play_project.play_commons.constants.Stream;

public class PlayRDFUtils {
	public static final String STREAM_NS = Namespace.STREAMS.getUri();
	public static final String STREAM_PREFIX = Namespace.STREAMS.getPrefix();
	public static final String STREAM_URI_FORMAT_LONG = "%s:%s" + Stream.STREAM_ID_SUFFIX;

	public static final String EVENT_NS = Namespace.EVENTS.getUri();
	public static final String EVENT_PREFIX = Namespace.EVENTS.getPrefix();
	public static final String EVENT_URI_FORMAT_LONG = "%s:%s" + eu.play_project.play_commons.constants.Event.EVENT_ID_SUFFIX;
	
	public static void populateWithPlayPrefixes(Map<String, URI> prefixes) {
		/* COMPLETE LIST
		prefixes.put("", new URI("http://events.event-processing.org/types/"));
		prefixes.put("DUL", new URI("http://www.ontologydesignpatterns.org/ont/dul/DUL.owl#"));
		prefixes.put("dbpedia", new URI("http://dbpedia.org/resource/"));
		prefixes.put("dc", new URI("http://purl.org/dc/elements/1.1/"));
		prefixes.put("e", new URI("http://events.event-processing.org/ids/"));
		prefixes.put("esr", new URI("http://imu.ntua.gr/play/esr/mcm/2#"));
		prefixes.put("foaf", new URI("http://xmlns.com/foaf/0.1/"));
		prefixes.put("geo", new URI("http://www.w3.org/2003/01/geo/wgs84_pos#"));
		prefixes.put("gn", new URI("http://www.geonames.org/ontology#"));
		prefixes.put("ngeo", new URI("http://geovocab.org/geometry#"));
		prefixes.put("owl", new URI("http://www.w3.org/2002/07/owl#"));
		prefixes.put("rdf", new URI("http://www.w3.org/1999/02/22-rdf-syntax-ns#"));
		prefixes.put("rdfg", new URI("http://www.w3.org/2004/03/trix/rdfg-1/"));
		prefixes.put("rdfs", new URI("http://www.w3.org/2000/01/rdf-schema#"));
		prefixes.put("s", new URI("http://streams.event-processing.org/ids/"));
		prefixes.put("sioc", new URI("http://rdfs.org/sioc/ns#"));
		prefixes.put("src", new URI("http://sources.event-processing.org/ids/"));
		prefixes.put("uccrisis", new URI("http://www.mines-albi.fr/nuclearcrisisevent/"));
		prefixes.put("uctelco", new URI("http://events.event-processing.org/uc/telco/"));
		prefixes.put("user", new URI("http://graph.facebook.com/schema/user#"));
		prefixes.put("xsd", new URI("http://www.w3.org/2001/XMLSchema#"));
		*/
		
		try {
			/*
			 * We are looping through a hardcoded shortlist of all available
			 * namespaces. Not all are needed for MCM.
			 */
			for (Namespace ns : new Namespace[] {
					Namespace.TYPES, Namespace.EVENTS, Namespace.STREAMS,
					Namespace.ESR, Namespace.GEO, Namespace.RDF,
					Namespace.SIOC, Namespace.UCTELCO, Namespace.USER,
					Namespace.XSD_NS }) {
				prefixes.put(ns.getPrefix(), new URI(ns.getUri()));
			}
		} catch (URISyntaxException e) {
			throw new IllegalStateException(e);
		}

	}

	public static String getDefaultStreamNameForEvent(Event event) {
		if (event instanceof TwitterEvent) return "TaxiUCTwitter";
		if (event instanceof FacebookStatusFeedEvent) return "FacebookStatusFeed";
		
		if (event instanceof UcTelcoCall) return "TaxiUCCall";
		if (event instanceof UcTelcoGeoLocation) return "TaxiUCGeoLocation";
		if (event instanceof UcTelcoEsrRecom) return "TaxiUCESRRecom";
		if (event instanceof UcTelcoAnswer) return "TaxiUCAnswer";
		if (event instanceof UcTelcoClic2Call) return "TaxiUCClic2Call";
		
		throw new UnsupportedOperationException();
		// FIXME Antonio: add other eventClasses
	}
}
