package tech.mcprison.prison.spigot.utils;

import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.Output;

public class PrisonUtilsDecay
	extends PrisonUtils
{
	
	private boolean enableDecayObby = false;
	
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
	
	
//	@Command(identifier = "prison utils decay obby", 
//			description = "Decays a block from obby to the target blocks",
//		onlyPlayers = false, 
//		permissions = "prison.utils.decay.obby", 
//		altPermissions = "prison.utils.decay.obby.others")
	public void utilDecayObby(CommandSender sender, 
			@Arg(name = "blockSourceExtended", 
				description = "Extended block that represents the source (location & original type") 
						String blockSourceExtended,
			@Arg(name = "blockTargetName", description = "Target block type after decaying.") 
						String blockTargetName,
			@Arg(name = "decayTimeTicks", description = "The length of time the decay lasts, in ticks")
						long decayTimeTicks
		
		 ) {
	
		if ( !isEnableDecayObby() ) {
			
			Output.get().logInfo( "Prison's utils command decayObby is disabled in modules.yml." );
		}
		else {
	
			
			
			
		}
	}


	public boolean isEnableDecayObby() {
		return enableDecayObby;
	}
	public void setEnableDecayObby( boolean enableDecayObby ) {
		this.enableDecayObby = enableDecayObby;
	}

}
