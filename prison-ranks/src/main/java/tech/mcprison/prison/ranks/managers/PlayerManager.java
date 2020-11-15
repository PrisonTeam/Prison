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
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.RankUtil;
import tech.mcprison.prison.ranks.RankupResults;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.events.FirstJoinEvent;
import tech.mcprison.prison.store.Collection;
import tech.mcprison.prison.store.Document;
import tech.mcprison.prison.util.PlaceholdersUtil;

/**
 * Manages all the players in the records.
 *
 * @author Faizaan A. Datoo
 */
public class PlayerManager
	implements ManagerPlaceholders {


    private Collection collection;
    private List<RankPlayer> players;

    private List<PlaceHolderKey> translatedPlaceHolderKeys;
    

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
    public Optional<RankPlayer> getPlayer(UUID uid, String playerName) {
    	Optional<RankPlayer> results = players.stream().filter(player -> player.uid.equals(uid)).findFirst();
    	
    	if ( !results.isPresent() ) {
    		results = Optional.ofNullable( addPlayer(uid, playerName) );
    	}
    	
    	// check to see if the name has changed, if so, then save because the new name was added:
    	if ( playerName != null && playerName.trim().length() > 0 && 
    				results.get().checkName( playerName ) ) {
    		try {
				savePlayer( results.get() );
			}
			catch ( IOException e ) {
				Output.get().logWarn( 
					String.format( "PlayerManager.getPlayer(): Failed to add new player name: %s. %s",
									playerName, e.getMessage()) );
			}
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
    	
    	Player player = event.getPlayer();
    	
        if (!getPlayer(player.getUUID(), player.getName()).isPresent()) {
        	addPlayer( player.getUUID(), player.getName() );
        }
    }

    

    public String getPlayerRankName( RankPlayer rankPlayer, String ladderName ) {
    	StringBuilder sb = new StringBuilder();

		if ( !rankPlayer.getRanks().isEmpty()) {
			for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getRanks().entrySet()) {
				if ( ladderName == null ||
					 ladderName != null && entry.getKey().name.equalsIgnoreCase( ladderName )) {
					
					if ( sb.length() > 0 ) {
						sb.append(" ");
					}
					sb.append(entry.getValue().name);
				}
			}
		}

		return sb.toString();
    }
    
    public String getPlayerRankTag( RankPlayer rankPlayer, String ladderName ) {
    	StringBuilder sb = new StringBuilder();
    	
    	if ( !rankPlayer.getRanks().isEmpty()) {
    		for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getRanks().entrySet()) {
    			if ( ladderName == null ||
   					 ladderName != null && entry.getKey().name.equalsIgnoreCase( ladderName )) {

//					if ( sb.length() > 0 ) {
//  	  				sb.append(" ");
//    				}
    				sb.append(entry.getValue().tag);
    			}
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
    
    public String getPlayerNextRankCost( RankPlayer rankPlayer, String ladderName, boolean formatted ) {
    	StringBuilder sb = new StringBuilder();
    	
    	if ( !rankPlayer.getRanks().isEmpty()) {
    		DecimalFormat dFmt = new DecimalFormat("#,##0.00");
    		for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getRanks().entrySet()) {
    			RankLadder key = entry.getKey();
    			if ( ladderName == null ||
      				 ladderName != null && key.name.equalsIgnoreCase( ladderName )) {
    				
    				if(key.getNext(key.getPositionOfRank(entry.getValue())).isPresent()) {
    					if ( sb.length() > 0 ) {
    						sb.append(", ");
    					}
    					
    					double cost = key.getNext(key.getPositionOfRank(entry.getValue())).get().cost;
    					if ( formatted ) {
    						sb.append( PlaceholdersUtil.formattedSize( cost ));
    					}
    					else {
    						sb.append( dFmt.format( cost ));
    					}
    				}
    			}
    		}
    	}
    	
    	return sb.toString();
    }
        
    public String getPlayerNextRankCostPercent( RankPlayer rankPlayer, String ladderName ) {
    	StringBuilder sb = new StringBuilder();
    	
        Player prisonPlayer = PrisonAPI.getPlayer(rankPlayer.uid).orElse(null);
        if( prisonPlayer == null ) {
        	Output.get().logError( String.format( "getPlayerNextRankCostPercent: " +
        			"Could not load player: %s", rankPlayer.uid) );
        	return "0";
        }
    	
    	if ( !rankPlayer.getRanks().isEmpty()) {
    		DecimalFormat dFmt = new DecimalFormat("#,##0.00");
    		for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getRanks().entrySet()) {
    			RankLadder key = entry.getKey();
    			if ( ladderName == null ||
     				 ladderName != null && key.name.equalsIgnoreCase( ladderName )) {
    				
    				if(key.getNext(key.getPositionOfRank(entry.getValue())).isPresent()) {
    					if ( sb.length() > 0 ) {
    						sb.append(",  ");
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
    	}
    	
    	return sb.toString();
    }
    
    public String getPlayerNextRankCostBar( RankPlayer rankPlayer, String ladderName ) {
    	StringBuilder sb = new StringBuilder();
    	
    	Player prisonPlayer = PrisonAPI.getPlayer(rankPlayer.uid).orElse(null);
    	if( prisonPlayer == null ) {
    		Output.get().logError( String.format( "getPlayerNextRankCostBar: " +
    				"Could not load player: %s", rankPlayer.uid) );
    		return "0";
    	}
    	
    	if ( !rankPlayer.getRanks().isEmpty()) {
    		
//    		DecimalFormat dFmt = new DecimalFormat("#,##0.00");
    		for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getRanks().entrySet()) {
    			RankLadder key = entry.getKey();
    			if ( ladderName == null ||
    					ladderName != null && key.name.equalsIgnoreCase( ladderName )) {
    				
    				if(key.getNext(key.getPositionOfRank(entry.getValue())).isPresent()) {
    					if ( sb.length() > 0 ) {
    						sb.append(",  ");
    					}
    					
    					Rank rank = key.getNext(key.getPositionOfRank(entry.getValue())).get();
    					double cost = rank.cost;
    					double balance = getPlayerBalance(prisonPlayer,rank);
    					
    				   	
    			    	sb.append( Prison.get().getIntegrationManager().
    			    					getProgressBar( balance, cost, false ));

    				}
    			}
    		}
    	}
    	
    	return sb.toString();
    }
    
    public String getPlayerNextRankCostRemaining( RankPlayer rankPlayer, String ladderName, boolean formatted ) {
    	StringBuilder sb = new StringBuilder();
    	
    	Player prisonPlayer = PrisonAPI.getPlayer(rankPlayer.uid).orElse(null);
    	if( prisonPlayer == null ) {
    		Output.get().logError( String.format( "getPlayerNextRankCostRemaining: " +
    				"Could not load player: %s", rankPlayer.uid) );
    		return "0";
    	}
    	
    	if ( !rankPlayer.getRanks().isEmpty()) {
    		DecimalFormat dFmt = new DecimalFormat("#,##0.00");
    		for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getRanks().entrySet()) {
    			RankLadder key = entry.getKey();
    			if ( ladderName == null ||
    					ladderName != null && key.name.equalsIgnoreCase( ladderName )) {
    				
    				if(key.getNext(key.getPositionOfRank(entry.getValue())).isPresent()) {
    					if ( sb.length() > 0 ) {
    						sb.append(",  ");
    					}
    					
    					Rank rank = key.getNext(key.getPositionOfRank(entry.getValue())).get();
    					double cost = rank.cost;
    					double balance = getPlayerBalance(prisonPlayer,rank);
    					
    					double remaining = cost - balance;
    					
    					if ( remaining < 0 ) {
    						remaining = 0;
    					}
    					
    					if ( formatted ) {
    						sb.append( PlaceholdersUtil.formattedSize( remaining ));
    					}
    					else {
    						sb.append( dFmt.format( remaining ));
    					}
    				}
    			}
    		}
    	}
    	
    	return sb.toString();
    }
    
    private String getPlayerBalance( RankPlayer rankPlayer, String ladderName, boolean formatted ) {
    	StringBuilder sb = new StringBuilder();
    	
    	Player prisonPlayer = PrisonAPI.getPlayer(rankPlayer.uid).orElse(null);
    	if( prisonPlayer == null ) {
    		Output.get().logError( String.format( "getPlayerBalance: " +
    				"Could not load player: %s", rankPlayer.uid) );
    		return "0";
    	}
    	
    	if ( !rankPlayer.getRanks().isEmpty()) {
    		DecimalFormat dFmt = new DecimalFormat("#,##0.00");
    		for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getRanks().entrySet()) {
    			RankLadder key = entry.getKey();
    			if ( ladderName == null ||
    					ladderName != null && key.name.equalsIgnoreCase( ladderName )) {
    				
    				if(key.getNext(key.getPositionOfRank(entry.getValue())).isPresent()) {
    					if ( sb.length() > 0 ) {
    						sb.append(",  ");
    					}
    					
    					Rank rank = key.getNext(key.getPositionOfRank(entry.getValue())).get();
    					double balance = getPlayerBalance(prisonPlayer,rank);
    					
    					if ( formatted ) {
    						sb.append( PlaceholdersUtil.formattedSize( balance ));
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
    		
    		EconomyIntegration economy = PrisonAPI.getIntegrationManager().getEconomy();

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
    
    public String getPlayerNextRankName( RankPlayer rankPlayer, String ladderName ) {
    	StringBuilder sb = new StringBuilder();
    	
    	if ( !rankPlayer.getRanks().isEmpty()) {
    		for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getRanks().entrySet()) {
    			RankLadder key = entry.getKey();
    			if ( ladderName == null ||
       				 ladderName != null && key.name.equalsIgnoreCase( ladderName )) {

    				if(key.getNext(key.getPositionOfRank(entry.getValue())).isPresent()) {
    					if ( sb.length() > 0 ) {
    						sb.append(" ");
    					}
    					sb.append(key.getNext(key.getPositionOfRank(entry.getValue())).get().name);
    				}
    			}
    		}
    	}
    	
    	return sb.toString();
    }
    
    public String getPlayerNextRankTag( RankPlayer rankPlayer, String ladderName ) {
    	StringBuilder sb = new StringBuilder();
    	
    	if ( !rankPlayer.getRanks().isEmpty()) {
    		for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getRanks().entrySet()) {
    			RankLadder key = entry.getKey();
    			if ( ladderName == null ||
          			 ladderName != null && key.name.equalsIgnoreCase( ladderName )) {

    				if(key.getNext(key.getPositionOfRank(entry.getValue())).isPresent()) {
//    					if ( sb.length() > 0 ) {
//    						sb.append(", ");
//    					}
    					sb.append(key.getNext(key.getPositionOfRank(entry.getValue())).get().tag);
    				}
    			}
    		}
    	}
    	
    	return sb.toString();
    }
    
    public String getTranslatePlayerPlaceHolder( UUID playerUuid, String playerName, String identifier ) {
    	String results = null;

    	if ( playerUuid != null ) {
    		
    		List<PlaceHolderKey> placeHolderKeys = getTranslatedPlaceHolderKeys();
    		
    		if ( !identifier.startsWith( IntegrationManager.PRISON_PLACEHOLDER_PREFIX_EXTENDED )) {
    			identifier = IntegrationManager.PRISON_PLACEHOLDER_PREFIX_EXTENDED + identifier;
    		}
    		
    		for ( PlaceHolderKey placeHolderKey : placeHolderKeys ) {
    			if ( placeHolderKey.getKey().equalsIgnoreCase( identifier )) {
    				results = getTranslatePlayerPlaceHolder( playerUuid, playerName, placeHolderKey );
    				break;
    			}
    		}
    	}
    	
    	return results;
    }
    
    public String getTranslatePlayerPlaceHolder( UUID playerUuid, String playerName, PlaceHolderKey placeHolderKey ) {
		String results = null;

		if ( playerUuid != null ) {
			
			PrisonPlaceHolders placeHolder = placeHolderKey.getPlaceholder();
			
			String ladderName = placeHolderKey.getData();
			
			Optional<RankPlayer> oPlayer = getPlayer(playerUuid, playerName);
			
			if ( oPlayer.isPresent() ) {
				RankPlayer rankPlayer = oPlayer.get();
				
				switch ( placeHolder ) {
					case prison_r:
					case prison_rank:
					case prison_r_laddername:
					case prison_rank_laddername:
						results = getPlayerRankName( rankPlayer, ladderName );
						break;
						
					case prison_rt:
					case prison_rank_tag:
					case prison_rt_laddername:
					case prison_rank_tag_laddername:
						results = getPlayerRankTag( rankPlayer, ladderName );
						break;
						
					case prison_rc:
					case prison_rankup_cost:
					case prison_rc_laddername:
					case prison_rankup_cost_laddername:
						results = getPlayerNextRankCost( rankPlayer, ladderName, false );
						break;
						
					case prison_rcf:
					case prison_rankup_cost_formatted:
					case prison_rcf_laddername:
					case prison_rankup_cost_formatted_laddername:
						results = getPlayerNextRankCost( rankPlayer, ladderName, true );
						break;
						
					case prison_rcp:
					case prison_rankup_cost_percent:
					case prison_rcp_laddername:
					case prison_rankup_cost_percent_laddername:
						results = getPlayerNextRankCostPercent( rankPlayer, ladderName );
						break;
						
					case prison_rcb:
					case prison_rankup_cost_bar:
					case prison_rcb_laddername:
					case prison_rankup_cost_bar_laddername:
						results = getPlayerNextRankCostBar( rankPlayer, ladderName );
						break;
						
					case prison_rcr:
					case prison_rankup_cost_remaining:
					case prison_rcr_laddername:
					case prison_rankup_cost_remaining_laddername:
						results = getPlayerNextRankCostRemaining( rankPlayer, ladderName, false );
						break;
						
					case prison_rcrf:
					case prison_rankup_cost_remaining_formatted:
					case prison_rcrf_laddername:
					case prison_rankup_cost_remaining_formatted_laddername:
						results = getPlayerNextRankCostRemaining( rankPlayer, ladderName, true );
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
						
					case prison_pb:
					case prison_player_balance:
					case prison_pb_laddername:
					case prison_player_balance_laddername:
						results = getPlayerBalance( rankPlayer, ladderName, false );
						
					default:
						break;
				}
			}
		}
		
		return results;
    }

    @Override
    public List<PlaceHolderKey> getTranslatedPlaceHolderKeys() {
    	if ( translatedPlaceHolderKeys == null ) {
    		translatedPlaceHolderKeys = new ArrayList<>();
    		
    		// This generates all of the placeholders for the player ranks:
    		List<PrisonPlaceHolders> placeHolders = PrisonPlaceHolders.getTypes( PlaceHolderFlags.PLAYER );
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
    		placeHolders = PrisonPlaceHolders.getTypes( PlaceHolderFlags.LADDERS );
    		
    		List<RankLadder> ladders = PrisonRanks.getInstance().getLadderManager().getLadders();
    		for ( RankLadder ladder : ladders ) {
    			for ( PrisonPlaceHolders ph : placeHolders ) {
    				String key = ph.name().replace( 
    						IntegrationManager.PRISON_PLACEHOLDER_LADDERNAME_SUFFIX, "_" + ladder.name ).
    							toLowerCase();
    				
    				PlaceHolderKey placeholder = new PlaceHolderKey(key, ph, ladder.name );
    				if ( ph.getAlias() != null ) {
    					String aliasName = ph.getAlias().name().replace( 
    							IntegrationManager.PRISON_PLACEHOLDER_LADDERNAME_SUFFIX, "_" + ladder.name ).
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
