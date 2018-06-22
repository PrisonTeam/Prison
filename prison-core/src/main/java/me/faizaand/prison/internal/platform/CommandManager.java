package me.faizaand.prison.internal.platform;

import me.faizaand.prison.commands.PluginCommand;

import java.util.List;
import java.util.Optional;

/**
 * Provides a registration bridge between Bukkit's command system and Prison's command system.
 *
 * @since 4.0
 */
public interface CommandManager {

    void registerCommand(PluginCommand command);

    void unregisterCommand(String command);

    Optional<PluginCommand> getCommand(String command);

    List<PluginCommand> getCommands();

}
