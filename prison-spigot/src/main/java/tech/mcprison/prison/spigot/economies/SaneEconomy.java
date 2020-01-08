package tech.mcprison.prison.spigot.economies;

import org.appledash.saneeconomy.economy.EconomyManager;
import org.appledash.saneeconomy.economy.economable.EconomablePlayer;
import org.bukkit.Bukkit;
import tech.mcprison.prison.integration.EconomyIntegration;
import tech.mcprison.prison.internal.Player;

/**
 * @author Faizaan A. Datoo
 */
public class SaneEconomy implements EconomyIntegration {

    public static final String PROVIDER_NAME = "SaneEconomy";
	private EconomyManager economyManager;

    public SaneEconomy() {
    	if (Bukkit.getPluginManager().isPluginEnabled(PROVIDER_NAME)) {
    		org.appledash.saneeconomy.SaneEconomy saneEconomy = 
    				(org.appledash.saneeconomy.SaneEconomy) 
    					Bukkit.getServer().getPluginManager().getPlugin(PROVIDER_NAME);
    		
    		if(saneEconomy != null) {
    			this.economyManager = saneEconomy.getEconomyManager();
    		}
    	}
    }

    @Override public double getBalance(Player player) {
        return economyManager.getBalance(toEconomablePlayer(player));
    }

    @Override public void setBalance(Player player, double amount) {
        economyManager.setBalance(toEconomablePlayer(player), amount);
    }

    @Override public void addBalance(Player player, double amount) {
        setBalance(player, getBalance(player) + amount);
    }

    @Override public void removeBalance(Player player, double amount) {
        setBalance(player, getBalance(player) - amount);
    }

    @Override public boolean canAfford(Player player, double amount) {
        return getBalance(player) >= amount;
    }

    private EconomablePlayer toEconomablePlayer(Player player) {
        return new EconomablePlayer(Bukkit.getOfflinePlayer(player.getUUID()));
    }

    @Override public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override public boolean hasIntegrated() {
        return false;
    }

}
