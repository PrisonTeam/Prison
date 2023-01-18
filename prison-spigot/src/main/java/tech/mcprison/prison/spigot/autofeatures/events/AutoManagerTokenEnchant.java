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

import com.vk2gpz.tokenenchant.event.TEBlockExplodeEvent;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.mines.features.MineBlockEvent.BlockEventType;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.output.Output.DebugTarget;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.api.PrisonMinesBlockBreakEvent;
import tech.mcprison.prison.spigot.autofeatures.AutoManagerFeatures;
import tech.mcprison.prison.spigot.block.BlockBreakPriority;
import tech.mcprison.prison.spigot.game.SpigotHandlerList;

public class AutoManagerTokenEnchant 
	extends AutoManagerFeatures 
	implements PrisonEventManager {
	
	private BlockBreakPriority bbPriority;
	
	private boolean teExplosionTriggerEnabled;

	public AutoManagerTokenEnchant() {
        super();
        
        this.teExplosionTriggerEnabled = true;
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

	
    public class AutoManagerTokenEnchantEventListener
		extends AutoManagerTokenEnchant
		implements Listener {
    	
        @EventHandler(priority=EventPriority.LOW) 
        public void onTEBlockExplode( TEBlockExplodeEvent e, BlockBreakPriority bbPriority) {
			if ( isDisabled( e.getBlock().getLocation().getWorld().getName() ) ||
					bbPriority.isDisabled() ) {
				return;
			}
			
			handleTEBlockExplodeEvent( e, bbPriority );
//        	genericBlockExplodeEventAutoManager( e );
        }
    }
    
    
    @Override
    public void initialize() {

    	String eP = getMessage( AutoFeatures.TokenEnchantBlockExplodeEventPriority );
    	setBbPriority( BlockBreakPriority.fromString( eP ) );
    	
//		boolean isEventEnabled = eP != null && !"DISABLED".equalsIgnoreCase( eP );

    	if ( getBbPriority() == BlockBreakPriority.DISABLED ) {
    		return;
    	}
    	
    	// Check to see if the class TEBlockExplodeEvent even exists:
    	try {
    		Output.get().logInfo( "AutoManager: checking if loaded: TokenEnchant" );
    		
    		Class.forName( "com.vk2gpz.tokenenchant.event.TEBlockExplodeEvent", false, 
    				this.getClass().getClassLoader() );
    		
    		Output.get().logInfo( "AutoManager: Trying to register TokenEnchant" );
    		
    		SpigotPrison prison = SpigotPrison.getInstance();
    		PluginManager pm = Bukkit.getServer().getPluginManager();
    		EventPriority ePriority = getBbPriority().getBukkitEventPriority(); 
    		
    		
    		AutoManagerTokenEnchantEventListener autoManagerlListener = 
    				new AutoManagerTokenEnchantEventListener();
    		
    		pm.registerEvent(TEBlockExplodeEvent.class, autoManagerlListener, ePriority,
    				new EventExecutor() {
    			public void execute(Listener l, Event e) { 
    				((AutoManagerTokenEnchantEventListener)l)
    				.onTEBlockExplode( (TEBlockExplodeEvent)e, getBbPriority() );
    			}
    		},
    				prison);
    		prison.getRegisteredBlockListeners().add( autoManagerlListener );
    		
    		
    		
    		
    		
    		
    		
//    		BlockBreakPriority eventPriority = BlockBreakPriority.fromString( eP );
//    		
//    		if ( eventPriority != BlockBreakPriority.DISABLED ) {
//    			
//    			EventPriority ePriority = EventPriority.valueOf( eventPriority.name().toUpperCase() );           
//    			
//    			
//    			OnBlockBreakEventTokenEnchantEventListenerMonitor normalListenerMonitor = 
//    										new OnBlockBreakEventTokenEnchantEventListenerMonitor();
//
//    			
//    			
//    			if ( eventPriority != BlockBreakPriority.MONITOR ) {
//    				
//    				if ( isBoolean( AutoFeatures.isAutoFeaturesEnabled )) {
//    					
//    					AutoManagerTokenEnchantEventListener autoManagerlListener = 
//    							new AutoManagerTokenEnchantEventListener();
//    					
//    					pm.registerEvent(TEBlockExplodeEvent.class, autoManagerlListener, ePriority,
//    							new EventExecutor() {
//    						public void execute(Listener l, Event e) { 
//    							((AutoManagerTokenEnchantEventListener)l)
//    							.onTEBlockExplode((TEBlockExplodeEvent)e);
//    						}
//    					},
//    							prison);
//    					prison.getRegisteredBlockListeners().add( autoManagerlListener );
//    				}
//    				else if ( isBoolean( AutoFeatures.normalDrop ) ) {
//    					
//    					OnBlockBreakEventTokenEnchantEventListener normalListener = 
//    							new OnBlockBreakEventTokenEnchantEventListener();
//    					
//    					pm.registerEvent(TEBlockExplodeEvent.class, normalListener, ePriority,
//    							new EventExecutor() {
//    						public void execute(Listener l, Event e) { 
//    							((OnBlockBreakEventTokenEnchantEventListener)l)
//    							.onTEBlockExplode((TEBlockExplodeEvent)e);
//    						}
//    					},
//    							prison);
//    					prison.getRegisteredBlockListeners().add( normalListener );
//    				}

//    			}
//    			else {
//    				
//    				pm.registerEvent(TEBlockExplodeEvent.class, normalListenerMonitor, EventPriority.MONITOR,
//    						new EventExecutor() {
//    					public void execute(Listener l, Event e) { 
//    						((OnBlockBreakEventTokenEnchantEventListenerMonitor)l)
//    						.onTEBlockExplode((TEBlockExplodeEvent)e);
//    					}
//    				},
//    						prison);
//    				prison.getRegisteredBlockListeners().add( normalListenerMonitor );
//    			}
    			
//    		}
    		
    	}
    	catch ( ClassNotFoundException e ) {
    		// TokenEnchant is not loaded... so ignore.
    		Output.get().logInfo( "AutoManager: TokenEnchant is not loaded" );
    	}
    	catch ( Exception e ) {
    		Output.get().logInfo( "AutoManager: TokenEnchant failed to load. [%s]", e.getMessage() );
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
    	
    	String eP = getMessage( AutoFeatures.TokenEnchantBlockExplodeEventPriority );
		boolean isEventEnabled = eP != null && !"DISABLED".equalsIgnoreCase( eP );

    	if ( !isEventEnabled ) {
    		return;
    	}
		
		// Check to see if the class TEBlockExplodeEvent even exists:
		try {
			
			Class.forName( "com.vk2gpz.tokenenchant.event.TEBlockExplodeEvent", false, 
							this.getClass().getClassLoader() );
			
    		BlockBreakPriority bbPriority = BlockBreakPriority.fromString( eP );

			String title = String.format( 
					"TEBlockExplodeEvent (%s)", 
					( bbPriority == null ? "--none--" : bbPriority.name()) );
			
			ChatDisplay eventDisplay = Prison.get().getPlatform().dumpEventListenersChatDisplay( 
					title, 
					new SpigotHandlerList( TEBlockExplodeEvent.getHandlerList()) );

			if ( eventDisplay != null ) {
				sb.append( eventDisplay.toStringBuilder() );
				sb.append( "\n" );
			}
		}
		catch ( ClassNotFoundException e ) {
			// TokenEnchant is not loaded... so ignore.
		}
		catch ( Exception e ) {
			Output.get().logInfo( "AutoManager: TokenEnchant failed to load. [%s]", e.getMessage() );
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
	public void handleTEBlockExplodeEvent( TEBlockExplodeEvent e, BlockBreakPriority bbPriority ) {
	
//	, boolean monitor, boolean blockEventsOnly, 
//			boolean autoManager ) {

		long start = System.nanoTime();
		
		// If the event is canceled, it still needs to be processed because of the 
		// MONITOR events:
		// An event will be "canceled" and "ignored" if the block 
		// BlockUtils.isUnbreakable(), or if the mine is activly resetting.
		// The event will also be ignored if the block is outside of a mine
		// or if the targetBlock has been set to ignore all block events which 
		// means the block has already been processed.
    	MinesEventResults eventResults = ignoreMinesBlockBreakEvent( e, e.getPlayer(), 
    									e.getBlock());
    	if ( eventResults.isIgnoreEvent() ) {
    		return;
    	}
//    	if ( ignoreMinesBlockBreakEvent( e, e.getPlayer(), e.getBlock()) ) {
//    		return;
//    	}
    	

		// Register all external events such as mcMMO and EZBlocks:
//		OnBlockBreakExternalEvents.getInstance().registerAllExternalEvents();
		
		StringBuilder debugInfo = new StringBuilder();
		
		
		debugInfo.append( String.format( "### ** genericBlockExplodeEvent ** ### " +
				"(event: TEBlockExplodeEvent, config: %s, priority: %s, canceled: %s) ",
				bbPriority.name(),
				bbPriority.getBukkitEventPriority().name(),
				(e.isCancelled() ? "TRUE " : "FALSE")
				) );
		
		
		// Process all priorities if the event has not been canceled, and 
		// process the MONITOR priority even if the event was canceled:
    	if ( !bbPriority.isMonitor() && !e.isCancelled() || 
    			bbPriority.isMonitor() ) {


//    		boolean isEventListenerEnabled = bbPriority == BlockBreakPriority.DISABLED;
    		
//	    	String eP = getMessage( AutoFeatures.TokenEnchantBlockExplodeEventPriority );
//			boolean isTEExplosiveEnabled = eP != null && !"DISABLED".equalsIgnoreCase( eP );


    		// Need to wrap in a Prison block so it can be used with the mines:
//    		SpigotBlock sBlock = SpigotBlock.getSpigotBlock(e.getBlock());
//    		SpigotPlayer sPlayer = new SpigotPlayer(e.getPlayer());
    		
    		BlockEventType eventType = BlockEventType.TEXplosion;
    		String triggered = checkCEExplosionTriggered( e );
    		
    		PrisonMinesBlockBreakEvent pmEvent = new PrisonMinesBlockBreakEvent( 
    					e.getBlock(), 
    					e.getPlayer(),
    					eventResults.getMine(),
//    					sBlock, sPlayer, 
    					bbPriority, eventType, triggered );
    		
    		// NOTE: Token Enchant will pass the event's block to prison, but that block may 
    		//       have already been processed by prison.  Therefore the PrisonMinesBlockBreakEvent
    		//       must enable the feature setForceIfAirBlock( true ).  That block will not be used a 
    		//       second time, but it will allow the explosion event to be processed.
    		pmEvent.setForceIfAirBlock( true );
    		
    		pmEvent.setUnprocessedRawBlocks( e.blockList() );
    		
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
    		

    		
    		// This is where the processing actually happens:
    		else {
    			
//    			debugInfo.append( "(normal processing initiating) " );
    			
    			// check all external events such as mcMMO and EZBlocks:
//    			if ( e instanceof BlockBreakEvent ) {
//    				processPMBBExternalEvents( pmEvent, debugInfo, e );
//    			}
    			
    			
    			EventListenerCancelBy cancelBy = EventListenerCancelBy.none; 
    			
    			cancelBy = processPMBBEvent( pmEvent, debugInfo );

    			
    			if ( cancelBy != EventListenerCancelBy.none ) {
    				
    				e.setCancelled( true );
    				debugInfo.append( "(event canceled) " );
    			}
//    			else if ( cancelBy == EventListenerCancelBy.drops ) {
//					try
//					{
//						e.setDropItems( false );
//						debugInfo.append( "(drop canceled) " );
//					}
//					catch ( NoSuchMethodError e1 )
//					{
//						String message = String.format( 
//								"Warning: The autoFeaturesConfig.yml setting `cancelAllBlockEventBlockDrops` " +
//										"is not valid for this version of Spigot. It's only vaid for spigot v1.12.x and higher. " +
//										"Modify the config settings and set this value to `false`.  For now, it is temporarily " +
//										"disabled. [%s]",
//										e1.getMessage() );
//						Output.get().logWarn( message );
//						
//						AutoFeaturesWrapper.getInstance().getAutoFeaturesConfig()
//								.setFeature( AutoFeatures.cancelAllBlockEventBlockDrops, false );
//					}
//
//    			}
    		}
    		
    		
//    		// now process all blocks (non-monitor):
//    		else if ( isEventListenerEnabled && 
//    				( pmEvent.getMine() != null || pmEvent.getMine() == null && 
//    									!isBoolean( AutoFeatures.pickupLimitToMines )) ) {
//
//    			
//    			if ( pmEvent.getExplodedBlocks().size() > 0 ) {
//    				
////					String triggered = checkCEExplosionTriggered( e );
////					
////
////					PrisonMinesBlockBreakEvent pmbbEvent = new PrisonMinesBlockBreakEvent( e.getBlock(), e.getPlayer(),
////	    												mine, block, explodedBlocks, BlockEventType.TEXplosion, triggered );
//	                Bukkit.getServer().getPluginManager().callEvent( pmEvent );
//	                if ( pmEvent.isCancelled() ) {
//	                	debugInfo.append( "(normal processing: PrisonMinesBlockBreakEvent was canceled) " );
//	                }
//	                else {
//	                	
//	                	// Cancel drops if so configured:
//	                	if ( isBoolean( AutoFeatures.cancelAllBlockEventBlockDrops ) ) {
//	                		
//	                		try
//	                		{
//	                			e.setYield( 0 );
////	                			e.setDropItems( false );
//	                		}
//	                		catch ( NoSuchMethodError e1 )
//	                		{
//	                			String message = String.format( 
//	                					"Warning: The autoFeaturesConfig.yml setting `cancelAllBlockEventBlockDrops` " +
//	                					"is not valid for this version of Spigot. Modify the config settings and set " +
//	                					"this value to `false`. [%s]",
//	                					e1.getMessage() );
//	                			Output.get().logWarn( message );
//	                		}
//	                	}
//	                	
//	                	// This is where the processing actually happens:
//	                	if ( doAction( pmEvent, debugInfo ) ) {
//	                		
//	                		if ( isBoolean( AutoFeatures.cancelAllBlockBreakEvents ) ) {
//	                			
//	                			e.setCancelled( true );
//	                		}
//	                		else {
//	                			
//	                			debugInfo.append( "(event was not canceled) " );
//	                		}
//	                		
//	                		finalizeBreakTheBlocks( pmEvent );
//	                		
//	                		doBlockEvents( pmEvent );
//
//	                	}
//	                	
//	                	else {
//	                		
//	                		debugInfo.append( "(doAction failed without details) " );
//	                	}
//	                	
//	                }
//    				
//    			}
//    			
//    			
//    			debugInfo.append( "(normal processing) " );
//   			}
//    		else {
//    			
//    			debugInfo.append( "(logic bypass) " );
//    		}
    			
    	}
    	
		if ( debugInfo.length() > 0 ) {
			
			long stop = System.nanoTime();
			debugInfo.append( " [" ).append( (stop - start) / 1000000d ).append( " ms]" );
			
			Output.get().logDebug( DebugTarget.blockBreak, debugInfo.toString() );
		}
	}

	private String checkCEExplosionTriggered( TEBlockExplodeEvent e )
	{
		String triggered = null;
		
		// Please be aware:  This function is named the same as the auto features setting, but this is 
		// not related.  This is only trying to get the name of the enchantment that triggered the event.
		if ( isTeExplosionTriggerEnabled() ) {
			
			try {
				triggered = e.getTrigger();
			}
			catch ( Exception | NoSuchMethodError ex ) {
				// Only print the error the first time, then suppress the error:
				String error = ex.getMessage();
				
				Output.get().logError( "Error: Trying to access the TEBlockExplodeEvent.getTrigger() " +
						"function.  Make sure you are using TokenEnchant v18.11.0 or newer. The new " +
						"getTrigger() function returns the TE Plugin that is firing the TEBlockExplodeEvent. " +
						"The Prison BlockEvents can be filtered by this triggered value. " +
						error );
				
				// Disable collecting the trigger.
				setTeExplosionTriggerEnabled( false );
				
			}
		}
		
		return triggered;
	}

	protected int checkBonusXp( Player player, Block block, ItemStack item ) {
		int bonusXp = 0;
		
		return bonusXp;
	}

	private boolean isTeExplosionTriggerEnabled() {
		return teExplosionTriggerEnabled;
	}

	private void setTeExplosionTriggerEnabled( boolean teExplosionTriggerEnabled ) {
		this.teExplosionTriggerEnabled = teExplosionTriggerEnabled;
	}
}
