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

package tech.mcprison.prison.output;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.platform.CommandSender;

import java.util.Arrays;

/**
 * Standardized output to the console and to players.
 *
 * @author Faizaan A. Datoo
 * @since 3.0
 */
public class Output {

    // Fields
    private static Output instance;

    // Constructor

    private Output() {
        instance = this;
    }

    // Public methods

    /**
     * Log an informational message to the console.
     *
     * @param message The informational message. May include color codes, but the default is white.
     */
    public void logInfo(String message, Object... args) {
        Prison.getInstance().getPlatform().log("&3Prison &7/// &f" + String.format(message, args));
    }

    /**
     * Log a warning to the console.
     *
     * @param message   The message describing the warning. May include color codes, but the default is orange.
     * @param throwable The exceptions thrown, if any.
     */
    public void logWarn(String message, Throwable... throwable) {
        Prison.getInstance().getPlatform().log("&3Prison &7/// &6" + message);

        if (throwable.length > 0) {
            Arrays.stream(throwable).forEach(Throwable::printStackTrace);
        }
    }

    /**
     * Log an error to the console.
     *
     * @param message   The message describing the error. May include color codes, but the default is red.
     * @param throwable The exceptions thrown, if any.
     */
    public void logError(String message, Throwable... throwable) {
        Prison.getInstance().getPlatform().log("&3Prison &7/// &c" + message);

        if (throwable.length > 0) {
            Arrays.stream(throwable).forEach(Throwable::printStackTrace);
        }
    }

    /**
     * Send information to a {@link CommandSender}.
     *
     * @param sender  The {@link CommandSender} receiving the message.
     * @param message The message to send. This may include color codes, but the default is white.
     */
    public void sendInfo(CommandSender sender, String message, Object... args) {
        sender.sendMessage("&3Prison &7/// &f" + String.format(message, args));
    }

    /**
     * Send a warning to a {@link CommandSender}.
     *
     * @param sender  The {@link CommandSender} receiving the message.
     * @param message The message to send. This may include color codes, but the default is orange.
     */
    public void sendWarn(CommandSender sender, String message, Object... args) {
        sender.sendMessage("&3Prison &7/// &6" + String.format(message, args));
    }

    /**
     * Send an error to a {@link CommandSender}.
     *
     * @param sender  The {@link CommandSender} receiving the message.
     * @param message The message to send. This may include color codes, but the default is red.
     */
    public void sendError(CommandSender sender, String message, Object... args) {
        sender.sendMessage("&3Prison &7/// &c" + String.format(message, args));
    }

    // Getters

    public static Output get() {
        if (instance == null) {
            new Output();
        }
        return instance;
    }

}
