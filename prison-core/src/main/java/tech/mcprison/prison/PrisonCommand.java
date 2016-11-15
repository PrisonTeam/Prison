/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2016 The Prison Team
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
import tech.mcprison.prison.commands.FlagArg;
import tech.mcprison.prison.commands.Flags;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.output.Alert;
import tech.mcprison.prison.platform.CommandSender;

/**
 * @author Faizaan A. Datoo
 */
public class PrisonCommand {

    @Command(identifier = "prison version", description = "Version information for Prison.", onlyPlayers = false)
    public void versionCommand(CommandSender sender) {
        sender.sendMessage("&7========== &d/prison version &7==========");
        sender.sendMessage("&7Version: &3" + Prison.get().getPlatform().getPluginVersion() + " &8(API level " + Prison.API_LEVEL + ")");
        sender.sendMessage(
            "&7Platform: &3" + Prison.get().getPlatform().getClass().getName());
        sender.sendMessage("&7Integrations:");
        sender.sendMessage("&7    Permissions: &cNone");
        sender.sendMessage("&7    Economy: &cNone");
        sender.sendMessage("&7    Selection: &cNone");
        sender.sendMessage("&7========== &d/prison version &7==========");
    }

    @Command(identifier = "prison modules", description = "List and manage Prison's modules.", onlyPlayers = false)
    public void modulesCommand(CommandSender sender) {
        sender.sendMessage("&7========== &d/prison modules &7==========");
        sender.sendMessage("&8To enable a module, use /prison modules enable.");
        sender.sendMessage("&8To disable a module, use /prison modules disable.");
        sender.sendMessage("");  // blank line
        for (Module module : Prison.get().getModuleManager().getModules()) {
            sender.sendMessage(
                "&7• &3" + module.getName() + " &8(" + module.getPackageName() + ") &3v" + module
                    .getVersion() + " &8- " + Prison.get().getModuleManager()
                    .getStatus(module.getName()));
        }
        sender.sendMessage("&7========== &d/prison modules &7==========");
    }

    @Command(identifier = "prison modules enable", description = "Enable a module.", onlyPlayers = false)
    public void moduleEnableCommand(CommandSender sender,
        @Arg(name = "moduleName") String moduleName) {
        Module module = getModule(moduleName);
        if (module == null) {
            sender.sendMessage("&7The module &c" + moduleName + " &7does not exist.");
            return;
        }

        if (module.isEnabled()) {
            sender.sendMessage("&7The module &c" + module.getName() + " &7is already enabled.");
            return;
        }

        boolean result = Prison.get().getModuleManager().enableModule(module);
        if (result) {
            sender.sendMessage("&7The module &3" + module.getName() + " &7has been enabled.");
        } else {
            sender.sendMessage("&7Failed to enable the module &c" + module.getName()
                + "&7. &8Check the console for details.");
        }
    }

    @Command(identifier = "prison modules disable", description = "Disable a module.", onlyPlayers = false)
    public void moduleDisableCommand(CommandSender sender,
        @Arg(name = "moduleName") String moduleName) {
        Module module = getModule(moduleName);
        if (module == null) {
            sender.sendMessage("&7The module &c" + moduleName + " &7does not exist.");
            return;
        }

        if (!module.isEnabled()) {
            sender.sendMessage("&7The module &c" + moduleName + " &7is already disabled.");
            return;
        }

        Prison.get().getModuleManager().disableModule(module);
        sender.sendMessage("&7The module &3" + module.getName() + " &7has been disabled.");
    }

    /*
    @Command(identifier = "prison patrons", description = "View the list of all of our patrons qualified to be in the list")
    @Flags(identifier = {"r"}, description = {"Refresh the patrons list"})
    public void patronsCommand(CommandSender sender, @FlagArg("r") boolean refresh) {
        if (refresh) {
            Prison.get().getPatrons().getPatrons();
            sender.sendMessage("&7Patrons list refreshed");
            return;
        }

        sender.sendMessage("&7============= &d/prison patrons &7=============");
        List<String> patrons = Patrons.get();

        if (patrons.size() == 0) {
            sender.sendMessage("&7We sadly have no patrons :(");
        } else {
            sender.sendMessage("&7We have &d" + patrons.size() + "&7 patrons! ");

            StringBuilder stringBuilder = new StringBuilder();
            for (String patron : patrons)
                stringBuilder.append("&d").append(patron).append("&7, ");
            sender.sendMessage(stringBuilder.toString());
        }

        sender.sendMessage("&7============= &d/prison patrons &7=============");
    }
    */

    @Command(identifier = "prison alerts", description = "View the list of alerts currently active", onlyPlayers = false, permissions = {
        "prison.alerts"}) @Flags(identifier = {"c"}, description = {"Clear alerts"})
    public void alertsCommand(CommandSender sender, @FlagArg("c") boolean clear) {
        if (clear) {
            Alert.get().clearAlerts();
            sender.sendMessage(Prison.get().getMessages().alertsCleared);
            return;
        }
        Alert.get().listAlerts().forEach(sender::sendMessage);
        sender.sendMessage("&8&oTo clear alerts, type /prison alerts -c.");
    }

    // Get a module by name or by package name
    private Module getModule(String name) {
        Module module =
            Prison.get().getModuleManager().getModule(name); // Try it by name first
        if (module == null) {
            module = Prison.get().getModuleManager()
                .getModuleByPackageName(name); // Try it by package name next
            if (module == null) {
                return null;
            }
        }
        return module;
    }

}
