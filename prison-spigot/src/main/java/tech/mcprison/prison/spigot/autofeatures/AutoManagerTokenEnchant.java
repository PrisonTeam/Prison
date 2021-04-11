package tech.mcprison.prison.spigot.autofeatures;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.vk2gpz.tokenenchant.event.TEBlockExplodeEvent;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.block.OnBlockBreakEventListener.BlockBreakPriority;

public class AutoManagerTokenEnchant 
	extends AutoManagerFeatures
	{

	public AutoManagerTokenEnchant() {
        super();
        
    }
	
	
	public void registerBlockBreakEvents(SpigotPrison spigotPrison ) {
	
		
		String bbePriority = getMessage( AutoFeatures.TokenEnchantBlockExplodeEventPriority );
		BlockBreakPriority blockBreakPriority = BlockBreakPriority.fromString( bbePriority );
		
		switch ( blockBreakPriority )
		{
			case LOWEST:
				Bukkit.getPluginManager().registerEvents( 
						new AutoManagerTokenEnchantEventLowest(), spigotPrison);
				break;
				
			case LOW:
				Bukkit.getPluginManager().registerEvents( 
						new AutoManagerTokenEnchantEventLow(), spigotPrison);
				break;
				
			case NORMAL:
				Bukkit.getPluginManager().registerEvents( 
						new AutoManagerTokenEnchantEventNormal(), spigotPrison);
				break;
				
			case HIGH:
				Bukkit.getPluginManager().registerEvents( 
						new AutoManagerTokenEnchantEventHigh(), spigotPrison);
				break;
				
			case HIGHEST:
				Bukkit.getPluginManager().registerEvents( 
						new AutoManagerTokenEnchantEventHighest(), spigotPrison);
				break;

			case DISABLED:
				Output.get().logInfo( "AutoManager TokenEnchant BlockExplodeEvent handling has been DISABLED." );
				break;

			default:
				break;
		}
		
	}

	
    public void onTEBlockExplode(TEBlockExplodeEvent e) {
    	if ( !e.isCancelled() ) {
    	    
    		genericBlockExplodeEventAutoManager( e, !isBoolean(AutoFeatures.isAutoManagerEnabled) );
    	}
    }
    
    
    public class AutoManagerTokenEnchantEventLowest
		extends AutoManagerTokenEnchant
		implements Listener {
    	
        @EventHandler(priority=EventPriority.LOWEST) 
        public void onTEBlockExplodeLow(TEBlockExplodeEvent e) {
        	super.onTEBlockExplode( e );
        }
    }
    
    public class AutoManagerTokenEnchantEventLow
    extends AutoManagerTokenEnchant
    implements Listener {
    	
    	@EventHandler(priority=EventPriority.LOW) 
    	public void onTEBlockExplodeLow(TEBlockExplodeEvent e) {
    		super.onTEBlockExplode( e );
    	}
    }
    
    public class AutoManagerTokenEnchantEventNormal
    extends AutoManagerTokenEnchant
    implements Listener {
    	
    	@EventHandler(priority=EventPriority.NORMAL) 
    	public void onTEBlockExplodeLow(TEBlockExplodeEvent e) {
    		super.onTEBlockExplode( e );
    	}
    }
    
    public class AutoManagerTokenEnchantEventHigh
    extends AutoManagerTokenEnchant
    implements Listener {
    	
    	@EventHandler(priority=EventPriority.HIGH) 
    	public void onTEBlockExplodeLow(TEBlockExplodeEvent e) {
    		super.onTEBlockExplode( e );
    	}
    }
    
    public class AutoManagerTokenEnchantEventHighest
    extends AutoManagerTokenEnchant
    implements Listener {
    	
    	@EventHandler(priority=EventPriority.HIGHEST) 
    	public void onTEBlockExplodeLow(TEBlockExplodeEvent e) {
    		super.onTEBlockExplode( e );
    	}
    }
	
}
