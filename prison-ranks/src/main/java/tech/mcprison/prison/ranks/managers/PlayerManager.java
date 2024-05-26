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

package tech.mcprison.prison.ranks.managers;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import com.google.common.eventbus.Subscribe;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.cache.PlayerCache;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.PlayerUtil;
import tech.mcprison.prison.internal.events.player.PlayerJoinEvent;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.placeholders.ManagerPlaceholders;
import tech.mcprison.prison.placeholders.PlaceHolderKey;
import tech.mcprison.prison.placeholders.PlaceholderAttributeBar;
import tech.mcprison.prison.placeholders.PlaceholderAttributeNumberFormat;
import tech.mcprison.prison.placeholders.PlaceholderAttributeText;
import tech.mcprison.prison.placeholders.PlaceholderIdentifier;
import tech.mcprison.prison.placeholders.PlaceholderManager;
import tech.mcprison.prison.placeholders.PlaceholderManager.PlaceholderFlags;
import tech.mcprison.prison.placeholders.PlaceholderManager.PrisonPlaceHolders;
import tech.mcprison.prison.placeholders.PlaceholderManagerUtils;
import tech.mcprison.prison.placeholders.PlaceholdersUtil;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.PlayerRank;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.data.RankPlayerFactory;
import tech.mcprison.prison.store.Collection;
import tech.mcprison.prison.store.Document;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;

/**
 * Manages all the players in the records.
 *
 * @author Faizaan A. Datoo
 */
public class PlayerManager
	extends PlayerManagerMessages
	implements ManagerPlaceholders {


    private Collection collection;
    private List<RankPlayer> players;
    private TreeMap<String, RankPlayer> playersByName;
    
    
    private List<PlaceHolderKey> translatedPlaceHolderKeys;
    
    private transient Set<String> playerErrors;

    public PlayerManager(Collection collection) {
    	super("PlayerManager");
    	
        this.collection = collection;
        
        this.players = new ArrayList<>();
        this.playersByName = new TreeMap<>();
        
        this.playerErrors = new HashSet<>();
        
        
        Prison.get().getEventBus().register(this);
    }

    /*
     * Methods
     */

//    /**
//     * Loads a player from a file and stores it in the registry for use on the server.
//     *
//     * @param playerFile The key that the player data is stored as. Case-sensitive.
//     * @throws IOException If the file could not be read, or if the file does not exist.
//     */
//    public void loadPlayer(String playerFile) throws IOException {
//        Document document = collection.get(playerFile).orElseThrow(IOException::new);
//        
//        RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
//
//        RankPlayer rankPlayer = rankPlayerFactory.createRankPlayer(document);
//        
//        players.add( rankPlayer );
//        
//        // add by uuid:
//        playersByName.put( rankPlayer.getUUID().toString(), rankPlayer );
//        
//        // add by name:
//        if ( rankPlayer.getNames().size() > 0 ) {
//        	playersByName.put( rankPlayer.getDisplayName(), rankPlayer );
//        	
//        }
//    }

    /**
     * Loads every player in the specified playerFolder.
     *
     * @throws IOException If one of the files could not be read, or if the playerFolder does not exist.
     */
    public void loadPlayers() throws IOException {
        List<Document> playerDocss = collection.getAll();
        
        final RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
        
        for ( Document playerDocument : playerDocss )
		{
        	RankPlayer rankPlayer = rankPlayerFactory.createRankPlayer(playerDocument);
            
            players.add( rankPlayer );
            
            // add by uuid:
            playersByName.put( rankPlayer.getUUID().toString(), rankPlayer );
            
            // add by name:
            if ( rankPlayer.getNames().size() > 0 ) {
            	playersByName.put( rankPlayer.getDisplayName(), rankPlayer );
            	
            }
            
		}
        

//        players.forEach(
//        		document -> 
//        			this.players.add(
//        					rankPlayerFactory.createRankPlayer(document) ));
        
    }
    
    

    /**
     * Saves a {@link RankPlayer} to disk.
     *
     * @param player     The {@link RankPlayer} to save.
     * @param playerFile The key to save as.
     * @throws IOException If the file could not be created or written to.
     * @see #savePlayer(RankPlayer) To save with the default conventional filename.
     */
    private boolean savePlayer(RankPlayer player, String playerFile) throws IOException {
    	boolean success = false;
    	
    	if ( !player.isEnableDirty() || player.isEnableDirty() && player.isDirty() ) {
    		
    		collection.save(playerFile, RankPlayerFactory.toDocument( player ) );
    		
    		player.setDirty( false );
    		
    		success = true;
    	}
//    	RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
    	
//        collection.save(playerFile, RankPlayerFactory.toDocument( player ) );
//        collection.insert(playerFile, player.toDocument());y
    	
    	return success;
    }

    public boolean savePlayer(RankPlayer player) {
    	boolean success = false;
    	
    	try {
    		success = this.savePlayer(player, player.filenamePlayer());
    	}
    	catch (IOException e) {
			
			String errorMessage = cannotSaveNewPlayerFile( player.getName(), player.filenamePlayer() );
			
			Output.get().logError( errorMessage, e);
		}
    	
    	return success;
    }

    /**
     * Saves every player in the registry.  If one player fails to save, it will not
     * prevent the others from being saved.
     *
     * @throws IOException If one of the players could not be saved.
     * @see #savePlayer(RankPlayer, String)
     */
//    public void savePlayers() throws IOException {
//        for (RankPlayer player : players) {
//        	
//        	// Catch exceptions if a failed save so other players can be saved:
//            try {
//				savePlayer(player);
//			}
//			catch ( Exception e )  {
//				
//				String errorMessage = cannotSavePlayerFile( player.filename() );
//	    		
//    			if ( !getPlayerErrors().contains( errorMessage ) ) {
//    				getPlayerErrors().add( errorMessage );
//    				Output.get().logError( errorMessage );
//    			}
//    			
////				Output.get().logError(errorMessage, e);
//			}
//        }
//    }
    
    /**
     * <p>If the player does not have a default rank, then assign it to them and
     * then save their new settings.
     * </p>
     * 
     * @param rPlayer
     */
	public void checkPlayerDefaultRank( RankPlayer rPlayer ) {

		if ( rPlayer.getPlayerRankDefault() == null ) {

			 // Try to perform the first join processing to give them the default rank:
	        RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
	        rankPlayerFactory.firstJoin( rPlayer );

	        // It now saves the new player changes within firstJoin()
	        //PrisonRanks.getInstance().getPlayerManager().savePlayer( rPlayer );
			
		}
	}
    
    /**
     * <p>This function will add all the players to all of the ranks they
     * are associated with.
     * </p>
     * 
     */
    public void connectPlayersToRanks( boolean checkPlayerBalances ) {
    	for ( RankPlayer player : players ) {
			
    		for ( PlayerRank pRank : player.getLadderRanks().values() ) {
    			
    			pRank.getRank().addPlayer( player, checkPlayerBalances );
    		}
		}
    }

    /*
     * Getters & Setters
     */

    public List<RankPlayer> getPlayers() {
        return players;
    }

    public TreeMap<String, RankPlayer> getPlayersByName() {
		return playersByName;
	}

//	public List<RankPlayer> getPlayersByTop() {
//		return playersByTop;
//	}

	public Set<String> getPlayerErrors() {
		return playerErrors;
	}

	/** 
     * <p>Get the player, if they don't exist, add them.
     * </p>
     * 
     * @param uid
     * @return
     */
    public RankPlayer getPlayer(UUID uid, String playerName) {
    	
    	RankPlayer results = null;
//    	boolean dirty = false;
    	
    	playerName = playerName == null ? "" : playerName.trim();
    	
    	if ( !playerName.isEmpty() && getPlayersByName().containsKey( playerName ) ) {
    		results = getPlayersByName().get( playerName );
    	}
    	
    	if ( results == null ) {
    		
    		debugLogPlayerInfo( "getPlayer(): UUID check:", playerName, false );
    		
    		for ( RankPlayer rankPlayer : players ) {
    			if ( uid != null && rankPlayer.getUUID().equals(uid) || 
    					
    				!playerName.isEmpty() &&
    					rankPlayer.getName() != null &&
    					rankPlayer.getName().equalsIgnoreCase( playerName ) ) {
    				
    				// This checks to see if they have a new name, if so, then adds it to the history:
    				// But the UID must match:
    				if ( uid != null && rankPlayer.getUUID().equals(uid) ) {
    					rankPlayer.setEnableDirty( true );
    					rankPlayer.setDirty( rankPlayer.checkName( playerName ) );
    				}
    				
    				results = rankPlayer;
    				break;
    			}
    		}
    	}
    	
//    	Optional<RankPlayer> results = players.stream().filter(
//    			player -> (uid != null ? 
//    					player.uid.equals(uid) : 
//    						( playerName != null || playerName.trim().length() == 0 ? false :
//    							player.checkName( playerName )))).findFirst();
    	
    	if ( results == null && playerName != null && !"console".equalsIgnoreCase( playerName ) ) {
    		
    		debugLogPlayerInfo( "getPlayer(): addPlayer:", playerName, false );
    		
    		results = addPlayer(uid, playerName);
    		
    		// addPlayer() will save the player:
//    		if ( results != null ) {
//    			
//    			results.setDirty( true );
//    		}
    		
//    		dirty = results != null;
    	}
    	
//    	// Save if dirty (changed or new):
//    	if ( results != null && results.isDirty() ) {
//    		savePlayer( results );
//    		
//    	}
    	
    	return results;
    }
    
    public RankPlayer getPlayer( Player player ) {
    	RankPlayer rPlayer = null;
    	if ( player != null ) {
    		rPlayer = getPlayer( player.getUUID(), player.getName() );
    	}
    	return rPlayer;
    }
    
    
    public RankPlayer addPlayer( Player player ) {
    	return addPlayer( player.getUUID(), player.getName() );
    }
    
    private RankPlayer addPlayer( UUID uid, String playerName ) {
    	RankPlayer results = null;

    	// addPlayer can only be rank in the primary thread:
    	if ( PrisonTaskSubmitter.isPrimaryThread() ) {
    		results = addPlayerSyncTask( uid, playerName );
    	}
    	else if ( !getPlayersByName().containsKey( playerName )) {
    		
    		// Submit the sync task to add player.  But since this is an 
    		// async thread, we can only return a null.  Future requests
    		// for this player's placeholder will resolve successfully
    		// and a return value of null is perfectly acceptable.
    		NewRankPlayerSyncTask syncTask = new NewRankPlayerSyncTask( uid, playerName );
    		PrisonTaskSubmitter.runTaskLater( syncTask, 0 );
    	}
    	return results;
    }
    
    protected RankPlayer addPlayerSyncTask( UUID uid, String playerName ) {
        RankPlayer newPlayer = null; 
        
        // Treat this like how we setup a singleton with sychronization with a 
        // check before and after to see if the object has been inserted:
        
        if ( uid != null && playerName != null && 
        		playerName.trim().length() > 0 && !"CONSOLE".equalsIgnoreCase( playerName ) &&
        		!getPlayersByName().containsKey( playerName )) {
        	
        	synchronized( getPlayersByName() ) {
        		
        		// recheck to ensure that the player's name is not in the getPlayersByName()
        		// collection... it could have been added since submitting the sync task:
        		
        		if ( !getPlayersByName().containsKey( playerName ) ) {
        			
        			RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
        			
        			// We need to create a new player data file.
        			newPlayer = new RankPlayer( uid, playerName );
//        			newPlayer.checkName( playerName );
        			newPlayer.setDirty( true );
        			
        			// WARNING: Must save the newPlayer object to the playerManager collections
        			//          before calling firstJoin():
        			
        			players.add(newPlayer);
        			getPlayersByName().put( playerName, newPlayer );

        			
        			debugLogPlayerInfo( "addPlayerSyncTask: firstJoin:", playerName, false );
        			
        			rankPlayerFactory.firstJoin( newPlayer );
        			
        			
        			boolean joined = newPlayer.getPlayerRankDefault() != null;
        			String msg = joined ? "joined" : "failed";
        			debugLogPlayerInfo( "addPlayerSyncTask: " + msg, playerName, false );
        			
        			
        			// the new player is now saved in firstJoin()(
        			//savePlayer(newPlayer);

        			
//        			try {
//        				
//        			} 
//        			catch (IOException e) {
//        				
//        				String errorMessage = cannotSaveNewPlayerFile( playerName, newPlayer.filename() );
//        				
//        				Output.get().logError( errorMessage, e);
//        			}
        		}
        		
        	}
        }
        
        
        return newPlayer;
    }
    
    /*
     * Listeners
     */

    @Subscribe 
    public void onPlayerJoin(PlayerJoinEvent event) {
    	
    	Player player = event.getPlayer();
    	
    	debugLogPlayerInfo( "onPlayerJoin:", player.getName(), true );

    	// Player is auto added if they do not exist when calling getPlayer so don't try to
    	// add them a second time.
        RankPlayer rPlayer = getPlayer(player.getUUID(), player.getName());
        
        rPlayer.doNothing();
    }
    
    public void debugLogPlayerInfo( String eventName, String playerName, boolean date ) {
    	
    	if ( Output.get().isDebug() ) {
    		
    		boolean newPlayer = !getPlayersByName().containsKey( playerName );
    		
    		SimpleDateFormat sdFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		
    		String msg = String.format(
    				"&6%s:  &c%s  &6%s  &d%s",
    				eventName,
    				playerName,
    				date ? sdFmt.format(new Date()) : "",
    				newPlayer ? "[New Player]" : ""
    				);
    		
    		Output.get().logInfo( msg );
    	}
    	
    }

    

    public String getPlayerRankName( RankPlayer rankPlayer, String ladderName ) {
    	StringBuilder sb = new StringBuilder();

		if ( !rankPlayer.getLadderRanks().isEmpty()) {
			for (Map.Entry<RankLadder, PlayerRank> entry : rankPlayer.getLadderRanks().entrySet()) {
				if ( ladderName == null ||
					 ladderName != null && entry.getKey().getName().equalsIgnoreCase( ladderName )) {
					
					if ( sb.length() > 0 ) {
						sb.append(" ");
					}
					sb.append(entry.getValue().getRank().getName());
				}
			}
		}

		if ( sb.length() == 0 && LadderManager.LADDER_PRESTIGES.equals( ladderName ) ) {
			// Use config setting for no-prestige-value ladder rank:
			
			String prestigeEmpty = Prison.get().getPlatform().getConfigString("prestige.no-prestige-value", "");
			sb.append( prestigeEmpty );
		}
		
		return sb.toString();
    }
    
    
    public String getPlayerRankNumber( RankPlayer rankPlayer, String ladderName, 
    						PlaceholderAttributeNumberFormat attributeNFormat ) {
    	StringBuilder sb = new StringBuilder();
    	
    	if ( !rankPlayer.getLadderRanks().isEmpty()) {
    		for (Map.Entry<RankLadder, PlayerRank> entry : rankPlayer.getLadderRanks().entrySet()) {
    			if ( ladderName == null ||
    					ladderName != null && entry.getKey().getName().equalsIgnoreCase( ladderName )) {
    				
    				if ( sb.length() > 0 ) {
    					sb.append(" ");
    				}
    				
    				int rankNumber = rankNumber(entry.getValue().getRank());
    				
    				if ( attributeNFormat != null ) {

    					sb.append( attributeNFormat.format( (long) rankNumber ) );
    				}
    				else {
    					sb.append( Integer.toString( rankNumber ) );
    				}
    			}
    		}
    	}
    	
    	return (sb.length() == 0 ? "0" : sb.toString());
    }
    
    /**
     * <p>This counts how many ranks there are from the bottom to the 
     * current rank level.  The lowest rank has a value of 1, no rank
     * will be zero.
     * </p>
     * @param value
     * @return
     */
    private int rankNumber( Rank value ) {
    	int results = 0;
    	if ( value != null ) {
    		Rank r = value;
    		while ( r != null ) {
    			results++;
    			r = r.getRankPrior();
    		}
    	}
		return results;
	}

	public String getPlayerRankTag( RankPlayer rankPlayer, String ladderName ) {
    	StringBuilder sb = new StringBuilder();
    	
    	if ( !rankPlayer.getLadderRanks().isEmpty()) {
    		for (Map.Entry<RankLadder, PlayerRank> entry : rankPlayer.getLadderRanks().entrySet()) {
    			if ( ladderName == null ||
   					 ladderName != null && entry.getKey().getName().equalsIgnoreCase( ladderName )) {

//					if ( sb.length() > 0 ) {
//  	  				sb.append(" ");
//    				}
    				Rank rank = entry.getValue().getRank();
    				
//    				if ( rank.getLadder() != null && rank.getLadder().isDefault() && 
//    						rank.getRankNext() == null ) {
//    					PlayerRank prestigeRank = rankPlayer.getPlayerRankPrestiges();
//    					
//    					if ( prestigeRank == null ) {
//    						RankLadder prestigeLadder = PrisonRanks.getInstance()
//    														.getLadderManager().getLadderPrestiges();
//    						if ( prestigeLadder != null ) {
//    							rank = prestigeLadder.getLowestRank().orElseGet( null );
//    						}
//    					}
//    					else {
//    						rank = prestigeRank.getRank().getRankNext();
//    					}
//    					
//    				}
    				
    				String tag = rank.getTag();
    				sb.append( tag == null ? rank.getName() : tag );
    			}
    		}
    	}
    	
		if ( sb.length() == 0 && LadderManager.LADDER_PRESTIGES.equals( ladderName ) ) {
			// Use config setting for no-prestige-value ladder rank:
			
			String prestigeEmpty = Prison.get().getPlatform().getConfigString("prestige.no-prestige-value", "");
			sb.append( prestigeEmpty );
		}
		
    	return sb.toString();
    }
    
    public List<Rank> getPlayerRanks( RankPlayer rankPlayer ) {
    	List<Rank> results = new ArrayList<>();

		if ( !rankPlayer.getLadderRanks().isEmpty()) {
			for (Map.Entry<RankLadder, PlayerRank> entry : rankPlayer.getLadderRanks().entrySet()) {
				results.add( entry.getValue().getRank() );
			}
		}

		return results;
    }
    
    public List<Rank> getPlayerNextRanks( RankPlayer rankPlayer ) {
    	List<Rank> results = new ArrayList<>();
    	
    	if ( !rankPlayer.getLadderRanks().isEmpty()) {
    		
    		RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
    		
    		for ( RankLadder ladder : rankPlayer.getLadderRanks().keySet() ) {
    			
    			Rank rank = rankPlayerFactory.getRank( rankPlayer, ladder ).getRank();
				if ( rank != null && rank.getRankNext() != null ) {
					Rank nextRank = rank.getRankNext();
					
					results.add( nextRank );
				}
    		}
    	}
    	
    	return results;
    }
    
    public String getPlayerNextRankCost( RankPlayer rankPlayer, String ladderName, 
    					boolean formatted, PlaceholderAttributeNumberFormat attributeNFormat ) {
    	StringBuilder sb = new StringBuilder();
    	
    	if ( !rankPlayer.getLadderRanks().isEmpty()) {
    		DecimalFormat dFmt = Prison.get().getDecimalFormatInt();
    		
    		RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
    		
    		for ( RankLadder ladder : rankPlayer.getLadderRanks().keySet() ) {
    			
    			if ( ladderName == null ||
    					ladderName != null && ladder.getName().equalsIgnoreCase( ladderName )) {
    				
    				boolean isDefault = ladder.getName().equals( LadderManager.LADDER_DEFAULT ) ;
    				
    				PlayerRank pRank = rankPlayerFactory.getRank( rankPlayer, ladder );
    				Rank nextRank = pRank.getRank().getRankNext();

    				if ( pRank != null &&
    						( nextRank != null || nextRank == null && isDefault )) {
    					
    					nextRank = getNextPrestigeRank( rankPlayer, isDefault, nextRank );
    					
    			        // This calculates the target rank, and takes in to consideration the player's existing rank:
    			        PlayerRank nextPRank = rankPlayer.calculateTargetPlayerRank( nextRank );
//    			        PlayerRank nextPRank = pRank.getTargetPlayerRankForPlayer( rankPlayer, nextRank );

    					//PlayerRank nextPRank = new PlayerRank( nextRank, pRank.getRankMultiplier() );
    					
    			        if ( nextPRank != null ) {
    			        	
    			        	if ( sb.length() > 0 ) {
    			        		sb.append(", ");
    			        	}
    			        	
    			        	double cost = nextPRank.getRankCost();
    			        	
    			        	if ( attributeNFormat != null ) {

    			        		sb.append( attributeNFormat.format( cost ) );
    			        	}
    			        	else  if ( formatted ) {
    			        		sb.append( PlaceholdersUtil.formattedMetricSISize( cost ));
    			        	}
    			        	else {
    			        		sb.append( dFmt.format( cost ));
    			        	}
    			        }
    				}
    			}
    		}
    		
    	}
    	
    	return sb.toString();
    }

	private Rank getNextPrestigeRank( RankPlayer rankPlayer, boolean isDefault, Rank nextRank )
	{
		Rank results = nextRank;
		
		// if nextRank is null and if prestiges are enabled, then get the next prestige rank:
		if ( nextRank == null && 
				rankPlayer != null &&
				isDefault && 
					Prison.get().getPlatform().getConfigBooleanFalse( "prestige.enabled" ) ) {
			
			RankLadder rLadder = PrisonRanks.getInstance().getLadderManager().getLadder( LadderManager.LADDER_PRESTIGES );
			
			if ( rLadder != null ) {
				
				PlayerRank pLadder = rankPlayer.getLadderRanks().get( rLadder );
				
				if ( pLadder == null ) {
					
					results = rLadder.getLowestRank().orElse( null );
				}
				else {
					
					results = pLadder.getRank().getRankNext();
				}
			}
		}
		
		return results;
	}
        
    public String getPlayerNextRankCostPercent( RankPlayer rankPlayer, String ladderName, 
    				PlaceholderAttributeNumberFormat attributeNFormat ) {
    	StringBuilder sb = new StringBuilder();
    	
    	if ( !rankPlayer.getLadderRanks().isEmpty()) {
    		DecimalFormat dFmt = Prison.get().getDecimalFormat("#,##0.00");
    		
    		RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
    		
    		for ( RankLadder ladder : rankPlayer.getLadderRanks().keySet() ) {
    			
    			if ( ladderName == null ||
    					ladderName != null && ladder.getName().equalsIgnoreCase( ladderName )) {
    				
    				boolean isDefault = ladder.getName().equals( LadderManager.LADDER_DEFAULT ) ;
    				
    				PlayerRank pRank = rankPlayerFactory.getRank( rankPlayer, ladder );
    				Rank nextRank = pRank.getRank().getRankNext();

    				if ( pRank != null && 
    						( nextRank != null || nextRank == null && isDefault ) ) {
    					
    					nextRank = getNextPrestigeRank( rankPlayer, isDefault, nextRank );
    					
    			        // This calculates the target rank, and takes in to consideration the player's existing rank:
    			        PlayerRank nextPRank =  rankPlayer.calculateTargetPlayerRank( nextRank );
//    			        PlayerRank nextPRank = pRank.getTargetPlayerRankForPlayer( rankPlayer, nextRank );

// 						PlayerRank nextPRank = new PlayerRank( nextRank, pRank.getRankMultiplier() );
    			        
    			        if ( nextPRank != null ) {
    			        	
    			        	if ( sb.length() > 0 ) {
    			        		sb.append(",  ");
    			        	}
    			        	
//    						Rank rank = key.getNext(key.getPositionOfRank(entry.getValue())).get();
    			        	double cost = nextPRank.getRankCost();
    			        	double balance = rankPlayer.getBalance( pRank.getRank().getCurrency() );
//    						double balance = getPlayerBalance(prisonPlayer,nextRank);
    			        	
    			        	double percent = (balance < 0 ? 0 : 
    			        		(cost == 0.0d || balance > cost ? 100.0 : 
    			        			balance / cost * 100.0 )
    			        			);
    			        	
    			        	if ( attributeNFormat != null ) {
    			        		
    			        		sb.append( attributeNFormat.format( percent ) );
    			        	}
    			        	else {
    			        		
    			        		sb.append( dFmt.format( percent ));
    			        	}
    			        }
    				}
    			}
    		}
    	}
    	
    	return sb.toString();
    }
    
    public String getPlayerNextRankCostBar( RankPlayer rankPlayer, String ladderName, 
    														PlaceholderAttributeBar attributeBar ) {
    	StringBuilder sb = new StringBuilder();
    	
    	if ( !rankPlayer.getLadderRanks().isEmpty()) {
    		
//    		DecimalFormat dFmt = Prison.get().getDecimalFormat("#,##0.00");
    		
    		RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();

    		for ( RankLadder ladder : rankPlayer.getLadderRanks().keySet() ) {
    		
    			if ( ladderName == null ||
    					ladderName != null && ladder.getName().equalsIgnoreCase( ladderName )) {
    				
    				boolean isDefault = ladder.getName().equals( LadderManager.LADDER_DEFAULT ) ;
    				
    				PlayerRank pRank = rankPlayerFactory.getRank( rankPlayer, ladder );
    				Rank rank = pRank.getRank();
    				Rank nextRank = rank.getRankNext();

    				if ( rank != null && 
    						( nextRank != null || nextRank == null && isDefault )) {
    					
    					nextRank = getNextPrestigeRank( rankPlayer, isDefault, nextRank );
    					
    			        // This calculates the target rank, and takes in to consideration the player's existing rank:
    			        PlayerRank nextPRank = rankPlayer.calculateTargetPlayerRank( nextRank );
//    			        PlayerRank nextPRank = pRank.getTargetPlayerRankForPlayer( rankPlayer, nextRank );

//    					PlayerRank nextPRank = new PlayerRank( nextRank, pRank.getRankMultiplier() );
    					
    			        if ( nextPRank != null ) {
    			        	
    			        	if ( sb.length() > 0 ) {
    			        		sb.append(",  ");
    			        	}
    			        	
    			        	double cost = nextPRank.getRankCost();
    			        	double balance = rankPlayer.getBalance( rank.getCurrency() );
//    						double balance = getPlayerBalance(prisonPlayer,nextRank);
    			        	
    			        	
    			        	sb.append( PlaceholderManagerUtils.getInstance().
    			        			getProgressBar( balance, cost, false, attributeBar ));
    			        }
    					
    				}
    			}
    		}
    	}
    	
    	return sb.toString();
    }
    
    /**
     * <p>This function will use the player's current balance in the currency that the next ranks
     * uses, and will subtract the cost of the next rank from their current balance.  This 
     * function returns what would remain after the transaction.  If this return a negative value
     * then it's an indicator that the player cannot yet afford the next rank.
     * </p>
     * 
     * @param rankPlayer
     * @param ladderName
     * @param formatted
     * @return
     */
    public String getPlayerNextRankCostRemaining( RankPlayer rankPlayer, String ladderName, 
    						boolean formatted, PlaceholderAttributeNumberFormat attributeNFormat ) {
    	StringBuilder sb = new StringBuilder();
    	
    	if ( !rankPlayer.getLadderRanks().isEmpty()) {
    		DecimalFormat dFmt = Prison.get().getDecimalFormat("#,##0.00");
    		
    		RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();

    		for ( RankLadder ladder : rankPlayer.getLadderRanks().keySet() ) {
    			
    			if ( ladderName == null ||
    					ladderName != null && ladder.getName().equalsIgnoreCase( ladderName )) {
    				
    				boolean isDefault = ladder.getName().equals( LadderManager.LADDER_DEFAULT ) ;
    				
    				PlayerRank pRank = rankPlayerFactory.getRank( rankPlayer, ladder );
    				Rank rank = pRank.getRank();
    				Rank nextRank = rank.getRankNext();
    				
    				if ( rank != null && 
    						( nextRank != null || nextRank == null && isDefault ) ) {
    					
    					nextRank = getNextPrestigeRank( rankPlayer, isDefault, nextRank );
    					
    			        // This calculates the target rank, and takes in to consideration the player's existing rank:
    			        PlayerRank nextPRank = rankPlayer.calculateTargetPlayerRank( nextRank );
//    			        PlayerRank nextPRank = pRank.getTargetPlayerRankForPlayer( rankPlayer, nextRank );

//    					PlayerRank nextPRank = new PlayerRank( nextRank, pRank.getRankMultiplier() );
    					
    			        if ( nextPRank != null ) {
    			        	
    			        	if ( sb.length() > 0 ) {
    			        		sb.append(",  ");
    			        	}
    			        	
    			        	double cost = nextPRank.getRankCost();
    			        	double balance = rankPlayer.getBalance( rank.getCurrency() );
//    					double balance = getPlayerBalance(prisonPlayer,nextRank);
    			        	
    			        	double remaining = cost - balance;
    			        	
    			        	// Without the following, if the player has more money than what the rank will cost,
    			        	// then it would result in a negative amount, which is wrong.  
    			        	// This is cost remaining... once they are able to afford a rankup, then remaining 
    			        	// cost will be zero.
    			        	if ( remaining < 0 ) {
    			        		remaining = 0;
    			        	}
    			        	
    			        	if ( attributeNFormat != null ) {
    			        		sb.append( attributeNFormat.format( remaining ) );
    			        	}
    			        	
    			        	else if ( formatted ) {
    			        		sb.append( PlaceholdersUtil.formattedMetricSISize( remaining ));
    			        	}
    			        	else {
    			        		sb.append( dFmt.format( remaining ));
    			        	}
    			        }
    				}
    			}
    		}
    	}
    	
    	return sb.toString();
    }
    
    
  public String getPlayerNextRankCostRemainingPercent( RankPlayer rankPlayer, String ladderName ) {
  	StringBuilder sb = new StringBuilder();
  	
  	if ( !rankPlayer.getLadderRanks().isEmpty()) {
  		DecimalFormat dFmt = Prison.get().getDecimalFormatInt();
  		
  		RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
  		
		for ( RankLadder ladder : rankPlayer.getLadderRanks().keySet() ) {
			
			if ( ladderName == null ||
					ladderName != null && ladder.getName().equalsIgnoreCase( ladderName )) {
				
				boolean isDefault = ladder.getName().equals( LadderManager.LADDER_DEFAULT ) ;
				
				PlayerRank pRank = rankPlayerFactory.getRank( rankPlayer, ladder );
				Rank rank = pRank.getRank();
				Rank nextRank = rank.getRankNext();
				
				if ( rank != null && 
						( nextRank != null || nextRank == null && isDefault ) ) {
					
					nextRank = getNextPrestigeRank( rankPlayer, isDefault, nextRank );
					
			        // This calculates the target rank, and takes in to consideration the player's existing rank:
			        PlayerRank nextPRank = rankPlayer.calculateTargetPlayerRank( nextRank );
//			        PlayerRank nextPRank = pRank.getTargetPlayerRankForPlayer( rankPlayer, nextRank );

//					PlayerRank nextPRank = new PlayerRank( nextRank, pRank.getRankMultiplier() );
					
			        if ( nextPRank != null ) {
			        	
			        	if ( sb.length() > 0 ) {
			        		sb.append(",  ");
			        	}
			        	
			        	double cost = nextPRank.getRankCost();
			        	double balance = rankPlayer.getBalance( rank.getCurrency() );
//					double balance = getPlayerBalance(prisonPlayer,nextRank);
			        	
			        	double remaining = cost - balance;
			        	
			        	// Without the following, if the player has more money than what the rank will cost,
			        	// then it would result in a negative amount, which is wrong.  
			        	// This is cost remaining... once they are able to afford a rankup, then remaining 
			        	// cost will be zero.
			        	if ( remaining < 0 ) {
			        		remaining = 0;
			        	}
			        	double percent = (remaining < 0 ? 0.0 : 
			        		(cost == 0.0d || remaining > cost ? 100.0 : 
			        			remaining / cost * 100.0 )
			        			);
			        	sb.append( dFmt.format( percent ));
			        }
				}
			}
		}
  	}
  	
  	return sb.toString();
  }
  
  public String getPlayerNextRankCostRemainingBar( RankPlayer rankPlayer, String ladderName, 
		  PlaceholderAttributeBar attributeBar ) {
	  StringBuilder sb = new StringBuilder();

	  if ( !rankPlayer.getLadderRanks().isEmpty()) {
		  //		  DecimalFormat dFmt = Prison.get().getDecimalFormatInt();

		  RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
		  
		  for ( RankLadder ladder : rankPlayer.getLadderRanks().keySet() ) {

			  if ( ladderName == null ||
					  ladderName != null && ladder.getName().equalsIgnoreCase( ladderName )) {

				  boolean isDefault = ladder.getName().equals( LadderManager.LADDER_DEFAULT ) ;
				  
				  PlayerRank pRank = rankPlayerFactory.getRank( rankPlayer, ladder );
				  Rank rank = pRank.getRank();
				  Rank nextRank = rank.getRankNext();
				  
				  if ( rank != null && 
						  ( nextRank != null || nextRank == null && isDefault ) ) {

					  
	  					nextRank = getNextPrestigeRank( rankPlayer, isDefault, nextRank );
					  
  			          // This calculates the target rank, and takes in to consideration the player's existing rank:
  			          PlayerRank nextPRank = rankPlayer.calculateTargetPlayerRank( nextRank );
//  			          PlayerRank nextPRank = pRank.getTargetPlayerRankForPlayer( rankPlayer, nextRank );

//					  PlayerRank nextPRank = new PlayerRank( nextRank, pRank.getRankMultiplier() );
					  
  			          if ( nextPRank != null ) {
  			        	
  			        	  if ( sb.length() > 0 ) {
  			        		  sb.append(",  ");
  			        	  }
  			        	  
  			        	  double cost = nextPRank.getRankCost();
  			        	  double balance = rankPlayer.getBalance( rank.getCurrency() );
//					  double balance = getPlayerBalance(prisonPlayer,nextRank);
  			        	  
  			        	  double remaining = cost - balance;
  			        	  
  			        	  // Without the following, if the player has more money than what the rank will cost,
  			        	  // then it would result in a negative amount, which is wrong.  
  			        	  // This is cost remaining... once they are able to afford a rankup, then remaining 
  			        	  // cost will be zero.
  			        	  if ( remaining < 0 ) {
  			        		  remaining = 0;
  			        	  }
  			        	  //					  double percent = (remaining < 0 ? 0.0 : 
  			        	  //						  (cost == 0.0d || remaining > cost ? 100.0 : 
  			        	  //							  remaining / cost * 100.0 )
  			        	  //							  );
  			        	  //					  sb.append( dFmt.format( percent ));
  			        	  
  			        	  sb.append( PlaceholderManagerUtils.getInstance().
  			        			  getProgressBar( remaining, cost, false, attributeBar ));
  			          }
				  }

			  }
		  }

	  }

	  return sb.toString();
  }

    /**
     * <p>This gets the player's balance as a formatted String based upon the ranks' 
     * custom currency, if it's set.  If there is a problem getting the custom 
     * currency, then it will return a value of zero for the player's balance, which
     * will prevent the player from ranking up due to insufficient funds.
     * </p>
     * 
     * @param rankPlayer
     * @param ladderName
     * @param formatted
     * @return
     */
    private String getPlayerBalance( RankPlayer rankPlayer, String ladderName, 
    								boolean formatted, PlaceholderAttributeNumberFormat attributeNFormat ) {
    	StringBuilder sb = new StringBuilder();
    	
//    	Player prisonPlayer = PrisonAPI.getPlayer(rankPlayer.getUUID()).orElse(null);
//    	if( prisonPlayer == null ) {
//    		
//    		String errorMessage = cannotLoadPlayerFile( rankPlayer.getUUID().toString() );
//    		
//    		String message = "getPlayerBalance: " + errorMessage;
//    		
//			if ( !getPlayerErrors().contains( message ) ) {
//				getPlayerErrors().add( message );
//				Output.get().logError( message );
//			}
//			
////    		return "0";
//    	}
    	
    	if ( !rankPlayer.getLadderRanks().isEmpty()) {
    		DecimalFormat dFmt = Prison.get().getDecimalFormatInt();
    		
    		RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
    		
    		for ( RankLadder ladder : rankPlayer.getLadderRanks().keySet() ) {
    			
    			if ( ladderName == null ||
    					ladderName != null && ladder.getName().equalsIgnoreCase( ladderName )) {
    				
    				PlayerRank pRank = rankPlayerFactory.getRank( rankPlayer, ladder );
    				Rank rank = pRank.getRank();
    				if ( rank != null ) {
    					if ( sb.length() > 0 ) {
    						sb.append(",  ");
    					}
    					
    					double balance = rankPlayer.getBalance( rank.getCurrency() );
//    					double balance = getPlayerBalance(prisonPlayer,rank);
    					
    					if ( attributeNFormat != null ) {

    						sb.append( attributeNFormat.format( balance ) );
    					}
    					
    					else if ( formatted ) {
    						sb.append( PlaceholdersUtil.formattedMetricSISize( balance ));
    					}
    					else {
    						sb.append( dFmt.format( balance ));
    					}
    				}
    			}
    		}
    	}

    	return sb.toString();
    }
    private String getPlayerAverageEarningsPerMinute( RankPlayer rankPlayer, String ladderName, 
    		boolean formatted, PlaceholderAttributeNumberFormat attributeNFormat ) {
    	StringBuilder sb = new StringBuilder();
    	
    	
    	if ( !rankPlayer.getLadderRanks().isEmpty()) {
    		DecimalFormat dFmt = Prison.get().getDecimalFormatInt();
    		
    		double epm = PlayerCache.getInstance().getPlayerEarningsPerMinute( rankPlayer );
    		
    		if ( attributeNFormat != null ) {

    			sb.append( attributeNFormat.format( epm ) );
    		}
    		
    		else if ( formatted ) {
    			sb.append( PlaceholdersUtil.formattedMetricSISize( epm ));
    		}
    		else {
    			sb.append( dFmt.format( epm ));
    		}
    		
    	}
    	
    	return sb.toString();
    }
 
    
    private String getPlayerTokenBalance( RankPlayer rankPlayer, 
    						int formatMode, PlaceholderAttributeNumberFormat attributeNFormat ) {
    	StringBuilder sb = new StringBuilder();
    	
    	DecimalFormat dFmt = Prison.get().getDecimalFormatInt();
    	
    	long tokens = rankPlayer.getPlayerCachePlayerData().getTokens();
    	
    	if ( attributeNFormat != null ) {

    		sb.append( attributeNFormat.format( tokens ) );
    	}
    	
    	else {
    		switch ( formatMode )
			{
				case 1: {
					sb.append( dFmt.format( tokens ));
					
					break;
				}
				case 2: {
					sb.append( PlaceholdersUtil.formattedMetricSISize( tokens ));
					
					break;
				}
				case 3: {
					sb.append( PlaceholdersUtil.formattedKmbtSISize( tokens, dFmt, " " ));
					
					break;
				}
				default:
					sb.append( Long.toString( tokens ));
			}
    	}
    		
//    	if ( formatted ) {
//    		sb.append( PlaceholdersUtil.formattedMetricSISize( tokens ));
//    	}
//    	else {
//    		sb.append( dFmt.format( tokens ));
//    	}
    	
    	return sb.toString();
    }
    
    
    private String getPlayerTokenAverageEarningsPerMinute( RankPlayer rankPlayer, 
    						boolean formatted, PlaceholderAttributeNumberFormat attributeNFormat ) {
    	StringBuilder sb = new StringBuilder();
    	
    	
    	if ( !rankPlayer.getLadderRanks().isEmpty()) {
    		DecimalFormat dFmt = Prison.get().getDecimalFormatInt();
    		
    		
    		double tpm = rankPlayer.getPlayerCachePlayerData().getAverageTokensPerMinute();
    		
    		if ( attributeNFormat != null ) {

    			sb.append( attributeNFormat.format( tpm ) );
    		}
    		
    		else if ( formatted ) {
    			sb.append( PlaceholdersUtil.formattedMetricSISize( tpm ));
    		}
    		else {
    			sb.append( dFmt.format( tpm ));
    		}
    		
    	}
    	
    	return sb.toString();
    }
    
    
    
//    /**
//     * <p>This gets the player's balance, and if the rank is provided, it will check to 
//     * see if there is a custom currency that needs to be used for that rank.  If there
//     * is a custom currency, then it will check the balance for that player using that
//     * currency.
//     * </p>
//     * 
//     * @param player
//     * @param rank
//     * @return
//     */
//    public double getPlayerBalance(Player player, Rank rank) {
//    	double playerBalance = 0;
//        	
//    	if ( player != null ) {
//    		
//    		if ( rank != null && rank.getCurrency() != null ) {
//    			EconomyCurrencyIntegration currencyEcon = PrisonAPI.getIntegrationManager()
//    					.getEconomyForCurrency( rank.getCurrency() );
//    			if ( currencyEcon != null ) {
//    				playerBalance = currencyEcon.getBalance( player, rank.getCurrency() );
//    			} else {
//    				
//    				String errorMessage = cannotLoadEconomyCurrency( player.getName(), rank.getCurrency() );
//    				
//    				if ( !getPlayerErrors().contains( errorMessage ) ) {
//    					getPlayerErrors().add( errorMessage );
//    					Output.get().logError( errorMessage );
//    				}
//    				
//    			}
//    			
//    		} else {
//    			
//    			EconomyIntegration economy = PrisonAPI.getIntegrationManager().getEconomy();
//    			
//    			if ( economy != null ) {
//    				playerBalance = economy.getBalance( player );
//    			} else {
//    				
//    				String errorMessage = cannotLoadEconomy( player.getName() );
//    				
//    				if ( !getPlayerErrors().contains( errorMessage ) ) {
//    					
//    					getPlayerErrors().add( errorMessage );
//    					Output.get().logError( errorMessage );
//    				}
//    				
//    			}
//    		}
//    	}
//
//    	return playerBalance;
//    }
    
    public String getPlayerNextRankName( RankPlayer rankPlayer, String ladderName ) {
    	StringBuilder sb = new StringBuilder();
    	
    	if ( !rankPlayer.getLadderRanks().isEmpty()) {
    		
    		RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
    		
    		for ( RankLadder ladder : rankPlayer.getLadderRanks().keySet() ) {
    			
    			if ( ladderName == null ||
    					ladderName != null && ladder.getName().equalsIgnoreCase( ladderName )) {
    				
    				PlayerRank pRank = rankPlayerFactory.getRank( rankPlayer, ladder );
    				Rank rank = pRank.getRank();
    				
  				  	if ( rank != null && rank.getRankNext() != null ) {
  				  		Rank nextRank = rank.getRankNext();
  				  		
  				  		if ( sb.length() > 0 ) {
  				  			sb.append(" ");
  				  		}
  				  		sb.append( nextRank.getName( ));
  				  	}
    			}
    		}
    	}
    	
    	return sb.toString();
    }
    
    
    public String getPlayerNextLinkedRankTag( RankPlayer rankPlayer, String ladderName ) {
    	StringBuilder sb = new StringBuilder();
    	
    	// Must always have a default rank:
    	PlayerRank pRankDefault = rankPlayer.getPlayerRankDefault();

    	// Prestiges ladder may not be enabled, or this may be null because they have not yet prestiged:
    	PlayerRank pRankPrestiges = rankPlayer.getPlayerRankPrestiges();
    	
    	if ( ladderName == null || ladderName.equalsIgnoreCase( LadderManager.LADDER_PRESTIGES ) ) {
    		
    		
    		// If default rank is the last rank, or at the last prestiges rank,
    		// then get next prestige rank for player:
    		if ( pRankDefault.getRank().getRankNext() == null ) {
    			
    			// If the player does not have a prestiges rank, then get the first one:
    			if ( pRankPrestiges == null && PrisonRanks.getInstance().getPrestigesLadder() != null ) {
    				
    				Rank firstPrestigeRank = PrisonRanks.getInstance().getPrestigesLadder().getLowestRank().orElse(null);
    				if ( firstPrestigeRank != null ) {
    					sb.append( firstPrestigeRank.getTag() );
    				}
    			}
    			// Else if player has a prestige rank, and there is a next prestige rank, get it's tag:
    			else if ( pRankPrestiges != null && pRankPrestiges.getRank().getRankNext() != null ) {
    				sb.append( pRankPrestiges.getRank().getRankNext().getTag() );
    				
    			}
    			
    			// else, if player has a prestige rank, and it's the last one, then just get that tag:
    			else if ( pRankPrestiges != null ) {
    				sb.append( pRankPrestiges.getRank().getTag() );
    				
    			}
    			
    		}
    		// else just get current prestige rank for player:
    		else if ( pRankPrestiges != null ) {
    			
    			sb.append( pRankPrestiges.getRank().getTag() );
    		}
    	}
    	if ( ladderName == null || ladderName.equalsIgnoreCase( LadderManager.LADDER_DEFAULT ) ) {

    		boolean showFirstRank = false;
    		boolean showNextRank = true;
    		

    		// If at last default rank, then get the default ladder's first rank's tag or just show current tag:
    		if ( pRankDefault.getRank().getRankNext() == null ) {

    			// Since the current default rank is the last, cannot show next default rank:
    			showNextRank = false;
    			
    			Rank firstPrestigeRank = PrisonRanks.getInstance().getPrestigesLadder().getLowestRank().orElse(null);
    			
    			// If firstPrestigeRank is null, then prestiges is not enabled, so cannot show first rank:
    			if ( firstPrestigeRank == null ) {
    				showFirstRank = false;
//    				showNextRank = false;
    			}
    			
    			// If current presetiges is null, then use the first prestiges rank:
    			else if ( pRankPrestiges == null ) {
    				showFirstRank = true;
//    				showNextRank = false;
    				
    			}
    			else if ( pRankPrestiges.getRank().getRankNext() == null ) {
    				// At the last presetiges rank... so do not reset the default ranks:
    				showFirstRank = false;
    			}
    			
    			else {
    				// Presetige is possible, so show first default rank:
    				showFirstRank = true;
    				
    			}
    		
    		}
    		
    		
    		if ( !showFirstRank && !showNextRank ) {
    			// Show current rank:
    			
    			sb.append( pRankDefault.getRank().getTag() );
    		}
    		else if ( showFirstRank ) {
    			
    			Rank firstDefaultRank = PrisonRanks.getInstance().getDefaultLadder().getLowestRank().orElse(null);
    			sb.append( firstDefaultRank.getTag() );
    		}
    		else {
    			// Show next rank:
    			
    			// Not at last default rank, so get next rank tag:
    			sb.append( pRankDefault.getRank().getRankNext().getTag() );
    		}
    		
    	}
    	
    	return sb.toString();
    }
    
    public String getPlayerNextRankTag( RankPlayer rankPlayer, String ladderName ) {
    	StringBuilder sb = new StringBuilder();
    	
//    	boolean hasDefault = false;
//    	boolean hasPrestige = false;
    	
    	if ( !rankPlayer.getLadderRanks().isEmpty()) {
    		
    		RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
    		
    		for ( RankLadder ladder : rankPlayer.getLadderRanks().keySet() ) {
    			
    			if ( ladderName == null ||
    					ladderName != null && ladder.getName().equalsIgnoreCase( ladderName )) {
    				
    				PlayerRank pRank = rankPlayerFactory.getRank( rankPlayer, ladder );
    				Rank rank = pRank.getRank();
    				
    				if ( rank.getLadder() != null && rank.getLadder().isDefault() && 
    						rank.getRankNext() == null ) {
    					PlayerRank prestigeRank = rankPlayer.getPlayerRankPrestiges();
    					
    					if ( prestigeRank == null ) {
    						RankLadder prestigeLadder = PrisonRanks.getInstance()
    														.getLadderManager().getLadderPrestiges();
    						if ( prestigeLadder != null ) {
    							
    							// Player does not have any prestige rank, so use the lowest prestige rank:
    							Rank nextRank = prestigeLadder.getLowestRank().orElseGet( null );
    							sb.append( nextRank.getTag() );
    							continue;
    						}
    					}
    					else {
    						// Get current prestige rank
    						rank = prestigeRank.getRank();
    					}
    					
    				}
    				
  				  	if ( rank != null && rank.getRankNext() != null ) {
  				  		Rank nextRank = rank.getRankNext();
  				  		
//    					if ( sb.length() > 0 ) {
//    						sb.append(", ");
//    					}

  				  		sb.append( nextRank.getTag() );
  				  	}
    			}
    		}
    	}
    	
    	
    	// NOTE: Only for the last rank on the default ladder, use the text value
    	//       from the language file to display in the place of the empty tag.
    	//       The idea is that if prestiges is enabled, then this is a way to 
    	//       indicate the player could prestige as the next step.
    	if ( sb.length() == 0 && LadderManager.LADDER_DEFAULT.equalsIgnoreCase( ladderName ) ) {
    		String replacementText = lastRankMessageForDefaultLadder();
    		if ( replacementText != null && !replacementText.trim().isEmpty() ) {
    			
    			sb.append( replacementText );
    		}
    	}
    	
    	return sb.toString();
    }
    

	private String getPlayerSellallMultiplier( RankPlayer rankPlayer, PlaceholderAttributeNumberFormat attributeNFormat ) {
		String results;
		double multiplier = rankPlayer.getSellAllMultiplier();
		if ( attributeNFormat != null ) {

			results = attributeNFormat.format( multiplier );
		}
		else {
			results = Double.toString( multiplier );
		}
		return results;
	}
	
    
    
    public String getTranslatePlayerPlaceHolder( PlaceholderIdentifier identifier ) {
    	
    	Player player = identifier.getPlayer();
    	
		PlayerManager pm = PrisonRanks.getInstance().getPlayerManager();
		RankPlayer rankPlayer = null;
		
		try {
			rankPlayer = pm.getPlayer( player );
		} 
		catch (Exception e) {
			
			String msg = String.format(
					"PlayerManager: failed to getPlayer(): %s [%s]",
					player == null ? "-null-" : player.getName(), 
					e.getMessage()
					);
			
			Output.get().logError( msg );
		}
    	

		String results = null;
		

		if ( rankPlayer != null ) {
			
			PlaceHolderKey placeHolderKey = identifier.getPlaceholderKey();
			
			
			PlaceholderAttributeBar attributeBar = identifier.getAttributeBar();
			PlaceholderAttributeNumberFormat attributeNFormat = identifier.getAttributeNFormat();
			PlaceholderAttributeText attributeText = identifier.getAttributeText();
			
//			int sequence = identifier.getSequence();
			
			
			
			PrisonPlaceHolders placeHolder = placeHolderKey.getPlaceholder();
			
			String ladderName = placeHolderKey.getData();
			
			if ( rankPlayer != null ) {
				
				identifier.setFoundAMatch( true );
				
				switch ( placeHolder ) {
					case prison_r:
					case prison_rank:
					case prison_r_laddername:
					case prison_rank_laddername:
						results = getPlayerRankName( rankPlayer, ladderName );
						break;
						
					case prison_rn:
					case prison_rank_number:
					case prison_rn_laddername:
					case prison_rank_number_laddername:
						results = getPlayerRankNumber( rankPlayer, ladderName, attributeNFormat );
						break;
						
					case prison_rt:
					case prison_rank_tag:
					case prison_rt_laddername:
					case prison_rank_tag_laddername:
						results = getPlayerRankTag( rankPlayer, ladderName );
						break;
						
					case prison_rank_ladder_position:
					case prison_rlp:
					case prison_rank_ladder_position_laddername:
					case prison_rlp_laddername:
						{
							// rank may be null:
							RankPlayerFactory rankPlayerFactory = new RankPlayerFactory();
							PlayerRank pRank = rankPlayerFactory.getRank( rankPlayer, ladderName );
							if ( pRank != null ) {
								Rank rank = pRank.getRank();
								
								results = rank == null ? "" : Integer.toString( rank.getPosition() );
							}
						}
						break;
						
					case prison_rc:
					case prison_rankup_cost:
					case prison_rc_laddername:
					case prison_rankup_cost_laddername:
						results = getPlayerNextRankCost( rankPlayer, ladderName, false, attributeNFormat );
						break;
						
					case prison_rcf:
					case prison_rankup_cost_formatted:
					case prison_rcf_laddername:
					case prison_rankup_cost_formatted_laddername:
						results = getPlayerNextRankCost( rankPlayer, ladderName, true, attributeNFormat );
						break;
						
					case prison_rcp:
					case prison_rankup_cost_percent:
					case prison_rcp_laddername:
					case prison_rankup_cost_percent_laddername:
						results = getPlayerNextRankCostPercent( rankPlayer, ladderName, attributeNFormat );
						break;
						
					case prison_rcb:
					case prison_rankup_cost_bar:
					case prison_rcb_laddername:
					case prison_rankup_cost_bar_laddername:
						results = getPlayerNextRankCostBar( rankPlayer, ladderName, attributeBar );
						break;
						
						
					case prison_rcr:
					case prison_rankup_cost_remaining:
					case prison_rcr_laddername:
					case prison_rankup_cost_remaining_laddername:
						results = getPlayerNextRankCostRemaining( rankPlayer, ladderName, false, attributeNFormat );
						break;
						
					case prison_rcrf:
					case prison_rankup_cost_remaining_formatted:
					case prison_rcrf_laddername:
					case prison_rankup_cost_remaining_formatted_laddername:
						results = getPlayerNextRankCostRemaining( rankPlayer, ladderName, true, attributeNFormat );
						break;
						
					case prison_rcrp:
					case prison_rankup_cost_remaining_percent:
					case prison_rcrp_laddername:
					case prison_rankup_cost_remaining_percent_laddername:
						results = getPlayerNextRankCostRemainingPercent( rankPlayer, ladderName );
						break;
						
					case prison_rcrb:
					case prison_rankup_cost_remaining_bar:
					case prison_rcrb_laddername:
					case prison_rankup_cost_remaining_bar_laddername:
						results = getPlayerNextRankCostRemainingBar( rankPlayer, ladderName, attributeBar );
						break;
						
						
					case prison_rr:
					case prison_rankup_rank:
					case prison_rr_laddername:
					case prison_rankup_rank_laddername:
						results = getPlayerNextRankName( rankPlayer, ladderName );
						break;
						
					case prison_rrt:
					case prison_rankup_rank_tag:
					case prison_rrt_laddername:
					case prison_rankup_rank_tag_laddername:
						results = getPlayerNextRankTag( rankPlayer, ladderName );
						break;
						
					case prison_rlrt:
					case prison_rankup_linked_rank_tag:
					case prison_rlrt_laddername:
					case prison_rankup_linked_rank_tag_laddername:
						results = getPlayerNextLinkedRankTag( rankPlayer, ladderName );
						break;
						
					case prison_pb:
					case prison_player_balance:
					case prison_pb_laddername:
					case prison_player_balance_laddername:
						results = getPlayerBalance( rankPlayer, ladderName, false, attributeNFormat );
						break;
						
					case prison_pbf:
					case prison_player_balance_formatted:
					case prison_pbf_laddername:
					case prison_player_balance_formatted_laddername:
						results = getPlayerBalance( rankPlayer, ladderName, true, attributeNFormat );
						break;
						
					case prison_pb_epm:
					case prison_player_balance_earnings_per_minute:
						
						results = getPlayerAverageEarningsPerMinute( rankPlayer, ladderName, false, attributeNFormat );
						break;
						
					case prison_pb_epmf:
					case prison_player_balance_earnings_per_minute_formatted:
						
						results = getPlayerAverageEarningsPerMinute( rankPlayer, ladderName, true, attributeNFormat );
						break;
						
						
						
					case prison_ptb:
					case prison_player_token_balance:
						results = getPlayerTokenBalance( rankPlayer, 0, attributeNFormat );
						break;
						
					case prison_ptbf:
					case prison_player_token_balance_formatted:
						results = getPlayerTokenBalance( rankPlayer, 1, attributeNFormat );
						break;

					case prison_ptbfm:
					case prison_player_token_balance_formatted_metric:
						results = getPlayerTokenBalance( rankPlayer, 2, attributeNFormat );
						break;
						
					case prison_ptbfk:
					case prison_player_token_balance_formatted_kmbt:
						results = getPlayerTokenBalance( rankPlayer, 3, attributeNFormat );
						break;
						
					case prison_ptb_epm:
					case prison_player_token_balance_earnings_per_minute:
						
						results = getPlayerTokenAverageEarningsPerMinute( rankPlayer, false, attributeNFormat );
						break;
						
					case prison_ptb_epmf:
					case prison_player_token_balance_earnings_per_minute_formatted:
						
						results = getPlayerTokenAverageEarningsPerMinute( rankPlayer, true, attributeNFormat );
						break;
						
						
						
						
					case prison_psm:
					case prison_player_sellall_multiplier:
						results = getPlayerSellallMultiplier( rankPlayer, attributeNFormat );
						break;
						
						
    				case prison_pbt:
    				case prison_pbtf:
    				case prison_player_blocks_total:
    				case prison_player_blocks_total_formatted:
    					long blocksTotal = PlayerCache.getInstance().getPlayerBlocksTotal( rankPlayer );
    					
    					if ( attributeNFormat != null ) {

    						results = attributeNFormat.format( blocksTotal );
    					}
    					else {
    						if ( placeHolder == PrisonPlaceHolders.prison_pbtf || 
    								placeHolder == PrisonPlaceHolders.prison_player_blocks_total_formatted ) {
    							
    							DecimalFormat iFmt = Prison.get().getDecimalFormatInt();
    							results = iFmt.format( blocksTotal );
    						}
    						else {
    							
    							results = Long.toString( blocksTotal );
    						}
    					}
    					break;
    						
    					
    					
						
					case prison_player_tool_id:
					case prison_ptid:
						{
							
							PlayerUtil pUtil = Prison.get().getPlatform().getPlayerUtil( player );
							results = pUtil.getItemInHandDisplayID();
						}
						break;
						
					case prison_player_tool_name:
					case prison_ptn:
						{
							
							PlayerUtil pUtil = Prison.get().getPlatform().getPlayerUtil( player );
							results = pUtil.getItemInHandDisplayName();
						}
						break;
						
					case prison_player_tool_material_type:
					case prison_ptmt:
						{
							PlayerUtil pUtil = Prison.get().getPlatform().getPlayerUtil( player );
							results = pUtil.getItemInHandItemMaterial();
						}
						break;

					case prison_player_tool_type:
					case prison_ptt:
						{
							PlayerUtil pUtil = Prison.get().getPlatform().getPlayerUtil( player );
							results = pUtil.getItemInHandItemType();
						}
						break;
						
					case prison_player_tool_data:
					case prison_ptdata:
						{
							
							results = "";
						}
						break;
						
					case prison_player_tool_lore:
					case prison_ptlore:
						{
							PlayerUtil pUtil = Prison.get().getPlatform().getPlayerUtil( player );
							results = pUtil.getItemInHandLore();
						}
						break;
						
						
					case prison_player_tool_durability_used:
					case prison_ptdu:
						{
							PlayerUtil pUtil = Prison.get().getPlatform().getPlayerUtil( player );
							results = Integer.toString( pUtil.getItemInHandDurabilityUsed() );
						}
						break;
						
					case prison_player_tool_durability_max:
					case prison_ptdm:
						{
							PlayerUtil pUtil = Prison.get().getPlatform().getPlayerUtil( player );
							results = Integer.toString( pUtil.getItemInHandDurabilityMax() );
						}
						break;

					case prison_player_tool_durability_remaining:
					case prison_ptdr:
						{
							PlayerUtil pUtil = Prison.get().getPlatform().getPlayerUtil( player );
							results = Integer.toString( pUtil.getItemInHandDurabilityRemaining() );
						}
						break;
					case prison_player_tool_durability_percent:
					case prison_ptdp:
						{
							PlayerUtil pUtil = Prison.get().getPlatform().getPlayerUtil( player );
							results = Double.toString( pUtil.getItemInHandDurabilityPercent() );
						}
						break;
					case prison_player_tool_durability_bar:
					case prison_ptdb:
						{
							PlayerUtil pUtil = Prison.get().getPlatform().getPlayerUtil( player );
							
							int max = pUtil.getItemInHandDurabilityMax();
							int used = pUtil.getItemInHandDurabilityUsed();
							
							results = PlaceholderManagerUtils.getInstance().
													getProgressBar( used, max, false, attributeBar );
						}
						break;
						
					case prison_player_tool_enchantment_fortune:
					case prison_ptef:
						{
							PlayerUtil pUtil = Prison.get().getPlatform().getPlayerUtil( player );
							results = Integer.toString( pUtil.getItemInHandEnchantmentFortune() );
						}
						break;
						
					case prison_player_tool_enchantment_efficency:
					case prison_ptee:
						{
							PlayerUtil pUtil = Prison.get().getPlatform().getPlayerUtil( player );
							results = Integer.toString( pUtil.getItemInHandEnchantmentEfficency() );
						}
						break;
						
					case prison_player_tool_enchantment_silktouch:
					case prison_ptes:
						{
							PlayerUtil pUtil = Prison.get().getPlatform().getPlayerUtil( player );
							results = Integer.toString( pUtil.getItemInHandEnchantmentSilkTouch() );
						}
						break;
						
					case prison_player_tool_enchantment_unbreaking:
					case prison_pteu:
						{
							PlayerUtil pUtil = Prison.get().getPlatform().getPlayerUtil( player );
							results = Integer.toString( pUtil.getItemInHandEnchantmentUnbreaking() );
						}
						break;
						
					case prison_player_tool_enchantment_luck:
					case prison_ptel:
						{
							PlayerUtil pUtil = Prison.get().getPlatform().getPlayerUtil( player );
							results = Integer.toString( pUtil.getItemInHandEnchantmentLuck() );
						}
						break;
						
					case prison_player_tool_enchantment_mending:
					case prison_ptem:
						{
							PlayerUtil pUtil = Prison.get().getPlatform().getPlayerUtil( player );
							results = Integer.toString( pUtil.getItemInHandEnchantmentMending() );
						}
						break;
						
					case prison_player_health:
					case prison_ph:
						{
							PlayerUtil pUtil = Prison.get().getPlatform().getPlayerUtil( player );
							results = Double.toString( pUtil.getHealth() );
						}
						break;
						
					case prison_player_health_max:
					case prison_phm:
						{
							PlayerUtil pUtil = Prison.get().getPlatform().getPlayerUtil( player );
							results = Double.toString( pUtil.getMaxHealth() );
						}
						break;
						
					case prison_player_air_max:
					case prison_pam:
						{
							PlayerUtil pUtil = Prison.get().getPlatform().getPlayerUtil( player );
							results = Integer.toString( pUtil.getMaximumAir() );
						}
						break;
						
					case prison_player_air_remaining:
					case prison_par:
						{
							PlayerUtil pUtil = Prison.get().getPlatform().getPlayerUtil( player );
							results = Integer.toString( pUtil.getRemainingAir() );
						}
						break;
						
					case prison_player_food_level:
					case prison_pfl:
						{
							PlayerUtil pUtil = Prison.get().getPlatform().getPlayerUtil( player );
							results = Integer.toString( pUtil.getFoodLevel() );
						}
						break;
						
					case prison_player_food_exhaustion:
					case prison_pfe:
						{
							PlayerUtil pUtil = Prison.get().getPlatform().getPlayerUtil( player );
							results = Double.toString( pUtil.getFoodExhaustion() );
						}
						break;
						
					case prison_player_food_saturation:
					case prison_pfs:
						{
							PlayerUtil pUtil = Prison.get().getPlatform().getPlayerUtil( player );
							results = Double.toString( pUtil.getFoodSaturation() );
						}
						break;
						
					case prison_player_level:
					case prison_pl:
						{
							PlayerUtil pUtil = Prison.get().getPlatform().getPlayerUtil( player );
							results = Integer.toString( pUtil.getLevel() );
						}
						break;
						
					case prison_player_walk_speed:
					case prison_pws:
						{
							PlayerUtil pUtil = Prison.get().getPlatform().getPlayerUtil( player );
							results = Double.toString( pUtil.getWalkSpeed() );
						}
						break;
						
					case prison_player_xp:
					case prison_pxp:
						{
							PlayerUtil pUtil = Prison.get().getPlatform().getPlayerUtil( player );
							results = Double.toString( pUtil.getExp() );
						}
						break;
						
					case prison_player_xp_to_level:
					case prison_pxptl:
						{
							PlayerUtil pUtil = Prison.get().getPlatform().getPlayerUtil( player );
							results = Double.toString( pUtil.getExpToLevel() );
						}
						break;

						
					default:
						identifier.setFoundAMatch( false );
						
						break;
				}
				
				
//				results = applySecondaryPlaceholders( rankPlayer, results );
				
				
				
				if ( attributeText != null && results != null ) {
					
					results = attributeText.format( results );
				}
			}
		}

		identifier.setText(results);
		
		return results;
    }


    /**
     * <p>This function will provide support for secondary placeholders for all player related placeholders.
     * These secondary placeholders are in addition to the preexisting positional placeholders
     * that are hard coded for the specific parameters. 
     * </p>
     * 
     * <p>These secondary placeholders can be inserted anywhere in the message.
     * </p>
     * 
     * <ul>
     * 	<li>{player}</li>
     * 	<li>{rank_default}</li>
     * 	<li>{rank_tag_default}</li>
     * 	<li>{rank_next_default}</li>
     * 	<li>{rank_next_tag_default}</li>
     * 	<li>{rank_prestiges}</li>
     * 	<li>{rank_tag_prestiges}</li>
     * 	<li>{rank_next_prestiges}</li>
     * 	<li>{rank_next_tag_prestiges}</li>
     * 
     *  <li>{player} {rank_default} {rank_tag_default} {rank_next_default} {rank_next_tag_default}</li>
     * 	<li>{rank_prestiges} {rank_tag_prestiges} {rank_next_prestiges} {rank_next_tag_prestiges}</li>
     * </ul>
     * 
     * @param rankPlayer
     * @param results
     * @return
     */
	private String applySecondaryPlaceholders(RankPlayer rankPlayer, String results) {

		results = applySecondaryPlaceholdersCheck( "{player}", rankPlayer.getName(), results );
		
		if ( rankPlayer.getPlayerRankDefault() != null ) {
			PlayerRank rankP = rankPlayer.getPlayerRankDefault();
			Rank rank = rankP == null ? null : rankP.getRank();
			Rank rankNext = rank == null ? null : rank.getRankNext();
			
			results = applySecondaryPlaceholdersCheck( "{rank_default}", 
					rank == null ? "" : rank.getName(), results );
			results = applySecondaryPlaceholdersCheck( "{rank_tag_default}", 
					rank == null ? "" : rank.getTag(), results );
			
			results = applySecondaryPlaceholdersCheck( "{rank_next_default}", 
					rankNext == null ? "" : rankNext.getName(), results );
			results = applySecondaryPlaceholdersCheck( "{rank_next_tag_default}", 
					rankNext == null ? "" : rankNext.getTag(), results );
		}
		
		if ( rankPlayer.getPlayerRankPrestiges() != null ) {
			
			PlayerRank rankP = rankPlayer.getPlayerRankPrestiges();
			Rank rank = rankP == null ? null : rankP.getRank();
			Rank rankNext = rank == null ? null : rank.getRankNext();
			
			results = applySecondaryPlaceholdersCheck( "{rank_prestiges}", 
					rank == null ? "" : rank.getName(), results );
			results = applySecondaryPlaceholdersCheck( "{rank_tag_prestiges}", 
					rank == null ? "" : rank.getTag(), results );
			
			results = applySecondaryPlaceholdersCheck( "{rank_next_prestiges}", 
					rankNext == null ? "" : rankNext.getName(), results );
			results = applySecondaryPlaceholdersCheck( "{rank_next_tag_prestiges}", 
					rankNext == null ? "" : rankNext.getTag(), results );
		}
		
		return results;
	}

	/**
	 * <p>Ths function will perform individual replacements of the given placeholders, but
	 * if the placeholder does not exist, then it will not change anything with the results
	 * and it will be just passed through.
	 * </p>
	 * 
	 * @param placeholder
	 * @param value
	 * @param results
	 * @return
	 */
	private String applySecondaryPlaceholdersCheck( String placeholder, String value, String results) {

		if ( results.contains( placeholder ) && value != null ) {
			results = results.replace( placeholder, value );
		}
		
		return results;
	}

	@Override
    public List<PlaceHolderKey> getTranslatedPlaceHolderKeys() {
    	if ( translatedPlaceHolderKeys == null ) {
    		translatedPlaceHolderKeys = new ArrayList<>();
    		
    		// This generates all of the placeholders for the player ranks:
    		List<PrisonPlaceHolders> placeHolders = PrisonPlaceHolders.getTypes( PlaceholderFlags.PLAYER );
    		for ( PrisonPlaceHolders ph : placeHolders ) {
    			PlaceHolderKey placeholder = new PlaceHolderKey(ph.name(), ph );
    			if ( ph.getAlias() != null ) {
    				String aliasName = ph.getAlias().name();
    				placeholder.setAliasName( aliasName );
    			}
    			
    			translatedPlaceHolderKeys.add( placeholder );
    			
    			// Getting too many placeholders... add back the extended prefix when looking up:
    			
//    			// Now generate a new key based upon the first key, but without the prison_ prefix:
//    			String key2 = ph.name().replace( 
//    					IntegrationManager.PRISON_PLACEHOLDER_PREFIX_EXTENDED, "" );
//    			PlaceHolderKey placeholder2 = new PlaceHolderKey(key2, ph, false );
//    			translatedPlaceHolderKeys.add( placeholder2 );
    		}
    		
    		
    		// This generates all of the placeholders for the ladders:
    		placeHolders = PrisonPlaceHolders.getTypes( PlaceholderFlags.LADDERS );
    		
    		List<RankLadder> ladders = PrisonRanks.getInstance().getLadderManager().getLadders();
    		for ( RankLadder ladder : ladders ) {
    			for ( PrisonPlaceHolders ph : placeHolders ) {
    				
    				if ( ph.hasFlag( PlaceholderFlags.ONLY_DEFAULT_OR_PRESTIGES ) && 
    						!ladder.getName().equalsIgnoreCase( LadderManager.LADDER_DEFAULT ) &&
    						!ladder.getName().equalsIgnoreCase( LadderManager.LADDER_PRESTIGES )
    						) {
    					// Placeholder is invalid for ladders that are not default or prestiges, so skip:
    					continue;
    				}
    				
    				String key = ph.name().replace( 
    						PlaceholderManager.PRISON_PLACEHOLDER_LADDERNAME_SUFFIX, "_" + ladder.getName() ).
    							toLowerCase();
    				
    				PlaceHolderKey placeholder = new PlaceHolderKey(key, ph, ladder.getName() );
    				if ( ph.getAlias() != null ) {
    					String aliasName = ph.getAlias().name().replace( 
    							PlaceholderManager.PRISON_PLACEHOLDER_LADDERNAME_SUFFIX, "_" + ladder.getName() ).
    								toLowerCase();
    					placeholder.setAliasName( aliasName );
    				}
    				translatedPlaceHolderKeys.add( placeholder );
    				
    				// Getting too many placeholders... add back the extended prefix when looking up:

//    				// Now generate a new key based upon the first key, but without the prison_ prefix:
//    				String key2 = key.replace( 
//    						IntegrationManager.PRISON_PLACEHOLDER_PREFIX_EXTENDED, "" );
//    				PlaceHolderKey placeholder2 = new PlaceHolderKey(key2, ph, ladder.name, false );
//    				translatedPlaceHolderKeys.add( placeholder2 );
    				
    			}
    			
    		}
    		
    	}
    	
    	return translatedPlaceHolderKeys;
    }
    
    @Override
    public void reloadPlaceholders() {
    	
    	// clear the class variable so they will regenerate:
    	translatedPlaceHolderKeys = null;
    	
    	// Regenerate the translated placeholders:
    	getTranslatedPlaceHolderKeys();
    }


}
