package me.faizaand.prison.spigot.integrations.economies;

import me.faizaand.prison.integration.EconomyIntegration;
import me.faizaand.prison.internal.GamePlayer;
import org.appledash.saneeconomy.economy.EconomyManager;
import org.appledash.saneeconomy.economy.economable.EconomablePlayer;
import org.bukkit.Bukkit;

/**
 * @author Faizaan A. Datoo
 */
public class SaneEconomy implements EconomyIntegration {

    private EconomyManager economyManager;

    public SaneEconomy() {
        org.appledash.saneeconomy.SaneEconomy saneEconomy = (org.appledash.saneeconomy.SaneEconomy) Bukkit
            .getServer().getPluginManager().getPlugin("SaneEconomy");
        if(saneEconomy == null) {
            return;
        }

        economyManager = saneEconomy.getEconomyManager();
    }

    @Override public double getBalance(GamePlayer player) {
        return economyManager.getBalance(toEconomablePlayer(player));
    }

    @Override public void setBalance(GamePlayer player, double amount) {
        economyManager.setBalance(toEconomablePlayer(player), amount);
    }

    @Override public void addBalance(GamePlayer player, double amount) {
        setBalance(player, getBalance(player) + amount);
    }

    @Override public void removeBalance(GamePlayer player, double amount) {
        setBalance(player, getBalance(player) - amount);
    }

    @Override public boolean canAfford(GamePlayer player, double amount) {
        return getBalance(player) >= amount;
    }

    private EconomablePlayer toEconomablePlayer(GamePlayer player) {
        return new EconomablePlayer(Bukkit.getOfflinePlayer(player.getUUID()));
    }

    @Override public String getProviderName() {
        return "SaneEconomy";
    }

    @Override public boolean hasIntegrated() {
        return false;
    }

}
