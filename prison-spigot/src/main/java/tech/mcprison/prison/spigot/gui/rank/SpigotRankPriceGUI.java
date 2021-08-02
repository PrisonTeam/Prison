package tech.mcprison.prison.spigot.gui.rank;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.gui.guiutility.Button;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

import java.util.List;

/**
 * @author GABRYCA
 */
public class SpigotRankPriceGUI extends SpigotGUIComponents {

    private final Player p;
    private final String rankName;
    private final Integer val;

    public SpigotRankPriceGUI(Player p, Integer val, String rankName){
        this.p = p;
        this.val = val;
        this.rankName = rankName;
    }

    public void open() {

        // Check if Ranks are enabled
        if (!(checkRanks(p))){
            return;
        }

        int dimension = 45;
        PrisonGUI gui = new PrisonGUI(p, dimension, "&3RankManager -> RankPrice");

        // Create a new lore
        List<String> changeDecreaseValueLore = createLore(
                messages.getString("Lore.ClickToDecrease")
        );
        List<String> confirmButtonLore;
        confirmButtonLore = createLore(
                messages.getString("Lore.LeftClickToConfirm"),
                messages.getString("Lore.Price2") + val,
                messages.getString("Lore.RightClickToCancel")
        );
        List<String> changeIncreaseValueLore = createLore(
                messages.getString("Lore.ClickToIncrease")
        );

        XMaterial decreaseMat = XMaterial.REDSTONE_BLOCK;

        // Decrease button
        gui.addButton(new Button(1, decreaseMat, changeDecreaseValueLore, SpigotPrison.format("&3" + rankName + " " + val + " - 1" )));
        gui.addButton(new Button(10, decreaseMat, 10, changeDecreaseValueLore, SpigotPrison.format("&3" + rankName + " " + val + " - 10")));
        gui.addButton(new Button(19, decreaseMat, changeDecreaseValueLore, SpigotPrison.format("&3" + rankName + " " + val + " - 100")));
        gui.addButton(new Button(28, decreaseMat, changeDecreaseValueLore, SpigotPrison.format("&3" + rankName + " " + val + " - 1000")));
        gui.addButton(new Button(37, decreaseMat, changeDecreaseValueLore, SpigotPrison.format("&3" + rankName + " " + val + " - 10000")));

        // Create a button and set the position
        gui.addButton(new Button(22, XMaterial.TRIPWIRE_HOOK, confirmButtonLore, SpigotPrison.format("&3" + "Confirm: " + rankName + " " + val)));

        XMaterial increaseMat = XMaterial.EMERALD_BLOCK;

        // Increase button
        gui.addButton(new Button(7, increaseMat, changeIncreaseValueLore, SpigotPrison.format("&3" + rankName + " " + val + " + 1" )));
        gui.addButton(new Button(16, increaseMat, 10, changeIncreaseValueLore, SpigotPrison.format("&3" + rankName + " " + val + " + 10")));
        gui.addButton(new Button(25, increaseMat, changeIncreaseValueLore, SpigotPrison.format("&3" + rankName + " " + val + " + 100")));
        gui.addButton(new Button(34, increaseMat, changeIncreaseValueLore, SpigotPrison.format("&3" + rankName + " " + val + " + 1000")));
        gui.addButton(new Button(43, increaseMat, changeIncreaseValueLore, SpigotPrison.format("&3" + rankName + " " + val + " + 10000")));

        gui.open();
    }
}
