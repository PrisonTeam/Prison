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

package tech.mcprison.prison.spigot.inventory;

import org.bukkit.Material;

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.inventory.FurnaceRecipe;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.util.BlockType;

/**
 * Created by DMP9 on 04/02/2017.
 */
public class SpigotFurnaceRecipe extends SpigotRecipe implements FurnaceRecipe {

    public SpigotFurnaceRecipe(org.bukkit.inventory.FurnaceRecipe wrapper) {
        super(wrapper);
    }

    @Override public ItemStack getInput() {
        return SpigotUtil.bukkitItemStackToPrison(
            ((org.bukkit.inventory.FurnaceRecipe) getWrapper()).getInput());
    }

    @Override public FurnaceRecipe setInput(BlockType input) {
    	Material material = SpigotUtil.getMaterial( input );
        ((org.bukkit.inventory.FurnaceRecipe) getWrapper())
            .setInput(material);
        return this;
    }

}
