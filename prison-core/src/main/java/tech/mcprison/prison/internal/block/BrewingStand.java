package tech.mcprison.prison.internal.block;

import tech.mcprison.prison.internal.inventory.InventoryHolder;

/**
 * Created by DMP9 on 04/02/2017.
 */
public interface BrewingStand extends BlockState, InventoryHolder {
    int getBrewingTime();

    void setBrewingTime(int brewTime);
}
