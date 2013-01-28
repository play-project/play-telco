package com.orange.play.events.types.templates;

import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class Stringable {
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);					
	}
}
