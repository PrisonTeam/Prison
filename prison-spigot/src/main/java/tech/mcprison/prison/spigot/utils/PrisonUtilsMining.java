package tech.mcprison.prison.spigot.utils;

import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.game.SpigotPlayer;

public class PrisonUtilsMining
	extends PrisonUtils
{
	private boolean enableMiningSmelt = false;
	private boolean enableMiningBlock = false;

	public PrisonUtilsMining() {
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
	
	
	@Command(identifier = "prison utils smelt", 
			description = "Smelts all smetable items in a player's inventory, which include whats in their backpacks.",
		onlyPlayers = false, 
		permissions = "prison.utils.mining.smelt", 
		altPermissions = "prison.utils.mining.smelt.others")
	public void utilMiningSmelt(CommandSender sender, 
			@Arg(name = "playerName", description = "Player name") String playerName  ) {
		
		if ( !isEnableMiningSmelt() ) {
			
			Output.get().logInfo( "Prison's utils command smelt is disabled in modules.yml." );
		}
		else {
	
			SpigotPlayer player = checkPlayerPerms( sender, playerName, 
					"prison.utils.mining.smelt", 
					"prison.utils.mining.smelt.others" );
	
			// Player cannot be null.  If it is null, then there was a failure.
			if ( player != null && player.isOnline() ) {
				
				SpigotPrison.getInstance().getAutoFeatures().playerSmelt( player );
			}
		}
	}
	
	@Command(identifier = "prison utils block", 
			description = "Blocks all blocable items in a player's inventory, which include whats in their backpacks.",
			onlyPlayers = false, 
			permissions = "prison.utils.mining.block", 
			altPermissions = "prison.utils.mining.block.others")
	public void utilMiningBlock(CommandSender sender, 
			@Arg(name = "playerName", description = "Player name") String playerName  ) {
		
		if ( !isEnableMiningSmelt() ) {
			
			Output.get().logInfo( "Prison's utils command block is disabled in modules.yml." );
		}
		else {
			
			SpigotPlayer player = checkPlayerPerms( sender, playerName, 
					"prison.utils.mining.block", 
					"prison.utils.mining.block.others" );
			
			// Player cannot be null.  If it is null, then there was a failure.
			if ( player != null && player.isOnline() ) {
				
				SpigotPrison.getInstance().getAutoFeatures().playerBlock( player );
			}
		}
	}

	public boolean isEnableMiningSmelt() {
		return enableMiningSmelt;
	}
	public void setEnableMiningSmelt( boolean enableMiningSmelt ) {
		this.enableMiningSmelt = enableMiningSmelt;
	}

	public boolean isEnableMiningBlock() {
		return enableMiningBlock;
	}
	public void setEnableMiningBlock( boolean enableMiningBlock ) {
		this.enableMiningBlock = enableMiningBlock;
	}
	
}
