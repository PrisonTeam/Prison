/*
 * Copyright (C) 2017 The MC-Prison Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.ranks.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.gson.internal.LinkedTreeMap;

import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.RankUtil;
import tech.mcprison.prison.store.Document;

/**
 * A certain sequence that rank-ups will follow. There may be multiple rank ladders on the server at
 * a time, and one rank may be a part of multiple ladders.
 *
 * @author Faizaan A. Datoo
 */
public class RankLadder {

    /*
     * Fields & Constants
     */

    public int id;
    public String name;
    public List<PositionRank> ranks;
    public int maxPrestige;

    /*
     * Document-related
     */

    public RankLadder() {
    }

    @SuppressWarnings( "unchecked" )
	public RankLadder(Document document) {
        this.id = RankUtil.doubleToInt(document.get("id"));
        this.name = (String) document.get("name");
        List<LinkedTreeMap<String, Object>> ranksLocal =
                (List<LinkedTreeMap<String, Object>>) document.get("ranks");

        this.ranks = new ArrayList<>();
        for (LinkedTreeMap<String, Object> rank : ranksLocal) {
            ranks.add(new PositionRank(RankUtil.doubleToInt(rank.get("position")),
                    RankUtil.doubleToInt((rank.get("rankId")))));
        }
        
        this.maxPrestige = RankUtil.doubleToInt(document.get("maxPrestige"));
        
    }

    public Document toDocument() {
        Document ret = new Document();
        ret.put("id", this.id);
        ret.put("name", this.name);
        ret.put("ranks", this.ranks);
        ret.put("maxPrestige", this.maxPrestige);
        return ret;
    }

    /*
     * Methods
     */


    /**
     * Add a rank to this ladder.
     *
     * @param position The place in line to put this rank, beginning at 0. The player will be taken
     *                 through each rank by order of their positions in the ladder.
     * @param rank     The {@link Rank} to add.
     */
    public void addRank(int position, Rank rank) {
        position = Math.min(position,
                ranks.size() + 1); // Make sure to cap it off at the upper limit or else problems
        int finalPosition = position;
        ranks.stream().filter(positionRank -> positionRank.getPosition() >= finalPosition)
                .forEach(positionRank -> positionRank.setPosition(positionRank.getPosition() + 1));

        ranks.add(new PositionRank(position, rank.id));
        
        // Reset the rank relationships:
        PrisonRanks.getInstance().getRankManager().connectRanks();
    }

    /**
     * Add a rank to this ladder. The rank's position will be set to the next available position
     * (i.e. at the end of the ladder).
     *
     * @param rank The {@link Rank} to add.
     */
    public void addRank(Rank rank) {
        ranks.add(new PositionRank(getNextAvailablePosition(), rank.id));
        
        // Reset the rank relationships:
        PrisonRanks.getInstance().getRankManager().connectRanks();
    }

    /**
     * Removes a rank from this ladder.
     *
     * @param position The position of the rank to be removed. The positions of the rest of the
     *                 ranks will be downshifted to fill the gap.
     */
    public void removeRank(int position) {
        ranks.stream().filter(positionRank -> positionRank.getPosition() > position).forEach(
                positionRank -> positionRank.setPosition(positionRank.getPosition() - 1)
        );

        Iterator<PositionRank> iter = ranks.iterator();
        while (iter.hasNext()) {
            PositionRank rank = iter.next();
            if (rank.getPosition() == position) {
                iter.remove();
                break;
            }
        }
        
        // Reset the rank relationships:
        PrisonRanks.getInstance().getRankManager().connectRanks();
    }

    /**
     * Orders the ranks in the rank list of this ladder by their position, in ascending order.
     */
    public void orderRanksByPosition() {
        ranks.sort(Comparator.comparingInt(PositionRank::getPosition));
        
        // Reset the rank relationships:
        PrisonRanks.getInstance().getRankManager().connectRanks();
    }

    /*
     * Getters & Setters
     */

    /**
     * Returns true if this ladder contains a rank with a specified ID.
     *
     * @param rankId The ID of the rank to search for.
     * @return True if the rank was found, false otherwise.
     */
    public boolean containsRank(int rankId) {
        return ranks.stream().anyMatch(rank -> rank.getRankId() == rankId);
    }

    /**
     * Returns the position of the specified {@link Rank} in this ladder.
     *
     * @param rank The {@link Rank} to retrieve the position of.
     * @return The position of the rank, or -1 if the rank was not found.
     */
    public int getPositionOfRank(Rank rank) {
        for (PositionRank rankEntry : ranks) {
            if (rankEntry.getRankId() == rank.id) {
                return rankEntry.getPosition();
            }
        }

        return -1;
    }

    /**
     * Returns the next highest rank in the ladder.
     *
     * @param after The position of the current rank.
     * @return An optional containing either the rank if there is a next rank in the ladder, or
     * empty if there isn't or if the rank does not exist anymore.
     */
    public Optional<Rank> getNext(int after) {
        List<Integer> positions =
                ranks.stream().map(PositionRank::getPosition).sorted().collect(Collectors.toList());

        int newIndex = positions.indexOf(after) + 1;
        if (newIndex >= positions.size()) {
            return Optional.empty();
        }

        int nextPosition = positions.get(newIndex);
        return getByPosition(nextPosition);
    }

    /**
     * Returns the next lowest rank in the ladder.
     *
     * @param before The position of the current rank.
     * @return An optional containing either the rank if there is a previous rank in the ladder, or
     * empty if there isn't or if the rank does not exist anymore.
     */
    public Optional<Rank> getPrevious(int before) {
        List<Integer> positions =
                ranks.stream().map(PositionRank::getPosition).sorted().collect(Collectors.toList());

        int newIndex = positions.indexOf(before) - 1;
        if (newIndex < 0) {
            return Optional.empty();
        }

        int previousPosition = positions.get(newIndex);
        return getByPosition(previousPosition);
    }

    /**
     * Searches for and returns a rank in the ladder, depending on the position in the ladder.
     *
     * @param position The position to search for.
     * @return An optional containing the rank if it was found, or empty if it wasn't.
     */
    public Optional<Rank> getByPosition(int position) {
        for (PositionRank posRank : ranks) {
            if (posRank.getPosition() == position) {
                return PrisonRanks.getInstance().getRankManager().getRank(posRank.getRankId());
            }
        }

        return Optional.empty();
    }

    // This next method is sort of precautionary. Sure, positions start at 0, but if the user decides to be crazy
    // and alters the position within the data files, we need to make sure we adjust accordingly. Never trust
    // editable data!

    /**
     * Finds the lowest rank present in this ladder. It does so by checking to see which position is the lowest.
     *
     * @return The rank option, or an empty optional if there are no ranks in this ladder.
     */
    public Optional<Rank> getLowestRank() {
        if (ranks.isEmpty()) return Optional.empty();

        PositionRank lowest = ranks.get(0);
        for (PositionRank posRank : ranks) {
            if (posRank.getPosition() < lowest.getPosition()) {
                lowest = posRank;
            }
        }

        return PrisonRanks.getInstance().getRankManager().getRank(lowest.getRankId());
    }

    /**
     * Returns the next available position for a rank, by finding the highest one.
     *
     * @return The open position.
     */
    private int getNextAvailablePosition() {
        if (ranks.size() == 0) {
            return 0; // obviously, if it's empty, we want to start at the bottom
        }

        orderRanksByPosition();
        return ranks.get(ranks.size() - 1).getPosition() + 1;
    }

    /*
     * equals() and hashCode()
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RankLadder)) {
            return false;
        }

        RankLadder that = (RankLadder) o;

        return id == that.id && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        return result;
    }

    public class PositionRank {

        private int position;
        private int rankId;

        public PositionRank(int position, int rankId) {
            this.position = position;
            this.rankId = rankId;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getRankId() {
            return rankId;
        }

        public void setRankId(int rankId) {
            this.rankId = rankId;
        }
    }

}
