package tech.mcprison.prison.spigot.configs;

import tech.mcprison.prison.spigot.SpigotPrison;

import java.io.*;
import java.util.Properties;

public class NewMessagesConfig {

    private static NewMessagesConfig instance;
    private Properties properties = new Properties();
    private final String defaultLanguage = SpigotPrison.getInstance().getConfig().getString("default-language");
    private final String path = "/module_conf/spigot/lang/";

    /**
     * Get MessagesConfig class and initialize it if necessary.
     * */
    public static NewMessagesConfig get(){
        if (instance == null){
         instance = new NewMessagesConfig();
         instance.initConfig();
        }
        return instance;
    }

    /**
     * Initialize the config, reading and caching data.
     * */
    private void initConfig(){
        try(FileInputStream data = new FileInputStream(SpigotPrison.getInstance().getDataFolder() + path + defaultLanguage + ".properties")){

            Properties temp = new Properties();
            temp.load(new InputStreamReader(data));
            properties = temp;

        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Get String.
     * */
    public String getString(StringID message){
        return properties.getProperty(message.toString());
    }

    public void reload(){
        initConfig();
    }

    public enum StringID {

        spigot_gui_lore_click_to_add,
        spigot_gui_lore_click_to_add_backpack,
        spigot_gui_lore_click_to_cancel,
        spigot_gui_lore_click_to_close,
        spigot_gui_lore_click_to_confirm,
        spigot_gui_lore_click_to_decrease,
        spigot_gui_lore_click_to_delete,
        spigot_gui_lore_click_to_disable,
        spigot_gui_lore_click_to_edit,
        spigot_gui_lore_click_to_enable,
        spigot_gui_lore_click_to_increase,
        spigot_gui_lore_click_to_manage_rank,
        spigot_gui_lore_click_to_open,
        spigot_gui_lore_click_to_rankup,
        spigot_gui_lore_click_to_rename,
        spigot_gui_lore_click_to_select,
        spigot_gui_lore_click_to_start_block_setup,
        spigot_gui_lore_click_to_teleport,
        spigot_gui_lore_click_to_use,

        spigot_gui_lore_click_left_to_confirm,
        spigot_gui_lore_click_left_to_edit,
        spigot_gui_lore_click_left_to_open,
        spigot_gui_lore_click_left_to_reset,

        spigot_gui_lore_click_right_to_cancel,
        spigot_gui_lore_click_right_to_delete,
        spigot_gui_lore_click_right_to_disable,
        spigot_gui_lore_click_right_to_enable,
        spigot_gui_lore_click_right_to_toggle,

        spigot_gui_lore_click_right_and_shift_to_delete,
        spigot_gui_lore_click_right_and_shift_to_disable,
        spigot_gui_lore_click_right_and_shift_to_toggle,

        spigot_gui_lore_backpack_id,
        spigot_gui_lore_blocks,
        spigot_gui_lore_blocktype,
        spigot_gui_lore_chance,
        spigot_gui_lore_command,
        spigot_gui_lore_currency,
        spigot_gui_lore_delay,
        spigot_gui_lore_id,
        spigot_gui_lore_info,
        spigot_gui_lore_minename,
        spigot_gui_lore_multiplier,
        spigot_gui_lore_name,
        spigot_gui_lore_owner,
        spigot_gui_lore_percentage,
        spigot_gui_lore_permission,
        spigot_gui_lore_players_at_rank,
        spigot_gui_lore_prestige_name,
        spigot_gui_lore_price,
        spigot_gui_lore_radius,
        spigot_gui_lore_rank_tag,
        spigot_gui_lore_reset_time,
        spigot_gui_lore_show_item,
        spigot_gui_lore_size,
        spigot_gui_lore_spawnpoint,
        spigot_gui_lore_volume,
        spigot_gui_lore_value,
        spigot_gui_lore_world,

        spigot_gui_lore_disabled,
        spigot_gui_lore_enabled,
        spigot_gui_lore_locked,
        spigot_gui_lore_next_page,
        spigot_gui_lore_prior_page,
        spigot_gui_lore_rankup,
        spigot_gui_lore_selected,
        spigot_gui_lore_unlocked,

        spigot_gui_lore_add_backpack_instruction_1,
        spigot_gui_lore_add_backpack_instruction_2,
        spigot_gui_lore_add_backpack_instruction_3,
        spigot_gui_lore_prestige_warning_1,
        spigot_gui_lore_prestige_warning_2,
        spigot_gui_lore_prestige_warning_3,
        spigot_gui_lore_ranks_setup_1,
        spigot_gui_lore_ranks_setup_2,
        spigot_gui_lore_ranks_setup_3,
        spigot_gui_lore_ranks_setup_4,
        spigot_gui_lore_ranks_setup_5,
        spigot_gui_lore_ranks_setup_6,
        spigot_gui_lore_ranks_setup_7,
        spigot_gui_lore_ranks_setup_8,
        spigot_gui_lore_sellall_delay_use_1,
        spigot_gui_lore_sellall_delay_use_2,
        spigot_gui_lore_set_mine_delay_instruction_1,
        spigot_gui_lore_set_mine_delay_instruction_2,
        spigot_gui_lore_set_mine_delay_instruction_3,
        spigot_gui_lore_show_item_description_1,
        spigot_gui_lore_show_item_description_2,
        spigot_gui_lore_show_item_description_3,
        spigot_gui_lore_skip_reset_instruction_1,
        spigot_gui_lore_skip_reset_instruction_2,
        spigot_gui_lore_skip_reset_instruction_3,

        spigot_gui_lore_autofeatures_button_description,
        spigot_gui_lore_backpacks_button_description,
        spigot_gui_lore_disable_notifications,
        spigot_gui_lore_enable_radius_mode,
        spigot_gui_lore_enable_within_mode,
        spigot_gui_lore_mines_button_description,
        spigot_gui_lore_no_multipliers,
        spigot_gui_lore_ranks_button_description,
        spigot_gui_lore_rankup_if_enough_money,
        spigot_gui_lore_sellall_button_description,
        spigot_gui_lore_sellall_edit_info,
        spigot_gui_lore_tp_to_mine,

        spigot_general_missing_permission
    }
}
