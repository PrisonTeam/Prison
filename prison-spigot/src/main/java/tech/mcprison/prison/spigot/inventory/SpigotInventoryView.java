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

package tech.mcprison.prison.spigot.inventory;

import org.bukkit.inventory.InventoryView;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.inventory.Inventory;
import tech.mcprison.prison.internal.inventory.InventoryType;
import tech.mcprison.prison.internal.inventory.Viewable;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.game.SpigotPlayer;

/**
 * Currently undocumented.
 *
 * @author DMP9
 */
public class SpigotInventoryView implements Viewable {

    InventoryView wrapper;

    public SpigotInventoryView(InventoryView wrapper) {
        this.wrapper = wrapper;
    }

    public InventoryView getWrapper() {
        return wrapper;
    }

    @Override public void close() {
        wrapper.close();
    }

    @Override public int convertSlot(int rawSlot) {
        return wrapper.convertSlot(rawSlot);
    }

    @Override public int countSlots() {
        return wrapper.countSlots();
    }

    @Override public Inventory getBottomInventory() {
        return new SpigotInventory(wrapper.getBottomInventory());
    }

    @Override public ItemStack getCursor() {
        return SpigotUtil.bukkitItemStackToPrison(wrapper.getCursor());
    }

    @Override public void setCursor(ItemStack item) {
        wrapper.setCursor(SpigotUtil.prisonItemStackToBukkit(item));
    }

    @Override public ItemStack getItem(int slot) {
        return SpigotUtil.bukkitItemStackToPrison(wrapper.getItem(slot));
    }

    @Override public Player getPlayer() {
        return new SpigotPlayer((org.bukkit.entity.Player) wrapper.getPlayer());
    }

    @Override public String getTitle() {
        return wrapper.getTitle();
    }

    @Override public Inventory getTopInventory() {
        return new SpigotInventory(wrapper.getTopInventory());
    }

    @Override public InventoryType getType() {
        return SpigotUtil.bukkitInventoryTypeToPrison(wrapper.getType());
    }

    @Override public void setItem(int slot, ItemStack item) {
        wrapper.setItem(slot, SpigotUtil.prisonItemStackToBukkit(item));
    }

    @Override public boolean setProperty(Property prop, int value) {
        return wrapper.setProperty(SpigotUtil.prisonPropertyToBukkit(prop), value);
    }

}
