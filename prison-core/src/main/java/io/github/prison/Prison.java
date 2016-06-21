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

package io.github.prison;

import com.google.common.eventbus.EventBus;
import io.github.prison.commands.CommandHandler;
import io.github.prison.commands.PluginCommand;
import io.github.prison.modules.ModuleManager;

/**
 * Entry point for implementations.
 *
 * @author SirFaizdat
 * @since 3.0
 */
public class Prison {

    // Singleton

    private static Prison instance = null;

    public static Prison getInstance() {
        if (instance == null) instance = new Prison();
        return instance;
    }

    // Fields

    private Platform platform;
    private ModuleManager moduleManager;
    private CommandHandler commandHandler;
    private EventBus eventBus;

    // Public methods

    /**
     * Initializes prison-core.
     * In the implementations, this should be called when the plugin is enabled.
     */
    public void init(Platform platform) {
        long startTime = System.currentTimeMillis();
        platform.log("&7> &dENABLE START &7 <");

        this.platform = platform;
        platform.log("&7Using platform &3%s&7.", platform.getClass().getName());

        this.eventBus = new EventBus();
        this.moduleManager = new ModuleManager();
        this.commandHandler = new CommandHandler();

        platform.log("&7Enabled &3Prison v%s&7.", platform.getPluginVersion());
        platform.log("&7> &dENABLE COMPLETE &5(%dms) &7<", (System.currentTimeMillis() - startTime));
    }

    /**
     * Finalizes and unregisters instances in prison-core.
     * In the implementations, this should be called when the plugin is disabled.
     */
    public void deinit() {
        moduleManager.unregisterAll();
    }

    // Getters

    public Platform getPlatform() {
        return platform;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public PluginCommand getCommand(String label) {
        for (PluginCommand command : platform.getCommands())
            if (command.getLabel().equalsIgnoreCase(label)) return command;
        return null;
    }

}
