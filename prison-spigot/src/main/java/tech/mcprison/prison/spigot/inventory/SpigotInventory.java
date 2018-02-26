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

import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.BeaconInventory;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.material.MaterialData;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.inventory.Inventory;
import tech.mcprison.prison.internal.inventory.InventoryHolder;
import tech.mcprison.prison.internal.inventory.InventoryType;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.util.BlockType;

import java.util.*;

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
        }
        return null;
    }

    public org.bukkit.inventory.Inventory getWrapper() {
        return wrapper;
    }

    @Override public String getTitle() {
        return wrapper.getTitle();
    }

    @Override public List<Player> getViewers() {
        List<Player> players = new ArrayList<>();
        wrapper.getViewers()
            .forEach(x -> players.add(new SpigotPlayer((org.bukkit.entity.Player) x)));
        return players;
    }

    @Override public int getSize() {
        return wrapper.getSize();
    }

    @Override public int getMaxStackSize() {
        return wrapper.getMaxStackSize();
    }

    @Override public void setMaxStackSize(int size) {
        wrapper.setMaxStackSize(size);
    }

    @Override public String getName() {
        return wrapper.getName();
    }

    @Override public boolean isEmpty() {
        return wrapper.getContents().length == 0;
    }

    @Override public boolean contains(ItemStack itemStack) {
        return wrapper.contains(SpigotUtil.prisonItemStackToBukkit(itemStack));
    }

    @Override public boolean contains(BlockType type) {
        MaterialData materialData = SpigotUtil.blockTypeToMaterial(type);
        org.bukkit.inventory.ItemStack stack =
            new org.bukkit.inventory.ItemStack(materialData.getItemType());
        stack.setData(materialData);
        return wrapper.contains(stack);
    }

    @Override public Iterator<ItemStack> getIterator() {
        ArrayList<ItemStack> prisonStacks = new ArrayList<>();
        Arrays.asList(wrapper.getContents())
            .forEach(x -> prisonStacks.add(SpigotUtil.bukkitItemStackToPrison(x)));
        return prisonStacks.iterator();
    }

    @Override public ItemStack[] getItems() {
        ArrayList<ItemStack> prisonStacks = new ArrayList<>();
        Arrays.asList(wrapper.getContents())
            .forEach(x -> prisonStacks.add(SpigotUtil.bukkitItemStackToPrison(x)));
        return (ItemStack[]) prisonStacks.toArray();
    }

    @Override public void setItems(List<ItemStack> items) {
        List<org.bukkit.inventory.ItemStack> stacks = new ArrayList<>();
        items.forEach(x -> stacks.add(SpigotUtil.prisonItemStackToBukkit(x)));
    }

    @Override public HashMap<Integer, ItemStack> getItems(BlockType type) {
        HashMap<Integer, ItemStack> result = new HashMap<>();
        List<ItemStack> items = Arrays.asList(getItems());
        items.removeIf(x -> x.getMaterial() != type);
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

    @Override public ItemStack getItem(int index) {
        return SpigotUtil.bukkitItemStackToPrison(wrapper.getItem(index));
    }

    @Override public void addItem(ItemStack... itemStack) {
        ArrayList<org.bukkit.inventory.ItemStack> stacks = new ArrayList<>();
        for (ItemStack stack : itemStack) {
            stacks.add(SpigotUtil.prisonItemStackToBukkit(stack));
        }
        wrapper.addItem((org.bukkit.inventory.ItemStack[]) stacks.toArray());
    }

    @Override public void removeItem(ItemStack... itemStack) {
        ArrayList<org.bukkit.inventory.ItemStack> stacks = new ArrayList<>();
        for (ItemStack stack : itemStack) {
            stacks.add(SpigotUtil.prisonItemStackToBukkit(stack));
        }
        wrapper.removeItem((org.bukkit.inventory.ItemStack[]) stacks.toArray());
    }

    @Override public void clearAll() {
        wrapper.clear();
    }

    @Override public void clearAll(int index) {
        wrapper.setContents(Arrays.copyOfRange(wrapper.getContents(), 0, index));
    }

    @Override public void clear(int index) {
        wrapper.clear(index);
    }

    @Override public void clear(BlockType type) {
        MaterialData materialData = SpigotUtil.blockTypeToMaterial(type);
        org.bukkit.inventory.ItemStack stack =
            new org.bukkit.inventory.ItemStack(materialData.getItemType());
        stack.setData(materialData);
        wrapper.remove(stack);
    }

    @Override public void clear(ItemStack stack) {
        wrapper.remove(SpigotUtil.prisonItemStackToBukkit(stack));
    }

    @Override public int first(ItemStack stack) {
        return wrapper.first(SpigotUtil.prisonItemStackToBukkit(stack));
    }

    @Override public int first(BlockType type) {
        MaterialData materialData = SpigotUtil.blockTypeToMaterial(type);
        org.bukkit.inventory.ItemStack stack =
            new org.bukkit.inventory.ItemStack(materialData.getItemType());
        stack.setData(materialData);
        return wrapper.first(stack);
    }

    @Override public int firstEmpty() {
        return wrapper.firstEmpty();
    }

    @Override public InventoryHolder getHolder() {
        return new SpigotInventoryHolder(wrapper.getHolder());
    }

    @Override public InventoryType getType() {
        return SpigotUtil.bukkitInventoryTypeToPrison(wrapper.getType());
    }

    @Override public Iterator<ItemStack> iterator() {
        return getIterator();
    }

}
