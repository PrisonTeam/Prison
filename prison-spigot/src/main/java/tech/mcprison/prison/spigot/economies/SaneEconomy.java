package tech.mcprison.prison.spigot.economies;

import tech.mcprison.prison.integration.EconomyIntegration;
import tech.mcprison.prison.internal.Player;

/**
 * @author Faizaan A. Datoo
 */
public class SaneEconomy 
	extends EconomyIntegration {

    private SaneEconomyWrapper econWrapper;

    public SaneEconomy() {
    	super( "SaneEconomy", "SaneEconomy" );
    }
	
	@Override
	public void integrate() {
		if (isRegistered()) {
			this.econWrapper = new SaneEconomyWrapper(getProviderName());
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
    public boolean hasIntegrated() {
        return false;
    }

	@Override
	public String getPluginSourceURL()
	{
		return "https://www.spigotmc.org/resources/saneeconomy-simple-but-featureful-economy.26223/";
	}
}
