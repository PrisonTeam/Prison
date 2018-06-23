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

package me.faizaand.prison.mines.managers;

import me.faizaand.prison.Prison;
import me.faizaand.prison.internal.GamePlayer;
import me.faizaand.prison.localization.Localizable;
import me.faizaand.prison.mines.MineException;
import me.faizaand.prison.mines.PrisonMines;
import me.faizaand.prison.mines.data.Block;
import me.faizaand.prison.mines.data.Mine;
import me.faizaand.prison.output.Output;
import me.faizaand.prison.store.Document;
import me.faizaand.prison.util.BlockType;
import me.faizaand.prison.util.GameLocation;
import me.faizaand.prison.util.Text;

import java.io.IOException;
import java.util.*;

/**
 * Manages the creation, removal, and management of mines.
 *
 * @author Dylan M. Perks
 */
public class MineManager {

    // Base list
    List<Mine> mines;

    me.faizaand.prison.store.Collection coll;

    // Declarations
    HashMap<String, List<BlockType>> randomizedBlocks;
    int resetCount = 0;

    /**
     * Initializes a new instance of {@link MineManager}
     */
    public MineManager(me.faizaand.prison.store.Collection collection) {
        mines = new ArrayList<>();
        randomizedBlocks = new HashMap<>();
        coll = collection;
        mines = new ArrayList<>();

        loadMines();

        Output.get().logInfo("Loaded " + mines.size() + " mines");
        resetCount = PrisonMines.getInstance().getConfig().resetTime;
    }

    public void loadMine(String mineFile) throws IOException, MineException {
        Document document = coll.get(mineFile).orElseThrow(IOException::new);
        mines.add(new Mine(document));
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

    private void selectiveSend(GamePlayer x, Localizable localizable) {
        if (PrisonMines.getInstance().getWorlds()
            .contains(x.getLocation().getWorld().getName().toLowerCase())) {
            localizable.sendTo(x);
        }
    }

    /**
     * Gets the {@link TimerTask} for the reset timer of this {@link MineManager}
     */
    public TimerTask getTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
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
        mines.forEach(Mine::reset);

        if (PrisonMines.getInstance().getConfig().resetMessages) {
            // Send it to everyone if it's not multi-world
            if (!PrisonMines.getInstance().getConfig().multiworld) {
                Prison.get().getPlatform().getPlayerManager().getOnlinePlayers().forEach(
                    x -> PrisonMines.getInstance().getMinesMessages()
                        .getLocalizable("reset_message")
                        .sendTo(x));
            } else { // Or those affected if it's multi-world
                Prison.get().getPlatform().getPlayerManager().getOnlinePlayers().forEach(x -> selectiveSend(x,
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

                    Prison.get().getPlatform().getPlayerManager().getOnlinePlayers().forEach(
                        x -> PrisonMines.getInstance().getMinesMessages()
                            .getLocalizable("reset_warning")
                            .withReplacements(Text.getTimeUntilString(resetCount * 1000))
                            .sendTo(x));
                } else {
                    Prison.get().getPlatform().getPlayerManager().getOnlinePlayers().forEach(x -> selectiveSend(x,
                        PrisonMines.getInstance().getMinesMessages().getLocalizable("reset_warning")
                            .withReplacements(Text.getTimeUntilString(resetCount * 1000))));
                }
            }
        }
    }

    public boolean removeMine(String id){
        if (getMine(id).isPresent()) {
            return removeMine(getMine(id).get());
        }
        else{
            return false;
        }
    }

    public boolean removeMine(Mine mine){
        return mines.remove(mine);
    }

    public static MineManager fromDb() {
        Optional<me.faizaand.prison.store.Collection> collOptional =
            PrisonMines.getInstance().getDb().getCollection("mines");

        if (!collOptional.isPresent()) {
            PrisonMines.getInstance().getDb().createCollection("mines");
            collOptional = PrisonMines.getInstance().getDb().getCollection("mines");

            if (!collOptional.isPresent()) {
                Output.get().logError("Could not create 'mines' collection.");
                PrisonMines.getInstance().getStatus()
                    .toFailed("Could not create mines collection in storage.");
                return null;
            }
        }

        return new MineManager(collOptional.get());
    }

    private void loadMines() {
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
     * Saves all the mines in this list. This should only be used for the instance created by {@link
     * PrisonMines}
     */
    public void saveMine(Mine mine) {
        coll.insert(mine.getName(), mine.toDocument());
    }

    public void saveMines(){
        for (Mine m : mines){
            saveMine(m);
        }
    }

    /**
     * Generates blocks for the specified mine and caches the result
     *
     * @param m the mine to randomize
     */
    public void generateBlockList(Mine m) {
        Random random = new Random();
        ArrayList<BlockType> blocks = new ArrayList<>();

        GameLocation min = m.getBounds().getMin();
        GameLocation max = m.getBounds().getMax();

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
        randomizedBlocks.put(m.getName(), blocks);
    }

    /**
     * Gets the randomized blocks cache
     *
     * @return a hashmap with all the randomized blocks
     */
    public HashMap<String, List<BlockType>> getRandomizedBlocks() {
        return randomizedBlocks;
    }

    /**
     * Returns the mine with the specified name.
     *
     * @param name The mine's name, case-sensitive.
     * @return An optional containing either the {@link Mine} if it could be found, or empty if it
     * does not exist by the specified name.
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

    public List<Mine> getMines() {
        return mines;
    }

}
