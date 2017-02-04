package tech.mcprison.prison.spigot.block;

import tech.mcprison.prison.block.Block;
import tech.mcprison.prison.block.BrewingStand;
import tech.mcprison.prison.internal.inventory.BrewerInventory;
import tech.mcprison.prison.spigot.inventory.SpigotBrewer;

/**
 * Created by DMP9 on 04/02/2017.
 */
public class SpigotBrewingStand implements BrewingStand {
    org.bukkit.block.BrewingStand wrapper;
    public SpigotBrewingStand(org.bukkit.block.BrewingStand wrapper){
        this.wrapper = wrapper;
    }
    @Override public int getBrewingTime() {
        return wrapper.getBrewingTime();
    }

    @Override public BrewerInventory getInventory() {
        return new SpigotBrewer(wrapper.getInventory());
    }

    @Override public void setBrewingTime(int brewTime) {
        wrapper.setBrewingTime(brewTime);
    }

    @Override public Block getBlock() {
        return new SpigotBlock(wrapper.getBlock());
    }
}
