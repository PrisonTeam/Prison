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

package tech.mcprison.prison.spigot.game;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.util.Text;

/**
 * @author Faizaan A. Datoo
 */
public class SpigotCommandSender implements CommandSender {

    private org.bukkit.command.CommandSender bukkitSender;

    public SpigotCommandSender(org.bukkit.command.CommandSender sender) {
        this.bukkitSender = sender;
    }

    @Override public String getName() {
        return bukkitSender.getName();
    }

    @Override public void dispatchCommand(String command) {
        Bukkit.getServer().dispatchCommand(bukkitSender, command);
    }

    @Override public boolean doesSupportColors() {
        return (this instanceof ConsoleCommandSender) && Bukkit.getConsoleSender() != null;
    }

    @Override public boolean hasPermission(String perm) {
        return bukkitSender.hasPermission(perm);
    }

    @Override public void sendMessage(String message) {
        bukkitSender.sendMessage(Text.translateAmpColorCodes(message));
    }

    @Override public void sendMessage(String[] messages) {
        for (String s : messages) {
            sendMessage(s);
        }
    }

    @Override public void sendRaw(String json) {
        if (bukkitSender instanceof org.bukkit.entity.Player) {
            json = Text.translateAmpColorCodes(json);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + bukkitSender.getName() + " " + json);
        }
    }

    public org.bukkit.command.CommandSender getWrapper() {
        return bukkitSender;
    }

}
