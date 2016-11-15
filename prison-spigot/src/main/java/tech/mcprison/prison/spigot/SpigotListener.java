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

package tech.mcprison.prison.spigot;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.platform.ItemStack;
import tech.mcprison.prison.platform.events.PlayerChatEvent;
import tech.mcprison.prison.spigot.compat.Compatibility;
import tech.mcprison.prison.util.Block;
import tech.mcprison.prison.util.ChatColor;
import tech.mcprison.prison.util.Location;

/**
 * Posts Prison's platform events.
 *
 * @author Faizaan A. Datoo
 */
public class SpigotListener implements Listener {

    private SpigotPrison spigotPrison;

    public SpigotListener(SpigotPrison spigotPrison) {
        this.spigotPrison = spigotPrison;
    }

    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this.spigotPrison);
    }

    @EventHandler public void onPlayerJoin(PlayerJoinEvent e) {
        Prison.get().getEventBus().post(
            new tech.mcprison.prison.platform.events.PlayerJoinEvent(
                new SpigotPlayer(e.getPlayer())));
    }

    @EventHandler public void onPlayerQuit(PlayerQuitEvent e) {
        Prison.get().getEventBus().post(
            new tech.mcprison.prison.platform.events.PlayerQuitEvent(
                new SpigotPlayer(e.getPlayer())));
    }

    @EventHandler public void onBlockPlace(BlockPlaceEvent e) {
        org.bukkit.Location block = e.getBlockPlaced().getLocation();
        tech.mcprison.prison.platform.events.BlockPlaceEvent event =
            new tech.mcprison.prison.platform.events.BlockPlaceEvent(
                Block.getBlock(e.getBlock().getTypeId()),
                new Location(new SpigotWorld(block.getWorld()), block.getX(), block.getY(),
                    block.getZ()), (new SpigotPlayer(e.getPlayer())));
        Prison.get().getEventBus().post(event);
        e.setCancelled(event.isCanceled());
    }

    @EventHandler public void onPlayerInteract(PlayerInteractEvent e) {
        // TODO Accept air events (block is null when air is clicked...)
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_AIR) {
            return;
        }

        // This one's a workaround for the double-interact event glitch.
        // The wand can only be used in the main hand
        if (spigotPrison.compatibility.getHand(e) != Compatibility.EquipmentSlot.HAND) {
            return;
        }

        org.bukkit.Location block = e.getClickedBlock().getLocation();
        tech.mcprison.prison.platform.events.PlayerInteractEvent event =
            new tech.mcprison.prison.platform.events.PlayerInteractEvent(
                new SpigotPlayer(e.getPlayer()),
                bukkitItemStackToPrisonItemStack(spigotPrison.compatibility.getItemInMainHand(e)),
                tech.mcprison.prison.platform.events.PlayerInteractEvent.Action
                    .valueOf(e.getAction().name()),
                new Location(new SpigotWorld(block.getWorld()), block.getX(), block.getY(),
                    block.getZ()));
        Prison.get().getEventBus().post(event);
        e.setCancelled(event.isCanceled());
    }

    private ItemStack bukkitItemStackToPrisonItemStack(org.bukkit.inventory.ItemStack bis) {
        String typeName = bis.getType().name().replace("_", " ").toLowerCase();
        String name = bis.hasItemMeta() ?
            (bis.getItemMeta().hasDisplayName() ? bis.getItemMeta().getDisplayName() : typeName) :
            typeName;
        int amount = bis.getAmount();
        Block block = Block.getBlock(bis.getType().getId());
        return new ItemStack(name, amount, block);
    }

    @EventHandler public void onPlayerChat(AsyncPlayerChatEvent e) {
        PlayerChatEvent event =
            new PlayerChatEvent(new SpigotPlayer(e.getPlayer()), e.getMessage(), e.getFormat());
        Prison.get().getEventBus().post(event);
        e.setFormat(ChatColor.translateAlternateColorCodes('&', event.getFormat() + "&r"));
        e.setMessage(event.getMessage());
        e.setCancelled(event.isCanceled());
    }

}
