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

package tech.mcprison.prison.alerts;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a single alert.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class Alert {

    public int id;
    public String message;
    public List<UUID> readBy;

    public Alert() {
    }

    public Alert(int id, String message) {
        this.id = id;
        this.message = message;
        this.readBy = new ArrayList<>();
    }

}
