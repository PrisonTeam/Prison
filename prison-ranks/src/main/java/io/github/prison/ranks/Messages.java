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

package io.github.prison.ranks;

import io.github.prison.Configurable;

/**
 * Represents Ranks' messages file. Contains all of the keys and their default values.
 * Every time a new value is added to a production version (i.e. public release),
 * the VERSION constant must be incremented by one to ensure the file will be regenerated.
 *
 * @author SirFaizdat
 * @author Camouoflage100
 */
public class Messages implements Configurable {

    public static final int VERSION = 10; // For everyone to reference
    public int version = VERSION; // For the configuration file to store

    // Entries
    public static String errorPlayerNotFound = "&cError &8&l| &7Player &d%s&7 not found!";
    public static String errorPlayerTopRank = "&cError &8&l| &7Player &d%s&7 is already the top rank!";
    public static String errorInvalidRank = "&cError &8&l| &d%s&7 is not a valid rank!";
    public static String errorNoRanksLoaded = "&cError &8&l| &7No currently loaded ranks!";
    public static String commandReload = "&7Reloaded all ranks!";
    public static String commandListHeadFood = "&7============= &d/ranks list &7=============";
    public static String commandList = "&7Name: &d%s &7- Id: &d%s &7- Ladder: &d%s &7- Tag: &d%s &7- Cost: &d%s";
    public static String commandCheckRank = "&d%s's&7 current rank is '&d%s&7'!";
    public static String commandSetRank = "&7Successfully set &d%s's &7rank from &d%s &7to &d%s&7!";
    public static String commandPromote = "&7Successfully promoted &d%s&7 from &d%s&7 to &d%s&7!";


    @Override
    public int getVersion() {
        return version;
    }
}
