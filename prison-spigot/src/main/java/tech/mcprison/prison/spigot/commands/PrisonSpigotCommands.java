package tech.mcprison.prison.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.modules.ModuleManager;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.SpigotPrisonGUI;
import tech.mcprison.prison.spigot.gui.mine.SpigotPlayerMinesGUI;
import tech.mcprison.prison.spigot.gui.rank.SpigotConfirmPrestigeGUI;
import tech.mcprison.prison.spigot.gui.rank.SpigotPlayerPrestigesGUI;
import tech.mcprison.prison.spigot.gui.rank.SpigotPlayerRanksGUI;
import tech.mcprison.prison.spigot.spiget.BluesSpigetSemVerComparator;

import java.util.Objects;

/**
 * @author GABRYCA
 * @author RoyalBlueRanger
 */
public class PrisonSpigotCommands implements CommandExecutor, Listener {

    boolean isChatEventActive;
    int id;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e) {
        if (isChatEventActive){
            Player p = e.getPlayer();
            String message = e.getMessage();
            Bukkit.getScheduler().cancelTask(id);
            if (message.equalsIgnoreCase("cancel")){
                isChatEventActive = false;
                p.sendMessage(SpigotPrison.format("&cPrestige cancelled"));
                e.setCancelled(true);
            } else if (message.equalsIgnoreCase("confirm")){
                Bukkit.getScheduler().runTask(SpigotPrison.getInstance(), () -> Bukkit.getServer().dispatchCommand(p, "rankup prestiges"));
                e.setCancelled(true);
                isChatEventActive = false;
            }
        }
    }

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
            sender.sendMessage(SpigotPrison.format("&cIncorrect usage, the command should be /prisonmanager -gui-ranks-mines-prestiges-prestige"));
            return true;
        }

        if (prisonmanagerGUI(sender, args, p)) return true;

        if (args[0].equalsIgnoreCase("ranks")){
            return prisonmanagerRanks(sender, p, GuiConfig);
        } else if (args[0].equalsIgnoreCase("mines")){
            return prisonmanagerMines(sender, p, GuiConfig);
        } else if (args[0].equalsIgnoreCase("prestiges")) {
            return prisonmanagerPrestiges(sender, p, GuiConfig);
        } else if (args[0].equalsIgnoreCase("prestige")){
            return prisonmanagerPrestige(sender, p);
        }

        return true;
    }

    private boolean prisonmanagerPrestige(CommandSender sender, Player p) {
        if (SpigotPrison.getInstance().getConfig().getBoolean("prestiges")) {

            if (!(PrisonRanks.getInstance().getLadderManager().getLadder("prestiges").isPresent())) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ranks ladder create prestiges");
            }

            PrisonRanks rankPlugin;

            ModuleManager modMan = Prison.get().getModuleManager();
            Module module = modMan == null ? null : modMan.getModule( PrisonRanks.MODULE_NAME ).orElse( null );

            rankPlugin = (PrisonRanks) module;

            LadderManager lm = null;
            if (rankPlugin != null) {
                lm = rankPlugin.getLadderManager();
            }

            if (lm != null && (!(lm.getLadder("default").isPresent()) ||
                    !(lm.getLadder("default").get().getLowestRank().isPresent()) ||
                    lm.getLadder("default").get().getLowestRank().get().name == null)) {
                sender.sendMessage(SpigotPrison.format("&cThere aren't ranks in the default ladder"));
                return true;
            }

            if (lm != null && (!(lm.getLadder("prestiges").isPresent()) ||
                    !(lm.getLadder("prestiges").get().getLowestRank().isPresent()) ||
                    lm.getLadder("prestiges").get().getLowestRank().get().name == null)) {
                sender.sendMessage(SpigotPrison.format("&cThere aren't prestiges in the prestige ladder"));
                return true;
            }

            try {
                SpigotConfirmPrestigeGUI gui = new SpigotConfirmPrestigeGUI(p);
                gui.open();
            } catch (Exception ex){
                isChatEventActive = true;
                sender.sendMessage(SpigotPrison.format("&aConfirm&3: Type the word &aconfirm &3 to confirm"));
                sender.sendMessage(SpigotPrison.format("&cCancel&3: Type the word &ccancel &3to cancel, &cyou've 15 seconds!"));
                Player finalP = p;
                id = Bukkit.getScheduler().scheduleSyncDelayedTask(SpigotPrison.getInstance(), () -> {
                    isChatEventActive = false;
                    finalP.sendMessage(SpigotPrison.format("&cYou ran out of time, prestige cancelled."));
                }, 20L * 15);
            }

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
