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

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.inventory.InventoryType;
import tech.mcprison.prison.internal.inventory.Viewable;

/**
 * Currently undocumented.
 *
 * @author DMP9
 */
public class InventoryClickEvent extends InventoryInteractEvent {

    private Click click;
    private InventoryType.SlotType type;
    private int slot;
    private Action action;
    private int hotbar = -1;
    private ItemStack current = null;

    public InventoryClickEvent(Viewable transaction, InventoryType.SlotType type, int slot,
        Click click, Action action) {
        super(transaction);
        this.type = type;
        this.slot = slot;
        this.click = click;
        this.action = action;
    }

    public InventoryClickEvent(Viewable transaction, InventoryType.SlotType type, int slot,
        Click click, Action action, int key) {
        super(transaction);
        this.type = type;
        this.slot = slot;
        this.click = click;
        this.action = action;
        this.hotbar = key;
    }

    public InventoryEvent.Action getAction() {
        return action;
    }

    public ItemStack getCurrentItem() {
        if (getSlotType() == InventoryType.SlotType.NONE) {
            return current;
        }
        return getView().getItem(slot);
    }

    public void setCurrentItem(ItemStack stack) {
        if (type == InventoryType.SlotType.NONE) {
            current = stack;
        } else {
            getView().setItem(slot, stack);
        }
    }

    public ItemStack getCursor() {
        return getView().getCursor();
    }

    public int getHotbarButton() {
        return hotbar;
    }

    public int getRawSlot() {
        return slot;
    }

    public int getSlot() {
        return getView().convertSlot(getRawSlot());
    }

    public InventoryType.SlotType getSlotType() {
        return type;
    }

    public boolean isLeftClick() {
        return (click == Click.LEFT) || (click == Click.SHIFT_LEFT) || (click == Click.DOUBLE_CLICK)
            || (click == Click.CREATIVE);
    }

    public boolean isRightClick() {
        return (click == Click.RIGHT) || (click == Click.SHIFT_RIGHT);
    }

    public boolean isShiftClick() {
        return (click == Click.SHIFT_LEFT) || (click == Click.SHIFT_RIGHT) || (click
            == Click.CONTROL_DROP);
    }

    public enum Click {
        /**
         * The left (or primary) mouse button.
         */
        LEFT, /**
         * Holding shift while pressing the left mouse button.
         */
        SHIFT_LEFT, /**
         * The right mouse button.
         */
        RIGHT, /**
         * Holding shift while pressing the right mouse button.
         */
        SHIFT_RIGHT, /**
         * Clicking the left mouse button on the grey area around the inventory.
         */
        WINDOW_BORDER_LEFT, /**
         * Clicking the right mouse button on the grey area around the inventory.
         */
        WINDOW_BORDER_RIGHT, /**
         * The middle mouse button, or a "scrollwheel click".
         */
        MIDDLE, /**
         * One of the number keys 1-9, correspond to slots on the hotbar.
         */
        NUMBER_KEY, /**
         * Pressing the left mouse button twice in quick succession.
         */
        DOUBLE_CLICK, /**
         * The "Drop" key (defaults to Q).
         */
        DROP, /**
         * Holding Ctrl while pressing the "Drop" key (defaults to Q).
         */
        CONTROL_DROP, /**
         * Any action done with the Creative inventory open.
         */
        CREATIVE, /**
         * A type of inventory manipulation not yet recognized by Bukkit.
         * <p>
         * This is only for transitional purposes on a new Minecraft update, and
         * should never be relied upon.
         * <p>
         * Any Click.UNKNOWN is called on a best-effort basis.
         */
        UNKNOWN
    }

}
