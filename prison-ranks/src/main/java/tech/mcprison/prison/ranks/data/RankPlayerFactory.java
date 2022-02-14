package tech.mcprison.prison.ranks.data;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.google.gson.internal.LinkedTreeMap;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.FirstJoinHandlerMessages;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.events.FirstJoinEvent;
import tech.mcprison.prison.store.Document;
import tech.mcprison.prison.util.ConversionUtil;

public class RankPlayerFactory
{
	
	   @SuppressWarnings( "unchecked" )
		public RankPlayer createRankPlayer(Document document) {
		   RankPlayer rankPlayer = null;
		   
	    	
	        UUID uuid = UUID.fromString((String) document.get("uid"));
	        
	        rankPlayer = new RankPlayer( uuid );
	        
	        LinkedTreeMap<String, Object> ranksLocal =
	            (LinkedTreeMap<String, Object>) document.get("ranks");
//	        LinkedTreeMap<String, Object> prestigeLocal =
//	            (LinkedTreeMap<String, Object>) document.get("prestige");
	        
//	        LinkedTreeMap<String, Object> blocksMinedLocal =
//	        		(LinkedTreeMap<String, Object>) document.get("blocksMined");
	        
	        Object namesListObject = document.get( "names" );
	        

	        for (String key : ranksLocal.keySet()) {
	        	
	        	int rankId = ConversionUtil.doubleToInt(ranksLocal.get(key));
	        	rankPlayer.getRanksRefs().put(key, rankId );
	        	
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
	        
	        return rankPlayer;
	    }

	    public Document toDocument( RankPlayer rankPlayer ) {
	        Document ret = new Document();
	        ret.put("uid", rankPlayer.getUUID());
	        ret.put("ranks", rankPlayer.getRanksRefs() );
//	        ret.put("prestige", this.prestige);
	        
	        ret.put("names", rankPlayer.getNames());

//	        ret.put("blocksMined", rankPlayer.getBlocksMined() );
	        return ret;
	    }



    /**
     * <p>This function will check to see if the player is on the default rank on 
     * the default ladder.  If not, then it will add them.  
     * </p>
     * 
     * <p>This is safe to run on anyone, even if they already are on the default ladder.
     * </p>
     * 
     * <p>Note, this will not save the player's new rank.  The save function must be
     * managed and called outside of this.
     * </p>
     */
    public void firstJoin( RankPlayer rankPlayer) {
    	
    	RankLadder defaultLadder = PrisonRanks.getInstance().getDefaultLadder();
    	
    	if ( !rankPlayer.getLadderRanks().containsKey( defaultLadder ) ) {
    		
    		Optional<Rank> firstRank = defaultLadder.getLowestRank();
    		
    		if ( firstRank.isPresent() ) {
    			Rank rank = firstRank.get();
    			
    			rankPlayer.addRank( rank );
    			
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
        if ( !ladderName.equalsIgnoreCase("default") ) {
        	Integer id = rankPlayer.getRanksRefs().remove(ladderName);
        	results = (id != null);
        	
        	RankLadder ladder = PrisonRanks.getInstance().getLadderManager().getLadder( ladderName );
        	if ( ladder != null && !ladder.getName().equalsIgnoreCase( "default" ) ) {
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
    	        			
    	        			PlayerRank pRank = createPlayerRank( rank );
    	        			rankPlayer.getLadderRanks().put( ladder, pRank );
    	        			
    	        			break;
    	        		}
    				}
    			}
    		}
    		
    		// Need to recalculate all rank multipliers:
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
    
    
    public PlayerRank createPlayerRank( Rank rank ) {
    	PlayerRank results = new PlayerRank( rank );
    	
		double rankMultiplier = results.getLadderBasedRankMultiplier( rank );
		
		results.setRankCost( rankMultiplier );
		
    	return results;
    }
    
    private PlayerRank createPlayerRank( Rank rank, double rankMultiplier ) {
    	PlayerRank results = new PlayerRank( rank, rankMultiplier );
    	
    	return results;
    }
    
	public PlayerRank getTargetPlayerRankForPlayer( PlayerRank playerRank, 
						RankPlayer player, Rank targetRank ) {
		PlayerRank targetPlayerRank = null;
		
		if ( targetRank != null ) {
			
			double targetRankMultiplier = playerRank.getLadderBasedRankMultiplier( targetRank );
			
			PlayerRank pRankForPLayer = getRank( player, targetRank.getLadder() );
			double existingRankMultiplier = pRankForPLayer == null ? 0 : 
							playerRank.getLadderBasedRankMultiplier( pRankForPLayer.getRank() );
			
			// Get the player's total rankMultiplier from the default ladder 
			// because they will always have a rank there:
			PlayerRank pRank = getRank( player, "default" );
			double playerMultipler = pRank == null ? 0 : pRank.getRankMultiplier();
			
			// So the actual rank multiplier that needs to be used, is based upon the 
			// Player's current multiplier PLUS the multiplier for the target rank 
			// AND MINUS the multiplier for the current rank the player has within the 
			// target rank's ladder.
			double rankMultiplier = playerMultipler + targetRankMultiplier - existingRankMultiplier;
			
			targetPlayerRank = createPlayerRank( targetRank, rankMultiplier );
		}
		
		return targetPlayerRank;
	}
    
	
	public double getRawRankCost( Rank rank ) {
		return rank.getCost();
	}
	public void setRawRankCost( Rank rank, double rawCost ) {
		rank.setCost( rawCost );
	}
	

}
