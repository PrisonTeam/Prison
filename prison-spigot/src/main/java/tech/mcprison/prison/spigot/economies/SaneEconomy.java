/*
 * Prison is a Minecraft plugin for the prison game mode.
 * Copyright (C) 2018 The Prison Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.spigot.economies;

import org.appledash.saneeconomy.economy.EconomyManager;
import org.appledash.saneeconomy.economy.economable.EconomablePlayer;
import org.bukkit.Bukkit;
import tech.mcprison.prison.integration.EconomyIntegration;
import tech.mcprison.prison.internal.Player;

/**
 * @author Faizaan A. Datoo
 */
public class SaneEconomy implements EconomyIntegration {

    private EconomyManager economyManager;

    public SaneEconomy() {
        org.appledash.saneeconomy.SaneEconomy saneEconomy = (org.appledash.saneeconomy.SaneEconomy) Bukkit
            .getServer().getPluginManager().getPlugin("SaneEconomy");
        if(saneEconomy == null) {
            return;
        }

        economyManager = saneEconomy.getEconomyManager();
    }

    @Override public double getBalance(Player player) {
        return economyManager.getBalance(toEconomablePlayer(player));
    }

    @Override public void setBalance(Player player, double amount) {
        economyManager.setBalance(toEconomablePlayer(player), amount);
    }

    @Override public void addBalance(Player player, double amount) {
        setBalance(player, getBalance(player) + amount);
    }

    @Override public void removeBalance(Player player, double amount) {
        setBalance(player, getBalance(player) - amount);
    }

    @Override public boolean canAfford(Player player, double amount) {
        return getBalance(player) >= amount;
    }

    private EconomablePlayer toEconomablePlayer(Player player) {
        return new EconomablePlayer(Bukkit.getOfflinePlayer(player.getUUID()));
    }

    @Override public String getProviderName() {
        return "SaneEconomy";
    }

    @Override public boolean hasIntegrated() {
        return false;
    }

}
