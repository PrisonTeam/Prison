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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Represents a {@link Blueprint} that has been populated by an instantiated object.
 * It maps the variable names to the {@link Data} wrappers.
 *
 * @author Faizaan A. Datoo
 * @since 1.0
 */
public class PopulatedBlueprint extends Blueprint {

    private Map<String, Data> dataMap;

    /**
     * Initialize from an existing {@link Blueprint} object.
     * You would use this if you plan to manually set the data map.
     *
     * @param blueprint The {@link Blueprint} to initialize from.
     */
    public PopulatedBlueprint(Blueprint blueprint) {
        super(blueprint.getVariables());
        dataMap = new HashMap<>();
    }

    /**
     * Initialize from an object, which will be serialized into a PopulatedBlueprint.
     *
     * @param obj The object to be serialized into a PopulatedBlueprint.
     */
    public PopulatedBlueprint(Object obj) {
        super(obj.getClass());
        dataMap = new HashMap<>();

        for (Map.Entry<String, Class<?>> entry : getVariables().entrySet()) {
            String name = entry.getKey();
            Class<?> type = entry.getValue();

            try {
                Field field = obj.getClass().getField(name);
                field.setAccessible(true);

                if (!field.getType().equals(type)) {
                    throw new IllegalStateException("field of incorrect type");
                }

                dataMap.put(name, new Data(field.get(obj)));

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    /**
     * Uses a <b>no-argument constructor</b> in a class to initialize it using the data contained within
     * this populated blueprint. The class must have a no-argument constructor!
     *
     * @param clazz The class type to deserialize this PopulatedBlueprint into.
     * @return The deserialized object, or null if the operation fails.
     */
    public <T> T deserialize(Class<T> clazz) {
        try {
            List<Field> fields = ClassUtil.getAllFields(new LinkedList<>(), clazz);
            T ret = clazz.newInstance();

            for (Field field : fields) {
                String fieldName = field.getName();
                Class<?> type = field.getType();
                Data value = this.getData(fieldName);

                if (value == null) {
                    throw new Exception("missing field value " + fieldName);
                }

                field.set(ret, value.as(type));

            }

            return ret;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Data> getDataMap() {
        return dataMap;
    }

    public Data getData(String name) {
        return dataMap.get(name);
    }

    public void setData(String name, Data data) {
        dataMap.put(name, data);
    }

}
