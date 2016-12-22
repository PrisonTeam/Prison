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
import tech.mcprison.prison.output.Output;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Compiles an object into a format that is ready to be inserted into a database,
 * using the corresponding {@link Blueprint}.
 * <p>
 * This class also decompiles {@link InstantiatedBlueprint} into their objects.
 *
 * @author Faizaan A. Datoo
 * @since 3.0
 */
public class BlueprintCompiler {

    private Blueprint blueprint;
    private Class<?> type;
    private Object object;

    public BlueprintCompiler(Blueprint blueprint, Object object) {
        this.blueprint = blueprint;
        this.type = blueprint.getType();
        this.object = object;
    }

    public BlueprintCompiler(Blueprint blueprint) {
        this.blueprint = blueprint;
        this.type = blueprint.getType();
    }

    /**
     * Compiles an object into a list of columns and values using a blueprint.
     *
     * @return The {@link Map} containing the column-value pairs.
     */
    public Map<String, Object> compile() {

        Map<String, Object> values = new HashMap<>();

        List<Field> fields = findFields();

        for (Field field : fields) {

            // Get what we need from the annotation
            Column columnAnnotation = field.getAnnotation(Column.class);
            String columnName = columnAnnotation.name();
            if (!blueprint.getColumns().contains(columnName)) {
                Output.get()
                    .logWarn("Object attempted to use column that isn't defined in the blueprint.");
                continue; // Skip this one
            }

            // Add the value to the list. Consider this one compiled ;)
            try {
                values.put(columnName, field.get(object));
            } catch (IllegalAccessException e) {
                // ... or not, because some idiot didn't make it public
                // We don't want to change the visibility because if it's not public, it's no longer a bean anyways - let's not encourage bad practice.
                Output.get()
                    .logWarn("Field " + field.getName() + " not public in object " + object, e);
            }

        }

        return values;
    }

    /**
     * Converts an {@link InstantiatedBlueprint} into an object.
     * The instantiated blueprint must already contain the values assigned to each column.
     *
     * @return The object that was decompiled, or null if something went wrong.
     */
    public Object decompile() {
        Constructor constructor = findConstructor();
        if (constructor == null) {
            return null;
        }

        Validate.notNull(blueprint, "Attempted to pass a null blueprint.");
        Validate.isInstanceOf(InstantiatedBlueprint.class, blueprint,
            "Attempted to pass a blueprint not of type InstantiatedBlueprint during decompilation.");

        try {
            // Create the new object with the provided blueprint.
            return constructor.newInstance(blueprint);
        } catch (InstantiationException e) {
            // The blueprint is abstract.
            Output.get().logWarn("The blueprint provided for " + type.getName()
                + " represents an abstract class that cannot be instantiated.", e);
        } catch (IllegalAccessException e) {
            // The constructor we need to access is inaccessible.
            Output.get()
                .logWarn("The blueprint init constructor for " + type.getName() + " is private.",
                    e);
        } catch (InvocationTargetException e) {
            // An error occurred within the type constructor.
            e.printStackTrace();
        }

        return null;

    }

    /**
     * Locates the constructor annotated with @BlueprintInit.
     *
     * @return The correct constructor, or null if there is none annotated.
     */
    private Constructor findConstructor() {
        Constructor[] constructors = type.getConstructors();

        // We're only concerned with the constructor with the @BlueprintInit annotation
        // This is the one we'll use to instantiate an object from decompiled rows
        for (Constructor constructor : constructors) {
            Annotation annotation = constructor.getAnnotation(BlueprintInit.class);
            if (annotation != null) {
                return constructor;
            }
        }

        return null;
    }

    /**
     * Finds all the fields annotated by @Column.
     *
     * @return The list of fields, or an empty list of none were found.
     */
    private List<Field> findFields() {
        List<Field> ret = new ArrayList<>();

        // We're only concerned with @Column-annotated fields
        // We don't know where the other fields go, so we'll ignore them
        for (Field field : object.getClass().getFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                ret.add(field);
            }
        }

        return ret;
    }

}
