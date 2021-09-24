package tech.mcprison.prison.spigot.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.bombs.MineBombData;
import tech.mcprison.prison.bombs.MineBombs;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.LogLevel;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.api.ExplosiveBlockBreakEvent;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.game.SpigotWorld;
import tech.mcprison.prison.util.Location;

public class PrisonUtilsMineBombs
		extends PrisonUtils
{
	private boolean enableMineBombs = false;
	
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
					
					XMaterial xBomb = XMaterial.matchXMaterial( bomb.getItemType() ).orElse( null );
					
					if ( xBomb != null ) {
						
						SpigotItemStack bombs = new SpigotItemStack( xBomb.parseItem() );
						if ( bombs != null ) {

							bombs.setDisplayName( bomb.getName() );
							bombs.setAmount( count );
							
							
							List<String> lore = bombs.getLore();
							
							lore.add( "&4Prison Mine Bomb:" );
							lore.add( "  &7" + bomb.getName() );
							lore.add( " " );
							
							lore.add( "Size, Diameter: " + ( 1 + 2 * bomb.getRadius()) );
							lore.add( "Shape: " + bomb.getExplosionShape() );
							
							String[] desc = bomb.getDescription().split( " " );
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
//							lore.add( " " + bomb.getDescription() );
							
							lore.add( " " );
							
							bombs.setLore( lore );
							
							
							player.getInventory().addItem( bombs );
						}
						
//						else {
//							
//							String message = "A mine bomb with the name of %s, is unable to generate an ItemStack. ";
//							
//							sender.sendMessage( String.format( message, bombName) );
//						}
						
					}
					
					else {
						
						String message = "A mine bomb with the name of %s, has an invalid itemType value. " +
								"'%s' does not exist in the XMaterial types. Contact Prison support for help " +
								"in finding the correct values to use. Google: 'XSeries XMaterial'";
						
						sender.sendMessage( String.format( message, bombName, bomb.getItemType() ) );
					}
					
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
	

	public boolean isEnableMineBombs() {
		return enableMineBombs;
	}
	public void setEnableMineBombs( boolean enableMineBombs ) {
		this.enableMineBombs = enableMineBombs;
	}
	
}
