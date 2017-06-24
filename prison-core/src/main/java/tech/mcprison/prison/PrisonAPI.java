package tech.mcprison.prison;

import com.google.common.eventbus.EventBus;
import tech.mcprison.prison.commands.CommandHandler;
import tech.mcprison.prison.commands.PluginCommand;
import tech.mcprison.prison.economy.Economy;
import tech.mcprison.prison.gui.GUI;
import tech.mcprison.prison.internal.Permissions;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.Scheduler;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.internal.platform.Capability;
import tech.mcprison.prison.internal.platform.Platform;
import tech.mcprison.prison.internal.scoreboard.ScoreboardManager;
import tech.mcprison.prison.modules.ModuleManager;
import tech.mcprison.prison.store.Storage;
import tech.mcprison.prison.troubleshoot.TroubleshootManager;
import tech.mcprison.prison.util.ItemManager;
import tech.mcprison.prison.util.Location;

import java.io.File;
import java.util.*;

/**
 * A static way to access {@link Platform} and {@link Prison} methods.
 * Much easier and recommended over using the aforementioned classes.
 *
 * @author Faizaan A. Datoo
 */
public class PrisonAPI {

    public static ModuleManager getModuleManager() {
        return Prison.get().getModuleManager();
    }

    public static CommandHandler getCommandHandler() {
        return Prison.get().getCommandHandler();
    }

    public static EventBus getEventBus() {
        return Prison.get().getEventBus();
    }

    public static ItemManager getItemManager() {
        return Prison.get().getItemManager();
    }

    public static Optional<World> getWorld(String name) {
        return Prison.get().getPlatform().getWorld(name);
    }

    public static Optional<Player> getPlayer(String name) {
        return Prison.get().getPlatform().getPlayer(name);
    }

    public static Optional<Player> getPlayer(UUID uuid) {
        return Prison.get().getPlatform().getPlayer(uuid);
    }

    public static Collection<Player> getOnlinePlayers() {
        return Prison.get().getPlatform().getOnlinePlayers();
    }

    public static Economy getEconomy() {
        return Prison.get().getPlatform().getEconomy();
    }

    public static Permissions getPermission() {
        return Prison.get().getPlatform().getPermissions();
    }

    public static String getPluginVersion() {
        return Prison.get().getPlatform().getPluginVersion();
    }

    public static File getPluginDirectory() {
        return Prison.get().getPlatform().getPluginDirectory();
    }

    public static void registerCommand(PluginCommand command) {
        Prison.get().getPlatform().registerCommand(command);
    }

    public static void unregisterCommand(String command) {
        Prison.get().getPlatform().unregisterCommand(command);
    }

    public static List<PluginCommand> getCommands() {
        return Prison.get().getPlatform().getCommands();
    }

    public static void dispatchCommand(String cmd) {
        Prison.get().getPlatform().dispatchCommand(cmd);
    }

    public static Scheduler getScheduler() {
        return Prison.get().getPlatform().getScheduler();
    }

    public static GUI createGUI(String title, int numRows) {
        return Prison.get().getPlatform().createGUI(title, numRows);
    }

    @Deprecated public static void toggleDoor(Location doorLocation) {
        Prison.get().getPlatform().toggleDoor(doorLocation);
    }

    public static void log(String message, Object... format) {
        Prison.get().getPlatform().log(message, format);
    }

    public static void debug(String message, Object... format) {
        Prison.get().getPlatform().debug(message, format);
    }

    public static Map<Capability, Boolean> getCapabilities() {
        return Prison.get().getPlatform().getCapabilities();
    }

    public static void showTitle(Player player, String title, String subtitle, int fade) {
        Prison.get().getPlatform().showTitle(player, title, subtitle, fade);
    }

    public static void showActionBar(Player player, String text, int duration) {
        Prison.get().getPlatform().showActionBar(player, text, duration);
    }

    public static ScoreboardManager getScoreboardManager() {
        return Prison.get().getPlatform().getScoreboardManager();
    }

    public static TroubleshootManager getTroubleshootManager() {
        return Prison.get().getTroubleshootManager();
    }

    public static Storage getStorage() {
        return Prison.get().getPlatform().getStorage();
    }
}
