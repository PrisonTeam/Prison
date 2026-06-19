package tech.mcprison.prison.backpacks;

import java.text.DecimalFormat;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.integration.IntegrationCore;
import tech.mcprison.prison.integration.IntegrationType;

public class PrisonCoreBackpackMessages
	extends IntegrationCore {
	
	public PrisonCoreBackpackMessages() {
		super( "PrisonBackpacks", "Prison", IntegrationType.BACKPACK );
	}

	protected String guiClickToDecreaseMsg() {
		return Prison.get().getLocaleManager()
		.getLocalizable( "core_gui__click_to_decrease" )
		.localize();
	}
	protected String guiClickToIncreaseMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__click_to_increase" )
				.localize();
	}
	
	
	protected String guiClickToCancelMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__click_to_cancel" )
				.localize();
	}
	protected String guiClickToCloseMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__click_to_close" )
				.localize();
	}
	protected String guiClickToConfirmMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__click_to_confirm" )
				.localize();
	}
	protected String guiClickToDeleteMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__click_to_delete" )
				.localize();
	}
	protected String guiClickToDisableMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__click_to_disable" )
				.localize();
	}
	protected String guiClickToEditMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__click_to_edit" )
				.localize();
	}
	protected String guiClickToEnableMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__click_to_enable" )
				.localize();
	}
	protected String guiClickToOpenMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__click_to_open" )
				.localize();
	}
	
	protected String guiLeftClickToConfirmMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__left_click_to_confirm" )
				.localize();
	}
	protected String guiLeftClickToResetMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__left_click_to_reset" )
				.localize();
	}
	protected String guiLeftClickToOpenMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__left_click_to_open" )
				.localize();
	}
	protected String guiLeftClickToEditMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__left_click_to_edit" )
				.localize();
	}
	
	protected String guiRightClickToCancelMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__right_click_to_cancel" )
				.localize();
	}
	protected String guiRightClickToDeleteMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__right_click_to_delete" )
				.localize();
	}
	protected String guiRightClickToDisableMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__right_click_to_disable" )
				.localize();
	}
	protected String guiRightClickToEnableMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__right_click_to_enable" )
				.localize();
	}
	protected String guiRightClickToToggleMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__right_click_to_toggle" )
				.localize();
	}

	
	protected String guiRightClickShiftToDeleteMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__right_click_and_shift_to_delete" )
				.localize();
	}
	protected String guiRightClickShiftToDisableMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__right_click_and_shift_to_disable" )
				.localize();
	}
	protected String guiRightClickShiftToToggleMsg() {
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__right_click_and_shift_to_toggle" )
				.localize();
	}
	

	
	protected String guiPageNextMsg() {
		
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__page_next" )
				.localize();
	}
	protected String guiPagePriorMsg() {
		
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__page_prior" )
				.localize();
	}

	protected String guiPageToolsCloseMsg() {
		
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__page_tools_close" )
				.localize();
	}
	protected String guiPageToolsGoBackMsg() {
		
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__page_tools_go_back" )
				.localize();
	}
	private String formmatPageToolsPlaceholders( String msg, int currentPage, int maxPage ) {
		
		if ( maxPage < 1 ) {
			maxPage = 1;
		}
		int priorPage = currentPage == 1 ? 1 : currentPage - 1;
		int nextPage = currentPage == maxPage ? maxPage : currentPage + 1;
		
		msg = msg.replace("{first_page}", "1" )
				 .replace("{prior_page}", Integer.toString(priorPage) )
				 .replace("{current_page}", Integer.toString(currentPage))
				 .replace("{next_page}", Integer.toString(nextPage))
				 .replace("{last_page}", Integer.toString(maxPage))
				 ;
				
		return msg;
	}
	protected String guiPageToolsCloseMsg( int currentPage, int maxPage ) {
		
		String msg = Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__page_tools_close" )
				.localize();
		
		return formmatPageToolsPlaceholders( msg, currentPage, maxPage );
	}
	protected String guiPageToolsFirstPageMsg( int currentPage, int maxPage ) {
		
		String msg = Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__page_tools_first_page" )
				.localize();
		
		return formmatPageToolsPlaceholders( msg, currentPage, maxPage );
	}
	protected String guiPageToolsPriorPageMsg( int currentPage, int maxPage ) {
		
		String msg = Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__page_tools_prior_page" )
				.localize();
		
		return formmatPageToolsPlaceholders( msg, currentPage, maxPage );
	}
	protected String guiPageToolsCurrentPageMsg( int currentPage, int maxPage ) {
		
		String msg = Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__page_tools_current_page" )
				.localize();
		
		return formmatPageToolsPlaceholders( msg, currentPage, maxPage );
	}
	protected String guiPageToolsNextPageMsg( int currentPage, int maxPage ) {
		
		String msg = Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__page_tools_next_page" )
				.localize();
		
		return formmatPageToolsPlaceholders( msg, currentPage, maxPage );
	}
	protected String guiPageToolsLastPageMsg( int currentPage, int maxPage ) {
		
		String msg = Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__page_tools_last_page" )
				.localize();
		
		return formmatPageToolsPlaceholders( msg, currentPage, maxPage );
	}
	
	
	
	protected String guiPriceMsg( Double price ) {
		
		DecimalFormat dFmt = Prison.get().getDecimalFormat( "#,##0.00" );
		String value = price == null ? dFmt.format(0) : dFmt.format(price);
		
		return guiPriceMsg( value );
	}
	protected String guiPriceMsg( Integer price ) {
		
		DecimalFormat dFmt = Prison.get().getDecimalFormatInt();
		String value = price == null ? dFmt.format(0) : dFmt.format(price);
		
		return guiPriceMsg( value );
	}
	protected String guiPriceMsg( String price ) {
		
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__price" )
				.withReplacements( price )
				.localize();
	}

	
	protected String guiConfirmMsg( String prestigeName, double value ) {
		
		DecimalFormat dFmt = Prison.get().getDecimalFormat( "#,##0.0" );
		String valueStr = dFmt.format(value);
		
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__confirm" )
				.withReplacements( prestigeName, valueStr )
				.localize();
	}
	
	
	protected String guiDelayMsg( int value ) {
		DecimalFormat dFmt = Prison.get().getDecimalFormatInt();
		String valueStr = dFmt.format(value);
		
		return guiDelayMsg( valueStr );
	}
	protected String guiDelayMsg( String value ) {
		
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__delay" )
				.withReplacements( value )
				.localize();
	}
	
	protected String guiMultiplierMsg( double value ) {
		
		DecimalFormat dFmt = Prison.get().getDecimalFormat( "#,##0.0" );
		String valueStr = dFmt.format(value);
		
		return guiMultiplierMsg( valueStr );
	}
	protected String guiMultiplierMsg( String value ) {
		
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__multiplier" )
				.withReplacements( value )
				.localize();
	}
	
	protected String guiValueMsg( String value ) {
		
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__value" )
				.withReplacements( value )
				.localize();
	}
	
	protected String guiPermissionMsg( String prestigeName ) {
		
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__permission" )
				.withReplacements( prestigeName )
				.localize();
	}
	
	protected String guiPrestigeNameMsg( String prestigeName ) {
		
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_gui__prestige_name" )
				.withReplacements( prestigeName )
				.localize();
	}
	
	
}
