package tech.mcprison.prison.troubleshoot.zip;

import java.io.File;
import java.nio.file.NotDirectoryException;

public class FileProvider implements SupportZipProvider{

    File file;
    public FileProvider(File file) throws NotDirectoryException {
        if (!file.isDirectory()){
            throw new NotDirectoryException(file.toString());
        }
        this.file = file;
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public SupportZipEntry[] generateSupportZipEntries() {
        return new SupportZipEntry[0];
    }
}
