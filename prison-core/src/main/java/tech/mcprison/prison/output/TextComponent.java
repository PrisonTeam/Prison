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

package tech.mcprison.prison.output;

import tech.mcprison.prison.internal.CommandSender;

/**
 * A component for boring plain old text.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class TextComponent extends DisplayComponent {

    protected String text;

    public TextComponent(String text, Object... args) {
        this.text = String.format(text, args);
    }

    @Override public String text() {
        return text;
    }

    @Override public void send(CommandSender sender) {
        sender.sendMessage(text());
    }

}
