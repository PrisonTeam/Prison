/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017-2020 The Prison Team
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

package tech.mcprison.prison.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utilities for creating various collections.
 *
 * @author Faizaan A. Datoo
 * @since 1.1
 */
public class CollectionUtil {

    /*
     * Constructor
     */

    private CollectionUtil() {
    }

    /*
     * Methods
     */

    /**
     * Creates a map out of an infinite amount of parameters. Every odd parameter (1, 3, 5, etc.) is a
     * key, and every even parameter (2, 4, 6, etc.) is a value.
     *
     * @return A {@link Map}.
     */
    @SuppressWarnings( "unchecked" )
	public static <K, V> Map<K, V> map(K key1, V value1, Object... objects) {
        Map<K, V> ret = new LinkedHashMap<>();

        ret.put(key1, value1);

        Iterator<Object> iter = Arrays.asList(objects).iterator();
        while (iter.hasNext()) {
            K key = (K) iter.next();
            V value = (V) iter.next();
            ret.put(key, value);
        }

        return ret;
    }

}
