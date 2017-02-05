package tech.mcprison.prison.internal.inventory;

/**
 * Represents a block or entity capable of holding an inventory (e.g. Chest)
 *
 * @author Dylan M. Perks
 * @since API 1.1
 */
public interface InventoryHolder {
    /**
     * Gets the {@link Inventory} this {@link InventoryHolder} is holding
     *
     * @return
     */
    public Inventory getInventory();
}
