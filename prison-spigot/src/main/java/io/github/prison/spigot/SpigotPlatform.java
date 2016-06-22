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

import io.github.prison.Capability;
import io.github.prison.Platform;
import io.github.prison.Prison;
import io.github.prison.Scheduler;
import io.github.prison.commands.PluginCommand;
import io.github.prison.gui.GUI;
import io.github.prison.internal.Player;
import io.github.prison.internal.World;
import io.github.prison.util.Location;
import io.github.prison.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;

import java.io.File;
import java.util.*;

/**
 * @author SirFaizdat
 */
class SpigotPlatform implements Platform {

    private SpigotPrison plugin;
    private List<PluginCommand> commands = new ArrayList<>();

    SpigotPlatform(SpigotPrison plugin) {
        this.plugin = plugin;
    }

    @Override
    public World getWorld(String name) {
        if (Bukkit.getWorld(name) == null) return null; // Avoid NPE
        return new SpigotWorld(Bukkit.getWorld(name));
    }

    @Override
    public Player getPlayer(String name) {
        if (Bukkit.getPlayer(name) == null) return null; // Avoid NPE
        return new SpigotPlayer(Bukkit.getPlayer(name));
    }

    @Override
    public Player getPlayer(UUID uuid) {
        if (Bukkit.getPlayer(uuid) == null) return null; // Avoid NPE
        return new SpigotPlayer(Bukkit.getPlayer(uuid));
    }

    @Override
    public List<Player> getOnlinePlayers() {
        List<Player> players = new ArrayList<>();
        for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) players.add(getPlayer(player.getUniqueId()));
        return players;
    }

    @Override
    public String getPluginVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public File getPluginDirectory() {
        return plugin.getDataFolder();
    }

    @Override
    public void registerCommand(PluginCommand command) {
        // Rather than putting commands into plugin.yml, we automatically inject them into the command map. Neat, eh?
        plugin.commandMap.register(command.getLabel(), "prison", new Command(command.getLabel(), command.getDescription(), command.getUsage(), Collections.emptyList()) {

            @Override
            public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                if (sender instanceof org.bukkit.entity.Player)
                    return Prison.getInstance().getCommandHandler().onCommand(new SpigotPlayer((org.bukkit.entity.Player) sender), command, commandLabel, args);
                return Prison.getInstance().getCommandHandler().onCommand(new SpigotCommandSender(sender), command, commandLabel, args);
            }

        });
        commands.add(command);
    }

    @Override
    public List<PluginCommand> getCommands() {
        return commands;
    }

    @Override
    public Scheduler getScheduler() {
        return plugin.scheduler;
    }

    @Override
    public GUI createGUI(String title, int numRows) {
        return new SpigotGUI(title, numRows);
    }

    public void toggleDoor(Location doorLocation) {
        org.bukkit.Location bLoc = new org.bukkit.Location(
                Bukkit.getWorld(doorLocation.getWorld().getName()),
                doorLocation.getX(),
                doorLocation.getY(),
                doorLocation.getZ()
        );
        Block block = bLoc.getWorld().getBlockAt(bLoc).getRelative(BlockFace.DOWN);

        BlockState state = block.getState();
        Openable openable = (Openable) state.getData();
        openable.setOpen(!openable.isOpen());
        state.setData((MaterialData) openable);
        state.update();
    }

    @Override
    public void log(String message, Object... format) {
        message = TextUtil.parse("&8[&3Prison&8]&r " + message, format);

        ConsoleCommandSender sender = Bukkit.getConsoleSender();
        if (sender == null) Bukkit.getLogger().info(ChatColor.stripColor(message));
        else sender.sendMessage(message);
    }

    @Override
    public Map<Capability, Boolean> getCapabilities() {
        Map<Capability, Boolean> capabilities = new HashMap<>();
        capabilities.put(Capability.GUI, true);
        return capabilities;
    }

}
