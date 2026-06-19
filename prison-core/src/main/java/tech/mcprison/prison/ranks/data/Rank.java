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

import tech.mcprison.prison.modules.ModuleElement;
import tech.mcprison.prison.modules.ModuleElementType;
import tech.mcprison.prison.sorting.PrisonSortable;

/**
 * Represents a single rank.
 *
 * @author Faizaan A. Datoo
 */
public class Rank
		//extends RankMessages
		implements PrisonSortable,
					ModuleElement,
					Comparable<Rank> {

	// This is to help eliminate RankLadder.PositionRank object:
	private transient int position = -1; 

    // The unique identifier used to distinguish this rank from others - this never changes.
    private int id;

    // The name of this rank, which is used with the user to identify ranks.
    private String name;

    // The tag that this rank has. It can be used as either a prefix or a suffix, depending on user preferences.
    private String tag;

    // The general cost of this rank, unit-independent. This value holds true for both XP and cost.
    private double cost;

    /** 
     * <p>Special currency to use. If null, then will use the standard currencies. 
     * If currency is not null, then it must exist in a an economy that is
     * supported in the EconomyCurrencyIntegration.
     * </p>
     * 
     */
    private String currency;
    
    // The commands that are run when this rank is attained.
    private List<String> rankUpCommands;

 
    
    private transient Rank rankPrior;
    private transient Rank rankNext;
    
    
    private List<ModuleElement> mines;
    private List<String> mineStrings;
    
    
    private transient RankLadder ladder;
    
    private transient final List<RankPlayer> players;
    
    private transient final StatsRankPlayerBalance statsPlayerBlance;
    
    private transient boolean dirty = false;
    

    public Rank() {
	    	super();
	    	
	    	this.rankUpCommands = new ArrayList<>();
	    	
	    	this.mines = new ArrayList<>();
	    	this.mineStrings = new ArrayList<>();
	    	
	    	this.players = new ArrayList<>();
	    	
	    	this.statsPlayerBlance = new StatsRankPlayerBalance( this );
    	
    }
    
    public Rank( int id, String name, String tag, double cost ) {
	    	this();
	    	
	    	
	    	this.id = id;
	    	this.name = name;
	    	this.tag = tag;
	    	this.cost = cost;
    }
    
    /**
     * <p>This is strictly used for testing only!
     * Never use this function outside of a jUnit test case!
     * </p>
     * 
     * @param id
     * @param name
     */
    protected Rank( String name ) {
	    	this();
	    	
	    	this.id = 0;
	    	this.name = name;
    }

    
	public StatsRankPlayerBalance getStatsPlayerBlance() {
		return statsPlayerBlance;
	}


	/**
	 * <p>This adds players to this rank.  It prevents duplicates.
	 * </p>
	 * 
	 * @param player
	 */
	public void addPlayer( RankPlayer player ) {
		addPlayer( player, true );
	}
	public void addPlayer( RankPlayer player, boolean checkPlayerBalances ) {
		
		if ( !getPlayers().contains( player ) ) {
			getPlayers().add( player );
			
			getStatsPlayerBlance().addPlayer( player, checkPlayerBalances );
		}
	}

	public void removePlayer( RankPlayer player ) {
		
		if ( getPlayers().contains( player ) ) {
			getPlayers().remove( player );
			
			getStatsPlayerBlance().removePlayer( player );
		}
	}
	
    @Override
    public String toString() {
    		return "Rank: " + id + " " + name;
    }
    
    public String filenameNew() {
    		return "rank_" + getName();
    }
    
    public String filenameOld() {
    		return getId() == -1 ? null : "rank_" + getId();
    }
    
    
    public RankLadder getLadder() {
    		return ladder;
    }
    public void setLadder( RankLadder ladder ) {
    		this.ladder = ladder;
    }
    

    /**
     * The test of equality should only be done upon the rank id.
     * This function is pretty much useless since you can only have
     * one rank with the same rank ID and with the same name.  So it
     * is sufficient to only check if the rank.id matches.  
     * The other fields, such as name, cost, currency, and tag 
     * are pretty much meaningless to use in this test.
     */
    @Override 
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rank)) {
            return false;
        }

        Rank rank = (Rank) o;

        // Rank.id is unique and there should never be two with the same rank.
        
        return id == rank.id;
    }

    @Override 
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        temp = Double.doubleToLongBits(cost);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + currency.hashCode();
        return result;
    }
    

	@Override
	public int compareTo( Rank r )
	{
		int results = 0;
		
		if ( r == null ) {
			results = -1;
		}
		else {
			results = Integer.compare( getPosition(), r.getPosition() );
		}
		
		return results;
	}

    /**
     * <p>This provides a quick reference to the position within the ladder's rank's
     * ArrayList. This value is zero based, where the first rank on the ladders has
     * a position of 0.
     * </p>
     * 
     * <p>This new implementation of position is lazy loaded and should never be saved.
     * This should never be used to access a rank or to refer to a rank; the
     * actual objects should be used for that.
     * </p>
     * 
     * @return
     */
	public int getPosition() {
		if ( position == -1 && getLadder() != null ) {
			
			position = 0;
			for ( Rank r : getLadder().getRanks() ) {
				if ( r == this ) {
					break;
				}
				position++;
			}

		}
		return position;
	}
	/**
	 * This will force the rank's position to be reset the next time it is 
	 * accessed.
	 * 
	 */
	public void resetPosition() {
		position = -1;
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

	public String getTag() {
		return tag;
	}
	public void setTag( String tag ) {
		this.tag = tag;
	}

	/**
	 * <p>This is publicly available as "raw" to underscore that it is not an
	 * adjusted cost value.
	 * </p>
	 * 
	 * @param rank
	 * @return
	 */
	public double getRawRankCost() {
		return cost;
	}
	public void setRawRankCost( double cost ) {
		this.cost = cost;
	}
	
	protected double getCost() {
		return cost;
	}
	protected void setCost( double cost ) {
		this.cost = cost;
	}

	public String getCurrency() {
		return currency;
	}
	public void setCurrency( String currency ) {
		this.currency = currency;
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
	
	public Rank getRankPrior() {
		return rankPrior;
	}
	public void setRankPrior( Rank rankPrior ) {
		this.rankPrior = rankPrior;
	}

	public Rank getRankNext() {
		return rankNext;
	}
	public void setRankNext( Rank rankNext ) {
		this.rankNext = rankNext;
	}

	@Override
	public ModuleElementType getModuleElementType() {
		return ModuleElementType.RANK;
	}

	public List<ModuleElement> getMines() {
		if ( mines == null ) {
			this.mines = new ArrayList<>();
		}
		return mines;
	}
	public void setMines( List<ModuleElement> mines ) {
		this.mines = mines;
	}

	public List<String> getMineStrings() {
		if ( mineStrings == null ) {
			this.mineStrings = new ArrayList<>();
		}
		return mineStrings;
	}
	public void setMineStrings( List<String> mineStrings ) {
		this.mineStrings = mineStrings;
	}

	public List<RankPlayer> getPlayers() {
		return players;
	}

	public boolean isDirty() {
		return dirty;
	}
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

}
