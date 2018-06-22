package me.faizaand.prison.mines.managers;

import me.faizaand.prison.internal.GamePlayer;

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

    public boolean hasAutosmelt(GamePlayer player) {
        return hasAutosmelt(player.getUUID());
    }

    public boolean hasAutosmelt(UUID uuid) {
        check(uuid);
        return players.get(uuid).autosmelt;
    }

    public boolean hasAutopickup(GamePlayer player) {
        return hasAutopickup(player.getUUID());
    }

    public boolean hasAutopickup(UUID uuid) {
        check(uuid);
        return players.get(uuid).autopickup;
    }

    public boolean hasAutoblock(GamePlayer player) {
        return hasAutoblock(player.getUUID());
    }

    public boolean hasAutoblock(UUID uuid) {
        check(uuid);
        return players.get(uuid).autoblock;
    }

    public void setAutosmelt(GamePlayer player, boolean option) {
        setAutosmelt(player.getUUID(), option);
    }

    public void setAutosmelt(UUID uuid, boolean option) {
        check(uuid);
        players.get(uuid).autosmelt = option;
    }

    public void setAutopickup(GamePlayer player, boolean option) {
        setAutopickup(player.getUUID(), option);
    }

    public void setAutopickup(UUID uuid, boolean option) {
        check(uuid);
        players.get(uuid).autopickup = option;
    }

    public void setAutoblock(GamePlayer player, boolean option) {
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
