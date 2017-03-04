package tech.mcprison.prison.internal.events.inventory;

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.events.Cancelable;

/**
 * Currently undocumented.
 *
 * @author DMP9
 */
public class FurnaceBurnEvent implements Cancelable {
    boolean canceled = false;
    int burnTime = -1;
    ItemStack fuel = null;
    boolean burning = false;
    Block block;

    public FurnaceBurnEvent(Block furnace, ItemStack fuel, int burnTime, boolean burning){
        block = furnace;
        this.fuel = fuel;
        this.burnTime = burnTime;
        this.burning = burning;
    }

    public int getBurnTime() {
        return burnTime;
    }

    public ItemStack getFuel() {
        return fuel;
    }

    public boolean isBurning() {
        return burning;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setBurning(boolean burning) {
        this.burning = burning;
    }

    public void setBurnTime(int burnTime) {
        this.burnTime = burnTime;
    }

    public void setCanceled(boolean cancel) {
        canceled = cancel;
    }

    public Block getBlock() {return block;}
}
