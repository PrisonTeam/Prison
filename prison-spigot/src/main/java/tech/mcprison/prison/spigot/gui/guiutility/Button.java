package tech.mcprison.prison.spigot.gui.guiutility;

import java.util.List;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

/**
 * @author GABRYCA
 * */
public class Button extends SpigotGUIComponents{

    private ItemStack buttonItem;
    private Integer position = null;

    /**
     * Create button (the recommended one).
     *
     * @param position - int.
     * @param buttonItem - XMaterial.
     * @param lore - List<String>.
     * @param title - String.
     * */
    public Button(Integer position, XMaterial buttonItem, List<String> lore, String title){
        if (position == null || position < 54) {
            this.position = position;
            this.buttonItem = createButton(buttonItem.parseItem(), lore, title );
        }
    }

    /**
     * Create button.
     *
     * @param position - int.
     * @param buttonItem - ItemStack.
     * @param title - String.
     * */
    public Button(Integer position, ItemStack buttonItem, String title){
        if (position == null || position < 54) {
            this.position = position;
            this.buttonItem = createButton(buttonItem, null, title );
        }
    }

    /**
     * Create button.
     *
     * @param position - int.
     * @param buttonItem - XMaterial.
     * @param title - String.
     * */
    public Button(Integer position, XMaterial buttonItem, String title){
        if (position == null || position < 54) {
            this.position = position;
            this.buttonItem = createButton(buttonItem.parseItem(), null, title );
        }
    }

    /**
     * Create button.
     *
     * @param position - int.
     * @param buttonItem - XMaterial.
     * @param amount - int.
     * @param lore - List<String>.
     * @param title - String.
     * */
    public Button(Integer position, XMaterial buttonItem, int amount, List<String> lore, String title){
        if (position == null || position < 54) {
            this.position = position;
            this.buttonItem = createButton(buttonItem.parseItem(), amount, lore, title );
        }
    }

    /**
     * Create button.
     *
     * @param position - int.
     * @param buttonItem - ItemStack.
     * */
    public Button(Integer position, ItemStack buttonItem){
        if (position == null || position < 54) {
            this.position = position;
            this.buttonItem = buttonItem;
        }
    }

    /**
     * Create button.
     *
     * @param position - int.
     * @param buttonMaterial - XMaterial.
     * @param amount - int.
     * @param title - String.
     * */
    public Button(Integer position, XMaterial buttonMaterial, int amount, String title){
    	if ( amount > 64 ) {
    		amount = 1;
    	}
        if ((position == null || position < 54) && amount <= 64) {
            this.position = position;
            this.buttonItem = createButton(buttonMaterial.parseItem(), amount, null, title );
        }
    }

//    /**
//     * Bug: Cannot correctly create a button with Material variants with spigot versions less than 1.13:
//     * 
//     * Create button.
//     *
//     * @param position - int.
//     * @param buttonMaterial - Material.
//     * @param amount - int.
//     * @param title - String.
//     * */
//    public Button(Integer position, Material buttonMaterial, int amount, String title){
//    	if ( amount > 64 ) {
//    		amount = 1;
//    	}
//        if ((position == null || position < 54) && amount <= 64) {
//            this.position = position;
//            this.buttonItem = createButton(buttonMaterial, amount, null, SpigotPrison.format(title));
//        }
//    }

    /**
     * Create button.
     *
     * @param position - int.
     * @param buttonMaterial - Material.
     * @param amount - int.
     * @param lore - ButtonLore.
     * @param title - String.
     * */
    public Button(Integer position, XMaterial buttonMaterial, int amount, ButtonLore lore, String title){
    	if ( amount > 64 ) {
    		amount = 1;
    	}
        if (position == null || position < 54) {
            this.position = position;
            this.buttonItem = createButton(buttonMaterial.parseItem(), amount, 
            		(lore == null ? null : lore.getLore() ),
            		title );
        }
    }

    /**
     * Create button.
     *
     * @param position - int.
     * @param buttonMaterial - Material.
     * @param lore - ButtonLore.
     * @param title - String.
     * */
    public Button(Integer position, XMaterial buttonMaterial, ButtonLore lore, String title){
        if (position == null || position < 54) {
            this.position = position;
            this.buttonItem = createButton(buttonMaterial.parseItem(), 
            		(lore == null ? null : lore.getLore() ),
            		title );
        }
    }

    /**
     * Set button item.
     * */
    public void setButtonItem(ItemStack item){
        this.buttonItem = item;
    }

    /**
     * Set button position.
     * */
    public void setPosition(int position){
        this.position = position;
    }

    /**
     * Get button ItemStack.
     *
     * @return buttonItem - ItemStack.
     * */
    public ItemStack getButtonItem(){
        return buttonItem;
    }

    /**
     * Get button position.
     *
     * @return position - int.
     * */
    public Integer getButtonPosition(){
        return position;
    }

    /**
     * Get button quantity.
     * */
    public int getButtonQuantity(){
        return buttonItem.getAmount();
    }

    /**
     * Set button lore.
     *
     * @param lore - List
     * */
    public void setButtonLore(List<String> lore){
        if (buttonItem.getItemMeta() != null) {
            buttonItem = createButton(buttonItem, lore, buttonItem.getItemMeta().getDisplayName());
        } else {
            buttonItem = createButton(buttonItem, lore, buttonItem.getType().name());
        }
    }

    /**
     * Set button lore.
     *
     * @param lore - ButtonLore.
     * */
    public void setButtonLore(ButtonLore lore){
        if (buttonItem.getItemMeta() != null) {
            buttonItem = createButton(buttonItem, lore.getLore(), buttonItem.getItemMeta().getDisplayName());
        } else {
            buttonItem = createButton(buttonItem, lore.getLore(), buttonItem.getType().name());
        }
    }

    /**
     * Add line to button lore.
     *
     * @param line - String.
     * */
    public void addButtonLoreLine(String line){
        if (getButtonLore() != null){
            List<String> lore = getButtonLore();
            lore.add( line );
            setButtonLore(lore);
        } else {
            setButtonLore(createLore( line ));
        }
    }

    /**
     * Add an unsafe enchantment to the button.
     *
     * @param enchant - Enchantment.
     * @param level - int.
     * */
    public void addUnsafeEnchantment(Enchantment enchant, int level){
        buttonItem.addUnsafeEnchantment(enchant, level);
    }

    /**
     * Get button lore.
     *
     * @return buttonLore - List<String>
     * */
    public List<String> getButtonLore(){
        if (buttonItem.getItemMeta() != null && buttonItem.getItemMeta().getLore() != null){
            return buttonItem.getItemMeta().getLore();
        }
        return null;
    }
}
