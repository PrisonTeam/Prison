package tech.mcprison.prison.spigot.block;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventException;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.RegisteredListener;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.autofeatures.AutoFeaturesWrapper;
import tech.mcprison.prison.spigot.spiget.BluesSpigetSemVerComparator;

public class OnBlockBreakExternalEvents {
	
	private static final OnBlockBreakExternalEvents instance = new OnBlockBreakExternalEvents();
	private boolean isDropItemsSupported = false;
	
//	private boolean isWorldGuardChecked = false;
//	private RegisteredListener registeredListenerWorldGuard = null; 
//	private Method worldGuardOnBlockBreakMethod;
	
	private boolean isMCMMOChecked = false;
	private RegisteredListener registeredListenerMCMMO = null; 
	
	private boolean isEZBlockChecked = false;
	private RegisteredListener registeredListenerEZBlock = null; 
	
	private boolean isQuestsChecked = false;
	private RegisteredListener registeredListenerQuests = null; 
	
	
	private boolean setup = false;
	
	private AutoFeaturesWrapper autoFeatureWrapper = null;
	
	
	private OnBlockBreakExternalEvents() {
		super();
		
		// if mc version is greater than or equal to 1.13.0.
		if ( new BluesSpigetSemVerComparator().compareMCVersionTo("1.13.0") >= 0 ) {
			this.isDropItemsSupported = true;
		}
		
		this.autoFeatureWrapper = AutoFeaturesWrapper.getInstance();
	}
	
	public static OnBlockBreakExternalEvents getInstance() {
//		if ( instance != null ) {
//			synchronized(OnBlockBreakExternalEvents.class) {
//				
//				if ( instance != null ) {
//					instance = new OnBlockBreakExternalEvents();
//					
//				}
//			}
//		}
		return instance;
	}
	


	public boolean isDropItemsSupported() {
		return isDropItemsSupported;
	}
	public void setDropItemsSupported( boolean isDropItemsSupported ) {
		this.isDropItemsSupported = isDropItemsSupported;
	}

	public AutoFeaturesFileConfig getAutoFeaturesConfig() {
		return autoFeatureWrapper.getAutoFeaturesConfig();
	}

	public boolean isBoolean( AutoFeatures feature ) {
		return autoFeatureWrapper.isBoolean( feature );
	}

	public String getMessage( AutoFeatures feature ) {
		return autoFeatureWrapper.getMessage( feature );
	}

	public int getInteger( AutoFeatures feature ) {
		return autoFeatureWrapper.getInteger( feature );
	}
	
	protected List<String> getListString( AutoFeatures feature ) {
		return autoFeatureWrapper.getListString( feature );
	}
	
	
	protected void registerAllExternalEvents() {
		
		if ( !setup ) {
			setup = true;
			
			
			// If mcMMO was not registered, then it will get registered. If mcMMO is not available 
			// then it will just bypass the registration. It will only be processed only once.
			registerMCMMO();
			
			
			registerEZBlock();
			
			
			registerQuests();
		
			
//			registerWorldGuard();
			
		}
		
		
		
		// Removed because there is a directly callable target with /prison debug now:
//		if ( Output.get().isDebug( DebugTarget.blockBreakListeners ) ) {
//			
//			String eventType = "BlockBreakEvent";
//			
//			RegisteredListener[] listeners = BlockBreakEvent.getHandlerList().getRegisteredListeners();
//			
//	        ChatDisplay display = new ChatDisplay("Event Dump: " + eventType );
//	        display.addText("&8All registered EventListeners (%d):", listeners.length );
//
//			for ( RegisteredListener eventListner : listeners ) {
//				String plugin = eventListner.getPlugin().getName();
//				EventPriority priority = eventListner.getPriority();
//				String listener = eventListner.getListener().getClass().getName();
//				
//				String message = String.format( "&3  Plugin: &7%s   %s  &3(%s)", 
//						plugin, priority.name(), listener);
//
//				display.addText( message );
//			}
//			
//			display.toLog( LogLevel.DEBUG );
//		}
		
	}

	
//	private void registerPriorityEvents() {
//		
//		// First priority plugins:
//		List<String> fpPlugins = getListString( AutoFeatures.firstPriorityBlockBreakEventPlugins );
//		
//		
//		// gather all plugins within the event:
//		TreeMap<String, Plugin> registeredPlugins = new TreeMap<>();
//		
//		
//		HandlerList handlers = BlockBreakEvent.getHandlerList();
//		
//		for ( RegisteredListener handler : handlers.getRegisteredListeners() ) {
//			
//			Plugin plugin = handler.getPlugin();
//			String pluginName = plugin.getName();
//			
//			if ( !registeredPlugins.containsKey( pluginName ) ) {
//				
//				registeredPlugins.put( pluginName, plugin );
//			}
//		}
//		
//		//handlers.get
//	}
	
	protected StringBuilder checkAllExternalEvents( BlockBreakEvent e ) {
		
		// check mcmmo
		StringBuilder sb = checkMCMMO( e );
		
		
		sb.append( checkEZBlock( e ) );
		
		
		sb.append( checkQuests( e ) );
		
		
		return sb;
	}
	
	protected StringBuilder checkAllExternalEvents( Player player, Block block ) {
		
		BlockBreakEvent bEvent = new BlockBreakEvent( block, player );
		
		return checkAllExternalEvents( bEvent );
	}

	
//	private synchronized void registerWorldGuard() {
//		
//		if ( !isWorldGuardChecked ) {
//			isWorldGuardChecked = true;
//
//			// com.sk89q.worldguard.bukkit.listener.EventAbstractionListener
//			// Name=WorldGuard  priority=NORMAL  class=class com.sk89q.worldguard.bukkit.listener.EventAbstractionListener
////			String klass = "class com.sk89q.worldguard.bukkit.listener.EventAbstractionListener";
//			
//			
//			for ( RegisteredListener rListener : BlockBreakEvent.getHandlerList().getRegisteredListeners() ) {
//				
//				Output.get().logInfo( "BBE :: *** listener ***  " +
//						"  isEnable=" + rListener.getPlugin().isEnabled() + 
//						"  Name=" + rListener.getPlugin().getName() + 
//						"  priority=" + rListener.getPriority().name() + 
//						"  class=" + rListener.getListener().getClass().toString()
//						);
//				
//				
//				if ( rListener.getPriority() == EventPriority.NORMAL &&
//						rListener.getPlugin().isEnabled() && 
//						rListener.getPlugin().getName().equalsIgnoreCase( "WorldGuard" ) &&
//						rListener.getListener().getClass().toString().contains( "EventAbstractionListener" ) &&
////						klass.equals( rListener.getListener().getClass().toString() ) &&
//						registeredListenerWorldGuard == null 
//						) {
//					
//					registeredListenerWorldGuard = rListener;
//					
//					Method[] methods = rListener.getClass().getMethods();
//					for ( Method method : methods ) {
//						if ( method.getName().equalsIgnoreCase( "onBlockBreak" ) && 
//								method.getParameterCount() == 1 ) {
//							worldGuardOnBlockBreakMethod = method;
//							break;
//						}
//					}
//
//					
//					Output.get().logInfo( "BBE :: *** WorldGuard registered  ***  " +
//							"  Listener=" + (registeredListenerWorldGuard != null) +
//							"  onBlockBreak=" + (worldGuardOnBlockBreakMethod != null)
//							
//							);
//					
//					break;
//				}
//			}
//			
//		}
//	}
//	
//	
//	
////	private void checkWorldGuard( Player player, Block block ) {
////		if ( registeredListenerWorldGuard != null ) {
////			BlockBreakEvent bEvent = new BlockBreakEvent( block, player );
////			checkWorldGuard( bEvent );
////		}
////	}
//	
//	protected void checkWorldGuard( BlockBreakEvent e ) {
//		if ( registeredListenerWorldGuard != null && !e.isCancelled() ) {
//			
////			try {
//				
//				if ( worldGuardOnBlockBreakMethod != null ) {
//					
//					try {
//						worldGuardOnBlockBreakMethod.setAccessible( true );
//						
//						worldGuardOnBlockBreakMethod.invoke( registeredListenerWorldGuard, e );
//						
//
//					}
//					catch ( IllegalAccessException | IllegalArgumentException | InvocationTargetException | 
//										NullPointerException ex ) {
//						Output.get().logInfo( "&3Error trying to access WorldGuard's EventAbstractionListener" +
//								" onBlockBreakEvent() through reflection.  Terminating future attempts.&3: [%s] ", 
//								ex.getMessage() );
//						
//						worldGuardOnBlockBreakMethod = null;
//						
//						registeredListenerWorldGuard = null;
//					}
//
//				}
//				
////				"EventAbstractionListener" "onBlockBreak"
//				// com.sk89q.worldguard.bukkit.listener.EventAbstractionListener.onBlockBreak(BlockBreakEvent)
//				
////				registeredListenerWorldGuard.callEvent( e );
//				
////				Output.get().logInfo( "BBE :: *** WorldGuard BlockBreakEvent forced  ***   canceled= " + e.isCancelled() );
////			}
////			catch ( EventException e1 ) {
////				e1.printStackTrace();
////			}
//		}
//	}
	

	/**
	 * <p>Checks to see if mcMMO is able to be enabled, and if it is, then call it's registered
	 * function that will do it's processing before prison will process the blocks.
	 * </p>
	 * 
	 * <p>This adds mcMMO support within mines for herbalism, mining, woodcutting, and excavation.
	 * </p>
	 * 
	 * @param e
	 */
	private void registerMCMMO() {
		
		if ( !isMCMMOChecked ) {
			
	    	boolean isProcessMcMMOBlockBreakEvents = isBoolean( AutoFeatures.isProcessMcMMOBlockBreakEvents );

			if ( isProcessMcMMOBlockBreakEvents ) {
				
				for ( RegisteredListener rListener : BlockBreakEvent.getHandlerList().getRegisteredListeners() ) {
					
					if ( rListener.getPlugin().isEnabled() && 
							rListener.getPlugin().getName().equalsIgnoreCase( "mcMMO" ) ) {
						
						registeredListenerMCMMO = rListener;
					}
				}
				
			}
			
			isMCMMOChecked = true;
		}
	}
	
//	private void checkMCMMO( Player player, Block block ) {
//		if ( registeredListenerMCMMO != null ) {
//			BlockBreakEvent bEvent = new BlockBreakEvent( block, player );
//			checkMCMMO( bEvent );
//		}
//	}
	
	private StringBuilder checkMCMMO( BlockBreakEvent e ) {
		StringBuilder sb = new StringBuilder();
		
		if ( registeredListenerMCMMO != null ) {
			
			try {
				
				boolean isCanceled = e.isCancelled();
				boolean isDropItems = false;
				
				if ( isDropItemsSupported() ) {
					isDropItems = e.isDropItems();
				}
				
				registeredListenerMCMMO.callEvent( e );
				
				sb.append( "[Quests" );
				if ( isCanceled != e.isCancelled() ) {
					sb.append( ":isCanceled=" ).append( e.isCancelled() );
				}
				if ( isDropItemsSupported() && isDropItems != e.isDropItems() ) {
					sb.append( ":isDropItems=" ).append( e.isDropItems() );
				}
				sb.append( "]" );
			}
			catch ( EventException e1 ) {
				e1.printStackTrace();
			}
		}
		
		return sb;
	}
	
	
	
	/**
	 * <p>Checks to see if mcMMO is able to be enabled, and if it is, then call it's registered
	 * function that will do it's processing before prison will process the blocks.
	 * </p>
	 * 
	 * <p>This adds mcMMO support within mines for herbalism, mining, woodcutting, and excavation.
	 * </p>
	 * 
	 * @param e
	 */
	private void registerEZBlock() {
		
		if ( !isEZBlockChecked ) {
			
	    	boolean isProcessMcMMOBlockBreakEvents = isBoolean( AutoFeatures.isProcessEZBlocksBlockBreakEvents );

			if ( isProcessMcMMOBlockBreakEvents ) {
				
				for ( RegisteredListener rListener : BlockBreakEvent.getHandlerList().getRegisteredListeners() ) {
					
					if ( rListener.getPlugin().isEnabled() && 
							rListener.getPlugin().getName().equalsIgnoreCase( "EZBlocks" ) ) {
						
						registeredListenerEZBlock = rListener;
					}
				}
				
			}
			
			isEZBlockChecked = true;
		}
	}
	
//	private void checkEZBlock( Player player, Block block ) {
//		if ( registeredListenerEZBlock != null ) {
//			BlockBreakEvent bEvent = new BlockBreakEvent( block, player );
//			checkEZBlock( bEvent );
//		}
//	}
	
	private StringBuilder checkEZBlock( BlockBreakEvent e ) {
		StringBuilder sb = new StringBuilder();
		
		if ( registeredListenerEZBlock != null ) {
			
			try {
				boolean isCanceled = e.isCancelled();
				boolean isDropItems = false;
				
				if ( isDropItemsSupported() ) {
					isDropItems = e.isDropItems();
				}
				
				registeredListenerEZBlock.callEvent( e );
				
				sb.append( "[EZBlock" );
				if ( isCanceled != e.isCancelled() ) {
					sb.append( ":isCanceled=" ).append( e.isCancelled() );
				}
				if ( isDropItemsSupported() && isDropItems != e.isDropItems() ) {
					sb.append( ":isDropItems=" ).append( e.isDropItems() );
				}
				sb.append( "]" );
			}
			catch ( EventException e1 ) {
				e1.printStackTrace();
			}
		}
		
		return sb;
	}
	

	
	/**
	 * <p>Checks to see if Quests is able to be enabled, and if it is, then call it's registered
	 * function that will do it's processing before prison will process the blocks.
	 * </p>
	 * 
	 * <p>This adds Quests support within mines for block break related quests.
	 * </p>
	 * 
	 * @param e
	 */
	private void registerQuests() {
		
		if ( !isQuestsChecked ) {
			
	    	boolean isProcessQuestsBreakEvents = isBoolean( AutoFeatures.isProcessQuestsBlockBreakEvents );

			if ( isProcessQuestsBreakEvents ) {
				
				for ( RegisteredListener rListener : BlockBreakEvent.getHandlerList().getRegisteredListeners() ) {
					
					if ( rListener.getPlugin().isEnabled() && 
							rListener.getPlugin().getName().equalsIgnoreCase( "Quests" ) ) {
						
						registeredListenerQuests = rListener;
					}
				}
				
			}
			
			isQuestsChecked = true;
		}
	}
	
//	private void checkQuests( Player player, Block block ) {
//		if ( registeredListenerQuests != null ) {
//			BlockBreakEvent bEvent = new BlockBreakEvent( block, player );
//			checkEZBlock( bEvent );
//		}
//	}
	
	private StringBuilder checkQuests( BlockBreakEvent e ) {
		StringBuilder sb = new StringBuilder();
		
		if ( registeredListenerQuests != null ) {
			
			try {
				
				boolean isCanceled = e.isCancelled();
				boolean isDropItems = false;
				
				if ( isDropItemsSupported() ) {
					isDropItems = e.isDropItems();
				}
				
				registeredListenerQuests.callEvent( e );
				
				sb.append( "[Quests" );
				if ( isCanceled != e.isCancelled() ) {
					sb.append( ":isCanceled=" ).append( e.isCancelled() );
				}
				if ( isDropItemsSupported() && isDropItems != e.isDropItems() ) {
					sb.append( ":isDropItems=" ).append( e.isDropItems() );
				}
				sb.append( "]" );
			}
			catch ( EventException e1 ) {
				e1.printStackTrace();
			}
		}
		
		return sb;
	}
	

	
}
