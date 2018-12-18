/*
 * Prison is a Minecraft plugin for the prison game mode.
 * Copyright (C) 2018 The Prison Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.output;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.CommandSender;

import java.util.Arrays;

/**
 * Standardized output to the console and to players.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class Output {

    // Fields
    private static Output instance;
    public String PREFIX_TEMPLATE = "&8| %s &8|";
    public String INFO_PREFIX = gen("&3Info") + " &7";
    public String WARNING_PREFIX = gen("&6Warning") + " &7";
    public String ERROR_PREFIX = gen("&cError") + " &7";

    // Constructor

    private Output() {
        instance = this;
    }

    // Public methods

    public static Output get() {
        if (instance == null) {
            new Output();
        }
        return instance;
    }

    public String format(String message, LogLevel level, Object... args) {
        String prefix = level == LogLevel.INFO ?
            INFO_PREFIX :
            level == LogLevel.WARNING ? WARNING_PREFIX : ERROR_PREFIX;
        return prefix + String.format(message, args);
    }

    /**
     * Log a message with a specified {@link LogLevel}
     */
    public void log(String message, LogLevel level, Object... args) {
        Prison.get().getPlatform().log(gen("&3Prison") + " " + (level == LogLevel.INFO ?
            "&f" :
            level == LogLevel.WARNING ? "&6" : "&c") + String.format(message, args));
    }

    /**
     * Log an informational message to the console.
     *
     * @param message The informational message. May include color codes, but the default is white.
     */
    public void logInfo(String message, Object... args) {
        log(message, LogLevel.INFO, args);
    }

    /**
     * Log a warning to the console.
     *
     * @param message   The message describing the warning. May include color codes, but the default is
     *                  orange.
     * @param throwable The exceptions thrown, if any.
     */
    public void logWarn(String message, Throwable... throwable) {
        log(message, LogLevel.WARNING);

        if (throwable.length > 0) {
            Arrays.stream(throwable).forEach(Throwable::printStackTrace);
        }
    }

    /**
     * Log an error to the console.
     *
     * @param message   The message describing the error. May include color codes, but the default is
     *                  red.
     * @param throwable The exceptions thrown, if any.
     */
    public void logError(String message, Throwable... throwable) {
        log(message, LogLevel.ERROR);

        if (throwable.length > 0) {
            Arrays.stream(throwable).forEach(Throwable::printStackTrace);
        }
    }

    /**
     * Send a message to a {@link CommandSender}
     */
    public void sendMessage(CommandSender sender, String message, LogLevel level, Object... args) {
        String prefix = level == LogLevel.INFO ?
            INFO_PREFIX :
            level == LogLevel.WARNING ? WARNING_PREFIX : ERROR_PREFIX;
        sender.sendMessage(prefix + String.format(message, args));
    }

    /**
     * Send information to a {@link CommandSender}.
     *
     * @param sender  The {@link CommandSender} receiving the message.
     * @param message The message to send. This may include color codes, but the default is grey.
     */
    public void sendInfo(CommandSender sender, String message, Object... args) {
        sender.sendMessage(INFO_PREFIX + String.format(message, args));
    }

    /**
     * Send a warning to a {@link CommandSender}.
     *
     * @param sender  The {@link CommandSender} receiving the message.
     * @param message The message to send. This may include color codes, but the default is grey.
     */
    public void sendWarn(CommandSender sender, String message, Object... args) {
        sender.sendMessage(WARNING_PREFIX + String.format(message, args));
    }

    /**
     * Send an error to a {@link CommandSender}.
     *
     * @param sender  The {@link CommandSender} receiving the message.
     * @param message The message to send. This may include color codes, but the default is grey.
     */
    public void sendError(CommandSender sender, String message, Object... args) {
        sender.sendMessage(ERROR_PREFIX + String.format(message, args));
    }

    // Private methods

    public String gen(String name) {
        return String.format(PREFIX_TEMPLATE, name);
    }

}
