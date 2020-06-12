/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017-2020 The Prison Team
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

package tech.mcprison.prison.commands;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandUtil {

    private static Pattern verifyArgumentsPattern = Pattern.compile("^(.*?)\\[(.*?)\\]$");

    public static String escapeArgumentVariable(String var) {
        if (var == null) {
            return null;
        }

        if (var.matches("^\\\\*\\?.*$")) {
            return "\\" + var;
        }

        return var;
    }

    public static Map<String, String[]> parseVerifiers(String verifiers) {
        Map<String, String[]> map = new LinkedHashMap<String, String[]>();

        if (verifiers.equals("")) {
            return map;
        }

        String[] arguments = verifiers.split("\\|");

        for (String arg : arguments) {
            Matcher matcher = verifyArgumentsPattern.matcher(arg);
            if (!matcher.matches()) {
                throw new IllegalArgumentException(
                    "The argrument \"" + arg + "\" is in invalid form.");
            }

            List<String> parameters = new ArrayList<String>();

            String sparameters = matcher.group(2);
            if (sparameters != null) {
                for (String parameter : sparameters.split(",")) {
                    parameters.add(parameter.trim());
                }
            }

            String argName = matcher.group(1).trim();

            map.put(argName, parameters.toArray(new String[0]));
        }

        return map;
    }
}
