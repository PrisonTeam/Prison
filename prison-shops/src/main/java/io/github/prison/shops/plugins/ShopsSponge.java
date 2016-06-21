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

package io.github.prison.shops.plugins;

import io.github.prison.shops.ShopsModule;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

/**
 * @author SirFaizdat
 */
@Plugin(id = "prison-shops", name = "PrisonShops", version = "3.0.0-SNAPSHOT", description = "The shops module for Prison.", dependencies = {@Dependency(id = "prison-sponge")})
public class ShopsSponge {

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        new ShopsModule(getVersion());
    }

    private String getVersion() {
        return Sponge.getPluginManager().getPlugin("prison-shops").get().getVersion().get();
    }

}
