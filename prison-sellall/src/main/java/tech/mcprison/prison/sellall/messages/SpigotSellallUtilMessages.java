package tech.mcprison.prison.sellall.messages;

import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.sellall.PrisonSellall;

/**
 * Note that this is a message class that is providing message support for
 * other classes outside of the prison-sellall module.  Therefore this
 * class is not 'paired' with the class that is using it.
 * It should be noted that if these messages are intended to be used outside
 * of this module, the messages are still stored within the path of
 * `plugins/Prison/module_conf/sellall/lang/`.
 * 
 * Please note that the location of these language files are stored within 
 * core project's resources: 'lang/sellall/'.
 *
 */
public class SpigotSellallUtilMessages {
	// This does nothing. Used as an example until real messages can be added.
	protected String prisonSellallTest01Msg() {
		
		return PrisonSellall.getInstance().getSellallMessages()
    			.getLocalizable( "sellall_test__sample_01" )
    			.localize();
	}
	
	// This does nothing. Used as an example until real messages can be added.
	protected String prisonSellallTest02Msg( String parameter ) {
		
		return PrisonSellall.getInstance().getSellallMessages()
				.getLocalizable( "sellall_test__sample_02" )
				.withReplacements( parameter )
				.localize();
	}
	
	protected String prisonSellallTest03Msg() {
		
		String msg01 = prisonSellallTest01Msg();
		String msg02 = prisonSellallTest02Msg( "sample02" );
		
		return PrisonSellall.getInstance().getSellallMessages()
				.getLocalizable( "sellall_test__sample_03" )
				.withReplacements( 
						msg01,
						msg02 )
				.localize();
		
	}
	
	
	protected String sellallAmountEarnedMsg( String earningsAmount ) {
		
		return PrisonSellall.getInstance().getSellallMessages()
				.getLocalizable( "sellall_spigot_utils__money_earned" )
				.withReplacements( earningsAmount )
				.localize();
	}
	
	protected void sellallCanOnlyUseSignsMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "sellall_spigot_utils__only_sellall_signs_are_enabled" )
				.sendTo( sender );
	}
	
	protected void sellallRateLimitExceededMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
		.getLocalizable( "sellall_spigot_utils__rate_limit_exceeded" )
		.sendTo( sender );
	}
	
	
	protected void sellallShopIsEmptyMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
		.getLocalizable( "sellall_spigot_utils__shop_is_empty" )
		.sendTo( sender );
	}
	
	protected void sellallYouHaveNothingToSellMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
		.getLocalizable( "sellall_spigot_utils__you_have_nothing_to_sell" )
		.sendTo( sender );
	}
	
	
	protected String guiClickToDecreaseMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
		.getLocalizable( "sellall_spigot_gui__click_to_decrease" )
		.localize();
	}
	
	protected String guiClickToIncreaseMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "sellall_spigot_gui__click_to_increase" )
				.localize();
	}
	
	protected String guiLeftClickToConfirmMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "sellall_spigot_gui__left_click_to_confirm" )
				.localize();
	}
	protected String guiLeftClickToResetMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "sellall_spigot_gui__left_click_to_reset" )
				.localize();
	}
	protected String guiLeftClickToOpenMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "sellall_spigot_gui__left_click_to_open" )
				.localize();
	}
	protected String guiLeftClickToEditMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "sellall_spigot_gui__left_click_to_edit" )
				.localize();
	}
	
	protected String guiRightClickToCancelMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "sellall_spigot_gui__right_click_to_cancel" )
				.localize();
	}
	protected String guiRightClickToDeleteMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "sellall_spigot_gui__right_click_to_delete" )
				.localize();
	}
	protected String guiRightClickToDisableMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "sellall_spigot_gui__right_click_to_disable" )
				.localize();
	}
	protected String guiRightClickToEnableMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "sellall_spigot_gui__right_click_to_enable" )
				.localize();
	}
	protected String guiRightClickToToggleMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "sellall_spigot_gui__right_click_to_toggle" )
				.localize();
	}

	
	protected String guiRightClickShiftToDeleteMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "sellall_spigot_gui__right_click_and_shift_to_delete" )
				.localize();
	}
	protected String guiRightClickShiftToDisableMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "sellall_spigot_gui__right_click_and_shift_to_disable" )
				.localize();
	}
	protected String guiRightClickShiftToToggleMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "sellall_spigot_gui__right_click_and_shift_to_toggle" )
				.localize();
	}
	
	
	
}
