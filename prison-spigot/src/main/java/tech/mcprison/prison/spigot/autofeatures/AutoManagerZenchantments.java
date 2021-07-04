package tech.mcprison.prison.spigot.autofeatures;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.block.OnBlockBreakEventListener.BlockBreakPriority;
import zedly.zenchantments.BlockShredEvent;

public class AutoManagerZenchantments
	extends AutoManagerFeatures {

	public AutoManagerZenchantments() {
		super();
	}
	
	
	public void registerBlockBreakEvents(SpigotPrison spigotPrison ) {
	
		
		String zbsPriority = getMessage( AutoFeatures.ZenchantmentsBlockShredEventPriority );
		BlockBreakPriority blockShredPriority = BlockBreakPriority.fromString( zbsPriority );
		
		if ( blockShredPriority != BlockBreakPriority.DISABLED ) {
			
			// Always register the monitor event:
			Bukkit.getPluginManager().registerEvents( 
					new AutoManagerBlockShredEventListenerMonitor(), spigotPrison);
		}

		
		switch ( blockShredPriority )
		{
			case LOWEST:
				Bukkit.getPluginManager().registerEvents( 
						new AutoManagerBlockShredEventListenerLowest(), spigotPrison);
				break;
				
			case LOW:
				Bukkit.getPluginManager().registerEvents( 
						new AutoManagerBlockShredEventListenerLow(), spigotPrison);
				break;
				
			case NORMAL:
				Bukkit.getPluginManager().registerEvents( 
						new AutoManagerBlockShredEventListenerNormal(), spigotPrison);
				break;
				
			case HIGH:
				Bukkit.getPluginManager().registerEvents( 
						new AutoManagerBlockShredEventListenerHigh(), spigotPrison);
				break;
				
			case HIGHEST:
				Bukkit.getPluginManager().registerEvents( 
						new AutoManagerBlockShredEventListenerHighest(), spigotPrison);
				break;

			case DISABLED:
				Output.get().logInfo( "AutoManager Zenchantments BlockShredEvent handling has been DISABLED." );
				break;

			default:
				break;
		}
		
	}
	
	  
    public class AutoManagerBlockShredEventListenerMonitor 
	    extends AutoManager
	    implements Listener {
    	
    	@EventHandler(priority=EventPriority.MONITOR) 
    	public void onBlockShredBreakMonitor(BlockShredEvent e) {
    		super.genericBlockEventMonitor( e );
    	}
    }
    
    public class AutoManagerBlockShredEventListenerLowest 
		extends AutoManager
		implements Listener {
        
        @EventHandler(priority=EventPriority.LOWEST) 
        public void onBlockShredBreak(BlockShredEvent e) {
        	super.onBlockShredBreak( e );
        }
    }
    
    public class AutoManagerBlockShredEventListenerLow 
	    extends AutoManager
	    implements Listener {
    	
    	@EventHandler(priority=EventPriority.LOW) 
    	public void onBlockShredBreak(BlockShredEvent e) {
    		super.onBlockShredBreak( e );
    	}
    }
    
    public class AutoManagerBlockShredEventListenerNormal 
	    extends AutoManager
	    implements Listener {
    	
    	@EventHandler(priority=EventPriority.NORMAL) 
    	public void onBlockShredBreak(BlockShredEvent e) {
    		super.onBlockShredBreak( e );
    	}
    }
    
    public class AutoManagerBlockShredEventListenerHigh 
	    extends AutoManager
	    implements Listener {
	    	
    	@EventHandler(priority=EventPriority.HIGH) 
    	public void onBlockShredBreak(BlockShredEvent e) {
    		super.onBlockShredBreak( e );
    	}
    }
    
    public class AutoManagerBlockShredEventListenerHighest 
    extends AutoManager
    implements Listener {
    	
    	@EventHandler(priority=EventPriority.HIGHEST) 
    	public void onBlockShredBreak(BlockShredEvent e) {
    		super.onBlockShredBreak( e );
    	}
    }

}
