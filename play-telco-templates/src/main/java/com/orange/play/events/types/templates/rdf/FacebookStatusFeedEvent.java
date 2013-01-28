package com.orange.play.events.types.templates.rdf;

import java.net.URI;

public class FacebookStatusFeedEvent extends FacebookEvent {
	private static final long serialVersionUID = 8627694861503583885L;

	protected String status = null;
	
	protected String uctelco_phoneNumber = null;
	protected String uctelco_userType = null;
	
	protected String user_id = null;
	protected URI user_link = null;
	protected String user_location = null;
	protected String user_name = null;
	
	protected String sioc_links_to = null;
	
	public FacebookStatusFeedEvent() {
	}
	
	@Override
	public void getRDFProperties(StringBuilder result) {
		super.getRDFProperties(result);
		
		addRDFPropertyString(result, ":status", this.status);

		addRDFPropertyString(result, "uctelco:phoneNumber", this.uctelco_phoneNumber);
		addRDFPropertyString(result, "uctelco:userType", this.uctelco_userType);
		
		addRDFPropertyString(result, "user:id", this.user_id);
		addRDFPropertyURI(result, "user:link", this.user_link);
		addRDFPropertyString(result, "user:location", this.user_location);
		addRDFPropertyString(result, "user:name", this.user_name);
		
		addRDFPropertyString(result, "sioc:links_to", this.sioc_links_to);
	}
}
