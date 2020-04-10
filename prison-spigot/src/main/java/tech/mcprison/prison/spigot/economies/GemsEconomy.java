package tech.mcprison.prison.spigot.economies;

import tech.mcprison.prison.integration.EconomyIntegration;
import tech.mcprison.prison.internal.Player;

public class GemsEconomy
	extends EconomyIntegration
{

	private GemsEconomyWrapper wrapper = null;
	private boolean availableAsAnAlternative = false;

    public GemsEconomy() {
    	super( "GemsEconomy", "GemsEconomy" );
    }
	
    /**
     * <p>If GemsEconomy is registered, always hook this up because this may be needed 
     * to be used instead of the primary economy if they are using a custom currency.
     * </p>
     */
	@Override
	public void integrate() {
		if ( isRegistered() // && classLoaded 
				) {
			try {
				this.wrapper = new GemsEconomyWrapper();
			}
			catch ( java.lang.NoClassDefFoundError | Exception e )
			{
				e.printStackTrace();
			}
		}
	}

    @Override 
    public double getBalance(Player player) {
        return wrapper.getBalance(player);
    }

    @Override 
    public void setBalance(Player player, double amount) {
    	double remainder = amount - getBalance(player);
    	if ( remainder > 0 ) {
    		wrapper.addBalance( player, amount );
    	} if ( remainder < 0 ) {
    		wrapper.withdraw( player, amount );
    	}
    }

    @Override 
    public void addBalance(Player player, double amount) {
        wrapper.addBalance(player, amount);
    }

    @Override 
    public void removeBalance(Player player, double amount) {
        wrapper.withdraw(player, amount);
    }

    @Override 
    public boolean canAfford(Player player, double amount) {
        return getBalance(player) >= amount;
    }
    
    @Override 
    public boolean hasIntegrated() {
        return wrapper != null;
    }

    @Override
    public String getDisplayName()
    {
    	return super.getDisplayName() + 
    			( availableAsAnAlternative ? " (disabled)" : "");
    }
    
	@Override
	public String getPluginSourceURL() {
		return "https://www.spigotmc.org/resources/gemseconomy.19655/";
	}

	
}
