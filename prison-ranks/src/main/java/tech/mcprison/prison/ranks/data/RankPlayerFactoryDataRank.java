package tech.mcprison.prison.ranks.data;

import java.util.TreeMap;

public class RankPlayerFactoryDataRank {
	
	private String rankName;
	private String ladderName;
	private int rankId;

	public RankPlayerFactoryDataRank() {
		super();
	}
	public RankPlayerFactoryDataRank( String rankName, String ladderName, int rankId ) {
		super();
		
		this.rankName = rankName;
		this.ladderName = ladderName;
		this.rankId = rankId;
	}
	
	public TreeMap<String,Object> getJsonObject(){
		TreeMap<String,Object> results = new TreeMap<>();
		
		results.put("rankName", getRankName());
		results.put("ladderName", getLadderName());
		
		results.put("rankId", Integer.valueOf(rankId));
		
		return results;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Rank: ")
			.append(getRankName())
			.append(" ")
			.append(getRankId())
			.append(" Ladder: ")
			.append(getLadderName());
		
		return sb.toString();
	}
	
	public String getRankName() {
		return rankName;
	}
	public void setRankName(String rankName) {
		this.rankName = rankName;
	}
	
	public String getLadderName() {
		return ladderName;
	}
	public void setLadderName(String ladderName) {
		this.ladderName = ladderName;
	}
	
	public int getRankId() {
		return rankId;
	}
	public void setRankId(int rankId) {
		this.rankId = rankId;
	}
	
}
