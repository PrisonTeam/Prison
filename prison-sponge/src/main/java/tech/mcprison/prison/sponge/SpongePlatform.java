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
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.title.Title;
import tech.mcprison.prison.commands.PluginCommand;
import tech.mcprison.prison.economy.Economy;
import tech.mcprison.prison.gui.GUI;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.Scheduler;
import tech.mcprison.prison.internal.Sign;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.internal.platform.Capability;
import tech.mcprison.prison.internal.platform.Platform;
import tech.mcprison.prison.internal.scoreboard.ScoreboardManager;
import tech.mcprison.prison.util.Location;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Faizaan A. Datoo
 */
public class SpongePlatform implements Platform {

    private SpongePrison plugin;

    public SpongePlatform(SpongePrison plugin) {
        this.plugin = plugin;
    }

    @Override public World getWorld(String name) {
        return null;
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
        return Sponge.getPluginManager().getPlugin("prison-sponge")
            .orElseThrow(IllegalStateException::new).getVersion().orElse("null");
    }

    @Override public File getPluginDirectory() {
        return plugin.getConfigDir().toFile();
    }

    @Override public void registerCommand(PluginCommand command) {

    }

    @Override public void unregisterCommand(String command) {

    }

    @Override public List<PluginCommand> getCommands() {
        return null;
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
        Text text = SpongeUtil.prisonTextToSponge(String.format(message, format));
        Sponge.getServer().getConsole().sendMessage(text);
    }

    @Override public Map<Capability, Boolean> getCapabilities() {
        Map<Capability, Boolean> capabilities = new HashMap<>();
        capabilities.put(Capability.ECONOMY, true);
        capabilities.put(Capability.GUI, false); // For now
        return capabilities;
    }

    @Override public void showTitle(Player player, String title, String subtitle, int fade) {
        Text titleText = SpongeUtil.prisonTextToSponge(title);
        Text subtitleText = SpongeUtil.prisonTextToSponge(subtitle);
        Title titleObj =
            Title.builder().title(titleText).subtitle(subtitleText).fadeIn(fade).fadeOut(fade)
                .build();

        Sponge.getServer().getPlayer(player.getUUID()).orElseThrow(IllegalStateException::new)
            .sendTitle(titleObj);
    }

    @Override public void showActionBar(Player player, String text) {
        Text textObj = SpongeUtil.prisonTextToSponge(text);

        Sponge.getServer().getPlayer(player.getUUID()).orElseThrow(IllegalStateException::new)
            .sendMessage(ChatTypes.ACTION_BAR, textObj);
    }

    @Override public ScoreboardManager getScoreboardManager() {
        return null;
    }

}
