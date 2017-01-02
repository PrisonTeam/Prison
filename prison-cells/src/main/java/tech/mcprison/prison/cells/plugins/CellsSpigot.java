package tech.mcprison.prison.cells.plugins;

import org.bukkit.plugin.java.JavaPlugin;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.cells.Cells;

/**
 * Created by DMP9 on 30/12/2016.
 */
public class CellsSpigot extends JavaPlugin {
    public void onEnable() {
        Prison.get().getModuleManager().registerModule(new Cells(getDescription().getVersion()));
    }
}
