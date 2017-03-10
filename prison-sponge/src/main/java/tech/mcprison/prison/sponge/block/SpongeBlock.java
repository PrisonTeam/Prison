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

package tech.mcprison.prison.sponge.block;

import org.spongepowered.api.world.World;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.block.BlockFace;
import tech.mcprison.prison.internal.block.BlockState;
import tech.mcprison.prison.sponge.SpongeUtil;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.Location;

import java.util.Arrays;
import java.util.List;

/**
 * @author Faizaan A. Datoo
 */
public class SpongeBlock implements Block {

    org.spongepowered.api.world.Location<World> blockLoc;

    public SpongeBlock(org.spongepowered.api.world.Location<World> blockLoc) {
        this.blockLoc = blockLoc;
    }

    @Override public Location getLocation() {
        return SpongeUtil.spongeLocationToPrison(blockLoc);
    }

    @Override public Block getRelative(BlockFace face) {
        return new SpongeBlock(blockLoc.getRelative(SpongeUtil.prisonBlockFaceToSponge(face)));
    }

    @Override public BlockType getType() {
        return Arrays.stream(BlockType.values())
            .filter(blockType -> blockType.getId().equals(blockLoc.getBlock().getType().getId()))
            .findFirst().orElse(null);
    }

    @Override public void setType(BlockType type) {
    }

    @Override public BlockState getState() {
        return null;
    }

    @Override public boolean breakNaturally() {
        return false;
    }

    @Override public List<ItemStack> getDrops() {
        return null;
    }

    @Override public List<ItemStack> getDrops(ItemStack tool) {
        return null;
    }

}
