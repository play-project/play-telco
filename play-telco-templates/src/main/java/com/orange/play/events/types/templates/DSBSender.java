package com.orange.play.events.types.templates;


import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import com.orange.play.events.types.templates.rdf.Event;

public final class DSBSender {
	public static final URI LOCALHOST_URI;
	public static final URI PLAY_DSB_URI;
	public static final URI ORANGE_OSVALDO_DSB_URI;
	public static final URI ORANGE_ANTONIO_DSB_URI;
	
	public static final HttpHost ORANGE_PROXY_S = new HttpHost("s-proxy", 3128);
	public static final HttpHost ORANGE_PROXY_P = new HttpHost("p-goodway.rd.francetelecom.fr", 3128);
	
	private static final String SOAP_HEADER_NAME = "SOAPAction";
	private static final String SOAP_HEADER_CONTENT = "Notify";
	private static final String ACCEPT_HEADER_CONTENT = "text/xml, text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2";
	private static final String ACCEPT_HEADER_NAME = "Accept";
	
	private static final String DSB_MESSAGE_CONTENT_TYPE = "text/xml; charset=utf-8";	
	private static final String DSB_EVENT_CONTENT_BODY_WRAPPER;
	
	static {
		// URI resolution
		URI tempLocalhostURI = null;
		URI tempPlayURI = null;
		URI tempOrangeOsvaldoURI = null;
		URI tempOrangeAntonioURI = null;
		try {
			tempLocalhostURI = new URI("http://127.0.0.1:7084/petals/services/NotificationConsumerPortService");
			tempPlayURI = new URI("http://46.105.181.221:8084/petals/services/NotificationConsumerPortService");
			tempOrangeOsvaldoURI = new URI("http://161.105.138.99:8080/playOrangeWS/services/NotificationConsumerService");
			tempOrangeAntonioURI = new URI("http://161.105.138.98:8089/playOrangeWS/services/NotificationConsumerService");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		LOCALHOST_URI = tempLocalhostURI;
		PLAY_DSB_URI = tempPlayURI;
		ORANGE_OSVALDO_DSB_URI = tempOrangeOsvaldoURI;
		ORANGE_ANTONIO_DSB_URI = tempOrangeAntonioURI;
		
		// Body wrapper
		DSB_EVENT_CONTENT_BODY_WRAPPER =
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<soap:Envelope \n" + 
				"\txmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" \n" + 
				"\txmlns:xsd=\"http://www.w3.org/1999/XMLSchema\" \n" +
				"\txmlns:xsi=\"http://www.w3.org/1999/XMLSchema-instance\" >\n" +
				"\t<soap:Header />\n" + 
				"\t<soap:Body>\n" + 
					"\t\t<wsnt:Notify xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\">\n" + 
						"\t\t\t<wsnt:NotificationMessage>\n" + 
							"\t\t\t\t<wsnt:Topic Dialect=\"http://www.w3.org/TR/1999/REC-xpath-19991116\" xmlns:s=\"http://streams.event-processing.org/ids/\">\n" +
								"\t\t\t\t\t%s\n" +
							"\t\t\t\t</wsnt:Topic>\n" + 
							
							"\t\t\t\t<wsnt:Message>\n" + 
								"\t\t\t\t\t<mt:nativeMessage xmlns:mt=\"http://www.event-processing.org/wsn/msgtype/\" mt:syntax=\"application/x-trig\">\n" +
									"%s\n" +
								"\t\t\t\t\t</mt:nativeMessage>\n" + 
							"\t\t\t\t</wsnt:Message>\n" + 
						"\t\t\t</wsnt:NotificationMessage>\n" + 
					"\t\t</wsnt:Notify>\n" +
				"\t</soap:Body>\n" + 
			"</soap:Envelope>\n";		
	}
	
	public static Integer send(URI dsbURI, HttpHost httpProxy, String eventContent, URI stream) {
		// Escape Trig, unescape quote mark
		String rdfBody = StringEscapeUtils.escapeXml(eventContent);
		rdfBody = rdfBody.replace("&quot;", "\"");
		
		// DSB Wrapper built manually
		String httpBody = String.format(DSB_EVENT_CONTENT_BODY_WRAPPER, stream.toString(), rdfBody);
		
		// Uncomment the following line to show the HTTP Body Request
		System.out.println(httpBody);
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		if (httpProxy != null)
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, httpProxy);
		try {
			HttpPost httpPost = new HttpPost(dsbURI);
			httpPost.setHeader(HTTP.CONTENT_TYPE, DSB_MESSAGE_CONTENT_TYPE);
			httpPost.setHeader(SOAP_HEADER_NAME, SOAP_HEADER_CONTENT);
			httpPost.setHeader(ACCEPT_HEADER_NAME, ACCEPT_HEADER_CONTENT);
			StringEntity stringEntity = new StringEntity(httpBody);
			httpPost.setEntity(stringEntity); 
			HttpResponse httpResponse = httpClient.execute(httpPost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			//Uncomment the following 2 lines to show the HTTP Body Response
			System.out.println(httpResponse);
			httpResponse.getEntity().writeTo(System.out);
			return statusCode;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Integer sendRDFObject(URI dsbURI, HttpHost httpProxy, RDFObject object, URI objectURI, URI stream) {
		return send(dsbURI, httpProxy, object.toRDF(objectURI), stream);
	}
	
	public static Integer sendEvent(URI dsbURI, HttpHost httpProxy, Event event, String eventId) {
		return send(dsbURI, httpProxy, event.toRDF(eventId), event.getStreamWithoutFragment());
	}
}
