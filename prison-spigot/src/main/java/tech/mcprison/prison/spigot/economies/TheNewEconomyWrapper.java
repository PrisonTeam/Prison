package tech.mcprison.prison.spigot.economies;

import net.tnemc.core.TNECore;
import net.tnemc.core.api.TNEAPI;
import tech.mcprison.prison.internal.Player;

import java.math.BigDecimal;

public class TheNewEconomyWrapper {

  private TNEAPI economy;

  public TheNewEconomyWrapper() {

    this.economy = new TNEAPI();
  }


  public boolean isEnabled() {
    return economy != null;
  }

  public boolean supportedCurrency(final String currency) {
    return TNECore.eco().currency().findCurrency(currency).isPresent();
  }

  public boolean hasAccount(final Player player) {
    return economy.hasPlayerAccount(player.getUUID());
  }

  public boolean hasBalance(final Player player, final String currency, final BigDecimal amount) {
    return economy.hasHoldings(player.getUUID().toString(),
            player.getLocation().getWorld().getName(),
            currency, amount);
  }

  public boolean setBalance(final Player player, final String currency, final BigDecimal amount) {
    return economy.setHoldings(player.getUUID().toString(),
            player.getLocation().getWorld().getName(),
            currency, amount);
  }

  public BigDecimal getBalance(final Player player, final String currency) {
    return economy.getHoldings(player.getUUID().toString(),
            player.getLocation().getWorld().getName(),
            currency);
  }

  public String defaultCurrency() {
    return economy.getDefaultCurrency().getIdentifier();
  }

  public boolean addBalance(final Player player, final BigDecimal amount, final String currency) {
    return economy.addHoldings(player.getUUID().toString(),
            player.getLocation().getWorld().getName(),
            currency,
            amount,
            "Prison").isSuccessful();
  }

  public boolean withdraw(final Player player, final BigDecimal amount, final String currency) {
    return economy.removeHoldings(player.getUUID().toString(),
            player.getLocation().getWorld().getName(),
            currency,
            amount,
            "Prison").isSuccessful();
  }
}
