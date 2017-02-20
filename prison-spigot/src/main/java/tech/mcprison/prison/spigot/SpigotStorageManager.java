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

package tech.mcprison.prison.spigot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.StorageManager;
import tech.mcprison.prison.store.Blueprint;
import tech.mcprison.prison.store.Data;
import tech.mcprison.prison.store.StorageException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author Faizaan A. Datoo
 */
public class SpigotStorageManager implements StorageManager {

    private File dataFolder;
    private Gson gson;

    public SpigotStorageManager(File dataFolder) {
        this.dataFolder = dataFolder;
        this.gson = new GsonBuilder().disableHtmlEscaping().create();
    }

    @Override public CompletableFuture<Boolean> store(String id, Blueprint blueprint) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                File file = new File(dataFolder, id + ".json");
                if (!file.exists()) {
                    file.createNewFile();
                }
                JSONObject obj = new JSONObject();
                for (Map.Entry<String, Pair<Data, Class<?>>> entry : blueprint.getKeyDataStore()
                    .entrySet()) {
                    obj.put(entry.getKey(),
                        gson.toJson(entry.getValue().getLeft().as(entry.getValue().getRight())));
                }
                Files.write(file.toPath(), obj.toJSONString().getBytes());

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }, Prison.get().getPlatform().getAsyncExecutor());
    }

    @Override public <T> CompletableFuture<T> retrieve(String id, Class<T> clazz) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                File file = new File(dataFolder, id);
                if (!file.exists()) {
                    return null;
                }

                String jsonString = new String(Files.readAllBytes(file.toPath()));
                JSONParser parser = new JSONParser();
                JSONObject obj = (JSONObject) parser.parse(jsonString);

                Blueprint model = new Blueprint(clazz.newInstance());
                for (Map.Entry<String, Pair<Data, Class<?>>> entry : model.getKeyDataStore()
                    .entrySet()) {
                    String raw = (String) obj.get(entry.getKey());
                    Data data = new Data();
                    data.set(gson.fromJson(raw, entry.getValue().getRight()));
                    model.set(entry.getKey(), data, entry.getValue().getRight());
                }

                return model.deserialize(clazz);

            } catch (IOException | ParseException | IllegalAccessException | InstantiationException | StorageException e) {
                e.printStackTrace();
                return null;
            }
        }, Prison.get().getPlatform().getAsyncExecutor());
    }

}
