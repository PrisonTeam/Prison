package tech.mcprison.prison.spigot.economies;

import tech.mcprison.prison.integration.EconomyCurrencyIntegration;
import tech.mcprison.prison.internal.Player;

import java.math.BigDecimal;

public class TheNewEconomy extends EconomyCurrencyIntegration {

  private TheNewEconomyWrapper wrapper = null;
  private boolean availableAsAnAlternative = false;

  public TheNewEconomy() {
    super("TheNewEconomy", "TheNewEconomy");
  }

  @Override
  public String getPluginSourceURL() {
    return "https://www.spigotmc.org/resources/the-new-economy.7805/";
  }

  @Override
  public void integrate() {
    if(isRegistered()) {
      this.wrapper = new TheNewEconomyWrapper();
    }
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
            (availableAsAnAlternative ? " (disabled)" : "");
  }

  @Override
  public boolean supportedCurrency(final String currency) {
    return wrapper != null && wrapper.supportedCurrency(currency);
  }

  @Override
  public boolean hasAccount(final Player player) {
    return wrapper.hasAccount(player);
  }

  /**
   * Returns the player's current balance.
   *
   * @param player The {@link Player}.
   *
   * @return a double.
   */
  @Override
  public double getBalance(final Player player) {
    return getBalance(player, wrapper.defaultCurrency());
  }

  @Override
  public double getBalance(final Player player, final String currency) {
    return wrapper.getBalance(player, currency).doubleValue();
  }

  /**
   * Sets the player's balance. This will overwrite the previous balance, if that was not clear.
   *
   * @param player The {@link Player}.
   * @param amount The amount.
   */
  @Override
  public boolean setBalance(final Player player, final double amount) {
    return setBalance(player, amount, wrapper.defaultCurrency());
  }

  @Override
  public boolean setBalance(final Player player, final double amount, final String currency) {
    return wrapper.setBalance(player, currency, new BigDecimal(amount));
  }

  /**
   * Adds to the player's current balance.
   *
   * @param player The {@link Player}.
   * @param amount The amount.
   */
  @Override
  public boolean addBalance(final Player player, final double amount) {
    return addBalance(player, amount, wrapper.defaultCurrency());
  }

  @Override
  public boolean addBalance(final Player player, final double amount, final String currency) {
    return wrapper.addBalance(player, new BigDecimal(amount), currency);
  }

  /**
   * Removes from the player's current balance.
   *
   * @param player The {@link Player}.
   * @param amount The amount.
   */
  @Override
  public boolean removeBalance(final Player player, final double amount) {
    return removeBalance(player, amount, wrapper.defaultCurrency());
  }

  @Override
  public boolean removeBalance(final Player player, final double amount, final String currency) {
    return wrapper.withdraw(player, new BigDecimal(amount), currency);
  }

  /**
   * Returns whether the player can afford a transaction.
   *
   * @param player The {@link Player}.
   * @param amount The amount.
   *
   * @return true if the player can afford it, false otherwise.
   */
  @Override
  public boolean canAfford(final Player player, final double amount) {
    return canAfford(player, amount, wrapper.defaultCurrency());
  }

  @Override
  public boolean canAfford(final Player player, final double amount, final String currency) {
    return wrapper.hasBalance(player, currency, new BigDecimal(amount));
  }
}
