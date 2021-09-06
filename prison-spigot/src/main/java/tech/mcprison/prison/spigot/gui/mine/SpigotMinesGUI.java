package tech.mcprison.prison.spigot.gui.mine;

import com.cryptomorin.xseries.XMaterial;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.data.PrisonSortableResults;
import tech.mcprison.prison.mines.managers.MineManager.MineSortOrder;
import tech.mcprison.prison.spigot.configs.NewMessagesConfig;
import tech.mcprison.prison.spigot.gui.PrisonSetupGUI;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

import java.text.DecimalFormat;

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

        // Create GUI.
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3MinesManager -> Mines");

        // Global Strings.
        String loreLeftClickOpen = newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_left_to_open);
        String loreShiftRightClickToDelete = newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_right_and_shift_to_delete);
        String loreInfo = newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_info);
        String loreWorld = newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_world);
        String loreSpawnPoint = newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_spawnpoint);
        String loreResetTime = newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_reset_time);
        String loreSizeOfMine = newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_size);
        String loreVolume = newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_volume);
        String loreBlocks = newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_blocks);

        // Global boolean.
        boolean useNewBlockModel = Prison.get().getPlatform().isUseNewPrisonBlockModel();

        // Only loop over the blocks that we need to show:
        int i = counter;
        for ( ; i < mines.getSortedList().size() && i < counter + pageSize; i++ ) {

            Mine m = mines.getSortedList().get(i);

            ButtonLore minesLore = new ButtonLore(createLore(
                    loreLeftClickOpen,
                    loreShiftRightClickToDelete
            ), createLore(loreInfo));

            // Add a lore
            minesLore.addLineLoreDescription("&7" + loreWorld + " &b" + m.getWorldName());
            String spawnPoint = m.getSpawn() != null ? m.getSpawn().toBlockCoordinates() : "&cnot set";
            minesLore.addLineLoreDescription("&7" + loreSpawnPoint + " &b" + spawnPoint);
            minesLore.addLineLoreDescription("&7" + loreResetTime + " &b" + m.getResetTime());

            if (!m.isVirtual()) {
                // Add a lore
                minesLore.addLineLoreDescription("&7" + loreSizeOfMine + " &b" + m.getBounds().getDimensions());
                minesLore.addLineLoreDescription("&7" + loreVolume + " &b" + m.getBounds().getTotalBlockCount());
            }

            // Add a lore
            minesLore.addLineLoreDescription("&7" + loreBlocks);

            // Init some variables and do the actions
            DecimalFormat dFmt = new DecimalFormat("##0.00");
            double totalChance = 0.0d;

            if (useNewBlockModel) {

                for (PrisonBlock block : m.getPrisonBlocks()) {
                    double chance = Math.round(block.getChance() * 100.0d) / 100.0d;
                    totalChance += chance;

                    String blockName =
                            StringUtils.capitalize(block.getBlockName().replaceAll("_", " ").toLowerCase());
                    minesLore.addLineLoreDescription("&7" + chance + "% - " + block.getBlockName() + "   (" + blockName + ")");
                }
            } /*else {

                for (BlockOld block : m.getBlocks()) {
                    double chance = Math.round(block.getChance() * 100.0d) / 100.0d;
                    totalChance += chance;
                    String blockName =
                            StringUtils.capitalize(block.getType().name().replaceAll("_", " ").toLowerCase());
                    minesLore.add(SpigotPrison.format("&7" + chance + "% - " + block.getType().name() + "   (" + blockName + ")"));
                }
            }*/

            if (totalChance < 100.0d) {
                minesLore.addLineLoreDescription("&e " + dFmt.format(100.0d - totalChance) + "%  - Air");
            }

            gui.addButton(new Button(null, XMaterial.COAL_ORE, minesLore, "&3" + m.getName()));
        }

        if (i < mines.getSortedList().size()) {
            gui.addButton(new Button(53, XMaterial.BOOK, new ButtonLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_next_page), null), "&7Next " + (i + 1)));
        }
        if (i >= (pageSize * 2)) {
            gui.addButton(new Button(51, XMaterial.BOOK, new ButtonLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_prior_page), null), "&7Prior " + (i - (pageSize * 2) - 1)));
        }

        // Open the GUI.
        gui.open();
    }
}
