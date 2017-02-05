package tech.mcprison.prison.internal.events;

import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.Location;

/**
 * Created by DMP9 on 23/01/2017.
 */
public class BlockBreakEvent implements Cancelable {

    private BlockType block;
    private Location blockLocation;
    private Player player;
    private boolean canceled = false;

    public BlockBreakEvent(BlockType block, Location blockLocation, Player player) {
        this.block = block;
        this.blockLocation = blockLocation;
        this.player = player;
    }

    @Override public boolean isCanceled() {
        return canceled;
    }

    @Override public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public BlockType getBlock() {
        return block;
    }

    public Location getBlockLocation() {
        return blockLocation;
    }

    public Player getPlayer() {
        return player;
    }
}
