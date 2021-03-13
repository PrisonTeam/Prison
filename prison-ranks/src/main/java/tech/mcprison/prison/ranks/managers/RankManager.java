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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.integration.EconomyCurrencyIntegration;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.commands.CommandCommands;
import tech.mcprison.prison.ranks.commands.LadderCommands;
import tech.mcprison.prison.ranks.commands.RankUpCommand;
import tech.mcprison.prison.ranks.commands.RanksCommands;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.store.Collection;
import tech.mcprison.prison.store.Document;

/**
 * Manages the creation, removal, and management of ranks.
 *
 * @author Faizaan A. Datoo
 */
public class RankManager {

    /*
     * Fields & Constants
     */

    private Collection collection;
    
    private List<Rank> loadedRanks;
    private TreeMap<String, Rank> ranksByName;
    private TreeMap<Integer, Rank> ranksById;

    private  CommandCommands rankCommandCommands;
    private RanksCommands ranksCommands;
    private RankUpCommand rankupCommands;
    private LadderCommands ladderCommands;

    
    /*
     * Constructor
     */

    /**
     * Instantiate this {@link RankManager}.
     */
    public RankManager(Collection collection) {
        this.collection = collection;
        
        this.loadedRanks = new ArrayList<>();
        this.ranksByName = new TreeMap<>();
        this.ranksById = new TreeMap<>();
    }

    /*
     * Methods & Getters & Setters
     */
    
    private void addRank( Rank rank ) {
    	if ( rank != null ) {
    		getLoadedRanks().add( rank );
    		String rankName = rank.getName();
    		getRanksByName().put( rankName.toLowerCase(), rank );
    		getRanksById().put( rank.getId(), rank );
    	}
    }
    
    private void removeRankFromCollections( Rank rank ) {
    	if ( rank != null ) {
    		getLoadedRanks().remove( rank );
    		getRanksByName().remove( rank.getName().toLowerCase() );
    		getRanksById().remove( rank.getId() );
    	}
    	
    }

    
    
    /**
     * Loads a rank from a file into the loaded ranks list.
     * After this method is called, the rank will be ready for use in the server.
     *
     * @param rankFile The key that this rank is stored as. Case sensitive.
     * @throws IOException If the file could not be read or does not exist.
     */
    public void loadRank(String rankFile) throws IOException {
        Document document = collection.get(rankFile).orElseThrow(IOException::new);
        
        addRank( new Rank(document) );
//        loadedRanks.add(new Rank(document));
    }

    /**
     * Loads every file within a directory with the extension ".rank.json".
     * If one file could not be loaded, it will simply be skipped.
     *
     * @throws IOException If the folder could not be found, or if a file could not be read or does not exist.
     */
    public void loadRanks() throws IOException {
        List<Document> ranks = collection.getAll();
        
        for ( Document rankDocument : ranks ) {
        	Rank rank = new Rank( rankDocument );
        	addRank( rank );
		}
        
//        ranks.forEach(document -> addRank(new Rank(document)));
//        ranks.forEach(document -> loadedRanks.add(new Rank(document)));
    }

    /**
     * Saves a rank to its save file.
     *
     * @param rank     The {@link Rank} to save.
     * @param saveFile The key to write the rank as. Case sensitive.
     */
    public void saveRank(Rank rank, String saveFile) {
        collection.save(saveFile, rank.toDocument());
    }

    /**
     * Saves a rank to its save file.
     *
     * @param rank The {@link Rank} to save.
     */
    public void saveRank(Rank rank) {
        this.saveRank(rank, rank.filename());
    }

    /**
     * Saves all the loaded ranks to their own files within a directory.
     *
     */
    public void saveRanks() {
        for (Rank rank : loadedRanks) {
            saveRank(rank);
        }
    }

    /**
     * Creates a new rank with the specified parameters.
     * This new rank will be loaded, but will not be written to disk until 
     * {@link #saveRank(Rank, String)} is called.
     *
     * @param name The name of this rank, for use with the user (i.e. this will be shown to the user).
     * @param tag  The tag of this rank, which is used for prefixes/suffixes.
     * @param cost The cost of this rank, in whichever units the player chose (i.e. money or experience).
     * @return An optional containing either the {@link Rank} if it could be created, or empty
     * if the rank's creation failed.
     */
    public Optional<Rank> createRank(String name, String tag, double cost) {
    	
    	int position = getRanks().size(); 
    			
        // Set the default values...
        Rank newRank = new Rank( position, getNextAvailableId(), name, tag, cost );

        // ... add it to the list...
        addRank(newRank);
//        loadedRanks.add(newRank);
        
//        // Reset the rank relationships:
//        connectRanks();

        // ...and return it.
        return Optional.of(newRank);
    }

    /**
     * Returns the next available ID for a new rank.
     * This works by adding one to the highest current rank ID.
     *
     * @return The next available rank's ID.
     */
    private int getNextAvailableId() {
    	
    	int current = (getRanksById().size() == 0 ?
    				-1 : getRanksById().lastKey().intValue());
    	
    	return current + 1;
    	
//        // Set the highest to -1 for now, since we'll add one at the end
//        int highest = -1;
//
//        // If anything's higher, it's now the highest...
//        for (Rank rank : loadedRanks) {
//            if (highest < rank.id) {
//                highest = rank.id;
//            }
//        }
//
//        return highest + 1;
    }

    /**
     * <p>Returns the rank with the specified name.
     * </p>
     * 
     * <p>Deprecated: use the non-Optional getRank() instead.
     * </p>
     *
     * @param name The rank's name, case-sensitive.
     * @return An optional containing either the {@link Rank} if it could be found, or empty if it does not exist by the specified name.
     */
    @Deprecated 
    public Optional<Rank> getRankOptional(String name) {
        return loadedRanks.stream().filter(rank -> rank.getName().equals(name)).findFirst();
    }

    /**
     * <p>The preferred way to get a rank by name. This has better performance.
     * </p>
     * 
     * @param name
     * @return
     */
    public Rank getRank(String name) {
    	return getRanksByName().get( name.toLowerCase() );
    }
    
    
// Not used anywhere... 
//    /**
//     * Returns the first rank that has an escaped name that has the & replaced with -.
//     */
//    public Rank getRankEscaped(String name) {
//    	return loadedRanks.stream().filter(rank -> 
//    					rank.getName().replace( "&", "-" ).equals(name)).findFirst().orElse( null );
//    }

    /**
     * Removes the provided rank. This will go through the process of removing the rank from the loaded
     * ranks list, removing the rank's save files, adjusting the ladder positions that this rank is a part of,
     * and finally, moving the players back to the previous rank of their ladder. This is a potentially destructive
     * operation; be sure that you are using it in the correct manner.
     *
     * @param rank The {@link Rank} to be removed.
     * @return true if the rank was removed successfully, false otherwise.
     */
    public boolean removeRank(Rank rank) {
        // ... remove it from each user, bumping them down to the next lowest rank...
    	
    	final Rank newRank = ( rank.getRankPrior() != null ? 
    								rank.getRankPrior() : 
    									rank.getRankNext() );
    	if ( newRank == null ) {
    		 Output.get().logError("Remove Rank Warning: No fallback rank exists so players with " +
    		 		"the rank that is being removed will have no rank on that ladder.");
    	}

    	
    	final boolean[] success = {true};
        for (RankLadder ladder : PrisonRanks.getInstance().getLadderManager()
        						.getLaddersWithRank(rank.getId())) {
        	
            // Move each player in this ladder to the new rank
            PrisonRanks.getInstance().getPlayerManager().getPlayers().forEach(rankPlayer -> {
            	Rank curRank = rankPlayer.getRank(ladder.getName());
                if ( curRank != null && rank.equals( curRank ) ) {
                    rankPlayer.removeRank(curRank);
                    if ( newRank != null ) {
                    	rankPlayer.addRank(ladder, newRank);
                    }
                    
                    try {
                        PrisonRanks.getInstance().getPlayerManager().savePlayer(rankPlayer);
                    } catch (IOException e) {
                        Output.get().logError("RemoveRank: Couldn't save player file.", e);
                    }
                    PrisonAPI.debug("Player %s is now %s", rankPlayer.getName(),
                        newRank.getName());
                }
            });
            
            
            // ... remove it from each ladder it was in...
            ladder.removeRank(ladder.getPositionOfRank(rank));
            try {
            	PrisonRanks.getInstance().getLadderManager().saveLadder(ladder);
            } catch (IOException e) {
            	success[0] = false;
            	Output.get().logError("RemoveRank: Could not save ladder " + ladder.getName() + ".", e);
            }
            
        }

        if(!success[0]) {
            return false;
        }

        // Remove it from the list...
        removeRankFromCollections( rank );
//        loadedRanks.remove(rank);
        
//        // Reset the rank relationships:
//        connectRanks();

        // ... and remove the rank's save files.
        collection.delete(rank.filename());
        return true;
    }

//    /**
//     * Returns the rank with the specified ID.
//     *
//     * @param id The rank's ID.
//     * @return An optional containing either the {@link Rank} if it could be found, or empty if it does not exist by the specified id.
//     */
//    @Deprecated 
//    public Optional<Rank> getRankOptional(int id) {
//        return loadedRanks.stream().filter(rank -> rank.getId() == id).findFirst();
//    }

    public Rank getRank( int id ) {
    	return getRanksById().get( id );
    }
    
    /**
     * Returns a list of all the loaded ranks on the server.
     *
     * @return A {@link List}. This will never return null, because if there are no loaded ranks, the list will just be empty.
     */
    public List<Rank> getRanks() {
        return loadedRanks;
    }

//    /**
//     * <p>This should be ran after the RanksManager and LadderManger are loaded.  Or any
//     * time a rank is add, removed, or position changed within a ladder. 
//     * </p>
//     * 
//     * <p>This function will set the temporal rankPrior and rankNext value in 
//     * each rank based upon each ladder. This will greatly simplify walking the 
//     * ladder by using the linked ranks without having to perform any expensive
//     * calculations.
//     * </p> 
//     */
//    public void connectRanks() {
//    	LadderManager lman = PrisonRanks.getInstance().getLadderManager();
//    	
//    	for ( RankLadder rLadder : lman.getLadders() ) {
//			
//    		rLadder.getPositionRanks().sort(Comparator.comparingInt(PositionRank::getPosition));
//    		
//    		Rank rankLast = null;
//    		for ( PositionRank pRank : rLadder.getPositionRanks() ) {
//    			if ( pRank != null && pRank.getPosition() >= 0 ) {
//    				Optional<Rank> opRank = rLadder.getByPosition(pRank.getPosition());
//    				if ( opRank.isPresent() ) {
//    					Rank rank = opRank.get();
//    					
//    					// reset the rankPrior and rankNext in case there are no hookups:
//    					// Important if ranks are removed, or inserted, or moved:
//    					rank.setRankPrior( null );
//    					rank.setRankNext( null );
//    					
//    					if ( rankLast != null ) {
//    						rank.setRankPrior( rankLast );
//    						rankLast.setRankNext( rank );
//    					}
//    					rankLast = rank;
//    				}
//    			}
//			}
//		}
//    }
    
    /*
     * <p>This function will go through ranks and find ranks that have defined currencies.
     * When a currency is assigned to a rank, it is verified to be valid.  So in theory
     * all currencies "should" be valid.  But plugins and setting can change.  
     * </p>
     * 
     * <p>By hitting all currencies up front when prison first loads, it "registers" 
     * all currencies with all economies that support currencies.  This also allows
     * error reporting to happen upon prison start up to report lost currencies.
     * And it allows each economy plugin the chance to list all supported currencies.
     * </p>   
     * 
     * <p>This almost has to be proactive when prison loads, since there is no way to
     * poll the economy plugins to find out what currencies it supports.  At least its
     * not a feature for GemsEconomy.
     * </p>
     * 
     */
    public void identifyAllRankCurrencies() {
    	for ( Rank rank : loadedRanks ) {
			if ( rank.getCurrency() != null ) {
				EconomyCurrencyIntegration currencyEcon = PrisonAPI.getIntegrationManager()
						.getEconomyForCurrency( rank.getCurrency() );
				if ( currencyEcon == null ) {
					Output.get().logError( 
						String.format( "Economy Failure: &7The currency &a%s&7 was registered with " +
							"rank &a%s&7, but it isn't supported by any Economy integration.",
							rank.getCurrency(), rank.getName()) );
				}
			}
		}
    	
    }
    
    
    
    public String listAllRanks( String ladderName, List<Rank> ranks, boolean includeAll ) {
    	StringBuilder sb = new StringBuilder();
    	
    	PlayerManager playerManager = PrisonRanks.getInstance().getPlayerManager();
    	
    	for (Rank rank : ranks ) {
    		
    		// Get the players per rank!!
			List<RankPlayer> playersList =
                    playerManager.getPlayers().stream()
                        .filter(rankPlayer -> rankPlayer.getLadderRanks().values().contains(rank))
                        .collect(Collectors.toList());
    		int players = playersList.size();
    		
    		if ( includeAll || !includeAll && players > 0 ) {
    			if ( sb.length() > 0 ) {
    				sb.append( ", " );
    			}
    			
    			
    			sb.append( " " ).append( rank.getName() );
    			
    			if ( players > 0 ) {
    				
    				sb.append( " (" ).append( players ).append( " )" );
    			}
    		}
		}
    	
    	sb.insert( 0, ": " );
    	sb.insert( 0, ladderName );
    	sb.insert( 0, "&7  " );
    	
    	return sb.toString();
    }
    
    
    /**
     * <p>Sends the output of ranksByLadders to the prison Output (console and logs).
     * </p>
     * 
     * @param includeAll If true then includes all ranks, otherwise just ranks within one more players
     */
    public void ranksByLadders(  boolean includeAll ) {
    	ranksByLadders( null, "all", includeAll );
    }
    
    public void ranksByLadders( CommandSender sender, boolean includeAll ) {
    	ranksByLadders( sender, "all", includeAll );
    }
    
    public void ranksByLadders( CommandSender sender, String ladderName, boolean includeAll ) {
    	
    	rankByLadderOutput( sender, "&7Ranks by ladders:" );
    	
    	// Track which ranks were included in the ladders listed:
    	List<Rank> ranksIncluded = new ArrayList<>();
    	
    	for ( RankLadder ladder : PrisonRanks.getInstance().getLadderManager().getLadders() ) {
    		if ( ladderName.equalsIgnoreCase( "all" ) || ladderName.equalsIgnoreCase( ladder.getName() ) ) {
    			
    			List<Rank> ladderRanks = ladder.getRanks();
    			ranksIncluded.addAll( ladderRanks );
    			
    			String ranksByLadder = listAllRanks( ladder.getName(), ladderRanks, includeAll );
    			
    			rankByLadderOutput( sender, ranksByLadder );
    		}
    	}
    	
    	if ( ladderName.equalsIgnoreCase( "all" ) || ladderName.equalsIgnoreCase( "none" ) ) {

    		// Next we need to get a list of all ranks that were not included. Use set 
    		List<Rank> ranksExcluded = new ArrayList<>( loadedRanks );
    		ranksExcluded.removeAll( ranksIncluded );
    		
    		// Next generate a list of ranks that are not associated with any ladder:
    		// NOTE: No players should be associated with ranks that are not tied to a ladder,
    		//       so enable "true" for includeAll to list all ranks that are not tied to ladders
    		//       since player count will always be zero.
    		String ranksByLadder = listAllRanks( "none", ranksExcluded, true );
    		
    		rankByLadderOutput( sender, ranksByLadder );
    	}
    }

	private void rankByLadderOutput( CommandSender sender, String ranksByLadder ) {
		if ( sender == null ) {
			Output.get().logInfo( ranksByLadder );
		}
		else {
			sender.sendMessage( ranksByLadder );
		}
	}

	
	
	
	
	private List<Rank> getLoadedRanks() {
		return loadedRanks;
	}

	private TreeMap<String, Rank> getRanksByName() {
		return ranksByName;
	}

	private TreeMap<Integer, Rank> getRanksById() {
		return ranksById;
	}
	
	
	public CommandCommands getRankCommandCommands() {
		return rankCommandCommands;
	}
	public void setRankCommandCommands( CommandCommands rankCommandCommands ) {
		this.rankCommandCommands = rankCommandCommands;
	}

	public RanksCommands getRanksCommands() {
		return ranksCommands;
	}
	public void setRanksCommands( RanksCommands ranksCommands ) {
		this.ranksCommands = ranksCommands;
	}

	public RankUpCommand getRankupCommands() {
		return rankupCommands;
	}
	public void setRankupCommands( RankUpCommand rankupCommands ) {
		this.rankupCommands = rankupCommands;
	}

	public LadderCommands getLadderCommands() {
		return ladderCommands;
	}
	public void setLadderCommands( LadderCommands ladderCommands ) {
		this.ladderCommands = ladderCommands;
	}
    
}
