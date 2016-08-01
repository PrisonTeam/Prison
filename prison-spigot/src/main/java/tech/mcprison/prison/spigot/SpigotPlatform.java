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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.PluginCommand;
import tech.mcprison.prison.economy.Economy;
import tech.mcprison.prison.gui.GUI;
import tech.mcprison.prison.internal.*;
import tech.mcprison.prison.modules.Capability;
import tech.mcprison.prison.spigot.economies.EssentialsEconomy;
import tech.mcprison.prison.spigot.economies.VaultEconomy;
import tech.mcprison.prison.util.Location;
import tech.mcprison.prison.util.TextUtil;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author SirFaizdat
 */
class SpigotPlatform implements Platform {

    private SpigotPrison plugin;
    private List<PluginCommand> commands = new ArrayList<>();

    SpigotPlatform(SpigotPrison plugin) {
        this.plugin = plugin;
    }

    @Override public World getWorld(String name) {
        if (Bukkit.getWorld(name) == null)
            return null; // Avoid NPE
        return new SpigotWorld(Bukkit.getWorld(name));
    }

    @Override public Player getPlayer(String name) {
        if (Bukkit.getPlayer(name) == null)
            return null; // Avoid NPE
        return new SpigotPlayer(Bukkit.getPlayer(name));
    }

    @Override public Player getPlayer(UUID uuid) {
        if (Bukkit.getPlayer(uuid) == null)
            return null; // Avoid NPE
        return new SpigotPlayer(Bukkit.getPlayer(uuid));
    }

    @Override public List<Player> getOnlinePlayers() {
        return Bukkit.getOnlinePlayers().stream().map(player -> getPlayer(player.getUniqueId()))
            .collect(Collectors.toList());
    }

    @Override public Sign getSign(Location location) {
        org.bukkit.block.Sign sign = (org.bukkit.block.Sign) new org.bukkit.Location(
            Bukkit.getWorld(location.getWorld().getName()), location.getX(), location.getY(),
            location.getZ()).getBlock().getState();
        return new SpigotSign(sign);
    }

    @Override public Economy getEconomy() {
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("Essentials"))
            return new EssentialsEconomy();
        else if (Bukkit.getServer().getPluginManager().isPluginEnabled("Vault"))
            return new VaultEconomy();
        else
            return null;
    }

    @Override public String getPluginVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override public File getPluginDirectory() {
        return plugin.getDataFolder();
    }

    @Override public void registerCommand(PluginCommand command) {
        plugin.commandMap.register(command.getLabel(), "prison",
            new Command(command.getLabel(), command.getDescription(), command.getUsage(),
                Collections.emptyList()) {

                @Override
                public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                    if (sender instanceof org.bukkit.entity.Player)
                        return Prison.getInstance().getCommandHandler()
                            .onCommand(new SpigotPlayer((org.bukkit.entity.Player) sender), command,
                                commandLabel, args);
                    return Prison.getInstance().getCommandHandler()
                        .onCommand(new SpigotCommandSender(sender), command, commandLabel, args);
                }

            });
        commands.add(command);
    }

    @Override public List<PluginCommand> getCommands() {
        return commands;
    }

    @Override public Scheduler getScheduler() {
        return plugin.scheduler;
    }

    @Override public GUI createGUI(String title, int numRows) {
        return new SpigotGUI(title, numRows);
    }

    public void toggleDoor(Location doorLocation) {
        org.bukkit.Location bLoc =
            new org.bukkit.Location(Bukkit.getWorld(doorLocation.getWorld().getName()),
                doorLocation.getX(), doorLocation.getY(), doorLocation.getZ());
        Block block = bLoc.getWorld().getBlockAt(bLoc).getRelative(BlockFace.DOWN);
        if (!isDoor(block.getType()))
            return;

        BlockState state = block.getState();
        Openable openable = (Openable) state.getData();
        openable.setOpen(!openable.isOpen());
        state.setData((MaterialData) openable);
        state.update();
        plugin.compatibility.playIronDoorSound(block.getLocation());
    }

    @Override public void log(String message, Object... format) {
        message = TextUtil.parse("&8[&3Prison&8]&r " + message, format);

        ConsoleCommandSender sender = Bukkit.getConsoleSender();
        if (sender == null)
            Bukkit.getLogger().info(ChatColor.stripColor(message));
        else
            sender.sendMessage(message);
    }

    @Override public void showTitle(Player player, String title, String subtitle, int fade) {
        org.bukkit.entity.Player play = Bukkit.getPlayer(player.getName());
        play.sendTitle(title, subtitle);
    }

    @Override public void showActionBar(Player player, String text) {
        org.bukkit.entity.Player play = Bukkit.getPlayer(player.getName());
    }

    private boolean isDoor(Material block) {
        return block == Material.ACACIA_DOOR || block == Material.BIRCH_DOOR
            || block == Material.DARK_OAK_DOOR || block == Material.IRON_DOOR_BLOCK
            || block == Material.JUNGLE_DOOR || block == Material.WOODEN_DOOR
            || block == Material.SPRUCE_DOOR;
    }

    @Override public Map<Capability, Boolean> getCapabilities() {
        Map<Capability, Boolean> capabilities = new HashMap<>();
        capabilities.put(Capability.GUI, true);
        return capabilities;
    }

}
