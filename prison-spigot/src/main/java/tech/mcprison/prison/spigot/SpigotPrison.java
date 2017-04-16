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

package tech.mcprison.prison.spigot;

import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.alerts.Alerts;
import tech.mcprison.prison.spigot.compat.Compatibility;
import tech.mcprison.prison.spigot.compat.Spigot18;
import tech.mcprison.prison.spigot.compat.Spigot19;
import tech.mcprison.prison.spigot.gui.GUIListener;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * The plugin class for the Spigot implementation.
 *
 * @author Faizaan A. Datoo
 */
public class SpigotPrison extends JavaPlugin {

    Field commandMap;
    Field knownCommands;
    SpigotScheduler scheduler;
    Compatibility compatibility;
    File dataDirectory;
    Metrics metrics;
    boolean debug, doAlertAboutConvert = false;

    @Override public void onLoad() {
        // The meta file is used to see if the folder needs converting.
        // If the folder doesn't contain it, it's probably not a Prison 3 thing.
        File metaFile = new File(getDataFolder(), ".meta");
        if (getDataFolder().exists()) {
            if (!metaFile.exists()) {
                File old = getDataFolder();
                old.renameTo(new File(getDataFolder().getParent(), "Prison.old"));
                doAlertAboutConvert = true;
            }
        }
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
            try {
                metaFile.createNewFile();
            } catch (IOException e) {
                System.out.println(
                    "Could not create .meta file, this will cause problems with the converter!");
            }
        }
    }

    @Override public void onEnable() {
        this.saveDefaultConfig();
        debug = getConfig().getBoolean("debug");
        initDataDir();
        initCommandMap();
        initCompatibility();
        this.scheduler = new SpigotScheduler(this);
        GUIListener.get().init(this);
        Prison.get().init(new SpigotPlatform(this));
        new SpigotListener(this).init();

        if (doAlertAboutConvert) {
            Alerts.getInstance().sendAlert(
                "An old installation of Prison has been detected. In this public beta release of Prison 3, there is no converter system yet. However, your old data "
                    + "is safely stored in a folder called 'Prison.old' in your plugins directory. You will be notified when your data is ready to be converted.");
        }
    }

    @Override public void onDisable() {
        this.scheduler.cancelAll();
        Prison.get().deinit();
    }

    public void initMetrics() {
        this.metrics = new Metrics(this);
    }

    private void initDataDir() {
        dataDirectory = new File(getDataFolder(), "data_storage");
        if (!dataDirectory.exists()) {
            dataDirectory.mkdir();
        }
    }

    private void initCommandMap() {
        try {
            commandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMap.setAccessible(true);
            knownCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommands.setAccessible(true);
        } catch (NoSuchFieldException e) {
            getLogger().severe(
                "&c&lReflection error: &7Ensure that you're using the latest version of Spigot and Prison.");
            e.printStackTrace();
        }
    }

    private void initCompatibility() {
        String[] version = Bukkit.getVersion().split("\\.");
        int minorVersionInt = Integer.parseInt(version[1]);

        if (minorVersionInt <= 8) {
            compatibility = new Spigot18();
        } else if (minorVersionInt >= 9) {
            compatibility = new Spigot19();
        } else {
            getLogger().severe(
                "This shouldn't even be possible, so something is clearly very wrong with your version.");
        }

        getLogger().info("Using version adapter " + compatibility.getClass().getName());
    }

    public File getDataDirectory() {
        return dataDirectory;
    }
}
