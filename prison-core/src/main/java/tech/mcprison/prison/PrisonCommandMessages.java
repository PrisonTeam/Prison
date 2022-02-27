package tech.mcprison.prison;

import java.text.DecimalFormat;

import tech.mcprison.prison.internal.CommandSender;

public class PrisonCommandMessages
{
	protected void coreDebugTestLocaleseMsg( CommandSender sender ) {
		Prison.get().getLocaleManager()
				.getLocalizable( "core_prison_utf8_test" )
				.setFailSilently()
				.sendTo( sender );
	}
	
/*
 * 

core_tokens__name_required=Prison Tokens=A player's name is required when used from console.
core_tokens__cannot_view_others_balances=Prison Tokens: You do not have permission to view other player's balances.
core_tokens__view_balance=&3%1 has %2 tokens.
core_tokens__add_invalid_amount=Prison Tokens: Invalid amount: '%1'. Must be greater than zero.
core_tokens__added_amount=&3%1 now has &7%2 &3tokens after adding &7%3&3.
core_tokens__removed_amount=&3%1 now has &7%2 &3tokens after removing &7%3&3.
core_tokens__set_amount=&3%1 now has &7%2 &3tokens.

 * 
 */
	
	protected void coreTokensNameRequiredMsg( CommandSender sender ) {
		Prison.get().getLocaleManager()
					.getLocalizable( "core_tokens__name_required" )
					.setFailSilently()
					.sendTo( sender );
	}
	
	protected void coreTokensBalanceCannotViewOthersMsg( CommandSender sender ) {
		Prison.get().getLocaleManager()
					.getLocalizable( "core_tokens__cannot_view_others_balances" )
					.setFailSilently()
					.sendTo( sender );
	}
	
	protected void coreTokensBalanceViewMsg( CommandSender sender, String name, long tokens ) {
		
		DecimalFormat dFmt = new DecimalFormat("#,##0");
    	String tokensMsg = dFmt.format( tokens );
    	
		Prison.get().getLocaleManager()
					.getLocalizable( "core_tokens__view_balance" )
					.setFailSilently()
					.withReplacements( name, tokensMsg )
					.sendTo( sender );
	}
	
	protected void coreTokensAddInvalidAmountMsg( CommandSender sender, long tokens ) {
		
		DecimalFormat dFmt = new DecimalFormat("#,##0");
		String tokensMsg = dFmt.format( tokens );
		
		Prison.get().getLocaleManager()
					.getLocalizable( "core_tokens__add_invalid_amount" )
					.setFailSilently()
					.withReplacements( tokensMsg )
					.sendTo( sender );
	}
	
	protected String coreTokensAddedAmountMsg(  
			String name, long tokens, long amount ) {
		
		DecimalFormat dFmt = new DecimalFormat("#,##0");
		String tokensMsg = dFmt.format( tokens );
		String amountMsg = dFmt.format( amount );
		
		return Prison.get().getLocaleManager()
					.getLocalizable( "core_tokens__added_amount" )
					.setFailSilently()
					.withReplacements( name, tokensMsg, amountMsg )
					.localize();
	}
	
	protected String coreTokensRemovedAmountMsg(  
			String name, long tokens, long amount ) {
		
		DecimalFormat dFmt = new DecimalFormat("#,##0");
		String tokensMsg = dFmt.format( tokens );
		String amountMsg = dFmt.format( amount );
		
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_tokens__removed_amount" )
				.setFailSilently()
				.withReplacements( name, tokensMsg, amountMsg )
				.localize();
	}
	
	protected String coreTokensSetAmountMsg(  
			String name, long tokens ) {
		
		DecimalFormat dFmt = new DecimalFormat("#,##0");
		String tokensMsg = dFmt.format( tokens );
		
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_tokens__set_amount" )
				.setFailSilently()
				.withReplacements( name, tokensMsg )
				.localize();
	}
	
	
	protected void coreRunCommandNameRequiredMsg( CommandSender sender ) {
		
		Prison.get().getLocaleManager()
					.getLocalizable( "core_runCmd__name_required" )
					.setFailSilently()
					.sendTo( sender );
	}
	
	protected void coreRunCommandCommandRequiredMsg( CommandSender sender ) {
		
		Prison.get().getLocaleManager()
		.getLocalizable( "core_runCmd__command_required" )
		.setFailSilently()
		.sendTo( sender );
	}
	
	
}
