package tech.mcprison.prison.spigot.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.tr7zw.nbtapi.NBTItem;
import tech.mcprison.prison.bombs.MineBombData;
import tech.mcprison.prison.bombs.MineBombs;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.block.OnBlockBreakMines;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
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

    @EventHandler( priority = EventPriority.LOW )
    public void onInteract( PlayerInteractEvent event ) {
//        if ( !event.getPlayer().hasPermission("prison.minebombs.use") ) {
//        	return;
//        }

        //Output.get().logInfo( "### PrisonBombListener: PlayerInteractEvent  01 " );
        
    	
        if ( (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || 
        		event.getAction().equals(Action.RIGHT_CLICK_AIR)) && 
        		event.getItem() != null && event.getItem().getType() != Material.AIR ) {
        	
        	// If the player is holding a mine bomb, then get the bomb and decrease the
        	// ItemStack in the player's hand by 1:
        	
        	
        	// Check to see if this is an mine bomb by checking the NBT key-value pair,
        	// which will also identify which mine bomb it is too.
        	// NOTE: Because we're just checking, do not auto update the itemstack.
        	NBTItem nbtItem = new NBTItem( event.getItem() );
        	
			if ( Output.get().isDebug() && nbtItem != null ) {
				Output.get().logInfo( "PrisonBombListener.onInteract ntb: %s", nbtItem.toString() );
			}
        	
        	if ( !nbtItem.hasKey( MineBombs.MINE_BOMBS_NBT_BOMB_KEY ) ) {
        		return;
        	}
        	
        	String bombName = nbtItem.getString( MineBombs.MINE_BOMBS_NBT_BOMB_KEY );
        	
        	Player player = event.getPlayer();
        	
//        	// Temp test stuff... remove when NBTs are working:
//        	{
//        		
//        		ItemStack item = new ItemStack(Material.APPLE);
//        		SpigotItemStack spItemStack = new SpigotItemStack( item );
//        		spItemStack.setNBTString("tasty", "fruit");
//        		SpigotPlayer spPlayer = new SpigotPlayer( player );
//        		spPlayer.getInventory().addItem(spItemStack);
//        		spPlayer.updateInventory();
//        		
////	        	ItemStack item = new ItemStack(Material.APPLE);
////	        	new NBTItem(item, true).setString("yummy", "apple");
////	        	player.getInventory().addItem(item);
//        		
//        		SpigotItemStack sItemStack = new SpigotItemStack( event.getItem() );
//        		Output.get().logInfo( sItemStack.getNBT().toString() );
//        	}
        	
        	MineBombData bomb = getPrisonUtilsMineBombs().getBombItem( bombName );
        	
        	
        	if ( bomb == null ) {
        		
        		return;
        	}
        	
        	SpigotBlock sBlock = null;
        	
        	SpigotPlayer sPlayer = new SpigotPlayer( player );
        	
        	// If clicking AIR, then event.getClickedBlock() will be null...
        	// so if null, then use the player's location for placing the bomb.
        	if ( event.getClickedBlock() == null ) {
        		Location loc = sPlayer.getLocation();
        		
        		// Get the block 3 away from the player, in the direction (vector) in which
        		// the player is looking.
        		sBlock = (SpigotBlock) loc.add( loc.getDirection().multiply( 3 ) ) .getBlockAt();
        	}
        	else {
        		sBlock = SpigotBlock.getSpigotBlock( event.getClickedBlock() );
        	}
        	
        	
        	Mine mine = blockBreakMines.findMine(player, sBlock, null, null);
        	if ( mine == null ) {
        		// player is not in a mine, so do not allow them to trigger a mine bomb:
        		
        		event.setCancelled( true );
        		return;
        	}
        	else if ( !mine.hasMiningAccess( sPlayer ) ) {
        		// Player does not have access to the mine, so don't allow them to trigger a mine bomb:
        		
        		event.setCancelled( true );
        		return;
        	}
        	
//        	Output.get().logInfo( "### PrisonBombListener: PlayerInteractEvent  02 " );
        	if ( getPrisonUtilsMineBombs().setBombInHand( player, bomb, sBlock ) ) {
        		
        		// The item was a bomb and it was activated.
        		// Cancel the event so the item will not be placed or processed farther.
        		
//        		Output.get().logInfo( "### PrisonBombListener: PlayerInteractEvent  03 Bomb detected - May not have been set. " );
        		event.setCancelled( true );
        	}
        	
        	
        	
 
        	
        }
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
