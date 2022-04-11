package tech.mcprison.prison.spigot.customblock;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.jojodmo.customitems.api.CustomItemsAPI;

import tech.mcprison.prison.autofeatures.AutoFeaturesWrapper;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.util.Location;

public class CustomItemsWrapper {

	
	private boolean supportsDrops = false;

	private final SpigotPrison plugin;

	public CustomItemsWrapper() {
		this.plugin = SpigotPrison.getInstance();
	}

	public String getCustomBlockId( Block block ) {
		
		org.bukkit.block.Block spigotBlock = ((SpigotBlock) block).getWrapper();
		return CustomItemsAPI.getCustomItemIDAtBlock( spigotBlock );
	}
	
	public String getCustomBlockId( org.bukkit.block.Block spigotBlock ) {
		
		return CustomItemsAPI.getCustomItemIDAtBlock( spigotBlock );
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

		org.bukkit.block.Block spigotBlock = ((SpigotBlock) block).getWrapper();
		
		// So to prevent this from causing lag, we will only get back the block with no updates
		// This will allow this function to exit:
		org.bukkit.block.Block resultBlock = 
				CustomItemsAPI.setCustomItemIDAtBlock( spigotBlock, customId, doBlockUpdate );
		
		return SpigotBlock.getSpigotBlock( resultBlock );
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
				
				// No physics update:
				
				SpigotBlock sBlock = (SpigotBlock) location.getBlockAt();
				
				org.bukkit.block.Block spigotBlock = sBlock.getWrapper();
				//org.bukkit.block.Block spigotBlock = ((SpigotBlock) prisonBlock).getWrapper();
				
				// Request the block change, but we don't need the results so ignore it
//				org.bukkit.block.Block resultBlock = 
				CustomItemsAPI.setCustomItemIDAtBlock( spigotBlock, prisonBlock.getBlockName(), true );
				
			}
		}.runTaskLater( getPlugin(), 0 );
		
	}

	/**
	 * <p>WARNING: CustomItems does not have a getDrops() function, so there is no way to 
	 * actually get custom drops for CustomItems blocks.  All we can do is return the custom 
	 * block.
	 * </p>
	 * 
	 * <p>If a getDrops() function is added in the future, then we will be able to hook that up
	 * with that future version.
	 * </p>
	 * 
	 * 
	 * CustomItemsAPI.breakCustomItemBlockWithoutDrops():
	 * Break the given block, and return the drops instead of dropping them normally
     * @param block - the block that should be broken
     * @param player - the player that broke the block
     * @param overrideItemUsed - what item should CustomItems pretend the user broke this block with? This should probably be the item in their main hand, but there are other special use cases where you might want to use a different ItemStack
     * @param doBlockUpdate - whether or not this should trigger a block update
     * @param override - false by default. Set this to true if you want to override CustomItem's decision to cancel the block break
     * @return
     *      {@code null} if the block is not a custom block, and a two-element entry otherwise:
     *          The first entry is false if CustomItems cancelled the block break, and true if the block was broken. This will always be true if you set override to true.
     *          The second entry is also a two-element entry:
     *              The first entry is overrideItemUsed after being modified by any actions. You should most likely replace whatever overrideItemUsed is in the player's inventory with this item stack.
     *              The second entry is a list of the items that the block would have normally dropped. This will be empty if the first entry is false
	 * 
	 * 
	 * @param prisonBlock
	 * @return
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
			
			if ( bBlock != null && bPlayer != null && bTool != null ) {
				
				
				SimpleEntry<Boolean, SimpleEntry<ItemStack, List<ItemStack>>> cuiDropResults = null;
				
				try {
					cuiDropResults =
							CustomItemsAPI.breakCustomItemBlockWithoutDrops( 
									bBlock, bPlayer, bTool, false, true );
				} 
				catch (Exception e) {
					Output.get().logError( "Failed: CustomItemsWrapper.getDrops: breakCustomItemBlockWithoutDrops: ", 
							e );
//					e.printStackTrace();
				}
				
				if ( cuiDropResults != null ) {
					
					Boolean cuiCanceled = cuiDropResults.getKey();
					ItemStack toolOverridden = cuiDropResults.getValue().getKey();
					List<ItemStack> cuiDrops = cuiDropResults.getValue().getValue();
					
					if ( cuiDrops != null && cuiDrops.size() > 0 ) {
						
						for (ItemStack itemStack : cuiDrops) {
							results.add( new SpigotItemStack( itemStack ) );
						}
					}
					
					if ( Output.get().isDebug() ) {
						StringBuilder sbDrops = new StringBuilder();
						
						for (SpigotItemStack sItemStack : results) {
							if ( sbDrops.length() > 0 ) {
								sbDrops.append(", ");
							}
							sbDrops.append(sItemStack.getName()).append(":").append(sItemStack.getAmount());
						}
						sbDrops.insert(0, "[").append("]");
						
						String message = String.format(
								"CustomItems.getDrops() results for block: %s  "
										+ "canceled: %b  drops: %s  player: %s  tool: %s",
										bBlock == null ? "null" : prisonBlock.getBlockName(),
										cuiCanceled,
										sbDrops.toString(), 
										bPlayer == null ? "null" : player.getName(),
										toolOverridden == null ? "null" : toolOverridden.getType().toString()
								);
						
						Output.get().logDebug( message );
					}
				}
			}
			else {
				if ( Output.get().isDebug() ) {
					String message = String.format(
							"CustomItems.getDrops(): failed to provide non-null inputs.  "
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
			
			org.bukkit.inventory.ItemStack bItemStack = CustomItemsAPI.getCustomItem( prisonBlock.getBlockName() );
			
			SpigotItemStack sItemStack = new SpigotItemStack( bItemStack );
			
			// Fix itemStack's displayName and set to the correct BlockType:
			sItemStack.setPrisonBlock( prisonBlock );
			
			results.add( sItemStack );
		}
		
		return results;
	}
	
	public List<String> getCustomBlockList() {
		return CustomItemsAPI.listBlockCustomItemIDs();
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
