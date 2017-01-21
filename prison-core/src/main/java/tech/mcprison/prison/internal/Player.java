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

package tech.mcprison.prison.internal;

import tech.mcprison.prison.internal.scoreboard.Scoreboard;
import tech.mcprison.prison.util.Location;

import java.util.UUID;

/**
 * Represents a player on the Minecraft server.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public interface Player extends CommandSender {

    /**
     * Returns the unique identifier for this player.
     */
    UUID getUUID();

    /**
     * Returns the player's display name (nickname), which may include colors.
     */
    String getDisplayName();

    /**
     * Sets the player's display name (nickname).
     *
     * @param newDisplayName The new display name. May include colors, amp-prefixed.
     */
    void setDisplayName(String newDisplayName);

    /**
     * Adds an {@link ItemStack} to the player's inventory.
     */
    void give(ItemStack itemStack);

    /**
     * Returns the player's current {@link Location}.
     */
    Location getLocation();

    /**
     * Teleports the player to another location.
     *
     * @param location The new {@link Location}.
     */
    void teleport(Location location);

    /**
     * @return Returns true if the player is online, false otherwise.
     */
    boolean isOnline();

    /**
     * Sets the player's visible scoreboard.
     *
     * @param scoreboard The {@link Scoreboard} to show the player.
     */
    void setScoreboard(Scoreboard scoreboard);



    @Override default boolean doesSupportColors() {
        return true;
    }

}
