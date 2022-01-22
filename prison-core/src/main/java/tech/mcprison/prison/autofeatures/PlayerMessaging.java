package tech.mcprison.prison.autofeatures;

import java.util.TreeMap;

public class PlayerMessaging
{

	private final TreeMap<MessageType, TreeMap<String, PlayerMessageData>> messages;
	
	
	public enum MessageType {
		title,
		actionBar;
	}
	
	public PlayerMessaging() {
		
		this.messages = new TreeMap<>();
		
		// Preload all the message types and setup their TreeMaps
		for ( MessageType mType : MessageType.values() ) {
			
			getMessages().put( mType, new TreeMap<>() );
		}
		
	}
	
	/**
	 * <p>This function adds a message to the player's PlayerCached object.  It uses the 
	 * default duration of 40 ticks (2 seconds).
	 * </p>
	 * 
	 * <p>If the same message is active, then the message will not be submitted.
	 * </p>
	 * 
	 * @param messageType
	 * @param message
	 * @return
	 */
	public PlayerMessageData addMessage( MessageType messageType, String message ) {
		
		return addMessage( messageType, message, 40L );
	}
	
	public PlayerMessageData addMessage( MessageType messageType, String message, long ticks ) {
		PlayerMessageData pmData = null;
		
		TreeMap<String, PlayerMessageData> messagesOfType = getMessages().get( messageType );
		
		if ( messagesOfType.containsKey( message ) ) {
			pmData = messagesOfType.get( message );
			
			// A message that was sent to the player before is requested to be sent again:
			pmData.addRepeatMessage( ticks );
		}
		else {
			
			// This message has not be sent to the player before:
			pmData = new PlayerMessageData( messageType, message, ticks );
			messagesOfType.put( message, pmData );
		}
		
		return pmData;
	}

	public TreeMap<MessageType, TreeMap<String, PlayerMessageData>> getMessages() {
		return messages;
	}
	
}
