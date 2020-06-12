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

/**
 * Currently undocumented.
 *
 * @author DMP9
 */
public interface Viewable {

    void close();

    int convertSlot(int rawSlot);

    int countSlots();

    Inventory getBottomInventory();

    ItemStack getCursor();

    void setCursor(ItemStack item);

    ItemStack getItem(int slot);

    Player getPlayer();

    String getTitle();

    Inventory getTopInventory();

    InventoryType getType();

    void setItem(int slot, ItemStack item);

    boolean setProperty(Viewable.Property prop, int value);

    enum Property {
        /**
         * The progress of the down-pointing arrow in a brewing inventory.
         */
        BREW_TIME(0, InventoryType.BREWING), /**
         * The progress of the flame in a furnace inventory.
         */
        BURN_TIME(0, InventoryType.FURNACE), /**
         * How many total ticks the current fuel should last.
         */
        TICKS_FOR_CURRENT_FUEL(1, InventoryType.FURNACE), /**
         * The progress of the right-pointing arrow in a furnace inventory.
         */
        COOK_TIME(2, InventoryType.FURNACE), /**
         * How many total ticks the current smelting should last.
         */
        TICKS_FOR_CURRENT_SMELTING(3, InventoryType.FURNACE), /**
         * In an enchanting inventory, the top button's experience level
         * value.
         */
        ENCHANT_BUTTON1(0, InventoryType.ENCHANTING), /**
         * In an enchanting inventory, the middle button's experience level
         * value.
         */
        ENCHANT_BUTTON2(1, InventoryType.ENCHANTING), /**
         * In an enchanting inventory, the bottom button's experience level
         * value.
         */
        ENCHANT_BUTTON3(2, InventoryType.ENCHANTING), /**
         * In an enchanting inventory, the first four bits of the player's xpSeed.
         */
        ENCHANT_XP_SEED(3, InventoryType.ENCHANTING), /**
         * In an enchanting inventory, the top button's enchantment's id
         */
        ENCHANT_ID1(4, InventoryType.ENCHANTING), /**
         * In an enchanting inventory, the middle button's enchantment's id
         */
        ENCHANT_ID2(5, InventoryType.ENCHANTING), /**
         * In an enchanting inventory, the bottom button's enchantment's id
         */
        ENCHANT_ID3(6, InventoryType.ENCHANTING), /**
         * In an enchanting inventory, the top button's level value.
         */
        ENCHANT_LEVEL1(7, InventoryType.ENCHANTING), /**
         * In an enchanting inventory, the middle button's level value.
         */
        ENCHANT_LEVEL2(8, InventoryType.ENCHANTING), /**
         * In an enchanting inventory, the bottom button's level value.
         */
        ENCHANT_LEVEL3(9, InventoryType.ENCHANTING), /**
         * In an beacon inventory, the levels of the beacon
         */
        LEVELS(0, InventoryType.BEACON), /**
         * In an beacon inventory, the primary potion effect
         */
        PRIMARY_EFFECT(1, InventoryType.BEACON), /**
         * In an beacon inventory, the secondary potion effect
         */
        SECONDARY_EFFECT(2, InventoryType.BEACON), /**
         * The repair's cost in xp levels
         */
        REPAIR_COST(0, InventoryType.ANVIL);
        int id;
        InventoryType style;

        Property(int id, InventoryType appliesTo) {
            this.id = id;
            style = appliesTo;
        }

        public InventoryType getType() {
            return style;
        }
    }

}
