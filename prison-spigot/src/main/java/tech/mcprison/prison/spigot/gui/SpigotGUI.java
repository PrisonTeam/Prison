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

package tech.mcprison.prison.spigot.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tech.mcprison.prison.gui.Button;
import tech.mcprison.prison.gui.GUI;
import tech.mcprison.prison.internal.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Faizaan A. Datoo
 */
public class SpigotGUI implements GUI {

    private Map<Integer, Button> buttons;
    private String title;
    private int numSlots;

    private Inventory bukkitInventory;

    public SpigotGUI(String title, int numSlots) {
        this.buttons = new HashMap<>();
        this.title = title;
        this.numSlots = numSlots;
    }

    @Override public void show(Player... players) {
        for (Player player : players) {
            org.bukkit.entity.Player bPlayer = Bukkit.getServer().getPlayer(player.getName());
            bPlayer.openInventory(bukkitInventory);
        }
        GUIListener.get().registerInventory(this);
    }

    @Override public GUI build() {
        bukkitInventory = Bukkit.getServer()
            .createInventory(null, numSlots, ChatColor.translateAlternateColorCodes('&', title));
        for (Map.Entry<Integer, Button> button : buttons.entrySet()) {
            bukkitInventory.setItem(button.getKey(), buttonToItemStack(button.getValue()));
        }

        return this;
    }

    private ItemStack buttonToItemStack(Button button) {
        ItemStack stack =
            new ItemStack(button.getItem().getLegacyId(), 1, button.getItem().getData());
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&r" + button.getName()));
        meta.setLore(button.getLore());
        stack.setItemMeta(meta);
        return stack;
    }

    @Override public String getTitle() {
        return title;
    }

    @Override public int getNumRows() {
        return numSlots / 9;
    }

    @Override public Map<Integer, Button> getButtons() {
        return buttons;
    }

    @Override public GUI addButton(int slot, Button button) {
        buttons.put(slot, button);
        return this;
    }

    public Inventory getWrapper() {
        return bukkitInventory;
    }
}
