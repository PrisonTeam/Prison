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

package io.github.prison.sponge;

import com.google.inject.Inject;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.util.logging.Logger;

import io.github.prison.Prison;

/**
 * @author Camouflage100
 */
@Plugin(id = "prison-sponge")
public class SpongePrison {

    @Inject
    Logger logger;
    SpongeScheduler scheduler;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        this.scheduler = new SpongeScheduler(this);
        Prison.getInstance().init(new SpongePlatform(this));
        new SpongeListener(this).init();
    }

    @Listener
    public void onServerStop(GameStoppingServerEvent event) {
        this.scheduler.cancelAll();
        Prison.getInstance().deinit();
    }

}
