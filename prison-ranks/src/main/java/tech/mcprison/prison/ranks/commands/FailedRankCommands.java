package tech.mcprison.prison.ranks.commands;

import java.util.List;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.BaseCommands;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.output.ChatDisplay;

public class FailedRankCommands
	extends BaseCommands
{

	
    public FailedRankCommands()
	{
		super( "FailedRanksNoEconomy" );
	}

	@Command(identifier = "ranks", description = "Ranks are Disabled - No Economy plugin detected", 
    								onlyPlayers = false )
    public void failedRanks( CommandSender sender ) {
        

        ChatDisplay display = new ChatDisplay("Prison Ranks are Disbled");
        
        display.addText("&aPrison Requires an Economy Plugin.");
        
        display.addText("");
        display.addText("Prison Ranks are disabled - No Economy plugin detected");
        display.addText("");
        display.addText("Add an Economy Plugin, such as EssentialsX, and then restart the server.");
        display.addText("");
        display.addText("For more information on how to setup Prison, see our extensive " +
        		"documentation that is online:");
        display.addText(". &7 https://prisonteam.github.io/Prison/prison_docs_000_toc.html");
        display.addText("");
        display.addText("Information on suggested plugins can be found here:");
        display.addText(". &7 https://prisonteam.github.io/Prison/prison_docs_012_setting_up_prison_basics.html");
        display.addText("");
        display.addText("If you need help with setting up prison, please see our documentation.");
        display.addText("If you find an issue with Prison, or need help for things not in the documenation, " +
        		"then please visit our discord server:");
        display.addText("");
        display.addText("");
        
		
        display.sendtoOutputLogInfo();
        
        // Broadcast to all online players:
        
        List<Player> onlinePlayers = Prison.get().getPlatform().getOnlinePlayers();
        
        for ( Player player : onlinePlayers ) {
			
        	display.send( player );
		}
        
//        if ( sender.isPlayer() ) {
//        	display.send( sender );
//        }
    	
    }


}
