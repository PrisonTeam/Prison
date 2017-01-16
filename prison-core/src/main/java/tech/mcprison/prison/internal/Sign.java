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

package tech.mcprison.prison.internal;

import tech.mcprison.prison.util.Location;

import java.util.List;

/**
 * Represents a sign in a Minecraft world.
 *
 * @author Faizaan A. Datoo
 */
public interface Sign {

    /**
     * Returns the location of this sign.
     *
     * @return the {@link Location}.
     */
    Location getLocation();

    /**
     * Returns the lines of text on this sign.
     * Each entry in the array is its own line.
     *
     * @return the String array.
     */
    String[] getLines();

    /**
     * Sets the lines of the sign, overwriting everything else on the sign.
     * Ampersand-prefixed color codes are automatically parsed.
     *
     * @param lines the new {@link List}.
     */
    void setLines(List<String> lines);

    /**
     * Sets a certain line of the sign to another text value.
     *
     * @param line  The line number.
     * @param value The value of this line.
     *              Ampersand-prefixed color codes are automatically parsed.
     */
    void setLine(int line, String value);

}
