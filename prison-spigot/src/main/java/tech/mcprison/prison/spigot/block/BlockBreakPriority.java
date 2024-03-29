package tech.mcprison.prison.spigot.block;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventPriority;

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
 * <p>BLOCKEVENT will run with a priority of MONItoR, but will process the same as 
 * MONITOR with the addition of running the BlockEvents and enabling sellall on
 * full inventory.  This will NOT break any 
 * blocks, or process any drops.
 * </p>
 *
 */
public enum BlockBreakPriority {
	
	DISABLED( null ),
	
	LOWEST( EventPriority.LOWEST ),
	LOW( EventPriority.LOW ),
	NORMAL( EventPriority.NORMAL ),
	HIGH( EventPriority.HIGH ),
	HIGHEST( EventPriority.HIGHEST ),
	
	BLOCKEVENTS( EventPriority.MONITOR ),
	MONITOR( EventPriority.MONITOR ),
	
	ACCESS( EventPriority.LOWEST ),
	ACCESSBLOCKEVENTS( EventPriority.MONITOR, ACCESS, BLOCKEVENTS ),
	ACCESSMONITOR( EventPriority.MONITOR, ACCESS, MONITOR ),
	;
	
	private final EventPriority bukkitEventPriority;
	private final List<BlockBreakPriority> componentPriorities;
	
	private BlockBreakPriority( EventPriority bukkitEventPriority ) {
		
		this.bukkitEventPriority = bukkitEventPriority;

		this.componentPriorities = new ArrayList<>();
	}
	
	private BlockBreakPriority( EventPriority bukkitEventPriority, BlockBreakPriority... components ) {
		this( bukkitEventPriority );

		for (BlockBreakPriority bbPriority : components) {
			this.componentPriorities.add( bbPriority );
		}
	}
	
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

	public boolean isAccess() {
		return this == ACCESS ||
			   this == ACCESSBLOCKEVENTS ||
			   this == ACCESSMONITOR
			   ;
	}
	
	public boolean isMonitor() {
		return this == MONITOR || 
			   this == BLOCKEVENTS ||
			   this == ACCESSBLOCKEVENTS ||
			   this == ACCESSMONITOR
			   ;
	}

	public boolean isDisabled() {
		return this == DISABLED;
	}
	
	public boolean isComponentCompound() {
		return getComponentPriorities().size() > 0
			   ;
	}
	
	public EventPriority getBukkitEventPriority() {
		return bukkitEventPriority;
	}

	public List<BlockBreakPriority> getComponentPriorities() {
		return componentPriorities;
	}
}
