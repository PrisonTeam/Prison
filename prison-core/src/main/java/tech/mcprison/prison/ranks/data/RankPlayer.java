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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.cache.PlayerCache;
import tech.mcprison.prison.cache.PlayerCachePlayerData;
import tech.mcprison.prison.integration.EconomyCurrencyIntegration;
import tech.mcprison.prison.integration.EconomyIntegration;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.inventory.Inventory;
import tech.mcprison.prison.internal.scoreboard.Scoreboard;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.placeholders.PlaceholdersUtil;
import tech.mcprison.prison.util.Gamemode;
import tech.mcprison.prison.util.Location;
import tech.mcprison.prison.util.Text;

/**
 * Represents a player with ranks.
 *
 * @author Faizaan A. Datoo
 */
public class RankPlayer 
			implements Player {

	public static final long DELAY_THREE_SECONDS = 20 * 3; // 3 seconds in ticks
	
	public static final long RANK_SCORE_COOLDOWN_MS = 1000 * 30; // 30 seconds
	public static final double RANK_SCORE_BALANCE_THRESHOLD_PERCENT = 0.05d; // 5%
	
		
    /*
     * Fields & Constants
     */

    private UUID uid;
    
    
    private TreeMap<RankLadder, PlayerRank> ladderRanks;
    
    // ranks is the storage structure used to save the player's ladder & ranks:
    private HashMap<String, Integer> ranksRefs; // <Ladder Name, Rank ID>
    
    // This prestige is not used.  Current prestige is just another ladder.
    //private HashMap<String, Integer> prestige; // <Ladder Name, Prestige>
    
    private List<RankPlayerName> names;
    
//    // Block name, count
//    private HashMap<String, Integer> blocksMined;

    
    // For tops processing.  Need current balance.
    private TreeMap<String, RankPlayerBalance> playerBalances;


    
    private EconomyIntegration economy = null;
    private double unsavedBalance = 0;
    private Object unsavedBalanceLock = new Object();
    private int ubTaskId = 0;
    
    private HashMap<String, EconomyIntegration> economyCustom = new HashMap<>();;
    
    
    
    /**
     * <p>The 'rankScore' fields are used to calculate the rankScore, of which
     * the 'rankScoreBalance' is used to track the player's balance when these
     * values were last calculated.  It is used to determine if there should 
     * be a recalculation of the score.
     * </p>
     */
    private double rankScoreBalance = 0;
    private String rankScoreCurrency = null;
    private double rankScoreBalanceThreshold = 0;
	private double rankScore = 0;
	private double rankScorePenalty = 0;
	private long rankScoreCooldown = 0L;
    

    public RankPlayer() {
    	super();
    	
    	this.ladderRanks = new TreeMap<>();
    	
        this.ranksRefs = new HashMap<>();
        //this.prestige = new HashMap<>();
        
//        this.blocksMined = new HashMap<>();
        
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

//    @SuppressWarnings( "unchecked" )
//	public RankPlayer(Document document) {
//    	this();
//    	
//        this.uid = UUID.fromString((String) document.get("uid"));
//        LinkedTreeMap<String, Object> ranksLocal =
//            (LinkedTreeMap<String, Object>) document.get("ranks");
////        LinkedTreeMap<String, Object> prestigeLocal =
////            (LinkedTreeMap<String, Object>) document.get("prestige");
//        
//        LinkedTreeMap<String, Object> blocksMinedLocal =
//        		(LinkedTreeMap<String, Object>) document.get("blocksMined");
//        
//        Object namesListObject = document.get( "names" );
//        
//
//        for (String key : ranksLocal.keySet()) {
//            ranksRefs.put(key, ConversionUtil.doubleToInt(ranksLocal.get(key)));
//        }
//        
////        for (String key : prestigeLocal.keySet()) {
////            prestige.put(key, RankUtil.doubleToInt(prestigeLocal.get(key)));
////        }
//        
//        this.blocksMined = new HashMap<>();
//        if ( blocksMinedLocal != null ) {
//        	for (String key : blocksMinedLocal.keySet()) {
//        		blocksMined.put(key, ConversionUtil.doubleToInt(blocksMinedLocal.get(key)));
//        	}
//        }
//        
//        if ( namesListObject != null ) {
//        	
//        	for ( Object rankPlayerNameMap : (ArrayList<Object>) namesListObject ) {
//        		LinkedTreeMap<String, Object> rpnMap = (LinkedTreeMap<String, Object>) rankPlayerNameMap;
//        		
//        		if ( rpnMap.size() > 0 ) {
//        			String name = (String) rpnMap.get( "name" );
//        			long date = ConversionUtil.doubleToLong( rpnMap.get( "date" ) );
//        			
//        			RankPlayerName rankPlayerName = new RankPlayerName( name, date );
//        			getNames().add( rankPlayerName );
////        			Output.get().logInfo( "RankPlayer: uuid: " + uid + " RankPlayerName: " + rankPlayerName.toString() );
//        		}
//        		
//        	}
//        }
//        
//    }
//
//    public Document toDocument() {
//        Document ret = new Document();
//        ret.put("uid", this.uid);
//        ret.put("ranks", this.ranksRefs);
////        ret.put("prestige", this.prestige);
//        
//        ret.put("names", this.names);
//
//        ret.put("blocksMined", this.blocksMined);
//        return ret;
//    }
//

    @Override
    public String toString() {
    	return getName() + " " + getRanks();
    }
    
    public String getRanks() {
    	StringBuilder sb  = new StringBuilder();
    	
    	for ( PlayerRank rank : getLadderRanks().values() ) {
    		
			sb.append( rank.getRank().getLadder() == null ? "--" : rank.getRank().getLadder().getName() )
				.append( ":" ).append( rank.getRank().getName() ).append( " " );
		}
    	
    	return sb.toString();
    }
    
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
    		if ( name == null || 
    				name != null && !name.equalsIgnoreCase( playerName ) ) {
    			
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

//	public HashMap<String, Integer> getBlocksMined() {
//		return blocksMined;
//	}
//	public void setBlocksMined( HashMap<String, Integer> blocksMined ) {
//		this.blocksMined = blocksMined;
//	}

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
    
    
//    /**
//     * <p>This function will check to see if the player is on the default rank on 
//     * the default ladder.  If not, then it will add them.  
//     * </p>
//     * 
//     * <p>This is safe to run on anyone, even if they already are on the default ladder.
//     * </p>
//     * 
//     * <p>Note, this will not save the player's new rank.  The save function must be
//     * managed and called outside of this.
//     * </p>
//     */
//    public void firstJoin() {
//    	
//    	RankLadder defaultLadder = PrisonRanks.getInstance().getDefaultLadder();
//    	
//    	if ( !getLadderRanks().containsKey( defaultLadder ) ) {
//    		
//    		Optional<Rank> firstRank = defaultLadder.getLowestRank();
//    		
//    		if ( firstRank.isPresent() ) {
//    			Rank rank = firstRank.get();
//    			
//    			addRank( rank );
//    			
//    			Prison.get().getEventBus().post(new FirstJoinEvent( this ));
//    			
//    			FirstJoinHandlerMessages messages = new FirstJoinHandlerMessages();
//    			Output.get().logWarn( messages.firstJoinSuccess( getName() ) );
//    			
//    		} else {
//    			
//    			FirstJoinHandlerMessages messages = new FirstJoinHandlerMessages();
//    			Output.get().logWarn( messages.firstJoinWarningNoRanksOnServer() );
//    		}
//    	}
//    	
//    }
    
    /**
     * Add a rank to this player.
     * If a rank on this ladder is already attached, it will automatically be removed and replaced with this new one.
     *
     * @param rank   The {@link Rank} to add.
     * @throws IllegalArgumentException If the rank specified is not on this ladder.
     */
    public void addRank( Rank rank) {
        if ( rank.getLadder() == null ) {
            throw new IllegalArgumentException("Rank must be on ladder.");
        }

        String ladderName = rank.getLadder().getName();
        
        // Remove the current rank on this ladder first
        if (ranksRefs.containsKey(ladderName)) {
            ranksRefs.remove(ladderName);
        }
        
        if ( ladderRanks.containsKey( rank.getLadder() ) ) {
        	
        	// Remove the player from the old rank:
        	PlayerRank oldRank = ladderRanks.get( rank.getLadder() );
        	oldRank.getRank().getPlayers().remove( this );
        	
        	ladderRanks.remove( rank.getLadder() );
        }

        ranksRefs.put(ladderName, rank.getId());
        
        PlayerRank pRank = new PlayerRank( rank );
        
        ladderRanks.put( rank.getLadder(), pRank );
        
        // Add the player to the new rank:
        rank.getPlayers().add( this );
        
        
        // Calculate and apply the rank multipliers:
        recalculateRankMultipliers();
    }

    public void recalculateRankMultipliers() {
    	double multiplier = 0;
    	
    	// First gather and calculate the multipliers:
    	Set<RankLadder> keys = ladderRanks.keySet();
    	for ( RankLadder rankLadder : keys )
		{
    		PlayerRank pRank = ladderRanks.get( rankLadder );
    		
    		double rankMultiplier = pRank.getLadderBasedRankMultiplier();
    		multiplier += rankMultiplier;
		}
    	
    	// We now have the multipliers, so apply them to all ranks:
    	for ( RankLadder rankLadder : keys )
		{
    		PlayerRank pRank = ladderRanks.get( rankLadder );
			
    		pRank.applyMultiplier( multiplier );
//    		pRank.setRankCost( pRank.getRank().getCost() * (1.0 + multiplier) );
		}
    	
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

//    /**
//     * Removes a ladder from this player, including whichever rank this player had in it.
//     * Cannot remove the default ladder.
//     *
//     * @param ladderName The ladder's name.
//     */
//    public boolean removeLadder(String ladderName) {
//    	boolean results = false;
//        if ( !ladderName.equalsIgnoreCase("default") ) {
//        	Integer id = ranksRefs.remove(ladderName);
//        	results = (id != null);
//        	
//        	RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder( ladderName );
//        	if ( ladder != null && !ladder.getName().equalsIgnoreCase( "default" ) ) {
//        		ladderRanks.remove( ladder );
//        	}
//        }
//        
//        return results;
//    }

//    /**
//     * Retrieves the rank that this player has in a certain ladder, if any.
//     *
//     * @param ladder The ladder to check.
//     * @return An optional containing the {@link Rank} if found, or empty if there isn't a rank by that ladder for this player.
//     */
//    public PlayerRank getRank(RankLadder ladder) {
//    	PlayerRank results = null;
//    	
//    	if ( ladder != null ) {
//    		
//    		Set<RankLadder> keys = ladderRanks.keySet();
//    		for ( RankLadder key : keys )
//    		{
//    			if ( key != null && key.getName().equalsIgnoreCase( ladder.getName() ) ) {
//    				results = ladderRanks.get( key );
//    			}
//    		}
//    	}
//
//    	return results;
//    	
////        if (!ranksRefs.containsKey(ladder.getName())) {
////            return null;
////        }
////        int id = ranksRefs.get(ladder.getName());
////        return PrisonRanks.getInstance().getRankManager().getRank(id);
//    }
//    
//    /**
//     * Retrieves the rank that this player has the specified ladder.
//     *
//     * @param ladder The ladder name to check.
//     * @return The {@link Rank} if found, otherwise null;
//     */
//    public PlayerRank getRank( String ladderName ) {
//    	
//    	RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder( ladderName );
//    	return getRank( ladder );
//    	
////    	Rank results = null;
////    	if (ladder != null && ranksRefs.containsKey(ladder)) {
////    		int id = ranksRefs.get(ladder);
////    		results = PrisonRanks.getInstance().getRankManager().getRank(id);
////    	}
////    	return results;
//    }

    

//	public HashMap<String, Integer> getPrestige() {
//		return prestige;
//	}
//    public void setPrestige( HashMap<String, Integer> prestige ) {
//		this.prestige = prestige;
//	}

	public void setRanks( HashMap<String, Integer> ranks ) {
		this.ranksRefs = ranks;
	}

//	/**
//     * Returns all ladders this player is a part of, along with each rank the player has in that ladder.
//     *
//     * @return The map containing this data.
//     */
//    public Map<RankLadder, PlayerRank> getLadderRanks( RankPlayer rankPlay) {
//    	
//    	if ( ladderRanks.isEmpty() && !ranksRefs.isEmpty() ) {
//    		
//    		//Map<RankLadder, Rank> ret = new HashMap<>();
//    		
//    		for (Map.Entry<String, Integer> entry : rankPlay.getRanksRefs().entrySet()) {
//    			RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder(entry.getKey());
//    			
//    			if ( ladder == null ) {
//    				continue; // Skip it
//    			}
//    			
//    			Rank rank = PrisonRanks.getInstance().getRankManager().getRank(entry.getValue());
//    			if ( rank == null ) {
//    				continue; // Skip it
//    			}
//    			
//    			PlayerRank pRank = new PlayerRank( rank );
//    			
//    			ladderRanks.put(ladder, pRank);
//    		}
//    		
//    		// Need to recalculate all rank multipliers:
//    		recalculateRankMultipliers();
//    	}
//
//        return ladderRanks;
//    }

	public TreeMap<RankLadder, PlayerRank> getLadderRanks() {
		return ladderRanks;
	}
//	public void setLadderRanks( TreeMap<RankLadder, PlayerRank> ladderRanks ) {
//		this.ladderRanks = ladderRanks;
//	}
	
	private RankLadder getRankLadder( String ladderName ) {
		RankLadder results = null;
		
		for ( RankLadder rLadder : getLadderRanks().keySet() ) {
			if ( rLadder.getName().equalsIgnoreCase( ladderName ) ) {
				results = rLadder;
			}
		}
		
		return results;
	}
	public PlayerRank getPlayerRank( String ladderName ) {
		PlayerRank results = null;
		
		RankLadder rLadder = getRankLadder( ladderName );
		
		if ( rLadder != null ) {
			results = getLadderRanks().get( rLadder );
		}
		
		return results;
	}
	public PlayerRank getPlayerRankDefault() {
		return getPlayerRank( "default" );
	}
	public PlayerRank getPlayerRankPrestiges() {
		return getPlayerRank( "prestiges" );
	}
    
    public HashMap<String, Integer> getRanksRefs(){
		return ranksRefs ;
	}
	public void setRanksRefs( HashMap<String, Integer> ranksRefs ) {
		this.ranksRefs = ranksRefs;
	}

	/**
     * <p>This function will check to see if the player has the same rank as the
     * targetRank, or if the target rank is lower on the ladder than where their
     * current rank is located.  This confirms that the two ranks are on the same
     * ladder, and it walks down the ladder, starting with the player's rank, until
     * it finds a match with the target rank.
     * </p>
     * 
     * @param targetRank
     * @return
     */
    public boolean hasAccessToRank( Rank targetRank ) {
    	boolean hasAccess = false;
    	
    	if ( targetRank != null && targetRank.getLadder() != null ) {
    		
    		PlayerRank pRank = getLadderRanks().get( targetRank.getLadder() );
    		
//    		PlayerRank pRank = getRank( targetRank.getLadder() );
    		if ( pRank != null ) {
    			
    			Rank rank = pRank.getRank();
    			if ( rank != null && 
    					rank.getLadder().equals( targetRank.getLadder() ) ) {
    				
    				hasAccess = rank.equals( targetRank );
    				Rank priorRank = rank.getRankPrior();
    				
    				while ( !hasAccess && priorRank != null ) {
    					
    					hasAccess = priorRank.equals( targetRank );
    					priorRank = priorRank.getRankPrior();
    				}
    			}
    		}
    	}
    	return hasAccess;
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
		PrisonAPI.dispatchCommand( command );
	}
	
	@Override
	public boolean hasPermission( String perm ) {
		Output.get().logError( "RankPlayer.hasPermission: Cannot access permissions for offline players." );
		return false;
	}
	
	@Override
	public void sendMessage( String message ) {
		Output.get().logError( "RankPlayer.sendMessage: Cannot send messages to offline players." );
	}
	
	@Override
	public void sendMessage( String[] messages ) {
		Output.get().logError( "RankPlayer.sendMessage: Cannot send messages to offline players." );
	}
	
	@Override
	public void sendRaw( String json ) {
		Output.get().logError( "RankPlayer.sendRaw: Cannot send messages to offline players." );
	}

	@Override
	public boolean doesSupportColors() {
		return false;
	}

	@Override
	public void give( ItemStack itemStack ) {
		Output.get().logError( "RankPlayer.give: Cannot give to offline players." );
	}

	@Override
	public Location getLocation() {
		Output.get().logError( "RankPlayer.getLocation: Offline players have no location." );
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
		Output.get().logError( "RankPlayer.teleport: Offline players cannot be teleported." );
	}

	@Override
	public void setScoreboard( Scoreboard scoreboard ) {
		Output.get().logError( "RankPlayer.setScoreboard: Offline players cannot use scoreboards." );
	}

	@Override
	public Gamemode getGamemode() {
		Output.get().logError( "RankPlayer.getGamemode: Offline is not a valid gamemode." );
		return null;
	}

	@Override
	public void setGamemode( Gamemode gamemode ) {
	}

	@Override
	public Optional<String> getLocale() {
		Output.get().logError( "RankPlayer.getLocale: Offline is not a valid gamemode." );
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
    	Player player = getPlayer();
    	return (player != null ? player.isPlayer() : false );
    }
	

	@Override
	public void updateInventory() {
		Player player = getPlayer();
		if ( player != null ) {
			
			player.updateInventory();
		}
	}

	@Override
	public Inventory getInventory() {
		Inventory results = null;
		
		Player player = getPlayer();
		if ( player != null ) {
			
			results = player.getInventory();
		}
		
		return results;
	}

//	@Override
//	public void printDebugInventoryInformationToConsole() {
//		
//	}
	
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
		
		EconomyIntegration economy = getEconomy();
		
		if ( economy != null ) {
			
			results = economy.getBalance( this );
			
			synchronized ( unsavedBalanceLock )
			{
				results += getBalanceUnsaved();
			}
			
			setCachedRankPlayerBalance( null, results );
		}
		
		return results;
	}
	
	public double getBalanceUnsaved() {
		return unsavedBalance;
	}
	
	public void addBalance( double amount ) {
		
		synchronized ( unsavedBalanceLock )
		{
			unsavedBalance += amount;
			
			if ( ubTaskId == 0 ) {
				
				ubTaskId = PrisonAPI.getScheduler().runTaskLaterAsync( 
						() -> {
							double tempBalance = 0;
							synchronized ( unsavedBalanceLock )
							{
								tempBalance = unsavedBalance;

								addBalanceEconomy( tempBalance );
								
								unsavedBalance = 0;
								ubTaskId = 0;
							}
						}, DELAY_THREE_SECONDS );
			}
		}
		
	
	}
	
	private boolean addBalanceEconomy( double amount ) {
		boolean results = false;
		
		EconomyIntegration economy = getEconomy();
		
		if ( economy != null ) {
			results = economy.addBalance( this, amount );
			addCachedRankPlayerBalance( null, amount );
		}
		return results;
	}
	
	public void removeBalance( double amount ) {
		
		double targetAmount = -1 * amount;
		addBalance( targetAmount );
		addCachedRankPlayerBalance( null, targetAmount );
		
//		EconomyIntegration economy = getEconomy();
//		
//		if ( economy != null ) {
//			economy.removeBalance( this, amount );
//			addCachedRankPlayerBalance( null, -1 * amount );
//		}
	}
	
	public void setBalance( double amount ) {
		
		// First subtract current balance:
		double targetAmount = -1 * getBalance();
		
		// add current amount which will result in the correct "ajustment":
		targetAmount += amount;
		
		addBalance( targetAmount );
		addCachedRankPlayerBalance( null, targetAmount );
		
//		EconomyIntegration economy = getEconomy();
//		
//		if ( economy != null ) {
//			economy.setBalance( this, amount );
//			setCachedRankPlayerBalance( null, amount );
//		}
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
	
	
	public boolean addBalanceBypassCache( double amount ) {
		boolean results = false;
		
		synchronized ( unsavedBalanceLock ) {
			
			results = addBalanceEconomy( amount );
		}
		
		return results;
	}
	
	public boolean addBalanceBypassCache( String currency, double amount ) {
		boolean results = false;
		
		if ( currency == null || currency.trim().isEmpty() || "default".equalsIgnoreCase( currency ) ) {
			
			results = addBalanceBypassCache( amount );
		}
		else {
			EconomyCurrencyIntegration currencyEcon = PrisonAPI.getIntegrationManager()
					.getEconomyForCurrency(currency );
			
			if ( currencyEcon != null ) {
				
				synchronized ( unsavedBalanceLock ) {
					results = currencyEcon.addBalance( this, amount, currency );
				}
				addCachedRankPlayerBalance( currency, amount );
			}
		}
		
		return results;
	}
	
	public boolean removeBalanceBypassCache( double amount ) {
		boolean results = false;
		
		double targetAmount = -1 * amount;
		results = addBalanceBypassCache( targetAmount );
		addCachedRankPlayerBalance( null, targetAmount );
		
		return results;
	}

	public boolean removeBalanceBypassCache( String currency, double amount ) {
		boolean results = false;
		
		if ( currency == null || currency.trim().isEmpty() ) {
			// No currency specified, so use the default currency:
			results = removeBalanceBypassCache( amount );
		}
		else {
			EconomyCurrencyIntegration currencyEcon = PrisonAPI.getIntegrationManager()
					.getEconomyForCurrency(currency );
			
			if ( currencyEcon != null ) {
				
				synchronized ( unsavedBalanceLock ) {
					results = currencyEcon.removeBalance( this, amount, currency );
				}
				addCachedRankPlayerBalance( currency, -1 * amount );
			}
		}
		return results;
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
				
				synchronized ( unsavedBalanceLock ) {
					currencyEcon.removeBalance( this, amount, currency );
				}
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
	
	/**
	 * addBalance: original t3: 2.19 1.34 1.37 1.37 1.24 0.81 1.49 2.05 1.17 0.98 1.0 0.62
	 * addBalance: opt Econ-localVar t3: 3.46 1.21 0.88 0.84 1.48 1.3 0.6 1.09 1.62 0.72 0.64 0.67 1.02 0.63 0.97 1.8
	 * addBalance: cached t3: 0.0322 0.0011 0.0016 0.0012 0.0111 0.0012 0.0012 0.0018 0.0011 0.0102 0.0010 0.0013 0.0015 0.0011 
	 * @return
	 */
	private EconomyIntegration getEconomy() {
		if ( economy == null ) {
			economy = PrisonAPI.getIntegrationManager().getEconomy();
		}
		return economy;
	}

	@Override
	public List<String> getPermissionsIntegrations( boolean detailed ) {
		List<String> results = new ArrayList<>();
		
		return results;
	}
	
	@Override
	public void setTitle( String title, String subtitle, int fadeIn, int stay, int fadeOut ) {
		Prison.get().getPlatform().setTitle( this, title, subtitle, fadeIn, stay, fadeOut );
	}
	
	@Override
	public void setActionBar( String actionBar ) {
		Prison.get().getPlatform().setActionBar( this, actionBar );
	}
	
	@Override
	public PlayerCache getPlayerCache() {
		return PlayerCache.getInstance();
	}
	
	@Override
	public PlayerCachePlayerData getPlayerCachePlayerData() {
		return PlayerCache.getInstance().getOnlinePlayer( this );
	}
	
	@Override
	public boolean isSneaking() {
		return false;
	}
	
	/**
	 * <p>Calculates the rankScore for the player's rank on the default ladder.
	 * The calculation is based upon how much the next rank costs.
	 * </p>
	 * 
	 */
	private void calculateRankScore() {
		PlayerRank rankCurrent = getPlayerRankDefault();

		Rank nRank = rankCurrent.getRank().getRankNext();
		String rankNextCurrency = nRank.getCurrency();
		
		PlayerRank pRankNext = rankCurrent.getTargetPlayerRankForPlayer( this, nRank );
		
		double cost = pRankNext.getRankCost();
		double balance = getBalance( rankNextCurrency );
		
		double score = balance;
		double penalty = 0d;
		
		// Do not apply the penalty if cost is zero:
		if ( isHesitancyDelayPenaltyEnabled() && cost > 0 ) {
			score = balance > cost ? cost : score;
			
			double excess = balance > cost ? balance - cost : 0d;
			penalty = excess * 0.2d;
		}
		
		score = (score - penalty);
		
		if ( cost > 0 ) {
			score /= cost * 100.0d;
		}
		
		double balanceThreshold = cost * RANK_SCORE_BALANCE_THRESHOLD_PERCENT;
		
		setRankScoreBalance( balance );
		setRankScoreCurrency( rankNextCurrency );
		setRankScoreBalanceThreshold( balanceThreshold );
		setRankScore( score );
		setRankScorePenalty( penalty );
		
		setRankScoreCooldown( System.currentTimeMillis() + RANK_SCORE_COOLDOWN_MS );
	}

	private void checkRecalculateRankScore() {
		
		if ( getRankScoreCooldown() == 0L || 
			System.currentTimeMillis() > getRankScoreCooldown() 
				) {
			
			double currentBalance = getBalance( getRankScoreCurrency() );
			
			if ( getRankScoreBalance() != 0 && (
					currentBalance == getRankScoreBalance() ||
					currentBalance >= (getRankScoreBalance() - getRankScoreBalanceThreshold()) ||
					currentBalance <= (getRankScoreBalance() + getRankScoreBalanceThreshold() ) )) {
				
				// increment the cooldown since the balance is either the same, or still
				// within the threshold range:
				setRankScoreCooldown( System.currentTimeMillis() + RANK_SCORE_COOLDOWN_MS );
			}
			else {
				calculateRankScore();
			}
		}
	}
	
	public static String printRankScoreLine1Header() {
		String header = String.format(
				"Rank  %-16s %-9s  %-6s %-9s %-9s %-9s",
					"Player",
					"Prestiges",
					"Rank",
					"Balance",
					"Rank-Score",
					"Penalty"
						
				);
		return header;
	}
	
	public String printRankScoreLine1( int rankPostion ) {
		
		DecimalFormat dFmt = new DecimalFormat("#,##0.00");
		
		PlayerRank prestRank = getPlayerRankPrestiges();
		PlayerRank defRank = getPlayerRankDefault();
		
		String prestRankTag = prestRank == null ? "---" : prestRank.getRank().getTag();
		String defRankTag = defRank == null ? "---" : defRank.getRank().getTag();
		
		String prestRankTagNc = Text.stripColor(prestRankTag);
		String defRankTagNc = Text.stripColor(defRankTag);
		
		String balanceStr = PlaceholdersUtil.formattedKmbtSISize( getBalance(), dFmt, " " );
		String sPenaltyStr = PlaceholdersUtil.formattedKmbtSISize( getRankScorePenalty(), dFmt, " " );
		
		String message = String.format(
				" %-3d  %-18s %-7s %-7s %9s %9s %9s",
					(rankPostion > 0 ? rankPostion : ""),
					getName(),
					prestRankTagNc,
					defRankTagNc,
					balanceStr,
					dFmt.format( getRankScore() ),
					sPenaltyStr
				);
		
		message = message
					.replace(prestRankTagNc, prestRankTag + "&r")
					.replace(defRankTagNc, defRankTag + "&r");

		return message;
	}
	
	public static String printRankScoreLine2Header() {
		String header = String.format(
				"Rank %s %s %-15s %9s",
				"Ranks",
				"Rank-Score",
				"Player",
				"Balance"
				
				);
		return header;
	}
	
	public String printRankScoreLine2( int rankPostion ) {
		
		DecimalFormat dFmt = new DecimalFormat("#,##0.00");
		
		PlayerRank prestRank = getPlayerRankPrestiges();
		PlayerRank defRank = getPlayerRankDefault();
		
		String prestRankTag = prestRank == null ? "---" : prestRank.getRank().getTag();
		String defRankTag = defRank == null ? "---" : defRank.getRank().getTag();
		
		String prestRankTagNc = Text.stripColor(prestRankTag);
		String defRankTagNc = Text.stripColor(defRankTag);
		
		String balanceStr = PlaceholdersUtil.formattedKmbtSISize( getBalance(), dFmt, " " );
//		String sPenaltyStr = PlaceholdersUtil.formattedKmbtSISize( getRankScorePenalty(), dFmt, " " );
		
		String ranks = prestRankTagNc + defRankTagNc;
		String message = String.format(
				" %-3d %-9s %6s %-17s %9s",
				(rankPostion > 0 ? rankPostion : ""),
				ranks,
				dFmt.format( getRankScore() ),
				getName(),
				balanceStr
				);
		
		message = message
				.replace(prestRankTagNc, prestRankTag + "&r")
				.replace(defRankTagNc, defRankTag + "&r");
		
		return message;
	}
	
	public boolean isHesitancyDelayPenaltyEnabled() {
		return Prison.get().getPlatform()
				.getConfigBooleanTrue( "top-stats.rank-players.hesitancy-delay-penalty" );
	}
	
	public double getRankScoreBalance() {
		return rankScoreBalance;
	}
	public void setRankScoreBalance( double rankScoreBalance ) {
		this.rankScoreBalance = rankScoreBalance;
	}

	public String getRankScoreCurrency() {
		return rankScoreCurrency;
	}
	public void setRankScoreCurrency( String rankScoreCurrency ) {
		this.rankScoreCurrency = rankScoreCurrency;
	}

	public double getRankScoreBalanceThreshold() {
		return rankScoreBalanceThreshold;
	}
	public void setRankScoreBalanceThreshold( double rankScoreBalanceThreshold ) {
		this.rankScoreBalanceThreshold = rankScoreBalanceThreshold;
	}
	public double getRankScore() {
		
		// check if the rankScore needs to be reset:
		checkRecalculateRankScore();
		
		return rankScore;
	}
	public void setRankScore( double rankScore ) {
		this.rankScore = rankScore;
	}

	public double getRankScorePenalty() {
		return rankScorePenalty;
	}
	public void setRankScorePenalty( double rankScorePenalty ) {
		this.rankScorePenalty = rankScorePenalty;
	}

	public long getRankScoreCooldown() {
		return rankScoreCooldown;
	}
	public void setRankScoreCooldown( long rankScoreCooldown ) {
		this.rankScoreCooldown = rankScoreCooldown;
	}
}
