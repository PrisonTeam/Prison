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
import tech.mcprison.prison.util.Text;

import java.util.LinkedList;

/**
 * A chat display is a utility for creating uniform and good-looking
 * in-chat menus and data displays.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class ChatDisplay {

  /*
   * Fields & Constants
   */

    private String title;
    private LinkedList<DisplayComponent> displayComponents;

  /*
   * Constructor
   */

    public ChatDisplay(String title) {
        this.title = Text.titleize(title);
        this.displayComponents = new LinkedList<>();
    }

  /*
   * Methods
   */

    public ChatDisplay addComponent(DisplayComponent component) {
        component.setDisplay(this);
        displayComponents.add(component);
        return this;
    }

    public ChatDisplay addText(String text, Object... args) {
        addComponent(new TextComponent(text, args));
        return this;
    }

    public ChatDisplay addEmptyLine() {
        addComponent(new TextComponent(""));
        return this;
    }

    public void send(CommandSender sender) {
        sender.sendMessage(title);
        for (DisplayComponent component : displayComponents) {
            component.send(sender);
        }
    }
    
    public void toLog(LogLevel logLevel) {
    	Output.get().log( title, logLevel );
        for (DisplayComponent component : displayComponents) {
        	Output.get().log( component.text(), logLevel );
        }
    }
    
    public void sendtoOutputLogInfo() {
        for (DisplayComponent component : displayComponents) {
        	Output.get().logInfo( component.text() );
        }
    }

}
