package tech.mcprison.prison.mines.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.Bounds;
import tech.mcprison.prison.util.Location;

public abstract class MineData {
	
	public static final int MINE_RESET__TIME_SEC__DEFAULT = 15 * 60; // 15 minutes
	public static final int MINE_RESET__TIME_SEC__MINIMUM = 30; // 30 seconds
	public static final long MINE_RESET__BROADCAST_RADIUS_BLOCKS = 150;
		
	private String name;
	private boolean enabled = false;
	
	private Bounds bounds;

	private Location spawn;
    private String worldName;
    private boolean hasSpawn = false;

    private int resetTime;
    private MineNotificationMode notificationMode;
	private long notificationRadius;
    
	private long targetResetTime;
	private int resetCount = 0;
	
    private List<Block> blocks;
    
    private long totalBlocksMined = 0;
    private double zeroBlockResetDelaySec;
    
    
    private boolean skipResetEnabled = false;
    private double skipResetPercent;
    private int skipResetBypassLimit;
    private transient int skipResetBypassCount;
    
    private List<String> resetCommands;
    
    private boolean usePagingOnReset = false;
    

    public enum MineNotificationMode {
    	disabled,
    	within,
    	radius,
    	
    	displayOptions
    	;
    	
    	public static MineNotificationMode fromString(String mode) {
    		return fromString(mode, radius);
    	}
    	public static MineNotificationMode fromString(String mode, MineNotificationMode defaultValue) {
    		MineNotificationMode results = defaultValue;
    		
    		if ( mode != null && mode.trim().length() > 0 ) {
    			for ( MineNotificationMode mnm : values() ) {
    				if ( mnm.name().equalsIgnoreCase( mode )) {
    					results = mnm;
    				}
    			}
    		}
    		
    		return results;
    	}
    }
    
    public MineData() {
    	this.blocks = new ArrayList<>();
    	
    	this.enabled = false;
    	
    	this.resetTime = MINE_RESET__TIME_SEC__DEFAULT;
    	this.notificationMode = MineNotificationMode.radius;
    	this.notificationRadius = MINE_RESET__BROADCAST_RADIUS_BLOCKS;
    	
    	this.targetResetTime = 0;
    	this.resetCount = 0;
    	this.totalBlocksMined = 0;
    	this.zeroBlockResetDelaySec = 0;
    
    	this.skipResetEnabled = false;
        this.skipResetPercent = 80.0D;
        this.skipResetBypassLimit = 50;
        this.skipResetBypassCount = 0;
        
        this.resetCommands = new ArrayList<>();
        
        this.usePagingOnReset = false;
    }

    /**
     * <p>This initialize function gets called after the classes are
     * instantiated, and is initiated from Mine class and propagates
     * to the MineData class.  Good for kicking off the scheduler.
     * </p>
     */
	protected void initialize() {
    	
    }
    
  
	
  
    public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled( boolean enabled ) {
		this.enabled = enabled;
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
    
	public String getWorldName() {
		return worldName;
	}
	public void setWorldName( String worldName ) {
		this.worldName = worldName;
	}
	
	/**
	 * <p>A mine cannot be created without defining the bounds.  Therefore,
	 * the "World" is stored within the bounds and that's where it should come from.
	 * At least this way, once the world is loaded, it will be set once and for all
	 * within the bounds and as such, it will help eliminate the overhead incurred by
	 * trying to get it from the platform object.
	 * </p>
	 * 
	 * <p>Until the world is fully loaded and processed by the prison mine manager,
	 * this function will not be able to return a value for world.
	 * </p>
	 * 
	 * @return
	 */
	public Optional<World> getWorld() {
		return Optional.ofNullable( getBounds().getMin().getWorld() );
//        return Prison.get().getPlatform().getWorld(worldName);
    }
	
	/**
	 * <p>This function sets the world object for this mine, then 
	 * indicates that this mine is enabled.
	 * This will be needed if the world in which the mine is located
	 * has not yet been loaded.  This may be the situation when using a
	 * world generated and loaded by Multiverse-core, or similar plugin.
	 * </p>
	 * 
	 * @param world
	 */
	public void setWorld( World world ) {
    	
		// Must add world to the two Bounds locations: 
		if ( world != null ) {
			getBounds().setWorld( world );
			
			// Add world to Spawn, if it exists:
			if ( getSpawn() != null ) {
				getSpawn().setWorld( world );
			}
		}

    	setEnabled( world != null );
	}

    public Bounds getBounds() {
        return bounds;
    }

    /**
     * <p>(Re)defines the boundaries for this mine.
     * </p>
     * 
     * <p>This function used to set the world name every time the bounds would 
     * change.  The world name MUST NEVER be changed.  If world is null then it will screw
     *  up the original location of when the was created.  World name is set
     *  in the document loader under Mine.loadFromDocument as the first field
     *  that is set when restoring from the file.
     * </p>
     *
     * @param bounds the new boundaries
     */
    public void setBounds(Bounds bounds) {
    	this.bounds = bounds;
        
    	// The world name MUST NEVER be changed.  If world is null then it will screw
    	// up the original location of when the was created.  World name is set
    	// in the document loader under Mine.loadFromDocument as the first field
    	// that is set when restoring from the file.
    	//this.worldName = bounds.getMin().getWorld().getName();
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
    
	public boolean isHasSpawn() {
		return hasSpawn;
	}
	public void setHasSpawn( boolean hasSpawn ) {
		this.hasSpawn = hasSpawn;
	}

	public int getResetTime() {
		return resetTime;
	}
	public void setResetTime( int resetTime ) {
		this.resetTime = resetTime;
	}

	public MineNotificationMode getNotificationMode() {
		return notificationMode;
	}
	public void setNotificationMode( MineNotificationMode notificationMode ) {
		this.notificationMode = notificationMode;
	}

	public long getNotificationRadius() {
		return notificationRadius;
	}
	public void setNotificationRadius( long notificationRadius ) {
		this.notificationRadius = notificationRadius;
	}

	public long getTargetResetTime() {
		return targetResetTime;
	}
	public void setTargetResetTime( long targetResetTime ) {
		this.targetResetTime = targetResetTime;
	}
	
	/**
	 * <p>This is the remaining time until a reset, in seconds.
	 * This is based upon the getTargetResetTime() in ms and the current
	 * System.currentTimeMillis().
	 * </p>
	 * 
	 * <p>The actual remaining time can vargy greatly and is highly
	 * dependent upon the server load.  Since many jobs within the whole
	 * mine reset process are scheduled to run in the future, their run
	 * time is just a request and it can vary if there are demanding jobs
	 * that are running, or if the bukkit/spigot's TPS starts dropping 
	 * below 20.
	 * </p>
	 * 
	 * @return
	 */
	public double getRemainingTimeSec() {
		// NOTE: timeleft can vary based upon server loads:
		long targetResetTime = getTargetResetTime();
		double remaining = ( targetResetTime <= 0 ? 0d : 
			(targetResetTime - System.currentTimeMillis()) / 1000d);
		return remaining;
	}

	public int incrementResetCount() {
		return ++resetCount;
	}
	public int getResetCount() {
		return resetCount;
	}
	public void setResetCount( int resetCount ) {
		this.resetCount = resetCount;
	}

	/**
	 * May not be 100% thread safe, but odds of collisions will be minimal and
	 * if its off by a few blocks its not a big deal since the value resets
	 * when the server resets.
	 * 
	 * @return
	 */
	public long addTotalBlocksMined( int blockCount ) {
		return totalBlocksMined += blockCount;
	}
	public long incrementTotalBlocksMined() {
		return ++totalBlocksMined;
	}
	public long getTotalBlocksMined() {
		return totalBlocksMined;
	}
	public void setTotalBlocksMined( long totalBlocksMined ) {
		this.totalBlocksMined = totalBlocksMined;
	}

	public boolean isZeroBlockResetDisabled() {
		return zeroBlockResetDelaySec == -1.0d;
	}
	public double getZeroBlockResetDelaySec() {
		return zeroBlockResetDelaySec;
	}
	public void setZeroBlockResetDelaySec( double zeroBlockResetDelaySec ) {
		this.zeroBlockResetDelaySec = zeroBlockResetDelaySec;
	}

	public boolean isSkipResetEnabled() {
		return skipResetEnabled;
	}
	public void setSkipResetEnabled( boolean skipResetEnabled ) {
		this.skipResetEnabled = skipResetEnabled;
	}

	public double getSkipResetPercent() {
		return skipResetPercent;
	}
	public void setSkipResetPercent( double skipResetPercent ) {
		this.skipResetPercent = skipResetPercent;
	}

	public int getSkipResetBypassLimit() {
		return skipResetBypassLimit;
	}
	public void setSkipResetBypassLimit( int skipResetBypassLimit ) {
		this.skipResetBypassLimit = skipResetBypassLimit;
	}

	public int incrementSkipResetBypassCount() {
		return ++skipResetBypassCount;
	}
	public int getSkipResetBypassCount() {
		return skipResetBypassCount;
	}
	public void setSkipResetBypassCount( int skipResetBypassCount ) {
		this.skipResetBypassCount = skipResetBypassCount;
	}

	public List<String> getResetCommands() {
		return resetCommands;
	}
	public void setResetCommands( List<String> resetCommands ) {
		this.resetCommands = resetCommands;
	}

	public boolean isUsePagingOnReset() {
		return usePagingOnReset;
	}

	public void setUsePagingOnReset( boolean usePagingOnReset ) {
		this.usePagingOnReset = usePagingOnReset;
	}
	
}
