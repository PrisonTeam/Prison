package tech.mcprison.prison.ranks.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.file.FileIOData;
import tech.mcprison.prison.file.JsonFileIO;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.tasks.TopNPlayerUpdateAsyncTask;

public class TopNPlayers
	implements FileIOData {

	private static TopNPlayers instance = null;
	
	public static final transient String PATH__TOP_N_PLAYERS = "data_storage";
	public static final transient String FILE_NAME__TOP_N_PLAYERS_JSON = "prisonTopN.json";
	
	public static final transient long DELAY_THIRTY_SECONDS_TICKS = 20 * 30;
	public static final transient long INTERVAL_FIVE_MINUTES_TICKS = 20 * 60 * 5;
	
	public static final long ONE_DAY_MS = 24 * 60 * 60 * 1000; // 1 day in ms

	public static final long ARCHIVE_CUTOFF_DAYS = 90; // 90 days
	
	private transient long archiveCutoffDaysMS;
	
	public transient File saveFile = null;
	
	private ArrayList<TopNPlayersData> topNList;
	private transient TreeMap<String, TopNPlayersData> topNMap;
	
	private ArrayList<TopNPlayersData> archivedList;
	private transient TreeMap<String, TopNPlayersData> archivedMap;
	
	private transient boolean calculatedRankScores = false;
	
	private transient boolean dirty = false;
	
	private TopNPlayers() {
		super();
		
		this.topNList = new ArrayList<>();
		this.topNMap = new TreeMap<>();
		
		this.archivedList = new ArrayList<>();
		this.archivedMap = new TreeMap<>();
		
		this.dirty = false;
		
		this.archiveCutoffDaysMS = 
				ONE_DAY_MS * Prison.get().getPlatform().getConfigLong( 
					"topNPlayers.archive.cutoff-days", ARCHIVE_CUTOFF_DAYS );
		
//		launchTopNPlayerUpdateAsyncTask();
		
	}
	
	public static TopNPlayers getInstance() {
		
		if ( instance == null ) {
			synchronized ( TopNPlayers.class ) {
				if ( instance == null ) {
					instance = new TopNPlayers();
					
					instance.loadSaveFile();
					
					instance.launchTopNPlayerUpdateAsyncTask();
				}
			}
		}
		
		return instance;
	}

	/**
	 * <p>The PlayerState will identify 
	 * @author Blue
	 *
	 */
	public enum PlayerState {
//		unknown,
		offline,
		online,
		archived;
	}
	
	public File getSaveFile() {
		if ( this.saveFile == null ) {
			
			File directory = new File( Prison.get().getDataFolder(), PATH__TOP_N_PLAYERS );
			
			this.saveFile = new File( directory, FILE_NAME__TOP_N_PLAYERS_JSON );
		}
		return saveFile;
	}
	
	private void launchTopNPlayerUpdateAsyncTask() {
		
		Long delayTicks = Prison.get().getPlatform().getConfigLong( 
						"topNPlayers.refresh.delay-ticks", DELAY_THIRTY_SECONDS_TICKS );
		Long intervalTicks = Prison.get().getPlatform().getConfigLong( 
						"topNPlayers.refresh.interval-ticks", INTERVAL_FIVE_MINUTES_TICKS );

		
		TopNPlayerUpdateAsyncTask.submitTaskTimerAsync( this, delayTicks, intervalTicks );
	}
	
	/**
	 * <p>Upon server startup, in an asynch thread, this function should be called
	 * to load the saved data from the file system.  If there is no saved data,
	 * then this function will access the PlayerManager and build an initial 
	 * collection from the existing players.
	 * </p>
	 * 
	 * <p>If any changes were made to any collections, or preexisting topN entries,
	 * then this class will be marked as <b>dirty</b> so this loader would know that
	 * it needs to update the save file.  It will then save it changes.
	 * </p>
	 */
	public void loadSaveFile() {
		JsonFileIO jfio = new JsonFileIO();
		
		TopNPlayers temp = (TopNPlayers) jfio.readJsonFile( getSaveFile(), this );
		
		if ( temp != null && 
				(temp.getTopNList().size() > 0 || 
				 temp.getArchivedList().size() > 0 )) {
			
			// Load from file was successful!
			setTopNList( temp.getTopNList() );
			setTopNMap( temp.getTopNMap() );
			setArchivedList( temp.getArchivedList() );
			setArchivedMap( temp.getArchivedMap() );

			// Since loading from a file, some players may now need to be archived:
			checkArchives();
		}
		else {
			// load from file was not successful, probably because there is no file.
			// So create a new collection of players from the PlayerManager:
			List<RankPlayer> players = PrisonRanks.getInstance().getPlayerManager().getPlayers();
			
			for (RankPlayer rankPlayer : players) {
				
				addPlayerData( rankPlayer );
			}
			
			// Do not need to check archives since the last seen date is processed 
			// when adding the player data.
		}
		
		
		// Sort:
		sortTopN();
		
		
		if ( isDirty() ) {
			saveToJson();
		}
	}
	
	public void saveToJson() {
		JsonFileIO jfio = new JsonFileIO();

		jfio.saveJsonFile( getSaveFile(), this );
	}

	private void checkArchives() {
		
		ArrayList<TopNPlayersData> temp = new ArrayList<>();
		
		long archiveDate = System.currentTimeMillis() - archiveCutoffDaysMS;
		
		// Locate the entries that need to be archived:
		for ( TopNPlayersData topN : topNList ) {
			if ( topN.getLastSeen() < archiveDate ) {
				temp.add(topN);
			}
		}
		
		// Now move them to the archived state:
		for (TopNPlayersData topN : temp) {
			
			// remove from list and map:
			getTopNList().remove(topN);
			getTopNMap().remove( topN.getKey() );
			
			// Change the status:
			topN.setPlayerState( PlayerState.archived );
			
			getArchivedList().add(topN);
			getArchivedMap().put( topN.getKey(), topN );
		}
		
		if ( temp.size() > 0 ) {
			
			setDirty( true );
		}
		
	}
	

	/**
	 * <p>This adds the topN player data. This player may already be in the 
	 * collection, so if they are, then this is treated more like an update.
	 * </p>
	 * 
	 * @param topN
	 */
	private void addPlayerData( TopNPlayersData topN ) {
		
		long archiveDate = System.currentTimeMillis() - archiveCutoffDaysMS;
		
		// First remove the player from all collections since it will be added back.
		// Since the last seen date may have changed, it may be added to a different
		// collection, hence why it needs to be first removed.
		if ( getTopNMap().containsKey( topN.getKey() ) ) {
			
			TopNPlayersData temp = getTopNMap().remove( topN.getKey() );
			getTopNList().remove( temp );

			setDirty( true );
		}
		
		if ( getArchivedMap().containsKey( topN.getKey() ) ) {
			
			TopNPlayersData temp = getArchivedMap().remove( topN.getKey() );
			getArchivedList().remove( temp );

			setDirty( true );
		}
		
		
		if ( topN.getLastSeen() < archiveDate ) {
			topN.setPlayerState( PlayerState.archived );
			
			getArchivedList().add(topN);
			getArchivedMap().put( topN.getKey(), topN );

			setDirty( true );
		}
		else {
			
			// Add the playerState of offline.  When refreshing calcs and
			// accessing online players, then change to online only when they are.
			topN.setPlayerState( PlayerState.offline );
			
			getTopNList().add(topN);
			getTopNMap().put( topN.getKey(), topN );

			setDirty( true );
		}

	}
	
	public void refreshAndSort() {
		
		if ( !calculatedRankScores ) {
			
			calculateAllRankScores( getTopNList() );
			calculateAllRankScores( getArchivedList() );
			
			calculatedRankScores = true;
		}
		
		// Get online players:
		List<Player> onlinePlayer = Prison.get().getPlatform().getOnlinePlayers();
		
		// Set all topNList entries to offline:
		for ( TopNPlayersData topN : topNList ) {
			if ( topN.getPlayerState() == PlayerState.online ) {
				topN.setPlayerState( PlayerState.offline );
			}
		}
		
		// Apply online only to online players:
		for (Player player : onlinePlayer) {
			
			RankPlayer rPlayer = PrisonRanks.getInstance().getPlayerManager().getPlayer(player);
			
			// Recalculate rankScore:
			rPlayer.calculateRankScore();

			TopNPlayersData topN = null;
			
			String key = player.getPlayerFileName();
			if ( getTopNMap().containsKey(key) ) {
				topN = getTopNMap().get(key);
				
				if ( topN.getrPlayer() == null ) {
					topN.setrPlayer( rPlayer );
				}

				topN.updateRankPlayer( rPlayer );
			}
			else {
				// Player is online, but yet they are not in the topN:
				topN = new TopNPlayersData( rPlayer );
			}
			
			// Set last seen date:
			topN.setLastSeen( System.currentTimeMillis() );
			
			addPlayerData( topN );
			
			topN.setPlayerState( PlayerState.online );
			
			
			setDirty( true );
			
		}
		
		ArrayList<TopNPlayersData> newTopNList = new ArrayList<>();
		newTopNList.addAll( getTopNMap().values() );
		
		ArrayList<TopNPlayersData> newArchivedList = new ArrayList<>();
		newArchivedList.addAll( getArchivedMap().values() );
				
		
		TopNPlayersData comparator = new TopNPlayersData();

		Collections.sort( newTopNList, comparator );
		Collections.sort( newArchivedList, comparator );
		
		setTopNList(newTopNList);
		setArchivedList(newArchivedList);
		
//		// sort:
//		sortTopN();
		
		// If there has been any changes since the last save, then 
		// save it:
		if ( isDirty() ) {
			setDirty( false );
			saveToJson();
		}
	}
	
	private void calculateAllRankScores( ArrayList<TopNPlayersData> topNList ) {

		for ( TopNPlayersData topN : topNList ) {
			
			RankPlayer rPlayer = topN.getrPlayer();
			
			if ( rPlayer == null ) {
				UUID nullUuid = null;
				rPlayer = PrisonRanks.getInstance().getPlayerManager().getPlayer( nullUuid, topN.getName() );
				topN.setrPlayer(rPlayer);
			}
			
			if ( rPlayer != null ) {
				rPlayer.calculateRankScore();
				
				// This will not update lastSeen:
				topN.updateRankPlayer( rPlayer );
			}
			
		}
	}

	/**
	 * <p>This sorts both the topNList and the archivedList.
	 * </p>
	 */
	private void sortTopN() {

		TopNPlayersData comparator = new TopNPlayersData();

		Collections.sort( getTopNList(), comparator );
		Collections.sort( getArchivedList(), comparator );
	}
	
	
	/**
	 * <p>This function will add the RankPlayer data to the topN collections.
	 * This does NOT sort any of the results, so this is to be used to add a lot
	 * of entries, such as on server startup, or with updating with OnlinePlayers.
	 * </p>
	 * 
	 * <p>See updatePlayerData() if being used with a single player, such as with rankup.
	 * </p>
	 * 
	 * @param rPlayer
	 */
	public void addPlayerData( RankPlayer rPlayer ) {
		
		// Recalculate the rankScore for the player:
		rPlayer.calculateRankScore();
		
		TopNPlayersData topN = getTopNPlayer( rPlayer );
		
		
		addPlayerData( topN );
		
	}
	
	private TopNPlayersData getTopNPlayer(RankPlayer rPlayer) {
		
		TopNPlayersData topN = null;
		
		String key = rPlayer.getPlayerFileName();
		
		if ( getTopNMap().containsKey( key ) ) {
			
			topN = getTopNMap().get( key );
		}
		else if ( getArchivedMap().containsKey( key ) ) {
			
			topN = getArchivedMap().get( key );
		}
		else {
			
			topN = new TopNPlayersData( rPlayer );
		}
		
		return topN;
	}

	/**
	 * <p>This function will update, or add, a player's information within topN.  and when
	 * The first thing this function does, is to calculate the rankScore for the RankPlayer.
	 * It will then add the player to the topN.  When finished it will provide a 
	 * simple sort of the topN results.  This will allow the player
	 * who just ranked up to reflect their changes in topN without having to 
	 * wait until the whole topN set is refreshed.  The sorting used here will not update
	 * any of the other player's balances or status, so this has a low-cost sorting.
	 * </p>
	 * 
	 * @param rPlayer
	 */
	public void updatePlayerData( RankPlayer rPlayer ) {
		
		addPlayerData( rPlayer);

		sortTopN();
	}
	
	public int getTopNSize() {
		return getTopNList().size();
	}
	public int getArchivedSize() {
		return getArchivedList().size();
	}
	
	public RankPlayer getTopNRankPlayer( int rankPosition ) {
		return getTopNRankPlayer( rankPosition, false );
	}
    
	public RankPlayer getTopNRankArchivedPlayer( int rankPosition ) {
		return getTopNRankPlayer( rankPosition, true );
	}
	
    /**
     * <p>This function will return the RankPlayer that is at the given rankPosition 
     * within the topN collection.  If rankPosition is out of range, then it will return
     * a null value.
     * </p>
     * 
     * @param rankPosition
     * @return
     */
    private RankPlayer getTopNRankPlayer( int rankPosition, boolean archived ) {
    	RankPlayer rPlayer = null;
    	
    	ArrayList<TopNPlayersData> tList = 
    			archived ? 
    					getArchivedList() :
    					getTopNList();
    	
    	if ( rankPosition >= 0 && tList.size() > rankPosition ) {
    		
    		TopNPlayersData topN = tList.get( rankPosition );
    		
    		rPlayer = topN.getrPlayer();
    		
    		if ( rPlayer == null ) {
    			
    			UUID nullUuid = null;
    			rPlayer = PrisonRanks.getInstance().getPlayerManager()
    							.getPlayer( nullUuid, topN.getName() );
    		}
    		
    		// The topN has the last extracted values, so copy them to the rPlayer if 
    		// it has not been updated.  This would be good for the archives.
    		if ( rPlayer != null && topN.getRankScore() != 0 && rPlayer.getRankScore() == 0 ) {

    			rPlayer.setRankScore( topN.getRankScore() );
    			rPlayer.setRankScorePenalty( topN.getRankScorePenalty() );
    			
    			rPlayer.setRankScoreBalance( topN.getBalance() );
    			rPlayer.setRankScoreCurrency( topN.getBalanceCurrency() );
    			
    		}
    		
    	}
    	
    	
    	
		return rPlayer;
    }
	
	
	public ArrayList<TopNPlayersData> getTopNList() {
		return topNList;
	}
	public void setTopNList(ArrayList<TopNPlayersData> topNList) {
		this.topNList = topNList;
	}

	public TreeMap<String, TopNPlayersData> getTopNMap() {
		return topNMap;
	}
	public void setTopNMap(TreeMap<String, TopNPlayersData> topNMap) {
		this.topNMap = topNMap;
	}

	public ArrayList<TopNPlayersData> getArchivedList() {
		return archivedList;
	}
	public void setArchivedList(ArrayList<TopNPlayersData> archivedList) {
		this.archivedList = archivedList;
	}

	public TreeMap<String, TopNPlayersData> getArchivedMap() {
		return archivedMap;
	}
	public void setArchivedMap(TreeMap<String, TopNPlayersData> archivedMap) {
		this.archivedMap = archivedMap;
	}

	public boolean isDirty() {
		return dirty;
	}
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
}