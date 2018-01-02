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

package tech.mcprison.prison.mines;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.gui.Action;
import tech.mcprison.prison.gui.Button;
import tech.mcprison.prison.gui.ClickedButton;
import tech.mcprison.prison.gui.GUI;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.localization.Localizable;
import tech.mcprison.prison.mines.util.Block;
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
public class MineManager implements List<Mine> {
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
    public MineManager() {
        mines = new ArrayList<>();
        randomizedBlocks = new HashMap<>();
        players = new HashMap<>();
    }

    /**
     * Gets the amount of mines in this {@link MineManager}
     *
     * @return the amount of loaded mines
     */
    public int size() {
        return mines.size();
    }

    /**
     * Returns true if there are no mines in this {@link MineManager}, false otherwise
     *
     * @return true if size() is equal to 0
     */
    public boolean isEmpty() {
        return mines.isEmpty();
    }

    /**
     * Check if there is an exact match of the specified {@link Mine} in this {@link MineManager}
     *
     * @param o the mine to check for
     * @return
     */
    public boolean contains(Object o) {
        return mines.contains(o);
    }

    /**
     * Gets the iterator of this {@link MineManager}
     *
     * @return the iterator
     */
    public Iterator<Mine> iterator() {
        return mines.iterator();
    }

    /**
     * Converts this {@link MineManager} to an array
     *
     * @return a Mine[] with all the mines contained in this instance
     */
    public Mine[] toArray() {
        return (Mine[]) mines.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return mines.toArray(a);
    }

    /**
     * Adds a {@link Mine} to this {@link MineManager} instance
     *
     * @param c the mine instance
     * @return if the add was successful
     */
    public boolean add(Mine c) {
        if (contains(c)) {
            return false;
        } else {
            return mines.add(c);
        }
    }

    /**
     * Removes an {@link Mine} from this {@link MineManager} instance (if the object is present)
     *
     * @param c
     * @return false if the mine was not found in this instance OR if the remove operation wasn't successful
     */
    public boolean remove(Object c) {
        if (!contains(c)) {
            return false;
        } else {
            return mines.remove(c);
        }
    }

    /**
     * Checks if this {@link MineManager} contains all of the objects in the specified colleciton
     *
     * @param c the collection
     * @return true if all of the objects are contained in this mine, false otherwise
     */
    public boolean containsAll(Collection c) {
        return mines.containsAll(c);
    }

    /**
     * Adds all of the specified {@link Mine}s to this {@link MineManager}
     *
     * @param c the collection to merge with this instance
     * @return true if the add operation succeeded
     */
    public boolean addAll(Collection<? extends Mine> c) {
        return mines.addAll(c);
    }

    /**
     * Adds all of the specified {@link Mine}s to this {@link MineManager} starting at the specified index
     *
     * @param c the collection to merge with this instance
     * @return true if the add operation succeeded
     */
    public boolean addAll(int index, Collection<? extends Mine> c) {
        return mines.addAll(index, c);
    }

    /**
     * Removes all of the objects contained within the given collection (if they are present)
     *
     * @param c the collection to remove from this instance
     * @return if the removal was successful
     */
    public boolean removeAll(Collection c) {
        return mines.removeAll(c);
    }

    /**
     * Removes all the mines from this {@link MineManager} except the ones contained
     * in the given collection
     *
     * @param c the items to keep in this instance
     * @return if the operation was successful
     */
    public boolean retainAll(Collection c) {
        return mines.retainAll(c);
    }

    /**
     * Removes all the mines contained within this {@link MineManager}
     */
    public void clear() {
        mines.clear();
    }

    /**
     * Gets a mine at the specified index
     *
     * @param index the index
     * @return the mine at the specified index
     */
    public Mine get(int index) {
        return mines.get(index);
    }

    /**
     * Replaces the mine at the specified index
     *
     * @param index   the index
     * @param element the mine to replace the old one with
     * @return the old mine
     */
    public Mine set(int index, Mine element) {
        return mines.set(index, element);
    }

    /**
     * Adds a mine at the specified index, incrementing the mine previously
     * occupying the index and all other mines after it (if any)
     *
     * @param index the index in which to insert the mine
     * @param c     the mine to insert
     */
    public void add(int index, Mine c) {
        mines.add(index, c);
    }

    /**
     * Removes the mine at the specified index
     *
     * @param index the index
     * @return the old mine
     */
    public Mine remove(int index) {
        return mines.remove(index);
    }

    /**
     * Gets the index of an element in this {@link MineManager}
     *
     * @param c the element to get the index of
     * @return the index of the element
     */
    public int indexOf(Object c) {
        return mines.indexOf(c);
    }

    /**
     * Gets the last index of an element in this {@link MineManager}
     *
     * @param c the element to get the last index of
     * @return the last index of the element
     */
    public int lastIndexOf(Object c) {
        return mines.lastIndexOf(c);
    }

    /**
     * Gets a {@link ListIterator} for this {@link MineManager}
     *
     * @return the iterator
     */
    public ListIterator<Mine> listIterator() {
        return mines.listIterator();
    }

    /**
     * Gets a {@link ListIterator} for this {@link MineManager} from the specified index
     *
     * @return the iterator
     */
    public ListIterator<Mine> listIterator(int index) {
        return mines.listIterator(index);
    }

    /**
     * Gets a section of this list (sublist) from the specified min and max indexes
     *
     * @param fromIndex the min index (start of the sublist)
     * @param toIndex   the max index (upper bound of the sublist)
     * @return
     */
    public MineManager subList(int fromIndex, int toIndex) {
        return (MineManager) mines.subList(fromIndex, toIndex);
    }

    /**
     * Checks for a mine with the specified name
     *
     * @param name the name to check for
     * @return true if this instance contains a mine with the specified name, false otherwise
     */
    public boolean contains(String name) {
        return select(new MinesFilter() {
            @Override public boolean accept(Mine c) {
                return c.getName().equalsIgnoreCase(name);
            }

            @Override public void action(Mine c) {

            }
        }).size() > 0;
    }

    /**
     * Creates a new list from this {@link MineManager}, but only with the elements accepted
     * by the specified {@link MinesFilter}
     *
     * @param filter the filter to use to create the new list
     * @return the new list
     * @see MinesFilter#accept(Mine)
     */
    public MineManager select(MinesFilter filter) {
        MineManager out = new MineManager();
        for (Mine c : this) {
            if (filter.accept(c)) {
                out.add(c);
            }
        }
        return out;
    }

    /**
     * Creates a new list from this {@link MineManager}, but only with the elements accepted
     * by the specified filter.
     *
     * @param filter the filter that should return true for items to keep, false otherwise.
     * @return the new list
     */
    public MineManager select(Predicate<? super Mine> filter) {
        MineManager out = new MineManager();
        out.addAll(this);
        out.removeIf(x -> !filter.test(x));
        return out;
    }

    /**
     * Loops through this {@link MineManager}, executing the action specified within the given
     * {@link MinesFilter}
     *
     * @param filter the filter that contains the loop action
     * @return this instance for chaining
     */
    public MineManager forEach(MinesFilter filter) {
        for (Mine c : this) {
            filter.action(c);
        }
        return this;
    }

    private void selectiveSend(Player x, Localizable localizable) {
        if (PrisonMines.get().getWorlds()
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
                if (PrisonMines.get().getConfig().resetTime == 0) {
                    return;
                }
                if (size() == 0) {
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

        if (PrisonMines.get().getConfig().resetMessages) {
            // Send it to everyone if it's not multi-world
            if (!PrisonMines.get().getConfig().multiworld) {
                Prison.get().getPlatform().getOnlinePlayers().forEach(
                    x -> PrisonMines.get().getMinesMessages().getLocalizable("reset_message")
                        .sendTo(x));
            } else { // Or those affected if it's multi-world
                Prison.get().getPlatform().getOnlinePlayers().forEach(x -> selectiveSend(x,
                    PrisonMines.get().getMinesMessages().getLocalizable("reset_message")));
            }
        }

        // And reset the count
        resetCount = PrisonMines.get().getConfig().resetTime;
    }

    private void broadcastResetWarnings() {
        if (!PrisonMines.get().getConfig().resetMessages) {
            return;
        }

        for (int i : PrisonMines.get().getConfig().resetWarningTimes) {
            if (resetCount == i) {
                if (!PrisonMines.get().getConfig().multiworld) {

                    Prison.get().getPlatform().getOnlinePlayers().forEach(
                        x -> PrisonMines.get().getMinesMessages().getLocalizable("reset_warning")
                            .withReplacements(Text.getTimeUntilString(resetCount * 1000))
                            .sendTo(x));
                } else {
                    Prison.get().getPlatform().getOnlinePlayers().forEach(x -> selectiveSend(x,
                        PrisonMines.get().getMinesMessages().getLocalizable("reset_warning")
                            .withReplacements(Text.getTimeUntilString(resetCount * 1000))));
                }
            }
        }
    }

    /**
     * Gets the mine with the specified name
     *
     * @param name the name to test for
     * @return the mine with the specified name or false if there is no mine with the name
     */
    public Mine get(String name) {
        MineManager sublist = select(new MinesFilter() {
            @Override public boolean accept(Mine c) {
                return c.getName().equalsIgnoreCase(name);
            }

            @Override public void action(Mine c) {

            }
        });
        if (sublist.size() == 0) {
            return null;
        }
        return sublist.get(0);
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

        Output.get().logInfo("Loaded " + size() + " mines");
        resetCount = PrisonMines.get().getConfig().resetTime;
        return this;
    }

    private boolean initColl() {
        Optional<tech.mcprison.prison.store.Collection> collOptional =
            PrisonMines.get().getDb().getCollection("mines");

        if (!collOptional.isPresent()) {
            PrisonMines.get().getDb().createCollection("mines");
            collOptional = PrisonMines.get().getDb().getCollection("mines");

            if (!collOptional.isPresent()) {
                Output.get().logError("Could not create 'mines' collection.");
                PrisonMines.get().getStatus()
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
                if (PrisonMines.get().getConfig().asyncReset) {
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
        for (Mine mine : this) {
            coll.insert(mine.getName(), mine.toDocument());
        }
    }

    /**
     * Resets all the mines in this list.
     */
    public void reset() {
        MinesFilter resetFilter = new MinesFilter() {
            @Override public boolean accept(Mine c) {
                return true;
            }

            @Override public void action(Mine c) {
                c.reset();
            }
        };
        forEach(resetFilter);
    }

    /**
     * Selects the mines accepted by the given filter, and resets them.
     *
     * @param resetFilter the filter
     * @see MinesFilter#accept(Mine)
     */
    public void reset(MinesFilter resetFilter) {
        select(resetFilter).forEach(Mine::reset);
    }

    /**
     * Checks the specified location to see if it is within a mine in this list.
     *
     * @param location the location to test
     * @return true if the location is within a mine in this list, false otherwise
     */
    public boolean isInMine(Location location) {
        return select(new MinesFilter() {
            @Override public boolean accept(Mine c) {
                return c.isInMine(location);
            }

            @Override public void action(Mine c) {

            }
        }).size() > 0;
    }

    /**
     * Gets randomized blocks for the specified mine
     *
     * @param m the mine to randomize
     * @return randomized blocks
     */
    public List<BlockType> getRandomizedBlocks(Mine m) {
        if (!randomizedBlocks.containsKey(m)) {
            generateBlockList(m);
        }
        List<BlockType> out = randomizedBlocks.get(m);
        randomizedBlocks.remove(m, out);
        return out;
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
     * Checks if the specified player can mine in/teleport to the specified mine
     *
     * @param player the player to test
     * @param mine   the mine to test
     * @return true if there are no teleport rules created for the player OR
     * the teleport rule allows the player to mine in/teleport to the mine, false
     * if the teleport rule exists and doesn't contain the specified mine.
     */
    public boolean canTeleport(Player player, Mine mine) {
        if (getTeleportRule(player) == null) {
            return true;
        } else {
            return getTeleportRule(player).contains(mine);
        }
    }

    /**
     * Checks if the specified player can mine in/teleport to the specified mine
     *
     * @param uuid the uuid to test
     * @param mine the mine to test
     * @return true if there are no teleport rules created for the player OR
     * the teleport rule allows the player to mine in/teleport to the mine, false
     * if the teleport rule exists and doesn't contain the specified mine.
     */
    public boolean canTeleport(UUID uuid, Mine mine) {
        if (getTeleportRule(uuid) == null) {
            return true;
        } else {
            return getTeleportRule(uuid).contains(mine);
        }
    }

    /**
     * Checks if a player can mine in the location. Always true if the location isn't within
     * any mines, otherwise it checks teleport rules.
     *
     * @param player   the player to test
     * @param location the location to test
     * @return true if the player is allowed to mine in this mine, false otherwise.
     */
    public boolean allowedToMine(Player player, Location location) {
        MineManager sublist = select(new MinesFilter() {
            @Override public boolean accept(Mine c) {
                return c.isInMine(location);
            }

            @Override public void action(Mine c) {

            }
        });
        if (sublist.size() > 1) {
            Output.get().logWarn(
                "Potential overlap in mines -- there are " + sublist.size() + " mines at location "
                    + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ()
                    + " in world " + location.getWorld().getName());
            forEach(x -> Output.get().logWarn(x.getName()));
        }
        if (sublist.select(new MinesFilter() {
            @Override public boolean accept(Mine c) {
                return !canTeleport(player, c);
            }

            @Override public void action(Mine c) {

            }
        }).size() == 0) {
            return true;
        }
        return false;
    }


    /**
     * Checks if a player can mine in the location. Always true if the location isn't within
     * any mines, otherwise it checks teleport rules.
     *
     * @param uuid     the uuid of a player to test
     * @param location the location to test
     * @return true if the player is allowed to mine in this mine, false otherwise.
     */
    public boolean allowedToMine(UUID uuid, Location location) {
        MineManager sublist = select(new MinesFilter() {
            @Override public boolean accept(Mine c) {
                return c.isInMine(location);
            }

            @Override public void action(Mine c) {

            }
        });
        if (sublist.size() > 1) {
            Output.get().logWarn(
                "Potential overlap in mines -- there are " + sublist.size() + " mines at location "
                    + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ()
                    + " in world " + location.getWorld().getName());
            forEach(x -> Output.get().logWarn(x.getName()));
        }
        if (sublist.select(new MinesFilter() {
            @Override public boolean accept(Mine c) {
                return !canTeleport(uuid, c);
            }

            @Override public void action(Mine c) {

            }
        }).size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * Clears cached randomized blocks for this mine
     *
     * @param mineName the name of the mine
     */
    public void clearCache(String mineName) {
        randomizedBlocks.entrySet().removeIf(x -> x.getKey().getName().equalsIgnoreCase(mineName));
    }

    /**
     * Clears all of the cached randomized blocks
     */
    public void clearCache() {
        randomizedBlocks.clear();
    }

}
