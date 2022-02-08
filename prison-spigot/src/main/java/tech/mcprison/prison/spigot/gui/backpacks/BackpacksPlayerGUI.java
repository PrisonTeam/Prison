package tech.mcprison.prison.spigot.gui.backpacks;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.backpacks.BackpacksUtil;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.guiutility.PrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;

public class BackpacksPlayerGUI extends SpigotGUIComponents {

    private final Player p;
    private final int id;

    /**
     * Load Backpack of a Player if exists.
     *
     * @param p - Player.
     * @param id - Backpack id.
     * */
    public BackpacksPlayerGUI(Player p, int id){
        this.p = p;
        this.id = id;
    }

    public void open(){

        BackpacksUtil backpacksUtil = BackpacksUtil.get();
//        NewBackpacksUtil backpacksUtil = NewBackpacksUtil.get();

        if (backpacksUtil == null){
            return;
        }

        SpigotPlayer sPlayer = new SpigotPlayer(p);

        Inventory inv = backpacksUtil.getBackpack(p, Integer.toString( id ));
//        Inventory inv = backpacksUtil.getBackpack(p, id);

        if (inv != null){
            PrisonGUI gui = new PrisonGUI(p, inv.getSize(), "&3" + p.getName() + " -> Backpack-" + id);

            Output.get().sendInfo(sPlayer, "Backpack" + id + " open with success!");

//            if (backpacksUtil.isBackpackOpenSoundEnabled()){
//                p.playSound(p.getLocation(), backpacksUtil.getBackpackOpenSound(),3,1);
//            }

            gui.open();
        }

        Output.get().sendWarn(sPlayer, "Backpack ID -> " + id + " not found");
    }

}
