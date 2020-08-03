package tech.mcprison.prison.spigot.gui.mine;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

import java.util.List;

public class SpigotPlayerMinesGUI extends SpigotGUIComponents {

    private final Player p;

    public SpigotPlayerMinesGUI(Player p) {
        this.p = p;
    }

    public void open(){

        // Init the ItemStack
//        ItemStack itemines;

        // Get the mines
        PrisonMines pMines = PrisonMines.getInstance();

        // Get the dimensions and if needed increases them
        int dimension = (int) Math.ceil(pMines.getMines().size() / 9D) * 9;

        // Load config
        Configuration GuiConfig = SpigotPrison.getGuiConfig();

        // If the inventory is empty
        if (dimension == 0){
            p.sendMessage(SpigotPrison.format(GuiConfig.getString("Gui.Message.EmptyGui")));
            p.closeInventory();
            return;
        }

        // If the dimension's too big, don't open the GUI
        if (dimension > 54){
            p.sendMessage(SpigotPrison.format(GuiConfig.getString("Gui.Message.TooManyMines")));
            p.closeInventory();
            return;
        }

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3Mines -> PlayerMines"));

        // Make the buttons for every Mine with info
        for (Mine m : pMines.getMines()) {

            // Init the lore array with default values for ladders
            List<String> mineslore = createLore(
                   );

            if (guiBuilder(GuiConfig, inv, m, mineslore)) return;

        }

        // Open the inventory
        this.p.openInventory(inv);

    }

    private boolean guiBuilder(Configuration guiConfig, Inventory inv, Mine m, List<String> mineslore) {
        try {
            buttonsSetup(guiConfig, inv, m, mineslore);
        } catch (NullPointerException ex){
            p.sendMessage(SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private void buttonsSetup(Configuration guiConfig, Inventory inv, Mine m, List<String> mineslore) {
        ItemStack itemines;
        Material material;

        if (p.hasPermission(guiConfig.getString("Options.Mines.PermissionWarpPlugin") + m.getName())){
            material = Material.COAL_ORE;
            mineslore.add(SpigotPrison.format(guiConfig.getString("Gui.Lore.StatusUnlockedMine")));
            mineslore.add(SpigotPrison.format(guiConfig.getString("Gui.Lore.ClickToTeleport")));
        } else {
            material = Material.REDSTONE_BLOCK;
            mineslore.add(SpigotPrison.format(guiConfig.getString("Gui.Lore.StatusLockedMine")));
        }

        // Create the button
        itemines = createButton(material, 1, mineslore, SpigotPrison.format("&3" + m.getName()));

        // Add the button to the inventory
        inv.addItem(itemines);
    }

}
