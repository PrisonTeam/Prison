package tech.mcprison.prison.spigot.autofeatures.events;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.PluginManager;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.autofeatures.AutoFeaturesWrapper;
import tech.mcprison.prison.mines.features.MineBlockEvent.BlockEventType;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.api.PrisonMinesBlockBreakEvent;
import tech.mcprison.prison.spigot.autofeatures.AutoManagerFeatures;
import tech.mcprison.prison.spigot.block.BlockBreakPriority;
import tech.mcprison.prison.spigot.game.SpigotHandlerList;


public class AutoManagerBlockBreakEvents 
	extends AutoManagerFeatures
	implements PrisonEventManager
{
	private BlockBreakPriority bbPriority;
	
	public AutoManagerBlockBreakEvents() {
        super();
    }
	public AutoManagerBlockBreakEvents( BlockBreakPriority bbPriority ) {
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
	
		initialize();
	}
	
	
    public class AutoManagerBlockBreakEventListener 
	    extends AutoManagerBlockBreakEvents
	    implements Listener {
    	
    	public AutoManagerBlockBreakEventListener( BlockBreakPriority bbPriority ) {
    		super( bbPriority );
    	}
		
		@EventHandler(priority=EventPriority.NORMAL) 
		public void onBlockBreak( BlockBreakEvent e, BlockBreakPriority bbPriority ) {
			
			if ( isDisabled( e.getBlock().getLocation().getWorld().getName() ) ||
					bbPriority.isDisabled() ) {
				return;
			}
			
			handleBlockBreakEvent( e, bbPriority );
		}

		@Override
		protected int checkBonusXp(Player player, Block block, ItemStack item) {
			return 0;
		}
	}
    

	@Override
	public void unregisterListeners() {
		
	}
	
    public void initialize() {
    	
    	// Check to see if the class BlockBreakEvent even exists:
    	try {
    		
    		Output.get().logInfo( "AutoManager: Trying to register BlockBreakEvent" );
    		
    		String eP = getMessage( AutoFeatures.blockBreakEventPriority );
    		BlockBreakPriority bbPriority = BlockBreakPriority.fromString( eP );
    		
    		setBbPriority( bbPriority );
    		
    		
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
    		Output.get().logInfo( "AutoManager: BlockBreakEvent failed to load. [%s]", e.getMessage() );
    	}
    }
    
	private void createListener( BlockBreakPriority bbPriority ) {
		
		SpigotPrison prison = SpigotPrison.getInstance();
		PluginManager pm = Bukkit.getServer().getPluginManager();
		EventPriority ePriority = bbPriority.getBukkitEventPriority();           
		
		AutoManagerBlockBreakEventListener autoManagerListener = 
							new AutoManagerBlockBreakEventListener( bbPriority );
		
		
		pm.registerEvent(BlockBreakEvent.class, autoManagerListener, ePriority,
				new EventExecutor() {
					public void execute(Listener l, Event e) {
						if ( l instanceof AutoManagerBlockBreakEventListener && 
								e instanceof BlockBreakEvent ) {
							
							((AutoManagerBlockBreakEventListener)l)
									.onBlockBreak( (BlockBreakEvent)e, getBbPriority() );
						}
					}
				},
				prison);
		
		prison.getRegisteredBlockListeners().add( autoManagerListener );
	}
    
    
//    /**
//     * <p>If one BlockBreak related event needs to be unregistered, then this function will
//     * unregisters all of them that has been registered through the auto features.  If 
//     * this function is called by different functions, the results will be the same. If
//     * they are ran back-to-back, then only the first call will remove all the Listeners
//     * and the other calls will do nothing since the source ArrayList will be emptied 
//     * and there would be nothing to remove.
//     * </p>
//     * 
//     */
//    @Override
//    public void unregisterListeners() {
//    	
////    	super.unregisterListeners();
//    }

	
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
		
		// Check to see if the class BlockBreakEvent even exists:
		try {
			
    		String eP = getMessage( AutoFeatures.blockBreakEventPriority );
    		BlockBreakPriority bbPriority = BlockBreakPriority.fromString( eP );

			String title = String.format( 
					"BlockBreakEvent (%s)", 
					( bbPriority == null ? "--none--" : bbPriority.name()) );
			
			ChatDisplay eventDisplay = Prison.get().getPlatform().dumpEventListenersChatDisplay( 
					title, 
					new SpigotHandlerList( BlockBreakEvent.getHandlerList()) );

			if ( eventDisplay != null ) {
				sb.append( eventDisplay.toStringBuilder() );
				sb.append( "\n" );
			}
			
			if ( bbPriority.isComponentCompound() ) {
				StringBuilder sbCP = new StringBuilder();
				for ( BlockBreakPriority bbp : bbPriority.getComponentPriorities() ) {
					if ( sbCP.length() > 0 ) {
						sbCP.append( ", " );
					}
					sbCP.append( "'" ).append( bbp.name() ).append( "'" );
				}
				
				String msg = String.format( "Note '%s' is a compound of: [%s]",
						bbPriority.name(),
						sbCP );
				
				sb.append( msg ).append( "\n" );
			}
		}
		catch ( Exception e ) {
			Output.get().logInfo( "AutoManager: BlockBreakEvent failed to load. [%s]", e.getMessage() );
		}
	}
    
    


    /**
     * <p>This genericBlockEvent handles the basics of a BlockBreakEvent to see if it has happened
     * within a mine or not.  If it is happening within a mine, then we process it with the doAction()
     * function.
     * </p>
     * 
     * @param e
     * @param montior Identifies that a monitor event called this function.  A monitor should only record
     * 					block break counts.
     */
	private void handleBlockBreakEvent( BlockBreakEvent e, BlockBreakPriority bbPriority ) {
			
		if ( e instanceof PrisonMinesBlockBreakEvent ) {
			return;
		}
		
		PrisonMinesBlockBreakEvent pmEvent = null;
    	long start = System.nanoTime();

		// If the event is canceled, it still needs to be processed because of the 
		// MONITOR events:
		// An event will be "canceled" and "ignored" if the block 
		// BlockUtils.isUnbreakable(), or if the mine is activly resetting.
		// The event will also be ignored if the block is outside of a mine
		// or if the targetBlock has been set to ignore all block events which 
		// means the block has already been processed.
    	MinesEventResults eventResults = ignoreMinesBlockBreakEvent( e, 
    						e.getPlayer(), e.getBlock());
    	if ( eventResults.isIgnoreEvent() ) {
    		return;
    	}

		
		// Register all external events such as mcMMO and EZBlocks:
//		OnBlockBreakExternalEvents.getInstance().registerAllExternalEvents();
		
		StringBuilder debugInfo = new StringBuilder();
		
		debugInfo.append( String.format( "&9### ** handleBlockBreakEvent ** ### " +
				"(event: BlockBreakEvent, config: %s, priority: %s, canceled: %s) ",
				bbPriority.name(),
				bbPriority.getBukkitEventPriority().name(),
				(e.isCancelled() ? "TRUE " : "FALSE")
				) );
		
		
		// Process all priorities if the event has not been canceled, and 
		// process the MONITOR priority even if the event was canceled:
    	if ( !bbPriority.isMonitor() && !e.isCancelled() || 
    			bbPriority.isMonitor() ) {

    		// Need to wrap in a Prison block so it can be used with the mines:
//    		SpigotBlock sBlock = SpigotBlock.getSpigotBlock(e.getBlock());
//    		SpigotPlayer sPlayer = new SpigotPlayer(e.getPlayer());
    		
    		BlockEventType eventType = BlockEventType.blockBreak;
    		String triggered = null;
    		
    		pmEvent = new PrisonMinesBlockBreakEvent( 
    				e.getBlock(), 
    				e.getPlayer(),
    				eventResults.getMine(),
//    					sBlock, sPlayer, 
    					bbPriority, eventType, triggered,
    					debugInfo );
    		

        	// NOTE: Check for the ACCESS priority and if someone does not have access, then return 
        	//       with a cancel on the event.  Both ACCESSBLOCKEVENTS and ACCESSMONITOR will be
        	//       converted to just ACCESS at this point, and the other part will run under either
        	//       BLOCKEVENTS or MONITOR.
        	if ( checkIfNoAccess( pmEvent, start ) ) {
        		
        		e.setCancelled( true );
        		return;
        	}
    		
    		// Validate the event. 
    		if ( !validateEvent( pmEvent ) ) {
    			
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
    			
//    			// Set the mine's PrisonBlockTypes for the block. Used to identify custom blocks.
//    			// Needed since processing of the block will lose track of which mine it came from.
//    			if ( pmEvent.getMine() != null ) {
//    				sBlock.setPrisonBlockTypes( pmEvent.getMine().getPrisonBlockTypes() );
//    			}
    			
//    			
//    			List<SpigotBlock> explodedBlocks = new ArrayList<>();
//    			pmEvent.setExplodedBlocks( explodedBlocks );
////    			String triggered = null;
//    			
//    			PrisonMinesBlockBreakEvent pmbbEvent = new PrisonMinesBlockBreakEvent( e.getBlock(), e.getPlayer(),
//    							pmEvent.getMine(), sBlock, explodedBlocks, BlockEventType.blockBreak, triggered );
//                Bukkit.getServer().getPluginManager().callEvent( pmEvent );
//                if ( pmEvent.isCancelled() ) {
//                	debugInfo.append( "(normal processing: &6PrisonMinesBlockBreakEvent was canceled by another plugin&9) " );
//                }
//                else {
//                	
//                	// Cancel drops if so configured:
//                	if ( isBoolean( AutoFeatures.cancelAllBlockEventBlockDrops ) ) {
//                		
//                		try
//                		{
//                			e.setDropItems( false );
//                		}
//                		catch ( NoSuchMethodError e1 )
//                		{
//                			String message = String.format( 
//                					"Warning: The autoFeaturesConfig.yml setting `cancelAllBlockEventBlockDrops` " +
//                					"is not valid for this version of Spigot. It's only vaid for spigot v1.12.x and higher. " +
//                					"Modify the config settings and set this value to `false`.  For now, it is temporarily " +
//                					"disabled. [%s]",
//                					e1.getMessage() );
//                			Output.get().logWarn( message );
//                			
//                			AutoFeaturesWrapper.getInstance().getAutoFeaturesConfig()
//                				.setFeature( AutoFeatures.cancelAllBlockEventBlockDrops, false );
//                		}
//                	}
//                	
//                	// doAction returns a boolean that indicates if the event should be canceled or not:
//                	if ( doAction( pmEvent, debugInfo ) ) {
////                	if ( doAction( sBlock, pmEvent.getMine(), pmEvent.getPlayer(), debugInfo ) ) {
//
//                		if ( isBoolean( AutoFeatures.cancelAllBlockBreakEvents ) ) {
//                			e.setCancelled( true );
//                		}
//                		else {
//                			
//                			debugInfo.append( "(event was not canceled) " );
//                		}
//                		
//                		// Break the blocks
//                		finalizeBreakTheBlocks( pmEvent );
//                		
//                		// Block counts and blockEvents
//                		doBlockEvents( pmEvent );
//                		
//                	}
//                	else {
//                		
//                		debugInfo.append( "&c(doAction failed without details)&9 " );
//                	}
//
//                }
    			
    			
//                debugInfo.append( "(normal processing completed) " );
//    		}
//    		else {
//    			
//    			debugInfo.append( "(logic bypass) " );
//    		}
    		
    	}

		printDebugInfo( pmEvent, start );
	}
	
	protected int checkBonusXp( Player player, Block block, ItemStack item ) {
		int bonusXp = 0;
		
		return bonusXp;
	}


}
