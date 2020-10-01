package tech.mcprison.prison.spigot.gui.mine;

import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.data.PrisonSortableMines;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.ListenersPrisonManager;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

public class SpigotPlayerMinesGUI extends SpigotGUIComponents {

    private final Player p;

    public SpigotPlayerMinesGUI(Player p) {
        this.p = p;
    }

    public void open(){

        // Init the ItemStack
        // ItemStack itemMines;

        // Get the mines
    	Set<Mine> mines = new PrisonSortableMines().getSortedSet();
        //PrisonMines pMines = PrisonMines.getInstance();

        // Get the dimensions and if needed increases them
        int dimension = (int) Math.ceil(mines.size() / 9D) * 9;

        // Load config
        Configuration GuiConfig = SpigotPrison.getGuiConfig();
        Configuration messages = SpigotPrison.getGuiMessagesConfig();

        // If the inventory is empty
        if (dimension == 0){
            p.sendMessage(SpigotPrison.format(messages.getString("Gui.Message.NoMines")));
            p.closeInventory();
            return;
        }

        // If the dimension's too big, don't open the GUI
        if (dimension > 54){
            p.sendMessage(SpigotPrison.format(messages.getString("Gui.Message.TooManyMines")));
            p.closeInventory();
            return;
        }

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3Mines -> PlayerMines"));

        // Make the buttons for every Mine with info
        for (Mine m : mines) {

            // Init the lore array with default values for ladders
            List<String> minesLore = createLore(
                   );

            if (guiBuilder(GuiConfig, inv, m, minesLore)) return;

        }

        // Open the inventory
        this.p.openInventory(inv);
        ListenersPrisonManager.get().addToGUIBlocker(p);
    }

    private boolean guiBuilder(Configuration guiConfig, Inventory inv, Mine m, List<String> minesLore) {
        try {
            buttonsSetup(guiConfig, inv, m, minesLore);
        } catch (NullPointerException ex){
            p.sendMessage(SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private void buttonsSetup(Configuration guiConfig, Inventory inv, Mine m, List<String> minesLore) {

        Configuration messages = SpigotPrison.getGuiMessagesConfig();

        ItemStack itemMines;
        Material material;
        String permission = SpigotPrison.format(guiConfig.getString("Options.Mines.PermissionWarpPlugin"));

        if (p.hasPermission(permission + m.getName()) || p.hasPermission(permission.substring(0, permission.length() - 1))){
            material = Material.COAL_ORE;
            minesLore.add(SpigotPrison.format(messages.getString("Gui.Lore.StatusUnlockedMine")));
            minesLore.add(SpigotPrison.format(messages.getString("Gui.Lore.ClickToTeleport")));
        } else {
            material = Material.REDSTONE_BLOCK;
            minesLore.add(SpigotPrison.format(messages.getString("Gui.Lore.StatusLockedMine")));
        }

        // Create the button
        itemMines = createButton(material, 1, minesLore, SpigotPrison.format("&3" + m.getName()));

        // Add the button to the inventory
        inv.addItem(itemMines);
    }

}
