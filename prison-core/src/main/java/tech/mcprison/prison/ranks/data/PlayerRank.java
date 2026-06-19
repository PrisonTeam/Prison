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
		
	}
	
	protected PlayerRank( Rank rank, double rankMultiplier ) {
		this( rank );
		
		this.rankMultiplier = rankMultiplier;
		
		setRankCost( rankMultiplier );
	}
	
	@Override
	public String toString() {
		return "PlayerRank: " + rank.getName() + " (" + rank.getId() + 
				" mult: " + rankMultiplier + " cost: " + rankCost + ")";
	}
	
	public void applyMultiplier( double rankMultiplier ) {
		
		this.rankMultiplier = rankMultiplier;
		
		setRankCost( rankMultiplier );
	}
	
	protected void setRankCost( double rankMultiplier ) {
		
		boolean applyMultiplier = rank.getLadder().isApplyRankCostMultiplierToLadder();
		
		if ( applyMultiplier ) {
			
			this.rankCost = rank.getCost() * (1.0 + rankMultiplier);
		}
		else {
			this.rankCost = rank.getCost();
		}
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
	
	

	public Rank getRank() {
		return rank;
	}

	public String getCurrency() {
		
		String currency = getRank() == null ? "" : getRank().getCurrency();
		
		return currency;
	}
	
	public Double getRankMultiplier() {
		return rankMultiplier;
	}

	public Double getRankCost() {
		return rankCost;
	}

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
