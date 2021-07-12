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
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.integration.EconomyCurrencyIntegration;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.localization.Localizable;
import tech.mcprison.prison.modules.ModuleElement;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.placeholders.ManagerPlaceholders;
import tech.mcprison.prison.placeholders.PlaceHolderKey;
import tech.mcprison.prison.placeholders.PlaceholderAttribute;
import tech.mcprison.prison.placeholders.PlaceholderAttributeNumberFormat;
import tech.mcprison.prison.placeholders.PlaceholderAttributeText;
import tech.mcprison.prison.placeholders.PlaceholderManager;
import tech.mcprison.prison.placeholders.PlaceholderManager.PlaceholderFlags;
import tech.mcprison.prison.placeholders.PlaceholderManager.PrisonPlaceHolders;
import tech.mcprison.prison.placeholders.PlaceholdersUtil;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.commands.CommandCommands;
import tech.mcprison.prison.ranks.commands.LadderCommands;
import tech.mcprison.prison.ranks.commands.RankUpCommand;
import tech.mcprison.prison.ranks.commands.RanksCommands;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.data.StatsRankPlayerBalanceData;
import tech.mcprison.prison.store.Collection;
import tech.mcprison.prison.store.Document;

/**
 * Manages the creation, removal, and management of ranks.
 *
 * @author Faizaan A. Datoo
 */
public class RankManager 
	implements ManagerPlaceholders {

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

    private List<PlaceHolderKey> translatedPlaceHolderKeys;
    
    public enum RanksByLadderOptions {
    	playersOnly("players"),
    	allRanks("all"),
    	full;
    	
    	
    	private final String altName;
    	
    	private RanksByLadderOptions() {
    		this.altName = null;
    	}
    	
    	private RanksByLadderOptions( String altName ) {
    		this.altName = altName;
    	}
    	
    	public static RanksByLadderOptions fromString( String value ) {
    		RanksByLadderOptions results = null;
    		
    		for ( RanksByLadderOptions opt : values() ) {
				if ( opt.name().equalsIgnoreCase( value ) || 
						opt.getAltName() != null && opt.getAltName().equalsIgnoreCase( value )) {
					results = opt;
					break;
				}
			}
    		
    		return results;
    	}

		public String getAltName() {
			return altName;
		}
    	
    }
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

    
    private void addRank( Rank rank ) {
    	if ( rank != null ) {
    		getLoadedRanks().add( rank );
    		String rankName = rank.getName();
    		getRanksByName().put( rankName.toLowerCase(), rank );
    		getRanksById().put( rank.getId(), rank );
    		
    		// Do not have to reset position number since the new rank is 
    		// added to the end of the ladder's rank List, and therefore 
    		// no other rank is impacted.
    	}
    }
    
    private void removeRankFromCollections( Rank rank ) {
    	if ( rank != null ) {
    		getLoadedRanks().remove( rank );
    		getRanksByName().remove( rank.getName().toLowerCase() );
    		getRanksById().remove( rank.getId() );
    		
    		// Since the removal of a rank could shift the position of
    		// more than one rank, then all positions should be reset.
    		
    		resetRankPositions( rank );
    	}
    	
    }

    
    private void resetRankPositions( Rank rank ) {
    	
    	for ( Rank r : rank.getLadder().getRanks() ) {
			r.resetPosition();
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
        	Localizable localManagerLog = PrisonRanks.getInstance().getRanksMessages()
        			.getLocalizable( "ranks_rankManager__remove_rank_warning" );
        	
        	Output.get().logError( localManagerLog.localize() );
    	}

    	
    	boolean success = true;
    	
    	RankLadder ladder = rank.getLadder();
    	
//        for (RankLadder ladder : PrisonRanks.getInstance().getLadderManager() .getLadder( rank )) 
        {
        	
            // Move each player in this ladder to the new rank
            PrisonRanks.getInstance().getPlayerManager().getPlayers().forEach(rankPlayer -> {
            	Rank curRank = rankPlayer.getRank(ladder.getName());
                if ( curRank != null && rank.equals( curRank ) ) {
                    rankPlayer.removeRank(curRank);
                    if ( newRank != null ) {
                    	rankPlayer.addRank(newRank);
                    }
                    
                    PrisonRanks.getInstance().getPlayerManager().savePlayer(rankPlayer);
//                    try {
//                    } catch (IOException e) {
//                    	Localizable localManagerLog = PrisonRanks.getInstance().getRanksMessages()
//                    			.getLocalizable( "ranks_rankManager__cannot_save_player_file" );
//                    	
//                    	Output.get().logError( localManagerLog.localize() );
//                    }
                    
                    Localizable localManagerLog = PrisonRanks.getInstance().getRanksMessages()
                    		.getLocalizable( "ranks_rankManager__cannot_save_player_file" )
                    		.withReplacements( 
                    					rankPlayer.getName(),
                    					newRank.getName() );
                    PrisonAPI.debug( localManagerLog.localize() );
                }
            });
            
            
            // ... remove it from each ladder it was in...
            ladder.removeRank( rank );
//            ladder.removeRank(ladder.getPositionOfRank(rank));
            if ( !PrisonRanks.getInstance().getLadderManager().save(ladder) ) {

            	success = false;
            	
            	Localizable localManagerLog = PrisonRanks.getInstance().getRanksMessages()
            			.getLocalizable( "ranks_rankManager__cannot_save_ladder_file" )
            			.withReplacements( ladder.getName() );
            	
            	Output.get().logError( localManagerLog.localize() );
            }
            
        }

        if( success ) {

        	// Remove it from the list...
        	removeRankFromCollections( rank );
        	
        	// Reset the rank relationships:
        	// connectRanks();
        	
        	// ... and remove the rank's save files.
        	collection.delete(rank.filename());
        }

        return success;
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
    public void identifyAllRankCurrencies( List<String> prisonStartupDetails ) {
    	for ( Rank rank : loadedRanks ) {
			if ( rank.getCurrency() != null ) {
				EconomyCurrencyIntegration currencyEcon = PrisonAPI.getIntegrationManager()
						.getEconomyForCurrency( rank.getCurrency() );
				if ( currencyEcon == null ) {
					
					Localizable localManagerLog = PrisonRanks.getInstance().getRanksMessages()
	            			.getLocalizable( "ranks_rankManager__failure_no_economy" )
	            			.withReplacements( rank.getCurrency(), rank.getName() );
	            	
	            	Output.get().logError( localManagerLog.localize() );
					
					prisonStartupDetails.add( localManagerLog.localize() );
				}
			}
		}
    	
    }
    
    
    
    public String listAllRanks( String ladderName, List<Rank> ranks, RanksByLadderOptions option ) {
    	StringBuilder sb = new StringBuilder();
    	
//    	PlayerManager playerManager = PrisonRanks.getInstance().getPlayerManager();
    	
    	for (Rank rank : ranks ) {
    		
    		int players = rank.getPlayers().size();
    		
    		// Get the players per rank!!
//			List<RankPlayer> playersList =
//                    playerManager.getPlayers().stream()
//                        .filter(rankPlayer -> rankPlayer.getLadderRanks().values().contains(rank))
//                        .collect(Collectors.toList());
//    		int players = playersList.size();
    		
    		if ( option == RanksByLadderOptions.allRanks || 
    					option == RanksByLadderOptions.full || players > 0 ) {
    			if ( sb.length() > 0 ) {
    				sb.append( ", " );
    			}
    			
    			
    			sb.append( " " ).append( rank.getName() );
    			
    			if ( players > 0 ) {
    				
    				sb.append( " (" ).append( players ).append( ")" );
    				
    				if ( option == RanksByLadderOptions.full ) {
    					sb.append( "[" );
    					
    					for ( RankPlayer rankPlayer : rank.getPlayers() )
						{
    						if ( rankPlayer.getName() != null ) {
    							
    							sb.append( rankPlayer.getName() ).append( " " );
    						}
						}
    					
    					// if last character is a space, then remove it:
    					if ( sb.charAt( sb.length() - 1 ) == ' ' ) {
    						sb.setLength( sb.length() - 1 );
    					}
    					sb.append( "]" );
    				}
    				
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
    public void ranksByLadders(  RanksByLadderOptions option ) {
    	ranksByLadders( null, "all", option );
    }
    
    public void ranksByLadders( CommandSender sender, RanksByLadderOptions option ) {
    	ranksByLadders( sender, "all", option );
    }
    
    public void ranksByLadders( CommandSender sender, String ladderName, RanksByLadderOptions option ) {
    	
    	Localizable localManagerLog = PrisonRanks.getInstance().getRanksMessages()
    			.getLocalizable( "ranks_rankManager__ranks_by_ladders" );
    	
    	rankByLadderOutput( sender, localManagerLog.localize() );
    	
    	// Track which ranks were included in the ladders listed:
    	List<Rank> ranksIncluded = new ArrayList<>();
    	
    	for ( RankLadder ladder : PrisonRanks.getInstance().getLadderManager().getLadders() ) {
    		if ( ladderName.equalsIgnoreCase( "all" ) || ladderName.equalsIgnoreCase( ladder.getName() ) ) {
    			
    			List<Rank> ladderRanks = ladder.getRanks();
    			ranksIncluded.addAll( ladderRanks );
    			
    			String ranksByLadder = listAllRanks( ladder.getName(), ladderRanks, option );
    			
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
    		// Update: Set the RanksByLadderOptions to full
    		String ranksByLadder = listAllRanks( "none", ranksExcluded, RanksByLadderOptions.full );
    		
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


	private String getRankCost( Rank rank, PlaceholderAttribute attribute, boolean formatted )
	{
		double cost = rank.getCost();
		
		String resultsx = null;
		DecimalFormat dFmt = new DecimalFormat("#,##0");
		if ( attribute != null && attribute instanceof PlaceholderAttributeNumberFormat ) {
			PlaceholderAttributeNumberFormat attributeNF = 
											(PlaceholderAttributeNumberFormat) attribute;
			resultsx = attributeNF.format( cost );
		}
		else  if ( formatted ) {
			resultsx =  PlaceholdersUtil.formattedMetricSISize( cost );
		}
		else {
			resultsx = dFmt.format( cost );
		}
		return resultsx;
	}
	
	
    public String getTranslateRankPlayersPlaceHolder( UUID playerUuid, String playerName, String identifier ) {
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
    				results = getTranslateRankPlayersPlaceHolder( playerUuid, playerName, placeHolderKey, attribute );
    				break;
    			}
    		}
    	}
    	
    	return results;
    }
	
	public String getTranslateRanksPlaceHolder( String identifier ) {
    	String results = null;
    	List<PlaceHolderKey> placeHolderKeys = getTranslatedPlaceHolderKeys();
    	
		if ( !identifier.startsWith( PlaceholderManager.PRISON_PLACEHOLDER_PREFIX_EXTENDED )) {
			identifier = PlaceholderManager.PRISON_PLACEHOLDER_PREFIX_EXTENDED + identifier;
		}
    	
		// placeholder Attributes:
		PlaceholderManager pman = Prison.get().getPlaceholderManager();
		String placeholder = pman.extractPlaceholderString( identifier );
		//PlaceholderAttribute attribute = pman.extractPlaceholderExtractAttribute( identifier );
		
    	for ( PlaceHolderKey placeHolderKey : placeHolderKeys ) {
			if ( placeHolderKey.getKey().equalsIgnoreCase( placeholder )) {
				
				//Mine mine = getMine( placeHolderKey.getData() );
				
				results = getTranslateRanksPlaceHolder( placeHolderKey, identifier );
				break;
			}
		}
    	
    	return results;
    }
	
	public String getTranslateRanksPlaceHolder( PlaceHolderKey placeHolderKey, String identifier ) {
    	String results = null;
    	
    	if ( identifier == null ) {
    		identifier = placeHolderKey.getKey();
    	}
 	
    	if ( placeHolderKey != null && identifier != null ) {

    		if ( !identifier.startsWith( PlaceholderManager.PRISON_PLACEHOLDER_PREFIX_EXTENDED )) {
    			identifier = PlaceholderManager.PRISON_PLACEHOLDER_PREFIX_EXTENDED + identifier;
    		}
     
    		// placeholder Attributes:
    		PlaceholderManager pman = Prison.get().getPlaceholderManager();
    		//String placeholder = pman.extractPlaceholderString( identifier );
    		PlaceholderAttribute attribute = pman.extractPlaceholderExtractAttribute( identifier );
    		
    		
    		String rankName = placeHolderKey.getData();
    		Rank rank = PrisonRanks.getInstance().getRankManager().getRank( rankName );

    		
    		results = getTranslateRanksPlaceHolder( placeHolderKey, rank, attribute );
    	}
    	
    	return results;
    }
    
    public String getTranslateRanksPlaceHolder( PlaceHolderKey placeHolderKey, 
    				Rank rank, PlaceholderAttribute attribute ) {
		String results = null;

		PrisonPlaceHolders placeHolder = placeHolderKey.getPlaceholder();
		
		DecimalFormat dFmt = new DecimalFormat("#,##0");
		
		if ( rank != null ) {
			
			switch ( placeHolder ) {
				
				
				case prison_rank__name_rankname:
				case prison_r_n_rankname:
					results = rank.getName();
					break;
					
				case prison_rank__tag_rankname:
				case prison_r_t_rankname:
					results = rank.getTag();
					break;
					
				case prison_rank__ladder_rankname:
				case prison_r_l_rankname:
					results = rank.getLadder() == null ? "" : rank.getLadder().getName();
					break;
					
				case prison_rank__ladder_position_rankname:
				case prison_r_lp_rankname:
					results = Integer.toString( rank.getPosition() );
					break;
					
				case prison_rank__cost_rankname:
				case prison_r_c_rankname:
					results = getRankCost( rank, attribute, false );
					break;
					
				case prison_rank__cost_formatted_rankname:
				case prison_r_cf_rankname:
					results = getRankCost( rank, attribute, true );
					break;
					
				case prison_rank__currency_rankname:
				case prison_r_cu_rankname:
					results = rank.getCurrency() == null ? "default" : rank.getCurrency();
					break;
					
				case prison_rank__id_rankname:
				case prison_r_id_rankname:
					results = Integer.toString( rank.getId() );
					break;
					
				case prison_rank__player_count_rankname:
				case prison_r_pc_rankname:
					List<RankPlayer> players =
					PrisonRanks.getInstance().getPlayerManager().getPlayers().stream()
					.filter(rPlayer -> rPlayer.getLadderRanks().values().contains(rank))
					.collect(Collectors.toList());
					
					results = Integer.toString( players.size() );
					break;
					
				case prison_rank__linked_mines_rankname:
				case prison_r_lm_rankname:
					StringBuilder sb = new StringBuilder();
					for ( ModuleElement mine : rank.getMines() ) {
						if ( sb.length() > 0 ) {
							sb.append( ", " );
						}
						sb.append( mine.getName() );
					}
					
					results = sb.toString();
					break;
					
					
				case prison_top_rank_balance_name_nnn_rankname: 
				case prison_trbn_nnn_rankname:
					{
						StatsRankPlayerBalanceData stats = rank.getStatsPlayerBlance().getTopStats( 1 );
						if ( stats != null ) {
							
							results = stats.getPlayer() == null ? "" : stats.getPlayer().getName();
						}
						else {
							results = "";
						}
					}
					
					break;
					
				case prison_top_rank_balance_score_nnn_rankname:
				case prison_trbs_nnn_rankname:
					{
						StatsRankPlayerBalanceData stats = rank.getStatsPlayerBlance().getTopStats( 1 );
						if ( stats != null ) {
							
							results = dFmt.format( stats.getScore());
						}
						else {
							results = "";
						}
					}
					
					break;
					
				case prison_top_rank_balance_balance_nnn_rankname:
				case prison_trbb_nnn_rankname:
					{
						StatsRankPlayerBalanceData stats = rank.getStatsPlayerBlance().getTopStats( 1 );
						if ( stats != null ) {
							
							results = stats.getPlayer() == null ? "" :
										dFmt.format( stats.getPlayer().getBalance( rank.getCurrency()) );
						}
						else {
							results = "";
						}
					}
					
					break;
					

				default:
					break;
			}
			
			if ( attribute != null && attribute instanceof PlaceholderAttributeText ) {
				PlaceholderAttributeText attributeText = (PlaceholderAttributeText) attribute;
				
				results = attributeText.format( results );
			}
		}
		
		return results;
    }

    
    public String getTranslateRankPlayersPlaceHolder(UUID playerUuid, String playerName, 
    		PlaceHolderKey placeHolderKey, PlaceholderAttribute attribute) {
    	String results = null;

		PrisonPlaceHolders placeHolder = placeHolderKey.getPlaceholder();
		
    	String rankName = placeHolderKey.getData();
		Rank rank = PrisonRanks.getInstance().getRankManager().getRank( rankName );
		
		
		PlayerManager pm = PrisonRanks.getInstance().getPlayerManager();
		RankPlayer rankPlayer = pm.getPlayer(playerUuid, playerName);
		
		DecimalFormat dFmt = new DecimalFormat("#,##0");
		
		if ( rank != null && rankPlayer != null ) {
			
			switch ( placeHolder ) {
				
				case prison_rank__player_cost_rankname:
				case prison_r_pcst_rankname:
					{
						double cost = calclateRankCost( rankPlayer, rank );
						
						if ( attribute != null && attribute instanceof PlaceholderAttributeNumberFormat ) {
    						PlaceholderAttributeNumberFormat attributeNF = 
    								(PlaceholderAttributeNumberFormat) attribute;
    						results = attributeNF.format( cost );
    					}
						else {
							
							results = dFmt.format( cost );
						}
					}
					break;
					
				case prison_rank__player_cost_formatted_rankname:
				case prison_r_pcf_rankname:
					{
						double cost = calclateRankCost( rankPlayer, rank );
						
						if ( attribute != null && attribute instanceof PlaceholderAttributeNumberFormat ) {
    						PlaceholderAttributeNumberFormat attributeNF = 
    								(PlaceholderAttributeNumberFormat) attribute;
    						results = attributeNF.format( cost );
    					}
						else {
							
							results = PlaceholdersUtil.formattedMetricSISize( cost );
						}
					}
					break;
				
					
				case prison_rank__player_cost_remaining_rankname:
				case prison_r_pcr_rankname:
					{
						double cost = calclateRankCost( rankPlayer, rank );
						double balance = rankPlayer.getBalance( rank.getCurrency() );
//						double balance = pm.getPlayerBalance( rankPlayer, rank);
						
						double remaining = cost - balance;
						
						if ( remaining < 0 ) {
    						remaining = 0;
    					}
						
						if ( attribute != null && attribute instanceof PlaceholderAttributeNumberFormat ) {
    						PlaceholderAttributeNumberFormat attributeNF = 
    								(PlaceholderAttributeNumberFormat) attribute;
    						results = attributeNF.format( remaining );
    					}
						else {
							
							results = dFmt.format( remaining );
						}
					}
					break;
					
				case prison_rank__player_cost_remaining_formatted_rankname:
				case prison_r_pcrf_rankname:
					{
						double cost = calclateRankCost( rankPlayer, rank );
						double balance = rankPlayer.getBalance( rank.getCurrency() );
//						double balance = pm.getPlayerBalance( rankPlayer, rank);
						
						double remaining = cost - balance;
						
						if ( remaining < 0 ) {
    						remaining = 0;
    					}
						
						if ( attribute != null && attribute instanceof PlaceholderAttributeNumberFormat ) {
    						PlaceholderAttributeNumberFormat attributeNF = 
    								(PlaceholderAttributeNumberFormat) attribute;
    						results = attributeNF.format( remaining );
    					}
						else {
							
							results = PlaceholdersUtil.formattedMetricSISize( remaining );
						}

					}
					break;
					
					
					
					
				case prison_rank__player_cost_percent_rankname:
				case prison_r_pcp_rankname:
					{
						double cost = calclateRankCost( rankPlayer, rank );
						double balance = rankPlayer.getBalance( rank.getCurrency() );
//						double balance = pm.getPlayerBalance( rankPlayer, rank);
						
						double percent = (balance < 0 ? 0 : 
    						(cost == 0.0d || balance > cost ? 100.0 : 
    							balance / cost * 100.0 )
    							);
    					results = dFmt.format( percent );
					}
					break;
					
				case prison_rank__player_cost_bar_rankname:
				case prison_r_pcb_rankname:
					{
						double cost = calclateRankCost( rankPlayer, rank );
						double balance = rankPlayer.getBalance( rank.getCurrency() );
//						double balance = pm.getPlayerBalance( rankPlayer, rank);
						
						results = Prison.get().getPlaceholderManager().
										getProgressBar( balance, cost, false, attribute );
					}
					break;
					
					
				default:
					break;
			}
			
			if ( attribute != null && attribute instanceof PlaceholderAttributeText ) {
				PlaceholderAttributeText attributeText = (PlaceholderAttributeText) attribute;
				
				results = attributeText.format( results );
			}
		}
		
		return results;
    }

	private double calclateRankCost( RankPlayer rankPlayer, Rank rank )
	{
		double cost = 0;
		// Get player's rank:
		Rank playerRank = rankPlayer.getRank( rank.getLadder() );
		if ( playerRank != null ) {
			
			if ( rank.getPosition() < playerRank.getPosition() ) {
				cost = 0;
			}
			else {
				cost = playerRank.getCost();
				Rank nextRank = playerRank;
				
				while ( nextRank != null &&
						nextRank.getPosition() < rank.getPosition() ) {
					
					cost += playerRank.getCost();
					nextRank = nextRank.getRankNext();
				}
			}
		}
		return cost;
	}


    
	@Override
    public List<PlaceHolderKey> getTranslatedPlaceHolderKeys() {
    	if ( translatedPlaceHolderKeys == null ) {
    		translatedPlaceHolderKeys = new ArrayList<>();
    		
    		// This generates all the placeholders for all ranks:
    		List<PrisonPlaceHolders> placeHolders = PrisonPlaceHolders.getTypes( PlaceholderFlags.RANKS );

    		placeHolders.addAll( PrisonPlaceHolders.getTypes( PlaceholderFlags.RANKPLAYERS ) );
    		
    		placeHolders.addAll( PrisonPlaceHolders.getTypes( PlaceholderFlags.STATSRANKS ) );
    		
    		
    		List<Rank> ranks = PrisonRanks.getInstance().getRankManager().getRanks();
    		for ( Rank rank : ranks ) {
    			for ( PrisonPlaceHolders ph : placeHolders ) {
    				
    				String rankName = rank.getName().toLowerCase();
    				
    				String key = ph.name().replace( 
    						PlaceholderManager.PRISON_PLACEHOLDER_RANKNAME_SUFFIX, "_" + rankName ).
    							toLowerCase();
    				
    				PlaceHolderKey placeholder = new PlaceHolderKey(key, ph, rankName );
    				if ( ph.getAlias() != null ) {
    					String aliasName = ph.getAlias().name().replace( 
    							PlaceholderManager.PRISON_PLACEHOLDER_RANKNAME_SUFFIX, "_" + rankName ).
    								toLowerCase();
    					placeholder.setAliasName( aliasName );
    				}
    				translatedPlaceHolderKeys.add( placeholder );
    				
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
