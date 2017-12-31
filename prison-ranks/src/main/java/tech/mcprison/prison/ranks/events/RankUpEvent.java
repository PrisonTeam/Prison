package tech.mcprison.prison.ranks.events;

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

    /*
     * Constructor
     */

    public RankUpEvent(RankPlayer player, Rank oldRank, Rank newRank, double cost) {
        this.player = player;
        this.oldRank = oldRank;
        this.newRank = newRank;
        this.cost = cost;
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

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

}
