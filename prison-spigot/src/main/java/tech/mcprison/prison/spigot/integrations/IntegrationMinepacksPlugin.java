package tech.mcprison.prison.spigot.integrations;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.cryptomorin.xseries.XMaterial;

import at.pcgamingfreaks.Minepacks.Bukkit.API.Backpack;
import at.pcgamingfreaks.Minepacks.Bukkit.API.MinepacksPlugin;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.block.SpigotItemStack;

public class IntegrationMinepacksPlugin
{
	private static IntegrationMinepacksPlugin instance = null;
	
	private MinepacksPlugin minepacks = null;
	
	
	private IntegrationMinepacksPlugin() {
		super();
		
		try {
			Plugin bukkitPlugin = Bukkit.getPluginManager().getPlugin("Minepacks");

			 if( bukkitPlugin instanceof MinepacksPlugin ) {
			    this.minepacks = (MinepacksPlugin) bukkitPlugin;
			 }
		}
		catch ( Exception e ) {
			Output.get().logWarn( "Unable to Minepacks integration.", e );
		}
		
	}
	
	public static IntegrationMinepacksPlugin getInstance() {
		if ( instance == null ) {
			synchronized( IntegrationMinepacksPlugin.class ) {
				if ( instance == null ) {
					instance = new IntegrationMinepacksPlugin();
				}
			}
		}
		return instance;
	}
	
	public boolean isEnabled() {
		return minepacks != null;
	}
	
    public MinepacksPlugin getMinepacks() {
    	
    	return minepacks;
    }
    
    public HashMap<Integer, SpigotItemStack> addItems( Player player, HashMap<Integer, SpigotItemStack> items ) {
    	
    	HashMap<Integer, SpigotItemStack> extras = new HashMap<>();
    	
    	if ( items != null && items.size() > 0 && isEnabled() ) {
    		Backpack bp = getMinepacks().getBackpackCachedOnly(player);

    		if ( bp != null ) {
    			
    			for ( SpigotItemStack spigotItemStack : items.values() ) {
    				
    				ItemStack iStack = SpigotUtil.prisonItemStackToBukkit( spigotItemStack );
//    				ItemStack iStack = spigotItemStack.getBukkitStack();
    				
    				if ( iStack != null ) {
    					ItemStack extra = bp.addItem( iStack );
    					extras.put( Integer.valueOf( extras.size() ), new SpigotItemStack(extra) );
    					
    					bp.setChanged();
    				}
    			}
    		}
    		else {
    			extras.putAll( items );
    		}
    	}
    	else {
    		extras.putAll( items );
    	}
    	
    	return extras;
    }
    
    public HashMap<Integer, ItemStack> addItemsBukkit( Player player, HashMap<Integer, ItemStack> items ) {
    	
    	HashMap<Integer, ItemStack> extras = new HashMap<>();
    	
    	if ( items != null && items.size() > 0 && isEnabled() ) {
    		Backpack bp = getMinepacks().getBackpackCachedOnly(player);
    		
    		if ( bp != null ) {
    			
    			for ( ItemStack itemStack : items.values() ) {
    				
    				if ( itemStack != null ) {
    					ItemStack extra = bp.addItem( itemStack );
    					extras.put( Integer.valueOf( extras.size() ), extra );
    					
    					bp.setChanged();
    				}
    			}
    		}
    		else {
    			extras.putAll( items );
    		}
    	}
    	else {
    		extras.putAll( items );
    	}
    	
    	return extras;
    }

    public HashMap<Integer, SpigotItemStack> smeltItems( Player player, XMaterial source, SpigotItemStack destStack ) {
    	
    	HashMap<Integer, SpigotItemStack> extras = new HashMap<>();
    	
		SpigotItemStack sourceStack = new SpigotItemStack( source.parseItem() );
    	
    	if ( isEnabled() && sourceStack != null && destStack != null ) {
    		Backpack bp = getMinepacks().getBackpackCachedOnly(player);
    		
    		if ( bp != null ) {
    			
    			Inventory inv = bp.getInventory();
    			
    			if ( inv.containsAtLeast( sourceStack.getBukkitStack(), 1 ) ) {
    				
    				int count = SpigotUtil.itemStackCount( source, inv );
    				if ( count > 0 ) {
    					sourceStack.setAmount( count );
    					destStack.setAmount( count );

    					inv.remove( sourceStack.getBukkitStack() );
    					
    					HashMap<Integer, SpigotItemStack> temp = new HashMap<>();
    					temp.put( Integer.valueOf( 0 ), destStack );
    					
    					extras.putAll( addItems( player, temp ) );

    					bp.setChanged();
    				}
					
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
			
			Backpack bp = getMinepacks().getBackpackCachedOnly(player);
			
			if ( bp != null ) {
				
				removed += SpigotUtil.itemStackRemoveAll( xMat, bp.getInventory() );
				
				if ( removed > 0 ) {
					bp.setChanged();
				}
			}
		}
		return removed;
	}
    
//	private int itemCount(XMaterial source, Inventory inv ) {
//		int count = 0;
//		if ( source != null ) {
//			ItemStack testStack = source.parseItem();
//
//			for (ItemStack is : inv.getContents() ) {
//				if ( is != null && is.isSimilar( testStack ) ) {
//					count += is.getAmount();
//				}
//			}
//		}
//		return count;
//	}

}
