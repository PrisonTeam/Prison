package tech.mcprison.prison.spigot.backpacks;

public class BackPacksUtil {

    private static BackPacksUtil instance;

    /**
     * Get SellAll instance.
     * */
    public static BackPacksUtil get() {
        if (instance == null){
            instance = new BackPacksUtil();
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
