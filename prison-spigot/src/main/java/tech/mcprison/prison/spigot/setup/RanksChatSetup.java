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

        Bukkit.dispatchCommand(sender, "ranks create A 0 default &7[&1A&7]&f");
        if (permissionManagerCommand != null) {
            Bukkit.dispatchCommand(sender, "ranks command add A " + permissionManagerCommand + "A");
        }

        Bukkit.dispatchCommand(sender, "ranks create B 1000 default &7[&2B&7]&f");
        if (permissionManagerCommand != null) {
            Bukkit.dispatchCommand(sender, "ranks command add B " + permissionManagerCommand + "B");
        }

        Bukkit.dispatchCommand(sender, "ranks create C 1500 default &7[&3C&7]&f");
        if (permissionManagerCommand != null) {
            Bukkit.dispatchCommand(sender, "ranks command add C " + permissionManagerCommand + "C");
        }

        Bukkit.dispatchCommand(sender, "ranks create D 2250 default &7[&4D&7]&f");
        if (permissionManagerCommand != null) {
            Bukkit.dispatchCommand(sender, "ranks command add D " + permissionManagerCommand + "D");
        }

        Bukkit.dispatchCommand(sender, "ranks create E 3375 default &7[&5E&7]&f");
        if (permissionManagerCommand != null) {
            Bukkit.dispatchCommand(sender, "ranks command add E " + permissionManagerCommand + "E");
        }

        Bukkit.dispatchCommand(sender, "ranks create F 5063 default &7[&6F&7]&f");
        if (permissionManagerCommand != null) {
            Bukkit.dispatchCommand(sender, "ranks command add F " + permissionManagerCommand + "F");
        }

        Bukkit.dispatchCommand(sender, "ranks create G 7594 default &7[&7G&7]&f");
        if (permissionManagerCommand != null) {
            Bukkit.dispatchCommand(sender, "ranks command add G " + permissionManagerCommand + "G");
        }

        Bukkit.dispatchCommand(sender, "ranks create H 11391 default &7[&8H&7]&f");
        if (permissionManagerCommand != null) {
            Bukkit.dispatchCommand(sender, "ranks command add H " + permissionManagerCommand + "H");
        }

        Bukkit.dispatchCommand(sender, "ranks create I 17086 default &7[&9I&7]&f");
        if (permissionManagerCommand != null) {
            Bukkit.dispatchCommand(sender, "ranks command add I " + permissionManagerCommand + "I");
        }

        Bukkit.dispatchCommand(sender, "ranks create J 25629 default &7[&aJ&7]&f");
        if (permissionManagerCommand != null) {
            Bukkit.dispatchCommand(sender, "ranks command add J " + permissionManagerCommand + "J");
        }

        Bukkit.dispatchCommand(sender, "ranks create K 38443 default &7[&bK&7]&f");
        if (permissionManagerCommand != null) {
            Bukkit.dispatchCommand(sender, "ranks command add K " + permissionManagerCommand + "K");
        }

        Bukkit.dispatchCommand(sender, "ranks create L 57665 default &7[&cL&7]&f");
        if (permissionManagerCommand != null) {
            Bukkit.dispatchCommand(sender, "ranks command add L " + permissionManagerCommand + "L");
        }

        Bukkit.dispatchCommand(sender, "ranks create M 86497 default &7[&dM&7]&f");
        if (permissionManagerCommand != null) {
            Bukkit.dispatchCommand(sender, "ranks command add M " + permissionManagerCommand + "M");
        }

        Bukkit.dispatchCommand(sender, "ranks create N 129746 default &7[&eN&7]&f");
        if (permissionManagerCommand != null) {
            Bukkit.dispatchCommand(sender, "ranks command add N " + permissionManagerCommand + "N");
        }

        Bukkit.dispatchCommand(sender, "ranks create O 194620 default &7[&fO&7]&f");
        if (permissionManagerCommand != null) {
            Bukkit.dispatchCommand(sender, "ranks command add O " + permissionManagerCommand + "O");
        }

        Bukkit.dispatchCommand(sender, "ranks create P 291929 default &7[&1P&7]&f");
        if (permissionManagerCommand != null) {
            Bukkit.dispatchCommand(sender, "ranks command add P " + permissionManagerCommand + "P");
        }

        Bukkit.dispatchCommand(sender, "ranks create Q 437894 default &7[&2Q&7]&f");
        if (permissionManagerCommand != null) {
            Bukkit.dispatchCommand(sender, "ranks command add Q " + permissionManagerCommand + "Q");
        }

        Bukkit.dispatchCommand(sender, "ranks create R 656841 default &7[&3R&7]&f");
        if (permissionManagerCommand != null) {
            Bukkit.dispatchCommand(sender, "ranks command add R " + permissionManagerCommand + "R");
        }

        Bukkit.dispatchCommand(sender, "ranks create S 985261 default &7[&4S&7]&f");
        if (permissionManagerCommand != null) {
            Bukkit.dispatchCommand(sender, "ranks command add S " + permissionManagerCommand + "S");
        }

        Bukkit.dispatchCommand(sender, "ranks create T 1477892 default &7[&5T&7]&f");
        if (permissionManagerCommand != null) {
            Bukkit.dispatchCommand(sender, "ranks command add T " + permissionManagerCommand + "T");
        }

        Bukkit.dispatchCommand(sender, "ranks create U 2216838 default &7[&6U&7]&f");
        if (permissionManagerCommand != null) {
            Bukkit.dispatchCommand(sender, "ranks command add U " + permissionManagerCommand + "U");
        }

        Bukkit.dispatchCommand(sender, "ranks create W 3325257 default &7[&7W&7]&f");if (permissionManagerCommand != null) {
            Bukkit.dispatchCommand(sender, "ranks command add W " + permissionManagerCommand + "W");
        }


        Bukkit.dispatchCommand(sender, "ranks create X 4987885 default &7[&8X&7]&f");
        if (permissionManagerCommand != null) {
            Bukkit.dispatchCommand(sender, "ranks command add X " + permissionManagerCommand + "X");
        }

        Bukkit.dispatchCommand(sender, "ranks create Y 7481828 default &7[&9Y&7]&f");
        if (permissionManagerCommand != null) {
            Bukkit.dispatchCommand(sender, "ranks command add Y " + permissionManagerCommand + "Y");
        }

        Bukkit.dispatchCommand(sender, "ranks create Z 11222742 default &7[&aZ&7]&f");
        if (permissionManagerCommand != null) {
            Bukkit.dispatchCommand(sender, "ranks command add Z " + permissionManagerCommand + "Z");
        }


        sender.sendMessage(SpigotPrison.format(messages.getString("Setup.Message.SuccessRanksSetup")));

    }

}
