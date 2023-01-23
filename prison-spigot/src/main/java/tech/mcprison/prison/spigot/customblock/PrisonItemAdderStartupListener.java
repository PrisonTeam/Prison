package tech.mcprison.prison.spigot.customblock;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;

public class PrisonItemAdderStartupListener
	implements Listener {

	
	public PrisonItemAdderStartupListener() {
		super();
		
		initialize();
	}
	
	public void initialize() {
		
		
		// Check to see if the class BlastUseEvent even exists:
		try {
			Output.get().logInfo( "PrisonItemAdderStartupListener: checking if loaded: ItemsAdder" );
			
			Class.forName( "dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent", false, 
					this.getClass().getClassLoader() );
			
			Output.get().logInfo( "PrisonItemAdderStartupListener: Trying to register ItemsAdder" );
			
			
			SpigotPrison prison = SpigotPrison.getInstance();
			PluginManager pm = Bukkit.getServer().getPluginManager();
			
			pm.registerEvents( this, prison);
			
			
		}
		catch ( ClassNotFoundException e ) {
			// CrazyEnchants is not loaded... so ignore.
			Output.get().logInfo( "PrisonItemAdderStartupListener: ItemsAdder is not loaded" );
		}
		catch ( Exception e ) {
			Output.get().logInfo( 
					"PrisonItemAdderStartupListener: ItemsAdder failed to load. [%s]", e.getMessage() );
		}

	}
	
	@EventHandler
	public void onItemsAdderStartup( ItemsAdderLoadDataEvent e )  {
		
		SpigotPrison prison = SpigotPrison.getInstance();

		PrisonItemsAdder pia = new PrisonItemsAdder();
		
		prison.registerIntegration( pia );
	}
}
