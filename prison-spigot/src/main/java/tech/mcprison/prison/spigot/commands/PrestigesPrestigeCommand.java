package tech.mcprison.prison.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
import tech.mcprison.prison.spigot.gui.rank.SpigotConfirmPrestigeGUI;

/**
 * @author GABRYCA
 */

public class PrestigesPrestigeCommand implements CommandExecutor, Listener {

    boolean isChatEventActive;
    int id;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e)
    {
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
}
