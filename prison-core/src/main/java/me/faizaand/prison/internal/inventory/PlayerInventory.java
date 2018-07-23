/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017 The Prison Team
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

package me.faizaand.prison.internal.inventory;

import me.faizaand.prison.internal.GameItemStack;

/**
 * Represents an {@link Inventory} belonging to a GamePlayer
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
    GameItemStack[] getArmorContents();

    /**
     * Sets the armor this player is wearing
     *
     * @param items a list of items to set as the players armor
     */
    void setArmorContents(GameItemStack[] items);

    /**
     * Gets the {@link GameItemStack} in this inventories boots slot
     *
     * @return the boots (or the item in the boots slot) being worn
     */
    GameItemStack getBoots();

    /**
     * Sets the item in the players boots slot
     *
     * @param boots the item to be placed in the players boots slot
     */
    void setBoots(GameItemStack boots);

    /**
     * Gets the {@link GameItemStack} in this inventories chestplate slot
     *
     * @return the chestplate (or the item in the chestplate slot) being worn
     */
    GameItemStack getChestplate();

    /**
     * Sets the item in the players chestplate slot
     *
     * @param chestplate the item to be placed in the players chestplate slot
     */
    void setChestplate(GameItemStack chestplate);

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
     * Gets the {@link GameItemStack} in this inventories helmet slot
     *
     * @return the helmet (or the item in the helmet slot) being worn
     */
    GameItemStack getHelmet();

    /**
     * Sets the item in the players helmet slot
     *
     * @param helmet the item to be placed in the players helmet slot
     */
    void setHelmet(GameItemStack helmet);

    /**
     * Gets the {@link GameItemStack} in this inventories leggings slot
     *
     * @return the leggings (or the item in the leggings slot) being worn
     */
    GameItemStack getLeggings();

    /**
     * Sets the item in the players leggings slot
     *
     * @param leggings the item to be placed in the players leggings slot
     */
    void setLeggings(GameItemStack leggings);

    /**
     * Gets the {@link GameItemStack} in the players right (main) hand
     *
     * @return the item the player is holding in his/her right hand
     */
    GameItemStack getItemInRightHand();

    /**
     * Sets the item in the players left (main) hand
     *
     * @param stack the item to be placed in the players right hand
     */
    void setItemInRightHand(GameItemStack stack);

    /**
     * Gets the {@link GameItemStack} in the players left (off) hand
     *
     * @return the item the player is holding in his/her left hand
     */
    GameItemStack getItemInLeftHand();


    /**
     * Sets the item in the players left (off) hand
     *
     * @param stack the item to be placed in the players left hand
     */
    void setItemInLeftHand(GameItemStack stack);

}
