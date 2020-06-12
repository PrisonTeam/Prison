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

package tech.mcprison.prison.selection;

import com.google.common.eventbus.Subscribe;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.events.player.PlayerInteractEvent;

/**
 * @author Faizaan A. Datoo
 */
public class SelectionListener {

    public void init() {
        Prison.get().getEventBus().register(this);
    }

    @Subscribe public void onPlayerInteract(PlayerInteractEvent e) {
        ItemStack ourItem = e.getItemInHand();
        ItemStack toolItem = SelectionManager.SELECTION_TOOL;

        if (!ourItem.equals(toolItem)) {
            return;
        }
        e.setCanceled(true);

        if (e.getAction() == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
            // Set first position
            Selection sel = Prison.get().getSelectionManager().getSelection(e.getPlayer());
            sel.setMin(e.getClicked());
            Prison.get().getSelectionManager().setSelection(e.getPlayer(), sel);
            e.getPlayer()
                .sendMessage("&7First position set to &8" + e.getClicked().toBlockCoordinates());

            checkForEvent(e.getPlayer(), sel);
        } else if (e.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            // Set second position
            Selection sel = Prison.get().getSelectionManager().getSelection(e.getPlayer());
            sel.setMax(e.getClicked());
            Prison.get().getSelectionManager().setSelection(e.getPlayer(), sel);
            e.getPlayer()
                .sendMessage("&7Second position set to &8" + e.getClicked().toBlockCoordinates());

            checkForEvent(e.getPlayer(), sel);
        }
    }

    private void checkForEvent(Player player, Selection sel) {
        if (sel.isComplete()) {
            PrisonAPI.getEventBus().post(new SelectionCompletedEvent(player, sel));
        }
    }

}
