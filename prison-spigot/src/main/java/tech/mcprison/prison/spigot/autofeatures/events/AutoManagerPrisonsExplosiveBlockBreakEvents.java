package tech.mcprison.prison.spigot.autofeatures.events;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.PluginManager;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.autofeatures.AutoFeaturesWrapper;
import tech.mcprison.prison.mines.features.MineBlockEvent.BlockEventType;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.api.ExplosiveBlockBreakEvent;
import tech.mcprison.prison.spigot.api.PrisonMinesBlockBreakEvent;
import tech.mcprison.prison.spigot.autofeatures.AutoManagerFeatures;
import tech.mcprison.prison.spigot.block.BlockBreakPriority;
import tech.mcprison.prison.spigot.block.SpigotItemStack;

public class AutoManagerPrisonsExplosiveBlockBreakEvents
	extends AutoManagerFeatures 
	implements PrisonEventManager {

	private BlockBreakPriority bbPriority;
	
	public AutoManagerPrisonsExplosiveBlockBreakEvents() {
		super();
	}

	public AutoManagerPrisonsExplosiveBlockBreakEvents( BlockBreakPriority bbPriority ) {
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

	
	public class AutoManagerExplosiveBlockBreakEventListener 
		extends AutoManagerPrisonsExplosiveBlockBreakEvents
		implements Listener {
    	
    	public AutoManagerExplosiveBlockBreakEventListener( BlockBreakPriority bbPriority ) {
    		super( bbPriority );
    	}
    	
		@EventHandler(priority=EventPriority.NORMAL) 
		public void onPrisonsExplosiveBlockBreakEvent( ExplosiveBlockBreakEvent e, BlockBreakPriority bbPriority ) {
			
			if ( isDisabled( e.getBlock().getLocation().getWorld().getName() ) ||
					bbPriority.isDisabled() ) {
				return;
			}

			handleExplosiveBlockBreakEvent( e, bbPriority );
		}
	}


	@Override
	public void initialize() {
		
		String eP = getMessage( AutoFeatures.ProcessPrisons_ExplosiveBlockBreakEventsPriority );
		BlockBreakPriority bbPriority = BlockBreakPriority.fromString( eP );
		
		setBbPriority( bbPriority );
		
//		boolean isEventEnabled = eP != null && !"DISABLED".equalsIgnoreCase( eP );

		if ( getBbPriority() == BlockBreakPriority.DISABLED ) {
			return;
		}
		
		try {
			
			Output.get().logInfo( "AutoManager: Trying to register ExplosiveBlockBreakEvent Listener" );

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
		catch ( Exception e ) {
			Output.get().logInfo( "AutoManager: Prison's own ExplosiveBlockBreakEvent failed to load. [%s]", e.getMessage() );
		}
	}

	private void createListener(BlockBreakPriority bbPriority) {
		
		SpigotPrison prison = SpigotPrison.getInstance();
		PluginManager pm = Bukkit.getServer().getPluginManager();
		EventPriority ePriority = bbPriority.getBukkitEventPriority();           
		
		
		AutoManagerExplosiveBlockBreakEventListener autoManagerListener = 
				new AutoManagerExplosiveBlockBreakEventListener( bbPriority );
		
		pm.registerEvent(ExplosiveBlockBreakEvent.class, autoManagerListener, ePriority,
				new EventExecutor() {
					public void execute(Listener l, Event e) { 
						
						ExplosiveBlockBreakEvent ebbEvent = (ExplosiveBlockBreakEvent) e;
						
						((AutoManagerExplosiveBlockBreakEventListener)l)
								.onPrisonsExplosiveBlockBreakEvent( ebbEvent, getBbPriority() );
					}
				},
				prison);
		
		prison.getRegisteredBlockListeners().add( autoManagerListener );
	}

   

    @Override
    public void unregisterListeners() {
    	
//    	super.unregisterListeners();
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

		String eP = getMessage( AutoFeatures.ProcessPrisons_ExplosiveBlockBreakEventsPriority );
		boolean isEventEnabled = eP != null && !"DISABLED".equalsIgnoreCase( eP );

    	if ( !isEventEnabled ) {
    		return;
    	}
		
		// Check to see if the class ExplosiveEvent even exists:
		try {
			
			
			HandlerList handlers = ExplosiveBlockBreakEvent.getHandlerList();
			
//    		String eP = getMessage( AutoFeatures.blockBreakEventPriority );
    		BlockBreakPriority bbPriority = BlockBreakPriority.fromString( eP );

    		dumpEventListenersCore( "ExplosiveBlockBreakEvent", handlers, bbPriority, sb );
    		
			
//    		BlockBreakPriority bbPriority = BlockBreakPriority.fromString( eP );
//			
//			String title = String.format( 
//					"ExplosiveBlockBreakEvent (%s)", 
//					( bbPriority == null ? "--none--" : bbPriority.name()) );
//			
//
//			ChatDisplay eventDisplay = Prison.get().getPlatform().dumpEventListenersChatDisplay( 
//					title, 
//					new SpigotHandlerList( ExplosiveBlockBreakEvent.getHandlerList()) );
//
//			if ( eventDisplay != null ) {
//				sb.append( eventDisplay.toStringBuilder() );
//				sb.append( "\n" );
//			}
//			
//			
//			if ( bbPriority.isComponentCompound() ) {
//				StringBuilder sbCP = new StringBuilder();
//				for ( BlockBreakPriority bbp : bbPriority.getComponentPriorities() ) {
//					if ( sbCP.length() > 0 ) {
//						sbCP.append( ", " );
//					}
//					sbCP.append( "'" ).append( bbp.name() ).append( "'" );
//				}
//				
//				String msg = String.format( "Note '%s' is a compound of: [%s]",
//						bbPriority.name(),
//						sbCP );
//				
//				sb.append( msg ).append( "\n" );
//			}
		}
		catch ( Exception e ) {
			Output.get().logInfo( "AutoManager: PrisonEnchants failed to load. [%s]", e.getMessage() );
		}
	}
    
	protected void handleExplosiveBlockBreakEvent( ExplosiveBlockBreakEvent e, 
			BlockBreakPriority bbPriority ) {
		
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
										e.getPlayer(), e.getBlock(),
										bbPriority, true );
    	
		if ( eventResults.isIgnoreEvent() ) {
			return;
		}
		
		
		StringBuilder debugInfo = new StringBuilder();
		
		debugInfo.append( String.format( "### ** handleExplosiveBlockBreakEvent (Prisons's bombs) ** ### " +
				"(event: ExplosiveBlockBreakEvent, config: %s, priority: %s, canceled: %s) ",
				bbPriority.name(),
				bbPriority.getBukkitEventPriority().name(),
				(e.isCancelled() ? "TRUE " : "FALSE")
				) );
		
		debugInfo.append( eventResults.getDebugInfo() );
		
		
		// Process all priorities if the event has not been canceled, and 
		// process the MONITOR priority even if the event was canceled:
    	if ( !bbPriority.isMonitor() && !e.isCancelled() || 
    			bbPriority.isMonitor() ) {
		
			
			BlockEventType eventType = BlockEventType.PrisonExplosion;
			String triggered = e.getTriggeredBy();
			
			pmEvent = new PrisonMinesBlockBreakEvent( 
						eventResults,
//						e.getBlock(), 
//						e.getPlayer(),
//						eventResults.getMine(),
//						bbPriority, 
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
			
			// If this event is fired, but yet there are no exploded blocks, then do not set 
			// forceIfAirBlock to true so this event is skipped.
			if ( e.getExplodedBlocks() != null && e.getExplodedBlocks().size() > 0 ) {
				
				pmEvent.setUnprocessedRawBlocks( e.getExplodedBlocks() );
				pmEvent.setForceIfAirBlock( e.isForceIfAirBlock() );
			}
			
			
			// Warning: toolInHand really needs to be defined in the event if the source is a
			//          Mine Bomb, otherwise auto features will detect the player is holding 
			//          a mine bomb which is not a pickaxe so the drops will be ZERO.  If they
			//          used their last mine bomb, then auto features will detect only AIR 
			//          in their hand.
			if ( e.getToolInHand() != null && e.getToolInHand() instanceof SpigotItemStack ) {
				pmEvent.setItemInHand( (SpigotItemStack) e.getToolInHand() );
			}
			
			
			
			// Note: If the mineBomb is set, then the bomb itself uses a pseudo 
			//       tool in hand, so need to disable durability calculations since
			//       if the pseudo tool breaks, it will clear the player's in-hand
			//       inventory stack, which will be more mine bombs if they had more 
			//       than one.
			if ( e.getMineBomb() != null ) {
				pmEvent.setCalculateDurability( false );
				
				// Set if forced autoSell:
				pmEvent.setForceAutoSell( e.getMineBomb().isAutosell() );
			}
			
    		
    		// Check to see if the blockConverter's EventTrigger should have
    		// it's blocks suppressed from explosion events.  If they should be
    		// removed, then it's removed within this funciton.
    		removeEventTriggerBlockksFromExplosions( pmEvent );
    		
  
			
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
    			
//    			debugInfo.append( "(normal processing initiating) " );
    			
    			// check all external events such as mcMMO and EZBlocks:
    			if ( e instanceof BlockBreakEvent ) {
    				processPMBBExternalEvents( pmEvent, e );
    			}
    			
    			
    			EventListenerCancelBy cancelBy = EventListenerCancelBy.none; 
    			
    			cancelBy = processPMBBEvent( pmEvent );

    			
    			if ( cancelBy == EventListenerCancelBy.event ) {
    				
    				e.setCancelled( true );
    				debugInfo.append( "(event canceled) " );
    			}
    			else if ( cancelBy == EventListenerCancelBy.drops ) {
					try
					{
						e.setDropItems( false );
						debugInfo.append( "(drop canceled) " );
					}
					catch ( NoSuchMethodError e1 )
					{
						String message = String.format( 
								"Warning: The autoFeaturesConfig.yml setting `cancelAllBlockEventBlockDrops` " +
										"is not valid for this version of Spigot. It's only vaid for spigot v1.12.x and higher. " +
										"Modify the config settings and set this value to `false`.  For now, it is temporarily " +
										"disabled. [%s]",
										e1.getMessage() );
						Output.get().logWarn( message );
						
						AutoFeaturesWrapper.getInstance().getAutoFeaturesConfig()
								.setFeature( AutoFeatures.cancelAllBlockEventBlockDrops, false );
					}

    			}
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
