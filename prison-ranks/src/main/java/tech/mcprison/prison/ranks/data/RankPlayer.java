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
import java.util.TreeMap;
import java.util.UUID;

import com.google.gson.internal.LinkedTreeMap;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.integration.EconomyCurrencyIntegration;
import tech.mcprison.prison.integration.EconomyIntegration;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.inventory.Inventory;
import tech.mcprison.prison.internal.scoreboard.Scoreboard;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.RankUtil;
import tech.mcprison.prison.ranks.top.RankPlayerBalance;
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
    
    
    private HashMap<RankLadder, Rank> ladderRanks;
    
    // ranks is the storage structure used to save the player's ladder & ranks:
    private HashMap<String, Integer> ranksRefs; // <Ladder Name, Rank ID>
    
    // This prestige is not used.  Current prestige is just another ladder.
    //private HashMap<String, Integer> prestige; // <Ladder Name, Prestige>
    
    private List<RankPlayerName> names;
    
    // Block name, count
    private HashMap<String, Integer> blocksMined;

    
    // For tops processing.  Need current balance.
    private TreeMap<String, RankPlayerBalance> playerBalances;
    //x
    
    
    /*
     * Document-related
     */

    public RankPlayer() {
    	super();
    	
    	this.ladderRanks = new HashMap<>();
    	
        this.ranksRefs = new HashMap<>();
        //this.prestige = new HashMap<>();
        
        this.playerBalances = new TreeMap<>();
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
    	this();
    	
        this.uid = UUID.fromString((String) document.get("uid"));
        LinkedTreeMap<String, Object> ranksLocal =
            (LinkedTreeMap<String, Object>) document.get("ranks");
//        LinkedTreeMap<String, Object> prestigeLocal =
//            (LinkedTreeMap<String, Object>) document.get("prestige");
        
        LinkedTreeMap<String, Object> blocksMinedLocal =
        		(LinkedTreeMap<String, Object>) document.get("blocksMined");
        
        Object namesListObject = document.get( "names" );
        

        for (String key : ranksLocal.keySet()) {
            ranksRefs.put(key, RankUtil.doubleToInt(ranksLocal.get(key)));
        }
        
//        for (String key : prestigeLocal.keySet()) {
//            prestige.put(key, RankUtil.doubleToInt(prestigeLocal.get(key)));
//        }
        
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
        ret.put("ranks", this.ranksRefs);
//        ret.put("prestige", this.prestige);
        
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

        String ladderName = ladder.getName();
        
        // Remove the current rank on this ladder first
        if (ranksRefs.containsKey(ladderName)) {
            ranksRefs.remove(ladderName);
        }
        
        if ( ladderRanks.containsKey( ladder ) ) {
        	ladderRanks.remove( ladder );
        }

        ranksRefs.put(ladderName, rank.getId());
        ladderRanks.put( ladder, rank );
    }

    /**
     * Remove a rank from this player.
     * This will also remove the ladder from this player.
     *
     * @param rank The The {@link Rank} to remove.
     */
    public void removeRank(Rank rank) {

    	if ( rank != null && rank.getLadder() != null ) {
    		
    		ladderRanks.remove( rank.getLadder() );
    		
    		ranksRefs.remove( rank.getLadder().getName() );
    	}
        
//        // When we loop through, we have to store our ladder name outside the loop to
//        // avoid a concurrent modification exception. So, we'll retrieve the data we need...
//        String ladderName = null;
//        for (Map.Entry<String, Integer> rankEntry : ranksRefs.entrySet()) {
//            if (rankEntry.getValue() == rank.getId()) { // This is our rank!
//                ladderName = rankEntry.getKey();
//            }
//        }
//
//        // ... and then remove it!
//        ranksRefs.remove(ladderName);
//        
//        ladderRanks.remove( rank.getLadder() );
    }
    
    public boolean hasLadder( String ladderName ) {
    	return ranksRefs.containsKey( ladderName );
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
        	Integer id = ranksRefs.remove(ladderName);
        	results = (id != null);
        	
        	RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder( ladderName );
        	if ( ladder != null && !ladder.getName().equalsIgnoreCase( "default" ) ) {
        		ladderRanks.remove( ladder );
        	}
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
    public Rank getRank(RankLadder ladder) {
    	
    	if ( !ladderRanks.containsKey( ladder ) ) {
    		return null;
    	}
    	
    	return ladderRanks.get( ladder );
    	
//        if (!ranksRefs.containsKey(ladder.getName())) {
//            return null;
//        }
//        int id = ranksRefs.get(ladder.getName());
//        return PrisonRanks.getInstance().getRankManager().getRank(id);
    }
    
    /**
     * Retrieves the rank that this player has the specified ladder.
     *
     * @param ladder The ladder name to check.
     * @return The {@link Rank} if found, otherwise null;
     */
    public Rank getRank( String ladderName ) {
    	
    	RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder( ladderName );
    	return getRank( ladder );
    	
//    	Rank results = null;
//    	if (ladder != null && ranksRefs.containsKey(ladder)) {
//    		int id = ranksRefs.get(ladder);
//    		results = PrisonRanks.getInstance().getRankManager().getRank(id);
//    	}
//    	return results;
    }

    

//	public HashMap<String, Integer> getPrestige() {
//		return prestige;
//	}
//    public void setPrestige( HashMap<String, Integer> prestige ) {
//		this.prestige = prestige;
//	}

	public void setRanks( HashMap<String, Integer> ranks ) {
		this.ranksRefs = ranks;
	}

	/**
     * Returns all ladders this player is a part of, along with each rank the player has in that ladder.
     *
     * @return The map containing this data.
     */
    public Map<RankLadder, Rank> getLadderRanks() {
    	
    	if ( ladderRanks.isEmpty() && !ranksRefs.isEmpty() ) {
    		
    		//Map<RankLadder, Rank> ret = new HashMap<>();
    		for (Map.Entry<String, Integer> entry : ranksRefs.entrySet()) {
    			RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder(entry.getKey());
    			
    			if ( ladder == null ) {
    				continue; // Skip it
    			}
    			
    			Rank rank = PrisonRanks.getInstance().getRankManager().getRank(entry.getValue());
    			if ( rank == null ) {
    				continue; // Skip it
    			}
    			
    			ladderRanks.put(ladder, rank);
    		}
    	}

        return ladderRanks;
    }

    /*
     * equals() and hashCode()
     */

    @Override 
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RankPlayer)) {
            return false;
        }

        RankPlayer that = (RankPlayer) o;

        return uid.equals(that.uid);
    }

    @Override 
    public int hashCode() {
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
    public Block getLineOfSightBlock() {
		return null;
	}
	
	@Override
    public List<tech.mcprison.prison.internal.block.Block> getLineOfSightBlocks() {
    	
    	List<tech.mcprison.prison.internal.block.Block> results = new ArrayList<>();
    	return results;
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
		Player player = getPlayer();
		return (player != null ? player.isOp() : false );
	}
	
	
	/**
	 * NOTE: A RankPlayer does not represent an online player with inventory.  
	 *       This class is not "connected" to the underlying bukkit Player
	 *       so technically this is not a bukkit Player object, especially since it
	 *       always represents offline players too.
	 */
    @Override 
    public boolean isPlayer() {
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
	
	/**
	 * <p>Player is not cached in this class, so if using it in a function 
	 * make a local variable to save it instead of calling this function multiple
	 * times since it is a high impact lookup.
	 * </p>
	 * 
	 * @return
	 */
	private Player getPlayer() {
		Player player = null;
		
		Optional<Player> oPlayer = Prison.get().getPlatform().getPlayer( uid );
		
		if ( oPlayer.isPresent() ) {
			player = oPlayer.get();
		}
		return player;
	}
	
	@Override
	public void recalculatePermissions() {
		Player player = getPlayer();
		if ( player != null ) {
			player.recalculatePermissions();
		}
	}
	
    @Override
    public List<String> getPermissions() {
    	Player player = getPlayer();
    	return (player == null ? new ArrayList<>() : player.getPermissions() );
    }
    
    @Override
    public List<String> getPermissions( String prefix ) {
    	Player player = getPlayer();
    	return (player == null ? new ArrayList<>() : player.getPermissions( prefix ) );
    }
    


	
	/**
     * <p>This will called by the placeholders, so need to get the actual
     * multipliers that exists in the SpigotPlayer object.
     * </p>
     * 
     * <p>If the player is offline, then just set to a value of 1.0 so as 
     * not to change any other value that may be used with this function.
     * If the player is offline, then there will be no inventory that can be
     * accessed and hence, none to sell, so a value of 1.0 should be fine.
     * </p>
     * 
     */
    @Override
    public double getSellAllMultiplier() {
    	double results = 1.0;
    	
    	Player player = getPlayer();
    	if ( player != null ) {
    		results = player.getSellAllMultiplier();
    	}
//    	
//    	Optional<Player> player = Prison.get().getPlatform().getPlayer( uid );
//    	
//    	if ( player.isPresent() ) {
//    		results = player.get().getSellAllMultiplier();
//    	}
    	
    	return results;
    }

    
    
    public TreeMap<String, RankPlayerBalance> getPlayerBalances() {
		return playerBalances;
	}
	public void setPlayerBalances( TreeMap<String, RankPlayerBalance> playerBalances ) {
		this.playerBalances = playerBalances;
	}
	
	
	
    private void addCachedRankPlayerBalance( String currency, double amount ) {
    	// Since the cache will be updated, do not allow it fetch the player's balance:
    	RankPlayerBalance balance = getCachedRankPlayerBalance( currency, false );

    	balance.addBalance( amount );
    }
    
    private void setCachedRankPlayerBalance( String currency, double amount ) {
    	// Since the cache will be updated, do not allow it fetch the player's balance:
    	RankPlayerBalance balance = getCachedRankPlayerBalance( currency, false );

    	balance.setBalance( amount );
    }
    
    
    public RankPlayerBalance getCachedRankPlayerBalance( String currency ) {
    	return getCachedRankPlayerBalance( currency, true );
    }
    
    /**
     * <p>This get's the player's cached balance for the given currency.  If it does not
     * exist, it will be added.
     * </p>
     * 
     * <p>This function should never be called from outside of this RankPlayer class 
     * because the getBalance(), addBalance(), and setBalance() functions will only
     * call this function IFF the economy to support the currency exists.
     * </p>
     * 
     * @param currency Optional. If null or blank, then sets it to the internal representation
     * 					of the DEFAULT_CURRENCY.
     * @param updateBalance A boolean that if true will get the player's current balance.
     * 					Otherwise if false, will not even if the cache is too old.
     * @return Returns the RankPlayerBalance object that contains the actual
     * 					balance and the currency that's related to it.
     */
	private RankPlayerBalance getCachedRankPlayerBalance( String currency, boolean updateBalance ) {
		RankPlayerBalance balance = null;
		
		if ( currency == null || currency.trim().isEmpty() ) {
			currency = RankPlayerBalance.DEFAULT_CURRENCY;
		}
		
		if ( !getPlayerBalances().containsKey( currency ) ) {
			getPlayerBalances().put( currency, new RankPlayerBalance( currency, 0 ) );
		}

		balance = getPlayerBalances().get( currency );
		
		// if allowed to updateBalance && is time to refresh:
		if ( updateBalance && balance.isRefreshBalance() ) {
			// refresh the balance
			if ( RankPlayerBalance.DEFAULT_CURRENCY.equalsIgnoreCase( balance.getCurrency() ) ) {
				balance.setBalance( getBalance() );
			}
			else {
				balance.setBalance( getBalance( currency ) );
			}
			
		}
		
		return balance;
	}

	public double getBalance() {
		double results = 0;
		
		EconomyIntegration economy = PrisonAPI.getIntegrationManager().getEconomy();
		
		if ( economy != null ) {
			
			results = economy.getBalance( this );
			setCachedRankPlayerBalance( null, results );
		}
		
		return results;
	}
	
	public void addBalance( double amount ) {
		EconomyIntegration economy = PrisonAPI.getIntegrationManager().getEconomy();

		if ( economy != null ) {
			economy.addBalance( this, amount );
			addCachedRankPlayerBalance( null, amount );
		}
	}
	
	public void removeBalance( double amount ) {
		EconomyIntegration economy = PrisonAPI.getIntegrationManager().getEconomy();
		
		if ( economy != null ) {
			economy.removeBalance( this, amount );
			addCachedRankPlayerBalance( null, -1 * amount );
		}
	}
	
	public void setBalance( double amount ) {
		EconomyIntegration economy = PrisonAPI.getIntegrationManager().getEconomy();
		
		if ( economy != null ) {
			economy.setBalance( this, amount );
			setCachedRankPlayerBalance( null, amount );
		}
	}
	
	
	public double getBalance( String currency ) {
		double results = 0;
		
		if ( currency == null || currency.trim().isEmpty() ) {
			// No currency specified, so use the default currency:
			results = getBalance();
		}
		else {
			
			EconomyCurrencyIntegration currencyEcon = PrisonAPI.getIntegrationManager()
					.getEconomyForCurrency( currency );
			if ( currencyEcon != null ) {
				
				results = currencyEcon.getBalance( this, currency );
				setCachedRankPlayerBalance( currency, results );
			}
		}
		
		return results;
	}
	
	public void addBalance( String currency, double amount ) {

		if ( currency == null || currency.trim().isEmpty() || "default".equalsIgnoreCase( currency ) ) {
			// No currency specified, so use the default currency:
			
//			double pre = getBalance();
			addBalance( amount );
//			double post = getBalance();
//			Output.get().logInfo( "###  RankPlayer.addBalance() amount= %s  pre= %s  post= %s", 
//					Double.toString( amount ), Double.toString( pre ), Double.toString( post ));
		}
		else {
			EconomyCurrencyIntegration currencyEcon = PrisonAPI.getIntegrationManager()
					.getEconomyForCurrency(currency );
			
			if ( currencyEcon != null ) {
				currencyEcon.addBalance( this, amount, currency );
				addCachedRankPlayerBalance( currency, amount );
			}
		}
	}
	
	public void removeBalance( String currency, double amount ) {
		
		if ( currency == null || currency.trim().isEmpty() ) {
			// No currency specified, so use the default currency:
			removeBalance( amount );
		}
		else {
			EconomyCurrencyIntegration currencyEcon = PrisonAPI.getIntegrationManager()
					.getEconomyForCurrency(currency );
			
			if ( currencyEcon != null ) {
				currencyEcon.removeBalance( this, amount, currency );
				addCachedRankPlayerBalance( currency, -1 * amount );
			}
		}
	}
	
	public void setBalance( String currency, double amount ) {
		
		if ( currency == null || currency.trim().isEmpty() ) {
			// No currency specified, so use the default currency:
			setBalance( amount );
		}
		else {
			
			EconomyCurrencyIntegration currencyEcon = PrisonAPI.getIntegrationManager()
					.getEconomyForCurrency(currency );
			
			if ( currencyEcon != null ) {
				currencyEcon.setBalance( this, amount, currency );
				setCachedRankPlayerBalance( currency, amount );
			}
		}
	}

	@Override
	public List<String> getPermissionsIntegrations( boolean detailed ) {
		List<String> results = new ArrayList<>();
		
		return results;
	}
}
