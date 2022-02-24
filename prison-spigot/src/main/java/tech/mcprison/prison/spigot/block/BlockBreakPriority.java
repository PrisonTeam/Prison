package tech.mcprison.prison.spigot.block;

/**
 * <p>This BlockBreakPriority sets both the actual priorities of the event, but it also
 * is able disable it, or only handle Prison's blockEvents.  For priorities 
 * LOWEST through HIGHEST, this will allow normal processing of the events.  
 * </p>
 * 
 * <p>DISABLED will prevent the activation of monitoring for the related block events.
 * </p>
 * 
 * <p>MONITOR should be minimal interaction, such as only recording the blocks being 
 * broken and the blocks counted.  It cannot change any blocks, process any drops, 
 * nor can it run any commands through the BlockEvents. 
 * </p>
 * 
 * <p>BLOCKEVENT will run with a priority of HIGHEST, but will process the same as 
 * MONITOR with the addition of running the BlockEvents.  This will NOT break any 
 * blocks, or process any drops.
 * </p>
 *
 */
public enum BlockBreakPriority {
	
	DISABLED,
	BLOCKEVENT,
	
	LOWEST,
	LOW,
	NORMAL,
	HIGH,
	HIGHEST,
	MONITOR
	;
	
	public static BlockBreakPriority fromString( String value ) {
		BlockBreakPriority results = BlockBreakPriority.DISABLED;
		
		if ( value != null ) {
			
			for ( BlockBreakPriority bbPriority : values() ) {
				if ( bbPriority.name().equalsIgnoreCase( value )) {
					results = bbPriority;
					break;
				}
			}
		}
		
		return results;
	}
}