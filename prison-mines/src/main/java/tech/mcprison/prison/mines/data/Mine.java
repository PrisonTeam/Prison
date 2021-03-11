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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlockStatusData;
import tech.mcprison.prison.mines.MineException;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.features.MineBlockEvent;
import tech.mcprison.prison.mines.features.MineLinerData;
import tech.mcprison.prison.mines.managers.MineManager;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.selection.Selection;
import tech.mcprison.prison.sorting.PrisonSortable;
import tech.mcprison.prison.store.Document;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.Bounds;
import tech.mcprison.prison.util.Location;

/**
 * @author Dylan M. Perks
 */
public class Mine 
	extends MineScheduler 
	implements PrisonSortable {

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
    	if ( selection == null ) {
    		setVirtual( true );
    	}
    	else {
    		
    		setBounds(selection.asBounds());
    		
    		setWorldName( getBounds().getMin().getWorld().getName());
    		
    		setEnabled( true );
    	}
        
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
		
		boolean dirty = false;
		boolean inconsistancy = false;
		
		String worldName = (String) document.get("world");
        setWorldName( worldName );
        setName((String) document.get("name")); // Mine name:

        
        String tag = (String) document.get("tag");
        setTag( tag );

        
        setVirtual( document.get("isVirtual") == null ? false : (boolean) document.get("isVirtual") );
        
        
        Double sortOrder = (Double) document.get( "sortOrder" );
        setSortOrder( sortOrder == null ? 0 : sortOrder.intValue() );

        
		World world = null;
		
		if ( !isVirtual() ) {
			if ( worldName == null || "Virtually-Undefined".equalsIgnoreCase( worldName ) ) {
				Output.get().logInfo( "Mines.loadFromDocument: Failure: World does not exist in Mine " +
						"file. mine= %s  world= %s " +
						"Contact support on how to fix.",  
						getName(), (world == null ? "null" : world ));
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
			
		
			
			Location locMin = getLocation(document, world, "minX", "minY", "minZ");
			Location locMax = getLocation(document, world, "maxX", "maxY", "maxZ");
			
			setBounds( new Bounds( 
					locMin,
					locMax));
			
			setHasSpawn((boolean) document.get("hasSpawn"));
			if (isHasSpawn()) {
				setSpawn(getLocation(document, world, "spawnX", "spawnY", "spawnZ", "spawnPitch", "spawnYaw"));
			}
			
		}
        

        
        Double resetTimeDouble = (Double) document.get("resetTime");
        setResetTime( resetTimeDouble != null ? resetTimeDouble.intValue() : PrisonMines.getInstance().getConfig().resetTime );

        
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

        Double resetThresholdPercent = (Double) document.get( "resetThresholdPercent" );
        setResetThresholdPercent( resetThresholdPercent == null ? 0 : resetThresholdPercent.doubleValue() );
 
        // When loading, skipResetBypassCount must be set to zero:
        setSkipResetBypassCount( 0 );
        
        
        String rankString = (String) document.get( "rank" );
        setRank( null );
        setRankString( rankString );
        
        
        // This is a validation set to ensure only one block type is loaded file system.
        // Must keep the first one loaded.
        Set<String> validateBlockNames = new HashSet<>();
        getBlocks().clear();

        List<String> docBlocks = (List<String>) document.get("blocks");
		for (String docBlock : docBlocks) {
			
			// If the file is manually edited and a comma is added to the end of the block list,
			// then docBlock could be null.  Skip processing if null.
			if ( docBlock != null ) {
				
				String[] split = docBlock.split("-");
				String blockTypeName = split[0];
//				double chance = split.length > 1 ? Double.parseDouble(split[1]) : 0;
//				long blockCount = split.length > 2 ? Long.parseLong(split[2]) : 0;
//				int constraintMin = split.length > 3 ? Integer.parseInt(split[3]) : 0;
//				int constraintMax = split.length > 4 ? Integer.parseInt(split[4]) : 0;
				
				if ( blockTypeName != null && !validateBlockNames.contains( blockTypeName )) {
					// Use the BlockType.name() load the block type:
					BlockType blockType = BlockType.getBlock(blockTypeName);
					if ( blockType != null ) {
						
						
						/**
						 * <p>The following is code to correct the use of items being used as a
						 * block in a mine, which will cause a failure in trying to place an 
						 * item as a block.
						 * </p>
						 * 
						 * <p>This is intended for the old block model and is temp code to ensure 
						 * that there are less errors the end user will experience.
						 * </p>
						 */
						String errorMessage = "Warning! An invalid block type of %s was " +
								"detect when loading blocks for " +
								"mine %s. %s is not a valid block type. Using " +
								"%s instead. If this is incorrect please fix manually.";
						
						if ( blockType == BlockType.REDSTONE ) {
							BlockType itemType = blockType;
							blockType = BlockType.REDSTONE_ORE;
							
							Output.get().logError( 
									String.format( errorMessage, itemType.name(), getName(), 
											"Redstone dust", blockType.name()) );
							
							dirty = true;
						}
						else if ( blockType == BlockType.NETHER_BRICK ) {
							BlockType itemType = blockType;
							blockType = BlockType.DOUBLE_NETHER_BRICK_SLAB;
							
							Output.get().logError( 
									String.format( errorMessage, itemType.name(), getName(), 
											"Individual nether brick", blockType.name()) );
							
							dirty = true;
						}
						
						BlockOld block = new BlockOld(blockType);

						block.parseFromSaveFileFormatStats( docBlock );
						
//						BlockOld block = new BlockOld(blockType, chance, blockCount);
//						block.setConstraintMin( constraintMin );
//						block.setConstraintMax( constraintMax );

						getBlocks().add(block);
					}
					else {
						String message = String.format( "Failure in loading block type from %s mine's " +
								"save file. Block type %s has no mapping.", getName(),
								blockTypeName );
						Output.get().logError( message );
					}
					
					validateBlockNames.add( blockTypeName );
				}
				else if (validateBlockNames.contains( blockTypeName ) ) {
					// Detected and fixed a duplication so mark as dirty so fixed block list is saved:
					dirty = true;
					inconsistancy = true;
				}
			}
			
        }
        
        
		// Reset validation checks:
		validateBlockNames.clear();
		getPrisonBlocks().clear();
		
		List<String> docPrisonBlocks = (List<String>) document.get("prisonBlocks");
		if ( docPrisonBlocks != null ) {
			
			for (String docBlock : docPrisonBlocks) {
				
				if ( docBlock != null ) {
					
					PrisonBlock prisonBlock = PrisonBlockStatusData.parseFromSaveFileFormat( docBlock );
					
					
					if ( prisonBlock != null && !validateBlockNames.contains( prisonBlock.getBlockName() )) {
						
						if ( prisonBlock.isLegacyBlock() ) {
							dirty = true;
						}
						addPrisonBlock( prisonBlock );
						
						validateBlockNames.add( prisonBlock.getBlockName() );
					}
					else if (validateBlockNames.contains( prisonBlock.getBlockName() ) ) {
						// Detected and fixed a duplication so mark as dirty so fixed block list is saved:
						dirty = true;
						inconsistancy = true;
					}
					
					
//					String[] split = docBlock.split("-");
//					String blockTypeName = split[0];
//					double chance = split.length > 1 ? Double.parseDouble(split[1]) : 0;
//					long blockCount = split.length > 2 ? Long.parseLong(split[2]) : 0;
//					int constraintMin = split.length > 3 ? Integer.parseInt(split[3]) : 0;
//					int constraintMax = split.length > 4 ? Integer.parseInt(split[4]) : 0;
//					int constraintExcludeTopLayers = split.length > 5 ? Integer.parseInt(split[5]) : 0;
//					int constraintExcludeBottomLayers = split.length > 6 ? Integer.parseInt(split[6]) : 0;
//
//					if ( blockTypeName != null ) {
//						// The new way to get the PrisonBlocks:  
//						//   The blocks return are cloned so they have their own instance:
//						PrisonBlock prisonBlock = Prison.get().getPlatform().getPrisonBlock( blockTypeName );
//						
//						if ( prisonBlock != null && !validateBlockNames.contains( blockTypeName )) {
//							prisonBlock.setChance( chance );
//							prisonBlock.setBlockCountTotal( blockCount );
//							prisonBlock.setConstraintMin( constraintMin );
//							prisonBlock.setConstraintMax( constraintMax );
//							prisonBlock.setConstraintExcludeTopLayers( constraintExcludeTopLayers );
//							prisonBlock.setConstraintExcludeBottomLayers( constraintExcludeBottomLayers );
//
//							
//							if ( prisonBlock.isLegacyBlock() ) {
//								dirty = true;
//							}
//							addPrisonBlock( prisonBlock );
//							
//							validateBlockNames.add( blockTypeName );
//						}
//						else if (validateBlockNames.contains( blockTypeName ) ) {
//							// Detected and fixed a duplication so mark as dirty so fixed block list is saved:
//							dirty = true;
//							inconsistancy = true;
//						}
//						
//					}
					
					
				}
			}
		}

        
        if ( isUseNewBlockModel() && 
        		getPrisonBlocks().size() == 0 && getBlocks().size() > 0 ) {
        	// Need to perform the initial conversion: 
        	
        	for ( BlockOld block : getBlocks() ) {
        		PrisonBlock prisonBlock = Prison.get().getPlatform().getPrisonBlock( block.getType().name() );
            	if ( prisonBlock != null ) {
            		
            		prisonBlock.setChance( block.getChance() );
            		prisonBlock.setBlockCountTotal( block.getBlockCountTotal() );
            		
            		addPrisonBlock( prisonBlock );

            		dirty = true;
            	}
        		
			}
        	Output.get().logInfo( "Notice: Mine: " + getName() + ": Existing prison block model has " +
        			"been converted to the new block model and will be saved." );
        }
        
        
        List<String> commands = (List<String>) document.get("commands");
        setResetCommands( commands == null ? new ArrayList<>() : commands );
        
        
        Boolean usePagingOnReset = (Boolean) document.get( "usePagingOnReset" );
        setUsePagingOnReset( usePagingOnReset == null ? false : usePagingOnReset.booleanValue() );

        
        List<String> mineBlockEvents = (List<String>) document.get("mineBlockEvents");
        if ( mineBlockEvents != null ) {
        	for ( String blockEvent : mineBlockEvents ) {
        		getBlockEvents().add( MineBlockEvent.fromSaveString( blockEvent ) );
        	}
        }
        
        
        String mineLinerData = (String) document.get("mineLinerData");
        setLinerData( MineLinerData.fromSaveString( mineLinerData ) );

        
        
        if ( dirty ) {
			
        	// Resave the mine data since an update to the mine format was detected and
        	// needs to be saved. Otherwise the bad data will always need to be converted
        	// every time the mine is loaded which may lead to other issues.
        	
        	// This is enabled since the original is not modified.

        	PrisonMines.getInstance().getMineManager().saveMine( this );
        	
        	if ( inconsistancy ) {
        		
        		Output.get().logInfo( "Notice: Mine: " + getName() + ": During the loading of this mine an " +
        				"inconsistancy was detected and was fixed then saved." );
        	}
        	else {
        		Output.get().logInfo( "Notice: Mine: " + getName() + ": Updated mine data was successfully saved." );
        		
        	}
        }
	}

    
    public Document toDocument() {
        Document ret = new Document();
        
        // If world name is not set, try to get it from the bounds:
        String worldName = getWorldName();
        if ( (worldName == null || worldName.trim().length() == 0 ||
        		"Virtually-Undefined".equalsIgnoreCase( worldName )) &&
        		getBounds() != null && getBounds().getMin() != null &&
        		getBounds().getMin().getWorld() != null ) {
        	worldName = getBounds().getMin().getWorld().getName();
        	setWorldName( worldName );
        }
        ret.put("world", worldName );
        ret.put("name", getName());
        
        ret.put( "isVirtual", isVirtual() );
        
        ret.put( "tag", getTag() );
        ret.put( "sortOrder", getSortOrder() );
        
        if ( !isVirtual() ) {
        	ret.put("minX", getBounds().getMin().getX());
        	ret.put("minY", getBounds().getMin().getY());
        	ret.put("minZ", getBounds().getMin().getZ());
        	ret.put("maxX", getBounds().getMax().getX());
        	ret.put("maxY", getBounds().getMax().getY());
        	ret.put("maxZ", getBounds().getMax().getZ());
        	ret.put("hasSpawn", isHasSpawn());
        	
        }
        
        ret.put("resetTime", getResetTime() );
        ret.put("notificationMode", getNotificationMode().name() );
        ret.put("notificationRadius", Long.valueOf( getNotificationRadius() ));

        ret.put( "zeroBlockResetDelaySec", Double.valueOf( getZeroBlockResetDelaySec() ) );
        
        ret.put( "skipResetEnabled", isSkipResetEnabled() );
        ret.put( "skipResetPercent", getSkipResetPercent() );
        ret.put( "skipResetBypassLimit", getSkipResetBypassLimit() );
        
        ret.put( "resetThresholdPercent", getResetThresholdPercent() );
        
        if (isHasSpawn()) {
            ret.put("spawnX", getSpawn().getX());
            ret.put("spawnY", getSpawn().getY());
            ret.put("spawnZ", getSpawn().getZ());
            ret.put("spawnPitch", getSpawn().getPitch());
            ret.put("spawnYaw", getSpawn().getYaw());
        }

        // This is a validation set to ensure only one block is written to file system:
        Set<String> validateBlockNames = new HashSet<>();

        List<String> blockStrings = new ArrayList<>();
        for (BlockOld block : getBlocks()) {
        	if ( !validateBlockNames.contains( block.getType().name() )) {
        		
        		blockStrings.add( block.toSaveFileFormat() );
        		
//        		// Use the BlockType.name() to save the block type to the file:
//        		blockStrings.add(block.getType().name() + "-" + block.getChance());
//            blockStrings.add(block.getType().getId() + "-" + block.getChance());
        		validateBlockNames.add( block.getType().name() );
        	}
        }
        
        ret.put("blocks", blockStrings);

        // reset validation for next block list:
        validateBlockNames.clear();
        
        List<String> prisonBlockStrings = new ArrayList<>();
        for (PrisonBlock pBlock : getPrisonBlocks() ) {
        	if ( !validateBlockNames.contains( pBlock.getBlockName()) ) {
        		
        		prisonBlockStrings.add( pBlock.toSaveFileFormat() );
        		
//        		prisonBlockStrings.add(pBlock.getBlockNameFormal() + "-" + pBlock.getChance());
        		validateBlockNames.add( pBlock.getBlockNameFormal() );
        	}
        }
        
        ret.put("prisonBlocks", prisonBlockStrings);

        ret.put("commands", getResetCommands());
        
        
        ret.put( "usePagingOnReset", isUsePagingOnReset() );
        
        
        if ( getRank() != null ) {
        	String rank = getRank().getModuleElementType() + "," + getRank().getName() + "," + 
        			getRank().getId() + "," + getRank().getTag();
        	ret.put("rank", rank );
        }

        List<String> mineBlockEvents = new ArrayList<>();
        for ( MineBlockEvent blockEvent : getBlockEvents() ) {
			mineBlockEvents.add( blockEvent.toSaveString() );
		}
        ret.put("mineBlockEvents", mineBlockEvents);
        
        
        String mineLinerData = getLinerData().toSaveString();
        ret.put("mineLinerData", mineLinerData );
        
        
        return ret;
    }

    @Override
    public String toString() {
    	return getName() + "  " + getTotalBlocksMined();
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


	public String getBlockListString()
	{
		StringBuilder sb = new StringBuilder();

       if ( isUseNewBlockModel() ) {
        	for ( PrisonBlock block : getPrisonBlocks()) {
        		if ( sb.length() > 0 ) {
        			sb.append( ", " );
        		}
        		sb.append( block.toString() );
        	}
        }
        else {
        	for ( BlockOld block : getBlocks() ) {
        		if ( sb.length() > 0 ) {
        			sb.append( ", " );
        		}
        		sb.append( block.toString() );
        	}
        }

		sb.insert( 0, ": [" );
		sb.append( "]" );
		sb.insert( 0, getName() );
		sb.insert( 0, "Mine " );
		
		return sb.toString();
	}

}
