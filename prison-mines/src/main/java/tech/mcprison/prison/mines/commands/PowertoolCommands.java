package tech.mcprison.prison.mines.commands;

import tech.mcprison.prison.commands.BaseCommands;

public class PowertoolCommands
	extends BaseCommands {
	
	public PowertoolCommands() {
		super("PowertoolCommands");
	}

//    @Command(identifier = "autosmelt", description = "Enables/disables the autosmelt tool.", permissions = "mines.autosmelt")
//    public void autosmeltCommand(CommandSender sender) {
//    	PrisonMines pMines = PrisonMines.getInstance();
//        if (!pMines.getPlayerManager().hasAutosmelt((Player) sender)) {
//        	pMines.getPlayerManager().setAutosmelt((Player) sender, true);
//        	pMines.getMinesMessages().getLocalizable("autosmelt_enabled")
//                .sendTo(sender);
//        } else {
//            pMines.getPlayerManager().setAutosmelt((Player) sender, false);
//            pMines.getMinesMessages().getLocalizable("autosmelt_disabled")
//                .sendTo(sender);
//        }
//    }
//
//    @Command(identifier = "autoblock", description = "Enables/disables the autoblock tool.", permissions = "mines.autoblock")
//    public void autoblockCommand(CommandSender sender) {
//    	PrisonMines pMines = PrisonMines.getInstance();
//        if (!pMines.getPlayerManager().hasAutoblock((Player) sender)) {
//            pMines.getPlayerManager().setAutoblock((Player) sender, true);
//            pMines.getMinesMessages().getLocalizable("autoblock_enabled")
//                .sendTo(sender);
//        } else {
//            pMines.getPlayerManager().setAutoblock((Player) sender, false);
//            pMines.getMinesMessages().getLocalizable("autoblock_disabled")
//                .sendTo(sender);
//        }
//    }
//
//    @Command(identifier = "autopickup", description = "Enables/disables the autopickup tool.", permissions = "mines.autopickup")
//    public void autopickupCommand(CommandSender sender) {
//    	PrisonMines pMines = PrisonMines.getInstance();
//        if (!pMines.getPlayerManager().hasAutopickup((Player) sender)) {
//            pMines.getPlayerManager().setAutopickup((Player) sender, true);
//            pMines.getMinesMessages().getLocalizable("autopickup_enabled")
//                .sendTo(sender);
//        } else {
//            pMines.getPlayerManager().setAutopickup((Player) sender, false);
//            pMines.getMinesMessages().getLocalizable("autopickup_disabled")
//                .sendTo(sender);
//        }
//    }
}
