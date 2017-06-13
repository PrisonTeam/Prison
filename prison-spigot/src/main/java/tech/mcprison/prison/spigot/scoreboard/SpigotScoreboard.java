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

package tech.mcprison.prison.spigot.scoreboard;

import tech.mcprison.prison.internal.scoreboard.DisplaySlot;
import tech.mcprison.prison.internal.scoreboard.Objective;
import tech.mcprison.prison.internal.scoreboard.Scoreboard;
import tech.mcprison.prison.internal.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Faizaan A. Datoo
 */
public class SpigotScoreboard implements Scoreboard {

    private org.bukkit.scoreboard.Scoreboard bScoreboard;

    public SpigotScoreboard(org.bukkit.scoreboard.Scoreboard bScoreboard) {
        this.bScoreboard = bScoreboard;
    }

    @Override public Objective getObjective(DisplaySlot slot) {
        return new SpigotObjective(
            bScoreboard.getObjective(org.bukkit.scoreboard.DisplaySlot.valueOf(slot.name())));
    }

    @Override public Objective registerNewObjective(String name, String criteria) {
        return new SpigotObjective(bScoreboard.registerNewObjective(name, criteria));
    }

    @Override public void resetScores(String name) {
        bScoreboard.resetScores(name);
    }

    @Override public Team registerNewTeam(String name) {
        return new SpigotTeam(bScoreboard.registerNewTeam(name));
    }

    @Override public List<Team> getTeams() {
        Set<org.bukkit.scoreboard.Team> teams = bScoreboard.getTeams();
        List<Team> ret = new ArrayList<>();

        teams.forEach((team -> ret.add(new SpigotTeam(team))));

        return ret;
    }

    public org.bukkit.scoreboard.Scoreboard getWrapper() {
        return bScoreboard;
    }

}
