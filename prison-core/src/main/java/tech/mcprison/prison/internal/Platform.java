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

package tech.mcprison.prison.internal;

import tech.mcprison.prison.commands.PluginCommand;
import tech.mcprison.prison.economy.Economy;
import tech.mcprison.prison.gui.GUI;
import tech.mcprison.prison.modules.Capability;
import tech.mcprison.prison.util.Location;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a platform that Prison has been implemented for.
 *
 * @author SirFaizdat
 * @author Camouflage100
 * @since 3.0
 */
public interface Platform {

    /**
     * Returns the world with the specified name.
     */
    World getWorld(String name);

    /**
     * Returns the player with the specified name.
     */
    Player getPlayer(String name);

    /**
     * Returns the player with the specified UUID.
     */
    Player getPlayer(UUID uuid);

    /**
     * Returns a list of all online players.
     */
    List<Player> getOnlinePlayers();

    /**
     * Returns a sign in the world.
     *
     * @param location The {@link Location} of the sign.
     * @return the {@link Sign}.
     */
    Sign getSign(Location location);

    /**
     * Returns the server's economy.
     * If there is no economy, then {@link Capability#ECONOMY} will be false for the platform.
     *
     * @return the {@link Economy} instance if the server supports it, or null if there is no supported server economy plugin.
     */
    Economy getEconomy();

    /**
     * Returns the plugin's version.
     */
    String getPluginVersion();

    /**
     * Returns the {@link File} representing the plugin's designated storage folder.
     * This directory must have already been created by the implementation.
     */
    File getPluginDirectory();

    /**
     * Registers a command with the server implementation.
     *
     * @param command The {@link PluginCommand} to register.
     */
    void registerCommand(PluginCommand command);

    /**
     * Unregisters a registered command.
     * This does not support command aliases, because those are currently not definable anyway.
     *
     * @param command The command to unregister, without the preceding '/'.
     */
    void unregisterCommand(String command);

    /**
     * Returns a list of all registered commands.
     */
    List<PluginCommand> getCommands();

    /**
     * Returns the {@link Scheduler}, which can be used to schedule tasks.
     */
    Scheduler getScheduler();

    /**
     * Creates a new {@link GUI} to show to players.
     *
     * @param title   The title of the GUI.
     * @param numRows The number of rows in the GUI; must be divisible by 9.
     * @return The {@link GUI}, ready for use.
     */
    GUI createGUI(String title, int numRows);

    /**
     * If an iron door is open, this method closes it.
     * If an iron door is closed, this method opens it.
     *
     * @param doorLocation The {@link Location} of the door.
     */
    void toggleDoor(Location doorLocation);

    /**
     * Log a colored message to the console (if supported).
     *
     * @param message The message. May include color codes, amp-prefixed.
     * @param format  The objects inserted via {@link String#format(String, Object...)}.
     */
    void log(String message, Object... format);

    /**
     * Returns a map of capabilities and whether or not this platform has them.
     */
    Map<Capability, Boolean> getCapabilities();

    /**
     * Send a title to a player
     *
     * @param player   The player that you want to send the title to
     * @param title    The text of the title
     * @param subtitle The text of the subtitle
     * @param fade     The length of the fade
     */
    void showTitle(Player player, String title, String subtitle, int fade);

    /**
     * Send an actionbar to a player
     *
     * @param player The player that you want to send the actionbar to
     * @param text   The text of the actionbar
     */
    void showActionBar(Player player, String text);

}
