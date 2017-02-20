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

package tech.mcprison.prison.spigot.store;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import tech.mcprison.prison.internal.StorageManager;
import tech.mcprison.prison.store.Blueprint;
import tech.mcprison.prison.store.Data;
import tech.mcprison.prison.store.StorageException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

/**
 * @author Faizaan A. Datoo
 */
public class JsonStorageManager implements StorageManager {

    private File dataFolder;
    private Gson gson;

    public JsonStorageManager(File dataFolder) {
        this.dataFolder = dataFolder;
        this.gson = new GsonBuilder().disableHtmlEscaping().create();
    }

    @Override public boolean store(String id, Blueprint blueprint) {
        try {
            // 'data/<id>.json' is the standard file name for flatfile.
            File file = new File(dataFolder, id + ".json");
            if (!file.exists()) {
                file.createNewFile();
            }

            // Create a JSONObject and loop through, adding all the blueprint data to the JSON string.
            JSONObject obj = new JSONObject();

            for (Map.Entry<String, Pair<Data, Class<?>>> entry : blueprint.getKeyDataStore()
                .entrySet()) {
                // Extract all the variables we need
                String key = entry.getKey();
                Pair<Data, Class<?>> value = entry.getValue();
                Data data = value.getLeft();
                Class<?> clazz = value.getRight();

                // Use GSON to serialize the actual objects- it's much better at this
                String json = gson.toJson(data.as(clazz));

                // Place it in the object
                obj.put(key, json);

            }

            // Write out the JSON that was just generated
            Files.write(file.toPath(), obj.toJSONString().getBytes());
            return true;

        } catch (IOException e) {
            // This is bad... let's just print it and call it a day
            e.printStackTrace();
            return false;
        }
    }

    @Override public <T> T retrieve(String id, Class<T> clazz) {
        try {
            // "data/<id>.json" is the standard file format.
            File file = new File(dataFolder, id + ".json");
            if (!file.exists()) {
                return null;
            }

            // Load up and parse the JSON string.
            String jsonString = new String(Files.readAllBytes(file.toPath()));
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(jsonString);

            // Create a new Blueprint based on the class we're deserializing.
            // The model blueprint is created as an unpopulated blueprint, meaning that the Data
            // wrappers do not yet have values set.
            Blueprint model = new Blueprint(clazz.newInstance());

            // We'll populate the blueprint now.
            for (Map.Entry<String, Pair<Data, Class<?>>> entry : model.getKeyDataStore()
                .entrySet()) {
                // Extract all the variables we need
                String key = entry.getKey();
                Pair<Data, Class<?>> value = entry.getValue();
                Class<?> dataType = value.getRight();

                // Retrieve the raw, GSON-serialized JSON from the save file's object.
                String rawJson = (String) obj.get(key);

                // Create a new data object and set it to the deserialization of the GSON rawJson string.
                Data data = new Data();
                data.set(gson.fromJson(rawJson, dataType));

                // Finally, set it in the model blueprint (i.e. populate it)
                model.set(key, data, dataType);
            }

            // To finish, we call the Blueprint's deserialize method.
            // This creates a new instance of the class and sets the field names
            // to whatever is set in the populated blueprint.
            return model.deserialize(clazz);

        } catch (IOException | ParseException | IllegalAccessException | InstantiationException | StorageException e) {
            // Something ain't right, and no one has any idea what it is.
            // Horrible exception handling, I know. TODO Perhaps this may be revised in the future
            e.printStackTrace();
            return null;
        }
    }

}
