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

package io.github.prison.mines;

import io.github.prison.util.Bounds;

/**
 * Data structure for a mine.
 * This is completely serializable by the module's Gson instance.
 *
 * @author SirFaizdat
 */
public class Mine {

    private String name;
    private Bounds bounds;
    private boolean isSync = true; // Sync mines reset all together
    private long resetTimeSeconds = 300L;

    public Mine(String name, Bounds bounds) {
        this.name = name;
        this.bounds = bounds;
    }

    public String getName() {
        return name;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }

    public long getResetTimeSeconds() {
        return resetTimeSeconds;
    }

    public void setResetTimeSeconds(long resetTimeSeconds) {
        this.resetTimeSeconds = resetTimeSeconds;
    }

}
