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

import com.google.gson.internal.LinkedTreeMap;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.RankUtil;
import tech.mcprison.prison.store.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents a player with ranks.
 *
 * @author Faizaan A. Datoo
 */
public class RankPlayer {

    /*
     * Fields & Constants
     */

    public UUID uid;
    public HashMap<String, Integer> ranks; // <Ladder Name, Rank ID>
    public HashMap<String, Integer> prestige; // <Ladder Name, Prestige>
    
    public List<RankPlayerName> names;
    

    /*
     * Document-related
     */

    public RankPlayer() {
    	super();
    }

    @SuppressWarnings( "unchecked" )
	public RankPlayer(Document document) {
    	
        this.uid = UUID.fromString((String) document.get("uid"));
        LinkedTreeMap<String, Object> ranksLocal =
            (LinkedTreeMap<String, Object>) document.get("ranks");
        LinkedTreeMap<String, Object> prestigeLocal =
            (LinkedTreeMap<String, Object>) document.get("prestige");
        
        Object namesListObject = document.get( "names" );
        

        this.ranks = new HashMap<>();
        for (String key : ranksLocal.keySet()) {
            ranks.put(key, RankUtil.doubleToInt(ranksLocal.get(key)));
        }
        
        this.prestige = new HashMap<>();
        for (String key : prestigeLocal.keySet()) {
            prestige.put(key, RankUtil.doubleToInt(prestigeLocal.get(key)));
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
        return ret;
    }

    /*
     * Methods
     */
    
    
    
    public boolean checkName( String playerName ) {
    	boolean added = false;
    	
    	// Check if the last name in the list is not the same as the name passed:
    	if ( getNames().size() == 0 ||
    			!getNames().get( getNames().size() - 1 ).getName().equalsIgnoreCase( playerName ) ) {
    		
    		RankPlayerName rpn = new RankPlayerName( playerName, System.currentTimeMillis() );
    		getNames().add( rpn );
    		
    		added = true;
    	}
    	
    	return added;
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
        if (!ladder.containsRank(rank.id)) {
            throw new IllegalArgumentException("Rank must be on ladder.");
        }

        // Remove the current rank on this ladder first
        if (ranks.containsKey(ladder.name)) {
            ranks.remove(ladder.name);
        }

        ranks.put(ladder.name, rank.id);
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
            if (rankEntry.getValue() == rank.id) { // This is our rank!
                ladderName = rankEntry.getKey();
            }
        }

        // ... and then remove it!
        ranks.remove(ladderName);
    }

    /**
     * Removes a ladder from this player, including whichever rank this player had in it.
     *
     * @param ladderName The ladder's name.
     */
    public void removeLadder(String ladderName) {
        if (ladderName.equalsIgnoreCase("default")) {
            return;
        }
        ranks.remove(ladderName);
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
    public Optional<Rank> getRank(RankLadder ladder) {
        if (!ranks.containsKey(ladder.name)) {
            return Optional.empty();
        }
        int id = ranks.get(ladder.name);
        return PrisonRanks.getInstance().getRankManager().getRank(id);
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
    		Optional<Rank> ladderOpt = PrisonRanks.getInstance().getRankManager().getRank(id);
    		if ( ladderOpt.isPresent() ) {
    			results =  ladderOpt.get();
    		}
    	}
    	return results;
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

            Optional<Rank> rank =
                PrisonRanks.getInstance().getRankManager().getRank(entry.getValue());
            if (!rank.isPresent()) {
                continue; // Skip it
            }

            ret.put(ladder.get(), rank.get());
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

}
