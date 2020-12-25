package tech.mcprison.prison.ranks.events;

import tech.mcprison.prison.ranks.RankUtil.PromoteForceCharge;
import tech.mcprison.prison.ranks.RankUtil.RankupCommands;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankPlayer;

/**
 * An event that fires when a player ranks up.
 *
 * @author Faizaan A. Datoo
 */
public class RankUpEvent {

    /*
     * Fields & Constants
     */

    private RankPlayer player;
    private Rank oldRank;
    private Rank newRank;
    private double cost;
    
    private RankupCommands commad;
    private PromoteForceCharge forceCharge;

    private boolean canceled = false;
    private String cancelReason = null;
    
    /*
     * Constructor
     */

    public RankUpEvent(RankPlayer player, 
    		Rank oldRank, Rank newRank, double cost, 
    		RankupCommands rankupCommand,
    		PromoteForceCharge forceCharge ) {
    	
        this.player = player;
        this.oldRank = oldRank;
        this.newRank = newRank;
        this.cost = cost;
        
        this.commad = rankupCommand;
        this.forceCharge = forceCharge;
    }

    /*
     * Getters & Setters
     */

    public RankPlayer getPlayer() {
        return player;
    }
    public void setPlayer(RankPlayer player) {
        this.player = player;
    }

    public Rank getOldRank() {
        return oldRank;
    }
    public void setOldRank(Rank oldRank) {
        this.oldRank = oldRank;
    }

    public Rank getNewRank() {
        return newRank;
    }
    public void setNewRank(Rank newRank) {
        this.newRank = newRank;
    }

    /**
     * <p>Warning: do not rely upon this field, since it may not
     * be accurate since it does not include a custom currency
     * if it is being used.  Instead, see the new rank for the cost
     * which will contain a custom currency.
     * @return
     */
    public double getCost() {
        return cost;
    }


	public RankupCommands getCommad() {
		return commad;
	}


	/**
	 * <p>This value is only used when using promote,
	 * demote, or set rank. 
	 * </p>
	 * 
	 * <p>Possible values are: no_charge, charge_player, 
	 * refund_player
	 * </p>
	 * @return
	 */
	public PromoteForceCharge getForceCharge() {
		return forceCharge;
	}


	/**
	 * <p>Indicates if a rankup was canceled outside of 
	 * prison. There are numerous checks that can cancel the
	 * the rankup before it even fires this event.  
	 * </p>
	 * 
	 * <p>This is fired before any currency checks occur.
	 * </p>
	 * @return
	 */
	public boolean isCanceled() {
		return canceled;
	}
	public void setCanceled( boolean canceled ) {
		this.canceled = canceled;
	}

	public String getCancelReason() {
		return cancelReason;
	}
	public void setCancelReason( String cancelReason ) {
		this.cancelReason = cancelReason;
	}
    
}
