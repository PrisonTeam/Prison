package tech.mcprison.prison.spigot.gui.rank;

import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

public class SpigotGUIMessages
	extends SpigotGUIComponents {


	protected void exampleMsg( CommandSender sender, String parameter ) {
		SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_examples__" )
				.withReplacements( 
						parameter )
				.sendTo( sender );
	}
	
	protected String exampleReturnMsg( String parameter ) {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_examples__" )
				.withReplacements( 
						parameter )
				.localize();
	}
	
	

	protected void guiRanksNoRanksMsg( CommandSender sender ) {
		SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_message_gui_ranks_empty" )
				.sendTo( sender );
	}
	
	protected String guiRanksClickToManageRankMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_click_to_manage_rank" )
				.localize();
	}
	
	protected String guiRanksLoreInfoMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_info" )
				.localize();
	}
	
	protected String guiRanksLoreIdMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_id" )
				.localize();
	}
	
	protected String guiRanksLoreNameMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_name" )
				.localize();
	}
	
	protected String guiRanksLoreRankTagMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_rank_tag" )
				.localize();
	}
	
	protected String guiRanksLorePlayersWithRankMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_players_at_rank" )
				.localize();
	}
	

	protected String guiRanksLoreCommandMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_command" )
				.localize();
	}
	
	protected String guiRanksLoreOwnerMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_owner" )
				.localize();
	}
	
	protected String guiRanksLoreBackpackIdMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_backpack_id" )
				.localize();
	}
	
	protected String guiRanksLoreChanceMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_chance" )
				.localize();
	}
	
	protected String guiRanksLoreBlockTypeMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_blocktype" )
				.localize();
	}
	
	protected String guiRanksLoreClickToAddMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_click_to_add" )
				.localize();
	}
	
	protected String guiRanksLoreClickToUseMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_click_to_use" )
				.localize();
	}
	
	protected String guiRanksLoreWorldMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_world" )
				.localize();
	}
	
	protected String guiRanksLoreSpawnPointMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_spawnpoint" )
				.localize();
	}
	
	protected String guiRanksLoreResetTimeMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_reset_time" )
				.localize();
	}
	
	protected String guiRanksLoreSizeMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_size" )
				.localize();
	}
	
	protected String guiRanksLoreVolumeMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_volume" )
				.localize();
	}
	
	protected String guiRanksLoreBlocksMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_blocks" )
				.localize();
	}
	
	protected String guiRanksLoreSkipResetInstruction1Msg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_skip_reset_instruction_1" )
				.localize();
	}
	
	protected String guiRanksLoreSkipResetInstruction2Msg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_skip_reset_instruction_2" )
				.localize();
	}
	
	protected String guiRanksLoreSkipResetInstruction3Msg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_skip_reset_instruction_3" )
				.localize();
	}
	
	protected String guiRanksLoreSetMineDelayInstruction1Msg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_set_mine_delay_instruction_1" )
				.localize();
	}
	
	protected String guiRanksLoreSetMineDelayInstruction2Msg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_set_mine_delay_instruction_2" )
				.localize();
	}
	
	protected String guiRanksLoreSetMineDelayInstruction3Msg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_set_mine_delay_instruction_3" )
				.localize();
	}
	
	protected String guiRanksLoreClickToTeleportMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_click_to_teleport" )
				.localize();
	}
	
	protected String guiRanksLoreTpToMineMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_tp_to_mine" )
				.localize();
	}
	
	protected String guiRanksLoreClickToRenameMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_click_to_rename" )
				.localize();
	}
	
	protected String guiRanksLoreMineNameMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_minename" )
				.localize();
	}
	
	protected String guiRanksLoreShowItemMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_show_item" )
				.localize();
	}

	protected String guiRanksLoreShowItemDescription1Msg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_show_item_description_1" )
				.localize();
	}
	
	protected String guiRanksLoreShowItemDescription2Msg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_show_item_description_2" )
				.localize();
	}
	
	protected String guiRanksLoreShowItemDescription3Msg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_show_item_description_3" )
				.localize();
	}
	
	protected String guiRanksLoreUnlockedMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_unlocked" )
				.localize();
	}
	
	protected String guiRanksLoreLockedMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_locked" )
				.localize();
	}
	
	protected String guiRanksLoreClickToSelectMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_click_to_select" )
				.localize();
	}
	
	protected String guiRanksLoreEnableWithinModeMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_enable_within_mode" )
				.localize();
	}
	
	protected String guiRanksLoreEnableRadiusModeMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_enable_radius_mode" )
				.localize();
	}
	
	protected String guiRanksLoreDisableNotificationsMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_disable_notifications" )
				.localize();
	}
	
	protected String guiRanksLoreSelecteMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_selected" )
				.localize();
	}
	
	protected String guiRanksLoreRadiusMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_radius" )
				.localize();
	}
	
	protected String guiRanksLorePercentageMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_percentage" )
				.localize();
	}
	
	protected String guiRanksLoreClickToStartBlockSetupMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_click_to_start_block_setup" )
				.localize();
	}
	
	

	protected void guiRanksLadderIsEmptyMsg( CommandSender sender, String ladderName ) {
		SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_message_gui_ladder_empty" )
				.withReplacements( 
						ladderName )
				.sendTo( sender );
	}
	
	
	protected String guiRanksLoreClickToRankupMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_click_to_rankup" )
				.localize();
	}
	
	protected String guiRanksLoreRankupIfEnoughMoneyMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_gui_lore_rankup_if_enough_money" )
				.localize();
	}
	
	

	protected void guiRanksPrestigesLadderIsEmptyMsg( CommandSender sender ) {
		SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_message_prestiges_empty" )
				.sendTo( sender );
	}

	
	protected void guiRanksRankupCommandsEmptyMsg( CommandSender sender ) {
		SpigotPrison.getInstance().getLocaleManager()
		.getLocalizable( "spigot_message_gui_ranks_rankup_commands_empty" )
		.sendTo( sender );
	}
	
	protected void guiRanksErrorEmptyMsg( CommandSender sender ) {
		SpigotPrison.getInstance().getLocaleManager()
		.getLocalizable( "spigot_message_gui_error_empty" )
		.sendTo( sender );
	}
	
	protected void guiRanksRankupCommandsTooManyMsg( CommandSender sender ) {
		SpigotPrison.getInstance().getLocaleManager()
		.getLocalizable( "spigot_message_gui_ranks_rankup_commands_too_many" )
		.sendTo( sender );
	}
	
	
}
