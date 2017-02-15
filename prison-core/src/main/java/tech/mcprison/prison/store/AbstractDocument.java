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

import tech.mcprison.prison.Prison;

import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of {@link Document}. In most cases, you can just extend this for all your
 * storage classes.
 *
 * @author Faizaan A. Datoo
 * @since 1.0
 */
public abstract class AbstractDocument implements Document {

    @Override public Map<String, String> getAllAsStrings() {
        Map<String, String> retVal = new HashMap<>();
        for (String key : getKeys()) {
            retVal.put(key, getAsString(key));
        }
        return retVal;
    }

    @Override public String toJson() {
        return GsonSingleton.getInstance().getGson().toJson(this);
    }

    @Override public <T> T fromJson(String json, Class<T> type) {
        return GsonSingleton.getInstance().getGson().fromJson(json, type);
    }

}
