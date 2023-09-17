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

import me.badbones69.crazyenchantments.api.events.BlastUseEvent;
import tech.mcprison.prison.autofeatures.AutoFeaturesWrapper;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.mines.features.MineBlockEvent.BlockEventType;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.api.PrisonMinesBlockBreakEvent;
import tech.mcprison.prison.spigot.autofeatures.AutoManagerFeatures;
import tech.mcprison.prison.spigot.block.BlockBreakPriority;
import tech.mcprison.prison.spigot.integrations.IntegrationCrazyEnchantmentsPickaxes;

public class AutoManagerCrazyEnchants
	extends AutoManagerFeatures
	implements PrisonEventManager
{
	private BlockBreakPriority bbPriority;
	
	private Boolean crazyEnchantEnabled;
	
	public AutoManagerCrazyEnchants() {
		super();
		
		this.crazyEnchantEnabled = null;
	}
	
	
	public AutoManagerCrazyEnchants( BlockBreakPriority bbPriority ) {
		super();
		
		this.crazyEnchantEnabled = null;
		
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

		
	public class AutoManagerBlastUseEventListener
		extends AutoManagerCrazyEnchants
		implements Listener {
    	
    	public AutoManagerBlastUseEventListener( BlockBreakPriority bbPriority ) {
    		super( bbPriority );
    	}
		
		@EventHandler(priority=EventPriority.NORMAL) 
		public void onCrazyEnchantsBlockExplode( BlastUseEvent e, BlockBreakPriority bbPriority) {
	
			if ( isDisabled( e.getPlayer().getLocation().getWorld().getName() ) ||
					bbPriority.isDisabled() ) {
				return;
			}
			
			handleBlastUseEvent( e, bbPriority );
		}
	}
    

	@Override
	public void initialize() {

		String eP = getMessage( AutoFeatures.CrazyEnchantsBlastUseEventPriority );
		
		BlockBreakPriority bbPriority = BlockBreakPriority.fromString( eP );
		setBbPriority( bbPriority );
		
//		boolean isEventEnabled = eP != null && !"DISABLED".equalsIgnoreCase( eP );
		
		if ( bbPriority == BlockBreakPriority.DISABLED ) {
			return;
		}
		
		// Check to see if the class BlastUseEvent even exists:
		try {
			Output.get().logInfo( "AutoManager: checking if loaded: CrazyEnchants" );
			
			Class.forName( "me.badbones69.crazyenchantments.api.events.BlastUseEvent", false, 
					this.getClass().getClassLoader() );
			
			Output.get().logInfo( "AutoManager: Trying to register CrazyEnchants" );
			
			
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
			Output.get().logInfo( "AutoManager: CrazyEnchants is not loaded" );
		}
		catch ( Exception e ) {
			Output.get().logInfo( "AutoManager: CrazyEnchants failed to load. [%s]", e.getMessage() );
		}
	}


	private void createListener(BlockBreakPriority bbPriority) {
		
		SpigotPrison prison = SpigotPrison.getInstance();
		PluginManager pm = Bukkit.getServer().getPluginManager();
		EventPriority ePriority = bbPriority.getBukkitEventPriority(); 
		
		
		AutoManagerBlastUseEventListener autoManagerListener = 
				new AutoManagerBlastUseEventListener( bbPriority );
		
		pm.registerEvent(BlastUseEvent.class, autoManagerListener, ePriority,
				new EventExecutor() {
					public void execute(Listener l, Event e) { 
						
						BlastUseEvent buEvent = (BlastUseEvent) e;
						
						((AutoManagerBlastUseEventListener)l)
										.onCrazyEnchantsBlockExplode( buEvent, getBbPriority() );
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
		
		String eP = getMessage( AutoFeatures.CrazyEnchantsBlastUseEventPriority );
		boolean isEventEnabled = eP != null && !"DISABLED".equalsIgnoreCase( eP );

		if ( !isEventEnabled ) {
			return;
		}
		
		// Check to see if the class BlastUseEvent even exists:
		try {
			
			Class.forName( "me.badbones69.crazyenchantments.api.events.BlastUseEvent", false, 
					this.getClass().getClassLoader() );
			
			
			HandlerList handlers = BlastUseEvent.getHandlerList();
			
//    		String eP = getMessage( AutoFeatures.blockBreakEventPriority );
    		BlockBreakPriority bbPriority = BlockBreakPriority.fromString( eP );

    		dumpEventListenersCore( "BlastUseEvent", handlers, bbPriority, sb );
    		
			
//    		BlockBreakPriority bbPriority = BlockBreakPriority.fromString( eP );
//
//			
//			String title = String.format( 
//					"BlastUseEvent (%s)", 
//					( bbPriority == null ? "--none--" : bbPriority.name()) );
//			
//			ChatDisplay eventDisplay = Prison.get().getPlatform().dumpEventListenersChatDisplay( 
//					title, 
//					new SpigotHandlerList( BlastUseEvent.getHandlerList()) );
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
		catch ( ClassNotFoundException e ) {
			// CrazyEnchants is not loaded... so ignore.
		}
		catch ( Exception e ) {
			Output.get().logInfo( "AutoManager: CrazyEnchants failed to load. [%s]", e.getMessage() );
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
	public void handleBlastUseEvent( BlastUseEvent e, BlockBreakPriority bbPriority ) {
			
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
    							e.getPlayer(), e.getBlockList().get( 0 ),
    							bbPriority, true );
    	
    	if ( eventResults.isIgnoreEvent() ) {
    		return;
    	}
				
		StringBuilder debugInfo = new StringBuilder();
		
		debugInfo.append( String.format( "### ** handleBlastUseEvent ** ### " +
				"(event: BlastUseEvent, config: %s, priority: %s, canceled: %s) ",
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
    			e.getBlockList().size() > 0 ) {

    		
//    		Block bukkitBlock = e.getBlockList().get( 0 );
    		
    		BlockEventType eventType = BlockEventType.CEXplosion;
    		String triggered = null;
    		

    		pmEvent = new PrisonMinesBlockBreakEvent( 
    				eventResults,
//    				bukkitBlock, 
//    				e.getPlayer(),
//    				eventResults.getMine(),
//   					bbPriority, 
    				eventType, 
    				triggered,
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
    		
    		for ( int i = 1; i < e.getBlockList().size(); i++ ) {
    			pmEvent.getUnprocessedRawBlocks().add( e.getBlockList().get( i ) );
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
    		

    		// now process all blocks (non-monitor):
    		else {
    			
    			// This is where the processing actually happens:
    			
//    			if ( e instanceof BlockBreakEvent ) {
//    				processPMBBExternalEvents( pmEvent, debugInfo, e );
//    			}
    			
    			
    			
    			EventListenerCancelBy cancelBy = EventListenerCancelBy.none; 
    			
    			cancelBy = processPMBBEvent( pmEvent );

    			
    			// NOTE: you cannot cancel a crazy enchant's drops, so this will 
    			//       always cancel the event.
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
    				

		}
    	
    	printDebugInfo( pmEvent, start );
	}

	@Override
	protected int checkBonusXp( Player player, Block block, ItemStack item ) {
		int bonusXp = 0;
		
		try {
			if ( isCrazyEnchantEnabled() == null ) {
				Class.forName( 
						"tech.mcprison.prison.spigot.integrations.IntegrationCrazyEnchantmentsPickaxes", false, 
						this.getClass().getClassLoader() );
				setCrazyEnchantEnabled( Boolean.TRUE );
			}
			
			if ( isCrazyEnchantEnabled() != null && isCrazyEnchantEnabled().booleanValue() && 
					item != null && IntegrationCrazyEnchantmentsPickaxes.getInstance().isEnabled() ) {
				
				bonusXp = IntegrationCrazyEnchantmentsPickaxes.getInstance()
						.getPickaxeEnchantmentExperienceBonus( player, block, item );
			}
		}
		catch ( NoClassDefFoundError | Exception e ) {
			setCrazyEnchantEnabled( Boolean.FALSE );
		}
		
		return bonusXp;
	}
	
	public Boolean isCrazyEnchantEnabled() {
		return crazyEnchantEnabled;
	}
	public void setCrazyEnchantEnabled( Boolean crazyEnchantEnabled ) {
		this.crazyEnchantEnabled = crazyEnchantEnabled;
	}
	
}
