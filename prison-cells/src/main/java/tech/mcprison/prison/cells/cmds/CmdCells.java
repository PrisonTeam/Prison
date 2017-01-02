package tech.mcprison.prison.cells.cmds;

import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.platform.CommandSender;

/**
 * Created by DMP9 on 30/12/2016.
 */
public class CmdCells {
    @Command(identifier = "cells list", description = "Lists all the created cells", onlyPlayers = false)
    public void list(CommandSender sender) {
        sender.sendMessage("&7========== &d/cells list &7==========");

    }
}
