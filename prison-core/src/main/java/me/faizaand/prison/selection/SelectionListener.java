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

package me.faizaand.prison.selection;

import me.faizaand.prison.Prison;
import me.faizaand.prison.events.EventPriority;
import me.faizaand.prison.events.EventType;
import me.faizaand.prison.internal.GameItemStack;
import me.faizaand.prison.internal.GamePlayer;
import me.faizaand.prison.util.GameLocation;

/**
 * @author Faizaan A. Datoo
 */
public class SelectionListener {

    public SelectionListener() {
        listenForInteract();
    }

    private void listenForInteract() {
        Prison.get().getEventManager().subscribe(EventType.PlayerInteractBlockEvent, objects -> {
            GameItemStack ourItem = (GameItemStack) objects[0];
            GamePlayer player = (GamePlayer) objects[1];
            boolean leftClick = (boolean) objects[2];
            GameLocation clickedLoc = (GameLocation) objects[3];

            GameItemStack toolItem = SelectionManager.SELECTION_TOOL;

            if(!ourItem.equals(toolItem)) return new Object[]{};

            Selection sel = Prison.get().getSelectionManager().getSelection(player);

            if(leftClick) {
                // set the first position on left click
                sel.setMin(clickedLoc);
                Prison.get().getSelectionManager().setSelection(player, sel);
                player.sendMessage("&7First position set to &8" + clickedLoc.toBlockCoordinates());
            } else {
                // Set second position on right click
                sel.setMax(clickedLoc);
                Prison.get().getSelectionManager().setSelection(player, sel);
                player.sendMessage("&7Second position set to &8" + clickedLoc.toBlockCoordinates());
            }

            if(sel.isComplete()) {
                Prison.get().getSelectionManager().getCallbacks().forEach(consumer -> consumer.accept(sel));
            }

            return null;
        }, EventPriority.HIGH);

    }

}
