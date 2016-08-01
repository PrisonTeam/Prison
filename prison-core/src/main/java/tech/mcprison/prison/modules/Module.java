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

package tech.mcprison.prison.modules;

import tech.mcprison.prison.Prison;

import java.io.File;

/**
 * Represents a module, which is a part of Prison that can be enabled and
 * disabled independently from the rest of the modules.
 *
 * @author SirFaizdat
 * @since 3.0
 */
public abstract class Module {

    private String name, version;
    private File dataFolder;
    private boolean enabled;

    public Module(String name, String version) {
        this.name = name;
        this.version = version;
        this.dataFolder = new File(Prison.getInstance().getPlatform().getPluginDirectory(), name);
        if (!this.dataFolder.exists())
            this.dataFolder.mkdir();
    }

    /**
     * Called when the module is to be enabled.
     */
    public void enable() {
    }

    /**
     * Called when a module is to be disabled.
     */
    public void disable() {
    }

    /**
     * Called when the plugin reloads.
     */
    public void reload() {
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    /**
     * The package name is the word "Prison" followed by the module name.
     */
    public String getPackageName() {
        return "Prison" + name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Returns this module's data folder, where all data can be stored.
     * It is located in the Prison data folder, and has the name of the module.
     * It is automatically generated.
     *
     * @return The {@link File} representing the data folder.
     */
    public File getDataFolder() {
        return dataFolder;
    }
}
