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

package tech.mcprison.prison.platform;

import tech.mcprison.prison.util.Block;
import tech.mcprison.prison.util.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents an item stack. An item stack is a uniquely named stack in a player's inventory.
 *
 * @author Faizaan A. Datoo
 * @since 3.0
 */
public class ItemStack {

    String name;
    int amount;
    Block material;
    private List<String> lore;

    public ItemStack(String name, int amount, Block material, String... lore) {
        this.name = name;
        this.amount = amount;
        this.material = material;
        this.lore = new ArrayList<>(Arrays.asList(lore));
    }

    /**
     * Returns the display name of the item stack. This may include colors.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the amount of items in this stack.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Returns the type of items in this stack.
     */
    public Block getMaterial() {
        return material;
    }

    public List<String> getLore() {
        return lore;
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemStack)) {
            return false;
        }

        ItemStack itemStack = (ItemStack) o;

        String myName = ChatColor.stripColor(name);  // Remove colors from my name
        String theirName = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&',
            itemStack.getName()));  // Remove colors from their name

        return myName.equals(theirName) && material == itemStack.material;

    }

    @Override public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + material.hashCode();
        return result;
    }

    @Override public String toString() {
        return "ItemStack{" + "name='" + name + '\'' + ", amount=" + amount + ", material="
            + material + '}';
    }

}
