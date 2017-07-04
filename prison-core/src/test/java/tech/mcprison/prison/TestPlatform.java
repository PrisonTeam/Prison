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
import tech.mcprison.prison.file.FileStorage;
import tech.mcprison.prison.gui.GUI;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.Scheduler;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.internal.platform.Capability;
import tech.mcprison.prison.internal.platform.Platform;
import tech.mcprison.prison.internal.scoreboard.ScoreboardManager;
import tech.mcprison.prison.store.Storage;
import tech.mcprison.prison.util.ChatColor;
import tech.mcprison.prison.util.Location;

import java.io.File;
import java.util.*;

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

    @Override public Optional<World> getWorld(String name) {
        return Optional.of(new TestWorld(name));
    }

    @Override public Optional<Player> getPlayer(String name) {
        return null;
    }

    @Override public Optional<Player> getPlayer(UUID uuid) {
        return null;
    }

    @Override public List<Player> getOnlinePlayers() {
        return new ArrayList<>();
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

    @Override public void dispatchCommand(String cmd) {
    }

    @Override public Scheduler getScheduler() {
        return new TestScheduler();
    }

    @Override public GUI createGUI(String title, int numRows) {
        return null;
    }

    @Override public void toggleDoor(Location doorLocation) {

    }

    @Override public void log(String message, Object... format) {
        if (suppressOutput) {
            return;
        }
        System.out.println(ChatColor.stripColor(String.format(message, format)));
    }

    @Override public void debug(String message, Object... format) {
        log(message, format);
    }

    @Override public Map<Capability, Boolean> getCapabilities() {
        return null;
    }

    @Override public void showTitle(Player player, String title, String subtitle, int fade) {

    }

    @Override public void showActionBar(Player player, String text, int duration) {

    }

    @Override public ScoreboardManager getScoreboardManager() {
        return null;
    }

    @Override public Storage getStorage() {
        return new FileStorage(getPluginDirectory());
    }

}
