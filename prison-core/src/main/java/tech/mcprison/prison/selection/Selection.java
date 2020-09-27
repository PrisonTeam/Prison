/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017-2020 The Prison Team
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

package tech.mcprison.prison.selection;

import tech.mcprison.prison.util.Bounds;
import tech.mcprison.prison.util.Location;

/**
 * Represents an individual selection.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class Selection {

    private Location min, max;

    public Selection() {
    }

    public Selection(Location min, Location max) {
        this.min = min;
        this.max = max;
    }

    public Location getMin() {
        return min;
    }

    public void setMin(Location min) {
        this.min = min;
    }

    public Location getMax() {
        return max;
    }

    public void setMax(Location max) {
        this.max = max;
    }

    /**
     * Returns whether or not both the minimum and maximum locations are set.
     *
     * @return true if they are, false otherwise.
     */
    public boolean isComplete() {
        return min != null && max != null;
    }

    public Bounds asBounds() {
        return new Bounds(min, max);
    }

}
