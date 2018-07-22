package me.faizaand.prison.command;

import me.faizaand.prison.internal.CommandSender;
import me.faizaand.prison.internal.GameConsole;
import me.faizaand.prison.internal.GamePlayer;

import java.util.LinkedList;
import java.util.List;

public class PrisonCommand {

    private String[] aliases;
    private String description;
    private String usage;
    private List<Argument> arguments;
    private Class<? extends CommandSender> senderType = CommandSender.class; // by default we'll take both console and players

    public PrisonCommand(String description, String usage, String... aliases) {
        this.aliases = aliases;
        this.description = description;
        this.usage = usage;
        this.arguments = new LinkedList<>();
    }

    public PrisonCommand applyArgument(Argument arg) {
        this.arguments.add(arg);
        return this;
    }

    public PrisonCommand assertPlayer() {
        this.senderType = GamePlayer.class;
        return this;
    }

    public PrisonCommand assertConsole() {
        this.senderType = GameConsole.class;
        return this;
    }

}
