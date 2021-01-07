package tech.mcprison.prison.spigot.economies;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import me.xanium.gemseconomy.api.GemsEconomyAPI;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.output.Output;

/**
 * <p>This economy wrapper presents some challenges.  The general API for Gems Economy has
 * recently changed in a "difficult" way.  The class and methods remain unchanged, but 
 * there was a class named Currency that has it's package name changed.  That change in
 * package names prevents overloading multiple instances of the the API so they can be
 * selected based up plugin versions.
 * </p>
 * 
 * <p>The way this wrapped deals with this challenge, is by using reflection.  We know the
 * method names and the parameters, but what we can't control, or deal with directly, 
 * is the variable package names associated with the Currency class.  So with reflection,
 * luckily, we can get around that issue by letting reflection get that object and
 * pass it as a parameter so we don't have to have the "correct" class to get this 
 * code compiled.
 * </p>
 * 
 * <p>So far this appears to be working well, but since I have not done much with reflection
 * I'm not so confident that all possible situations have been fully tested.  
 * </p>
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
	
	
	public boolean isEnabled() {
		return economy != null;
	}
	
	public boolean supportedCurrency( String currencyName ) {
		boolean supported = ( getCurrencyRefection( currencyName ) != null );
		
		return supported;
	}
//	
//	public boolean supportedCurrency( String currencyName ) {
//		Currency currency = getCurrency( currencyName );
//		
//		boolean supported = (currency != null);
//		
//		return supported;
//	}
	
	private Object getCurrencyRefection( String currencyName ) {
		Object results = null;
		try {
			Method methodGetCurrency = economy.getClass().getMethod( "getCurrency", String.class );
			methodGetCurrency.setAccessible( true );
			
			results = methodGetCurrency.invoke( economy, currencyName );
		}
		catch ( NoSuchMethodException | SecurityException | IllegalAccessException | 
				IllegalArgumentException | InvocationTargetException e ) {
		
			Output.get().logInfo( "GemsEconomy: Error accessing currency: %s [%s] ", 
					currencyName, e.getMessage() );
		}

		return results;
	}
	
//	public Currency getCurrency( String currencyName ) {
//		Currency currency = null;
//		if (economy != null && 
//				currencyName != null && currencyName.trim().length() > 0) {
//			currency = economy.getCurrency( currencyName );
//		}
//		return currency;
//	}
	
	public double getBalance(Player player) {
        return getBalance(player, null);
    }

	public double getBalance(Player player, String currencyName) {
		double results = 0;
		if (economy != null) {
			if ( currencyName == null ) {
				results = economy.getBalance(player.getUUID());
			} 
			else {
				results = getBalanceReflection( player.getUUID(), currencyName, player.getName() );
			}
		}
		return results;
	}

	/**
	 * <p>This use of reflection targets a method named getBalance and is identified with 
	 * two parameters, instead of the method by the same name that only has one parameter.
	 * Use of getMethods allows selecting the correct method without having to specify the 
	 * parameters (which we "don't" know what they will be at runtime).
	 * </p>
	 * 
	 * @param uuid
	 * @param currencyName
	 * @param playerName
	 * @return
	 */
	private double getBalanceReflection( UUID uuid, String currencyName, String playerName ) {
		double results = 0;
		
		Method methodGEco = null;
		
		Method[] methods = economy.getClass().getMethods();
		for ( Method method : methods ) {
			if ( method.getName().equalsIgnoreCase( "getBalance" ) && 
					method.getParameterCount() == 2 ) {
				methodGEco = method;
				break;
			}
		}
		
		if ( methodGEco != null ) {
			
			try {
				Object currencyObj = getCurrencyRefection( currencyName );
				
				if ( currencyObj != null ) {
					methodGEco.setAccessible( true );

					Object balance = methodGEco.invoke( economy, uuid, currencyObj );
					
					if ( balance != null ) {
						results = (double) balance;
					}
				}
			}
			catch ( IllegalAccessException | IllegalArgumentException | InvocationTargetException | 
								NullPointerException e ) {
				Output.get().logInfo( "&3Error trying to access &7%s &3balance for %s. This is generally related to " +
						"an incorrect player name, or the player has not performed a transaction yet to be " +
						"added to Gems Economy. Have them login and use command &7/bal &3: [%s] ", 
						currencyName, playerName, e.getMessage() );
				
			}
		}
		
		return results;
	}
	
//	public double getBalance(Player player, String currencyName) {
//		double results = 0;
//		if (economy != null) {
//			Currency currency = getCurrency( currencyName );
//			if ( currency == null ) {
//				results = economy.getBalance(player.getUUID());
//			} else {
//				results = economy.getBalance(player.getUUID(), currency);
//			}
//		}
//		return results;
//	}
	
	
	public void addBalance(Player player, double amount) {
		addBalance(player, amount, null);
	}
	
	public void addBalance(Player player, double amount, String currencyName) {
		if (economy != null) {
			if ( currencyName == null ) {
				economy.deposit(player.getUUID(), amount);
			} 
			else {
//				Currency currency = getCurrency( currencyName );
//				economy.deposit(player.getUUID(), amount, currency);
				addBalanceReflection( player.getUUID(), amount, currencyName );
			}
		}
	}
	
	private void addBalanceReflection(UUID uuid, double amount, String currencyName ) {
		
		Method methodGEco = null;
		
		Method[] methods = economy.getClass().getMethods();
		for ( Method method : methods ) {
			if ( method.getName().equalsIgnoreCase( "deposit" ) && 
					method.getParameterCount() == 3 ) {
				methodGEco = method;
				break;
			}
		}
		
		if ( methodGEco != null ) {
			
			try {
				Object currencyObj = getCurrencyRefection( currencyName );
				
				if ( currencyObj != null ) {
					methodGEco.setAccessible( true );
					
					methodGEco.invoke( economy, uuid, amount, currencyObj );
				}
			}
			catch ( IllegalAccessException | IllegalArgumentException | InvocationTargetException e ) {
				Output.get().logInfo( "GemsEconomy: Error depositing currency:  %s &3%s &7[%s] ", 
						Double.toString( amount ), currencyName, e.getMessage() );
			}
		}
		
	}
	
//	public void addBalance(Player player, double amount, String currencyName) {
//		if (economy != null) {
//			Currency currency = getCurrency( currencyName );
//			if ( currency == null ) {
//				economy.deposit(player.getUUID(), amount);
//			} else {
//				economy.deposit(player.getUUID(), amount, currency);
//			}
//		}
//	}
	
	
	public void withdraw(Player player, double amount) {
		withdraw(player, amount, null);
	}
	
	public void withdraw(Player player, double amount, String currencyName) {
		if (economy != null) {
			if ( currencyName == null ) {
				economy.withdraw(player.getUUID(), amount);
			} 
			else {
//				Currency currency = getCurrency( currencyName );
//				economy.withdraw(player.getUUID(), amount, currency);
				withdrawReflection( player.getUUID(), amount, currencyName );
			}
		}
	}
	
	
	private void withdrawReflection(UUID uuid, double amount, String currencyName ) {
		
		Method methodGEco = null;
		
		Method[] methods = economy.getClass().getMethods();
		for ( Method method : methods ) {
			if ( method.getName().equalsIgnoreCase( "withdraw" ) && 
					method.getParameterCount() == 3 ) {
				methodGEco = method;
				break;
			}
		}
		
		if ( methodGEco != null ) {
			
			try {
				Object currencyObj = getCurrencyRefection( currencyName );

				if ( currencyObj != null ) {
					methodGEco.setAccessible( true );
					
					methodGEco.invoke( economy, uuid, amount, currencyObj );
				}
			}
			catch ( IllegalAccessException | IllegalArgumentException | InvocationTargetException e ) {
				Output.get().logInfo( "GemsEconomy: Error withdrawing currency:  %s &3%s &7[%s] ", 
						Double.toString( amount ), currencyName, e.getMessage() );

			}
		}
		
	}
//	public void withdraw(Player player, double amount, String currencyName) {
//		if (economy != null) {
//			Currency currency = getCurrency( currencyName );
//			if ( currency == null ) {
//				economy.withdraw(player.getUUID(), amount);
//			} else {
//				economy.withdraw(player.getUUID(), amount, currency);
//			}
//		}
//	}
	
}
