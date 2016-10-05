/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2016 The Prison Team
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

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import tech.mcprison.prison.economy.Economy;
import tech.mcprison.prison.platform.Player;

/**
 * @author SirFaizdat
 */
public class VaultEconomy implements Economy {

    net.milkbowl.vault.economy.Economy economy = null;

    public VaultEconomy() {
        RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> economyProvider =
            Bukkit.getServer().getServicesManager()
                .getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
    }

    @Override public double getBalance(Player player) {
        if (economy == null) {
            return 0;
        }
        return economy.getBalance(player.getName());
    }

    @Override public void setBalance(Player player, double amount) {
        if (economy == null) {
            return;
        }
        economy.bankWithdraw(player.getName(), getBalance(player));
        economy.bankDeposit(player.getName(), amount);
    }

    @Override public void addBalance(Player player, double amount) {
        if (economy == null) {
            return;
        }
        economy.bankDeposit(player.getName(), amount);
    }

    @Override public void removeBalance(Player player, double amount) {
        if (economy == null) {
            return;
        }
        economy.bankWithdraw(player.getName(), amount);
    }

    @Override public boolean canAfford(Player player, double amount) {
        if (economy == null) {
            return false;
        }
        return economy.bankHas(player.getName(), amount).transactionSuccess();
    }

}
