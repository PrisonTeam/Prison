package tech.mcprison.prison.bombs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.file.JsonFileIO;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.output.LogLevel;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.util.Location;
import tech.mcprison.prison.util.Text;

public class MineBombs
	implements Comparable<MineBombs>
{
	public static final String MINE_BOMBS_NBT_KEY = "MineBombNbtKey";
	public static final String MINE_BOMBS_NBT_THROWER_UUID = "MineBombNbtThrowerUUID";
	public static final String MINE_BOMBS_NBT_OWNER_UUID = "MineBombNbtOwnerUUID";

	public static final String MINE_BOMBS_FILE_NAME = "mineBombsConfig.json";
	public static final String MINE_BOMBS_PATH_NAME = "module_conf/mines";
	
//	public static final String MINE_BOMBS_NBT_BOMB_KEY = "PrisonMineBombNbtKey";
	
	
	private static MineBombs instance;
	
	private MineBombsConfigData configData;
	
	
	
	public enum ExplosionShape {
		
		ring_x,
		ring_y,
		ring_z,

		disk_x,
		disk_y,
		disk_z,
		
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

		public static List<String> asList()
		{
			List<String> results = new ArrayList<>();
			
			for ( ExplosionShape shape : values() )
			{
				results.add( shape.name() );
			}
			
			return results;
		}
	}
	
	public enum ExplosionOrientation {
		x_axis,
		y_axis,
		z_axis,
		
		full
	}
	
	
	public enum AnimationPattern {
		none,
		infinity,
		infinityEight,
		bounce,
		orbital
		
		;
		
		public static final AnimationPattern fromString( String animationPattern ) {
			AnimationPattern results = AnimationPattern.infinity;
			
			if ( animationPattern != null ) {
				
				for (AnimationPattern ap : values() ) {
					if ( ap.name().equalsIgnoreCase( animationPattern) ) {
						results = ap;
						break;
					}
				}
			}
			
			return results;
		}
	}
	
	/**
	 * DO NOT USE!
	 * This has been set to protected only to be used by
	 * junit tests!
	 * DO NOT USE this constructor!
	 */
	protected MineBombs() {
		super();
		
		this.configData = new MineBombsConfigData();
		
		loadConfigJson();
		
		validateMineBombs();
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

	
	@Override
	public int compareTo(MineBombs o) {

		int results = 0;

		if ( o == null ) {
			results = -1;
		}
		
		if ( results == 0 ) {
			
			MineBombsConfigData cData = getConfigData();
			MineBombsConfigData cDataO = o.getConfigData();
			
			results = cData.compareTo( cDataO );
		}
		
		return results;
	}
	
	
//	/**
//	 * <p>This finds a bomb with the given name, and returns a clone.  The clone is
//	 * important since individual instances will set the isActivated() variable to 
//	 * true if the bomb is active.  If it's activated, then that indicates the 
//	 * bomb will be used and the bomb was removed from the player's inventory.
//	 * </p>
//	 * 
//	 * @param bombName
//	 * @return
//	 */
//	public MineBombData findBomb( String bombName ) {
//		MineBombData bombOriginal = null;
//
//		if ( bombName != null ) {
//			
//			// Remove color codes from bomb's name for matching:
//			bombName = Text.stripColor( bombName );
//			
//			bombOriginal = getConfigData().getBombs().get( bombName.toLowerCase() );
//		}
//		
//		return bombOriginal.clone();
//	}
	
	
	/**
	 * <p>This finds a bomb based upon a bombName which may include formatting.
	 * All formatting is removed from the search name, and the bomb names prior to
	 * making any comparisons.  The bomb's key is also checked too for a match.
	 * </p>
	 * 
	 * <p>When a mine bomb is found, it is cloned since some bomb values will 
	 * change during the detonation sequence, so cloning prevents altering the
	 * original source.
	 * </p>
	 * 
	 * @param bombName
	 * @return
	 */
	public MineBombData findBombByName( String bombName )
	{
		MineBombData results = null;
		
		String cleanedBombName = Text.stripColor( bombName.toLowerCase() );
		
		if ( cleanedBombName != null && !cleanedBombName.isEmpty() ) {
			for ( String bombKey : getConfigData().getBombs().keySet() )
			{
				MineBombData bomb = getConfigData().getBombs().get( bombKey );
				
				if ( bomb != null ) {
					String cBombName = Text.stripColor( bomb.getName().toLowerCase() );

					if ( cBombName != null && 
							(cBombName.equalsIgnoreCase( cleanedBombName ) || 
								cleanedBombName.equalsIgnoreCase( bombKey )) ) {
						
						results = bomb;
					}
				}
				
				
			}
		}
		
		return results == null ? null : results.clone();
	}
	
	
	public void saveConfigJson() {
		
		JsonFileIO fio = new JsonFileIO( null, null );
		
		File configFile = getConfigFile( fio );
		
		// Ensure it's set to the correct data version so it won't falsely resave on reload:
		getConfigData().setDataFormatVersion( MineBombsConfigData.MINE_BOMB_DATA_FORMAT_VERSION );
		
		fio.saveJsonFile( configFile, getConfigData() );
		
	}
	
	public String toJson() {
		JsonFileIO fio = new JsonFileIO( null, null );
		
		String json = fio.toString( this );
		
		return json;
	}
	
	public static MineBombs fromJson( String json ) {
		JsonFileIO fio = new JsonFileIO();
		
		MineBombs mBombs = fio.fromString( json, MineBombs.class );

		return mBombs;
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
		
		boolean configExists = configFile.exists();
		
		if ( !configExists ) {
			
			// The save file does not exist so regenerate the default bombs listing:
			getConfigData().getBombs().clear();
			
			MineBombDefaultConfigSettings defaultConfigs = new MineBombDefaultConfigSettings();
			defaultConfigs.setupDefaultMineBombData( this );
			
			
		}
		
		else {
			
			MineBombsConfigData configs = 
					(MineBombsConfigData) fio.readJsonFile( configFile, getConfigData() );

			if ( configs != null ) {
				
				boolean dirty = configs.validateMineBombEffects();
				
				setConfigData( configs );
				
				if ( dirty ||
						configs.getDataFormatVersion() < 
								MineBombsConfigData.MINE_BOMB_DATA_FORMAT_VERSION ) {
					
					// Need to update the format version then save a new copy of the configs.
					mineBombApplyVersionUpdates( configs );
					
					// first backup the old file by renaming it:
					
					int oldVersion = configs.getDataFormatVersion();
					
					String backupTag = "ver_" + oldVersion;
					File backupFile = fio.getBackupFile( configFile, backupTag, "bu" );

					boolean renamed = configFile.renameTo( backupFile );
					
					if ( renamed || dirty ) {
						configs.setDataFormatVersion( MineBombsConfigData.MINE_BOMB_DATA_FORMAT_VERSION );
						
						fio.saveJsonFile( configFile, configs );
						
						Output.get().logInfo( String.format( 
									"MineBomb Data Format was updated and saved: Loaded v%d and updated to v%d. " +
									"The old data is archived as: [%s]",
									oldVersion, configs.getDataFormatVersion(),
									backupFile.getName()
//									backupFile.getAbsolutePath()
								) );
					}
					
					
				}
			}
		}

		
		StringBuilder sbMsg = new StringBuilder();
		int cnt = getConfigData().getBombs().size();
		sbMsg.append( "Prison Mine Bombs: " )
			.append( cnt )
			.append( "[" );
		
		for ( String key : getConfigData().getBombs().keySet() ) {
			MineBombData bomb = getConfigData().getBombs().get( key );
			
			sbMsg.append( " " )
				.append( bomb.getName() );
		}
		 
		sbMsg.append( " ]" );
		
		Output.get().logInfo( sbMsg.toString() );
		
		
	}
	
	/**
	 * <p>This function will process older config data versions and apply updates to the 
	 * various fields to bring it up to date.  For example, if a new field is added, the 
	 * default value would be null, so this is a way to ensure there is a non-null default
	 * value added.  Keep in mind that if the field is null, then it will not be saved in
	 * the json file which means that when the file is edited the person won't know there 
	 * is a new field added.
	 * </p>
	 * 
	 * @param configs
	 */
	private void mineBombApplyVersionUpdates( MineBombsConfigData configs ) {
	
		// If dataFormatVersion is 0, then need to fix the new itemName field so it 
		// is set to the new default value, otherwise it will remain null.
		if ( configs.getDataFormatVersion() == 0 ) {
			
			Set<String> keys = configs.getBombs().keySet();
			for (String key : keys) {
				MineBombData bombData = configs.getBombs().get( key );
				
				// Just getting the itemName will set nulls to the default value:
				bombData.getItemName();
			}
			
		}
	
	}

	
	
	/**
	 * <p>This will apply simple validation to the mine bombs that were just loaded.
	 */
	public void validateMineBombs()
	{
		boolean isDirty = false;
		List<String> errors = new ArrayList<>();
		
		
		MineBombsConfigData config = getConfigData();
		
		if ( config.getDataFormatVersion() > MineBombsConfigData.MINE_BOMB_DATA_FORMAT_VERSION || 
				config.getDataFormatVersion() < 0 ) {
			config.setDataFormatVersion( MineBombsConfigData.MINE_BOMB_DATA_FORMAT_VERSION );

			errors.add( String.format( 
					"Invalid dataFormatVersion was found and it was set to %d.",
					config.getDataFormatVersion()) );
			isDirty = true;
		}
		
		for ( String key : config.getBombs().keySet() )
		{
			MineBombData bomb = config.getBombs().get( key );
			
			// bombItemId is the first line of the lore and should id the bomb:
//			String cleanBombItemId = Text.stripColor( bomb.getBombItemId().replace( " ", "" ));
//			if ( !cleanBombItemId.equalsIgnoreCase( bomb.getBombItemId() ) ) {
//				
//				errors.add( String.format( 
//						"Invalid bombItemId: was: [%s]  fixed: [%s].",
//						bomb.getBombItemId(), 
//						cleanBombItemId ) );
//				bomb.setBombItemId( cleanBombItemId );
//				isDirty = true;
//			}
			
			
			// Bomb names can contain color codes now, but not spaces, since that will 
			// mess up commands related to bombs, since the commands would require the bomb names
			// to not have a space.
			String cleanName = bomb.getName().replace( " ", "_" );
//			String cleanName = Text.stripColor( bomb.getName().replace( " ", "_" ));
			if ( !cleanName.equalsIgnoreCase( bomb.getName() ) ) {
				
				errors.add( String.format( 
						"Invalid bomb name: Cannot contain spaces. The spaces have been replaced with '_'. " +
						"was: [%s]  fixed: [%s].",
						bomb.getName(), 
						cleanName ) );
				bomb.setName( cleanName );
				isDirty = true;
			}
			
			if ( bomb.getItemType() == null || bomb.getItemType().trim().length() == 0 ) {
				
				errors.add( String.format( 
						"Invalid bomb itemType: Cannot be empty." ) );
				
			}
			
			PrisonBlock item = PrisonBlock.parseFromSaveFileFormat( bomb.getItemType() );
			if ( item == null ) {
				
				errors.add( String.format( 
						"Invalid itemType for bomb: %s Cannot be mapped. " +
						"currently: [%s] ",
						bomb.getName(),
						bomb.getItemType()  ) );
//				isDirty = true;
			}
			
			if ( bomb.getItemType().trim().length() == 0 ) {
				errors.add( String.format( 
						"Invalid itemType for bomb: %s  Cannot be empty. Using 'cobblestone'. " +
								"was: [%s] ",
								bomb.getName(),
								bomb.getItemType() ) );
				
				bomb.setItemType( PrisonBlock.fromBlockName( "cobblestone" ).getBlockName() );
				isDirty = true;
			}
			
			if ( bomb.getRadius() < 0 ) {
				errors.add( String.format( 
						"Invalid radius for bomb: %s  Cannot be less than 1. Setting to 1. " +
								"was: [%d] ",
								bomb.getName(),
								bomb.getRadius() ) );
				
				bomb.setRadius( 1 );
				isDirty = true;
			}
			
			if ( bomb.getRadiusInner() < 0 ) {
				errors.add( String.format( 
						"Invalid radiusInner for bomb: %s  Cannot be negative. Setting to 0. " +
								"was: [%d] ",
								bomb.getName(),
								bomb.getRadiusInner() ) );
				
				bomb.setRadiusInner( 0 );
				isDirty = true;
			}
			
			if ( bomb.getHeight() < 0 ) {
				errors.add( String.format( 
						"Invalid height for bomb: %s  Cannot be negative. Setting to 0. " +
								"was: [%d] ",
								bomb.getName(),
								bomb.getHeight() ) );
				
				bomb.setHeight( 0 );
				isDirty = true;
			}
			
			if ( bomb.getRemovalChance() <= 0d || bomb.getRemovalChance() > 100d ) {
				errors.add( String.format( 
						"Invalid removalChance for bomb: %s  Cannot be <= 0 or > 100. " +
								"Setting to 100. " +
								"was: [%d] ",
								bomb.getName(),
								bomb.getRemovalChance() ) );
				
				bomb.setRemovalChance( 100d );
				isDirty = true;
			}
			
			// map all names to lower case
			if ( bomb.getAllowedMines().size() > 0 ) {
				List<String> mines = new ArrayList<>();
				boolean cleaned = false;
				
				for (String mineName : bomb.getAllowedMines() ) {
					String cleanedMineName = mineName.toLowerCase();
					
					if ( !cleanedMineName.equals( mineName ) ) {
						cleaned = true;
					}

					if ( !Prison.get().getPlatform().isMineNameValid(cleanedMineName) ) {
						Output.get().log( "MineBomb %s: invalid mine name for allowedMines: %s  Removed.", 
								LogLevel.WARNING,
								bomb.getName(), cleanedMineName );
						cleaned = true;
					}
					else {
						
						mines.add(cleanedMineName);
					}

				}
				if ( cleaned ) {
					bomb.setAllowedMines(mines);
					isDirty = true;
				}
			}
			
			// map all names to lower case
			if ( bomb.getPreventedMines().size() > 0 ) {
				List<String> mines = new ArrayList<>();
				boolean cleaned = false;
				
				for (String mineName : bomb.getPreventedMines() ) {
					String cleanedMineName = mineName.toLowerCase();
					
					if ( !cleanedMineName.equals( mineName ) ) {
						cleaned = true;
					}

					if ( !Prison.get().getPlatform().isMineNameValid(cleanedMineName) ) {
						Output.get().log( "MineBomb %s: invalid mine name for prevented-Mines: %s  Removed.", 
								LogLevel.WARNING,
								bomb.getName(), cleanedMineName );
						cleaned = true;
					}
					else {
						
						mines.add(cleanedMineName);
					}

				}
				if ( cleaned ) {
					bomb.setPreventedMines(mines);
					isDirty = true;
				}
			}
			
		}
		
		
		if ( isDirty ) {
			// save configs
			
			String message = String.format(
					"&4Prison Mine Bombs Validation Errors Detected: &3Changes were " +
					"made to the structure of the mine bomb configurations and are being " +
					"saved.");
			
			Output.get().logWarn( message );
			
			for ( String error : errors )
			{
				Output.get().logWarn( "  " + error );
			}
			
			saveConfigJson();
		}
	}

	public List<Location> calculateCylinder( Location loc, int radius, boolean hollow ) {
		List<Location> results = new ArrayList<>();
		
		
		
		return results;
	}
	
	public List<Location> calculateSphere( Location loc, int radius, boolean hollow ) {
		
		return calculateSphere( loc, radius, hollow, 0, ExplosionOrientation.full );
	}
	
	public List<Location> calculateSphere( Location loc, int radius, boolean hollow, 
			int radiusInner ) {
		return calculateSphere( loc, radius, hollow, radiusInner, ExplosionOrientation.full );
	}
	
	
	
	public List<Location> calculateSphere( Location loc, int radius, boolean hollow, 
				int radiusInner, ExplosionOrientation explosionOrientation ) {
		List<Location> results = new ArrayList<>();
		
		if ( loc != null && radius > 0 ) {
			int cenX = loc.getBlockX();
			int cenY = loc.getBlockY();
			int cenZ = loc.getBlockZ();
			
			boolean xOri = explosionOrientation == ExplosionOrientation.x_axis;
			boolean yOri = explosionOrientation == ExplosionOrientation.y_axis;
			boolean zOri = explosionOrientation == ExplosionOrientation.z_axis;

			double radiusSqr = radius * radius;
			
			// If the radiusInner is not specified (== 0), then subtract one from radius.
			double radiusHSqr = radiusInner == 0 ?
									((radius - 1) * (radius - 1)) : 
										(radiusInner * radiusInner);
			
			for ( int x = (xOri ? cenX : cenX - radius) ; x <= (xOri ? cenX : cenX + radius) ; x++ ) {
				double xSqr = (cenX - x) * (cenX - x);
				
				for ( int y = (yOri ? cenY : cenY - radius) ; y <= (yOri ? cenY : cenY + radius) ; y++ ) {
					double ySqr = (cenY - y) * (cenY - y);
					
					for ( int z = (zOri ? cenZ : cenZ - radius) ; z <= (zOri ? cenZ : cenZ + radius) ; z++ ) {
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
	
	
	public List<Location> calculateCube( Location loc1, Location loc2 ) {
		List<Location> results = new ArrayList<>();
		
		// get all x, y, & z:
		int xLoc1 = loc1.getBlockX();
		int xLoc2 = loc2.getBlockX();
		int yLoc1 = loc1.getBlockY();
		int yLoc2 = loc2.getBlockY();
		int zLoc1 = loc1.getBlockZ();
		int zLoc2 = loc2.getBlockZ();
		
		// loc1 must be less than loc2... it does not matter if x, y, or z gets
		// switched around:
		if ( xLoc1 > xLoc2 ) {
			int x = xLoc1;
			xLoc1 = xLoc2;
			xLoc2 = x;
		}
		if ( yLoc1 > yLoc2 ) {
			int y = yLoc1;
			yLoc1 = yLoc2;
			yLoc2 = y;
		}
		if ( zLoc1 > zLoc2 ) {
			int z = zLoc1;
			zLoc1 = zLoc2;
			zLoc2 = z;
		}
		
		for ( int x = xLoc1 ; x <= xLoc2 ; x++ ) {
			for ( int y = yLoc1 ; y <= yLoc2; y++ ) {
				for ( int z = zLoc1 ; z <= zLoc2 ; z++ ) {
					
					Location l = new Location( loc1.getWorld(), x, y, z );
					results.add( l );
					
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
