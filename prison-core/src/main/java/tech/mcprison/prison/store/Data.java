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

import java.util.List;

/**
 * Represents a single piece of data. It can be of any type.
 * This is primarily used in the {@link Blueprint} class to wrap variables
 * into serializable packages, representable as strings.
 *
 * @author Faizaan A. Datoo
 * @since 1.0
 */
public class Data {

    private Object value = null;

    public Object get() {
        return value;
    }

    public void set(Object value) {
        this.value = value;
    }

    public void empty() {
        this.value = null;
    }

    public <T> T as(Class<T> clazz) {
        return clazz.cast(value);
    }

    public double asDouble() {
        return (double) get();
    }

    public float asFloat() {
        return (float) get();
    }

    public int asInt() {
        return (int) get();
    }

    public long asLong() {
        return (long) get();
    }

    public String asString() {
        return (String) get();
    }

    public <T> List<T> asList(Class<T> type) {
        return (List<T>) get();
    }

    @Override public String toString() {
        return value.toString();
    }
}
