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

import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.platform.Capability;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.output.BulletedListComponent;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.util.Text;

/**
 * Root commands for managing the platform as a whole, in-game.
 *
 * @author Faizaan A. Datoo
 * @since API 0.1
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

        String permissions =
            Prison.get().getPlatform().getCapabilities().get(Capability.PERMISSIONS) ?
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

    @Command(identifier = "prison convert", description = "Convert your Prison 2 data to Prison 3.", onlyPlayers = false)
    public void convertCommand(CommandSender sender) {
        sender.sendMessage(Prison.get().getPlatform().runConverter());
    }

}
