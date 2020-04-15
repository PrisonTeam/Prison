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

import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.integration.EconomyCurrencyIntegration;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankLadder.PositionRank;
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

    /*
     * Constructor
     */

    /**
     * Instantiate this {@link RankManager}.
     */
    public RankManager(Collection collection) {
        this.collection = collection;
        this.loadedRanks = new ArrayList<>();
    }

    /*
     * Methods & Getters & Setters
     */

    /**
     * Loads a rank from a file into the loaded ranks list.
     * After this method is called, the rank will be ready for use in the server.
     *
     * @param rankFile The key that this rank is stored as. Case sensitive.
     * @throws IOException If the file could not be read or does not exist.
     */
    public void loadRank(String rankFile) throws IOException {
        Document document = collection.get(rankFile).orElseThrow(IOException::new);
        loadedRanks.add(new Rank(document));
    }

    /**
     * Loads every file within a directory with the extension ".rank.json".
     * If one file could not be loaded, it will simply be skipped.
     *
     * @throws IOException If the folder could not be found, or if a file could not be read or does not exist.
     */
    public void loadRanks() throws IOException {
        List<Document> ranks = collection.getAll();
        ranks.forEach(document -> loadedRanks.add(new Rank(document)));
    }

    /**
     * Saves a rank to its save file.
     *
     * @param rank     The {@link Rank} to save.
     * @param saveFile The key to write the rank as. Case sensitive.
     * @throws IOException If the rank could not be serialized, or if the rank could not be saved to the file.
     */
    public void saveRank(Rank rank, String saveFile) throws IOException {
        collection.save(saveFile, rank.toDocument());
    }

    /**
     * Saves a rank to its save file.
     *
     * @param rank The {@link Rank} to save.
     * @throws IOException If the rank could not be serialized, or if the rank could not be saved to the file.
     */
    public void saveRank(Rank rank) throws IOException {
        this.saveRank(rank, rank.filename());
    }

    /**
     * Saves all the loaded ranks to their own files within a directory.
     *
     * @throws IOException If the rankFolder does not exist, or if one of the ranks could not be saved.
     */
    public void saveRanks() throws IOException {
        for (Rank rank : loadedRanks) {
            saveRank(rank);
        }
    }

    /**
     * Creates a new rank with the specified parameters.
     * This new rank will be loaded, but will not be written to disk until {@link #saveRank(Rank, String)} is called.
     *
     * @param name The name of this rank, for use with the user (i.e. this will be shown to the user).
     * @param tag  The tag of this rank, which is used for prefixes/suffixes.
     * @param cost The cost of this rank, in whichever units the player chose (i.e. money or experience).
     * @return An optional containing either the {@link Rank} if it could be created, or empty
     * if the rank's creation failed.
     */
    public Optional<Rank> createRank(String name, String tag, double cost) {
        // Set the default values...
        Rank newRank = new Rank();
        newRank.id = getNextAvailableId();
        newRank.name = name;
        newRank.tag = tag;
        newRank.cost = cost;
        newRank.rankUpCommands = new ArrayList<>();

        // ... add it to the list...
        loadedRanks.add(newRank);
        
        // Reset the rank relationships:
        connectRanks();

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
        // Set the highest to -1 for now, since we'll add one at the end
        int highest = -1;

        // If anything's higher, it's now the highest...
        for (Rank rank : loadedRanks) {
            if (highest < rank.id) {
                highest = rank.id;
            }
        }

        return highest + 1;
    }

    /**
     * Returns the rank with the specified name.
     *
     * @param name The rank's name, case-sensitive.
     * @return An optional containing either the {@link Rank} if it could be found, or empty if it does not exist by the specified name.
     */
    public Optional<Rank> getRank(String name) {
        return loadedRanks.stream().filter(rank -> rank.name.equals(name)).findFirst();
    }

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
        for (RankLadder ladder : PrisonRanks.getInstance().getLadderManager()
            .getLaddersWithRank(rank.id)) {
            int next =
                Math.max(0, ladder.getPositionOfRank(rank) - 1); // either one less, or the bottom

            Optional<Rank> newRank = ladder.getByPosition(next);
            if (!newRank.isPresent()) {
                // TODO Do something here ... default rank!
                return false;
            }

            // Move each player in this ladder to the new rank
            PrisonRanks.getInstance().getPlayerManager().getPlayers().forEach(rankPlayer -> {
                if (rankPlayer.getRank(ladder).isPresent()) {
                    rankPlayer.removeRank(rankPlayer.getRank(ladder).get());
                    rankPlayer.addRank(ladder, newRank.get());
                    try {
                        PrisonRanks.getInstance().getPlayerManager().savePlayer(rankPlayer);
                    } catch (IOException e) {
                        Output.get().logError("Couldn't save player file.", e);
                    }
                    PrisonAPI.debug("Player %s is now %s", rankPlayer.uid.getLeastSignificantBits(),
                        newRank.get().name);
                }
            });
        }

        // ... remove it from each ladder it was in...
        final boolean[] success = {true};
        PrisonRanks.getInstance().getLadderManager().getLaddersWithRank(rank.id)
            .forEach(rankLadder -> {
                rankLadder.removeRank(rankLadder.getPositionOfRank(rank));
                try {
                    PrisonRanks.getInstance().getLadderManager().saveLadder(rankLadder);
                } catch (IOException e) {
                    success[0] = false;
                    Output.get().logError("Could not save ladder.", e);
                }
            });
        if(!success[0]) {
            return false;
        }

        // Remove it from the list...
        loadedRanks.remove(rank);
        
        // Reset the rank relationships:
        connectRanks();

        // ... and remove the rank's save files.
        collection.delete(rank.filename());
        return true;
    }

    /**
     * Returns the rank with the specified ID.
     *
     * @param id The rank's ID.
     * @return An optional containing either the {@link Rank} if it could be found, or empty if it does not exist by the specified id.
     */
    public Optional<Rank> getRank(int id) {
        return loadedRanks.stream().filter(rank -> rank.id == id).findFirst();
    }

    /**
     * Returns a list of all the loaded ranks on the server.
     *
     * @return A {@link List}. This will never return null, because if there are no loaded ranks, the list will just be empty.
     */
    public List<Rank> getRanks() {
        return loadedRanks;
    }

    /**
     * <p>This should be ran after the RanksManager and LadderManger are loaded.  Or any
     * time a rank is add, removed, or position changed within a ladder. 
     * </p>
     * 
     * <p>This function will set the temporal rankPrior and rankNext value in 
     * each rank based upon each ladder. This will greatly simplify walking the 
     * ladder by using the linked ranks without having to perform any expensive
     * calculations.
     * </p> 
     */
    public void connectRanks() {
    	LadderManager lman = PrisonRanks.getInstance().getLadderManager();
    	
    	for ( RankLadder rLadder : lman.getLadders() ) {
			
    		Rank rankLast = null;
    		for ( PositionRank pRank : rLadder.ranks ) {
				Rank rank = rLadder.getByPosition(pRank.getPosition()).get();
				if ( rankLast != null ) {
					rank.rankPrior = rankLast;
					rankLast.rankNext = rank;
				}
				rankLast = rank;
			}
		}
    }
    
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
			if ( rank.currency != null ) {
				EconomyCurrencyIntegration currencyEcon = PrisonAPI.getIntegrationManager()
						.getEconomyForCurrency( rank.currency );
				if ( currencyEcon == null ) {
					Output.get().logError( 
						String.format( "Economy Failure: &7The currency &a%s&7 was registered with " +
							"rank &a%s&7, but it isn't supported by any Economy integration.",
							rank.currency, rank.name) );
				}
			}
		}
    	
    }
}
