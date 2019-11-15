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

package tech.mcprison.prison.mines.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.mines.MineException;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.events.MineResetEvent;
import tech.mcprison.prison.mines.managers.MineManager;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.store.Document;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.Bounds;
import tech.mcprison.prison.util.Location;

/**
 * @author Dylan M. Perks
 */
public class Mine {

	private Bounds bounds;

	private Location spawn;
    private String worldName, name;
    private boolean hasSpawn = false;

    private List<Block> blocks;


    /**
     * Creates a new, empty mine instance
     */
    public Mine() {
        blocks = new ArrayList<>();
    }

    private Location getLocation(Document doc, World world, String x, String y, String z) {
    	return new Location(world, (double) doc.get(x), (double) doc.get(y), (double) doc.get(z));
    }
    
    private Location getLocation(Document doc, World world, String x, String y, String z, String pitch, String yaw) {
    	Location loc = getLocation(doc, world, x, y, z);
    	loc.setPitch( ((Double) doc.get(pitch)).floatValue() );
    	loc.setYaw( ((Double) doc.get(yaw)).floatValue() );
    	return loc;
    }
    /**
     * Loads a mine from a document.
     *
     * @param document The document to load from.
     * @throws MineException If the mine couldn't be loaded from the document.
     */
    public Mine(Document document) throws MineException {
    	
        Optional<World> worldOptional = Prison.get().getPlatform().getWorld((String) document.get("world"));
        if (!worldOptional.isPresent()) {
            throw new MineException("world doesn't exist");
        }
        World world = worldOptional.get();

        setBounds( new Bounds( 
        			getLocation(document, world, "minX", "minY", "minZ"),
        			getLocation(document, world, "maxX", "maxY", "maxZ")));
        
        setHasSpawn((boolean) document.get("hasSpawn"));
        if (isHasSpawn()) {
        	setSpawn(getLocation(document, world, "spawnX", "spawnY", "spawnZ", "spawnPitch", "spawnYaw"));
        }

        setWorldName(world.getName());
        setName((String) document.get("name"));

        this.blocks = new ArrayList<>();
        
        @SuppressWarnings( "unchecked" )
		List<String> docBlocks = (List<String>) document.get("blocks");
        for (String docBlock : docBlocks) {
            String[] split = docBlock.split("-");
            String id = split[0];
            double chance = Double.parseDouble(split[1]);

            Block block = new Block(BlockType.getBlock(id), chance);
            blocks.add(block);
        }
    }

    
    public Document toDocument() {
        Document ret = new Document();
        ret.put("world", worldName);
        ret.put("name", name);
        ret.put("minX", getBounds().getMin().getX());
        ret.put("minY", getBounds().getMin().getY());
        ret.put("minZ", getBounds().getMin().getZ());
        ret.put("maxX", getBounds().getMax().getX());
        ret.put("maxY", getBounds().getMax().getY());
        ret.put("maxZ", getBounds().getMax().getZ());
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
            blockStrings.add(block.getType().getId() + "-" + block.getChance());
        }
        ret.put("blocks", blockStrings);

        return ret;
    }

    /**
     * NOTE: Have no idea WHY this always returns a value of true; why not void then?
     * 
     * @return
     */
    public boolean reset() {
        // The all-important event
        MineResetEvent event = new MineResetEvent(this);
        Prison.get().getEventBus().post(event);
        if (event.isCanceled()) {
            return true;
        }

        try {
            Optional<World> worldOptional = getWorld();
            if (!worldOptional.isPresent()) {
                Output.get().logError("Could not reset mine " + name
                    + " because the world it was created in does not exist.");
                return false;
            }
            World world = worldOptional.get();

            MineManager manager = PrisonMines.getInstance().getMineManager();
            if ( !manager.getRandomizedBlocks().containsKey( name ) ) {
            	manager.generateBlockList(this);
            }
            List<BlockType> blockTypes = manager.getRandomizedBlocks().get(name);

            teleportAllPlayersOut( world, getBounds().getyBlockMax() );

            int i = 0;
            boolean isFillMode = PrisonMines.getInstance().getConfig().fillMode;
            for (int y = getBounds().getyBlockMin(); y <= getBounds().getyBlockMax(); y++) {
                for (int x = getBounds().getxBlockMin(); x <= getBounds().getxBlockMax(); x++) {
                    for (int z = getBounds().getzBlockMin(); z <= getBounds().getzBlockMax(); z++) {
                    	Location targetBlock = new Location(world, x, y, z);
                    	
                        if (!isFillMode || isFillMode && world.getBlockAt(targetBlock).isEmpty()) {
                        	targetBlock.getBlockAt().setType(blockTypes.get(i++));
                        } 
                    }
                }
            }
            if (PrisonMines.getInstance().getConfig().asyncReset) {
                asyncGen();
            }

            return true;
        } catch (Exception e) {
            Output.get().logError("&cFailed to reset mine " + name, e);
            return false;
        }
    }

    /**
     * <p>This function teleports players out of existing mines if they are within 
     * their boundaries within the world where the Mine exists.</p>
     * 
     * <p>Using only players within the existing world of the current mine, each
     * player is checked to see if they are within the mine, and if they are they
     * are teleported either to the mine's spawn location, or straight up from their
     * current location to the top of the mine (assumes air space will exist there).</p>
     * 
     * <p>This function eliminates possible bug of players being teleported from other
     * worlds, and also eliminates the possibility that the destination could
     * ever be null.</p>
     * 
     * @param world - world 
     * @param targetY
     */
    private void teleportAllPlayersOut(World world, int targetY) {
    	List<Player> players = (world.getPlayers() != null ? world.getPlayers() : 
    							Prison.get().getPlatform().getOnlinePlayers());
    	for (Player player : players) {
            if ( isSameWorld(world, getBounds().getMin().getWorld()) && 
            		getBounds().within(player.getLocation())) {
            	
            	Location destination = null;
            	if (this.hasSpawn && getWorld().isPresent()) {
            		destination = this.spawn;
            	} else {
            		destination = player.getLocation();
            		destination.setY( targetY );
            	}
            			
            	player.teleport( destination );
                PrisonMines.getInstance().getMinesMessages().getLocalizable("teleported")
                		.withReplacements(this.name).sendTo(player);
            }
        }
    }
    
    /**
     * <p>This is a temporary fix until the Bounds.within() checks for the
     * same world.  For now, it is assumed that Bounds.min and Bounds.max are 
     * the same world, but that may not always be the case.</p>
     * 
     * @param w1 First world to compare to
     * @param w2 Second world to compare to
     * @return true if they are the same world
     */
    private boolean isSameWorld(World w1, World w2) {
    	// TODO Need to fix Bounds.within() to test for same worlds:
    	return w1 == null && w2 == null ||
    			w1 != null && w2 != null &&
    			w1.getName().equalsIgnoreCase(w2.getName());
    }
    

    private void asyncGen() {
        try {
            Prison.get().getPlatform().getScheduler()
                .runTaskLaterAsync(
                    () -> PrisonMines.getInstance().getMineManager().generateBlockList(this), 0L);
        } catch (Exception e) {
            Output.get().logWarn("Couldn't generate blocks for mine " + name
                + " asynchronously. The blocks will be generated synchronously later.", e);
        }
    }


    /**
     * Gets the spawn for this mine
     *
     * @return the location of the spawn. {@link Optional#empty()} if no spawn is present OR the
     * world can't be found
     */
    public Location getSpawn() {
    	return spawn;
    }

    /**
     * Sets the spawn for this mine.
     *
     * @param location the new spawn
     * @return this instance for chaining
     */
    public Mine setSpawn(Location location) {
    	hasSpawn = (location != null);
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
     */
    public Optional<World> getWorld() {
        return Prison.get().getPlatform().getWorld(worldName);
    }

    public Bounds getBounds() {
        return bounds;
    }

    /**
     * (Re)defines the boundaries for this mine
     *
     * @param bounds the new boundaries
     * @return this instance for chaining
     */
    public Mine setBounds(Bounds bounds) {
    	this.bounds = bounds;
        this.worldName = bounds.getMin().getWorld().getName();
        return this;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    /**
     * Sets the blocks for this mine
     *
     * @param blockMap the new blockmap with the {@link BlockType} as the key, and the chance of the
     * block appearing as the value.
     * @return this instance for chaining
     */
    public Mine setBlocks(HashMap<BlockType, Integer> blockMap) {
        blocks = new ArrayList<>();
        for (Map.Entry<BlockType, Integer> entry : blockMap.entrySet()) {
            blocks.add(new Block(entry.getKey(), entry.getValue()));
        }
        return this;
    }

    public boolean isInMine(Location location) {
        return getBounds().within(location);
    }

    public boolean isInMine(BlockType blockType) {
        for (Block block : getBlocks()) {
            if (blockType == block.getType()) {
                return true;
            }
        }
        return false;
    }

    public double area() {
        return getBounds().getArea();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Mine) && (((Mine) obj).name).equals(name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }



	public String getWorldName()
	{
		return worldName;
	}
	public void setWorldName( String worldName )
	{
		this.worldName = worldName;
	}

	public boolean isHasSpawn()
	{
		return hasSpawn;
	}
	public void setHasSpawn( boolean hasSpawn )
	{
		this.hasSpawn = hasSpawn;
	}

}
