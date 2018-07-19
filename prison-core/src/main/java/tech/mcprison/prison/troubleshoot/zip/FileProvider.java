package tech.mcprison.prison.troubleshoot.zip;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
        List<SupportZipEntry> out = new ArrayList<>();
        try {
            for (String file : getFileNames(new ArrayList<>(),file)){
                SupportZipEntry entry = new SupportZipEntry();
                entry.setPath(file);
                entry.setData(Files.readAllBytes(new File(this.file,file).toPath()));
                out.add(entry);
            }
        } catch (IOException e) {
            return new SupportZipEntry[0];
        }
        return (SupportZipEntry[])out.toArray();
    }

    private List<String> getFileNames(List<String> fileNames, File dir) throws IOException {
            for (File path : dir.listFiles()) {
                if(path.isDirectory()) {
                    getFileNames(fileNames, path);
                } else {
                    fileNames.add(path.toURI().relativize(file.toURI()).getPath());
                }
            }
        return fileNames;
    }
}
