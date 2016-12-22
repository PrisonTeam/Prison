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

package tech.mcprison.prison.store;

import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.Map;

/**
 * A blueprint with the columns set to the values.
 * This is useful in the decompilation process.
 *
 * @author Faizaan A. Datoo
 * @since 3.0
 */
public class InstantiatedBlueprint extends Blueprint {

    private Blueprint master;
    private Map<String, Object> values;

    public InstantiatedBlueprint(Blueprint master) {
        super(master.getType());
        this.master = master;
        this.values = new HashMap<>();
    }

    public void addValue(String column, Object obj) {
        values.put(column, obj);
    }

    public Object getValue(String column) {
        return values.get(column);
    }

    public Map<String, Object> getValues() {
        return values;
    }

    @Override public String getName() {
        return master.getName();
    }

}
