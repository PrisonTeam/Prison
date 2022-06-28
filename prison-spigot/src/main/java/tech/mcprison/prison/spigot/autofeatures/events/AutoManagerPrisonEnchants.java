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

import me.pulsi_.prisonenchants.events.PEExplosionEvent;
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
import tech.mcprison.prison.spigot.block.OnBlockBreakExternalEvents;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.spigot.game.SpigotHandlerList;
import tech.mcprison.prison.spigot.game.SpigotPlayer;

public class AutoManagerPrisonEnchants
	extends AutoManagerFeatures 
	implements PrisonEventManager {
	
	private BlockBreakPriority bbPriority;
	
	public AutoManagerPrisonEnchants() {
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

	/**
	 * For Pulsi_'s PrisonEnchants plugin:
	 *
	 */
	public class AutoManagerPEExplosiveEventListener 
		extends AutoManagerPrisonEnchants
		implements Listener {
		
		@EventHandler(priority=EventPriority.NORMAL) 
		public void onPrisonEnchantsExplosiveEvent( PEExplosionEvent e, BlockBreakPriority bbPriority ) {
			
			if ( isDisabled( e.getBlockBroken().getLocation().getWorld().getName() ) ) {
				return;
			}

			
//			me.pulsi_.prisonenchants.events.PEExplosionEvent
			
			handlePEExplosionEvent( e, bbPriority );
			
//			genericBlockExplodeEventAutoManager( e );
		}
	}
	

	@Override
	public void initialize() {

		String eP = getMessage( AutoFeatures.PrisonEnchantsExplosiveEventPriority );
		setBbPriority( BlockBreakPriority.fromString( eP ) );
		
//		boolean isEventEnabled = eP != null && !"DISABLED".equalsIgnoreCase( eP );

		if ( getBbPriority() == BlockBreakPriority.DISABLED ) {
			return;
		}
		
		// Check to see if the class ExplosiveEvent even exists:
		try {
			Output.get().logInfo( "AutoManager: checking if loaded: Pulsi_'s PrisonEnchants" );
			
			Class.forName( "me.pulsi_.prisonenchants.events.PEExplosionEvent", false, 
					this.getClass().getClassLoader() );
			
			Output.get().logInfo( "AutoManager: Trying to register Pulsi_'s PrisonEnchants" );
			
			
			
			
			SpigotPrison prison = SpigotPrison.getInstance();
			PluginManager pm = Bukkit.getServer().getPluginManager();
			EventPriority ePriority = getBbPriority().getBukkitEventPriority(); 
			
			AutoManagerPEExplosiveEventListener autoManagerlListener = 
					new AutoManagerPEExplosiveEventListener();
			
			pm.registerEvent(PEExplosionEvent.class, autoManagerlListener, ePriority,
					new EventExecutor() {
				public void execute(Listener l, Event e) { 
					
					PEExplosionEvent peeEvent = (PEExplosionEvent) e;
					
					((AutoManagerPEExplosiveEventListener)l)
					.onPrisonEnchantsExplosiveEvent( peeEvent, getBbPriority() );
				}
			},
					prison);
			prison.getRegisteredBlockListeners().add( autoManagerlListener );
			
			
			
		}
		catch ( ClassNotFoundException e ) {
			// PrisonEnchants is not loaded... so ignore.
			Output.get().logInfo( "AutoManager: Pulsi_'s PrisonEnchants is not loaded" );
		}
		catch ( Exception e ) {
			Output.get().logInfo( "AutoManager: Pulsi_'s PrisonEnchants failed to load. [%s]", e.getMessage() );
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
		
		String eP = getMessage( AutoFeatures.PrisonEnchantsExplosiveEventPriority );
		boolean isEventEnabled = eP != null && !"DISABLED".equalsIgnoreCase( eP );

    	if ( !isEventEnabled ) {
    		return;
    	}
		
		// Check to see if the class ExplosiveEvent even exists:
		try {
			
			Class.forName( "me.pulsi_.prisonenchants.events.PEExplosionEvent", false, 
							this.getClass().getClassLoader() );
			
    		BlockBreakPriority bbPriority = BlockBreakPriority.fromString( eP );
			
			String title = String.format( 
					"Pulsi_'s PEExplosionEvent (%s)", 
					( bbPriority == null ? "--none--" : bbPriority.name()));
			
			ChatDisplay eventDisplay = Prison.get().getPlatform().dumpEventListenersChatDisplay( 
					title, 
					new SpigotHandlerList( PEExplosionEvent.getHandlerList()) );

			if ( eventDisplay != null ) {
				sb.append( eventDisplay.toStringBuilder() );
				sb.append( "\n" );
			}
		}
		catch ( ClassNotFoundException e ) {
			// PrisonEnchants is not loaded... so ignore.
		}
		catch ( Exception e ) {
			Output.get().logInfo( "AutoManager: PrisonEnchants failed to load. [%s]", e.getMessage() );
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
	public void handlePEExplosionEvent( PEExplosionEvent e, BlockBreakPriority bbPriority) {
		
		
//	, boolean monitor, boolean blockEventsOnly, 
//			boolean autoManager ) {

		long start = System.nanoTime();
		
    	if ( e.isCancelled() ||  processMinesBlockBreakEvent( e, e.getPlayer(), e.getBlockBroken()) ) {
    		return;
    	}
    	

		// Register all external events such as mcMMO and EZBlocks:
		OnBlockBreakExternalEvents.getInstance().registerAllExternalEvents();
				
		StringBuilder debugInfo = new StringBuilder();
		
		
		debugInfo.append( String.format( "### ** genericExplosiveEvent (Pulsi) ** ### " +
				"(event: PEExplosionEvent, config: %s, priority: %s, canceled: %s) ",
				bbPriority.name(),
				bbPriority.getBukkitEventPriority().name(),
				(e.isCancelled() ? "TRUE " : "FALSE")
				) );
		
		
    	if ( bbPriority != BlockBreakPriority.MONITOR && !e.isCancelled() || bbPriority == BlockBreakPriority.MONITOR ) {


    		
	    	String eP = getMessage( AutoFeatures.PrisonEnchantsExplosiveEventPriority );
			boolean isPEExplosiveEnabled = eP != null && !"DISABLED".equalsIgnoreCase( eP );


    		// Need to wrap in a Prison block so it can be used with the mines:
    		SpigotBlock sBlock = SpigotBlock.getSpigotBlock(e.getBlockBroken());
    		SpigotPlayer sPlayer = new SpigotPlayer(e.getPlayer());
    		
    		BlockEventType eventType = BlockEventType.PEExplosive;
    		String triggered = null; // e.getTriggeredBy();
    		
    		PrisonMinesBlockBreakEvent pmEvent = new PrisonMinesBlockBreakEvent( e.getBlockBroken(), e.getPlayer(),
    					sBlock, sPlayer, bbPriority, eventType, triggered );
    		
    		pmEvent.setUnprocessedRawBlocks( e.getExplodedBlocks() );
    		
    		
    		if ( !validateEvent( pmEvent, debugInfo ) ) {
    			
    			// The event has not passed validation. All logging and Errors have been recorded
    			// so do nothing more. This is to just prevent normal processing from occurring.
    			
    			if ( pmEvent.isCancelOriginalEvent() ) {
    				
    				e.setCancelled( true );
    			}
    		}

    		
    		else if ( pmEvent.getBbPriority() == BlockBreakPriority.MONITOR ) {
    			// Stop here, and prevent additional processing. Monitors should never process the event beyond this.
    		}
    		


    		// now process all blocks (non-monitor):
    		else if ( isPEExplosiveEnabled && 
    				( pmEvent.getMine() != null || pmEvent.getMine() == null && !isBoolean( AutoFeatures.pickupLimitToMines )) ) {
    			if ( pmEvent.getExplodedBlocks().size() > 0 ) {
    				
//					String triggered = null;
    				
					
//	    			PrisonMinesBlockBreakEvent pmbbEvent = new PrisonMinesBlockBreakEvent( dummyBlock.getWrapper(), e.getPlayer(),
//	    												mine, dummyBlock, explodedBlocks, BlockEventType.PEExplosive, triggered );
	                Bukkit.getServer().getPluginManager().callEvent(pmEvent);
	                if ( pmEvent.isCancelled() ) {
	                	debugInfo.append( "(normal processing: PrisonMinesBlockBreakEvent was canceled) " );
	                }
	                else {
	                	
//	                	// Cancel drops if so configured:
//	                	if ( isBoolean( AutoFeatures.cancelAllBlockEventBlockDrops ) ) {
//	                		
//	                		try
//	                		{
//	                			e.setDropItems( false );
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
