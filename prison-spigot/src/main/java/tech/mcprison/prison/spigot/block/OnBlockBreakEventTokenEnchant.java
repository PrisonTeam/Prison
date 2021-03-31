package tech.mcprison.prison.spigot.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.vk2gpz.tokenenchant.event.TEBlockExplodeEvent;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.spigot.autofeatures.AutoManagerFeatures;

public class OnBlockBreakEventTokenEnchant
	extends AutoManagerFeatures
	implements Listener {

	public OnBlockBreakEventTokenEnchant() {
	    super();
	    
	}

	
    @EventHandler(priority=EventPriority.MONITOR) 
    public void onTEBlockExplodeMonitor(TEBlockExplodeEvent e) {
    
    	genericBlockExplodeEventMonitor( e );
    }
    
    
    @EventHandler(priority=EventPriority.LOW) 
    public void onTEBlockExplodeLow(TEBlockExplodeEvent e) {

    	boolean isTEExplosiveEnabled = isBoolean( AutoFeatures.isProcessTokensEnchantExplosiveEvents );
    	
    	if ( isTEExplosiveEnabled ) {
    		
    		genericBlockExplodeEvent( e, !isBoolean(AutoFeatures.isAutoManagerEnabled) );
    	}
    }
    
    
}
