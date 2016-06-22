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

package io.github.prison.sponge;

import io.github.prison.gui.GUI;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import io.github.prison.Platform;
import io.github.prison.Prison;
import io.github.prison.Scheduler;
import io.github.prison.commands.PluginCommand;
import io.github.prison.internal.Player;
import io.github.prison.internal.World;

/**
 * @author SirFaizdat
 */
@SuppressWarnings("OptionalGetWithoutIsPresent")
class SpongePlatform implements Platform {

    private SpongePrison plugin;
    private List<PluginCommand> commands = new ArrayList<>();

    SpongePlatform(SpongePrison plugin) {
        this.plugin = plugin;
    }

    @Override
    public World getWorld(String name) {
        return new SpongeWorld(Sponge.getServer().getWorld(name).get());
    }

    @Override
    public Player getPlayer(String name) {
        return new SpongePlayer(Sponge.getServer().getPlayer(name).get());
    }

    @Override
    public Player getPlayer(UUID uuid) {
        return new SpongePlayer(Sponge.getServer().getPlayer(uuid).get());
    }

    @Override
    public List<Player> getOnlinePlayers() {
        List<Player> players = Sponge.getServer().getOnlinePlayers().stream().map(player -> getPlayer(player.getUniqueId())).collect(Collectors.toList());
        return players;
    }

    @Override
    public String getPluginVersion() {
        return Sponge.getPluginManager().getPlugin("prison-sponge").get().getVersion().get();
    }

    @Override
    public File getPluginDirectory() {
        return Sponge.getPluginManager().getPlugin("prison-sponge").get().getAssetDirectory().get().toFile();
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public void registerCommand(PluginCommand command) {
        CommandManager commandManager = Sponge.getCommandManager();
        commandManager.register(plugin, new CommandCallable() {
            @Override
            public CommandResult process(CommandSource commandSource, String s) throws CommandException {
                String[] args = s.split(" ");

                if (commandSource instanceof org.spongepowered.api.entity.living.player.Player) // It's a player
                    Prison.getInstance().getCommandHandler().onCommand(new SpongePlayer((org.spongepowered.api.entity.living.player.Player) commandSource), command, command.getLabel(), args);
                else // It's not a player, just another command sender... boring!
                    Prison.getInstance().getCommandHandler().onCommand(new SpongeCommandSender(commandSource), command, command.getLabel(), args);

                return CommandResult.success();
            }

            @Override
            public List<String> getSuggestions(CommandSource commandSource, String s) throws CommandException {
                return Collections.emptyList();
            }

            @Override
            public boolean testPermission(CommandSource commandSource) {
                return true; // This is checked later
            }

            @Override
            public Optional<? extends Text> getShortDescription(CommandSource commandSource) {
                return Optional.of(Text.of(command.getDescription()));
            }

            @Override
            public Optional<? extends Text> getHelp(CommandSource commandSource) {
                return Optional.of(Text.of(command.getDescription()));
            }

            @Override
            public Text getUsage(CommandSource commandSource) {
                return Text.of(command.getUsage());
            }
        }, command.getLabel());
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
        return new SpongeGUI(title, numRows);
    }

    @Override
    public void log(String message, Object... format) {
        Text msg = TextSerializers.FORMATTING_CODE.deserialize(String.format("&8[&3Prison&8]&r " + message, format));
        Sponge.getServer().getConsole().sendMessage(msg);
    }
}
