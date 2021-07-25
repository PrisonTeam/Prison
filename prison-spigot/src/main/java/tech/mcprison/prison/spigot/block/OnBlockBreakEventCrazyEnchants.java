package tech.mcprison.prison.spigot.block;

public class OnBlockBreakEventCrazyEnchants
//	extends OnBlockBreakEventListener
{
	public OnBlockBreakEventCrazyEnchants() {
		super();
	}
	

//	public void registerBlastUseEvents( SpigotPrison spigotPrison ) {
//		
//    	boolean isCEBlockExplodeEnabled = isBoolean( AutoFeatures.isProcessCrazyEnchantsBlockExplodeEvents );
//    	
//    	if ( !isCEBlockExplodeEnabled ) {
//    		return;
//    	}
//    	
//		// Check to see if the class BlastUseEvent even exists:
//		try {
//			Output.get().logInfo( "OnBlockBreakEventListener: checking if loaded: CrazyEnchants" );
//			
//			Class.forName( "me.badbones69.crazyenchantments.api.events.BlastUseEvent", false, 
//							this.getClass().getClassLoader() );
//			
//			Output.get().logInfo( "OnBlockBreakEventListener: Trying to register CrazyEnchants" );
//			
//			
//			String cePriority = getMessage( AutoFeatures.CrazyEnchantsBlastUseEventPriority );
//			BlockBreakPriority crazyEnchantsPriority = BlockBreakPriority.fromString( cePriority );
//			
//			// always register a monitor event:
//			if ( crazyEnchantsPriority != BlockBreakPriority.DISABLED ) {
//				Bukkit.getPluginManager().registerEvents( 
//						new OnBlockBreakBlastUseEventListenerMonitor(), spigotPrison);
//			}
//			
//			switch ( crazyEnchantsPriority )
//			{
//				case LOWEST:
//					Bukkit.getPluginManager().registerEvents( 
//							new OnBlockBreakBlastUseEventListenerLowest(), spigotPrison);
//					break;
//					
//				case LOW:
//					Bukkit.getPluginManager().registerEvents( 
//							new OnBlockBreakBlastUseEventListenerLow(), spigotPrison);
//					break;
//					
//				case NORMAL:
//					Bukkit.getPluginManager().registerEvents( 
//							new OnBlockBreakBlastUseEventListenerNormal(), spigotPrison);
//					break;
//					
//				case HIGH:
//					Bukkit.getPluginManager().registerEvents( 
//							new OnBlockBreakBlastUseEventListenerHigh(), spigotPrison);
//					break;
//					
//				case HIGHEST:
//					Bukkit.getPluginManager().registerEvents( 
//							new OnBlockBreakBlastUseEventListenerHighest(), spigotPrison);
//					break;
//				case DISABLED:
//					Output.get().logInfo( "OnBlockBreakEventLisenters Crazy Enchant's BlastUseEvent " +
//								"handling has been DISABLED." );
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
//			Output.get().logInfo( "OnBlockBreakEventListener: CrazyEnchants is not loaded" );
//		}
//	}
//
//	
//
//	   
//    public class OnBlockBreakBlastUseEventListenerMonitor 
//		extends OnBlockBreakEventListener
//		implements Listener {
//    	
//        @EventHandler(priority=EventPriority.MONITOR) 
//        public void onCrazyEnchantsBlockExplodeMonitor(BlastUseEvent e) {
//        	super.onCrazyEnchantsBlockExplode( e );
//        }
//    }
//    
//    public class OnBlockBreakBlastUseEventListenerLowest 
//	    extends OnBlockBreakEventListener
//	    implements Listener {
//	    	
//    	@EventHandler(priority=EventPriority.LOWEST) 
//    	public void onCrazyEnchantsBlockExplodeLowest(BlastUseEvent e) {
//    		super.onCrazyEnchantsBlockExplode( e );
//    	}
//    }
//    
//    public class OnBlockBreakBlastUseEventListenerLow 
//	    extends OnBlockBreakEventListener
//	    implements Listener {
//    	
//    	@EventHandler(priority=EventPriority.LOW) 
//    	public void onCrazyEnchantsBlockExplodeLow(BlastUseEvent e) {
//    		super.onCrazyEnchantsBlockExplode( e );
//    	}
//    }
//    
//    public class OnBlockBreakBlastUseEventListenerNormal 
//	    extends OnBlockBreakEventListener
//	    implements Listener {
//    	
//    	@EventHandler(priority=EventPriority.NORMAL) 
//    	public void onCrazyEnchantsBlockExplodeNormal(BlastUseEvent e) {
//    		super.onCrazyEnchantsBlockExplode( e );
//    	}
//    }
//    
//    public class OnBlockBreakBlastUseEventListenerHigh 
//	    extends OnBlockBreakEventListener
//	    implements Listener {
//    	
//    	@EventHandler(priority=EventPriority.HIGH) 
//    	public void onCrazyEnchantsBlockExplodeHigh(BlastUseEvent e) {
//    		super.onCrazyEnchantsBlockExplode( e );
//    	}
//    }
//    
//    public class OnBlockBreakBlastUseEventListenerHighest 
//	    extends OnBlockBreakEventListener
//	    implements Listener {
//    	
//    	@EventHandler(priority=EventPriority.HIGHEST) 
//    	public void onCrazyEnchantsBlockExplodeHighest(BlastUseEvent e) {
//    		super.onCrazyEnchantsBlockExplode( e );
//    	}
//    }
//    
}
