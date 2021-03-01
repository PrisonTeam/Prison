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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import com.google.common.eventbus.Subscribe;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.commands.BaseCommands;
import tech.mcprison.prison.integration.EconomyCurrencyIntegration;
import tech.mcprison.prison.integration.EconomyIntegration;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.events.player.PlayerJoinEvent;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.placeholders.ManagerPlaceholders;
import tech.mcprison.prison.placeholders.PlaceHolderKey;
import tech.mcprison.prison.placeholders.PlaceholderAttribute;
import tech.mcprison.prison.placeholders.PlaceholderAttributeNumberFormat;
import tech.mcprison.prison.placeholders.PlaceholderAttributeText;
import tech.mcprison.prison.placeholders.PlaceholderManager;
import tech.mcprison.prison.placeholders.PlaceholderManager.PlaceHolderFlags;
import tech.mcprison.prison.placeholders.PlaceholderManager.PrisonPlaceHolders;
import tech.mcprison.prison.placeholders.PlaceholderResults;
import tech.mcprison.prison.placeholders.PlaceholdersUtil;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.RankUtil;
import tech.mcprison.prison.ranks.RankupResults;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.events.FirstJoinEvent;
import tech.mcprison.prison.store.Collection;
import tech.mcprison.prison.store.Document;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;

/**
 * Manages all the players in the records.
 *
 * @author Faizaan A. Datoo
 */
public class PlayerManager
	extends BaseCommands
	implements ManagerPlaceholders {


    private Collection collection;
    private List<RankPlayer> players;
    private TreeMap<String, RankPlayer> playersByName;
    
    private List<RankPlayer> playersByTop;

    private List<PlaceHolderKey> translatedPlaceHolderKeys;
    
    private transient Set<String> playerErrors;

    public PlayerManager(Collection collection) {
    	super("PlayerMangager");
    	
        this.collection = collection;
        this.players = new ArrayList<>();
        this.playersByName = new TreeMap<>();
        
        this.playerErrors = new HashSet<>();

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
        RankPlayer rankPlayer = new RankPlayer(document);
        
        players.add( rankPlayer );
        
        // add by uuid:
        playersByName.put( rankPlayer.getUUID().toString(), rankPlayer );
        
        // add by name:
        if ( rankPlayer.getNames().size() > 0 ) {
        	playersByName.put( rankPlayer.getDisplayName(), rankPlayer );
        	
        }
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
				
    			if ( !getPlayerErrors().contains( message ) ) {
    				getPlayerErrors().add( message );
    				Output.get().logError( message );
    			}
    			
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

    public TreeMap<String, RankPlayer> getPlayersByName() {
		return playersByName;
	}

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
    	boolean dirty = false;
    	
    	if ( playerName != null && getPlayersByName().containsKey( playerName ) ) {
    		results = getPlayersByName().get( playerName );
    	}
    	
    	if ( results == null ) {
    		
    		for ( RankPlayer rankPlayer : players ) {
    			if ( uid != null && rankPlayer.getUUID().equals(uid) || 
    					uid == null && playerName != null && playerName.trim().length() > 0 &&
    					rankPlayer.getDisplayName() != null &&
    					rankPlayer.getDisplayName().equalsIgnoreCase( playerName ) ) {
    				
    				// This checks to see if they have a new name, if so, then adds it to the history:
    				// But the UID must match:
    				if ( uid != null && rankPlayer.getUUID().equals(uid) ) {
    					dirty = rankPlayer.checkName( playerName );
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
    		results = addPlayer(uid, playerName);
    		dirty = results != null;
    	}
    	
    	// Save if dirty (change or new):
    	if ( dirty && results != null ) {
    		try {
				savePlayer( results );
			}
			catch ( IOException e ) {
				String message = String.format( "PlayerManager.getPlayer(): Failed to add new player name: %s. %s",
									playerName, e.getMessage());
    			if ( !getPlayerErrors().contains( message ) ) {
    				
    				getPlayerErrors().add( message );
    				Output.get().logError( message );
    			}
			}
    	}
    	
    	
    	return results;
    }
    
    public RankPlayer getPlayer( Player player ) {
    	RankPlayer rPlayer = null;
    	if ( player != null ) {
    		rPlayer = getPlayer( player.getUUID(), player.getName() );
    	}
    	return rPlayer;
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
        			
        			// We need to create a new player data file.
        			newPlayer = new RankPlayer( uid, playerName );
        			newPlayer.checkName( playerName );
        			
        			players.add(newPlayer);
        			getPlayersByName().put( playerName, newPlayer );
        			
        			try {
        				savePlayer(newPlayer);
        				
        				Player player = getPlayer( null, playerName, uid );
        				
        				// Assign the player to the default rank:
        				String ladder = null; // will set to the "default" ladder
        				String rank = null;   // will set to the "default" rank
        				
        				// Set the rank to the default ladder and the default rank.  The results are logged
        				// before the results are returned, so can ignore the results:
        				@SuppressWarnings( "unused" )
        				RankupResults results = new RankUtil().setRank(player, newPlayer, ladder, rank, 
        						playerName, "FirstJoinEvent");
        				
        				
        				Prison.get().getEventBus().post(new FirstJoinEvent(newPlayer));
        			} 
        			catch (IOException e) {
        				Output.get().logError(
        						"Failed to create new player data file for player " + 
        								(playerName == null ? "<NoNameAvailable>" : playerName) + 
        								"  target filename: " + newPlayer.filename(), e);
        			}
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
    	
    	// Player is auto added if they do not exist when calling getPlayer so don't try to
    	// add them a second time.
        getPlayer(player.getUUID(), player.getName());
        
    }

    

    public String getPlayerRankName( RankPlayer rankPlayer, String ladderName ) {
    	StringBuilder sb = new StringBuilder();

		if ( !rankPlayer.getLadderRanks().isEmpty()) {
			for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getLadderRanks().entrySet()) {
				if ( ladderName == null ||
					 ladderName != null && entry.getKey().getName().equalsIgnoreCase( ladderName )) {
					
					if ( sb.length() > 0 ) {
						sb.append(" ");
					}
					sb.append(entry.getValue().getName());
				}
			}
		}

		return sb.toString();
    }
    
    
    public String getPlayerRankNumber( RankPlayer rankPlayer, String ladderName, 
    						PlaceholderAttribute attribute ) {
    	StringBuilder sb = new StringBuilder();
    	
    	if ( !rankPlayer.getLadderRanks().isEmpty()) {
    		for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getLadderRanks().entrySet()) {
    			if ( ladderName == null ||
    					ladderName != null && entry.getKey().getName().equalsIgnoreCase( ladderName )) {
    				
    				if ( sb.length() > 0 ) {
    					sb.append(" ");
    				}
    				
    				int rankNumber = rankNumber(entry.getValue());
    				
    				if ( attribute != null && attribute instanceof PlaceholderAttributeNumberFormat ) {
    					PlaceholderAttributeNumberFormat attributeNF = 
    													(PlaceholderAttributeNumberFormat) attribute;
    					sb.append( attributeNF.format( (long) rankNumber ) );
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
    		for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getLadderRanks().entrySet()) {
    			if ( ladderName == null ||
   					 ladderName != null && entry.getKey().getName().equalsIgnoreCase( ladderName )) {

//					if ( sb.length() > 0 ) {
//  	  				sb.append(" ");
//    				}
    				sb.append(entry.getValue().getTag());
    			}
    		}
    	}
    	
    	return sb.toString();
    }
    
    public List<Rank> getPlayerRanks( RankPlayer rankPlayer ) {
    	List<Rank> results = new ArrayList<>();

		if ( !rankPlayer.getLadderRanks().isEmpty()) {
			for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getLadderRanks().entrySet()) {
				results.add( entry.getValue() );
			}
		}

		return results;
    }
    
    public List<Rank> getPlayerNextRanks( RankPlayer rankPlayer ) {
    	List<Rank> results = new ArrayList<>();
    	
    	if ( !rankPlayer.getLadderRanks().isEmpty()) {
    		for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getLadderRanks().entrySet()) {
    			
    			RankLadder key = entry.getKey();
    			if(key.getNext(key.getPositionOfRank(entry.getValue())).isPresent()) {
    				
    				Rank nextRank = key.getNext(key.getPositionOfRank(entry.getValue())).get();
    				results.add( nextRank );
    			}
    		}
    	}
    	
    	return results;
    }
    
    public String getPlayerNextRankCost( RankPlayer rankPlayer, String ladderName, 
    					boolean formatted, PlaceholderAttribute attribute ) {
    	StringBuilder sb = new StringBuilder();
    	
    	if ( !rankPlayer.getLadderRanks().isEmpty()) {
    		DecimalFormat dFmt = new DecimalFormat("#,##0");
    		for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getLadderRanks().entrySet()) {
    			RankLadder key = entry.getKey();
    			if ( ladderName == null ||
      				 ladderName != null && key.getName().equalsIgnoreCase( ladderName )) {
    				
    				if(key.getNext(key.getPositionOfRank(entry.getValue())).isPresent()) {
    					if ( sb.length() > 0 ) {
    						sb.append(", ");
    					}
    					
    					double cost = key.getNext(key.getPositionOfRank(entry.getValue())).get().getCost();
    					
        				if ( attribute != null && attribute instanceof PlaceholderAttributeNumberFormat ) {
        					PlaceholderAttributeNumberFormat attributeNF = 
        													(PlaceholderAttributeNumberFormat) attribute;
        					sb.append( attributeNF.format( cost ) );
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
    	
    	return sb.toString();
    }
        
    public String getPlayerNextRankCostPercent( RankPlayer rankPlayer, String ladderName ) {
    	StringBuilder sb = new StringBuilder();
    	
        Player prisonPlayer = PrisonAPI.getPlayer(rankPlayer.getUUID()).orElse(null);
        if( prisonPlayer == null ) {
        	String message = String.format( "getPlayerNextRankCostPercent: " +
        			"Could not load player: %s", rankPlayer.getUUID());
			
        	if ( !getPlayerErrors().contains( message ) ) {
				getPlayerErrors().add( message );
				Output.get().logError( message );
			}
        	return "0";
        }
    	
    	if ( !rankPlayer.getLadderRanks().isEmpty()) {
    		DecimalFormat dFmt = new DecimalFormat("#,##0");
    		for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getLadderRanks().entrySet()) {
    			RankLadder key = entry.getKey();
    			if ( ladderName == null ||
     				 ladderName != null && key.getName().equalsIgnoreCase( ladderName )) {
    				
    				if(key.getNext(key.getPositionOfRank(entry.getValue())).isPresent()) {
    					if ( sb.length() > 0 ) {
    						sb.append(",  ");
    					}
    					
    					Rank rank = key.getNext(key.getPositionOfRank(entry.getValue())).get();
    					double cost = rank.getCost();
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
    
    public String getPlayerNextRankCostBar( RankPlayer rankPlayer, String ladderName, 
    														PlaceholderAttribute attribute ) {
    	StringBuilder sb = new StringBuilder();
    	
    	Player prisonPlayer = PrisonAPI.getPlayer(rankPlayer.getUUID()).orElse(null);
    	if( prisonPlayer == null ) {
    		String message = String.format( "getPlayerNextRankCostBar: " +
    				"Could not load player: %s", rankPlayer.getUUID());

    		if ( !getPlayerErrors().contains( message ) ) {
				getPlayerErrors().add( message );
				Output.get().logError( message );
			}
			
    		return "0";
    	}
    	
    	if ( !rankPlayer.getLadderRanks().isEmpty()) {
    		
//    		DecimalFormat dFmt = new DecimalFormat("#,##0.00");
    		for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getLadderRanks().entrySet()) {
    			RankLadder key = entry.getKey();
    			if ( ladderName == null ||
    					ladderName != null && key.getName().equalsIgnoreCase( ladderName )) {
    				
    				if(key.getNext(key.getPositionOfRank(entry.getValue())).isPresent()) {
    					if ( sb.length() > 0 ) {
    						sb.append(",  ");
    					}
    					
    					Rank rank = key.getNext(key.getPositionOfRank(entry.getValue())).get();
    					double cost = rank.getCost();
    					double balance = getPlayerBalance(prisonPlayer,rank);
    					
    				   	
    			    	sb.append( Prison.get().getPlaceholderManager().
    			    					getProgressBar( balance, cost, false, attribute ));

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
    						boolean formatted, PlaceholderAttribute attribute ) {
    	StringBuilder sb = new StringBuilder();
    	
    	Player prisonPlayer = PrisonAPI.getPlayer(rankPlayer.getUUID()).orElse(null);
    	if( prisonPlayer == null ) {
    		String message = String.format( "getPlayerNextRankCostRemaining: " +
    				"Could not load player: %s", rankPlayer.getUUID());
    		
			if ( !getPlayerErrors().contains( message ) ) {
				getPlayerErrors().add( message );
				Output.get().logError( message );
			}
			
    		return "0";
    	}
    	
    	if ( !rankPlayer.getLadderRanks().isEmpty()) {
    		DecimalFormat dFmt = new DecimalFormat("#,##0");
    		for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getLadderRanks().entrySet()) {
    			RankLadder key = entry.getKey();
    			if ( ladderName == null ||
    					ladderName != null && key.getName().equalsIgnoreCase( ladderName )) {
    				
    				if(key.getNext(key.getPositionOfRank(entry.getValue())).isPresent()) {
    					if ( sb.length() > 0 ) {
    						sb.append(",  ");
    					}
    					
    					Rank rank = key.getNext(key.getPositionOfRank(entry.getValue())).get();
    					double cost = rank.getCost();
    					double balance = getPlayerBalance(prisonPlayer,rank);
    					
    					double remaining = cost - balance;
    					
//    					if ( remaining < 0 ) {
//    						remaining = 0;
//    					}
    					
        				if ( attribute != null && attribute instanceof PlaceholderAttributeNumberFormat ) {
        					PlaceholderAttributeNumberFormat attributeNF = 
        													(PlaceholderAttributeNumberFormat) attribute;
        					sb.append( attributeNF.format( remaining ) );
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
    								boolean formatted, PlaceholderAttribute attribute ) {
    	StringBuilder sb = new StringBuilder();
    	
    	Player prisonPlayer = PrisonAPI.getPlayer(rankPlayer.getUUID()).orElse(null);
    	if( prisonPlayer == null ) {
    		String message = String.format( "getPlayerBalance: " +
    				"Could not load player: %s", rankPlayer.getUUID());
    		
			if ( !getPlayerErrors().contains( message ) ) {
				getPlayerErrors().add( message );
				Output.get().logError( message );
			}
			
    		return "0";
    	}
    	
    	if ( !rankPlayer.getLadderRanks().isEmpty()) {
    		DecimalFormat dFmt = new DecimalFormat("#,##0");
    		for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getLadderRanks().entrySet()) {
    			RankLadder key = entry.getKey();
    			if ( ladderName == null ||
    					ladderName != null && key.getName().equalsIgnoreCase( ladderName )) {
    				
    				if(key.getNext(key.getPositionOfRank(entry.getValue())).isPresent()) {
    					if ( sb.length() > 0 ) {
    						sb.append(",  ");
    					}
    					
    					Rank rank = key.getNext(key.getPositionOfRank(entry.getValue())).get();
    					double balance = getPlayerBalance(prisonPlayer,rank);
    					
        				if ( attribute != null && attribute instanceof PlaceholderAttributeNumberFormat ) {
        					PlaceholderAttributeNumberFormat attributeNF = 
        													(PlaceholderAttributeNumberFormat) attribute;
        					sb.append( attributeNF.format( balance ) );
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
    
    /**
     * <p>This gets the player's balance, and if the rank is provided, it will check to 
     * see if there is a custom currency that needs to be used for that rank.  If there
     * is a custom currency, then it will check the balance for that player using that
     * currency.
     * </p>
     * 
     * @param player
     * @param rank
     * @return
     */
    private double getPlayerBalance(Player player, Rank rank) {
    	double playerBalance = 0;
        	
    	if ( rank != null && rank.getCurrency() != null ) {
    		EconomyCurrencyIntegration currencyEcon = PrisonAPI.getIntegrationManager()
    						.getEconomyForCurrency( rank.getCurrency() );
    		if ( currencyEcon != null ) {
        		playerBalance = currencyEcon.getBalance( player, rank.getCurrency() );
    		} else {
    			String message = String.format( "Failed to load Economy to get the balance for " +
						"player %s with a currency of %s.",
						player.getName(), rank.getCurrency() );
    			
    			if ( !getPlayerErrors().contains( message ) ) {
    				getPlayerErrors().add( message );
    				Output.get().logError( message );
    			}
    			
    		}
    		
    	} else {
    		
    		EconomyIntegration economy = PrisonAPI.getIntegrationManager().getEconomy();

    		if ( economy != null ) {
    			playerBalance = economy.getBalance( player );
    		} else {
    			String message = String.format( "Failed to load Economy to get the balance for player %s.",
						player.getName() );
    			Output.get().logError( message );
    			if ( !getPlayerErrors().contains( message ) ) {
    				
    				getPlayerErrors().add( message );
    				Output.get().logError( message );
    			}
    			
    		}
    	}

    	return playerBalance;
    }
    
    public String getPlayerNextRankName( RankPlayer rankPlayer, String ladderName ) {
    	StringBuilder sb = new StringBuilder();
    	
    	if ( !rankPlayer.getLadderRanks().isEmpty()) {
    		for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getLadderRanks().entrySet()) {
    			RankLadder key = entry.getKey();
    			if ( ladderName == null ||
       				 ladderName != null && key.getName().equalsIgnoreCase( ladderName )) {

    				if(key.getNext(key.getPositionOfRank(entry.getValue())).isPresent()) {
    					if ( sb.length() > 0 ) {
    						sb.append(" ");
    					}
    					sb.append(key.getNext(key.getPositionOfRank(entry.getValue())).get().getName());
    				}
    			}
    		}
    	}
    	
    	return sb.toString();
    }
    
    public String getPlayerNextRankTag( RankPlayer rankPlayer, String ladderName ) {
    	StringBuilder sb = new StringBuilder();
    	
    	if ( !rankPlayer.getLadderRanks().isEmpty()) {
    		for (Map.Entry<RankLadder, Rank> entry : rankPlayer.getLadderRanks().entrySet()) {
    			RankLadder key = entry.getKey();
    			if ( ladderName == null ||
          			 ladderName != null && key.getName().equalsIgnoreCase( ladderName )) {

    				if(key.getNext(key.getPositionOfRank(entry.getValue())).isPresent()) {
//    					if ( sb.length() > 0 ) {
//    						sb.append(", ");
//    					}
    					sb.append(key.getNext(key.getPositionOfRank(entry.getValue())).get().getTag());
    				}
    			}
    		}
    	}
    	
    	return sb.toString();
    }
    

	private String getPlayerSellallMultiplier( RankPlayer rankPlayer, PlaceholderAttribute attribute ) {
		String results;
		double multiplier = rankPlayer.getSellAllMultiplier();
		if ( attribute != null && attribute instanceof PlaceholderAttributeNumberFormat ) {
			PlaceholderAttributeNumberFormat attributeNF = 
													(PlaceholderAttributeNumberFormat) attribute;
			results = attributeNF.format( multiplier );
		}
		else {
			results = Double.toString( multiplier );
		}
		return results;
	}
	
    /**
     * <p>Entry point for translating placeholders.
     * </p>
     * @param playerUuid
     * @param playerName
     * @param identifier
     * @return
     */
    public String getTranslatePlayerPlaceHolder( UUID playerUuid, String playerName, String identifier ) {
    	String results = null;

    	if ( playerUuid != null && identifier != null ) {
    		
    		List<PlaceHolderKey> placeHolderKeys = getTranslatedPlaceHolderKeys();
    		
    		identifier = identifier.toLowerCase();
    		
    		if ( !identifier.startsWith( PlaceholderManager.PRISON_PLACEHOLDER_PREFIX_EXTENDED )) {
    			identifier = PlaceholderManager.PRISON_PLACEHOLDER_PREFIX_EXTENDED + identifier;
    		}
    		
    		// placeholder Attributes: 
    		PlaceholderManager pman = Prison.get().getPlaceholderManager();
    		String placeholder = pman.extractPlaceholderString( identifier );
    		PlaceholderAttribute attribute = pman.extractPlaceholderExtractAttribute( identifier );
    		
    		for ( PlaceHolderKey placeHolderKey : placeHolderKeys ) {
    			if ( placeHolderKey.getKey().equalsIgnoreCase( placeholder )) {
    				results = getTranslatePlayerPlaceHolder( playerUuid, playerName, placeHolderKey, attribute );
    				break;
    			}
    		}
    	}
    	
    	return results;
    }
    public String getTranslatePlayerPlaceHolder( UUID playerUuid, String playerName, PlaceholderResults placeholderResults ) {
    	String results = null;
    	
    	if ( playerUuid != null && placeholderResults.hasResults() ) {
    		
    		List<PlaceHolderKey> placeHolderKeys = getTranslatedPlaceHolderKeys();
    		
    		String identifier = placeholderResults.getIdentifier();
    		
    		if ( !identifier.startsWith( PlaceholderManager.PRISON_PLACEHOLDER_PREFIX_EXTENDED )) {
    			identifier = PlaceholderManager.PRISON_PLACEHOLDER_PREFIX_EXTENDED + identifier;
    		}
    		
    		// placeholder Attributes: 
    		PlaceholderManager pman = Prison.get().getPlaceholderManager();
    		String placeholder = pman.extractPlaceholderString( identifier );
    		PlaceholderAttribute attribute = pman.extractPlaceholderExtractAttribute( identifier );
    		
    		if ( placeholderResults.getPlaceholder() != null ) {
    			results = getTranslatePlayerPlaceHolder( playerUuid, playerName, placeholderResults.getPlaceholder(), attribute );
    		}
    		else {
    			// Need to hunt for the placeholder:
    			
    			for ( PlaceHolderKey placeHolderKey : placeHolderKeys ) {
    				if ( placeHolderKey.getKey().equalsIgnoreCase( placeholder )) {
    					results = getTranslatePlayerPlaceHolder( playerUuid, playerName, placeHolderKey, attribute );
    					break;
    				}
    			}
    		}
    		
    	}
    	
    	return results;
    }
    
    
    
    public String getTranslatePlayerPlaceHolder( UUID playerUuid, String playerName, 
    					PlaceHolderKey placeHolderKey, PlaceholderAttribute attribute ) {
		String results = null;

		if ( playerUuid != null ) {
			
			PrisonPlaceHolders placeHolder = placeHolderKey.getPlaceholder();
			
			String ladderName = placeHolderKey.getData();
			
			RankPlayer rankPlayer = getPlayer(playerUuid, playerName);
			
			if ( rankPlayer != null ) {
				
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
						results = getPlayerRankNumber( rankPlayer, ladderName, attribute );
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
						results = getPlayerNextRankCost( rankPlayer, ladderName, false, attribute );
						break;
						
					case prison_rcf:
					case prison_rankup_cost_formatted:
					case prison_rcf_laddername:
					case prison_rankup_cost_formatted_laddername:
						results = getPlayerNextRankCost( rankPlayer, ladderName, true, attribute );
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
						results = getPlayerNextRankCostBar( rankPlayer, ladderName, attribute );
						break;
						
					case prison_rcr:
					case prison_rankup_cost_remaining:
					case prison_rcr_laddername:
					case prison_rankup_cost_remaining_laddername:
						results = getPlayerNextRankCostRemaining( rankPlayer, ladderName, false, attribute );
						break;
						
					case prison_rcrf:
					case prison_rankup_cost_remaining_formatted:
					case prison_rcrf_laddername:
					case prison_rankup_cost_remaining_formatted_laddername:
						results = getPlayerNextRankCostRemaining( rankPlayer, ladderName, true, attribute );
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
						results = getPlayerBalance( rankPlayer, ladderName, false, attribute );
						break;
						
					case prison_psm:
					case prison_player_sellall_multiplier:
						results = getPlayerSellallMultiplier( rankPlayer, attribute );
						break;
						
					default:
						break;
				}
				
				if ( attribute != null && attribute instanceof PlaceholderAttributeText ) {
					PlaceholderAttributeText attributeText = (PlaceholderAttributeText) attribute;
					
					results = attributeText.format( results );
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
