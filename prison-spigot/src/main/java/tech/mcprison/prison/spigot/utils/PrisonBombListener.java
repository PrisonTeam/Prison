package tech.mcprison.prison.spigot.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.bombs.MineBombData;
import tech.mcprison.prison.bombs.MineBombs;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.block.OnBlockBreakMines;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.spigot.compat.Compatibility.EquipmentSlot;
import tech.mcprison.prison.spigot.compat.SpigotCompatibility;
import tech.mcprison.prison.spigot.game.SpigotLocation;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.game.entity.SpigotArmorStand;
import tech.mcprison.prison.spigot.nbt.PrisonNBTUtil;
import tech.mcprison.prison.util.Location;

/**
 * <p>This listener class handles the player's interaction with using a bomb.
 * 
 * </p>
 *
 * <p>Some of the inspiration for the handling of player's bomb was found in the
 * following open source project:
 * https://github.com/hpfxd/bombs/blob/master/src/main/java/xyz/tooger/bombs/BombListener.java
 * 
 * </p>
 */
public class PrisonBombListener
	implements Listener
{
	
	private PrisonUtilsMineBombs prisonUtilsMineBombs;
	
	private OnBlockBreakMines blockBreakMines;
	
	public PrisonBombListener( PrisonUtilsMineBombs utilsMineBombs ) {
		super();
		
		this.prisonUtilsMineBombs = utilsMineBombs;
		
		this.blockBreakMines = new OnBlockBreakMines();
	}
	
	/**
	 * <p>This is the interactive event of when a player places a bomb by 
	 * right-clicking on a block within the mine.
	 * </p>
	 * 
	 * @param event
	 */
	@EventHandler( priority = EventPriority.LOW )
	public void mineBombPlacementEvent( BlockPlaceEvent event ) {
		
		ItemStack iStack = event.getItemInHand();
		
		if ( iStack != null && iStack.getType() != Material.AIR ) {
			
        	
        	String bombName = checkMineBombItemStack( iStack );
        	
        	if ( bombName != null ) {
        		
        		Player player = event.getPlayer();
        		SpigotPlayer sPlayer = new SpigotPlayer( player );
        		
        		MineBombData mineBomb = MineBombs.getInstance().findBombByName( sPlayer, bombName );
        		
        		
        		PrisonNBTUtil.setNBTString(iStack, 
		        				MineBombs.MINE_BOMBS_NBT_KEY, 
		        				bombName );
        		PrisonNBTUtil.setNBTString(iStack, 
        						MineBombs.MINE_BOMBS_NBT_THROWER_UUID, 
        						player.getUniqueId().toString() );
        		
        		Block targetBlock = event.getBlockAgainst();
        		
        		SpigotBlock lsBlock = sPlayer.getLineOfSightBlock();
        		
        		Output.get().logInfo( 
        				"### Place mine bomb: mineBombPlacementEvent: block: %s  sight: %s", 
        				targetBlock.getLocation().toString(),
        				lsBlock.getLocation().toString() );
        		
        		EquipmentSlot hand = SpigotCompatibility.getInstance().getHand(event);
        		
        		boolean canceled = processBombTriggerEvent( player, mineBomb, 
        				targetBlock, hand );
        		
        		if ( canceled ) {
        			event.setCancelled( canceled );
        		}
        	}
			
		}
		
	}
	
	/**
	 * <p>Throwing mine bombs.
	 * </p>
	 * 
	 * https://www.spigotmc.org/threads/tutorial-how-to-throw-items-like-knives-1-18-2.561482/
	 * 
	 * @param event
	 */
	@EventHandler( priority = EventPriority.LOW )
    public void mineBombThrowEvent( PlayerInteractEvent event ) {
		
		ItemStack iStack = event.getItem();
		Action action = event.getAction(); // This is how we tell if it's a right-click
	
		
		if ( iStack != null && action != null &&
				iStack.getType() != Material.AIR && 
				action == Action.RIGHT_CLICK_AIR ) {
			

			String bombName = checkMineBombItemStack( iStack );
        	
        	if ( bombName != null ) {
        		
        		// We do not know if the bomb will be in a mine, but we know 
        		// it's a mine bomb so no other listener should handle it.
        		event.setCancelled( true );
        		
        		Player player = event.getPlayer();
        		SpigotPlayer sPlayer = new SpigotPlayer( player );
        		
        		// If the player is on a mine bomb cooldown, then mineBomb will be null:
        		MineBombData mineBomb = MineBombs.getInstance().findBombByName( sPlayer, bombName );
        		
        		if ( mineBomb != null ) {
        			
        			EquipmentSlot hand = SpigotCompatibility.getInstance().getHand(event);
        			
        			PrisonNBTUtil.setNBTString(iStack, 
        					MineBombs.MINE_BOMBS_NBT_KEY, 
        					bombName );
        			PrisonNBTUtil.setNBTString(iStack, 
        					MineBombs.MINE_BOMBS_NBT_THROWER_UUID, 
        					player.getUniqueId().toString() );
        			
        			SpigotLocation loc = new SpigotLocation( 
        					player.getEyeLocation().add( player.getLocation().getDirection() ));
        			
//        		XMaterial xMat = XMaterial.matchXMaterial(iStack);
        			
        			
        			ArmorStand armorStand = player.getWorld().spawn( 
        					loc.getBukkitLocation(), 
        					ArmorStand.class);
        			
        			armorStand.setVisible( false );
        			armorStand.setItemInHand( iStack );
        			
        			
        			SpigotArmorStand sArmorStand = new SpigotArmorStand(armorStand);
        			sArmorStand.setNbtString( MineBombs.MINE_BOMBS_NBT_KEY, bombName );
        			sArmorStand.setNbtString( MineBombs.MINE_BOMBS_NBT_THROWER_UUID, 
        					player.getUniqueId().toString() );
        			
        			
        			Vector velocity = player.getLocation().getDirection().multiply( 
        					mineBomb.getThrowVelocity() );
        			
        			armorStand.setVelocity( velocity );
        			
        			armorStand.setRemoveWhenFarAway(false);
        			
        			
        			
        			
        			// Remove the unused armorStand in 120 seconds:
        			final int taskId = SpigotPrison.getInstance().getScheduler()
        					.runTaskLater( 
        							new Runnable() {
        								public void run() {
        									armorStand.remove();
        								}
        							}, 120 * 20);
        			
        			
        			// Run the following every tick to detect when the armor stand has
        			// come to rest for 4 ticks, then trigger the animation.
        			final SpigotArmorStand aStnd = sArmorStand;
        			
        			
        			// Save the taskId so this task can be canceled:
        			mineBomb.setTaskId(
        					SpigotPrison.getInstance().getScheduler()
        					.runTaskTimer(
        							new Runnable() {
        								private List<Location> locs = new ArrayList<>();
        								
        								public void run() {
        									Location loc = aStnd.getLocation();
        									locs.add(loc);
        									
        									if ( locs.size() > 4 ) {
        										locs.remove(0);
        									}
        									
        									if ( locs.size() == 4 ) {
        										if ( locs.get(0).equals(locs.get(1)) && 
        												locs.get(1).equals(locs.get(2)) &&
        												locs.get(2).equals(locs.get(3))
        												) {
        											
        											// Set the mine bomb's location where the item has landed:
        											mineBomb.setPlacedBombLocation( loc );
        											
        											// Cancel this task:
        											SpigotPrison.getInstance().getScheduler().cancelTask( mineBomb.getTask() );
        											
        											// Cancel the cancellation task:
        											SpigotPrison.getInstance().getScheduler().cancelTask(taskId);
        											
        											
        											// remove armor stand:
        											aStnd.remove();
        											
        											// Start mine bomb animation:
//        											loc.setY( loc.getY() );
        											SpigotBlock targetBlock = (SpigotBlock) loc.getBlockAt();
        											
        											Mine mine = getMine(sPlayer, mineBomb, targetBlock );
        											
        											if ( mine != null ) {
        												// It landed within a mine... subtract one from the player's inventory,
        												// if it's used up, then remove from the inventory:
//    								iStack.setAmount( iStack.getAmount() - 1);
//    								if ( iStack.getAmount() == 0 ) {
//    									player.getInventory().removeItem( iStack );
//    								}
        												
        												processBombTriggerEvent( sPlayer, mine, mineBomb, targetBlock, hand );
        											}
        											
        										}
        									}
        								}
        								
        							}, 2, 1));
        		}
        		else {
        			int cooldownTicks = MineBombs.checkPlayerCooldown(sPlayer);
        			
        			getPrisonUtilsMineBombs().mineBombsCoolDownMsg( sPlayer, cooldownTicks );
        		}
        		
    			

    			
    			
    			
//    			player.launchProjectile( armorStand, velocity );
    			
//    			ThrowableProjectile tProj = new Throw
    			
    					
//    			ProjectileSource pSrc = new Projec
//    			Projectile proj ;
//    			proj.set
        		
        		
//        		Snowball snowball = player.getWorld().spawn(loc.getBukkitLocation(), Snowball.class);
//        		snowball.setPassenger( armorStand );
//        		Snowball snowball = player.getWorld().spawn(loc.getBukkitLocation(), Snowball.class);
        		
        		// the '.setItem()' was introduced with Spigot v1.14.x
                //snowball.setItem(iStack); // uses the exact item clicked

//                snowball.setVelocity(player.getLocation().getDirection().multiply(3));
                
                //iStack.setAmount(iStack.getAmount()-1);
        		
        		
        	}
		}
    }
	


	/**
	 * <p>This function will initiate the placement of a mine bomb. 
	 * This handles the right-clicking on a block.  This will not
	 * throw it.
	 * </p>
	 * @param event
	 */
	@EventHandler( priority = EventPriority.LOW )
    public void bombPlacementEvent( PlayerInteractEvent event ) {
//        if ( !event.getPlayer().hasPermission("prison.minebombs.use") ) {
//        	return;
//        }

        //Output.get().logInfo( "### PrisonBombListener: PlayerInteractEvent  01 " );
        
		ItemStack iStack = event.getItem();
    	
        if ( iStack != null && iStack.getType() != Material.AIR &&
        		event.getAction().equals(Action.RIGHT_CLICK_BLOCK) ) {
        	
        	// If the player is holding a mine bomb, then get the bomb and decrease the
        	// ItemStack in the player's hand by 1:
        	
        	
        	String bombName = checkMineBombItemStack( iStack );
        	
        	if ( bombName != null ) {
        		
        		// We do not know if the bomb will be in a mine, but we know 
        		// it's a mine bomb so no other listener should handle it.
        		event.setCancelled( true );
        		
        		Player player = event.getPlayer();
        		SpigotPlayer sPlayer = new SpigotPlayer( player );
        		
        		// If the player is on a mine bomb cooldown, then mineBomb will be null:
        		MineBombData mineBomb = MineBombs.getInstance().findBombByName( sPlayer, bombName);
        		
        		if ( mineBomb != null ) {
        			
        			//String ownerUUID = PrisonNBTUtil.getNBTString( iStack, MineBombs.MINE_BOMBS_NBT_OWNER_UUID );
        			
        			Block targetBlock = event.getClickedBlock();
        			
            		
            		SpigotBlock lsBlock = sPlayer.getLineOfSightBlock();
            		
            		Location losLoc = sPlayer.getLineOfSightExactLocation();
            		
            		// The placedBombLocation will be used as the center point of the animation:
            		mineBomb.setPlacedBombLocation( losLoc );
            		
            		Output.get().logInfo( 
            				"### Place mine bomb: bombPlacementEvent: block: %s  "
            								+ "sight: %s  LineOfSight: %s ", 
            				new SpigotLocation( targetBlock.getLocation()).toString(),
            				lsBlock.getLocation().toString(),
            				(losLoc == null ? "null" : losLoc.toString()) );

            		if ( losLoc != null ) {
            			lsBlock = (SpigotBlock) losLoc.getBlockAt();
            			
            			targetBlock = lsBlock.getWrapper();
            		}
            		
            		
        			
        			EquipmentSlot hand = SpigotCompatibility.getInstance().getHand(event);
        			
        			processBombTriggerEvent( player, mineBomb, targetBlock, hand );
        			
        		}
        		else {
        			int cooldownTicks = MineBombs.checkPlayerCooldown(sPlayer);
        			
        			getPrisonUtilsMineBombs().mineBombsCoolDownMsg( sPlayer, cooldownTicks );
        		}
        		
        	}
        }
    }

	private String checkMineBombItemStack( ItemStack iStack ) {
		// Check to see if this is an mine bomb by checking the NBT key-value pair,
		// which will also identify which mine bomb it is too.
		// NOTE: Because we're just checking, do not auto update the itemstack.
//        	NBTItem nbtItem = new NBTItem( event.getItem() );
		
		String bombName = PrisonNBTUtil.getNBTString( iStack, MineBombs.MINE_BOMBS_NBT_KEY );
//		String ownerUUID = PrisonNBTUtil.getNBTString( iStack, MineBombs.MINE_BOMBS_NBT_OWNER_UUID );
		
		if ( bombName != null && bombName.trim().length() == 0 ) {
			bombName = null;
		}
		else if ( bombName != null ){
			
//			if ( Output.get().isDebug() ) {
//				Output.get().logInfo( "PrisonBombListener.onInteract (item) "
//						+ "bombName: &7%s&r &3::  nbt: &r\\Q%s\\E", 
//						bombName, 
//						PrisonNBTUtil.nbtDebugString( iStack ).replace("%", "_")
////        				(nbtItem == null ? "&a-no-nbt-" : nbtItem.toString()) 
//						);
//			}
		}
		

		return bombName;
	}

//    private boolean processBombTriggerEvent( SpigotPlayer sPlayer,
//    		Mine mine,
//			MineBombData mineBomb, SpigotBlock sBlock) {
//
//    	EquipmentSlot hand = null;
//    	
//    	return mine == null ? false : processBombTriggerEvent( sPlayer, mine, mineBomb, sBlock, hand );
//	}
	private boolean processBombTriggerEvent( Player player, 
					MineBombData mineBomb, Block targetBlock,
					EquipmentSlot hand ) {
		
		SpigotPlayer sPlayer = new SpigotPlayer( player );
		
		SpigotBlock sBlock = null;
		
//		SpigotPlayer sPlayer = new SpigotPlayer( player );
		
		// If clicking AIR, then event.getClickedBlock() will be null...
		// so if null, then use the player's location for placing the bomb.
		if ( targetBlock == null ) {
			Location loc = sPlayer.getLocation();
			
			// Get the block 3 away from the player, in the direction (vector) in which
			// the player is looking.
			sBlock = (SpigotBlock) loc.add( loc.getDirection().multiply( 3 ) ) .getBlockAt();
		}
		else {
			sBlock = SpigotBlock.getSpigotBlock( targetBlock );
		}
		
		Mine mine = getMine(sPlayer, mineBomb, sBlock );
		
		return mine == null ? false : processBombTriggerEvent( sPlayer, mine, mineBomb, sBlock, hand );
	}
	
	private boolean processBombTriggerEvent( SpigotPlayer sPlayer, 
				Mine mine,
				MineBombData mineBomb, 
				SpigotBlock sBlock, 
				EquipmentSlot hand ) {
		
		boolean canceled = false;
		
		if ( getPrisonUtilsMineBombs().setBombInHand( sPlayer, mineBomb, sBlock, hand ) ) {
			
			// The item was a bomb and it was activated.
			// Cancel the event so the item will not be placed or processed farther.
			
//    		Output.get().logInfo( "### PrisonBombListener: PlayerInteractEvent  03 Bomb detected - May not have been set. " );
			canceled = true;
		}
		
		return canceled;
	}

	/**
	 * <p>This function checks to see if the mine bomb is within a mine, and if it is, 
	 * then it checks to see if the player has access to the mine.  Then finally, 
	 * the mine bomb is checked to see if it is allowed to be used within the mine.
	 * </p>
	 * 
	 * <p>If the player is permitted to use the mine bomb in the mine, then the mine will
	 * be returned.  If they are not permitted, then a null will be returned.
	 * </p>
	 * 
	 * @param sPlayer
	 * @param mineBomb
	 * @param sBlock
	 * @return
	 */
	private Mine getMine(SpigotPlayer sPlayer, MineBombData mineBomb, SpigotBlock sBlock ) {
		Mine mine = blockBreakMines.findMine( sPlayer, sBlock, null, null);
		
		if ( mine == null ) {
			// player is not in a mine, so do not allow them to trigger a mine bomb:
			
			if ( Output.get().isDebug() ) {
				String msg = "MineBombs: Cannot use mine bombs use outside of mines.";
				sPlayer.setActionBar( msg );
//				Output.get().logInfo( msg );
			}
			
		}
		else if ( !mine.hasMiningAccess( sPlayer ) ) {
			// Player does not have access to the mine, so don't allow them to trigger a mine bomb:
			
			if ( Output.get().isDebug() ) {
				String msg = String.format(
						"MineBombs: %s&r, you do not have access to mine %s&r.",
						sPlayer.getName(), mine.getName() );
				sPlayer.setActionBar( msg );
//				Output.get().logInfo( msg );
			}
			
			mine = null;
		}
		else {
			
			HashSet<String> allowedMines = new HashSet<>( mineBomb.getAllowedMines() );
			HashSet<String> preventedMines = new HashSet<>( mineBomb.getPreventedMines() );
			List<String> globalPreventedMines = (List<String>) Prison.get().getPlatform()
					.getConfigStringArray("prison-mines.mine-bombs.prevent-usage-in-mines");
			preventedMines.addAll( globalPreventedMines );
			
			// Skip prevent-in-mines check if mine is within the allowedMines list:
			if ( !allowedMines.contains( mine.getName().toLowerCase() ) ) {
				
				if ( preventedMines.contains( mine.getName().toLowerCase() ) ) {
					
					// Mine bombs are not allowed to be used in this mine so cancel:
					
					String msg = String.format(
							"&3You cannot use the &7%s &3bomb in mine &7%s.&r",
							mineBomb.getNameTag(),
							mine.getTag()
							);
					sPlayer.setActionBar( msg );
//					sPlayer.sendMessage( msg );
					
					mine = null;
				}
			}
		}
		
		return mine;
	}
    

//    @EventHandler( priority = EventPriority.HIGHEST, ignoreCancelled = false )
    public void onBlockPlace( PlayerDropItemEvent event ) {

    	//Output.get().logInfo( "### PrisonBombListener: PlayerDropItemEvent " );
        
        if ( event.isCancelled() && event.getItemDrop().hasMetadata( "prisonMineBomb" ) ) {
        	
//        	event.getItemDrop().getMetadata( "prisonMineBomb" );
        	
//        	event.getItemDrop().

        	event.setCancelled( false );
        }
        
    }
    
//    @EventHandler( priority = EventPriority.HIGHEST, ignoreCancelled = false )
//    public void onBlockPlace2( PlayerInteractEvent event ) {
//
//    	Output.get().logInfo( "### PrisonBombListener: PlayerInteractEvent - oof" );
//    }
    
////    @EventHandler( priority = EventPriority.HIGHEST, ignoreCancelled = false )
//    public void onBlockPlace3( BlockPlaceEvent event ) {
//    	
//    	Output.get().logInfo( "### PrisonBombListener: BlockPlaceEvent  HIGHEST  isCanceled= " + event.isCancelled() );
//
////    	event.getBlockPlaced();
//    	
//    	
//    	event.setBuild( true );
//    	event.setCancelled( false );
//    	
//    	ItemStack item = event.getItemInHand();
//    	
//    	
//    	if ( item.hasItemMeta() && item.getItemMeta().hasDisplayName() ) {
//    		ItemMeta meta = item.getItemMeta();
//    		
//    		Output.get().logInfo( "### PrisonBombListener: BlockPlaceEvent  " + meta.getDisplayName() );
//    		
////    		meta.getCustomTagContainer().hasCustomTag( null, null )
//    		
////    		meta.
////    		
////    		Multimap<Attribute, AttributeModifier> attributes = meta.getAttributeModifiers();
////    		
////    		
////    		for ( String attri : attributes. ) {
////    			
////    		}
//    		
//    	}
//    }
    
////    @EventHandler( priority = EventPriority.HIGHEST, ignoreCancelled = false )
//    public void onBlockPlace3( PlayerInteractEvent event ) {
//    	
//    	Output.get().logInfo( "### PrisonBombListener: PlayerInteractEvent " );
//    	
//    	
//    }

	public PrisonUtilsMineBombs getPrisonUtilsMineBombs() {
		return prisonUtilsMineBombs;
	}
	public void setPrisonUtilsMineBombs( PrisonUtilsMineBombs prisonUtilsMineBombs ) {
		this.prisonUtilsMineBombs = prisonUtilsMineBombs;
	}

//    @EventHandler( priority = EventPriority.HIGHEST, ignoreCancelled = false )
//    public void onBlockPlace3( BlockDropItemEvent event ) {
//    	
//    	Output.get().logInfo( "### PrisonBombListener: PlayerInteractEvent " );
//    	
//    	
//    }
    
//    @EventHandler( priority = EventPriority.HIGHEST )
//    public void onBlockPlace2( Player event ) {
//    	
//    	
//    	if ( event.isCancelled() && event.getItemDrop().hasMetadata( "prisonMineBomb" ) ) {
//    		
////        	event.getItemDrop().getMetadata( "prisonMineBomb" );
//    		
////        	event.getItemDrop().
//    		
//    		event.setCancelled( false );
//    	}
//    	
//    }
    
    
}
