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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import tech.mcprison.prison.gui.Button;
import tech.mcprison.prison.gui.ClickedButton;
import tech.mcprison.prison.gui.GUI;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.game.SpigotPlayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Faizaan A. Datoo
 */
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

    @EventHandler public void reactToClick(InventoryClickEvent e) {
        final GUI[] gui = new GUI[1];
        final Button[] b =
            new Button[1]; // Workaround to Java lambda's stupid final rule, elements within final arrays are re-assignable >:)
        inventories.stream().filter(inv -> inv.getTitle().equals(e.getInventory().getTitle()))
            .forEach(inv -> {
                gui[0] = inv;
                b[0] = inv.getButtons().get(e.getSlot());
            });
        e.getWhoClicked().sendMessage("Click recognized");
        if (b[0] == null) {
            e.getWhoClicked().sendMessage("NOT BTN");
            return;
        }
        e.setCancelled(true);
        if (b[0].isCloseOnClick()) {
            e.getWhoClicked().closeInventory();
        }
        b[0].getAction()
            .run(new ClickedButton(b[0], gui[0], new SpigotPlayer((Player) e.getWhoClicked())));
    }

    @EventHandler public void closeInventory(InventoryCloseEvent e) {
        Iterator<GUI> i = inventories.iterator();
        while (i.hasNext()) {
            if (i.next().getTitle().equals(e.getInventory().getTitle())) {
                i.remove(); // Remove it if found
            }
        }
    }

}
