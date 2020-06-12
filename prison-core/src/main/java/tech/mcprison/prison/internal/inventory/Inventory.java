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
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.util.BlockType;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Represents an inventory.
 * An inventory can belong to a player, a chest, or to nothing at all (i.e. as a GUI).
 *
 * @author Dylan M. Perks
 * @author Faizaan A. Datoo
 */
public interface Inventory extends Iterable<ItemStack> {

    /**
     * The size of the inventory. This will be a multiple of 9.
     *
     * @return The inventory's size.
     */
    int getSize();

    /**
     * The maximum size that an {@link ItemStack} can be in this inventory.
     *
     * @return The inventory's max stack size.
     */
    int getMaxStackSize();

    /**
     * Set the maximum size that an {@link ItemStack} can be in this inventory.
     *
     * @param size The inventory's new maximum stack size.
     */
    void setMaxStackSize(int size);

    /**
     * The name of this inventory, shown at the top.
     *
     * @return The inventory's name.
     */
    String getName();

    /**
     * Returns whether the inventory is empty or not.
     *
     * @return true if it's empty, false otherwise.
     */
    boolean isEmpty();

    /**
     * Returns whether the {@link ItemStack} is contained by the inventory.
     *
     * @param itemStack The {@link ItemStack} to look for. Note that the amount of this stack will be
     *                  taken into account. For non-amount-dependant searches, use {@link #contains(BlockType)}.
     * @return true if the inventory contains the item stack, false otherwise.
     */
    boolean contains(ItemStack itemStack);

    /**
     * Returns whether the {@link BlockType} is contained by the inventory.
     *
     * @param type The {@link BlockType} to look for.
     * @return true if the inventory contains the block type, false otherwise.
     */
    boolean contains(BlockType type);

    Iterator<ItemStack> getIterator();

    /**
     * Returns an array of all the items in this inventory.
     *
     * @return An array of {@link ItemStack}.
     */
    ItemStack[] getItems();

    /**
     * Sets the contents of this inventory
     *
     * @param items the items to replace the current contents with
     */
    void setItems(List<ItemStack> items);

    /**
     * Creates a {@link HashMap} with items with the same {@link BlockType}
     *
     * @param type the {@link BlockType} to check the inventory contents against.
     * @return a HashMap with the index of items with matching BlockTypes as the keys, and said items
     * as the values
     */
    HashMap<Integer, ItemStack> getItems(BlockType type);

    /**
     * Creates a {@link HashMap} with items matching the specified {@link ItemStack}
     *
     * @param stack the {@link ItemStack} to check the inventory contents against.
     * @return a HashMap with the index of items with matching items as the keys, and said items as
     * the values
     */
    HashMap<Integer, ItemStack> getItems(ItemStack stack);

    /**
     * Gets an item in a specific slot of this inventory
     *
     * @param index the index of the item
     * @return the item at the specified index
     */
    ItemStack getItem(int index);

    /**
     * Adds the specified item(s) to a this inventory
     *
     * @param itemStack the item(s) to add to this inventory
     */
    void addItem(ItemStack... itemStack);

    /**
     * Removes the specified item(s) to a this inventory
     *
     * @param itemStack the item(s) to remove to this inventory
     */
    void removeItem(ItemStack... itemStack);

    /**
     * Clears all the items contained within this inventory
     */
    void clearAll();

    /**
     * Clears all the items contained within this inventory from the specified index
     *
     * @param index the index to start clearing from
     */
    void clearAll(int index);

    /**
     * Clears a slot of this inventory
     *
     * @param index the index of the slot to be cleared
     */
    void clear(int index);

    /**
     * Clears all the slots with matching BlockTypes
     *
     * @param type the {@link BlockType} to remove from the inventory
     */
    void clear(BlockType type);

    /**
     * Clears all the slots matching the specified {@link ItemStack}
     *
     * @param stack the {@link ItemStack} to remove from this inventory
     */
    void clear(ItemStack stack);

    /**
     * Gets the index of the first instance of the specified {@link ItemStack} in this inventory.
     *
     * @param stack the item to get the first index of
     * @return the first index of the item
     */
    int first(ItemStack stack);

    /**
     * Gets the index of the first instance of the specified {@link BlockType} in this inventory.
     *
     * @param type the {@link BlockType} to get the first index of
     * @return the first index of the BlockType
     */
    int first(BlockType type);

    /**
     * Gets the index of the first empty slot in this inventory
     *
     * @return the first empty slot index
     */
    int firstEmpty();

    /**
     * Gets the {@link InventoryHolder} of this inventory
     *
     * @return the holder of this inventory (i.e. Entity or Block
     * @see InventoryType
     */
    InventoryHolder getHolder();

    /**
     * Gets all the players viewing this Inventory
     *
     * @return a list of players viewing this inventory
     */
    List<Player> getViewers();

    /**
     * Gets the title of this inventory
     *
     * @return the title of this inventory
     */
    String getTitle();

    /**
     * Gets the type of this inventory
     *
     * @return the type of this inventory
     * @see InventoryHolder
     */
    InventoryType getType();

}
