package tech.mcprison.prison.spigot.gui.guiutility;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author GABRYCA
 * */
public class Button extends SpigotGUIComponents{

    private ItemStack buttonItem;
    private int position;

    /**
     * Create button.
     *
     * @param position - int.
     * @param buttonItem - ItemStack.
     * @param title - String.
     * */
    public Button(int position, ItemStack buttonItem, String title){
        if (position < 54) {
            this.position = position;
            this.buttonItem = createButton(buttonItem, null, title);
        }
    }

    /**
     * Create button.
     *
     * @param position - int.
     * @param buttonItem - XMaterial.
     * @param title - String.
     * */
    public Button(int position, XMaterial buttonItem, String title){
        if (position < 54) {
            this.position = position;
            this.buttonItem = createButton(buttonItem.parseItem(), null, title);
        }
    }

    /**
     * Create button.
     *
     * @param position - int.
     * @param buttonItem - ItemStack.
     * */
    public Button(int position, ItemStack buttonItem){
        if (position < 54) {
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
    public Button(int position, XMaterial buttonMaterial, int amount, String title){
        if (position < 54 && amount <= 64) {
            this.position = position;
            this.buttonItem = createButton(buttonMaterial.parseMaterial(), amount, null, title);
        }
    }

    /**
     * Create button.
     *
     * @param position - int.
     * @param buttonMaterial - Material.
     * @param amount - int.
     * @param title - String.
     * */
    public Button(int position, Material buttonMaterial, int amount, String title){
        if (position < 54 && amount <= 64) {
            this.position = position;
            this.buttonItem = createButton(buttonMaterial, amount, null, title);
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
    public int getButtonPosition(){
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
     * @param lore - List<String>
     * */
    public void setButtonLore(List<String> lore){
        if (buttonItem.getItemMeta() != null) {
            buttonItem = createButton(buttonItem, lore, buttonItem.getItemMeta().getDisplayName());
        } else {
            buttonItem = createButton(buttonItem, lore, buttonItem.getType().name());
        }
    }

    /**
     * Add line to button lore.
     *
     * @param line - String.
     * */
    public void addButtonLoreLine(String line){
        if (buttonItem.getItemMeta() != null && buttonItem.getItemMeta().getLore() != null){
            buttonItem.getItemMeta().getLore().add(line);
        } else {
            setButtonLore(createLore(line));
        }
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
