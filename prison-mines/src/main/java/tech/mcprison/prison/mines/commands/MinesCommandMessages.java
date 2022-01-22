package tech.mcprison.prison.mines.commands;

import tech.mcprison.prison.commands.BaseCommands;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.mines.PrisonMines;

public class MinesCommandMessages
	extends BaseCommands
{

	public MinesCommandMessages( String cmdGroup )
	{
		super( cmdGroup );
	}
	
	protected void exampleMsg( CommandSender sender, String mineName ) {
		PrisonMines.getInstance().getMinesMessages()
				.getLocalizable( "mines_mtp__" )
				.withReplacements( 
						mineName )
				.sendTo( sender );
	}
	
	protected void teleportUnableToTeleportMsg( CommandSender sender ) {
		// Sorry. You're unable to teleport there.
		PrisonMines.getInstance().getMinesMessages()
		.getLocalizable( "mines_mtp__unable_to_teleport" )
		.sendTo( sender );
	}
	
	protected void teleportCannotTeleportOtherPlayersMsg( CommandSender sender ) {
		// &3You cannot teleport other players to a mine. Ignoring parameter.
		PrisonMines.getInstance().getMinesMessages()
		.getLocalizable( "mines_mtp__unable_to_teleport_others" )
		.sendTo( sender );
	}

	protected void teleportNoTargetMineFoundMsg( CommandSender sender ) {
		// No target mine found. &3Resubmit teleport request with a mine name.
		PrisonMines.getInstance().getMinesMessages()
		.getLocalizable( "mines_mtp__no_target_mine_found" )
		.sendTo( sender );
	}
	
	protected void teleportPlayerMustBeIngameMsg( CommandSender sender ) {
		// &3The player must be in the game.
		PrisonMines.getInstance().getMinesMessages()
		.getLocalizable( "mines_mtp__player_must_be_in_game" )
		.sendTo( sender );
	}
	
	protected void teleportNamedPlayerMustBeIngameMsg( CommandSender sender ) {
		// &3Specified player is not in the game so they cannot be teleported.
		PrisonMines.getInstance().getMinesMessages()
		.getLocalizable( "mines_mtp__player_must_be_in_game" )
		.sendTo( sender );
	}
	
	protected void teleportCannotUseVirtualMinesMsg( CommandSender sender ) {
		// &cInvalid option. This mine is a virtual mine&7. Use &a/mines set area &7to enable the mine.
		PrisonMines.getInstance().getMinesMessages()
		.getLocalizable( "mines_mtp__cannot_use_virtual_mines" )
		.sendTo( sender );
	}
	
	protected void teleportFailedMsg( CommandSender sender ) {
		// &3Telport failed. Are you sure you're a Player?
		PrisonMines.getInstance().getMinesMessages()
		.getLocalizable( "mines_mtp__teleport_failed" )
		.sendTo( sender );
	}
	
}
