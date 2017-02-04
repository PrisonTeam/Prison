package tech.mcprison.prison.spigot.inventory;

import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.BeaconInventory;
import org.bukkit.inventory.BrewerInventory;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.inventory.Inventory;
import tech.mcprison.prison.internal.inventory.InventoryHolder;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
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

    public static SpigotInventory fromWrapper(org.bukkit.inventory.Inventory wrapper){
        switch (SpigotUtil.bukkitInventoryTypeToPrison(wrapper.getType())){
            case ANVIL:
                return new SpigotAnvil((AnvilInventory) wrapper);
            case BEACON:
                return new SpigotBeacon((BeaconInventory) wrapper);
            case BREWING:
                return new SpigotBrewer((BrewerInventory) wrapper);

        }
        return null;
    }

    public org.bukkit.inventory.Inventory getWrapper(){
        return wrapper;
    }

    @Override public String getTitle(){
        return wrapper.getTitle();
    }

    @Override public List<Player> getViewers(){
        List<Player> players = new ArrayList<>();
        wrapper.getViewers().forEach(x -> players.add(new SpigotPlayer((org.bukkit.entity.Player) x)));
        return players;
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

    @Override public void setItems(List<ItemStack> items){
        List<org.bukkit.inventory.ItemStack> stacks = new ArrayList<>();
        items.forEach(x -> stacks.add(SpigotUtil.prisonItemStackToBukkit(x)));
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
        return wrapper.contains(SpigotUtil.prisonItemStackToBukkit(itemStack),amount);
    }

    @Override public boolean contains(BlockType type) {
        return wrapper.contains(SpigotUtil.blockTypeToMaterial(type));
    }

    @Override public boolean contains(BlockType type, int amount) {
        return wrapper.contains(SpigotUtil.blockTypeToMaterial(type),amount);
    }

    @Override public Iterator<ItemStack> getIterator() {
        ArrayList<ItemStack> prisonStacks = new ArrayList<>();
        Arrays.asList(wrapper.getContents()).forEach(x -> prisonStacks.add(SpigotUtil.bukkitItemStackToPrison(x)));
        return prisonStacks.iterator();
    }

    @Override public List<ItemStack> getItems() {
        ArrayList<ItemStack> prisonStacks = new ArrayList<>();
        Arrays.asList(wrapper.getContents()).forEach(x -> prisonStacks.add(SpigotUtil.bukkitItemStackToPrison(x)));
        return prisonStacks;
    }

    @Override public HashMap<Integer, ItemStack> getItems(BlockType type) {
        HashMap<Integer,ItemStack> result = new HashMap<>();
        List<ItemStack> items = getItems();
        items.removeIf(x -> x.getMaterial() != type);
        items.forEach(y -> result.put(items.indexOf(y),y));
        return result;
    }

    @Override public HashMap<Integer, ItemStack> getItems(ItemStack stack) {
        HashMap<Integer,ItemStack> result = new HashMap<>();
        List<ItemStack> items = getItems();
        items.removeIf(x -> x != stack);
        items.forEach(y -> result.put(items.indexOf(y),y));
        return result;
    }

    @Override public ItemStack getItem(int index) {
        return SpigotUtil.bukkitItemStackToPrison(wrapper.getItem(index));
    }

    @Override public void addItem(ItemStack... itemStack) {
        ArrayList<org.bukkit.inventory.ItemStack> stacks = new ArrayList<>();
        for (ItemStack stack : itemStack){
            stacks.add(SpigotUtil.prisonItemStackToBukkit(stack));
        }
        wrapper.addItem((org.bukkit.inventory.ItemStack[]) stacks.toArray());
    }

    @Override public void removeItem(ItemStack... itemStack) {
        ArrayList<org.bukkit.inventory.ItemStack> stacks = new ArrayList<>();
        for (ItemStack stack : itemStack){
            stacks.add(SpigotUtil.prisonItemStackToBukkit(stack));
        }
        wrapper.removeItem((org.bukkit.inventory.ItemStack[]) stacks.toArray());
    }

    @Override public void clearAll() {
        wrapper.clear();
    }

    @Override public void clearAll(int index) {
        wrapper.setContents(Arrays.copyOfRange(wrapper.getContents(),0,index));
    }

    @Override public void clear(int index) {
        wrapper.clear(index);
    }

    @Override public void clear(BlockType type) {
        wrapper.remove(SpigotUtil.blockTypeToMaterial(type));
    }

    @Override public void clear(ItemStack stack) {
        wrapper.remove(SpigotUtil.prisonItemStackToBukkit(stack));
    }

    @Override public int first(ItemStack stack) {
        return wrapper.first(SpigotUtil.prisonItemStackToBukkit(stack));
    }

    @Override public int first(BlockType type) {
        return wrapper.first(SpigotUtil.blockTypeToMaterial(type));
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
