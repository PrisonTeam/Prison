package tech.mcprison.prison.spigot.block;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventException;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.RegisteredListener;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;

public abstract class OnBlockBreakExternalEvents {

	private boolean isMCMMOChecked = false;
	private RegisteredListener registeredListenerMCMMO = null; 
	
	private boolean isEZBlockChecked = false;
	private RegisteredListener registeredListenerEZBlock = null; 
	
	
	
	public abstract AutoFeaturesFileConfig getAutoFeaturesConfig();

	public abstract boolean isBoolean( AutoFeatures feature );

	public abstract String getMessage( AutoFeatures feature );

	public abstract int getInteger( AutoFeatures feature );
	
	protected abstract List<String> getListString( AutoFeatures feature );
	
	
	protected void registerAllExternalEvents() {
		
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
