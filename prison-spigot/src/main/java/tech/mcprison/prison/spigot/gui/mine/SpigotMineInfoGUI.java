package tech.mcprison.prison.spigot.gui.mine;

import java.util.List;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;

import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.configs.NewMessagesConfig;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.ButtonLore;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

/**
 * @author GABRYCA
 */
public class SpigotMineInfoGUI extends SpigotGUIComponents {

    private final Player p;
	private final Mine mine;
    private final String mineName;
    int dimension = 45;

    public SpigotMineInfoGUI(Player p, Mine mine, String mineName){
        this.p = p;
        this.mine = mine;
        this.mineName = mineName;
    }

    public void open(){

        PrisonGUI gui = new PrisonGUI(p, dimension, SpigotPrison.format("&3Mines -> MineInfo"));

        ButtonLore resetMineLore = new ButtonLore(createLore(
                newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_left_to_reset),
                newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_right_to_toggle),
                newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_right_and_shift_to_toggle)),
                createLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_skip_reset_instruction_1),
                        newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_skip_reset_instruction_2),
                        newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_skip_reset_instruction_3),
                        "",
                        newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_set_mine_delay_instruction_1),
                        newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_set_mine_delay_instruction_2),
                        newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_set_mine_delay_instruction_3)));

        ButtonLore mineSpawnLore = new ButtonLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_to_use), newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_spawnpoint));
        ButtonLore minesNotificationsLore = new ButtonLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_to_open), newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_to_edit));
        ButtonLore minesTpLore = new ButtonLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_to_teleport), newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_tp_to_mine));
        ButtonLore blocksOfTheMineLore = new ButtonLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_to_open), newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_blocks));
        ButtonLore mineResetTimeLore = new ButtonLore(createLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_to_open)), createLore(
                newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_reset_time) + " &7" + mine.getResetTime()));
        ButtonLore mineRenameLore = new ButtonLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_to_rename), newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_minename) + " " + mineName);
        ButtonLore closeGUILore = new ButtonLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_to_close), null);

        // Create the button, set the material, amount, lore and name
        gui.addButton(new Button(dimension-1, XMaterial.RED_STAINED_GLASS_PANE, closeGUILore, SpigotPrison.format("&cClose")));
        gui.addButton(new Button(10, XMaterial.EMERALD_BLOCK, resetMineLore, SpigotPrison.format("&3Reset_Mine: " + mineName)));
        gui.addButton(new Button(12, XMaterial.COMPASS, mineSpawnLore, SpigotPrison.format("&3Mine_Spawn: " + mineName)));
        gui.addButton(new Button(14, XMaterial.OAK_SIGN, minesNotificationsLore, SpigotPrison.format("&3Mine_notifications: " + mineName)));
        gui.addButton(new Button(16, XMaterial.ARROW, minesTpLore, SpigotPrison.format("&3TP_to_the_Mine: " + mineName)));
        gui.addButton(new Button(28, XMaterial.COAL_ORE, blocksOfTheMineLore, SpigotPrison.format("&3Blocks_of_the_Mine: " + mineName)));
        gui.addButton(new Button(30, XMaterial.CLOCK, 1, mineResetTimeLore, SpigotPrison.format("&3Reset_Time: " + mineName)));
        gui.addButton(new Button(32 ,XMaterial.FEATHER, mineRenameLore, SpigotPrison.format("&3Mine_Name: " + mineName)));

        // Mine show Item of Player's GUI aka /gui mines.
        XMaterial xMaterial = XMaterial.COAL_ORE;
        String customItem = guiConfig.getString("Options.Mines.MaterialType." + mineName);
        if (customItem != null){
            XMaterial mineXMaterial = SpigotUtil.getXMaterial(customItem);
            if (mineXMaterial != null){
                xMaterial = mineXMaterial;
            }
        }

        // Lore
        ButtonLore mineShowItemLore = new ButtonLore(createLore(newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_click_to_edit)), createLore(
                newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_show_item) + " &7" + xMaterial.name(),
                newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_show_item_description_1),
                newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_show_item_description_2),
                newMessages.getString(NewMessagesConfig.StringID.spigot_gui_lore_show_item_description_3)
        ));

        // ItemStack
        gui.addButton(new Button(34, xMaterial, mineShowItemLore, "&3Mine_Show_Item: " + mineName));

        // Opens the inventory
        gui.open();
    }
}
