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

package tech.mcprison.prison.store;

import tech.mcprison.prison.util.ClassUtil;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Holds a collection of variable names and types that can be used to construct
 * the object it represents.
 *
 * @author Faizaan A. Datoo
 * @since 1.0
 */
public class Blueprint {

    private Map<String, Class<?>> variables;

    /**
     * Create a new blueprint with a pre-defined set of variables.
     *
     * @param variables A map containing the name of each variable and its type.
     */
    public Blueprint(Map<String, Class<?>> variables) {
        this.variables = variables;
    }

    /**
     * Creates a new blueprint with an empty set of variables.
     */
    public Blueprint() {
        this.variables = new HashMap<>();
    }

    /**
     * Serializes a Class to a Blueprint.
     *
     * @param clazz The class to serialize.
     */
    public Blueprint(Class<?> clazz) {
        this.variables = new HashMap<>();

        List<Field> fields = ClassUtil.getAllFields(new LinkedList<>(), clazz);
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            Class<?> type = field.getType();
            variables.put(name, type);
        }
    }

    public Map<String, Class<?>> getVariables() {
        return variables;
    }

}
