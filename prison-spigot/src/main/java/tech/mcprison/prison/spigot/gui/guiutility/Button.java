package tech.mcprison.prison.spigot.gui.guiutility;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.spigot.SpigotPrison;

import java.util.List;

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
            this.buttonItem = createButton(buttonItem.parseItem(), lore, SpigotPrison.format(title));
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
            this.buttonItem = createButton(buttonItem, null, SpigotPrison.format(title));
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
            this.buttonItem = createButton(buttonItem.parseItem(), null, SpigotPrison.format(title));
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
            this.buttonItem = createButton(buttonItem.parseMaterial(), amount, lore, SpigotPrison.format(title));
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
        if ((position == null || position < 54) && amount <= 64) {
            this.position = position;
            this.buttonItem = createButton(buttonMaterial.parseMaterial(), amount, null, SpigotPrison.format(title));
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
    public Button(Integer position, Material buttonMaterial, int amount, String title){
        if ((position == null || position < 54) && amount <= 64) {
            this.position = position;
            this.buttonItem = createButton(buttonMaterial, amount, null, SpigotPrison.format(title));
        }
    }

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
        if (position == null || position < 54) {
            this.position = position;
            this.buttonItem = createButton(buttonMaterial.parseMaterial(), amount, lore.getLore(), SpigotPrison.format(title));
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
            this.buttonItem = createButton(buttonMaterial.parseItem(), lore.getLore(), SpigotPrison.format(title));
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
            lore.add(SpigotPrison.format(line));
            setButtonLore(lore);
        } else {
            setButtonLore(createLore(SpigotPrison.format(line)));
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
