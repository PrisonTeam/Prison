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

package xyz.faizaan.prison.troubleshoot;

import xyz.faizaan.prison.internal.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The TroubleshootManager is responsible for containing instances of all registered {@link Troubleshooter}s.
 * You should register your {@link Troubleshooter} here.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class TroubleshootManager {

    private List<Troubleshooter> troubleshooters;

    public TroubleshootManager() {
        troubleshooters = new ArrayList<>();
    }

    /**
     * Register a {@link Troubleshooter} with the manager.
     *
     * @param troubleshooter The instantiated {@link Troubleshooter} to register.
     * @throws IllegalArgumentException If a {@link Troubleshooter} with the same name is already registered.
     */
    public void registerTroubleshooter(Troubleshooter troubleshooter) {
        if (getTroubleshooter(troubleshooter.getName()).isPresent()) {
            throw new IllegalArgumentException(
                "troubleshooter with name " + troubleshooter.getName() + " already registered");
        }
        troubleshooters.add(troubleshooter);
    }

    /**
     * Invoke a specific {@link Troubleshooter}.
     *
     * @param name   The name of the {@link Troubleshooter}. This is not case sensitive, but should contain underscores.
     * @param sender The {@link CommandSender} invoking this troubleshooter. Messages will be sent to this sender.
     * @return The {@link TroubleshootResult} if the {@link Troubleshooter} could be run, or null if there is no {@link Troubleshooter} by the provided name.
     */
    public TroubleshootResult invokeTroubleshooter(String name, CommandSender sender) {
        Optional<Troubleshooter> troubleshooter = getTroubleshooter(name);
        return troubleshooter.map(troubleshooter1 -> troubleshooter1.invoke(sender)).orElse(null);

    }

    public Optional<Troubleshooter> getTroubleshooter(String name) {
        return troubleshooters.stream()
            .filter(troubleshooter -> troubleshooter.getName().equalsIgnoreCase(name)).findFirst();
    }

    public List<Troubleshooter> getTroubleshooters() {
        return troubleshooters;
    }

}
