/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2016 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.prison.ranks;

/**
 * Represents a single rank. All values are serializable.
 *
 * @author Camouflage100
 */
public class Rank {

    private double cost;
    private int rankLadder;
    private int rankId;
    private String name;
    private String tag;

    public Rank() {
    }

    public Rank(int id, double cost, int rankLadder, String name) {
        this.cost = cost;
        this.rankLadder = rankLadder;
        this.name = name;
        this.rankId = id;
        this.tag = null;
    }

    public Rank(int id, double cost, int rankLadder, String name, String tag) {
        this.cost = cost;
        this.rankLadder = rankLadder;
        this.name = name;
        this.tag = tag;
        this.rankId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRankId() {
        return rankId;
    }

    public int getRankLadder() {
        return rankLadder;
    }

    public void setRankLadder(int rankLadder) {
        this.rankLadder = rankLadder;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
