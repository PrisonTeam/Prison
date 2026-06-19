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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.BeaconInventory;
import org.bukkit.inventory.BrewerInventory;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.internal.inventory.Inventory;
import tech.mcprison.prison.internal.inventory.InventoryHolder;
import tech.mcprison.prison.internal.inventory.InventoryType;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.spigot.game.SpigotPlayer;

/**
 * Created by DMP9 on 03/02/2017.
 */
public class SpigotInventory implements Inventory {

    private org.bukkit.inventory.Inventory wrapper;

    public SpigotInventory(org.bukkit.inventory.Inventory wrapper) {
        this.wrapper = wrapper;
    }

    public static SpigotInventory fromWrapper(org.bukkit.inventory.Inventory wrapper) {
        switch (SpigotUtil.bukkitInventoryTypeToPrison(wrapper.getType())) {
            case ANVIL:
                return new SpigotAnvil((AnvilInventory) wrapper);
            case BEACON:
                return new SpigotBeacon((BeaconInventory) wrapper);
            case BREWING:
                return new SpigotBrewer((BrewerInventory) wrapper);
            case CHEST:
                return new SpigotInventory(wrapper);
                
			case CRAFTING:
				break;
			case CREATIVE:
				break;
			case DISPENSER:
				break;
			case DROPPER:
				break;
			case ENCHANTING:
				break;
			case ENDER_CHEST:
				break;
			case FURNACE:
				break;
			case HOPPER:
				break;
			case PLAYER:
				break;
			case VILLAGER:
				break;
			case WORKBENCH:
				break;
			default:
				break;
        }
        return null;
    }

    public org.bukkit.inventory.Inventory getWrapper() {
        return wrapper;
    }

    @SuppressWarnings( "deprecation" )
	@Override 
	public String getTitle() {
        return wrapper.getTitle();
    }

    @Override 
    public List<Player> getViewers() {
        List<Player> players = new ArrayList<>();
        wrapper.getViewers()
            .forEach(x -> players.add(new SpigotPlayer((org.bukkit.entity.Player) x)));
        return players;
    }

    @Override 
    public int getSize() {
        return wrapper.getSize();
    }

    @Override 
    public int getMaxStackSize() {
        return wrapper.getMaxStackSize();
    }

    @Override 
    public void setMaxStackSize(int size) {
        wrapper.setMaxStackSize(size);
    }

    @SuppressWarnings( "deprecation" )
	@Override 
	public String getName() {
        return wrapper.getName();
    }

    @Override 
    public boolean isEmpty() {
        return wrapper.getContents().length == 0;
    }

    @Override 
    public boolean contains(ItemStack itemStack) {
        return wrapper.contains(SpigotUtil.prisonItemStackToBukkit(itemStack));
    }

    @Override 
    public boolean contains( PrisonBlock type ) {
    	XMaterial xMat = SpigotUtil.getXMaterial( type );
    	return wrapper.contains( xMat.parseItem() );
    	
//    	org.bukkit.inventory.ItemStack bukkitStack = SpigotUtil.getItemStack( type, 1 );
//    	return wrapper.contains(bukkitStack);
//        MaterialData materialData = SpigotUtil.blockTypeToMaterial(type);
//        org.bukkit.inventory.ItemStack stack =
//            new org.bukkit.inventory.ItemStack(materialData.getItemType());
//        stack.setData(materialData);
//        return wrapper.contains(stack);
    }

    @Override 
    public Iterator<ItemStack> getIterator() {
        ArrayList<ItemStack> prisonStacks = new ArrayList<>();
        Arrays.asList(wrapper.getContents())
            .forEach(x -> prisonStacks.add(SpigotUtil.bukkitItemStackToPrison(x)));
        return prisonStacks.iterator();
    }

    @Override 
    public ItemStack[] getItems() {
        ArrayList<ItemStack> prisonStacks = new ArrayList<>();
        Arrays.asList(wrapper.getContents())
            .forEach(x -> prisonStacks.add(SpigotUtil.bukkitItemStackToPrison(x)));
        return prisonStacks.toArray(new ItemStack[]{});
    }

    @Override 
    public void setItems(List<ItemStack> items) {
        List<org.bukkit.inventory.ItemStack> stacks = new ArrayList<>();
        items.forEach(x -> stacks.add(SpigotUtil.prisonItemStackToBukkit(x)));
    }

    @Override 
    public HashMap<Integer, ItemStack> getItems( PrisonBlock type ) {
        HashMap<Integer, ItemStack> result = new HashMap<>();
        List<ItemStack> items = Arrays.asList(getItems());
        items.removeIf(x -> x.getMaterial().compareTo( type ) != 0 );
        items.forEach(y -> result.put(items.indexOf(y), y));
        return result;
    }

    @Override public HashMap<Integer, ItemStack> getItems(ItemStack stack) {
        HashMap<Integer, ItemStack> result = new HashMap<>();
        List<ItemStack> items = Arrays.asList(getItems());
        items.removeIf(x -> x != stack);
        items.forEach(y -> result.put(items.indexOf(y), y));
        return result;
    }

    @Override 
    public ItemStack getItem(int index) {
        return SpigotUtil.bukkitItemStackToPrison(wrapper.getItem(index));
    }

    @Override 
    public void addItem(ItemStack... itemStack) {
        ArrayList<org.bukkit.inventory.ItemStack> stacks = new ArrayList<>();
        for (ItemStack stack : itemStack) {
            stacks.add(SpigotUtil.prisonItemStackToBukkit(stack));
        }
        wrapper.addItem(stacks.toArray(new org.bukkit.inventory.ItemStack[]{}));
    }

    @Override 
    public void removeItem(ItemStack... itemStack) {
    	
    	int size = wrapper.getSize();
    	
    	// This may be sub optimal, but when dealing with modified items, such as with 
    	// custom names, prison cannot faithfully reproduce them such that the bukkit's 
    	// ItemStack.removeItem() will work correctly.  We cannot properly reproduce 
    	// all enchantments, NBTs, and other alterations.  So much match on individual
    	// field values such as types and display names.
    	for (ItemStack stack : itemStack) {
    		
    		String itemName = stack.getMaterial().getBlockNameSearch();
    		int amt = stack.getAmount();
    		
    		for ( int i = 0; i < size && amt > 0; i++ ) {
    			org.bukkit.inventory.ItemStack item = wrapper.getItem(i);
    			
    			if ( item != null ) {
    				
    				SpigotItemStack sItemStack = new SpigotItemStack( item );
    				String sItemName = sItemStack.getMaterial().getBlockNameSearch();
    				
    				if ( itemName.equalsIgnoreCase( sItemName ) ) {
    					
    					// We have a match on item's blockNameSearch, so remove the requested
    					// quantity. Set the current bukkit ItemStack's amount to what we 
    					// need to remove, then use the bukkit item stack to remove it.
    					// Exit out of the bukkit for loop by seeing amt to zero.
    					
    					// If the amount is more than what can be removed in one item stack,
    					// bukkit will remove the total requested amount from multiple item stacks,
    					// so we only need to Process one item stack.
    					item.setAmount(amt);
    				
    					wrapper.remove( item );
    					
    					amt = 0;
    					
    				}
    			}
    		}
    	}
    	
    	
    	
//        ArrayList<org.bukkit.inventory.ItemStack> stacks = new ArrayList<>();
//        
//        for (ItemStack stack : itemStack) {
//            stacks.add(SpigotUtil.prisonItemStackToBukkit(stack));
//        }
//        
//        wrapper.removeItem(stacks.toArray(new org.bukkit.inventory.ItemStack[]{}));
    }

    @Override 
    public void clearAll() {
        wrapper.clear();
    }

    @Override 
    public void clearAll(int index) {
        wrapper.setContents(Arrays.copyOfRange(wrapper.getContents(), 0, index));
    }

    @Override 
    public void clear(int index) {
        wrapper.clear(index);
    }

    @Override 
    public void clear( PrisonBlock type ) {
    	
    	XMaterial xMat = SpigotUtil.getXMaterial( type );
    	
    	if ( xMat != null ) { 
    		wrapper.remove( xMat.parseItem() );
    	}
    	
//    	org.bukkit.inventory.ItemStack bukkitStack = SpigotUtil.getItemStack( type, 1 );
//    	wrapper.remove( bukkitStack );
//        MaterialData materialData = SpigotUtil.blockTypeToMaterial(type);
//        org.bukkit.inventory.ItemStack stack =
//            new org.bukkit.inventory.ItemStack(materialData.getItemType());
//        stack.setData(materialData);
//        wrapper.remove(stack);
    }

    @Override 
    public void clear(ItemStack stack) {
        wrapper.remove(SpigotUtil.prisonItemStackToBukkit(stack));
    }

    @Override 
    public int first(ItemStack stack) {
        return wrapper.first(SpigotUtil.prisonItemStackToBukkit(stack));
    }

    @Override 
    public int first( PrisonBlock type) {
    	int results = -1;
    	
    	XMaterial xMat = SpigotUtil.getXMaterial( type );
    	
    	if ( xMat != null ) { 
    		results = wrapper.first( xMat.parseItem() );
    	}
    	
    	return results;
//    	org.bukkit.inventory.ItemStack bukkitStack = SpigotUtil.getItemStack( type, 1 );
//    	return wrapper.first(bukkitStack);
//        MaterialData materialData = SpigotUtil.blockTypeToMaterial(type);
//        org.bukkit.inventory.ItemStack stack =
//            new org.bukkit.inventory.ItemStack(materialData.getItemType());
//        stack.setData(materialData);
//        return wrapper.first(stack);
    }

    @Override
    public int firstEmpty() {
        return wrapper.firstEmpty();
    }

    @Override 
    public InventoryHolder getHolder() {
        return new SpigotInventoryHolder(wrapper.getHolder());
    }

    @Override 
    public InventoryType getType() {
        return SpigotUtil.bukkitInventoryTypeToPrison(wrapper.getType());
    }

    @Override 
    public Iterator<ItemStack> iterator() {
        return getIterator();
    }

}
