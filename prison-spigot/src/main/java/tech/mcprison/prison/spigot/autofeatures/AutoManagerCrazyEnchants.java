package tech.mcprison.prison.spigot.autofeatures;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.PluginManager;

import me.badbones69.crazyenchantments.api.events.BlastUseEvent;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.LogLevel;
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
		
		new AutoManagerBlastUseEventListener().initialize();
		
//    	boolean isCEBlockExplodeEnabled = isBoolean( AutoFeatures.isProcessCrazyEnchantsBlockExplodeEvents );
//    	
//    	if ( !isCEBlockExplodeEnabled ) {
//    		return;
//    	}
//		
//		// Check to see if the class BlastUseEvent even exists:
//		try {
//			Output.get().logInfo( "AutoManager: checking if loaded: CrazyEnchants" );
//			
//			Class.forName( "me.badbones69.crazyenchantments.api.events.BlastUseEvent", false, 
//							this.getClass().getClassLoader() );
//			
//			Output.get().logInfo( "AutoManager: Trying to register CrazyEnchants" );
//			
//			String cePriority = getMessage( AutoFeatures.CrazyEnchantsBlastUseEventPriority );
//			BlockBreakPriority crazyEnchantsPriority = BlockBreakPriority.fromString( cePriority );
//			
//			
//
//			
//			switch ( crazyEnchantsPriority )
//			{
//				case LOWEST:
//					Bukkit.getPluginManager().registerEvents( 
//							new AutoManagerBlastUseEventListenerLowest(), spigotPrison);
//					break;
//					
//				case LOW:
//					Bukkit.getPluginManager().registerEvents( 
//							new AutoManagerBlastUseEventListenerLow(), spigotPrison);
//					break;
//					
//				case NORMAL:
//					Bukkit.getPluginManager().registerEvents( 
//							new AutoManagerBlastUseEventListenerNormal(), spigotPrison);
//					break;
//					
//				case HIGH:
//					Bukkit.getPluginManager().registerEvents( 
//							new AutoManagerBlastUseEventListenerHigh(), spigotPrison);
//					break;
//					
//				case HIGHEST:
//					Bukkit.getPluginManager().registerEvents( 
//							new AutoManagerBlastUseEventListenerHighest(), spigotPrison);
//					break;
//				case DISABLED:
//					Output.get().logInfo( "AutoManager Crazy Enchant's BlastUseEvent handling has been DISABLED." );
//					break;
//					
//					
//				default:
//					break;
//			}
//			
//		}
//		catch ( ClassNotFoundException e ) {
//			// CrazyEnchants is not loaded... so ignore.
//			Output.get().logInfo( "AutoManager: CrazyEnchants is not loaded" );
//		}
	}

	
	
	public class AutoManagerBlastUseEventListener
		extends AutoManager
		implements Listener {
		
		@EventHandler(priority=EventPriority.LOW) 
		public void onCrazyEnchantsBlockExplode(BlastUseEvent e) {
			super.onCrazyEnchantsBlockExplode( e );
		}
		
		public void initialize() {
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

				
				String ceP = getMessage( AutoFeatures.CrazyEnchantsBlastUseEventPriority );
				BlockBreakPriority cePriority = BlockBreakPriority.fromString( ceP );

				if ( cePriority != BlockBreakPriority.DISABLED ) {
					
					EventPriority ePriority = EventPriority.valueOf( cePriority.name().toUpperCase() );           
					
					PluginManager pm = Bukkit.getServer().getPluginManager();

					pm.registerEvent(BlastUseEvent.class, this, ePriority,
							new EventExecutor() {
								public void execute(Listener l, Event e) { 
									((AutoManagerBlastUseEventListener)l)
													.onCrazyEnchantsBlockExplode((BlastUseEvent)e);
								}
							},
							SpigotPrison.getInstance());
					
				}
				
				// The following is paper code:
//				var executor = EventExecutor
//						.create( AutoManagerBlastUseEventListener.class
//								.getDeclaredMethod( "onCrazyEnchantsBlockExplode", BlastUseEvent.class ),
//								BlastUseEvent.class );
//				
//				Bukkit.getServer().getPluginManager()
//					.register( BlastUseEvent.class, this, EventPriority.LOW, executor, SpigotPrison.getInstance() );
			}
			catch ( ClassNotFoundException e ) {
				// CrazyEnchants is not loaded... so ignore.
				Output.get().logInfo( "AutoManager: CrazyEnchants is not loaded" );
			}
			catch ( Exception e ) {
				Output.get().logInfo( "AutoManager: CrazyEnchants failed to load. [%s]", e.getMessage() );
			}
		}
	}
   
    	boolean isCEBlockExplodeEnabled = isBoolean( AutoFeatures.isProcessCrazyEnchantsBlockExplodeEvents );
    	
    	if ( !isCEBlockExplodeEnabled ) {
    		return;
    	}
		
		// Check to see if the class BlastUseEvent even exists:
		try {
			Output.get().logInfo( "AutoManager: checking if loaded: CrazyEnchants" );
			
			Class.forName( "me.badbones69.crazyenchantments.api.events.BlastUseEvent", false, 
							this.getClass().getClassLoader() );
			
			}
		}
		catch ( ClassNotFoundException e ) {
			// CrazyEnchants is not loaded... so ignore.
		}
	}
    
//    public class AutoManagerBlastUseEventListenerLowest 
//		extends AutoManager
//		implements Listener {
//    	
//        @EventHandler(priority=EventPriority.LOWEST) 
//        public void onCrazyEnchantsBlockExplodeLow(BlastUseEvent e) {
//        	super.onCrazyEnchantsBlockExplode( e );
//        }
//    }
//    
//    public class AutoManagerBlastUseEventListenerLow 
//	    extends AutoManager
//	    implements Listener {
//    	
//    	@EventHandler(priority=EventPriority.LOW) 
//    	public void onCrazyEnchantsBlockExplodeLow(BlastUseEvent e) {
//    		super.onCrazyEnchantsBlockExplode( e );
//    	}
//    }
//    
//    public class AutoManagerBlastUseEventListenerNormal 
//	    extends AutoManager
//	    implements Listener {
//    	
//    	@EventHandler(priority=EventPriority.NORMAL) 
//    	public void onCrazyEnchantsBlockExplodeLow(BlastUseEvent e) {
//    		super.onCrazyEnchantsBlockExplode( e );
//    	}
//    }
//    
//    public class AutoManagerBlastUseEventListenerHigh 
//	    extends AutoManager
//	    implements Listener {
//    	
//    	@EventHandler(priority=EventPriority.HIGH) 
//    	public void onCrazyEnchantsBlockExplodeLow(BlastUseEvent e) {
//    		super.onCrazyEnchantsBlockExplode( e );
//    	}
//    }
//    
//    public class AutoManagerBlastUseEventListenerHighest 
//	    extends AutoManager
//	    implements Listener {
//    	
//    	@EventHandler(priority=EventPriority.HIGHEST) 
//    	public void onCrazyEnchantsBlockExplodeLow(BlastUseEvent e) {
//    		super.onCrazyEnchantsBlockExplode( e );
//    	}
//    }
    

}
