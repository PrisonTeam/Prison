package tech.mcprison.prison.internal.events.inventory;

import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.events.Cancelable;
import tech.mcprison.prison.internal.inventory.BrewerInventory;

/**
 * Currently undocumented.
 *
 * @author DMP9
 */
public class BrewEvent implements Cancelable {
    private boolean canceled = false;
    private BrewerInventory contents = null;
    private int fuelLevel = 0;
    private Block block = null;

    public BrewEvent(Block block, BrewerInventory contents, int fuelLevel){
        this.contents = contents;
        this.fuelLevel = fuelLevel;
        this.block = block;
    }

    public BrewerInventory getContents() {
        return contents;
    }

    public int getFuelLevel() {
        return fuelLevel;
    }

    public Block getBlock(){
        return block;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean cancel) {
        canceled = cancel;
    }
}
