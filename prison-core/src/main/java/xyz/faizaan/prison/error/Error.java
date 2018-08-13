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

package xyz.faizaan.prison.error;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single error, and all the data attached.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class Error {

    private String description;
    private List<ErrorStackTrace> stackTraces;

    public Error(String description) {
        this.description = description;
        this.stackTraces = new ArrayList<>();
    }

    public Error appendStackTrace(String desc, Throwable t) {
        this.stackTraces.add(new ErrorStackTrace(t, desc));
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ErrorStackTrace> getStackTraces() {
        return stackTraces;
    }
}
