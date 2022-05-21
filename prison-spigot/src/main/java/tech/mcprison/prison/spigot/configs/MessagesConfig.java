package tech.mcprison.prison.spigot.configs;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.spigot.SpigotPrison;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * <p>This class was supposed to use the multi-language support tools within prison, of which it does not.
 * Therefore, it needs to be trashed and replaced with the proper tools.
 * </p>
 * 
 * <p>Do not use!
 * </p>
 *
 */
@Deprecated
public class MessagesConfig {

    private static MessagesConfig instance;
    private Properties properties = new Properties();
    private final String defaultLanguage = SpigotPrison.getInstance().getConfig().getString("default-language");
    private final String path = "/module_conf/spigot/lang/";

    /**
     * Get MessagesConfig class and initialize it if necessary.
     * */
    @Deprecated
    public static MessagesConfig get(){
        if (instance == null){
         instance = new MessagesConfig();
         instance.initConfig();
        }
        return instance;
    }

    /**
     * Initialize the config, reading and caching data.
     * */
    @Deprecated
    private void initConfig(){
    	
    	String pathStr = SpigotPrison.getInstance().getDataFolder() + path;
    	File path = new File( pathStr );
    	
    	String enUSFileName = "en_US" + ".properties";
    	File enUSFile = new File( path, enUSFileName );
    	
    	String targetFileName = defaultLanguage + ".properties";
    	File targetFile = new File( path, targetFileName );
    	
    	if ( enUSFile.exists() && !targetFile.exists() ) {
    		targetFile = enUSFile;
    		
			Prison.get().getLocaleLoadInfo().add( String.format( 
					"&3pseudo-Module: &7GUI-sellall  &3Locale: &7%s  &3Warning: Locale file does not exist; using " +
					" &7%s.properties&3.  path: %s", 
					targetFileName, enUSFileName, pathStr ) );
    	}
    	else {
    		Prison.get().getLocaleLoadInfo().add( String.format( 
    				"&3pseudo-Module: &7GUI-sellall  &3Locale: &7%s  Using this language file with no validation.  path: %s", 
    						targetFileName, enUSFileName, pathStr ) );
    		
    	}
    	
    	
        try (
        		FileInputStream is = new FileInputStream( targetFile )
        	) {

            Properties temp = new Properties();

            // NOTE: This code is from tech.mcprison.prison.localization.LocaleManager.loadLocale(String, InputStream, boolean)
        	

            // The InputStream is part of a zipEntry so it cannot be closed, or it will close the zip stream
            BufferedReader br = new BufferedReader( new InputStreamReader( is, Charset.forName("UTF-8") ));
            String line = br.readLine();
          
            while ( line != null ) {
            	if ( !line.startsWith( "#" ) && line.contains( "=" ) ) {
          		
            		String[] keyValue = line.split( "\\=" );
            		String value = (keyValue.length > 1 ? keyValue[1] : ""); // StringEscapeUtils.escapeJava( keyValue[1] );
            		temp.put( keyValue[0], value );
            	}
          	
            	line = br.readLine();
            }
            
            // WARNING: cannot use the properties.load() function since it is NOT utf-8 capable.
//            temp.load(new InputStreamReader(data));
            properties = temp;

        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Get String.
     * */
    public String getString(StringID message){
        		
    	String msg = properties.getProperty(message.toString());
    	
    	if ( msg == null || msg.trim().isEmpty() ) {
    		msg = message.name();
    	}
    	
    	return msg;
    }

    @Deprecated
    public void reload(){
        initConfig();
    }

    public enum StringID {

        spigot_gui_lore_click_to_add,
        spigot_gui_lore_click_to_add_backpack,
        spigot_gui_lore_click_to_cancel,
        spigot_gui_lore_click_to_close,
        spigot_gui_lore_click_to_confirm,
//        spigot_gui_lore_click_to_decrease,
        spigot_gui_lore_click_to_delete,
        spigot_gui_lore_click_to_disable,
        spigot_gui_lore_click_to_edit,
        spigot_gui_lore_click_to_enable,
//        spigot_gui_lore_click_to_increase,
        spigot_gui_lore_click_to_manage_rank,
        spigot_gui_lore_click_to_open,
        spigot_gui_lore_click_to_rankup,
        spigot_gui_lore_click_to_rename,
        spigot_gui_lore_click_to_select,
        spigot_gui_lore_click_to_start_block_setup,
        spigot_gui_lore_click_to_teleport,
        spigot_gui_lore_click_to_use,

//        spigot_gui_lore_click_left_to_confirm,
//        spigot_gui_lore_click_left_to_edit,
//        spigot_gui_lore_click_left_to_open,
//        spigot_gui_lore_click_left_to_reset,

//        spigot_gui_lore_click_right_to_cancel,
//        spigot_gui_lore_click_right_to_delete,
//        spigot_gui_lore_click_right_to_disable,
//        spigot_gui_lore_click_right_to_enable,
//        spigot_gui_lore_click_right_to_toggle,

//        spigot_gui_lore_click_right_and_shift_to_delete,
//        spigot_gui_lore_click_right_and_shift_to_disable,
//        spigot_gui_lore_click_right_and_shift_to_toggle,

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
//        spigot_gui_lore_price,
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


        spigot_message_missing_permission,
        spigot_message_chat_event_time_end,
        spigot_message_event_cancelled,
        spigot_message_command_wrong_format,
        spigot_message_console_error,

        spigot_message_ladder_default_empty,

        spigot_message_mines_disabled,
        spigot_message_mines_name_chat_1,
        spigot_message_mines_name_chat_2,
        spigot_message_mines_name_chat_cancelled,
        spigot_message_mines_item_show_edit_success,
        spigot_message_mines_or_gui_disabled,

        spigot_message_backpack_cant_own,
        spigot_message_backpack_delete_error,
        spigot_message_backpack_delete_success,
        spigot_message_backpack_format_error,
        spigot_message_backpack_limit_decrement_fail,
        spigot_message_backpack_limit_edit_success,
        spigot_message_backpack_limit_not_number,
        spigot_message_backpack_limit_reached,
        spigot_message_backpack_missing_playername,
        spigot_message_backpack_resize_success,
        spigot_message_backpack_size_must_be_multiple_of_9,

        spigot_message_prestiges_disabled,
        spigot_message_prestiges_empty,
        spigot_message_prestiges_or_gui_disabled,
        spigot_message_prestiges_confirm,
        spigot_message_prestiges_cancel,
        spigot_message_prestiges_cancelled,
        spigot_message_prestiges_cancelled_wrong_keyword,

        spigot_message_ranks_disabled,
        spigot_message_ranks_or_gui_disabled,
        spigot_message_ranks_tag_chat_rename_1,
        spigot_message_ranks_tag_chat_rename_2,
        spigot_message_ranks_tag_chat_cancelled,
        
        spigot_message_ladders_disabled,
        spigot_message_ladders_or_gui_disabled,

        spigot_message_sellall_auto_already_enabled,
        spigot_message_sellall_auto_already_disabled,
        spigot_message_sellall_auto_disabled,
        spigot_message_sellall_auto_disabled_cant_use,
        spigot_message_sellall_auto_enabled,
        spigot_message_sellall_auto_perusertoggleable_enabled,
        spigot_message_sellall_auto_perusertoggleable_disabled,
        spigot_message_sellall_auto_perusertoggleable_already_enabled,
        spigot_message_sellall_auto_perusertoggleable_already_disabled,
        spigot_message_sellall_boolean_input_invalid,
        spigot_message_sellall_cant_find_item_config,
        spigot_message_sellall_currency_chat_1,
        spigot_message_sellall_currency_chat_2,
        spigot_message_sellall_currency_chat_3,
        spigot_message_sellall_currency_chat_4,
        spigot_message_sellall_currency_edit_success,
        spigot_message_sellall_currency_not_found,
        spigot_message_sellall_hand_disabled,
        spigot_message_sellall_hand_enabled,
        spigot_message_sellall_hand_is_disabled,
        spigot_message_sellall_item_add_success,
        spigot_message_sellall_item_already_added,
        spigot_message_sellall_item_delete_success,
        spigot_message_sellall_item_edit_success,
        spigot_message_sellall_item_id_not_found,
        spigot_message_sellall_item_missing_name,
        spigot_message_sellall_item_missing_price,
        spigot_message_sellall_item_not_found,
        spigot_message_sellall_default_values_success,
        spigot_message_sellall_delay_already_enabled,
        spigot_message_sellall_delay_already_disabled,
        spigot_message_sellall_delay_disabled,
        spigot_message_sellall_delay_disabled_cant_use,
        spigot_message_sellall_delay_edit_success,
        spigot_message_sellall_delay_enabled,
        spigot_message_sellall_delay_not_number,
//        spigot_message_sellall_delay_wait,
        spigot_message_sellall_gui_disabled,
//        spigot_message_sellall_money_earned,
        spigot_message_sellall_multiplier_add_success,
        spigot_message_sellall_multiplier_are_disabled,
        spigot_message_sellall_multiplier_cant_find,
        spigot_message_sellall_multiplier_delete_success,
        spigot_message_sellall_multiplier_disabled,
        spigot_message_sellall_multiplier_edit_success,
        spigot_message_sellall_multiplier_enabled,
//        spigot_message_sellall_sell_empty,
//        spigot_message_sellall_sell_nothing_sellable,
//        spigot_message_sellall_sell_sign_only,
        spigot_message_sellall_sell_sign_notify,
        spigot_message_sellall_trigger_already_disabled,
        spigot_message_sellall_trigger_already_enabled,
        spigot_message_sellall_trigger_disabled,
        spigot_message_sellall_trigger_enabled,
        spigot_message_sellall_trigger_is_disabled,
        spigot_message_sellall_trigger_item_add_success,
        spigot_message_sellall_trigger_item_cant_find,
        spigot_message_sellall_trigger_item_delete_success,
        spigot_message_sellall_trigger_item_missing,

        spigot_message_gui_backpack_disabled,
        spigot_message_gui_backpack_empty,
        spigot_message_gui_backpack_too_many,
        spigot_message_gui_close_success,
        spigot_message_gui_error,
        spigot_message_gui_error_empty,
        spigot_message_gui_ladder_empty,
        spigot_message_gui_ladder_too_many,
        spigot_message_gui_mines_empty,
        spigot_message_gui_mines_too_many,
        spigot_message_gui_prestiges_empty,
        spigot_message_gui_prestiges_too_many,
        spigot_message_gui_ranks_empty,
        spigot_message_gui_ranks_rankup_commands_empty,
        spigot_message_gui_ranks_rankup_commands_too_many,
        spigot_message_gui_ranks_too_many,
        spigot_message_gui_reload_success,
        spigot_message_gui_sellall_disabled,
        spigot_message_gui_sellall_empty,
        spigot_message_gui_too_high,
        spigot_message_gui_too_low_value,
    }
}
