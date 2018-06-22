package me.faizaand.prison.spigot.game;

import me.faizaand.prison.gui.GUI;
import me.faizaand.prison.internal.GamePlayer;
import me.faizaand.prison.internal.platform.GuiManager;
import me.faizaand.prison.spigot.gui.SpigotGUI;
import me.faizaand.prison.spigot.util.ActionBarUtil;
import me.faizaand.prison.util.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SpigotGuiManager implements GuiManager {

    @Override
    public void showTitle(GamePlayer player, String title, String subtitle, int fade) {
        Player play = Bukkit.getPlayer(player.getName());
        play.sendTitle(title, subtitle);
    }

    @Override
    public void showActionBar(GamePlayer player, String text, int duration) {
        Player play = Bukkit.getPlayer(player.getName());
        ActionBarUtil.sendActionBar(play, Text.translateAmpColorCodes(text), duration);
    }


    @Override
    public GUI createGUI(String title, int numRows) {
        return new SpigotGUI(title, numRows);
    }
}
