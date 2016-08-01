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

package tech.mcprison.prison.ranks;

import tech.mcprison.prison.internal.config.Configurable;

/**
 * Represents Ranks' messages file. Contains all of the keys and their default values. Every time a
 * new value is added to a production version (i.e. public release), the VERSION constant must be
 * incremented by one to ensure the file will be regenerated.
 *
 * @author SirFaizdat
 * @author Camouoflage100
 */
public class Messages implements Configurable {

    public static final int VERSION = 19;
    public static String commandListHeadFood = "&7============= &d/ranks list &7=============";
    public static String commandListAdmin =
        "&7Name: &d%s &7- Id: &d%s &7- Ladder: &d%s &7- Tag: &d%s &7- Cost: &d%s";
    public static String commandListUser = "&7Name: &d%s &7- Cost: &d%s";
    public static String commandCheckRank = "&d%s's&7 current rank is '&d%s&7'!";
    public static String commandSetRank = "&7Successfully set &d%s's &7rank from &d%s &7to &d%s&7!";
    public static String commandPromote = "&7Successfully promoted &d%s&7 from &d%s&7 to &d%s&7!";
    public static String commandDemote = "&7Successfully demoted &d%s&7 from &d%s&7 to &d%s&7!";
    public static String commandSetTag = "&7Successfully set the tag of &d%s&7 to &d%s&7!";
    public static String commandCreateRank = "&7Successfully create rank &d%s&7!";
    public static String commandSetRankCost = "&7Successfully set rank &d%s's&7 cost to &d%s&7!";
    public static String commandRankup = "&7You've ranked up to &d%s&7!";
    private static String error = "&cError &8&l| &7";
    // Entries
    public static String errorPlayerNotFound = error + "Player &d%s&7 not found!";
    public static String errorPlayerTopRank = error + "Player &d%s&7 is already the top rank!";
    public static String errorPlayerBottomRank =
        error + "Player &d%s&7 is already the bottom rank!";
    public static String errorInvalidRank = error + "&d%s&7 is not a valid rank!";
    public static String errorNoRanksLoaded = error + "No currently loaded ranks!";
    public static String errorRankExists = error + "&d%s&7 already exists!";
    public static String errorNotEnoughMoney =
        error + "&d%s&7 costs &d%s&7, but you only have &d%s&7!";
    public static String errorTopRank =
        error + "You cannot rank up anymore, you are already the top rank :D";
    public int version = VERSION;

    @Override public int getVersion() {
        return version;
    }
}
