package tech.mcprison.prison.spigot.block;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.util.BlockType;

public class SpigotItemStack
		extends ItemStack {

	private org.bukkit.inventory.ItemStack bukkitStack;
	
	public SpigotItemStack( org.bukkit.inventory.ItemStack bukkitStack ) {
		super();
		
		this.bukkitStack = bukkitStack;
		
        if (bukkitStack == null || bukkitStack.getType().equals(Material.AIR)) {
        	  setAmount( 0 );
              setMaterial( BlockType.AIR );
        }
        else {
        	
        	ItemMeta meta;
        	if (!bukkitStack.hasItemMeta()) {
        		meta = Bukkit.getItemFactory().getItemMeta(bukkitStack.getType());
        	} else {
        		meta = bukkitStack.getItemMeta();
        	}
        	
        	String displayName = null;
        	
        	if (meta.hasDisplayName()) {
        		displayName = meta.getDisplayName();
        	}
        	
        	int amount = bukkitStack.getAmount();
        	
        	BlockType type = SpigotPrison.getInstance().getCompatibility()
        			.getBlockType( bukkitStack );
//        BlockType type = materialToBlockType(bukkitStack.getType());
        	
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
	
    public SpigotItemStack(String displayName, int amount, BlockType material, String... lore) {
        super(displayName, amount, material, lore );
    }

    public SpigotItemStack(int amount, BlockType material, String... lore) {
    	super( amount, material, lore );
    }
    
    
	public org.bukkit.inventory.ItemStack getBukkitStack() {
		return bukkitStack;
	}

	public void setBukkitStack( org.bukkit.inventory.ItemStack bukkitStack ) {
		this.bukkitStack = bukkitStack;
	}
}
