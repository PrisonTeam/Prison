/*
 * Prison is a Minecraft plugin for the prison game mode.
 * Copyright (C) 2018 The Prison Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package xyz.faizaan.prison.spigot.store.file;

import xyz.faizaan.prison.store.Collection;
import xyz.faizaan.prison.store.Database;

import java.io.File;
import java.util.*;

/**
 * @author Faizaan A. Datoo
 */
public class FileDatabase implements Database {

    private File dbDir;
    private Map<String, xyz.faizaan.prison.store.Collection> collectionMap;

    public FileDatabase(File dbDir) {
        this.dbDir = dbDir;
        this.collectionMap = new HashMap<>();

        File[] collectionDirs = dbDir.listFiles(File::isDirectory);
        if (collectionDirs != null) {
            for (File collDir : collectionDirs) {
                collectionMap.put(collDir.getName(), new FileCollection(collDir));
            }
        }
    }

    @Override public Optional<xyz.faizaan.prison.store.Collection> getCollection(String name) {
        return Optional.ofNullable(collectionMap.get(name));
    }

    @Override public void createCollection(String name) {
        File collDir = new File(dbDir, name);
        if (collDir.exists()) {
            return;
        }
        collDir.mkdir();

        collectionMap.put(name, new FileCollection(collDir));
    }

    @Override public void deleteCollection(String name) {
        File collDir = new File(dbDir, name);
        if (!collDir.exists()) {
            return;
        }

        xyz.faizaan.prison.store.Collection coll = collectionMap.get(name);
        coll.dispose();
        collDir.delete();
        collectionMap.remove(name);
    }

    @Override public List<Collection> getCollections() {
        return new ArrayList<>(collectionMap.values());
    }

    @Override public String getName() {
        return dbDir.getName();
    }

    @Override public void dispose() {
        collectionMap.clear();
    }

}
