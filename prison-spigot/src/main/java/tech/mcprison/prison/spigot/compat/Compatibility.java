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

package tech.mcprison.prison.spigot.compat;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.spigot.inventory.SpigotPlayerInventory;

/**
 * Different Spigot versions have different methods.
 * The compatibility system ensures that each version can be used with the same code.
 *
 * @author Faizaan A. Datoo
 */
public interface Compatibility 
	extends CompatibilityGUI {
	
	
    public EquipmentSlot getHand(PlayerInteractEvent e);

    public ItemStack getItemInMainHand(PlayerInteractEvent e);

    public ItemStack getItemInMainHand(Player player);
    
    public ItemStack getItemInMainHand(PlayerInventory playerInventory);
    
    public SpigotItemStack getPrisonItemInMainHand(PlayerInteractEvent e);
    
    public SpigotItemStack getPrisonItemInMainHand(Player player);
    
    public ItemStack getItemInOffHand(PlayerInteractEvent e);

    public ItemStack getItemInOffHand(Player player);
    
    public ItemStack getItemInOffHand(PlayerInventory playerInventory);
    
    public void setItemStackInMainHand( SpigotPlayerInventory inventory, SpigotItemStack itemStack );
    
    public void setItemStackInOffHand( SpigotPlayerInventory inventory, SpigotItemStack itemStack );
    
    public void breakItemInMainHand(Player player);
    
    public void playIronDoorSound(Location loc);

    public Sound getAnvilSound();

    public Sound getLevelUpSound();

    public Sound getOpenChestSound();

    public Sound getCloseChestSound();

    public Sound getEntityItemBreakSound();

    enum EquipmentSlot {
        HAND, OFF_HAND, FEET, LEGS, CHEST, HEAD
    }


}
