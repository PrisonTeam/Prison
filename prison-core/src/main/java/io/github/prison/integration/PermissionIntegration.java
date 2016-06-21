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

package io.github.prison.integration;

import io.github.prison.internal.Player;

/**
 * An integration for a permissions plugin.
 *
 * @author SirFaizdat
 * @since 3.0
 */
public interface PermissionIntegration extends Integration {

    /**
     * Returns the group the player is in, in a specific world on a specific ladder.
     *
     * @param player The {@link Player} to check the group of.
     * @param world  The world to check in (name, case sensitive).
     * @param ladder The ladder to check in.
     * @return The name of the group, or null if none could be found.
     */
    String getGroup(Player player, String world, String ladder);

    /**
     * Returns the group the player is in on a specific ladder.
     * The player's current world is used.
     *
     * @param player The {@link Player} to check the group of.
     * @param ladder The ladder to check in.
     * @return The name of the group, or null if none could be found.
     * @see #getGroup(Player, String, String)
     */
    String getGroup(Player player, String ladder);

    /**
     * Returns the group the player is in.
     * The player's current world and the default ladder are used.
     *
     * @param player The {@link Player} to check the group of.
     * @return The name of the group, or null if none could be found.
     * @see #getGroup(Player, String, String)
     */
    String getGroup(Player player);

    /**
     * Add a group to a player in the specified world.
     *
     * @param player The {@link Player} to change the group of.
     * @param world  The world to change the group in.
     * @param group  The group to change the player to.
     */
    void addGroup(Player player, String world, String group);

    /**
     * Add a group to a player in the player's current world.
     *
     * @param player The {@link Player} to change the group of.
     * @param group  The group to change the player to.
     */
    void addGroup(Player player, String group);

    /**
     * Remove a group from a player in the specified world.
     *
     * @param player The {@link Player} to change the group of.
     * @param world  The world to change the group in.
     * @param group  The group to remove the player from.
     */
    void removeGroup(Player player, String world, String group);

    /**
     * Remove a group to a player in the player's current world.
     *
     * @param player The {@link Player} to change the group of.
     * @param group  The group to remove the player from.
     */
    void removeGroup(Player player, String group);

}
