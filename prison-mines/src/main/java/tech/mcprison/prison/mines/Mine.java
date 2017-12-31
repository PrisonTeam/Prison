/*
 * Prison is a Minecraft plugin for the prison game mode.
 * Copyright (C) 2017 The Prison Team
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

package tech.mcprison.prison.mines;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.mines.events.MineResetEvent;
import tech.mcprison.prison.mines.util.Block;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.store.Document;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.Bounds;
import tech.mcprison.prison.util.Location;

import java.util.*;

/**
 * @author Dylan M. Perks
 */
public class Mine {

    /*
     * Fields & Constants
     */

    private Location min, max, spawn;
    private String worldName, name;
    private boolean hasSpawn = false;

    private List<Block> blocks;

    /*
     * Constructors
     */

    /**
     * Creates a new, empty mine instance
     */
    public Mine() {
        blocks = new ArrayList<>();
    }

    /**
     * Loads a mine from a document.
     *
     * @param document The document to load from.
     * @throws Exception If the mine couldn't be loaded from the document.
     */
    public Mine(Document document) throws Exception {
        Optional<World> worldOptional =
            Prison.get().getPlatform().getWorld((String) document.get("world"));
        if (!worldOptional.isPresent()) {
            throw new Exception("world doesn't exist");
        }

        min = new Location(worldOptional.get(), (double) document.get("minX"),
            (double) document.get("minY"), (double) document.get("minZ"));
        max = new Location(worldOptional.get(), (double) document.get("maxX"),
            (double) document.get("maxY"), (double) document.get("maxZ"));

        hasSpawn = (boolean) document.get("hasSpawn");
        if (hasSpawn) {
            spawn = new Location(worldOptional.get(), (double) document.get("spawnX"),
                (double) document.get("spawnY"), (double) document.get("spawnZ"),
                ((Double) document.get("spawnPitch")).floatValue(),
                ((Double) document.get("spawnYaw")).floatValue());
        }

        worldName = worldOptional.get().getName();
        name = (String) document.get("name");

        blocks = new ArrayList<>();
        List<String> docBlocks = (List<String>) document.get("blocks");
        for (String docBlock : docBlocks) {
            String[] split = docBlock.split("-");
            String id = split[0];
            double chance = Double.parseDouble(split[1]);

            Block block = new Block();
            block.create(BlockType.getBlock(id), chance);
            blocks.add(block);
        }
    }

    /*
     * Methods
     */

    public Document toDocument() {
        Document ret = new Document();
        ret.put("world", worldName);
        ret.put("name", name);
        ret.put("minX", min.getX());
        ret.put("minY", min.getY());
        ret.put("minZ", min.getZ());
        ret.put("maxX", max.getX());
        ret.put("maxY", max.getY());
        ret.put("maxZ", max.getZ());
        ret.put("hasSpawn", hasSpawn);

        if (hasSpawn) {
            ret.put("spawnX", spawn.getX());
            ret.put("spawnY", spawn.getY());
            ret.put("spawnZ", spawn.getZ());
            ret.put("spawnPitch", spawn.getPitch());
            ret.put("spawnYaw", spawn.getYaw());
        }

        List<String> blockStrings = new ArrayList<>();
        for (Block block : blocks) {
            blockStrings.add(block.type.getId() + "-" + block.chance);
        }
        ret.put("blocks", blockStrings);

        return ret;
    }

    public void teleport(Player... players) {
        for (Player p : players) {
            p.teleport(getSpawn().orElse(
                null)); // Should probably fail with an exception, but an NPE is as good as any..
            PrisonMines.get().getMinesMessages().getLocalizable("teleported").withReplacements(name)
                .sendTo(p);
        }
    }

    public boolean reset() {
        // The all-important event
        MineResetEvent event = new MineResetEvent(this);
        Prison.get().getEventBus().post(event);
        if (event.isCanceled()) {
            return true;
        }

        try {
            int i = 0;

            Optional<World> worldOptional = getWorld();
            if (!worldOptional.isPresent()) {
                Output.get().logError("Could not reset mine " + name
                    + " because the world it was created in does not exist.");
                return false;
            }
            World world = worldOptional.get();

            List<BlockType> blockTypes = PrisonMines.get().getMines().getRandomizedBlocks(this);
            int maxX = Math.max(min.getBlockX(), max.getBlockX());
            int minX = Math.min(min.getBlockX(), max.getBlockX());
            int maxY = Math.max(min.getBlockY(), max.getBlockY());
            int minY = Math.min(min.getBlockY(), max.getBlockY());
            int maxZ = Math.max(min.getBlockZ(), max.getBlockZ());
            int minZ = Math.min(min.getBlockZ(), max.getBlockZ());

            teleportOutPlayers(maxY);

            for (int y = minY; y <= maxY; y++) {
                for (int x = minX; x <= maxX; x++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        if (PrisonMines.get().getConfig().fillMode && !world
                            .getBlockAt(new Location(world, x, y, z)).isEmpty()) {
                            continue; // Skip this block because it is not air
                        }

                        new Location(world, x, y, z).getBlockAt().setType(blockTypes.get(i));
                        i++;
                    }
                }
            }
            if (PrisonMines.get().getConfig().asyncReset) {
                asyncGen();
            }

            return true;
        } catch (Exception e) {
            Output.get().logError("&cFailed to reset mine " + name, e);
            return false;
        }
    }

    private void teleportOutPlayers(int maxY) {
        for (Player player : Prison.get().getPlatform().getOnlinePlayers()) {
            if (getBounds().within(player.getLocation())) {
                if (hasSpawn) {
                    teleport(player);
                } else {
                    Location l = player.getLocation();
                    player.teleport(
                        new Location(l.getWorld(), l.getX(), maxY + 3, l.getZ(), l.getPitch(),
                            l.getYaw()));
                }
            }
        }
    }

    private void asyncGen() {
        try {
            Prison.get().getPlatform().getScheduler()
                .runTaskLaterAsync(() -> PrisonMines.get().getMines().generateBlockList(this), 0L);
        } catch (Exception e) {
            Output.get().logWarn("Couldn't generate blocks for mine " + name
                + " asynchronously. The blocks will be generated synchronously later.", e);
        }
    }

    /*
     * Getters & Setters
     */

    /**
     * Checks for a spawn for this mine.
     *
     * @return true if a spawn is present, false otherwise
     * @see Mine#hasSpawn()
     */
    public boolean hasSpawn() {
        return hasSpawn;
    }

    /**
     * Gets the spawn for this mine
     *
     * @return the location of the spawn. {@link Optional#empty()} if no spawn is present OR the world can't be found
     */
    public Optional<Location> getSpawn() {
        if (!hasSpawn) {
            return Optional.empty();
        } else {
            return getWorld().isPresent() ? Optional.ofNullable(spawn) : Optional.empty();
        }
    }

    /**
     * Sets the spawn for this mine.
     *
     * @param location the new spawn
     * @return this instance for chaining
     */
    public Mine setSpawn(Location location) {
        hasSpawn = true;
        spawn = location;
        return this;
    }

    /**
     * Gets the name of this mine
     *
     * @return the name of this mine
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this mine
     *
     * @param name the new name
     * @return this instance for chaining
     */
    public Mine setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Gets the world
     *
     * @return
     */
    public Optional<World> getWorld() {
        return Prison.get().getPlatform().getWorld(worldName);
    }

    public Bounds getBounds() {
        return new Bounds(min, max);
    }

    /**
     * (Re)defines the boundaries for this mine
     *
     * @param bounds the new boundaries
     * @return this instance for chaining
     */
    public Mine setBounds(Bounds bounds) {
        min = bounds.getMin();
        max = bounds.getMax();
        worldName = bounds.getMin().getWorld().getName();
        return this;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    /**
     * Sets the blocks for this mine
     *
     * @param blockMap the new blockmap with the {@link BlockType} as the key, and the chance of the block appearing as the value.
     * @return this instance for chaining
     */
    public Mine setBlocks(HashMap<BlockType, Integer> blockMap) {
        blocks = new ArrayList<>();
        for (Map.Entry<BlockType, Integer> entry : blockMap.entrySet()) {
            blocks.add(new Block().create(entry.getKey(), entry.getValue()));
        }
        return this;
    }

    public boolean isInMine(Location location) {
        return getBounds().within(location);
    }

    public boolean isInMine(BlockType blockType) {
        for (Block block : blocks) {
            if (blockType == block.type) {
                return true;
            }
        }
        return false;
    }

    public double area() {
        return getBounds().getArea();
    }

    @Override public boolean equals(Object obj) {
        return (obj instanceof Mine) && (((Mine) obj).name).equals(name);
    }

    @Override public int hashCode() {
        return name.hashCode();
    }

}
