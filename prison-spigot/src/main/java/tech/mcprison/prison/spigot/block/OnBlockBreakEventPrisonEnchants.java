package tech.mcprison.prison.spigot.block;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.pulsi_.prisonenchants.enchantments.custom.explosive.ExplosiveEvent;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;

public class OnBlockBreakEventPrisonEnchants
	extends OnBlockBreakEventListener
{
	public OnBlockBreakEventPrisonEnchants() {
		super();
	}
	

	public void registerExplosiveEvents( SpigotPrison spigotPrison ) {
	
		boolean isPEExplosiveEnabled = isBoolean( AutoFeatures.isProcessPrisonEnchantsExplosiveEvents );
		
		if ( !isPEExplosiveEnabled ) {
			return;
		}
	
//	ExplosiveEvent;
	
		// Check to see if the class BlastUseEvent even exists:
		try {
			Output.get().logInfo( "OnBlockBreakEventListener: checking if loaded: PrisonEnchants" );
			
			Class.forName( "me.pulsi_.prisonenchants.enchantments.custom.explosive.ExplosiveEvent", false, 
							this.getClass().getClassLoader() );
			
			Output.get().logInfo( "OnBlockBreakEventListener: Trying to register PrisonEnchants" );
			
			
			String pePriority = getMessage( AutoFeatures.PrisonEnchantsExplosiveEventPriority );
			BlockBreakPriority prisonEnchantsPriority = BlockBreakPriority.fromString( pePriority );
			
			// always register a monitor event:
			if ( prisonEnchantsPriority != BlockBreakPriority.DISABLED ) {
				Bukkit.getPluginManager().registerEvents( 
						new OnBlockBreakExplosiveEventListenerMonitor(), spigotPrison);
			}
			
			
			switch ( prisonEnchantsPriority )
			{
				case LOWEST:
					Bukkit.getPluginManager().registerEvents( 
							new OnBlockBreakExplosiveEventListenerLowest(), spigotPrison);
					break;
					
				case LOW:
					Bukkit.getPluginManager().registerEvents( 
							new OnBlockBreakExplosiveEventListenerLow(), spigotPrison);
					break;
					
				case NORMAL:
					Bukkit.getPluginManager().registerEvents( 
							new OnBlockBreakExplosiveEventListenerNormal(), spigotPrison);
					break;
					
				case HIGH:
					Bukkit.getPluginManager().registerEvents( 
							new OnBlockBreakExplosiveEventListenerHigh(), spigotPrison);
					break;
					
				case HIGHEST:
					Bukkit.getPluginManager().registerEvents( 
							new OnBlockBreakExplosiveEventListenerHighest(), spigotPrison);
					break;
				case DISABLED:
					Output.get().logInfo( "OnBlockBreakEventLisenters PrisonEnchants' ExplosiveEvent " +
								"handling has been DISABLED." );
					break;
					
					
				default:
					break;
			}
			
		}
		catch ( ClassNotFoundException e ) {
			// PrisonEnchants is not loaded... so ignore.
			Output.get().logInfo( "OnBlockBreakEventListener: PrisonEnchants is not loaded" );
		}
	}



   
	public class OnBlockBreakExplosiveEventListenerMonitor 
		extends OnBlockBreakEventListener
		implements Listener {
		
	    @EventHandler(priority=EventPriority.MONITOR) 
	    public void onPrisonEnchantsExplosiveEventMonitor(ExplosiveEvent e) {
	    	super.onPrisonEnchantsExplosiveEvent( e );
	    }
	}

	public class OnBlockBreakExplosiveEventListenerLowest 
	    extends OnBlockBreakEventListener
	    implements Listener {
	    	
		@EventHandler(priority=EventPriority.LOWEST) 
		public void onPrisonEnchantsExplosiveEventLowest(ExplosiveEvent e) {
			super.onPrisonEnchantsExplosiveEvent( e );
		}
	}
	
	public class OnBlockBreakExplosiveEventListenerLow 
	    extends OnBlockBreakEventListener
	    implements Listener {
		
		@EventHandler(priority=EventPriority.LOW) 
		public void onPrisonEnchantsExplosiveEventLow(ExplosiveEvent e) {
			super.onPrisonEnchantsExplosiveEvent( e );
		}
	}
	
	public class OnBlockBreakExplosiveEventListenerNormal 
	    extends OnBlockBreakEventListener
	    implements Listener {
		
		@EventHandler(priority=EventPriority.NORMAL) 
		public void onPrisonEnchantsExplosiveEventNormal(ExplosiveEvent e) {
			super.onPrisonEnchantsExplosiveEvent( e );
		}
	}
	
	public class OnBlockBreakExplosiveEventListenerHigh 
	    extends OnBlockBreakEventListener
	    implements Listener {
		
		@EventHandler(priority=EventPriority.HIGH) 
		public void onPrisonEnchantsExplosiveEventHigh(ExplosiveEvent e) {
			super.onPrisonEnchantsExplosiveEvent( e );
		}
	}
	
	public class OnBlockBreakExplosiveEventListenerHighest 
	    extends OnBlockBreakEventListener
	    implements Listener {
		
		@EventHandler(priority=EventPriority.HIGHEST) 
		public void onPrisonEnchantsExplosiveEventHighest(ExplosiveEvent e) {
			super.onPrisonEnchantsExplosiveEvent( e );
		}
	}
}
