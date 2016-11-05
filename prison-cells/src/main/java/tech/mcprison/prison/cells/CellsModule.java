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

package tech.mcprison.prison.cells;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.adapters.LocationAdapter;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.util.Location;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

/**
 * @author SirFaizdat
 */
public class CellsModule extends Module {

    public CellsModule(String version) {
        super("Cells", version);
    }

    // Fields
    private List<Cell> cells;
    private File cellDirectory;
    private Gson gson;

    // Overridden methods

    @Override public void enable() {

        // Creates a reference to the directory that stores cell data (/Prison/Cells/data)
        cellDirectory = new File(getDataFolder(), "data");
        if (!cellDirectory.exists()) {
            cellDirectory.mkdirs();
        }

        initGson();
        loadCells();

    }

    // Methods

    private void initGson() {
        gson = new GsonBuilder().registerTypeAdapter(Location.class, new LocationAdapter())
            .setPrettyPrinting().disableHtmlEscaping().create();
    }

    private void loadCells() {
        // We're only loading files with the .cell.json extension
        File[] saves = cellDirectory.listFiles((dir, name) -> name.endsWith(".cell.json"));

        // If saves is null, then an internal I/O error has occurred. We can't do much about that here, so just alert the user
        if (saves == null) {
            Prison.getInstance().getPlatform().log(
                "&cFailed to read saves from directory plugins/Prison/Cells/data. This probably means an I/O error has occurred.");
            Prison.getInstance().getPlatform().log("&cCells will not be loaded.");
            return;
        }

        // And now, we load them all
        Arrays.stream(saves).forEach(this::loadCell);
    }

    private void loadCell(File save) {
        try {
            String json = new String(Files.readAllBytes(save.toPath()));

            Cell cell = gson.fromJson(json, Cell.class);
            cells.add(cell);

        } catch (IOException e) {
            Prison.getInstance().getPlatform()
                .log("&cFailed to read cell save file %s.", save.getName());
            e.printStackTrace();
            Prison.getInstance().getPlatform().log("&eThe file will be skipped.");
        }
    }

}
