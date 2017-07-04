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
import tech.mcprison.prison.integration.IntegrationType;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.output.BulletedListComponent;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.troubleshoot.TroubleshootResult;
import tech.mcprison.prison.troubleshoot.Troubleshooter;
import tech.mcprison.prison.util.Text;

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

        String permissions =
            Prison.get().getIntegrationManager().hasForType(IntegrationType.PERMISSION) ?
                "&a" + Prison.get().getIntegrationManager().getForType(IntegrationType.PERMISSION)
                    .get().getProviderName() :
                "&cNone";

        display.text(Text.tab("&7Permissions: " + permissions));

        String economy = Prison.get().getIntegrationManager().hasForType(IntegrationType.ECONOMY) ?
            "&a" + Prison.get().getIntegrationManager().getForType(IntegrationType.ECONOMY).get()
                .getProviderName() :
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

    @Command(identifier = "prison troubleshoot", description = "Run a troubleshooter.", onlyPlayers = false)
    public void troubleshootCommand(CommandSender sender,
        @Arg(name = "name", def = "list") String name) {
        // They just want to list stuff
        if (name.equals("list")) {
            sender.dispatchCommand("prison troubleshoot list");
            return;
        }

        TroubleshootResult result =
            PrisonAPI.getTroubleshootManager().invokeTroubleshooter(name, sender);
        if (result == null) {
            Output.get().sendError(sender, "The troubleshooter %s doesn't exist.", name);
            return;
        }

        ChatDisplay display = new ChatDisplay("Result Summary");
        display.text("&7Troubleshooter name: &b%s", name.toLowerCase()) //
            .text("&7Result type: &b%s", result.getResult().name()) //
            .text("&7Result details: &b%s", result.getDescription()) //
            .send(sender);

    }

    @Command(identifier = "prison troubleshoot list", description = "List the troubleshooters.", onlyPlayers = false)
    public void troubleshootListCommand(CommandSender sender) {
        ChatDisplay display = new ChatDisplay("Troubleshooters");
        display.text("&8Type /prison troubleshoot <name> to run a troubleshooter.");

        BulletedListComponent.BulletedListBuilder builder =
            new BulletedListComponent.BulletedListBuilder();
        for (Troubleshooter troubleshooter : PrisonAPI.getTroubleshootManager()
            .getTroubleshooters()) {
            builder.add("&b%s &8- &7%s", troubleshooter.getName(), troubleshooter.getDescription());
        }
        display.addComponent(builder.build());

        display.send(sender);
    }

    @Command(identifier = "prison convert", description = "Convert your Prison 2 data to Prison 3.", onlyPlayers = false)
    public void convertCommand(CommandSender sender) {
        sender.sendMessage(Prison.get().getPlatform().runConverter());
    }

}
