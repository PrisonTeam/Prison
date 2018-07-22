package me.faizaand.prison.internal;

/**
 * A console command sender.
 */
public interface GameConsole extends CommandSender {

    default String getName() { return "CONSOLE"; }

    @Override
    default boolean hasPermission(String perm) {
        return true; // console has all permissions
    }



}
