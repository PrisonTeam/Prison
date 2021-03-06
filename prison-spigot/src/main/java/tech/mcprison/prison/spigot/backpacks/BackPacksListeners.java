package tech.mcprison.prison.spigot.backpacks;

import org.bukkit.event.Listener;

public class BackPacksListeners implements Listener {

    private static BackPacksListeners instance;

    /**
     * Get BackPacksListeners instance.
     * */
    public static BackPacksListeners get() {
        if (instance == null) {
            instance = new BackPacksListeners();
        }
        return instance;
    }

    /**
     * Java getBoolean's broken so I made my own.
     * */
    public boolean getBoolean(String string){

        if (string == null){
            return false;
        }

        if (string.equalsIgnoreCase("true")){
            return true;
        } else if (string.equalsIgnoreCase("false")){
            return false;
        }

        return false;
    }

}


