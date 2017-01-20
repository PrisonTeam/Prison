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

package tech.mcprison.prison;

import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tech.mcprison.prison.adapters.LocationAdapter;
import tech.mcprison.prison.commands.CommandHandler;
import tech.mcprison.prison.commands.PluginCommand;
import tech.mcprison.prison.config.ConfigurationLoader;
import tech.mcprison.prison.internal.platform.Platform;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.modules.ModuleManager;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.selection.SelectionManager;
import tech.mcprison.prison.store.AnnotationExclusionStrategy;
import tech.mcprison.prison.store.Exclude;
import tech.mcprison.prison.util.EventExceptionHandler;
import tech.mcprison.prison.util.ItemManager;
import tech.mcprison.prison.util.Location;

import java.io.File;

/**
 * Entry point for implementations. <p> An instance of Prison can be retrieved using the static
 * {@link Prison#get()} method, however in order to use the core libraries, you must call
 * {@link Prison#init(Platform)} with a valid {@link Platform} implementation.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class Prison {

    // Singleton

    private static Prison instance = null;
    private Platform platform;

    // Fields

    public static final int API_LEVEL = 1;

    private File dataFolder;
    private ModuleManager moduleManager;
    private CommandHandler commandHandler;
    private SelectionManager selectionManager;
    private ConfigurationLoader configurationLoader, messagesLoader;
    private Gson gson;
    private EventBus eventBus;
    private ItemManager itemManager;

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
     * Note that modules <b>should not call this method</b> unless under very special circumstances.
     * This is intended solely for the implementations.
     */
    public void init(Platform platform) {
        long startTime = System.currentTimeMillis();

        this.platform = platform;
        Output.get().logInfo("Using platform &3%s&f.", platform.getClass().getName());
        Output.get().logInfo("Enable start...");

        // Initialize various parts of the API. The magic happens here :)
        initDataFolder();
        initGson();
        initMessages();
        initConfig();
        initManagers();


        this.commandHandler.registerCommands(new PrisonCommand());

        Output.get()
            .logInfo("Enabled &3Prison v%s in %d milliseconds.", getPlatform().getPluginVersion(),
                (System.currentTimeMillis() - startTime));
    }

    private void initDataFolder() {
        // Creates the /Prison/Core directory, for core configuration.
        this.dataFolder = new File(platform.getPluginDirectory(), "Core");
        if (!this.dataFolder.exists()) {
            this.dataFolder.mkdir();
        }
    }

    private void initGson() {
        // Creates a handy instance of GSON with pretty printing, disabled HTML escaping, @Exclude support, and all adapters registered.
        // Note that if any adapters are added to the adapters package, this block must be updated.
        this.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping()
            .setExclusionStrategies(new AnnotationExclusionStrategy())
            .registerTypeAdapter(Location.class, new LocationAdapter()).create();
    }

    private void initMessages() {
        // Our localization configuration, located in /Prison/Core/messages.json
        this.messagesLoader =
            new ConfigurationLoader(getDataFolder(), "messages.json", Messages.class,
                Messages.VERSION);
        this.messagesLoader.loadConfiguration();
    }

    private void initConfig() {
        // Our configuration, located in /Prison/Core/config.json
        this.configurationLoader =
            new ConfigurationLoader(getDataFolder(), "config.json", Configuration.class,
                Configuration.VERSION);
        this.configurationLoader.loadConfiguration();
    }

    private void initManagers() {
        // Now we initialize the API
        this.eventBus = new EventBus(new EventExceptionHandler());
        this.moduleManager = new ModuleManager();
        this.commandHandler = new CommandHandler();
        this.selectionManager = new SelectionManager();
        this.itemManager = new ItemManager();
    }

    /**
     * Finalizes and unregisters instances in prison-core. In the implementations, this should be
     * called when the plugin is disabled.
     */
    public void deinit() {
        moduleManager.unregisterAll();
    }

    // Getters

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
     * Returns the core data folder, which is located at "/plugins/Prison/Core". This contains the
     * core config.json and messages.json files, as well as other global data.
     *
     * @return the {@link File}.
     */
    public File getDataFolder() {
        return dataFolder;
    }

    /**
     * Returns an instance of {@link Gson}, which can be used to serialize/de-serialize JSON files.
     * This comes with the annotation exclusion strategy (see {@link Exclude})
     * and all adapters in the <i>tech.mcprison.prison.adapters</i> package registered.
     *
     * @return The {@link Gson} object, ready for use.
     */
    public Gson getGson() {
        return gson;
    }

    /**
     * Returns the configuration class, which contains each configuration option in a public
     * variable. It is loaded and saved via a {@link ConfigurationLoader}.
     *
     * @return the {@link Configuration}.
     */
    public Configuration getConfig() {
        return (Configuration) configurationLoader.getConfig();
    }

    /**
     * Returns the messages class, which contains each localization option in a public variable. It
     * is loaded and saved via a {@link ConfigurationLoader}.
     *
     * @return the {@link Messages}.
     */
    public Messages getMessages() {
        return (Messages) messagesLoader.getConfig();
    }

    /**
     * Returns the event bus, where event listeners can be registered and events can be posted.
     *
     * @return The {@link EventBus}.
     */
    public EventBus getEventBus() {
        return eventBus;
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
     * Returns the selection manager, which stores each player's current selection using Prison's
     * selection wand.
     *
     * @return The {@link SelectionManager}.
     */
    public SelectionManager getSelectionManager() {
        return selectionManager;
    }
    /**
     * Returns the item manager, which manages the "friendly" names of items
     */
    public ItemManager getItemManager(){return itemManager;}

    /**
     * This method is mainly for the use of the command library. It retrieves a list of commands
     * from the internal, and then queries the list for a command with a certain label.
     * <p>
     * The chances that a module will have to use this is slim. Instead, use the command library
     * located in the {@link tech.mcprison.prison.commands} package.
     *
     * @param label The command's label.
     * @return The {@link PluginCommand}, or null if no command exists by that label.
     */
    public PluginCommand getCommand(String label) {
        for (PluginCommand command : platform.getCommands()) {
            if (command.getLabel().equalsIgnoreCase(label)) {
                return command;
            }
        }
        return null;
    }

}
