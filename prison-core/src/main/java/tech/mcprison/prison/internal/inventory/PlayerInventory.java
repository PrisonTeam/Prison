/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017-2020 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.internal.inventory;

import tech.mcprison.prison.internal.ItemStack;

/**
 * Represents an {@link Inventory} belonging to a Player
 *
 * @author Dylan M. Perks
 * @since API 1.0
 */
public interface PlayerInventory extends Inventory {

    /**
     * Gets the armor this player is wearing
     *
     * @return a list of items in this inventories armor slots
     */
    ItemStack[] getArmorContents();

    /**
     * Sets the armor this player is wearing
     *
     * @param items a list of items to set as the players armor
     */
    void setArmorContents(ItemStack[] items);

    /**
     * Gets the {@link ItemStack} in this inventories boots slot
     *
     * @return the boots (or the item in the boots slot) being worn
     */
    ItemStack getBoots();

    /**
     * Sets the item in the players boots slot
     *
     * @param boots the item to be placed in the players boots slot
     */
    void setBoots(ItemStack boots);

    /**
     * Gets the {@link ItemStack} in this inventories chestplate slot
     *
     * @return the chestplate (or the item in the chestplate slot) being worn
     */
    ItemStack getChestplate();

    /**
     * Sets the item in the players chestplate slot
     *
     * @param chestplate the item to be placed in the players chestplate slot
     */
    void setChestplate(ItemStack chestplate);

    /**
     * Gets the slot number of the selected hotbar slot
     *
     * @return the slot number
     */
    int getHeldItemSlot();

    /**
     * Sets the players selected hotbar slot
     *
     * @param slot the new slot number
     */
    void setHeldItemSlot(int slot);

    /**
     * Gets the {@link ItemStack} in this inventories helmet slot
     *
     * @return the helmet (or the item in the helmet slot) being worn
     */
    ItemStack getHelmet();

    /**
     * Sets the item in the players helmet slot
     *
     * @param helmet the item to be placed in the players helmet slot
     */
    void setHelmet(ItemStack helmet);

    /**
     * Gets the {@link ItemStack} in this inventories leggings slot
     *
     * @return the leggings (or the item in the leggings slot) being worn
     */
    ItemStack getLeggings();

    /**
     * Sets the item in the players leggings slot
     *
     * @param leggings the item to be placed in the players leggings slot
     */
    void setLeggings(ItemStack leggings);

    /**
     * Gets the {@link ItemStack} in the players right (main) hand
     *
     * @return the item the player is holding in his/her right hand
     */
    ItemStack getItemInRightHand();

    /**
     * Sets the item in the players left (main) hand
     *
     * @param stack the item to be placed in the players right hand
     */
    void setItemInRightHand(ItemStack stack);

    /**
     * Gets the {@link ItemStack} in the players left (off) hand
     *
     * @return the item the player is holding in his/her left hand
     */
    ItemStack getItemInLeftHand();


    /**
     * Sets the item in the players left (off) hand
     *
     * @param stack the item to be placed in the players left hand
     */
    void setItemInLeftHand(ItemStack stack);

}
