package tech.mcprison.prison.autofeatures;

import tech.mcprison.prison.autofeatures.PlayerMessaging.MessageType;

public class PlayerMessageData
{
	private final long startTime;
	private long targetEndTime;
	
	private final MessageType messageType;
	private final String message;
	private long ticks = 20L;
	
	private int submitted = 0;
	private int hits = -1;
	
	private int taskId;

	
	public PlayerMessageData( MessageType messageType, String message, long ticks ) {
		super();
		
		this.startTime = System.currentTimeMillis();
		
		this.targetEndTime = this.startTime + ( ticks * 50 );
		
		this.messageType = messageType;
		this.message = message;
		this.ticks = ticks;
		
		this.hits++;
	}

	
	/**
	 * <p>When a new message has been submitted to be displayed to the
	 * player, this function will check to see if the same message is
	 * currently being displayed to the player by checking if the 
	 * current system time is greater than targetEndTime.
	 * </p>
	 * 
	 * <p>If the current time is less than targetEndTime, then that
	 * indicates that the message is active and therefore can be
	 * ignored.
	 * </p>
	 * 
	 * <p>If the current time is greater than the targetEndTime then
	 * that indicates that the message should be displayed. The way 
	 * it is indicated that the message needs to be displayed is 
	 * by setting the jobId to -1, which indicates no job is currently
	 * running.
	 * </p>
	 * 
	 * @param ticks
	 */
	public void addRepeatMessage( long ticks ) {
		this.hits++;
		
		long currentTime = System.currentTimeMillis();
		
		// if currentType is greater than targetEndTime then 
		// needs to submit a new task, which will be indicated
		// by setting the jobId to -1.
		if ( currentTime > targetEndTime ) {
			setTaskId( -1 );
		}
		else {
			// do nothing... the message is already being displayed
		}
	}
	
	public long getStartTime() {
		return startTime;
	}

	public long getTargetEndTime() {
		return targetEndTime;
	}
	public void setTargetEndTime( long targetEndTime ) {
		this.targetEndTime = targetEndTime;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public String getMessage()
	{
		return message;
	}

	public long getTicks() {
		return ticks;
	}
	public void setTicks( long ticks ) {
		this.ticks = ticks;
	}

	public int getSubmitted() {
		return submitted;
	}
	public void setSubmitted( int submitted ) {
		this.submitted = submitted;
	}

	public int getHits() {
		return hits;
	}
	public void setHits( int hits ) {
		this.hits = hits;
	}

	public int getTaskId() {
		return taskId;
	}
	public void setTaskId( int taskId ) {
		this.taskId = taskId;
		
		this.submitted++;
	}
	
}
