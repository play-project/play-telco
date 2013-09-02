package com.orange.play.gcmAPI;

public class RegistrationId {

	private String gcmRegistrationId;

	public RegistrationId(String gcmRegistrationId) {
		this.gcmRegistrationId = gcmRegistrationId;
	}
	
	public String getGcmRegistrationId() {
		return gcmRegistrationId;
	}

	/**
	 * Used to replace/update the registration ID after Google sends a new
	 * "canonical" registration ID for a device.
	 */
	public void setGcmRegistrationId(String gcmRegistrationId) {
		this.gcmRegistrationId = gcmRegistrationId;
	}
	
	@Override
	public String toString() {
		return gcmRegistrationId;
	}
}
