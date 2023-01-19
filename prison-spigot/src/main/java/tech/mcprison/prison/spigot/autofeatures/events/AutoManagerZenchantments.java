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
import tech.mcprison.prison.output.Output.DebugTarget;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.api.PrisonMinesBlockBreakEvent;
import tech.mcprison.prison.spigot.autofeatures.AutoManagerFeatures;
import tech.mcprison.prison.spigot.block.BlockBreakPriority;
import tech.mcprison.prison.spigot.game.SpigotHandlerList;
import zedly.zenchantments.BlockShredEvent;

public class AutoManagerZenchantments
	extends AutoManagerFeatures
	implements PrisonEventManager {

	private BlockBreakPriority bbPriority;
	
	public AutoManagerZenchantments() {
		super();
	}
	
	public AutoManagerZenchantments( BlockBreakPriority bbPriority ) {
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

	  
    public class AutoManagerBlockShredEventListener
	    extends AutoManagerZenchantments
	    implements Listener {
    	
    	public AutoManagerBlockShredEventListener( BlockBreakPriority bbPriority ) {
    		super( bbPriority );
    	}
	
    	@EventHandler(priority=EventPriority.NORMAL) 
    	public void onBlockShredBreak( BlockShredEvent e, BlockBreakPriority bbPriority ) {
    		
    		if ( isDisabled( e.getBlock().getLocation().getWorld().getName() ) ) {
				return;
			}
			
    		handleZenchantmentsBlockBreakEvent( e, bbPriority );
			
//    		genericBlockEventAutoManager( e );
    	}
    }
 
    
    
    @Override
    public void initialize() {
    	
    	String eP = getMessage( AutoFeatures.ZenchantmentsBlockShredEventPriority );
		BlockBreakPriority bbPriority = BlockBreakPriority.fromString( eP );
		
		setBbPriority( bbPriority );
		
//		boolean isEventEnabled = eP != null && !"DISABLED".equalsIgnoreCase( eP );

    	if ( getBbPriority() == BlockBreakPriority.DISABLED ) {
    		return;
    	}
    	
    	// Check to see if the class BlastUseEvent even exists:
    	try {
    		Output.get().logInfo( "AutoManager: checking if loaded: Zenchantments" );
    		
    		Class.forName( "zedly.zenchantments.BlockShredEvent", false, 
    				this.getClass().getClassLoader() );
    		
    		Output.get().logInfo( "AutoManager: Trying to register Zenchantments" );
    		
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
    		// Zenchantments is not loaded... so ignore.
    		Output.get().logInfo( "AutoManager: Zenchantments is not loaded" );
    	}
    	catch ( Exception e ) {
    		Output.get().logInfo( "AutoManager: Zenchantments failed to load. [%s]", e.getMessage() );
    	}
    }

	private void createListener( BlockBreakPriority bbPriority ) {
		
		SpigotPrison prison = SpigotPrison.getInstance();
		PluginManager pm = Bukkit.getServer().getPluginManager();
		EventPriority ePriority = bbPriority.getBukkitEventPriority();    
		
		AutoManagerBlockShredEventListener autoManagerListener = 
							new AutoManagerBlockShredEventListener( bbPriority );
		
		pm.registerEvent(BlockShredEvent.class, autoManagerListener, ePriority,
				new EventExecutor() {
			public void execute(Listener l, Event e) { 
				if ( l instanceof AutoManagerBlockShredEventListener && 
						e instanceof BlockShredEvent ) {
							
							AutoManagerBlockShredEventListener lmon = 
														(AutoManagerBlockShredEventListener) l;
							
							BlockShredEvent event = (BlockShredEvent) e;
							lmon.onBlockShredBreak( event, getBbPriority() );
						}
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
 
    	String eP = getMessage( AutoFeatures.ZenchantmentsBlockShredEventPriority );
		boolean isEventEnabled = eP != null && !"DISABLED".equalsIgnoreCase( eP );
    	
    	if ( !isEventEnabled ) {
    		return;
    	}
    	
    	// Check to see if the class BlastUseEvent even exists:
    	try {
    		
    		Class.forName( "zedly.zenchantments.BlockShredEvent", false, 
    				this.getClass().getClassLoader() );
    		
    		BlockBreakPriority bbPriority = BlockBreakPriority.fromString( eP );
			
			String title = String.format( 
					"BlockShredEvent (%s)", 
					( bbPriority == null ? "--none--" : bbPriority.name()) );

    		
    		ChatDisplay eventDisplay = Prison.get().getPlatform().dumpEventListenersChatDisplay( 
    				title, 
    				new SpigotHandlerList( BlockShredEvent.getHandlerList()) );
    		
    		if ( eventDisplay != null ) {
    			sb.append( eventDisplay.toStringBuilder() );
    			sb.append( "NOTE: Zenchantments uses the same HandlerList as BlockBreakEvent so " +
    					"listeners are combined due to this bug.\n" );
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
    	catch ( ClassNotFoundException e ) {
    		// Zenchantments is not loaded... so ignore.
    	}
    	catch ( Exception e ) {
    		Output.get().logInfo( "AutoManager: zenchantments failed to load. [%s]", e.getMessage() );
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
	private void handleZenchantmentsBlockBreakEvent( BlockBreakEvent e, BlockBreakPriority bbPriority ) {
			
//			boolean monitor, boolean blockEventsOnly, 
//			boolean autoManager ) {
		
		if ( e instanceof PrisonMinesBlockBreakEvent ) {
			return;
		}
		
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
//    	if ( ignoreMinesBlockBreakEvent( e, e.getPlayer(), e.getBlock()) ) {
//    		return;
//    	}

		
		// Register all external events such as mcMMO and EZBlocks:
//		OnBlockBreakExternalEvents.getInstance().registerAllExternalEvents();
		
		StringBuilder debugInfo = new StringBuilder();
		
		debugInfo.append( String.format( "### ** handleZenchantmentsBlockBreakEvent ** ### " +
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
    		
    		PrisonMinesBlockBreakEvent pmEvent = new PrisonMinesBlockBreakEvent( 
    						e.getBlock(), 
    						e.getPlayer(),
    						eventResults.getMine(),
//    						sBlock, sPlayer, 
    						bbPriority, eventType, triggered,
        					debugInfo );
    		
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

    		else {
    			
    			// This is where the processing actually happens:
    			
    			
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
    	
		if ( debugInfo.length() > 0 ) {
			
			long stop = System.nanoTime();
			debugInfo.append( " [" ).append( (stop - start) / 1000000d ).append( " ms]" );
			
			Output.get().logDebug( DebugTarget.blockBreak, debugInfo.toString() );
		}
	}


//	private EventListenerCancelBy processBlockBreaks(PrisonMinesBlockBreakEvent pmEvent, 
//			SpigotBlock sBlock, StringBuilder debugInfo ) {
//		
//		EventListenerCancelBy cancelBy = EventListenerCancelBy.none;
//		
//		// This is where the processing actually happens:
//		if ( pmEvent.getMine() != null || pmEvent.getMine() == null && 
//				!isBoolean( AutoFeatures.pickupLimitToMines ) ) {
//			debugInfo.append( "(normal processing initiating) " );
//			
//			// Set the mine's PrisonBlockTypes for the block. Used to identify custom blocks.
//			// Needed since processing of the block will lose track of which mine it came from.
//			if ( pmEvent.getMine() != null ) {
//				sBlock.setPrisonBlockTypes( pmEvent.getMine().getPrisonBlockTypes() );
//			}
//			
//			// check all external events such as mcMMO and EZBlocks:
////			debugInfo.append( 
////					OnBlockBreakExternalEvents.getInstance().checkAllExternalEvents( e ) );
//			
//			List<SpigotBlock> explodedBlocks = new ArrayList<>();
//			pmEvent.setExplodedBlocks( explodedBlocks );
////    			String triggered = null;
//			
////    			PrisonMinesBlockBreakEvent pmbbEvent = new PrisonMinesBlockBreakEvent( e.getBlock(), e.getPlayer(),
////    							pmEvent.getMine(), sBlock, explodedBlocks, BlockEventType.blockBreak, triggered );
//			Bukkit.getServer().getPluginManager().callEvent( pmEvent );
//			if ( pmEvent.isCancelled() ) {
//				debugInfo.append( "(normal processing: PrisonMinesBlockBreakEvent was canceled by another plugin!) " );
//			}
//			else {
//				
//				// Cancel drops if so configured:
//				if ( isBoolean( AutoFeatures.cancelAllBlockEventBlockDrops ) ) {
//					
//					cancelBy = EventListenerCancelBy.drops;
//					
//				}
//				
//				// doAction returns a boolean that indicates if the event should be canceled or not:
//				if ( doAction( pmEvent, debugInfo ) ) {
////                	if ( doAction( sBlock, pmEvent.getMine(), pmEvent.getPlayer(), debugInfo ) ) {
//					
//					if ( isBoolean( AutoFeatures.cancelAllBlockBreakEvents ) ) {
//						cancelBy = EventListenerCancelBy.event;
//					}
//					else {
//						
//						debugInfo.append( "(event was not canceled) " );
//					}
//					
//					finalizeBreakTheBlocks( pmEvent );
//					
//					doBlockEvents( pmEvent );
//					
//				}
//				else {
//					
//					debugInfo.append( "(doAction failed without details) " );
//				}
//				
//			}
//			
//			
//			debugInfo.append( "(normal processing completed) " );
//		}
//		else {
//			
//			debugInfo.append( "(logic bypass) " );
//		}
//		return cancelBy;
//	}
	
	@Override
	protected int checkBonusXp( Player player, Block block, ItemStack item ) {
		int bonusXp = 0;
		
		return bonusXp;
	}
}
