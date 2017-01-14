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
import tech.mcprison.prison.internal.events.Event;
import tech.mcprison.prison.internal.events.EventListener;
import tech.mcprison.prison.output.Output;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Keeps track of each module and each module's status.
 *
 * @author Faizaan A. Datoo
 * @since API 30
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
        if (getModule(module.getName()) != null) {
            return; // Already added
        }
        modules.add(module);
        enableModule(module);
    }

    private void validateVersion(Module module) {
        if (module.getApiTarget() == Prison.API_LEVEL) {
            return; // Version matches, no need to continue
        }

        setStatus(module.getName(), "&6Version mismatch (update module)");
        Output.get().logWarn(
            "API level mismatch! " + module.getPackageName() + " is on API " + module.getApiTarget()
                + ", while prison-core is on API " + Prison.API_LEVEL
                + ". This may cause problems.");
    }

    /**
     * Enable an already loaded module.
     *
     * @param module The {@link Module} to enable.
     * @return true if the enable succeeded, false otherwise.
     */
    public boolean enableModule(Module module) {
        long startTime = System.currentTimeMillis();
        Output.get().logInfo("%s enable start...", module.getName());

        module.enable();
        module.setEnabled(true);
        validateVersion(module);

        // If the status is still null, then nothing went wrong during the enable.
        if (getStatus(module.getName()) == null || getStatus(module.getName())
            .contains("Disabled")) {
            setStatus(module.getName(), "&aEnabled");
        } else { // Anything else and we assume that the enable failed.
            Output.get().logInfo("%s enable &cfailed&f, in %d milliseconds.", module.getName(),
                (System.currentTimeMillis() - startTime));
            return false;
        }

        Output.get().logInfo("%s enable succeeded, in %d milliseconds.", module.getName(),
            (System.currentTimeMillis() - startTime));
        return true;
    }

    /**
     * Unregister a module. This will disable it and then remove it from the list.
     *
     * @param module The {@link Module} to enable.
     */
    public void unregisterModule(Module module) {
        disableModule(module);
        moduleStates.remove(module.getName());
        modules.remove(getModule(module
            .getName())); // Using the getter so that we know the thing being removed is in the list
    }

    /**
     * Disable an already loaded module.
     *
     * @param module The {@link Module} to disable.
     */
    public void disableModule(Module module) {
        module.disable();
        module.setEnabled(false);
        setStatus(module.getName(), "&cDisabled");
    }

    /**
     * Unregister all modules.
     *
     * @see #unregisterModule(Module)
     */
    public void unregisterAll() {
        modules.forEach(this::disableModule);
        modules.clear();
    }

    /**
     * Returns the {@link Module} with the specified name.
     */
    public Module getModule(String name) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }

    /**
     * Returns the {@link Module} with the specified package name.
     */
    public Module getModuleByPackageName(String name) {
        for (Module module : modules) {
            if (module.getPackageName().equalsIgnoreCase(name)) {
                return module;
            }
        }
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
    private HashMap<Module,Method> methods;

    /**
     * Registers all declared methods within the {@Link EventListener} that have parameters assignable from {@Link Event}.
     * @param module the module
     * @param eventListener the class with event handlers
     */
    public void registerListener(Module module,EventListener eventListener){
        for (Method m : eventListener.getClass().getDeclaredMethods()){
            if (m.getParameterCount() > 1){
                continue; // We can't invoke this one.
            }
            if (m.getParameterTypes()[0].isAssignableFrom(Event.class)){
                methods.put(module,m);
            }
        }
    }

    public void invokeEvent(Event event) throws InvocationTargetException, IllegalAccessException {
        for (Method method : methods.values()){
            if (method.getParameterTypes()[0].isInstance(event)){
                method.invoke(method.getDeclaringClass(),event);
            }
        }
    }
}
