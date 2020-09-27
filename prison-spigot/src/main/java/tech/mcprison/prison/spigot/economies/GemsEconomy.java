package tech.mcprison.prison.spigot.economies;

import tech.mcprison.prison.integration.EconomyCurrencyIntegration;
import tech.mcprison.prison.internal.Player;

public class GemsEconomy
	extends EconomyCurrencyIntegration
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
		addDebugInfo( "1" );
		if ( isRegistered()) {
			addDebugInfo( "2" );
			try {
				addDebugInfo( "3" );
				this.wrapper = new GemsEconomyWrapper();
				addDebugInfo( "4" );
			}
			catch ( java.lang.NoClassDefFoundError | Exception e ) {
				addDebugInfo( "5:Exception:" + e.getMessage() );
				e.printStackTrace();
			}
		}
		addDebugInfo( "6" );
	}

	
	@Override
	public boolean supportedCurrency( String currencyName ) {
		boolean supported = false;
		
		if ( wrapper != null ) {
			supported = wrapper.supportedCurrency( currencyName );
		}
		
		return supported;
	}
	
    @Override 
    public double getBalance(Player player) {
    	double amount = 0;
    	if ( wrapper != null ) {
    		amount = wrapper.getBalance(player);
    	}
    	return amount;
    }
    
    @Override 
    public double getBalance(Player player, String currencyName) {
    	double amount = 0;
    	if ( wrapper != null ) {
    		amount = wrapper.getBalance(player, currencyName);
    	}
    	return amount;
    }

    @Override 
    public void setBalance(Player player, double amount) {
    	if ( wrapper != null ) {
    		double remainder = amount - getBalance(player);
    		if ( remainder > 0 ) {
    			wrapper.addBalance( player, amount );
    		} if ( remainder < 0 ) {
    			wrapper.withdraw( player, amount );
    		}
    	}
    }
    
    @Override 
    public void setBalance(Player player, double amount, String currencyName) {
    	if ( wrapper != null ) {
    		double remainder = amount - getBalance(player, currencyName);
    		if ( remainder > 0 ) {
    			wrapper.addBalance( player, amount, currencyName );
    		} if ( remainder < 0 ) {
    			wrapper.withdraw( player, amount, currencyName );
    		}
    	}
    }

    @Override 
    public void addBalance(Player player, double amount) {
    	if ( wrapper != null ) {
    		wrapper.addBalance(player, amount);
    	}
    }
    
    @Override 
    public void addBalance(Player player, double amount, String currencyName) {
    	if ( wrapper != null ) {
    		wrapper.addBalance(player, amount, currencyName);
    	}
    }

    @Override 
    public void removeBalance(Player player, double amount) {
    	if ( wrapper != null ) {
    		wrapper.withdraw(player, amount);
    	}
    }
    
    @Override 
    public void removeBalance(Player player, double amount, String currencyName) {
    	if ( wrapper != null ) {
    		wrapper.withdraw(player, amount, currencyName);
    	}
    }

    @Override 
    public boolean canAfford(Player player, double amount) {
    	boolean results = false;
    	if ( wrapper != null ) {
    		results = getBalance(player) >= amount;
    	}
    	return results;
    }
    
    @Override 
    public boolean canAfford(Player player, double amount, String currencyName) {
    	boolean results = false;
    	if ( wrapper != null ) {
    		results = getBalance(player, currencyName) >= amount;
    	}
    	return results;
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
