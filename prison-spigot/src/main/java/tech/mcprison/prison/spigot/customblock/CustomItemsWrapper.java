package tech.mcprison.prison.spigot.customblock;

import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;

import com.jojodmo.customitems.api.CustomItemsAPI;

import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.util.Location;

public class CustomItemsWrapper {
	

	private final SpigotPrison plugin;

	public CustomItemsWrapper() {
		this.plugin = SpigotPrison.getInstance();
	}

	public String getCustomBlockId( Block block ) {
		
		org.bukkit.block.Block spigotBlock = ((SpigotBlock) block).getWrapper();
		CustomItemsAPI.getCustomItemIDAtBlock( spigotBlock );
		return null;
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
		
		return new SpigotBlock( resultBlock );
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
				org.bukkit.block.Block spigotBlock = ((SpigotBlock) location.getBlockAt()).getWrapper();
				//org.bukkit.block.Block spigotBlock = ((SpigotBlock) prisonBlock).getWrapper();
				
				// Request the block change, but we don't need the results so ignore it
//				org.bukkit.block.Block resultBlock = 
				CustomItemsAPI.setCustomItemIDAtBlock( spigotBlock, prisonBlock.getBlockName(), true );
				
			}
		}.runTaskLater( getPlugin(), 0 );
		
	}

	public List<String> getCustomBlockList() {
		return CustomItemsAPI.listBlockCustomItemIDs();
	}
	
	public SpigotPrison getPlugin() {
		return plugin;
	}
}
