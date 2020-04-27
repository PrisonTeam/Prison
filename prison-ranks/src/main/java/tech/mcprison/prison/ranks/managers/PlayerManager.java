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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.google.common.eventbus.Subscribe;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.integration.EconomyCurrencyIntegration;
import tech.mcprison.prison.integration.EconomyIntegration;
import tech.mcprison.prison.integration.IntegrationManager;
import tech.mcprison.prison.integration.IntegrationManager.PlaceHolderFlags;
import tech.mcprison.prison.integration.IntegrationManager.PrisonPlaceHolders;
import tech.mcprison.prison.integration.IntegrationType;
import tech.mcprison.prison.integration.ManagerPlaceholders;
import tech.mcprison.prison.integration.PlaceHolderKey;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.events.player.PlayerJoinEvent;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.RankUtil;
import tech.mcprison.prison.ranks.RankupResults;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.events.FirstJoinEvent;
import tech.mcprison.prison.store.Collection;
import tech.mcprison.prison.store.Document;

/**
 * Manages all the players in the records.
 *
 * @author Faizaan A. Datoo
 */
public class PlayerManager
	implements ManagerPlaceholders {

    /*
     * Fields & Constants
     */

    private Collection collection;
    private List<RankPlayer> players;

    /*
     * Constructor
     */

    public PlayerManager(Collection collection) {
        this.collection = collection;
        this.players = new ArrayList<>();

        Prison.get().getEventBus().register(this);
    }

    /*
     * Methods
     */

    /**
     * Loads a player from a file and stores it in the registry for use on the server.
     *
     * @param playerFile The key that the player data is stored as. Case-sensitive.
     * @throws IOException If the file could not be read, or if the file does not exist.
     */
    public void loadPlayer(String playerFile) throws IOException {
        Document document = collection.get(playerFile).orElseThrow(IOException::new);
        players.add(new RankPlayer(document));
    }

    /**
     * Loads every player in the specified playerFolder.
     *
     * @throws IOException If one of the files could not be read, or if the playerFolder does not exist.
     */
    public void loadPlayers() throws IOException {
        List<Document> players = collection.getAll();
        players.forEach(document -> this.players.add(new RankPlayer(document)));
    }

    /**
     * Saves a {@link RankPlayer} to disk.
     *
     * @param player     The {@link RankPlayer} to save.
     * @param playerFile The key to save as.
     * @throws IOException If the file could not be created or written to.
     * @see #savePlayer(RankPlayer) To save with the default conventional filename.
     */
    public void savePlayer(RankPlayer player, String playerFile) throws IOException {
        collection.save(playerFile, player.toDocument());
//        collection.insert(playerFile, player.toDocument());
    }

    public void savePlayer(RankPlayer player) throws IOException {
        this.savePlayer(player, player.filename());
    }

    /**
     * Saves every player in the registry.  If one player fails to save, it will not
     * prevent the others from being saved.
     *
     * @throws IOException If one of the players could not be saved.
     * @see #savePlayer(RankPlayer, String)
     */
    public void savePlayers() throws IOException {
        for (RankPlayer player : players) {
        	
        	// Catch exceptions if a failed save so other players can be saved:
            try {
				savePlayer(player);
			}
			catch ( Exception e )
			{
				String message = "An error occurred while saving the player files: "  +
						player.filename();
				Output.get().logError(message, e);
			}
        }
    }

    /*
     * Getters & Setters
     */

    public List<RankPlayer> getPlayers() {
        return players;
    }

    /** 
     * <p>Get the player, if they don't exist, add them.
     * </p>
     * 
     * @param uid
     * @return
     */
    public Optional<RankPlayer> getPlayer(UUID uid) {
    	Optional<RankPlayer> results = players.stream().filter(player -> player.uid.equals(uid)).findFirst();
    	
    	if ( !results.isPresent() ) {
    		results = Optional.ofNullable( addPlayer(uid, null) );
    	}
    	
    	return results;
    }
    
    
    private RankPlayer addPlayer( UUID uid, String playerName ) {
    	// We need to create a new player data file.
        RankPlayer newPlayer = new RankPlayer();
        newPlayer.uid = uid;
        newPlayer.ranks = new HashMap<>();
        newPlayer.prestige = new HashMap<>();

        players.add(newPlayer);

        try {
            savePlayer(newPlayer);

            // Assign the player to the default rank:
            String ladder = null; // will set to the "default" ladder
            String rank = null;   // will set to the "default" rank
            
            // Set the rank to the default ladder and the default rank.  The results are logged
            // before the results are returned, so can ignore the results:
            @SuppressWarnings( "unused" )
            RankupResults results = new RankUtil().setRank(newPlayer, ladder, rank, 
            							playerName, "FirstJoinEvent");
            
            
            Prison.get().getEventBus().post(new FirstJoinEvent(newPlayer));
        } 
        catch (IOException e) {
            Output.get().logError(
                "Failed to create new player data file for player " + 
                		(playerName == null ? "<NoNameAvailable>" : playerName) + 
                		"  target filename: " + newPlayer.filename(), e);
        }
        
        return newPlayer;
    }
    
    /*
     * Listeners
     */

    @Subscribe public void onPlayerJoin(PlayerJoinEvent event) {
        if (!getPlayer(event.getPlayer().getUUID()).isPresent()) {
        	addPlayer( event.getPlayer().getUUID(), event.getPlayer().getName() );
        }
    }

    

    public String getPlayerRankName( RankPlayer rankPlayer ) {
    	StringBuilder sb = new StringBuilder();

		if ( !rankPlayer.getRanks().isEmpty()) {
			for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getRanks().entrySet()) {
				if ( sb.length() > 0 ) {
					sb.append(", ");
				}
				sb.append(entry.getValue().name);
			}
		}

		return sb.toString();
    }
    
    public String getPlayerRankTag( RankPlayer rankPlayer ) {
    	StringBuilder sb = new StringBuilder();
    	
    	if ( !rankPlayer.getRanks().isEmpty()) {
    		for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getRanks().entrySet()) {
    			if ( sb.length() > 0 ) {
    				sb.append(", ");
    			}
    			sb.append(entry.getValue().tag);
    		}
    	}
    	
    	return sb.toString();
    }
    
    public List<Rank> getPlayerRanks( RankPlayer rankPlayer ) {
    	List<Rank> results = new ArrayList<>();

		if ( !rankPlayer.getRanks().isEmpty()) {
			for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getRanks().entrySet()) {
				results.add( entry.getValue() );
			}
		}

		return results;
    }
    
    public List<Rank> getPlayerNextRanks( RankPlayer rankPlayer ) {
    	List<Rank> results = new ArrayList<>();
    	
    	if ( !rankPlayer.getRanks().isEmpty()) {
    		for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getRanks().entrySet()) {
    			
    			RankLadder key = entry.getKey();
    			if(key.getNext(key.getPositionOfRank(entry.getValue())).isPresent()) {
    				
    				Rank nextRank = key.getNext(key.getPositionOfRank(entry.getValue())).get();
    				results.add( nextRank );
    			}
    		}
    	}
    	
    	return results;
    }
    
    public String getPlayerNextRankCost( RankPlayer rankPlayer ) {
    	StringBuilder sb = new StringBuilder();
    	
    	if ( !rankPlayer.getRanks().isEmpty()) {
    		DecimalFormat dFmt = new DecimalFormat("#,##0.00");
    		for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getRanks().entrySet()) {
    			RankLadder key = entry.getKey();
    			if(key.getNext(key.getPositionOfRank(entry.getValue())).isPresent()) {
    				if ( sb.length() > 0 ) {
    					sb.append(", ");
    				}
    				
    				double cost = key.getNext(key.getPositionOfRank(entry.getValue())).get().cost;
    				sb.append( dFmt.format( cost ));
    			}
    		}
    	}
    	
    	return sb.toString();
    }
    
    public String getPlayerNextRankCostPercent( RankPlayer rankPlayer ) {
    	StringBuilder sb = new StringBuilder();
    	
        Player prisonPlayer = PrisonAPI.getPlayer(rankPlayer.uid).orElse(null);
        if( prisonPlayer == null ) {
        	Output.get().logError( String.format( "getPlayerNextRankCostPercent: " +
        			"Could not load player: %s", rankPlayer.uid) );
        	return "0";
        }
    	
    	if ( !rankPlayer.getRanks().isEmpty()) {
    		DecimalFormat dFmt = new DecimalFormat("#,##0.00%");
    		for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getRanks().entrySet()) {
    			RankLadder key = entry.getKey();
    			if(key.getNext(key.getPositionOfRank(entry.getValue())).isPresent()) {
    				if ( sb.length() > 0 ) {
    					sb.append(", ");
    				}
    				
    				Rank rank = key.getNext(key.getPositionOfRank(entry.getValue())).get();
    				double cost = rank.cost;
    				double balance = getPlayerBalance(prisonPlayer,rank);
    				
    				double percent = (balance < 0 ? 0 : 
    									(cost == 0.0d || balance > cost ? 100.0 : 
    											balance / cost * 100.0 )
    								 );
    				sb.append( dFmt.format( percent ));
    			}
    		}
    	}
    	
    	return sb.toString();
    }
    
    private double getPlayerBalance(Player player, Rank rank) {
    	double playerBalance = 0;
        	
    	if ( rank.currency != null ) {
    		EconomyCurrencyIntegration currencyEcon = PrisonAPI.getIntegrationManager()
    						.getEconomyForCurrency( rank.currency );
    		if ( currencyEcon != null ) {
        		playerBalance = currencyEcon.getBalance( player, rank.currency );
    		} else {
    			Output.get().logError( 
    					String.format( "Failed to load Economy to get the balance for " +
    							"player %s with a currency of %s.",
    							player.getName(), rank.currency ));
    		}
    		
    	} else {
    		
    		EconomyIntegration economy = (EconomyIntegration) PrisonAPI.getIntegrationManager()
    				.getForType(IntegrationType.ECONOMY).orElse(null);
    		if ( economy != null ) {
    			playerBalance = economy.getBalance( player );
    		} else {
    			Output.get().logError( 
    					String.format( "Failed to load Economy to get the balance for player %s.",
    							player.getName() ));
    		}
    	}

    	return playerBalance;
    }
    
    public String getPlayerNextRankName( RankPlayer rankPlayer ) {
    	StringBuilder sb = new StringBuilder();
    	
    	if ( !rankPlayer.getRanks().isEmpty()) {
    		for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getRanks().entrySet()) {
    			RankLadder key = entry.getKey();
    			if(key.getNext(key.getPositionOfRank(entry.getValue())).isPresent()) {
    				if ( sb.length() > 0 ) {
    					sb.append(", ");
    				}
    				sb.append(key.getNext(key.getPositionOfRank(entry.getValue())).get().name);
    			}
    		}
    	}
    	
    	return sb.toString();
    }
    
    public String getPlayerNextRankTag( RankPlayer rankPlayer ) {
    	StringBuilder sb = new StringBuilder();
    	
    	if ( !rankPlayer.getRanks().isEmpty()) {
    		for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getRanks().entrySet()) {
    			RankLadder key = entry.getKey();
    			if(key.getNext(key.getPositionOfRank(entry.getValue())).isPresent()) {
    				if ( sb.length() > 0 ) {
    					sb.append(", ");
    				}
    				sb.append(key.getNext(key.getPositionOfRank(entry.getValue())).get().tag);
    			}
    		}
    	}
    	
    	return sb.toString();
    }
    
    public String getTranslatePlayerPlaceHolder( UUID playerUuid, String identifier ) {
    	PrisonPlaceHolders placeHolder = PrisonPlaceHolders.fromString( identifier );
    	return placeHolder == PrisonPlaceHolders.no_match__ ? null :
    		getTranslatePlayerPlaceHolder( playerUuid, placeHolder );
    }
    
    public String getTranslatePlayerPlaceHolder( UUID playerUuid, PrisonPlaceHolders placeHolder ) {
		String results = null;

		Optional<RankPlayer> oPlayer = getPlayer(playerUuid);
		
		if ( oPlayer.isPresent() ) {
			RankPlayer rankPlayer = oPlayer.get();
			
			switch ( placeHolder ) {
				case prison_r:
				case prison_rank:
					results = getPlayerRankName( rankPlayer );
					break;

				case prison_rt:
				case prison_rank_tag:
					results = getPlayerRankTag( rankPlayer );
					break;
					
				case prison_rc:
				case prison_rankup_cost:
					results = getPlayerNextRankCost( rankPlayer );
					break;
					
				case prison_rcp:
				case prison_rankup_cost_percent:
					results = getPlayerNextRankCostPercent( rankPlayer );
					break;
					
				case prison_rr:
				case prison_rankup_rank:
					results = getPlayerNextRankName( rankPlayer );
					break;
					
				case prison_rrt:
				case prison_rankup_rank_tag:
					results = getPlayerNextRankTag( rankPlayer );
					break;
					
				default:
					break;
			}
		}
		
		return results;
    }

    @Override
    public List<PlaceHolderKey> getTranslatedPlaceHolderKeys() {
    	List<PlaceHolderKey> results = new ArrayList<>();
    	
    	List<PrisonPlaceHolders> placeHolders = PrisonPlaceHolders.getTypes( PlaceHolderFlags.PLAYER );
    	for ( PrisonPlaceHolders ph : placeHolders ) {
			PlaceHolderKey placeholder = new PlaceHolderKey(ph.name(), ph );
			results.add( placeholder );
			
			// Now generate a new key based upon the first key, but without the prison_ prefix:
			String key2 = ph.name().replace( 
					IntegrationManager.PRISON_PLACEHOLDER_PREFIX + "_", "" );
			PlaceHolderKey placeholder2 = new PlaceHolderKey(key2, ph );
			results.add( placeholder2 );
		}
    	
    	return results;
    }
}
