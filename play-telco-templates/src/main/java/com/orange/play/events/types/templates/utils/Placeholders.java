package com.orange.play.events.types.templates.utils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Placeholders {
	private static final Random random = new Random();
	private static final Pattern incPattern = Pattern.compile("\\{inc\\+([0-9]+)\\.([0-9]+)\\}");
	private static final Pattern rPattern = Pattern.compile("\\{r0\\-9\\}");
		
	public static String substitutePlaceholders(String input, int state) {
		String current = input;
		
		String previous = current + "different";
		while (!previous.equals(current)) {
			previous = current;
			current = substitutePlaceholderInc(current, state);
		}
		previous = current + "different";
		while (!previous.equals(current)) {
			previous = current;
			current = substitutePlaceholderR(current, state);
		}
		return current;
	}
	
	private static String substitutePlaceholderInc(String input, int state) {
		Matcher matcher = incPattern.matcher(input);
		if (!matcher.find())
			return input;
		int delta = 1;
		int start = 0;
		delta = Integer.parseInt(matcher.group(1));
		start = Integer.parseInt(matcher.group(2));	
		return matcher.replaceFirst(start + delta * state + "");
	}
	
	private static String substitutePlaceholderR(String input, int state) {
		Matcher matcher = rPattern.matcher(input);
		if (!matcher.find())
			return input;
		return matcher.replaceFirst(random.nextInt(9) + "");
	}

}
