/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017 The Prison Team
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
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldLoadEvent;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.events.Cancelable;
import tech.mcprison.prison.internal.events.player.PlayerChatEvent;
import tech.mcprison.prison.internal.events.player.PlayerPickUpItemEvent;
import tech.mcprison.prison.internal.events.world.PrisonWorldLoadEvent;
import tech.mcprison.prison.spigot.compat.Compatibility;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.game.SpigotWorld;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.ChatColor;
import tech.mcprison.prison.util.Location;

/**
 * Posts Prison's internal events.
 *
 * <p>getTypeId() was deprecated with 1.9.4.
 * </p>
 *  
 * <p>PlayerPickupItemEvent is deprecated with spigot v1.12.2.
 * </p>
 * 
 * @author Faizaan A. Datoo
 */
//@SuppressWarnings( "deprecation" )
@SuppressWarnings( "deprecation" )
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
            new tech.mcprison.prison.internal.events.player.PlayerJoinEvent(
                new SpigotPlayer(e.getPlayer())));
    }

    @EventHandler public void onPlayerQuit(PlayerQuitEvent e) {
        Prison.get().getEventBus().post(
            new tech.mcprison.prison.internal.events.player.PlayerQuitEvent(
                new SpigotPlayer(e.getPlayer())));
    }

    @EventHandler public void onPlayerKicked(PlayerKickEvent e) {
        Prison.get().getEventBus().post(
            new tech.mcprison.prison.internal.events.player.PlayerKickEvent(
                new SpigotPlayer(e.getPlayer()), e.getReason()));
    }

    // @SuppressWarnings( "deprecation" ) // deprecated with v1.9.4
	@EventHandler public void onBlockPlace(BlockPlaceEvent e) {
        org.bukkit.Location block = e.getBlockPlaced().getLocation();
        tech.mcprison.prison.internal.events.block.BlockPlaceEvent event =
            new tech.mcprison.prison.internal.events.block.BlockPlaceEvent(
                BlockType.getBlock(e.getBlock().getTypeId()),
                new Location(new SpigotWorld(block.getWorld()), block.getX(), block.getY(),
                    block.getZ()), (new SpigotPlayer(e.getPlayer())));
        Prison.get().getEventBus().post(event);
        doCancelIfShould(event, e);
    }

    // @SuppressWarnings( "deprecation" ) // deprecated with v1.9.4
	@EventHandler public void onBlockBreak(BlockBreakEvent e) {
        org.bukkit.Location block = e.getBlock().getLocation();
        tech.mcprison.prison.internal.events.block.BlockBreakEvent event =
            new tech.mcprison.prison.internal.events.block.BlockBreakEvent(
                BlockType.getBlock(e.getBlock().getTypeId()),
                new Location(new SpigotWorld(block.getWorld()), block.getX(), block.getY(),
                    block.getZ()), (new SpigotPlayer(e.getPlayer())),e.getExpToDrop());
        Prison.get().getEventBus().post(event);
        doCancelIfShould(event, e);
    }
    
    /**
     * <p>Monitors when new worlds are loaded, then it fires off a Prison's version of the
     * same event type.  This is used to initialize mines that are waiting for world to be
     * loaded through plugins such as Multiverse-core.
     * </p>
     * 
     * @param e The world event
     */
    @EventHandler
    public void onWorldLoadEvent( WorldLoadEvent e ) {
    	PrisonWorldLoadEvent pwlEvent = new PrisonWorldLoadEvent(e.getWorld().getName());
    	
    	Prison.get().getEventBus().post(pwlEvent);
    }

    @EventHandler public void onPlayerInteract(PlayerInteractEvent e) {
        // TODO Accept air events (block is null when air is clicked...)

        // Check to see if we support the Action
        tech.mcprison.prison.internal.events.player.PlayerInteractEvent.Action[] values = tech.mcprison.prison.internal.events.player.PlayerInteractEvent.Action.values();
        boolean has = false;
        for (tech.mcprison.prison.internal.events.player.PlayerInteractEvent.Action value : values) {
            if(value.name().equals(e.getAction().name())) has = true;
        }
        if(!has) return; // we don't support this Action

        // This one's a workaround for the double-interact event glitch.
        // The wand can only be used in the main hand
        if (spigotPrison.compatibility.getHand(e) != Compatibility.EquipmentSlot.HAND) {
            return;
        }

        org.bukkit.Location block = e.getClickedBlock().getLocation();
        tech.mcprison.prison.internal.events.player.PlayerInteractEvent event =
            new tech.mcprison.prison.internal.events.player.PlayerInteractEvent(
                new SpigotPlayer(e.getPlayer()),
                SpigotUtil.bukkitItemStackToPrison(spigotPrison.compatibility.getItemInMainHand(e)),
                tech.mcprison.prison.internal.events.player.PlayerInteractEvent.Action
                    .valueOf(e.getAction().name()),
                new Location(new SpigotWorld(block.getWorld()), block.getX(), block.getY(),
                    block.getZ()));
        Prison.get().getEventBus().post(event);
        doCancelIfShould(event, e);
    }

    @EventHandler public void onPlayerDropItem(PlayerDropItemEvent e) {
        tech.mcprison.prison.internal.events.player.PlayerDropItemEvent event =
            new tech.mcprison.prison.internal.events.player.PlayerDropItemEvent(
                new SpigotPlayer(e.getPlayer()),
                SpigotUtil.bukkitItemStackToPrison(e.getItemDrop().getItemStack()));
        Prison.get().getEventBus().post(event);
        doCancelIfShould(event, e);
    }

    // deprecated with v1.9.4
    // spigot 1.12.4 deprecates PlayerPickupItemEvent
    //@SuppressWarnings( "deprecation" ) 
	@EventHandler public void onPlayerPickUpItem(PlayerPickupItemEvent e) {
        PlayerPickUpItemEvent event = new PlayerPickUpItemEvent(new SpigotPlayer(e.getPlayer()),
            SpigotUtil.bukkitItemStackToPrison(e.getItem().getItemStack()));
        Prison.get().getEventBus().post(event);
        doCancelIfShould(event, e);
    }

    @EventHandler(priority=EventPriority.LOW) 
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        PlayerChatEvent event =
            new PlayerChatEvent(new SpigotPlayer(e.getPlayer()), e.getMessage(), e.getFormat());
        Prison.get().getEventBus().post(event);
        e.setFormat(ChatColor.translateAlternateColorCodes('&', event.getFormat() + "&r"));
        e.setMessage(event.getMessage());
        doCancelIfShould(event, e);
    }

    private void doCancelIfShould(Cancelable ours, Cancellable theirs) {
        if(ours.isCanceled()) {
            // We shouldn't set this to false, because some event handlers check for that.
            theirs.setCancelled(true);
        }
    }

}
