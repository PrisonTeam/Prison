package tech.mcprison.prison.spigot.integrations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.integration.IntegrationCore;
import tech.mcprison.prison.integration.IntegrationType;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.api.BackpackEvent;
import tech.mcprison.prison.spigot.api.BackpackEvent.BackpackAction;
import tech.mcprison.prison.spigot.api.BackpackEvent.BackpackResults;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.spigot.inventory.SpigotInventory;
import tech.mcprison.prison.spigot.sellall.SellAllData;
import tech.mcprison.prison.spigot.sellall.SellAllUtil;

public class IntegrationBackpackAPI 
	extends IntegrationCore {

	private static IntegrationBackpackAPI instance = null;
	
	private int registeredListenersCount = 0;
	
//	private boolean enabled = false;
	
	private IntegrationBackpackAPI() {
		super( "ApiBackpacks", "ApiBackpacks", IntegrationType.BACKPACK );
		
//		this.enabled = false;
		
	}
	
	
	public static IntegrationBackpackAPI getInstance() {
		if ( instance == null ) {
			synchronized( IntegrationBackpackAPI.class ) {
				if ( instance == null ) {
					instance = new IntegrationBackpackAPI();
				}
			}
		}
		return instance;
	}
	
	
	public boolean isEnabled() {
		
		HandlerList listeners = BackpackEvent.getHandlerList();
		
		int count = listeners.getRegisteredListeners().length;
		
		boolean enabled = count > 0;

		if ( getRegisteredListenersCount() != count ) {
			
			String msg = String.format(
					"IntegrationBackpackAPI: The active number of Prison's BackpackEvent listeners has changed. "
					+ "It is now %s and was %s.",
						Integer.toString(count),
						Integer.toString(getRegisteredListenersCount())
					);
			Output.get().logInfo( msg );
			
			setRegisteredListenersCount(count);
		}
		
		return enabled;
	}
	

	/**
	 * <p>For this function, hasIntegrated(), it must always return a value of true,
	 * although the isEnabled() may return false. The isEnabled() is dynamic and
	 * may change a few times, depending if the plugin that is listening to that
	 * event is resetting multiple times.
	 * </p>
	 * 
	 * <p>This function is used to determine if it should be listed as an active
	 * integration or not.  This is only set and checked at startup or reloading.
	 * </p>
	 */
    @Override
    public boolean hasIntegrated() {
    	return true;
//    	return isEnabled();
    }
    
    
    /**
     * Remove reference to Minepacks and then get a new instance, which will basically 
     * be similar to reloading the integration to Minepacks.
     */
    @Override
    public void disableIntegration() {

    	// Do nothing. Since isEnabled() is based upon having at least one registered 
    	// listener, and it's checked each time any of these functions are used, 
    	// it will be self managing and handle dynamically added and removed listeners.
    	
//    	this.enabled = false;
    }
    
    @Override
    public String getDisplayName()
    {
    	return super.getDisplayName();
    }
    
	@Override
	public String getPluginSourceURL() {
		return "(no ULR avilable since this is an API envent)";
	}


    

	public int getRegisteredListenersCount() {
		return registeredListenersCount;
	}
	public void setRegisteredListenersCount(int registeredListenersCount) {
		this.registeredListenersCount = registeredListenersCount;
	}


	public HashMap<Integer, SpigotItemStack> addItems( Player player, HashMap<Integer, SpigotItemStack> items ) {
    	
	    	HashMap<Integer, SpigotItemStack> extras = new HashMap<>();
	    	
	    	if ( items != null && items.size() > 0 && isEnabled() ) {
	    		
	    		BackpackEvent bpEvent = new BackpackEvent( player );
	    		bpEvent.setAction( BackpackAction.addItems );
	
				if ( bpEvent.fireBackpackEvent() ) {
	
					boolean changedBackpack = false;
					
					for ( Inventory inv : bpEvent.getInventory() ) {
						
						if ( inv != null ) {
							
							for ( SpigotItemStack spigotItemStack : items.values() ) {
								
								ItemStack iStack = SpigotUtil.prisonItemStackToBukkit( spigotItemStack );
								
								if ( iStack != null ) {
									
									HashMap<Integer, ItemStack> extra = inv.addItem( iStack );
									
									Set<Integer> keys = extra.keySet();
									for ( Integer key : keys ) {
										ItemStack entry = extra.get(key);
										
										extras.put( key, new SpigotItemStack(entry) );
	
										changedBackpack = true;
									}
									
									
								}
							}
							
						}
						
						
						// Since we are processing multiple occurrences of inventories, then
						// we need to reset items to extras so they can be processed in the
						// next inventory.
						
						items.clear();
						items.putAll( extras );
						extras.clear();
					}
					
					if ( changedBackpack ) {
						bpEvent.setResults( BackpackResults.contentsChanged );
						
						bpEvent.getCallback().run();
					}
					
				}
	    	}
	
	    	
	    	// All extras will be in items, so move them back to extras:
	    	extras.putAll( items );
	    	
	    	return extras;
    }
    
    public HashMap<Integer, ItemStack> addItemsBukkit( Player player, HashMap<Integer, ItemStack> items ) {
    	
	    	HashMap<Integer, ItemStack> extras = new HashMap<>();
	    	
	    	if ( items != null && items.size() > 0 && isEnabled() ) {
	    		
	    		BackpackEvent bpEvent = new BackpackEvent( player );
	    		bpEvent.setAction( BackpackAction.addItems );
	
				if ( bpEvent.fireBackpackEvent() ) {
					
					boolean changedBackpack = false;
					
					for ( Inventory inv : bpEvent.getInventory() ) {
						
						if ( inv != null ) {
							
							for ( ItemStack itemStack : items.values() ) {
								
								if ( itemStack != null ) {
									
									extras.putAll( inv.addItem( itemStack ));
																	
									changedBackpack = true;
								}
							}
							
						}
						
						// Since we are processing multiple occurrences of inventories, then
						// we need to reset items to extras so they can be processed in the
						// next inventory.
						
						items.clear();
						items.putAll( extras );
						extras.clear();
					}
					
					if ( changedBackpack ) {
						bpEvent.setResults( BackpackResults.contentsChanged );
						
						bpEvent.getCallback().run();
					}
				}
	    	}
	
	    	// All extras will be in items, so put them back in extras:
	    	extras.putAll( items );
	    	
	    	return extras;
    }

    public HashMap<Integer, SpigotItemStack> smeltItems( Player player, XMaterial source, SpigotItemStack destStack ) {
    	
	    	HashMap<Integer, SpigotItemStack> extras = new HashMap<>();
	    	
			SpigotItemStack sourceStack = new SpigotItemStack( source.parseItem() );
	    	
	    	if ( isEnabled() && sourceStack != null && destStack != null ) {
	    		
	    		BackpackEvent bpEvent = new BackpackEvent( player );
	    		bpEvent.setAction( BackpackAction.smeltItems );
	
				if ( bpEvent.fireBackpackEvent() ) {
					
					boolean changedBackpack = false;
	
					for ( Inventory inv : bpEvent.getInventory() ) {
						
						if ( inv != null ) {
							
							if ( inv.containsAtLeast( sourceStack.getBukkitStack(), 1 ) ) {
								
								int count = SpigotUtil.itemStackCount( source, inv );
								if ( count > 0 ) {
									sourceStack.setAmount( count );
									destStack.setAmount( count );
									
									inv.remove( sourceStack.getBukkitStack() );
									
									HashMap<Integer, SpigotItemStack> temp = new HashMap<>();
									temp.put( Integer.valueOf( 0 ), destStack );
									
									extras.putAll( addItems( player, temp ) );
									
									changedBackpack = true;
								}
								
							}
						}
						
						
						// Since we are smeltings, extras will be the results of the smelt,
						// and as such, for multiple inventories, they can be combined in to
						// one extras object without having to reset it each time.
						
					}
					
					if ( changedBackpack ) {
						bpEvent.setResults( BackpackResults.contentsChanged );
						
						bpEvent.getCallback().run();
					}
					
				}
					
	    	}
	    	
	    	return extras;
    }
    
    /**
     * <p>Removes a given XMaterial from the Minepack's backpack if it exists.
     * This function returns the number of items that were removed.
     * </p>
     * 
     * @param player
     * @param xMat
     * @return
     */
	public int itemStackRemoveAll(Player player, XMaterial xMat) {
		int removed = 0;
		
		if ( xMat != null && isEnabled() ) {
			
    		BackpackEvent bpEvent = new BackpackEvent( player );
    		bpEvent.setAction( BackpackAction.removeAll );

			if ( bpEvent.fireBackpackEvent() ) {
				
				boolean changedBackpack = false;
				
				for ( Inventory inv : bpEvent.getInventory() ) {
					
					if ( inv != null ) {
						
						removed += SpigotUtil.itemStackRemoveAll( xMat, inv );
						
						if ( removed > 0 ) {
							
							changedBackpack = true;
						}
					}
					
				}
				
				if ( changedBackpack ) {
					bpEvent.setResults( BackpackResults.contentsChanged );
					
					bpEvent.getCallback().run();
				}
			}
			
		}
		return removed;
	}

	
    public List<SellAllData> sellInventoryItems( Player player, double multiplier ) {
		List<SellAllData> soldItems = new ArrayList<>();
		
	    	if ( isEnabled()  ) {
	    		
	    		BackpackEvent bpEvent = new BackpackEvent( player );
	    		bpEvent.setAction( BackpackAction.removeAll );
	
				if ( bpEvent.fireBackpackEvent() ) {
					
					boolean changedBackpack = false;
					
					for ( Inventory inv : bpEvent.getInventory() ) {
						
						if ( inv != null ) {
							
							SpigotInventory sInventory = new SpigotInventory( inv );
							
							soldItems.addAll( SellAllUtil.get().sellInventoryItems( sInventory, multiplier ) );
							
							if ( soldItems.size() > 0 ) {
	
								changedBackpack = true;
							}
						}
					}
					
					if ( changedBackpack ) {
						bpEvent.setResults( BackpackResults.contentsChanged );
						
						bpEvent.getCallback().run();
					}
				}
	    		
	    	}
		
		return soldItems;
	}
	
}
