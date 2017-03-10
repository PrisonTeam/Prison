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

package tech.mcprison.prison.output;

import tech.mcprison.prison.chat.FancyMessage;

/**
 * A component that can be attached to a {@link ChatDisplay}.
 * Display components add some text to the display in a certain way.
 *
 * @author Faizaan A. Datoo
 * @since API 0.1
 */
public abstract class DisplayComponent {

  /*
   * Fields & Constants
   */

    protected ChatDisplay display;

  /*
   * Methods
   */

    /**
     * Returns the text that is being appended to the {@link ChatDisplay}.
     * This should return the raw JSON if {@link tech.mcprison.prison.chat.FancyMessage} is involved.
     * To retrieve a raw JSON string, call {@link FancyMessage#toJSONString()}.
     *
     * @return The string of text to add.
     */
    public abstract String text();

    /*
     * Getters & Setters
     */

    void setDisplay(ChatDisplay display) {
        this.display = display;
    }
}
