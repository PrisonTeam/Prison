/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2016 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.prison.spigot;

import io.github.prison.Prison;
import io.github.prison.internal.ItemStack;
import io.github.prison.internal.events.PlayerChatEvent;
import io.github.prison.util.Block;
import io.github.prison.util.Location;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;

/**
 * Posts Prison's internal events.
 *
 * @author SirFaizdat
 */
public class SpigotListener implements Listener {

    private SpigotPrison spigotPrison;

    public SpigotListener(SpigotPrison spigotPrison) {
        this.spigotPrison = spigotPrison;
    }

    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this.spigotPrison);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Prison.getInstance().getEventBus().post(new io.github.prison.internal.events.PlayerJoinEvent(new SpigotPlayer(e.getPlayer())));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Prison.getInstance().getEventBus().post(new io.github.prison.internal.events.PlayerQuitEvent(new SpigotPlayer(e.getPlayer())));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Prison.getInstance().getEventBus().post(new io.github.prison.internal.events.BlockPlaceEvent(
                e.isCancelled(),
                Block.getBlock(e.getBlock().getTypeId()),
                (new SpigotPlayer(e.getPlayer()))
        ));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        // TODO Accept air events (block is null when air is clicked...)
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_AIR) return;

        // This one's a workaround for the double-interact event glitch.
        // The wand can only be used in the main hand
        if (e.getHand() != EquipmentSlot.HAND) return;

        org.bukkit.Location block = e.getClickedBlock().getLocation();
        Prison.getInstance().getEventBus().post(new io.github.prison.internal.events.PlayerInteractEvent(
                new SpigotPlayer(e.getPlayer()),
                bukkitItemStackToPrisonItemStack(e.getPlayer().getInventory().getItemInMainHand()),
                io.github.prison.internal.events.PlayerInteractEvent.Action.valueOf(e.getAction().name()),
                new Location(new SpigotWorld(block.getWorld()), block.getX(), block.getY(), block.getZ())
        ));
    }

    private ItemStack bukkitItemStackToPrisonItemStack(org.bukkit.inventory.ItemStack bis) {
        String typeName = bis.getType().name().replace("_", " ").toLowerCase();
        String name = bis.hasItemMeta() ? (bis.getItemMeta().hasDisplayName() ? bis.getItemMeta().getDisplayName() : typeName) : typeName;
        int amount = bis.getAmount();
        Block block = Block.getBlock(bis.getType().getId());
        return new ItemStack(name, amount, block);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        PlayerChatEvent event = new PlayerChatEvent(new SpigotPlayer(e.getPlayer()), e.getMessage());
        Prison.getInstance().getEventBus().post(event);
        if (event.isCancelled()) e.setCancelled(true);
    }

}
