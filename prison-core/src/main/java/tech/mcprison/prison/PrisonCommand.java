/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison;

import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.displays.BulletedListComponent;
import tech.mcprison.prison.displays.ChatDisplay;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.platform.Capability;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.modules.ModuleManager;
import tech.mcprison.prison.util.Text;

import java.util.Optional;

/**
 * Root commands for managing the platform as a whole, in-game.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class PrisonCommand {

    @Command(identifier = "prison version", description = "Version information for Prison.", onlyPlayers = false)
    public void versionCommand(CommandSender sender) {
        ChatDisplay display = new ChatDisplay("/prison version");
        display
                .text("&7Version: &3%s &8(API level %d)", Prison.get().getPlatform().getPluginVersion(),
                        Prison.API_LEVEL);

        display.text("&7Platform: &3%s", Prison.get().getPlatform().getClass().getName());
        display.text("&7Integrations:");

        String permissions = Prison.get().getPlatform().getCapabilities().get(Capability.PERMISSIONS) ?
                "&aYes" :
                "&cNone";

        display.text(Text.tab("&7Permissions: " + permissions));

        String economy = Prison.get().getPlatform().getCapabilities().get(Capability.ECONOMY) ?
                "&aYes" :
                "&cNone";

        display.text(Text.tab("&7Economy: " + economy));

        display.send(sender);
    }

    @Command(identifier = "prison modules", description = "List and manage Prison's modules.", onlyPlayers = false)
    public void modulesCommand(CommandSender sender) {
        ChatDisplay display = new ChatDisplay("/prison modules");
//        display
//            .addComponent(new TextComponent("&7To enable a module, use /prison modules enable."));
//        display
//            .addComponent(new TextComponent("&7To disable a module, use /prison modules disable."));
        display.emptyLine();

        BulletedListComponent.BulletedListBuilder builder =
                new BulletedListComponent.BulletedListBuilder();
        for (Module module : Prison.get().getModuleManager().getModules()) {
            builder.add("&3%s &8(%s) &3v%s &8- %s", module.getName(), module.getPackageName(),
                    module.getVersion(), module.getStatus().getMessage());
        }

        display.addComponent(builder.build());

        display.send(sender);
    }

    // FIXME THESE COMMANDS ARE BROKEN
    // Modules can't load and unload on the fly.

    //    @Command(identifier = "prison modules enable", description = "Enable a module.", onlyPlayers = false)
    public void moduleEnableCommand(CommandSender sender,
                                    @Arg(name = "moduleName") String moduleName) {
        Optional<Module> moduleOptional = getModule(moduleName);
        if (!moduleOptional.isPresent()) {
            Output.get().sendError(sender, "&7The module &c%s &7does not exist.", moduleName);
            return;
        }

        Module module = moduleOptional.get();

        if (module.isEnabled()) {
            Output.get()
                    .sendWarn(sender, "&7The module &c%s &7is already enabled.", module.getName());
            return;
        }

        boolean result = Prison.get().getModuleManager().enableModule(module);
        if (result) {
            Output.get()
                    .sendInfo(sender, "&7The module &3%s &7has been enabled.", module.getName());
        } else {
            Output.get().sendError(sender,
                    "&7Failed to enable the module &c%s &7. &8Check the console for details.",
                    module.getName());
        }
    }

    //    @Command(identifier = "prison modules disable", description = "Disable a module.", onlyPlayers = false)
    public void moduleDisableCommand(CommandSender sender,
                                     @Arg(name = "moduleName") String moduleName) {
        Optional<Module> moduleOptional = getModule(moduleName);
        if (!moduleOptional.isPresent()) {
            Output.get().sendError(sender, "&7The module &c%s &7does not exist.", moduleName);
            return;
        }

        Module module = moduleOptional.get();

        if (!module.isEnabled()) {
            Output.get().sendWarn(sender, "&7The module &c%s &7is already disabled.", moduleName);
            return;
        }

        Prison.get().getModuleManager().disableModule(module);
        Output.get().sendInfo(sender, "&7The module &3%s &7has been disabled.", module.getName());
    }

    // Get a module by name or by package name
    private Optional<Module> getModule(String name) {
        ModuleManager manager = Prison.get().getModuleManager();
        return Optional.ofNullable(
                manager.getModule(name).orElse(manager.getModuleByPackageName(name).orElse(null)));
    }

}
