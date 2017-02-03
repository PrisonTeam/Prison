package tech.mcprison.prison.internal.inventory;

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.util.BlockType;

import java.util.HashMap;
import java.util.Iterator;

public interface Inventory extends Iterable<ItemStack> {

    int getSize();

    int getMaxStackSize();

    String getName();

    int setSize();

    int setMaxStackSize();

    boolean isEmpty();

    boolean contains(ItemStack... itemStack);

    boolean contains(ItemStack itemStack,int amount);

    boolean contains(BlockType type);

    boolean contains(BlockType type,int amount);

    Iterator<ItemStack> getIterator();

    ItemStack[] getItems();

    HashMap<Integer,ItemStack> getItems(BlockType type);

    HashMap<Integer,ItemStack> getItems(ItemStack stack);

    ItemStack getItem(int index);

    boolean addItem(ItemStack... itemStack);

    boolean removeItem(ItemStack... itemStack);

    void clearAll();

    void clearAll(int index);

    void clear(int index);

    void clear(BlockType type);

    void clear(ItemStack stack);

    int first(ItemStack stack);

    int first(BlockType type);

    int firstEmpty();

    InventoryHolder getHolder();
}