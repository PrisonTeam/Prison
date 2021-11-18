package tech.mcprison.prison.bombs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.file.JsonFileIO;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.util.Location;

public class MineBombs
{
	public static final String MINE_BOMBS_FILE_NAME = "mineBombsConfig.json";
	public static final String MINE_BOMBS_PATH_NAME = "module_conf/mines";
	
	private static MineBombs instance;
	
	private final MineBombsConfigData configData;
	
	
	
	public enum ExplosionShape {
		
		cube,
		
		sphere,
		sphereHollow
		;
		
		public static ExplosionShape fromString( String shape ) {
			ExplosionShape results = ExplosionShape.sphere;
			
			if ( shape != null ) {
				for ( ExplosionShape xShape : values() )
				{
					if ( xShape.name().equalsIgnoreCase( shape ) ) {
						results = xShape;
						break;
					}
				}
			}
			
			return results;
		}
	}
	
	private MineBombs() {
		super();
		
		this.configData = new MineBombsConfigData();
		
		loadConfigJson();
	}

	public static MineBombs getInstance() {
		if ( instance == null ) {
			synchronized ( MineBombs.class ) {
				if ( instance == null ) {
					
					instance = new MineBombs();
				}
			}
		}
		return instance;
	}
	
	/**
	 * <p>This finds a bomb with the given name, and returns a clone.  The clone is
	 * important since individual instances will set the isActivated() variable to 
	 * true if the bomb is active.  If it's activated, then that indicates the 
	 * bomb will be used and the bomb was removed from the player's inventory.
	 * </p>
	 * 
	 * @param bombName
	 * @return
	 */
	public MineBombData findBomb( String bombName ) {
		MineBombData bombOriginal = null;

		if ( bombName != null ) {
			
			bombOriginal = getConfigData().getBombs().get( bombName.toLowerCase() );
		}
		
		return bombOriginal.clone();
	}
	
	public void saveConfigJson() {
		
		JsonFileIO fio = new JsonFileIO( null, null );
		
		File configFile = getConfigFile( fio );
		
		fio.saveJsonFile( configFile, getConfigData() );
		
	}
	
	public File getConfigFile( JsonFileIO jsonFileIO ) {

		File path = new File( jsonFileIO.getProjectRootDiretory(), MINE_BOMBS_PATH_NAME );
		path.mkdirs();
		File configFile = new File( path, MINE_BOMBS_FILE_NAME );
		
		return configFile;
	}
	
	public void loadConfigJson() {
		JsonFileIO fio = new JsonFileIO( null, null );
		
		File configFile = getConfigFile( fio );
		
		if ( !configFile.exists() ) {
			setupDefaultMineBombData();
		}
		
		else {
			
			MineBombsConfigData configs = 
					(MineBombsConfigData) fio.readJsonFile( configFile, getConfigData() );
			
			if ( configs != null && configs.getBombs().size() > 0 ) {
				
				getConfigData().getBombs().putAll( configs.getBombs() );
				
			}
		}
	}
	
	public List<Location> calculateSphere( Location loc, int radius, boolean hollow ) {
		
		return calculateSphere( loc, radius, hollow, 0 );
	}
	
	public List<Location> calculateSphere( Location loc, int radius, boolean hollow, int radiusInner ) {
		List<Location> results = new ArrayList<>();
		
		if ( loc != null && radius > 0 ) {
			int cenX = loc.getBlockX();
			int cenY = loc.getBlockY();
			int cenZ = loc.getBlockZ();
			
			double radiusSqr = radius * radius;
			
			// If the radiusInner is not specified (== 0), then subtract one from radius.
			double radiusHSqr = radiusInner == 0 ?
									((radius - 1) * (radius - 1)) : 
										(radiusInner * radiusInner);
			
			for ( int x = cenX - radius ; x <= cenX + radius ; x++ ) {
				double xSqr = (cenX - x) * (cenX - x);
				for ( int y = cenY - radius ; y <= cenY + radius ; y++ ) {
					double ySqr = (cenY - y) * (cenY - y);
					for ( int z = cenZ - radius ; z <= cenZ + radius ; z++ ) {
						double zSqr = (cenZ - z) * (cenZ - z);
						
						double distSqr = xSqr + ySqr + zSqr;
						
						if ( distSqr <= radiusSqr &&
								(!hollow || 
								 hollow && distSqr >= radiusHSqr )) {
							
							Location l = new Location( loc.getWorld(), x, y, z );
							results.add( l );
						}
					}
				}
			}
		}
		return results;
	}
	
	
	public List<Location> calculateCube( Location loc, int radius ) {
		List<Location> results = new ArrayList<>();
		
		if ( loc != null && radius > 0 ) {
			int cenX = loc.getBlockX();
			int cenY = loc.getBlockY();
			int cenZ = loc.getBlockZ();
						
			for ( int x = cenX - radius ; x <= cenX + radius ; x++ ) {
				for ( int y = cenY + 1; y >= 0 && y >= cenY + 1 - (radius * 2) ; y-- ) {
					for ( int z = cenZ - radius ; z <= cenZ + radius ; z++ ) {
						
						Location l = new Location( loc.getWorld(), x, y, z );
						results.add( l );
						
					}
				}
			}
		}
		return results;
	}
	
	public void setupDefaultMineBombData()
	{
		if ( getConfigData().getBombs().size() == 0 ) {

//			XMaterial.WOODEN_PICKAXE;
//			XMaterial.STONE_PICKAXE;
//			XMaterial.IRON_PICKAXE;
//			XMaterial.GOLDEN_PICKAXE;
//			XMaterial.DIAMOND_PICKAXE;
//			XMaterial.NETHERITE_PICKAXE;

			{
				MineBombData mbd = new MineBombData( 
						"SmallBomb", "brewing_stand", ExplosionShape.sphere.name(), 2, "Small Mine Bomb" );
				mbd.setToolInHandName( "DIAMOND_PICKAXE" );
				mbd.setToolInHandFortuneLevel( 0 );
				mbd.setDescription("A small mine bomb made with some chemicals and a brewing stand.");
				
				getConfigData().getBombs().put( mbd.getName().toLowerCase(), mbd );
				
			}
			
			{
				MineBombData mbd = new MineBombData( 
						"MediumBomb", "firework_rocket", ExplosionShape.sphere.name(), 5, "Medium Mine Bomb" );
				mbd.setDescription("A medium mine bomb made from leftover fireworks, " +
						"but supercharged with a strange green glowing liquid.");
				mbd.setToolInHandName( "DIAMOND_PICKAXE" );
				mbd.setToolInHandFortuneLevel( 3 );

				getConfigData().getBombs().put( mbd.getName().toLowerCase(), mbd );
				
			}

			{
				MineBombData mbd = new MineBombData( 
						"LargeBomb", "tnt", ExplosionShape.sphereHollow.name(), 12, "Large Mine Bomb" );
				mbd.setRadiusInner( 3 );
				mbd.setDescription("A large mine bomb made from TNT with some strange parts " +
						"that maybe be described as alien technology.");
				mbd.setToolInHandName( "DIAMOND_PICKAXE" );
				mbd.setToolInHandFortuneLevel( 3 );
				
				getConfigData().getBombs().put( mbd.getName().toLowerCase(), mbd );
			}
			
			{
				MineBombData mbd = new MineBombData( 
						"OofBomb", "tnt_minecart", ExplosionShape.sphereHollow.name(), 19, "Oof Mine Bomb" );
				mbd.setRadiusInner( 3 );
				mbd.setDescription("An oof-ably large mine bomb made with a minecart heaping with TNT.  " +
						"Unlike the large mine bomb, this one obviously is built with alien technology.");
				
				mbd.setToolInHandName( "GOLDEN_PICKAXE" );
				mbd.setToolInHandFortuneLevel( 13 );
				
				mbd.setAutosell( true );
				mbd.setGlowing( true );
				mbd.setAutosell( true );
				
				getConfigData().getBombs().put( mbd.getName().toLowerCase(), mbd );
			}
			
			{
				MineBombData mbd = new MineBombData( 
						"WimpyBomb", "GUNPOWDER", ExplosionShape.sphere.name(), 5, 
							"A Wimpy Mine Bomb" );
				mbd.setRadiusInner( 2 );
				mbd.setDescription("A whimpy bomb made with gunpowder and packs the punch of a " +
						"dull wooden pickaxe. For some reason, it only has a 40% chance of removing " +
						"a block.");
				
				mbd.setToolInHandName( "WOODEN_PICKAXE" );
				mbd.setToolInHandFortuneLevel( 0 );
				mbd.setRemovalChance( 40.0d );
				
				getConfigData().getBombs().put( mbd.getName().toLowerCase(), mbd );
			}
			
			
			{
				MineBombData mbd = new MineBombData( 
						"CubeBomb", "SLIME_BLOCK", ExplosionShape.cube.name(), 2, 
						"A Cubic Bomb" );
				mbd.setDescription("The most anti-round bomb you will ever be able to find. " +
						"It's totally cubic.");
				
				mbd.setToolInHandName( "DIAMOND_PICKAXE" );
				mbd.setToolInHandFortuneLevel( 7 );
				mbd.setRemovalChance( 100.0d );
				
				mbd.setGlowing( true );
				
				getConfigData().getBombs().put( mbd.getName().toLowerCase(), mbd );
			}
			
			
			saveConfigJson();
			
			Output.get().logInfo( "Mine bombs: setup default values." );
		}
		else {
			Output.get().logInfo( "Could not generate a mine bombs save file since at least one " +
						"mine bomb already exists." );
		}
		
	}

	public MineBombsConfigData getConfigData() {
		return configData;
	}

}
