/*
 * Prison is a Minecraft plugin for the prison game mode.
 * Copyright (C) 2018 The Prison Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.mines.commands;

import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.mines.PrisonMines;

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
