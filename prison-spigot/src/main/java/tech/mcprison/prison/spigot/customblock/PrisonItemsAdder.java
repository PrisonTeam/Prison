package tech.mcprison.prison.spigot.customblock;

import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.integration.CustomBlockIntegration;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlock.PrisonBlockType;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.util.Location;

public class PrisonItemsAdder 		
	extends CustomBlockIntegration {

	private PrisonItemsAdderWrapper itemsAdderWrapper;
	
	public PrisonItemsAdder() {
		super("ItemsAdder", "ItemsAdder", PrisonBlockType.ItemsAdder, "itemsadder:" );
    }
	
	
	

	@Override
	public void integrate() {

		// BluesSpigetSemVerComparator semVer = new BluesSpigetSemVerComparator();

		if ( isRegistered()) {
			try {
				
				this.itemsAdderWrapper = new PrisonItemsAdderWrapper();
				
				
				List<PrisonBlock> prisonBlocks = getCustomBlockList();
				for ( PrisonBlock block : prisonBlocks )
				{
					Output.get().logInfo( "####  Custom Block: " + block.toString() );
				}
				
				String message = String.format(
						"Enabling ItemsAdder v%s   custom blocks loaded: %d",
						getVersion(),
						prisonBlocks.size() );
				
				Output.get().logInfo( "&7" + message + "enabled." );

				
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
        return (itemsAdderWrapper != null);
    }
	
	@Override
	public String getCustomBlockId( Block block ) {
		return itemsAdderWrapper.getCustomBlockId( block );
	}
	
	
	public String getCustomBlockId( org.bukkit.block.Block spigotBlock ) {
		
		return itemsAdderWrapper.getCustomBlockId( spigotBlock );
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
			
			if ( results != null ) {
				
				Location loc = new Location( block.getLocation() );
				results.setLocation( loc );
			}
		}
		
		return results;
	}
	
	public PrisonBlock getCustomBlock( org.bukkit.block.Block spigotBlock ) {
		PrisonBlock results = null;
		
		String customBlockId = getCustomBlockId( spigotBlock );
		
		if ( customBlockId != null ) {
			results = SpigotPrison.getInstance().getPrisonBlockTypes()
									.getBlockTypesByName( customBlockId );
			
			if ( results != null ) {
				Location loc = SpigotUtil.bukkitLocationToPrison( spigotBlock.getLocation() );

				results.setLocation( loc );
			}
			
//			SpigotBlock sBlock = new SpigotBlock();
		}
		
		return results;
	}
	
	
	@Override
	public Block setCustomBlockId( Block block, String customId, boolean doBlockUpdate ) {
		return itemsAdderWrapper.setCustomBlockId( block, customId, doBlockUpdate );
	}
	
	
	@Override
	public void setCustomBlockIdAsync( PrisonBlock prisonBlock, Location location ) {
		itemsAdderWrapper.setCustomBlockIdAsync( prisonBlock, location );
	}
	
	@Override
	public List<? extends ItemStack> getDrops( Player player, PrisonBlock prisonBlock, ItemStack tool ) {
		
		SpigotPlayer sPlayer = player != null && player instanceof SpigotPlayer ? 
											(SpigotPlayer) player : null;
		SpigotItemStack sTool = tool != null && tool instanceof SpigotItemStack ?
											(SpigotItemStack) tool : null;
		
		List<? extends ItemStack> results = itemsAdderWrapper.getDrops( prisonBlock, sPlayer, sTool );
		
		return results;
	}
	
	@Override
	public List<PrisonBlock> getCustomBlockList()
	{
		List<PrisonBlock> results = new ArrayList<>();
		
		for ( String block : itemsAdderWrapper.getCustomBlockList() ) {
			
			PrisonBlock prisonBlock = new PrisonBlock( getBlockType(), block );

			prisonBlock.setValid( true );
			prisonBlock.setBlock( true );
			
			results.add( prisonBlock );
		}
		
		return results;
	}



	@Override
	public String getPluginSourceURL() {
		return "https://polymart.org/resource/itemsadder-custom-items-etc.1851";
	}


}
