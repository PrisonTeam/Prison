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
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.RankUtil;
import tech.mcprison.prison.sorting.PrisonSortable;
import tech.mcprison.prison.store.Document;

/**
 * Represents a single rank.
 *
 * @author Faizaan A. Datoo
 */
public class Rank
		implements PrisonSortable,
					ModuleElement {

    /*
     * Fields & Constants
     */

    // The unique identifier used to distinguish this rank from others - this never changes.
    public int id;

    // The name of this rank, which is used with the user to identify ranks.
    public String name;

    // The tag that this rank has. It can be used as either a prefix or a suffix, depending on user preferences.
    public String tag;

    // The general cost of this rank, unit-independent. This value holds true for both XP and cost.
    public double cost;

    /** 
     * <p>Special currency to use. If null, then will use the standard currencies. 
     * If currency is not null, then it must exist in a an economy that is
     * supported in the EconomyCurrencyIntegration.
     * </p>
     * 
     */
    public String currency;
    
    // The commands that are run when this rank is attained.
    public List<String> rankUpCommands;

    
    public transient Rank rankPrior;
    public transient Rank rankNext;
    
    
    private List<ModuleElement> mines;
    private List<String> mineStrings;
    
    
    /*
     * Document-related
     */

    public Rank() {
    	super();
    	
    	this.mines = new ArrayList<>();
    	this.mineStrings = new ArrayList<>();
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
    	this.id = 0;
    	this.name = name;
    }

    @SuppressWarnings( "unchecked" )
	public Rank(Document document) {
        try
		{
			this.id = RankUtil.doubleToInt(document.get("id"));
			this.name = (String) document.get("name");
			this.tag = (String) document.get("tag");
			this.cost = (double) document.get("cost");
			String currency = (String) document.get("currency");
			this.currency = (currency == null || 
					"null".equalsIgnoreCase( currency ) ? null : currency);
			this.rankUpCommands = (List<String>) document.get("commands");
			
	        List<String> mineStrings = (List<String>) document.get("mines");
	        getMines().clear();
	        setMineStrings( mineStrings );
	        

			
		}
		catch ( Exception e )
		{
			Output.get().logError( 
					String.format( "&aFailure: Loading Ranks! &7Exception parsing rank documents. " +
					"Rank id= %s name= %s  [%s]", 
					Integer.toString( this.id ), (this.name == null ? "null" : this.name ),
					e.getMessage())
					);
		}
    }

    public Document toDocument() {
        Document ret = new Document();
        ret.put("id", this.id);
        ret.put("name", this.name);
        ret.put("tag", this.tag);
        ret.put("cost", this.cost);
        ret.put("currency", this.currency);
        ret.put("commands", this.rankUpCommands);
        
        List<String> mineStrings = new ArrayList<>();
        if ( getMines() != null ) {
        	for ( ModuleElement mine : getMines() ) {
        		 String mineString = mine.getModuleElementType() + "," + mine.getName() + "," + 
        				 mine.getId() + "," + mine.getTag();
        		 mineStrings.add( mineString );
			}
        }
        ret.put("mines", mineStrings);
        
        return ret;
    }
    
    
    @Override
    public String toString() {
    	return "Rank: " + id + " " + name;
    }
    
    public String filename() {
    	return "rank_" + id;
    }
    
    
    /*
     * equals() and hashCode()
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

        if (id != rank.id) {
            return false;
        }
        if (Double.compare(rank.cost, cost) != 0) {
            return false;
        }
        
        if ( currency != null && rank.currency == null || 
        		currency != null && rank.currency != null && 
        				!currency.equals( rank.currency ) ) {
        	return false;
        }
        	
        if (!name.equals(rank.name)) {
            return false;
        }
        return tag != null ? tag.equals(rank.tag) : rank.tag == null;
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

	public double getCost() {
		return cost;
	}
	public void setCost( double cost ) {
		this.cost = cost;
	}

	public String getCurrency() {
		return currency;
	}
	public void setCurrency( String currency ) {
		this.currency = currency;
	}

	public List<String> getRankUpCommands() {
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
		return mines;
	}
	public void setMines( List<ModuleElement> mines ) {
		this.mines = mines;
	}

	public List<String> getMineStrings() {
		return mineStrings;
	}
	public void setMineStrings( List<String> mineStrings ) {
		this.mineStrings = mineStrings;
	}
    
	
}
