/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.spigot.economies;

import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import java.math.BigDecimal;
import tech.mcprison.prison.economy.Economy;
import tech.mcprison.prison.internal.Player;

/**
 * Integrates with Essentials Economy.
 *
 * @author Faizaan A. Datoo
 */
public class EssentialsEconomy implements Economy {

  @Override
  public double getBalance(Player player) {
    try {
      return com.earth2me.essentials.api.Economy.getMoneyExact(player.getName())
          .doubleValue();
    } catch (UserDoesNotExistException e) {
      player.sendMessage("You don't exist in the economy plugin.");
      return 0.0;
    }
  }

  @Override
  public void setBalance(Player player, double amount) {
    try {
      com.earth2me.essentials.api.Economy.setMoney(player.getName(), new BigDecimal(amount));
    } catch (UserDoesNotExistException | NoLoanPermittedException e) {
      player.sendMessage("You don't exist in the economy plugin.");
    }
  }

  @Override
  public void addBalance(Player player, double amount) {
    setBalance(player, getBalance(player) + amount);
  }

  @Override
  public void removeBalance(Player player, double amount) {
    setBalance(player, getBalance(player) - amount);
  }

  @Override
  public boolean canAfford(Player player, double amount) {
    return getBalance(player) >= amount;
  }

}
