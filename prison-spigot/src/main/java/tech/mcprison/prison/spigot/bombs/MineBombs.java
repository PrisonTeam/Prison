package tech.mcprison.prison.spigot.bombs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.file.JsonFileIO;
import tech.mcprison.prison.util.Location;

public class MineBombs
{
	public static final String MINE_BOMBS_FILE_NAME = "mineBombsConifg.json";
	public static final String MINE_BOMBS_PATH_NAME = "module_conf/mines";
	
	private static MineBombs instance;
	
	private MineBombsConfigData configData;
	
	
	
	public enum ExplosionShape {
		sphere,
		sphereHollow
		;
		
	}
	
	private MineBombs() {
		super();
		
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
		
		MineBombsConfigData configs = 
				(MineBombsConfigData) fio.readJsonFile( configFile, getConfigData() );
		
		setConfigData( configs );
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

	public MineBombsConfigData getConfigData() {
		return configData;
	}
	public void setConfigData( MineBombsConfigData configData ) {
		this.configData = configData;
	}

}
