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
import java.util.List;
import java.util.Optional;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.mines.MineException;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.managers.MineManager;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.selection.Selection;
import tech.mcprison.prison.store.Document;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.Bounds;
import tech.mcprison.prison.util.Location;

/**
 * @author Dylan M. Perks
 */
public class Mine 
	extends MineScheduler {

    /**
     * Creates a new, empty mine instance
     */
    public Mine() {
        super();
        
        // Kick off the initialize:
        initialize();
    }

    
    /**
     * <p>This is called when a mine is first created.
     * </p>
     * 
     * @param name
     * @param selection
     */
    public Mine(String name, Selection selection) {
    	super();
    	
    	setName(name);
    	setBounds(selection.asBounds());
    	
    	setWorldName( getBounds().getMin().getWorld().getName());
    	
    	setEnabled( true );
        
        // Kick off the initialize:
        initialize();
    }
    
    /**
     * <p>Loads a mine from a document.
     * </p>
     * 
     * <p>Note that the location where the loadFromDocument() occurs in the whole 
     * "create the objects" is in the "middle".  All classes that are extended
     * from are instantiated first due to the super() function call.  So when Mine
     * tries to be instantiated, it first drills all the way down to MineData and 
     * then runs all the initialization code within MineData and then works back 
     * through all of the classes, instantiating everything, one layer at a time.
     * </p>
     * 
     * <p>Then when it gets back up to this class, Mine, all parents have been fully
     * instantiated so all collections will have been assigned non-null values 
     * as an example.  Then this class loads the data from the document object.
     * This is important, since all parents have been initialized, now the document
     * loader is making it a "mine". 
     * </p>
     * 
     * <p>Then at that point, after the mine data is loaded, it once again drills all the
     * way down to the MineData ancestor class, using the initialize() functions, where
     * it then starts to initialize all classes from MineData, back up to Mine.
     * What this enables and allows, is when a class is initialized, it will have access
     * to the fully loaded mine data.  This is a perfect example of being able to start
     * submitting the mine reset jobs since all data has been loaded, and all lower 
     * functions have been ran.
     * </p>
     * 
     * <p>So the over all design of the Mine objects is that all ancestors instantiate
     * first, from MineData to Mine. Then the mine is loaded from the file system.
     * Then all ancestors are initialized from MineData to Mine.  This gives a high
     * degree of control over when actions can be ran over a mine, and have confidence the
     * data and conditions will be there.
     * </p>
     *
     * @param document The document to load from.
     * @throws MineException If the mine couldn't be loaded from the document.
     */
    public Mine(Document document) throws MineException {
    	super();
    	
        loadFromDocument( document );
        
        // Kick off the initialize:
        // This is critically vital to ensure the workflow is generated with the contents
        // from the document and not the defaults as set by the super().
        initialize();
    }

    
    /**
     * <p>This initialize function gets called after the classes are
     * instantiated, and is initiated from Mine class and propagates
     * to the MineData class.  Good for kicking off the scheduler.
     * </p>
     */
	@Override
	protected void initialize() {
    	super.initialize();
    	
    }

	/**
	 * <p>The loading of a mine checks to ensure if the world exists.  If not, then 
	 * traditionally, it would not load the mine.  The problem with this model is 
	 * the world may not exist yet, if running Multiverse-core (or another similar 
	 * plugin) and as such, may falsely cause mine failures.  This is the situation if
	 * the mine exists within a world that must be loaded by Multiverse-core.  If it was
	 * a standard world, then it would be fine. 
	 * </p>
	 * 
	 * <p>Soft dependencies do not provide a solution. One bad solution for this 
	 * situation, is to manually add a hard dependency to Multiverse-core. This
	 * should not be used.
	 * </p>
	 * 
	 * <p>As a better solution to this problem, mines will be loaded as normal, but
	 * if the world does not exist, then their initialization, or enablement, will be
	 * delayed until the world is available. 
	 * </p>
	 * 
	 * @param document
	 * @throws MineException
	 */
	@SuppressWarnings( "unchecked" )
	private void loadFromDocument( Document document )
			throws MineException {
		String worldName = (String) document.get("world");
        setWorldName( worldName );
        setName((String) document.get("name")); // Mine name:
		
		World world = null;
		
		if ( worldName == null ) {
			Output.get().logInfo( "Mines.loadFromDocument: Failure: World does not exist in Mine file. mine= %s " +
					"Contact support on how to fix.",  
					getName());
		}
		
		Optional<World> worldOptional = Prison.get().getPlatform().getWorld(worldName);
        if (!worldOptional.isPresent()) {
            MineManager mineMan = PrisonMines.getInstance().getMineManager();
            
            // Store this mine and the world in MineManager's unavailableWorld for later
            // processing and hooking up to the world object. Print an error message upon
            // the first mine's world not existing.
            mineMan.addUnavailableWorld( worldName, this );
            
            setEnabled( false );
        }
        else {
        	world = worldOptional.get();
        	setEnabled( true );
        }
        
//        World world = worldOptional.get();

        
        Double resetTimeDouble = (Double) document.get("resetTime");
        setResetTime( resetTimeDouble != null ? resetTimeDouble.intValue() : PrisonMines.getInstance().getConfig().resetTime );

        setBounds( new Bounds( 
        			getLocation(document, world, "minX", "minY", "minZ"),
        			getLocation(document, world, "maxX", "maxY", "maxZ")));
        
        setHasSpawn((boolean) document.get("hasSpawn"));
        if (isHasSpawn()) {
        	setSpawn(getLocation(document, world, "spawnX", "spawnY", "spawnZ", "spawnPitch", "spawnYaw"));
        }

        
        setNotificationMode( MineNotificationMode.fromString( (String) document.get("notificationMode")) ); 
        Double noteRadius = (Double) document.get("notificationRadius");
        setNotificationRadius( noteRadius == null ? MINE_RESET__BROADCAST_RADIUS_BLOCKS : noteRadius.longValue() );

        Double zeroBlockResetDelaySec = (Double) document.get("zeroBlockResetDelaySec");
        setZeroBlockResetDelaySec( zeroBlockResetDelaySec == null ? 0.0d : zeroBlockResetDelaySec.doubleValue() );
        
        Boolean skipResetEnabled = (Boolean) document.get( "skipResetEnabled" );
        setSkipResetEnabled( skipResetEnabled == null ? false : skipResetEnabled.booleanValue() );
        Double skipResetPercent = (Double) document.get( "skipResetPercent" );
        setSkipResetPercent( skipResetPercent == null ? 80.0D : skipResetPercent.doubleValue() );
        Double skipResetBypassLimit = (Double) document.get( "skipResetBypassLimit" );
        setSkipResetBypassLimit( skipResetBypassLimit == null ? 50 : skipResetBypassLimit.intValue() );
        // When loading, skipResetBypassCount must be set to zero:
        setSkipResetBypassCount( 0 );
        
		List<String> docBlocks = (List<String>) document.get("blocks");
        for (String docBlock : docBlocks) {
            String[] split = docBlock.split("-");
            String blockTypeName = split[0];
            double chance = Double.parseDouble(split[1]);

            // Use the BlockType.name() load the block type:
            Block block = new Block(BlockType.getBlock(blockTypeName), chance);
            getBlocks().add(block);
        }
        
        List<String> commands = (List<String>) document.get("commands");
        setResetCommands( commands == null ? new ArrayList<>() : commands );
	}

    
    public Document toDocument() {
        Document ret = new Document();
        ret.put("world", getWorldName());
        ret.put("name", getName());
        ret.put("minX", getBounds().getMin().getX());
        ret.put("minY", getBounds().getMin().getY());
        ret.put("minZ", getBounds().getMin().getZ());
        ret.put("maxX", getBounds().getMax().getX());
        ret.put("maxY", getBounds().getMax().getY());
        ret.put("maxZ", getBounds().getMax().getZ());
        ret.put("hasSpawn", isHasSpawn());
        
        ret.put("resetTime", getResetTime() );
        ret.put("notificationMode", getNotificationMode().name() );
        ret.put("notificationRadius", Long.valueOf( getNotificationRadius() ));

        ret.put( "zeroBlockResetDelaySec", Double.valueOf( getZeroBlockResetDelaySec() ) );
        
        ret.put( "skipResetEnabled", isSkipResetEnabled() );
        ret.put( "skipResetPercent", getSkipResetPercent() );
        ret.put( "skipResetBypassLimit", getSkipResetBypassLimit() );
        
        if (isHasSpawn()) {
            ret.put("spawnX", getSpawn().getX());
            ret.put("spawnY", getSpawn().getY());
            ret.put("spawnZ", getSpawn().getZ());
            ret.put("spawnPitch", getSpawn().getPitch());
            ret.put("spawnYaw", getSpawn().getYaw());
        }

        List<String> blockStrings = new ArrayList<>();
        for (Block block : getBlocks()) {
        	// Use the BlockType.name() to save the block type to the file:
            blockStrings.add(block.getType().name() + "-" + block.getChance());
//            blockStrings.add(block.getType().getId() + "-" + block.getChance());
        }
        ret.put("blocks", blockStrings);

        ret.put("commands", getResetCommands());
        
        return ret;
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
    
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Mine) && (((Mine) obj).getName()).equals(getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

}
