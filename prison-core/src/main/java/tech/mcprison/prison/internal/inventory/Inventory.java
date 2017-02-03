package tech.mcprison.prison.internal.inventory;

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.util.BlockType;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Represents an inventory.
 * An inventory can belong to a player, a chest, or to nothing at all (i.e. as a GUI).
 *
 * @author Dylan M. Perks
 * @author Faizaan A. Datoo
 */
public interface Inventory extends Iterable<ItemStack> {

    /**
     * The size of the inventory. This will be a multiple of 9.
     *
     * @return The inventory's size.
     */
    int getSize();

    /**
     * Set the size of this inventory. This must be a multiple of 9.
     *
     * @param size The new size.
     * @throws IllegalArgumentException If the size is not a multiple of 9.
     */
    void setSize(int size);

    /**
     * The maximum size that an {@link ItemStack} can be in this inventory.
     *
     * @return The inventory's max stack size.
     */
    int getMaxStackSize();

    /**
     * Set the maximum size that an {@link ItemStack} can be in this inventory.
     *
     * @param size The inventory's new maximum stack size.
     */
    void setMaxStackSize(int size);

    /**
     * The name of this inventory, shown at the top.
     *
     * @return The inventory's name.
     */
    String getName();

    /**
     * Returns whether the inventory is empty or not.
     *
     * @return true if it's empty, false otherwise.
     */
    boolean isEmpty();

    /**
     * Returns whether the {@link ItemStack} is contained by the inventory.
     *
     * @param itemStack The {@link ItemStack} to look for. Note that the amount of this stack will
     *                  be taken into account. For non-amount-dependant searches, use {@link #contains(BlockType)}.
     * @return true if the inventory contains the item stack, false otherwise.
     */
    boolean contains(ItemStack itemStack);

    /**
     * Returns whether the {@link BlockType} is contained by the inventory.
     *
     * @param type The {@link BlockType} to look for.
     * @return true if the inventory contains the block type, false otherwise.
     */
    boolean contains(BlockType type);

    Iterator<ItemStack> getIterator();

    /**
     * Returns an array of all the items in this inventory.
     *
     * @return An array of {@link ItemStack}.
     */
    ItemStack[] getItems();

    HashMap<Integer, ItemStack> getItems(BlockType type);

    HashMap<Integer, ItemStack> getItems(ItemStack stack);

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
