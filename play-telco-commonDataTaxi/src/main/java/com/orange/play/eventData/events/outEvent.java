package com.orange.play.eventData.events;
/*************************************************************************/
/**
 * <p>This class defines an outEvent type definition.</p>
 * <p> for output event<p>
 * <p>
 * outEvent class defines outgoing events from the Federated MW
 * PLAY project - root Event class for outgoing Events 
 * @author Philippe Gibert BIZZ/DIAM/EMB
 */
public class outEvent extends event {
	
	public outEvent() {
		super();
	}


	public outEvent(String timeStamp, String uniqueId, String sequenceNumber) {
		super(timeStamp, uniqueId,  sequenceNumber);
	}
	
}
