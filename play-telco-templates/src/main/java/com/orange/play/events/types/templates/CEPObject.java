package com.orange.play.events.types.templates;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;

import com.orange.play.events.types.templates.utils.CEPUtils;


public abstract class CEPObject extends Stringable {
	private static final String STREAM_NAME_FORMAT = "stream=%s";
	private static final String PROPERTY_FORMAT = ",%s=%s";

	public String getCEPClassName() {
		String className = this.getClass().getSimpleName();
		if (className == null || className.isEmpty())
			className = this.getClass().getSuperclass().getSimpleName();
		return className;
	}
	
	private static String toCEP(CEPObject cepObject) throws Exception {
		StringBuilder result = new StringBuilder();
		result.append(String.format(STREAM_NAME_FORMAT, cepObject.getCEPClassName()));
		
		Class<?> cepObjectClass = cepObject.getClass();
		Class<?> ancestorClass = cepObjectClass;
		do {
			for (Field field : ancestorClass.getDeclaredFields()) {
				if (Modifier.isStatic(field.getModifiers()))
					continue;
				if (Modifier.isFinal(field.getModifiers()))
					continue;
				Class<?> fieldClass = field.getType();
				field.setAccessible(true);
				Object fieldObject = field.get(cepObject);
				String fieldString = "";
				if (fieldObject == null) {
					continue;
				} else if (fieldClass == Date.class) {
					Date fieldConverted = (Date)fieldObject;
					fieldString = CEPUtils.formatDateTime(fieldConverted);
				} else {
					fieldString = fieldObject.toString();
				}
				result.append(String.format(PROPERTY_FORMAT, field.getName(), CEPUtils.escapeField(fieldString)));				
			}
			ancestorClass = ancestorClass.getSuperclass();
		} while (ancestorClass != null);
		
		return result.toString();						
	}
	
	public String toCEP() throws Exception {
		return toCEP(this);
	}
}
