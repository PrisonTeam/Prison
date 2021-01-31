package tech.mcprison.prison.mines.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlock.PrisonBlockType;
import tech.mcprison.prison.mines.features.MineBlockEvent;
import tech.mcprison.prison.mines.features.MineLinerData;
import tech.mcprison.prison.modules.ModuleElement;
import tech.mcprison.prison.modules.ModuleElementType;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.Bounds;
import tech.mcprison.prison.util.Location;

public abstract class MineData
		implements ModuleElement {
	
	public static final int MINE_RESET__TIME_SEC__DEFAULT = 15 * 60; // 15 minutes
	public static final int MINE_RESET__TIME_SEC__MINIMUM = 30; // 30 seconds
	public static final long MINE_RESET__BROADCAST_RADIUS_BLOCKS = 150;
	
	public static final String MINE_NOTIFICATION_PERMISSION_PREFIX = "mines.notification.";
	
	private transient final ModuleElementType elementType;
		
	private String name;
	private String tag;
	
	private boolean enabled = false;
	private boolean virtual = false;
	
	/**
	 * A sortOrder of -1 means it should be excluded from most mine listings.
	 * An example would be for private mines or child mines where you only want the
	 * parent listed.
	 */
	private int sortOrder = 0;
	
	private Bounds bounds;

	private Location spawn;
    private String worldName;
    private boolean hasSpawn = false;

    private int resetTime;
    private MineNotificationMode notificationMode;
	private long notificationRadius;
	private boolean useNotificationPermission = false;
    
	private long targetResetTime;
	private int resetCount = 0;
	
	/**
	 * This list of blocks represents the old Prison block model. It is being
	 * phased out since it has limited flexibility and complex issues with 
	 * supporting magic values with the older bukkit versions.
	 */
    private List<Block> blocks;
    
    /**
     * This list of PrisonBlocks represents the new Prison block model. Its 
     * more flexible and able to support other plugins's custom blocks and
     * the core bukkit blocks are based upon Cryptomorin's XSeries' XMaterial
     * for greater flexibility and cross version support.
     */
    private List<PrisonBlock> prisonBlocks;
    
    /**
     * To better identify if custom blocks need to be checked upon block break 
     * events since the custom plugins will have to identify their own blocks, 
     * this set contains the collection of non-minecraft PrisonBlockTypes that
     * are used in this mine.  That way it's a simple and fast check to see if,
     * and more importantly, which custom block plugin needs to be checked.
     */
    private transient Set<PrisonBlockType> prisonBlockTypes;
    
    private long totalBlocksMined = 0;
    private double zeroBlockResetDelaySec;

    private double resetThresholdPercent = 0;
    
    
    private boolean skipResetEnabled = false;
    private double skipResetPercent;
    private int skipResetBypassLimit;
    private transient int skipResetBypassCount;
    
    private List<String> resetCommands;
    
    private boolean usePagingOnReset = false;
    
    private ModuleElement rank;
    /**
     * When loading mines, ranks will not have been loaded yet, so must
     * save the rankString to be paired to the Ranks later.
     * The rankString are the components of the ModuleElement.
     */
    private String rankString;

    
    private List<MineBlockEvent> blockEvents;
    
    private MineLinerData linerData;
    

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
    	this.elementType = ModuleElementType.MINE;
    	
    	this.tag = null;
    	
    	this.blocks = new ArrayList<>();
    	this.prisonBlocks = new ArrayList<>();
    	this.prisonBlockTypes = new HashSet<>();
    	
    	
    	this.enabled = false;
    	this.virtual = false;
    	
    	/**
    	 * Mines are sorted based upon the sortOrder, ascending.  If a mine is given
    	 * a value of -1 then it will be excluded from most mine listings.
    	 */
    	this.sortOrder = 0;
    	
    	this.resetTime = MINE_RESET__TIME_SEC__DEFAULT;
    	this.notificationMode = MineNotificationMode.radius;
    	this.notificationRadius = MINE_RESET__BROADCAST_RADIUS_BLOCKS;
    	this.useNotificationPermission = false;
    	
    	this.targetResetTime = 0;
    	this.resetCount = 0;
    	this.totalBlocksMined = 0;
    	this.zeroBlockResetDelaySec = 0;
    	this.resetThresholdPercent = 0;
    
    	this.skipResetEnabled = false;
        this.skipResetPercent = 80.0D;
        this.skipResetBypassLimit = 50;
        this.skipResetBypassCount = 0;
        
        this.resetCommands = new ArrayList<>();
        
        this.usePagingOnReset = false;
        
        this.rank = null;
        this.rankString = null;
        
        this.blockEvents = new ArrayList<>();
        
        this.linerData = new MineLinerData();
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
		return !isVirtual() && enabled;
	}
	public void setEnabled( boolean enabled ) {
		this.enabled = enabled;
	}
	
	
	/**
	 * <p>A virtual mine does not have any coordinates defined for either the
	 * mine itself, or the spawn point.  A virtual mine can never be enabled.
	 * </p>
	 * 
	 * <p>A virtual mine can be potentially useful to be pre-created and auto 
	 * configured.
	 * </p>
	 * 
	 * @return
	 */
	public boolean isVirtual() {
		return virtual;
	}
	public void setVirtual( boolean virtual ) {
		this.virtual = virtual;
	}

	public ModuleElementType getModuleElementType() {
		return elementType;
	}

    /**
     * Gets the name of this mine
     *
     * @return the name of this mine
     */
    public String getName() {
        return name;
    }

    public String getTag() {
    	return tag;
    }
    public void setTag( String tag ) {
    	this.tag = tag;
    }
    
    public int getSortOrder() {
    	return sortOrder;
    }
    public void setSortOrder( int sortOrder ) {
    	this.sortOrder = sortOrder;
    }
    
    
    /**
     * Mines do not use an id.  So these will always
     * return a -1 and will ignore any value that is
     * set.  An id is forced by Ranks and Ladders.
     */
    public int getId() {
    	return -1;
    }
    public void setId( int idIsIgnored ) {
    	// ignore
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
		if ( isVirtual() ) {
			return "Virtually-Undefined";
		}
		return worldName;
	}
	public void setWorldName( String worldName ) {
		// cannot set the world name if it is a virtual mine:
		if ( !isVirtual() ) {
			this.worldName = worldName;
		}
		
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
		return Optional.ofNullable( isVirtual() ? null : getBounds().getMin().getWorld() );
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
    	
    	if ( bounds != null && ( isVirtual() || !getWorld().isPresent() ||
    			getWorldName() == null || getWorldName().trim().length() == 0 ||
    			getWorldName().equalsIgnoreCase( "Virtually-Undefined" )) ) {
    		 
        	World world = bounds.getMin().getWorld();
        	
        	if ( world != null ) {
        		
        		setWorld( world );
        		setWorldName( world.getName() );
        		setVirtual( false );
        		setEnabled( true );
        		
        		Output.get().logInfo( "Mine " + getName() + ": world has been set and is now enabled." );
        	}
        	else {
        		setEnabled( false );
        		Output.get().logWarn( 
        				String.format( "&cCould not activate mine &7%s &cbecause the " +
        				"world object cannot be aquired. Bounds failed be set correctly " +
        				"and this mine is &ddisabled&c.", getName()) );
        	}
    	}
        
    	// The world name MUST NEVER be changed.  If world is null then it will screw
    	// up the original location of when the was created.  World name is set
    	// in the document loader under Mine.loadFromDocument as the first field
    	// that is set when restoring from the file.
    	//this.worldName = bounds.getMin().getWorld().getName();
    }

    public List<Block> getBlocks() {
        return blocks;
    }
    

    public List<PrisonBlock> getPrisonBlocks() {
		return prisonBlocks;
	}

    
	public Set<PrisonBlockType> getPrisonBlockTypes() {
		return prisonBlockTypes;
	}
	
	public void addPrisonBlock( PrisonBlock prisonBlock ) {
		if ( prisonBlock != null && !isInMine( prisonBlock )) {
			
			getPrisonBlocks().add( prisonBlock );
			addPrisonBlockType( prisonBlock );
		}
	}
	
	private void addPrisonBlockType( PrisonBlock prisonBlock ) {
		if ( !getPrisonBlockTypes().contains( prisonBlock.getBlockType() )) {
			getPrisonBlockTypes().add( prisonBlock.getBlockType() );
		}
	}

	public boolean removePrisonBlock( PrisonBlock prisonBlock ) {
		boolean results = false;
		
		if ( prisonBlock != null && isInMine( prisonBlock ) ) {
			
			results = getPrisonBlocks().remove( prisonBlock );

			// regenerate the PrisonBlockTypes in case the removed block was the last type in the mine
			getPrisonBlockTypes().clear();
			for ( PrisonBlock pBlock : getPrisonBlocks() ) {
				addPrisonBlockType( pBlock );
			}
		}
		return results;
	}
	
	/**
	 * This is only used in an obsolete conversion utility.
	 * 
	 * Adding the newer PrisonBlocks for compatibility.
	 * 
     * Sets the blocks for this mine
     *
     * @param blockMap the new blockmap with the {@link BlockType} as the key, and the chance of the
     * block appearing as the value.
     */
    public void setBlocks(HashMap<BlockType, Integer> blockMap) {
        this.blocks.clear();
        this.prisonBlocks.clear();
        
        for (Map.Entry<BlockType, Integer> entry : blockMap.entrySet()) {
            blocks.add(new Block(entry.getKey(), entry.getValue()));
            
            PrisonBlock prisonBlock = Prison.get().getPlatform().getPrisonBlock( entry.getKey().name() );
            if ( prisonBlock != null ) {
            	prisonBlock.setChance( entry.getValue() );
            	prisonBlocks.add( prisonBlock );
            }
        }
    }

    public boolean isInMine(Location location) {
    	if ( isVirtual() ) {
    		return false;
    	}
        return getBounds().withinIncludeTopOfMine(location);
    }
    
    public boolean isInMine(BlockType blockType) {
    	//TODO Not sure if virtual should return false... they do have blocks.
//    	if ( isVirtual() ) {
//    		return false;
//    	}
        for (Block block : getBlocks()) {
            if (blockType == block.getType()) {
                return true;
            }
        }
        return false;
    }

    public boolean isInMine(PrisonBlock blockType) {
    	//TODO Not sure if virtual should return false... they do have blocks.
    	if ( isVirtual() ) {
    		return false;
    	}
    	for (PrisonBlock block : getPrisonBlocks()) {
    		if (blockType.getBlockNameFormal().equalsIgnoreCase( block.getBlockNameFormal())) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public PrisonBlock getPrisonBlock( PrisonBlock blockType ) {
    	PrisonBlock results = null;
    	
    	for (PrisonBlock block : getPrisonBlocks()) {
    		if (blockType.getBlockNameFormal().equalsIgnoreCase( block.getBlockNameFormal())) {
    			results = block;
    			break;
    		}
    	}
    	
    	return results;
    }

    public double area() {
    	if ( isVirtual() ) {
    		return 0;
    	}
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
    	// cannot set spawn when virtual:
    	if ( !isVirtual() ) {
    		hasSpawn = (location != null);
    		spawn = location;
    	}
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
	
	public boolean isUseNotificationPermission() {
		return useNotificationPermission;
	}
	public void setUseNotificationPermission( boolean useNotificationPermission ) {
		this.useNotificationPermission = useNotificationPermission;
	}

	public String getMineNotificationPermissionName() {
		return MINE_NOTIFICATION_PERMISSION_PREFIX + getName().toLowerCase();
	}
	
	/**
	 * <p>This is the remaining time until a reset, in seconds.
	 * This is based upon the getTargetResetTime() in ms and the current
	 * System.currentTimeMillis().
	 * </p>
	 * 
	 * <p>The actual remaining time can vary greatly and is highly
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

	public double getResetThresholdPercent() {
		return resetThresholdPercent;
	}
	public void setResetThresholdPercent( double resetThresholdPercent ) {
		this.resetThresholdPercent = resetThresholdPercent;
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

	public ModuleElement getRank() {
		return rank;
	}
	public void setRank( ModuleElement rank ) {
		this.rank = rank;
	}

	public String getRankString() {
		return rankString;
	}
	public void setRankString( String rankString ) {
		this.rankString = rankString;
	}

	public List<MineBlockEvent> getBlockEvents() {
		return blockEvents;
	}
	public void setBlockEvents( List<MineBlockEvent> blockEvents ) {
		this.blockEvents = blockEvents;
	}
	
	public boolean getBlockEventsRemove( String command ) {
		boolean results = false;
		MineBlockEvent blockEvent = null;
		for ( MineBlockEvent be : getBlockEvents() ) {
			if ( be.getCommand().equalsIgnoreCase( command ) ) {
				blockEvent = be;
			}
		}
		
		if ( blockEvent != null ) {
			results = getBlockEvents().remove( blockEvent );
		}
		
		return results;
	}

	public MineLinerData getLinerData() {
		return linerData;
	}
	public void setLinerData( MineLinerData linerData ) {
		this.linerData = linerData;
	}
	
}
