package tech.mcprison.prison.mines.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.Bounds;
import tech.mcprison.prison.util.Location;

public abstract class MineData
{
	public static final int DEFAULT_MINE_RESET_TIME_SEC = 15 * 60; // 15 minutes
	private Bounds bounds;

	private Location spawn;
    private String worldName, name;
    private boolean hasSpawn = false;

    private int resetTime = DEFAULT_MINE_RESET_TIME_SEC;
    
    private List<Block> blocks;

    public MineData() {
    	this.blocks = new ArrayList<>();
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
     */
    public void setBounds(Bounds bounds) {
    	this.bounds = bounds;
        this.worldName = bounds.getMin().getWorld().getName();
    }

    public List<Block> getBlocks() {
        return blocks;
    }
    
    /**
     * Sets the blocks for this mine
     *
     * @param blockMap the new blockmap with the {@link BlockType} as the key, and the chance of the
     * block appearing as the value.
     */
    public void setBlocks(HashMap<BlockType, Integer> blockMap) {
        blocks = new ArrayList<>();
        for (Map.Entry<BlockType, Integer> entry : blockMap.entrySet()) {
            blocks.add(new Block(entry.getKey(), entry.getValue()));
        }
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
    public void setSpawn(Location location) {
    	hasSpawn = (location != null);
        spawn = location;
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
     */
    public void setName(String name) {
        this.name = name;
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

	public int getResetTime()
	{
		return resetTime;
	}
	public void setResetTime( int resetTime )
	{
		this.resetTime = resetTime;
	}
}
