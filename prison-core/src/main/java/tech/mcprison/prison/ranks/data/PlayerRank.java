package tech.mcprison.prison.ranks.data;

/**
 * <p>PlayerRank is a container for holding a players rank, along with their
 * rankMultiplier and the calculated rankCost.
 * </p>
 *
 */
public class PlayerRank
	implements Comparable<PlayerRank>
{
	
	private final Rank rank;
	
	private Double rankMultiplier = null;
	private Double rankCost = null;

	protected PlayerRank( Rank rank ) {
		super();
		
		this.rank = rank;
		
//		double rankMultiplier = getLadderBasedRankMultiplier( rank );
//		
//		setRankCost( rankMultiplier );
////		this.rankCost = rank.getCost() * (1.0 + rankMultiplier);
	}
	
	protected PlayerRank( Rank rank, double rankMultiplier ) {
		this( rank );
		
		this.rankMultiplier = rankMultiplier;
		
		setRankCost( rankMultiplier );
//		this.rankCost = rank.getCost() * (1.0 + rankMultiplier);
	}
	
	@Override
	public String toString() {
		return "PlayerRank: " + rank.getName() + " (" + rank.getId() + 
				" mult: " + rankMultiplier + " cost: " + rankCost + ")";
	}
	
	public void applyMultiplier( double rankMultiplier ) {
		
		this.rankMultiplier = rankMultiplier;
		
		setRankCost( rankMultiplier );
//		this.rankCost = rank.getCost() * (1.0 + rankMultiplier);
	}
	
	protected void setRankCost( double rankMultiplier ) {
		
		this.rankCost = rank.getCost() * (1.0 + rankMultiplier);
	}
	
	public double getLadderBasedRankMultiplier() {
		return getLadderBasedRankMultiplier( getRank() );
	}
	
	public  double getLadderBasedRankMultiplier( Rank rank ) {
		double rankMultiplier = 0;
		
		if ( rank != null && rank.getLadder() != null ) {
			double ladderMultiplier = rank.getLadder().getRankCostMultiplierPerRank();
			
			// Because it's zero based... so add a 1
			rankMultiplier = ladderMultiplier * (1 + rank.getPosition());
		}
		
		return rankMultiplier;
	}
	
	
//	public static double getRawRankCost( Rank rank ) {
//		return rank.getCost();
//	}
//	public static void setRawRankCost( Rank rank, double rawCost ) {
//		rank.setCost( rawCost );
//	}
	
//	public static PlayerRank getTargetPlayerRankForPlayer( RankPlayer player, Rank targetRank ) {
//		PlayerRank targetPlayerRank = null;
//		
//		if ( targetRank != null ) {
//			
//			double targetRankMultiplier = getLadderBaseRankdMultiplier( targetRank );
//			
//			PlayerRank pRankForPLayer = player.getRank( targetRank.getLadder() );
//			double existingRankMultiplier = pRankForPLayer == null ? 0 : 
//				getLadderBaseRankdMultiplier( pRankForPLayer.getRank() );
//			
//			// Get the player's total rankMultiplier from the default ladder 
//			// because they will always have a rank there:
//			PlayerRank pRank = player.getRank( "default" );
//			double playerMultipler = pRank == null ? 0 : pRank.getRankMultiplier();
//			
//			// So the actual rank multiplier that needs to be used, is based upon the 
//			// Player's current multiplier PLUS the multiplier for the target rank 
//			// AND MINUS the multiplier for the current rank the player has within the 
//			// target rank's ladder.
//			double rankMultiplier = playerMultipler + targetRankMultiplier - existingRankMultiplier;
//			
//			targetPlayerRank = new PlayerRank( targetRank, rankMultiplier );
//		}
//		
//		return targetPlayerRank;
//	}

	
	public PlayerRank getTargetPlayerRankForPlayer( RankPlayer player, Rank targetRank ) {
		return getTargetPlayerRankForPlayer( this, player, targetRank );
	}
	
	public PlayerRank getTargetPlayerRankForPlayer( PlayerRank playerRank, RankPlayer player, Rank targetRank )
	{
		PlayerRank targetPlayerRank = null;
	
		if ( targetRank != null )
		{
	
			double targetRankMultiplier = playerRank.getLadderBasedRankMultiplier( targetRank );
	
			
			PlayerRank pRankForPLayer = player.getLadderRanks().get( targetRank.getLadder() );
			
	//		PlayerRank pRankForPLayer = getRank( player, targetRank.getLadder() );
			double existingRankMultiplier = pRankForPLayer == null ? 0
					: playerRank.getLadderBasedRankMultiplier( pRankForPLayer.getRank() );
	
			// Get the player's total rankMultiplier from the default ladder
			// because they will always have a rank there:
			RankLadder defaultLadder = getDefaultLadder( player );
			
			PlayerRank pRank = player.getLadderRanks().get( defaultLadder );
//			PlayerRank pRank = getRank( player, "default" );
			double playerMultipler = pRank == null ? 0 : pRank.getRankMultiplier();
	
			// So the actual rank multiplier that needs to be used, is based upon
			// the
			// Player's current multiplier PLUS the multiplier for the target rank
			// AND MINUS the multiplier for the current rank the player has within
			// the
			// target rank's ladder.
			double rankMultiplier = playerMultipler + targetRankMultiplier - existingRankMultiplier;
	
			targetPlayerRank = createPlayerRank( targetRank, rankMultiplier );
		}
	
		return targetPlayerRank;
	}
	
	private RankLadder getDefaultLadder( RankPlayer player )
	{
		RankLadder defaultLadder = null;
		
		for ( RankLadder ladder : player.getLadderRanks().keySet() )
		{
			if ( ladder.getName().equalsIgnoreCase( "default" ) ) {
				defaultLadder = ladder;
			}
		}
		
		return defaultLadder;
	}

	private PlayerRank createPlayerRank( Rank rank, double rankMultiplier ) {
		PlayerRank results = new PlayerRank( rank, rankMultiplier );
		
		return results;
	}

	public Rank getRank() {
		return rank;
	}

	public Double getRankMultiplier() {
		return rankMultiplier;
	}
//	public void setRankMultiplier( Double rankMultiplier ) {
//		this.rankMultiplier = rankMultiplier;
//	}

	public Double getRankCost() {
		return rankCost;
	}
//	public void setRankCost( Double rankCost ) {
//		this.rankCost = rankCost;
//	}

	@Override
	public int compareTo( PlayerRank pr )
	{
		int results = 0;
		
		if ( pr == null ) {
			results = -1;
		}
		else {
			results = getRank().compareTo( pr.getRank() );
		}
		
		return results;
	}

}
