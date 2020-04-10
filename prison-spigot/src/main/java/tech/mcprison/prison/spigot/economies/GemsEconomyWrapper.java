package tech.mcprison.prison.spigot.economies;

import me.xanium.gemseconomy.api.GemsEconomyAPI;
import me.xanium.gemseconomy.economy.Currency;
import tech.mcprison.prison.internal.Player;

/**
 * 
 * 
 * https://gitlab.com/xanium-s-spigot-plugins/GemsEconomy/-/blob/master/src/main/java/me/xanium/gemseconomy/api/GemsEconomyAPI.java
 * 
 *
 */
public class GemsEconomyWrapper
{
	private GemsEconomyAPI economy;
	
	public GemsEconomyWrapper() {
		super();
		

		this.economy = new GemsEconomyAPI();
	}
	
	
	public boolean isEnabled()
	{
		return economy != null;
	}
	
	public Currency getCurrency( String currencyName ) {
		Currency currency = null;
		if (economy != null && 
				currencyName != null && currencyName.trim().length() > 0) {
			currency = economy.getCurrency( currencyName );
		}
		return currency;
	}
	
	public double getBalance(Player player) {
        return getBalance(player, null);
    }

	public double getBalance(Player player, String currencyName) {
		double results = 0;
		if (economy != null) {
			Currency currency = getCurrency( currencyName );
			if ( currency == null ) {
				results = economy.getBalance(player.getUUID());
			} else {
				results = economy.getBalance(player.getUUID(), currency);
			}
		}
		return results;
	}

	
	public void addBalance(Player player, double amount) {
		addBalance(player, amount, null);
	}
	
	public void addBalance(Player player, double amount, String currencyName) {
		if (economy != null) {
			Currency currency = getCurrency( currencyName );
			if ( currency == null ) {
				economy.deposit(player.getUUID(), amount);
			} else {
				economy.deposit(player.getUUID(), amount, currency);
			}
		}
	}
	
	
	public void withdraw(Player player, double amount) {
		withdraw(player, amount, null);
	}
	
	public void withdraw(Player player, double amount, String currencyName) {
		if (economy != null) {
			Currency currency = getCurrency( currencyName );
			if ( currency == null ) {
				economy.withdraw(player.getUUID(), amount);
			} else {
				economy.withdraw(player.getUUID(), amount, currency);
			}
		}
	}
	
}
