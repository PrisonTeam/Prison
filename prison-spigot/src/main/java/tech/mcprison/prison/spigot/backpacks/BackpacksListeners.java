package tech.mcprison.prison.spigot.backpacks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.compat.Compatibility;
import tech.mcprison.prison.spigot.compat.SpigotCompatibility;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.gui.ListenersPrisonManager;

public class BackpacksListeners implements Listener {

    Compatibility compat = SpigotCompatibility.getInstance();

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoinBackpack(PlayerJoinEvent e){
        defaultBackpackSetOnJoin(e);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBackpackCloseEvent(InventoryCloseEvent e){
        saveBackpackEdited(e);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDeadBackpack(PlayerDeathEvent e){
        onDeathBackpackAction(e);
    }



    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerBackpackEdit(InventoryClickEvent e){
        backpackEditEvent(e);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerClickBackpackItem(PlayerInteractEvent e){

        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        backpackItemClickAction(e);
    }

    private void onDeathBackpackAction(PlayerDeathEvent e) {

        BackpacksUtil bUtil = BackpacksUtil.get();

        if (bUtil == null){
            return;
        }

        //TODO
        // Add life-saver "totems" in the future, an extra condition that will check if a player is immune to this, maybe he got
        // A special usable item, a permission as a VIP, something so he won't lose the inventory.
        // This option should be added in the future in the config with a whole "equipe" of commands and configs.
        if (bUtil.isBackpackLostOnDeath()){
            bUtil.resetBackpacks(e.getEntity());
        }
    }

    private void defaultBackpackSetOnJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();
        BackpacksUtil bUtil = BackpacksUtil.get();

        if (bUtil == null){
            return;
        }

        // Check if Player already owns Backpack for some reasons.
        if (bUtil.getBackpacks(p) == null){
            bUtil.createBackpack(p);
        }

        bUtil.giveBackpackOpenItem(p);
    }

    private void saveBackpackEdited(InventoryCloseEvent e) {

        BackpacksUtil bUtil = BackpacksUtil.get();

        if (bUtil == null){
            return;
        }

        if (ListenersPrisonManager.activeGui.contains((Player) e.getPlayer())){

            String title = compat.getGUITitle(e);
            Player p = (Player) e.getPlayer();

            if (title == null){
                return;
            }

            String id;
            int idNumber;

            title = title.substring(2);
            try {
                id = title.substring(e.getPlayer().getName().length() + 13);
            } catch (IndexOutOfBoundsException ignored){
                return;
            }

            try {
               idNumber = Integer.parseInt(id);
            } catch (NumberFormatException ignored){
                return;
            }

            if (title.equalsIgnoreCase(p.getName() + " -> Backpack-" + id)){

                if (bUtil.isBackpackCloseSoundEnabled()){
                    bUtil.playBackpackCloseSound((Player) e.getPlayer());
                }

                if (bUtil.getEditedBackpack().contains(p)){
                    bUtil.setBackpack(p, e.getInventory(), idNumber);
                    bUtil.removeFromEditedBackpack(p);
                    Output.get().sendInfo(new SpigotPlayer(p), "Backpack closed with success!");
                }
            }
        }
    }

    private void backpackEditEvent(InventoryClickEvent e) {

        BackpacksUtil bUtil = BackpacksUtil.get();

        if (bUtil == null){
            return;
        }

        Player p = (Player) e.getWhoClicked();

        if (ListenersPrisonManager.activeGui.contains(p)) {

            String title = compat.getGUITitle(e);
            String id;

            if (title == null){
                return;
            }
            title = title.substring(2);

            try {
                id = title.substring(e.getWhoClicked().getName().length() + 13);
            } catch (IndexOutOfBoundsException ignored){
                return;
            }

            if (title.equalsIgnoreCase(e.getWhoClicked().getName() + " -> Backpack-" + id)){
                bUtil.addToEditedBackpack(p);
            }
        }
    }

    private void backpackItemClickAction(PlayerInteractEvent e) {

        BackpacksUtil bUtil = BackpacksUtil.get();

        if (bUtil == null){
            return;
        }

        Player p = e.getPlayer();
        ItemStack inHandItem = SpigotCompatibility.getInstance().getItemInMainHand(p);
        ItemStack materialConf = bUtil.getBackpackOpenItem();

        if (materialConf == null){
            return;
        }

        if (inHandItem == null){
            return;
        }

        if (inHandItem.equals(materialConf)) {
            Bukkit.dispatchCommand(p, "gui backpackslist");
            e.setCancelled(true);
        }
    }

    /**
     * Java getBoolean's broken so I made my own.
     * */
    public boolean getBoolean(String string){
        return string != null && string.equalsIgnoreCase("true");
    }

}
