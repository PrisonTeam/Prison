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

package io.github.prison.util;

/**
 * Utilities for manipulating strings.
 *
 * @author SirFaizdat
 * @since 3.0
 */
public class TextUtil {

    private TextUtil() {}

    /**
     * @deprecated Implementations handle chat coloring.
     */
    @Deprecated
    public static String parse(String message, Object... format) {
        return ChatColor.translateAlternateColorCodes('&', String.format(message, format));
    }

    /**
     * If a String does not end with a period/dot ("."), it will be appended.
     *
     * @param msg   The message to append a dot to.
     * @param color The color code of the dot at the end.
     * @return The edited string.
     */
    public static String dotIfNotPresent(String msg, String color) {
        return color + (msg.endsWith(".") ? "" : ".");
    }

    /**
     * Removes all spaces from a string, and splits it at a delimiter.
     *
     * @param message   The message to pack and split.
     * @param delimiter The String to split at.
     * @return The String array.
     */
    public static String[] packAndSplit(String message, String delimiter) {
        return message.replaceAll(" ", "").split(delimiter);
    }

}
