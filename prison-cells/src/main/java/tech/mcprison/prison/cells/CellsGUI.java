package tech.mcprison.prison.cells;

import tech.mcprison.prison.Prison;

/**
 * Created by DMP9 on 30/12/2016.
 */
public class CellsGUI {
    public boolean canShow(){return Prison.get().getPlatform().getClass().getName() == "SpigotPlatform";}
}
