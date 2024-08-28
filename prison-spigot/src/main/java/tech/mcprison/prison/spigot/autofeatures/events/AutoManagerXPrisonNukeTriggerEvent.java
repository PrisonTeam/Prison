package tech.mcprison.prison.spigot.autofeatures.events;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.PluginManager;

import dev.drawethree.xprison.enchants.api.events.NukeTriggerEvent;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.autofeatures.AutoFeaturesWrapper;
import tech.mcprison.prison.mines.features.MineBlockEvent.BlockEventType;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.api.PrisonMinesBlockBreakEvent;
import tech.mcprison.prison.spigot.autofeatures.AutoManagerFeatures;
import tech.mcprison.prison.spigot.block.BlockBreakPriority;

public class AutoManagerXPrisonNukeTriggerEvent 	
		extends AutoManagerFeatures
		implements PrisonEventManager
{

	
//	private ExplosionTriggerEvent ete;
//	private LayerTriggerEvent lte;
//	private NukeTriggerEvent nte;
	
	//dev.drawethree.xprison.enchants.api.events.NukeTriggerEvent;
	
	
	
	private BlockBreakPriority bbPriority;
	
	public AutoManagerXPrisonNukeTriggerEvent() {
		super();
	}
	
	public AutoManagerXPrisonNukeTriggerEvent( BlockBreakPriority bbPriority ) {
		super();
		
		this.bbPriority = bbPriority;
	}
	
	
	public BlockBreakPriority getBbPriority() {
		return bbPriority;
	}
	public void setBbPriority( BlockBreakPriority bbPriority ) {
		this.bbPriority = bbPriority;
	}
	
	@Override
	public void registerEvents() {
		
		if ( AutoFeaturesWrapper.getInstance().isBoolean(AutoFeatures.isAutoManagerEnabled) ) {
			
			initialize();
		}
	}


public class AutoManagerXPrisonNukeTriggerEventListener
	extends AutoManagerXPrisonNukeTriggerEvent
	implements Listener {
	
	public AutoManagerXPrisonNukeTriggerEventListener( BlockBreakPriority bbPriority ) {
		super( bbPriority );
	}
	
	@EventHandler(priority=EventPriority.NORMAL) 
	public void onXPrisonNukeTriggerEvent( 
			NukeTriggerEvent e, BlockBreakPriority bbPriority) {

		if ( isDisabled( e.getPlayer().getLocation().getWorld().getName() ) ||
				bbPriority.isDisabled() ) {
			return;
		}
		
		handleXPrisonNukeTriggerEvent( e, bbPriority );
	}
}


@Override
public void initialize() {

	String eP = getMessage( AutoFeatures.XPrisonNukeTriggerEventPriority );
	BlockBreakPriority bbPriority = BlockBreakPriority.fromString( eP );
	
	setBbPriority( bbPriority );
	
//	boolean isEventEnabled = eP != null && !"DISABLED".equalsIgnoreCase( eP );
	
	if ( getBbPriority() == BlockBreakPriority.DISABLED ) {
		return;
	}
	
	// Check to see if the class NukeTriggerEvent even exists:
	try {
		Output.get().logInfo( "AutoManager: checking if loaded: XPrison NukeTriggerEvent" );
		
		Class.forName( "dev.drawethree.xprison.enchants.api.events.NukeTriggerEvent", false, 
				this.getClass().getClassLoader() );
		
		Output.get().logInfo( "AutoManager: Trying to register XPrison NukeTriggerEvent" );
		
		if ( getBbPriority() != BlockBreakPriority.DISABLED ) {
			if ( bbPriority.isComponentCompound() ) {
				
				for (BlockBreakPriority subBBPriority : bbPriority.getComponentPriorities()) {
					
					createListener( subBBPriority );
				}
			}
			else {
				
				createListener(bbPriority);
			}
			
		}
	}
	catch ( ClassNotFoundException e ) {
		// CrazyEnchants is not loaded... so ignore.
		Output.get().logInfo( "AutoManager: XPrison NukeTriggerEvent is not loaded" );
	}
	catch ( Exception e ) {
		Output.get().logInfo( "AutoManager: XPrison NukeTriggerEvent failed to load. [%s]", e.getMessage() );
	}
}

private void createListener( BlockBreakPriority bbPriority ) {
	
	SpigotPrison prison = SpigotPrison.getInstance();
	PluginManager pm = Bukkit.getServer().getPluginManager();
	EventPriority ePriority = bbPriority.getBukkitEventPriority(); 
	
	
	AutoManagerXPrisonNukeTriggerEventListener autoManagerListener = 
			new AutoManagerXPrisonNukeTriggerEventListener( bbPriority );
	
	pm.registerEvent(
			NukeTriggerEvent.class, 
			autoManagerListener, ePriority,
			new EventExecutor() {
				public void execute(Listener l, Event e) { 
					
					NukeTriggerEvent exEvent = (NukeTriggerEvent) e;
					
					((AutoManagerXPrisonNukeTriggerEventListener)l)
									.onXPrisonNukeTriggerEvent(exEvent, bbPriority);
				}
			},
			prison);
	prison.getRegisteredBlockListeners().add( autoManagerListener );
}


@Override
public void unregisterListeners() {
	
//	super.unregisterListeners();
}

@Override
public void dumpEventListeners() {
	
	StringBuilder sb = new StringBuilder();
	
	dumpEventListeners( sb );
	
	if ( sb.length() > 0 ) {

		
		for ( String line : sb.toString().split( "\n" ) ) {
			
			Output.get().logInfo( line );
		}
	}
	
}


@Override
public void dumpEventListeners( StringBuilder sb ) {
	
	String eP = getMessage( AutoFeatures.XPrisonNukeTriggerEventPriority );
	boolean isEventEnabled = eP != null && !"DISABLED".equalsIgnoreCase( eP );

	if ( !isEventEnabled ) {
		return;
	}
	
	// Check to see if the class NukeTriggerEvent even exists:
	try {
		
		Class.forName( "dev.drawethree.xprison.enchants.api.events.NukeTriggerEvent", false, 
				this.getClass().getClassLoader() );
		
		
		HandlerList handlers = NukeTriggerEvent.getHandlerList();
		
//		String eP = getMessage( AutoFeatures.blockBreakEventPriority );
		BlockBreakPriority bbPriority = BlockBreakPriority.fromString( eP );

		dumpEventListenersCore( "XPrison NukeTriggerEvent", handlers, bbPriority, sb );
		
		
//		
//		BlockBreakPriority bbPriority = BlockBreakPriority.fromString( eP );
//
//		
//		String title = String.format( 
//				"NukeTriggerEvent (%s)", 
//				( bbPriority == null ? "--none--" : bbPriority.name()) );
//		
//		ChatDisplay eventDisplay = Prison.get().getPlatform().dumpEventListenersChatDisplay( 
//				title, 
//				new SpigotHandlerList( NukeTriggerEvent.getHandlerList()) );
//		
//		if ( eventDisplay != null ) {
//			sb.append( eventDisplay.toStringBuilder() );
//			sb.append( "\n" );
//		}
//		
//		
//		if ( bbPriority.isComponentCompound() ) {
//			StringBuilder sbCP = new StringBuilder();
//			for ( BlockBreakPriority bbp : bbPriority.getComponentPriorities() ) {
//				if ( sbCP.length() > 0 ) {
//					sbCP.append( ", " );
//				}
//				sbCP.append( "'" ).append( bbp.name() ).append( "'" );
//			}
//			
//			String msg = String.format( "Note '%s' is a compound of: [%s]",
//					bbPriority.name(),
//					sbCP );
//			
//			sb.append( msg ).append( "\n" );
//		}
	}
	catch ( ClassNotFoundException e ) {
		// XPrison is not loaded... so ignore.
	}
	catch ( Exception e ) {
		String causedBy = e.getCause() == null ? "" : e.getCause().getMessage();
		
		Output.get().logInfo( "AutoManager: XPrison NukeTriggerEvent failed to load. "
				+ "[%s] Caused by: [%s]", 
				e.getMessage(),
				causedBy );
	}
}


/**
 * <p>Since there are multiple blocks associated with this event, pull out the player first and
 * get the mine, then loop through those blocks to make sure they are within the mine.
 * </p>
 * 
 * <p>The logic in this function is slightly different compared to genericBlockEvent() because this
 * event contains multiple blocks so it's far more efficient to process the player data once. 
 * So that basically needed a slight refactoring.
 * </p>
 * 
 * @param e
 */
public void handleXPrisonNukeTriggerEvent( NukeTriggerEvent e, BlockBreakPriority bbPriority ) {
		
	PrisonMinesBlockBreakEvent pmEvent = null;
	long start = System.nanoTime();
	
	// If the event is canceled, it still needs to be processed because of the 
	// MONITOR events:
	// An event will be "canceled" and "ignored" if the block 
	// BlockUtils.isUnbreakable(), or if the mine is actively resetting.
	// The event will also be ignored if the block is outside of a mine
	// or if the targetBlock has been set to ignore all block events which 
	// means the block has already been processed.
	MinesEventResults eventResults = ignoreMinesBlockBreakEvent( e, 
							e.getPlayer(),  e.getOriginBlock(),
							bbPriority, true );
	
	if ( eventResults.isIgnoreEvent() ) {
		return;
	}
	
			
	StringBuilder debugInfo = new StringBuilder();
	
	debugInfo.append( String.format( "&6### ** handleXPrisonNukeTriggerEvent ** ###&3 " +
			"(event: &6NukeTriggerEvent&3, config: %s, priority: %s, canceled: %s) ",
			bbPriority.name(),
			bbPriority.getBukkitEventPriority().name(),
			(e.isCancelled() ? "TRUE " : "FALSE")
			) );
	
	debugInfo.append( eventResults.getDebugInfo() );
	
	
	// NOTE that check for auto manager has happened prior to accessing this function.
	
	// Process all priorities if the event has not been canceled, and 
	// process the MONITOR priority even if the event was canceled:
	if ( !bbPriority.isMonitor() && !e.isCancelled() || 
			bbPriority.isMonitor() &&
			e.getBlocksAffected().size() > 0 ) {

	
		
//		Block bukkitBlock = e.getBlocks().get( 0 );
		
		BlockEventType eventType = BlockEventType.XPrisonNukeTriggerEvent;
		String triggered = null;
		

		pmEvent = new PrisonMinesBlockBreakEvent( 
					eventResults,
//					bukkitBlock, 
//					e.getPlayer(),
//					eventResults.getMine(),
//					bbPriority, 
					eventType, triggered,
					debugInfo );
		

    	// NOTE: Check for the ACCESS priority and if someone does not have access, then return 
    	//       with a cancel on the event.  Both ACCESSBLOCKEVENTS and ACCESSMONITOR will be
    	//       converted to just ACCESS at this point, and the other part will run under either
    	//       BLOCKEVENTS or MONITOR.
		// This check has to be performed after creating the pmEvent object since it uses
		// a lot of the internal variables and objects.  There is not much of an impact since
		// the validateEvent() has not been ran yet.
    	if ( checkIfNoAccess( pmEvent, start ) ) {
    		
    		e.setCancelled( true );
    		return;
    	}
    	
		
		for ( int i = 0; i < e.getBlocksAffected().size(); i++ ) {
			pmEvent.getUnprocessedRawBlocks().add( e.getBlocksAffected().get( i ) );
		}
		
		
		
		
		// Check to see if the blockConverter's EventTrigger should have
		// it's blocks suppressed from explosion events.  If they should be
		// removed, then it's removed within this funciton.
		removeEventTriggerBlocksFromExplosions( pmEvent );
		

		
		
		if ( !validateEvent( pmEvent ) ) {
			
			// The event has not passed validation. All logging and Errors have been recorded
			// so do nothing more. This is to just prevent normal processing from occurring.
			
			if ( pmEvent.isCancelOriginalEvent() ) {
				
				e.setCancelled( true );
			}
			
			debugInfo.append( "(doAction failed validation) " );
		}

		

		// The validation was successful, but stop processing for the MONITOR priorities.
		// Note that BLOCKEVENTS processing occurred already within validateEvent():
		else if ( pmEvent.getBbPriority().isMonitor() ) {
			// Stop here, and prevent additional processing. 
			// Monitors should never process the event beyond this.
		}
		

		
		// This is where the processing actually happens:
		else {
			
//			debugInfo.append( "(normal processing initiating) " );
			
			// check all external events such as mcMMO and EZBlocks:
//			if ( e instanceof BlockBreakEvent ) {
//				processPMBBExternalEvents( pmEvent, debugInfo, e );
//			}
//			
			
			EventListenerCancelBy cancelBy = EventListenerCancelBy.none; 
			
			cancelBy = processPMBBEvent( pmEvent );

			
			if ( cancelBy != EventListenerCancelBy.none ) {
				
				e.setCancelled( true );
				debugInfo.append( "(event canceled) " );
			}
//			else if ( cancelBy == EventListenerCancelBy.drops ) {
//				try
//				{
//					e.setDropItems( false );
//					debugInfo.append( "(drop canceled) " );
//				}
//				catch ( NoSuchMethodError e1 )
//				{
//					String message = String.format( 
//							"Warning: The autoFeaturesConfig.yml setting `cancelAllBlockEventBlockDrops` " +
//									"is not valid for this version of Spigot. It's only vaid for spigot v1.12.x and higher. " +
//									"Modify the config settings and set this value to `false`.  For now, it is temporarily " +
//									"disabled. [%s]",
//									e1.getMessage() );
//					Output.get().logWarn( message );
//					
//					AutoFeaturesWrapper.getInstance().getAutoFeaturesConfig()
//							.setFeature( AutoFeatures.cancelAllBlockEventBlockDrops, false );
//				}
//
//			}
		}


	}
	
	printDebugInfo( pmEvent, start );
}

	@Override
	protected int checkBonusXp( Player player, Block block, ItemStack item ) {
		int bonusXp = 0;
		
		return bonusXp;
	}
}