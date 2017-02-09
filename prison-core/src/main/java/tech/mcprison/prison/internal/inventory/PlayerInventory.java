package tech.mcprison.prison.internal.inventory;

import tech.mcprison.prison.internal.ItemStack;

/**
 * Represents an {@link Inventory} belonging to a Player
 *
 * @author Dylan M. Perks
 * @since API 1.1
 */
public interface PlayerInventory extends Inventory {
    /**
     * Gets the armor this player is wearing
     *
     * @return a list of items in this inventories armor slots
     */
    ItemStack[] getArmorContents();

    /**
     * Gets the {@link ItemStack} in this inventories boots slot
     *
     * @return the boots (or the item in the boots slot) being worn
     */
    ItemStack getBoots();

    /**
     * Gets the {@link ItemStack} in this inventories chestplate slot
     *
     * @return the chestplate (or the item in the chestplate slot) being worn
     */
    ItemStack getChestplate();

    /**
     * Gets the slot number of the selected hotbar slot
     *
     * @return the slot number
     */
    int getHeldItemSlot();

    /**
     * Gets the {@link ItemStack} in this inventories helmet slot
     *
     * @return the helmet (or the item in the helmet slot) being worn
     */
    ItemStack getHelmet();

    /**
     * Gets the {@link ItemStack} in this inventories leggings slot
     *
     * @return the leggings (or the item in the leggings slot) being worn
     */
    ItemStack getLeggings();

    /**
     * Sets the armor this player is wearing
     *
     * @return a list of items to set as the players armor
     */
    void setArmorContents(ItemStack[] items);

    /**
     * Gets the {@link ItemStack} in the players right (main) hand
     *
     * @return the item the player is holding in his/her right hand
     */
    ItemStack getItemInRightHand();

    /**
     * Gets the {@link ItemStack} in the players left (off) hand
     *
     * @return the item the player is holding in his/her left hand
     */
    ItemStack getItemInLeftHand();

    /**
     * Sets the item in the players boots slot
     *
     * @param boots the item to be placed in the players boots slot
     */
    void setBoots(ItemStack boots);

    /**
     * Sets the item in the players chestplate slot
     *
     * @param chestplate the item to be placed in the players chestplate slot
     */
    void setChestplate(ItemStack chestplate);

    /**
     * Sets the item in the players leggings slot
     *
     * @param leggings the item to be placed in the players leggings slot
     */
    void setLeggings(ItemStack leggings);

    /**
     * Sets the item in the players helmet slot
     *
     * @param helmet the item to be placed in the players helmet slot
     */
    void setHelmet(ItemStack helmet);

    /**
     * Sets the item in the players left (off) hand
     *
     * @param stack the item to be placed in the players left hand
     */
    void setItemInLeftHand(ItemStack stack);

    /**
     * Sets the item in the players left (main) hand
     *
     * @param stack the item to be placed in the players right hand
     */
    void setItemInRightHand(ItemStack stack);

    /**
     * Sets the players selected hotbar slot
     *
     * @param slot the new slot number
     */
    void setHeldItemSlot(int slot);
}
