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

public class OnBlockBreakExternalEvents {
	
	private static final OnBlockBreakExternalEvents instance = new OnBlockBreakExternalEvents();

//	private boolean isWorldGuardChecked = false;
//	private RegisteredListener registeredListenerWorldGuard = null; 
//	private Method worldGuardOnBlockBreakMethod;
	
	private boolean isMCMMOChecked = false;
	private RegisteredListener registeredListenerMCMMO = null; 
	
	private boolean isEZBlockChecked = false;
	private RegisteredListener registeredListenerEZBlock = null; 
	
	
	private AutoFeaturesWrapper autoFeatureWrapper = null;
	
	
	private OnBlockBreakExternalEvents() {
		super();
		
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
		
		
//		registerWorldGuard();

		
		// If mcMMO was not registered, then it will get registered. If mcMMO is not available 
		// then it will just bypass the registration. It will only be processed only once.
		registerMCMMO();
		
		
		registerEZBlock();
		
	}

	
	protected void checkAllExternalEvents( BlockBreakEvent e ) {
		
		// check mcmmo
		checkMCMMO( e );
		
		
		checkEZBlock( e );
		
	}
	
	protected void checkAllExternalEvents( Player player, Block block ) {
		
		// check mcmmo
		checkMCMMO( player, block );
		
		
		checkEZBlock( player, block );
		
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
	
	private void checkMCMMO( Player player, Block block ) {
		if ( registeredListenerMCMMO != null ) {
			BlockBreakEvent bEvent = new BlockBreakEvent( block, player );
			checkMCMMO( bEvent );
		}
	}
	
	private void checkMCMMO( BlockBreakEvent e ) {
		if ( registeredListenerMCMMO != null ) {
			
			try {
				registeredListenerMCMMO.callEvent( e );
			}
			catch ( EventException e1 ) {
				e1.printStackTrace();
			}
		}
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
	
	private void checkEZBlock( Player player, Block block ) {
		if ( registeredListenerEZBlock != null ) {
			BlockBreakEvent bEvent = new BlockBreakEvent( block, player );
			checkEZBlock( bEvent );
		}
	}
	
	private void checkEZBlock( BlockBreakEvent e ) {
		if ( registeredListenerEZBlock != null ) {
			
			try {
				registeredListenerEZBlock.callEvent( e );
			}
			catch ( EventException e1 ) {
				e1.printStackTrace();
			}
		}
	}
	

	
}
