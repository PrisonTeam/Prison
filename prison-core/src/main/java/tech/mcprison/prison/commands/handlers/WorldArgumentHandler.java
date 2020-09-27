/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017-2020 The Prison Team
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

package tech.mcprison.prison.commands.handlers;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.*;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.World;

public class WorldArgumentHandler extends ArgumentHandler<World> {

    public WorldArgumentHandler() {
        addVariable("sender", "The command executor", new ArgumentVariable<World>() {
            @Override
            public World var(CommandSender sender, CommandArgument argument, String varName)
                throws CommandError {
                if (!(sender instanceof Player)) {
                    throw new CommandError(
                        Prison.get().getLocaleManager().getLocalizable("cantAsConsole")
                            .localizeFor(sender));
                }

                return ((Player) sender).getLocation().getWorld();
            }
        });
    }

    @Override public World transform(CommandSender sender, CommandArgument argument, String value)
        throws TransformError {
        return Prison.get().getPlatform().getWorld(value).orElseThrow(() -> new TransformError(
            Prison.get().getLocaleManager().getLocalizable("worldNotFound").withReplacements(value)
                .localizeFor(sender)));
    }
}
