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

package tech.mcprison.prison.sponge;

import com.google.inject.Inject;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.util.logging.Logger;

/**
 * @author Faizaan A. Datoo
 */
@Plugin(id = "prison-sponge", name = "Prison", description = "An all-in-one solution for a Prison server.", url = "http://mc-prison.tech")
public class SpongePrison {

    @Inject
    private Logger logger;

    @Listener
    public void onServerStart(GameStartedServerEvent e) {

    }

    public Logger getLogger() {
        return logger;
    }

}
