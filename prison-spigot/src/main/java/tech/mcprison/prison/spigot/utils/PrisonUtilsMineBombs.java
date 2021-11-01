package tech.mcprison.prison.spigot.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.autofeatures.AutoFeaturesWrapper;
import tech.mcprison.prison.bombs.MineBombData;
import tech.mcprison.prison.bombs.MineBombs;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.output.LogLevel;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.api.ExplosiveBlockBreakEvent;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.spigot.compat.SpigotCompatibility;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.game.SpigotWorld;
import tech.mcprison.prison.util.Location;
import tech.mcprison.prison.util.Text;

public class PrisonUtilsMineBombs
		extends PrisonUtils
{
	public static final String MINE_BOMBS_LORE_1 = "&4Prison Mine Bomb:";
	public static final String MINE_BOMBS_LORE_2_PREFIX = "  &7";
	public static final int MINE_BOMBS_COOLDOWN_TICKS = 5 * 20; // 5 seconds  // 15 seconds
	
	private boolean enableMineBombs = false;
	
	public static final Map<String, Integer> playerCooldowns = new TreeMap<>();
	
	public PrisonUtilsMineBombs() {
		super();
		
	}
	
	/**
	 * <p>There is no initialization needed for these commands.
	 * <p>
	 * 
	 * <p>This function must return a value of true to indicate that this 
	 * set of commands are enabled.  If it is set to false, then these
	 * commands will not be registered when prison is loaded.
	 * </p>
	 * 
	 * @return
	 */
	@Override
	protected Boolean initialize()
	{
		return true;
	}

	@Command(identifier = "prison utils bombs", 
			description = "Activates a mine bomb for a given player at a specific location. " +
					"The attributes of the bomb can be controlled with this command, such " +
					"as shape, size, etc...",
		onlyPlayers = false, 
		permissions = "prison.utils.bombs",
		altPermissions = "prison.utils.bombs.others")
	public void utilsMineBombs( CommandSender sender, 
			@Arg(name = "playerName", description = "Player name") String playerName,
			@Arg(name = "worldCoordinates", 
			description = "Coordinates within the world that represents the target location " +
					"of the bomb's explosion. Format:" +
					"'(worldName,x,y,z)'. Use placeholder '{worldCoordinates}'") 
					String worldCoordinates,

					
			@Arg(name = "shape", description = "Shape of Explosion. [round]", 
						def = "round") String shape,
			@Arg(name = "radiusSize", description = "Size of Explosion in block radius. [1 though 20]",
						def = "3" )
							String radiusSize
			
			) {
		
		if ( !isEnableMineBombs() ) {
			
			Output.get().logInfo( "Prison's utils command mine bombs is disabled in modules.yml." );
		}
		else {
			
			SpigotPlayer player = checkPlayerPerms( sender, playerName, 
					"prison.utils.bomb", "prison.utils.bomb.others" );

			if ( player != null ) {
				
				if ( playerName != null && !playerName.equalsIgnoreCase( player.getName() ) ) {
					// Need to shift the player's name over to the message:
					
				}
				
				
				Location location = Location.decodeWorldCoordinates( worldCoordinates );
				
				
				
				if ( location == null ) {
					Output.get().logInfo( "Prison utils bomb: worldCoordinates requires a value that " +
							"specifies the coordinates: '(worldName,x,y,z)'. " +
							"Use placeholder '{worldCoordinates}'. Was: [%s]",
							worldCoordinates );
					return;
				}
				
				int radius = 3;
				
				radius = Integer.parseInt( radiusSize );

				MineBombs mBombs = MineBombs.getInstance();

				
				SpigotWorld world = (SpigotWorld) location.getWorld();
				
				List<Location> blockLocations = mBombs.calculateSphere( location, radius, false );
				List<org.bukkit.block.Block> blocks = new ArrayList<>();
				
				for ( Location bLocation : blockLocations ) {
					SpigotBlock sBlock = (SpigotBlock) world.getBlockAt( bLocation );
					blocks.add( sBlock.getWrapper() );
				}
				
				
				SpigotBlock targetBlock = (SpigotBlock) world.getBlockAt( location );
				
				
				
				ExplosiveBlockBreakEvent explodeEvent = new ExplosiveBlockBreakEvent( 
						targetBlock.getWrapper(), player.getWrapper(), blocks );
				explodeEvent.setTriggeredBy( "minebombs" );
				
				explodeEvent.setForceIfAirBlock( true );
				
				Bukkit.getServer().getPluginManager().callEvent( explodeEvent );

			}
		}
	}

	

	@Command(identifier = "prison utils bomb list", 
			description = "A list of all available bombs.",
		onlyPlayers = false, 
		permissions = "prison.utils.bombs" )
	public void utilsMineBombsList( CommandSender sender
			
			) {
		
		if ( !isEnableMineBombs() ) {
			
			Output.get().logInfo( "Prison's utils command mine bombs is disabled in modules.yml." );
		}
		else {
			
			MineBombs mBombs = MineBombs.getInstance();
			
			Set<String> keys = mBombs.getConfigData().getBombs().keySet();
			for ( String key : keys ) {
				
				MineBombData bomb = mBombs.getConfigData().getBombs().get( key );
				
				String message = String.format( 
						"%-12s %-7s Radius= %s (%s)\n   %s", 
						bomb.getName(), bomb.getExplosionShape(), Integer.toString(bomb.getRadius()),
						bomb.getItemType(), bomb.getDescription() );
				
//				sender.sendMessage( message );
				
				Output.get().log( message, LogLevel.PLAIN );
			}
			
		}
	}

	
	@Command(identifier = "prison utils bomb give", 
			description = "Gives the player a mine bomb. Can also provide a list of " +
					"all available bombs.",
					onlyPlayers = false, 
					permissions = "prison.utils.bombs",
					altPermissions = "prison.utils.bombs.others")
	public void utilsMineBombsGive( CommandSender sender, 
			@Arg(name = "playerName", description = "Player name") String playerName,
			
			@Arg(name = "bombName", description = "The bomb name, or 'list' to show all available " +
					"bombs. [list]", 
					def = "list") String bombName,
			@Arg(name = "quantity", description = "Quantity of bombs to give. [1 > +]",
			def = "1" )
	String quantity
	
			) {
		
		if ( !isEnableMineBombs() ) {
			
			Output.get().logInfo( "Prison's utils command mine bombs is disabled in modules.yml." );
		}
		else {
			
			
			if ( "list".equalsIgnoreCase( bombName ) ) {
				
				utilsMineBombsList( sender );
				return;
			}
			
			SpigotPlayer player = checkPlayerPerms( sender, playerName, 
					"prison.utils.bomb", "prison.utils.bomb.others" );
			
			if ( player != null ) {
				
				if ( playerName != null && !playerName.equalsIgnoreCase( player.getName() ) ) {
					// Need to shift the player's name over to the message:
					
				}
				
				
				if ( bombName == null ) {
					Output.get().logInfo( "Prison utils giveBomb: You need to specify a bomb name, or " +
							"use 'list' to show all available bombs. " );
					return;
				}
				
				
				int count = 1;
				
				count = Integer.parseInt( quantity );
				
				MineBombs mBombs = MineBombs.getInstance();
				
				MineBombData bomb = mBombs.getConfigData().getBombs().get( bombName );
				
				if ( bomb != null ) {
					
					SpigotItemStack bombs = getItemStackBomb( bomb );
					
					if ( bombs != null ) {
						
						bombs.setAmount( count );
						player.getInventory().addItem( bombs );
						
					}
					else {
						
						String message = "A mine bomb with the name of %s, has an invalid itemType value. " +
								"'%s' does not exist in the XMaterial types. Contact Prison support for help " +
								"in finding the correct values to use. Google: 'XSeries XMaterial'";
						
						sender.sendMessage( String.format( message, bombName, bomb.getItemType() ) );
					}
					
//					XMaterial xBomb = XMaterial.matchXMaterial( bomb.getItemType() ).orElse( null );
//					
//					if ( xBomb != null ) {
//						
//						SpigotItemStack bombs = new SpigotItemStack( xBomb.parseItem() );
//						if ( bombs != null ) {
//
//							bombs.setDisplayName( bomb.getName() );
//							bombs.setAmount( count );
//							
//							
//							List<String> lore = bombs.getLore();
//							
//							lore.add( MINE_BOMBS_LORE_1 );
//							lore.add( MINE_BOMBS_LORE_2_PREFIX + bomb.getName() );
//							lore.add( " " );
//							
//							lore.add( "Size, Diameter: " + ( 1 + 2 * bomb.getRadius()) );
//							lore.add( "Shape: " + bomb.getExplosionShape() );
//							
//							String[] desc = bomb.getDescription().split( " " );
//							StringBuilder sb = new StringBuilder();
//							
//							for ( String d : desc ) {
//								
//								sb.append( d ).append( " " );
//								
//								if ( sb.length() > 30 ) {
//									sb.insert( 0, "  " );
//									
//									lore.add( sb.toString() );
//									sb.setLength( 0 );
//								}
//							}
//							if ( sb.length() > 0 ) {
//								sb.insert( 0, "  " );
//								
//								lore.add( sb.toString() );
//							}
////							lore.add( " " + bomb.getDescription() );
//							
//							lore.add( " " );
//							
//							bombs.setLore( lore );
//							
//							
//							player.getInventory().addItem( bombs );
//						}
//						
////						else {
////							
////							String message = "A mine bomb with the name of %s, is unable to generate an ItemStack. ";
////							
////							sender.sendMessage( String.format( message, bombName) );
////						}
//						
//					}
					
//					else {
//						
//						String message = "A mine bomb with the name of %s, has an invalid itemType value. " +
//								"'%s' does not exist in the XMaterial types. Contact Prison support for help " +
//								"in finding the correct values to use. Google: 'XSeries XMaterial'";
//						
//						sender.sendMessage( String.format( message, bombName, bomb.getItemType() ) );
//					}
					
				}
				
				else {
					String message = "A mine bomb with the name of %s does not exist.";
					
					sender.sendMessage( String.format( message, bombName ) );
				}
				
//				if ( "list".equalsIgnoreCase( bombName ) ) {
//					
//					Set<String> keys = mBombs.getConfigData().getBombs().keySet();
//					for ( String key : keys ) {
//						
//						MineBombData bomb = mBombs.getConfigData().getBombs().get( key );
//						
//						String message = String.format( 
//								"%-12s %-7s Radius= %s (%s)\n  %s", 
//								bomb.getName(), bomb.getExplosionShape(), Integer.toString(bomb.getRadius()),
//								bomb.getItemType(), bomb.getDescription() );
//						
//						sender.sendMessage( message );
//					}
//				}
				
				
			}
		}
	}
	
	
	public static SpigotItemStack getItemStackBomb( MineBombData bomb ) {
		SpigotItemStack bombs = null;
		
		XMaterial xBomb = XMaterial.matchXMaterial( bomb.getItemType() ).orElse( null );
		
		if ( xBomb != null ) {
			
			bombs = new SpigotItemStack( xBomb.parseItem() );
			if ( bombs != null ) {

				bombs.setDisplayName( bomb.getName() );
				//bombs.setAmount( count );
				
				
				List<String> lore = bombs.getLore();
				
				lore.add( MINE_BOMBS_LORE_1 );
				lore.add( MINE_BOMBS_LORE_2_PREFIX + bomb.getName() );
				lore.add( " " );
				
				lore.add( "Size, Diameter: " + ( 1 + 2 * bomb.getRadius()) );
				lore.add( "Shape: " + bomb.getExplosionShape() );
				
				String bombDesc = bomb.getDescription();
				String[] desc = ( bombDesc == null ? "" : bombDesc ).split( " " );
				StringBuilder sb = new StringBuilder();
				
				for ( String d : desc ) {
					
					sb.append( d ).append( " " );
					
					if ( sb.length() > 30 ) {
						sb.insert( 0, "  " );
						
						lore.add( sb.toString() );
						sb.setLength( 0 );
					}
				}
				if ( sb.length() > 0 ) {
					sb.insert( 0, "  " );
					
					lore.add( sb.toString() );
				}
//				lore.add( " " + bomb.getDescription() );
				
				lore.add( " " );
				
				bombs.setLore( lore );
				
			}
			
		}
		
		return bombs;
	}
	
	
	/**
	 * <p>This takes a player and checks their main hand to see if it contains a bomb.  
	 * If it does, then it return the selected bomb, and it reduces the quantity by
	 * one. 
	 * </p>
	 * 
	 * @param player
	 * @return
	 */
	public static boolean setBombInHand( Player player, SpigotBlock sBlock ) {
		boolean isABomb = false;
		
    	
    	SpigotItemStack itemInHand = SpigotCompatibility.getInstance().getPrisonItemInMainHand( player );

    	if ( itemInHand != null ) {
    		List<String> lore = itemInHand.getLore();
    		
    		String prisonBombId = lore.size() > 0 ? Text.stripColor( lore.get( 0 ) ) : "";
    		String bombName = lore.size() > 1 ? Text.stripColor( lore.get( 1 ) ).trim() : "";
    		
    		if ( prisonBombId.equalsIgnoreCase( Text.stripColor( MINE_BOMBS_LORE_1 )) ) {
    			
    			

    			// String bombName = lore1.trim();
    			
    			MineBombs mBombs = MineBombs.getInstance();
    			
    			MineBombData bomb = mBombs.findBomb( bombName );
    			
				String prisonExplosiveHandlerPriority = AutoFeaturesWrapper.getInstance().getMessage( 
						AutoFeatures.ProcessPrisons_ExplosiveBlockBreakEventsPriority );
				
				if ( bomb != null && "DISABLED".equalsIgnoreCase( prisonExplosiveHandlerPriority ) ) {
					isABomb = true;

					Output.get().logWarn( "A Prison Mine Bomb was attempted to be used, but the " +
							"handling of its explosion is DISABLED.  Edit the 'autoFeaturesConfig.yml' " +
							"file and set 'ProcessPrisons_ExplosiveBlockBreakEventsPriority: NORMAL'." );
				}
    			
				else if ( bomb != null ) {
					isABomb = true;

					// if the toolInHand has not been set, use a diamond pickaxe:
					if ( bomb.getToolInHand() == null ) {
						XMaterial xMat = XMaterial.DIAMOND_PICKAXE;
						bomb.setToolInHand( xMat.name() );
					}
					
    				SpigotPlayer sPlayer = new SpigotPlayer( player );

    				String playerUUID = player.getUniqueId().toString();
    				int cooldownTicks = checkPlayerCooldown( playerUUID );
    			
    				if ( cooldownTicks == 0 ) {
    					
    					
    					// Set cooldown:
    					PrisonUtilsMineBombs.addPlayerCooldown( playerUUID );
    					
    					SpigotItemStack bombs = PrisonUtilsMineBombs.getItemStackBomb( bomb );
    					
    					if ( bombs != null ) {
    						
    						// Setting activated to true indicates the bomb is live and it has
    						// been removed from the player's inventory:
    						bomb.setActivated( true );
    						
    						// Remove from inventory:
    						itemInHand.setAmount( itemInHand.getAmount() - 1 );
    						
    						// Not sure if the following is needed?
    						SpigotCompatibility.getInstance().setItemInMainHand( player, itemInHand.getBukkitStack() );

    						
    						//Output.get().logInfo( "### PrisonBombListener: PlayerInteractEvent  05 " );
    						
    						// SpigotBlock sBlock = new SpigotBlock( event.getClickedBlock() );
    						
    						//Output.get().logInfo( "### PrisonBombListener: PlayerInteractEvent  dropping block " );
    						
    						SpigotBlock bombBlock = sBlock != null ? sBlock : (SpigotBlock) player.getLocation().getBlock();
    						
    						
    						
    						// For mine bombs, take the block below where the bomb's item was dropped.  The floating 
    						// item is not the block that needs to be the target block for the explosion.  Also, the block
    						// if it is on top of the mine, would be identified as being outside of the mine.
    						int count = 0;
    						while ( (count++ == 0 || bombBlock.isEmpty()) && bombBlock.getLocation().getBlockY() > 1 ) {
    							Location bbLocation = bombBlock.getLocation();
    							bbLocation.setY( bbLocation.getBlockY() - 1 );
    							
    							bombBlock = (SpigotBlock) bbLocation.getBlockAt();
    							
//    							Output.get().logInfo( 
//    									"#### PrisonUtilsMineBombs:  bomb y loc: " + bombBlock.getWrapper().getLocation().getBlockY() + 
//    										"  " + bombBlock.getLocation().getBlockY() + "  count= " + count );
    						}
    						
//    						Output.get().logInfo( 
//    								"#### PrisonUtilsMineBombs:  bomb loc: " + bombBlock.getLocation().toWorldCoordinates() );
    						
    						//int throwSpeed = 2;

    						// This places the item so it will float:
    						final Item dropped = player.getWorld().dropItem( 
    									bombBlock.getWrapper().getLocation(), bombs.getBukkitStack() );
    						
    						dropped.setPickupDelay( Integer.MAX_VALUE );
    						dropped.setCustomName( bomb.getName() );
    						//dropped.setVelocity(player.getLocation().getDirection().multiply( throwSpeed ).normalize() );
    						
    						int delayInTicks = 5 * 20; // 5 secs
    						
    						
    						// Get the block that is y - 1 lower than the original block:
//    						SpigotBlock bombBlock = (SpigotBlock) sBlock.getRelative( BlockFace.DOWN );
    						
    						
    						// Submit the bomb's task to go off:
    						PrisonUtilsMineBombs.setoffBombDelayed( sPlayer, bomb, dropped, bombBlock, delayInTicks );
    						

    						

    						// setGlow is invalid for spigot 1.8.8:
    						//dropped.setGlowing( bomb.isGlowing() );
    						
    						// setGravity is invalid for spigot 1.8.8:
//    						dropped.setGravity( false );
    						
    						
    						
//    						dropped.setMetadata( "prisonMineBomb", new FixedMetadataValue( SpigotPrison.getInstance(), true ) );
    						//dropped.setMetadata( "prisonMineName",  new FixedMetadataValue( SpigotPrison.getInstance(), "mineName" ) );
    						
    						
    					}
    				}
    				
    				else {
    					
            			float cooldownSeconds = cooldownTicks / 20.0f;
            			DecimalFormat dFmt = new DecimalFormat( "0.0" );
            			
            			String message = 
            					String.format( "You cannot use another Prison Mine Bomb for %s seconds.", 
            							dFmt.format( cooldownSeconds ) );
            			sPlayer.sendMessage( message );

    				}
    				
    			}
    		}
    	}
    	
    	return isABomb;
	}
	
	public static boolean addPlayerCooldown( String playerUUID ) {
		return addPlayerCooldown( playerUUID, MINE_BOMBS_COOLDOWN_TICKS );
	}
	
	/**
	 * <p>If a cooldown is not setup for the player, this will try to add one and 
	 * will return a value of true to indicate the a cooldown was set.  If a cooldown
	 * already exists, then this will return a value of false.
	 * </p>
	 * 
	 * @param playerUUID
	 * @param ticks
	 * @return
	 */
	public static boolean addPlayerCooldown( String playerUUID, int ticks ) {
		boolean results = false;
		
		if ( !playerCooldowns.containsKey( playerUUID ) || 
				playerCooldowns.get( playerUUID ) <= 0 ) {
			
			playerCooldowns.put( playerUUID, ticks );
			
			 new BukkitRunnable() {
				 
                 @Override
                 public void run() {
                	 
                	 int ticksRemaining = playerCooldowns.get( playerUUID ) - 10;
                	 
                	 if ( ticksRemaining <= 0 ) {
                		 playerCooldowns.remove( playerUUID );
                		 this.cancel();
                	 }
                	 else {
                		 playerCooldowns.put( playerUUID, ticksRemaining );
                	 }
                	 
                 }
             }.runTaskTimer( SpigotPrison.getInstance(), 10, 10);
			
             results = true;
		}
		
		return results;
	}
	
	public static int checkPlayerCooldown( String playerUUID ) {
		int results = 0;
		
		if ( playerCooldowns.containsKey( playerUUID ) ) {
			results = playerCooldowns.get( playerUUID );
		}
		
		return results;
	}

	public static boolean setoffBombDelayed( SpigotPlayer sPlayer, MineBombData bomb, Item droppedBomb, 
						SpigotBlock targetBlock, int delayTicks ) {
		boolean results = false;
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				
				// Remove the item that the player threw:
				droppedBomb.remove();
				
				Location location = targetBlock.getLocation();
				
				if ( location == null ) {
					location = sPlayer.getLocation();
				}
				
				
				MineBombs mBombs = MineBombs.getInstance();
				
				// Calculate all the locations that are included in the explosion:
				List<Location> blockLocations = mBombs.calculateSphere( location, 
														bomb.getRadius(), false );

				
				SpigotWorld world = (SpigotWorld) location.getWorld();

				// Convert to spigot blocks:
				List<org.bukkit.block.Block> blocks = new ArrayList<>();
				for ( Location bLocation : blockLocations ) {
					SpigotBlock sBlock = (SpigotBlock) world.getBlockAt( bLocation );
					if ( !sBlock.isEmpty() ) {
						
						blocks.add( sBlock.getWrapper() );
					}
				}
				
				
//				SpigotBlock targetBlock = (SpigotBlock) world.getBlockAt( location );
				
				
				ExplosiveBlockBreakEvent explodeEvent = new ExplosiveBlockBreakEvent( 
						targetBlock.getWrapper(), sPlayer.getWrapper(), blocks );
				explodeEvent.setTriggeredBy( bomb.getName() );
				explodeEvent.setMineBomb( bomb );
				
				// Set the toolInHand that needs to be used for the bomb:
				// Default to a diamond pickaxe if not specified.
				// The bomb must define a tool, otherwise auto features will
				// use the mine bombs that the player is holding, or if it was their
				// last one, it would be just AIR:
				String toolInHandName = bomb.getToolInHand();
				if ( toolInHandName == null || toolInHandName.trim().isEmpty() ) {
						XMaterial xMat = XMaterial.DIAMOND_PICKAXE;
						toolInHandName = xMat.name();
				}
				XMaterial xMatTool = XMaterial.matchXMaterial( toolInHandName ).orElseGet( null );
				ItemStack toolInHand = new SpigotItemStack( xMatTool == null ?
						XMaterial.DIAMOND_PICKAXE.parseItem() : 
							xMatTool.parseItem() );
				explodeEvent.setToolInHand( toolInHand );
				
				
				// Mine bombs should not calculate durability:
				explodeEvent.setCalculateDurability( false );
				
				
				// Normally the explosion will ONLY work if the center target block was non-AIR.
				// This setting allows the explosion to be processed even if it is air.
				explodeEvent.setForceIfAirBlock( true );
				
				Bukkit.getServer().getPluginManager().callEvent( explodeEvent );

				if ( explodeEvent.isCancelled() ) {
					
					if ( Output.get().isDebug() ) {
						Output.get().logDebug( "Mine Bomb's ExplosiveBlockBreakEvent has been canceled. " +
								"It may have been processed successfully." );
					}
				}
				else {
					// If it wasn't canceled, then it may not have been handled
					
					if ( Output.get().isDebug() ) {
						Output.get().logDebug( "Mine Bomb's ExplosiveBlockBreakEvent has NOT been canceled." );
					}
				}

				
			}
		}.runTaskLater( SpigotPrison.getInstance(), delayTicks );
		
		//.runTaskTimer( SpigotPrison.getInstance(), 10, 10);
		
		return results;
	}
	
	public static double getPlayerCoolDownRemaining()
	{
		
		return 0;
	}
	
	public boolean isEnableMineBombs() {
		return enableMineBombs;
	}
	public void setEnableMineBombs( boolean enableMineBombs ) {
		this.enableMineBombs = enableMineBombs;
	}
	
}
