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

package tech.mcprison.prison.troubleshoot;

/**
 * A TroubleshootResult is given to a user when the {@link Troubleshooter} has completed execution.
 * This could be due to success, requiring further action, or complete failure.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class TroubleshootResult {

    private Result result;
    private String description;

    public TroubleshootResult(Result result, String description) {
        this.result = result;
        this.description = description;
    }

    public Result getResult() {
        return result;
    }

    public String getDescription() {
        return description;
    }

    public enum Result {
        SUCCESS, USER_ACTION, FAILURE
    }


}
