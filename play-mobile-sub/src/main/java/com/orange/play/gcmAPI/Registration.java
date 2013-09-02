package com.orange.play.gcmAPI;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Registration {

    /** Maps for efficient lookup and reverse lookup of phone number <-> regId (1:1 mappings) */
    private final Map<String, RegistrationId> phoneNumberToRegistrationId = new HashMap<String, RegistrationId>();
    /** Maps for efficient lookup and reverse lookup of phone number <-> regId (1:1 mappings) */
    private final Map<RegistrationId, String> registrationIdToPhoneNumber = new HashMap<RegistrationId, String>();

    /** Maps for efficient lookup and reverse lookup of topics <-> regIds (n:m mappings) */
    private final Map<String, Set<RegistrationId>> topicToRegistrationIds = new HashMap<String, Set<RegistrationId>>();
    /** Maps for efficient lookup and reverse lookup of topics <-> regIds (n:m mappings) */
    private final Map<RegistrationId, Set<String>> registrationIdToTopics = new HashMap<RegistrationId, Set<String>>();

    /** Overview of all existing regIds */
    private final Map<String, RegistrationId> registrationIds = new HashMap<String, RegistrationId>();

    private static Registration instance;
    
    // Private constructor prevents instantiation from other classes
    private Registration() {
    }
    
    public static Registration getInstance() {
    	if (instance == null) {
    		instance = new Registration();
    	}
    	return instance;
    }
    
    public synchronized void addPhoneNumberMapping(String registrationId, String phoneNumber) {
    	RegistrationId regId = registrationIds.get(registrationId);
    	if (regId == null) {
    		regId = new RegistrationId(registrationId);
    		registrationIds.put(registrationId, regId);
    	}
    	// Maintain lookup map
    	phoneNumberToRegistrationId.put(phoneNumber, regId);
    	// Maintain reverse lookup map
    	registrationIdToPhoneNumber.put(regId, phoneNumber);
    }

    public synchronized void addTopicMapping(String registrationId, String topicUrl) {
    	RegistrationId regId = registrationIds.get(registrationId);
    	if (regId == null) {
    		regId = new RegistrationId(registrationId);
    		registrationIds.put(registrationId, regId);
    	}
    	
    	// Maintain lookup map
    	if (topicToRegistrationIds.containsKey(topicUrl)) {
    		topicToRegistrationIds.get(topicUrl).add(regId);
    	}
    	else {
    		Set<RegistrationId> set = new HashSet<RegistrationId>();
    		set.add(regId);
    		topicToRegistrationIds.put(topicUrl, set);
    	}
    	
    	// Maintain reverse lookup map
    	if (registrationIdToTopics.containsKey(regId)) {
    		registrationIdToTopics.get(regId).add(topicUrl);
    	}
    	else {
    		Set<String> set = new HashSet<String>();
    		set.add(topicUrl);
    		registrationIdToTopics.put(regId, set);
    	}
    }
    
    public synchronized void updateRegistrationId(String oldId, String newId) {
    	RegistrationId regIdToUpdate = registrationIds.remove(oldId);
    	regIdToUpdate.setGcmRegistrationId(newId);
    	registrationIds.put(newId, regIdToUpdate);
    }
    
    public synchronized void removeRegistrationId(String oldId) {
    	if (registrationIds.containsKey(oldId)) {
    		RegistrationId regId = registrationIds.remove(oldId);
    		
        	// Maintain phone lookup maps
    		if (registrationIdToPhoneNumber.get(regId) != null) {
    			// Lookup
    			String phoneNumber = registrationIdToPhoneNumber.remove(regId);
    			// Reverse
    			phoneNumberToRegistrationId.remove(phoneNumber);
    		}

        	// Maintain topic lookup maps
    		if (registrationIdToTopics.get(regId) != null) {
    			// Lookup
    			Set<String> topics = registrationIdToTopics.remove(regId);
    			// Reverse
    			for (String topic : topics) {
    				topicToRegistrationIds.remove(topic);
    			}
    		}
    	}
    }

    public synchronized String getRegistrationIdForPhoneNumber(String phoneNumber) {
    	return phoneNumberToRegistrationId.get(phoneNumber).getGcmRegistrationId();
    }
    
    public synchronized Set<String> getRegistrationIdsForTopic(String topicUrl) {
    	Set<String> results = new HashSet<String>();
    	// Transform the Set<RegistrationId> to Set<String>
    	for (RegistrationId regId : topicToRegistrationIds.get(topicUrl)) {
    		results.add(regId.getGcmRegistrationId());
    	}
    	return results;
    }
    
}
