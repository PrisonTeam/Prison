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

    public Blueprint(Map<String, Class<?>> variables) {
        this.variables = variables;
    }

    public Blueprint() {
        this.variables = new HashMap<>();
    }

    public Blueprint(Object obj) {
        this.variables = new HashMap<>();

        List<Field> fields = getAllFields(new LinkedList<>(), obj.getClass());
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            Class<?> type = field.getType();
            variables.put(name, type);
        }
    }

    public PopulatedBlueprint deserialize(Object obj) {
        PopulatedBlueprint blueprint = new PopulatedBlueprint(variables);

        for (Map.Entry<String, Class<?>> entry : variables.entrySet()) {
            String name = entry.getKey();
            Class<?> type = entry.getValue();
            Data value = null;

            try {
                Field objField = obj.getClass().getField(name);
                objField.setAccessible(true);
                value = new Data(objField.get(obj));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }

            blueprint.setData(name, value);
        }

        return blueprint;

    }

    public Map<String, Class<?>> getVariables() {
        return variables;
    }

    private List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null) {
            fields = getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }

}
