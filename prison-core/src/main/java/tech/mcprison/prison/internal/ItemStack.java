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

package tech.mcprison.prison.internal;

import org.apache.commons.lang3.StringUtils;
import tech.mcprison.prison.util.BlockType;

import java.util.*;

/**
 * Represents an item stack. An item stack is a uniquely named stack in a player's inventory.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class ItemStack {

    private String displayName = null;
    private int amount;
    private BlockType material;
    private List<String> lore;
    private Map<Integer, Integer> enchantments;

    public ItemStack(String displayName, int amount, BlockType material, String... lore) {
        this.displayName = displayName;
        this.amount = amount;
        this.material = material;
        this.lore = new ArrayList<>(Arrays.asList(lore));
        this.enchantments = new HashMap<>();
    }

    public ItemStack(int amount, BlockType material, String... lore) {
        this.amount = amount;
        this.material = material;
        this.lore = new ArrayList<>(Arrays.asList(lore));
    }

    /**
     * Returns the name of the item stack, derived from its BlockType name.
     */
    public String getName() {
        return StringUtils.capitalize(material.name().replaceAll("_", " ").toLowerCase());
    }

    /**
     * Returns the display name of the item stack. This may include colors and may also be null!
     */
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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
    public BlockType getMaterial() {
        return material;
    }

    public List<String> getLore() {
        return lore;
    }

    public Map<Integer, Integer> getEnchantments() {
        return enchantments;
    }

    public void addEnchantment(int enchantment, int level) {
        enchantments.put(enchantment, level);
    }

    public boolean hasEnchantments() {
        return !enchantments.isEmpty();
    }

    public boolean hasEnchantment(int enchantment) {
        return enchantments.containsKey(enchantment);
    }

    @Override public int hashCode() {
        int result = amount;
        result = 31 * result + material.hashCode();
        return result;
    }

    @Override public String toString() {
        return "ItemStack{" + "displayName='" + displayName + '\'' + ", amount=" + amount
            + ", material=" + material + ", lore=" + lore + ", enchantments=" + enchantments + '}';
    }
}
