package tech.mcprison.prison.spigot.economies;

import tech.mcprison.prison.integration.EconomyCurrencyIntegration;
import tech.mcprison.prison.internal.Player;

public class CoinsEngineEconomy
	extends EconomyCurrencyIntegration {


	private CoinsEngineEconomyWrapper wrapper = null;
	private boolean availableAsAnAlternative = false;

    public CoinsEngineEconomy() {
    	super( "CoinsEngineEconomy", "CoinsEngineEconomy" );
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
				this.wrapper = new CoinsEngineEconomyWrapper();
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
    		
    		synchronized ( wrapper ) {
    			amount = wrapper.getBalance(player);
    		}
    	}
    	return amount;
    }
    
    @Override
    public boolean hasAccount( Player player ) {
    	return true;
    }
    
    @Override 
    public double getBalance(Player player, String currencyName) {
    	double amount = 0;
    	if ( wrapper != null ) {
    			
    		synchronized ( wrapper ) {
    			amount = wrapper.getBalance(player, currencyName, false);
    		}
    	}
    	return amount;
    }
    
//    @Override 
//    public double getBalance(Player player, String currencyName, boolean quite) {
//    	double amount = 0;
//    	if ( wrapper != null ) {
//    		
//    		synchronized ( wrapper ) {
//    			amount = wrapper.getBalance(player, currencyName, quite);
//    		}
//    	}
//    	return amount;
//    }

    @Override 
    public boolean setBalance(Player player, double amount) {
    	boolean results = false;

    	if ( wrapper != null ) {
    		synchronized ( wrapper ) {
    			
    			double bal = getBalance(player);
    			double remainder = amount - bal;
    			
    			if ( remainder > 0 ) {
    				wrapper.addBalance( player, remainder );
    			} 
    			else if ( remainder < 0 ) {
    				wrapper.withdraw( player, (remainder * -1) );
    			}
    			double balResults = getBalance(player);
    			
    			results = balResults == amount;
    		}
    	}
    	return results;
    }
    
    @Override 
    public boolean setBalance(Player player, double amount, String currencyName) {
    	boolean results = false;
    	
    	if ( wrapper != null ) {
    		
    		synchronized ( wrapper ) {
				
    			double bal = getBalance(player, currencyName);
    			double remainder = amount - bal;
    			
    			if ( remainder > 0 ) {
    				wrapper.addBalance( player, remainder, currencyName );
    			} 
    			else if ( remainder < 0 ) {
    				wrapper.withdraw( player, (remainder * -1), currencyName );
    			}
    			double balResults = getBalance(player, currencyName);
    			
    			results = balResults == amount;
			}
    	}
    	return results;
    }

    @Override 
    public boolean addBalance(Player player, double amount) {
    	boolean results = false;

    	if ( wrapper != null ) {

    		synchronized ( wrapper ) {

    			double bal = getBalance(player);
    			wrapper.addBalance(player, amount);
    			double balResults = getBalance(player);
    			
    			results = balResults == amount + bal;
    		}
    	}
    	
    	return results;
    }
    
    @Override 
    public boolean addBalance(Player player, double amount, String currencyName) {
    	boolean results = false;

    	if ( wrapper != null ) {
    		
    		synchronized ( wrapper ) {
    			
    			double bal = getBalance(player, currencyName);
    			wrapper.addBalance(player, amount, currencyName);
    			double balResults = getBalance(player, currencyName);
    			
    			results = balResults == amount + bal;
    		}
    	}

    	return results;
    }

    @Override 
    public boolean removeBalance(Player player, double amount) {
    	boolean results = false;

    	if ( wrapper != null ) {
    		synchronized ( wrapper ) {
    			
    			double bal = getBalance(player);
    			wrapper.withdraw(player, amount);
    			double balResults = getBalance(player);
    			
    			results = balResults == bal - amount;
    		}
    	}

    	return results;
    }
    
    @Override 
    public boolean removeBalance(Player player, double amount, String currencyName) {
    	boolean results = false;

    	if ( wrapper != null ) {
    		synchronized ( wrapper ) {
    			
    			double bal = getBalance(player, currencyName);
    			wrapper.withdraw(player, amount, currencyName);
    			double balResults = getBalance(player, currencyName);
    			
    			results = balResults == bal - amount;
    		}
    	}

    	return results;
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
        return wrapper != null && wrapper.isEnabled();
    }

    @Override
    public void disableIntegration() {
    	wrapper = null;
    }
    
    @Override
    public String getDisplayName()
    {
    	return super.getDisplayName() + 
    			( availableAsAnAlternative ? " (disabled)" : "");
    }
    
	@Override
	public String getPluginSourceURL() {
		return "https://www.spigotmc.org/resources/coinsengine-economy-and-virtual-currencies.84121/";
	}


}
