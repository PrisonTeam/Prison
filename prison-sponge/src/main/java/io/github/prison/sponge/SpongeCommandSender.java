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

package io.github.prison.sponge;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.serializer.TextSerializers;

import io.github.prison.internal.CommandSender;
import io.github.prison.internal.Player;

/**
 * @author SirFaizdat
 * @author Camouflage100
 */
public class SpongeCommandSender implements CommandSender {

    private CommandSource commandSource;

    public SpongeCommandSender(CommandSource commandSource) {
        this.commandSource = commandSource;
    }

    @Override
    public String getName() {
        return commandSource.getName();
    }

    @Override
    public boolean doesSupportColors() {
        return true;
    }

    @Override
    public boolean hasPermission(String perm) {
        return commandSource.hasPermission(perm);
    }

    @Override
    public void sendMessage(String message) {
        commandSource.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
    }

    @Override
    public void sendError(String error) {
        commandSource.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&cError &8&l| &7" + error + "&7!"));
    }

    @Override
    public void sendMessage(String[] messages) {
        for(String message : messages) sendMessage(message);
    }

    @Override
    public void sendRaw(String json) {
        if (commandSource instanceof Player) {
            ((Player) commandSource).sendRaw(json);
        }
    }

}
