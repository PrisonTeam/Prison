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
import java.util.List;
import java.util.Optional;

import com.google.gson.internal.LinkedTreeMap;

import tech.mcprison.prison.sorting.PrisonSortable;
import tech.mcprison.prison.store.Document;

/**
 * A certain sequence that rank-ups will follow. There may be multiple rank ladders on the server at
 * a time, and one rank may be a part of multiple ladders.
 *
 * @author Faizaan A. Datoo
 */
public class RankLadder 
		implements PrisonSortable, Comparable<RankLadder> {

	public static final String DEFAULT = "default";
	public static final String PRESTIGES = "prestiges";
	
	
    private int id;
    private String name;
    private List<Rank> ranks;
    
    
    
    // The commands that are run when this rank is attained.
    private List<String> rankUpCommands;

    
    private double rankCostMultiplierPerRank = 0.0d;
    private boolean applyRankCostMultiplierToLadder = true;
    
    private transient boolean dirty = false;


    public RankLadder() {
	    	super();
	    	
	    	this.rankUpCommands = new ArrayList<>();
	    	
	    	this.ranks = new ArrayList<>();
	    	
	    	this.applyRankCostMultiplierToLadder = true;
    }
    
    public RankLadder( int id, String name ) {
	    	this();
	    	
	    	this.id = id;
	    	this.name = name;
    }


	public boolean isDefault() {
		return getName().equalsIgnoreCase( DEFAULT );
	}

	public boolean isPrestiges() {
		return getName().equalsIgnoreCase( PRESTIGES );
	}
	
	

    public Document toDocument() {
        Document ret = new Document();
        
        if ( this.id != -1 ) {
        		ret.put("id", this.id);
        }
        ret.put("name", this.name);
        
        List<String> cmds = new ArrayList<>();
        for ( String cmd : getRankUpCommands() ) {
        		// Filters out possible nulls:
			if ( cmd != null ) {
				cmds.add( cmd );
			}
		}
        ret.put("commands", cmds);
        
        // getRanks() no longer returns an intermediate object, it contains the ranks:
        List<LinkedTreeMap<String, Object>> ranksLocal =
                						new ArrayList<LinkedTreeMap<String, Object>>();
        for ( Rank rank : getRanks() ) {
	        	LinkedTreeMap<String, Object> rnk = new LinkedTreeMap<String, Object>();
	        	
	        	
	        	rnk.put( "rankName", rank.getName());
	
	        	ranksLocal.add( rnk );
		}
        ret.put("ranks", ranksLocal);
        
        
        ret.put( "rankCostMultiplierPerRank", getRankCostMultiplierPerRank() );
        
        ret.put( "applyRankCostMultiplierToLadder", isApplyRankCostMultiplierToLadder() );
        
        return ret;
    }

    @Override 
    public String toString() {
    		return "Ladder: " + name + "  ranks: " + (ranks == null ? 0 : ranks.size());
    }
    
    public List<Rank> getRanks() {
    		return ranks;
    }

    @Override
	public int compareTo( RankLadder rl )
	{
	    	int results = -1;
	    	if ( rl != null ) {
	    		results = getName().compareTo( rl.getName() );
	    	}
		return results;
	}

    /**
     * <p>This function finds a rank with the given name.  The rank name
     * must be an exact match, but it's case insensitive.
     * </p>
     * 
     * @param rank
     * @return
     */
    public Rank getRank( String rank ) {
	    	Rank results = null;
	    	
	    	for ( Rank r : ranks ) {
			if ( r.getName().equalsIgnoreCase( rank ) ) {
				results = r;
				break;
			}
		}
    	
	    	return results;
    }
    
    
    /**
     * This function should never be used since magic numbers as used in
     * the rank ID are no longer valid.  This function is ONLY used when loading
     * old player file data so it can be converted to the newer format.
     * 
     * @param rankId
     * @return
     */
    public Rank getRank( int rankId ) {
	    	Rank results = null;
	    	
	    	if ( rankId != -1 ) {
	    		
	    		for ( Rank r : ranks ) {
	    			if ( r.getId() != -1 && r.getId() == rankId ) {
	    				results = r;
	    				break;
	    			}
	    		}
	    	}
	    	
	    	return results;
    }
    
	/**
     * Add a rank to this ladder.
     *
     * @param position The place in line to put this rank, beginning at 0. The player will be taken
     *                 through each rank by order of their positions in the ladder.
     * @param rank     The {@link Rank} to add.
     */
    public void addRank(int position, Rank rank) {
    	
	    	if ( position < 0 ) {
	    		position = 0;
	    	}
	    	else if ( position > getRanks().size() ) {
	    		getRanks().add( rank );
	    	}
	    	else {
	    		getRanks().add( position, rank );
	    	}
	
	    	rank.setLadder( this );

        // Update the rank positions along with next and prior:
        connectRanks();
        
    }
    

    /**
     * Add a rank to this ladder. The rank's position will be set to the next available position
     * (i.e. at the end of the ladder).
     * 
     * The sort the ladder based upon the 
     *
     * @param rank The {@link Rank} to add.
     */
    public void addRank(Rank rank) {
	    	rank.setLadder( this );
	    	
	    	getRanks().add( rank );
	
	    	// Update the rank positions along with next and prior:
	    	connectRanks();
	    	
    }



    public void removeRank( Rank rank ) {
    	
	    	boolean success = getRanks().remove( rank );
	    	
	    	if ( success ) {
	    		rank.setLadder( null );
	    		
	    		// Update the rank positions along with next and prior:
	    		connectRanks();
	    	}
    }
    
    /**
     * <p>Every time a rank is added, removed, or moved, then this 
     * function will update the rank's position value and set the 
     * next and prior values.
     * </p>
     * 
     * <p>The field position may not be needed and will be eliminated
     * if possible.  But for now, to ensure compatibility during the 
     * transition, it is providing a needed behavior.
     * </p>
     * 
     */
    private void connectRanks() {
    	
	    	Rank rankLast = null;
	    	
	    	// The inserted rank may not be at the end of ranks, so go through all ranks and
	    	// update their position value:
	    	for ( int i = 0; i < getRanks().size(); i++ ) {
	    		Rank rank = getRanks().get( i );
	    		
	    		rank.resetPosition();
	    		
	    		// reset the rankPrior and rankNext in case there are no hookups:
	    		// Important if ranks are removed, or inserted, or moved:
	    		rank.setRankPrior( null );
	    		rank.setRankNext( null );
	    		
	    		if ( rankLast != null ) {
	    			rank.setRankPrior( rankLast );
	    			rankLast.setRankNext( rank );
	    		}
	    		rankLast = rank;
	    		
	    	}
    }


    /**
     * Returns true if this ladder contains the Rank.
     * 
     * @param the rank to search for.
     * @return True if the rank was found, false otherwise.
     */
    public boolean containsRank( Rank rank ) {
    		return ranks.contains( rank );
    }

    // This next method is sort of precautionary. Sure, positions start at 0, but if the user decides 
    // to be crazy and alters the position within the data files, we need to make sure we adjust 
    // accordingly. Never trust editable data!

    /**
     * Finds the lowest rank present in this ladder. It does so by checking to see which 
     * position is the lowest.
     *
     * @return The rank option, or an empty optional if there are no ranks in this ladder.
     */
	public Optional<Rank> getLowestRank() {
	    	Rank results = null;
	    	
	    	if ( getRanks().size() > 0 ) {
	    		results = getRanks().get( 0 );
	    	}
	    	
	    	return results == null ? Optional.empty() : Optional.of( results );
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RankLadder)) {
            return false;
        }

        RankLadder that = (RankLadder) o;

        return id == that.id && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        return result;
    }


	public int getId() {
		return id;
	}
	public void setId( int id ) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName( String name ) {
		this.name = name;
	}


	public List<String> getRankUpCommands() {
		if ( rankUpCommands == null ) {
			rankUpCommands = new ArrayList<>();
		}
		return rankUpCommands;
	}
	public void setRankUpCommands( List<String> rankUpCommands ) {
		this.rankUpCommands = rankUpCommands;
	}

	public double getRankCostMultiplierPerRank() {
		return rankCostMultiplierPerRank;
	}
	public void setRankCostMultiplierPerRank( double rankCostMultiplierPerRank ) {
		this.rankCostMultiplierPerRank = rankCostMultiplierPerRank;
	}

	public boolean isApplyRankCostMultiplierToLadder() {
		return applyRankCostMultiplierToLadder;
	}
	public void setApplyRankCostMultiplierToLadder(boolean applyRankCostMultiplierToLadder) {
		this.applyRankCostMultiplierToLadder = applyRankCostMultiplierToLadder;
	}

	public boolean isDirty() {
		return dirty;
	}
	public void setDirty( boolean dirty ) {
		this.dirty = dirty;
	}
	
}
