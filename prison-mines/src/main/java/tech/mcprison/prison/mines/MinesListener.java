package tech.mcprison.prison.mines;

import com.google.common.eventbus.Subscribe;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.events.player.PlayerSuffocationEvent;
import tech.mcprison.prison.internal.events.world.PrisonWorldLoadEvent;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.tasks.MineTeleportWarmUpTask;
import tech.mcprison.prison.selection.SelectionCompletedEvent;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;
import tech.mcprison.prison.util.Bounds;

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

    
    @Subscribe
    public void onWorldLoadListener( PrisonWorldLoadEvent e ) {
    	
    	String worldName = e.getWorldName();
    	PrisonMines.getInstance().getMineManager().assignAvailableWorld( worldName );
    	
    }
    
    
    /**
     * <p>If a player is suffocating, and if they are within a mine, then based upon the config
     * settings, the play may not experience suffocation, and they may be teleported to
     * the mine's spawn location, or the center of the mine.
     * </p>
     * @param e
     */
    @Subscribe
    public void onPlayerSuffocationListener( PlayerSuffocationEvent e ) {
    	
    	Player player = e.getPlayer();
    	Mine mine = PrisonMines.getInstance().findMineLocation( player );

    	if ( mine != null ) {
    		
    		// If players can't be suffocated in mines, then cancel the suffocation event:
    		if ( !Prison.get().getPlatform().getConfigBooleanFalse( "prison-mines.enable-suffocation-in-mines" ) ) {
    		
    			e.setCanceled( true );
    		}    		
    		
	    		
	    	if ( Prison.get().getPlatform().getConfigBooleanTrue( "prison-mines.tp-to-spawn-on-mine-resets" ) ) {
	
	    		// Submit the teleport task to run in 3 ticks.  This will allow the suffocation
	    		// event to be canceled.  If the player moves then they don't need to be teleported
	    		// so it will be canceled.
	    		MineTeleportWarmUpTask mineTeleportWarmUp = new MineTeleportWarmUpTask( 
	    				player, mine, "spawn", 0.5 );
	    		mineTeleportWarmUp.setMessageSuccess( 
	    				"&7You have been teleported out of the mine to prevent suffocating." );
	    		mineTeleportWarmUp.setMessageFailed( null );
	    		
	    		PrisonTaskSubmitter.runTaskLater( mineTeleportWarmUp, 3 );
//    			mine.teleportPlayerOut( player );
	    	}

//    		
//    		
//    		// To "move" the player out of the mine, they are elevated by one block above the surface
//    		// so need to remove the glass block if one is spawned under them.  If there is no glass
//    		// block, then it will do nothing.
//    		mine.submitTeleportGlassBlockRemoval();
    		
//    		player.sendMessage( "&7You have been teleported out of the mine to prevent suffocating." );
    	}
    	else {
    		player.sendMessage( "&7You cannot be teleported to safety.  Good luck." );
    		
    	}
    }
    
    
//    /**
//     * Powertool helper
//     */
//    @Subscribe
//    public void onBlockBreak(BlockBreakEvent e) {
//        if (PrisonMines.getInstance().getPlayerManager().hasAutosmelt(e.getPlayer())) {
//            smelt(e.getBlockLocation().getBlockAt()
//                .getDrops(((PlayerInventory) e.getPlayer().getInventory()).getItemInRightHand()));
//        }
//        if (PrisonMines.getInstance().getPlayerManager().hasAutopickup(e.getPlayer())) {
//            e.getPlayer().getInventory()
//                .addItem(e.getBlockLocation().getBlockAt().getDrops().toArray(new ItemStack[]{}));
//            e.getBlockLocation().getBlockAt().setType(BlockType.AIR);
//            e.setCanceled(true);
//        }
//        if (PrisonMines.getInstance().getPlayerManager().hasAutoblock(e.getPlayer())) {
//            block(e.getPlayer());
//        }
//    }

//    private void smelt(List<ItemStack> drops) {
//        drops.replaceAll(x -> {
//            if (x.getMaterial() == BlockType.GOLD_ORE) {
//                return new ItemStack(x.getAmount(), BlockType.GOLD_INGOT);
//            } else if (x.getMaterial() == BlockType.IRON_ORE) {
//                return new ItemStack(x.getAmount(), BlockType.IRON_INGOT);
//            } else {
//                return x;
//            }
//        });
//    }

//    private void block(Player player) {
//        List<ItemStack> itemList = Arrays.asList(player.getInventory().getItems());
//        List<ItemStack> giveBack = new ArrayList<>();
//        itemList.replaceAll(x -> {
//            if (x != null) {
//                if (x.getMaterial() == BlockType.DIAMOND) {
//                    if (x.getAmount() % 9 > 0) {
//                        giveBack.add(new ItemStack(x.getAmount() % 9, x.getMaterial()));
//                    }
//                    return new ItemStack(x.getAmount() / 9, BlockType.DIAMOND_BLOCK);
//                } else if (x.getMaterial() == BlockType.EMERALD) {
//                    if (x.getAmount() % 9 > 0) {
//                        giveBack.add(new ItemStack(x.getAmount() % 9, x.getMaterial()));
//                    }
//                    return new ItemStack(x.getAmount() / 9, BlockType.EMERALD_BLOCK);
//                } else if (x.getMaterial() == BlockType.IRON_INGOT) {
//                    if (x.getAmount() % 9 > 0) {
//                        giveBack.add(new ItemStack(x.getAmount() % 9, x.getMaterial()));
//                    }
//                    return new ItemStack(x.getAmount() / 9, BlockType.IRON_BLOCK);
//                } else if (x.getMaterial() == BlockType.GLOWSTONE_DUST) {
//                    if (x.getAmount() % 4 > 0) {
//                        giveBack.add(new ItemStack(x.getAmount() % 9, x.getMaterial()));
//                    }
//                    return new ItemStack(x.getAmount() / 4, BlockType.GLOWSTONE);
//                } else if (x.getMaterial() == BlockType.GOLD_INGOT) {
//                    if (x.getAmount() % 9 > 0) {
//                        giveBack.add(new ItemStack(x.getAmount() % 9, x.getMaterial()));
//                    }
//                    return new ItemStack(x.getAmount() / 9, BlockType.GOLD_BLOCK);
//                } else if (x.getMaterial() == BlockType.COAL) {
//                    if (x.getAmount() % 9 > 0) {
//                        giveBack.add(new ItemStack(x.getAmount() % 9, x.getMaterial()));
//                    }
//                    return new ItemStack(x.getAmount() / 9, BlockType.BLOCK_OF_COAL);
//                } else if (x.getMaterial() == BlockType.REDSTONE) {
//                    if (x.getAmount() % 9 > 0) {
//                        giveBack.add(new ItemStack(x.getAmount() % 9, x.getMaterial()));
//                    }
//                    return new ItemStack(x.getAmount() / 9, BlockType.REDSTONE_BLOCK);
//                } else if (x.getMaterial() == BlockType.LAPIS_LAZULI) {
//                    return new ItemStack(x.getAmount() / 9, BlockType.LAPIS_LAZULI_BLOCK);
//                } else {
//                    return x;
//                }
//            } else {
//                return x;
//            }
//        });
//        player.getInventory().setItems(itemList);
//        player.getInventory().addItem((ItemStack[]) giveBack.toArray());
//    }

}
