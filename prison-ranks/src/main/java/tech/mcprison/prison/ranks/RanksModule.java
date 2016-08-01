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

package tech.mcprison.prison.ranks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.adapters.LocationAdapter;
import tech.mcprison.prison.internal.config.ConfigurationLoader;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.ranks.listeners.SignListener;
import tech.mcprison.prison.ranks.listeners.UserListener;
import tech.mcprison.prison.util.Location;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Camouflage100
 */
public class RanksModule extends Module {

    private File ranksDirectory;
    private File usersDirectory;
    private Gson gson;
    private List<Rank> ranks;
    private List<RankUser> users;
    private ConfigurationLoader messagesLoader;

    public RanksModule(String version) {
        super("Ranks", version);
        Prison.getInstance().getModuleManager().registerModule(this);
    }

    @Override public void enable() {
        File ranksModuleDir =
            new File(Prison.getInstance().getPlatform().getPluginDirectory(), "Ranks");
        if (!ranksModuleDir.exists())
            ranksModuleDir.mkdir();

        ranksDirectory = new File(ranksModuleDir, "ranks");
        if (!ranksDirectory.exists())
            ranksDirectory.mkdir();

        usersDirectory = new File(ranksModuleDir, "users");
        if (!usersDirectory.exists())
            usersDirectory.mkdir();

        this.messagesLoader =
            new ConfigurationLoader(ranksModuleDir, "messages.json", Messages.class,
                Messages.VERSION);
        this.messagesLoader.loadConfiguration();

        ranks = new ArrayList<>();
        users = new ArrayList<>();

        initGson();
        loadAllRanks();
        loadAllUsers();

        new UserListener(this).init();
        new SignListener(this).init();

        Prison.getInstance().getCommandHandler().registerCommands(new RankCommands(this));
    }

    @Override public void disable() {
        getRanks().forEach(this::saveRank);
    }

    private void initGson() {
        this.gson = new GsonBuilder().registerTypeAdapter(Location.class, new LocationAdapter())
            .setPrettyPrinting().disableHtmlEscaping().create();
    }

    protected void loadAllRanks() {
        File[] files = ranksDirectory.listFiles((dir, name) -> name.endsWith(".rank.json"));
        for (File file : files) {
            try {
                String json = new String(Files.readAllBytes(file.toPath()));
                Rank cell = gson.fromJson(json, Rank.class);
                ranks.add(cell);
            } catch (IOException e) {
                Prison.getInstance().getPlatform()
                    .log("&cError while loading rank file %s.", file.getName());
                e.printStackTrace();
            }
        }
    }

    public void saveRank(Rank rank) {
        if (getRank(rank.getRankId()) == null)
            ranks.add(rank);

        String json = gson.toJson(rank);
        try {
            Files.write(new File(ranksDirectory, rank.getRankId() + ".rank.json").toPath(),
                json.getBytes());
        } catch (IOException e) {
            Prison.getInstance().getPlatform()
                .log("&cError while saving rank %s.", rank.getRankId());
            e.printStackTrace();
        }
    }

    private void loadAllUsers() {
        File[] files = usersDirectory.listFiles((dir, name) -> name.endsWith(".user.json"));
        for (File file : files) {
            try {
                String json = new String(Files.readAllBytes(file.toPath()));
                RankUser user = gson.fromJson(json, RankUser.class);
                users.add(user);
            } catch (IOException e) {
                Prison.getInstance().getPlatform()
                    .log("&cError while loading user file %s.", file.getName());
                e.printStackTrace();
            }
        }
    }

    public void saveRankUser(RankUser user) {
        if (getUser(user.getUuid()) == null)
            users.add(user);
        user.setRank(getRank(user.getRank().getRankId()));
        String json = gson.toJson(user);
        try {
            File f = new File(usersDirectory, user.getUuid() + ".user.json");
            Files.write(f.toPath(), json.getBytes());
        } catch (IOException e) {
            Prison.getInstance().getPlatform().log("&cError while saving user %s.", user.getUuid());
            e.printStackTrace();
        }
    }

    public List<Rank> getRanks() {
        ranks.sort((o1, o2) -> String.valueOf(o1.getRankLadder())
            .compareTo(String.valueOf(o2.getRankLadder())));
        return ranks;
    }

    public int getNextLadder() {
        int highestId = 0;
        for (Rank rank : ranks) {
            if (rank.getRankLadder() > highestId)
                highestId = rank.getRankLadder();
        }

        return highestId + 1;
    }

    public Messages getMessages() {
        return (Messages) messagesLoader.getConfig();
    }

    public File getRanksDirectory() {
        return ranksDirectory;
    }

    public File getUsersDirectory() {
        return usersDirectory;
    }

    public Rank getRank(int id) {
        for (Rank rank : ranks)
            if (rank.getRankId() == id)
                return rank;
        return null;
    }

    public Rank getRankByName(String name) {
        for (Rank rank : ranks)
            if (rank.getName().toLowerCase().compareTo(name.toLowerCase()) == 0)
                return rank;
        return null;
    }

    public Rank getBottomRank() {
        for (int i = 0; i != -1; i++) {
            if (getRank(i) != null)
                return getRank(i);
        }
        return null;
    }

    public Rank getTopRank() {
        Rank topRank = getBottomRank();
        for (Rank rank : getRanks()) {
            if (rank.getRankLadder() > topRank.getRankLadder()) {
                topRank = rank;
            }
        }
        return topRank;
    }

    public Rank getRankByLadder(int id) {
        for (Rank rank : getRanks())
            if (rank.getRankLadder() == id)
                return rank;
        return null;
    }

    public Rank getRankByLadder(boolean up, Rank rank) {
        if (!up) {
            Rank rankBefore = null;

            for (Rank rank1 : getRanks()) {
                if (rank1.getRankLadder() == rank.getRankLadder())
                    return rankBefore;

                rankBefore = rank1;
            }
        } else {
            for (int i = rank.getRankLadder() + 1; i > 0; i++) {
                if (getRankByLadder(i) != null) {
                    return getRankByLadder(i);
                }
            }
        }

        return null;
    }

    public RankUser getUser(UUID uuid) {
        for (RankUser user : users)
            if (user.getUuid().compareTo(uuid) == 0)
                return user;
        return null;
    }

}
