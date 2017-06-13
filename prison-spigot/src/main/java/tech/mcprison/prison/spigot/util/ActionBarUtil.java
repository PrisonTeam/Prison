package tech.mcprison.prison.spigot.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.error.Error;
import tech.mcprison.prison.spigot.SpigotPrison;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Utility for sending action bars.
 *
 * @author Faizaan A. Datoo
 */
public class ActionBarUtil {
    /*
     * This is an adaptation of the ActionBarAPI class by ConnorLinfoot. It has
     * been adapted to use Prison's APIs.
     */

    private static SpigotPrison plugin;
    private static String nmsver;
    private static boolean useOldMethods;

    public static void init(SpigotPrison plugin) {
        ActionBarUtil.plugin = plugin;

        nmsver = Bukkit.getServer().getClass().getPackage().getName();
        nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);

        if (nmsver.equalsIgnoreCase("v1_8_R1") || nmsver
            .equalsIgnoreCase("v1_7_")) { // Not sure if 1_7 works for the protocol hack?
            useOldMethods = true;
        }
    }

    public static void sendActionBar(Player player, String message) {
        try {
            Class<?> c1 = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
            Object p = c1.cast(player);
            Object ppoc;
            Class<?> c4 = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutChat");
            Class<?> c5 = Class.forName("net.minecraft.server." + nmsver + ".Packet");
            if (useOldMethods) {
                Class<?> c2 = Class.forName("net.minecraft.server." + nmsver + ".ChatSerializer");
                Class<?> c3 =
                    Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
                Method m3 = c2.getDeclaredMethod("a", String.class);
                Object cbc = c3.cast(m3.invoke(c2, "{\"text\": \"" + message + "\"}"));
                ppoc =
                    c4.getConstructor(new Class<?>[] {c3, byte.class}).newInstance(cbc, (byte) 2);
            } else {
                Class<?> c2 =
                    Class.forName("net.minecraft.server." + nmsver + ".ChatComponentText");
                Class<?> c3 =
                    Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
                Object o = c2.getConstructor(new Class<?>[] {String.class}).newInstance(message);
                ppoc = c4.getConstructor(new Class<?>[] {c3, byte.class}).newInstance(o, (byte) 2);
            }
            Method m1 = c1.getDeclaredMethod("getHandle");
            Object h = m1.invoke(p);
            Field f1 = h.getClass().getDeclaredField("playerConnection");
            Object pc = f1.get(h);
            Method m5 = pc.getClass().getDeclaredMethod("sendPacket", c5);
            m5.invoke(pc, ppoc);
        } catch (Exception ex) {
            Prison.get().getErrorManager().throwError(
                new Error("Could not send action bar.").appendStackTrace("during NMS calls", ex));
        }
    }

    public static void sendActionBar(final Player player, final String message, int duration) {
        sendActionBar(player, message);

        // Send and resend the message for duration amount of time.
        if (duration >= 0) {
            new BukkitRunnable() {
                @Override public void run() {
                    sendActionBar(player, "");
                }
            }.runTaskLater(plugin, duration + 1);
        }

        while (duration > 60) {
            duration -= 60;
            int sched = duration % 60;
            new BukkitRunnable() {
                @Override public void run() {
                    sendActionBar(player, message);
                }
            }.runTaskLater(plugin, (long) sched);
        }
    }

    public static void sendActionBarToAllPlayers(String message) {
        sendActionBarToAllPlayers(message, -1);
    }

    public static void sendActionBarToAllPlayers(String message, int duration) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            sendActionBar(p, message, duration);
        }
    }

}
