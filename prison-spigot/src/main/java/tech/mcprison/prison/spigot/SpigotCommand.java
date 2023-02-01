package tech.mcprison.prison.spigot;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.customblock.PrisonItemsAdder;

public class SpigotCommand {
	
	public SpigotCommand() {
		super();
		
		// Register these commands:
		Prison.get().getCommandHandler().registerCommands( this );
	}

    @Command(identifier = "prison support test itemsAdder", 
    		description = "Initial test of accessing ItemsAdder.", 
    		onlyPlayers = false, permissions = "prison.admin" )
    public void testItemAdderCommand(CommandSender sender ) {
    	

    	PrisonItemsAdder pia = new PrisonItemsAdder();
    	
    	
    	Output.get().logInfo( "Prison Support: Starting to access ItemsAdder:" );
    	Output.get().logInfo( "  This is just a preliminary test just to identify if prison can access the "
    			+ "ItemsAddr list of custom blocks.  Once this can be verified, along with the format that "
    			+ "they are using, then Prison can be setup to utilize those items as custom blocks within "
    			+ "Prison.  Please copy and past these results to the discord server to the attention "
    			+ "of Blue." );
    	Output.get().logInfo( "  Will list all custom blocks: ItemsAdder.getAllItems() with only isBlock():" );
    	
    	pia.integrate();
    	

    	if ( pia.hasIntegrated() ) {
    		
    		pia.testCustomBlockRegistry();
    	}
    	else {
    		Output.get().logInfo( "Warning: Prison has not been able to establish a connection to "
    				+ "ItemsAdder.  Make sure it has been installed and is loading successfully." );
    	}
    	
    	
    	Output.get().logInfo( "Prison Support: Compleated tests with access to ItemsAdder:" );
    }
}
