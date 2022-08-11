package tech.mcprison.prison.ranks.data;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import tech.mcprison.prison.ranks.data.TopNPlayers.PlayerState;

/**
 * <p>This class represent a player within the topN rankings.
 * Since PlayerState has three values: 
 * </p>
 * 
 * <p>The way that the rank scores are calculated are rather complex, but this class 
 * tries to simplify the details.
 * </p>
 * 
 * <p>The primary way topN is sorted is by **current** rank positions for both the
 * default ladder and the prestiges ladder.  For the prestiges ladder, if they do not have 
 * a current rank on that ladder, then they would have a rank-ladder position of zero.
 * </p>
 * 
 * <p>
 * These rank position values become the first and second tier of sorting.
 * </p>
 * 
 * 
 * @author Blue
 *
 */
public class TopNPlayersData
	implements Comparator<TopNPlayersData>,
			Comparable<TopNPlayersData>
{
	
	private String name;
	private String playerFileName;
	
	private PlayerState playerState;
	
	// For report generation, not sorting:
	private transient RankPlayer rPlayer;
	
	private long lastSeen;
	private String lastSeenFormatted;
	
	private String balanceCurrency;
	private double balance;
	
	private double rankScore;
	private double rankScorePenalty;
	
	private int rankPositionDefault;
	private int rankPositionPrestiges;
	
	private static transient SimpleDateFormat sdFmt = new SimpleDateFormat( "yyyy-MM-dd kk:mm:ss" );
	
	public TopNPlayersData() {
		super();
		
		this.playerState = PlayerState.offline;
	}

	public TopNPlayersData( RankPlayer rPlayer) {
		this();
		
		this.rPlayer = rPlayer;
		
		this.name = rPlayer.getName();
		this.playerFileName = rPlayer.getPlayerFileName();
		
//		// Note: the nextPlayer rank could be in either the default ladder, 
//		//       or the next prestige rank if at end of default ladder.
//		PlayerRank nextRank = rPlayer.getNextPlayerRank();
		
		
		// This may be expensive getting lastSeen from the player's cache data:
		setLastSeen( rPlayer.getPlayerCachePlayerData().getLastSeenDate() );
		
//		this.lastSeenFormatted = sdFmt.format( new Date( this.lastSeen ) );
		
		updateRankPlayer( rPlayer );
		
	}
	
	public void updateRankPlayer( RankPlayer rPlayer ) {
		
		setRankScore( rPlayer.getRankScore() );
		setRankScorePenalty( rPlayer.getRankScorePenalty() );
		
		setBalance( rPlayer.getRankScoreBalance() );
		setBalanceCurrency( rPlayer.getRankScoreCurrency() );
		
		setRankPositionDefault( rPlayer.getRankPositonDefault() );
		setRankPositionPrestiges( rPlayer.getRankPositonPrestiges() );
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append( getPlayerState().name() ).append( " " );

		sb.append( getName() ).append( " " );
		sb.append( getRankPositionPrestiges() ).append( " " );
		sb.append( getRankPositionDefault() ).append( " " );

		sb.append( getRankScore() ).append( " " );
		sb.append( getLastSeenFormatted() ).append( " " );
		
		return sb.toString();
	}

	@Override
	public int compareTo(TopNPlayersData o) {
		
		int results = Integer.compare( o.getRankPositionPrestiges(), getRankPositionPrestiges() );
		
		if ( results == 0 ) {
			
			results = Integer.compare( o.getRankPositionDefault(), getRankPositionDefault() );
			
			if ( results == 0 ) {
				results = Double.compare( o.getRankScore(), getRankScore() );
				
				if ( results == 0 ) {
					results = o.getName().compareToIgnoreCase( getName() );
				}
			}
		}
		
		return results;
	}
	
	@Override
	public int compare(TopNPlayersData o1, TopNPlayersData o2) {
		
		int results = o1.compareTo( o2 );
		return results;
	}
	
	public String getKey() {
		return getPlayerFileName();
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getPlayerFileName() {
		return playerFileName;
	}
	public void setPlayerFileName(String playerFileName) {
		this.playerFileName = playerFileName;
	}

	public PlayerState getPlayerState() {
		return playerState;
	}
	public void setPlayerState(PlayerState playerState) {
		this.playerState = playerState;
	}
	
	public RankPlayer getrPlayer() {
		return rPlayer;
	}
	public void setrPlayer(RankPlayer rPlayer) {
		this.rPlayer = rPlayer;
	}

	public long getLastSeen() {
		return lastSeen;
	}
	public void setLastSeen(long lastSeen) {
		
		this.lastSeen = lastSeen;
		
		this.lastSeenFormatted = sdFmt.format( new Date( lastSeen) );
	}

	public String getLastSeenFormatted() {
		return lastSeenFormatted;
	}
	public void setLastSeenFormatted(String lastSeenFormatted) {
		this.lastSeenFormatted = lastSeenFormatted;
	}

	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getBalanceCurrency() {
		return balanceCurrency;
	}
	public void setBalanceCurrency(String balanceCurrency) {
		this.balanceCurrency = balanceCurrency;
	}

	public double getRankScore() {
		return rankScore;
	}
	public void setRankScore(double rankScore) {
		this.rankScore = rankScore;
	}

	public double getRankScorePenalty() {
		return rankScorePenalty;
	}
	public void setRankScorePenalty(double rankScorePenalty) {
		this.rankScorePenalty = rankScorePenalty;
	}

	public int getRankPositionDefault() {
		return rankPositionDefault;
	}
	public void setRankPositionDefault(int rankPositionDefault) {
		this.rankPositionDefault = rankPositionDefault;
	}

	public int getRankPositionPrestiges() {
		return rankPositionPrestiges;
	}
	public void setRankPositionPrestiges(int rankPositionPrestiges) {
		this.rankPositionPrestiges = rankPositionPrestiges;
	}


}
