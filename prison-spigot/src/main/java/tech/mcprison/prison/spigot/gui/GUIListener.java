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

package tech.mcprison.prison.spigot.gui;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import tech.mcprison.prison.gui.GUI;
import tech.mcprison.prison.spigot.SpigotPrison;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Faizaan A. Datoo
 */
// From GABRYCA, I don't know if this's still needed, I won't remove it for now, but might be in the future
public class GUIListener implements Listener {

    private static GUIListener instance;
    private List<GUI> inventories = new ArrayList<>();

    public static GUIListener get() {
        if (instance == null) {
            instance = new GUIListener();
        }
        return instance;
    }

    public void init(SpigotPrison prison) {
        Bukkit.getServer().getPluginManager().registerEvents(this, prison);
    }

    public void registerInventory(GUI inv) {
        inventories.add(inv);
    }

    @EventHandler public void closeInventory(InventoryCloseEvent e) {
        // Remove it if found
        inventories.removeIf(gui -> gui.getTitle().equals(e.getInventory().getTitle()));
    }

}
