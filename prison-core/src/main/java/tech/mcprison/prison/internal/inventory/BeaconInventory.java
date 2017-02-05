package tech.mcprison.prison.internal.inventory;

import tech.mcprison.prison.internal.ItemStack;

/**
 * Represents an inventory created by a beacon
 *
 * @author Dylan M. Perks
 * @since API 1.1
 */
public interface BeaconInventory {
    /**
     * Gets the {@link ItemStack} powering this beacon
     *
     * @return the item powering the beacon.
     */
    ItemStack getItem();

    /**
     * Sets the {@link ItemStack} powering this beacon.
     *
     * @param item the item that should be used to power this beacon.
     */
    void setItem(ItemStack item);
}
