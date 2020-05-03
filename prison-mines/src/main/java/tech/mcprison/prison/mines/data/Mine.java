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

    
    public Mine(String name, Selection selection) {
    	super();
    	
    	setName(name);
    	setBounds(selection.asBounds());
        
        // Kick off the initialize:
        initialize();
    }
    
    /**
     * Loads a mine from a document.
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

	private void loadFromDocument( Document document )
			throws MineException {
		Optional<World> worldOptional = Prison.get().getPlatform().getWorld((String) document.get("world"));
        if (!worldOptional.isPresent()) {
            throw new MineException("world doesn't exist");
        }
        World world = worldOptional.get();

        setName((String) document.get("name"));
        
        Double resetTimeDouble = (Double) document.get("resetTime");
        setResetTime( resetTimeDouble != null ? resetTimeDouble.intValue() : PrisonMines.getInstance().getConfig().resetTime );

        setBounds( new Bounds( 
        			getLocation(document, world, "minX", "minY", "minZ"),
        			getLocation(document, world, "maxX", "maxY", "maxZ")));
        
        setHasSpawn((boolean) document.get("hasSpawn"));
        if (isHasSpawn()) {
        	setSpawn(getLocation(document, world, "spawnX", "spawnY", "spawnZ", "spawnPitch", "spawnYaw"));
        }

        setWorldName(world.getName());
        
        setNotificationMode( MineNotificationMode.fromString( (String) document.get("notificationMode")) ); 
        Double noteRadius = (Double) document.get("notificationRadius");
        setNotificationRadius( noteRadius == null ? MINE_RESET__BROADCAST_RADIUS_BLOCKS : noteRadius.longValue() );

        Double zeroBlockResetDelaySec = (Double) document.get("zeroBlockResetDelaySec");
        setZeroBlockResetDelaySec( zeroBlockResetDelaySec == null ? 0 : zeroBlockResetDelaySec.intValue() );
        
        Boolean skipResetEnabled = (Boolean) document.get( "skipResetEnabled" );
        setSkipResetEnabled( skipResetEnabled == null ? false : skipResetEnabled.booleanValue() );
        Double skipResetPercent = (Double) document.get( "skipResetPercent" );
        setSkipResetPercent( skipResetPercent == null ? 80.0D : skipResetPercent.doubleValue() );
        Double skipResetBypassLimit = (Double) document.get( "skipResetBypassLimit" );
        setSkipResetBypassLimit( skipResetBypassLimit == null ? 50 : skipResetBypassLimit.intValue() );
        // When loading, skipResetBypassCount must be set to zero:
        setSkipResetBypassCount( 0 );
        
        @SuppressWarnings( "unchecked" )
		List<String> docBlocks = (List<String>) document.get("blocks");
        for (String docBlock : docBlocks) {
            String[] split = docBlock.split("-");
            String blockTypeName = split[0];
            double chance = Double.parseDouble(split[1]);

            // Use the BlockType.name() load the block type:
            Block block = new Block(BlockType.getBlock(blockTypeName), chance);
            getBlocks().add(block);
        }
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

        ret.put( "zeroBlockResetDelaySec", Integer.valueOf( getZeroBlockResetDelaySec() ) );
        
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
