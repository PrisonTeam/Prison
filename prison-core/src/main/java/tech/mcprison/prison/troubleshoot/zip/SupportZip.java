package tech.mcprison.prison.troubleshoot.zip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.error.Error;

public class SupportZip {

    public static String create() throws IOException {
        String name = getFileName();
        ZipOutputStream out = new ZipOutputStream(
            new FileOutputStream(new File(PrisonAPI.getPluginDirectory(), name)));
        SupportZipManager mgr = Prison.get().getSupportZipManager();
        for (Entry<String, SupportZipProvider> provider : mgr.providers.entrySet()) {
            SupportZipEntry[] array = null;
            try {
                array = provider.getValue().generateSupportZipEntries();
            }catch(Exception e){
                Error error = new Error("Something went wrong while adding \""+provider.getKey()+"\" to the Support ZIP. It will not be included in the ZIP.");
                error.appendStackTrace("",e);
                Prison.get().getErrorManager().throwError(error);
            }
            for (SupportZipEntry entry : array) {
                ZipEntry zipEntry = new ZipEntry(provider.getKey() + "/" + entry.getPath());
                out.putNextEntry(zipEntry);
                byte[] data = entry.getData();
                out.write(data, 0, data.length);
                out.closeEntry();
            }
        }
        out.close();
        return name;
    }

    private static String getFileName() {
        return "prison-support" + new SimpleDateFormat("ddmonyyyy-HH-mm").format(Date.from(Instant.now()))
            + ".zip";
    }
}
