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

package tech.mcprison.prison.commands;

import java.lang.reflect.Method;

public class RootCommand extends RegisteredCommand {

    private PluginCommand root;

    RootCommand(PluginCommand root, CommandHandler handler ) {
        super(root.getLabel(), handler, null );
        this.root = root;
    }

    public PluginCommand getBukkitCommand() {
        return root;
    }

    @Override 
    protected void set(Object methodInstance, Method method) {
        super.set(methodInstance, method);
        root.setDescription(getDescription());
        root.setUsage(getUsage());
    }
}
