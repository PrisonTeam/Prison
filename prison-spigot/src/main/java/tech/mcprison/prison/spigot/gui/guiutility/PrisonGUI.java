package tech.mcprison.prison.spigot.gui.guiutility;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.game.SpigotPlayer;

/**
 * @author GABRYCA
 * */
public class PrisonGUI extends SpigotGUIComponents{

    private Player p;
    private Inventory inv;
    private String permission = null;

    /**
     * Basic PrisonGUI empty.
     *
     * @param p - Player.
     * @param size - int.
     * @param title - String.
     * */
    public PrisonGUI(Player p, int size, String title){
        this.p = p;
        this.inv = Bukkit.createInventory(null, size, SpigotPrison.format(title));
    }

    /**
     * Basic PrisonGUI with nothing, this is not recommended.
     *
     * @param p - Player.
     * */
    public PrisonGUI(Player p){
        this.p = p;
    }

    /**
     * Set an inventory for the GUI from another already externally made.
     *
     * @param inv - Inventory.
     * */
    public void setInventory(Inventory inv){
        this.inv = inv;
    }

    /**
     * Create an inventory with basic data.
     *
     * @param owner - Can be null, not required.
     * @param size - Integer multiple of 9, between 9 and 54.
     * @param title - Inventory title.
     * */
    public void createInventory(Player owner, int size, String title){
        this.inv = Bukkit.createInventory(owner, size, SpigotPrison.format(title));
    }

    /**
     * Set permission to open the Inventory/GUI.
     * */
    public void setPermission(String permission){
        this.permission = permission;
    }

    /**
     * Get inventory.
     *
     * @return inv - Inventory.
     * */
    public Inventory getInventory(){
        return inv;
    }

    /**
     * Add button to the GUI, you can set the position while creating the Button.
     *
     * (Give a look to the Button class in the same package of this utility).
     * */
    public void addButton(Button button){
        if (button != null){
            if (button.getButtonPosition() == null){
                inv.addItem(button.getButtonItem());
            } else if (button.getButtonPosition() < inv.getSize()){
                inv.setItem(button.getButtonPosition(), button.getButtonItem());
            }
        }
    }

    /**
     * Open GUI.
     * */
    public void open(){
        if (p != null && inv != null) {
            if (permission != null){
                if (p.hasPermission(permission)){
                    openGUI(p, inv);
                } else {
                    SpigotPlayer spigotPlayer = new SpigotPlayer(p);
                    Output.get().sendWarn(spigotPlayer, SpigotPrison.format("Sorry, you don't have the permission! [" + permission + "]"));
                }
            } else {
                openGUI(p, inv);
            }
        }
    }
}
