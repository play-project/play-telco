package com.orange.play.events.types.templates.rdf;

import java.net.URI;

public class TwitterEvent extends Event {
	private static final long serialVersionUID = -2768509974051846067L;
	protected Integer uctelco_sequenceNumber = null;
	protected URI uctelco_uniqueId = null;
	protected String uctelco_userType = null;
	protected String uctelco_phoneNumber = null;
	
	protected String screenName = null;
	protected Integer followersCount = null;
	protected Integer friendsCount = null;
	protected String hashTag = null;
	protected Boolean isRetweet = null;
	protected String twitterName = null;
	protected String userMention = null;
	
	protected String sioc_links_to = null;
	protected String sioc_content = null;
	
	public TwitterEvent() {
	}
	
	@Override
	public void getRDFProperties(StringBuilder result) {
		super.getRDFProperties(result);
		
		addRDFPropertyString(result, ":screenName", this.screenName);
		addRDFPropertyInt(result, ":followersCount", this.followersCount);
		addRDFPropertyInt(result, ":friendsCount", this.friendsCount);
		addRDFPropertyString(result, ":hashTag", this.hashTag);
		addRDFPropertyBoolean(result, ":isRetweet", this.isRetweet);
		addRDFPropertyString(result, ":twitterName", this.twitterName);
		addRDFPropertyString(result, ":userMention", this.userMention);

		addRDFPropertyInt(result, "uctelco:sequenceNumber", this.uctelco_sequenceNumber);
		addRDFPropertyURI(result, "uctelco:uniqueId", this.uctelco_uniqueId);
		addRDFPropertyString(result, "uctelco:userType", this.uctelco_userType);
		addRDFPropertyString(result, "uctelco:phoneNumber", this.uctelco_phoneNumber);
		
		addRDFPropertyString(result, "sioc:links_to", this.sioc_links_to);
		addRDFPropertyString(result, "sioc:content", this.sioc_content);
	}

	public Integer getUctelco_sequenceNumber() {
		return uctelco_sequenceNumber;
	}

	public void setUctelco_sequenceNumber(Integer uctelco_sequenceNumber) {
		this.uctelco_sequenceNumber = uctelco_sequenceNumber;
	}

	public URI getUctelco_uniqueId() {
		return uctelco_uniqueId;
	}

	public void setUctelco_uniqueId(URI uctelco_uniqueId) {
		this.uctelco_uniqueId = uctelco_uniqueId;
	}

	public String getUctelco_userType() {
		return uctelco_userType;
	}

	public void setUctelco_userType(String uctelco_userType) {
		this.uctelco_userType = uctelco_userType;
	}

	public String getUctelco_phoneNumber() {
		return uctelco_phoneNumber;
	}

	public void setUctelco_phoneNumber(String uctelco_phoneNumber) {
		this.uctelco_phoneNumber = uctelco_phoneNumber;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenNumber) {
		this.screenName = screenNumber;
	}

	public Integer getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(Integer followersCount) {
		this.followersCount = followersCount;
	}

	public Integer getFriendsCount() {
		return friendsCount;
	}

	public void setFriendsCount(Integer friendsCount) {
		this.friendsCount = friendsCount;
	}

	public String getHashTag() {
		return hashTag;
	}

	public void setHashTag(String hashTag) {
		this.hashTag = hashTag;
	}

	public Boolean getIsRetweet() {
		return isRetweet;
	}

	public void setIsRetweet(Boolean isRetweet) {
		this.isRetweet = isRetweet;
	}

	public String getTwitterName() {
		return twitterName;
	}

	public void setTwitterName(String twitterName) {
		this.twitterName = twitterName;
	}

	public String getUserMention() {
		return userMention;
	}

	public void setUserMention(String userMention) {
		this.userMention = userMention;
	}

	public String getSioc_links_to() {
		return sioc_links_to;
	}

	public void setSioc_links_to(String sioc_links_to) {
		this.sioc_links_to = sioc_links_to;
	}

	public String getSioc_content() {
		return sioc_content;
	}

	public void setSioc_content(String sioc_content) {
		this.sioc_content = sioc_content;
	}
}
