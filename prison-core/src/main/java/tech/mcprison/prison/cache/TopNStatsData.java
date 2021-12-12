package tech.mcprison.prison.cache;

import tech.mcprison.prison.ranks.data.Rank;

public class TopNStatsData
{
	
	private String playerUuid;
	private String playerName;
	
	
	private long totalBlocks;

	private long currentTokens;
	private double currentBalance;
	
	
	private String topRankPrestigesName;
	private String topRankDefaultName;
	
	private transient Rank topRankPrestiges;
	private transient Rank topRankDefault;
	
	
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
		return topRankPrestiges;
	}
	public void setTopRankPrestiges( Rank topRankPrestiges ) {
		this.topRankPrestiges = topRankPrestiges;
	}

	public Rank getTopRankDefault() {
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
	
}
