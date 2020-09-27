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

import tech.mcprison.prison.internal.inventory.CraftingInventory;
import tech.mcprison.prison.internal.inventory.InventoryType;
import tech.mcprison.prison.internal.inventory.Recipe;
import tech.mcprison.prison.internal.inventory.Viewable;

/**
 * Currently undocumented.
 *
 * @author DMP9
 */
public class CraftItemEvent extends InventoryClickEvent {

    private Recipe recipe;

    public CraftItemEvent(Recipe recipe, Viewable what, InventoryType.SlotType type, int slot,
        Click click, Action action) {
        super(what, type, slot, click, action);
        this.recipe = recipe;
    }

    public CraftItemEvent(Recipe recipe, Viewable what, InventoryType.SlotType type, int slot,
        Click click, Action action, int key) {
        super(what, type, slot, click, action, key);
        this.recipe = recipe;
    }

    public CraftingInventory getInventory() {
        return (CraftingInventory) getView().getTopInventory();
    }

    public Recipe getRecipe() {
        return recipe;
    }

}
