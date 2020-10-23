package tech.mcprison.prison.mines.data;

import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.Bounds;
import tech.mcprison.prison.util.Bounds.Edges;
import tech.mcprison.prison.util.Location;

public class MineLinerBuilder {

	private Mine mine;
	private Bounds liner;
	
	private LinerPatterns pattern;
	
	private List<List<List<String>>> pattern3d = null;
	
	
	public enum LinerPatterns {
		
		bright,
		white,
		
		blackAndWhite,
		seaEchos,
		obby
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
	
	public MineLinerBuilder( Mine mine, Edges edge, LinerPatterns pattern ) {
		super();
		
		this.pattern3d = new ArrayList<>();
		
		this.mine = mine;
		
		// Liner is one larger in walls and depth.
		this.liner = 
				new Bounds( 
						new Bounds( mine.getBounds(), 
								Edges.bottom, 1 ), Edges.walls, 1);
		
		this.pattern = pattern;
		
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
				generatePattern( world, xMin, xMax, yMax, yMax, zMin, zMax );
				break;
				
			case bottom:
				
				select2DPattern( edge );
				// Bottom is where yMin is constant (yMax = yMin):
				generatePattern( world, xMin, xMax, yMin, yMin, zMin, zMax );
				
				break;
				
			case north:
				
				select2DPattern( edge );
				// North is where zMax is constant (zMin = zMax):
				generatePattern( world, xMin, xMax, yMin, yMax, zMax, zMax );
				
				insertLadders( edge, world, xMin, xMax, yMin, yMax, zMax, zMax );
				
				break;

			case south:
				
				select2DPattern( edge );
				// South is where zMin is constant (zMax = zMin):
				generatePattern( world, xMin, xMax, yMin, yMax, zMin, zMin );

				insertLadders( edge, world, xMin, xMax, yMin, yMax, zMax, zMax );
				
				break;
				
			case east:
				
				select2DPattern( edge );
				// East is where xMin is constant (xMax = xMin):
				generatePattern( world, xMin, xMin, yMin, yMax, zMin, zMax );
				
				insertLadders( edge, world, xMin, xMax, yMin, yMax, zMax, zMax );
			
				break;

			case west:
				
				select2DPattern( edge );
				// West is where xMax is constant (xMin = xMax):
				generatePattern( world, xMax, xMax, yMin, yMax, zMin, zMax );
				
				insertLadders( edge, world, xMin, xMax, yMin, yMax, zMax, zMax );
				
				break;
				
			default:
				break;
		}
	}
	
	/**
	 * This function perhaps should be combined with generatePattern() to automatically insert ladders
	 * in one pass.
	 * 
	 * @param edge
	 * @param world
	 * @param xMin
	 * @param xMax
	 * @param yMin
	 * @param yMax
	 * @param zMin
	 * @param zMax
	 */
	private void insertLadders( Edges edge, World world, int xMin, int xMax, int yMin, int yMax, int zMin, int zMax) {
		
	}
	
	
	private void generatePattern( World world, int xMin, int xMax, int yMin, int yMax, int zMin, int zMax) {
		try {
			
			boolean useNewBlockModel = Prison.get().getPlatform().getConfigBooleanFalse( "use-new-prison-block-model" );
			
			// Output.get().logInfo( "MineRest.resetSynchonouslyInternal() " + getName() );

//			Output.get().logInfo( "### MineLinerBuilder - xMin=%d, xMax=%d, yMin=%d, yMax=%d, zMin=%d, zMax=%d ",
//					xMin, xMax, yMin, yMax, zMin, zMax);
			
			
			for (int x = xMin; x <= xMax; x++) {
				
				// Get the block-pattern-x position, mapped relative to the 2d pattern:
				int x3d = (x - xMin) % getPattern3d().size();
				
				for (int y = yMin; y <= yMax; y++) {

					// Get the block-pattern-x position, mapped relative to the 2d pattern:
					int y3d = (y - yMin) % getPattern3d().get( x3d ).size();
					
					
					for (int z = zMin; z <= zMax; z++) {
						
						// Get the block-pattern-z position, mapped relative to the d2 pattern:
						int z3d = (z - zMin) % getPattern3d().get( x3d ).get( y3d ).size();
						
						String nextBlockName = getPattern3d().get( x3d ).get( y3d ).get( z3d );
						
//						Output.get().logInfo( "### MineLinerBuilder - block: %s   x=%d, y=%d, z=%d  " +
//								"x3d=%d, y3d=%d, z3d=%d ",
//								nextBlockName, x, y, z, x3d, y3d, z3d);
						
						Location targetLocation = new Location(world, x, y, z);
						Block targetBlock = targetLocation.getBlockAt();
						
						// Do not replace any air blocks: This allows us to follow the contour of
						// the terrain.
						
						if ( useNewBlockModel ) {
							
							if ( !targetBlock.isEmpty() ) {
								PrisonBlock nextBlockType = new PrisonBlock(nextBlockName);
								targetBlock.setPrisonBlock( nextBlockType );
							}
							
						}
						else {
							
							if ( !targetBlock.isEmpty() ) {
								BlockType nextBlockType = BlockType.fromString( nextBlockName );
								targetBlock.setType( nextBlockType );
							}
						}
					}
				}
				
				
			}
			
			
		} catch (Exception e) {
			Output.get().logError("&cFailed to generate the mine liner " + getMine().getName(), e);
		}

	}
	
	private void select2DPattern( Edges edge ) {
		
		String[][] pattern2d = null;
		
		switch ( getPattern() )
		{
			
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
				
			case obby:
				String[][] obby =
				{
						{ "obsidian" }
				};
				pattern2d = obby;
				
				break;
				
			case white: 
				String[][] white =
				{
					{ "iron_block", "end_stone" },
					{ "end_stone", "iron_block" }
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

							{ "chiseled_quartz_block", "pillar_quartz_block" },
							{ "glowstone", "quartz_stone" },
							{ "chiseled_quartz_block", "pillar_quartz_block" }
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
	
}
