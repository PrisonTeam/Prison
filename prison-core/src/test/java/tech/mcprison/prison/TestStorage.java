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

package tech.mcprison.prison;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import tech.mcprison.prison.store.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Faizaan A. Datoo
 */
public class TestStorage implements Storage {

    private File storageFolder;

    public TestStorage(File storageFolder) {
        this.storageFolder = storageFolder;
        this.storageFolder.mkdirs();
    }

    @Override public void write(String key, Object obj) {
        File file = new File(getCollectionFolder(obj.getClass()), key + ".json");
        PopulatedBlueprint blueprint = new PopulatedBlueprint(obj);
        JSONObject object = new JSONObject();

        for (Map.Entry<String, Data> dataEntry : blueprint.getDataMap().entrySet()) {
            String name = dataEntry.getKey();
            Data val = dataEntry.getValue();

            object.put(name, val.toJson());
        }

        try {
            file.createNewFile();
            Files.write(file.toPath(), object.toJSONString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public <T> T read(String key, Class<T> type) {
        File file = new File(getCollectionFolder(type), key + ".json");
        return read(file, type);
    }

    private <T> T read(File file, Class<T> type) {
        Blueprint blueprint = new Blueprint(type);

        JSONObject object;
        try {
            String json = new String(Files.readAllBytes(file.toPath()));
            object = (JSONObject) new JSONParser().parse(json);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }

        PopulatedBlueprint finalBlueprint = new PopulatedBlueprint(blueprint);
        for (Map.Entry<String, Class<?>> entry : blueprint.getVariables().entrySet()) {
            String name = entry.getKey();
            Class<?> varType = entry.getValue();

            String json = (String) object.get(name);
            Data data = new Data(GsonSingleton.getInstance().getGson().fromJson(json, varType));

            finalBlueprint.setData(name, data);
        }

        return finalBlueprint.deserialize(type);
    }

    @Override public <T> List<T> readAll(Class<T> type) {
        List<T> ret = new ArrayList<>();

        File[] files = getCollectionFolder(type).listFiles((dir, name) -> name.endsWith(".json"));

        if (files != null) {
            for (File file : files) {
                ret.add(read(file, type));
            }
        }

        return ret;
    }

    private String getCollectionName(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    private File getCollectionFolder(Class<?> clazz) {
        File file = new File(storageFolder, getCollectionName(clazz));
        file.mkdirs();
        return file;
    }

}
