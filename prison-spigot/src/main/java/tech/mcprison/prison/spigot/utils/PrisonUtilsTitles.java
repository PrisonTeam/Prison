package tech.mcprison.prison.spigot.utils;

import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.commands.Wildcard;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.game.SpigotPlayer;


/**
 * 
 * https://javadoc.io/doc/net.md-5/bungeecord-chat/1.16-R0.3/net/md_5/bungee/api/ChatMessageType.html
 * 
 * Player#spigot().sendMessage(ChatMessageType.ACTIONBAR, new TextComponent("Some message"))
 * Instead creating a new TextComponent, even better is TextComponent.fromLegacyText("Some message")
 * 
 * https://www.spigotmc.org/threads/send-actionbar.418827/
 *
 */
public class PrisonUtilsTitles
		extends PrisonUtils
{
	private boolean enableTitlesTitle = false;
//	private boolean enableTitlesSubtitle = false;
	private boolean enableTitlesActionBar = false;
//	private boolean enableTitlesClear = false;
//	private boolean enableTitlesReset = false;
//	private boolean enableTitlesTimes = false;


	public PrisonUtilsTitles() {
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
	

	@Command(identifier = "prison utils titles title", 
			description = "Displays a Title on the player's screen. " +
					"If the title is too large, it will overflow on the " +
					"sides and will not wrap or be reduced in size. To include a subtitle, " +
					"and/or an actionBar message, use a double colon between each section. " +
					"For example: 'Hello Title!::Subtitle is Here!::I'm on an actionBar!'",
		onlyPlayers = false, 
		permissions = "prison.utils.titles.title",
		altPermissions = "prison.utils.titles.title.others")
	public void utilsTitlesTitle( CommandSender sender, 
			@Arg(name = "playerName", description = "Player name") String playerName,
			
			@Wildcard(join=true)
			@Arg(name = "message", 
					description = "The message to display in the title.") String message 
			) {
		
		if ( !isEnableTitlesTitle() ) {
			
			Output.get().logInfo( "Prison's utils command titles title is disabled in modules.yml." );
		}
		else {
			
			SpigotPlayer player = checkPlayerPerms( sender, playerName, 
					"prison.utils.titles.title", "prison.utils.titles.title.others" );

			if ( player != null ) {
				
				if ( playerName != null && !playerName.equalsIgnoreCase( player.getName() ) ) {
					// Need to shift the player's name over to the message:
					
					message = playerName + " " + message;
					playerName = null;
				}
				
				String[] messages = message.split( "::" );
				
				String title = ( messages.length > 0 ? messages[0] : "");
				String subtitle = ( messages.length > 1 ? messages[1] : "");
				String actionBar = ( messages.length > 2 ? messages[2] : "");
				
				player.setTitle( title, subtitle, -1, -1, -1 );
				
				if ( actionBar != null && !actionBar.isEmpty() ) {
					
					player.setActionBar( actionBar );
				}
			}
		}
	}
	
	
	@Command(identifier = "prison utils titles actionBar", 
			description = "Displays an actionBar text on the player's screen. The actionBar is " +
					"located directly above the hot bar. ActionBar may not work on older " +
					"versions of spigot.",
					onlyPlayers = false, 
					permissions = "prison.utils.titles.actionbar",
					altPermissions = "prison.utils.titles.actionbar.others")
	public void utilsTitlesActionBar( CommandSender sender, 
			@Arg(name = "playerName", description = "Player name") String playerName,
			
			@Wildcard(join=true)
			@Arg(name = "message", 
				description = "The message to display in the action bar.") String message 
			) {
		
		if ( !isEnableTitlesActionBar() ) {
			
			Output.get().logInfo( "Prison's utils command titles action bar is disabled in modules.yml." );
		}
		else {
			
			SpigotPlayer player = checkPlayerPerms( sender, playerName, 
					"prison.utils.titles.actionbar", "prison.utils.titles.actionbar.others" );

			if ( player != null ) {
				
				
				
//				Player#spigot().sendMessage(ChatMessageType.ACTIONBAR, new TextComponent("Some message"))
				
				if ( playerName != null && !playerName.equalsIgnoreCase( player.getName() ) ) {
					// Need to shift the player's name over to the message:
					
					message = playerName + " " + message;
					playerName = null;
				}
				
				player.setActionBar( message );
				
			}
		}
	}
	
	
	
	public boolean isEnableTitlesTitle() {
		return enableTitlesTitle;
	}
	public void setEnableTitlesTitle( boolean enableTitlesTitle ) {
		this.enableTitlesTitle = enableTitlesTitle;
	}

	public boolean isEnableTitlesActionBar() {
		return enableTitlesActionBar;
	}
	public void setEnableTitlesActionBar( boolean enableTitlesActionBar ) {
		this.enableTitlesActionBar = enableTitlesActionBar;
	}

}
