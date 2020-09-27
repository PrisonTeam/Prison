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

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.inventory.ShapelessRecipe;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.util.BlockType;

/**
 * Created by DMP9 on 04/02/2017.
 */
public class SpigotShapelessRecipe extends SpigotRecipe implements ShapelessRecipe {

    public SpigotShapelessRecipe(org.bukkit.inventory.ShapelessRecipe wrapper) {
        super(wrapper);
    }

    @Override public ShapelessRecipe addIngredient(int count, BlockType ingredient) {
    	Material mat = SpigotUtil.getMaterial( ingredient );
        ((org.bukkit.inventory.ShapelessRecipe) getWrapper())
            .addIngredient(count, mat);
        return this;
    }

    @Override public ShapelessRecipe addIngredient(BlockType ingredient) {
    	Material mat = SpigotUtil.getMaterial( ingredient );
        ((org.bukkit.inventory.ShapelessRecipe) getWrapper())
            .addIngredient(mat);
        return this;
    }

    @Override public List<ItemStack> getIngredientList() {
        List<org.bukkit.inventory.ItemStack> bukkit =
            ((org.bukkit.inventory.ShapelessRecipe) getWrapper()).getIngredientList();
        List<ItemStack> result = new ArrayList<>();
        bukkit.forEach(x -> result.add(SpigotUtil.bukkitItemStackToPrison(x)));
        return result;
    }

    @Override public ShapelessRecipe removeIngredient(int count, BlockType ingredient) {
    	Material mat = SpigotUtil.getMaterial( ingredient );
        ((org.bukkit.inventory.ShapelessRecipe) getWrapper())
            .removeIngredient(count, mat);
        return this;
    }

    @Override public ShapelessRecipe removeIngredient(BlockType ingredient) {
    	Material mat = SpigotUtil.getMaterial( ingredient );
        ((org.bukkit.inventory.ShapelessRecipe) getWrapper())
            .removeIngredient(mat);
        return this;
    }

}
