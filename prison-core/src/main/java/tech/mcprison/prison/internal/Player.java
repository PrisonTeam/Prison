/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017-2020 The Prison Team
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

package tech.mcprison.prison.internal;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import tech.mcprison.prison.cache.PlayerCache;
import tech.mcprison.prison.cache.PlayerCachePlayerData;
import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.inventory.InventoryHolder;
import tech.mcprison.prison.internal.scoreboard.Scoreboard;
import tech.mcprison.prison.util.Gamemode;
import tech.mcprison.prison.util.Location;

/**
 * Represents a player on the Minecraft server.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public interface Player 
		extends CommandSender, InventoryHolder {

    /**
     * Returns the unique identifier for this player.
     */
    public UUID getUUID();

    /**
     * Returns the player's display name (nickname), which may include colors.
     */
    public String getDisplayName();
    
    /**
     * <p>This constructs a player file named based upon the UUID followed 
     * by the player's name.  This format is used so it's easier to identify
     * the correct player.
     * </p>
     * 
     * <p>The format should be UUID-PlayerName.json.  The UUID is a shortened 
     * format, which should still produce a unique id.  The name, when read, 
     * is based upon the UUID and not the player's name, which may change.
     * This format includes the player's name to make it easier to identify
     * who's record is whom's.
     * </p>
     * 
     * @return
     */
    public String getPlayerFileName();

    public String toString();
    
    /**
     * Sets the player's display name (nickname).
     *
     * @param newDisplayName The new display name. May include colors, amp-prefixed.
     */
    public void setDisplayName(String newDisplayName);

    /**
     * Adds an {@link ItemStack} to the player's inventory.
     */
    public void give(ItemStack itemStack);

    /**
     * Returns the player's current {@link Location}.
     */
    public Location getLocation();

    
    /**
     * Follows the player's line of slight to return the distant 
     * block that they are looking at.
     * 
     * @return
     */
    public Block getLineOfSightBlock();
    
    

    public List<Block> getLineOfSightBlocks();
    	
    	
    	
    /**
     * Teleports the player to another location.
     *
     * @param location The new {@link Location}.
     */
    public void teleport(Location location);

    /**
     * @return Returns true if the player is online, false otherwise.
     */
    public boolean isOnline();

    /**
     * Sets the player's visible scoreboard.
     *
     * @param scoreboard The {@link Scoreboard} to show the player.
     */
    public void setScoreboard(Scoreboard scoreboard);

    /**
     * Returns the player's current {@link Gamemode}
     */
    public Gamemode getGamemode();

    /**
     * Changes the player's {@link Gamemode} to the specified value
     *
     * @param gamemode the new gamemode
     */
    public void setGamemode(Gamemode gamemode);

    /**
     * Returns this player's locale.
     *
     * @return An {@link Optional} containing the locale of this player, or empty if it couldn't be
     * retrieved.
     */
    public Optional<String> getLocale();


    @Override 
    public default boolean doesSupportColors() {
        return true;
    }

    /**
     * Rebuilds this players inventory so that, if it has been modified, the client has an up-to-date
     * inventory. May not be necessary on all platforms but should be used where ever the player inventory
     * is modified
     */
    public void updateInventory();
    
    
//    /**
//     * Dumps the inventory contents of the player to the console.
//     * 
//     */
//    public void printDebugInventoryInformationToConsole();
    

    public void setTitle( String title, String subtitle, int fadeIn, int stay, int fadeOut );
	
    
	public void setActionBar( String actionBar );

	public PlayerCache getPlayerCache();

	public PlayerCachePlayerData getPlayerCachePlayerData();
	
	public boolean isSneaking();
	
	
	public boolean isMinecraftStatisticsEnabled();
	
	public void incrementMinecraftStatsMineBlock( Player player, String blockName, int quantity );
	
	public void incrementMinecraftStatsDropCount( Player player, String blockName, int quantity);

//	public RankPlayer getRankPlayer();
	
}
