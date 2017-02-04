package tech.mcprison.prison.internal.inventory;

import tech.mcprison.prison.internal.ItemStack;

/**
 * Created by DMP9 on 04/02/2017.
 */
public interface PlayerInventory extends Inventory {
    ItemStack[]	getArmorContents();

    ItemStack getBoots();

    ItemStack getChestplate();

    int	getHeldItemSlot();

    ItemStack getHelmet();

    ItemStack getLeggings();

    void setArmorContents(ItemStack[] items);

    ItemStack getItemInRightHand();

    ItemStack getItemInLeftHand();

    void setBoots(ItemStack boots);

    void setChestplate(ItemStack boots);

    void setLeggings(ItemStack boots);

    void setHelmet(ItemStack boots);

    void setItemInLeftHand(ItemStack stack);

    void setItemInRightHand(ItemStack stack);

    void setHeldItemSlot(int slot);
}
