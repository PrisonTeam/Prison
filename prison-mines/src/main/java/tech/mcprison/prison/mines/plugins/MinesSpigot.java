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

package tech.mcprison.prison.mines.plugins;

import org.bukkit.plugin.java.JavaPlugin;
import tech.mcprison.prison.mines.MinesModule;

/**
 * If this module is installed on a Spigot server, this class will be ran.
 * All we do here is invoke the MinesModule constructor, which does the rest of the work.
 *
 * @author SirFaizdat
 */
public class MinesSpigot extends JavaPlugin {

    @Override public void onEnable() {
        new MinesModule(getDescription().getVersion());
    }


}
