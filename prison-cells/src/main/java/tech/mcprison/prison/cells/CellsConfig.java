package tech.mcprison.prison.cells;

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.store.AbstractJsonable;

import java.io.File;
import java.io.IOException;

/**
 * Created by DMP9 on 01/01/2017.
 */
public class CellsConfig extends AbstractJsonable<CellsConfig> {
    private CellsConfig() {
    }

    public CellsConfig load() {
        File f = new File(Cells.get().getDataFolder(), "config.json");
        CellsConfig json;
        try {
            json = fromFile(f);
        } catch (IOException e) {
            Output.get().logError("Couldn't open &eCells&c configuration", e);
            return null;
        }
        return json;
    }
}
