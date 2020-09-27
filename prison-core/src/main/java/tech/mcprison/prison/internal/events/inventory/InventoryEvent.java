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

package tech.mcprison.prison.internal.events.inventory;

import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.inventory.Inventory;
import tech.mcprison.prison.internal.inventory.Viewable;

import java.util.List;

/**
 * Currently undocumented.
 *
 * @author DMP9
 */
public class InventoryEvent {

    private Viewable transaction;


    public InventoryEvent(Viewable transaction) {
        this.transaction = transaction;
    }

    /**
     * Gets the primary Inventory involved in this transaction
     *
     * @return The upper inventory.
     */
    public Inventory getInventory() {
        return transaction.getTopInventory();
    }

    /**
     * Gets the list of players viewing the primary (upper) inventory involved
     * in this event
     *
     * @return A list of people viewing.
     */
    public List<Player> getViewers() {
        return transaction.getTopInventory().getViewers();
    }

    /**
     * Gets the view object itself
     *
     * @return InventoryView
     */
    public Viewable getView() {
        return transaction;
    }

    public enum Action {
        /**
         * Nothing will happen from the click.
         * <p>
         * There may be cases where nothing will happen and this is value is not
         * provided, but it is guaranteed that this value is accurate when given.
         */
        NOTHING, /**
         * All of the items on the clicked slot are moved to the cursor.
         */
        PICKUP_ALL, /**
         * Some of the items on the clicked slot are moved to the cursor.
         */
        PICKUP_SOME, /**
         * Half of the items on the clicked slot are moved to the cursor.
         */
        PICKUP_HALF, /**
         * One of the items on the clicked slot are moved to the cursor.
         */
        PICKUP_ONE, /**
         * All of the items on the cursor are moved to the clicked slot.
         */
        PLACE_ALL, /**
         * Some of the items from the cursor are moved to the clicked slot
         * (usually up to the max stack size).
         */
        PLACE_SOME, /**
         * A single item from the cursor is moved to the clicked slot.
         */
        PLACE_ONE, /**
         * The clicked item and the cursor are exchanged.
         */
        SWAP_WITH_CURSOR, /**
         * The entire cursor item is dropped.
         */
        DROP_ALL_CURSOR, /**
         * One item is dropped from the cursor.
         */
        DROP_ONE_CURSOR, /**
         * The entire clicked slot is dropped.
         */
        DROP_ALL_SLOT, /**
         * One item is dropped from the clicked slot.
         */
        DROP_ONE_SLOT, /**
         * The item is moved to the opposite inventory if a space is found.
         */
        MOVE_TO_OTHER_INVENTORY, /**
         * The clicked item is moved to the hotbar, and the item currently there
         * is re-added to the player's inventory.
         */
        HOTBAR_MOVE_AND_READD, /**
         * The clicked slot and the picked hotbar slot are swapped.
         */
        HOTBAR_SWAP, /**
         * A max-size stack of the clicked item is put on the cursor.
         */
        CLONE_STACK, /**
         * The inventory is searched for the same material, and they are put on
         * the cursor up to the maxiumum stack size.
         */
        COLLECT_TO_CURSOR, /**
         * An unrecognized ClickType.
         */
        UNKNOWN
    }

}
