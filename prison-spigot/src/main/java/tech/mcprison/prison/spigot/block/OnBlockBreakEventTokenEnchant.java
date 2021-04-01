package tech.mcprison.prison.spigot.block;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.vk2gpz.tokenenchant.event.TEBlockExplodeEvent;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.autofeatures.AutoManagerFeatures;
import tech.mcprison.prison.spigot.block.OnBlockBreakEventListener.BlockBreakPriority;

public class OnBlockBreakEventTokenEnchant
	extends AutoManagerFeatures
	{

	public OnBlockBreakEventTokenEnchant() {
	    super();
	    
	}

	
	public void registerBlockBreakEvents(SpigotPrison spigotPrison ) {
	
		
		// Always register the event Monitor
		Bukkit.getPluginManager().registerEvents( 
				new OnBlockBreakEventTokenEnchantMonitor(), spigotPrison);

		
		String bbePriority = getMessage( AutoFeatures.blockBreakEventPriority );
		BlockBreakPriority blockBreakPriority = BlockBreakPriority.fromString( bbePriority );
		
		switch ( blockBreakPriority )
		{
			case LOWEST:
				Bukkit.getPluginManager().registerEvents( 
						new OnBlockBreakEventTokenEnchantLowest(), spigotPrison);
				break;
				
			case LOW:
				Bukkit.getPluginManager().registerEvents( 
						new OnBlockBreakEventTokenEnchantLow(), spigotPrison);
				break;
				
			case NORMAL:
				Bukkit.getPluginManager().registerEvents( 
						new OnBlockBreakEventTokenEnchantNormal(), spigotPrison);
				break;
				
			case HIGH:
				Bukkit.getPluginManager().registerEvents( 
						new OnBlockBreakEventTokenEnchantHigh(), spigotPrison);
				break;
				
			case HIGHEST:
				Bukkit.getPluginManager().registerEvents( 
						new OnBlockBreakEventTokenEnchantHighest(), spigotPrison);
				break;

			default:
				break;
		}
		
	}


    
    
    public void onTEBlockExplodeLow(TEBlockExplodeEvent e) {

    	boolean isTEExplosiveEnabled = isBoolean( AutoFeatures.isProcessTokensEnchantExplosiveEvents );
    	
    	if ( isTEExplosiveEnabled ) {
    		
    		genericBlockExplodeEvent( e, !isBoolean(AutoFeatures.isAutoManagerEnabled) );
    	}
    }
    
    
    public class OnBlockBreakEventTokenEnchantMonitor
		extends OnBlockBreakEventTokenEnchant
		implements Listener {
    	
        @EventHandler(priority=EventPriority.MONITOR) 
        public void onTEBlockExplodeMonitor(TEBlockExplodeEvent e) {
        
        	genericBlockExplodeEventMonitor( e );
        }
    }
    
    
    public class OnBlockBreakEventTokenEnchantLowest
		extends OnBlockBreakEventTokenEnchant
		implements Listener {
    	
        @EventHandler(priority=EventPriority.LOWEST) 
        public void onTEBlockExplodeLow(TEBlockExplodeEvent e) {
        	super.onTEBlockExplodeLow( e );
        }
    }
    
    public class OnBlockBreakEventTokenEnchantLow
    extends OnBlockBreakEventTokenEnchant
    implements Listener {
    	
    	@EventHandler(priority=EventPriority.LOW) 
    	public void onTEBlockExplodeLow(TEBlockExplodeEvent e) {
    		super.onTEBlockExplodeLow( e );
    	}
    }
    
    public class OnBlockBreakEventTokenEnchantNormal
    extends OnBlockBreakEventTokenEnchant
    implements Listener {
    	
    	@EventHandler(priority=EventPriority.NORMAL) 
    	public void onTEBlockExplodeLow(TEBlockExplodeEvent e) {
    		super.onTEBlockExplodeLow( e );
    	}
    }
    
    public class OnBlockBreakEventTokenEnchantHigh
    extends OnBlockBreakEventTokenEnchant
    implements Listener {
    	
    	@EventHandler(priority=EventPriority.HIGH) 
    	public void onTEBlockExplodeLow(TEBlockExplodeEvent e) {
    		super.onTEBlockExplodeLow( e );
    	}
    }
    
    public class OnBlockBreakEventTokenEnchantHighest
    extends OnBlockBreakEventTokenEnchant
    implements Listener {
    	
    	@EventHandler(priority=EventPriority.HIGHEST) 
    	public void onTEBlockExplodeLow(TEBlockExplodeEvent e) {
    		super.onTEBlockExplodeLow( e );
    	}
    }
    
}
