package com.orange.play.eventData.events;
/*************************************************************************/
/**
 * <p>This class defines an inEvent type definition.</p>
 * <p> for input  event<p>
 * <p>
 * inEvent class defines incoming events to the Federated MW
 *
 * PLAY project - root Event class for incoming Events
 * @author Philippe Gibert BIZZ/DIAM/EMB
 */
public class inEvent extends event {
	
	public inEvent() {
		super();
	}
	
	public inEvent(String timeStamp, String uniqueId, String sequenceNumber) {
		super(timeStamp, uniqueId,  sequenceNumber);
	}
	
	
}
