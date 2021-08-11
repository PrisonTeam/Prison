package tech.mcprison.prison.ranks.data;

public class StatsRankPlayerBalanceData
		implements Comparable<StatsRankPlayerBalanceData>
{
	private final Rank rank;
	private final RankPlayer player;
	
	private double score = 0;
	private double penalty = 0;

	public StatsRankPlayerBalanceData( Rank rank, RankPlayer player, boolean isPleanltyEnabled ) {
		super();
		
		this.rank = rank;
		this.player = player;
		
		recalc( isPleanltyEnabled );
	}
	
	public StatsRankPlayerBalanceData() {
		super();
		
		this.rank = null;
		this.player = null;
	}
	
	public void recalc( boolean isPenaltyEnabled ) {
		double balance = player.getBalance( rank.getCurrency() );
		double score = balance;
		
		// The "cost" should be the cost of the next rank.  If the next rank does not exist,
		// then it should be the current rank.
		PlayerRank pRank = player.getRank( rank.getLadder() );
		double cost = pRank.getRankCost();  // This is the fallback value if nextRank doesn't exist.

		if ( rank.getRankNext() != null ) {
			PlayerRank pRankNext = new PlayerRank( rank.getRankNext(), pRank.getRankMultiplier() );
			cost = pRankNext.getRankCost();
		}
//		double cost = rank.getRankNext() == null ? rank.getCost() : rank.getRankNext().getCost();
		double penalty = 0d;
		
		// Do not apply the penalty if cost is zero:
		if ( isPenaltyEnabled && cost > 0 ) {
			score = balance > cost ? cost : score;
			
			double excess = balance > cost ? balance - cost : 0d;
			penalty = excess * 0.2d;
		}
		
		score = (score - penalty);
		
		if ( cost > 0 ) {
			score /= cost * 100.0d;
		}
		
		setScore( score );
		setPenalty( penalty );
	}


	
	@Override
	public boolean equals( Object obj )
	{
		boolean results = obj != null && obj instanceof StatsRankPlayerBalanceData &&
				getPlayer().equals( ((StatsRankPlayerBalanceData) obj).getPlayer() );
				
		return results;
	}

	@Override
	public int compareTo( StatsRankPlayerBalanceData statsData )
	{
		int results = 0;
		
		if ( statsData == null ) {
			results = 1;
		}
		else if ( getScore() < statsData.getScore() ) { 
			results = -1;
		}
		else if ( getScore() > statsData.getScore() ) {
			results = 1;
		}
		
		return results;
	}

	
	public Rank getRank() {
		return rank;
	}

	public RankPlayer getPlayer() {
		return player;
	}

	public double getScore() {
		return score;
	}
	public void setScore( double score ) {
		this.score = score;
	}

	public double getPenalty() {
		return penalty;
	}
	public void setPenalty( double penalty ) {
		this.penalty = penalty;
	}

}
