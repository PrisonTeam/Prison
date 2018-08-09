package xyz.faizaan.prison.mines.commands;

import xyz.faizaan.prison.commands.Command;
import xyz.faizaan.prison.internal.CommandSender;
import xyz.faizaan.prison.internal.Player;
import xyz.faizaan.prison.mines.PrisonMines;

public class PowertoolCommands {

    @Command(identifier = "autosmelt", description = "Enables/disables the autosmelt tool.", permissions = "mines.autosmelt")
    public void autosmeltCommand(CommandSender sender) {
        if (!PrisonMines.getInstance().getPlayerManager().hasAutosmelt((Player) sender)) {
            PrisonMines.getInstance().getPlayerManager().setAutosmelt((Player) sender, true);
            PrisonMines.getInstance().getMinesMessages().getLocalizable("autosmelt_enabled")
                .sendTo(sender);
        } else {
            PrisonMines.getInstance().getPlayerManager().setAutosmelt((Player) sender, false);
            PrisonMines.getInstance().getMinesMessages().getLocalizable("autosmelt_disabled")
                .sendTo(sender);
        }
    }

    @Command(identifier = "autoblock", description = "Enables/disables the autoblock tool.", permissions = "mines.autoblock")
    public void autoblockCommand(CommandSender sender) {
        if (!PrisonMines.getInstance().getPlayerManager().hasAutoblock((Player) sender)) {
            PrisonMines.getInstance().getPlayerManager().setAutoblock((Player) sender, true);
            PrisonMines.getInstance().getMinesMessages().getLocalizable("autoblock_enabled")
                .sendTo(sender);
        } else {
            PrisonMines.getInstance().getPlayerManager().setAutoblock((Player) sender, false);
            PrisonMines.getInstance().getMinesMessages().getLocalizable("autoblock_disabled")
                .sendTo(sender);
        }
    }

    @Command(identifier = "autopickup", description = "Enables/disables the autopickup tool.", permissions = "mines.autopickup")
    public void autopickupCommand(CommandSender sender) {
        if (!PrisonMines.getInstance().getPlayerManager().hasAutopickup((Player) sender)) {
            PrisonMines.getInstance().getPlayerManager().setAutopickup((Player) sender, true);
            PrisonMines.getInstance().getMinesMessages().getLocalizable("autopickup_enabled")
                .sendTo(sender);
        } else {
            PrisonMines.getInstance().getPlayerManager().setAutopickup((Player) sender, false);
            PrisonMines.getInstance().getMinesMessages().getLocalizable("autopickup_disabled")
                .sendTo(sender);
        }
    }
}
