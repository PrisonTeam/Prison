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

package tech.mcprison.prison;

import tech.mcprison.prison.commands.PluginCommand;
import tech.mcprison.prison.economy.Economy;
import tech.mcprison.prison.gui.GUI;
import tech.mcprison.prison.internal.platform.Capability;
import tech.mcprison.prison.internal.*;
import tech.mcprison.prison.internal.platform.Platform;
import tech.mcprison.prison.internal.scoreboard.ScoreboardManager;
import tech.mcprison.prison.util.ChatColor;
import tech.mcprison.prison.util.Location;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Faizaan A. Datoo
 */
public class TestPlatform implements Platform {

    private File pluginDirectory;
    private boolean suppressOutput;

    public TestPlatform(File pluginDirectory, boolean suppressOutput) {
        this.pluginDirectory = pluginDirectory;
        this.suppressOutput = suppressOutput;
    }

    @Override public World getWorld(String name) {
        return new TestWorld(name);
    }

    @Override public Player getPlayer(String name) {
        return null;
    }

    @Override public Player getPlayer(UUID uuid) {
        return null;
    }

    @Override public List<Player> getOnlinePlayers() {
        return null;
    }

    @Override public Sign getSign(Location location) {
        return null;
    }

    @Override public Economy getEconomy() {
        return null;
    }

    @Override public String getPluginVersion() {
        return "Tests";
    }

    @Override public File getPluginDirectory() {
        return pluginDirectory;
    }

    @Override public void registerCommand(PluginCommand command) {

    }

    @Override public void unregisterCommand(String command) {

    }

    @Override public List<PluginCommand> getCommands() {
        return Collections.emptyList();
    }

    @Override public Scheduler getScheduler() {
        return null;
    }

    @Override public GUI createGUI(String title, int numRows) {
        return null;
    }

    @Override public void toggleDoor(Location doorLocation) {

    }

    @Override public void log(String message, Object... format) {
        if(suppressOutput) return;
        System.out.println(ChatColor.stripColor(String.format(message, format)));
    }

    @Override public Map<Capability, Boolean> getCapabilities() {
        return null;
    }

    @Override public void showTitle(Player player, String title, String subtitle, int fade) {

    }

    @Override public void showActionBar(Player player, String text) {

    }

    @Override public ScoreboardManager getScoreboardManager() {
        return null;
    }

}
