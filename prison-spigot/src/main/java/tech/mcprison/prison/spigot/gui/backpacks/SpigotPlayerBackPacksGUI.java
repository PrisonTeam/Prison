package tech.mcprison.prison.spigot.gui.backpacks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.backpacks.BackPacksUtil;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

public class SpigotPlayerBackPacksGUI extends SpigotGUIComponents {

    private final Player p;
    private final BackPacksUtil backPacksUtil = BackPacksUtil.get();
    private Inventory inv = null;
    int dimension = 54;

    public SpigotPlayerBackPacksGUI(Player p) {
        this.p = p;
    }

    public void open(){

        // Create the inventory
        dimension = backPacksUtil.getBackpackSize(p);
        inv = Bukkit.createInventory(p, dimension, SpigotPrison.format("&3" + p.getName() + " -> Backpack"));

        if (guiBuilder()) return;

        // Open the inventory
        openGUI(p, inv);
    }

    private boolean guiBuilder() {
        try {
            buttonsSetup();
        } catch (NullPointerException ex){
            Output.get().sendError(new SpigotPlayer(p), SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private void buttonsSetup() {

        inv = backPacksUtil.getInventory(p, inv);
    }
}
