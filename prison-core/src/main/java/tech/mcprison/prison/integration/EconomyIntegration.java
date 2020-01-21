package tech.mcprison.prison.integration;

import tech.mcprison.prison.internal.Player;

/**
 * An {@link Integration} for an economy plugin.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public abstract class EconomyIntegration 
	extends IntegrationCore {

	public EconomyIntegration( String keyName, String providerName ) {
		super( keyName, providerName, IntegrationType.ECONOMY );
		
	}
	
    /**
     * Returns the player's current balance.
     *
     * @param player The {@link Player}.
     * @return a double.
     */
    public abstract double getBalance(Player player);

    /**
     * Sets the player's balance.
     * This will overwrite the previous balance, if that was not clear.
     *
     * @param player The {@link Player}.
     * @param amount The amount.
     */
    public abstract void setBalance(Player player, double amount);

    /**
     * Adds to the player's current balance.
     *
     * @param player The {@link Player}.
     * @param amount The amount.
     */
    public abstract void addBalance(Player player, double amount);

    /**
     * Removes from the player's current balance.
     *
     * @param player The {@link Player}.
     * @param amount The amount.
     */
    public abstract void removeBalance(Player player, double amount);

    /**
     * Returns whether or not the player can afford a transaction.
     *
     * @param player The {@link Player}.
     * @param amount The amount.
     * @return true if the player can afford it, false otherwise.
     */
    public abstract boolean canAfford(Player player, double amount);

}
