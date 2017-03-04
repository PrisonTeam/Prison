package tech.mcprison.prison.internal.events.inventory;

import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.events.Cancelable;
import tech.mcprison.prison.internal.inventory.Viewable;

/**
 * Currently undocumented.
 *
 * @author DMP9
 */
public class InventoryInteractEvent extends InventoryEvent implements Cancelable {
    private boolean cancel = false;

    public InventoryInteractEvent(Viewable transaction) {
        super(transaction);
    }

    @Override public boolean isCanceled() {
        return cancel;
    }

    @Override public void setCanceled(boolean canceled) {
        cancel = canceled;
    }

    public Player getWhoClicked() {
        return transaction.getPlayer();
    }
}
