package tech.mcprison.prison.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.spigot.SpigotPrison;

public class PrestigesTemplateCommand {

    public static boolean onCommand(CommandSender sender, String[] args) {

        // Get the player
        Player p = (Player) sender;

        if (args.length == 3) {

            // Check the permission
            if (!(p.hasPermission("prison.admin"))) {
                sender.sendMessage(SpigotPrison.format("&cSorry, but you don't have the permission &1[&c-Prison.admin&1]"));
                return true;
            }

            // Get the ladderManager
            LadderManager lm = PrisonRanks.getInstance().getLadderManager();

            String rankName;
            int x = 0;

            try {
                while (lm.getLadder(args[0]).get().getNext(x).isPresent()){
                    x++;
                }
            } catch (Exception ex3){
                x--;
            }

            try {
                rankName = lm.getLadder(args[0]).get().getByPosition(x).get().name;
            } catch (Exception ex){
                sender.sendMessage(SpigotPrison.format("&cThat ladder might not exists"));
                return true;
            }

            p.sendMessage(SpigotPrison.format("&aThe last rank's " + rankName));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ranks ladder create prestiges");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), SpigotPrison.format("ranks create + 1000 prestiges &8[&c&l+&8]&f"));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ranks command add " + rankName + " " + args[1]);
            rankName = lm.getLadder(args[0]).get().getLowestRank().get().name;
            p.sendMessage(SpigotPrison.format("&aThe first rank's " + rankName));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ranks command add " + rankName + " " + args[2]);
            p.sendMessage(SpigotPrison.format("&aLadder -prestiges- has been created with success, &c&lDON'T RENAME IT &cor prestiges won't work, &ayou can rename the ranks inside the ladder and add new, they'll work as prestiges."));

            return true;
        }
        return true;
    }
}
