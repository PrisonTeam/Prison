package tech.mcprison.prison.spigot.autofeatures;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.vk2gpz.tokenenchant.event.TEBlockExplodeEvent;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;

public class AutoManagerTokenEnchant 
	extends AutoManagerFeatures
	implements Listener {

	public AutoManagerTokenEnchant() {
        super();
        
    }
	
    @EventHandler(priority=EventPriority.LOW) 
    public void onTEBlockExplodeLow(TEBlockExplodeEvent e) {
    	if ( !e.isCancelled() ) {
    	    
    		genericBlockExplodeEventAutoManager( e, !isBoolean(AutoFeatures.isAutoManagerEnabled) );
    	}
    }
    
	
}
