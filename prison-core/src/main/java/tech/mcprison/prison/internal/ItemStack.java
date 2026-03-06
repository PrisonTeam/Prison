/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017-2020 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.util.Text;

/**
 * Represents an item stack. An item stack is a uniquely named stack in a player's inventory.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class ItemStack {

    private String displayName = null;
    private int amount;
    private PrisonBlock material;
    private List<String> lore;

    public static final ItemStack SELECTION_WAND;
    
    static {
	    	SELECTION_WAND = new ItemStack( 1, PrisonBlock.SELECTION_WAND, 
					"&7Corner 1 - Left click",
		            "&7Corner 2 - Right click");
    }
    
    /**
     * Do not use this constructor, it is for unit testing.
     */
    protected ItemStack() {
	    	super();
	    	
	    	this.lore = new ArrayList<>();
    }
    
    public ItemStack(String displayName, int amount, PrisonBlock material, String... lore) {
        this.displayName = displayName;
        this.amount = amount;
        this.material = material.clone();
        this.lore = new ArrayList<>(Arrays.asList(lore));
        
        if ( displayName != null && displayName.trim().length() > 0 ) {
        	this.material.setDisplayName( displayName.trim() );
        }
        
    }

    public ItemStack(int amount, PrisonBlock material, String... lore) {
        this.amount = amount;
        this.material = material;
        this.lore = new ArrayList<>(Arrays.asList(lore));
        
        if ( material.getDisplayName() != null && material.getDisplayName().trim().length() > 0 ) {
        		setDisplayName( material.getDisplayName() );
        }
    }

    public ItemStack( ItemStack iStack ) {
	    	this.displayName = iStack.getDisplayName();
	    	this.amount = iStack.getAmount();
	    	this.material = iStack.getMaterial().clone();
	    	this.lore = new ArrayList<>( iStack.getLore() );
        
        if ( displayName != null && displayName.trim().length() > 0 ) {
        		this.material.setDisplayName( displayName.trim() );
        }
    }
    
    /**
     * Returns the name of the item stack, derived from its BlockType name.
     */
    public String getName() {
	    	String name = (material != null ? 
	    						material.getBlockName() : 
	    						( getDisplayName() != null ? 
	    								getDisplayName() : 
	    								"none"));
        return StringUtils.capitalize(name.replaceAll("_", " ").toLowerCase());
    }

    /**
     * Returns the display name of the item stack. This may include colors and may also be null!
     */
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the amount of items in this stack.
     */
    public int getAmount() {
        return amount;
    }
    public void setAmount( int amount ) {
		this.amount = amount;
	}

	/**
     * Returns the type of items in this stack.
     */
    public PrisonBlock getMaterial() {
    	
	    	if ( getDisplayName() != null && getDisplayName().trim().length() > 0 && 
	    			material.getDisplayName() == null || 
	    					material.getDisplayName() != null && material.getDisplayName().trim().length() == 0 ) {
	    		material.setDisplayName( getDisplayName() );
	    	}
    	
        return material;
    }
    public void setMaterial( PrisonBlock material ) {
		this.material = material;
	}
    
    /**
     * <p>The material for an item stack is the PrisonBlock.
     * </p>
     * 
     * @return
     */
    public PrisonBlock getPrisonBlock() {
    		return getMaterial();
    }

	public List<String> getLore() {
        return lore;
    }
    public void setLore( List<String> lore ) {
		this.lore = lore;
	}


    @Override 
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof ItemStack)) {
            return false;
        }

        ItemStack stack = (ItemStack) o;
        
        String stackName = Text.stripColor(stack.displayName);
        boolean isDisplayNamesEqual = (displayName != null ? 
        		Text.stripColor(displayName).equals(stackName) :
        			(stack.displayName == null));

        return isDisplayNamesEqual && 
        		material.compareTo( stack.material ) == 0;
    }

    @Override 
    public int hashCode() {
        int result = amount;
        result = 31 * result + material.hashCode();
        return result;
    }

    @Override 
    public String toString() {
        return "ItemStack{" + "displayName='" + displayName + '\'' + ", amount=" + amount
            + ", material=" + material + ", lore=" + lore +
            "}";
    }
}
