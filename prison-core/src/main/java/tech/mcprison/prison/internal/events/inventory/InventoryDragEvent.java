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

import com.google.common.collect.ImmutableSet;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.inventory.Viewable;

import java.util.Map;
import java.util.Set;

/**
 * Currently undocumented.
 *
 * @author DMP9
 */
public class InventoryDragEvent extends InventoryInteractEvent {

    private boolean right;
    private ItemStack newCursor;
    private ItemStack oldCursor;
    private Map<Integer, ItemStack> slots;
    private Set<Integer> parsedSlots;

    public InventoryDragEvent(Viewable transaction, ItemStack newCursor, ItemStack oldCursor,
        boolean right, Map<Integer, ItemStack> slots) {
        super(transaction);
        this.right = right;
        this.newCursor = newCursor;
        this.oldCursor = oldCursor;
        this.slots = slots;
        ImmutableSet.Builder<Integer> b = ImmutableSet.builder();
        for (Integer slot : slots.keySet()) {
            b.add(transaction.convertSlot(slot));
        }
        this.parsedSlots = b.build();
    }

    public ItemStack getCursor() {
        return newCursor;
    }

    public void setCursor(ItemStack newCursor) {
        this.newCursor = newCursor;
    }

    public Set<Integer> getInventorySlots() {
        return parsedSlots;
    }

    public Map<Integer, ItemStack> getNewItems() {
        return slots;
    }

    public ItemStack getOldCursor() {
        return oldCursor;
    }

    public Set<Integer> getRawSlots() {
        return slots.keySet();
    }

    public Drag getType() {
        return right ? Drag.SINGLE : Drag.EVEN;
    }

    public enum Drag {
        SINGLE, EVEN
    }

}
