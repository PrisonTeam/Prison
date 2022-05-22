package tech.mcprison.prison.gui;

import java.text.DecimalFormat;

import tech.mcprison.prison.Prison;

public class PrisonCoreGuiMessages {
	

	protected String guiClickToDecreaseMsg() {
		return Prison.get().getLocaleManager()
		.getLocalizable( "sellall_spigot_gui__click_to_decrease" )
		.localize();
	}
	protected String guiClickToIncreaseMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "sellall_spigot_gui__click_to_increase" )
				.localize();
	}
	protected String guiClickToEditMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "sellall_spigot_gui__click_to_edit" )
				.localize();
	}
	protected String guiClickToOpenMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "sellall_spigot_gui__click_to_open" )
				.localize();
	}
	
	protected String guiLeftClickToConfirmMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "sellall_spigot_gui__left_click_to_confirm" )
				.localize();
	}
	protected String guiLeftClickToResetMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "sellall_spigot_gui__left_click_to_reset" )
				.localize();
	}
	protected String guiLeftClickToOpenMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "sellall_spigot_gui__left_click_to_open" )
				.localize();
	}
	protected String guiLeftClickToEditMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "sellall_spigot_gui__left_click_to_edit" )
				.localize();
	}
	
	protected String guiRightClickToCancelMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "sellall_spigot_gui__right_click_to_cancel" )
				.localize();
	}
	protected String guiRightClickToDeleteMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "sellall_spigot_gui__right_click_to_delete" )
				.localize();
	}
	protected String guiRightClickToDisableMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "sellall_spigot_gui__right_click_to_disable" )
				.localize();
	}
	protected String guiRightClickToEnableMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "sellall_spigot_gui__right_click_to_enable" )
				.localize();
	}
	protected String guiRightClickToToggleMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "sellall_spigot_gui__right_click_to_toggle" )
				.localize();
	}

	
	protected String guiRightClickShiftToDeleteMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "sellall_spigot_gui__right_click_and_shift_to_delete" )
				.localize();
	}
	protected String guiRightClickShiftToDisableMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "sellall_spigot_gui__right_click_and_shift_to_disable" )
				.localize();
	}
	protected String guiRightClickShiftToToggleMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "sellall_spigot_gui__right_click_and_shift_to_toggle" )
				.localize();
	}
	

	
	protected String guiPageNextMsg() {
		
		return Prison.get().getLocaleManager()
				.getLocalizable( "sellall_spigot_gui__page_next" )
				.localize();
	}
	protected String guiPagePriorMsg() {
		
		return Prison.get().getLocaleManager()
				.getLocalizable( "sellall_spigot_gui__page_prior" )
				.localize();
	}
	
	
	protected String guiPriceMsg( Double price ) {
		
		DecimalFormat dFmt = new DecimalFormat( "#,##0.00" );
		String value = price == null ? dFmt.format(0) : dFmt.format(price);
		
		return guiPriceMsg( value );
	}
	protected String guiPriceMsg( Integer price ) {
		
		DecimalFormat dFmt = new DecimalFormat( "#,##0" );
		String value = price == null ? dFmt.format(0) : dFmt.format(price);
		
		return guiPriceMsg( value );
	}
	protected String guiPriceMsg( String price ) {
		
		return Prison.get().getLocaleManager()
				.getLocalizable( "sellall_spigot_utils__money_earned" )
				.withReplacements( price )
				.localize();
	}

	
	protected String guiConfirmMsg( String prestigeName, double value ) {
		
		DecimalFormat dFmt = new DecimalFormat( "#,##0.0" );
		String valueStr = dFmt.format(value);
		
		return Prison.get().getLocaleManager()
				.getLocalizable( "sellall_spigot_gui__confirm" )
				.withReplacements( prestigeName, valueStr )
				.localize();
	}
	
	protected String guiMultiplierMsg( double value ) {
		
		DecimalFormat dFmt = new DecimalFormat( "#,##0.0" );
		String valueStr = dFmt.format(value);
		
		return guiMultiplierMsg( valueStr );
	}
	protected String guiMultiplierMsg( String value ) {
		
		return Prison.get().getLocaleManager()
				.getLocalizable( "sellall_spigot_gui__multiplier" )
				.withReplacements( value )
				.localize();
	}
	
	protected String guiPrestigeNameMsg( String prestigeName ) {
		
		return Prison.get().getLocaleManager()
				.getLocalizable( "sellall_spigot_gui__prestige_name" )
				.withReplacements( prestigeName )
				.localize();
	}
	
	
}
