package tech.mcprison.prison.cache;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;

/**
 * <p>This class contains the user data that is used for TopN stats.
 * It keeps track of the player's stats for totalBlocks, tokens, balance,
 * and their Prestiges and Default rank position.
 * This data prevents the need to access the player's cache object.
 * </p>
 * 
 * <p>For the user of the prestiges Rank object, and the default Rank object, 
 * upon first access, it will try to look up those values and will store the
 * Rank object in a local variable for faster access later.  Those stored 
 * objects will not be persisted and hence why there is string field for their
 * names.
 * </p>
 * 
 * @author Blue
 *
 */
public class TopNStatsData
{
	
	private String playerUuid;
	private String playerName;
	
	
	private long totalBlocks;

	private long currentTokens;
	private double currentBalance;
	
	
	private String topRankPrestigesName;
	private transient Rank topRankPrestiges;
	private transient boolean presetigeRankCheck = false;
	
	private String topRankDefaultName;
	private transient Rank topRankDefault;
	private transient boolean defaultRankCheck = false;
	
	
	private long lastSeenDate;
	
	private long lastUpdateDate;
	
	
	public TopNStatsData() {
		super();
		
	}

	public String getPlayerUuid() {
		return playerUuid;
	}
	public void setPlayerUuid( String playerUuid ) {
		this.playerUuid = playerUuid;
	}

	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName( String playerName ) {
		this.playerName = playerName;
	}

	public long getTotalBlocks() {
		return totalBlocks;
	}
	public void setTotalBlocks( long totalBlocks ) {
		this.totalBlocks = totalBlocks;
	}

	public long getCurrentTokens() {
		return currentTokens;
	}
	public void setCurrentTokens( long currentTokens ) {
		this.currentTokens = currentTokens;
	}

	public double getCurrentBalance() {
		return currentBalance;
	}
	public void setCurrentBalance( double currentBalance ) {
		this.currentBalance = currentBalance;
	}

	public String getTopRankPrestigesName() {
		return topRankPrestigesName;
	}
	public void setTopRankPrestigesName( String topRankPrestigesName ) {
		this.topRankPrestigesName = topRankPrestigesName;
	}

	public String getTopRankDefaultName() {
		return topRankDefaultName;
	}
	public void setTopRankDefaultName( String topRankDefaultName ) {
		this.topRankDefaultName = topRankDefaultName;
	}

	public Rank getTopRankPrestiges() {
		if ( !presetigeRankCheck && topRankPrestiges == null ) {
			presetigeRankCheck = true;
			topRankPrestiges = lookupRank( RankLadder.PRESTIGES, getTopRankPrestigesName() );
		}
		return topRankPrestiges;
	}
	public void setTopRankPrestiges( Rank topRankPrestiges ) {
		this.topRankPrestiges = topRankPrestiges;
	}

	public Rank getTopRankDefault() {
		if ( !defaultRankCheck && topRankDefault == null ) {
			defaultRankCheck = true;
			topRankDefault = lookupRank( RankLadder.DEFAULT, getTopRankDefaultName() );
		}
		return topRankDefault;
	}
	public void setTopRankDefault( Rank topRankDefault ) {
		this.topRankDefault = topRankDefault;
	}

	public long getLastSeenDate() {
		return lastSeenDate;
	}
	public void setLastSeenDate( long lastSeenDate ) {
		this.lastSeenDate = lastSeenDate;
	}

	public long getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate( long lastUpdateDate ) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	private Rank lookupRank( String ladder, String rank ) {
		Rank results = null;
		
		if ( rank != null ) {
			RankLadder rLadder = Prison.get().getPlatform().getRankLadder( ladder );

			results = rLadder.getRank( rank );
		}
		
		return results;
	}
}
