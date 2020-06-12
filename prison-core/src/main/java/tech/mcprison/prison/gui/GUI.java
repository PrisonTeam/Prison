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

package tech.mcprison.prison.gui;

import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.inventory.Inventory;
import tech.mcprison.prison.internal.inventory.InventoryHolder;

import java.util.Map;

/**
 * A GUI is an inventory with buttons that players may click to perform certain actions.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public interface GUI extends InventoryHolder {

    /**
     * Show this GUI to a player or players.
     */
    void show(Player... player);

    /**
     * Build the GUI. Must be called before it is shown to players.
     *
     * @return Returns itself to allow for chaining.
     */
    GUI build();

    /**
     * Returns the title of this GUI, which is shown at the top of the inventory.
     */
    String getTitle();

    /**
     * Returns the amount of rows in the GUI.
     * Must be divisible by 9.
     */
    int getNumRows();

    /**
     * Returns a map, mapping slot numbers to {@link Button}s.
     */
    Map<Integer, Button> getButtons();

    /**
     * Add a button to a slot.
     *
     * @return Returns itself to allow for chaining.
     */
    GUI addButton(int slot, Button button);

    /**
     * Returns the inventory associated with this GUI. The inventory is null until {@link GUI#build()}
     * is called.
     */
    Inventory getInventory();

}
