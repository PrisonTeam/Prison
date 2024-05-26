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

package tech.mcprison.prison;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import tech.mcprison.prison.cache.PlayerCache;
import tech.mcprison.prison.cache.PlayerCachePlayerData;
import tech.mcprison.prison.file.JsonFileIO;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.inventory.Inventory;
import tech.mcprison.prison.internal.scoreboard.Scoreboard;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.util.Gamemode;
import tech.mcprison.prison.util.Location;

/**
 * @author Faizaan A. Datoo
 */
public class TestPlayer 
				implements Player {

    // just allows input to be given to the user

    private List<String> input = new ArrayList<>();

    public List<String> getInput() {
        return input;
    }

    @Override public String getName() {
        return "Testing";
    }

    @Override 
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
    public String getPlayerFileName() {
    	
    	return JsonFileIO.filenamePlayer( this );
    }
    
    @Override public void updateInventory() {
    }

    @Override public void dispatchCommand(String command) {

    }

    @Override public boolean hasPermission(String perm) {
        return true;
    }

    @Override public void sendMessage(String message) {
        System.out.println(message);
        input.add(message);
    }

    @Override public void sendMessage(String[] messages) {
        input.addAll(Arrays.asList(messages));
    }

    @Override public void sendRaw(String json) {
        input.add(json);
    }

    @Override public UUID getUUID() {
        return null;
    }

    @Override public String getDisplayName() {
        return null;
    }

    @Override public void setDisplayName(String newDisplayName) {

    }

    @Override public void give(ItemStack itemStack) {

    }

    @Override public Location getLocation() {
        return null;
    }
    
	@Override
    public Block getLineOfSightBlock() {
		return null;
	}
	
	@Override
    public List<tech.mcprison.prison.internal.block.Block> getLineOfSightBlocks() {
    	
    	List<tech.mcprison.prison.internal.block.Block> results = new ArrayList<>();
    	return results;
	}
	
	
    @Override public void teleport(Location location) {

    }

    @Override public boolean isOnline() {
        return true;
    }

    @Override public void setScoreboard(Scoreboard scoreboard) {

    }

    @Override public Gamemode getGamemode() {
        return null;
    }

    @Override public void setGamemode(Gamemode gamemode) {

    }

    @Override public Optional<String> getLocale() {
        return Optional.of("en_US");
    }

    @Override public boolean isOp() {
        return true;
    }

    @Override
    public boolean isPlayer() {
    	return false;
    }
    
    @Override public Inventory getInventory() {
        return null;
    }
    
//    @Override
//    public void printDebugInventoryInformationToConsole() {
//    	
//    }
    
    public void recalculatePermissions() {
    	
    }
    
    
    @Override
    public List<String> getPermissions() {
    	List<String> results = new ArrayList<>();
    	
    	return results;
    }
    
    @Override
    public List<String> getPermissions( String prefix ) {
    	List<String> results = new ArrayList<>();
    	
    	for ( String perm : getPermissions() ) {
			if ( perm.startsWith( prefix ) ) {
				results.add( perm );
			}
		}
    	
    	return results;
    }
    
	@Override
	public List<String> getPermissionsIntegrations( boolean detailed ) {
		List<String> results = new ArrayList<>();
		
		return results;
	}

    
    @Override
    public double getSellAllMultiplier() {
    	return 1.0;
    }
    
	@Override
	public void setTitle( String title, String subtitle, int fadeIn, int stay, int fadeOut ) {
	}
	
	@Override
	public void setActionBar( String actionBar ) {
	}

	@Override
	public PlayerCache getPlayerCache()
	{
		return null;
	}

	@Override
	public PlayerCachePlayerData getPlayerCachePlayerData()
	{
		return null;
	}
	
	@Override
	public boolean isSneaking() {
		return false;
	}
	
	@Override
	public boolean isMinecraftStatisticsEnabled() {
		return false;
	}
	

	@Override
	public void incrementMinecraftStatsMineBlock( Player player, String blockName, int quantity) {
		
	}
	
	@Override
	public void incrementMinecraftStatsDropCount( Player player, String blockName, int quantity) {
		
	}

	@Override
	public List<String> getSellAllMultiplierListings() {
		return new ArrayList<>();
	}


	@Override
	public void sendMessage(List<String> messages) {
		
	}

	@Override
	public Player getPlatformPlayer() {
		return null;
	}

	@Override
	public RankPlayer getRankPlayer() {
		return null;
	}
	
}
