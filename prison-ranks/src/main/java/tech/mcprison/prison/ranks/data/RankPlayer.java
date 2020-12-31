/*
 * Copyright (C) 2017 The MC-Prison Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.ranks.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.google.gson.internal.LinkedTreeMap;

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.inventory.Inventory;
import tech.mcprison.prison.internal.scoreboard.Scoreboard;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.RankUtil;
import tech.mcprison.prison.store.Document;
import tech.mcprison.prison.util.Gamemode;
import tech.mcprison.prison.util.Location;

/**
 * Represents a player with ranks.
 *
 * @author Faizaan A. Datoo
 */
public class RankPlayer 
			implements Player {

    /*
     * Fields & Constants
     */

    private UUID uid;
    private HashMap<String, Integer> ranks; // <Ladder Name, Rank ID>
    private HashMap<String, Integer> prestige; // <Ladder Name, Prestige>
    
    private List<RankPlayerName> names;
    
    // Block name, count
    public HashMap<String, Integer> blocksMined;

    /*
     * Document-related
     */

    public RankPlayer() {
    	super();
    	
        this.ranks = new HashMap<>();
        this.prestige = new HashMap<>();
    }
    
    public RankPlayer( UUID uid ) {
    	this();
    	
    	this.uid = uid;
    }
    
    public RankPlayer( UUID uid, String playerName ) {
    	this( uid );
    	
    	checkName( playerName );
    }

    @SuppressWarnings( "unchecked" )
	public RankPlayer(Document document) {
    	
        this.uid = UUID.fromString((String) document.get("uid"));
        LinkedTreeMap<String, Object> ranksLocal =
            (LinkedTreeMap<String, Object>) document.get("ranks");
        LinkedTreeMap<String, Object> prestigeLocal =
            (LinkedTreeMap<String, Object>) document.get("prestige");
        
        LinkedTreeMap<String, Object> blocksMinedLocal =
        		(LinkedTreeMap<String, Object>) document.get("blocksMined");
        
        Object namesListObject = document.get( "names" );
        

        this.ranks = new HashMap<>();
        for (String key : ranksLocal.keySet()) {
            ranks.put(key, RankUtil.doubleToInt(ranksLocal.get(key)));
        }
        
        this.prestige = new HashMap<>();
        for (String key : prestigeLocal.keySet()) {
            prestige.put(key, RankUtil.doubleToInt(prestigeLocal.get(key)));
        }
        
        this.blocksMined = new HashMap<>();
        if ( blocksMinedLocal != null ) {
        	for (String key : blocksMinedLocal.keySet()) {
        		blocksMined.put(key, RankUtil.doubleToInt(blocksMinedLocal.get(key)));
        	}
        }
        
        if ( namesListObject != null ) {
        	
        	for ( Object rankPlayerNameMap : (ArrayList<Object>) namesListObject ) {
        		LinkedTreeMap<String, Object> rpnMap = (LinkedTreeMap<String, Object>) rankPlayerNameMap;
        		
        		if ( rpnMap.size() > 0 ) {
        			String name = (String) rpnMap.get( "name" );
        			long date = RankUtil.doubleToLong( rpnMap.get( "date" ) );
        			
        			RankPlayerName rankPlayerName = new RankPlayerName( name, date );
        			getNames().add( rankPlayerName );
//        			Output.get().logInfo( "RankPlayer: uuid: " + uid + " RankPlayerName: " + rankPlayerName.toString() );
        		}
        		
        	}
        }
        
    }

    public Document toDocument() {
        Document ret = new Document();
        ret.put("uid", this.uid);
        ret.put("ranks", this.ranks);
        ret.put("prestige", this.prestige);
        
        ret.put("names", this.names);

        ret.put("blocksMined", this.blocksMined);
        return ret;
    }

    /*
     * Methods
     */
    
    public UUID getUUID() {
    	return uid;
    }
    
    /**
     * If the player has any names in the getNames() collection, of which they may not,
     * then getDisaplyName() will return the last one set, otherwise it will return
     * a null.
     */
    public String getDisplayName() {
    	return getLastName();
    }
    
    public void setDisplayName(String newDisplayName) {
    	checkName( newDisplayName );
    }
    
    public boolean isOnline() {
    	return false;
    }
    
    /**
     * <p>Check to see what the last instance of the player's name was, if it 
     * is a new name, then go ahead and add it to the player's name list.
     * If a check was ran from the CONSOLE then it could feed in the name
     * CONSOLE so exclude that.
     * </p>
     * 
     * @param playerName
     * @return
     */
    public boolean checkName( String playerName ) {
    	boolean added = false;
    	
    	// If the playerName is not valid, don't try to add it:
    	if ( playerName != null && playerName.trim().length() > 0 && 
    			!"CONSOLE".equalsIgnoreCase( playerName ) ) {
    		
    		String name = getLastName();
    		
    		// Check if the last name in the list is not the same as the name passed:
    		if ( name != null && !name.equalsIgnoreCase( playerName ) ) {
    			
    			RankPlayerName rpn = new RankPlayerName( playerName, System.currentTimeMillis() );
    			getNames().add( rpn );
    			
    			added = true;
    		}
    	}
    	
    	return added;
    }
    
    private String getLastName() {
    	String name = getNames().size() == 0 ?
    			null :
    			getNames().get( getNames().size() - 1 ).getName();
    	
    	return name;
    }
    

    public List<RankPlayerName> getNames() {
    	if ( names == null ) {
    		names = new ArrayList<>();
    	}
		return names;
	}
	public void setNames( List<RankPlayerName> names ) {
		this.names = names;
	}

	public HashMap<String, Integer> getBlocksMined() {
		return blocksMined;
	}
	public void setBlocksMined( HashMap<String, Integer> blocksMined ) {
		this.blocksMined = blocksMined;
	}

	/**
     * <p>This is a helper function to ensure that the given file name is 
     * always generated correctly and consistently.
     * </p>
     * 
     * @return "player_" plus the least significant bits of the UID
     */
    public String filename()
    {
    	return "player_" + uid.getLeastSignificantBits();
    }
    
    
    /**
     * Add a rank to this player.
     * If a rank on this ladder is already attached, it will automatically be removed and replaced with this new one.
     *
     * @param ladder The {@link RankLadder} that this rank belongs to.
     * @param rank   The {@link Rank} to add.
     * @throws IllegalArgumentException If the rank specified is not on this ladder.
     */
    public void addRank(RankLadder ladder, Rank rank) {
        if (!ladder.containsRank(rank.getId())) {
            throw new IllegalArgumentException("Rank must be on ladder.");
        }

        // Remove the current rank on this ladder first
        if (ranks.containsKey(ladder.getName())) {
            ranks.remove(ladder.getName());
        }

        ranks.put(ladder.getName(), rank.getId());
    }

    /**
     * Remove a rank from this player.
     * This will also remove the ladder from this player.
     *
     * @param rank The The {@link Rank} to remove.
     */
    public void removeRank(Rank rank) {

        // When we loop through, we have to store our ladder name outside the loop to
        // avoid a concurrent modification exception. So, we'll retrieve the data we need...
        String ladderName = null;
        for (Map.Entry<String, Integer> rankEntry : ranks.entrySet()) {
            if (rankEntry.getValue() == rank.getId()) { // This is our rank!
                ladderName = rankEntry.getKey();
            }
        }

        // ... and then remove it!
        ranks.remove(ladderName);
    }

    /**
     * Removes a ladder from this player, including whichever rank this player had in it.
     * Cannot remove the default ladder.
     *
     * @param ladderName The ladder's name.
     */
    public boolean removeLadder(String ladderName) {
    	boolean results = false;
        if ( !ladderName.equalsIgnoreCase("default") ) {
        	Integer id = ranks.remove(ladderName);
        	results = (id != null);
        }
        return results;
    }

    /*
     * Getters & Setters
     */

    /**
     * Retrieves the rank that this player has in a certain ladder, if any.
     *
     * @param ladder The ladder to check.
     * @return An optional containing the {@link Rank} if found, or empty if there isn't a rank by that ladder for this player.
     */
    @Deprecated
    public Optional<Rank> getRank(RankLadder ladder) {
        if (!ranks.containsKey(ladder.getName())) {
            return Optional.empty();
        }
        int id = ranks.get(ladder.getName());
        return PrisonRanks.getInstance().getRankManager().getRankOptional(id);
    }
    
    /**
     * Retrieves the rank that this player has the specified ladder.
     *
     * @param ladder The ladder name to check.
     * @return The {@link Rank} if found, otherwise null;
     */
    public Rank getRank(String ladder) {
    	Rank results = null;
    	if (ladder != null && ranks.containsKey(ladder)) {
    		int id = ranks.get(ladder);
    		results = PrisonRanks.getInstance().getRankManager().getRank(id);
    	}
    	return results;
    }

    

	public HashMap<String, Integer> getPrestige() {
		return prestige;
	}
	public void setPrestige( HashMap<String, Integer> prestige ) {
		this.prestige = prestige;
	}

	public void setRanks( HashMap<String, Integer> ranks ) {
		this.ranks = ranks;
	}

	/**
     * Returns all ladders this player is a part of, along with each rank the player has in that ladder.
     *
     * @return The map containing this data.
     */
    public Map<RankLadder, Rank> getRanks() {
        Map<RankLadder, Rank> ret = new HashMap<>();
        for (Map.Entry<String, Integer> entry : ranks.entrySet()) {
            Optional<RankLadder> ladder =
                PrisonRanks.getInstance().getLadderManager().getLadder(entry.getKey());
            if (!ladder.isPresent()) {
                continue; // Skip it
            }

            Rank rank = PrisonRanks.getInstance().getRankManager().getRank(entry.getValue());
            if ( rank == null ) {
                continue; // Skip it
            }

            ret.put(ladder.get(), rank);
        }

        return ret;
    }

    /*
     * equals() and hashCode()
     */

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RankPlayer)) {
            return false;
        }

        RankPlayer that = (RankPlayer) o;

        return uid.equals(that.uid);
    }

    @Override public int hashCode() {
        return uid.hashCode();
    }

	@Override
	public String getName()
	{
		String name = getLastName();
		
		if ( name == null ) {
			name = Long.toString( uid.getLeastSignificantBits() );
		}
		return name;
	}

	@Override
	public void dispatchCommand( String command )
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean hasPermission( String perm ) {
		Output.get().logError( "SpigotOfflinePlayer.hasPermission: Cannot access permissions for offline players." );
		return false;
	}
	
	@Override
	public void sendMessage( String message ) {
		Output.get().logError( "SpigotOfflinePlayer.sendMessage: Cannot send messages to offline players." );
	}
	
	@Override
	public void sendMessage( String[] messages ) {
		Output.get().logError( "SpigotOfflinePlayer.sendMessage: Cannot send messages to offline players." );
	}
	
	@Override
	public void sendRaw( String json ) {
		Output.get().logError( "SpigotOfflinePlayer.sendRaw: Cannot send messages to offline players." );
	}

	@Override
	public boolean doesSupportColors() {
		return false;
	}

	@Override
	public void give( ItemStack itemStack ) {
		Output.get().logError( "SpigotOfflinePlayer.give: Cannot give to offline players." );
	}

	@Override
	public Location getLocation() {
		Output.get().logError( "SpigotOfflinePlayer.getLocation: Offline players have no location." );
		return null;
	}

	@Override
	public void teleport( Location location ) {
		Output.get().logError( "SpigotOfflinePlayer.teleport: Offline players cannot be teleported." );
	}

	@Override
	public void setScoreboard( Scoreboard scoreboard ) {
		Output.get().logError( "SpigotOfflinePlayer.setScoreboard: Offline players cannot use scoreboards." );
	}

	@Override
	public Gamemode getGamemode() {
		Output.get().logError( "SpigotOfflinePlayer.getGamemode: Offline is not a valid gamemode." );
		return null;
	}

	@Override
	public void setGamemode( Gamemode gamemode ) {
	}

	@Override
	public Optional<String> getLocale() {
		Output.get().logError( "SpigotOfflinePlayer.getLocale: Offline is not a valid gamemode." );
		return null;
	}

	@Override
	public boolean isOp() {
		return false;
	}

	@Override
	public void updateInventory() {
	}

	@Override
	public Inventory getInventory() {
		return null;
	}

	@Override
	public void printDebugInventoryInformationToConsole() {
		
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
    

}
