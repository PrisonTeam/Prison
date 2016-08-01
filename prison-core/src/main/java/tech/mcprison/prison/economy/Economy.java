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

package tech.mcprison.prison.economy;

import tech.mcprison.prison.internal.Player;

/**
 * Allows you to interact with the server's economy.
 *
 * @author SirFaizdat
 * @since 3.0
 */
public interface Economy {

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

}
