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

package tech.mcprison.prison.spigot;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.util.Block;
import tech.mcprison.prison.util.Location;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author SirFaizdat
 */
public class SpigotWorld implements World {

    private org.bukkit.World bukkitWorld;

    public SpigotWorld(org.bukkit.World bukkitWorld) {
        this.bukkitWorld = bukkitWorld;
    }

    @Override public String getName() {
        return bukkitWorld.getName();
    }

    @Override public List<Player> getPlayers() {
        return Bukkit.getServer().getOnlinePlayers().stream()
            .filter(player -> player.getWorld().getName().equals(bukkitWorld.getName()))
            .map((Function<org.bukkit.entity.Player, SpigotPlayer>) SpigotPlayer::new)
            .collect(Collectors.toList());
    }

    @Override public Block getBlockAt(Location location) {
        return Block.getBlock(
            bukkitWorld.getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ())
                .getTypeId());
    }

    @Override public void setBlockAt(Location location, Block block) {
        bukkitWorld.getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ())
            .setType(Material.getMaterial(block.getLegacyId()));
    }

}
