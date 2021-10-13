package tech.mcprison.prison.spigot.utils;

import java.text.DecimalFormat;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import com.google.common.collect.Multimap;

import tech.mcprison.prison.bombs.MineBombData;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.spigot.game.SpigotPlayer;

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
	

//    @EventHandler( priority = EventPriority.LOW )
    public void onInteract( PlayerInteractEvent event ) {
//        if ( !event.getPlayer().hasPermission("prison.minebombs.use") ) {
//        	return;
//        }

        Output.get().logInfo( "### PrisonBombListener: PlayerInteractEvent  01 " );
        
        if ( event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR) ) {
        	
        	// If the player is holding a mine bomb, then get the bomb and decrease the
        	// ItemStack in the player's hand by 1:
        	MineBombData bomb = PrisonUtilsMineBombs.getBombInHand( event.getPlayer() );
        	
        	Output.get().logInfo( "### PrisonBombListener: PlayerInteractEvent  02  is bomb null " + (bomb == null) );

        	if ( bomb != null  ) {

        		Output.get().logInfo( "### PrisonBombListener: PlayerInteractEvent  03 " );
        		
        		Player player = event.getPlayer();

        		SpigotPlayer sPlayer = new SpigotPlayer( player );
        		
        		// if the bomb is not activated, then that indicates that the players is 
        		// under a cooldown and the bomb cannot be used, nor has it been removed from their
        		// inventory.
        		if ( bomb.isActivated() ) {
        		
//        			Output.get().logInfo( "### PrisonBombListener: PlayerInteractEvent  04 " );
        		
        			SpigotItemStack bombs = PrisonUtilsMineBombs.getItemStackBomb( bomb );
        			
        			if ( bombs != null ) {

        				Output.get().logInfo( "### PrisonBombListener: PlayerInteractEvent  05 " );
        				
        				SpigotBlock sBlock = new SpigotBlock( event.getClickedBlock() );
        				
        				Output.get().logInfo( "### PrisonBombListener: PlayerInteractEvent  dropping block " );
        				
        				int throwSpeed = 2;
        				
//        				final Item dropped = player.getWorld().dropItem(player.getLocation(), bombs.getBukkitStack() );
//        				dropped.setVelocity(player.getLocation().getDirection().multiply( throwSpeed ).normalize() );
//        				dropped.setPickupDelay( 50000 );
//        				
//        				dropped.setGlowing( bomb.isGlowing() );
//        				dropped.setCustomName( bomb.getName() );
//        				
//        				dropped.setMetadata( "prisonMineBomb", new FixedMetadataValue( SpigotPrison.getInstance(), true ) );
//        				//dropped.setMetadata( "prisonMineName",  new FixedMetadataValue( SpigotPrison.getInstance(), "mineName" ) );
//        				
//        				
//        				PrisonUtilsMineBombs.setoffBombDelayed( sPlayer, bomb, dropped, sBlock, throwSpeed );
        				
        			}
        		}
        		else {
        			
        			String playerUUID = player.getUniqueId().toString();
        			int cooldownTicks = PrisonUtilsMineBombs.checkPlayerCooldown( playerUUID );
        			float cooldownSeconds = cooldownTicks / 2.0f;
        			DecimalFormat dFmt = new DecimalFormat( "0.0" );
        			
        			String message = 
        					String.format( "You cannot use another Prison Mine Bomb for %s seconds.", 
        							dFmt.format( cooldownSeconds ) );
        			sPlayer.sendMessage( message );
        		}
        		
        	}
        	
        	else {
        		
        		// Do nothing since this means the event is not Prison Mine Bomb related.
        	}
        	
        	
 
        	
        }
    }
    

//    @EventHandler( priority = EventPriority.HIGHEST, ignoreCancelled = false )
    public void onBlockPlace( PlayerDropItemEvent event ) {

    	Output.get().logInfo( "### PrisonBombListener: PlayerDropItemEvent " );
        
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
    
//    @EventHandler( priority = EventPriority.HIGHEST, ignoreCancelled = false )
    public void onBlockPlace3( BlockPlaceEvent event ) {
    	
    	Output.get().logInfo( "### PrisonBombListener: BlockPlaceEvent  HIGHEST  isCanceled= " + event.isCancelled() );

//    	event.getBlockPlaced();
    	
    	
    	event.setBuild( true );
    	event.setCancelled( false );
    	
    	ItemStack item = event.getItemInHand();
    	
    	
    	if ( item.hasItemMeta() && item.getItemMeta().hasDisplayName() ) {
    		ItemMeta meta = item.getItemMeta();
    		
    		Output.get().logInfo( "### PrisonBombListener: BlockPlaceEvent  " + meta.getDisplayName() );
    		
//    		meta.getCustomTagContainer().hasCustomTag( null, null )
    		
//    		meta.
//    		
//    		Multimap<Attribute, AttributeModifier> attributes = meta.getAttributeModifiers();
//    		
//    		
//    		for ( String attri : attributes. ) {
//    			
//    		}
    		
    	}
    	
    	
    }
    
//    @EventHandler( priority = EventPriority.HIGHEST, ignoreCancelled = false )
    public void onBlockPlace3( PlayerInteractEvent event ) {
    	
    	Output.get().logInfo( "### PrisonBombListener: PlayerInteractEvent " );
    	
    	
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
