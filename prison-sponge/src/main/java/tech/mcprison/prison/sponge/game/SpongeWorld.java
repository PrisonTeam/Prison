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

package tech.mcprison.prison.sponge.game;


import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.util.Location;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Faizaan A. Datoo
 */
public class SpongeWorld implements World {

    private org.spongepowered.api.world.World spongeWorld;

    public SpongeWorld(org.spongepowered.api.world.World spongeWorld) {
        this.spongeWorld = spongeWorld;
    }

    @Override public String getName() {
        return spongeWorld.getName();
    }

    @Override public List<Player> getPlayers() {
        return spongeWorld.getPlayers().stream().map(SpongePlayer::new)
            .collect(Collectors.toList());
    }

    @Override public Block getBlockAt(Location location) {
        return null;
    }

    public org.spongepowered.api.world.World getSpongeWorld() {
        return spongeWorld;
    }

}
