package me.faizaand.prison.spigot.game;

import me.faizaand.prison.internal.GameConsole;
import org.bukkit.Bukkit;

public class SpigotConsole extends SpigotCommandSender implements GameConsole {

    public SpigotConsole() {
        super(Bukkit.getConsoleSender());
    }

}
