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
import java.util.ArrayList;
import java.util.List;

/**
 * @author Faizaan A. Datoo
 */
public class CellsModule extends Module {

    private List<Cell> cells;
    private Gson gson;

    public CellsModule(String version) {
        super("Cells", version, 30);
        Prison.get().getModuleManager().registerModule(this);
    }

    @Override public void enable() {
        this.initGson();
        this.cells = new ArrayList<>();
    }

    /*
     * Public methods
     */

    /**
     * Loads a cell from a .cell.json file and returns the {@link Cell} object.
     *
     * @param file The {@link File} to load from.
     * @return The {@link Cell} from the file.
     * @throws IOException If the file could not be found or deserialized, or other I/O errors occur.
     */
    public Cell loadCell(File file) throws IOException {
        String json = new String(Files.readAllBytes(file.toPath()));
        Cell cell = gson.fromJson(json, Cell.class);
        return cell;
    }

    /*
     * Private methods
     */

    private void initGson() {
        gson = new GsonBuilder().registerTypeAdapter(Location.class, new LocationAdapter())
            .disableHtmlEscaping().setPrettyPrinting().create();
    }

    /*
     * Getters & setters
     */

    public List<Cell> getCells() {
        return cells;
    }

}
