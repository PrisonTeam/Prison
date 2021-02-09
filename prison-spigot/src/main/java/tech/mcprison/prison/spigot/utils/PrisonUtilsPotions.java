package tech.mcprison.prison.spigot.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.commands.Wildcard;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.game.SpigotPlayer;

public class PrisonUtilsPotions
	extends PrisonUtils
{

	private boolean enablePotionEffects = false;
	private boolean enablePotions = false;
	
	private List<String> potionEffectsList;
	
	public enum PotionEffectOptions {
		ambient,
		particles, 
		icon,
		forceConfictRemoval;
		
		public static PotionEffectOptions fromString( String value ) {
			PotionEffectOptions results = null;
			
			for ( PotionEffectOptions opt : values() ) {
				if ( opt.name().equalsIgnoreCase( value ) ) {
					results = opt;
					break;
				}
			}
			
			return results;
		}
	}
	
	public PrisonUtilsPotions() {
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
	
	
	@Command(identifier = "prison utils potion", 
			description = "Apply a potion effect to a player",
		onlyPlayers = false, 
		permissions = "prison.utils.potion",
		altPermissions = "prison.utils.potion.others")
	public void utilPotionEffects( CommandSender sender, 
			@Arg(name = "playerName", description = "Player name") String playerName,
			@Arg(name = "potionName", description = "A potion effect name. Use 'list' to " +
					"get a list of availble potions.", def = "list") String potionName,
			
			@Arg(name = "duration", description = "Duration of a potion in ticks. " +
					"Optional. Default 5 seconds.", def = "100") String duration,
			@Arg(name = "amplifier", description = "Amplifier of a potion. " +
					"Optional. Default 5 seconds.", def = "100") String amplifier,
			
			@Wildcard(join=true)
			@Arg(name = "options", 
					description = "Options [ambient particles icon forceConfictRemoval]", 
					def = "") String options ) {
		
		if ( !isEnablePotionEffects() ) {
			
			Output.get().logInfo( "Prison's utils command potionEffects is disabled in modules.yml." );
		}
		else {
			
			if ( "list".equalsIgnoreCase( playerName ) || "list".equalsIgnoreCase( potionName ) ) {
				
				StringBuilder sb = new StringBuilder();
				
				for ( PotionType pType : PotionType.values() ) {
					sb.append( pType.name().toLowerCase() ).append( ' ' );
				} 
				
//				for ( PotionEffectType effect : PotionEffectType.values() ) {
//					if ( effect != null && effect.getName() != null ) {
//						sb.append( effect.getName().toLowerCase() ).append( ' ' );
//					}
//				}
				
				sender.sendMessage( String.format( "&7Valid potion names: %s", sb.toString() ) );
				
				return;
			}
			
			// First try to get the potion from the player name. Try playerName 
			// first since that's where it will be if playerName is not provided.
			PotionEffectType potion = potionFromString( playerName );
			if ( potion == null ) {
				// Try from potionName:
				potion = potionFromString( potionName );
			}
			else {
				// if potion was in playerName, then need to shift all parameters
				// over by one.
				options = options == null ? amplifier : options + " " + amplifier;
				amplifier = duration;
				duration = potionName;
				potionName = playerName;
				playerName = null;
			}
			
			if ( potion == null ) {
				sender.sendMessage(
						"&7Unable to apply a potion effect without a potion. Please select " +
						"one and try again. Use 'list' to see what's available." );
				
				return;
			}
			
			SpigotPlayer player = checkPlayerPerms( sender, playerName, 
									"prison.utils.potion", "prison.utils.potion.others" );
			
			/**
			 * An amplifier value ranges from 0 to 256. Anything above 128 is the same as 
			 * negative values.  Values above around 100 may not behave as expected, and 
			 * values above 128 (or negative) may never be predictable. Values outside
			 * the range of 0 to 256 just wraps around (high order bits are ignored) and
			 * maybe be unpredictable.
			 */
			int ampliferValue = intValue( amplifier, 0, 0, 256 );
			
			
			/**
			 * The duration is in ticks, at 20 per second, and last for 50 milliseconds
			 * each under ideal conditions.  Lag can drop the TPS so a value of 20 is
			 * never guarenteed to be exactly 1 second.
			 */
			int durationTicks = intValue( duration, 100, 0, 64000 );
			
			
			
			List<PotionEffectOptions> effects = new ArrayList<>();
			
			if ( options != null && options.trim().isEmpty() ) {
				String[] opts = options.trim().split( " " );
				for ( String opt : opts ) {
					PotionEffectOptions effect = PotionEffectOptions.fromString( opt );
					
					if ( effect != null ) {
						effects.add( effect );
					}
				}
			}
			
			
			boolean isAmbient = effects.contains( PotionEffectOptions.ambient );
			boolean isParticles = effects.contains( PotionEffectOptions.particles );
			boolean isIcon = effects.contains( PotionEffectOptions.icon );
			
			boolean isForceConfictRemoval = effects.contains( PotionEffectOptions.forceConfictRemoval );
			
			PotionEffect potionEffect = new PotionEffect( potion, durationTicks, ampliferValue, 
					isAmbient, isParticles, isIcon );

			if ( potionEffect != null ) {
				player.getWrapper().addPotionEffect( potionEffect, isForceConfictRemoval );
			}
			
		}
	}
	


	private PotionEffectType potionFromString( String potionName ) {
		PotionEffectType results = null;
		
		for ( String potion : getPotionEffectList() ) {
			if ( potion.equalsIgnoreCase( potionName ) ) {
				results = PotionEffectType.getByName( potion );
			}
		}
		
		return results;
	}
	
	private List<String> getPotionEffectList() {
		if ( potionEffectsList == null ) {
			
			potionEffectsList = new ArrayList<>();
			
			for ( PotionEffectType potion : PotionEffectType.values() ) {
				potionEffectsList.add( potion.getName() );
			}
		}
		return potionEffectsList;
	}

	
	public boolean isEnablePotionEffects() {
		return enablePotionEffects;
	}
	public void setEnablePotionEffects( boolean enablePotionEffects ) {
		this.enablePotionEffects = enablePotionEffects;
	}

	public boolean isEnablePotions() {
		return enablePotions;
	}
	public void setEnablePotions( boolean enablePotions ) {
		this.enablePotions = enablePotions;
	}

}
