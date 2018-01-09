package tech.mcprison.prison.mines;

import com.google.common.eventbus.Subscribe;
import java.util.List;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.events.block.BlockBreakEvent;
import tech.mcprison.prison.internal.events.player.PlayerJoinEvent;
import tech.mcprison.prison.internal.inventory.PlayerInventory;
import tech.mcprison.prison.selection.SelectionCompletedEvent;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.Bounds;

/**
 * @author Faizaan A. Datoo
 */
public class MinesListener {

    @Subscribe public void onSelectionComplete(SelectionCompletedEvent e) {
        Bounds bounds = e.getSelection().asBounds();
        String dimensions = bounds.getWidth() + "x" + bounds.getHeight() + "x" + bounds.getLength();
        e.getPlayer().sendMessage("&3Ready. &7Your mine will be &8" + dimensions
            + "&7 blocks. Type /mines create to create it.");
    }

    /**
     * Powertool helper
     */
    @Subscribe public void onBlockBreak(BlockBreakEvent e){
         if (PrisonMines.getInstance().getPlayerManager().hasAutosmelt(e.getPlayer())){
             smelt(e.getBlockLocation().getBlockAt().getDrops(((PlayerInventory)e.getPlayer().getInventory()).getItemInRightHand()));
         }
    }

    private void smelt(List<ItemStack> drops){
         drops.replaceAll(x -> {if (x.getMaterial() == BlockType.COAL_ORE){return new ItemStack(x.getAmount(),BlockType.COAL);}else{return x;}});
    }

}
