package tech.mcprison.prison.cells.plugins;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.cells.Cells;

/**
 *
 */
@Plugin(id = "prison-demo", name = "PrisonDemo", version = "1.0.0", dependencies = {
        @Dependency(id = "prison")})
public class CellsSponge {

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        Prison.get().getModuleManager().registerModule(new Cells("1.0.0"));
    }

}