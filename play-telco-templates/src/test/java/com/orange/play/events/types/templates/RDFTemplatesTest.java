package com.orange.play.events.types.templates;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.orange.play.events.types.templates.rdf.Event;
import com.orange.play.events.types.templates.rdf.FacebookStatusFeedEvent;
import com.orange.play.events.types.templates.rdf.UcTelcoAnswer;
import com.orange.play.events.types.templates.rdf.geo_Point;
import com.orange.play.events.types.templates.rdf.UcTelcoCall;
import com.orange.play.events.types.templates.rdf.UcTelcoEsrRecom;
import com.orange.play.events.types.templates.rdf.esrActions.esr_Action;
import com.orange.play.events.types.templates.rdf.esrActions.esr_ShowFriendGeolocation;
import com.orange.play.events.types.templates.rdf.esrActions.esr_SubscribeTo;
import com.orange.play.events.types.templates.rdf.esrActions.esr_UnsubscribeFrom;
import com.orange.play.events.types.templates.rdf.esrActions.uctelco_ComposeMail;
import com.orange.play.events.types.templates.rdf.esrActions.uctelco_OpenFacebook;
import com.orange.play.events.types.templates.rdf.esrActions.uctelco_OpenTwitter;

@SuppressWarnings("serial")
public class RDFTemplatesTest {
	
	private static final URI testURI = DSBSender.PLAY_DSB_URI;
	
	/*
	public static void main(String[] args) throws URISyntaxException {		
		new RDFTemplatesTest();
	}

	public RDFTemplatesTest() {
		performTests();
	}
	
	public void performTests() {
		//sendRecommendationMultipleActions();
		//sendRecommendationOpenFacebook();
		//sendRecommendationOpenTwitter();
		//sendRecommendationComposeMail();
		
		//sendAnswer();
		
		//sendFacebookStatusFeed1();
		
		//sendRecommendation1();
		sendThreeCallsToPlay();
		//sendCompositeEventToPlay();
	}
	*/

	@Test
	public void sendRecommendationMultipleActions() {
		// RDF Events
		List<Event> events = new ArrayList<Event>();
				
		try {
			events.add(new UcTelcoEsrRecom(){{
				Set<esr_Action> esr_Actions = new HashSet<esr_Action>();
				esr_Actions.add(new esr_SubscribeTo() {{
					setEsr_toStream(new URI("http://streams.event-processing.org/ids/TaxiUCGeoLocationAntonio#stream"));
				}});
				esr_Actions.add(new esr_UnsubscribeFrom(){{
					setEsr_fromStream(new URI("http://streams.event-processing.org/ids/TaxiUCGeoLocationRoland#stream"));
				}});
				esr_Actions.add(new uctelco_OpenFacebook(){{
					setUser_id("1234567890");
				}});
				esr_Actions.add(new uctelco_OpenTwitter(){{
					setScreenName("antonio_aversa");
				}});
				esr_Actions.add(new uctelco_ComposeMail(){{
					setUctelco_mailAddress(new URI("mailto:differenziale@gmail.com"));
					setUctelco_mailContent("This is the initial content for the mail");
				}});
				esr_Actions.add(new esr_ShowFriendGeolocation(){{
					setUctelco_phoneNumber("33638611526");
					setGeo_lat(45.0);
					setGeo_long(120.4);
				}});

				setMessage("Open my app");
				setEsr_recommendation(new URI("http://imu.ntua.gr/san/esr/1.1/recommendation/esr1234567890123456789"));
				setUctelco_calleePhoneNumber("33638611526");
				setUctelco_callerPhoneNumber("33638611526"); //33638611117
				setUctelco_ackRequired(false);
				setUctelco_answerRequired(true);
				setUctelco_action(esr_Actions);
				setLocation(new geo_Point(){{
					setGeo_lat(23.0);
					setGeo_long(40.0);
				}});
			}});
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		int i = 0;
		for (Event event : events) {
			DSBSender.sendEvent(testURI, DSBSender.ORANGE_PROXY_S, 
				event, (i++) + "");
		}
	}
	
	@Test
	public void sendRecommendationOpenFacebook() {
		// RDF Events
		List<Event> events = new ArrayList<Event>();
				
		try {
			events.add(new UcTelcoEsrRecom(){{
				Set<esr_Action> esr_Actions = new HashSet<esr_Action>();
				esr_Actions.add(new uctelco_OpenFacebook(){{
					setUser_id("1234567890");
				}});

				setMessage("open facebook: your friend is there!");
				setEsr_recommendation(new URI("http://imu.ntua.gr/san/esr/1.1/recommendation/esr1234567890123456789"));
				setUctelco_calleePhoneNumber("33638611526");
				setUctelco_callerPhoneNumber("33638611526"); //33638611117
				setUctelco_ackRequired(true);
				setUctelco_answerRequired(true);
				setUctelco_action(esr_Actions);
				setLocation(new geo_Point(){{
					setGeo_lat(23.0);
					setGeo_long(40.0);
				}});
			}});
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		int i = 0;
		for (Event event : events) {
			DSBSender.sendEvent(testURI, DSBSender.ORANGE_PROXY_S, 
				event, (i++) + "");
		}
	}
	
	@Test
	public void sendRecommendationOpenTwitter() {
		// RDF Events
		List<Event> events = new ArrayList<Event>();
				
		try {
			events.add(new UcTelcoEsrRecom(){{
				Set<esr_Action> esr_Actions = new HashSet<esr_Action>();
				esr_Actions.add(new uctelco_OpenTwitter(){{
					setScreenName("antonio_aversa");
				}});

				setMessage("open twitter: your friend is there!");
				setEsr_recommendation(new URI("http://imu.ntua.gr/san/esr/1.1/recommendation/esr1234567890123456789"));
				setUctelco_calleePhoneNumber("33638611526");
				setUctelco_callerPhoneNumber("33638611526"); //33638611117
				setUctelco_ackRequired(true);
				setUctelco_answerRequired(true);
				setUctelco_action(esr_Actions);
				setLocation(new geo_Point(){{
					setGeo_lat(23.0);
					setGeo_long(40.0);
				}});
			}});
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		int i = 0;
		for (Event event : events) {
			DSBSender.sendEvent(testURI, DSBSender.ORANGE_PROXY_S, 
				event, (i++) + "");
		}
	}

	@Test
	public void sendRecommendationComposeMail() {
		// RDF Events
		List<Event> events = new ArrayList<Event>();
				
		try {
			events.add(new UcTelcoEsrRecom(){{
				Set<esr_Action> esr_Actions = new HashSet<esr_Action>();
				esr_Actions.add(new uctelco_ComposeMail(){{
					setUctelco_mailAddress(new URI("mailto:differenziale@gmail.com"));
					setUctelco_mailContent("This is the initial content for the mail");
				}});

				setMessage("write a mail to your friend");
				setEsr_recommendation(new URI("http://imu.ntua.gr/san/esr/1.1/recommendation/esr1234567890123456789"));
				setUctelco_calleePhoneNumber("33638611526");
				setUctelco_callerPhoneNumber("33638611526"); //33638611117
				setUctelco_ackRequired(true);
				setUctelco_answerRequired(true);
				setUctelco_action(esr_Actions);
				setLocation(new geo_Point(){{
					setGeo_lat(23.0);
					setGeo_long(40.0);
				}});
			}});
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		int i = 0;
		for (Event event : events) {
			DSBSender.sendEvent(testURI, DSBSender.ORANGE_PROXY_S, 
				event, (i++) + "");
		}
	}
	
	@Test
	public void sendAnswer() {
		// RDF Events
		List<Event> events = new ArrayList<Event>();
				
		events.add(new UcTelcoAnswer(){{
			Set<esr_Action> esr_Actions = new HashSet<esr_Action>();
			esr_Actions.add(new uctelco_OpenFacebook(){{
				setUser_id("1234567890");
			}});

			setMessage("ubi maior minor cessat");
			setLocation(new geo_Point(){{
				setGeo_lat(23.0);
				setGeo_long(40.0);
			}});
		}});
		
		int i = 0;
		for (Event event : events) {
			DSBSender.sendEvent(testURI, DSBSender.ORANGE_PROXY_S, 
				event, (i++) + "");
		}
	}
	
	@Test
	public void sendFacebookStatusFeed1() {
		// RDF Events
		List<Event> events = new ArrayList<Event>();
				
		events.add(new FacebookStatusFeedEvent(){{
			
			setMessage("This is going to be shown");
			setLocation(new geo_Point(){{
				setGeo_lat(23.0);
				setGeo_long(40.0);
			}});
		}});
		
		int i = 0;
		for (Event event : events) {
			DSBSender.sendEvent(testURI, DSBSender.ORANGE_PROXY_S, 
				event, (i++) + "");
		}		
	}
		
	@Test
	public void sendRecommendation1() {
		// RDF Events
		List<Event> events = new ArrayList<Event>();
				
		try {
			events.add(new UcTelcoEsrRecom(){{
				setMessage("This is going to be shown");
				setEsr_recommendation(new URI("http://imu.ntua.gr/san/esr/1.1/recommendation/esr1234567890123456789"));
				setUctelco_calleePhoneNumber("33638611117");
				setUctelco_callerPhoneNumber("33638611117");//33638611526
				Set<esr_Action> esr_Actions = new HashSet<esr_Action>();
				esr_Actions.add(new uctelco_ComposeMail(){{
					setUctelco_mailAddress(new URI("mailto:differenziale@gmail.com"));
					setUctelco_mailSubject("Mail subject generated from recom");
					setUctelco_mailContent("This is the initial content for the mail");
				}});
				setUctelco_action(esr_Actions);
			}});
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		int i = 0;
		for (Event event : events) {
			DSBSender.sendEvent(testURI, DSBSender.ORANGE_PROXY_S, 
				event, (i++) + "");
		}
	}

	@Test
	public void sendCompositeEventToPlay() {
		// RDF Events
		List<Event> events = new ArrayList<Event>();
		
		events.add(new UcTelcoCall(){{
			setEndTime(new Date());
			setStreamName("TaxiUCCall");
			setLocation(new geo_Point() {{
				setGeo_lat(2.0);
				setGeo_long(4.0);
			}});
		}});
		
		int i = 0;
		for (Event event : events) {
			DSBSender.sendEvent(testURI, DSBSender.ORANGE_PROXY_S, 
				event, (i++) + "");
		}
	}

	@Test
	public void sendThreeCallsToPlay() {
		final String callerPhoneNumberPar = "33638611526";
		final String calleePhoneNumberPar = "33638860864";
		final String directionPar = "outgoing";
		final String messagePar = "test";
		
		// RDF Events
		List<Event> events = new ArrayList<Event>();
		
		events.add(new UcTelcoCall(){{
			setMessage(messagePar);
			setUctelco_calleePhoneNumber(calleePhoneNumberPar);
			setUctelco_callerPhoneNumber(callerPhoneNumberPar);
			setUctelco_direction(directionPar);
			setLocation(new geo_Point(){{
				setGeo_lat(1.0);
				setGeo_long(2.0);
			}});
		}});
		
		events.add(new UcTelcoCall(){{
			setMessage(messagePar);
			setUctelco_calleePhoneNumber(calleePhoneNumberPar);
			setUctelco_callerPhoneNumber(callerPhoneNumberPar);
			setUctelco_direction(directionPar);
			setLocation(new geo_Point(){{
				setGeo_lat(1.0);
				setGeo_long(2.0);
			}});
		}});
		
		events.add(new UcTelcoCall(){{
			setMessage(messagePar);
			setUctelco_calleePhoneNumber(calleePhoneNumberPar);
			setUctelco_callerPhoneNumber(callerPhoneNumberPar);
			setUctelco_direction(directionPar);
			setLocation(new geo_Point(){{
				setGeo_lat(1.0);
				setGeo_long(2.0);
			}});
		}});
		
		int i = 0;
		for (Event event : events) {
			DSBSender.sendEvent(testURI, DSBSender.ORANGE_PROXY_S, 
				event, (i++) + "");
		}
	}

}
