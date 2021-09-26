package tech.mcprison.prison.spigot.utils;

import java.text.DecimalFormat;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import tech.mcprison.prison.bombs.MineBombData;
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

    @EventHandler( priority = EventPriority.LOW )
    public void onInteract(PlayerInteractEvent event) {
        if ( !event.getPlayer().hasPermission("prison.minebombs.use") ) {
        	return;
        }
        
        if ( event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR) ) {
        	
        	// If the player is holding a mine bomb, then get the bomb and decrease the
        	// ItemStack in the player's hand by 1:
        	MineBombData bomb = PrisonUtilsMineBombs.getBombInHand( event.getPlayer() );
        	
        	if ( bomb != null  ) {

        		Player player = event.getPlayer();

        		SpigotPlayer sPlayer = new SpigotPlayer( player );
        		
        		// if the bomb is not activated, then that indicates that the players is 
        		// under a cooldown and the bomb cannot be used, nor has it been removed from their
        		// inventory.
        		if ( bomb.isActivated() ) {
        		
        		
        			SpigotItemStack bombs = PrisonUtilsMineBombs.getItemStackBomb( bomb );
        			
        			if ( bombs != null ) {
        				
        				SpigotBlock sBlock = new SpigotBlock( event.getClickedBlock() );
        				
        				
        				int throwSpeed = 2;
        				
        				final Item dropped = player.getWorld().dropItem(player.getLocation(), bombs.getBukkitStack() );
        				dropped.setVelocity(player.getLocation().getDirection().multiply( throwSpeed ).normalize() );
        				dropped.setPickupDelay( 50000 );
        				
        				
        				PrisonUtilsMineBombs.setoffBombDelayed( sPlayer, bomb, dropped, sBlock, throwSpeed );
        				
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
}
