package tech.mcprison.prison.integration;

import tech.mcprison.prison.internal.Player;

/**
 * An {@link Integration} for an economy plugin.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public interface EconomyIntegration extends Integration {

    /**
     * Returns the player's current balance.
     *
     * @param player The {@link Player}.
     * @return a double.
     */
    double getBalance(Player player);

    /**
     * Sets the player's balance.
     * This will overwrite the previous balance, if that was not clear.
     *
     * @param player The {@link Player}.
     * @param amount The amount.
     */
    void setBalance(Player player, double amount);

    /**
     * Adds to the player's current balance.
     *
     * @param player The {@link Player}.
     * @param amount The amount.
     */
    void addBalance(Player player, double amount);

    /**
     * Removes from the player's current balance.
     *
     * @param player The {@link Player}.
     * @param amount The amount.
     */
    void removeBalance(Player player, double amount);

    /**
     * Returns whether or not the player can afford a transaction.
     *
     * @param player The {@link Player}.
     * @param amount The amount.
     * @return true if the player can afford it, false otherwise.
     */
    boolean canAfford(Player player, double amount);

    @Override default IntegrationType getType() {
        return IntegrationType.ECONOMY;
    }

}
