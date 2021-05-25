package tech.mcprison.prison.spigot.block;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.badbones69.crazyenchantments.api.events.BlastUseEvent;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;

public class OnBlockBreakEventCrazyEnchants
	extends OnBlockBreakEventListener
{
	public OnBlockBreakEventCrazyEnchants() {
		super();
	}
	

	public void registerBlastUseEvents( SpigotPrison spigotPrison ) {
		
    	boolean isCEBlockExplodeEnabled = isBoolean( AutoFeatures.isProcessCrazyEnchantsBlockExplodeEvents );
    	
    	if ( !isCEBlockExplodeEnabled ) {
    		return;
    	}
    	
		// Check to see if the class BlastUseEvent even exists:
		try {
			Output.get().logInfo( "OnBlockBreakEventListener: checking if loaded: CrazyEnchants" );
			
			Class.forName( "me.badbones69.crazyenchantments.api.events.BlastUseEvent", false, 
							this.getClass().getClassLoader() );
			
			Output.get().logInfo( "OnBlockBreakEventListener: Trying to register CrazyEnchants" );
			
			
			String cePriority = getMessage( AutoFeatures.CrazyEnchantsBlastUseEventPriority );
			BlockBreakPriority crazyEnchantsPriority = BlockBreakPriority.fromString( cePriority );
			
			// always register a monitor event:
			if ( crazyEnchantsPriority != BlockBreakPriority.DISABLED ) {
				Bukkit.getPluginManager().registerEvents( 
						new OnBlockBreakBlastUseEventListenerMonitor(), spigotPrison);
			}
			
			switch ( crazyEnchantsPriority )
			{
				case LOWEST:
					Bukkit.getPluginManager().registerEvents( 
							new OnBlockBreakBlastUseEventListenerLowest(), spigotPrison);
					break;
					
				case LOW:
					Bukkit.getPluginManager().registerEvents( 
							new OnBlockBreakBlastUseEventListenerLow(), spigotPrison);
					break;
					
				case NORMAL:
					Bukkit.getPluginManager().registerEvents( 
							new OnBlockBreakBlastUseEventListenerNormal(), spigotPrison);
					break;
					
				case HIGH:
					Bukkit.getPluginManager().registerEvents( 
							new OnBlockBreakBlastUseEventListenerHigh(), spigotPrison);
					break;
					
				case HIGHEST:
					Bukkit.getPluginManager().registerEvents( 
							new OnBlockBreakBlastUseEventListenerHighest(), spigotPrison);
					break;
				case DISABLED:
					Output.get().logInfo( "OnBlockBreakEventLisenters Crazy Enchant's BlastUseEvent " +
								"handling has been DISABLED." );
					break;
					
					
				default:
					break;
			}
			
		}
		catch ( ClassNotFoundException e ) {
			// CrazyEnchants is not loaded... so ignore.
			Output.get().logInfo( "OnBlockBreakEventListener: CrazyEnchants is not loaded" );
		}
	}

	

	   
    public class OnBlockBreakBlastUseEventListenerMonitor 
		extends OnBlockBreakEventListener
		implements Listener {
    	
        @EventHandler(priority=EventPriority.MONITOR) 
        public void onCrazyEnchantsBlockExplodeLow(BlastUseEvent e) {
        	super.onCrazyEnchantsBlockExplodeLow( e );
        }
    }
    
    public class OnBlockBreakBlastUseEventListenerLowest 
	    extends OnBlockBreakEventListener
	    implements Listener {
	    	
    	@EventHandler(priority=EventPriority.LOWEST) 
    	public void onCrazyEnchantsBlockExplodeLow(BlastUseEvent e) {
    		super.onCrazyEnchantsBlockExplodeLow( e );
    	}
    }
    
    public class OnBlockBreakBlastUseEventListenerLow 
	    extends OnBlockBreakEventListener
	    implements Listener {
    	
    	@EventHandler(priority=EventPriority.LOW) 
    	public void onCrazyEnchantsBlockExplodeLow(BlastUseEvent e) {
    		super.onCrazyEnchantsBlockExplodeLow( e );
    	}
    }
    
    public class OnBlockBreakBlastUseEventListenerNormal 
	    extends OnBlockBreakEventListener
	    implements Listener {
    	
    	@EventHandler(priority=EventPriority.NORMAL) 
    	public void onCrazyEnchantsBlockExplodeLow(BlastUseEvent e) {
    		super.onCrazyEnchantsBlockExplodeLow( e );
    	}
    }
    
    public class OnBlockBreakBlastUseEventListenerHigh 
	    extends OnBlockBreakEventListener
	    implements Listener {
    	
    	@EventHandler(priority=EventPriority.HIGH) 
    	public void onCrazyEnchantsBlockExplodeLow(BlastUseEvent e) {
    		super.onCrazyEnchantsBlockExplodeLow( e );
    	}
    }
    
    public class OnBlockBreakBlastUseEventListenerHighest 
	    extends OnBlockBreakEventListener
	    implements Listener {
    	
    	@EventHandler(priority=EventPriority.HIGHEST) 
    	public void onCrazyEnchantsBlockExplodeLow(BlastUseEvent e) {
    		super.onCrazyEnchantsBlockExplodeLow( e );
    	}
    }
    
}
