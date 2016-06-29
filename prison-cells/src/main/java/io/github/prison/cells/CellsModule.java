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
 */
public class CellsModule extends Module {

    private List<CellUser> users;
    private List<Cell> cells;
    private File userDirectory;
    private File cellDirectory;
    private Gson gson;
    private CellCreator cellCreator;
    private ConfigurationLoader messagesLoader;

    public CellsModule(String version) {
        super("Cells", version);
        Prison.getInstance().getModuleManager().registerModule(this);
    }

    @Override
    public void enable() {
        initGson();
        initMessages();

        this.users = new ArrayList<>();
        this.userDirectory = new File(getDataFolder(), "users");
        loadUsers();
        checkOnlineUsers();

        this.cells = new ArrayList<>();
        this.cellDirectory = new File(getDataFolder(), "cells");
        loadCells();

        this.cellCreator = new CellCreator(this);

        new UserListener(this).init();
        new CellListener(this).init();
        Prison.getInstance().getCommandHandler().registerCommands(new CellCommand(this));
    }

    /**
     * Retrieve a user by its UUID.
     *
     * @param uuid The user's UUID.
     * @return the {@link CellUser}, or null if one couldn't be found by the specified UUID.
     */
    public CellUser getUser(UUID uuid) {
        return users.stream().filter(cellUser -> cellUser.getUUID() == uuid).findFirst().orElse(null);
    }

    /**
     * Stores the edited user object in the list, and saves it to a file.
     *
     * @param user the {@link CellUser}.
     */
    public void saveUser(CellUser user) {
        // Replace the old object and insert the new one
        if (getUser(user.getUUID()) != null) users.remove(getUser(user.getUUID()));
        users.add(user);

        File file = new File(userDirectory, user.getUUID().toString() + ".user.json");
        try {
            Files.write(file.toPath(), gson.toJson(user).getBytes());
        } catch (IOException e) {
            Prison.getInstance().getPlatform().log("&cThe user file %s could not be saved. Here's the stack trace:", file.getName());
            e.printStackTrace();
        }
    }

    /**
     * Retrieve a cell by its ID.
     *
     * @param id The ID of the cell.
     * @return the {@link Cell}, or null if one couldn't be found with the specified ID.
     */
    public Cell getCell(int id) {
        return cells.stream().filter(cell -> cell.getId() == id).findFirst().orElse(null);
    }

    /**
     * Retrieve a cell by its door location.
     * The door location must be the top half of the door.
     *
     * @param loc The location of the door.
     * @return The {@link Cell} with this door location, or null if there isn't one.
     */
    public Cell getCellByDoorLocation(Location loc) {
        return cells.stream().filter(cell -> cell.getDoorLocation().equals(loc)).findFirst().orElse(null);
    }

    /**
     * Retrieve a cell by a location within its bounds.
     * @param loc The location of the block to check.
     * @return The {@link Cell} with this location within, or null if there isn't one.
     * @see io.github.prison.util.Bounds#within(Location)
     */
    public Cell getCellByLocationWithin(Location loc) {
        return cells.stream().filter(cell -> cell.getBounds().within(loc)).findFirst().orElse(null);
    }

    /**
     * Stores the edited cell object in the list, and saves it to a file.
     *
     * @param cell the {@link Cell}.
     */
    public void saveCell(Cell cell) {
        // Replace the old object and insert the new one
        if (getCell(cell.getId()) != null) cells.remove(getCell(cell.getId()));
        cells.add(cell);

        File file = new File(cellDirectory, cell.getId() + ".cell.json");
        try {
            Files.write(file.toPath(), gson.toJson(cell).getBytes());
        } catch (IOException e) {
            Prison.getInstance().getPlatform().log("&cThe cell file %s could not be saved. Here's the stack trace:", file.getName());
            e.printStackTrace();
        }
    }

    /**
     * Returns the next available cell ID.
     *
     * @return the integer.
     */
    public int getNextCellId() {
        return cells.size() + 1;
    }

    public CellCreator getCellCreator() {
        return cellCreator;
    }

    public Messages getMessages() {
        return (Messages) messagesLoader.getConfig();
    }

    ///
    /// Private Methods
    /// Lasciate ogne speranza, voi ch'intrate!
    ///

    private void initGson() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .registerTypeAdapter(Location.class, new LocationAdapter())
                .create();
    }

    private void initMessages() {
        this.messagesLoader = new ConfigurationLoader(getDataFolder(), "messages.json", Messages.class, Messages.VERSION);
        this.messagesLoader.loadConfiguration();
    }

    private void loadUsers() {
        if (!userDirectory.exists()) {
            userDirectory.mkdir();
            return; // No need to continue, we already know there are no files
        }

        File[] userFiles = userDirectory.listFiles((dir, name) -> name.endsWith(".user.json"));
        for (File file : userFiles) {
            try {
                String json = new String(Files.readAllBytes(file.toPath()));
                CellUser user = gson.fromJson(json, CellUser.class);
                users.add(user);
            } catch (IOException e) {
                Prison.getInstance().getPlatform().log("&cThe user file %s could not be loaded. Here's the stack trace:", file.getName());
                e.printStackTrace();
            }
        }
    }

    // Reload safety, if anyone online doesn't have a user file, it's created.
    private void checkOnlineUsers() {
        Prison.getInstance().getPlatform().getOnlinePlayers().stream()
                .filter(player -> getUser(player.getUUID()) == null)
                .forEach(player -> saveUser(new CellUser(player.getUUID())));
    }

    private void loadCells() {
        if (!cellDirectory.exists()) {
            cellDirectory.mkdir();
            return; // No need to continue, if the directory didn't exist then there are no files (obviously)
        }

        File[] cellFiles = cellDirectory.listFiles((dir, name) -> name.endsWith(".cell.json"));
        for (File file : cellFiles) {
            try {
                String json = new String(Files.readAllBytes(file.toPath()));
                Cell cell = gson.fromJson(json, Cell.class);
                cells.add(cell);
            } catch (IOException e) {
                Prison.getInstance().getPlatform().log("&cThe cell file %s could not be loaded. Here's the stack trace:", file.getName());
                e.printStackTrace();
            }
        }
    }

}
