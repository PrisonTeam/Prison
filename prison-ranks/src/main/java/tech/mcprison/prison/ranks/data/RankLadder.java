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

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.RankUtil;
import tech.mcprison.prison.ranks.managers.RankManager;
import tech.mcprison.prison.sorting.PrisonSortable;
import tech.mcprison.prison.store.Document;

/**
 * A certain sequence that rank-ups will follow. There may be multiple rank ladders on the server at
 * a time, and one rank may be a part of multiple ladders.
 *
 * @author Faizaan A. Datoo
 */
public class RankLadder 
		implements PrisonSortable {

    /*
     * Fields & Constants
     */

    private int id;
    private String name;
    private List<Rank> ranks;
//    private List<PositionRank> ranks;
    
    private int maxPrestige;
   
    
    private List<String> permissions;
    private List<String> permissionGroups;
 
    
    private boolean dirty = false;

    /*
     * Document-related
     */

    public RankLadder() {
    	super();
    	
    	this.ranks = new ArrayList<>();
    	
    	this.permissions = new ArrayList<>();
    	this.permissionGroups =  new ArrayList<>();

    }
    
    public RankLadder( int id, String name ) {
    	this();
    	
    	this.id = id;
    	this.name = name;
    }

    @SuppressWarnings( "unchecked" )
	public RankLadder(Document document, PrisonRanks prisonRanks) {
    	this();
    	
    	RankManager rankManager = prisonRanks.getRankManager();
    	
        this.id = RankUtil.doubleToInt(document.get("id"));
        this.name = (String) document.get("name");
        
        
        List<LinkedTreeMap<String, Object>> ranksLocal =
                (List<LinkedTreeMap<String, Object>>) document.get("ranks");

        this.ranks = new ArrayList<>();
        for (LinkedTreeMap<String, Object> rank : ranksLocal) {
        	
        	
        	// The only real field that is important here is rankId to tie the 
        	// rank back to this ladder.  Name helps clarify the contents of the 
        	// Ladder file. Position is redundant (its now in Rank), but 
        	// it identifies the load order.
        	int rPos = RankUtil.doubleToInt(rank.get("position"));
        	int rRankId = RankUtil.doubleToInt((rank.get("rankId")));
        	String rRankName = (String) rank.get( "rankName" );
        	
        	Rank rankPrison = null;
        	
        	if ( rankManager != null &&  
    				rankManager.getRank( rRankId ) !=  null) {

        		rankPrison = rankManager.getRank( rRankId );
        		addRank( rankPrison );

//        		Output.get().logInfo( "RankLadder load : " + getName() + 
//        				"  rank= " + rankPrison.getName() + " " + rankPrison.getId() + 
//        				" " + rankPrison.getPosition() );
        		
//        		// if null look it up from loaded ranks:
//        		if ( rRankName == null  ) {
//        			rRankName = rankPrison.getName();
//        			dirty = true;
//        		}
        	}
        	else {
        		// Rank not found. Try to create it? The name maybe wrong.
        		String rankName = rRankName != null && !rRankName.trim().isEmpty() ?
        					rRankName : "Rank " + rRankId;
        		double cost = getRanks().size() == 0 ? 0 : 
        					getRanks().get( getRanks().size() - 1 ).getCost() * 3;
        		Rank newRank = new Rank(rPos, rRankId, rankName, null, cost );
        		
        		addRank( newRank );
        		
//        		String message = String.format( 
//        				"Loading RankLadder Error: A rank for %s was not found so it was " +
//        				"fabricated: %s  id=%d  tag=%s  cost=%d", getName(), newRank.getName(), newRank.getId(),
//        				newRank.getTag(), newRank.getCost() );
//        		Output.get().logError( message );
        	}
        	
//            ranks.add(new PositionRank( rPos, rRankId, rRankName, rankPrison ));
        }
        
        this.maxPrestige = RankUtil.doubleToInt(document.get("maxPrestige"));
        
		
		getPermissions().clear();
		Object perms = document.get( "permissions" );
		if ( perms != null ) {
			List<String> permissions = (List<String>) perms;
			for ( String permission : permissions ) {
				getPermissions().add( permission );
			}
		}
        
		
		getPermissionGroups().clear();
		Object permsGroups = document.get( "permissionGroups" );
		if ( perms != null ) {
			List<String> permissionGroups = (List<String>) permsGroups;
			for ( String permissionGroup : permissionGroups ) {
				getPermissionGroups().add( permissionGroup );
			}
		}

    }

    public Document toDocument() {
        Document ret = new Document();
        ret.put("id", this.id);
        ret.put("name", this.name);
        
        // getRanks() no longer returns an intermediate object, it contains the ranks:
        List<LinkedTreeMap<String, Object>> ranksLocal =
                						new ArrayList<LinkedTreeMap<String, Object>>();
        for ( Rank rank : getRanks() ) {
        	LinkedTreeMap<String, Object> rnk = new LinkedTreeMap<String, Object>();
        	
        	rnk.put( "position", Double.valueOf( (double) rank.getPosition() ));
        	rnk.put( "rankId", Double.valueOf( (double) rank.getId() ));
        	rnk.put( "rankName", rank.getName());

        	ranksLocal.add( rnk );
		}
        ret.put("ranks", ranksLocal);
//        ret.put("ranks", this.ranks);
        
        
        ret.put("maxPrestige", this.maxPrestige);
        
        ret.put( "permissions", getPermissions() );
        ret.put( "permissionGroups", getPermissionGroups() );
        
        return ret;
    }

    @Override 
    public String toString() {
    	return "Ladder: " + name + "  ranks: " + (ranks == null ? 0 : ranks.size());
    }
    
    public List<Rank> getRanks() {
    	return ranks;
    	
//    	List<Rank> rankz = new ArrayList<>();
//
//    	RankManager rankManager = PrisonRanks.getInstance().getRankManager();
//    	
//    	for ( PositionRank rank : ranks ) {
//    		
//    		if ( rank != null && rank.rank == null ) {
//    			//
//    			Rank rnk = rankManager.getRank( rank.rankId );
//    			if ( rnk != null ) {
//    				rank.rank = rnk;
//    			}
//    			else {
//    				Output.get().logWarn( "RankLadder.listAllRanks(): " +
//    						"Could not get Rank from rankId: " + rank.rankId );
//    			}
//    		}
//    		rankz.add( rank.rank );
//    	}
//    	
//    	return rankz;
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
    		position = getRanks().size();
    	}
//        position = Math.min(position,
//                getRanks().size() + 1); // Make sure to cap it off at the upper limit or else problems
//        int finalPosition = position;
//        ranks.stream().filter(positionRank -> positionRank.getPosition() >= finalPosition)
//                .forEach(positionRank -> positionRank.setPosition(positionRank.getPosition() + 1));

        rank.setPosition( position );
        getRanks().add( position, rank );
        rank.setLadder( this );
//        ranks.add(new PositionRank(position, rank.getId(), rank.getName(), rank));

        // Update the rank positions along with next and prior:
        connectRanks();
        
//        // Reset the rank relationships:
//        PrisonRanks.getInstance().getRankManager().connectRanks();
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
    	int position = getRanks().size();
    	rank.setPosition( position );
    	rank.setLadder( this );
    	
    	getRanks().add( rank );

    	// Update the rank positions along with next and prior:
    	connectRanks();
    	
//        ranks.add(new PositionRank(getNextAvailablePosition(), rank.getId(), rank.getName(), rank));
        
//        // Reset the rank relationships:
//        PrisonRanks.getInstance().getRankManager().connectRanks();
    }

    /**
     * Removes a rank from this ladder.
     *
     * @param position The position of the rank to be removed. The positions of the rest of the
     *                 ranks will be downshifted to fill the gap.
     */
    public void removeRank(int position) {
    	
    	Rank rank = getRanks().remove( position );
    	
    	rank.setLadder( null );

    	// Update the rank positions along with next and prior:
    	connectRanks();

    	
//        ranks.stream().filter(positionRank -> positionRank.getPosition() > position).forEach(
//                positionRank -> positionRank.setPosition(positionRank.getPosition() - 1)
//        );
//
//        Iterator<PositionRank> iter = ranks.iterator();
//        while (iter.hasNext()) {
//            PositionRank rank = iter.next();
//            if (rank.getPosition() == position) {
//                iter.remove();
//                break;
//            }
//        }
        
//        // Reset the rank relationships:
//        PrisonRanks.getInstance().getRankManager().connectRanks();
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
    		if ( rank.getPosition() != i ) {
    			rank.setPosition( i );
    		}
    		
    		// reset the rankPrior and rankNext in case there are no hookups:
    		// Important if ranks are removed, or inserted, or moved:
    		rank.setRankPrior( null );
    		rank.setRankNext( null );
    		
    		if ( rankLast != null ) {
    			rank.setRankPrior( rankLast );
    			rankLast.setRankNext( rank );
    		}
    		rankLast = rank;
    		
//    		String message = "Ladder " + getName() + " " + rank.getName() + 
//    				"  position=" + rank.getPosition();
//    		Output.get().logInfo( message );
    	}
    }

//    /**
//     * Orders the ranks in the rank list of this ladder by their position, in ascending order.
//     */
//    public void orderRanksByPosition() {
//        
//    	// Do not sort here:
//    	//The ranks within a ladder will be sorted within the function connectRanks():
//    	//ranks.sort(Comparator.comparingInt(PositionRank::getPosition));
//        
//        // Reset the rank relationships:
//        PrisonRanks.getInstance().getRankManager().connectRanks();
//    }

    /*
     * Getters & Setters
     */

    /**
     * Returns true if this ladder contains a rank with a specified ID.
     *
     * @param rankId The ID of the rank to search for.
     * @return True if the rank was found, false otherwise.
     */
    public boolean containsRank(int rankId) {
        return ranks.stream().anyMatch(rank -> rank.getId() == rankId);
    }

    /**
     * Returns the position of the specified {@link Rank} in this ladder.
     *
     * @param rank The {@link Rank} to retrieve the position of.
     * @return The position of the rank, or -1 if the rank was not found.
     */
    public int getPositionOfRank(Rank rank) {
    	
    	return getRanks().contains( rank ) ? rank.getPosition() : -1;
    	
//        for (PositionRank rankEntry : ranks) {
//            if (rankEntry.getRankId() == rank.getId()) {
//                return rankEntry.getPosition();
//            }
//        }
//
//        return -1;
    }

    /**
     * Returns the next highest rank in the ladder.
     *
     * @param after The position of the current rank.
     * @return An optional containing either the rank if there is a next rank in the ladder, or
     * empty if there isn't or if the rank does not exist anymore.
     */
    public Optional<Rank> getNext(int after) {
    	Rank results = null;
    	
    	if ( getRanks().size() >= after ) {
    		results = getRanks().get( after ).getRankNext();
    	}
    	
    	return results == null ? Optional.empty() : Optional.of( results );
//        List<Integer> positions =
//                ranks.stream().map(PositionRank::getPosition).sorted().collect(Collectors.toList());
//
//        int newIndex = positions.indexOf(after) + 1;
//        if (newIndex >= positions.size()) {
//            return Optional.empty();
//        }
//
//        int nextPosition = positions.get(newIndex);
//        return getByPosition(nextPosition);
    }

    /**
     * Returns the next lowest rank in the ladder.
     *
     * @param before The position of the current rank.
     * @return An optional containing either the rank if there is a previous rank in the ladder, or
     * empty if there isn't or if the rank does not exist anymore.
     */
    public Optional<Rank> getPrevious(int before) {
    	Rank results = null;
    	
    	if ( getRanks().size() >= before ) {
    		results = getRanks().get( before ).getRankPrior();
    	}
    	
    	return results == null ? Optional.empty() : Optional.of( results );
    	
//        List<Integer> positions =
//                ranks.stream().map(PositionRank::getPosition).sorted().collect(Collectors.toList());
//
//        int newIndex = positions.indexOf(before) - 1;
//        if (newIndex < 0) {
//            return Optional.empty();
//        }
//
//        int previousPosition = positions.get(newIndex);
//        return getByPosition(previousPosition);
    }

    /**
     * Searches for and returns a rank in the ladder, depending on the position in the ladder.
     *
     * @param position The position to search for.
     * @return An optional containing the rank if it was found, or empty if it wasn't.
     */
	public Optional<Rank> getByPosition(int position) {
    	Rank results = null;
    	
    	if ( getRanks().size() >= position ) {
    		results = getRanks().get( position );
    	}
    	
    	return results == null ? Optional.empty() : Optional.of( results );
//
//    	
//        for (PositionRank posRank : ranks) {
//            if (posRank.getPosition() == position) {
//                return PrisonRanks.getInstance().getRankManager().getRankOptional(posRank.getRankId());
//            }
//        }
//
//        return Optional.empty();
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
    	
    	for ( Rank r : getRanks() ) {
			if ( results == null || r.getPosition() < results.getPosition() ) {
				results = r;
			}
		}
    	
    	return results == null ? Optional.empty() : Optional.of( results );
//
//    	
//        if (ranks.isEmpty()) return Optional.empty();
//
//        PositionRank lowest = ranks.get(0);
//        for (PositionRank posRank : ranks) {
//            if (posRank.getPosition() < lowest.getPosition()) {
//                lowest = posRank;
//            }
//        }
//
//        return PrisonRanks.getInstance().getRankManager().getRankOptional(lowest.getRankId());
    }

//    /**
//     * Returns the next available position for a rank, by finding the highest one.
//     *
//     * @return The open position.
//     */
//    private int getNextAvailablePosition() {
//    	Rank results = null;
//    	
//    	for ( Rank r : getRanks() ) {
//			if ( results == null || r.getPosition() > results.getPosition() ) {
//				results = r;
//			}
//		}
//    	
//    	return results == null ? -1 : results.getPosition() + 1;
//    	
////        if (ranks.size() == 0) {
////            return 0; // obviously, if it's empty, we want to start at the bottom
////        }
////
////        //orderRanksByPosition();
////        // Reset the rank relationships:
////        PrisonRanks.getInstance().getRankManager().connectRanks();
////        
////        return ranks.get(ranks.size() - 1).getPosition() + 1;
//    }

    /*
     * equals() and hashCode()
     */

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

//    public class PositionRank {
//
//        private int position;
//        private int rankId;
//        private String rankName;
//        
//        /** 
//         * Adds a link to the actual Rank. This will save a lot of busy 
//         * work and can reduce the complexity of a lot of code.
//         */
//        private transient Rank rank;
//
//        /**
//         * 
//         * @param position
//         * @param rankId
//         * @param rankName rankName is never used but makes it easier to read the saved files
//         */
//        public PositionRank(int position, int rankId, String rankName, Rank rank ) {
//            this.position = position;
//            this.rankId = rankId;
//            this.rankName = rankName;
//        }
//
//        public int getPosition() {
//            return position;
//        }
//
//        public void setPosition(int position) {
//            this.position = position;
//        }
//
//        public int getRankId() {
//            return rankId;
//        }
//
//        public void setRankId(int rankId) {
//            this.rankId = rankId;
//        }
//
//		public String getRankName()
//		{
//			return rankName;
//		}
//
//		public void setRankName( String rankName )
//		{
//			this.rankName = rankName;
//		}
//
//		public Rank getRank()
//		{
//			return rank;
//		}
//
//		public void setRank( Rank rank )
//		{
//			this.rank = rank;
//		}
//    }
    

    /**
     * <p>Identifies of the Ladder contains a permission.
     * </p>
     * 
     * @param permission
     * @return
     */
	public boolean hasPermission( String permission ) {
		boolean results = false;
		
		for ( String perm : getPermissions() ) {
			if ( perm.equalsIgnoreCase( permission ) ) {
				results = true;
				break;
			}
		}
		
		return results;
	}
	
	/**
	 * <p>Identifies if the Ladder contains a permission group.
	 * </p>
	 * 
	 * @param permissionGroup
	 * @return
	 */
	public boolean hasPermissionGroup( String permissionGroup ) {
		boolean results = false;
		
		for ( String perm : getPermissionGroups() ) {
			if ( perm.equalsIgnoreCase( permissionGroup ) ) {
				results = true;
				break;
			}
		}
		
		return results;
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

	public int getMaxPrestige() {
		return maxPrestige;
	}
	public void setMaxPrestige( int maxPrestige ) {
		this.maxPrestige = maxPrestige;
	}

//	public List<PositionRank> getPositionRanks() {
//		return ranks;
//	}
//	public void setPositionRanks( List<PositionRank> ranks ) {
//		this.ranks = ranks;
//	}
	
	public List<String> getPermissions() {
		return permissions;
	}
	public void setPermissions( List<String> permissions ) {
		this.permissions = permissions;
	}

	public List<String> getPermissionGroups() {
		return permissionGroups;
	}
	public void setPermissionGroups( List<String> permissionGroups ) {
		this.permissionGroups = permissionGroups;
	}

	public boolean isDirty() {
		return dirty;
	}
	public void setDirty( boolean dirty ) {
		this.dirty = dirty;
	}

}
