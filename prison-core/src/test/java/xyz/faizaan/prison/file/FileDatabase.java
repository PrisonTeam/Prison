package xyz.faizaan.prison.file;

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
