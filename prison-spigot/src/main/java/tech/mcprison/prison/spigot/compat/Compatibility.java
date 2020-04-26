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

package tech.mcprison.prison.spigot.compat;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Different Spigot versions have different methods.
 * The compatibility system ensures that each version can be used with the same code.
 *
 * @author Faizaan A. Datoo
 */
public interface Compatibility {

    EquipmentSlot getHand(PlayerInteractEvent e);

    ItemStack getItemInMainHand(PlayerInteractEvent e);

    ItemStack getItemInMainHand(Player player);
    
    void playIronDoorSound(Location loc);

    enum EquipmentSlot {
        HAND, OFF_HAND, FEET, LEGS, CHEST, HEAD
    }

}
