package tech.mcprison.prison.ranks.commands;

import tech.mcprison.prison.commands.BaseCommands;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.ranks.PrisonRanks;

public class CommandCommandsMessages
				extends BaseCommands {

	public CommandCommandsMessages( String cmdGroup ) {
		super( cmdGroup );
	}

	protected void ranksCommandAddCannotUsePercentSymbols( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_commandCommands__command_add_cannot_use_percent_symbols" )
				.sendTo( sender );
	}
	
	protected String ranksCommandAddPlaceholdersMsg( String placeholders ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_commandCommands__command_add_placeholders" )
				.withReplacements( 
						placeholders )
				.localize();
	}
	
	protected void rankDoesNotExistMsg( CommandSender sender, String rankName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_commandCommands__rank_does_not_exist" )
				.withReplacements( 
						rankName )
				.sendTo( sender );
	}
	
	
	protected void ranksCommandAddDuplicateMsg( CommandSender sender, String newCommand, String rankName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_commandCommands__command_add_duplicate" )
				.withReplacements(
						newCommand,
						rankName )
				.sendTo( sender );
	}
	
	protected void ranksCommandAddSuccessMsg( CommandSender sender, 
					String command, String rankName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_commandCommands__command_add_success" )
				.withReplacements( 
						command,
						rankName )
				.sendTo( sender );
	}
	
	protected void ranksCommandRemoveSuccessMsg( CommandSender sender, 
			String command, String rankName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_commandCommands__command_remove_sucess" )
				.withReplacements( 
						command,
						rankName )
				.sendTo( sender );
	}
	
	protected void ranksCommandRemoveFailedMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_commandCommands__command_remove_failed" )
				.sendTo( sender );
	}
	

	protected void ranksCommandListContainsNoneMsg( CommandSender sender, 
			String rankName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_commandCommands__command_list_contains_none" )
				.withReplacements( 
						rankName )
				.sendTo( sender );
	}
	
	protected String ranksCommandListCmdHeaderMsg( String rankTag ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_commandCommands__command_list_cmd_header" )
				.withReplacements( 
						rankTag )
				.localize();
	}
	
	protected String ranksCommandListClickCmdToRemoveMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_commandCommands__command_list_click_cmd_to_remove" )
				.localize();
	}
	
	protected String ranksCommandListClickToRemoveMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_commandCommands__command_list_click_to_remove" )
				.localize();
	}
	
	protected String ranksCommandListAddButtonMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_commandCommands__command_list_add_button" )
				.localize();
	}
	
	protected String ranksCommandListAddNewCommandToolTipMsg() {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_commandCommands__command_list_add_new_command_tool_tip" )
				.localize();
	}
	
    protected void rankRowNumberMustBeGreaterThanZero( CommandSender sender, Integer row ) {
		PrisonRanks.getInstance().getRanksMessages()
		.getLocalizable( "ranks_commandCommands__command_row_number_must_be_greater_than_zero" )
		.withReplacements( 
				Integer.toString( row ) )
		.sendTo( sender );
	}
    
    protected void rankRowNumberTooHigh( CommandSender sender, Integer maxValue, Integer row ) {
    	PrisonRanks.getInstance().getRanksMessages()
    	.getLocalizable( "ranks_commandCommands__command_row_number_too_high" )
    	.withReplacements( 
    			Integer.toString( maxValue ),
    			Integer.toString( row ) )
    	.sendTo( sender );
    }
	
	
	
	
	protected String ladderCommandAddPlaceholdersMsg( String placeholders ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_commandCommands__ladder_command_add_placeholders" )
				.withReplacements( 
						placeholders )
				.localize();
	}
	
	protected void ladderDoesNotExistMsg( CommandSender sender, String rankName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_commandCommands__ladder_does_not_exist" )
				.withReplacements( 
						rankName )
				.sendTo( sender );
	}
	
	
	protected void ladderCommandAddDuplicateMsg( CommandSender sender, String newCommand, String rankName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_commandCommands__ladder_command_add_duplicate" )
				.withReplacements(
						newCommand,
						rankName )
				.sendTo( sender );
	}
	
	protected void ladderCommandAddSuccessMsg( CommandSender sender, 
					String command, String rankName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_commandCommands__ladder_command_add_success" )
				.withReplacements( 
						command,
						rankName )
				.sendTo( sender );
	}
	
	protected void ladderCommandRemoveSuccessMsg( CommandSender sender, 
			String command, String rankName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_commandCommands__ladder_command_remove_sucess" )
				.withReplacements( 
						command,
						rankName )
				.sendTo( sender );
	}
	
	protected void ladderCommandRemoveFailedMsg( CommandSender sender ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_commandCommands__ladder_command_remove_failed" )
				.sendTo( sender );
	}
	

	protected void ladderCommandListContainsNoneMsg( CommandSender sender, 
			String rankName ) {
		PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_commandCommands__ladder_command_list_contains_none" )
				.withReplacements( 
						rankName )
				.sendTo( sender );
	}

	
	protected String ladderCommandListCmdHeaderMsg( String rankTag ) {
		return PrisonRanks.getInstance().getRanksMessages()
				.getLocalizable( "ranks_commandCommands__ladder_command_list_cmd_header" )
				.withReplacements( 
						rankTag )
				.localize();
	}
	
}
