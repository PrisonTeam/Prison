package tech.mcprison.prison.spigot.block;

public class OnBlockBreakEventZenchantments
//	extends OnBlockBreakEventListener
{
	public OnBlockBreakEventZenchantments() {
		super();
	}

	
	
//	public void registerBlockBreakEvents(SpigotPrison spigotPrison ) {
//	
//		
//		String zbsPriority = getMessage( AutoFeatures.ZenchantmentsBlockShredEventPriority );
//		BlockBreakPriority blockShredPriority = BlockBreakPriority.fromString( zbsPriority );
//		
//		if ( blockShredPriority != BlockBreakPriority.DISABLED ) {
//			
//			// Always register the monitor event:
//			Bukkit.getPluginManager().registerEvents( 
//					new OnBlockBreakBlockShredEventListenerMonitor(), spigotPrison);
//		}
//
//		
//		switch ( blockShredPriority )
//		{
//			case LOWEST:
//				Bukkit.getPluginManager().registerEvents( 
//						new OnBlockBreakBlockShredEventListenerLowest(), spigotPrison);
//				break;
//				
//			case LOW:
//				Bukkit.getPluginManager().registerEvents( 
//						new OnBlockBreakBlockShredEventListenerLow(), spigotPrison);
//				break;
//				
//			case NORMAL:
//				Bukkit.getPluginManager().registerEvents( 
//						new OnBlockBreakBlockShredEventListenerNormal(), spigotPrison);
//				break;
//				
//			case HIGH:
//				Bukkit.getPluginManager().registerEvents( 
//						new OnBlockBreakBlockShredEventListenerHigh(), spigotPrison);
//				break;
//				
//			case HIGHEST:
//				Bukkit.getPluginManager().registerEvents( 
//						new OnBlockBreakBlockShredEventListenerHighest(), spigotPrison);
//				break;
//
//			case DISABLED:
//				Output.get().logInfo( "OnBlockBreak Zenchantments BlockShredEvent handling has been DISABLED." );
//				break;
//
//			default:
//				break;
//		}
//		
//	}
	
	  
//    public class OnBlockBreakBlockShredEventListenerMonitor 
//	    extends OnBlockBreakEventListener
//	    implements Listener {
//    	
//    	@EventHandler(priority=EventPriority.MONITOR) 
//    	public void onBlockShredBreakMonitor(BlockShredEvent e) {
//    		super.genericBlockEventMonitor( e );
//    	}
//    }
//    
//    public class OnBlockBreakBlockShredEventListenerLowest 
//		extends OnBlockBreakEventListener
//		implements Listener {
//        
//        @EventHandler(priority=EventPriority.LOWEST) 
//        public void onBlockShredBreak(BlockShredEvent e) {
//        	super.onBlockShredBreak( e );
//        }
//    }
//    
//    public class OnBlockBreakBlockShredEventListenerLow 
//	    extends OnBlockBreakEventListener
//	    implements Listener {
//    	
//    	@EventHandler(priority=EventPriority.LOW) 
//    	public void onBlockShredBreak(BlockShredEvent e) {
//    		super.onBlockShredBreak( e );
//    	}
//    }
//    
//    public class OnBlockBreakBlockShredEventListenerNormal 
//	    extends OnBlockBreakEventListener
//	    implements Listener {
//    	
//    	@EventHandler(priority=EventPriority.NORMAL) 
//    	public void onBlockShredBreak(BlockShredEvent e) {
//    		super.onBlockShredBreak( e );
//    	}
//    }
//    
//    public class OnBlockBreakBlockShredEventListenerHigh 
//	    extends OnBlockBreakEventListener
//	    implements Listener {
//	    	
//    	@EventHandler(priority=EventPriority.HIGH) 
//    	public void onBlockShredBreak(BlockShredEvent e) {
//    		super.onBlockShredBreak( e );
//    	}
//    }
//    
//    public class OnBlockBreakBlockShredEventListenerHighest 
//    extends OnBlockBreakEventListener
//    implements Listener {
//    	
//    	@EventHandler(priority=EventPriority.HIGHEST) 
//    	public void onBlockShredBreak(BlockShredEvent e) {
//    		super.onBlockShredBreak( e );
//    	}
//    }

}
