package tech.mcprison.prison.spigot.autofeatures;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.badbones69.crazyenchantments.api.events.BlastUseEvent;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.block.OnBlockBreakEventListener.BlockBreakPriority;

public class AutoManagerCrazyEnchants
	extends AutoManagerFeatures
{
	
	public AutoManagerCrazyEnchants() {
		super();
	}
	
	public void registerBlastUseEvents( SpigotPrison spigotPrison ) {
		
    	boolean isCEBlockExplodeEnabled = isBoolean( AutoFeatures.isProcessCrazyEnchantsBlockExplodeEvents );
    	
    	if ( !isCEBlockExplodeEnabled ) {
    		return;
    	}
		
		// Check to see if the class BlastUseEvent even exists:
		try {
			Output.get().logInfo( "AutoManager: checking if loaded: CrazyEnchants" );
			
			Class.forName( "me.badbones69.crazyenchantments.api.events.BlastUseEvent", false, 
							this.getClass().getClassLoader() );
			
			Output.get().logInfo( "AutoManager: Trying to register CrazyEnchants" );
			
			String cePriority = getMessage( AutoFeatures.CrazyEnchantsBlastUseEventPriority );
			BlockBreakPriority crazyEnchantsPriority = BlockBreakPriority.fromString( cePriority );
			
			switch ( crazyEnchantsPriority )
			{
				case LOWEST:
					Bukkit.getPluginManager().registerEvents( 
							new AutoManagerBlastUseEventListenerLowest(), spigotPrison);
					break;
					
				case LOW:
					Bukkit.getPluginManager().registerEvents( 
							new AutoManagerBlastUseEventListenerLow(), spigotPrison);
					break;
					
				case NORMAL:
					Bukkit.getPluginManager().registerEvents( 
							new AutoManagerBlastUseEventListenerNormal(), spigotPrison);
					break;
					
				case HIGH:
					Bukkit.getPluginManager().registerEvents( 
							new AutoManagerBlastUseEventListenerHigh(), spigotPrison);
					break;
					
				case HIGHEST:
					Bukkit.getPluginManager().registerEvents( 
							new AutoManagerBlastUseEventListenerHighest(), spigotPrison);
					break;
				case DISABLED:
					Output.get().logInfo( "AutoManager Crazy Enchant's BlastUseEvent handling has been DISABLED." );
					break;
					
					
				default:
					break;
			}
			
		}
		catch ( ClassNotFoundException e ) {
			// CrazyEnchants is not loaded... so ignore.
			Output.get().logInfo( "AutoManager: CrazyEnchants is not loaded" );
		}
	}

   
    
    public class AutoManagerBlastUseEventListenerLowest 
		extends AutoManager
		implements Listener {
    	
        @EventHandler(priority=EventPriority.LOWEST) 
        public void onCrazyEnchantsBlockExplodeLow(BlastUseEvent e) {
        	super.onCrazyEnchantsBlockExplodeLow( e );
        }
    }
    
    public class AutoManagerBlastUseEventListenerLow 
	    extends AutoManager
	    implements Listener {
    	
    	@EventHandler(priority=EventPriority.LOW) 
    	public void onCrazyEnchantsBlockExplodeLow(BlastUseEvent e) {
    		super.onCrazyEnchantsBlockExplodeLow( e );
    	}
    }
    
    public class AutoManagerBlastUseEventListenerNormal 
	    extends AutoManager
	    implements Listener {
    	
    	@EventHandler(priority=EventPriority.NORMAL) 
    	public void onCrazyEnchantsBlockExplodeLow(BlastUseEvent e) {
    		super.onCrazyEnchantsBlockExplodeLow( e );
    	}
    }
    
    public class AutoManagerBlastUseEventListenerHigh 
	    extends AutoManager
	    implements Listener {
    	
    	@EventHandler(priority=EventPriority.HIGH) 
    	public void onCrazyEnchantsBlockExplodeLow(BlastUseEvent e) {
    		super.onCrazyEnchantsBlockExplodeLow( e );
    	}
    }
    
    public class AutoManagerBlastUseEventListenerHighest 
	    extends AutoManager
	    implements Listener {
    	
    	@EventHandler(priority=EventPriority.HIGHEST) 
    	public void onCrazyEnchantsBlockExplodeLow(BlastUseEvent e) {
    		super.onCrazyEnchantsBlockExplodeLow( e );
    	}
    }
    

}
