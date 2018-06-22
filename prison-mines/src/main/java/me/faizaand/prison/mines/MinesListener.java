package me.faizaand.prison.mines;

import me.faizaand.prison.Prison;
import me.faizaand.prison.events.EventPriority;
import me.faizaand.prison.events.EventType;
import me.faizaand.prison.internal.GameItemStack;
import me.faizaand.prison.internal.GamePlayer;
import me.faizaand.prison.internal.block.Block;
import me.faizaand.prison.internal.inventory.PlayerInventory;
import me.faizaand.prison.util.BlockType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Faizaan A. Datoo, Dylan M. Perks
 */
public class MinesListener {

    public MinesListener() {
        Prison.get().getEventManager().subscribe(EventType.BlockBreakEvent, new Class[]{GamePlayer.class, Block.class}, objects -> {
            GamePlayer player = ((GamePlayer) objects[0]);
            Block block = ((Block) objects[1]);

            if (PrisonMines.getInstance().getPlayerManager().hasAutosmelt(player)) {
                smelt(block.getDrops(((PlayerInventory) player.getInventory()).getItemInRightHand()));
            }
            if (PrisonMines.getInstance().getPlayerManager().hasAutopickup(player)) {
                player.getInventory()
                        .addItem(block.getDrops().toArray(new GameItemStack[]{}));
                block.setType(BlockType.AIR);
                return true;
            }
            if (PrisonMines.getInstance().getPlayerManager().hasAutoblock(player)) {
                block(player);
            }

            return false;
        }, EventPriority.HIGH);
    }

//  TODO Figure out selection system
//    @Subscribe
//    public void onSelectionComplete(SelectionCompletedEvent e) {
//        Bounds bounds = e.getSelection().asBounds();
//        String dimensions = bounds.getWidth() + "x" + bounds.getHeight() + "x" + bounds.getLength();
//        e.getPlayer().sendMessage("&3Ready. &7Your mine will be &8" + dimensions
//                + "&7 blocks. Type /mines create to create it.");
//    }

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
