package com.orange.play.events.types.templates.rdf.esrActions;

import java.net.URI;
import java.util.Map;

import com.google.gson.JsonObject;
import com.orange.play.events.types.templates.RDFObject;
import com.orange.play.events.types.templates.utils.PlayRDFUtils;
import com.orange.play.events.types.templates.utils.RDFUtils;

public abstract class esr_Action extends RDFObject {
	
	private static final long serialVersionUID = -3436655573279362907L;

	public esr_Action() {
		PlayRDFUtils.populateWithPlayPrefixes(prefixes);
	}
	
	public static esr_Action createFromJSON(JsonObject jsonObject, Map<String, URI> prefixes) throws IllegalArgumentException {
		URI rdf_type = RDFUtils.getAndCheckRdf_typeFromJson(jsonObject, prefixes);
		esr_Action esr_action = null;
		if (rdf_type.equals(getRdf_typeFor(esr_SubscribeTo.class, prefixes))) {
			esr_action = new esr_SubscribeTo();
		} else if (rdf_type.equals(getRdf_typeFor(esr_UnsubscribeFrom.class, prefixes))) {
			esr_action = new esr_UnsubscribeFrom();
		} else if (rdf_type.equals(getRdf_typeFor(uctelco_OpenFacebook.class, prefixes))) {
			esr_action = new uctelco_OpenFacebook();
		} else if (rdf_type.equals(getRdf_typeFor(uctelco_OpenTwitter.class, prefixes))) {
			esr_action = new uctelco_OpenTwitter();
		} else if (rdf_type.equals(getRdf_typeFor(uctelco_ComposeMail.class, prefixes))) {
			esr_action = new uctelco_ComposeMail();
		} else if (rdf_type.equals(getRdf_typeFor(esr_ShowFriendGeolocation.class, prefixes))) {
			esr_action = new esr_ShowFriendGeolocation();
		} else {
			throw new IllegalArgumentException(String.format("unrecognized rdf_type: %s", rdf_type));
		}
		esr_action.loadFromJSON(jsonObject);
		return esr_action;
	}

}
