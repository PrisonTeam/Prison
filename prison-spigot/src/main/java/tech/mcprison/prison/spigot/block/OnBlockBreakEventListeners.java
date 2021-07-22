package tech.mcprison.prison.spigot.block;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;

public class OnBlockBreakEventListeners
	extends OnBlockBreakEventListener
{
	
	
	public void registerBlockBreakEvents(SpigotPrison spigotPrison ) {
	
		
		String bbePriority = getMessage( AutoFeatures.blockBreakEventPriority );
		BlockBreakPriority blockBreakPriority = BlockBreakPriority.fromString( bbePriority );
		
		if ( blockBreakPriority != BlockBreakPriority.DISABLED ) {
			
			// Always register the monitor event:
			Bukkit.getPluginManager().registerEvents( 
					new OnBlockBreakEventListenerMonitor(), spigotPrison);
		}
		
		switch ( blockBreakPriority )
		{
			case LOWEST:
				Bukkit.getPluginManager().registerEvents( 
						new OnBlockBreakEventListenerLowest(), spigotPrison);
				break;
				
			case LOW:
				Bukkit.getPluginManager().registerEvents( 
						new OnBlockBreakEventListenerLow(), spigotPrison);
				break;
				
			case NORMAL:
				Bukkit.getPluginManager().registerEvents( 
						new OnBlockBreakEventListenerNormal(), spigotPrison);
				break;
				
			case HIGH:
				Bukkit.getPluginManager().registerEvents( 
						new OnBlockBreakEventListenerHigh(), spigotPrison);
				break;
				
			case HIGHEST:
				Bukkit.getPluginManager().registerEvents( 
						new OnBlockBreakEventListenerHighest(), spigotPrison);
				break;

			case DISABLED:
				Output.get().logInfo( "BlockBreakEvent handling and monitoring has been DISABLED." );
				break;
				
			default:
				break;
		}
		
		
		
		boolean isTEBlockExplosiveEnabled = isBoolean( AutoFeatures.isProcessTokensEnchantExplosiveEvents );
		
		if ( isTEBlockExplosiveEnabled ) {
			
			try {
				Class.forName("com.vk2gpz.tokenenchant.event.TEBlockExplodeEvent");
				
//				AutoManagerTokenEnchant tokenEnchant = new AutoManagerTokenEnchant();
//				tokenEnchant.registerBlockBreakEvents( spigotPrison );
				
				OnBlockBreakEventTokenEnchant bbTokenEnchant = new OnBlockBreakEventTokenEnchant();
				bbTokenEnchant.registerBlockBreakEvents( spigotPrison );
				
//			Bukkit.getPluginManager().registerEvents(new AutoManagerTokenEnchant(), spigotPrison);
//			Bukkit.getPluginManager().registerEvents(new OnBlockBreakEventTokenEnchant(), spigotPrison);
				
			} 
			catch (ClassNotFoundException e) {
				// TokenEnchant is not available on this server which is not an error.  Just
				// ignore this situation and do not register the TE explosion events.
			}
		}
		
		
		
    	boolean isCEBlockExplodeEnabled = isBoolean( AutoFeatures.isProcessCrazyEnchantsBlockExplodeEvents );
    	
    	if ( isCEBlockExplodeEnabled ) {
    		OnBlockBreakEventCrazyEnchants bbeCE = new OnBlockBreakEventCrazyEnchants();
    		bbeCE.registerBlastUseEvents( spigotPrison );
    	}

		
		
    	boolean isZenBlockExplodeEnabled = isBoolean( AutoFeatures.isProcessZenchantsBlockExplodeEvents );
		
		if ( isZenBlockExplodeEnabled ) {
			
			OnBlockBreakEventZenchantments zenBlockEvents = new OnBlockBreakEventZenchantments();
			zenBlockEvents.registerBlockBreakEvents( spigotPrison );
		}
		
		
		boolean isPEExplosiveEnabled = isBoolean( AutoFeatures.isProcessPrisonEnchantsExplosiveEvents );
		
		if ( isPEExplosiveEnabled ) {
			
			OnBlockBreakEventPrisonEnchants explosiveEvents = new OnBlockBreakEventPrisonEnchants();
			explosiveEvents.registerExplosiveEvents( spigotPrison );
		}
		
	}
	
	

	   
    public class OnBlockBreakEventListenerMonitor
		extends OnBlockBreakEventListener
		implements Listener {
    	
    	
        /**
         * <p>The EventPriorty.MONITOR means that the state of the event is OVER AND DONE,
         * so this function CANNOT do anything with the block, other than "monitor" what
         * happened.  That is all we need to do, is to just count the number of blocks within
         * a mine that have been broken.
         * </p>
         * 
         * <p><b>Note:</b> Because this is a MONITOR event, we cannot do anything with the 
         * target block here. Mostly because everything has already been done with it, and 
         * this is only intended to MONITOR the final results. 
         * </p>
         * 
         * <p>One interesting fact about this monitoring is that we know that a block was broken,
         * not because of what is left (should be air), but because this function was called.
         * There is a chance that the event was canceled and the block remains unbroken, which
         * is what WorldGuard would do.  But the event will also be canceled when auto pickup is
         * enabled, and at that point the BlockType will be air.
         * </p>
         * 
         * <p>If the event is canceled it's important to check to see that the BlockType is Air,
         * since something already broke the block and took the drop.  
         * If it is not canceled we still need to count it since it will be a normal drop.  
         * </p>
         * 
         * @param e
         */
        @EventHandler(priority=EventPriority.MONITOR) 
        public void onBlockBreakMonitor(BlockBreakEvent e) {

        	genericBlockEventMonitor( e );
        }
        
//        @EventHandler(priority=EventPriority.MONITOR) 
//        public void onBlockShredBreakMonitor(BlockShredEvent e) {
//        	genericBlockEventMonitor( e );
//        }
        
//        @EventHandler(priority=EventPriority.MONITOR) 
//        public void onTEBlockExplodeMonitor(TEBlockExplodeEvent e) {
    //    
//        	genericBlockExplodeEventMonitor( e );
//        }

//        @EventHandler(priority=EventPriority.MONITOR) 
//    	public void onCrazyEnchantsBlockExplodeMonitor( BlastUseEvent e ) {
//    		
//        	genericBlockExplodeEventMonitor( e );
//    	}
        
    	
    }
	
    
    public class OnBlockBreakEventListenerLowest
		extends OnBlockBreakEventListener
		implements Listener {
    	
        @EventHandler(priority=EventPriority.LOWEST) 
        public void onBlockBreak(BlockBreakEvent e) {
        	super.onBlockBreak( e );
        }
        
//        @EventHandler(priority=EventPriority.LOWEST) 
//        public void onBlockShredBreak(BlockShredEvent e) {
//        	super.onBlockShredBreak( e );
//        }
        
//        @EventHandler(priority=EventPriority.LOWEST) 
//        public void onCrazyEnchantsBlockExplodeLow(BlastUseEvent e) {
//        	super.onCrazyEnchantsBlockExplodeLow( e );
//        }
    }
    
    
    public class OnBlockBreakEventListenerLow
	    extends OnBlockBreakEventListener
	    implements Listener {
    	
    	@EventHandler(priority=EventPriority.LOW) 
    	public void onBlockBreak(BlockBreakEvent e) {
    		super.onBlockBreak( e );
    	}
    	
//    	@EventHandler(priority=EventPriority.LOW) 
//    	public void onBlockShredBreak(BlockShredEvent e) {
//    		super.onBlockShredBreak( e );
//    	}
    	
//    	@EventHandler(priority=EventPriority.LOW) 
//    	public void onCrazyEnchantsBlockExplodeLow(BlastUseEvent e) {
//    		super.onCrazyEnchantsBlockExplodeLow( e );
//    	}
    }
    
    
    public class OnBlockBreakEventListenerNormal
	    extends OnBlockBreakEventListener
	    implements Listener {
    	
    	@EventHandler(priority=EventPriority.NORMAL) 
    	public void onBlockBreak(BlockBreakEvent e) {
    		super.onBlockBreak( e );
    	}
    	
//    	@EventHandler(priority=EventPriority.NORMAL) 
//    	public void onBlockShredBreak(BlockShredEvent e) {
//    		super.onBlockShredBreak( e );
//    	}
    	
//    	@EventHandler(priority=EventPriority.NORMAL) 
//    	public void onCrazyEnchantsBlockExplodeLow(BlastUseEvent e) {
//    		super.onCrazyEnchantsBlockExplodeLow( e );
//    	}
    }
    
    
    public class OnBlockBreakEventListenerHigh
	    extends OnBlockBreakEventListener
	    implements Listener {
    	
    	@EventHandler(priority=EventPriority.HIGH) 
    	public void onBlockBreak(BlockBreakEvent e) {
    		super.onBlockBreak( e );
    	}
    	
//    	@EventHandler(priority=EventPriority.HIGH) 
//    	public void onBlockShredBreak(BlockShredEvent e) {
//    		super.onBlockShredBreak( e );
//    	}
    	
//    	@EventHandler(priority=EventPriority.HIGH) 
//    	public void onCrazyEnchantsBlockExplodeLow(BlastUseEvent e) {
//    		super.onCrazyEnchantsBlockExplodeLow( e );
//    	}
    }
    
    
    public class OnBlockBreakEventListenerHighest
	    extends OnBlockBreakEventListener
	    implements Listener {
    	
    	@EventHandler(priority=EventPriority.HIGHEST) 
    	public void onBlockBreak(BlockBreakEvent e) {
    		super.onBlockBreak( e );
    	}
    	
//    	@EventHandler(priority=EventPriority.HIGHEST) 
//    	public void onBlockShredBreak(BlockShredEvent e) {
//    		super.onBlockShredBreak( e );
//    	}
    	
//    	@EventHandler(priority=EventPriority.HIGHEST) 
//    	public void onCrazyEnchantsBlockExplodeLow(BlastUseEvent e) {
//    		super.onCrazyEnchantsBlockExplodeLow( e );
//    	}
    }
    
 

}
