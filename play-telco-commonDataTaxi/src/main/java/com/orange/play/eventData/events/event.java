package com.orange.play.eventData.events;
/*************************************************************************/
/**
 * <p>This class defines an event type definition.</p>
 * <p> for input and output event<p>
 * <p>
 * eventb  defines several properties, that defines an 
 * event type : 
 * <ul>
 *   <li>A timeStamp</li>
 *   <li>A uniqueId</li>
 *   <li>A sequenceNumber</li>
 * </ul>
 * </p>
 * this class should be abstract - Never  directly instanced
 * PLAY project - root Event class
 * @author Philippe Gibert BIZZ/DIAM/EMB
 */
public class event  {

	/** basic timeStamp for SLA */
	protected String  timeStamp;
	/** uniqueId for SLA */
	protected String  uniqueId;
	/** sequenceNumber for SLA */
	protected String  sequenceNumber;

	/** Constructors */
	public event() {
		super();
	}
	public event(String timeStamp, String uniqueId, String sequenceNumber) {
		super();
		this.timeStamp = timeStamp;
		this.uniqueId = uniqueId;
		this.sequenceNumber = sequenceNumber;
	}

	/** Getters and setters */
	public String getTimestamp() {
		return timeStamp;
	}
	public void setTimestamp(String timestamp) {
		this.timeStamp = timestamp;
	}
	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public String getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
}
