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

package tech.mcprison.prison.gui;

import tech.mcprison.prison.util.BlockType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A button is an item in an inventory GUI. When it is clicked, an action is performed.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class Button {

    private BlockType item;
    private Action action;
    private String name;
    private List<String> lore;
    private boolean closeOnClick = false;

    /**
     * @param item         The item that this button will show.
     * @param action       The action implementation; this code is run when the button is clicked.
     * @param name         The name of the button (shown as the item name).
     * @param closeOnClick Whether to close the GUI on click.
     */
    public Button(BlockType item, Action action, String name, boolean closeOnClick) {
        this.item = item;
        this.action = action;
        this.name = name;
        this.closeOnClick = closeOnClick;
        this.lore = new ArrayList<>();
    }

    /**
     * @param item         The item that this button will show.
     * @param action       The action implementation; this code is run when the button is clicked.
     * @param name         The name of the button (shown as the item name).
     * @param closeOnClick Whether to close the GUI on click.
     * @param lore         The lore text to put under the button.
     */
    public Button(BlockType item, Action action, String name, boolean closeOnClick,
        String... lore) {
        this.item = item;
        this.action = action;
        this.name = name;
        this.closeOnClick = closeOnClick;
        this.lore = Arrays.asList(lore);
    }

    public BlockType getItem() {
        return item;
    }

    public Action getAction() {
        return action;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

    public void addLore(String lore) {
        this.lore.add(lore);
    }

    public boolean isCloseOnClick() {
        return closeOnClick;
    }

}
