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

import io.github.prison.internal.ItemStack;
import io.github.prison.internal.Player;
import io.github.prison.util.Block;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SirFaizdat
 */
public class SelectionManager {

    public static final ItemStack SELECTION_TOOL = new ItemStack("&6Selection Wand", 1, Block.BLAZE_ROD);
    private Map<Player, Selection> selectionMap;

    public SelectionManager() {
        this.selectionMap = new HashMap<>();
        new SelectionListener().init();
    }

    /**
     * ... then lobbest thou thy Holy Selection Tool of Antioch towards thy foe, who being naughty in My sight, shall snuff it.
     * @param player The {@link Player} to give the selection tool to
     */
    public void bestowSelectionTool(Player player) {
        player.give(SELECTION_TOOL);
    }

    public Selection getSelection(Player player) {
        if(!selectionMap.containsKey(player)) selectionMap.put(player, new Selection());
        return selectionMap.get(player);
    }

}
