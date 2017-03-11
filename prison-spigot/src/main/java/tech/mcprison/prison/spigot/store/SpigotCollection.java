package tech.mcprison.prison.spigot.store;

import tech.mcprison.prison.store.Collection;
import tech.mcprison.prison.store.Document;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * @author Faizaan A. Datoo
 */
public class SpigotCollection implements Collection {

    private File collDir;

    public SpigotCollection(File collDir) {
        this.collDir = collDir;
    }

    @Override public String getName() {
        return null;
    }

    @Override public Optional<Document> get(String key) {
        return null;
    }

    @Override public void insert(String key, Document document) {

    }

    @Override public void remove(String key) {

    }

    @Override public List<Document> filter(Document document) {
        return null;
    }

    @Override public List<Document> getAll() {
        return null;
    }

    @Override public void dispose() {

    }
}
