package tech.mcprison.prison.spigot.setup;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import tech.mcprison.prison.spigot.SpigotPrison;

public class RanksChatSetup {

    private static RanksChatSetup instance;

    public RanksChatSetup(){}

    public static RanksChatSetup get() {
        if (instance == null) {
            instance = new RanksChatSetup();
        }
        return instance;
    }

    public void setup(CommandSender sender){

        Configuration messages = SpigotPrison.getMessagesConfig();
        Configuration config = SpigotPrison.getGuiConfig();

        sender.sendMessage(SpigotPrison.format(messages.getString("Setup.Message.WelcomeToRanksSetup")));

        String permissionManagerCommand = null;
        String permission = config.getString("Options.Mines.PermissionWarpPlugin");

        if (SpigotPrison.getInstance().getServer().getPluginManager().getPlugin("LuckPerms") != null){
            permissionManagerCommand = "lp user {player} permission set " + permission;
        } else if (SpigotPrison.getInstance().getServer().getPluginManager().getPlugin("PermissionsEx") != null){
            permissionManagerCommand = "pex user {player} add " + permission;
        } else if (SpigotPrison.getInstance().getServer().getPluginManager().getPlugin("UltraPermissions") != null){
            permissionManagerCommand = "upc addplayerpermission {player} " + permission;
        } else if (SpigotPrison.getInstance().getServer().getPluginManager().getPlugin("zPermissions") != null){
            permissionManagerCommand = "permissions player {player} set " + permission;
        } else if (SpigotPrison.getInstance().getServer().getPluginManager().getPlugin("PowerfulPerms") != null){
            permissionManagerCommand = "pp user {player} add " + permission;
        }

        int colorID = 1;
        double price = 0;

        for ( char rankName = 'A'; rankName <= 'Z'; rankName++) {

            Bukkit.dispatchCommand(sender, "ranks create " + rankName + " " + price + " default &7[&" + ((colorID++ % 9) + 1) + "" + rankName + "&7]&f");
            if (permissionManagerCommand != null) {
                Bukkit.dispatchCommand(sender, "ranks command add " + rankName + " " + permissionManagerCommand + rankName);
            }

            if (price == 0){
                price += 1000;
            } else {
                price = price * 1.5;
            }
        }

        sender.sendMessage(SpigotPrison.format(messages.getString("Setup.Message.SuccessRanksSetup")));
    }
}
