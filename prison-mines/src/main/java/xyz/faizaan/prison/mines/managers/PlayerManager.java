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

package xyz.faizaan.prison.mines.managers;

import xyz.faizaan.prison.internal.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

    public PlayerManager() {
        players = new HashMap<>();
    }

    public class MinesPlayer {

        public boolean autosmelt;
        public boolean autopickup;
        public boolean autoblock;

        public MinesPlayer init() {
            autosmelt = false;
            autopickup = false;
            autoblock = false;
            return this;
        }
    }

    HashMap<UUID, MinesPlayer> players;

    public boolean hasAutosmelt(Player player) {
        return hasAutosmelt(player.getUUID());
    }

    public boolean hasAutosmelt(UUID uuid) {
        check(uuid);
        return players.get(uuid).autosmelt;
    }

    public boolean hasAutopickup(Player player) {
        return hasAutopickup(player.getUUID());
    }

    public boolean hasAutopickup(UUID uuid) {
        check(uuid);
        return players.get(uuid).autopickup;
    }

    public boolean hasAutoblock(Player player) {
        return hasAutoblock(player.getUUID());
    }

    public boolean hasAutoblock(UUID uuid) {
        check(uuid);
        return players.get(uuid).autoblock;
    }

    public void setAutosmelt(Player player, boolean option) {
        setAutosmelt(player.getUUID(), option);
    }

    public void setAutosmelt(UUID uuid, boolean option) {
        check(uuid);
        players.get(uuid).autosmelt = option;
    }

    public void setAutopickup(Player player, boolean option) {
        setAutopickup(player.getUUID(), option);
    }

    public void setAutopickup(UUID uuid, boolean option) {
        check(uuid);
        players.get(uuid).autopickup = option;
    }

    public void setAutoblock(Player player, boolean option) {
        setAutoblock(player.getUUID(), option);
    }

    public void setAutoblock(UUID uuid, boolean option) {
        check(uuid);
        players.get(uuid).autoblock = option;
    }

    private void check(UUID uuid){
        if (!players.containsKey(uuid)){
            players.put(uuid,new MinesPlayer().init());
        }
    }
}
