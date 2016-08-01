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

package tech.mcprison.prison.spigot;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.spigot.compat.Compatibility;
import tech.mcprison.prison.spigot.compat.Spigot18;
import tech.mcprison.prison.spigot.compat.Spigot19;

import java.lang.reflect.Field;

/**
 * The plugin class for the Spigot implementation.
 *
 * @author SirFaizdat
 */
public class SpigotPrison extends JavaPlugin {

    CommandMap commandMap;
    SpigotScheduler scheduler;
    Compatibility compatibility;

    @Override public void onLoad() {
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
    }

    @Override public void onEnable() {
        initCommandMap();
        initCompatibility();
        this.scheduler = new SpigotScheduler(this);
        GUIListener.getInstance().init(this);
        Prison.getInstance().init(new SpigotPlatform(this));
        new SpigotListener(this).init();
    }

    @Override public void onDisable() {
        this.scheduler.cancelAll();
        Prison.getInstance().deinit();
    }

    private void initCommandMap() {
        final Field bukkitCommandMap;
        try {
            bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Prison.getInstance().getPlatform().log(
                "&c&lReflection error: &7Ensure that you're using the latest version of Spigot and Prison.");
            e.printStackTrace();
        }
    }

    private void initCompatibility() {
        String minorVersion = Bukkit.getVersion().split("\\.")[1];
        int minorVersionInt = Integer.parseInt(minorVersion);
        if (minorVersionInt <= 8)
            compatibility = new Spigot18();
        else if (minorVersionInt >= 9)
            compatibility = new Spigot19();
        getLogger().info("Using version adapter " + compatibility.getClass().getName());
    }

}
