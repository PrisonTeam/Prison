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

package io.github.prison.mines;

import io.github.prison.internal.config.Configurable;

/**
 * @author SirFaizdat
 */
public class Messages implements Configurable {

    // See the Config class for information on what these are
    public static final int VERSION = 0;
    public int version = VERSION;

    public String mineCreated = io.github.prison.Messages.INFO_PREFIX + "Successfully created new mine &3%s&7.";

    @Override
    public int getVersion() {
        return version;
    }

}
