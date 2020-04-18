package tech.mcprison.prison.ranks;

import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.ranks.RankUtil.RankupCommands;
import tech.mcprison.prison.ranks.RankUtil.RankupStatus;
import tech.mcprison.prison.ranks.RankUtil.RankupTransactions;
import tech.mcprison.prison.ranks.data.Rank;

public class RankupResults {
	
	private RankupCommands command;
	
	private String player;
	private String executor;
	
    private RankupStatus status;
    private Rank originalRank;
    private Rank targetRank;
    private String message;
    
    private List<RankupTransactions> transactions;

    private double balanceInitial;
    private double balanceFinal;
    
    private int rankupCommandsAvailable = 0;
    private int rankupCommandsExecuted = 0;
    
    private long timestampStart = 0;
    private long timestampStop = 0;
    
    public RankupResults(RankupCommands command, String playerName, String executorName) {
        super();
        
    	this.transactions = new ArrayList<>();
        
    	this.command = command;
        this.player = playerName;
        this.executor = executorName;
        
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

	public String getPlayer() {
		return player;
	}
	public void setPlayer( String player ) {
		this.player = player;
	}

	public String getExecutor() {
		return executor;
	}
	public void setExecutor( String executor ) {
		this.executor = executor;
	}

	public RankupStatus getStatus() {
		return status;
	}
	public void setStatus( RankupStatus status ) {
		this.status = status;
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
