package tech.mcprison.prison.spigot.utils;

import java.util.Arrays;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;

import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.game.SpigotPlayer;

public class PrisonUtilsSounds
	extends PrisonUtils
{
	private boolean enableSoundEffects = false;
	
	public PrisonUtilsSounds() {
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
	
	
	@Command(identifier = "prison utils sounds", 
			description = "Plays a sound effect to a player",
		onlyPlayers = false, 
		permissions = "prison.utils.sounds",
		altPermissions = "prison.utils.sounds.others")
	public void utilSoundsPlay( CommandSender sender, 
			@Arg(name = "playerName", description = "Player name") String playerName,
			@Arg(name = "soundName", description = "A sound name. Use 'list <pageNumber>' to " +
					"get a list of availble sounds (50 per page).", def = "list") String soundName,
			
			@Arg(name = "volume", description = "Volume of a sound. " +
					"Optional. Default 10.0. (0.0 to 100.0).", def = "10.0") String volumeValue,
			@Arg(name = "pitch", description = "Pitch of a sound. " +
					"Optional. Default 1.0. (0.0 to 10.0)", def = "1.0") String pitchValue
			
//			,
//			@Wildcard(join=true)
//			@Arg(name = "options", 
//					description = "Options [op1 op2]", 
//					def = "") String options 
			) {
		
		if ( !isEnableSoundEffects() ) {
			
			Output.get().logInfo( "Prison's utils command sound is disabled in modules.yml." );
		}
		else {
			
			
			if ( "list".equalsIgnoreCase( playerName ) || "list".equalsIgnoreCase( soundName ) ) {

				int page = intValue( soundName, -1, 1, 100 );
				if ( page == -1 ) {
					page = intValue( volumeValue, 1, 1, 100 );
					
				}
				
				StringBuilder sb = new StringBuilder();
				
				int totalCount = Sound.values().length;
				int pageSize = 50;
				int start = (page - 1) * pageSize + 1;
				int end = start + pageSize - 1;
				int maxPage = (int) Math.ceil( totalCount / pageSize );
				
				Sound[] sounds = Arrays.copyOfRange( Sound.values(), start, end );
				
				for ( Sound sound : sounds ) {
					sb.append( sound.name().toLowerCase() ).append( ' ' );
				} 
				
				sender.sendMessage( String.format( "&7Valid sound names. Page %d of %d of %s: %s", 
						page, maxPage, totalCount, sb.toString() ) );
				
				return;
			}
			
			// First try to get the sound from the player name. Try playerName 
			// first since that's where it will be if playerName is not provided.
			Sound sound = soundFromString( playerName );
			if ( sound == null ) {
				// Try from potionName:
				sound = soundFromString( soundName );
			}
			else {
				// if sound was in playerName, then need to shift all parameters
				// over by one.
//				options = options == null ? duration : options + " " + duration;
				pitchValue = volumeValue;
				volumeValue = soundName;
				soundName = playerName;
				playerName = null;
			}
			
			if ( soundName == null ) {
				sender.sendMessage(
						"&7Unable to play a sound without a sound name. Please select " +
						"one and try again. Use 'list <pageNumber>' to see what's available." );
				
				return;
			}
			
			SpigotPlayer player = checkPlayerPerms( sender, playerName, 
									"prison.utils.sound", "prison.utils.sound.others" );
			
			
			/**
			 * The volume of the sound. This will range from a range of 0.0 to 100.0, with
			 * a default value of 10.0.
			 * 
			 */
			float volume = floatValue( volumeValue, 10.0f, 0.0f, 100.0f );
			
			
			/**
			 * The pitch of the sound. This will range from 0.0 to 10.0, with the
			 * default value of 1.0.
			 */
			float pitch = floatValue( pitchValue, 1.0f, 0.0f, 10.0f );
			
			
			LivingEntity entity = player.getWrapper();
			
			entity.getWorld().playSound( entity.getLocation(), sound, volume, pitch );
			
		}
	}
	
	
	public Sound soundFromString( String soundName ) {
		Sound results = null;
		
		for ( Sound sound : Sound.values() ) {
			if ( sound.name().equalsIgnoreCase( soundName ) ) {
				results = sound;
			}
		}
		
		return results;
	}

	public boolean isEnableSoundEffects() {
		return enableSoundEffects;
	}
	public void setEnableSoundEffects( boolean enableSoundEffects ) {
		this.enableSoundEffects = enableSoundEffects;
	}
	
}
