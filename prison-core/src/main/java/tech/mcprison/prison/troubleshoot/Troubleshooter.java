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

import tech.mcprison.prison.internal.CommandSender;

/**
 * A Troubleshooter performs a maintenance routine for a common problem that a user may face.
 * Before registering a Troubleshooter, ask yourself if the problem can be avoided in your
 * design first. Troubleshooters add an extra task for the user which may hinder the user experience.
 * <p>
 * Troubleshooters are expected to safely perform fixes during runtime. A good example of where a
 * Troubleshooter can be applied is in the old ranking up bug in Prison 2, in which rank IDs would
 * corrupt and cause the same rank to be purchased repeatedly. A Troubleshooter could have set the
 * rank IDs manually at runtime.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public abstract class Troubleshooter {

    private String name, description;

    public Troubleshooter(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract TroubleshootResult invoke(CommandSender invoker);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
