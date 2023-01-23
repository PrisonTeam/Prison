package tech.mcprison.prison.spigot.customblock;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.ItemsAdder;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.autofeatures.AutoFeaturesWrapper;
import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlock.PrisonBlockType;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.util.Location;

public class PrisonItemsAdderWrapper {
	
	private boolean supportsDrops = false;

	private final SpigotPrison plugin;

	public PrisonItemsAdderWrapper() {
		this.plugin = SpigotPrison.getInstance();
	}

	public String getCustomBlockId( Block block ) {
		
		org.bukkit.block.Block spigotBlock = ((SpigotBlock) block).getWrapper();
		
		return getCustomBlockId( spigotBlock );
		//return CustomItemsAPI.getCustomItemIDAtBlock( spigotBlock );
	}
	
	public String getCustomBlockId( org.bukkit.block.Block spigotBlock ) {
		
		CustomBlock cBlock = CustomBlock.byAlreadyPlaced(spigotBlock);
		
		return ( cBlock == null ? null : cBlock.getDisplayName() );
		//return CustomItemsAPI.getCustomItemIDAtBlock( spigotBlock );
	}


	/**
	 * <p>This should only be called when running in the bukkit synchronous thread.
	 * </p>
	 * 
	 * @param block
	 * @param customId
	 * @param doBlockUpdate
	 * @return
	 */
	public Block setCustomBlockId( Block block, String customId, boolean doBlockUpdate ) {
		
		String cBlockId = getCustomBlockId( block );
		
		SpigotBlock sBlock = (SpigotBlock) block;
		
		CustomBlock cBlock = CustomBlock.place( cBlockId, sBlock.getWrapper().getLocation() );
		
		return SpigotBlock.getSpigotBlock( cBlock.getBlock() );
		

//		org.bukkit.block.Block spigotBlock = ((SpigotBlock) block).getWrapper();
		
		// So to prevent this from causing lag, we will only get back the block with no updates
		// This will allow this function to exit:
//		org.bukkit.block.Block resultBlock = 
//				CustomItemsAPI.setCustomItemIDAtBlock( spigotBlock, customId, doBlockUpdate );
		
//		return SpigotBlock.getSpigotBlock( resultBlock );
	}
	
	
	/**
	 * <p>This should only be ran through an asynchronous thread since it will submit a task
	 * on the bukkit synchronous thread to perform the actual update.  This does not need to 
	 * return a block.
	 * </p>
	 * 
	 * @param prisonBlock
	 * @param location
	 * @param doBlockUpdate
	 * @return
	 */
	public void setCustomBlockIdAsync( PrisonBlock prisonBlock, Location location )
	{
	
		new BukkitRunnable() {
			@Override
			public void run() {
				
				
				String cBlockId = getCustomBlockId( prisonBlock );
				
				SpigotBlock sBlock = (SpigotBlock) location.getBlockAt();
				
				@SuppressWarnings("unused")
				CustomBlock cBlock = CustomBlock.place( cBlockId, sBlock.getWrapper().getLocation() );
				
				
				// No physics update:
				
//				SpigotBlock sBlock = (SpigotBlock) location.getBlockAt();
//				
//				org.bukkit.block.Block spigotBlock = sBlock.getWrapper();
				//org.bukkit.block.Block spigotBlock = ((SpigotBlock) prisonBlock).getWrapper();
				
				// Request the block change, but we don't need the results so ignore it
//				org.bukkit.block.Block resultBlock = 
//				CustomItemsAPI.setCustomItemIDAtBlock( spigotBlock, prisonBlock.getBlockName(), true );
				
			}
		}.runTaskLater( getPlugin(), 0 );
		
	}

	/**
	 * 
	 */
	public List<SpigotItemStack> getDrops( PrisonBlock prisonBlock, SpigotPlayer player, SpigotItemStack tool ) {
		List<SpigotItemStack> results = new ArrayList<>();
		
		if ( isSupportsDrops() && 
				AutoFeaturesWrapper.getInstance().isBoolean( AutoFeatures.isUseCustomBlocksCustomItemsGetDrops ) &&
				prisonBlock instanceof SpigotBlock ) {
			
			SpigotBlock sBlock = (SpigotBlock) prisonBlock;
			
			org.bukkit.block.Block bBlock = sBlock.getWrapper();
			Player bPlayer = player.getWrapper();
			ItemStack bTool = tool.getBukkitStack();
			
			boolean includeSelfBlock = false;
			
			if ( bBlock != null && bTool != null ) {
				
				
				
				List<ItemStack> drops = CustomBlock.getLoot( bBlock, bTool, includeSelfBlock );
				StringBuilder sb = new StringBuilder();
				
				if ( drops != null ) {
					
					for (ItemStack iStack : drops ) {
						SpigotItemStack sItemStack = new SpigotItemStack( iStack );
						
						results.add( sItemStack );
						
						sb.append( "[" ).append( sItemStack.toString() ).append( "]" );
					}
				}
				
			
				String message = String.format(
						"PrisonItemsAdder.getDrops() results for block: %s  "
								+ "player: %s  tool: %s  drops: %s  ",
								bBlock == null ? "null" : prisonBlock.getBlockName(),
								bPlayer == null ? "null" : player.getName(),
								bTool == null ? "null" : bTool.toString(),
								sb.toString()
						);
				
				Output.get().logDebug( message );
			}
			else {
				if ( Output.get().isDebug() ) {
					String message = String.format(
							"PrisonItemsAdder.getDrops(): failed to provide non-null inputs.  "
							+ "block: %s  player: %s  tool: %s", 
							bBlock == null ? "null" : prisonBlock.getBlockName(), 
							bPlayer == null ? "null" : player.getName(),
							bTool == null ? "null" : tool.getName()
							);
					
					Output.get().logDebug( message );
				}
			}
			
		}

		if ( results.size() == 0 ) {
			
			String itemsAdderBlockId = getCustomBlockId( prisonBlock );
			
			org.bukkit.inventory.ItemStack bItemStack = 
							CustomBlock.getInstance( itemsAdderBlockId ).getItemStack();
			
			SpigotItemStack sItemStack = new SpigotItemStack( bItemStack );
			
			// Fix itemStack's displayName and set to the correct BlockType:
			sItemStack.setPrisonBlock( prisonBlock );
			
			results.add( sItemStack );
		}
		
		return results;
	}
	
	
	public List<String> getCustomBlockList() {
		List<String> customList = new ArrayList<>();
		
//		List<String> customListx = new ArrayList<>( CustomBlock.getNamespacedIdsInRegistry() );
		
		
		List<CustomStack> allItems = ItemsAdder.getAllItems();
		
		for ( CustomStack cStack : allItems ) {
			if ( cStack.isBlock() ) {
				String namespace = cStack.getNamespacedID();
				String name = cStack.getDisplayName();

				Output.get().logDebug( 
						"ItemsAdder:  namespacedID: %s  displayName: %s  PrisonBlock: %s:%s", 
						namespace,
						name, 
						PrisonBlockType.ItemsAdder,
						name
						);
				
				customList.add( namespace );
			}
		}

		return customList;
	}
	
	public SpigotPrison getPlugin() {
		return plugin;
	}

	public boolean isSupportsDrops() {
		return supportsDrops;
	}
	public void setSupportsDrops(boolean supportsDrops) {
		this.supportsDrops = supportsDrops;
	}
	
	
}
