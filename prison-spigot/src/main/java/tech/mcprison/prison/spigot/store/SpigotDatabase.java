package tech.mcprison.prison.spigot.store;

import tech.mcprison.prison.store.Collection;
import tech.mcprison.prison.store.Database;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Faizaan A. Datoo
 */
public class SpigotDatabase implements Database {

    private File dbDir;
    private Map<String, Collection> collectionMap;

    public SpigotDatabase(File dbDir) {
        this.dbDir = dbDir;

        File[] collectionDirs = dbDir.listFiles(File::isDirectory);
        if(collectionDirs != null) {
            for(File collDir : collectionDirs) {
                collectionMap.put(collDir.getName(), new SpigotCollection(collDir));
            }
        }
    }

    @Override public Optional<Collection> getCollection(String name) {
        return Optional.ofNullable(collectionMap.get(name));
    }

    @Override public void createCollection(String name) {
        File collDir = new File(dbDir, name);
        if(collDir.exists()) {
            return;
        }

        collectionMap.put(name, new SpigotCollection(collDir));
    }

    @Override public void deleteCollection(String name) {
        File collDir = new File(dbDir, name);
        if(!collDir.exists()) {
            return;
        }

        Collection coll = collectionMap.get(name);
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
