package tech.mcprison.prison.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.modules.ModuleManager;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.spigot.SpigotPrison;

/**
 * @author GABRYCA
 */

public class PrestigesPrestigeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (SpigotPrison.getInstance().getConfig().getBoolean("prestiges")) {

            if (!(PrisonRanks.getInstance().getLadderManager().getLadder("prestiges").isPresent())) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ranks ladder create prestiges");
            }

            Player p = null;

            if (!(sender instanceof Player || sender instanceof tech.mcprison.prison.internal.Player)) {
                sender.sendMessage(SpigotPrison.format("&cFor some reasons, it looks like you aren't a player"));
                return true;
            } else {
                if (sender instanceof Player) {
                    p = (Player) sender;
                }
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

            Bukkit.dispatchCommand(sender, "rankup prestiges");

        }

        return true;
    }
}
