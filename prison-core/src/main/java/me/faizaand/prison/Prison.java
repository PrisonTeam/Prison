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

package me.faizaand.prison;

import me.faizaand.prison.alerts.Alerts;
import me.faizaand.prison.commands.CommandHandler;
import me.faizaand.prison.error.Error;
import me.faizaand.prison.error.ErrorManager;
import me.faizaand.prison.events.EventManager;
import me.faizaand.prison.events.StandardEventManager;
import me.faizaand.prison.integration.Integration;
import me.faizaand.prison.integration.IntegrationManager;
import me.faizaand.prison.internal.platform.Platform;
import me.faizaand.prison.localization.LocaleManager;
import me.faizaand.prison.modules.Module;
import me.faizaand.prison.modules.ModuleManager;
import me.faizaand.prison.modules.PluginEntity;
import me.faizaand.prison.output.Output;
import me.faizaand.prison.selection.SelectionManager;
import me.faizaand.prison.signs.DisplaySignManager;
import me.faizaand.prison.store.Database;
import me.faizaand.prison.troubleshoot.TroubleshootManager;
import me.faizaand.prison.troubleshoot.Troubleshooter;
import me.faizaand.prison.troubleshoot.inbuilt.ItemTroubleshooter;
import me.faizaand.prison.util.ItemManager;

import java.io.File;
import java.util.Optional;

/**
 * Entry point for implementations. <p> An instance of Prison can be retrieved using the static
 * {@link Prison#get()} method, however in order to use the core libraries, you must call
 * {@link Prison#init(Platform)} with a valid {@link Platform} implementation.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class Prison implements PluginEntity {

    // Singleton

    public static final int API_LEVEL = 4;
    private static Prison instance = null;

    // Fields
    private Platform platform;
    private File dataFolder;
    private ModuleManager moduleManager;
    private CommandHandler commandHandler;
    private LocaleManager localeManager;
    private ItemManager itemManager;
    private ErrorManager errorManager;
    private TroubleshootManager troubleshootManager;
    private IntegrationManager integrationManager;
    private SelectionManager selectionManager;
    private Database metaDatabase;

    /**
     * Gets the current instance of this class. <p> An instance will always be available after
     * the implementation invokes the {@link Prison#init(Platform)} method.
     *
     * @return an instance of Prison.
     */
    public static Prison get() {
        if (instance == null) {
            instance = new Prison();
        }
        return instance;
    }

    // Public methods

    /**
     * Initializes prison-core. In the implementations, this should be called when the plugin is
     * enabled. After this is called, every getter in this class will return a value.
     * <p>
     * Note that modules <b>should not call this method</b>. This is solely for the implementations.
     */
    public boolean init(Platform platform) {
        long startTime = System.currentTimeMillis();

        this.platform = platform;
        sendBanner();
        Output.get().logInfo("Enable start...");

        // Initialize various parts of the API. The magic happens here :)
        if (!initDataFolder()) {
            return false;
        }
        initManagers();
        if (!initMetaDatabase()) {
            return false;
        }
        Alerts.getInstance(); // init alerts
        DisplaySignManager.getInstance(); // init display signs

        this.selectionManager = new SelectionManager();
        
        this.commandHandler.registerCommands(new PrisonCommand());

        Output.get()
                .logInfo("Enabled &3Prison v%s in %d milliseconds.", getPlatform().getPluginVersion(),
                        (System.currentTimeMillis() - startTime));

        registerInbuiltTroubleshooters();

        if (getPlatform().shouldShowAlerts())
            scheduleAlertNagger();

        return true;
    }

    /**
     * Steps after the initialization of the entire API.
     */
    public void postLoad() {
    }

    // Initialization steps

    private void sendBanner() {
        Prison.get().getPlatform().log("");
        Prison.get().getPlatform().log("&6 _____      _                 ");
        Prison.get().getPlatform().log("&6|  __ \\    (_)                ");
        Prison.get().getPlatform().log("&6| |__) | __ _ ___  ___  _ __  ");
        Prison.get().getPlatform().log("&6|  ___/ '__| / __|/ _ \\| '_ \\");
        Prison.get().getPlatform().log("&6| |   | |  | \\__ \\ (_) | | | |");
        Prison.get().getPlatform().log("&6|_|   |_|  |_|___/\\___/|_| |_|");
        Prison.get().getPlatform().log("");
        Prison.get().getPlatform().log("Loading version %s on platform %s...", Prison.get().getPlatform().getPluginVersion(),
                platform.getClass().getSimpleName());
    }

    private boolean initDataFolder() {
        // Creates the /Prison directory, for core configuration.
        this.dataFolder = getPlatform().getPluginDirectory();
        return this.dataFolder.exists() || this.dataFolder.mkdir();
    }

    private boolean initMetaDatabase() {
        Optional<Database> metaDatabaseOptional = getPlatform().getStorage().getDatabase("meta");
        if (!metaDatabaseOptional.isPresent()) {
            getPlatform().getStorage().createDatabase("meta");
            metaDatabaseOptional = getPlatform().getStorage().getDatabase("meta");

            if (!metaDatabaseOptional.isPresent()) {
                Output.get().logError(
                        "Could not create the meta database. This means that something is wrong with the data storage for the plugin.");
                Output.get().logError("The plugin will now disable.");
                return false;
            }
        }
        metaDatabase = metaDatabaseOptional.get();
        return true;
    }

    private void initManagers() {
        // Now we initialize the API
        this.localeManager = new LocaleManager(this, "lang/core");
        this.errorManager = new ErrorManager(this);
        this.moduleManager = new ModuleManager();
        this.commandHandler = new CommandHandler();
        this.troubleshootManager = new TroubleshootManager();
        this.integrationManager = new IntegrationManager();

        try {
            this.itemManager = new ItemManager();
        } catch (Exception e) {
            this.errorManager.throwError(new Error(
                    "Error while loading items.csv. Try running /prison troubleshoot item_scan.")
                    .appendStackTrace("when loading items.csv", e));
            Output.get().logError("Try running /prison troubleshoot item_scan.");
        }
    }

    private void registerInbuiltTroubleshooters() {
        Prison.get().getTroubleshootManager().registerTroubleshooter(new ItemTroubleshooter());
    }

    private void scheduleAlertNagger() {
        // Nag the user with alerts every 5 minutes
        Prison.get().getPlatform().getScheduler().runTaskTimerAsync(() -> Prison.get().getPlatform().getPlayerManager().getOnlinePlayers().stream()
                .filter(player -> player.hasPermission("prison.admin")
                        && Alerts.getInstance().getAlertsFor(player.getUUID()).size() > 0)
                .forEach(Alerts.getInstance()::showAlerts), 60 * 20 * 5, 60 * 20 * 5);
    }

    // End initialization steps

    /**
     * Finalizes and unregisters instances in prison-core. In the implementations, this should be
     * called when the plugin is disabled.
     */
    public void deinit() {
        moduleManager.unregisterAll();
    }

    // Getters

    @Override
    public String getName() {
        return "PrisonCore";
    }

    /**
     * Returns the Platform in use, which contains methods for interacting with the Minecraft server
     * on whichever implementation is currently being used.
     *
     * @return the {@link Platform}.
     */
    public Platform getPlatform() {
        return platform;
    }

    /**
     * Returns the core data folder, which is located at "/plugins/Prison". This contains the
     * core config.json and messages.json files, as well as other global data.
     *
     * @return the {@link File}.
     */
    public File getDataFolder() {
        return dataFolder;
    }

    /**
     * Returns the {@link LocaleManager} for the plugin. This contains the global messages that Prison
     * uses to run its command library, and the like. {@link Module}s have their own {@link
     * LocaleManager}s, so that each module can have independent localization.
     *
     * @return The global locale manager instance.
     */
    public LocaleManager getLocaleManager() {
        return localeManager;
    }

    /**
     * Returns the core's {@link ErrorManager}. For modules, you should use your own module's error manager via {@link Module#getErrorManager()}.
     *
     * @return The core's error manager instance.
     */
    public ErrorManager getErrorManager() {
        return errorManager;
    }

    /**
     * Returns the core's event manager.
     *
     * @return the event manager.
     */
    public EventManager getEventManager() {
        return getPlatform().getEventManager();
    }

    /**
     * Returns the module manager, which stores instances of all registered {@link
     * Module}s and manages their state.
     *
     * @return The {@link ModuleManager}.
     */
    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    /**
     * Returns the command handler, where command methods can be registered using the {@link
     * CommandHandler#registerCommands(Object)} method.
     *
     * @return The {@link CommandHandler}.
     */
    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    /**
     * Returns the item manager, which manages the "friendly" names of items
     */
    public ItemManager getItemManager() {
        return itemManager;
    }

    /**
     * Returns the meta database, which is used to store data from within the core.
     */
    public Database getMetaDatabase() {
        return metaDatabase;
    }

    /**
     * Returns the troubleshoot manager, which is used to register {@link Troubleshooter}s.
     */
    public TroubleshootManager getTroubleshootManager() {
        return troubleshootManager;
    }

    /**
     * Returns the integration manager, which returns {@link Integration}s.
     */
    public IntegrationManager getIntegrationManager() {
        return integrationManager;
    }

    public SelectionManager getSelectionManager() {
        return selectionManager;
    }

}
