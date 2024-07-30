package tech.mcprison.prison.spigot.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
//import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XEntityType;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.autofeatures.AutoFeaturesWrapper;
import tech.mcprison.prison.bombs.MineBombData;
import tech.mcprison.prison.bombs.MineBombDetonateTask;
import tech.mcprison.prison.bombs.MineBombEffectsData;
import tech.mcprison.prison.bombs.MineBombEffectsData.EffectType;
import tech.mcprison.prison.bombs.MineBombs;
import tech.mcprison.prison.bombs.MineBombs.AnimationPattern;
import tech.mcprison.prison.bombs.MineBombs.ExplosionShape;
import tech.mcprison.prison.bombs.animations.BombAnimationsTask;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.commands.Wildcard;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Entity;
import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.api.ExplosiveBlockBreakEvent;
import tech.mcprison.prison.spigot.block.OnBlockBreakMines;
import tech.mcprison.prison.spigot.block.PrisonItemStackNotSupportedRuntimeException;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.spigot.compat.Compatibility.EquipmentSlot;
import tech.mcprison.prison.spigot.compat.SpigotCompatibility;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.game.SpigotWorld;
import tech.mcprison.prison.spigot.game.entity.SpigotArmorStand;
import tech.mcprison.prison.spigot.game.entity.SpigotEntity;
import tech.mcprison.prison.spigot.game.entity.SpigotEntityType;
import tech.mcprison.prison.spigot.inventory.SpigotPlayerInventory;
import tech.mcprison.prison.spigot.nbt.PrisonNBTUtil;
import tech.mcprison.prison.util.BluesSemanticVersionComparator;
import tech.mcprison.prison.util.Bounds;
import tech.mcprison.prison.util.Location;
import tech.mcprison.prison.util.Text;

public class PrisonUtilsMineBombs
		extends PrisonUtilsMineBombsMessages 
{
	public static final String MINE_BOMBS_LORE_1 = "&4Prison Mine Bomb:";
	public static final String MINE_BOMBS_LORE_2_PREFIX = "  &7";
	public static final int MINE_BOMBS_COOLDOWN_TICKS = 5 * 20; // 5 seconds  // 15 seconds
	
	
	private boolean enableMineBombs = false;
	
	private static PrisonUtilsMineBombs instance;
	
	
	public PrisonUtilsMineBombs() {
		super();
		
		instance = this;
	}
	
	public static PrisonUtilsMineBombs getInstance() {
		if ( instance == null ) {
			synchronized ( PrisonUtilsMineBombs.class ) {
				
				if ( instance == null ) {
					new PrisonUtilsMineBombs();
				}
			}
		}
		
		return instance;
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
		//validateMineBombs();
		
		return true;
	}

	
	
	@Command(identifier = "prison utils bomb findArmorStands", 
			description = "This will remove abandonded/zombi armor stands within "
					+ "a radius of a player, or self.",
		onlyPlayers = false, 
		permissions = "prison.utils.bombs",
		altPermissions = "prison.utils.bombs.others")
	public void utilsFindMineBombsArmorStands( CommandSender sender, 
			@Arg(name = "playerName", description = "Player name") String playerName,
			@Arg(name = "radius", def = "10",
			description = "Number of blocks in a radius from the player "
					+ "to search for armor stands to remove.") int radius,
			@Wildcard(join=true)
			@Arg(name = "options", def = "list",
					description = "Action to perform. 'list' will list all armor stands within"
							+ "the general cubic-radius that are marked with the mine bomb NBT Id."
							+ "To include 'any' armor stand include the keyword '*any*'. "
							+ "'show' will make the armor stands visible for a few minutes "
							+ "and optionally provide an id for showing only one.  "
							+ "removeAll' will remove all armor stands "
							+ "within the general cubic-radius. And 'removeId' will remove "
							+ "only one armor stand that matches the supplied ID. "
							+ " [list, show <id>, removeAll, removeId <id>, any]") String options
			
			) {
		if ( !isEnableMineBombs() ) {
			
			Output.get().logInfo( "Prison's utils command mine bombs is disabled in modules.yml." );
		}
		else {
			
			SpigotPlayer player = checkPlayerPerms( sender, playerName, 
					"prison.utils.bomb", "prison.utils.bomb.others" );

			sender.sendMessage( "#### findArmorStands: radius: " + radius );

			boolean list = false;
			boolean show = false;
			boolean removeAll = false;
			boolean removeId = false;
			boolean any = false;
			
			
			String uuid = null;
			
			options = options.toLowerCase().trim();
			
			if ( options.contains( "any" ) ) {
				any = true;
				options = options.replace( "any", "" ).trim();
			}
			if ( options.contains( "list" ) ) {
				list = true;
				options = options.replace( "list", "" ).trim();
			}
			if ( options.contains( "show" ) ) {
				show = true;
				options = options.replace( "show", "" ).trim();
				uuid = options;
			}
			if ( options.contains( "removeall" ) ) {
				removeAll = true;
				options = options.replace( "removeall", "" ).trim();
			}
			if ( options.contains( "removeid" ) ) {
				removeId = true;
				options = options.replace( "removeid", "" ).trim();
				uuid = options;
			}
			
			if ( player != null ) {

				if ( show ) {
					Output.get().logInfo( "&3Sorry 'show' is not yet implemented. "
							+ "Please ping Blue on discord. ");
				}
				
				Location loc = player.getLocation();
				SpigotEntityType seType = new SpigotEntityType( XEntityType.ARMOR_STAND );
				
				List<Entity> entities = player.getNearbyEntities(radius, seType);
				
				DecimalFormat iFmt = Prison.getDecimalFormatStaticInt();
				DecimalFormat dFmt = Prison.getDecimalFormatStatic( "#,##0.0");
				
				int count = 0;
				for (Entity entity : entities) {
					SpigotEntity sEntity = (SpigotEntity) entity;
					SpigotArmorStand sArmorStand = new SpigotArmorStand( entity );
					
					String bombName = getBombName( sEntity, sArmorStand );
					
					boolean hasId = false;
					
//					String bombName = sEntity.getNbtString( MineBombs.MINE_BOMBS_NBT_KEY );
					if ( bombName != null && bombName.trim().length() > 0 ) {
						hasId = true;
					}
					
					if ( !any && !hasId ) {
						// Not a mine bomb armor stand so continue and bypass processing:
						continue;
					}

					
					tech.mcprison.prison.internal.Player owner = getOwner(sEntity, sArmorStand);
					
					tech.mcprison.prison.internal.Player thrower = getThrower(sEntity, sArmorStand);
					
					
					Location l = entity.getLocation();
					
					String id = entity.getUniqueId().toString().substring(30);
					
					int ticks = entity.getTicksLived();
					double times = ticks / 20.0;
					String sfx = "secs";
					if ( times > 60 ) {
						times /= 60;
						sfx = "mins";
					}
					if ( times > 60 ) {
						times /= 60;
						sfx = "hrs";
					}
					
					Bounds bounds = new Bounds( loc, l );
					
					String status = "";
					
					// actions:
					if ( show ) {
//						sEntity.getWrapper().
					}
					if ( list ) {
						// Do nothing:
					}
					else if ( removeAll ||
								removeId && id.equalsIgnoreCase( uuid ) 
							) {
						sEntity.remove();
						status = "&a** removed **";
					}
					
					String msg = String.format(
							"&3ArmorStand: distance &7%4s  &3id: &7%s  "
							+ "&3x:%4s y:%4s z: %4s  "
							+ "&3age: &7%5s " // Age
							+ "&3%-4s  "      // Age unit
							+ "&3%s  " // sfx
							+ "&3bombName: &c%s  "
							+ "&3owner: &a%s  "
							+ "&3usedBy: &a%s ",
							iFmt.format( bounds.getDistance3d() ),
							id,
							dFmt.format( l.getX() ),
							dFmt.format( l.getY() ),
							dFmt.format( l.getZ() ),
							dFmt.format( times ), // age
							sfx, 
							status,
							bombName != null && bombName.trim().length() > 0 ? 
									bombName : "none",
							owner != null ? owner.getName() : "&dunknown",
							thrower != null ? thrower.getName() : "&dunknown"
							);
					
					sender.sendMessage( msg );
					
					count++;
				}
				
				if ( count == 0 ) {
					sender.sendMessage( "&3-= No ArmmorStands were located with the selected filters =-");
				}
				
//				 Location EntityArea = new Location(Bukkit.getWorld("world"),125,71,105);
//	                World world = Bukkit.getServer().getWorld("world");
//	                List<Entity> entList = world.getEntities();
//	             
//	                for(Entity current : entList){
//	                    if(current instanceof Item){
//	                        current.remove();
//	                    }
//	                }
				
			}
			
		}
		

	}
	
	
	/**
	 * <p>This tries to get the bomb name from the armor stand, if it cannot get it 
	 * directly from the armor stand, then it will try to get it from the 
	 * item that the armor stand is holding.
	 * </p>
	 * 
	 * @param sEntity
	 * @param sArmorStand
	 * @return
	 */
	private String getBombName(SpigotEntity sEntity, SpigotArmorStand sArmorStand) {
		String key = MineBombs.MINE_BOMBS_NBT_KEY;
		return getNBTString( sEntity, sArmorStand, key );
	}
	private tech.mcprison.prison.internal.Player getOwner(SpigotEntity sEntity, SpigotArmorStand sArmorStand) {
		tech.mcprison.prison.internal.Player results = null;
		String key = MineBombs.MINE_BOMBS_NBT_OWNER_UUID;
		String uuid = getNBTString( sEntity, sArmorStand, key );
		if ( uuid != null && uuid.trim().length() > 0 ) {
			results = Prison.get().getPlatform().getPlayer( UUID.fromString(uuid)).orElse(null);
		}
		return results;
	}
	private tech.mcprison.prison.internal.Player getThrower(SpigotEntity sEntity, SpigotArmorStand sArmorStand) {
		tech.mcprison.prison.internal.Player results = null;
		String key = MineBombs.MINE_BOMBS_NBT_THROWER_UUID;
		String uuid = getNBTString( sEntity, sArmorStand, key );
		if ( uuid != null && uuid.trim().length() > 0 ) {
			results = Prison.get().getPlatform().getPlayer( UUID.fromString(uuid)).orElse(null);
		}
		return results;
	}
	
	private String getNBTString(SpigotEntity sEntity, SpigotArmorStand sArmorStand, String key) {
		String bombName = null;
		
		bombName = sEntity.getNbtString( key );
		if ( bombName == null || bombName.trim().length() == 0 ) {
			
			SpigotItemStack iStack = (SpigotItemStack) sArmorStand.getItemInHand();
			
			if ( iStack != null && !iStack.isAir() ) {
				
				bombName = PrisonNBTUtil.getNBTString( iStack.getBukkitStack(), key );
				
				if ( bombName == null || bombName.trim().length() == 0 ) {
					
					bombName = PrisonNBTUtil.getNBTString( iStack.getBukkitStack(), key );
				}
			}
		}
		
		return bombName;
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

					
			@Arg(name = "shape", description = "Shape of Explosion. Use the command '/prison utils bomb list' for a list of shapes. [sphere]", 
						def = "sphere") String shape,
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
				
				try {
					radius = Integer.parseInt( radiusSize );
				} catch (NumberFormatException e) {
					Output.get().logInfo( "Prison utils bomb: invalid value for radius [%s] defaulting to %d", 
							radiusSize, radius);
				}

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

	

	@Command(identifier = "prison utils bomb reload", 
			description = "Reloads and validates the mine bombs.",
		onlyPlayers = false, 
		permissions = "prison.utils.bombs",
		aliases = "prison reload bombs")
	public void utilsMineBombsReload( CommandSender sender
			
			) {
		if ( !isEnableMineBombs() ) {
			
			Output.get().logInfo( "Prison's utils command mine bombs is disabled in modules.yml." );
		}
		else {
			
			MineBombs mBombs = MineBombs.getInstance();
			
			mBombs.loadConfigJson();
			
			mBombs.validateMineBombs();
		}		
	}
	
	/**
	 * <p>Just a wrapper to reload mine bombs so it does not have to pass a null value.
	 * This can also be used for initially loading the configs to force data updates.
	 * </p>
	 * 
	 */
	public void reloadPrisonMineBombs() {
		
		utilsMineBombsReload( null );
	}
	

	@Command(identifier = "prison utils bomb list", 
			description = "A list of all available bombs, including their full details.  " +
					"This command also shows a list of settings for the animation patterns, "
					+ "shapes, sounds, and visual effects. This command is best ran in the console.",
		onlyPlayers = false, 
		permissions = "prison.utils.bombs" )
	public void utilsMineBombsList( CommandSender sender,
			@Arg(name = "option", description = "Options: By default, the abbreviated list of "
					+ "mine bombs are shown, with the 'all' options shows a lot mmore of the "
					+ "bomb details. " +
					"The options of 'animations', 'shapes', 'sounds', and 'visuals' lists valid settings " +
					"to be used with the bombs. [all animations shapes sounds visuals]", def = "" ) 
			String options
			
			) {
		
		if ( !isEnableMineBombs() ) {
			
			Output.get().logInfo( "Prison's utils command mine bombs is disabled in modules.yml." );
		}
		else {
			
			MineBombs mBombs = MineBombs.getInstance();
			
			List<String> messages = new ArrayList<>();
			
			boolean optAll = false;
			
			if ( options != null && options.toLowerCase().contains( "all" ) ) {
				options = options.replace( "all", "" ).trim();
				optAll = true;
			}
			
			if ( options != null && options.toLowerCase().contains( "animations" ) ) {
				// exit after showing the list of mine bomb animations:
				
				messages.add( "&7Animations Patterns:" );
				
				List<String> animations = AnimationPattern.asList();
				messages.addAll( Text.formatColumnsFromList( animations, 4 ) );
				
				sender.sendMessage( messages.toArray( new String[0] ) );
				return;
			}
			
			
			if ( options != null && options.toLowerCase().contains( "shapes" ) ) {
				// exit after showing shapes:
				
				messages.add( "&7Shapes:" );
				List<String> shapes = ExplosionShape.asList();
				messages.addAll( Text.formatColumnsFromList( shapes, 4 ) );
				
				sender.sendMessage( messages.toArray( new String[0] ) );
				return;
			}
			
			if ( options != null && options.toLowerCase().contains( "sounds" ) ) {
				List<String> sounds = new ArrayList<>();
				
//				// If running less than MC 1.9.0, ie 1.8.x, then use different code for effects:
//				boolean is18 = new BluesSpigetSemVerComparator().compareMCVersionTo( "1.9.0" ) < 0 ;
//
//				if ( is18 ) {
//					
//				}

//				for ( Sound p : Sound.values() ) {
//					sounds.add( p.name() );
//				}
//				
//				messages.add( "&7Sound Effects:" );
//				for ( String line : Text.formatColumnsFromList( sounds, 3 ) ) {
//					messages.add( "      " + line );
//				}
//				sounds.clear();
				
				
				
				for ( XSound p : XSound.values() ) {
					sounds.add( p.name() );
				}
				Collections.sort( sounds );
				
				messages.add( "&7XSound Effects:" );
				for ( String line : Text.formatColumnsFromList( sounds, 3 ) ) {
					messages.add( "      " + line );
				}
				
				sender.sendMessage( messages.toArray( new String[0] ) );
				return;
			}
			
			
			
			
			if ( options != null && options.toLowerCase().contains( "visuals" ) ) {
				List<String> visuals = new ArrayList<>();
				
				// bukkit 1.8.8:
//				Effect.values()
				
				// If running less than MC 1.9.0, ie 1.8.x, then use different code for effects:
				boolean is18 = new BluesSemanticVersionComparator().compareMCVersionTo( "1.9.0" ) < 0 ;

				if ( is18 ) {
					for ( Effect p : Effect.values() ) {
						visuals.add( p.name() );
					}
					Collections.sort( visuals );
//				for ( Particle p : Particle.values() ) {
//					visuals.add( p.name() );
//				}
					
					messages.add( "&7Visual Effects (bukkit 1.8.x: Effect):" );
					for ( String line : Text.formatColumnsFromList( visuals, 4 ) ) {
						messages.add( "      " + line );
					}
					
				}
				else {
					
					for ( Particle p : Particle.values() ) {
						visuals.add( p.name() );
					}
					Collections.sort( visuals );
//				for ( Particle p : Particle.values() ) {
//					visuals.add( p.name() );
//				}
					
					messages.add( "&7Visual Effects (Particle):" );
					for ( String line : Text.formatColumnsFromList( visuals, 4 ) ) {
						messages.add( "      " + line );
					}
					
				}

				
				
				sender.sendMessage( messages.toArray( new String[0] ) );
				return;
			}
			
			
			DecimalFormat dFmt = new DecimalFormat( "#,##0.000" );
			
			
			Set<String> keys = mBombs.getConfigData().getBombs().keySet();
			for ( String key : keys ) {
				
				MineBombData bomb = mBombs.getConfigData().getBombs().get( key );
				
				String message = String.format( 
						"&7%-12s    &3AnimationPattern & Speed: &7%s %s   &3Throw Speed: &7(%s - %s)", 
						bomb.getName(), 
						bomb.getAnimationPattern().name(),
						dFmt.format( bomb.getAnimationSpeed() ),
						dFmt.format( bomb.getThrowVelocityLow() ),
						dFmt.format( bomb.getThrowVelocityHigh() )
						);
				
				messages.add( message );
				
				
				String msg2 = String.format( 
						"      &3FuseDelayTicks: &7%d   &3CooldownTicks: &7%d", 
						bomb.getFuseDelayTicks(),
						bomb.getCooldownTicks()
						);
				
				messages.add( msg2 );
				
				
				
				ExplosionShape shape = ExplosionShape.fromString( bomb.getExplosionShape() );
				String messageShape = null;
				switch ( shape )
				{
					case cube:
						{
							int lenght = 1 + (bomb.getRadius() * 2);
							messageShape = String.format( 
									"      &3Shape: &7%s   &3Size: &7%d &3x &7%d &3x &7%d   &3Based on Radius: &7%d.5", 
									bomb.getExplosionShape(), 
									lenght, lenght, lenght,
									bomb.getRadius() );
							break;
						}
					
					case sphereHollow:
					case ring_x:
					case ring_y:
					case ring_z:
					{
						messageShape = String.format( 
								"      &3Shape: &7%s   &3Radius: &7%d.5   &3RadiusInner: &7%d.5", 
								bomb.getExplosionShape(), bomb.getRadius(), bomb.getRadiusInner() );
						break;
					}
					
					case sphere:
					case disk_x:
					case disk_y:
					case disk_z:
					{
						messageShape = String.format( 
								"      &3Shape: &7%s   &3Radius: &7%d.5", 
								bomb.getExplosionShape(), bomb.getRadius() );
						break;
					}
					default:
					{
						messageShape = "      &4(no shape defined)";
					}
				}
				if ( messageShape != null && !messageShape.isEmpty() ) {
					
					messages.add( messageShape );
				}
				
				
				if ( optAll ) {
					
					String message2 = String.format( 
							"      &3ToolInHand: &7%s  &3Fortune: &7%d  &3Percent Chance: &7%f",
							bomb.getToolInHandName(), 
							bomb.getToolInHandFortuneLevel(),
							bomb.getRemovalChance() );
					messages.add( message2 );
					
					
					String message3 = String.format( 
							"      &3ItemType: &7%s  &3Autosell: &7%b  &3Glowng: &7%b   &3Gravity: &7%b",
							bomb.getItemType(), 
							bomb.isAutosell(),
							bomb.isGlowing(),
							bomb.isGravity() );
					messages.add( message3 );
				}
				
				
				messages.add( "      " + bomb.getDescription() );
				
				
				if ( optAll ) {
					
					
					List<String> sounds = new ArrayList<>();
					for ( MineBombEffectsData sfx : bomb.getSoundEffects() )
					{
						sounds.add( "      " + sfx.toString() );
						
					}
					if ( sounds.size() > 0 ) {
						messages.add( "    &3Sound Effects:" );
						messages.addAll( sounds );
					}
					
					
					List<String> visual = new ArrayList<>();
					for ( MineBombEffectsData vfx : bomb.getVisualEffects() )
					{
						visual.add( "      " + vfx.toStringShort() );
						
					}
					if ( visual.size() > 0 ) {
						messages.add( "    &3Visual Effects:" );
						messages.addAll( visual );
					}
					
					
					
				}
				
//				Output.get().log( message, LogLevel.PLAIN );
			}
			
			sender.sendMessage( messages.toArray( new String[0] ) );
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
					"bombs, with 'listall' including sound and visual effects. [list listall]", 
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
				
				utilsMineBombsList( sender, null );
				return;
			}
			
			if ( "listall".equalsIgnoreCase( bombName ) ) {
				
				utilsMineBombsList( sender, "all" );
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
				
				MineBombData bomb = mBombs.findBombByName( player, bombName);
				
//				// Remove color codes from bomb's name for matching:
//				bombName = Text.stripColor( bombName );
//				
//				Set<String> keys = mBombs.getConfigData().getBombs().keySet();
//				for ( String key : keys ) {
//					MineBombData mbd = mBombs.getConfigData().getBombs().get( key );
//					String cleanedBombName = Text.stripColor( mbd.getName() );
//					if ( cleanedBombName.equalsIgnoreCase( bombName ) ) {
//						bomb = mbd;
//						break;
//					}
//				}
				
				if ( bomb != null ) {
					
					ItemStack bombs = getItemStackBomb( bomb, player );
					
					if ( bombs != null ) {
						
						bombs.setAmount( count );
						player.getWrapper().getInventory().addItem( bombs );
						
						player.getWrapper().updateInventory();
//						player.updateInventory();
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
	
	
	public ItemStack getItemStackBomb( MineBombData bombData, SpigotPlayer player ) {
		ItemStack sItemStack = null;
		SpigotItemStack bombs = null;
//		NBTItem nbtItem = null;
		
		XMaterial xBomb = XMaterial.matchXMaterial( bombData.getItemType() ).orElse( null );
		
		if ( xBomb != null ) {
			
			
			try {
				
				// Create the spigot/bukkit ItemStack:
				sItemStack = xBomb.parseItem();
				
				if ( sItemStack != null ) {
					
					bombs = new SpigotItemStack( sItemStack );
				}
			} 
			catch (PrisonItemStackNotSupportedRuntimeException e) {
				// Ignore
			}
			
			if ( bombs != null ) {
				
				bombs.setDisplayName( bombData.getName() );

				if ( bombData.getItemName() != null ) {
					String itemName = bombData.getItemName().replace("{name}", bombData.getName() );
					
					bombs.setDisplayName( itemName );
				}
				
				//bombs.setAmount( count );
				
//				if ( bomb.isGlowing() ) {
//					bombs.addEnchantment(  );
//				}
				
				List<String> lore = new ArrayList<>( bombData.getLore() );
				
				// lore.add( 0, bombData.getLoreBombItemId() );
				
				bombs.setLore( lore );

				// SpigotCompatibility.getInstance().setCustomModelData( bombs, bombData.getCustomModelData() );
				
				sItemStack = bombs.getBukkitStack();
			}
			
			if ( sItemStack != null ) {
				
				// Set the NBT String key-value pair:
				PrisonNBTUtil.setNBTString(sItemStack, MineBombs.MINE_BOMBS_NBT_KEY, bombData.getName() );
				PrisonNBTUtil.setNBTString(sItemStack, MineBombs.MINE_BOMBS_NBT_OWNER_UUID, player.getUUID().toString() );
				
				
//				nbtItem = new NBTItem( sItemStack, true );
//				nbtItem.setString( MineBombs.MINE_BOMBS_NBT_BOMB_KEY, bombData.getName() );

				
				// Set the customModelData on the bomb to allow for custom skins:
				SpigotCompatibility.getInstance().setCustomModelData( sItemStack, bombData.getCustomModelData() );

				
				if ( Output.get().isDebug() ) {
					Output.get().logInfo( "getItemStackBombs ntb: %s", 
							Text.translateAmpColorCodes( PrisonNBTUtil.nbtDebugString(sItemStack) )
							 );
				}
			}
			
		}
		else {
			String message = String.format( 
					"Invalid MineBomb Item: Bomb: %s  Cannot map '%s' to an XMaterial.  " +
					"See this URL for valid XMaterial types: " +
					"https://github.com/CryptoMorin/XSeries/blob/master/src/main/java" +
					"/com/cryptomorin/xseries/XMaterial.java", bombData.getName(), bombData.getItemType() );
			
			Output.get().logError( message );
		}
		
		return sItemStack;
	}
	
	
	public boolean isItemABomb( Player player ) {
		
		MineBombData bomb = getBombItem( player );
		
		return ( bomb != null );
	}
	
	/**
	 * <p>This takes a player, and checks to see if it is a prison mine bomb by checking the 
	 * NBT tag in the constant: MINE_BOMBS_NBT_BOMB_KEY.  If it is, then it uses that key
	 * value to search for the proper bomb, and if found, then it returns it.
	 * </p>
	 * 
	 * 
	 * @param player
	 * @return MineBombData if item in hand is one, otherwise null
	 */
	public MineBombData getBombItem( Player player ) {
		MineBombData bomb = null;
		
		SpigotItemStack itemInHand = SpigotCompatibility.getInstance().getPrisonItemInMainHand( player );
		
		if ( itemInHand != null && itemInHand.hasNBTKey( MineBombs.MINE_BOMBS_NBT_KEY ) ) {
			
			String bombName = itemInHand.getNBTString( MineBombs.MINE_BOMBS_NBT_KEY );
			
			SpigotPlayer sPlayer = new SpigotPlayer( player );
			
			bomb = MineBombs.getInstance().findBombByName( sPlayer, bombName );
//			bomb = getBombItem( bombName );
		}
		
		return bomb;
	}
	
//	public MineBombData getBombItem( tech.mcprison.prison.internal.Player player, String bombName ) {
//		
//		MineBombData bomb = MineBombs.getInstance().findBombByName( player, bombName );
//		
//		return bomb;
//	}
	
	/**
	 * <p>This takes a player and checks their main hand to see if it contains a bomb.  
	 * If it does, then it return the selected bomb, and it reduces the quantity by
	 * one. 
	 * </p>
	 * 
	 * <p>It's important that this returns a "true" if this is a bomb, even though the 
	 * bomb cannot be activated.
	 * </p>
	 * 
	 * @param sPlayer
	 * @param bomb 
	 * @param sBlock 
	 * @param hand
	 * @return
	 */
	public boolean setBombInHand( SpigotPlayer sPlayer, 
					MineBombData bomb, 
					SpigotBlock sBlock, 
					tech.mcprison.prison.spigot.compat.Compatibility.EquipmentSlot hand ) {
		boolean isABomb = false;
		
//		MineBombData bomb = getBombItem( player );
		
		if ( bomb != null ) {
			
			
			String prisonExplosiveHandlerPriority = AutoFeaturesWrapper.getInstance().getMessage( 
					AutoFeatures.ProcessPrisons_ExplosiveBlockBreakEventsPriority );
			
			if ( bomb != null && "DISABLED".equalsIgnoreCase( prisonExplosiveHandlerPriority ) ) {
				
				Output.get().logWarn( "A Prison Mine Bomb was attempted to be used, but the " +
						"handling of its explosion is DISABLED.  Edit the 'autoFeaturesConfig.yml' " +
						"file and set 'ProcessPrisons_ExplosiveBlockBreakEventsPriority: NORMAL'." );
				
				// Must return a true from here on out so the original event is canceled.
				// Returning a true does not mean the bomb was activated.
				isABomb = true;
				
				return isABomb;
			}
			
			else if ( bomb != null ) {
				
				// Must return a true from here on out so the original event is canceled.
				// Returning a true does not mean the bomb was activated.
				isABomb = true;
				
//					XMaterial.GUNPOWDER;
//					XMaterial.BLAZE_POWDER;
//					XMaterial.WOODEN_PICKAXE;
//					XMaterial.STONE_PICKAXE;
//					XMaterial.IRON_PICKAXE;
//					XMaterial.GOLDEN_PICKAXE;
//					XMaterial.DIAMOND_PICKAXE;
//					XMaterial.NETHERITE_PICKAXE;
				
//					XMaterial.SLIME_BLOCK
				
				// if the toolInHand has not been set, use a diamond pickaxe:
				if ( bomb.getToolInHandName() == null ) {
					XMaterial xMat = XMaterial.DIAMOND_PICKAXE;
					bomb.setToolInHandName( xMat.name() );
				}
				
//				SpigotPlayer sPlayer = new SpigotPlayer( player );
				
//				String playerUUID = sPlayer.getUniqueId().toString();
//				String playerUUID = player.getUniqueId().toString();


//				int cooldownTicks = MineBombs.checkPlayerCooldown( sPlayer );
				
//				if ( cooldownTicks <= 0 ) 
				{
					
					
					// NOTE: cooldown is set when using MineBombs.findBombByName();
					// Set cooldown:
//					MineBombs.addPlayerCooldown( sPlayer, bomb.getCooldownTicks() );
					
					SpigotItemStack bombs = new SpigotItemStack( 
							getItemStackBomb( bomb, sPlayer ));
					
					if ( bombs != null ) {
						
						if ( sBlock == null ) {
							sBlock = (SpigotBlock) sPlayer.getLocation().getBlockAt();
						}
						
						SpigotBlock bombBlock = sBlock;
						
						
//    						// If the clicked on block is empty, then the player probably clicked on air.  
//    						// Check the next lower block (y - 1) until a valid block is found, or until y < 1
//    						while ( sBlock.isEmpty() && sBlock.getLocation().getBlockY() > 0 ) {
//    							
//    							Block tempBlock = sBlock.getLocation().getBlockAtDelta( 0, -1, 0 );
//    							if ( tempBlock != null && tempBlock instanceof SpigotBlock ) {
//    								sBlock = (SpigotBlock) tempBlock;
//    							}
//    							
//    							// if not empty, take one block deeper below the surface:
////    							if ( !sBlock.isEmpty() && sBlock.getLocation().getBlockY() > 0 ) {
////
////        							tempBlock = sBlock.getLocation().getBlockAtDelta( 0, -1, 0 );
////        							if ( tempBlock != null && tempBlock instanceof SpigotBlock ) {
////        								sBlock = (SpigotBlock) tempBlock;
////        							}
////    							}
//    						}
						
						
						
						// For mine bombs, take the block below where the bomb's item was dropped.  The floating 
						// item is not the block that needs to be the target block for the explosion.  Also, the block
						// if it is on top of the mine, would be identified as being outside of the mine.
						int count = 0;
						boolean isAir = bombBlock.isEmpty();
						while (  (count++ <= ( isAir ? 1 : 0 ) || bombBlock.isEmpty()) && bombBlock.getLocation().getBlockY() > 1 ) {
							
							int adjustY = bomb.getPlacementAdjustmentY();
							
							Block tempBlock = bombBlock.getLocation().getBlockAtDelta( 0, adjustY, 0 );
							if ( tempBlock != null && tempBlock instanceof SpigotBlock ) {
								bombBlock = (SpigotBlock) tempBlock;
							}
							
//    							Output.get().logInfo( 
//    									"#### PrisonUtilsMineBombs:  bomb y loc: " + bombBlock.getWrapper().getLocation().getBlockY() + 
//    										"  " + bombBlock.getLocation().getBlockY() + "  count= " + count );
						}
						
						bomb.setPlacedBombBlock( bombBlock );
						
//    						Output.get().logInfo( 
//    								"#### PrisonUtilsMineBombs:  bomb loc: " + bombBlock.getLocation().toWorldCoordinates() );
						
						//int throwSpeed = 2;
						
						
						
						// check if in a mine:
						OnBlockBreakMines obbm = new OnBlockBreakMines();
						Mine mine = obbm.findMine( sPlayer, bombBlock, null, null );
						
						if ( mine == null ) {
							// Cannot set the bomb outside of a mine, so cancel:
							return isABomb;
						}
						
						// Setting activated to true indicates the bomb is live and it has
						// been removed from the player's inventory:
						bomb.setActivated( true );
						
						
						SpigotItemStack itemInHand = 
								hand == EquipmentSlot.HAND ? 
										SpigotCompatibility.getInstance().getPrisonItemInMainHand( sPlayer ) :
											SpigotCompatibility.getInstance().getPrisonItemInOffHand( sPlayer )
											;
						
						// Remove from inventory:
						int inHandBombCount = itemInHand.getAmount() - 1;
						if ( inHandBombCount == 0 ) {
							if ( hand == EquipmentSlot.HAND ) {
								
								SpigotCompatibility.getInstance()
											.setItemInMainHand( sPlayer, null );
							}
							else {
								
								SpigotPlayerInventory sInventory = (SpigotPlayerInventory) sPlayer.getInventory();
								SpigotItemStack sItemStack = null;
								
								SpigotCompatibility.getInstance()
											.setItemStackInOffHand( sInventory, sItemStack );
							}
						}
						else {
							
							itemInHand.setAmount( inHandBombCount );
						}


						// Apply updates to the player's inventory:
						sPlayer.updateInventory();
						
						
					
						final SpigotBlock sBombBlock = bombBlock;
						MineBombDetonateTask detonateBomb = new MineBombDetonateTask() {

							@Override
							public void runDetonation() {
								
								// Submit the bomb's task to go off:
								setoffBombDelayed( sPlayer, bomb, sBombBlock );
							}
						};
						
						
						// This setups the animations that are assigned to each bomb type and 
						// will submit the tasks, unless no animations are chosen.
						BombAnimationsTask animationsTask = new BombAnimationsTask();
						
						animationsTask.animatorFactory( bomb, bombBlock, itemInHand, detonateBomb );
						

						
						
//						@SuppressWarnings( "unused" )
//						PlacedMineBombItemTask submitPlacedMineBombItem = 
//								submitPlacedMineBombItemTask( bomb, bombBlock, bombs );
//    						placeMineBombItem( bomb, bombBlock, bombs );
						
						// This places the item so it will float:
//    						final Item dropped = player.getWorld().dropItem( 
//    									bombBlock.getWrapper().getLocation(), bombs.getBukkitStack() );
						
//    						dropped.setPickupDelay( Integer.MAX_VALUE );
//    						dropped.setCustomName( bomb.getName() );
						//dropped.setVelocity(player.getLocation().getDirection().multiply( throwSpeed ).normalize() );
						
//    						int delayInTicks = 5 * 20; // 5 secs
						
						
						// If running MC 1.9.0 or higher, then can use the glowing feature.  Ignore for 1.8.x.
//    						if ( new BluesSpigetSemVerComparator().compareMCVersionTo( "1.9.0" ) >= 0 ) {
//    							
//    							dropped.setGlowing( bomb.isGlowing() );
//
//    							
//    							// setGravity is invalid for spigot 1.8.8:
//    							dropped.setGravity( bomb.isGravity() );
//    						}
						
						
						
						// Submit the bomb's task to go off:
						
//						setoffBombDelayed( sPlayer, bomb, bombBlock );
						
						
						
//    						dropped.setMetadata( "prisonMineBomb", new FixedMetadataValue( SpigotPrison.getInstance(), true ) );
						//dropped.setMetadata( "prisonMineName",  new FixedMetadataValue( SpigotPrison.getInstance(), "mineName" ) );
						
					}
				}
				
//				else {
//					
//					mineBombsCoolDownMsg( sPlayer, cooldownTicks );
//					
//				}
				
			}
		}
    	
    	return isABomb;
	}
	
	
	public boolean validateMineBombsSpigotVersion() {
		boolean results = true;
		
		MineBombs mBombs = MineBombs.getInstance();
		
		Map<String, MineBombData> bombs = mBombs.getConfigData().getBombs();
		
		Set<String> keys = bombs.keySet();
		for ( String key : keys ) {
			MineBombData mbData = bombs.get( key );
			
			List<MineBombEffectsData> deleteSounds = new ArrayList<>();
			for ( MineBombEffectsData sEffect : mbData.getSoundEffects() ) {
				
				if ( !utilsMineBombsValidate( "sounds", sEffect.getEffectName() ) ) {
					results = false;
					deleteSounds.add( sEffect );
					
					Output.get().logInfo( "MineBomb Validation Error: Invalid sound removed: %s : [%s]", 
							mbData.getName(), sEffect.toString() );
				}
			}
			if ( deleteSounds.size() > 0 ) {
				mbData.getSoundEffects().removeAll( deleteSounds );
			}

			List<MineBombEffectsData> deleteVisuals = new ArrayList<>();
			for ( MineBombEffectsData sEffect : mbData.getVisualEffects() ) {
				
				if ( !utilsMineBombsValidate( "visuals", sEffect.getEffectName() ) ) {
					results = false;
					deleteVisuals.add( sEffect );
					
					Output.get().logInfo( "MineBomb Validation Error: Invalid visual removed: %s : [%s]", 
							mbData.getName(), sEffect.toStringShort() );
				}
			}
			if ( deleteVisuals.size() > 0 ) {
				mbData.getVisualEffects().removeAll( deleteSounds );
			}
			
			if ( !utilsMineBombsValidate( "shapes", mbData.getExplosionShape() ) ) {
				results = false;
				
				Output.get().logInfo( "MineBomb Validation Error: Invalid shape changed to 'sphere': %s : [%s]", 
						mbData.getName(), mbData.getExplosionShape() );

				mbData.setExplosionShape( ExplosionShape.sphere.name() );
				
			}
			

		}
		
		if ( !results ) {
			// There was an invalid setting.  Save the changed configs:
			Output.get().logInfo( "MineBomb Validation: Saving mine bombs due to changes in the configs." ); 

			mBombs.saveConfigJson();
		}
		
		return results;
	}

	

	public void validateMineBommbEffect(MineBombEffectsData mineBombEffect) {
		
		if ( !isEnableMineBombs() ) {
			
			Output.get().logInfo( "Prison's utils command mine bombs is disabled in modules.yml." );
		}
		else if ( mineBombEffect == null ) {
			
			Output.get().logInfo( "Prison utils MineBomb: MineBombEffect is null. The specified effects cannot include nulls." );
		}
		else {
			
			switch ( mineBombEffect.getEffectType() ) {
			
			case sounds:
				
				{
					mineBombEffect.setValid( false );
					
					for ( XSound p : XSound.values() ) {
						if ( p.name().equalsIgnoreCase( mineBombEffect.getEffectName() ) ) {
							mineBombEffect.setValid( true );
							break;
						}
					}
				}
				
				break;
				
			case visuals:

				{
					mineBombEffect.setValid( false );
					
					// bukkit 1.8.8:
//					Effect.values()
					
					// If running less than MC 1.9.0, ie 1.8.x, then use different code for effects:
					boolean is1_8 = new BluesSemanticVersionComparator().compareMCVersionTo( "1.9.0" ) < 0 ;

					if ( is1_8 ) {
						for ( Effect p : Effect.values() ) {
							if ( p.name().equalsIgnoreCase( mineBombEffect.getEffectName() ) ) {
								mineBombEffect.setValid( true );
								break;
							}
						}
						
					}
					else {
						
						for ( Particle p : Particle.values() ) {
							if ( p.name().equalsIgnoreCase( mineBombEffect.getEffectName() ) ) {
								mineBombEffect.setValid( true );
								break;
							}
						}
					}	
				}
				
				break;

			default:
				break;
			}
			
			if ( mineBombEffect.getEffectType() == EffectType.sounds ) {
				
			}
			if ( mineBombEffect.getEffectType() == EffectType.visuals ) {
				
			}
		}
	}

	public boolean utilsMineBombsValidate( String mbObjectType,
			String name
			) {
		
		boolean results = false;
		
		if ( mbObjectType == null ) {
			Output.get().logInfo( "Prison util MineBomb: utilsMineBombsValidate: Error: objectType is null" );
		}
		else if ( name == null ) {
			Output.get().logInfo( "Prison util MineBomb: utilsMineBombsValidate: Error: name is null" );
		}
		
		else if ( !isEnableMineBombs() ) {
			
			Output.get().logInfo( "Prison's utils command mine bombs is disabled in modules.yml." );
		}
		else {
			
			
			if ( mbObjectType.equalsIgnoreCase( "shapes" ) ) {
				
				List<String> shapes = ExplosionShape.asList();
				for (String shape : shapes) {
					if ( shape.equalsIgnoreCase( name ) ) {
						results = true;
						break;
					}
				}

			}
			
			else if ( mbObjectType.equalsIgnoreCase( "sounds" ) ) {

				
				for ( XSound p : XSound.values() ) {
					if ( p.name().equalsIgnoreCase( name ) ) {
						results = true;
						break;
					}
				}

			}
			
			else if ( mbObjectType.equalsIgnoreCase( "visuals" ) ) {
				
				// bukkit 1.8.8:
//				Effect.values()
				
				// If running less than MC 1.9.0, ie 1.8.x, then use different code for effects:
				boolean is1_8 = new BluesSemanticVersionComparator().compareMCVersionTo( "1.9.0" ) < 0 ;

				if ( is1_8 ) {
					for ( Effect p : Effect.values() ) {
						if ( p.name().equalsIgnoreCase( name ) ) {
							results = true;
							break;
						}
					}
					
				}
				else {
					
					for ( Particle p : Particle.values() ) {
						if ( p.name().equalsIgnoreCase( name ) ) {
							results = true;
							break;
						}
					}
				}
			}
			
		}
		
		return results;
	}


	
//	public void placeMineBombItem( MineBombData bomb, SpigotBlock sBlock, SpigotItemStack item ) {
//	
//		
//		Location location = sBlock.getLocation();
//		
//		SpigotWorld sWorld = (SpigotWorld) location.getWorld();
//
//		
//		EulerAngle arm = new EulerAngle(1, 0, 0);
//		
//		ArmorStand as = sWorld.getWrapper().spawn( sWorld.getBukkitLocation( location ), ArmorStand.class);
//		as.setCustomName( bomb.getName() );
//		as.setCustomNameVisible(false);
//		as.setVisible(false);
//		as.setRemoveWhenFarAway(false);
//		as.setItemInHand( sWorld.getBukkitItemStack( item ) );
//		as.setRightArmPose(arm);
//		as.setRemoveWhenFarAway(false);
//		
//		if ( new BluesSpigetSemVerComparator().compareMCVersionTo( "1.9.0" ) >= 0 ) {
//			
//			as.setGlowing( bomb.isGlowing() );
//			
//			// setGravity is invalid for spigot 1.8.8:
//			as.setGravity( bomb.isGravity() );
//		}
//	}

	
	
//	protected short getFortune(SpigotItemStack itemInHand){
//		short results = (short) 0;
//		
//		try {
//			if ( itemInHand != null && 
//					itemInHand.getBukkitStack() != null && 
//					itemInHand.getBukkitStack().containsEnchantment( Enchantment.LOOT_BONUS_BLOCKS ) &&
//					itemInHand.getBukkitStack().getEnchantments() != null ) {
//				results = (short) itemInHand.getBukkitStack().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
//			}
//		}
//		catch ( NullPointerException e ) {
//			// Ignore. This happens when a TokeEnchanted tool is used when TE is not installed anymore.
//			// It throws this exception:  Caused by: java.lang.NullPointerException: null key in entry: null=5
//		}
//		
//		return results;
//	}


	
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
