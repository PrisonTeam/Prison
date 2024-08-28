package tech.mcprison.prison.spigot.autofeatures.events;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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

import me.pulsi_.prisonenchants.events.PEExplosionEvent;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.autofeatures.AutoFeaturesWrapper;
import tech.mcprison.prison.mines.features.MineBlockEvent.BlockEventType;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.api.PrisonMinesBlockBreakEvent;
import tech.mcprison.prison.spigot.autofeatures.AutoManagerFeatures;
import tech.mcprison.prison.spigot.block.BlockBreakPriority;

/**
 * <p>This supports Pulsi's plugin, PrisonEnchants.  There are three different versions
 * and prison supports them all.
 * </p>
 * 
 * <ul>
 * 		<li>v1.x - Older versions of PEExplosionEvent. 
 * 				Uses functions: getBlockBroken() and getExplodedBlocks().</li>
 * 		<li>v2.0.0 through v2.2.0 - Uses functions: getBlocks().  Does not 
 * 				have the original broken block.</li>
 * 		<li>v2.2.1 + - Uses functions: getBlocks() and 
 * 
 * </ul>
 */
public class AutoManagerPrisonEnchants
	extends AutoManagerFeatures 
	implements PrisonEventManager {
	
	private PEExplosionEventVersion peApiVersion;
	
	private BlockBreakPriority bbPriority;
	
	public enum PEExplosionEventVersion {
		undefined,
		pev1_0_0,
		pev2_0_0,
		pev2_2_1;
	}
	
	public AutoManagerPrisonEnchants() {
		super();
	}
	
	public AutoManagerPrisonEnchants( BlockBreakPriority bbPriority ) {
		super();
		
		this.peApiVersion = PEExplosionEventVersion.undefined;
		
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

	/**
	 * For Pulsi_'s PrisonEnchants plugin:
	 *
	 */
	public class AutoManagerPEExplosiveEventListener 
		extends AutoManagerPrisonEnchants
		implements Listener {
		
    	public AutoManagerPEExplosiveEventListener( BlockBreakPriority bbPriority ) {
    		super( bbPriority );
    	}
    	
		@EventHandler(priority=EventPriority.NORMAL) 
		public void onPrisonEnchantsExplosiveEvent( PEExplosionEvent e, BlockBreakPriority bbPriority ) {
			
			Block block = getBlock( e );
			
			if ( isDisabled( block.getLocation().getWorld().getName() ) ||
					bbPriority.isDisabled() ) {
				return;
			}

			
//			me.pulsi_.prisonenchants.events.PEExplosionEvent
			
			handlePEExplosionEvent( e, bbPriority );
			
//			genericBlockExplodeEventAutoManager( e );
		}
	}
	

	@SuppressWarnings("unused")
	@Override
	public void initialize() {

		String eP = getMessage( AutoFeatures.PrisonEnchantsExplosiveEventPriority );
		BlockBreakPriority bbPriority = BlockBreakPriority.fromString( eP );
		
		setBbPriority( bbPriority );
		
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
			
//			{
//				// PrisonEnchants-API-v1.0.0:
//				//me.pulsi_.prisonenchants.events.PEExplosionEvent
//		
//				PEExplosionEvent peEE = new PEExplosionEvent();
//				Block block = peEE.getBlockBroken();
//				String eventName = peEE.getEventName();
//				List<Block> explodedBlocks = peEE.getExplodedBlocks();
//				HandlerList handlers = peEE.getHandlers();
//				HandlerList handlerList = peEE.getHandlerList();
//				Player player = peEE.getPlayer();
//				boolean async = peEE.isAsynchronous();
//				boolean canceled = peEE.isCancelled();
//				peEE.setCancelled(false);
//				
//			}
//			{
//				// PrisonEnchants-API-v2.2.0:
//				//me.pulsi_.prisonenchants.events.PEExplosionEvent
//		
//				PEExplosionEvent peEE = new PEExplosionEvent();
//				List<Block> blocks = peEE.getBlocks();
//				PEEnchant enchantSource = peEE.getEnchantSource(); // not needed
//				String eventName = peEE.getEventName();
//				HandlerList handlers = peEE.getHandlers();
//				HandlerList handlerList = peEE.getHandlerList();
//				Player player = peEE.getPlayer();
//				boolean async = peEE.isAsynchronous();
//				boolean canceled = peEE.isCancelled();
//				peEE.setBlocks( blocks );
//				peEE.setCancelled(false);
//			}
//			{
//				// PrisonEnchants-API-v2.2.1:
//				//me.pulsi_.prisonenchants.events.PEExplosionEvent
//				NOTE: v2.2.1 adds the function getOrigin();
//				
//				Location locationOfOriginalBlock = peEE.getOrigin();
//			}
			
			
			
			
			Class<?> klass = PEExplosionEvent.class;
			if ( hasMethod( "getBlockBroken", klass ) &&
				 hasMethod( "getExplodedBlocks", klass ) ) {
				
					 setPeApiVersion( PEExplosionEventVersion.pev1_0_0 );
			}
			else if ( hasMethod( "getBlocks", klass ) &&
					  !hasMethod( "getOrigin", klass ) ) {
				
				setPeApiVersion( PEExplosionEventVersion.pev2_0_0 );
			}
			else if ( hasMethod( "getBlocks", klass ) &&
					hasMethod( "getOrigin", klass ) ) {
				
				setPeApiVersion( PEExplosionEventVersion.pev2_2_1 );
			}

			
			String msg = "";
			if ( getPeApiVersion() == PEExplosionEventVersion.undefined ) {
				msg = "&cWarning: AutoFeatures has been configured to use Pulsi's "
						+ "PrisonEnchant's PEExplosionEvent but the plugin is not "
						+ "loaded or active.";
			}
			else if ( getPeApiVersion() == PEExplosionEventVersion.pev2_2_1 ) {
				msg = "&6PEExplosionEvent API based on v2.2.1 or newer has been found and registered.";
			}
			else {
				msg = "&6PEExplosionEvent API has been found and registered, but it is "
						+ "out of date.  &cPlease upgrade PrisonEnchants to the lastest release "
						+ "for best results. &66https://polymart.org/resource/prisonenchants.1434";
			}

			Output.get().logWarn( msg );
			
			
			
			
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
			// PrisonEnchants is not loaded... so ignore.
			Output.get().logInfo( "AutoManager: Pulsi_'s PrisonEnchants is not loaded" );
		}
		catch ( Exception e ) {
			Output.get().logInfo( "AutoManager: Pulsi_'s PrisonEnchants failed to load. [%s]", e.getMessage() );
		}
	}

	/**
	 * <p>Checks a Class to see if the given function exists. 
	 * Based upon our own specific needs with this, we are just checking
	 * setters so no parameters need to be specified.
	 * </p>
	 * 
	 * @param methodName
	 * @param klass
	 * @return
	 */
	private boolean hasMethod( String methodName, Class<?> klass ) {
		boolean results = false;
		
		try {
			Method method = klass.getMethod( methodName, (Class<?>[]) null);
			
			results = ( method != null );
		} 
		catch (NoSuchMethodException | SecurityException e) {
		  // Ignore exceptions... 
		}
		
		return results;
	}
	
	private Block getBlock( PEExplosionEvent event ) {
		Block results = null;
		
		if ( getPeApiVersion() == PEExplosionEventVersion.pev1_0_0 ) {
			
			results = event.getBlockBroken();
		}
		else if ( getPeApiVersion() == PEExplosionEventVersion.pev2_0_0 ) {
			
			results = event.getBlocks().size() > 0 ? 
					event.getBlocks().get(0) : null;
		}
		else if ( getPeApiVersion() == PEExplosionEventVersion.pev2_2_1 ) {

			Location bLocation = event.getOrigin();
			results = bLocation.getWorld().getBlockAt(bLocation);
		}
		
		return results;
	}
	
	private List<Block> getBlocks( PEExplosionEvent event ) {
		List<Block> results = new ArrayList<>();
		
		if ( getPeApiVersion() == PEExplosionEventVersion.pev1_0_0 ) {
			
			results.addAll( event.getExplodedBlocks() );
		}
		else if ( getPeApiVersion() == PEExplosionEventVersion.pev2_0_0 ) {
			
			results.addAll( event.getBlocks().subList(1, event.getBlocks().size()));
		}
		else if ( getPeApiVersion() == PEExplosionEventVersion.pev2_2_1 ) {
			
			results.addAll( event.getBlocks() );
		}
		
		return results;
	}
	
	private void createListener(BlockBreakPriority bbPriority) {
		
		SpigotPrison prison = SpigotPrison.getInstance();
		PluginManager pm = Bukkit.getServer().getPluginManager();
		EventPriority ePriority = bbPriority.getBukkitEventPriority(); 
		
		AutoManagerPEExplosiveEventListener autoManagerListener = 
				new AutoManagerPEExplosiveEventListener( bbPriority );
		
		pm.registerEvent(PEExplosionEvent.class, autoManagerListener, ePriority,
				new EventExecutor() {
			public void execute(Listener l, Event e) { 
				
				PEExplosionEvent peeEvent = (PEExplosionEvent) e;
				
				((AutoManagerPEExplosiveEventListener)l)
				.onPrisonEnchantsExplosiveEvent( peeEvent, getBbPriority() );
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
		
		String eP = getMessage( AutoFeatures.PrisonEnchantsExplosiveEventPriority );
		boolean isEventEnabled = eP != null && !"DISABLED".equalsIgnoreCase( eP );

    	if ( !isEventEnabled ) {
    		return;
    	}
		
		// Check to see if the class ExplosiveEvent even exists:
		try {
			
			Class.forName( "me.pulsi_.prisonenchants.events.PEExplosionEvent", false, 
							this.getClass().getClassLoader() );
			
			
			HandlerList handlers = PEExplosionEvent.getHandlerList();
			
//    		String eP = getMessage( AutoFeatures.blockBreakEventPriority );
    		BlockBreakPriority bbPriority = BlockBreakPriority.fromString( eP );

    		dumpEventListenersCore( "Pulsi_'s PEExplosionEvent", handlers, bbPriority, sb );
    		
			
//    		BlockBreakPriority bbPriority = BlockBreakPriority.fromString( eP );
//			
//			String title = String.format( 
//					"Pulsi_'s PEExplosionEvent (%s)", 
//					( bbPriority == null ? "--none--" : bbPriority.name()));
//			
//			ChatDisplay eventDisplay = Prison.get().getPlatform().dumpEventListenersChatDisplay( 
//					title, 
//					new SpigotHandlerList( PEExplosionEvent.getHandlerList()) );
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
		
		PrisonMinesBlockBreakEvent pmEvent = null;
		long start = System.nanoTime();
		
		// If the event is canceled, it still needs to be processed because of the 
		// MONITOR events:
		// An event will be "canceled" and "ignored" if the block 
		// BlockUtils.isUnbreakable(), or if the mine is actively resetting.
		// The event will also be ignored if the block is outside of a mine
		// or if the targetBlock has been set to ignore all block events which 
		// means the block has already been processed.
		
		// NOTE: support for v1.0, v2.2, and v2.2.1 has different block structures:
		Block bBlock = getBlock( e );
		
    	MinesEventResults eventResults = ignoreMinesBlockBreakEvent( e, 
    			e.getPlayer(), bBlock,
    			bbPriority, true );
    	
    	if ( eventResults.isIgnoreEvent() ) {
    		return;
    	}
				
		StringBuilder debugInfo = new StringBuilder();
		
		
		debugInfo.append( String.format( "&6### ** handlePEEExplosionEvent (Pulsi) ** ###&3 " +
				"(event: &6PEExplosionEvent&3, config: %s, priority: %s, canceled: %s) ",
				bbPriority.name(),
				bbPriority.getBukkitEventPriority().name(),
				(e.isCancelled() ? "TRUE " : "FALSE")
				) );
		
		debugInfo.append( eventResults.getDebugInfo() );
		
		
		// Process all priorities if the event has not been canceled, and 
		// process the MONITOR priority even if the event was canceled:
    	if ( !bbPriority.isMonitor() && !e.isCancelled() || 
    			bbPriority.isMonitor() ) {

    		
    		BlockEventType eventType = BlockEventType.PEExplosive;
    		String triggered = null; // e.getTriggeredBy();
    		
    		pmEvent = new PrisonMinesBlockBreakEvent( 
    					eventResults,
//    					e.getBlockBroken(), 
//    					e.getPlayer(),
//    					eventResults.getMine(),
//    					bbPriority, 
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
        	
        	List<Block> blocks = getBlocks( e );
        	
    		pmEvent.setUnprocessedRawBlocks( blocks );
    		
    		
    		// Check to see if the blockConverter's EventTrigger should have
    		// it's blocks suppressed from explosion events.  If they should be
    		// removed, then it's removed within this function.
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
    			
//    			debugInfo.append( "(normal processing initiating) " );
    			
    			// check all external events such as mcMMO and EZBlocks:
//    			if ( e instanceof BlockBreakEvent ) {
//    				processPMBBExternalEvents( pmEvent, debugInfo, e );
//    			}
    			
    			
    			EventListenerCancelBy cancelBy = EventListenerCancelBy.none; 
    			
    			cancelBy = processPMBBEvent( pmEvent );

    			
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
		
		return bonusXp;
	}

	public PEExplosionEventVersion getPeApiVersion() {
		return peApiVersion;
	}
	public void setPeApiVersion(PEExplosionEventVersion peApiVersion) {
		this.peApiVersion = peApiVersion;
	}


}
