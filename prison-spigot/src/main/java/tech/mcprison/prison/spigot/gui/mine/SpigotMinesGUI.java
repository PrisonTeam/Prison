package tech.mcprison.prison.spigot.gui.mine;

import com.cryptomorin.xseries.XMaterial;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.BlockOld;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.data.PrisonSortableResults;
import tech.mcprison.prison.mines.managers.MineManager.MineSortOrder;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.PrisonSetupGUI;
import tech.mcprison.prison.spigot.gui.SpigotGUIComponents;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @author GABRYCA
 * @author RoyalBlueRanger (rBluer)
 */
public class SpigotMinesGUI extends SpigotGUIComponents {

    private final Player p;
    private int counter;

    public SpigotMinesGUI(Player p, int counter) {
        this.p = p;
        this.counter = counter;
    }

    public void open(){

        // Get the mines - In sort order, minus any marked as suppressed
        PrisonSortableResults mines = PrisonMines.getInstance().getMines( MineSortOrder.sortOrder );


        // If the inventory is empty
        if (mines.getSortedList().size() == 0){
            p.closeInventory();
            PrisonSetupGUI gui = new PrisonSetupGUI(p);
            gui.open();
            return;
        }

        // Get the dimensions and if needed increases them
        int dimension = 54;
        int pageSize = 45;

        // Create the inventory and set up the owner, dimensions or number of slots, and title
        Inventory inv = Bukkit.createInventory(null, dimension, SpigotPrison.format("&3MinesManager -> Mines"));

        // Only loop over the blocks that we need to show:
        int i = counter;
        for ( ; i < mines.getSortedList().size() && i < counter + pageSize; i++ ) {

            Mine m = mines.getSortedList().get(i);

            // Init the lore array with default values for ladders
            List<String> minesLore = createLore(
                    messages.getString("Lore.LeftClickToOpen"),
                    messages.getString("Lore.ShiftAndRightClickToDelete"),
                    "",
                    messages.getString("Lore.Info"));

            // Add a lore
            minesLore.add(SpigotPrison.format(messages.getString("Lore.World") +  m.getWorldName()));
            String spawnPoint = m.getSpawn() != null ? m.getSpawn().toBlockCoordinates() : "&cnot set";
            minesLore.add(SpigotPrison.format(messages.getString("Lore.SpawnPoint") + spawnPoint));
            minesLore.add(SpigotPrison.format(messages.getString("Lore.ResetTime") + m.getResetTime()));

            if (!m.isVirtual()) {
                // Add a lore
                minesLore.add(SpigotPrison.format(messages.getString("Lore.SizeOfMine") + m.getBounds().getDimensions()));
                minesLore.add(SpigotPrison.format(messages.getString("Lore.Volume") + m.getBounds().getTotalBlockCount()));
            }

            // Add a lore
            minesLore.add(SpigotPrison.format(messages.getString("Lore.Blocks")));

            // Init some variables and do the actions
            DecimalFormat dFmt = new DecimalFormat("##0.00");
            double totalChance = 0.0d;

            boolean useNewBlockModel = Prison.get().getPlatform().getConfigBooleanFalse( "use-new-prison-block-model" );

            if (useNewBlockModel) {

                for (PrisonBlock block : m.getPrisonBlocks()) {
                    double chance = Math.round(block.getChance() * 100.0d) / 100.0d;
                    totalChance += chance;

                    String blockName =
                            StringUtils.capitalize(block.getBlockName().replaceAll("_", " ").toLowerCase());
                    minesLore.add(SpigotPrison.format("&7" + chance + "% - " + block.getBlockName() + "   (" + blockName + ")"));
                }
            } else {

                for (BlockOld block : m.getBlocks()) {
                    double chance = Math.round(block.getChance() * 100.0d) / 100.0d;
                    totalChance += chance;
                    String blockName =
                            StringUtils.capitalize(block.getType().name().replaceAll("_", " ").toLowerCase());
                    minesLore.add(SpigotPrison.format("&7" + chance + "% - " + block.getType().name() + "   (" + blockName + ")"));
                }
            }

            if (totalChance < 100.0d) {
                minesLore.add(SpigotPrison.format("&e " + dFmt.format(100.0d - totalChance) + "%  - Air"));
            }

            // Create the button
            ItemStack itemMines = createButton(XMaterial.COAL_ORE.parseItem(), minesLore, SpigotPrison.format("&3" + m.getName()));
            inv.addItem(itemMines);
        }

        if (i < mines.getSortedList().size()) {
            List<String> nextPageLore = createLore(messages.getString("Lore.ClickToNextPage"));

            ItemStack nextPageButton = createButton(Material.BOOK, 1, nextPageLore, "&7Next " + (i + 1));
            inv.setItem(53, nextPageButton);
        }
        if (i >= (pageSize * 2)) {
            List<String> priorPageLore = createLore(messages.getString("Lore.ClickToPriorPage"));

            ItemStack priorPageButton = createButton(Material.BOOK, 1, priorPageLore,
                    "&7Prior " + (i - (pageSize * 2) - 1));
            inv.setItem(51, priorPageButton);
        }

        // Open the inventory
        openGUI(p, inv);
    }
}
