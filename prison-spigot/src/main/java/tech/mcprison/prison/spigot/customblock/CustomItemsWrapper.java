package tech.mcprison.prison.spigot.customblock;

import java.util.List;

import com.jojodmo.customitems.api.CustomItemsAPI;

import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.spigot.block.SpigotBlock;

public class CustomItemsWrapper {

	public CustomItemsWrapper() {
	
	}

	public String getCustomBlockId( Block block ) {
		
		org.bukkit.block.Block spigotBlock = ((SpigotBlock) block).getWrapper();
		CustomItemsAPI.getCustomItemIDAtBlock( spigotBlock );
		return null;
	}

	public Block setCustomBlockId( Block block, String customId, boolean doBlockUpdate ) {

		org.bukkit.block.Block spigotBlock = ((SpigotBlock) block).getWrapper();
		org.bukkit.block.Block resultBlock = CustomItemsAPI.setCustomItemIDAtBlock( spigotBlock, customId, doBlockUpdate );
		return new SpigotBlock( resultBlock );
	}

	public List<String> getCustomBlockList() {
		return CustomItemsAPI.listBlockCustomItemIDs();
	}
}
