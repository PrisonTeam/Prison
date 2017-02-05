package tech.mcprison.prison.internal.block;

import tech.mcprison.prison.internal.inventory.InventoryHolder;
import tech.mcprison.prison.util.Location;

/**
 * Created by DMP9 on 04/02/2017.
 */
public interface DoubleChest extends InventoryHolder {
    InventoryHolder getLeftSide();

    InventoryHolder getRightSide();

    Location getLocation();
}
