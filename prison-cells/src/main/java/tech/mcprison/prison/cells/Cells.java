package tech.mcprison.prison.cells;

import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.output.Output;

/**
 *
 */
public class Cells extends Module {
    private static Cells i = null;
    private CellsConfig config;

    public CellsConfig getConfig() {
        return config;
    }

    public static Cells get() {
        return i;
    }

    private CellsList cells;

    public CellsList getCells() {
        return cells;
    }

    private static String version = "Unregistered-Snapshot";
    private boolean initialized = false;

    public boolean getInitialized() {
        return initialized;
    }

    public Cells(String version) {
        super("Cells", version, 30);
    }

    public void enable() {
        Output.get().logInfo("&bEnabling &ePrison Cells&b...");
    }
}
