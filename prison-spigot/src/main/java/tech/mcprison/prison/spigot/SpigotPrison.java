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
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.update.spiget.SpigetUpdate;
import org.inventivetalent.update.spiget.UpdateCallback;
import org.inventivetalent.update.spiget.comparator.VersionComparator;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.PrisonCommand;
import tech.mcprison.prison.alerts.Alerts;
import tech.mcprison.prison.integration.Integration;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.LogLevel;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.spigot.compat.Compatibility;
import tech.mcprison.prison.spigot.compat.Spigot18;
import tech.mcprison.prison.spigot.compat.Spigot19;
import tech.mcprison.prison.spigot.economies.EssentialsEconomy;
import tech.mcprison.prison.spigot.economies.SaneEconomy;
import tech.mcprison.prison.spigot.economies.VaultEconomy;
import tech.mcprison.prison.spigot.gui.GUIListener;
import tech.mcprison.prison.spigot.permissions.LuckPermissions;
import tech.mcprison.prison.spigot.permissions.LuckPerms5;
import tech.mcprison.prison.spigot.permissions.VaultPermissions;
import tech.mcprison.prison.spigot.placeholder.MVdWPlaceholderIntegration;
import tech.mcprison.prison.spigot.placeholder.PlaceHolderAPIIntegration;

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
    boolean debug = false;

    private File dataDirectory;
    private boolean doAlertAboutConvert = false;

//    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onLoad() {
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
    public void onEnable() {
        this.saveDefaultConfig();
        debug = getConfig().getBoolean("debug", false);

        initDataDir();
        initCommandMap();
        initCompatibility();
        initUpdater();
        this.scheduler = new SpigotScheduler(this);
        GUIListener.get().init(this);
        Prison.get().init(new SpigotPlatform(this), Bukkit.getVersion());
        Prison.get().getLocaleManager().setDefaultLocale(getConfig().getString("default-language", "en_US"));
        new SpigotListener(this).init();
        
        initIntegrations();
        initModules();

        applyDeferredIntegrationInitializations();
        
        initMetrics();

        if (doAlertAboutConvert) {
            Alerts.getInstance().sendAlert(
                    "&7An old installation of Prison has been detected. &3Type /prison convert to convert your old data automatically. &7If you already converted, delete the 'Prison.old' folder so that we stop nagging you.");
        }
        
        // Finally print the version after loading the prison plugin:
        PrisonCommand cmdVersion = new PrisonCommand();
        ChatDisplay cdVersion = cmdVersion.displayVersion();
        cdVersion.toLog( LogLevel.INFO );
    }

    @Override
    public void onDisable() {
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
            for (Module m : PrisonAPI.getModuleManager().getModules()) {
                valueMap.put(m.getName(), 1);
            }
            return valueMap;
        }));

        // Report the API level
        metrics.addCustomChart(
                new Metrics.SimplePie("api_level", () -> "API Level " + Prison.API_LEVEL));
        
        Optional<Module> prisonMinesOpt = Prison.get().getModuleManager().getModule( PrisonMines.MODULE_NAME );
        Optional<Module> prisonRanksOpt = Prison.get().getModuleManager().getModule( PrisonRanks.MODULE_NAME );
        
        int mineCount = !prisonMinesOpt.isPresent() ? 0 : ((PrisonMines) prisonMinesOpt.get()).getMineManager().getMines().size();
        int rankCount = !prisonRanksOpt.isPresent() ? 0 : ((PrisonRanks) prisonRanksOpt.get()).getRankCount();
        int ladderCount = !prisonRanksOpt.isPresent() ? 0 : ((PrisonRanks) prisonRanksOpt.get()).getladderCount();
        
        metrics.addCustomChart(new Metrics.MultiLineChart("mines_ranks_and_ladders", new Callable<Map<String, Integer>>() {
            @Override
            public Map<String, Integer> call() throws Exception {
                Map<String, Integer> valueMap = new HashMap<>();
                valueMap.put("mines", mineCount);
                valueMap.put("ranks", rankCount);
                valueMap.put("ladders", ladderCount);
                return valueMap;
            }
        }));
    }

	private void initUpdater() {
        if (!getConfig().getBoolean("check-updates")) {
            return; // Don't check if they don't want it
        }

        SpigetUpdate updater = new SpigetUpdate(this, Prison.SPIGOTMC_ORG_PROJECT_ID);
//        SpigetUpdate updater = new SpigetUpdate(this, 1223);
        updater.setVersionComparator(VersionComparator.EQUAL);

        updater.checkForUpdate(new UpdateCallback() {
            @Override
            public void updateAvailable(String newVersion, String downloadUrl,
                                        boolean hasDirectDownload) {
                Alerts.getInstance().sendAlert(
                        "&3%s is now available. &7Go to the &lSpigot&r&7 page to download the latest release with new features and fixes :)",
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
        int minorVersionInt = 9;
        try {
            minorVersionInt = Integer.parseInt(version[1]);
        } catch (NumberFormatException e) {
            try {
                minorVersionInt =
                        Integer.parseInt(version[1].substring(0, version[1].indexOf(')')));
            } catch (Exception ex) {
                Output.get().logError(
                        "Unable to determine server version. Assuming spigot 1.9 or greater.");
            }
        }

        if (minorVersionInt <= 8) {
            compatibility = new Spigot18();
        } else {
            compatibility = new Spigot19();
        }

        getLogger().info("Using version adapter " + compatibility.getClass().getName());
    }

    private void initIntegrations() {

    	registerIntegration(new VaultEconomy());
        registerIntegration(new EssentialsEconomy());
        registerIntegration(new SaneEconomy());

        registerIntegration(new VaultPermissions());
        registerIntegration(new LuckPerms5());
        registerIntegration(new LuckPermissions());

        registerIntegration(new MVdWPlaceholderIntegration());
        registerIntegration(new PlaceHolderAPIIntegration());
    }
    
    private void registerIntegration(Integration integration) {
    	integration.setRegistered( Bukkit.getPluginManager().isPluginEnabled(integration.getProviderName()) );

    	integration.integrate();
		
    	PrisonAPI.getIntegrationManager().register(integration);
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
    }

    private void applyDeferredIntegrationInitializations() {
    	for ( Integration deferredIntegration : PrisonAPI.getIntegrationManager().getDeferredIntegrations() ) {
    		deferredIntegration.deferredInitialization();
    	}
    }
    
    private File getBundledFile(String name) {
        getDataFolder().mkdirs();
        File file = new File(getDataFolder(), name);
        if (!file.exists()) {
            saveResource(name, false);
        }
        return file;
    }

    private YamlConfiguration loadConfig(String file) {
        return YamlConfiguration.loadConfiguration(getBundledFile(file));
    }

    File getDataDirectory() {
        return dataDirectory;
    }

}
