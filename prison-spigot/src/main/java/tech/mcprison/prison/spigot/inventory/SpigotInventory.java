package tech.mcprison.prison.spigot.inventory;

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.inventory.Inventory;
import tech.mcprison.prison.internal.inventory.InventoryHolder;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.InventoryType;

import java.util.*;

/**
 * Created by DMP9 on 03/02/2017.
 */
public abstract class SpigotInventory implements Inventory {
    private org.bukkit.inventory.Inventory wrapper;

    public SpigotInventory(org.bukkit.inventory.Inventory wrapper){
        this.wrapper = wrapper;
    }

    public org.bukkit.inventory.Inventory getWrapper(){
        return wrapper;
    }

    @Override public int getSize() {
        return wrapper.getSize();
    }

    @Override public int getMaxStackSize() {
        return wrapper.getMaxStackSize();
    }

    @Override public String getName() {
        return wrapper.getName();
    }

    @Override public void setMaxStackSize(int size) {
        wrapper.setMaxStackSize(size);
    }

    @Override public boolean isEmpty() {
        return wrapper.getContents().length == 0;
    }

    @Override public boolean contains(ItemStack... itemStack) {
        for (ItemStack stack : itemStack){
            if (!wrapper.contains(SpigotUtil.prisonItemStackToBukkit(stack))){
                return false;
            }
        }
        return true;
    }

    @Override public boolean contains(ItemStack itemStack, int amount) {
        return false;
    }

    @Override public boolean contains(BlockType type) {
        return false;
    }

    @Override public boolean contains(BlockType type, int amount) {
        return false;
    }

    @Override public Iterator<ItemStack> getIterator() {
        return null;
    }

    @Override public Iterator<ItemStack> getIterator(int index) {
        return null;
    }

    @Override public List<ItemStack> getItems() {
        ArrayList<ItemStack> prisonStacks = new ArrayList<>();
        Arrays.asList(wrapper.getContents()).forEach(x -> prisonStacks.add(SpigotUtil.bukkitItemStackToPrison(x)));
        return prisonStacks;
    }

    @Override public HashMap<Integer, ItemStack> getItems(BlockType type) {
        return null;
    }

    @Override public HashMap<Integer, ItemStack> getItems(ItemStack stack) {
        return null;
    }

    @Override public ItemStack getItem(int index) {
        return null;
    }

    @Override public boolean addItem(ItemStack... itemStack) {
        return false;
    }

    @Override public boolean removeItem(ItemStack... itemStack) {
        return false;
    }

    @Override public void clearAll() {

    }

    @Override public void clearAll(int index) {

    }

    @Override public void clear(int index) {

    }

    @Override public void clear(BlockType type) {

    }

    @Override public void clear(ItemStack stack) {

    }

    @Override public int first(ItemStack stack) {
        return 0;
    }

    @Override public int first(BlockType type) {
        return 0;
    }

    @Override public int firstEmpty() {
        return 0;
    }

    @Override public InventoryHolder getHolder() {
        return null;
    }

    @Override public InventoryType getType() {
        return null;
    }

    @Override public Iterator<ItemStack> iterator() {
        return null;
    }
}
