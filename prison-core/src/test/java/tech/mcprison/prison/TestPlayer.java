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

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.inventory.Inventory;
import tech.mcprison.prison.internal.scoreboard.Scoreboard;
import tech.mcprison.prison.util.Gamemode;
import tech.mcprison.prison.util.Location;

import java.util.*;

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

    @Override public void updateInventory() {
    }

    @Override public void dispatchCommand(String command) {

    }

    @Override public boolean hasPermission(String perm) {
        return true;
    }

    @Override public void sendMessage(String message) {
        input.add(message);
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

    @Override public Gamemode getGamemode() {
        return null;
    }

    @Override public void setGamemode(Gamemode gamemode) {

    }

    @Override public Optional<String> getLocale() {
        return Optional.of("en_US");
    }

    @Override public boolean isOp() {
        return true;
    }

    @Override public Inventory getInventory() {
        return null;
    }
}
