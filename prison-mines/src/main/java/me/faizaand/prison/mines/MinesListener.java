package me.faizaand.prison.mines;

import com.google.common.eventbus.Subscribe;
import me.faizaand.prison.internal.GameItemStack;
import me.faizaand.prison.internal.GamePlayer;
import me.faizaand.prison.internal.events.block.BlockBreakEvent;
import me.faizaand.prison.internal.inventory.PlayerInventory;
import me.faizaand.prison.selection.SelectionCompletedEvent;
import me.faizaand.prison.util.BlockType;
import me.faizaand.prison.util.Bounds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Faizaan A. Datoo, Dylan M. Perks
 */
public class MinesListener {

    @Subscribe
    public void onSelectionComplete(SelectionCompletedEvent e) {
        Bounds bounds = e.getSelection().asBounds();
        String dimensions = bounds.getWidth() + "x" + bounds.getHeight() + "x" + bounds.getLength();
        e.getPlayer().sendMessage("&3Ready. &7Your mine will be &8" + dimensions
            + "&7 blocks. Type /mines create to create it.");
    }

    /**
     * Powertool helper
     */
    @Subscribe
    public void onBlockBreak(BlockBreakEvent e) {
        if (PrisonMines.getInstance().getPlayerManager().hasAutosmelt(e.getPlayer())) {
            smelt(e.getBlockLocation().getBlockAt()
                .getDrops(((PlayerInventory) e.getPlayer().getInventory()).getItemInRightHand()));
        }
        if (PrisonMines.getInstance().getPlayerManager().hasAutopickup(e.getPlayer())) {
            e.getPlayer().getInventory()
                .addItem(e.getBlockLocation().getBlockAt().getDrops().toArray(new GameItemStack[]{}));
            e.getBlockLocation().getBlockAt().setType(BlockType.AIR);
            e.setCanceled(true);
        }
        if (PrisonMines.getInstance().getPlayerManager().hasAutoblock(e.getPlayer())) {
            block(e.getPlayer());
        }
    }

    private void smelt(List<GameItemStack> drops) {
        drops.replaceAll(x -> {
            if (x.getMaterial() == BlockType.GOLD_ORE) {
                return new GameItemStack(x.getAmount(), BlockType.GOLD_INGOT);
            } else if (x.getMaterial() == BlockType.IRON_ORE) {
                return new GameItemStack(x.getAmount(), BlockType.IRON_INGOT);
            } else {
                return x;
            }
        });
    }

    private void block(GamePlayer player) {
        List<GameItemStack> itemList = Arrays.asList(player.getInventory().getItems());
        List<GameItemStack> giveBack = new ArrayList<>();
        itemList.replaceAll(x -> {
            if (x != null) {
                if (x.getMaterial() == BlockType.DIAMOND) {
                    if (x.getAmount() % 9 > 0) {
                        giveBack.add(new GameItemStack(x.getAmount() % 9, x.getMaterial()));
                    }
                    return new GameItemStack(x.getAmount() / 9, BlockType.DIAMOND_BLOCK);
                } else if (x.getMaterial() == BlockType.EMERALD) {
                    if (x.getAmount() % 9 > 0) {
                        giveBack.add(new GameItemStack(x.getAmount() % 9, x.getMaterial()));
                    }
                    return new GameItemStack(x.getAmount() / 9, BlockType.EMERALD_BLOCK);
                } else if (x.getMaterial() == BlockType.IRON_INGOT) {
                    if (x.getAmount() % 9 > 0) {
                        giveBack.add(new GameItemStack(x.getAmount() % 9, x.getMaterial()));
                    }
                    return new GameItemStack(x.getAmount() / 9, BlockType.IRON_BLOCK);
                } else if (x.getMaterial() == BlockType.GLOWSTONE_DUST) {
                    if (x.getAmount() % 4 > 0) {
                        giveBack.add(new GameItemStack(x.getAmount() % 9, x.getMaterial()));
                    }
                    return new GameItemStack(x.getAmount() / 4, BlockType.GLOWSTONE);
                } else if (x.getMaterial() == BlockType.GOLD_INGOT) {
                    if (x.getAmount() % 9 > 0) {
                        giveBack.add(new GameItemStack(x.getAmount() % 9, x.getMaterial()));
                    }
                    return new GameItemStack(x.getAmount() / 9, BlockType.GOLD_BLOCK);
                } else if (x.getMaterial() == BlockType.COAL) {
                    if (x.getAmount() % 9 > 0) {
                        giveBack.add(new GameItemStack(x.getAmount() % 9, x.getMaterial()));
                    }
                    return new GameItemStack(x.getAmount() / 9, BlockType.BLOCK_OF_COAL);
                } else if (x.getMaterial() == BlockType.REDSTONE) {
                    if (x.getAmount() % 9 > 0) {
                        giveBack.add(new GameItemStack(x.getAmount() % 9, x.getMaterial()));
                    }
                    return new GameItemStack(x.getAmount() / 9, BlockType.REDSTONE_BLOCK);
                } else if (x.getMaterial() == BlockType.LAPIS_LAZULI) {
                    return new GameItemStack(x.getAmount() / 9, BlockType.LAPIS_LAZULI_BLOCK);
                } else {
                    return x;
                }
            } else {
                return x;
            }
        });
        player.getInventory().setItems(itemList);
        player.getInventory().addItem((GameItemStack[]) giveBack.toArray());
    }

}
