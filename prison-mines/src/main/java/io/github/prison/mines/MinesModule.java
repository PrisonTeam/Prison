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

package io.github.prison.mines;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.prison.Prison;
import io.github.prison.adapters.LocationAdapter;
import io.github.prison.internal.config.ConfigurationLoader;
import io.github.prison.modules.Module;
import io.github.prison.util.Location;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SirFaizdat
 */
public class MinesModule extends Module {

    private File minesDirectory;
    private List<Mine> mines;
    private Gson gson;
    private Config config;
    private ConfigurationLoader configurationLoader;

    public MinesModule(String version) {
        super("Mines", version);
        Prison.getInstance().getModuleManager().registerModule(this);
    }

    @Override
    public void enable() {
        initGson();

        this.configurationLoader = new ConfigurationLoader(getDataFolder(), "config.json", Config.class, Config.VERSION);

        this.minesDirectory = new File(getDataFolder(), "mines");
        if (!this.minesDirectory.exists()) this.minesDirectory.mkdir();
        loadAll();

        Prison.getInstance().getCommandHandler().registerCommands(new MinesCommand());
    }

    /**
     * Returns the mine that identifies by the specified name.
     *
     * @param name The name of the mine, case-insensitive.
     * @return The {@link Mine}, or null if there's no mine by the specified name.
     */
    public Mine getMine(String name) {
        return mines.stream().filter(mine -> mine.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    /**
     * Saves a mine to disk. The mine file will be saved in the "Prison/Mines/mines" folder, with the name "mineName.mine.json".
     *
     * @param mine The {@link Mine} to save.
     */
    public void saveMine(Mine mine) {
        // Remove the old object and add the new one
        if (getMine(mine.getName()) != null)
            mines.remove(getMine(mine.getName())); // Lists are a bit wonky with mine identification
        mines.add(mine);
        String json = gson.toJson(mine);

        File file = new File(minesDirectory, mine.getName() + ".mine.json");
        try {
            Files.write(file.toPath(), json.getBytes(Charset.forName("UTF-8")));
        } catch (IOException e) {
            Prison.getInstance().getPlatform().log("&cFailed to save mine %s. Stack trace:", mine.getName());
            e.printStackTrace();
        }
    }

    public Config getConfig() {
        return (Config) configurationLoader.getConfig();
    }

    ///
    /// Private methods
    ///

    private void initGson() {
        this.gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().registerTypeAdapter(Location.class, new LocationAdapter()).create();
    }

    private void loadAll() {
        this.mines = new ArrayList<>();
        File[] files = this.minesDirectory.listFiles((dir, name) -> name.endsWith(".mine.json"));

        for (File file : files) {
            try {
                String json = new String(Files.readAllBytes(file.toPath()));
                Mine mine = gson.fromJson(json, Mine.class);
                mines.add(mine);
            } catch (IOException e) {
                Prison.getInstance().getPlatform().log("&cError while loading mine %s. Stack trace;", file.getName());
                e.printStackTrace();
            }
        }
    }

}
