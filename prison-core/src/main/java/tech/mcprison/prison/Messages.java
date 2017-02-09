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

package tech.mcprison.prison;

import tech.mcprison.prison.config.MessageConfigurable;

/**
 * Represents Prison's messages file. Contains all of the keys and their default values.
 * Every time a new value is added to a production version (i.e. public release),
 * the VERSION constant must be incremented by one to ensure the file will be regenerated.
 *
 * @author Faizaan A. Datoo
 * @since API 0.1
 */
public class Messages extends MessageConfigurable {

    public static final int VERSION = 6; // For everyone to reference
    public int version = VERSION; // For the configuration file to store

    // Entries

    public String includeError = ERROR_PREFIX + "[%p] has an invalid value.";
    public String excludeError = ERROR_PREFIX + "[%p] has an invalid value.";
    public String cantAsConsole = ERROR_PREFIX + "You can't do this as console.";
    public String missingArgument =
            ERROR_PREFIX + "The argument [%s] is not defined (it has no default value).";
    public String missingFlagArgument =
            ERROR_PREFIX + "The flag -%s does not have the required parameters.";
    public String undefinedFlagArgument =
            ERROR_PREFIX + "The argument [%s] to the flag -%s is not defined";
    public String internalErrorOccurred =
            ERROR_PREFIX + "An internal error occurred while attempting to perform this command.";
    public String noPermission =
            ERROR_PREFIX + "You lack the necessary permissions to perform this command.";
    public String blockParseError = ERROR_PREFIX + "The parameter [%p] is not a valid block.";
    public String numberParseError = ERROR_PREFIX + "The parameter [%p] is not a number.";
    public String numberTooLow =
            ERROR_PREFIX + "The parameter [%p] must be equal or greater than %1";
    public String numberTooHigh = ERROR_PREFIX + "The parameter [%p] must be equal or less than %1";
    public String numberRangeError = ERROR_PREFIX
            + "The parameter [%p] must be equal or greater than %1 and less than or equal to %2";
    public String tooFewCharacters =
            ERROR_PREFIX + "The parameter [%p] must be less than %1 characters.";
    public String tooManyCharacters =
            ERROR_PREFIX + "The parameter [%p] must be less than %1 characters.";
    public String playerNotOnline = ERROR_PREFIX + "The player %1 is not online.";
    public String worldNotFound = ERROR_PREFIX + "The world \"%1\" was not found";
    public String selectionNeeded = ERROR_PREFIX + "You must create a selection first!";
    public String alertsPresent =
            INFO_PREFIX + "You have &c&l%d alerts &r&c. Type /prison alerts to view them.";
    public String alertsCleared = INFO_PREFIX + "You have cleared your alerts.";

    @Override
    public int getVersion() {
        return version;
    }
}
