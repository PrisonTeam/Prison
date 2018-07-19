package tech.mcprison.prison.troubleshoot.zip;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.Map.Entry;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.store.Collection;
import tech.mcprison.prison.store.Database;
import tech.mcprison.prison.store.Document;

public class DataStorageProvider implements SupportZipProvider {

    @Override
    public String getName() {
        return "data_storage";
    }

    @Override
    public SupportZipEntry[] generateSupportZipEntries() {
        ArrayList<SupportZipEntry> entries = new ArrayList<>();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        for (Database db : PrisonAPI.getStorage().getDatabases()){
            for (Collection coll : db.getCollections()){
                for (Entry<String,Document> document : coll.map().entrySet()){
                    SupportZipEntry entry = new SupportZipEntry();
                    entry.setPath(db.getName()+"/"+coll.getName()+"/"+document.getKey()+".json");
                    entry.setData(gson.toJson(document.getValue()).getBytes());
                    entries.add(entry);
                }
            }
        }
        return (SupportZipEntry[]) entries.toArray();
    }
}
