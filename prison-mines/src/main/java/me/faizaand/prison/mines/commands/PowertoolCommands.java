package me.faizaand.prison.mines.commands;

import me.faizaand.prison.commands.Command;
import me.faizaand.prison.internal.CommandSender;
import me.faizaand.prison.internal.GamePlayer;
import me.faizaand.prison.mines.PrisonMines;

public class PowertoolCommands {

    @Command(identifier = "autosmelt", description = "Enables/disables the autosmelt tool.", permissions = "mines.autosmelt")
    public void autosmeltCommand(CommandSender sender) {
        if (!PrisonMines.getInstance().getPlayerManager().hasAutosmelt((GamePlayer) sender)) {
            PrisonMines.getInstance().getPlayerManager().setAutosmelt((GamePlayer) sender, true);
            PrisonMines.getInstance().getMinesMessages().getLocalizable("autosmelt_enabled")
                .sendTo(sender);
        } else {
            PrisonMines.getInstance().getPlayerManager().setAutosmelt((GamePlayer) sender, false);
            PrisonMines.getInstance().getMinesMessages().getLocalizable("autosmelt_disabled")
                .sendTo(sender);
        }
    }

    @Command(identifier = "autoblock", description = "Enables/disables the autoblock tool.", permissions = "mines.autoblock")
    public void autoblockCommand(CommandSender sender) {
        if (!PrisonMines.getInstance().getPlayerManager().hasAutoblock((GamePlayer) sender)) {
            PrisonMines.getInstance().getPlayerManager().setAutoblock((GamePlayer) sender, true);
            PrisonMines.getInstance().getMinesMessages().getLocalizable("autoblock_enabled")
                .sendTo(sender);
        } else {
            PrisonMines.getInstance().getPlayerManager().setAutoblock((GamePlayer) sender, false);
            PrisonMines.getInstance().getMinesMessages().getLocalizable("autoblock_disabled")
                .sendTo(sender);
        }
    }

    @Command(identifier = "autopickup", description = "Enables/disables the autopickup tool.", permissions = "mines.autopickup")
    public void autopickupCommand(CommandSender sender) {
        if (!PrisonMines.getInstance().getPlayerManager().hasAutopickup((GamePlayer) sender)) {
            PrisonMines.getInstance().getPlayerManager().setAutopickup((GamePlayer) sender, true);
            PrisonMines.getInstance().getMinesMessages().getLocalizable("autopickup_enabled")
                .sendTo(sender);
        } else {
            PrisonMines.getInstance().getPlayerManager().setAutopickup((GamePlayer) sender, false);
            PrisonMines.getInstance().getMinesMessages().getLocalizable("autopickup_disabled")
                .sendTo(sender);
        }
    }
}
