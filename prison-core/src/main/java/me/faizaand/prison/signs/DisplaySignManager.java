package me.faizaand.prison.signs;

import me.faizaand.prison.Prison;
import me.faizaand.prison.events.EventPriority;
import me.faizaand.prison.events.EventType;
import me.faizaand.prison.internal.GamePlayer;
import me.faizaand.prison.internal.block.Block;
import me.faizaand.prison.output.Output;
import me.faizaand.prison.util.BlockType;

public class DisplaySignManager {

    private static DisplaySignManager ourInstance = new DisplaySignManager();

    public static DisplaySignManager getInstance() {
        return ourInstance;
    }


    private DisplaySignManager() {
        listenForSignPlace();
    }

    /*
     * TODO CHECK TO SEE IF SIGNCHANGEEVENT DEALS WITH CREATING/DESTROYING!
     */

    private void listenForSignPlace() {
        Prison.get().getEventManager().subscribe(EventType.BlockPlaceEvent, objects -> {
            Block block = (Block) objects[0];
            GamePlayer player = (GamePlayer) objects[1];

            if(weDontCareAboutEvent(block, player)) {
                return new Object[] {}; // validation failed, end now
            }
            // todo

            return new Object[]{};
        }, EventPriority.NORMAL);
    }

    private void listenForSignBreak() {
        Prison.get().getEventManager().subscribe(EventType.BlockBreakEvent, objects -> {
            Block block = (Block) objects[0];
            GamePlayer player = (GamePlayer) objects[1];

            if(weDontCareAboutEvent(block, player)) {
                return new Object[] {}; // validation failed, end now
            }

            // todo

            return new Object[]{};
        }, EventPriority.NORMAL);
    }

    /**
     * Ensure that this event is one that we're concerned about (i.e. player has permission to make display signs,
     * and the block is a sign).
     */
    private boolean weDontCareAboutEvent(Block block, GamePlayer player) {
        // We make sure that we're dealing with a sign
        if (block.getType() != BlockType.STANDING_SIGN_BLOCK && block.getType() != BlockType.WALL_MOUNTED_SIGN_BLOCK) {
            return true;
        }

        // Make sure
        if(!player.hasPermission("prison.signs")) {
            // todo localize
            Output.get().sendError(player, "You lack the necessary permissions to alter signs.");
            return true;
        }

        return false;
    }

}
