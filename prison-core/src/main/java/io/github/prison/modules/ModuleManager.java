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

package io.github.prison.modules;

import io.github.prison.Prison;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Keeps track of each module and each module's status.
 *
 * @author SirFaizdat
 * @since 3.0
 */
public class ModuleManager {

    private List<Module> modules;
    private Map<String, String> moduleStates;

    public ModuleManager() {
        modules = new ArrayList<>();
        moduleStates = new HashMap<>();
    }

    /**
     * Register a new module.
     */
    public void registerModule(Module module) {
        if (getModule(module.getName()) != null) return; // Already added
        modules.add(module);
        module.enable();
        validateVersion(module);
        // If the status is still null, then nothing went wrong during the enable.
        if (getStatus(module.getName()) == null) setStatus(module.getName(), "&aEnabled");
    }

    private void validateVersion(Module module) {
        if (module.getVersion().equals(Prison.getInstance().getPlatform().getPluginVersion()))
            return; // Version matches, no need to continue

        setStatus(module.getName(), "&6Version mismatch (update module)");
        Prison.getInstance().getPlatform().log("&6Warning: &7Version mismatch! Please update &6%s &7to version &6%s&7.", module.getPackageName(), Prison.getInstance().getPlatform().getPluginVersion());
    }

    /**
     * Unregister a module. This will disable it and then remove it from the list.
     */
    public void unregisterModule(Module module) {
        module.disable();
        setStatus(module.getName(), "&cDisabled (unregistered)");
        modules.remove(getModule(module.getName())); // Using the getter so that we know the thing being removed is in the list
    }

    /**
     * Unregister all modules.
     *
     * @see #unregisterModule(Module)
     */
    public void unregisterAll() {
        modules.forEach(Module::disable);
        modules.clear();
    }

    /**
     * Returns the {@link Module} with the specified name.
     */
    public Module getModule(String name) {
        for (Module module : modules) if (module.getName().equalsIgnoreCase(name)) return module;
        return null;
    }

    /**
     * Returns a list of all modules.
     */
    public List<Module> getModules() {
        return modules;
    }

    /**
     * Returns the status of a module (enabled or error message), in the form of a color-coded string.
     * This is meant to show to users.
     */
    public String getStatus(String moduleName) {
        return moduleStates.get(moduleName);
    }

    /**
     * Set the status of a module.
     *
     * @param moduleName The name of the module.
     * @param newStatus  The module's status. May include color codes, amp-prefixed.
     */
    public void setStatus(String moduleName, String newStatus) {
        moduleStates.put(moduleName, newStatus);
    }

}
