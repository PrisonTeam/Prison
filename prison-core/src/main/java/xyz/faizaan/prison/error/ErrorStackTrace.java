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

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Represents a single stack trace and a description.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class ErrorStackTrace {

    private Throwable throwable;
    private String description;

    public ErrorStackTrace(Throwable throwable, String description) {
        this.throwable = throwable;
        this.description = description;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override public String toString() {
        String exceptionText = ExceptionUtils.getStackTrace(throwable);
        return ("Description: " + description + "\n") + "Stack trace:\n" + exceptionText;
    }
}
