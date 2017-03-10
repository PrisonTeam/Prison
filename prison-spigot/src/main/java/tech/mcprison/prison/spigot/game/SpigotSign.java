/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017 The Prison Team
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

package tech.mcprison.prison.spigot.game;

import org.apache.commons.lang.Validate;
import tech.mcprison.prison.internal.Sign;
import tech.mcprison.prison.util.Location;

import java.util.List;

/**
 * @author Faizaan A. Datoo
 */
public class SpigotSign implements Sign {

    private org.bukkit.block.Sign bSign;

    public SpigotSign(org.bukkit.block.Sign bSign) {
        this.bSign = bSign;
    }

    @Override public Location getLocation() {
        org.bukkit.Location bLoc = bSign.getLocation();
        return new Location(new SpigotWorld(bLoc.getWorld()), bLoc.getX(), bLoc.getY(),
            bLoc.getZ());
    }

    @Override public String[] getLines() {
        return bSign.getLines();
    }

    @Override public void setLines(List<String> lines) {
        for (int i = 0; i < 4; i++) {
            bSign.setLine(i, lines.get(i));
        }
        bSign.update();
    }

    @Override public void setLine(int line, String value) {
        Validate.isTrue(line >= 0 && line < 4, "line must be 0 <= line < 4");
        bSign.setLine(line, value);
        bSign.update();
    }

    public org.bukkit.block.Sign getWrapper() {
        return bSign;
    }

}
