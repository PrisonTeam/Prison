package tech.mcprison.prison.troubleshoot.zip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;

public class SupportZip {
    public static String create(List<String> include) throws IOException {
        String name = getFileName();
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(new File(PrisonAPI.getPluginDirectory(),name)));
        SupportZipManager mgr = Prison.get().getSupportZipManager();
        if (include.size() == 0){
            include = new ArrayList<>(mgr.providers.keySet());
        }
        for (Entry<String,SupportZipProvider> provider : mgr.providers.entrySet()){
            if (include.contains(provider.getKey().toLowerCase())){
                for (SupportZipEntry entry : provider.getValue().generateSupportZipEntries()){
                    ZipEntry zipEntry = new ZipEntry(provider.getKey()+"/"+entry.getPath());
                    out.putNextEntry(zipEntry);
                    byte[] data = entry.getData();
                    out.write(data, 0, data.length);
                    out.closeEntry();
                }
            }
        }
        out.close();
        return name;
    }
    public static List<String> processProviders(String params){
        String[] split = params.split(" ");
        SupportZipManager mgr = Prison.get().getSupportZipManager();
        ArrayList<String> result = new ArrayList<>();
        for (String param : split){
            if (mgr.providers.containsKey(param.toLowerCase()) && !result.contains(param.toLowerCase())){
                result.add(param.toLowerCase());
            }
        }
        return result;
    }
    private static String getFileName(){
        return "prison-support"+ new SimpleDateFormat("ddmonyyyy").format(Date.from(Instant.now()))+".zip";
    }
}
