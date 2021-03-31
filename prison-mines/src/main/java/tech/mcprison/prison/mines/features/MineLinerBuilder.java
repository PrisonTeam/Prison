package tech.mcprison.prison.mines.features;

import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.block.BlockFace;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.Bounds;
import tech.mcprison.prison.util.Bounds.Edges;
import tech.mcprison.prison.util.Location;

public class MineLinerBuilder {
	
	public static final String REPAIR_LINER = "repair_liner";

	private Mine mine;
	private Bounds liner;
	
	private LinerPatterns pattern;
	
	private boolean isForced = false;
	
	private List<List<List<String>>> pattern3d = null;
	
	
	public enum LinerPatterns {
		
		bright,
		white,
		
		blackAndWhite,
		seaEchos,
		obby,
		bedrock,
		glowingPlanks,
		darkOakPrismarine,
		beacon,
		bricked,
		
		repair,
		remove,
		removeAll
		;
		
		public static LinerPatterns fromString( String pattern ) {
			LinerPatterns results = null;
			
			if ( pattern != null && pattern.trim().length() > 0 ) {
				for ( LinerPatterns lp : values() ) {
					if ( lp.name().equalsIgnoreCase( pattern.trim() )) {
						results = lp;
					}
				}
			}
			
			return results;
		}

		public static String toStringAll() {
			StringBuilder sb = new StringBuilder();
			
			for ( LinerPatterns pattern : values() )
			{
				if ( sb.length() > 0 ) {
					sb.append( " " );
				}
				
				sb.append(  pattern.name() );
			}
			
			return sb.toString();
		}

	}
	
	/** 
	 * Use only in jUnit tests.
	 */
	protected MineLinerBuilder() {
		super();
		
		this.pattern3d = new ArrayList<>();
		
	}
	
	public MineLinerBuilder( Mine mine, Edges edge, LinerPatterns pattern, boolean isForced ) {
		super();
		
		this.pattern3d = new ArrayList<>();
		
		this.mine = mine;
		
		// Liner is one larger in walls and depth.
		this.liner = 
				new Bounds( 
						new Bounds( mine.getBounds(), 
								Edges.bottom, 1 ), Edges.walls, 1);
		
		this.pattern = pattern;
		
		this.isForced = isForced;
		
		if ( pattern != null ) {
			mine.enableTracer();
			
			generatePattern( edge );
		}
	}
	
	private void generatePattern( Edges edge ) {
		
		World world = getLiner().getMin().getWorld();
		
		int xMin = getLiner().getxBlockMin();
		int yMin = getLiner().getyBlockMin();
		int zMin = getLiner().getzBlockMin();

		int xMax = getLiner().getxBlockMax();
		int yMax = getLiner().getyBlockMax();
		int zMax = getLiner().getzBlockMax();
		
		switch ( edge )
		{
			case walls:
				generatePattern( Edges.north );
				generatePattern( Edges.east );
				generatePattern( Edges.south );
				generatePattern( Edges.west );
			
				break;
				
			case top:
				
				select2DPattern( edge );
				// Top is where yMax is constant (yMin = yMax):
				generatePattern( edge, world, xMin, xMax, yMax, yMax, zMin, zMax );
				break;
				
			case bottom:
				
				select2DPattern( edge );
				// Bottom is where yMin is constant (yMax = yMin):
				generatePattern( edge, world, xMin, xMax, yMin, yMin, zMin, zMax );
				
				break;
				
			case north:
				// North is in the direction of negative Z
				
				select2DPattern( edge );
				// North is where zMin is constant (zMax = zMin):
				generatePattern( edge, world, xMin, xMax, yMin, yMax, zMin, zMin );
				
				break;

			case south:
				// South is in the direction of positive Z
				
				select2DPattern( edge );
				// South is where zMax is constant (zMin = zMax):
				generatePattern( edge, world, xMin, xMax, yMin, yMax, zMax, zMax );

				break;
				
			case east:
				
				select2DPattern( edge );
				// East is where xMax is constant (xMin = xMax):
				generatePattern( edge, world, xMax, xMax, yMin, yMax, zMin, zMax );
				
				break;

			case west:
				
				select2DPattern( edge );
				// West is where xMin is constant (xMax = xMin):
				generatePattern( edge, world, xMin, xMin, yMin, yMax, zMin, zMax );
				
				break;
				
			default:
				break;
		}
	}
	
	
	
	private void generatePattern( Edges edge, World world, int xMin, int xMax, int yMin, int yMax, int zMin, int zMax) {
		try {
			
			// Output.get().logInfo( "MineRest.resetSynchonouslyInternal() " + getName() );

//			Output.get().logInfo( "### MineLinerBuilder - xMin=%d, xMax=%d, yMin=%d, yMax=%d, zMin=%d, zMax=%d ",
//					xMin, xMax, yMin, yMax, zMin, zMax);
			
			
			boolean isLadderPossible = false;
			
			
			BlockFace blockFace = null;
//			BlockFace blockFaceOpposite = null;
			switch ( edge )
			{
				case north:
					blockFace = BlockFace.NORTH;
//					blockFaceOpposite = BlockFace.SOUTH;
					isLadderPossible = true;
					break;
				case south:
					blockFace = BlockFace.SOUTH;
//					blockFaceOpposite = BlockFace.NORTH;
					isLadderPossible = true;
					break;
				case east:
					blockFace = BlockFace.EAST;
//					blockFaceOpposite = BlockFace.WEST;
					isLadderPossible = true;
					break;
				case west:
					blockFace = BlockFace.WEST;
//					blockFaceOpposite = BlockFace.EAST;
					isLadderPossible = true;
					break;
				case top:
					blockFace = BlockFace.UP;
//					blockFaceOpposite = BlockFace.UP;
					break;
				case bottom:
					blockFace = BlockFace.DOWN;
//					blockFaceOpposite = BlockFace.DOWN;
					break;

				default:
					break;
			}
			
			
			for (int y = yMin; y <= yMax + (isForced && yMin > yMax ? -1 : 0); y++) {
				
				for (int x = xMin; x <= xMax; x++) {

					// Get the block-pattern-x position, mapped relative to the 2d pattern:
					int x3d = (x - xMin) % getPattern3d().size();
					
					// Get the block-pattern-x position, mapped relative to the 2d pattern:
					int y3d = (y - yMin) % getPattern3d().get( x3d ).size();
					
					
					for (int z = zMin; z <= zMax; z++) {
						
						// Get the block-pattern-z position, mapped relative to the d2 pattern:
						int z3d = (z - zMin) % getPattern3d().get( x3d ).get( y3d ).size();
						
						String nextBlockName = getPattern3d().get( x3d ).get( y3d ).get( z3d );
						
						boolean isLadderBlock = isLadderPossible && y > yMin && ( 
												isLadderBlock( x, xMin, xMax ) ||
												isLadderBlock( z, zMin, zMax ));
						
//						Output.get().logInfo( "### MineLinerBuilder - %s  %s  %s isLadder=%s   x=%d, y=%d, z=%d  " +
//								"  block: %s  ",
//								(isXPos || isZPos) ? "Y" : "N", (isX1 || isZ1) ? "Y" : "N", (isX2 || isZ2) ? "Y" : "N",
//								(isLadderBlock ? "Y" : "N"), x, y, z,  nextBlockName);
						
						Location targetLocation = new Location(world, x, y, z);
						Block tBlock = targetLocation.getBlockAt();
						Block tBlockPlus1 = getRelativeBlock( targetLocation, edge, 1 );
						Block tBlockPlus2 = getRelativeBlock( targetLocation, edge, 2 );
						
						
//						Output.get().logInfo( "### MineLinerBuilder - %s isLadder=%s  " +
//								"Loc:%s tB:%s tB1:%s tB2:%s  " +
//								" x=%s y=%d z=%d  block: %s  ",
//								edge, (isLadderBlock ? "Y" : " "), 
//								targetLocation.toBlockCoordinates(),
//								tBlock.getLocation().toBlockCoordinates(), 
//								tBlockPlus1.getLocation().toBlockCoordinates(), 
//								tBlockPlus2.getLocation().toBlockCoordinates(), 
//									x, y, z,  nextBlockName);
						
						if ( getMine().isUseNewBlockModel() ) {
							
							if ( REPAIR_LINER.equalsIgnoreCase( nextBlockName ) ) {
								
								if ( isLadderBlock ) {

									tBlock.setPrisonBlock( tBlockPlus2.getPrisonBlock() );
									tBlockPlus1.setPrisonBlock( tBlockPlus2.getPrisonBlock() );
								}
								else {
									
									tBlock.setPrisonBlock( tBlockPlus1.getPrisonBlock() );
								}
							}
							
							else if ( isForced ||
									!tBlock.isEmpty() ||
									isLadderBlock && !tBlockPlus1.isEmpty() ) {
										
								PrisonBlock nextBlockType = new PrisonBlock(nextBlockName);
								
								if ( isLadderBlock ) {
									
									tBlockPlus1.setPrisonBlock( nextBlockType );

									PrisonBlock ladderBlockType = new PrisonBlock("ladder");
									tBlock.setPrisonBlock( ladderBlockType );
									tBlock.setBlockFace( blockFace );
								}
								else {
									
									tBlock.setPrisonBlock( nextBlockType );
								}
							}
							
						}
						else {
							
							if ( REPAIR_LINER.equalsIgnoreCase( nextBlockName ) ) {
								
								
//								Output.get().logInfo( "#### repair : isLadderBlock: %s block types:  tb: %s  tb1: %s tb2: %s",
//										(isLadderBlock ? "Y" : "-" ),
//										tBlock.getType(), tBlockPlus1.getType(), tBlockPlus2.getType() );
//								
								if ( isLadderBlock ) {
									
									tBlock.setType( tBlockPlus2.getType() );
									tBlockPlus1.setType( tBlockPlus2.getType() );
								}
								else {
									
									tBlock.setType( tBlockPlus1.getType() );
								}
							}
							
							else if ( isForced ||
									!tBlock.isEmpty() ||
									isLadderBlock && !tBlockPlus1.isEmpty() ) {
								
								BlockType nextBlockType = BlockType.fromString( nextBlockName );
//								if ( nextBlockType == null ) {
//									nextBlockType = BlockType.fromString( nextBlockName );
//								}
								
								if ( isLadderBlock ) {
									
									tBlockPlus1.setType( nextBlockType );

									BlockType ladderBlockType = BlockType.LADDER;
									tBlock.setType( ladderBlockType );
									tBlock.setBlockFace( blockFace );
								}
								else {
									
									tBlock.setType( nextBlockType );
								}
								
							}
						}
					}
				}
				
				
			}
			
			
		} catch (Exception e) {
			Output.get().logError("&cFailed to generate the mine liner " + getMine().getName(), e);
		}

	}
	
	/**
	 * <p>This identifies if the curr position should be a ladder block. This
	 * will identify either two or three ladder points, depending upon 
	 * if the length is odd or even.
	 * </p>
	 * 
	 * <p>The center point will always be a ladder point.  But if the length 
	 * is even
	 * then the next block after the mid is also a ladder point.  But if
	 * the length is odd, then include both sides of the center point.
	 * </p>
	 * 
	 * <p>Skip checks: if min == max then that's the face we are building... so skip because
	 * it's the other two dimension that are being processed. Since the liner
	 * is one block bigger than the mine on each end, skip if curr is also
	 * equal to min or max (the corners).
	 * </p>
	 * 
	 * <p>If the mine on the edge is 3 blocks or less in width, have them all
	 * be ladders.  
	 * 
	 * @param curr
	 * @param min
	 * @param max
	 * @return
	 */
	private boolean isLadderBlock( int curr, int min, int max ) {
		
		boolean results = false;
		
		// Skip if the face or corners of liner.
		if ( min != max && curr != min && curr != max ) {
			
			int len = max - min + 1;
			boolean isEven = len % 2 == 0;

			int mid = (int) Math.floor( len / 2d ) + ( isEven ? -1 : 0);

			// The following is actually 3 blocks since max and min are
			// skipped due to being corners.  So if the min is 1 to 3 blocks
			// wide, always have ladders that wide.
			results = len <= 5;
			
			if ( len > 5 ) {
				
				
				if ( curr == (min + mid) ) {
					results = true;
				}
				else {
					results = isEven ?
						// if distance is even, then next ladder position is mid - 1
						 ( curr == min + mid + 1 ) :
						// If odd, then one above and below mid:
						( curr == min + mid + 1 || curr == min + mid - 1);
				}

			}

//			Output.get().logInfo( "#### isLadderBlock: curr=%d min=%d max=%d  " +
//					"  len=%d  mid=%d  " +
//					"isEven=%s  results=%s " +
//					" (min+mid)=%d ",
//					curr, min, max, len, mid,
//					(isEven ? "true" : "false"),
//					(results ? "true" : "false"), (min+mid) );
			
		}
		
		return results;
	}
	
	/**
	 * <p>This gets a block that is offset in the direction (edge) that is
	 * specified.
	 * </p>
	 * 
	 * @param location
	 * @param edge
	 * @param offset
	 * @return
	 */
	private Block getRelativeBlock( Location location, Edges edge, int offset )
	{
		Location relLoc = new Location( location );
		switch ( edge )
		{
			case north:
				relLoc.setZ( relLoc.getBlockZ() - offset );
				break;
			case south:
				relLoc.setZ( relLoc.getBlockZ() + offset );
				break;
			case east:
				relLoc.setX( relLoc.getBlockX() + offset );
				break;
			case west:
				relLoc.setX( relLoc.getBlockX() - offset );
				break;
			case top:
				relLoc.setY( relLoc.getBlockY() + offset );
				break;
			case bottom:
				relLoc.setY( relLoc.getBlockY() - offset );
				break;

			default:
				break;
		}

		Block block = relLoc.getBlockAt();
				
		return block;
	}


	/**
	 * The block names that are used in these 2D patterns much match both
	 * the BlockType enums (the old prison block names) and the Xmaterial names.
	 * If they don't match both, use the XMaterial names, then add that name to the 
	 * BlockType enum as an XMaterial altName.
	 * 
	 * @param edge
	 */
	private void select2DPattern( Edges edge ) {
		
		String[][] pattern2d = null;
		
		switch ( getPattern() )
		{
			case repair:
				String[][] repair =
				{
						{ REPAIR_LINER }
				};
				pattern2d = repair;
				break;
				
			
			case blackAndWhite:
				String[][] baw =
				{
						{ "obsidian", "pillar_quartz_block" },
						{ "pillar_quartz_block", "coal_block" }
				};
				pattern2d = baw;
				break;
				
			
			case seaEchos:
				String[][] seaEchos =
				{
						{ "pillar_quartz_block", "pillar_quartz_block", "pillar_quartz_block" },
						{ "pillar_quartz_block", "obsidian", "obsidian" },
						{ "pillar_quartz_block", "obsidian", "sea_lantern" },
				};
				pattern2d = seaEchos;
				break;
				
				
			case beacon:
				String[][] beacon =
				{
						{ "beacon", "diamond_block", "diamond_block", "diamond_block" },
						{ "diamond_block", "diamond_block", "diamond_block", "diamond_block" },
						{ "diamond_block", "diamond_block", "diamond_block", "diamond_block" }
				};
				pattern2d = beacon;
				break;
				
				
			case obby:
				String[][] obby =
				{
						{ "obsidian" }
				};
				pattern2d = obby;
				break;
				
				
			case bedrock:
				String[][] bedrock =
				{
						{ "bedrock" }
				};
				pattern2d = bedrock;
				break;
				
				
			case glowingPlanks:
				String[][] glowingPlanks =
				{
						{ "dark_oak_planks", "spruce_planks", "acacia_planks", "glowstone" }, // dsag
						{ "birch_planks", "acacia_planks", "jungle_planks", "dark_oak_planks" },     // bajd
						{ "acacia_planks", "glowstone", "dark_oak_planks", "spruce_planks" }, // dsag
						{ "jungle_planks", "dark_oak_planks", "birch_planks", "acacia_planks" },     // bajd
						
						{ "dark_oak_planks", "birch_planks", "acacia_planks", "jungle_planks" },      // dbaj
						{ "spruce_planks", "acacia_planks", "glowstone", "dark_oak_planks" },  // sagd
						{ "acacia_planks", "jungle_planks", "dark_oak_planks", "birch_planks" },      // dbaj
						{ "glowstone", "dark_oak_planks", "spruce_planks", "acacia_planks" }   // sagd
				};
				pattern2d = glowingPlanks;
				break;

				
			case darkOakPrismarine:
				String[][] darkOakPrismarine =
				{
						{ "prismarine_bricks", "dark_prismarine", "dark_oak_planks", "prismarine" },
						{ "dark_oak_planks", "prismarine", "prismarine_bricks", "dark_prismarine" }
				};
				pattern2d = darkOakPrismarine;
				break;
				
				
			case bricked:
				String[][] bricked =
				{
						{ "prismarine_bricks", "jungle_planks", "brick_block" },
						{ "mossy_stone_bricks", "dark_prismarine", "dark_oak_planks" },
						{ "spruce_planks", "nether_bricks", "sea_lantern" }
				};
				pattern2d = bricked;
				break;

				
			case white: 
				String[][] white =
				{
					{ "iron_block", "chiseled_quartz_block" },
					{ "chiseled_quartz_block", "iron_block" }
				};
				pattern2d = white;
				break;

				
			case bright:
			default:
				
				String[][] bright =
						{
							{ "iron_block", "end_stone" },
							{ "end_stone", "glowstone" },
							{ "iron_block", "end_stone" },

							{ "chiseled_quartz_block", "quartz_pillar" },
							{ "glowstone", "quartz_block" },
							{ "chiseled_quartz_block", "quartz_pillar" }
						};
				pattern2d = bright;
				
				break;
		}
		
		apply2Dto3DPattern( edge, pattern2d );
	}
	
		
	protected void apply2Dto3DPattern( Edges edge, String[][] pattern2d )
	{
		// This is a 3d nested list:
		pattern3d = new ArrayList<>();
		
		for ( int a = 0; a < pattern2d.length; a++ ) {
			String[] aArray = pattern2d[a];
			
			for ( int b = 0; b < aArray.length; b++ ) {
				String value = pattern2d[a][b];
				
				switch ( edge )
				{
					case top:
					case bottom:
						// Top or bottom requires Y to be zero since that is the unchanging dimension:
						{
							// For each value of a add X list:
							List<List<String>> xList = null;
							if ( pattern3d.size() == a ) {
								xList = new ArrayList<>();
								pattern3d.add( xList );
							}
							else {
								xList = pattern3d.get( a );
							}
							
							// y is static and always zero:
							List<String> yList = null;
							if ( xList.size() == 0 ) {
								yList = new ArrayList<>();
								xList.add( yList );
							}
							else {
								yList = xList.get( 0 );
							}
							
							// Add the Z for each value of b:
							yList.add( value );
						}
						
						break;
						
					case north:
					case south:
						// North or south requires z to be zero for the unchanging dimension:
						// For each value of a add X list:
						{
							// For each value of a add X list:
							List<List<String>> xList = null;
							if ( pattern3d.size() == a ) {
								xList = new ArrayList<>();
								pattern3d.add( xList );
							}
							else {
								xList = pattern3d.get( a );
							}
							
							// y is static and always zero:
							List<String> yList = null;
							if ( xList.size() == b ) {
								yList = new ArrayList<>();
								xList.add( yList );
							}
							else {
								yList = xList.get( b );
							}
							
							// Add the value for z. It will always be the zeroth element of y:
							yList.add( value );
						}
					
						break;
					
					case east:
					case west:
						// east or west requires x to be zero for the unchanging dimension:
						{
							// For each value of a add X list:
							List<List<String>> xList = null;
							if ( pattern3d.size() == 0 ) {
								xList = new ArrayList<>();
								pattern3d.add( xList );
							}
							else {
								xList = pattern3d.get( 0 );
							}
							
							// y is static and always zero:
							List<String> yList = null;
							if ( xList.size() == a ) {
								yList = new ArrayList<>();
								xList.add( yList );
							}
							else {
								yList = xList.get( a );
							}
							
							// Add the value for each value of b:
							yList.add( value );
						}
						
						break;


					default:
						break;
				}
				
			}
		}
		
		
	}


	public List<List<List<String>>> getPattern3d() {
		return pattern3d;
	}
	public void setPattern3d( List<List<List<String>>> pattern3d ) {
		this.pattern3d = pattern3d;
	}

	public Mine getMine() {
		return mine;
	}
	public void setMine( Mine mine ) {
		this.mine = mine;
	}

	public Bounds getLiner() {
		return liner;
	}
	public void setLiner( Bounds liner ) {
		this.liner = liner;
	}

	public LinerPatterns getPattern() {
		return pattern;
	}
	public void setPattern( LinerPatterns pattern ) {
		this.pattern = pattern;
	}

	public boolean isForced() {
		return isForced;
	}
	public void setForced( boolean isForced ) {
		this.isForced = isForced;
	}
	
}
