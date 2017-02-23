package tech.mcprison.prison.spigot.inventory;

import org.bukkit.inventory.InventoryView;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.inventory.Inventory;
import tech.mcprison.prison.internal.inventory.Viewable;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.util.InventoryType;

/**
 * Currently undocumented.
 *
 * @author DMP9
 */
public class SpigotInventoryView implements Viewable {
    InventoryView wrapper;
    public SpigotInventoryView(InventoryView wrapper){
        this.wrapper = wrapper;
    }
    public InventoryView getWrapper(){
        return wrapper;
    }
    @Override public void close() {
        wrapper.close();
    }

    @Override public int convertSlot(int rawSlot) {
        return wrapper.convertSlot(rawSlot);
    }

    @Override public int countSlots() {
        return wrapper.countSlots();
    }

    @Override public Inventory getBottomInventory() {
        return new SpigotInventory(wrapper.getBottomInventory());
    }

    @Override public ItemStack getCursor() {
        return SpigotUtil.bukkitItemStackToPrison(wrapper.getCursor());
    }

    @Override public ItemStack getItem(int slot) {
        return SpigotUtil.bukkitItemStackToPrison(wrapper.getItem(slot));
    }

    @Override public Player getPlayer() {
        return new SpigotPlayer((org.bukkit.entity.Player) wrapper.getPlayer());
    }

    @Override public String getTitle() {
        return wrapper.getTitle();
    }

    @Override public Inventory getTopInventory() {
        return new SpigotInventory(wrapper.getTopInventory());
    }

    @Override public InventoryType getType() {
        return SpigotUtil.bukkitInventoryTypeToPrison(wrapper.getType());
    }

    @Override public void setCursor(ItemStack item) {
        wrapper.setCursor(SpigotUtil.prisonItemStackToBukkit(item));
    }

    @Override public void setItem(int slot, ItemStack item) {
        wrapper.setItem(slot,SpigotUtil.prisonItemStackToBukkit(item));
    }

    @Override public boolean setProperty(Property prop, int value) {
        return wrapper.setProperty(SpigotUtil.prisonPropertyToBukkit(prop),value);
    }
}
