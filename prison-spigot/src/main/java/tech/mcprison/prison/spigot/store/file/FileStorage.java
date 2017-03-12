package tech.mcprison.prison.spigot.store.file;

import tech.mcprison.prison.store.Database;
import tech.mcprison.prison.store.Storage;

import java.io.File;
import java.util.*;

/**
 * @author Faizaan A. Datoo
 */
public class FileStorage implements Storage {

    private File rootDir;
    private Map<String, Database> databaseMap;

    public FileStorage(File rootDir) {
        this.rootDir = rootDir;
        this.databaseMap = new HashMap<>();

        // Each folder in the root directory is its own database.
        // We'll initialize each of them here.
        File[] databaseFiles = this.rootDir.listFiles(File::isDirectory);
        if (databaseFiles != null) {
            for(File dbFile : databaseFiles) {
                databaseMap.put(dbFile.getName(), new FileDatabase(dbFile));
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
            return; // A database by this name already exists. As promised, do nothing.
        }

        directory.mkdir();
        databaseMap.put(name, new FileDatabase(directory));
    }

    @Override public void deleteDatabase(String name) {
        File directory = new File(rootDir, name);
        if(!directory.exists()) {
            return; // A database by this name does not exist. As promised, do nothing.
        }

        Database db = databaseMap.get(name);
        if(db == null) {
            return; // Still doesn't exist. Do nothing.
        }

        db.dispose();
        directory.delete();
        databaseMap.remove(name);
    }

    @Override public List<Database> getDatabases() {
        return new ArrayList<>(databaseMap.values());
    }
}
