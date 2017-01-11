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

package tech.mcprison.prison.sponge;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.serializer.TextSerializers;
import tech.mcprison.prison.internal.CommandSender;

import java.util.Arrays;

/**
 * @author Faizaan A. Datoo
 */
public class SpongeCommandSender implements CommandSender {

    CommandSource commandSource;

    public SpongeCommandSender(CommandSource commandSource) {
        this.commandSource = commandSource;
    }

    @Override public String getName() {
        return commandSource.getName();
    }

    @Override public void dispatchCommand(String command) {
        Sponge.getGame().getCommandManager().process(commandSource, command);
    }

    @Override public boolean doesSupportColors() {
        return true;
    }

    @Override public boolean hasPermission(String perm) {
        return commandSource.hasPermission(perm);
    }

    @Override public void sendMessage(String message) {
        commandSource.sendMessage(TextSerializers.LEGACY_FORMATTING_CODE.deserialize(message));
    }

    @Override public void sendMessage(String[] messages) {
        Arrays.stream(messages).forEach(this::sendMessage);
    }

    @Override public void sendRaw(String json) {
        Sponge.getCommandManager()
            .process(Sponge.getServer().getConsole(), "tellraw " + getName() + " " + json);
    }
}
