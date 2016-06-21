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

package io.github.prison.spigot;

import io.github.prison.internal.CommandSender;
import io.github.prison.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

/**
 * @author SirFaizdat
 */
public class SpigotCommandSender implements CommandSender {

    private org.bukkit.command.CommandSender bukkitSender;

    public SpigotCommandSender(org.bukkit.command.CommandSender sender) {
        this.bukkitSender = sender;
    }

    @Override
    public String getName() {
        return bukkitSender.getName();
    }

    @Override
    public boolean doesSupportColors() {
        return (this instanceof ConsoleCommandSender) && Bukkit.getConsoleSender() != null;
    }

    @Override
    public boolean hasPermission(String perm) {
        return bukkitSender.hasPermission(perm);
    }

    @Override
    public void sendMessage(String message) {
        bukkitSender.sendMessage(TextUtil.parse(message));
    }

    @Override
    public void sendMessage(String[] messages) {
        for (String s : messages) sendMessage(s);
    }

    @Override
    public void sendRaw(String json) {
        // I know, I know, this is bad practice - but it beats using *shudders* NMS code.
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + getName() + " " + json);
    }

}
