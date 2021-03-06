package tech.mcprison.prison.spigot.slime;

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

public class SlimeBlockFunEventListener 
	implements Listener {
	
	private boolean enabled = false;

	private final TreeMap<Long, SlimeBlockFunEventData> playerSlimeJumpCache;

	
	public SlimeBlockFunEventListener() {
		super();
		
		this.playerSlimeJumpCache = new TreeMap<>();
		
		this.enabled = SpigotPrison.getInstance().getConfig().getBoolean("slime-fun");
		
	}

	@EventHandler(priority=EventPriority.LOW)
	public void onPlayerJumpOnSlime(PlayerMoveEvent e ) {
		if ( !isEnabled() ) {
			return;
		}
		
		Player player = e.getPlayer();

		Location loc = player.getLocation();
		Location loc1 = loc.clone();
		loc1.setY( loc.getY() - 1 );
		Block onBlock1 = loc1.getBlock();
		
		if ( onBlock1.getType() == Material.AIR ||
				onBlock1.getType() == Material.SLIME_BLOCK
					) {
			
			Vector velocity = player.getVelocity();
			double velY = velocity.getY();

			// Record player's System time stamp on Jump Boost:
			Long playerUUIDLSB = Long.valueOf( e.getPlayer().getUniqueId().getLeastSignificantBits() );
			
			if ( onBlock1.getType() == Material.AIR && 
					getPlayerSlimeJumpCache().containsKey( playerUUIDLSB ) ) {
				
				SlimeBlockFunEventData moveEventData = getPlayerSlimeJumpCache().get( playerUUIDLSB );
				
				if ( moveEventData != null ) {
					moveEventData.inAir( loc1.getY(), player );
				}
				
			}
			else if ( velY > 0.2 ) {
				
				// checking and boosting over two blocks below feet caused server to crash due to too much boost! :)
				// Too much boost, multiplied by the vector will crash the server if it exceeds 1024, of which
				// is prevented... so don't worry about it. :)
//				Location loc2 = loc1.clone();
//				loc2.setY( loc2.getY() - 1 );
//				Block onBlock2 = loc2.getBlock();
				
				if ( onBlock1.getType() == Material.SLIME_BLOCK  
//				 || onBlock2.getType() == Material.SLIME_BLOCK
						) {
					
					ItemStack itemInHand = SpigotPrison.getInstance().getCompatibility().getItemInMainHand( player );
					
					double boost = getBoost( itemInHand );
					
					
					// Record player's System time stamp on Jump Boost:
					// Add player jump data to the cache:
					if ( !getPlayerSlimeJumpCache().containsKey( playerUUIDLSB ) ) {
						SlimeBlockFunEventData moveEventData = new SlimeBlockFunEventData( playerUUIDLSB, loc.getY() );
						getPlayerSlimeJumpCache().put( playerUUIDLSB, moveEventData );
						
						player.sendMessage( "SlimeFun: Use at your own risk. Jumpping out of the " +
												"world may crash the server." );
					}
					
					getPlayerSlimeJumpCache().get( playerUUIDLSB )
														.addJumpEvent( loc1.getY(), boost, velY );
					
					Vector newVelocity = calculateVelocityY( boost, velocity, player );
					
					player.setVelocity( newVelocity );
					
				}
			}
		
		}
	}

	/**
	 * <p>If max velocity exceeds 1024 then the server could crash.
	 * This function makes sure that the calculated velocity for y does not
	 * exceed 1024; if it does, then it is set to 1024.
	 * </p>
	 * 
	 * @param boost
	 * @param velocityOriginal
	 * @param player
	 * @return
	 */
	private Vector calculateVelocityY( double boost, Vector velocityOriginal, Player player ) {
		Vector newVelocity = velocityOriginal.clone();
		
		double velocityY = newVelocity.getY() * boost;
		
		double velocityX = newVelocity.getX() * boost * 0.13d;
		double velocityZ = newVelocity.getZ() * boost * 0.13d;
		
		
		if ( velocityY > 1024.0 ) {
			DecimalFormat f4Fmt = new DecimalFormat("#,##0.0000");
			
			player.sendMessage( "SlimeFun: Exceeded max velocity!! velY:" + 
						f4Fmt.format( velocityY ) );
			
			velocityY = 1024.0;
		}
		
		newVelocity.setX( velocityX );
		newVelocity.setY( velocityY );
		newVelocity.setZ( velocityZ );

		return newVelocity;
	}

	private double getBoost( ItemStack itemInHand )
	{
		double boost = 1.27d;
		
		
		// Due to variations with gold and wood PickAxe need to use a dynamic 
		// Material name selection which will fit for the version of MC that is
		// being ran.
		Material holding = itemInHand.getType();
		
		if ( holding == Material.DIAMOND_PICKAXE ) {
			boost *= 3.0;
		}
		else if ( holding == Material.matchMaterial( "GOLD_PICKAXE" ) ||
				  holding == Material.IRON_PICKAXE ||
				  holding == Material.STONE_PICKAXE ||
				  holding == Material.matchMaterial( "WOOD_PICKAXE" ) ) {
			boost *= 2.85;
		}
		else if ( holding == Material.DIAMOND_BLOCK ) {
			boost *= 1.65;
		}
		else if ( holding == Material.GOLD_BLOCK ) {
			boost *= 1.45;
		}
		else if ( holding == Material.IRON_BLOCK ) {
			boost *= 1.20;
		}
		
		
//		switch ( itemInHand.getType() )
//		{
//			case DIAMOND_PICKAXE:
//				boost *= 2.0;
//				break;
//				
//			case GOLD_PICKAXE:
//			case IRON_PICKAXE:
//			case STONE_PICKAXE:
//			case WOOD_PICKAXE:
//				boost *= 2.85;
//				break;
//
//			case DIAMOND_BLOCK:
//				boost *= 1.65;
//				break;
//				
//			case GOLD_BLOCK:
//				boost *= 1.45;
//				break;
//				
//			case IRON_BLOCK:
//				boost *= 1.20;
//				break;
//				
//
//			default:
//				break;
//		}
		return boost;
	}

	@EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageEvent(final EntityDamageEvent e) {
        if (!isEnabled() || !(e.getEntity() instanceof Player)) {
            return;
        }

        if (e.getCause() == DamageCause.FALL) {
        	
        	Player p = (Player) e.getEntity();
        	
			// Record player's System time stamp on Jump Boost:
			Long playerUUIDLSB = Long.valueOf( p.getUniqueId().getLeastSignificantBits() );
			if ( getPlayerSlimeJumpCache().containsKey( playerUUIDLSB )) {
				SlimeBlockFunEventData moveEventData = getPlayerSlimeJumpCache().get( playerUUIDLSB );
				
				// If player jumped on slime within 16 seconds, cancel fall damage:
				if ( moveEventData.hasLanded(p) ) {
					
					e.setCancelled( true );
					p.sendMessage( "By the grace of the Great Slime Block you survive!" );
				} 
			}
        	
        }
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled( boolean enabled ) {
		this.enabled = enabled;
	}

	public TreeMap<Long, SlimeBlockFunEventData> getPlayerSlimeJumpCache()
	{
		return playerSlimeJumpCache;
	}
	
}
