package tech.mcprison.prison.ranks.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import com.google.gson.internal.LinkedTreeMap;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.cache.PlayerCachePlayerData;
import tech.mcprison.prison.file.JsonFileIO;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.FirstJoinHandlerMessages;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.commands.RankUpCommand;
import tech.mcprison.prison.ranks.events.FirstJoinEvent;
import tech.mcprison.prison.ranks.managers.LadderManager;
import tech.mcprison.prison.store.Document;
import tech.mcprison.prison.util.ConversionUtil;

public class RankPlayerFactory
{
	JsonFileIO jfIO = new JsonFileIO();
	
	   @SuppressWarnings( "unchecked" )
		public RankPlayer createRankPlayer(Document document) {
		   RankPlayer rankPlayer = null;
		   
	    	
		   String uuidStr = document.containsKey("uid") ?
				   				(String) document.get("uid") :
				   					document.containsKey("uuid") ?
				   						(String) document.get("uuid") :
				   							null;
		   
	        UUID uuid = UUID.fromString( uuidStr );
	        
	        rankPlayer = new RankPlayer( uuid );
	        
	        LinkedTreeMap<String, Object> ranksLocal =
	            (LinkedTreeMap<String, Object>) document.get("ranks");
//	        LinkedTreeMap<String, Object> prestigeLocal =
//	            (LinkedTreeMap<String, Object>) document.get("prestige");
	        
//	        LinkedTreeMap<String, Object> blocksMinedLocal =
//	        		(LinkedTreeMap<String, Object>) document.get("blocksMined");
	        
	        

	        for (String key : ranksLocal.keySet()) {
	        	
	        	Object rankObj = ranksLocal.get(key);
	        	
	        	if ( rankObj instanceof Double ) {
	        		int rankId = ConversionUtil.doubleToInt( rankObj );
	        		rankPlayer.getRanksRefs().put(key, rankId );
	        	}
	        	else {
	        		// It's a json object:
	        		// We only need to get the rankId from the json String, but the other fields are there
	        		// for human readability.
	        		String json = rankObj.toString();
	        		RankPlayerFactoryDataRank rData = jfIO.fromString( json, RankPlayerFactoryDataRank.class );
	        		if ( rData != null ) {
	        			rankPlayer.getRanksRefs().put( rData.getLadderName(), rData.getRankId() );
	        		}
	        	}
	        	
	        }
	        
	        
	        // Sets up the Ladder and Rank objects:
	        setupLadderRanks( rankPlayer );
	        
	        
	        
	        
//	        for (String key : prestigeLocal.keySet()) {
//	            prestige.put(key, RankUtil.doubleToInt(prestigeLocal.get(key)));
//	        }
	        
////	        rankPlayer.setBlocksMined( new HashMap<>() );
//	        if ( blocksMinedLocal != null ) {
//	        	for (String key : blocksMinedLocal.keySet()) {
//	        		rankPlayer.getBlocksMined().put(key, ConversionUtil.doubleToInt(blocksMinedLocal.get(key)));
//	        	}
//	        }

	        
	        Object namesListObject = document.get( "names" );
	        
	        if ( namesListObject != null ) {
	        	
	        	for ( Object rankPlayerNameMap : (ArrayList<Object>) namesListObject ) {
	        		LinkedTreeMap<String, Object> rpnMap = (LinkedTreeMap<String, Object>) rankPlayerNameMap;
	        		
	        		if ( rpnMap.size() > 0 ) {
	        			String name = (String) rpnMap.get( "name" );
	        			long date = ConversionUtil.doubleToLong( rpnMap.get( "date" ) );
	        			
	        			RankPlayerName rankPlayerName = new RankPlayerName( name, date );
	        			rankPlayer.getNames().add( rankPlayerName );
//	        			Output.get().logInfo( "RankPlayer: uuid: " + uid + " RankPlayerName: " + rankPlayerName.toString() );
	        		}
	        		
	        	}
	        }
	        
	        
	        
	        if ( document.get("balance") != null ) {
	        	
	        	rankPlayer.setCurrentBalanceTemp( (Double) document.get("balance") );
	        }
	        
	        if ( document.get("totalBlocks") != null ) {
	     	        	
	        	rankPlayer.setTotalBlocksTemp( getLong( rankPlayer.getName(), "totalBlocks", document ) );
	        }
	        
	        if ( document.get("totalTokens") != null ) {
	        	
	        	rankPlayer.setTotalTokensTemp( getLong( rankPlayer.getName(), "totalTokens", document ) );
	        }
	        
	        if ( document.get("lastSeenDate") != null ) {
	        	
	        	rankPlayer.setLastSeenDateTemp( getLong( rankPlayer.getName(), "lastSeenDate", document ) );
	        }
	        
	        
	        if ( document.get("lastSaved") != null ) {
	        	rankPlayer.setLastSaved( getLong( rankPlayer.getName(), "lastSaved", document )  );
	        }
	        
	        
	        if ( document.get("lastRefreshed") != null ) {
	        	rankPlayer.setLastRefreshed( getLong( rankPlayer.getName(), "lastRefreshed", document )  );
	        }
	        
	        

	     
	        
	        // The new field permsSnapShot will be a list of all of the player's perms, captured 
	        // at major moments when they are online.  Since perms are not available offline, 
	        // this will provide a "rough" listing of what they maybe.  Its beyond the scope of 
	        // this snap shot to ensure these perms are "current"; if they change after this 
	        // image is taken, then that's not our problem.
	        if ( document.get("permsSnapShot") != null ) {
	        	List<String> perms = (List<String>) document.get("permsSnapShot");
	        	rankPlayer.setPermsSnapShot( perms );
	        }


	        if ( document.get("sellallMultiplier") != null ) {
	        	double multValue = (Double) document.get("sellallMultiplier");
	        	rankPlayer.setSellallMultiplierValue( multValue );
	        }
	        
	        if ( document.get("sellallMultipliers") != null ) {
	        	List<String> mults = (List<String>) document.get("sellallMultipliers");
	        	rankPlayer.setSellallMultipliers( mults );
	        }
	        
	        
	        return rankPlayer;
	    }

	   private long getLong( String playerName, String field, Document document ) {
		   	long value = 0L;
        	
		   	Object obj = document.get(field);
		   	if ( obj == null ) {
		   	}
        	if ( obj instanceof Long ) {
        		value = (Long) obj;
        	}
        	else if ( obj instanceof Double ) {
        		Double duble = (Double) obj;
        		value = duble.longValue();
        	}
        	
        	return value;
	   }
	    public static Document toDocument( RankPlayer rankPlayer ) {
	    	
	    	// Update some stats from the playerCache:
	    	PlayerCachePlayerData cacheData = rankPlayer.getPlayerCache().getOnlinePlayerCached(rankPlayer);
	    	rankPlayer.updateTotalLastValues( cacheData , false);
	    	
	        Document ret = new Document();
	        ret.put("uid", rankPlayer.getUUID());
	        
	        
	        
	        Set<RankLadder> ladders = rankPlayer.getLadderRanks().keySet();
	        TreeMap<String,Object> playerRanks = new TreeMap<>();
	        for (RankLadder ladder : ladders) {
	        	PlayerRank rank = rankPlayer.getLadderRanks().get(ladder);
	        	
	        	RankPlayerFactoryDataRank rData = new RankPlayerFactoryDataRank( 
	        			rank.getRank().getName(), ladder.getName(), rank.getRank().getId() );
//	        	String json = jfIO.toString( rData );
	        	
	        	playerRanks.put( rData.getLadderName(), rData.getJsonObject() );

	        	// The key must be ladder name:
//	        	playerRanks.put( rData.getRankName(), rData.getJsonObject() );
			}
	        
	        ret.put("ranks", playerRanks );
//	        ret.put("ranks", rankPlayer.getRanksRefs() );

	        
//	        ret.put("prestige", this.prestige);
	        
	        ret.put("names", rankPlayer.getNames());
	        
	        
	        ret.put("balance", Double.valueOf( rankPlayer.getCurrentBalanceTemp() ));
	        ret.put("totalBlocks", Long.valueOf( rankPlayer.getTotalBlocksTemp() ));
	        ret.put("totalTokens", Long.valueOf( rankPlayer.getTotalTokensTemp() ));
	        ret.put("lastSeenDate", Long.valueOf( rankPlayer.getLastSeenDateTemp() ));
	        
	        
	        
	        Player sPlayer = rankPlayer.getPlatformPlayer();
	        
	        
//	        // create a timestamp:
//	        ret.put( "lastSeenDate", Long.valueOf( rankPlayer.getLastSeenDate() ));
	        
	        // create a timestamp:
	        ret.put( "lastSaved", Long.valueOf( System.currentTimeMillis() ));

	        
	        if ( sPlayer != null && sPlayer.isOnline() ) {
	        	ret.put( "lastRefreshed", Long.valueOf( System.currentTimeMillis() ));
	        }
	        else {
	        	ret.put( "lastRefreshed", rankPlayer.getLastRefreshed() );
	        }
	        

	        // The new field permsSnapShot will be a list of all of the player's perms, captured 
	        // at major moments when they are online.  Since perms are not available offline, 
	        // this will provide a "rough" listing of what they maybe.  Its beyond the scope of 
	        // this snap shot to ensure these perms are "current"; if they change after this 
	        // image is taken, then that's not our problem.
	        List<String> perms = new ArrayList<>();
	        
	        
	        if ( sPlayer != null && sPlayer.isOnline() ) {
	        	// If the player is online, get a fresh list of perms:
	        	perms = sPlayer.getPermissions();
	        	rankPlayer.setPermsSnapShot(perms);
	        }
	        else {
	        	// Otherwise since the player if offline, then save whatever permsSnapShot is already available:
	        	perms = rankPlayer.getPermsSnapShot();
	        }
	        ret.put( "permsSnapShot", perms );
	        
	        
	        double multValue = 1d;
	        if ( sPlayer != null && sPlayer.isOnline() ) {
	        	multValue = sPlayer.getSellAllMultiplier();
	        }
	        else {
	        	// Otherwise since offline, save the existing list of multipliers:
	        	multValue = rankPlayer.getSellallMultiplierValue();
	        }
	        ret.put( "sellallMultiplier", multValue );

	        List<String> multipliers = new ArrayList<>();
	        if ( sPlayer != null && sPlayer.isOnline() ) {
	        	multipliers = sPlayer.getSellAllMultiplierListings();
	        }
	        else {
	        	// Otherwise since offline, save the existing list of multipliers:
	        	multipliers = rankPlayer.getSellallMultipliers();
	        }
	        ret.put("sellallMultipliers", multipliers);
	        
	        

//	        ret.put("blocksMined", rankPlayer.getBlocksMined() );
	        return ret;
	    }



    /**
     * <p>This function will check to see if the player is on the default rank on 
     * the default ladder.  If not, then it will add them.  
     * </p>
     * 
     * <p>This is safe to run on anyone, even if they already are on the default ladder 
     * since it will skip processing for them.
     * </p>
     * 
     * <p>Note, this will save the player's new rank.
     * </p>
     */
    public void firstJoin( RankPlayer rankPlayer) {
    	
    	RankLadder defaultLadder = PrisonRanks.getInstance().getDefaultLadder();
    	
    	if ( defaultLadder == null ) {
    		
    		Output.get().logError( "RankPlayerFactory.firstJoin: No default ladder!!" );
    		
    	}
    	else if ( !rankPlayer.getLadderRanks().containsKey( defaultLadder ) ) {
    		
    		Optional<Rank> firstRank = defaultLadder.getLowestRank();
    		
    		if ( firstRank.isPresent() ) {
    			Rank defaultRank = firstRank.get();
    			
    			
    			RankUpCommand rankupCommands = PrisonRanks.getInstance().getRankManager().getRankupCommands();
    			
    			rankupCommands.setPlayerRankFirstJoin( rankPlayer, defaultRank );
    			rankPlayer.setDirty( true );
    			
    			
    			// Saves the new player's rank:
    			PrisonRanks.getInstance().getPlayerManager().savePlayer(rankPlayer);
    			
//    			rankPlayer.addRank( defaultRank );
    			
    			Prison.get().getEventBus().post(new FirstJoinEvent( rankPlayer ));
    			
    			FirstJoinHandlerMessages messages = new FirstJoinHandlerMessages();
    			Output.get().logWarn( messages.firstJoinSuccess( rankPlayer.getName() ) );
    			
    		} else {
    			
    			FirstJoinHandlerMessages messages = new FirstJoinHandlerMessages();
    			Output.get().logWarn( messages.firstJoinWarningNoRanksOnServer() );
    		}
    	}
    	
    }
    
    
    /**
     * Removes a ladder from this player, including whichever rank this player had in it.
     * Cannot remove the default ladder.
     *
     * @param ladderName The ladder's name.
     */
    public boolean removeLadder( RankPlayer rankPlayer, String ladderName ) {
    	boolean results = false;
        if ( !ladderName.equalsIgnoreCase(LadderManager.LADDER_DEFAULT) ) {
        	Integer id = rankPlayer.getRanksRefs().remove(ladderName);
        	results = (id != null);
        	
        	RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder( ladderName );
        	if ( ladder != null && !ladder.getName().equalsIgnoreCase( LadderManager.LADDER_DEFAULT ) ) {
        		rankPlayer.getLadderRanks().remove( ladder );
        	}
        }
        
        return results;
    }

    
    /**
     * <p>If the player does not have a rank on the ladder, and force is enabled,
     * then return the first rank on the ladder.
     * </p>
     * 
     * @param rankPlayer
     * @param ladder
     * @param force
     * @return
     */
    public PlayerRank getRank( RankPlayer rankPlayer, RankLadder ladder, boolean force ) {
    	PlayerRank results = getRank( rankPlayer, ladder );
    	
    	if ( force && results == null ) {
    		Rank tempRank = ladder.getLowestRank().get();

    		if ( tempRank != null ) {
    			results = new PlayerRank( tempRank );
    			
    			// force cost calculations with a zero multiplier:
    			results.applyMultiplier( 0 );
    		}
    	}
    	
    	return results;
    }
    
    /**
     * Retrieves the rank that this player has in a certain ladder, if any.
     *
     * @param ladder The ladder to check.
     * @return An optional containing the {@link Rank} if found, or empty if there isn't a rank by that ladder for this player.
     */
    public PlayerRank getRank( RankPlayer rankPlayer, RankLadder ladder ) {
    	PlayerRank results = null;
    	
    	if ( ladder != null ) {
    		
    		Set<RankLadder> keys = rankPlayer.getLadderRanks().keySet();
    		for ( RankLadder key : keys )
    		{
    			if ( key != null && key.getName().equalsIgnoreCase( ladder.getName() ) ) {
    				results = rankPlayer.getLadderRanks().get( key );
    			}
    		}
    	}

    	return results;
    	
//        if (!ranksRefs.containsKey(ladder.getName())) {
//            return null;
//        }
//        int id = ranksRefs.get(ladder.getName());
//        return PrisonRanks.getInstance().getRankManager().getRank(id);
    }
    
    
//	/**
//     * Returns all ladders this player is a part of, along with each rank the player has in that ladder.
//     *
//     * @return The map containing this data.
//     */
//    public Map<RankLadder, PlayerRank> getLadderRanksx( RankPlayer rankPlayer ) {
//    	
//    	if ( rankPlayer.getLadderRanks().isEmpty() && !rankPlayer.getRanksRefs().isEmpty() ) {
//    		
//    		//Map<RankLadder, Rank> ret = new HashMap<>();
//    		
//    		for (Map.Entry<String, Integer> entry : rankPlayer.getRanksRefs().entrySet()) {
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
//    			rankPlayer.getLadderRanks().put(ladder, pRank);
//    		}
//    		
//    		// Need to recalculate all rank multipliers:
//    		rankPlayer.recalculateRankMultipliers();
//    	}
//
//        return rankPlayer.getLadderRanks();
//    }

    
    /**
     * <p>This function is used when setting up a RankPlayer after loading from the 
     * file system.  This takes the magic numbers that are used for ranks/ladders and
     * finds the correct matches, which results in actual Rank objects.  These ranks are
     * then saved within the RankPlayer object for each player.
     * </p>
     * 
     * @param rankPlayer
     */
    public void setupLadderRanks( RankPlayer rankPlayer ) {
    	
    	if ( rankPlayer.getLadderRanks().isEmpty() && !rankPlayer.getRanksRefs().isEmpty() ) {
    		
    		//Map<RankLadder, Rank> ret = new HashMap<>();
    		
    		for (Map.Entry<String, Integer> entry : rankPlayer.getRanksRefs().entrySet()) {
    			String ladderName = entry.getKey();
    			int rankId = entry.getValue();
    			
    			RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder( ladderName );
    			
    			if ( ladder != null ) {
    				
    	        	for ( Rank rank : ladder.getRanks() ) {
    	        		if ( rank.getId() == rankId ) {
    	        			
    	        			PlayerRank pRank = rankPlayer.calculateTargetPlayerRank( rank );
//    	        			PlayerRank pRank = createPlayerRank( rank );
    	        			rankPlayer.getLadderRanks().put( ladder, pRank );
    	        			
    	        			break;
    	        		}
    				}
    			}
    		}
    		
    		// Need to recalculate all rank multipliers: This may be redundant.
    		rankPlayer.recalculateRankMultipliers();
    	}

    }

    
    
    /**
     * Retrieves the rank that this player has the specified ladder.
     *
     * @param ladder The ladder name to check.
     * @return The {@link Rank} if found, otherwise null;
     */
    public PlayerRank getRank( RankPlayer rankPlayer, String ladderName ) {
    	
    	RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder( ladderName );
    	return getRank( rankPlayer, ladder );
    	
//    	Rank results = null;
//    	if (ladder != null && ranksRefs.containsKey(ladder)) {
//    		int id = ranksRefs.get(ladder);
//    		results = PrisonRanks.getInstance().getRankManager().getRank(id);
//    	}
//    	return results;
    }
    
    
    /**
     * <p>This function will create a PlayerRank without a player.  This is to be 
     * used only with caution and where a player cannot be created, such as 
     * a command to list generic PlayerRanks.
     * </p>
     * 
     * <p>This is only used in one location: 
     * tech.mcprison.prison.ranks.commands.RanksCommands.rankInfoDetails(CommandSender, Rank, String)
     * </p>
     * 
     * @param rank
     * @return
     */
    public PlayerRank createPlayerRank( Rank rank ) {
    	PlayerRank results = new PlayerRank( rank );
    	
		double rankMultiplier = results.getLadderBasedRankMultiplier( rank );
		
		results.setRankCost( rankMultiplier );
		
    	return results;
    }
    
//    private PlayerRank createPlayerRank( Rank rank, double rankMultiplier ) {
//    	PlayerRank results = new PlayerRank( rank, rankMultiplier );
//    	
//    	return results;
//    }
    
	public PlayerRank getTargetPlayerRankForPlayer( PlayerRank playerRank, 
						RankPlayer player, Rank targetRank ) {
		PlayerRank targetPlayerRank = player.calculateTargetPlayerRank( targetRank );
		
//		if ( targetRank != null ) {
//			
//			double targetRankMultiplier = playerRank.getLadderBasedRankMultiplier( targetRank );
//			
//			PlayerRank pRankForPLayer = getRank( player, targetRank.getLadder() );
//			double existingRankMultiplier = pRankForPLayer == null ? 0 : 
//							playerRank.getLadderBasedRankMultiplier( pRankForPLayer.getRank() );
//			
//			// Get the player's total rankMultiplier from the default ladder 
//			// because they will always have a rank there:
//			PlayerRank pRank = getRank( player, LadderManager.LADDER_DEFAULT );
//			double playerMultipler = pRank == null ? 0 : pRank.getRankMultiplier();
//			
//			// So the actual rank multiplier that needs to be used, is based upon the 
//			// Player's current multiplier PLUS the multiplier for the target rank 
//			// AND MINUS the multiplier for the current rank the player has within the 
//			// target rank's ladder.
//			double rankMultiplier = playerMultipler + targetRankMultiplier - existingRankMultiplier;
//			
//			targetPlayerRank = createPlayerRank( targetRank, rankMultiplier );
//		}
		
		return targetPlayerRank;
	}
    
	
	public double getRawRankCost( Rank rank ) {
		return rank.getCost();
	}
	public void setRawRankCost( Rank rank, double rawCost ) {
		rank.setCost( rawCost );
	}
	

}
