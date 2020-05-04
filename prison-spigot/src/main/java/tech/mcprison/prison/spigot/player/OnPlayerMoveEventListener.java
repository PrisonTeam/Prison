package tech.mcprison.prison.spigot.player;

import java.text.DecimalFormat;
import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import tech.mcprison.prison.spigot.SpigotPrison;

public class OnPlayerMoveEventListener 
	implements Listener {
	

	private final TreeMap<Long, Long> playerSlimeJumpCache;

	public OnPlayerMoveEventListener() {
		super();
		
		this.playerSlimeJumpCache = new TreeMap<>();
	}

	@EventHandler(priority=EventPriority.LOW)
	public void onPlayerJumpOnSlime(PlayerMoveEvent e ) {
		
		Player player = e.getPlayer();
		Vector velocity = player.getVelocity();
		double velY = velocity.getY();

		if ( velY > 0.15 ) {
			Location location = player.getLocation();
			location.setY( location.getY() - 1 );
			
			Block onBlock = location.getBlock();
			
			if ( onBlock.getType() == Material.SLIME_BLOCK ) {
//				String blockType = (onBlock == null ? "no block" : onBlock.getType().name() );
//				Output.get().logInfo( "%s  %s ", blockType, Double.toString( velY ) );
				
    			// Record player's System time stamp on Jump Boost:
    			Long playerUUIDLSB = Long.valueOf( e.getPlayer().getUniqueId().getLeastSignificantBits() );
    			getPlayerSlimeJumpCache().put( playerUUIDLSB, Long.valueOf( System.currentTimeMillis() ) );
				
    			ItemStack itemInHand = SpigotPrison.getInstance().getCompatibility().getItemInMainHand( player );
    			
    			double boost = 1.40d;
    			
    			switch ( itemInHand.getType() )
				{
					case DIAMOND_PICKAXE:
					case GOLD_PICKAXE:
					case IRON_PICKAXE:
					case STONE_PICKAXE:
					case WOOD_PICKAXE:
						boost *= 2.0;
						break;

					case DIAMOND_BLOCK:
						boost *= 1.50;
						break;
						
					case GOLD_BLOCK:
						boost *= 1.35;
						break;
						
					case IRON_BLOCK:
						boost *= 1.25;
						break;
						

					default:
						break;
				}
    			
    			
				Vector newVel = velocity.clone();
				boost = newVel.getY() * boost;
				newVel.setY( boost );
				
				player.setVelocity( newVel );
				
				DecimalFormat fFmt = new DecimalFormat("#,##0.0000");
				player.sendMessage( "Slime Block Bounce: boost= " + fFmt.format( boost ) + 
							"  inHand= " + itemInHand.getType().name() );
			}
		}
		
	}

	@EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageEvent(final EntityDamageEvent e) {
		e.getEntity().sendMessage( "ouch!!  " + e.getCause().name() );
		
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getEntity();
        if (e.getCause() == DamageCause.FALL) {
        	
			// Record player's System time stamp on Jump Boost:
			Long playerUUIDLSB = Long.valueOf( p.getUniqueId().getLeastSignificantBits() );
			if ( getPlayerSlimeJumpCache().containsKey( playerUUIDLSB )) {
				Long jumpTime = getPlayerSlimeJumpCache().get( playerUUIDLSB );
				
				// If player jumpped on slime within 20 seconds, cancel fall damage:
				if ( System.currentTimeMillis() - jumpTime.longValue() <= 20 * 1000 ) {
					
					e.setCancelled( true );
					p.sendMessage( "By the grace of the Great Slime Block you survive!" );
				}
			}
        	
        }
	}
	
	public TreeMap<Long, Long> getPlayerSlimeJumpCache()
	{
		return playerSlimeJumpCache;
	}
	
}
