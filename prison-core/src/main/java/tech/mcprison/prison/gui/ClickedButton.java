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

import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.util.BlockType;

import java.util.List;

public class ClickedButton {

    Button originalButton;
    GUI clickedGUI;
    Player clickingPlayer;

    public ClickedButton(Button btn, GUI gui, Player player) {
        originalButton = btn;
        clickedGUI = gui;
        clickingPlayer = player;
    }

    public BlockType getItem() {
        return originalButton.getItem();
    }

    public Action getAction() {
        return originalButton.getAction();
    }

    public String getName() {
        return originalButton.getName();
    }

    public List<String> getLore() {
        return originalButton.getLore();
    }

    public void addLore(String lore) {
        this.getLore().add(lore);
    }

    public boolean isCloseOnClick() {
        return originalButton.isCloseOnClick();
    }

    public GUI getClickedGUI() {
        return clickedGUI;
    }

    public Player getPlayer() {
        return clickingPlayer;
    }

}
