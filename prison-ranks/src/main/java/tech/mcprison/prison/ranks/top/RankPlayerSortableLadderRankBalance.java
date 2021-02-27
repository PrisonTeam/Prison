package tech.mcprison.prison.ranks.top;

import java.util.Comparator;

import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;

public class RankPlayerSortableLadderRankBalance
	implements Comparator<RankPlayer>
{
	
	private RankLadder ladder;
	private Rank rank;
	
	public RankPlayerSortableLadderRankBalance( String ladder ) {
		super();
		
		this.ladder = null;
		this.rank = null;
		
		if ( ladder != null ) {
			
			this.ladder = PrisonRanks.getInstance().getLadderManager()
									.getLadder( ladder );
		}
	}
	
	public RankPlayerSortableLadderRankBalance( String ladder, String rank ) {
		this( ladder );
		
		if ( rank != null ) {
			
			this.rank = PrisonRanks.getInstance().getRankManager().getRank( rank );
		}
	}
	
	

	/**
	 * <p>Compares one player to another player within one ladder only.
	 * They are ordered first by rank, then by top-score within the rank, 
	 * and then finally alphabetically.
	 * </p>
	 * 
	 * <p>The top-score is calculated as a percentage of the player's balance
	 * to the rankup cost at the next rank.  If the player is at the highest
	 * rank on the ladder, then the top-score can exceed 100%.  If the player is
	 * not at the top rank in the ladder, then if they exceed 100% then the
	 * percentage above 100% becomes a penalty applied against a top-score of 100
	 * and 1/10 the calculated value above 100%.
	 * </p>
	 * 
	 * <p>Examples:
	 * </p>
	 * <ul>
	 *   <li>If rankup cost is 1,000 and the player has 729, then their top-score
	 *   		will be 72.90.</li>
	 *   <li>If rankup cost is 1,000 and the player has 1,000, then their top-score
	 *   		is 100.0.</li>
	 *   <li>If rankup cost is 1,000 and the player has 2,000, then their top-score 
	 *   		will be 90.0.  Since they have 200% of the rankup cost, the first
	 *   		100% sets their top-score to 100.00, then the remainder 100% is divided 
	 *   		by 10 which yields 10.0 and is then subtracted from the 100.00 
	 *   		for final amount of 90.00.</li>
	 *   <li>If rankup cost is 1,000 and the player has 3,456, then their top-score is
	 *   		calculated as 100.00 with a penalty of (3456/1000 - 100) / 10 = 24.56.
	 *   		A final top-score of: 100.00 - 24.56 = 75.44.</li>
	 *   <li>If rankup cost is 1,000 and is the last rank on the ladder and the player
	 *   		has 3,456.  Then the top-score is 345.60.</li>
	 *   		 
	 * </ul>
	 * 
	 */
	@Override
	public int compare( RankPlayer rp1, RankPlayer rp2 ) {
		int results = 0;
		
		if ( rp1 == null ) {
			results = -1;
		}
		else if ( rp2 == null ) {
			results = 1;
		}
		else {
			Rank r1 = rp1.getRank( getLadder().getName() );
			Rank r2 = rp2.getRank( getLadder().getName() );
			
			if ( r1 == null ) {
				results = -1;
			}
			else if ( r2 == null ) {
				results = 1;
			}
			else {
				
				// If ladder is not null, then compare the rank positions:
				if ( getLadder() != null ) {
					
					// compare the Ranks' position.  Ranks with higher position are greater:
					
					int pos1 = getLadder().getPositionOfRank( r1 );
					int pos2 = getLadder().getPositionOfRank( r2 );
					
					results = Integer.compare( pos1, pos2 );
				}
				
				// results == 0 then so far the two RankPlayers share the same rank:
				if ( results == 0 ) {
					// Need to compare the player's balance:
					
					String currency = r1.getCurrency();
					
					
					RankPlayerBalance bal1 = rp1.getCachedRankPlayerBalance( currency );
					RankPlayerBalance bal2 = rp2.getCachedRankPlayerBalance( currency );
					
					double topScore1 = calculateTopScore( r1, bal1.getBalance() );
					double topScore2 = calculateTopScore( r2, bal2.getBalance() );
					
					results = Double.compare( topScore1, topScore2 );
					
					if ( results == 0 ) {
						results = rp1.getName().compareToIgnoreCase( rp2.getName() );
					}
					
				}
				
				
			}

		}
		
		return results;
	}

	/**
	 * <p>This calculates the topScore value.  The topScore is based upon the
	 * percent remaining to rank up.  If there is no higher rank, it uses the current 
	 * rank's cost, otherwise it uses the next ranks' cost.
	 * </p>
	 * 
	 * <p>If there is another rank, the max topScore is 100% and if the player has
	 * more than the rankup cost, then their topScore is adjusted with a 
	 * penality for not ranking up.  The excess above 100% is used as the penalty 
	 * after it is divided by 10.  So if the player has 200% more than the rankup
	 * cost, then their penalty will be minus 10%: (200% - 100%) / 10.
	 * 
	 * @param rank
	 * @param balance
	 * @return
	 */
	private double calculateTopScore( Rank rank, double balance ) {
		double topScore = 0;
		
		if ( balance != 0 ) {
			Rank nextRank = rank.getRankNext();
			
			double nextRankCost = nextRank == null ? rank.getCost() : nextRank.getCost();
			
			topScore = nextRankCost / balance;
			
			// if there is a next rank, and topScore > 100% then adjust the score.
			// Calculate the adjustment by subtracting 100.0 and then dividing by 10.
			// Then set topScore to 100, then subtract the adjustment.
			if ( nextRank != null && topScore > 100.0 ) {
				double adjustment = (topScore - 100.0) / 10;
				topScore = 100 - adjustment;
				
				if ( topScore < 0 ) {
					topScore = 0.01;
				}
			}
		}
		
		return topScore;
	}

	public RankLadder getLadder() {
		return ladder;
	}

	public Rank getRank() {
		return rank;
	}

	
}
