/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017-2020 The Prison Team
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

package tech.mcprison.prison.internal.events.player;

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.events.Cancelable;
import tech.mcprison.prison.util.Location;

/**
 * Platform-independent event, which is posted when a player clicks something.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class PlayerInteractEvent implements Cancelable {

    private Player player;
    private ItemStack itemInHand;
    private Action action;
    private Location clicked;
    private boolean canceled = false;

    public PlayerInteractEvent(Player player, ItemStack itemInHand, Action action,
        Location clicked) {
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

    @Override public boolean isCanceled() {
        return canceled;
    }

    @Override public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public enum Action {
        LEFT_CLICK_BLOCK, RIGHT_CLICK_BLOCK
    }

}
