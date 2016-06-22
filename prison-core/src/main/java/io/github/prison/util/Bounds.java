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

package io.github.prison.util;

/**
 * Represents the area between two {@link Location}s.
 *
 * @author SirFaizdat
 * @since 3.0
 */
public class Bounds {

    private Location min, max;

    public Bounds() {
    }

    public Bounds(Location min, Location max) {
        this.min = min;
        this.max = max;
    }

    public double getWidth() {
        return Math.max(min.getX(), max.getX()) - Math.min(min.getX(), max.getX());
    }

    public double getHeight() {
        return Math.max(min.getY(), max.getY()) - Math.min(min.getY(), max.getY());
    }

    public double getLength() {
        return Math.max(min.getZ(), max.getZ()) - Math.min(min.getZ(), max.getZ());
    }

    public double getArea() {
        return getWidth() * getHeight() * getLength();
    }

    public boolean within(Location location) {
        double minX = Math.min(min.getX(), max.getX());
        double minY = Math.min(min.getY(), max.getY());
        double minZ = Math.min(min.getZ(), max.getZ());
        double maxX = Math.max(min.getX(), max.getX());
        double maxY = Math.max(min.getY(), max.getY());
        double maxZ = Math.max(min.getZ(), max.getZ());

        return location.getX() >= minX && location.getX() <= maxX // Within X
                && location.getY() >= minY && location.getY() <= maxY // Within Y
                && location.getZ() >= minZ && location.getZ() <= maxZ; // Within Z
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

    @Override
    public String toString() {
        return "Bounds{" +
                "min=" + min.toCoordinates() +
                ", max=" + max.toCoordinates() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bounds)) return false;

        Bounds bounds = (Bounds) o;

        if (min != null ? !min.equals(bounds.min) : bounds.min != null) return false;
        return max != null ? max.equals(bounds.max) : bounds.max == null;

    }

    @Override
    public int hashCode() {
        int result = min != null ? min.hashCode() : 0;
        result = 31 * result + (max != null ? max.hashCode() : 0);
        return result;
    }

}
