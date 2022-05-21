package tech.mcprison.prison.spigot.block;

import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.spigot.SpigotPrison;

public class OnBlockBreakEventCoreMessages {

	
	protected void exampleMsg( CommandSender sender, String mineName ) {
		SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_blockbreak_core__" )
				.withReplacements( 
						mineName )
				.sendTo( sender );
	}
	
	
	protected String mineIsBeingResetMsg( String mineTagName ) {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_blockbreak_mines__mine_is_being_reset__please_wait" )
				.withReplacements( 
						mineTagName )
				.localize();
	}
	
	protected String toolIsWornOutMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_blockbreak_mines__mine_is_being_reset__please_wait" )
				.localize();
	}
	
	protected String inventoryIsFullMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_auto_manager__inventory_is_full" )
				.localize();
	}
	
	protected String inventoryIsFullLosingItemsMsg() {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_auto_manager__inventory_is_full_losing_items" )
				.localize();
	}
	
	

}
