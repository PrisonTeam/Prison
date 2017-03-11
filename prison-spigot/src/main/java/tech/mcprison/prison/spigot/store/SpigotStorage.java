package tech.mcprison.prison.spigot.store;

import tech.mcprison.prison.store.Database;
import tech.mcprison.prison.store.Storage;

import java.io.File;
import java.util.*;

/**
 * @author Faizaan A. Datoo
 */
public class SpigotStorage implements Storage {

    private File rootDir;
    private Map<String, Database> databaseMap;

    public SpigotStorage(File rootDir) {
        this.rootDir = rootDir;
        this.databaseMap = new HashMap<>();

        File[] databaseFiles = this.rootDir.listFiles(File::isDirectory);
        if (databaseFiles != null) {
            for(File dbFile : databaseFiles) {
                databaseMap.put(dbFile.getName(), new SpigotDatabase(dbFile));
            }
        }
    }

    @Override public boolean isConnected() {
        return rootDir.exists();
    }

    @Override public Optional<Database> getDatabase(String name) {
        return Optional.ofNullable(databaseMap.get(name));
    }

    @Override public void createDatabase(String name) {
        File directory = new File(rootDir, name);
        if(directory.exists()) {
            return;
        }

        directory.mkdir();
        databaseMap.put(name, new SpigotDatabase(directory));
    }

    @Override public void deleteDatabase(String name) {
        File directory = new File(rootDir, name);
        if(!directory.exists()) {
            return;
        }

        Database db = databaseMap.get(name);
        if(db == null) {
            return;
        }

        db.dispose();
        directory.delete();

        databaseMap.remove(name);
    }

    @Override public List<Database> getDatabases() {
        return new ArrayList<>(databaseMap.values());
    }
}
