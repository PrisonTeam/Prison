package tech.mcprison.prison.spigot.economies;

import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.integration.EconomyIntegration;
import tech.mcprison.prison.integration.IntegrationType;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.output.Output;

/**
 * @author Faizaan A. Datoo
 */
public class SaneEconomy 
	extends EconomyIntegration {

    private SaneEconomyWrapper econWrapper;
    private boolean availableAsAnAlternative = false;
    
    public SaneEconomy() {
    	super( "SaneEconomy", "SaneEconomy" );
    }
	
	@Override
	public void integrate() {
		if (isRegistered()) {
			
			// if an econ is already registered, then don't register this one:
			boolean econAlreadySet = PrisonAPI.getIntegrationManager().getForType( IntegrationType.ECONOMY ).isPresent();
				
			if ( !econAlreadySet ) {
				this.econWrapper = new SaneEconomyWrapper(getProviderName());
			} else {
				Output.get().logInfo( "SaneEconomy is not directly enabled - Available as backup. " );
				this.availableAsAnAlternative = true;
			}

		}
	}
	
    
    @Override
    public boolean hasAccount( Player player ) {
    	return econWrapper.hasAccount( player );
    }
	
    @Override 
    public double getBalance(Player player) {
        return econWrapper.getBalance(player);
    }

    @Override 
    public boolean setBalance(Player player, double amount) {
    	econWrapper.setBalance(player, amount);
    	return true;
    }

    @Override 
    public boolean addBalance(Player player, double amount) {
        return setBalance(player, getBalance(player) + amount);
    }

    @Override 
    public boolean removeBalance(Player player, double amount) {
        return setBalance(player, getBalance(player) - amount);
    }

    @Override 
    public boolean canAfford(Player player, double amount) {
        return getBalance(player) >= amount;
    }
    
    @Override 
    public boolean hasIntegrated() {
        return econWrapper != null;
    }
    
    @Override
    public void disableIntegration() {
    	econWrapper = null;
    }
    
    @Override
    public String getDisplayName()
    {
    	return super.getDisplayName() + " (API v0.15.0)"+
    			( availableAsAnAlternative ? " (disabled)" : "");
    }
    
	@Override
	public String getPluginSourceURL() {
		return "https://www.spigotmc.org/resources/saneeconomy-simple-but-featureful-economy.26223/";
	}
}
