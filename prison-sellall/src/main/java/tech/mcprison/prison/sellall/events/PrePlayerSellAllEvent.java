package tech.mcprison.prison.sellall.events;

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.events.Cancelable;
import tech.mcprison.prison.internal.inventory.PlayerInventory;

/**
 * Represents an event called when a player is about to sell all of their items
 */
public class PrePlayerSellAllEvent implements Cancelable {

    private Player player;
    private PlayerInventory inventory;

    private boolean canceled = false;
    private String cancelReason = null;

    public PrePlayerSellAllEvent(Player player, PlayerInventory items) {
        super();
        this.player = player;
        this.inventory = items;
    }

    /**
     * Gets the player associated with this event
     *
     * @return the player associated with this event
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the inventory associated with this event
     *
     * @return the inventory associated with this event
     */
    public PlayerInventory getInventory() {
        return inventory;
    }

    /**
     * Checks to see if this event has been canceled
     *
     * @return true if this event has been canceled, false otherwise
     */
    public boolean isCanceled() {
        return canceled;
    }

    /**
     * Sets the canceled status of this event
     *
     * @param canceled the new canceled status of this event
     */
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

}
