/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017 The Prison Team
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

package me.faizaand.prison.commands.handlers;

import me.faizaand.prison.Prison;
import me.faizaand.prison.commands.ArgumentHandler;
import me.faizaand.prison.commands.CommandArgument;
import me.faizaand.prison.commands.CommandError;
import me.faizaand.prison.commands.TransformError;
import me.faizaand.prison.internal.CommandSender;
import me.faizaand.prison.internal.GamePlayer;

public class PlayerArgumentHandler extends ArgumentHandler<GamePlayer> {

    public PlayerArgumentHandler() {
        addVariable("sender", "The command executor", (sender, argument, varName) -> {
            if (!(sender instanceof GamePlayer)) {
                throw new CommandError(
                    Prison.get().getLocaleManager().getLocalizable("cantAsConsole")
                        .localizeFor(sender));
            }

            return ((GamePlayer) sender);
        });
    }

    @Override public GamePlayer transform(CommandSender sender, CommandArgument argument, String value)
        throws TransformError {
        return Prison.get().getPlatform().getPlayerManager().getPlayer(value).orElseThrow(() -> new TransformError(
            Prison.get().getLocaleManager().getLocalizable("playerNotOnline")
                .withReplacements(value).localizeFor(sender)));
    }
}
