package tech.mcprison.prison.spigot.economies;

import su.nightexpress.coinsengine.api.CoinsEngineAPI;
import su.nightexpress.coinsengine.api.currency.Currency;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.game.SpigotPlayer;

public class CoinsEngineEconomyWrapper {

	private CoinsEngineAPI economy;
	
	public CoinsEngineEconomyWrapper() {
		super();
		
		
		try {
			this.economy = new CoinsEngineAPI();
		} 
		catch (Exception e) {
			// If CoinsEngineAPI does not exist, then ignore this exception:
		}
		
		
	}
	
	
	public boolean isEnabled() {
		return economy != null;
	}
	
	public boolean supportedCurrency( String currencyName ) {
		boolean supported = ( getCurrency( currencyName ) != null );
		
		return supported;
	}
	
	
	public Currency getCurrency( String currencyNamme ) {
		return CoinsEngineAPI.getCurrency( currencyNamme );
	}

	
	public double getBalance(Player player) {

		Output.get().logWarn( "CoinsEngine getBalance() - Fail: MUST include a currencyName.");
        return getBalance(player, null, false);
    }

	public double getBalance(Player player, String currencyName, boolean quite ) {
		double results = 0;
		if (economy != null && player instanceof SpigotPlayer ) {
			
			org.bukkit.entity.Player sPlayer = ((SpigotPlayer) player).getWrapper();
			
			Currency currency = getCurrency( currencyName );
			if ( currency != null && sPlayer != null) {

				results = CoinsEngineAPI.getBalance( sPlayer, currency );
			}
		}
		return results;
	}
	
	public void addBalance(Player player, double amount) {

		Output.get().logWarn( "CoinsEngine addBalance() - Fail: MUST include a currencyName.");
		addBalance(player, amount, null);
	}
	
	public void addBalance(Player player, double amount, String currencyName) {
		
		if (economy != null && player instanceof SpigotPlayer ) {
			
			org.bukkit.entity.Player sPlayer = ((SpigotPlayer) player).getWrapper();
			
			Currency currency = getCurrency( currencyName );
			if ( currency != null && sPlayer != null) {

				CoinsEngineAPI.addBalance( sPlayer, currency, amount );
			}
		}
	}
	
	
	public void withdraw(Player player, double amount) {

		Output.get().logWarn( "CoinsEngine withdraw() - Fail: MUST include a currencyName.");
		withdraw(player, amount, null);
	}
	
	public void withdraw(Player player, double amount, String currencyName) {
		
		
		if (economy != null && player instanceof SpigotPlayer ) {
			
			org.bukkit.entity.Player sPlayer = ((SpigotPlayer) player).getWrapper();
			
			Currency currency = getCurrency( currencyName );
			if ( currency != null && sPlayer != null) {

				CoinsEngineAPI.removeBalance( sPlayer, currency, amount );
			}
		}
	
	}
	
}
