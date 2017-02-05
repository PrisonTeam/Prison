package tech.mcprison.prison.internal.inventory;

/**
 * Represents an inventory created by a {@link tech.mcprison.prison.block.DoubleChest}
 *
 * @author Dylan M. Perks
 * @since API 1.1
 */
public interface DoubleChestInventory {
    /**
     * Gets the {@link Inventory} of the left Chest
     *
     * @return a container inventory for the left chest
     */
    Inventory getLeftSide();

    /**
     * Gets the {@link Inventory} of the right Chest
     *
     * @return a container inventory for the right chest
     */
    Inventory getRightSide();
}
