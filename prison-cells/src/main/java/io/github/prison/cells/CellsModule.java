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
import io.github.prison.ConfigurationLoader;
import io.github.prison.Prison;
import io.github.prison.adapters.LocationAdapter;
import io.github.prison.cells.listeners.CellListener;
import io.github.prison.cells.listeners.UserListener;
import io.github.prison.modules.Module;
import io.github.prison.util.Location;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private File usersDirectory;
    private Gson gson;
    private List<Cell> cells;
    private List<CellUser> users;
    private CellCreationQueue queue;
    private ConfigurationLoader messagesLoader;

    @Override
    public void enable() {
        cellsDirectory = new File(getDataFolder(), "cells");
        if (!cellsDirectory.exists()) cellsDirectory.mkdir();

        usersDirectory = new File(getDataFolder(), "users");
        if (!usersDirectory.exists()) usersDirectory.mkdir();

        this.messagesLoader = new ConfigurationLoader(getDataFolder(), "messages.json", Messages.class, Messages.VERSION);
        this.messagesLoader.loadConfiguration();

        cells = new ArrayList<>();
        users = new ArrayList<>();

        initGson();
        loadAllCells();
        loadAllUsers();

        this.queue = new CellCreationQueue(this);

        new UserListener(this).init();
        new CellListener(this).init();

        Prison.getInstance().getCommandHandler().registerCommands(new CellsCommands(this));
    }

    private void initGson() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Location.class, new LocationAdapter())
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
    }

    private void loadAllCells() {
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

    private void loadAllUsers() {
        File[] files = usersDirectory.listFiles((dir, name) -> name.endsWith(".user.json"));
        for (File file : files) {
            try {
                String json = new String(Files.readAllBytes(file.toPath()));
                CellUser cellUser = gson.fromJson(json, CellUser.class);
                users.add(cellUser);
            } catch (IOException e) {
                Prison.getInstance().getPlatform().log("&cError while loading cell user file %s.", file.getName());
                e.printStackTrace();
            }
        }
    }

    public void saveCell(Cell cell) {
        if (getCell(cell.getCellId()) == null) cells.add(cell);

        String json = gson.toJson(cell);
        try {
            Files.write(new File(cellsDirectory, cell.getCellId() + ".cell.json").toPath(), json.getBytes());
        } catch (IOException e) {
            Prison.getInstance().getPlatform().log("&cError while saving cell %s.", cell.getCellId());
            e.printStackTrace();
        }
    }

    public Cell getCell(int cellId) {
        for (Cell cell : cells) if (cell.getCellId() == cellId) return cell;
        return null;
    }

    public Cell getCellByDoor(Location location) {
        for (Cell cell : cells)
            if (location.toCoordinates().equals(cell.getDoorLocation().toCoordinates())) return cell;
        return null;
    }

    public Cell getCellByLocation(Location location) {
        for (Cell cell : cells) if (cell.getBounds().within(location)) return cell;
        return null;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void saveCellUser(CellUser cellUser) {
        if (getUser(cellUser.getUUID()) == null) users.add(cellUser);

        String json = gson.toJson(cellUser);
        try {
            Files.write(new File(usersDirectory, cellUser.getUUID().toString() + ".user.json").toPath(), json.getBytes());
        } catch (IOException e) {
            Prison.getInstance().getPlatform().log("&cError while saving cell user %s.", cellUser.getUUID());
            e.printStackTrace();
        }
    }

    public CellUser getUser(UUID uuid) {
        for (CellUser user : users) if (user.getUUID().compareTo(uuid) == 0) return user;
        return null;
    }

    public List<CellUser> getUsers() {
        return users;
    }

    public int getNextCellId() {
        int highestId = 0;
        for (Cell cell : cells) {
            if (cell.getCellId() > highestId) highestId = cell.getCellId();
        }

        return highestId + 1;
    }

    public CellCreationQueue getQueue() {
        return queue;
    }

    public Messages getMessages() {
        return (Messages) messagesLoader.getConfig();
    }

    public File getCellsDirectory() {
        return cellsDirectory;
    }

    public File getUsersDirectory() {
        return usersDirectory;
    }
}
