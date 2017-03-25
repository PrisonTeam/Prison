package tech.mcprison.prison.file;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tech.mcprison.prison.store.Collection;
import tech.mcprison.prison.store.Document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Faizaan A. Datoo
 */
public class FileCollection implements Collection {

    private static Gson gson;

    static {
        gson = new GsonBuilder().disableHtmlEscaping().create();
    }

    private File collDir;
    private LoadingCache<String, Document> documentCache;

    public FileCollection(File collDir) {
        this.collDir = collDir;
        this.documentCache = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES)
            .build(new CacheLoader<String, Document>() {
                @Override public Document load(String key) throws Exception {
                    return readFromDisk(key);
                }
            });
    }

    @Override public String getName() {
        return collDir.getName();
    }

    @Override public Optional<Document> get(String key) {
        try {
            return Optional.ofNullable(documentCache.get(key));
        } catch (ExecutionException e) {
            return Optional.empty();
        }
    }

    private Document readFromDisk(String key) {
        File file = getFile(key);
        if (!file.exists()) {
            return null;
        }

        try {
            String json = new String(Files.readAllBytes(file.toPath()));
            return gson.fromJson(json, Document.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override public void insert(String key, Document document) {
        writeToDisk(key, document);
        documentCache.put(key, document);
    }

    private void writeToDisk(String key, Document document) {
        File file = getFile(key);
        if (file.exists()) {
            file.delete();
        }

        try {
            String json = gson.toJson(document);
            file.createNewFile();
            Files.write(file.toPath(), json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public void remove(String key) {
        File file = getFile(key);
        if (file.exists()) {
            file.delete();
        }

        documentCache.invalidate(key);
    }

    @Override public List<Document> filter(Document document) {
        List<Document> ret = new ArrayList<>();

        for (Document doc : getAll()) {
            if (Maps.difference(document, doc).entriesInCommon().size() == document.size()) {
                ret.add(doc);
            }
        }

        return ret;
    }

    @Override public List<Document> getAll() {
        File[] files = collDir.listFiles((dir, name) -> name.endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                get(file.getName().split("\\.")[0]);
            }
        }
        return new ArrayList<>(documentCache.asMap().values());
    }

    @Override public void dispose() {
        documentCache.invalidateAll();
    }

    private File getFile(String key) {
        return new File(collDir, key + ".json");
    }

}
