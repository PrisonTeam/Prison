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
import tech.mcprison.prison.ranks.PrisonRanks;
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
	
	// This is to help eliminate RankLadder.PositionRank object:
	private int position; 

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

    
    private List<String> permissions;
    private List<String> permissionGroups;
    
    
    private transient Rank rankPrior;
    private transient Rank rankNext;
    
    
    private List<ModuleElement> mines;
    private List<String> mineStrings;
    
    
    private transient RankLadder ladder;
    
    /*
     * Document-related
     */

    public Rank() {
    	super();
    	
    	this.rankUpCommands = new ArrayList<>();
    	
    	this.mines = new ArrayList<>();
    	this.mineStrings = new ArrayList<>();
    	
    	this.permissions = new ArrayList<>();
    	this.permissionGroups =  new ArrayList<>();
    	
    }
    
    public Rank( int position, int id, String name, String tag, double cost ) {
    	this();
    	
    	this.position = position;
    	
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
    	this.position = 0;
    	this.id = 0;
    	this.name = name;
    }

    @SuppressWarnings( "unchecked" )
	public Rank(Document document) {
    	this();
    	
        try
		{
        	Object pos = document.get("position");
        	this.position = RankUtil.doubleToInt( pos == null ? 0.0d : pos );
        	
			this.id = RankUtil.doubleToInt(document.get("id"));
			this.name = (String) document.get("name");
			this.tag = (String) document.get("tag");
			this.cost = (double) document.get("cost");
			
			String currency = (String) document.get("currency");
			this.currency = (currency == null || 
					"null".equalsIgnoreCase( currency ) ? null : currency);

			getRankUpCommands().clear();
			Object cmds = document.get("commands");
			if ( cmds != null ) {

				List<String> commands = (List<String>) cmds;
				for ( String cmd : commands ) {
					if ( cmd != null ) {
						getRankUpCommands().add( cmd );
					}
				}
				
				// This was allowing nulls to be added to the live commands... 
//				this.rankUpCommands = (List<String>) cmds;
			}
			
			getMines().clear();
			getMineStrings().clear();
			Object minesObj = document.get("mines");
			if ( minesObj != null ) {
				List<String> mineStrings = (List<String>) minesObj;
				setMineStrings( mineStrings );
			}
			
			
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
        ret.put("position", this.position );
        ret.put("id", this.id);
        ret.put("name", this.name);
        ret.put("tag", this.tag);
        ret.put("cost", this.cost);
        ret.put("currency", this.currency);
        
        List<String> cmds = new ArrayList<>();
        for ( String cmd : getRankUpCommands() ) {
        	// Filters out possible nulls:
			if ( cmd != null ) {
				cmds.add( cmd );
			}
		}
        ret.put("commands", cmds);
        
        List<String> mineStrings = new ArrayList<>();
        if ( getMines() != null ) {
        	for ( ModuleElement mine : getMines() ) {
        		 String mineString = mine.getModuleElementType() + "," + mine.getName() + "," + 
        				 mine.getId() + "," + mine.getTag();
        		 mineStrings.add( mineString );
			}
        }
        ret.put("mines", mineStrings);
        
        ret.put( "permissions", getPermissions() );
        ret.put( "permissionGroups", getPermissionGroups() );
        
        return ret;
    }
    
    

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

    
    @Override
    public String toString() {
    	return "Rank: " + id + " " + name;
    }
    
    public String filename() {
    	return "rank_" + id;
    }
    
    
    public RankLadder getLadder() {
    	if ( ladder == null ) {
    		
    		ladder = PrisonRanks.getInstance().getLadderManager().getLadder( this );
    	}
    	
    	return ladder;
    }
    public void setLadder( RankLadder ladder ) {
    	this.ladder = ladder;
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

    
	public int getPosition() {
		return position;
	}
	public void setPosition( int position ) {
		this.position = position;
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
		if ( rankUpCommands == null ) {
			rankUpCommands = new ArrayList<>();
		}
		return rankUpCommands;
	}
	public void setRankUpCommands( List<String> rankUpCommands ) {
		this.rankUpCommands = rankUpCommands;
	}
	
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
    
	
}
