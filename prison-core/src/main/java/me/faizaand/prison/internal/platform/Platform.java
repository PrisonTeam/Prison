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

package me.faizaand.prison.internal.platform;

import me.faizaand.prison.internal.Scheduler;
import me.faizaand.prison.internal.scoreboard.ScoreboardManager;
import me.faizaand.prison.store.Storage;

import java.io.File;
import java.util.Map;

/**
 * Represents an internal platform that Prison has been implemented for.
 * The internal platform is responsible for connecting Prison's APIs to the underlying server API.
 *
 * @author Faizaan A. Datoo
 * @author Camouflage100
 * @since API 1.0
 */
public interface Platform {

    PlayerManager getPlayerManager();

    CommandManager getCommandManager();

    WorldManager getWorldManager();

    GuiManager getGuiManager();

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
     * Runs a command as the console (i.e. with all privileges).
     *
     * @param cmd The command to run, without the '/'.
     */
    void dispatchCommand(String cmd);

    /**
     * Returns the {@link Scheduler}, which can be used to schedule tasks.
     */
    Scheduler getScheduler();

    /**
     * Log a colored message to the console (if supported).
     *
     * @param message The message. May include color codes, amp-prefixed.
     * @param format  The objects inserted via {@link String#format(String, Object...)}.
     */
    void log(String message, Object... format);

    /**
     * Logs a debug message to the console if the user has debug messages enabled.
     *
     * @param message The message. May include color codes, amp-prefixed.
     * @param format  The The objects inserted via {@link String#format(String, Object...)}.
     */
    void debug(String message, Object... format);

    /**
     * Runs the converter for this platform.
     *
     * @return The output of the converter. It will be sent to whoever ran the converter system (e.g. usually a command sender).
     */
    default String runConverter() {
        return "This operation is unsupported on this platform.";
    }

    /**
     * Returns a map of capabilities and whether or not this internal has them.
     */
    Map<Capability, Boolean> getCapabilities();

    /**
     * Returns the scoreboard manager.
     */
    ScoreboardManager getScoreboardManager();

    /**
     * Returns the storage manager.
     */
    Storage getStorage();

    /**
     * Returns true if the server should show alerts to in-game players, false otherwise.
     * This is a configuration option.kkjksdf;erljnkx.jcsmka.f.fdlwe;s.x. frrer5
     */
    boolean shouldShowAlerts();

}
