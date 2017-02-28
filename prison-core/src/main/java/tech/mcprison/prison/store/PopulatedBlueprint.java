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

    public PopulatedBlueprint(Map<String, Class<?>> variables, Map<String, Data> dataMap) {
        super(variables);
        this.dataMap = dataMap;
    }

    public PopulatedBlueprint(Map<String, Class<?>> variables) {
        super(variables);
        this.dataMap = new HashMap<>();
    }

    public PopulatedBlueprint() {
        this.dataMap = new HashMap<>();
    }

    public PopulatedBlueprint(Object obj, Map<String, Data> dataMap) {
        super(obj);
        this.dataMap = dataMap;
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
