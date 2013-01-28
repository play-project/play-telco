package com.orange.play.eventData.events.outEvents;

import org.apache.commons.lang3.StringEscapeUtils2;

import com.orange.play.eventData.events.outEvent;

// @author Osvaldo Cocucci

public class RecommendationEvent extends outEvent {

	String stream;
	String callee;
	String caller;
	String recommendation;
	String url;

	public RecommendationEvent() {
	}

	public RecommendationEvent(String timeStamp, String uniqueId,
			String sequenceNumber) {
		super(timeStamp, uniqueId, sequenceNumber);
	}

	public RecommendationEvent(String timeStamp, String uniqueId,
			String sequenceNumber, String stream,
			String callee, String caller, String id, String recommendation,
			String url) {
		super(timeStamp, uniqueId, sequenceNumber);
		this.stream = stream;
		this.callee = callee;
		this.caller = caller;
		this.recommendation = recommendation;
		this.url = url;
	}


	public String getStream() {
		return stream;
	}

	public void setStream(String stream) {
		this.stream = stream;
	}

	public String getCallee() {
		return callee;
	}

	public void setCallee(String callee) {
		this.callee = callee;
	}

	public String getCaller() {
		return caller;
	}

	public void setCaller(String caller) {
		this.caller = caller;
	}



	public String getRecommendation() {
		return recommendation;
	}

	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String toRDF(String AppID) {
		String pattern05 = AppID;
		String pattern07 = "RecommendationEvent";
		String pattern09 = "http://streams.event-processing.org/ids/RecommendationEvent#stream";

		String Header = "<mt:nativeMessage xmlns:mt=\"http://www.event-processing.org/wsn/msgtype/\" mt:syntax=\"application/x-trig\">\n";
		String Footer = "</mt:nativeMessage>\n";
		String str1 = "@prefix :        <http://events.event-processing.org/types/> .\n"
				+ "@prefix e:       <http://events.event-processing.org/ids/> .\n"
				+ "@prefix dsb:     <http://www.petalslink.org/dsb/topicsns/> .\n"
				+ "@prefix xsd:     <http://www.w3.org/2001/XMLSchema#> .\n"
				+ "@prefix uctelco: <http://events.event-processing.org/uc/telco/> .\n"
				+ "@prefix geo:     <http://www.w3.org/2003/01/geo/wgs84_pos#> .\n"
				+ "@prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#> ."
				+ "@prefix foaf:    <http://xmlns.com/foaf/0.1/> ."
				+ "@prefix rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ."
				+ "e:#3# {\n"
				+ "e:#3##event uctelco:sequenceNumber \"#4#\"^^xsd:integer ;\n"
				+ "uctelco:uniqueId \"#6#\" ;\n"
				+ "uctelco:stream \"#11#\" ;\n"
				+ "uctelco:callee \"#12#\" ;\n"
				+ "uctelco:caller \"#13#\" ;\n"
				+ "uctelco:recommendation \"#14#\" ;\n"
				+ "uctelco:url \"#15#\" ;\n"
				+ "# but also some other data according to event format\n"
				+ "a :#7# ;\n"
				+ ":endTime \"#8#\"^^xsd:dateTime ;\n"
				+ ":source <http://sources.event-processing.org/ids/#5##source> ;\n"
				+ ":stream <#9#> . \n" + "}\n";
		String avaTemplate = str1;
		int indexSep = this.uniqueId.indexOf(':');
		// common part
		// System.out.print("avaTemplate\n"+avaTemplate);

		avaTemplate = avaTemplate.replaceFirst("#3#",
				this.uniqueId.substring(indexSep + 1));
		avaTemplate = avaTemplate.replaceFirst("#3#",
				this.uniqueId.substring(indexSep + 1));
		avaTemplate = avaTemplate.replaceFirst("#4#", this.sequenceNumber);
		avaTemplate = avaTemplate.replaceFirst("#5#", pattern05);
		avaTemplate = avaTemplate.replaceFirst("#6#", this.uniqueId);
		avaTemplate = avaTemplate.replaceFirst("#7#", pattern07);
		avaTemplate = avaTemplate.replaceFirst("#8#",
				this.timeStamp.replace(' ', 'T'));
		avaTemplate = avaTemplate.replaceFirst("#9#", pattern09);
		// local part of the event

		avaTemplate = avaTemplate.replaceFirst("#11#", this.stream);
		avaTemplate = avaTemplate.replaceFirst("#12#", this.callee);
		avaTemplate = avaTemplate.replaceFirst("#13#", this.caller);
		avaTemplate = avaTemplate.replaceFirst("#14#", this.recommendation);
		avaTemplate = avaTemplate.replaceFirst("#15#", this.url);
		if (AppID == "Android")
			return StringEscapeUtils2.escapeXml(avaTemplate);
		else
			return Header + StringEscapeUtils2.escapeXml(avaTemplate) + Footer;
	}
}
