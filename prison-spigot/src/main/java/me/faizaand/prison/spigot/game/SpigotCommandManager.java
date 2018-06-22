package me.faizaand.prison.spigot.game;

import me.faizaand.prison.Prison;
import me.faizaand.prison.commands.PluginCommand;
import me.faizaand.prison.internal.platform.CommandManager;
import me.faizaand.prison.output.Output;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.*;

public class SpigotCommandManager implements CommandManager {

    private Field commandMap, knownCommands;
    private List<PluginCommand> commands = new ArrayList<>();

    public SpigotCommandManager() {
        try {
            commandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMap.setAccessible(true);
            knownCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommands.setAccessible(true);
        } catch (NoSuchFieldException e) {
            Output.get().logError(
                    "&c&lReflection error: &7Ensure that you're using the latest version of Spigot and Prison.");
            e.printStackTrace();
        }
    }

    public void registerCommand(PluginCommand command) {
        try {
            ((SimpleCommandMap) commandMap.get(Bukkit.getServer()))
                    .register(command.getLabel(), "prison",
                            new Command(command.getLabel(), command.getDescription(), command.getUsage(),
                                    Collections.emptyList()) {

                                @Override
                                public boolean execute(CommandSender sender, String commandLabel,
                                                       String[] args) {
                                    if (sender instanceof Player) {
                                        return Prison.get().getCommandHandler()
                                                .onCommand(new SpigotPlayer((org.bukkit.entity.Player) sender),
                                                        command, commandLabel, args);
                                    }
                                    return Prison.get().getCommandHandler()
                                            .onCommand(new SpigotCommandSender(sender), command, commandLabel,
                                                    args);
                                }

                            });
            commands.add(command);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void unregisterCommand(String commandName) {
        try {
            ((Map<String, Command>) knownCommands
                    .get(commandMap.get(Bukkit.getServer()))).remove(commandName);
            this.commands.removeIf(pluginCommand -> pluginCommand.getLabel().equals(commandName));
        } catch (IllegalAccessException e) {
            e.printStackTrace(); // This should only happen if something's wrong up there.
        }
    }

    @Override
    public Optional<PluginCommand> getCommand(String label) {
        for (PluginCommand command : getCommands()) {
            if (command.getLabel().equalsIgnoreCase(label)) {
                return Optional.of(command);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<PluginCommand> getCommands() {
        return commands;
    }

}
