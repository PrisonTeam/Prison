package tech.mcprison.prison.cells;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.gui.GUI;
import tech.mcprison.prison.platform.Capability;
import tech.mcprison.prison.platform.IncapableException;

/**
 * Created by DMP9 on 30/12/2016.
 */
public class CellsGUI {
    public boolean canShow() {
        return Prison.get().getPlatform().getClass().getName() == "SpigotPlatform";
    }

    private GUI gui;

    public CellsGUI() {
        if (!canShow()) {
            throw new IncapableException(Capability.GUI);
        }
        gui = Prison.get().getPlatform().createGUI("", 1);

    }
}
