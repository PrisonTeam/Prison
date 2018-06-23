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

import me.faizaand.prison.internal.GameItemStack;
import me.faizaand.prison.internal.GamePlayer;
import me.faizaand.prison.util.BlockType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Faizaan A. Datoo
 */
public class SelectionManager {

    public static final GameItemStack SELECTION_TOOL =
            new GameItemStack("&6Selection Wand", 1, BlockType.BLAZE_ROD, "&7Corner 1 - Left click",
                    "&7Corner 2 - Right click");
    private Map<String, Selection> selectionMap;
    private List<Consumer<Selection>> callbacks;

    public SelectionManager() {
        this.selectionMap = new HashMap<>();
        this.callbacks = new ArrayList<>();
        new SelectionListener();
    }

    /**
     * ... then lobbest thou thy Holy Selection Tool of Antioch towards thy foe, who being naughty in
     * My sight, shall snuff it.
     *
     * @param player The {@link GamePlayer} to give the selection tool to
     */
    public void bestowSelectionTool(GamePlayer player) {
        player.give(SELECTION_TOOL);
    }

    public Selection getSelection(GamePlayer player) {
        if (!selectionMap.containsKey(player.getName())) {
            Selection sel = new Selection();
            sel.setOwner(player);
            selectionMap.put(player.getName(), sel);
        }
        return selectionMap.get(player.getName());
    }

    public void setSelection(GamePlayer player, Selection selection) {
        selectionMap.put(player.getName(), selection);
    }

    public void addCallback(Consumer<Selection> consumer) {
        callbacks.add(consumer);
    }

    public List<Consumer<Selection>> getCallbacks() {
        return callbacks;
    }

}
