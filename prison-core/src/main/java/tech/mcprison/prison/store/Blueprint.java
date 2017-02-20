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

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;
import java.util.*;

/**
 * A {@link Blueprint} contains a mapping of every field and its value in the form of a {@link Data} wrapper.
 * It is used to serialize and deserialize classes.
 * <p>
 * To serialize a class, you simply call the {@link Blueprint#Blueprint(Object)} constructor, with the instantiated
 * object to be serialized. This blueprint instance will then be populated, and is ready to be sent on to {@link Storage} for actual storage.
 * <p>
 * To deserialize a Blueprint back into a class, you'll need a populated blueprint instance. Call the {@link Blueprint#deserialize(Class)} instance
 * on it, and you're good to go. Alternatively, you could use the utility method in the {@link Storage} class.
 *
 * @author Faizaan A. Datoo
 * @since 1.0
 */
public class Blueprint {

    private Map<String, Pair<Data, Class<?>>> keyDataStore;

    public Blueprint() {
        keyDataStore = new HashMap<>();
    }

    public Blueprint(Object toSerialize) {
        this();
        List<Field> fields = getAllFields(new LinkedList<>(), toSerialize.getClass());

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.isAnnotationPresent(Exclude.class)) {
                    continue; // Skip excluded fields
                }

                String key = field.getName();
                Data data = new Data();
                if (field.get(toSerialize) != null) {
                    Object val = field.get(toSerialize);
                    data.set(val);
                }
                Class<?> type = field.getType();

                set(key, data, type);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public Blueprint(Map<String, Pair<Data, Class<?>>> keyDataStore) {
        this.keyDataStore = keyDataStore;
    }

    public <T> T deserialize(Class<T> clazz) throws StorageException {
        try {
            T ret = clazz.newInstance();

            for (Field field : getAllFields(new LinkedList<>(), clazz)) {
                field.setAccessible(true);

                if (field.isAnnotationPresent(Exclude.class)) {
                    continue; // Skip it, it's excluded
                }

                Pair<Data, Class<?>> dataPair = keyDataStore.get(field.getName());
                if (dataPair != null) {
                    if (!field.getType().getName().equals(dataPair.getRight().getName())) {
                        throw new StorageException(
                            "Field " + field.getName() + " of type " + clazz.getName()
                                + " must be a " + dataPair.getRight().getName());
                    }

                    field.set(ret, dataPair.getLeft().as(dataPair.getRight()));
                }
            }

            return ret;
        } catch (InstantiationException e) {
            throw new StorageException(
                "Failed to instantiate the deserialized class. Does it have a no-args constructor?",
                e);
        } catch (IllegalAccessException e) {
            throw new StorageException(
                "Failed to access the class to be deserialized. Is it public?", e);
        }
    }

    public Data get(String key) {
        return keyDataStore.get(key).getKey();
    }

    public void set(String key, Data val, Class<?> type) {
        keyDataStore.put(key, new MutablePair<>(val, type));
    }

    public Set<String> getKeys() {
        return keyDataStore.keySet();
    }

    public Collection<Pair<Data, Class<?>>> getValues() {
        return keyDataStore.values();
    }

    public Map<String, Pair<Data, Class<?>>> getKeyDataStore() {
        return keyDataStore;
    }

    private List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null) {
            fields = getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }

}
