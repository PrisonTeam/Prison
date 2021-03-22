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

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.spigot.inventory.SpigotPlayerInventory;

/**
 * Spigot 1.9, 1.10, and 1.11.
 *
 * @author Faizaan A. Datoo
 */
public class Spigot19 
	extends Spigot19GUI
	implements Compatibility {

    @Override 
    public EquipmentSlot getHand(PlayerInteractEvent e) {
        if (e.getHand() == null) {
            return null;
        } else {
            return EquipmentSlot.valueOf(e.getHand().name());
        }
    }

    @Override 
    public ItemStack getItemInMainHand(PlayerInteractEvent e) {
        return getItemInMainHand( e.getPlayer() );
    }

    @Override 
    public ItemStack getItemInMainHand(Player player) {
    	return getItemInMainHand( player.getInventory() );
    }
    
	@Override 
    public ItemStack getItemInMainHand(PlayerInventory playerInventory) {
    	return playerInventory.getItemInMainHand();
    }
    
    public SpigotItemStack getPrisonItemInMainHand(PlayerInteractEvent e) {
    	return SpigotUtil.bukkitItemStackToPrison( getItemInMainHand( e ) );
    }
    
    public SpigotItemStack getPrisonItemInMainHand(Player player) {
    	return SpigotUtil.bukkitItemStackToPrison( getItemInMainHand( player ) );
    }
    
	@Override 
	public ItemStack getItemInOffHand(PlayerInteractEvent e) {
        return getItemInOffHand(e.getPlayer());
    }

    @Override 
    public ItemStack getItemInOffHand(Player player ) {
    	return getItemInOffHand(player.getInventory());
    }
    
    public ItemStack getItemInOffHand(PlayerInventory playerInventory) {
    	return playerInventory.getItemInOffHand();
    }
    
    @Override
    public void setItemStackInMainHand( SpigotPlayerInventory inventory, SpigotItemStack itemStack ) {
    	
    	((org.bukkit.inventory.PlayerInventory) inventory.getWrapper())
    			.setItemInMainHand( itemStack.getBukkitStack() );
    }
    
    @Override
    public void setItemStackInOffHand( SpigotPlayerInventory inventory, SpigotItemStack itemStack ) {
    	
    	((org.bukkit.inventory.PlayerInventory) inventory.getWrapper())
    	.setItemInOffHand( itemStack.getBukkitStack() );
    }
    @Override 
    public void playIronDoorSound(Location loc) {
        loc.getWorld().playEffect(loc, Effect.IRON_DOOR_TOGGLE, null);
    }

    @Override
    public Sound getAnvilSound() {
        return Sound.valueOf("BLOCK_ANVIL_BREAK");
    }

    @Override
    public Sound getLevelUpSound() {
        return Sound.valueOf("ENTITY_PLAYER_LEVELUP");
    }

    @Override
    public Sound getOpenChestSound() {
        return Sound.valueOf("BLOCK_CHEST_OPEN");
    }

    @Override
    public Sound getCloseChestSound() {
        return Sound.valueOf("BLOCK_CHEST_CLOSE");
    }

    @Override
    public Sound getEntityItemBreakSound() {
        return Sound.valueOf("ENTITY_ITEM_BREAK");
    }

    @Override
	public void breakItemInMainHand( Player player ) {
		player.getInventory().setItemInMainHand( null );
		
		player.playSound(player.getLocation(), getEntityItemBreakSound(), 1.0F, 0.85F);
	}

}
