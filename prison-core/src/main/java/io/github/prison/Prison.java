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
import io.github.prison.commands.Command;
import io.github.prison.commands.CommandHandler;
import io.github.prison.commands.PluginCommand;
import io.github.prison.internal.CommandSender;
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
        this.platform = platform;
        this.eventBus = new EventBus();
        this.moduleManager = new ModuleManager();
        this.commandHandler = new CommandHandler();

        commandHandler.registerCommands(this);

        platform.log("&7Started &3Prison v%s&7 on platform &3%s.", platform.getPluginVersion(), platform.getClass().getName());
    }

    @Command(identifier = "prison", description = "The base command for Prison.", onlyPlayers = false)
    public void onPrisonTyped(CommandSender sender) {
        sender.sendMessage("&cHooray!");
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
