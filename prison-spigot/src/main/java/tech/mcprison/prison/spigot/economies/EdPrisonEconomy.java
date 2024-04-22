package tech.mcprison.prison.spigot.economies;

import java.util.List;

import tech.mcprison.prison.integration.EconomyCurrencyIntegration;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.output.Output;

public class EdPrisonEconomy
	extends EconomyCurrencyIntegration {

	private EdPrisonEconomyWrapper wrapper = null;
	private boolean availableAsAnAlternative = false;

    public EdPrisonEconomy() {
    	super( "EdPrison", "EdPrison" );
    }

	
    /**
     * <p>If EdPrison economy is registered, always hook this up because this may be needed 
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
				this.wrapper = new EdPrisonEconomyWrapper( getProviderName());
				addDebugInfo( "4" );
				
				List<String> currencies = this.wrapper.getAllEconomies();

				addDebugInfo( "4.5 - add currencies: " + currencies.size() );
				
				StringBuilder sb = new StringBuilder();
				
				for (String currency : currencies) {
					sb.append( currency ).append( " " );
					getSupportedCurrencies().put( currency, true );
				}
				
				Output.get().logInfo( 
						"EdPrison Economies supported: " + 
						(sb.length() > 0 ?
								sb.toString() :
								"*none*") );
				
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
    	double amount = getBalance( player, null );
    	
    	return amount;
    }
    
    @Override 
    public double getBalance(Player player, String currencyName) {
    	double amount = 0;
    	if ( wrapper != null ) {
    			
    		synchronized ( wrapper ) {
    			amount = wrapper.getBalance(player, currencyName);
    		}
    	}
    	return amount;
    }

    @Override 
    public boolean setBalance(Player player, double amount) {
    	boolean results = setBalance( player, amount, null );

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
    	boolean results = addBalance( player, amount, null );
    	
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
    	boolean results = removeBalance( player, amount, null );

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
    	boolean results = canAfford( player, amount, null );

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
		return "https://builtbybit.com/creators/edwardbelt.184156/";
	}

	
}
