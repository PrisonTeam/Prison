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
	
	public PlayerRank( Rank rank, double rankMultiplier ) {
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
		double ladderMultiplier = rank.getLadder().getRankCostMultiplierPerRank();
		
		// Because it's zero based... so add a 1
		double rankMultiplier = ladderMultiplier * (1 + rank.getPosition());
		
		return rankMultiplier;
	}
	public static double getRawRankCost( Rank rank ) {
		return rank.getCost();
	}
	public static void setRawRankCost( Rank rank, double rawCost ) {
		rank.setCost( rawCost );
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
