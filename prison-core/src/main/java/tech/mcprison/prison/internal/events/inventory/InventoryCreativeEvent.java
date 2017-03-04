package tech.mcprison.prison.internal.events.inventory;

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.inventory.Viewable;
import tech.mcprison.prison.util.InventoryType;

/**
 * Currently undocumented.
 *
 * @author DMP9
 */
public class InventoryCreativeEvent extends InventoryClickEvent {

    ItemStack item;

    public InventoryCreativeEvent(Viewable transaction, InventoryType.SlotType type, int slot,
        ItemStack newItem) {
        super(transaction, type, slot, Click.CREATIVE, Action.PLACE_ALL);
        item = newItem;
    }

    public ItemStack getCursor() {
        return item;
    }

    public void setCursor(ItemStack item) {
        this.item = item;
    }
}
