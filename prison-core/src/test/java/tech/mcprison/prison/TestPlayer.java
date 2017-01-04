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

package tech.mcprison.prison;

import tech.mcprison.prison.platform.ItemStack;
import tech.mcprison.prison.platform.Player;
import tech.mcprison.prison.platform.scoreboard.Scoreboard;
import tech.mcprison.prison.util.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author Faizaan A. Datoo
 */
public class TestPlayer implements Player {

    // just allows input to be given to the user

    private List<String> input = new ArrayList<>();

    public List<String> getInput() {
        return input;
    }

    @Override public String getName() {
        return "Testing";
    }

    @Override public void dispatchCommand(String command) {

    }

    @Override public boolean hasPermission(String perm) {
        return true;
    }

    @Override public void sendMessage(String message) {
        input.add(message);
    }

    @Override public void sendError(String error) {
        input.add(error);
    }

    @Override public void sendMessage(String[] messages) {
        input.addAll(Arrays.asList(messages));
    }

    @Override public void sendRaw(String json) {
        input.add(json);
    }

    @Override public UUID getUUID() {
        return null;
    }

    @Override public String getDisplayName() {
        return null;
    }

    @Override public void setDisplayName(String newDisplayName) {

    }

    @Override public void give(ItemStack itemStack) {

    }

    @Override public Location getLocation() {
        return null;
    }

    @Override public void teleport(Location location) {

    }

    @Override public boolean isOnline() {
        return true;
    }

    @Override public void setScoreboard(Scoreboard scoreboard) {

    }
}
