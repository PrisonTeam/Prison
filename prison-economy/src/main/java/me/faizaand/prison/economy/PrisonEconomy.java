package me.faizaand.prison.economy;

import me.faizaand.prison.Prison;
import me.faizaand.prison.modules.Module;
import me.faizaand.prison.output.Output;
import me.faizaand.prison.store.Database;

public class PrisonEconomy extends Module {

    // https://github.com/lucko/LuckPerms/blob/68e4d36f405c18b564afcad4be4d5143aa68913e/bukkit/src/main/java/me/lucko/luckperms/bukkit/vault/VaultHookManager.java
    // good example of how to integrate with Vault but we'll do that in the implementation since it uses Bukkit stuff (i.e. platform-specific integration)

    private static PrisonEconomy instance;

    private Database database;
    private EconomyManager economyManager;

    public PrisonEconomy(String version) {
        super("Economy", version, 4);
    }

    @Override
    public void enable() {
        instance = this;
        if (!initDatabase()) {
            return;
        }

        this.economyManager = new EconomyManager();
        if (!this.economyManager.load()) {
            return;
        }

        new EconomyListener();
        new EconomyCommands();
    }

    @Override
    public void disable() {
        this.economyManager.save();
    }

    private boolean initDatabase() {
        Database databaseOpt = Prison.get().getPlatform().getStorage().getDatabase("economy").orElseGet(() ->
                Prison.get().getPlatform().getStorage().createDatabase("economy"));
        if (databaseOpt == null) {
            Output.get().logError("Could not initialize economy database. The economy will not load!");
            getStatus().toFailed("could not initialize economy database");
            return false;
        }

        this.database = databaseOpt;

        return true;
    }

    public static PrisonEconomy getInstance() {
        return instance;
    }

    public EconomyManager getEconomyManager() {
        return economyManager;
    }

    public Database getDatabase() {
        return database;
    }

}
