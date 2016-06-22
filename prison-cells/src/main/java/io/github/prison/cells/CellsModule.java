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

package io.github.prison.cells;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.prison.Prison;
import io.github.prison.modules.Module;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SirFaizdat
 * @author Camouflage100
 */
public class CellsModule extends Module {

    public CellsModule(String version) {
        super("Cells", version);
        Prison.getInstance().getModuleManager().registerModule(this);
    }

    private File cellsDirectory;
    private Gson gson;
    private List<Cell> cells;

    @Override
    public void enable() {
        cellsDirectory = new File(Prison.getInstance().getPlatform().getPluginDirectory(), "cells");
        if (!cellsDirectory.exists()) cellsDirectory.mkdir();
        cells = new ArrayList<>();

        initGson();
        loadAll();

        Prison.getInstance().getCommandHandler().registerCommands(new CellsCommands());
    }

    private void initGson() {
        this.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    }

    private void loadAll() {
        File[] files = cellsDirectory.listFiles((dir, name) -> name.endsWith(".cell.json"));
        for (File file : files) {
            try {
                String json = new String(Files.readAllBytes(file.toPath()));
                Cell cell = gson.fromJson(json, Cell.class);
                cells.add(cell);
            } catch (IOException e) {
                Prison.getInstance().getPlatform().log("&cError while loading cell file %s.", file.getName());
                e.printStackTrace();
            }
        }
    }

    public Cell getCell(int cellId) {
        for (Cell cell : cells) if (cell.getCellId() == cellId) return cell;
        return null;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public File getCellsDirectory() {
        return cellsDirectory;
    }

}
