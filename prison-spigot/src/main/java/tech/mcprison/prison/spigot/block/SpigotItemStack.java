package tech.mcprison.prison.spigot.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

import com.cryptomorin.xseries.XMaterial;

import me.xanium.gemseconomy.nbt.NBTItem;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.spigot.SpigotUtil;

public class SpigotItemStack
		extends ItemStack {

	private NBTItem bukkitStack;
//	private org.bukkit.inventory.ItemStack bukkitStack;
	
	public SpigotItemStack( org.bukkit.inventory.ItemStack bukkitStack ) {
		super();
		
		NBTItem nbtItemStack = new NBTItem( bukkitStack );

		this.bukkitStack = nbtItemStack;
//		this.bukkitStack = bukkitStack;
		
        if (bukkitStack == null || bukkitStack.getType().equals(Material.AIR)) {
        	  setAmount( 0 );
              setMaterial( PrisonBlock.AIR );
              setDisplayName( "air" );
        }
        else {
        	
        	ItemMeta meta;
        	if (!bukkitStack.hasItemMeta()) {
        		meta = Bukkit.getItemFactory().getItemMeta(bukkitStack.getType());
        	} else {
        		meta = bukkitStack.getItemMeta();
        	}

        	
        	
        	// We are getting the bukkit amount here, but if it changes, we must set the amount
        	// in the bukkitStack:
        	int amount = bukkitStack.getAmount();
        	
        	XMaterial xMat = XMaterial.matchXMaterial( bukkitStack );
        	
        	PrisonBlock type = SpigotUtil.getPrisonBlock( xMat );
        	
//        	BlockType type = SpigotCompatibility.getInstance()
//        			.getBlockType( bukkitStack );
//        BlockType type = materialToBlockType(bukkitStack.getType());
        	
        	
        	String displayName = null;
        	
        	if (meta.hasDisplayName()) {
        		displayName = meta.getDisplayName();
        	}
        	else if ( type != null ) {
        		displayName = type.getBlockName().toLowerCase();
        	}
        	
        	List<String> lores = new ArrayList<>();
        	
        	if ( meta.hasLore() ) {
        		for ( String lore : meta.getLore() ) {
					lores.add( lore );
				}
        	}
        	
        	setDisplayName( displayName );
        	setAmount( amount );
        	setMaterial( type );
        	setLore( lores );
        }

		
    }
	

	
    public SpigotItemStack(String displayName, int amount, PrisonBlock material, String... lore) {
        super(displayName, amount, material, lore );
    }

    public SpigotItemStack(int amount, PrisonBlock material, String... lore) {
    	super( amount, material, lore );
    }
    
    
    public void setPrisonBlock( PrisonBlock pBlock ) {
    	
    	String displayName = pBlock.getBlockName();
    	
    	setDisplayName( displayName );
    	
    	setMaterial( pBlock );
    }
    
    public boolean hasNBTKey( String key ) {
    	boolean results = false;
    	
    	if ( bukkitStack != null ) {
    		results = bukkitStack.hasKey( key );
    	}
    	
    	return results;
    }
    
    public void setNBTString( String key, String value ) {
    	if ( bukkitStack != null ) {
    		bukkitStack.setString( key, value );
    	}
    }
    public String getNBTString( String key ) {
    	String results = null;
    	if ( bukkitStack != null ) {
    		results = bukkitStack.getString( key );
    	}
    	return results;
    }
    
    public void setNBTInt( String key, int value ) {
    	if ( bukkitStack != null ) {
    		bukkitStack.setInteger( key, value );
    	}
    }
    public int getNBTInt( String key ) {
    	int results = -1;
    	if ( bukkitStack != null ) {
    		results = bukkitStack.getInteger( key );
    	}
    	return results;
    }
    
    public void setNBTDouble( String key, double value ) {
    	if ( bukkitStack != null ) {
    		bukkitStack.setDouble( key, value );
    	}
    }
    public double getNBTDouble( String key ) {
    	double results = -1d;
    	if ( bukkitStack != null ) {
    		results = bukkitStack.getDouble( key );
    	}
    	return results;
    }
    
    public void setNBTBoolean( String key, boolean value ) {
    	if ( bukkitStack != null ) {
    		bukkitStack.setBoolean( key, value );
    	}
    }
    public boolean getNBTBoolean( String key ) {
    	boolean results = false;
    	if ( bukkitStack != null ) {
    		results = bukkitStack.getBoolean( key );
    	}
    	return results;
    }
	
	/**
	 * <p>This function overrides the Prison's ItemStack class's setAmount() to perform the 
	 * same behavior of setting the amount to the requested value, but it also updates
	 * the bukkitStack's amount to ensure that it is the correct value.
	 * </p>
	 * 
	 */
	@Override
	public void setAmount( int amount ) {
		super.setAmount( amount );
		
		if ( bukkitStack != null ) {
			bukkitStack.getItem().setAmount( amount );
		}
	}
	
	
	
	public boolean isAir() {
		boolean results = false;
		
		if ( getMaterial() != null && getMaterial().isAir() ||
				getName() != null && "air".equalsIgnoreCase( getName() ) ) {
			results = true;
		}
		else if ( getBukkitStack() != null ) {
			results = getBukkitStack().getType().equals( Material.AIR );
		}
		
		return results;
	}
	
	public boolean isBlock() {
		boolean results = false;
		
		if ( getBukkitStack() != null ) {
			results = getBukkitStack().getType().isBlock();
//			XMaterial xMat = XMaterial.matchXMaterial( getBukkitStack() );
//			if ( xMat != null ) {
//				results = xMat.parseMaterial().isBlock();
//			}
		}
		
		return results;
	}
	

    
	public org.bukkit.inventory.ItemStack getBukkitStack() {
		return bukkitStack.getItem();
	}

	public void setBukkitStack( org.bukkit.inventory.ItemStack bukkitStack ) {
		
		NBTItem nbtItemStack = new NBTItem( bukkitStack );
		this.bukkitStack = nbtItemStack;
//		this.bukkitStack = bukkitStack;
	}

	public Map<String, Object> serialize()
	{
		Map<String, Object> results = new HashMap<>();
		
		if ( getBukkitStack() != null ) {
			results = getBukkitStack().serialize();
		}
		
		results.put( "prison_version", Prison.get().getPlatform().getPluginVersion() );
		results.put( "isAir", isAir() );
		results.put( "isBlock", isBlock() );
		
		return results;
	}
}
