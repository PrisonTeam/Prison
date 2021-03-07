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
 * @author Faizaan A. Datoo
 */
public class Spigot18 
	extends Spigot18GUI 
	implements Compatibility {

	
	
    @Override 
    public EquipmentSlot getHand(PlayerInteractEvent e) {
        return EquipmentSlot.HAND; // Spigot 1.8 only has one hand
    }

	@Override 
	public ItemStack getItemInMainHand(PlayerInteractEvent e) {
        return getItemInMainHand( e.getPlayer() );
    }

    @Override 
    public ItemStack getItemInMainHand(Player player ) {
    	return getItemInMainHand( player.getInventory() );
    }
    
    @SuppressWarnings( "deprecation" )
	@Override 
    public ItemStack getItemInMainHand(PlayerInventory playerInventory) {
    	return playerInventory.getItemInHand();
    }
    
    
    public SpigotItemStack getPrisonItemInMainHand(PlayerInteractEvent e) {
    	return SpigotUtil.bukkitItemStackToPrison( getItemInMainHand( e ) );
    }
    
    public SpigotItemStack getPrisonItemInMainHand(Player player) {
    	return SpigotUtil.bukkitItemStackToPrison( getItemInMainHand( player ) );
    }

	@Override 
	public ItemStack getItemInOffHand(PlayerInteractEvent e) {
        return getItemInOffHand( e.getPlayer() );
    }

    @Override 
    public ItemStack getItemInOffHand(Player player ) {
    	return getItemInOffHand( player.getInventory() );
    }
    
    /**
     * This function does not exist in v1.8 so returns null.
     */
    public ItemStack getItemInOffHand(PlayerInventory playerInventory) {
    	return null;
    }
    
    @SuppressWarnings( "deprecation" )
	@Override
    public void setItemStackInMainHand( SpigotPlayerInventory inventory, SpigotItemStack itemStack ) {
    	
    	((org.bukkit.inventory.PlayerInventory) inventory.getWrapper())
    			.setItemInHand( itemStack.getBukkitStack() );
    }
    
    /**
     * Spigot v1.8 does not have an off hand, so set it to main hand.
     */
	@Override
    public void setItemStackInOffHand( SpigotPlayerInventory inventory, SpigotItemStack itemStack ) {
    	setItemStackInMainHand( inventory, itemStack );
    }
    
    @Override 
    public void playIronDoorSound(Location loc) {
        loc.getWorld().playEffect(loc, Effect.DOOR_TOGGLE, null);
    }

    @Override
    public Sound getAnvilSound() {
        return Sound.valueOf("ANVIL_LAND");
    }

    @Override
    public Sound getLevelUpSound() {
        return Sound.valueOf("LEVEL_UP");
    }

    @Override
    public Sound getOpenChestSound() {
        return Sound.valueOf("CHEST_OPEN");
    }

    @Override
    public Sound getCloseChestSound() {
        return Sound.valueOf("CHEST_CLOSE");
    }

    @Override
    public Sound getEntityItemBreakSound() {
        return Sound.valueOf("ITEM_BREAK");
    }

    @SuppressWarnings( "deprecation" )
	@Override
	public void breakItemInMainHand( Player player ) {
		player.setItemInHand( null );
		
		try
		{
			player.playSound(player.getLocation(), getEntityItemBreakSound(), 1.0F, 0.85F);
		}
		catch ( NoSuchFieldError e )
		{
			// Sound does not exist for this version of spigot.  Ignore.
		} 
	}

}
