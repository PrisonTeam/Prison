/*
 * Prison is a Minecraft plugin for the prison game mode.
 * Copyright (C) 2017 The Prison Team
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

package tech.mcprison.prison.mines.data;


import java.util.ArrayList;
import java.util.Arrays;

import tech.mcprison.prison.file.FileIOData;

/**
 * Represents the Mines configuration file.
 */
public class MinesConfig implements FileIOData {

//    /**
//     * True if randomized blocks for mines should be cached for faster resets. False otherwise
//     */
//    public boolean asyncReset = true;

    /**
     * True if reset warnings an reset broadcasts should be enabled. False otherwise
     */
    public boolean resetMessages = true;

//    /**
//     * True if broadcasts should only be enabled in the worlds specified in the <i>worlds</i> list.
//     * False otherwise.
//     *
//     * @see MinesConfig#worlds
//     */
//    public boolean multiworld = false;

    /**
     * True if only blocks that are air should be replaced. False otherwise
     */
    public boolean fillMode = false;

    /**
     * The duration between mine resets in seconds.
     */
    public int resetTime = MineData.MINE_RESET__TIME_SEC__DEFAULT;

//    /**
//     * The worlds that reset messages should be broadcasted to. Ignored if multiworld is disabled.
//     *
//     * @see MinesConfig#multiworld
//     */
//    public ArrayList<String> worlds =
//        new ArrayList<>(Arrays.<String>asList(new String[]{"plots", "mines"}));

    /**
     * The time between mine reset warnings. Ignored if resetMessages is disabled.
     *
     * @see MinesConfig#resetMessages
     */
    public ArrayList<Integer> resetWarningTimes =
        new ArrayList<>(Arrays.<Integer>asList(new Integer[]{600, 300, 60}));

}
