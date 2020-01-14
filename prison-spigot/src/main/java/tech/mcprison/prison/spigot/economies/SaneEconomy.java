package tech.mcprison.prison.spigot.economies;

import org.bukkit.Bukkit;

import tech.mcprison.prison.integration.EconomyIntegration;
import tech.mcprison.prison.internal.Player;

/**
 * @author Faizaan A. Datoo
 */
public class SaneEconomy implements EconomyIntegration {

    public static final String PROVIDER_NAME = "SaneEconomy";
    private SaneEconomyWrapper econWrapper;

    public SaneEconomy() {
    	super();
    }
	
	@Override
	public void integrate() {
		if (Bukkit.getPluginManager().isPluginEnabled(PROVIDER_NAME)) {
			this.econWrapper = new SaneEconomyWrapper(PROVIDER_NAME);
		}
	}
	
    @Override 
    public double getBalance(Player player) {
        return econWrapper.getBalance(player);
    }

    @Override 
    public void setBalance(Player player, double amount) {
    	econWrapper.setBalance(player, amount);
    }

    @Override 
    public void addBalance(Player player, double amount) {
        setBalance(player, getBalance(player) + amount);
    }

    @Override 
    public void removeBalance(Player player, double amount) {
        setBalance(player, getBalance(player) - amount);
    }

    @Override 
    public boolean canAfford(Player player, double amount) {
        return getBalance(player) >= amount;
    }

    @Override 
    public String getProviderName() {
        return PROVIDER_NAME;
    }
    
    @Override
    public String getKeyName() {
    	return PROVIDER_NAME;
    }
    
    @Override 
    public boolean hasIntegrated() {
        return false;
    }

}
