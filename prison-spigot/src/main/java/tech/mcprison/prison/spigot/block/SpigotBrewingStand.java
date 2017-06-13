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

package tech.mcprison.prison.spigot.block;

import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.block.BrewingStand;
import tech.mcprison.prison.internal.inventory.BrewerInventory;
import tech.mcprison.prison.spigot.inventory.SpigotBrewer;

/**
 * Created by DMP9 on 04/02/2017.
 */
public class SpigotBrewingStand implements BrewingStand {

    private org.bukkit.block.BrewingStand wrapper;

    public SpigotBrewingStand(org.bukkit.block.BrewingStand wrapper) {
        this.wrapper = wrapper;
    }

    @Override public int getBrewingTime() {
        return wrapper.getBrewingTime();
    }

    @Override public void setBrewingTime(int brewTime) {
        wrapper.setBrewingTime(brewTime);
    }

    @Override public BrewerInventory getInventory() {
        return new SpigotBrewer(wrapper.getInventory());
    }

    @Override public Block getBlock() {
        return new SpigotBlock(wrapper.getBlock());
    }
}
