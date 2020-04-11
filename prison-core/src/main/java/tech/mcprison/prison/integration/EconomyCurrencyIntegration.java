package tech.mcprison.prison.integration;

import java.util.TreeMap;

import tech.mcprison.prison.internal.Player;

public abstract class EconomyCurrencyIntegration
		extends EconomyIntegration
{
	private TreeMap<String, Boolean> supportedCurrencies;

	public EconomyCurrencyIntegration( String keyName, String providerName ) {
		super( keyName, providerName );
		
		this.supportedCurrencies = new TreeMap<>();
	}
	
	public boolean hasCurrency( String currency ) {
		
		if ( !getSupportedCurrencies().containsKey( currency ) ) {
			boolean valid = supportedCurrency( currency );
			getSupportedCurrencies().put( currency, Boolean.valueOf( valid ) );
		}
		
		return getSupportedCurrencies().get( currency ).booleanValue();
	}
	
	public abstract boolean supportedCurrency( String currency );
	
	public abstract double getBalance( Player player, String currency );

	public abstract void setBalance( Player player, double amount, String currency );

	public abstract void addBalance( Player player, double amount, String currency );

	public abstract void removeBalance( Player player, double amount, String currency );

	public abstract boolean canAfford( Player player, double amount, String currency );
	

	public TreeMap<String, Boolean> getSupportedCurrencies() {
		return supportedCurrencies;
	}
	public void setSupportedCurrencies( TreeMap<String, Boolean> supportedCurrencies ) {
		this.supportedCurrencies = supportedCurrencies;
	}

}
