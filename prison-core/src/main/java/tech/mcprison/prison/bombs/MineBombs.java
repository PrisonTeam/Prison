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
		sphere,
		sphereHollow
		;
		
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
		List<Location> results = new ArrayList<>();
		
		if ( loc != null && radius > 0 ) {
			int cenX = loc.getBlockX();
			int cenY = loc.getBlockY();
			int cenZ = loc.getBlockZ();
			
			double radiusSqr = radius * radius;
			double radiusHSqr = (radius - 1) * (radius - 1);
			
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
	
	public void setupDefaultMineBombData()
	{
		if ( getConfigData().getBombs().size() == 0 ) {

			MineBombData mbDataSmall = new MineBombData( 
					"SmallBomb", "brewing_stand", "sphere", 2, "Small Mine Bomb" );
			mbDataSmall.setDescription("A small mine bomb made with some chemicals and a brewing stand.");
			
			MineBombData mbDataMedium = new MineBombData( 
					"MediumBomb", "firework_rocket", "sphere", 5, "Medium Mine Bomb" );
			mbDataMedium.setDescription("A medium mine bomb made from leftover fireworks, " +
					"but supercharged with a strange green glowing liquid.");
			
			MineBombData mbDataLarge = new MineBombData( 
					"LargeBomb", "tnt", "sphereHollow", 12, "Large Mine Bomb" );
			mbDataLarge.setDescription("A large mine bomb made from TNT with some strange parts " +
					"that maybe be " +
					"described as alien technology.");
			
			MineBombData mbDataOof = new MineBombData( 
					"OofBomb", "tnt_minecart", "sphereHollow", 19, "Oof Mine Bomb" );
			mbDataOof.setDescription("An oof-ably large mine bomb made with a minecart heaping with TNT.  " +
					"Unlike the large mine bomb, this one obviously is built with alien technology.");
			
			
			getConfigData().getBombs().put( mbDataSmall.getName().toLowerCase(), mbDataSmall );
			getConfigData().getBombs().put( mbDataMedium.getName().toLowerCase(), mbDataMedium );
			getConfigData().getBombs().put( mbDataLarge.getName().toLowerCase(), mbDataLarge );
			getConfigData().getBombs().put( mbDataOof.getName().toLowerCase(), mbDataOof );
			
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
