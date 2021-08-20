package tech.mcprison.prison.ranks.data;

public class PlayerRank
{
	
	private final Rank rank;
	
	private Double rankMultiplier = null;
	private Double rankCost = null;

	public PlayerRank( Rank rank ) {
		super();
		
		this.rank = rank;
		
		double rankMultiplier = getLadderBasedRankMultiplier();
		
		setRankCost( rankMultiplier );
//		this.rankCost = rank.getCost() * (1.0 + rankMultiplier);
	}
	
	private PlayerRank( Rank rank, double rankMultiplier ) {
		this( rank );
		
		this.rankMultiplier = rankMultiplier;
		
		setRankCost( rankMultiplier );
//		this.rankCost = rank.getCost() * (1.0 + rankMultiplier);
	}
	
	public void applyMultiplier( double rankMultiplier ) {
		
		this.rankMultiplier = rankMultiplier;
		
		setRankCost( rankMultiplier );
//		this.rankCost = rank.getCost() * (1.0 + rankMultiplier);
	}
	
	private void setRankCost( double rankMultiplier ) {
		
		this.rankCost = rank.getCost() * (1.0 + rankMultiplier);
	}
	
	public double getLadderBasedRankMultiplier() {
		
		return getLadderBaseRankdMultiplier( getRank() );
	}
	
	public static double getLadderBaseRankdMultiplier( Rank rank ) {
		double rankMultiplier = 0;
		
		if ( rank != null && rank.getLadder() != null ) {
			double ladderMultiplier = rank.getLadder().getRankCostMultiplierPerRank();
			
			// Because it's zero based... so add a 1
			rankMultiplier = ladderMultiplier * (1 + rank.getPosition());
		}
		
		return rankMultiplier;
	}
	public static double getRawRankCost( Rank rank ) {
		return rank.getCost();
	}
	public static void setRawRankCost( Rank rank, double rawCost ) {
		rank.setCost( rawCost );
	}
	
	public static PlayerRank getTargetPlayerRankForPlayer( RankPlayer player, Rank targetRank ) {
		PlayerRank targetPlayerRank = null;
		
		if ( targetRank != null ) {
			
			double targetRankMultiplier = getLadderBaseRankdMultiplier( targetRank );
			
			PlayerRank pRankForPLayer = player.getRank( targetRank.getLadder() );
			double existingRankMultiplier = pRankForPLayer == null ? 0 : 
				getLadderBaseRankdMultiplier( pRankForPLayer.getRank() );
			
			// Get the player's total rankMultiplier from the default ladder 
			// because they will always have a rank there:
			double playerMultipler = player.getRank( "default" ).getRankMultiplier();
			
			// So the actual rank multiplier that needs to be used, is based upon the 
			// Player's current multiplier PLUS the multiplier for the target rank 
			// AND MINUS the multiplier for the current rank the player has within the 
			// target rank's ladder.
			double rankMultiplier = playerMultipler + targetRankMultiplier - existingRankMultiplier;
			
			targetPlayerRank = new PlayerRank( targetRank, rankMultiplier );
		}
		
		return targetPlayerRank;
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

}
