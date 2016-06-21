/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2016 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.prison.mines;

import io.github.prison.commands.Command;
import io.github.prison.internal.CommandSender;

/**
 * @author SirFaizdat
 */
public class MinesCommand {

    @Command(identifier = "mines", description = "Create and manage mines.", onlyPlayers = false)
    public void baseCommand(CommandSender sender) {
        sender.sendMessage("This command has not yet been implemented.");
    }

}
