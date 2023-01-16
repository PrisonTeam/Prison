package tech.mcprison.prison.spigot.autofeatures.events;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.PluginManager;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.mines.features.MineBlockEvent.BlockEventType;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.output.Output.DebugTarget;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.api.ExplosiveBlockBreakEvent;
import tech.mcprison.prison.spigot.api.PrisonMinesBlockBreakEvent;
import tech.mcprison.prison.spigot.autofeatures.AutoManagerFeatures;
import tech.mcprison.prison.spigot.block.BlockBreakPriority;
import tech.mcprison.prison.spigot.block.OnBlockBreakExternalEvents;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.spigot.game.SpigotHandlerList;
import tech.mcprison.prison.spigot.game.SpigotPlayer;

public class AutoManagerPrisonsExplosiveBlockBreakEvents
	extends AutoManagerFeatures 
	implements PrisonEventManager {

	private BlockBreakPriority bbPriority;
	
	public AutoManagerPrisonsExplosiveBlockBreakEvents() {
		super();
	}

	
	public BlockBreakPriority getBbPriority() {
		return bbPriority;
	}
	public void setBbPriority( BlockBreakPriority bbPriority ) {
		this.bbPriority = bbPriority;
	}

	@Override
	public void registerEvents() {
	
		initialize();
		
	}

	
	public class AutoManagerExplosiveBlockBreakEventListener 
		extends AutoManagerPrisonsExplosiveBlockBreakEvents
		implements Listener {
		
		@EventHandler(priority=EventPriority.NORMAL) 
		public void onPrisonsExplosiveBlockBreakEvent( ExplosiveBlockBreakEvent e, BlockBreakPriority bbPriority ) {
			
			if ( isDisabled( e.getBlock().getLocation().getWorld().getName() ) ) {
				return;
			}

			handleExplosiveBlockBreakEvent( e, bbPriority );
		}
	}


	@Override
	public void initialize() {
		
		String eP = getMessage( AutoFeatures.ProcessPrisons_ExplosiveBlockBreakEventsPriority );
  		setBbPriority( BlockBreakPriority.fromString( eP ) );
		
//		boolean isEventEnabled = eP != null && !"DISABLED".equalsIgnoreCase( eP );

		if ( getBbPriority() == BlockBreakPriority.DISABLED ) {
			return;
		}
		
		try {
			
			Output.get().logInfo( "AutoManager: Trying to register ExplosiveBlockBreakEvent Listener" );

			
			SpigotPrison prison = SpigotPrison.getInstance();
			PluginManager pm = Bukkit.getServer().getPluginManager();
			EventPriority ePriority = getBbPriority().getBukkitEventPriority();           
			
			
			AutoManagerExplosiveBlockBreakEventListener autoManagerlListener = 
					new AutoManagerExplosiveBlockBreakEventListener();
			
			pm.registerEvent(ExplosiveBlockBreakEvent.class, autoManagerlListener, ePriority,
					new EventExecutor() {
				public void execute(Listener l, Event e) { 
					
					ExplosiveBlockBreakEvent ebbEvent = (ExplosiveBlockBreakEvent) e;
					
					((AutoManagerExplosiveBlockBreakEventListener)l)
							.onPrisonsExplosiveBlockBreakEvent( ebbEvent, getBbPriority() );
				}
			},
					prison);
			prison.getRegisteredBlockListeners().add( autoManagerlListener );
			
			
		}
		catch ( Exception e ) {
			Output.get().logInfo( "AutoManager: Prison's own ExplosiveBlockBreakEvent failed to load. [%s]", e.getMessage() );
		}
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
			
    		BlockBreakPriority bbPriority = BlockBreakPriority.fromString( eP );
			
			String title = String.format( 
					"ExplosiveBlockBreakEvent (%s)", 
					( bbPriority == null ? "--none--" : bbPriority.name()) );
			

			ChatDisplay eventDisplay = Prison.get().getPlatform().dumpEventListenersChatDisplay( 
					title, 
					new SpigotHandlerList( ExplosiveBlockBreakEvent.getHandlerList()) );

			if ( eventDisplay != null ) {
				sb.append( eventDisplay.toStringBuilder() );
				sb.append( "\n" );
			}
		}
		catch ( Exception e ) {
			Output.get().logInfo( "AutoManager: PrisonEnchants failed to load. [%s]", e.getMessage() );
		}
	}
    
	protected void handleExplosiveBlockBreakEvent( ExplosiveBlockBreakEvent e, BlockBreakPriority bbPriority ) {
		
//		boolean monitor, boolean blockEventsOnly, 
//		boolean autoManager ) {
	
		long start = System.nanoTime();
		
		if ( e.isCancelled() ||  ignoreMinesBlockBreakEvent( e, e.getPlayer(), e.getBlock()) ) {
			return;
		}
		
		
		// Register all external events such as mcMMO and EZBlocks:
		OnBlockBreakExternalEvents.getInstance().registerAllExternalEvents();
		
		StringBuilder debugInfo = new StringBuilder();
		
		
		debugInfo.append( String.format( "### ** genericExplosiveEvent (Prisons's bombs) ** ### " +
				"(event: ExplosiveBlockBreakEvent, config: %s, priority: %s, canceled: %s) ",
				bbPriority.name(),
				bbPriority.getBukkitEventPriority().name(),
				(e.isCancelled() ? "TRUE " : "FALSE")
				) );
		
		
		// Process all priorities if the event has not been canceled, and 
		// process the MONITOR priority even if the event was canceled:
    	if ( !bbPriority.isMonitor() && !e.isCancelled() || 
    			bbPriority.isMonitor() ) {
		
			
	    	String eP = getMessage( AutoFeatures.ProcessPrisons_ExplosiveBlockBreakEventsPriority );
			boolean isPPrisonExplosiveBlockBreakEnabled = eP != null && !"DISABLED".equalsIgnoreCase( eP );
	
			
			// Need to wrap in a Prison block so it can be used with the mines:
			SpigotBlock sBlock = SpigotBlock.getSpigotBlock(e.getBlock());
			SpigotPlayer sPlayer = new SpigotPlayer(e.getPlayer());
			
			BlockEventType eventType = BlockEventType.PrisonExplosion;
			String triggered = e.getTriggeredBy();
			
			PrisonMinesBlockBreakEvent pmEvent = new PrisonMinesBlockBreakEvent( e.getBlock(), e.getPlayer(),
					sBlock, sPlayer, bbPriority, eventType, triggered );
	
			
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
			}
			
			if ( !validateEvent( pmEvent, debugInfo ) ) {
				
				// The event has not passed validation. All logging and Errors have been recorded
				// so do nothing more. This is to just prevent normal processing from occurring.
				
				if ( pmEvent.isCancelOriginalEvent() ) {
					
					e.setCancelled( true );
				}
				
				debugInfo.append( "(doAction failed validation) " );
			}
			
			

    		// The validation was successful, but stop processing for the MONITOR priorities.
    		// Note that BLOCKEVENTS processing occured already within validateEvent():
    		else if ( pmEvent.getBbPriority().isMonitor() ) {
    			// Stop here, and prevent additional processing. 
    			// Monitors should never process the event beyond this.
    		}
			
	
			
			// now process all blocks (non-monitor):
			else if ( isPPrisonExplosiveBlockBreakEnabled && 
					( pmEvent.getMine() != null || pmEvent.getMine() == null && 
					!isBoolean( AutoFeatures.pickupLimitToMines )) ) {
				
				if ( pmEvent.getExplodedBlocks().size() > 0 ) {
					
	//				String triggered = null;
					
					
	//    			PrisonMinesBlockBreakEvent pmbbEvent = new PrisonMinesBlockBreakEvent( dummyBlock.getWrapper(), e.getPlayer(),
	//    												mine, dummyBlock, explodedBlocks, BlockEventType.PEExplosive, triggered );
					Bukkit.getServer().getPluginManager().callEvent(pmEvent);
					if ( pmEvent.isCancelled() ) {
						debugInfo.append( "(normal processing: PrisonMinesBlockBreakEvent was canceled) " );
					}
					else {
						
	                	// Cancel drops if so configured:
	                	if ( isBoolean( AutoFeatures.cancelAllBlockEventBlockDrops ) ) {
	                		
	                		try
	                		{
	                			e.setDropItems( false );
	                		}
	                		catch ( NoSuchMethodError e1 )
	                		{
	                			String message = String.format( 
	                					"Warning: The autoFeaturesConfig.yml setting `cancelAllBlockEventBlockDrops` " +
	                					"is not valid for this version of Spigot. Modify the config settings and set " +
	                					"this value to `false`. [%s]",
	                					e1.getMessage() );
	                			Output.get().logWarn( message );
	                		}
	                	}
	                	
						if ( doAction( pmEvent, debugInfo ) ) {
							
							if ( isBoolean( AutoFeatures.cancelAllBlockBreakEvents ) ) {
								
								e.setCancelled( true );
							}
							else {
								
								debugInfo.append( "(event was not canceled) " );
							}
							
							finalizeBreakTheBlocks( pmEvent );
	                		
	                		doBlockEvents( pmEvent );
	
						}
						
						else {
							
							debugInfo.append( "(doAction failed without details) " );
						}
						
					}
				}
				
				
				debugInfo.append( "(normal processing) " );
			}
			else {
				
				debugInfo.append( "(logic bypass) " );
			}
			
		}
		
		if ( debugInfo.length() > 0 ) {
			
			long stop = System.nanoTime();
			debugInfo.append( " [" ).append( (stop - start) / 1000000d ).append( " ms]" );
			
			Output.get().logDebug( DebugTarget.blockBreak, debugInfo.toString() );
		}
		
	}

	@Override
	protected int checkBonusXp( Player player, Block block, ItemStack item ) {
		int bonusXp = 0;
		
		return bonusXp;
	}
}
