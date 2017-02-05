package tech.mcprison.prison.internal.inventory;

import tech.mcprison.prison.internal.ItemStack;

/**
 * Created by DMP9 on 04/02/2017.
 */
public interface PlayerInventory extends Inventory {
    ItemStack[] getArmorContents();

    void setArmorContents(ItemStack[] items);

    ItemStack getBoots();

    void setBoots(ItemStack boots);

    ItemStack getChestplate();

    void setChestplate(ItemStack boots);

    int getHeldItemSlot();

    void setHeldItemSlot(int slot);

    ItemStack getHelmet();

    void setHelmet(ItemStack boots);

    ItemStack getLeggings();

    void setLeggings(ItemStack boots);

    ItemStack getItemInRightHand();

    void setItemInRightHand(ItemStack stack);

    ItemStack getItemInLeftHand();

    void setItemInLeftHand(ItemStack stack);
}
