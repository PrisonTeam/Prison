package tech.mcprison.prison.commands;

import java.util.Optional;
import java.util.UUID;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Player;

public abstract class BaseCommands
{
	private String cmdGroup;
	
	public BaseCommands( String cmdGroup ) {
		this.cmdGroup = cmdGroup;
	}

	public String getCmdGroup() {
		return cmdGroup;
	}
	public void setCmdGroup( String cmdGroup ) {
		this.cmdGroup = cmdGroup;
	}
	

    /**
     * <p>Gets a player by name.  If the player is not online, then try to get them from 
     * the offline player list. If not one is found, then return a null.
     * </p>
     * 
     * <p>The getOfflinePlayer() will now include RankPlayer as a fall back to help
     * ensure a player is always returned, if its a valid player.
     * </p>
     * 
     * <p>Never should this function return a Player based upon sender.
     * </p>
     * 
     * @param sender
     * @param playerName is optional, if not supplied, then sender will be used
     * @return Player if found, or null.
     */
	public Player getPlayerByName( // CommandSender sender, 
			String playerName ) {
		return getPlayerByName( // sender, 
				playerName, null );
	}
	
	/**
	 * <p>This function should only return a Player based upon either the player's name
	 * or their UUID.  It should never return a Player based upon sender.
	 * </p>
	 * 
	 * <p>Gets a player by name.  If the player is not online, then try to get them from 
     * the offline player list. If not one is found, then return a null.
     * </p>
     * 
     * <p>The getOfflinePlayer() will now include RankPlayer as a fall back to help
     * ensure a player is always returned, if its a valid player.
     * </p>
     * 
     * 
	 * @param sender
	 * @param playerName
	 * @param uuid
	 * @return
	 */
	public Player getPlayerByName( // CommandSender sender, 
			String playerName, UUID uuid ) {
		Player result = null;
		
		result = Prison.get().getPlatform().getRankPlayer( uuid, playerName );

		return result;
	}
	
	public Player getOnlinePlayer( CommandSender sender, String playerName ) {
		Player result = null;
		
		playerName = playerName != null && !playerName.trim().isEmpty() ? 
				playerName : sender != null ? sender.getName() : null;
		
		if ( playerName != null ) {
			Optional<Player> opt = Prison.get().getPlatform().getPlayer( playerName );

			if ( opt.isPresent() ) {
				result = opt.get();
			}
			
		}
		return result;
	}
	
}
