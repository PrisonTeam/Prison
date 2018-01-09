package tech.mcprison.prison.mines.commands;

import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.mines.PrisonMines;

public class PowertoolCommands {
    @Command(identifier = "autosmelt", description = "Enables/disables the autosmelt tool.", permissions = "mines.autosmelt")
    public void autosmeltCommand(CommandSender sender) {
            if (!PrisonMines.getInstance().getPlayerManager().hasAutosmelt((Player)sender)) {
                PrisonMines.getInstance().getPlayerManager().setAutosmelt((Player)sender,true);
                PrisonMines.getInstance().getMinesMessages().getLocalizable("autosmelt_enabled")
                    .sendTo(sender);
            } else {
                PrisonMines.getInstance().getPlayerManager().setAutosmelt((Player)sender,false);
                PrisonMines.getInstance().getMinesMessages().getLocalizable("autosmelt_disabled")
                    .sendTo(sender);
            }
    }
    @Command(identifier = "autoblock", description = "Enables/disables the autoblock tool.", permissions = "mines.autoblock")
    public void autoblockCommand(CommandSender sender) {
            if (!PrisonMines.getInstance().getPlayerManager().hasAutosmelt((Player)sender)) {
                PrisonMines.getInstance().getPlayerManager().setAutoblock((Player)sender,true);
                PrisonMines.getInstance().getMinesMessages().getLocalizable("autoblock_enabled")
                    .sendTo(sender);
            } else {
                PrisonMines.getInstance().getPlayerManager().setAutoblock((Player)sender,false);
                PrisonMines.getInstance().getMinesMessages().getLocalizable("autoblock_disabled")
                    .sendTo(sender);
            }
    }
    @Command(identifier = "autopickup", description = "Enables/disables the autopickup tool.", permissions = "mines.autopickup")
    public void autopickupCommand(CommandSender sender) {
            if (!PrisonMines.getInstance().getPlayerManager().hasAutosmelt((Player)sender)) {
                PrisonMines.getInstance().getPlayerManager().setAutopickup((Player)sender,true);
                PrisonMines.getInstance().getMinesMessages().getLocalizable("autopickup_enabled")
                    .sendTo(sender);
            } else {
                PrisonMines.getInstance().getPlayerManager().setAutopickup((Player)sender,false);
                PrisonMines.getInstance().getMinesMessages().getLocalizable("autopickup_disabled")
                    .sendTo(sender);
            }
    }
}
