package tech.mcprison.prison.spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.SpigotPrisonGUI;
import tech.mcprison.prison.spigot.gui.mine.SpigotPlayerMinesGUI;
import tech.mcprison.prison.spigot.gui.rank.SpigotPlayerPrestigesGUI;
import tech.mcprison.prison.spigot.gui.rank.SpigotPlayerRanksGUI;
import tech.mcprison.prison.spigot.spiget.BluesSpigetSemVerComparator;

import java.util.Objects;

/**
 * @author GABRYCA
 * @author RoyalBlueRanger
 */
public class PrisonSpigotCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(SpigotPrison.getInstance().getConfig().getString("prison-gui-enabled").equalsIgnoreCase("true"))){
            sender.sendMessage(SpigotPrison.format("&cThe GUI's disabled, if you want to use it, edit the config.yml!"));
            return true;
        }

        if(!(sender instanceof Player || sender instanceof tech.mcprison.prison.internal.Player)){
            sender.sendMessage(SpigotPrison.format("&cLooks like you aren't a player"));
            return true;
        }

        Player p = null;
        if (sender instanceof Player) {
            p = (Player) sender;
        }

        // Load config
        Configuration GuiConfig = SpigotPrison.getGuiConfig();

        if (args.length == 0) {
            sender.sendMessage(SpigotPrison.format("&cIncorrect usage, the command should be /prisonmanager -gui-ranks-mines-prestiges-"));
            return true;
        }

        if (prisonmanagerGUI(sender, args, p)) return true;

        if (args[0].equalsIgnoreCase("ranks")){
            return prisonmanagerRanks(sender, p, GuiConfig);
        } else if (args[0].equalsIgnoreCase("mines")){
            return prisonmanagerMines(sender, p, GuiConfig);
        } else if (args[0].equalsIgnoreCase("prestiges")) {
            return prisonmanagerPrestiges(sender, p, GuiConfig);
        }

        return true;
    }

    private boolean prisonmanagerPrestiges(CommandSender sender, Player p, Configuration guiConfig) {
        if (!(Objects.requireNonNull(SpigotPrison.getInstance().getConfig().getString("prestiges")).equalsIgnoreCase("true"))) {
            sender.sendMessage(SpigotPrison.format("&cPrestiges are disabled by default, please edit it in your config.yml!"));
            return true;
        }
        if (!(Objects.requireNonNull(guiConfig.getString("Options.Prestiges.GUI_Enabled")).equalsIgnoreCase("true"))) {
            sender.sendMessage(SpigotPrison.format("&cSorry, but this GUI's disabled in your GuiConfig.yml"));
            return true;
        }
        if (Objects.requireNonNull(guiConfig.getString("Options.Prestiges.Permission_GUI_Enabled")).equalsIgnoreCase("true")) {
            if (!(sender.hasPermission(Objects.requireNonNull(guiConfig.getString("Options.Prestiges.Permission_GUI"))))){
                sender.sendMessage(SpigotPrison.format("&cSorry, but you're missing the permission to open this GUI [" + guiConfig.getString("Options.Prestiges.Permission_GUI") + "]"));
                return true;
            }
            SpigotPlayerPrestigesGUI gui = new SpigotPlayerPrestigesGUI(p);
            gui.open();
        } else {
            SpigotPlayerPrestigesGUI gui = new SpigotPlayerPrestigesGUI(p);
            gui.open();
        }
        return true;
    }

    private boolean prisonmanagerMines(CommandSender sender, Player p, Configuration guiConfig) {
        if (!(Objects.requireNonNull(guiConfig.getString("Options.Mines.GUI_Enabled")).equalsIgnoreCase("true"))){
            sender.sendMessage(SpigotPrison.format("&cSorry, but this GUI's disabled in your GuiConfig.yml"));
            return true;
        }
        if (Objects.requireNonNull(guiConfig.getString("Options.Mines.Permission_GUI_Enabled")).equalsIgnoreCase("true")){
            if (!(sender.hasPermission(Objects.requireNonNull(guiConfig.getString("Options.Mines.Permission_GUI"))))){
                sender.sendMessage(SpigotPrison.format("&cSorry, but you're missing the permission to open this GUI [" + guiConfig.getString("Options.Mines.Permission_GUI") + "]"));
                return true;
            }
            SpigotPlayerMinesGUI gui = new SpigotPlayerMinesGUI(p);
            gui.open();
        } else {
            SpigotPlayerMinesGUI gui = new SpigotPlayerMinesGUI(p);
            gui.open();
        }
        return true;
    }

    private boolean prisonmanagerRanks(CommandSender sender, Player p, Configuration guiConfig) {
        if (!(Objects.requireNonNull(guiConfig.getString("Options.Ranks.GUI_Enabled")).equalsIgnoreCase("true"))) {
            sender.sendMessage(SpigotPrison.format("&cSorry, but this GUI's disabled in your GuiConfig.yml"));
            return true;
        }
        if (Objects.requireNonNull(guiConfig.getString("Options.Ranks.Permission_GUI_Enabled")).equalsIgnoreCase("true")) {
            if (!(sender.hasPermission(Objects.requireNonNull(guiConfig.getString("Options.Ranks.Permission_GUI"))))) {
                sender.sendMessage(SpigotPrison.format("&cSorry, but you're missing the permission to open this GUI [" + guiConfig.getString("Options.Ranks.Permission_GUI") + "]"));
                return true;
            }
            SpigotPlayerRanksGUI gui = new SpigotPlayerRanksGUI(p);
            gui.open();
            return true;
        } else {
            SpigotPlayerRanksGUI gui = new SpigotPlayerRanksGUI(p);
            gui.open();
        }
        return true;
    }

    private boolean prisonmanagerGUI(CommandSender sender, String[] args, Player p) {
        if ((sender.hasPermission("prison.admin") || sender.hasPermission("prison.prisonmanagergui")) && args[0].equalsIgnoreCase("gui")){
            if ( new BluesSpigetSemVerComparator().compareMCVersionTo("1.9.0") < 0 ) {
                sender.sendMessage(SpigotPrison.format("&cSorry, but GUIs don't work with versions prior to 1.9.0 due to issues"));
                return true;
            }
            SpigotPrisonGUI gui = new SpigotPrisonGUI(p);
            gui.open();
            return true;
        }
        return false;
    }
}
