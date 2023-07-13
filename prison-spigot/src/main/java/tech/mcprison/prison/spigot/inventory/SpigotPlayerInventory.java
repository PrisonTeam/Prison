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
import java.util.Arrays;
import java.util.List;

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.internal.inventory.PlayerInventory;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.spigot.compat.SpigotCompatibility;

/**
 * Created by DMP9 on 04/02/2017.
 */
public class SpigotPlayerInventory 
	extends SpigotInventory 
	implements PlayerInventory {

    public SpigotPlayerInventory(org.bukkit.inventory.PlayerInventory wrapper) {
        super(wrapper);
    }

    /**
     * <p>This function gets all of the player's contents from the getContents(),
     * getExtraContents(), and getArmorContents().
     * </p>
     * 
     * @return
     */
    public List<ItemStack> getAllContents() {
    	List<ItemStack> results = new ArrayList<>();
    	
    	for ( ItemStack iStack : getContents() ) {
    		if ( iStack != null ) {
    			results.add(iStack);
    		}
    	}
    	
    	try {
			for ( ItemStack iStack : getExtraContents() ) {
				if ( iStack != null ) {
					results.add(iStack);
				}
			}
		} catch (NoSuchMethodError e) {
			// Ignore on older versions of spigot... Spigot 1.8.8 does not have this function.
		}
    	
    	// then remove the armor ItemStacks:
    	for ( ItemStack iStack : getArmorContents() ) {
    		if ( iStack != null ) {
    			results.remove(iStack);
    		}
    	}
    	
    	return results;
    }
    
    @Override
    public void removeItem( ItemStack... iStack ) {
    	if ( iStack != null ) {
    		
    		// Remove all items...
    		super.removeItem(iStack);

    		// But since this is a player's inventory, try to remove them from the players slots
    		for (ItemStack itemStack : iStack) {
    			PrisonBlock pBlock = itemStack.getMaterial();
    			
    			if ( isPrisonBlockEqual( pBlock, getBoots() ) ) {
    				setBoots( new SpigotItemStack( 0, PrisonBlock.AIR ) );
    			}
    			if ( isPrisonBlockEqual( pBlock, getChestplate() ) ) {
    				setChestplate( new SpigotItemStack( 0, PrisonBlock.AIR ) );
    			}
    			if ( isPrisonBlockEqual( pBlock, getHelmet() ) ) {
    				setHelmet( new SpigotItemStack( 0, PrisonBlock.AIR ) );
    			}
    			if ( isPrisonBlockEqual( pBlock, getLeggings() ) ) {
    				setLeggings( new SpigotItemStack( 0, PrisonBlock.AIR ) );
    			}
    			
    			try {
    				
    				if ( isPrisonBlockEqual( pBlock, getItemInLeftHand() ) ) {
    					setItemInLeftHand( new SpigotItemStack( 0, PrisonBlock.AIR ) );
    				}
    			}
    			catch ( Exception e ) {
    				// java 1.8 may not be able to handle this
    			}
    			try {
    				
    				if ( isPrisonBlockEqual( pBlock, getItemInRightHand() ) ) {
    					setItemInRightHand( new SpigotItemStack( 0, PrisonBlock.AIR ) );
    				}
    			}
    			catch ( Exception e ) {
    				// java 1.8 may not be able to handle this
    			}
    			
			}
    	}
    }
    
    
    private boolean isPrisonBlockEqual( PrisonBlock pBlock, ItemStack iStack ) {
    	boolean results = false;
    	
    	if ( pBlock != null && iStack != null ) {
    		results = pBlock.equals( iStack.getMaterial() );
    	}
    	
    	return results;
    }
    
    @Override
	public ItemStack[] getContents() {
    	List<ItemStack> items = new ArrayList<>();
        Arrays.asList(((org.bukkit.inventory.PlayerInventory) getWrapper()).getContents())
            .forEach(x -> items.add(SpigotUtil.bukkitItemStackToPrison(x)));
        return (ItemStack[]) items.toArray();
    }
    
    @Override
	public void setContents( ItemStack[] items ) {
		List<org.bukkit.inventory.ItemStack> stacks = new ArrayList<>();
        Arrays.asList(items).forEach(x -> stacks.add(SpigotUtil.prisonItemStackToBukkit(x)));
        ((org.bukkit.inventory.PlayerInventory) getWrapper())
            .setContents((org.bukkit.inventory.ItemStack[]) stacks.toArray());
	}
	
	@Override
	public ItemStack[] getExtraContents() {
		try {
	    	List<ItemStack> items = new ArrayList<>();
	        Arrays.asList(((org.bukkit.inventory.PlayerInventory) getWrapper()).getExtraContents())
	            .forEach(x -> items.add(SpigotUtil.bukkitItemStackToPrison(x)));
	        return (ItemStack[]) items.toArray();
		}
		catch ( Exception e ) {
			return null;
		}
	}
	@Override
	public void setExtraContents( ItemStack[] items ) {
		try {
			List<org.bukkit.inventory.ItemStack> stacks = new ArrayList<>();
			Arrays.asList(items).forEach(x -> stacks.add(SpigotUtil.prisonItemStackToBukkit(x)));
			((org.bukkit.inventory.PlayerInventory) getWrapper())
			.setExtraContents((org.bukkit.inventory.ItemStack[]) stacks.toArray());
		}
		catch ( Exception e ) {
			// extraContents does not exist in spigot 1.8
		}
	}
	
	
    
    @Override
    public ItemStack[] getArmorContents() {
        List<ItemStack> items = new ArrayList<>();
        Arrays.asList(((org.bukkit.inventory.PlayerInventory) getWrapper()).getArmorContents())
            .forEach(x -> items.add(SpigotUtil.bukkitItemStackToPrison(x)));
        return (ItemStack[]) items.toArray();
    }

    @Override 
    public void setArmorContents(ItemStack[] items) {
        List<org.bukkit.inventory.ItemStack> stacks = new ArrayList<>();
        Arrays.asList(items).forEach(x -> stacks.add(SpigotUtil.prisonItemStackToBukkit(x)));
        ((org.bukkit.inventory.PlayerInventory) getWrapper())
            .setArmorContents((org.bukkit.inventory.ItemStack[]) stacks.toArray());
    }

    @Override public ItemStack getBoots() {
        return SpigotUtil.bukkitItemStackToPrison(
            ((org.bukkit.inventory.PlayerInventory) getWrapper()).getBoots());
    }

    @Override public void setBoots(ItemStack boots) {
        ((org.bukkit.inventory.PlayerInventory) getWrapper())
            .setBoots(SpigotUtil.prisonItemStackToBukkit(boots));
    }

    @Override public ItemStack getChestplate() {
        return SpigotUtil.bukkitItemStackToPrison(
            ((org.bukkit.inventory.PlayerInventory) getWrapper()).getChestplate());
    }

    @Override public void setChestplate(ItemStack chestplate) {
        ((org.bukkit.inventory.PlayerInventory) getWrapper())
            .setBoots(SpigotUtil.prisonItemStackToBukkit(chestplate));
    }

    @Override public int getHeldItemSlot() {
        return ((org.bukkit.inventory.PlayerInventory) getWrapper()).getHeldItemSlot();
    }

    @Override public void setHeldItemSlot(int slot) {
        ((org.bukkit.inventory.PlayerInventory) getWrapper()).setHeldItemSlot(slot);
    }

    @Override public ItemStack getHelmet() {
        return SpigotUtil.bukkitItemStackToPrison(
            ((org.bukkit.inventory.PlayerInventory) getWrapper()).getHelmet());
    }

    @Override public void setHelmet(ItemStack helmet) {
        ((org.bukkit.inventory.PlayerInventory) getWrapper())
            .setBoots(SpigotUtil.prisonItemStackToBukkit(helmet));
    }

    @Override public ItemStack getLeggings() {
        return SpigotUtil.bukkitItemStackToPrison(
            ((org.bukkit.inventory.PlayerInventory) getWrapper()).getLeggings());
    }

    @Override public void setLeggings(ItemStack leggings) {
        ((org.bukkit.inventory.PlayerInventory) getWrapper())
            .setBoots(SpigotUtil.prisonItemStackToBukkit(leggings));
    }

    @Override public ItemStack getItemInLeftHand() {

    	return SpigotUtil.bukkitItemStackToPrison(
    			SpigotCompatibility.getInstance().getItemInOffHand( 
						((org.bukkit.inventory.PlayerInventory) getWrapper()) )
    				);
    	
//        return SpigotUtil.bukkitItemStackToPrison(
//            ((org.bukkit.inventory.PlayerInventory) getWrapper()).getItemInOffHand());
    }

    @Override public void setItemInLeftHand(ItemStack stack) {
    	
    	if ( stack instanceof SpigotItemStack ) {
    		
    		SpigotCompatibility.getInstance()
    						.setItemStackInOffHand( this, ((SpigotItemStack) stack) );
    	}
    }

    @Override
    public ItemStack getItemInRightHand() {
    	
    	
    	return SpigotUtil.bukkitItemStackToPrison(
    			SpigotCompatibility.getInstance().getItemInMainHand( 
    						((org.bukkit.inventory.PlayerInventory) getWrapper()) ));
    }

    @Override 
    public void setItemInRightHand(ItemStack stack) {
    	
    	if ( stack instanceof SpigotItemStack ) {
    		
    		SpigotCompatibility.getInstance()
    						.setItemStackInMainHand( this, ((SpigotItemStack) stack) );
    	}
    }
    
}
