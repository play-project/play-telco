package com.orange.play.events.types.templates.exchange;

import java.io.Serializable;

public class FacebookExchange implements Serializable {
	private static final long serialVersionUID = 2771558863658768117L;
	
	protected final String postId;
	protected final String fromId;
	protected final String fromName;
	protected final String toId;
	protected final String toName;
	protected final String message;
	protected final String picture;
	protected final String link;
	protected final String name;
	protected final String caption;
	protected final String description;
	protected final String icon;
	protected final String createdTime;
	protected final String updatedTime;
	
	public FacebookExchange(String postId, String fromId, String fromName, String toId, String toName, String message, String picture,
			String link, String name, String caption, String description, String icon, String createdTime, String updatedTime) {
		super();
		this.postId = postId;
		this.fromId = fromId;
		this.fromName = fromName;
		this.toId = toId;
		this.toName = toName;
		this.message = message;
		this.picture = picture;
		this.link = link;
		this.name = name;
		this.caption = caption;
		this.description = description;
		this.icon = icon;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
	}

	public String getPostId() {
		return postId;
	}

	public String getFromId() {
		return fromId;
	}

	public String getFromName() {
		return fromName;
	}

	public String getToId() {
		return toId;
	}

	public String getToName() {
		return toName;
	}

	public String getMessage() {
		return message;
	}

	public String getPicture() {
		return picture;
	}

	public String getLink() {
		return link;
	}

	public String getName() {
		return name;
	}

	public String getCaption() {
		return caption;
	}

	public String getDescription() {
		return description;
	}

	public String getIcon() {
		return icon;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public String getUpdatedTime() {
		return updatedTime;
	}
}
