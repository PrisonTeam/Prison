/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2016 The Prison Team
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

package io.github.prison.spigot;

import io.github.prison.internal.ItemStack;
import io.github.prison.internal.Player;
import io.github.prison.util.Location;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

/**
 * @author SirFaizdat
 */
public class SpigotPlayer extends SpigotCommandSender implements Player {

    private org.bukkit.entity.Player bukkitPlayer;

    public SpigotPlayer(org.bukkit.entity.Player bukkitPlayer) {
        super(bukkitPlayer);
        this.bukkitPlayer = bukkitPlayer;
    }

    @Override
    public UUID getUUID() {
        return bukkitPlayer.getUniqueId();
    }

    @Override
    public String getDisplayName() {
        return bukkitPlayer.getDisplayName();
    }

    @Override
    public void setDisplayName(String newDisplayName) {
        bukkitPlayer.setDisplayName(newDisplayName);
    }

    @Override
    public void give(ItemStack itemStack) {
        org.bukkit.inventory.ItemStack bStack = new org.bukkit.inventory.ItemStack(itemStack.getMaterial().getLegacyId(), itemStack.getAmount());
        ItemMeta meta = bStack.getItemMeta();
        meta.setDisplayName(itemStack.getName());
        bStack.setItemMeta(meta);

        bukkitPlayer.getInventory().addItem(bStack);
    }

    @Override
    public Location getLocation() {
        return new Location(
                new SpigotWorld(bukkitPlayer.getWorld()),
                bukkitPlayer.getLocation().getX(),
                bukkitPlayer.getLocation().getY(),
                bukkitPlayer.getLocation().getZ(),
                bukkitPlayer.getLocation().getPitch(),
                bukkitPlayer.getLocation().getYaw());
    }

    @Override
    public void teleport(Location location) {
        bukkitPlayer.teleport(new org.bukkit.Location(
                Bukkit.getWorld(location.getWorld().getName()),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getPitch(),
                location.getYaw()
        ), PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

}
