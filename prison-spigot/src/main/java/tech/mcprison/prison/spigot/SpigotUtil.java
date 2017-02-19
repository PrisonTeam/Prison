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

package tech.mcprison.prison.spigot;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tech.mcprison.prison.spigot.game.SpigotWorld;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.InventoryType;
import tech.mcprison.prison.util.Location;

import java.util.Collections;
import java.util.List;

/**
 * Utilities for converting Prison-Core types to Spigot types.
 *
 * @author Faizaan A. Datoo
 */
public class SpigotUtil {

    private SpigotUtil() {
    }

    /*
     * BlockType and Material
     */

    public static BlockType materialToBlockType(Material material) {
        return BlockType.getBlock(material.getId()); // To be safe, we use legacy ID
    }

    public static Material blockTypeToMaterial(BlockType type) {
        return Material.getMaterial(type.getLegacyId()); // To be safe, we use legacy ID
    }

    /*
     * Location
     */

    public static Location bukkitLocationToPrison(org.bukkit.Location bukkitLocation) {
        return new Location(new SpigotWorld(bukkitLocation.getWorld()), bukkitLocation.getX(),
            bukkitLocation.getY(), bukkitLocation.getZ(), bukkitLocation.getPitch(),
            bukkitLocation.getYaw());
    }

    public static org.bukkit.Location prisonLocationToBukkit(Location prisonLocation) {
        return new org.bukkit.Location(Bukkit.getWorld(prisonLocation.getWorld().getName()),
            prisonLocation.getX(), prisonLocation.getY(), prisonLocation.getZ(),
            prisonLocation.getYaw(), prisonLocation.getPitch());
    }

    /*
     * ItemStack
     */

    public static tech.mcprison.prison.internal.ItemStack bukkitItemStackToPrison(
        ItemStack bukkitStack) {
        ItemMeta meta = bukkitStack.getItemMeta();
        if(!bukkitStack.hasItemMeta()) {
            meta = Bukkit.getItemFactory().getItemMeta(bukkitStack.getType());
        }

        String displayName;

        if (meta.hasDisplayName()) {
            displayName = meta.getDisplayName();
        } else {
            displayName = StringUtils
                .capitalize(bukkitStack.getType().name().replaceAll("_", " ").toLowerCase());
        }

        int amount = bukkitStack.getAmount();

        BlockType type = materialToBlockType(bukkitStack.getType());

        List<String> lore = meta.hasLore() ? meta.getLore() : Collections.emptyList();
        String[] lore_arr = lore.toArray(new String[lore.size()]);

        return new tech.mcprison.prison.internal.ItemStack(displayName, amount, type, lore_arr);
    }

    public static ItemStack prisonItemStackToBukkit(
        tech.mcprison.prison.internal.ItemStack prisonStack) {
        ItemStack bukkitStack =
            new ItemStack(blockTypeToMaterial(prisonStack.getMaterial()), prisonStack.getAmount());
        ItemMeta meta = bukkitStack.getItemMeta();
        meta.setDisplayName(prisonStack.getName());
        meta.setLore(prisonStack.getLore());
        bukkitStack.setItemMeta(meta);

        return bukkitStack;
    }

    /*
     * InventoryType
     */
    public static InventoryType bukkitInventoryTypeToPrison(
        org.bukkit.event.inventory.InventoryType type) {
        return InventoryType.valueOf(type.toString());
    }

    public static org.bukkit.event.inventory.InventoryType prisonInventoryTypeToBukkit(
        InventoryType type) {
        return org.bukkit.event.inventory.InventoryType.valueOf(type.toString());
    }
}
