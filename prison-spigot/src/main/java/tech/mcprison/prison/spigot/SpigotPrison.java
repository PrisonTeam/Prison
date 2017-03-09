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

import java.io.File;
import java.lang.reflect.Field;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.spigot.compat.Compatibility;
import tech.mcprison.prison.spigot.compat.Spigot18;
import tech.mcprison.prison.spigot.compat.Spigot19;
import tech.mcprison.prison.spigot.gui.GUIListener;

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

  @Override
  public void onLoad() {
    if (!getDataFolder().exists()) {
      getDataFolder().mkdir();
    }
  }

  @Override
  public void onEnable() {
    this.saveDefaultConfig();
    initDataDir();
    initCommandMap();
    initCompatibility();
    this.scheduler = new SpigotScheduler(this);
    GUIListener.get().init(this);
    Prison.get().init(new SpigotPlatform(this));
    new SpigotListener(this).init();
  }

  @Override
  public void onDisable() {
    this.scheduler.cancelAll();
    Prison.get().deinit();
  }

  private void initDataDir() {
    dataDirectory = new File(getDataFolder(), "data");
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
    int majorVersionint = Integer.parseInt(version[0]);
    int minorVersionInt = Integer.parseInt(version[1]);

    if (majorVersionint == 1) {
      if (minorVersionInt <= 8) {
        compatibility = new Spigot18();
      } else if (minorVersionInt >= 9) {
        compatibility = new Spigot19();
      } else {
        getLogger().severe(
            "Care to explain what version of Minecraft you're using? contact@mc-prison.tech plz tell us");
      }
    }

    getLogger().info("Using version adapter " + compatibility.getClass().getName());
  }

  public File getDataDirectory() {
    return dataDirectory;
  }
}
