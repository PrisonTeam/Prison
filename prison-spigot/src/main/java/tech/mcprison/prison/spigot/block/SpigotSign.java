/*
 * Prison is a Minecraft plugin for the prison game mode.
 * Copyright (C) 2018 The Prison Team
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

package tech.mcprison.prison.spigot.block;

import com.google.common.collect.ImmutableList;
import tech.mcprison.prison.internal.block.Sign;

import java.util.List;

/**
 * @author Faizaan A. Datoo
 */
public class SpigotSign extends SpigotBlockState implements Sign {

    public SpigotSign(SpigotBlock block) {
        super(block);
    }

    @Override public List<String> getLines() {
        org.bukkit.block.Sign bSign = (org.bukkit.block.Sign) block.getWrapper().getState();
        return ImmutableList.copyOf(bSign.getLines());
    }

    @Override public void setLines(List<String> lines) {
        for (int i = 0; i < Math.max(lines.size(), 4); i++) {
            setLine(i, lines.get(i));
        }
    }

    @Override public void setLine(int line, String text) {
        org.bukkit.block.Sign bSign = (org.bukkit.block.Sign) block.getWrapper().getState();
        bSign.setLine(line, text);
        bSign.update();
    }

}
