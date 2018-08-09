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

package xyz.faizaan.prison.sponge;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.title.Title;
import xyz.faizaan.prison.commands.PluginCommand;
import xyz.faizaan.prison.gui.GUI;
import xyz.faizaan.prison.internal.Player;
import xyz.faizaan.prison.internal.Scheduler;
import xyz.faizaan.prison.internal.World;
import xyz.faizaan.prison.internal.platform.Capability;
import xyz.faizaan.prison.internal.platform.Platform;
import xyz.faizaan.prison.internal.scoreboard.ScoreboardManager;
import xyz.faizaan.prison.sponge.game.SpongePlayer;
import xyz.faizaan.prison.sponge.game.SpongeWorld;
import xyz.faizaan.prison.store.Storage;
import xyz.faizaan.prison.util.Location;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Faizaan A. Datoo
 */
public class SpongePlatform implements Platform {

    private SpongePrison plugin;

    public SpongePlatform(SpongePrison plugin) {
        this.plugin = plugin;
    }

    @Override public Optional<World> getWorld(String name) {
        return Optional
            .ofNullable(Sponge.getServer().getWorld(name).map(SpongeWorld::new).orElse(null));
    }

    @Override public Optional<Player> getPlayer(String name) {
        return Optional.ofNullable(Sponge.getServer().getOnlinePlayers().stream()
            .filter(player -> player.getName().equals(name)).findFirst().map(SpongePlayer::new)
            .orElse(null));
    }

    @Override public Optional<Player> getPlayer(UUID uuid) {
        return Optional.ofNullable(Sponge.getServer().getOnlinePlayers().stream()
            .filter(player -> player.getUniqueId().equals(uuid)).findFirst().map(SpongePlayer::new)
            .orElse(null));
    }

    @Override public List<Player> getOnlinePlayers() {
        return Sponge.getServer().getOnlinePlayers().stream().map(SpongePlayer::new)
            .collect(Collectors.toList());
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

    @Override public void dispatchCommand(String cmd) {
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

    @Override public void debug(String message, Object... format) {

    }

    @Override public Map<Capability, Boolean> getCapabilities() {
        Map<Capability, Boolean> capabilities = new HashMap<>();
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

    @Override public void showActionBar(Player player, String text, int duration) {
        Text textObj = SpongeUtil.prisonTextToSponge(text);
        // FIXME Doesn't consider duration
        Sponge.getServer().getPlayer(player.getUUID()).orElseThrow(IllegalStateException::new)
            .sendMessage(ChatTypes.ACTION_BAR, textObj);
    }

    @Override public ScoreboardManager getScoreboardManager() {
        return null;
    }

    @Override public Storage getStorage() {
        return null;
    }

    @Override
    public boolean shouldShowAlerts() {
        return true;
    }
}
