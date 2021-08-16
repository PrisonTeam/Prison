package tech.mcprison.prison.ranks;

import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.ranks.RankUtil.RankupCommands;
import tech.mcprison.prison.ranks.RankUtil.RankupStatus;
import tech.mcprison.prison.ranks.RankUtil.RankupTransactions;
import tech.mcprison.prison.ranks.data.PlayerRank;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.ranks.data.RankPlayer;

public class RankupResults {
	
	private RankupCommands command;
	
	RankPlayer rankPlayer;
//	private String player;
	private String executor;
	
    private RankupStatus status;
    
    private RankLadder ladder;
    private String ladderName;
	private String rankName;
	private PlayerRank playerRankOriginal;
	private PlayerRank playerRankTarget;
    private Rank originalRank;
    private Rank targetRank;
    private String message;
    
    private List<RankupTransactions> transactions;

    private double balanceInitial;
    private double balanceFinal;
    private String currency;
    
    private int rankupCommandsAvailable = 0;
    private int rankupCommandsExecuted = 0;
    
    private long timestampStart = 0;
    private long timestampStop = 0;
    
    public RankupResults(RankupCommands command, RankPlayer rankPlayer, String executorName,
    		String ladderName, String rankName) {
        super();
        
        this.status = RankupStatus.IN_PROGRESS;
        
    	this.transactions = new ArrayList<>();
        
    	this.command = command;
    	
    	this.rankPlayer = rankPlayer;
//        this.player = playerName;
        this.executor = executorName;
        
        this.ladderName = ladderName;
        this.rankName = rankName;
        
        this.currency = null;
        
        this.timestampStart = System.currentTimeMillis();
    }
    
    
    public RankupResults addTransaction( RankupStatus status, RankupTransactions transaction ) {
    	setStatus( status );
    	getTransactions().add( transaction );
    	this.timestampStop = System.currentTimeMillis();
    	return this;
    }
    public RankupResults addTransaction( RankupTransactions transaction ) {
    	getTransactions().add( transaction );
    	this.timestampStop = System.currentTimeMillis();
    	return this;
    }

    
    public long getElapsedTime() {
    	long elapsed = getTimestampStart() - getTimestampStop();
    	
    	return ( elapsed < 0 ? 0 : elapsed);
    }
    
    
	public RankupCommands getCommand() {
		return command;
	}
	public void setCommand( RankupCommands command ) {
		this.command = command;
	}
	
	public RankPlayer getRankPlayer() {
		return rankPlayer;
	}
	public void setRankPlayer( RankPlayer rankPlayer ) {
		this.rankPlayer = rankPlayer;
	}

//	public String getPlayer() {
//		return player;
//	}
//	public void setPlayer( String player ) {
//		this.player = player;
//	}

	
	public RankLadder getLadder() {
		return ladder;
	}
	public void setLadder( RankLadder ladder ) {
		this.ladder = ladder;
	}

	public String getExecutor() {
		return executor;
	}
	public void setExecutor( String executor ) {
		this.executor = executor;
	}

	public String getLadderName() {
		return ladderName;
	}
	public void setLadderName( String ladderName ) {
		this.ladderName = ladderName;
	}

	public String getRankName() {
		return rankName;
	}
	public void setRankName( String rankName ) {
		this.rankName = rankName;
	}

	public RankupStatus getStatus() {
		return status;
	}
	public void setStatus( RankupStatus status ) {
		this.status = status;
	}

	public PlayerRank getPlayerRankOriginal() {
		if ( playerRankOriginal == null && originalRank != null && rankPlayer != null ) {
			PlayerRank pRank = rankPlayer.getRank( originalRank.getLadder() );
			playerRankOriginal = pRank;
		}
		return playerRankOriginal;
	}
	public void setPlayerRankOriginal( PlayerRank playerRankOriginal ) {
		this.playerRankOriginal = playerRankOriginal;
	}

	public PlayerRank getPlayerRankTarget() {
		if ( playerRankTarget == null && 
				getOriginalRank() != null && getOriginalRank().getRankNext() != null &&
				targetRank != null ) {

	        // This calculates the target rank, and takes in to consideration the player's existing rank:
	        playerRankTarget = PlayerRank.getTargetPlayerRankForPlayer( rankPlayer, targetRank );
			
//			PlayerRank pRank = rankPlayer.getRank( originalRank.getLadder() );
//			PlayerRank pRankNext = new PlayerRank( targetRank, pRank.getRankMultiplier() );

//			playerRankTarget = pRankNext;
		}
		return playerRankTarget;
	}
	public void setPlayerRankTarget( PlayerRank playerRankTarget ) {
		this.playerRankTarget = playerRankTarget;
	}
	

	public Rank getOriginalRank() {
		return originalRank;
	}
	public void setOriginalRank( Rank originalRank ) {
		this.originalRank = originalRank;
	}

	public Rank getTargetRank() {
		return targetRank;
	}
	public void setTargetRank( Rank targetRank ) {
		this.targetRank = targetRank;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage( String message ) {
		this.message = message;
	}

	public List<RankupTransactions> getTransactions() {
		return transactions;
	}
	public void setTransactions( List<RankupTransactions> transactions ) {
		this.transactions = transactions;
	}

	public double getBalanceInitial() {
		return balanceInitial;
	}
	public void setBalanceInitial( double balanceInitial ) {
		this.balanceInitial = balanceInitial;
	}

	public double getBalanceFinal() {
		return balanceFinal;
	}
	public void setBalanceFinal( double balanceFinal ) {
		this.balanceFinal = balanceFinal;
	}

	public String getCurrency() {
		return currency;
	}
	public void setCurrency( String currency ){
		this.currency = currency;
	}

	public int getRankupCommandsAvailable() {
		return rankupCommandsAvailable;
	}
	public void setRankupCommandsAvailable( int rankupCommandsAvailable ) {
		this.rankupCommandsAvailable = rankupCommandsAvailable;
	}

	public int getRankupCommandsExecuted() {
		return rankupCommandsExecuted;
	}
	public void setRankupCommandsExecuted( int rankupCommandsExecuted ) {
		this.rankupCommandsExecuted = rankupCommandsExecuted;
	}

	public long getTimestampStart() {
		return timestampStart;
	}
	public void setTimestampStart( long timestampStart ) {
		this.timestampStart = timestampStart;
	}

	public long getTimestampStop() {
		return timestampStop;
	}
	public void setTimestampStop( long timestampStop ) {
		this.timestampStop = timestampStop;
	}
	
}
