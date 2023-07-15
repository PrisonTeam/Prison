package tech.mcprison.prison.sellall.messages;

import tech.mcprison.prison.gui.PrisonCoreGuiMessages;
import tech.mcprison.prison.internal.CommandSender;
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
public class SpigotVariousGuiMessages
	extends PrisonCoreGuiMessages {
	
	
	
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
	
	
	public String sellallAmountEarnedMsg( String earningsAmount ) {
		
		return PrisonSellall.getInstance().getSellallMessages()
				.getLocalizable( "sellall_spigot_utils__money_earned" )
				.withReplacements( earningsAmount )
				.localize();
	}
	
	protected void sellallCanOnlyUseSignsMsg( CommandSender sender ) {
		PrisonSellall.getInstance().getSellallMessages()
				.getLocalizable( "sellall_spigot_utils__only_sellall_signs_are_enabled" )
				.sendTo( sender );
	}
	
	protected void sellallRateLimitExceededMsg( CommandSender sender ) {
		PrisonSellall.getInstance().getSellallMessages()
		.getLocalizable( "sellall_spigot_utils__rate_limit_exceeded" )
		.sendTo( sender );
	}
	
	
	protected void sellallShopIsEmptyMsg( CommandSender sender ) {
		PrisonSellall.getInstance().getSellallMessages()
		.getLocalizable( "sellall_spigot_utils__shop_is_empty" )
		.sendTo( sender );
	}
	
	public void sellallYouHaveNothingToSellMsg( CommandSender sender ) {
		PrisonSellall.getInstance().getSellallMessages()
		.getLocalizable( "sellall_spigot_utils__you_have_nothing_to_sell" )
		.sendTo( sender );
	}
	
	public void sellallIsDisabledMsg( CommandSender sender ) {
		PrisonSellall.getInstance().getSellallMessages()
		.getLocalizable( "sellall_spigot_utils__sellall_is_disabled" )
		.sendTo( sender );
	}
	
	public void sellallGUIIsDisabledMsg( CommandSender sender ) {
		PrisonSellall.getInstance().getSellallMessages()
		.getLocalizable( "sellall_spigot_utils__sellall_gui_is_disabled" )
		.sendTo( sender );
	}
	
	
}
