package tech.mcprison.prison.spigot.configs;

import org.bukkit.configuration.Configuration;

import java.io.File;
import java.io.IOException;

public abstract class SpigotConfigComponents {

    protected void fileMaker(File file) {
        if(!file.exists()) {
            try {
                File parentDir = file.getParentFile();
                parentDir.mkdirs();
                file.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
