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

package me.faizaand.prison.spigot;

import me.faizaand.prison.Prison;
import me.faizaand.prison.convert.ConversionManager;
import me.faizaand.prison.convert.ConversionResult;
import me.faizaand.prison.events.EventManager;
import me.faizaand.prison.events.EventType;
import me.faizaand.prison.events.StandardEventManager;
import me.faizaand.prison.internal.Scheduler;
import me.faizaand.prison.internal.platform.Capability;
import me.faizaand.prison.internal.platform.CommandManager;
import me.faizaand.prison.internal.platform.Platform;
import me.faizaand.prison.internal.platform.PlayerManager;
import me.faizaand.prison.internal.scoreboard.ScoreboardManager;
import me.faizaand.prison.output.BulletedListComponent;
import me.faizaand.prison.output.LogLevel;
import me.faizaand.prison.output.Output;
import me.faizaand.prison.spigot.game.SpigotCommandManager;
import me.faizaand.prison.spigot.game.SpigotGuiManager;
import me.faizaand.prison.spigot.game.SpigotPlayerManager;
import me.faizaand.prison.spigot.game.SpigotWorldManager;
import me.faizaand.prison.spigot.game.scoreboard.SpigotScoreboardManager;
import me.faizaand.prison.spigot.handlers.BlockBreakEventHandler;
import me.faizaand.prison.spigot.handlers.PlayerChatEventHandler;
import me.faizaand.prison.spigot.handlers.PlayerInteractEventHandler;
import me.faizaand.prison.spigot.handlers.PlayerJoinEventHandler;
import me.faizaand.prison.spigot.store.file.FileStorage;
import me.faizaand.prison.spigot.util.ActionBarUtil;
import me.faizaand.prison.store.Storage;
import me.faizaand.prison.util.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Faizaan A. Datoo
 */
class SpigotPlatform implements Platform {

    private SpigotPrison plugin;

    private ScoreboardManager scoreboardManager;
    private SpigotPlayerManager playerManager;
    private SpigotWorldManager worldManager;
    private SpigotCommandManager commandManager;
    private SpigotGuiManager guiManager;
    private EventManager eventManager;

    private Storage storage;

    SpigotPlatform(SpigotPrison plugin) {
        this.plugin = plugin;

        this.scoreboardManager = new SpigotScoreboardManager();
        this.playerManager = new SpigotPlayerManager();
        this.worldManager = new SpigotWorldManager();
        this.commandManager = new SpigotCommandManager();
        this.guiManager = new SpigotGuiManager();
        this.eventManager = new StandardEventManager(this::initEventHandlers);
        this.initEventHandlers();

        this.storage = initStorage();
        ActionBarUtil.init(plugin);
    }

    private Storage initStorage() {
        String confStorage = plugin.getConfig().getString("storage", "file");
        if (confStorage.equalsIgnoreCase("file")) {
            return new FileStorage(plugin.getDataDirectory());
        } else {
            Output.get().logError("Unknown file storage type in configuration \"" + confStorage
                    + "\". Using file storage.");
            Output.get().logWarn(
                    "Note: In this version of Prison 3, 'file' is the only supported type of storage. We're working to bring other storage types soon.");
            return new FileStorage(plugin.getDataDirectory());
        }
    }

    @Override
    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public SpigotWorldManager getWorldManager() {
        return worldManager;
    }

    @Override
    public CommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public SpigotGuiManager getGuiManager() {
        return guiManager;
    }

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    private void initEventHandlers() {
        this.eventManager.registerHandler(EventType.PlayerJoinEvent, new PlayerJoinEventHandler());
        this.eventManager.registerHandler(EventType.BlockBreakEvent, new BlockBreakEventHandler());
        this.eventManager.registerHandler(EventType.PlayerChatEvent, new PlayerChatEventHandler());
        this.eventManager.registerHandler(EventType.PlayerInteractBlockEvent, new PlayerInteractEventHandler());
    }

    @Override
    public String getPluginVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public File getPluginDirectory() {
        return plugin.getDataFolder();
    }

    @Override
    public void dispatchCommand(String cmd) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
    }

    @Override
    public Scheduler getScheduler() {
        return plugin.scheduler;
    }

    @Override
    public void log(String message, Object... format) {
        message = Text.translateAmpColorCodes(String.format(message, format));

        ConsoleCommandSender sender = Bukkit.getConsoleSender();
        if (sender == null) {
            Bukkit.getLogger().info(ChatColor.stripColor(message));
        } else {
            sender.sendMessage(message);
        }
    }

    @Override
    public void debug(String message, Object... format) {
        if (!plugin.debug) {
            return;
        }

        log(Output.get().gen("&eDebug") + " &7", message, format);
    }

    @Override
    public String runConverter() {
        File file = new File(plugin.getDataFolder().getParent(), "Prison.old");
        if (!file.exists()) {
            return Output.get().format(
                    "I could not find a 'Prison.old' folder to convert. You probably haven't had Prison 2 installed before, so you don't need to convert :)",
                    LogLevel.WARNING);
        }

        List<ConversionResult> results = ConversionManager.getInstance().runConversion();

        if (results.size() == 0) {
            return Text
                    .translateAmpColorCodes("&7There are no conversions to be run at this time.");
        }

        BulletedListComponent.BulletedListBuilder builder =
                new BulletedListComponent.BulletedListBuilder();
        for (ConversionResult result : results) {
            String status =
                    result.getStatus() == ConversionResult.Status.Success ? "&aSuccess" : "&cFailure";
            builder.add(
                    result.getAgentName() + " &8- " + status + " &7(" + result.getReason() + "&7)");
        }

        return builder.build().text();
    }

    @Override
    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    @Override
    public Storage getStorage() {
        return storage;
    }

    @Override
    public boolean shouldShowAlerts() {
        return plugin.getConfig().getBoolean("show-alerts", true);
    }

    @Override
    public Map<Capability, Boolean> getCapabilities() {
        Map<Capability, Boolean> capabilities = new HashMap<>();
        capabilities.put(Capability.ACTION_BARS, true);
        capabilities.put(Capability.GUI, true);
        return capabilities;
    }

}
