/*
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

package tech.mcprison.prison.mines.managers;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.localization.Localizable;
import tech.mcprison.prison.mines.legacy.MinesFilter;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.data.Block;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.store.Document;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.Location;
import tech.mcprison.prison.util.Text;

import java.util.*;
import java.util.function.Predicate;

/**
 * Represents a collection of mines which can be iterated through in a normal <i>for</i> loop
 */
public class MineManager {
    // Base list
    List<Mine> mines;

    tech.mcprison.prison.store.Collection coll;

    // Declarations
    HashMap<Mine, List<BlockType>> randomizedBlocks;
    int resetCount = 0;

    // NPE
    private HashMap<UUID, MineManager> players;

    // Inherited methods -- don't know why I make things so difficult

    /**
     * Initializes a new instance of {@link MineManager}
     */
    public MineManager(tech.mcprison.prison.store.Collection collection) {
        mines = new ArrayList<>();
        randomizedBlocks = new HashMap<>();
        players = new HashMap<>();
        coll = collection;
    }

    /**
     * Adds a {@link Mine} to this {@link MineManager} instance
     *
     * @param c the mine instance
     * @return if the add was successful
     */
    public boolean add(Mine c) {
        if (mines.contains(c)) {
            return false;
        } else {
            return mines.add(c);
        }
    }

    private void selectiveSend(Player x, Localizable localizable) {
        if (PrisonMines.getInstance().getWorlds()
            .contains(x.getLocation().getWorld().getName().toLowerCase())) {
            localizable.sendTo(x);
        }
    }

    /**
     * Gets the {@link TimerTask} for the reset timer of this {@link MineManager}
     *
     * @return
     */
    public TimerTask getTimerTask() {
        return new TimerTask() {
            @Override public void run() {
                // Perform initial checks
                if (PrisonMines.getInstance().getConfig().resetTime == 0) {
                    return;
                }
                if (mines.size() == 0) {
                    return;
                }

                // It's time to reset
                if (resetCount == 0) {
                    resetMines();
                } else {
                    broadcastResetWarnings();
                }

                if (resetCount > 0) {
                    resetCount--;
                }
            }
        };
    }

    private void resetMines() {
        reset();

        if (PrisonMines.getInstance().getConfig().resetMessages) {
            // Send it to everyone if it's not multi-world
            if (!PrisonMines.getInstance().getConfig().multiworld) {
                Prison.get().getPlatform().getOnlinePlayers().forEach(
                    x -> PrisonMines.getInstance().getMinesMessages().getLocalizable("reset_message")
                        .sendTo(x));
            } else { // Or those affected if it's multi-world
                Prison.get().getPlatform().getOnlinePlayers().forEach(x -> selectiveSend(x,
                    PrisonMines.getInstance().getMinesMessages().getLocalizable("reset_message")));
            }
        }

        // And reset the count
        resetCount = PrisonMines.getInstance().getConfig().resetTime;
    }

    private void broadcastResetWarnings() {
        if (!PrisonMines.getInstance().getConfig().resetMessages) {
            return;
        }

        for (int i : PrisonMines.getInstance().getConfig().resetWarningTimes) {
            if (resetCount == i) {
                if (!PrisonMines.getInstance().getConfig().multiworld) {

                    Prison.get().getPlatform().getOnlinePlayers().forEach(
                        x -> PrisonMines.getInstance().getMinesMessages().getLocalizable("reset_warning")
                            .withReplacements(Text.getTimeUntilString(resetCount * 1000))
                            .sendTo(x));
                } else {
                    Prison.get().getPlatform().getOnlinePlayers().forEach(x -> selectiveSend(x,
                        PrisonMines.getInstance().getMinesMessages().getLocalizable("reset_warning")
                            .withReplacements(Text.getTimeUntilString(resetCount * 1000))));
                }
            }
        }
    }

    /**
     * Initializes this {@link MineManager}. This should only be used for the instance created by
     * {@link PrisonMines}
     *
     * @return the initialized list or null if it couldn't initialize
     */
    public MineManager initialize() {
        mines = new ArrayList<>();

        if (!initColl()) {
            return null;
        }

        loadAll();

        Output.get().logInfo("Loaded " + mines.size() + " mines");
        resetCount = PrisonMines.getInstance().getConfig().resetTime;
        return this;
    }

    private boolean initColl() {
        Optional<tech.mcprison.prison.store.Collection> collOptional =
            PrisonMines.getInstance().getDb().getCollection("mines");

        if (!collOptional.isPresent()) {
            PrisonMines.getInstance().getDb().createCollection("mines");
            collOptional = PrisonMines.getInstance().getDb().getCollection("mines");

            if (!collOptional.isPresent()) {
                Output.get().logError("Could not create 'mines' collection.");
                PrisonMines.getInstance().getStatus()
                    .toFailed("Could not create mines collection in storage.");
                return false;
            }
        }

        coll = collOptional.get();
        return true;
    }

    private void loadAll() {
        List<Document> mineDocuments = coll.getAll();

        for (Document document : mineDocuments) {
            try {
                Mine m = new Mine(document);
                add(m);
                if (PrisonMines.getInstance().getConfig().asyncReset) {
                    generateBlockList(m);
                }
            } catch (Exception e) {
                Output.get()
                    .logError("&cFailed to load mine " + document.getOrDefault("name", "null"), e);
            }
        }
    }

    /**
     * Saves all the mines in this list. This should only be used for the instance created by
     * {@link PrisonMines}
     */
    public void save() {
        for (Mine mine : mines) {
            coll.insert(mine.getName(), mine.toDocument());
        }
    }

    /**
     * Resets all the mines in this list.
     */
    public void reset() {
        mines.forEach(Mine::reset);
    }

    /**
     * Generates blocks for the specified mine and caches the result
     *
     * @param m the mine to randomize
     */
    public void generateBlockList(Mine m) {
        Random random = new Random();
        ArrayList<BlockType> blocks = new ArrayList<>();

        Location min = m.getBounds().getMin();
        Location max = m.getBounds().getMax();

        int maxX = Math.max(min.getBlockX(), max.getBlockX());
        int minX = Math.min(min.getBlockX(), max.getBlockX());
        int maxY = Math.max(min.getBlockY(), max.getBlockY());
        int minY = Math.min(min.getBlockY(), max.getBlockY());
        int maxZ = Math.max(min.getBlockZ(), max.getBlockZ());
        int minZ = Math.min(min.getBlockZ(), max.getBlockZ());
        double target = ((maxY + 1) - minY) * ((maxX + 1) - minX) * ((maxZ + 1) - minZ);

        for (int i = 0; i < target; i++) {
            int chance = random.nextInt(101);
            boolean set = false;
            for (Block block : m.getBlocks()) {
                if (chance <= block.chance) {
                    blocks.add(block.type);
                    set = true;
                    break;
                } else {
                    chance -= block.chance;
                }
            }
            if (!set) {
                blocks.add(BlockType.AIR);
            }
        }
        randomizedBlocks.put(m, blocks);
    }

    /**
     * Gets the randomized blocks cache
     *
     * @return a hashmap with all the randomized blocks
     */
    public HashMap<Mine, List<BlockType>> getRandomizedBlocks() {
        return randomizedBlocks;
    }

    /**
     * Adds a teleport rule. Teleport rules allow players only to teleport to/mine in certain mines.
     *
     * @param player  the player to add a teleport rule for
     * @param sublist
     */
    public void addTeleportRule(Player player, MineManager sublist) {
        if (players == null) {
            players = new HashMap<>();
        }
        players.put(player.getUUID(), sublist);
    }


    /**
     * Adds a teleport rule. Teleport rules allow players only to teleport to/mine in certain mines.
     *
     * @param uuid    the player's uuid to add a teleport rule for
     * @param sublist
     */
    public void addTeleportRule(UUID uuid, MineManager sublist) {
        if (players == null) {
            players = new HashMap<>();
        }
        players.put(uuid, sublist);
    }

    /**
     * Removed all teleport rules for the specified player
     *
     * @param player
     */
    public void removeTeleportRule(Player player) {
        players.remove(player.getUUID());
    }


    /**
     * Removed all teleport rules for the specified UUID
     *
     * @param uuid
     */
    public void removeTeleportRule(UUID uuid) {
        players.remove(uuid);
    }

    /**
     * Gets all the mines that the specified player is allowed to teleport to/mine in
     *
     * @param player the player
     * @return the teleport rule
     */
    public MineManager getTeleportRule(Player player) {
        return players.get(player.getUUID());
    }

    /**
     * Gets all the mines that the specified player is allowed to teleport to/mine in
     *
     * @param uuid the player's UUID
     * @return the teleport rule
     */
    public MineManager getTeleportRule(UUID uuid) {
        return players.get(uuid);
    }

    /**
     * Returns the mine with the specified name.
     *
     * @param name The mine's name, case-sensitive.
     * @return An optional containing either the {@link Mine} if it could be found, or empty if it does not exist by the specified name.
     */
    public Optional<Mine> getMine(String name) {
        return mines.stream().filter(mine -> mine.getName().equals(name)).findFirst();
    }

    /**
     * Clears all of the cached randomized blocks
     */
    public void clearCache() {
        randomizedBlocks.clear();
    }

    public List<Mine> getMines() { return mines; }

}
