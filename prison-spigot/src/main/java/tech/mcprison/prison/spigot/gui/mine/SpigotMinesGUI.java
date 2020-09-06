package tech.mcprison.prison.spigot.gui.mine;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Block;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SpigotMinesGUI extends SpigotGUIComponents {

    private final Player p;

    public SpigotMinesGUI(Player p) {
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
            p.sendMessage(SpigotPrison.format(GuiConfig.getString("Gui.Message.NoMines")));
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
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3MinesManager -> Mines"));

        // Make the buttons for every Mine with info
        for (Mine m : pMines.getMines()) {

            if (guiBuilder(GuiConfig, inv, m)) return;

        }

        // Open the inventory
        this.p.openInventory(inv);

    }

    private boolean guiBuilder(Configuration guiConfig, Inventory inv, Mine m) {
        try {
            buttonsSetup(guiConfig, inv, m);
        } catch (NullPointerException ex){
            p.sendMessage(SpigotPrison.format("&cThere's a null value in the GuiConfig.yml [broken]"));
            ex.printStackTrace();
            return true;
        }
        return false;
    }

    private void buttonsSetup(Configuration guiConfig, Inventory inv, Mine m) {
        ItemStack itemines;
        // Init the lore array with default values for ladders
        List<String> mineslore = createLore(
                guiConfig.getString("Gui.Lore.LeftClickToOpen"),
                guiConfig.getString("Gui.Lore.ShiftAndRightClickToDelete"),
                "",
                guiConfig.getString("Gui.Lore.Info"));

        // Add a lore
        mineslore.add(SpigotPrison.format(guiConfig.getString("Gui.Lore.World") +  m.getWorldName()));

        // Init a variable and add it to the lore
        String spawnPoint = m.getSpawn() != null ? m.getSpawn().toBlockCoordinates() : "&cnot set";
        mineslore.add(SpigotPrison.format(guiConfig.getString("Gui.Lore.SpawnPoint") + spawnPoint));

        // Add a lore
        mineslore.add(SpigotPrison.format(guiConfig.getString("Gui.Lore.ResetTime") + m.getResetTime()));

        // Add a lore
        mineslore.add(SpigotPrison.format(guiConfig.getString("Gui.Lore.SizeOfMine") + m.getBounds().getDimensions()));

        // Add a lore
        mineslore.add(SpigotPrison.format(guiConfig.getString("Gui.Lore.Volume") + m.getBounds().getTotalBlockCount()));

        // Add a lore
        mineslore.add(SpigotPrison.format(guiConfig.getString("Gui.Lore.Blocks")));

        // Init some variables and do the actions
        DecimalFormat dFmt = new DecimalFormat("##0.00");
        double totalChance = 0.0d;
        
        boolean useNewBlockModel = Prison.get().getPlatform().getConfigBooleanFalse( "use-new-prison-block-model" );

        if ( useNewBlockModel ) {
        	
        	for (PrisonBlock block : m.getPrisonBlocks()) {
        		double chance = Math.round(block.getChance() * 100.0d) / 100.0d;
        		totalChance += chance;
        		
        		String blockName =
        				StringUtils.capitalize(block.getBlockName().replaceAll("_", " ").toLowerCase());
        		mineslore.add(SpigotPrison.format("&7" + chance + "% - " + block.getBlockName() + "   (" + blockName + ")"));
        	}
        }
        else {
        	
        	for (Block block : m.getBlocks()) {
        		double chance = Math.round(block.getChance() * 100.0d) / 100.0d;
        		totalChance += chance;
         		
        		String blockName =
        				StringUtils.capitalize(block.getType().name().replaceAll("_", " ").toLowerCase());
        		mineslore.add(SpigotPrison.format("&7" + chance + "% - " + block.getType().name() + "   (" + blockName + ")"));
        	}
        }
        

        if (totalChance < 100.0d) {
            mineslore.add(SpigotPrison.format("&e " + dFmt.format(100.0d - totalChance) + "%  - Air"));
        }

        // Create the button
        itemines = createButton(Material.COAL_ORE, 1, mineslore, SpigotPrison.format("&3" + m.getName()));

        // Add the button to the inventory
        inv.addItem(itemines);
    }

}
