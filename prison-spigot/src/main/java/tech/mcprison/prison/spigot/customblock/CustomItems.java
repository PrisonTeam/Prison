package tech.mcprison.prison.spigot.customblock;

import java.util.ArrayList;
import java.util.List;

import com.jojodmo.customitems.api.CustomItemsAPI;

import tech.mcprison.prison.integration.CustomBlockIntegration;
import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlock.PrisonBlockType;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.spiget.BluesSpigetSemVerComparator;

/**
 * Custom Items 3.7.11 — New API features
This version adds two new features to the Custom Items API: listCustomItemIDs and listBlockCustomItemIDs
 *
 */
public class CustomItems
		extends CustomBlockIntegration {

	private CustomItemsWrapper customItemsWrapper;
	
	public CustomItems() {
		super("CustomItems", "CustomItems", PrisonBlockType.CustomItems, "cui:" );
    }
	
	@Override
	public void integrate() {

		BluesSpigetSemVerComparator semVer = new BluesSpigetSemVerComparator();

		if ( isRegistered()) {
			try {
				
				if ( semVer.compareTo( getVersion(), "3.7.11" ) >= 0 ) {
					
					if ( CustomItemsAPI.isEnabled() ) {
						
						this.customItemsWrapper = new CustomItemsWrapper();
						
						List<PrisonBlock> prisonBlocks = getCustomBlockList();
						for ( PrisonBlock block : prisonBlocks )
						{
							Output.get().logInfo( "####  Custom Block: " + block.toString() );
						}
					}
				}
				else {
					Output.get().logWarn( 
							String.format( "&cWarning: &3The plugin &7CustomItems v%s &3is enabled, " +
									"but is too old and is not supported. Must " +
									"use at least &7v3.7.11 &3or newer.", getVersion() ) );
				}
				
			}
			catch ( NoClassDefFoundError | IllegalStateException e ) {
				// ignore this exception since it means the plugin was not loaded
			}
			catch ( Exception e ) {
				e.printStackTrace();
			}
		}
		
	}
	
    
    @Override
    public boolean hasIntegrated() {
        return (customItemsWrapper != null);
    }
	
	@Override
	public String getCustomBlockId( Block block ) {
		return customItemsWrapper.getCustomBlockId( block );
	}
	
	/**
	 * <p>This function is supposed to identify if the given block is a custom block, and 
	 * if it is a custom block, then this function will return the correct PrisonBlock
	 * to match it's type. The PrisonBlock that will be returned, will come from the
	 * collection of valid blocks that were generated upon server startup.  
	 * </p>
	 * 
	 * <p>If there is no match, then this function will return a null.
	 * </p>
	 * 
	 * <p>It's also important to know that the original block that is retrieved from 
	 * PrisonBlockTypes.getBlockTypesByName() is cloned prior to returning it to this
	 * function, so it's safe to do anything you want with it.
	 * </p>
	 * 
	 * @param block
	 * @return The matched and cloned PrisonBlock, otherwise it will return a null if no match.
	 */
	@Override
	public PrisonBlock getCustomBlock( Block block ) {
		PrisonBlock results = null;
		
		String customBlockId = getCustomBlockId( block );
		
		if ( customBlockId != null ) {
			results = SpigotPrison.getInstance().getPrisonBlockTypes()
									.getBlockTypesByName( customBlockId );
		}
		
		return results;
	}
	
	@Override
	public Block setCustomBlockId( Block block, String customId, boolean doBlockUpdate ) {
		return customItemsWrapper.setCustomBlockId( block, customId, doBlockUpdate );
	}
	
	@Override
	public List<PrisonBlock> getCustomBlockList()
	{
		List<PrisonBlock> results = new ArrayList<>();
		
		for ( String block : customItemsWrapper.getCustomBlockList() ) {
			
			PrisonBlock prisonBlock = new PrisonBlock( getBlockType(), block );

			prisonBlock.setValid( true );
			prisonBlock.setBlock( true );
			
			results.add( prisonBlock );
		}
		
		return results;
	}



	@Override
	public String getPluginSourceURL() {
		return "https://polymart.org/resource/custom-items.1";
	}
}
