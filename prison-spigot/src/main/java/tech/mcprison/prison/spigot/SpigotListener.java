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

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.block.BrewingStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.events.inventory.InventoryClickEvent;
import tech.mcprison.prison.internal.events.inventory.InventoryEvent;
import tech.mcprison.prison.internal.events.player.PlayerChatEvent;
import tech.mcprison.prison.internal.events.player.PlayerPickUpItemEvent;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.spigot.compat.Compatibility;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.game.SpigotWorld;
import tech.mcprison.prison.spigot.inventory.SpigotBrewer;
import tech.mcprison.prison.spigot.inventory.SpigotCrafting;
import tech.mcprison.prison.spigot.inventory.SpigotInventory;
import tech.mcprison.prison.spigot.inventory.SpigotInventoryView;
import tech.mcprison.prison.spigot.inventory.SpigotRecipe;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.ChatColor;
import tech.mcprison.prison.util.InventoryType;
import tech.mcprison.prison.util.Location;

/**
 * Posts Prison's internal events.
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

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e) {
    Prison.get().getEventBus().post(
        new tech.mcprison.prison.internal.events.player.PlayerJoinEvent(
            new SpigotPlayer(e.getPlayer())));
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent e) {
    Prison.get().getEventBus().post(
        new tech.mcprison.prison.internal.events.player.PlayerQuitEvent(
            new SpigotPlayer(e.getPlayer())));
  }

  @EventHandler
  public void onPlayerKicked(PlayerKickEvent e) {
    Prison.get().getEventBus().post(
        new tech.mcprison.prison.internal.events.player.PlayerKickEvent(
            new SpigotPlayer(e.getPlayer()), e.getReason()));
  }

  @EventHandler
  public void onBlockPlace(BlockPlaceEvent e) {
    org.bukkit.Location block = e.getBlockPlaced().getLocation();
    tech.mcprison.prison.internal.events.block.BlockPlaceEvent event =
        new tech.mcprison.prison.internal.events.block.BlockPlaceEvent(
            BlockType.getBlock(e.getBlock().getTypeId()),
            new Location(new SpigotWorld(block.getWorld()), block.getX(), block.getY(),
                block.getZ()), (new SpigotPlayer(e.getPlayer())));
    Prison.get().getEventBus().post(event);
    e.setCancelled(event.isCanceled());
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent e) {
    org.bukkit.Location block = e.getBlock().getLocation();
    tech.mcprison.prison.internal.events.block.BlockBreakEvent event =
        new tech.mcprison.prison.internal.events.block.BlockBreakEvent(
            BlockType.getBlock(e.getBlock().getTypeId()),
            new Location(new SpigotWorld(block.getWorld()), block.getX(), block.getY(),
                block.getZ()), (new SpigotPlayer(e.getPlayer())));
    Prison.get().getEventBus().post(event);
    e.setCancelled(event.isCanceled());
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent e) {
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
    tech.mcprison.prison.internal.events.player.PlayerInteractEvent event =
        new tech.mcprison.prison.internal.events.player.PlayerInteractEvent(
            new SpigotPlayer(e.getPlayer()),
            SpigotUtil.bukkitItemStackToPrison(spigotPrison.compatibility.getItemInMainHand(e)),
            tech.mcprison.prison.internal.events.player.PlayerInteractEvent.Action
                .valueOf(e.getAction().name()),
            new Location(new SpigotWorld(block.getWorld()), block.getX(), block.getY(),
                block.getZ()));
    Prison.get().getEventBus().post(event);
    e.setCancelled(event.isCanceled());
  }

  @EventHandler
  public void onPlayerDropItem(PlayerDropItemEvent e) {
    tech.mcprison.prison.internal.events.player.PlayerDropItemEvent event =
        new tech.mcprison.prison.internal.events.player.PlayerDropItemEvent(
            new SpigotPlayer(e.getPlayer()),
            SpigotUtil.bukkitItemStackToPrison(e.getItemDrop().getItemStack()));
    Prison.get().getEventBus().post(event);
    e.setCancelled(event.isCanceled());
  }

  @EventHandler
  public void onPlayerPickUpItem(PlayerPickupItemEvent e) {
    PlayerPickUpItemEvent event = new PlayerPickUpItemEvent(new SpigotPlayer(e.getPlayer()),
        SpigotUtil.bukkitItemStackToPrison(e.getItem().getItemStack()));
    Prison.get().getEventBus().post(event);
    e.setCancelled(event.isCanceled());
  }

  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent e) {
    PlayerChatEvent event =
        new PlayerChatEvent(new SpigotPlayer(e.getPlayer()), e.getMessage(), e.getFormat());
    Prison.get().getEventBus().post(event);
    e.setFormat(ChatColor.translateAlternateColorCodes('&', event.getFormat() + "&r"));
    e.setMessage(event.getMessage());
    e.setCancelled(event.isCanceled());
  }

  @EventHandler
  public void onBrew(BrewEvent e) {
    tech.mcprison.prison.internal.events.inventory.BrewEvent event =
        new tech.mcprison.prison.internal.events.inventory.BrewEvent(
            new SpigotBlock(e.getBlock()), new SpigotBrewer(e.getContents()),
            ((BrewingStand) e.getBlock()).getFuelLevel());
    Prison.get().getEventBus().post(event);
    e.setCancelled(event.isCanceled());
  }

  @EventHandler
  public void onCraftItem(CraftItemEvent e) {
    tech.mcprison.prison.internal.events.inventory.CraftItemEvent event =
        new tech.mcprison.prison.internal.events.inventory.CraftItemEvent(
            new SpigotRecipe(e.getRecipe()), new SpigotInventoryView(e.getView()),
            SpigotUtil.bukkitSlotTypeToPrison(e.getSlotType()), e.getRawSlot(),
            InventoryClickEvent.Click.valueOf(e.getClick().toString()),
            InventoryEvent.Action.valueOf(e.getAction().toString()));
    Prison.get().getEventBus().post(event);
    e.setCancelled(event.isCanceled());
    e.setCurrentItem(SpigotUtil.prisonItemStackToBukkit(event.getCurrentItem()));
  }

  @EventHandler
  public void onFurnaceBurn(FurnaceBurnEvent e) {
    tech.mcprison.prison.internal.events.inventory.FurnaceBurnEvent event =
        new tech.mcprison.prison.internal.events.inventory.FurnaceBurnEvent(
            new SpigotBlock(e.getBlock()), SpigotUtil.bukkitItemStackToPrison(e.getFuel()),
            e.getBurnTime(), e.isBurning());
    Prison.get().getEventBus().post(event);
    e.setCancelled(event.isCanceled());
    e.setBurning(event.isBurning());
    e.setBurnTime(event.getBurnTime());
  }

  @EventHandler
  public void onFurnaceExtract(FurnaceExtractEvent e) {
    tech.mcprison.prison.internal.events.inventory.FurnaceExtractEvent event =
        new tech.mcprison.prison.internal.events.inventory.FurnaceExtractEvent(
            new SpigotPlayer(e.getPlayer()), new SpigotBlock(e.getBlock()),
            SpigotUtil.materialToBlockType(e.getItemType()), e.getItemAmount(),
            e.getExpToDrop());
    Prison.get().getEventBus().post(event);
    e.setExpToDrop(event.getExpToDrop());
  }

  @EventHandler
  public void onFurnaceSmelt(FurnaceSmeltEvent e) {
    tech.mcprison.prison.internal.events.inventory.FurnaceSmeltEvent event =
        new tech.mcprison.prison.internal.events.inventory.FurnaceSmeltEvent(
            new SpigotBlock(e.getBlock()), SpigotUtil.bukkitItemStackToPrison(e.getSource()),
            SpigotUtil.bukkitItemStackToPrison(e.getResult()));
    Prison.get().getEventBus().post(event);
    e.setCancelled(e.isCancelled());
  }

  @EventHandler
  public void onInventoryClose(InventoryCloseEvent e) {
    tech.mcprison.prison.internal.events.inventory.InventoryCloseEvent event =
        new tech.mcprison.prison.internal.events.inventory.InventoryCloseEvent(
            new SpigotInventoryView(e.getView()));
    Prison.get().getEventBus().post(event);
  }

  @EventHandler
  public void onInventoryCreative(InventoryCreativeEvent e) {
    tech.mcprison.prison.internal.events.inventory.InventoryCreativeEvent event =
        new tech.mcprison.prison.internal.events.inventory.InventoryCreativeEvent(
            new SpigotInventoryView(e.getView()),
            InventoryType.SlotType.valueOf(e.getSlotType().name()), e.getSlot(),
            SpigotUtil.bukkitItemStackToPrison(e.getCursor()));
    Prison.get().getEventBus().post(event);
    e.setCursor(SpigotUtil.prisonItemStackToBukkit(event.getCursor()));
    e.setCancelled(event.isCanceled());
    e.setCurrentItem(SpigotUtil.prisonItemStackToBukkit(event.getCurrentItem()));
  }

  @EventHandler
  public void onInventoryDrag(InventoryDragEvent e) {
    HashMap<Integer, ItemStack> slots = new HashMap<>();
    e.getNewItems().entrySet()
        .forEach(x -> slots.put(x.getKey(), SpigotUtil.bukkitItemStackToPrison(x.getValue())));
    tech.mcprison.prison.internal.events.inventory.InventoryDragEvent event =
        new tech.mcprison.prison.internal.events.inventory.InventoryDragEvent(
            new SpigotInventoryView(e.getView()),
            SpigotUtil.bukkitItemStackToPrison(e.getCursor()),
            SpigotUtil.bukkitItemStackToPrison(e.getOldCursor()),
            e.getType() == DragType.SINGLE, slots);
    Prison.get().getEventBus().post(event);
    e.setCancelled(event.isCanceled());
    e.setCursor(SpigotUtil.prisonItemStackToBukkit(event.getCursor()));
  }

  @EventHandler
  public void onInventoryMoveItem(InventoryMoveItemEvent e) {
    tech.mcprison.prison.internal.events.inventory.InventoryMoveItemEvent event =
        new tech.mcprison.prison.internal.events.inventory.InventoryMoveItemEvent(
            new SpigotInventory(e.getSource()), SpigotUtil.bukkitItemStackToPrison(e.getItem()),
            new SpigotInventory(e.getDestination()), e.getSource() == e.getInitiator());
    Prison.get().getEventBus().post(event);
    e.setCancelled(event.isCanceled());
    e.setItem(SpigotUtil.prisonItemStackToBukkit(event.getItem()));
  }

  @EventHandler
  public void onInventoryOpen(InventoryOpenEvent e) {
    tech.mcprison.prison.internal.events.inventory.InventoryOpenEvent event =
        new tech.mcprison.prison.internal.events.inventory.InventoryOpenEvent(
            new SpigotInventoryView(e.getView()));
    Prison.get().getEventBus().post(event);
    e.setCancelled(event.isCanceled());
  }

  @EventHandler
  public void onPrepareAnvil(PrepareAnvilEvent e) {
    tech.mcprison.prison.internal.events.inventory.PrepareAnvilEvent event =
        new tech.mcprison.prison.internal.events.inventory.PrepareAnvilEvent(
            new SpigotInventoryView(e.getView()),
            SpigotUtil.bukkitItemStackToPrison(e.getResult()));
    Prison.get().getEventBus().post(event);
    e.setResult(SpigotUtil.prisonItemStackToBukkit(event.getResult()));
  }

  @EventHandler
  public void onPrepareItemCraft(PrepareItemCraftEvent e) {
    tech.mcprison.prison.internal.events.inventory.PrepareItemCraftEvent event =
        new tech.mcprison.prison.internal.events.inventory.PrepareItemCraftEvent(
            new SpigotInventoryView(e.getView()), new SpigotCrafting(e.getInventory()),
            e.isRepair());
    Prison.get().getEventBus().post(event);
  }

}
