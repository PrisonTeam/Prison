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

import tech.mcprison.prison.ranks.RankUtil;
import tech.mcprison.prison.store.Document;

import java.util.List;

/**
 * Represents a single rank.
 *
 * @author Faizaan A. Datoo
 */
public class Rank {

    /*
     * Fields & Constants
     */

    // The unique identifier used to distinguish this rank from others - this never changes.
    public int id;

    // The name of this rank, which is used with the user to identify ranks.
    public String name;

    // The tag that this rank has. It can be used as either a prefix or a suffix, depending on user preferences.
    public String tag;

    // The general cost of this rank, unit-independent. This value holds true for both XP and cost.
    public double cost;

    // The commands that are run when this rank is attained.
    public List<String> rankUpCommands;

    /*
     * Document-related
     */

    public Rank() {
    }

    public Rank(Document document) {
        this.id = RankUtil.doubleToInt(document.get("id"));
        this.name = (String) document.get("name");
        this.tag = (String) document.get("tag");
        this.cost = (double) document.get("cost");
        this.rankUpCommands = (List<String>) document.get("commands");
    }

    public Document toDocument() {
        Document ret = new Document();
        ret.put("id", this.id);
        ret.put("name", this.name);
        ret.put("tag", this.tag);
        ret.put("cost", this.cost);
        ret.put("commands", this.rankUpCommands);
        return ret;
    }

    /*
     * equals() and hashCode()
     */

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rank)) {
            return false;
        }

        Rank rank = (Rank) o;

        if (id != rank.id) {
            return false;
        }
        if (Double.compare(rank.cost, cost) != 0) {
            return false;
        }
        if (!name.equals(rank.name)) {
            return false;
        }
        return tag != null ? tag.equals(rank.tag) : rank.tag == null;
    }

    @Override public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        temp = Double.doubleToLongBits(cost);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

}
