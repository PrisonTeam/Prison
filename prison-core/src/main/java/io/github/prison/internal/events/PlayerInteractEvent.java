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

package io.github.prison.internal.events;

import io.github.prison.internal.ItemStack;
import io.github.prison.internal.Player;
import io.github.prison.util.Location;

/**
 * Platform-independent event, which is posted when a player clicks something.
 *
 * @author SirFaizdat
 * @since 3.0
 */
public class PlayerInteractEvent {

    private Player player;
    private ItemStack itemInHand;
    private Action action;
    private Location clicked;

    public PlayerInteractEvent(Player player, ItemStack itemInHand, Action action, Location clicked) {
        this.player = player;
        this.itemInHand = itemInHand;
        this.action = action;
        this.clicked = clicked;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getItemInHand() {
        return itemInHand;
    }

    public Action getAction() {
        return action;
    }

    public Location getClicked() {
        return clicked;
    }

    public enum Action {
        LEFT_CLICK_BLOCK,
        RIGHT_CLICK_BLOCK
    }

}
