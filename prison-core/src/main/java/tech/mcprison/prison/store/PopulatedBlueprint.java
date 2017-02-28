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
import java.util.HashMap;
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

    public PopulatedBlueprint(Blueprint blueprint) {
        super(blueprint.getVariables());
        dataMap = new HashMap<>();
    }

    public PopulatedBlueprint(Object obj) {
        super(obj);
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
