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

import io.github.prison.internal.Player;
import io.github.prison.internal.World;
import io.github.prison.util.Block;
import io.github.prison.util.Location;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.trait.BlockTrait;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author SirFaizdat
 */
public class SpongeWorld implements World {

    private org.spongepowered.api.world.World spongeWorld;

    public SpongeWorld(org.spongepowered.api.world.World spongeWorld) {
        this.spongeWorld = spongeWorld;
    }

    @Override
    public String getName() {
        return spongeWorld.getName();
    }

    @Override
    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();
        for(org.spongepowered.api.entity.living.player.Player player : Sponge.getServer().getOnlinePlayers()) {
            players.add(new SpongePlayer(player));
        }
        return players;
    }

    @Override
    public Block getBlockAt(Location location) {
        return Block.getBlock(spongeWorld.getBlock(location.getBlockX(), location.getBlockY(), location.getBlockZ()).getId());
    }

    @Override
    public void setBlockAt(Location location, Block block) {
        Optional<BlockType> optBlock = Sponge.getRegistry().getType(BlockType.class, block.getId());
        if (optBlock.isPresent()) {
            BlockState defaultState = optBlock.get().getDefaultState();
            Optional<BlockTrait<?>> optTrait = defaultState.getTrait("variant");
            if (optTrait.isPresent()) {
                // TODO This probably won't work, I need a data byte to variant translator
                Optional<BlockState> optState = defaultState.withTrait(optTrait.get(), block.getData());
                if (optState.isPresent()) {
                    BlockState blockState = optState.get();
                    spongeWorld.setBlock(location.getBlockX(), location.getBlockY(), location.getBlockZ(), blockState);
                }
            }
        }
    }
}
