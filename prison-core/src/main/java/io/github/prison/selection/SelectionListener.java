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

package io.github.prison.selection;

import com.google.common.eventbus.Subscribe;
import io.github.prison.Prison;
import io.github.prison.internal.events.PlayerInteractEvent;

/**
 * @author SirFaizdat
 */
public class SelectionListener {

    public void init() {
        Prison.getInstance().getEventBus().register(this);
    }

    @Subscribe
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (!e.getItemInHand().equals(SelectionManager.SELECTION_TOOL)) return;
        e.setCanceled(true);

        if (e.getAction() == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
            // Set first position
            Selection sel = Prison.getInstance().getSelectionManager().getSelection(e.getPlayer());
            sel.setMin(e.getClicked());
            Prison.getInstance().getSelectionManager().setSelection(e.getPlayer(), sel);
            e.getPlayer().sendMessage("&7First position set to &8" + e.getClicked().toCoordinates());
        } else if (e.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            // Set second position
            Selection sel = Prison.getInstance().getSelectionManager().getSelection(e.getPlayer());
            sel.setMax(e.getClicked());
            Prison.getInstance().getSelectionManager().setSelection(e.getPlayer(), sel);
            e.getPlayer().sendMessage("&7Second position set to &8" + e.getClicked().toCoordinates());
        }
    }

}
