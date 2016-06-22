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

package io.github.prison;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Loads the configuration from its file.
 *
 * @author SirFaizdat
 * @since 3.0
 */
public class ConfigurationLoader {


    private Class<? extends Configurable> clazz;
    private Configurable config;
    private int targetVersion;
    private File rootFolder;
    private File configFile;
    private String fileName;
    private Gson gson;

    public ConfigurationLoader(File rootFolder, String fileName, Class<? extends Configurable> clazz, int targetVersion) {
        gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        this.clazz = clazz;
        this.targetVersion = targetVersion;
        this.rootFolder = rootFolder;
        this.fileName = fileName;
        this.configFile = new File(rootFolder, fileName);
    }

    /**
     * Load the configuration from the config.json file in the plugin's folder.
     */
    public void loadConfiguration() {
        try {
            // If it doesn't exist, create it
            if (!configFile.exists()) {
                try {
                    config = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                writeConfiguration();
                return;
            }

            // It does exist, so let's load it.
            String json = new String(Files.readAllBytes(Paths.get(configFile.getPath())));

            if (isOutdated(json)) {
                duplicateConfigFile();
                Prison.getInstance().getPlatform().log("&c&lAlert: &7Your " + fileName + " file has been regenerated. I made a backup of your old file, so remember to reconfigure it!");
                return;
            }

            readConfiguration(json);
        } catch (IOException e) {
            Prison.getInstance().getPlatform().log("&c&lError: &7Failed to load the " + fileName + " file.");
            e.printStackTrace();
        }

    }

    public Configurable getConfig() {
        return config;
    }

    // -------------------------------------------- //
    // Configuration IO steps
    // -------------------------------------------- //

    private void writeConfiguration() throws IOException {
        configFile.createNewFile();
        String json = gson.toJson(config);
        Files.write(configFile.toPath(), json.getBytes());
    }

    private void readConfiguration(String json) throws IOException {
        config = gson.fromJson(json, clazz);
    }

    private boolean isOutdated(String json) {
        // Read only the version value
        try {
            JSONObject obj = (JSONObject) new JSONParser().parse(json);
            int version = Math.toIntExact((long) obj.get("version"));
            return version != targetVersion;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void duplicateConfigFile() throws IOException {
        // Rename the old config file to old-config-timestamp.json
        String fileName = "old-" + this.fileName.split("\\.")[0] + "-" + new SimpleDateFormat("yyyyMMdd-hhmm'.json'").format(new Date());
        File newConfigFile = new File(rootFolder, fileName);
        configFile.renameTo(newConfigFile);

        // Now, write a new config file
        configFile = new File(rootFolder, this.fileName);
        try {
            config = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return;
        }
        writeConfiguration();
    }

}
