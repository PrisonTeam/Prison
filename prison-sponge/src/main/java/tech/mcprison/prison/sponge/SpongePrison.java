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

package tech.mcprison.prison.sponge;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import tech.mcprison.prison.Prison;

import java.nio.file.Path;

/**
 * @author Faizaan A. Datoo
 */
@Plugin(id = "prison-sponge", name = "Prison", description = "An all-in-one solution for a Prison server.", version = "3.1.1.0", url = "http://mc-prison.tech")
public class SpongePrison {

    @Inject private Logger logger;

    @Inject @ConfigDir(sharedRoot = false) private Path configDir;

    @SuppressWarnings( "unused" )
	@Inject private Metrics metrics;

    @Listener public void onServerStart(GameStartedServerEvent e) {
        if (!configDir.toFile().exists()) {
            configDir.toFile().mkdirs();
        }
        Prison.get().init(new SpongePlatform(this), Sponge.getPlatform().getMinecraftVersion().getName());
    }

    public Logger getLogger() {
        return logger;
    }

    public Path getConfigDir() {
        return configDir;
    }

}
