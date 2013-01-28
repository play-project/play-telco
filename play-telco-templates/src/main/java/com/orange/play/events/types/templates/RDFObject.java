package com.orange.play.events.types.templates;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.JsonObject;
import com.orange.play.events.types.templates.utils.RDFUtils;


public abstract class RDFObject extends Stringable implements Serializable {
	private static final long serialVersionUID = 1115143872061081608L;

	public final static String RDF_PREFIX_FORMAT = "@prefix %s: %s .\n";
	
	public final static String RDF_COMMENT = "#%s\n";
	public final static String RDF_PROPERTY_TERMINATION = ";\n";
	public final static String RDF_INSTANCE_TERMINATION = ".\n";
	public final static String RDF_GRAPH_TERMINATION = "}\n";
	
	public final static String RDF_GRAPH_NAME_FORMAT = "%s {\n";
	public final static String RDF_INSTANCE_ID_FORMAT = "%s\n";
	public final static String RDF_TYPE_FORMAT = "a %s " + RDF_PROPERTY_TERMINATION;
	public final static String RDF_PROPERTY_WITH_TYPE_FORMAT = "%s \"%s\"^^%s " + RDF_PROPERTY_TERMINATION;
	public final static String RDF_PROPERTY_URL_FORMAT="%s %s " + RDF_PROPERTY_TERMINATION;
	public final static String RDF_PROPERTY_URL_SET_FORMAT="%s %s " + RDF_PROPERTY_TERMINATION;
	
	protected final Map<String, URI> prefixes = new HashMap<String, URI>();
	protected final Map<String, Integer> namespaces = new HashMap<String, Integer>();
	protected final Map<URI, RDFObject> subObjects = new HashMap<URI, RDFObject>();
	protected RDFObject parent = null;
	
 	protected RDFObject() {
	}
	
 	@Override
 	public String toString() {
 		return ToStringBuilder.reflectionToString(this); 	
 	}
 	
	protected boolean isAnonimous() {
		return false;
	}
	
	protected static String getRDFClassNameFor(Class<?> clazz) {
		String className = clazz.getSimpleName();
		if (className == null || className.isEmpty())
			className = clazz.getSuperclass().getSimpleName();
		className = className.replace("_", ":");
		if (!className.contains(":"))
			className = ":" + className;
		return className;
	}
	
	protected static URI getRdf_typeFor(Class<?> clazz, Map<String, URI> prefixes) {
		return RDFUtils.resolveURI(getRDFClassNameFor(clazz), prefixes);
	}
	
	public String getRDFClassName() {
		return getRDFClassNameFor(this.getClass());
	}
	
	public URI getRdf_type() {
		return getRdf_typeFor(this.getClass(), prefixes);
	}
	
	public Map<String, URI> getPrefixes() {
		return prefixes;
	}
	
	public void loadFromJSON(JsonObject jsonObject) throws IllegalArgumentException {	
		// If not anonimous => just check rdf_type correctness
		if (!isAnonimous()) {
			URI rdf_type = RDFUtils.getAndCheckRdf_typeFromJson(jsonObject, prefixes);
			if (!rdf_type.equals(getRdf_type()))
				throw new IllegalArgumentException("rdf_type mismatch");
		}
	}
	
	public abstract void getRDFProperties(StringBuilder result);
		
	public String toRDF(URI objectURI) {
		return toRDF(objectURI, true, false);
	}
	
	public String toRDF(URI objectURI, boolean trig, boolean recursiveTrig) {
		StringBuilder result = new StringBuilder();

		//If the root, print prefixes
		if (parent == null) {
			for (Map.Entry<String, URI> prefix: prefixes.entrySet())
				result.append(String.format(RDF_PREFIX_FORMAT, prefix.getKey(), RDFUtils.formatURI(prefix.getValue(), null)));		
		}
		
		addRDFComment(result, " mainObject");
		
		// RDF Graph starting
		if (trig) {
			URI graphURI;
			try {
				graphURI = new URI(objectURI.getScheme(), objectURI.getSchemeSpecificPart(), null);
				result.append(String.format(RDF_GRAPH_NAME_FORMAT, RDFUtils.formatURI(graphURI, prefixes)));
			} catch (URISyntaxException e) {
				throw new IllegalStateException(e);
			}
		}
			
		// RDF Instance starting (URI of the RDF object)
		result.append(String.format(RDF_INSTANCE_ID_FORMAT, RDFUtils.formatURI(objectURI, prefixes)));
		
		int beforeLength = result.length();
		
		// RDF Type Property (a :RDFClassName)
		if (!isAnonimous())
			result.append(String.format(RDF_TYPE_FORMAT, this.getRDFClassName()));

		// Other Properties
		this.getRDFProperties(result);
		int afterLength = result.length();
		if (afterLength > beforeLength) {
			int lastPropertyTermination = result.lastIndexOf(RDF_PROPERTY_TERMINATION);
			result.replace(lastPropertyTermination, lastPropertyTermination + RDF_PROPERTY_TERMINATION.length(), "");
		}
		
		// RDF termination (point)
		result.append(RDF_INSTANCE_TERMINATION);
		
		if (!subObjects.isEmpty()) {
			addRDFEmptyLine(result);	
			
			addRDFComment(result, " subObjects");
			for (Map.Entry<URI, RDFObject> subObject : subObjects.entrySet()) {
				result.append(subObject.getValue().toRDF(subObject.getKey(), recursiveTrig, recursiveTrig));
				addRDFEmptyLine(result);
			}
		}
		
		// RDF graph termination (bracket)
		if (trig)
			result.append(RDF_GRAPH_TERMINATION);
		
		return result.toString();
	}

	public static void addRDFEmptyLine(StringBuilder result) {
		result.append("\n");
	}
	
	public static void addRDFComment(StringBuilder result, String comment) {
		if (comment == null)
			return;
		result.append(String.format(RDF_COMMENT, comment));
	}
	
	public void addRDFPropertyObject(StringBuilder result, String name, String namespace, RDFObject value) {
		if (value == null)
			return;
		
		value.parent = this;
		
		RDFObject root = this;
		while (root.parent != null)
			root = root.parent;
		Map<String, Integer> rootNamespaces = root.namespaces;
		
		// Generate subObjectURI 
		Integer currentIndex = rootNamespaces.get(namespace);
		if (currentIndex == null)
			currentIndex = 0;
		else
			currentIndex++;
		rootNamespaces.put(namespace, currentIndex);
		URI subObjectURI;
		try {
			subObjectURI = new URI(String.format("%s://%d", namespace, currentIndex));
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
		// Add complex property
		addRDFPropertyURI(result, name, subObjectURI);
		subObjects.put(subObjectURI, value);
	}
	
	public void addRDFPropertyRDFObjectSet(StringBuilder result, String name, String namespace, Set<? extends RDFObject> value) {
		for (RDFObject rdfObject : value) {
			addRDFPropertyObject(result, name, namespace, rdfObject);
		}
	}
	
	public void addRDFPropertyRDFPrimitive(StringBuilder result, String name, Object value) {
		if (value instanceof String)
			addRDFPropertyString(result, name, (String)value);
		if (value instanceof Long)
			addRDFPropertyLong(result, name, (Long)value);
		if (value instanceof Integer)
			addRDFPropertyInt(result, name, (Integer)value);
		if (value instanceof Short)
			addRDFPropertyShort(result, name, (Short)value);
		if (value instanceof Byte)
			addRDFPropertyByte(result, name, (Byte)value);
		if (value instanceof Double)
			addRDFPropertyDouble(result, name, (Double)value);
		if (value instanceof Float)
			addRDFPropertyFloat(result, name, (Float)value);
		if (value instanceof Date)
			addRDFPropertyDateTime(result, name, (Date)value);
	}
	
	public void addRDFPropertyRDFPrimitiveSet(StringBuilder result, String name, Set<? extends Object> values) {
		if (values == null)
			return;
		for (Object value : values) {
			addRDFPropertyRDFPrimitive(result, name, value);
		}
	}
	
	private static void addRDFProperty(StringBuilder result, String name, String value, String type) {
		if (value == null || value.isEmpty())
			return;
		result.append(String.format(RDF_PROPERTY_WITH_TYPE_FORMAT, name, value, type));
	}
	
	public static void addRDFPropertyString(StringBuilder result, String name, String value) {
		if (value == null || value.isEmpty())
			return;
		addRDFProperty(result, name, value, "xsd:string");
	}
	
	public static void addRDFPropertyLong(StringBuilder result, String name, Long value) {
		if (value == null)
			return;
		addRDFProperty(result, name, value + "", "xsd:long");
	}

	public static void addRDFPropertyInt(StringBuilder result, String name, Integer value) {
		if (value == null)
			return;
		addRDFProperty(result, name, value + "", "xsd:int");
	}
	
	public static void addRDFPropertyShort(StringBuilder result, String name, Short value) {
		if (value == null)
			return;
		addRDFProperty(result, name, value + "", "xsd:short");
	}
	
	public static void addRDFPropertyByte(StringBuilder result, String name, Byte value) {
		if (value == null)
			return;
		addRDFProperty(result, name, value + "", "xsd:byte");
	}
	
	public static void addRDFPropertyDouble(StringBuilder result, String name, Double value) {
		if (value == null)
			return;
		addRDFProperty(result, name, value + "", "xsd:double");
	}
	
	public static void addRDFPropertyFloat(StringBuilder result, String name, Float value) {
		if (value == null)
			return;
		addRDFProperty(result, name, value + "", "xsd:float");
	}
	
	public static void addRDFPropertyBoolean(StringBuilder result, String name, Boolean value) {
		if (value == null)
			return;
		addRDFProperty(result, name, Boolean.toString(value), "xsd:boolean");
	}
	
	public static void addRDFPropertyDateTime(StringBuilder result, String name, Date value) {
		if (value == null)
			return;
		addRDFProperty(result, name, RDFUtils.formatDateTime(value), "xsd:dateTime");
	}	

	public static void addRDFPropertyDate(StringBuilder result, String name, Date value) {
		if (value == null)
			return;
		addRDFProperty(result, name, RDFUtils.formatDate(value), "xsd:date");
	}	

	public static void addRDFPropertyTime(StringBuilder result, String name, Date value) {
		if (value == null)
			return;
		addRDFProperty(result, name, RDFUtils.formatTime(value), "xsd:time");
	}	
	
	public void addRDFPropertyURI(StringBuilder result, String name, URI value) {
		if (value == null)
			return;
		result.append(String.format(RDF_PROPERTY_URL_FORMAT, name, RDFUtils.formatURI(value, prefixes)));
	}
	
}
