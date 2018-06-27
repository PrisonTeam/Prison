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
import me.faizaand.prison.alerts.Alerts;
import me.faizaand.prison.cells.PrisonCells;
import me.faizaand.prison.integration.Integration;
import me.faizaand.prison.mines.PrisonMines;
import me.faizaand.prison.modules.Module;
import me.faizaand.prison.output.Output;
import me.faizaand.prison.ranks.PrisonRanks;
import me.faizaand.prison.shops.PrisonShops;
import me.faizaand.prison.spigot.gui.GUIListener;
import me.faizaand.prison.spigot.integrations.economies.EssentialsEconomy;
import me.faizaand.prison.spigot.integrations.economies.SaneEconomy;
import me.faizaand.prison.spigot.integrations.economies.VaultEconomy;
import me.faizaand.prison.spigot.integrations.permissions.LuckPermissions;
import me.faizaand.prison.spigot.integrations.permissions.VaultPermissions;
import me.faizaand.prison.spigot.integrations.placeholder.MVdWPlaceholderIntegration;
import me.lucko.helper.Events;
import me.lucko.helper.plugin.ExtendedJavaPlugin;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.player.PlayerJoinEvent;
import org.inventivetalent.update.spiget.SpigetUpdate;
import org.inventivetalent.update.spiget.UpdateCallback;
import org.inventivetalent.update.spiget.comparator.VersionComparator;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * The plugin class for the Spigot implementation.
 *
 * @author Faizaan A. Datoo
 */
public class SpigotPrison extends ExtendedJavaPlugin {

    SpigotScheduler scheduler;
    boolean debug = false;

    private File dataDirectory;
    private boolean doAlertAboutConvert = false;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void load() {
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

    @Override
    public void enable() {
        this.saveDefaultConfig();
        debug = getConfig().getBoolean("debug", false);

        initDataDir();
        initMetrics();
        initUpdater();
        this.scheduler = new SpigotScheduler(this);
        GUIListener.get().init(this);

        Prison.get().init(new SpigotPlatform(this));

        Prison.get().getLocaleManager().setDefaultLocale(getConfig().getString("default-language", "en_US"));
        initIntegrations();
        initModules();

        // Makes sure all new players get their notification if they have an alert
        Events.subscribe(PlayerJoinEvent.class).handler(e -> Alerts.getInstance()
                .showAlerts(Prison.get().getPlatform().getPlayerManager().getPlayer(e.getPlayer().getUniqueId()).orElseThrow(IllegalStateException::new)));

        if (doAlertAboutConvert) {
            Alerts.getInstance().sendAlert(
                    "&7An old installation of Prison has been detected. &3Type /prison convert to convert your old data automatically. &7If you already converted, delete the 'Prison.old' folder so that we stop nagging you.");
        }

        Prison.get().postLoad();
    }

    @Override
    public void disable() {
        this.scheduler.cancelAll();
        Prison.get().deinit();
    }

    private void initMetrics() {
        if (!getConfig().getBoolean("send-metrics", true)) {
            return; // Don't check if they don't want it
        }
        Metrics metrics = new Metrics(this);

        // Report the modules being used
        metrics.addCustomChart(new Metrics.SimpleBarChart("modules_used", () -> {
            Map<String, Integer> valueMap = new HashMap<>();
            for (Module m : Prison.get().getModuleManager().getModules()) {
                valueMap.put(m.getName(), 1);
            }
            return valueMap;
        }));

        // Report the API level
        metrics.addCustomChart(
                new Metrics.SimplePie("api_level", () -> "API Level " + Prison.API_LEVEL));
    }

    private void initUpdater() {
        if (!getConfig().getBoolean("check-updates")) {
            return; // Don't check if they don't want it
        }

        SpigetUpdate updater = new SpigetUpdate(this, 1223);
        updater.setVersionComparator(VersionComparator.EQUAL);

        updater.checkForUpdate(new UpdateCallback() {
            @Override
            public void updateAvailable(String newVersion, String downloadUrl,
                                        boolean hasDirectDownload) {
                Alerts.getInstance().sendAlert(
                        "&3%s is now available. &7Go to the &lBukkit&r&7 or &lSpigot&r&7 page to download the latest release with new features and fixes :)",
                        newVersion);
            }

            @Override
            public void upToDate() {
                // Plugin is up-to-date
            }
        });

    }

    private void initDataDir() {
        dataDirectory = new File(getDataFolder(), "data_storage");
        if (!dataDirectory.exists()) {
            dataDirectory.mkdir();
        }
    }

    private void initIntegrations() {
        registerIntegration("Essentials", EssentialsEconomy.class);
        registerIntegration("SaneEconomy", SaneEconomy.class);
        registerIntegration("Vault", VaultEconomy.class);

        registerIntegration("LuckPerms", LuckPermissions.class);
        registerIntegration("Vault", VaultPermissions.class);

        registerIntegration("MVdWPlaceholderAPI", MVdWPlaceholderIntegration.class);
    }

    private void registerIntegration(String pluginName, Class<? extends Integration> integration) {
        if (Bukkit.getPluginManager().isPluginEnabled(pluginName)) {
            try {
                Prison.get().getIntegrationManager().register(integration.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                getLogger()
                        .log(Level.WARNING, "Could not initialize integration " + integration.getName(),
                                e);
            }
        }
    }

    private void initModules() {
        YamlConfiguration modulesConf = loadConfig("modules.yml");

        if (modulesConf.getBoolean("mines")) {
            Prison.get().getModuleManager()
                    .registerModule(new PrisonMines(getDescription().getVersion()));
        } else {
            Output.get().logInfo("Not loading mines because it's disabled in modules.yml.");
        }

        if (modulesConf.getBoolean("ranks")) {
            Prison.get().getModuleManager()
                    .registerModule(new PrisonRanks(getDescription().getVersion()));
        } else {
            Output.get().logInfo("Not loading ranks because it's disabled in modules.yml");
        }

        if (modulesConf.getBoolean("shops")) {
            Prison.get().getModuleManager()
                    .registerModule(new PrisonShops(getDescription().getVersion()));
        } else {
            Output.get().logInfo("Not loading shops because it's disabled in modules.yml");
        }

        if (modulesConf.getBoolean("cells")) {
            Prison.get().getModuleManager()
                    .registerModule(new PrisonCells(getDescription().getVersion()));
        } else {
            Output.get().logInfo("Not loading cells because it's disabled in modules.yml");
        }

    }

    @Nonnull
    public File getBundledFile(String name) {
        getDataFolder().mkdirs();
        File file = new File(getDataFolder(), name);
        if (!file.exists()) {
            saveResource(name, false);
        }
        return file;
    }

    @Nonnull
    public YamlConfiguration loadConfig(String file) {
        return YamlConfiguration.loadConfiguration(getBundledFile(file));
    }

    File getDataDirectory() {
        return dataDirectory;
    }

}
