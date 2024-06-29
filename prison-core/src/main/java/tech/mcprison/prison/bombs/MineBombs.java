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
{
	public static final String MINE_BOMBS_FILE_NAME = "mineBombsConfig.json";
	public static final String MINE_BOMBS_PATH_NAME = "module_conf/mines";
	
	public static final String MINE_BOMBS_NBT_BOMB_KEY = "PrisonMineBombNbtKey";
	
	
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
		bounce,
		orbital
		
		;
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
		
		// Ensure it's set to the correct data version so it won't falsely resave on reload:
		getConfigData().setDataFormatVersion( MineBombsConfigData.MINE_BOMB_DATA_FORMAT_VERSION );
		
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
		
		boolean configExists = configFile.exists();
		
		if ( !configExists ) {
			MineBombDefaultConfigSettings defaultConfigs = new MineBombDefaultConfigSettings();
			defaultConfigs.setupDefaultMineBombData( this );
			
		}
		
		else {
			
			MineBombsConfigData configs = 
					(MineBombsConfigData) fio.readJsonFile( configFile, getConfigData() );

			if ( configs != null ) {
				
				configs.validateMineBombEffects();
				
				setConfigData( configs );
				
				if ( configs.getDataFormatVersion() < 
								MineBombsConfigData.MINE_BOMB_DATA_FORMAT_VERSION ) {
					
					// Need to update the format version then save a new copy of the configs.
					mineBombApplyVersionUpdates( configs );
					
					// first backup the old file by renaming it:
					
					int oldVersion = configs.getDataFormatVersion();
					
					String backupTag = "ver_" + oldVersion;
					File backupFile = fio.getBackupFile( configFile, backupTag, "bu" );

					boolean renamed = configFile.renameTo( backupFile );
					
					if ( renamed ) {
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
	
	
	
//	
//	@SuppressWarnings( "unused" )
//	public void setupDefaultMineBombData()
//	{
//		if ( getConfigData().getBombs().size() == 0 ) {
//
////			XMaterial.WOODEN_PICKAXE;
////			XMaterial.STONE_PICKAXE;
////			XMaterial.IRON_PICKAXE;
////			XMaterial.GOLDEN_PICKAXE;
////			XMaterial.DIAMOND_PICKAXE;
////			XMaterial.NETHERITE_PICKAXE;
//
//			MineBombEffectsData mbeSound01 = new MineBombEffectsData( "ENTITY_CREEPER_PRIMED", EffectState.placed, 0 );
//			MineBombEffectsData mbeSound02 = new MineBombEffectsData( "CAT_HISS", EffectState.placed, 0 );
//			
//			MineBombEffectsData mbeSound03 = new MineBombEffectsData( "ENTITY_GENERIC_EXPLODE", EffectState.explode, 0 );
//			MineBombEffectsData mbeSound04 = new MineBombEffectsData( "ENTITY_DRAGON_FIREBALL_EXPLODE", EffectState.explode, 0 );
//			
//			// Does not work with spigot 1.8.x:
//			MineBombEffectsData mbeExplode01 = new MineBombEffectsData( "FIREWORKS_SPARK", EffectState.placed, 0 );
//			// Does not work with spigot 1.8.x:
//			MineBombEffectsData mbeExplode02 = new MineBombEffectsData( "BUBBLE_COLUMN_UP", EffectState.placed, 0 );
//			MineBombEffectsData mbeExplode03 = new MineBombEffectsData( "ENCHANTMENT_TABLE", EffectState.placed, 0 );
//			
////			MineBombEffectsData mbeExplode05 = new MineBombEffectsData( "END_ROD", EffectState.placed, 0 );
//			MineBombEffectsData mbeExplode04 = new MineBombEffectsData( "FLAME", EffectState.placed, 0 );
//			// Does not work with spigot 1.8.x:
//			MineBombEffectsData mbeExplode08 = new MineBombEffectsData( "DRAGON_BREATH", EffectState.placed, 0 );
//
//			MineBombEffectsData mbeExplode06a = new MineBombEffectsData( "SMOKE", EffectState.placed, 0 );
//			// Does not work with spigot 1.8.x:
//			MineBombEffectsData mbeExplode06 = new MineBombEffectsData( "SMOKE_NORMAL", EffectState.placed, 0 );
//			// Does not work with spigot 1.8.x:
//			MineBombEffectsData mbeExplode07 = new MineBombEffectsData( "SMOKE_LARGE", EffectState.placed, 0 );
//			
//			MineBombEffectsData mbeExplode10 = new MineBombEffectsData( "EXPLOSION_NORMAL", EffectState.explode, 0 );
//			MineBombEffectsData mbeExplode11 = new MineBombEffectsData( "EXPLOSION_LARGE", EffectState.explode, 0 );
//			// Does not work with spigot 1.8.x:
//			MineBombEffectsData mbeExplode12 = new MineBombEffectsData( "EXPLOSION_HUGE", EffectState.explode, 0 );
//			
//			
//			{
//				MineBombData mbd = new MineBombData( 
//						"SmallBomb", "brewing_stand", ExplosionShape.sphere.name(), 2, "&dSmall &6Mine &eBomb &3(lore line 1)" );
//				
//				mbd.setNameTag( "&6&kABC&r&c-= &7{name}&c =-&6&kCBA" );
//				
//				mbd.setToolInHandName( "DIAMOND_PICKAXE" );
//				mbd.setToolInHandFortuneLevel( 0 );
//				mbd.setDescription("A small mine bomb made with some chemicals and a brewing stand.");
//				
//				mbd.getLore().add( "&4Lore line 2" );
//				mbd.getLore().add( "&aLore line &73" );
//
//				mbd.getSoundEffects().add( mbeSound01.clone() );
//				mbd.getSoundEffects().add( mbeSound02.clone().setOffsetTicks( 30 ) );
//				mbd.getSoundEffects().add( mbeSound03.clone() );
//				
//				mbd.getVisualEffects().add( mbeExplode04.clone() );
//				mbd.getVisualEffects().add( mbeExplode03.clone().setOffsetTicks( 30 ) );
//				
//				mbd.getVisualEffects().add( mbeExplode06a.clone() );
//				mbd.getVisualEffects().add( mbeExplode10.clone() );
//				mbd.getVisualEffects().add( mbeExplode06.clone() );
//				
//				mbd.setCooldownTicks( 10 );
//
//				getConfigData().getBombs().put( mbd.getName().toLowerCase(), mbd );
//				
//			}
//			
//			{
//				MineBombData mbd = new MineBombData( 
//						"MediumBomb", "firework_rocket", ExplosionShape.sphere.name(), 5, "Medium Mine Bomb" );
//				mbd.setDescription("A medium mine bomb made from leftover fireworks, " +
//						"but supercharged with a strange green glowing liquid.");
//				
//				mbd.setNameTag( "&6&k1 23 456&r&a-=- &7{name}&a -=-&6&k654 32 1" );
//
//				mbd.setToolInHandName( "DIAMOND_PICKAXE" );
//				mbd.setToolInHandFortuneLevel( 3 );
//				
//				mbd.getSoundEffects().add( mbeSound01.clone() );
//				mbd.getSoundEffects().add( mbeSound02.clone().setOffsetTicks( 30 ) );
//				mbd.getSoundEffects().add( mbeSound03.clone() );
//				
//				mbd.getVisualEffects().add( mbeExplode04.clone() );
//				mbd.getVisualEffects().add( mbeExplode03.clone().setOffsetTicks( 30 ) );
//				
//				mbd.getVisualEffects().add( mbeExplode10.clone() );
//				mbd.getVisualEffects().add( mbeExplode06.clone() );
//
//				mbd.setCooldownTicks( 60 );
//				
//				getConfigData().getBombs().put( mbd.getName().toLowerCase(), mbd );
//				
//			}
//
//			{
//				MineBombData mbd = new MineBombData( 
//						"LargeBomb", "tnt", ExplosionShape.sphereHollow.name(), 12, "Large Mine Bomb" );
//				mbd.setRadiusInner( 3 );
//				mbd.setDescription("A large mine bomb made from TNT with some strange parts " +
//						"that maybe be described as alien technology.");
//				mbd.setToolInHandName( "DIAMOND_PICKAXE" );
//				mbd.setToolInHandFortuneLevel( 3 );
//				
//				mbd.getSoundEffects().add( mbeSound01.clone() );
//				mbd.getSoundEffects().add( mbeSound02.clone().setOffsetTicks( 30 ) );
//				mbd.getSoundEffects().add( mbeSound03.clone().setVolumne( 2.0f ) );
//				
//				mbd.getVisualEffects().add( mbeExplode04.clone() );
//				mbd.getVisualEffects().add( mbeExplode03.clone().setOffsetTicks( 30 ) );
//				
//				mbd.getVisualEffects().add( mbeExplode10.clone() );
//				mbd.getVisualEffects().add( mbeExplode06.clone() );
//				mbd.getVisualEffects().add( mbeExplode06a.clone() );
//
//				mbd.setCooldownTicks( 60 );
//				
//				getConfigData().getBombs().put( mbd.getName().toLowerCase(), mbd );
//			}
//			
//			{
//				MineBombData mbd = new MineBombData( 
//						"OofBomb", "tnt_minecart", ExplosionShape.sphereHollow.name(), 21, "Oof Mine Bomb" );
//				mbd.setRadiusInner( 3 );
//				
//				mbd.setNameTag( "&c&k1&6&k23&e&k456&r&a-=- &4{countdown} &5-=- &7{name} &5-=- &4{countdown} &a-=-&e&k654&6&k32&c&k1" );
//				
//				mbd.setDescription("An oof-ably large mine bomb made with a minecart heaping with TNT.  " +
//						"Unlike the large mine bomb, this one obviously is built with alien technology.");
//				
//				mbd.setToolInHandName( "GOLDEN_PICKAXE" );
//				mbd.setToolInHandFortuneLevel( 13 );
//				
//				mbd.getSoundEffects().add( mbeSound01.clone() );
//				mbd.getSoundEffects().add( mbeSound02.clone().setOffsetTicks( 10 ).setVolumne( 0.25f ).setPitch( 0.25f ) );
//				mbd.getSoundEffects().add( mbeSound02.clone().setOffsetTicks( 20 ).setVolumne( 0.5f ).setPitch( 0.5f ) );
//				mbd.getSoundEffects().add( mbeSound02.clone().setOffsetTicks( 30 ).setVolumne( 1.0f ).setPitch( 0.75f ) );
//				mbd.getSoundEffects().add( mbeSound02.clone().setOffsetTicks( 40 ).setVolumne( 2.0f ).setPitch( 1.5f ) );
//				mbd.getSoundEffects().add( mbeSound02.clone().setOffsetTicks( 50 ).setVolumne( 5.0f ).setPitch( 2.5f ) );
//				
//				mbd.getSoundEffects().add( mbeSound03.clone().setOffsetTicks( 0 ).setVolumne( 3.0f ) );
//				mbd.getSoundEffects().add( mbeSound04.clone().setOffsetTicks( 5 ).setVolumne( 1.5f ) );
//				mbd.getSoundEffects().add( mbeSound03.clone().setOffsetTicks( 10 ).setVolumne( 2.5f ) );
//				mbd.getSoundEffects().add( mbeSound04.clone().setOffsetTicks( 15 ).setVolumne( 1.0f ) );
//				mbd.getSoundEffects().add( mbeSound03.clone().setOffsetTicks( 20 ).setVolumne( 2.0f ) );
//				mbd.getSoundEffects().add( mbeSound04.clone().setOffsetTicks( 25 ).setVolumne( 0.75f ) );
//				mbd.getSoundEffects().add( mbeSound03.clone().setOffsetTicks( 30 ).setVolumne( 1.5f ) );
//				mbd.getSoundEffects().add( mbeSound04.clone().setOffsetTicks( 35 ).setVolumne( 0.55f ) );
//				mbd.getSoundEffects().add( mbeSound03.clone().setOffsetTicks( 40 ).setVolumne( 1.0f ) );
//				mbd.getSoundEffects().add( mbeSound04.clone().setOffsetTicks( 45 ).setVolumne( 0.25f ) );
//				mbd.getSoundEffects().add( mbeSound03.clone().setOffsetTicks( 50 ).setVolumne( 0.5f ) );
//				mbd.getSoundEffects().add( mbeSound04.clone().setOffsetTicks( 55 ).setVolumne( 0.15f ) );
//				
//				
//				mbd.getVisualEffects().add( mbeExplode06.clone() );
//				mbd.getVisualEffects().add( mbeExplode06a.clone() );
//				mbd.getVisualEffects().add( mbeExplode03.clone() );
//				mbd.getVisualEffects().add( mbeExplode12.clone() );
//				mbd.getVisualEffects().add( mbeExplode12.clone().setOffsetTicks( 30 ) );
//				mbd.getVisualEffects().add( mbeExplode12.clone().setOffsetTicks( 60 ) );
//				mbd.getVisualEffects().add( mbeExplode07.clone().setOffsetTicks( 60 ) );
//				mbd.getVisualEffects().add( mbeExplode08.clone().setOffsetTicks( 90 ) );
//				
//				mbd.getVisualEffects().add( mbeExplode10.clone() );
//				mbd.getVisualEffects().add( mbeExplode06.clone().setOffsetTicks( 20 ) );
//				
//				mbd.setAutosell( true );
//				mbd.setGlowing( true );
//				mbd.setAutosell( true );
//				
//				mbd.setCooldownTicks( 60 );
//				mbd.setFuseDelayTicks( 13 * 20 ); // 13 seconds
//
//				getConfigData().getBombs().put( mbd.getName().toLowerCase(), mbd );
//			}
//			
//			{
//				MineBombData mbd = new MineBombData( 
//						"WimpyBomb", "GUNPOWDER", ExplosionShape.sphere.name(), 5, 
//							"A Wimpy Mine Bomb" );
////				mbd.setLoreBombItemId( "&7A &2Wimpy &cBomb &9...&02A3F" );
//				
//				mbd.setNameTag( "&7A &2Wimpy &cBomb" );
//				
//				mbd.setRadiusInner( 2 );
//				mbd.setDescription("A whimpy bomb made with gunpowder and packs the punch of a " +
//						"dull wooden pickaxe. For some reason, it only has a 40% chance of removing " +
//						"a block.");
//				
//				mbd.getLore().add( "" );
//				mbd.getLore().add( "A whimpy bomb made with gunpowder and packs the punch " );
//				mbd.getLore().add( "of a dull wooden pickaxe. For some reason, it only " );
//				mbd.getLore().add( "has a 40% chance of removing a block." );
//				mbd.getLore().add( "" );
//				mbd.getLore().add( "Not labeled for retail sale." );
//				
//				mbd.setToolInHandName( "WOODEN_PICKAXE" );
//				mbd.setToolInHandFortuneLevel( 0 );
//				mbd.setRemovalChance( 40.0d );
//				
//				mbd.getSoundEffects().add( mbeSound01.clone() );
//				mbd.getSoundEffects().add( mbeSound02.clone().setOffsetTicks( 30 ) );
//				mbd.getSoundEffects().add( mbeSound03.clone() );
//				
//				mbd.getVisualEffects().add( mbeExplode01.clone() );
//				mbd.getVisualEffects().add( mbeExplode02.clone().setOffsetTicks( 30 ) );
//				mbd.getVisualEffects().add( mbeExplode03.clone().setOffsetTicks( 10 ) );
//				mbd.getVisualEffects().add( mbeExplode04.clone() );
//				
//				mbd.getVisualEffects().add( mbeExplode10.clone() );
//				mbd.getVisualEffects().add( mbeExplode06.clone() );
//				mbd.getVisualEffects().add( mbeExplode06a.clone() );
//				mbd.getVisualEffects().add( mbeExplode11.clone().setOffsetTicks( 05 ) );
//				
//				mbd.setCooldownTicks( 3 * 20 ); // 3 seconds
//				mbd.setFuseDelayTicks( 2 * 20 ); // 2 seconds
//
//				mbd.setGlowing( true );
//				mbd.setGravity( false );
//				
//				mbd.setCooldownTicks( 5 );
//
//				getConfigData().getBombs().put( mbd.getName().toLowerCase(), mbd );
//			}
//			
//			
//			{
//				MineBombData mbd = new MineBombData( 
//						"CubeBomb", "SLIME_BLOCK", ExplosionShape.cube.name(), 2, 
//						"A Cubic Bomb" );
//				mbd.setDescription("The most anti-round bomb you will ever be able to find. " +
//						"It's totally cubic.");
//				
//				mbd.setToolInHandName( "DIAMOND_PICKAXE" );
//				mbd.setToolInHandFortuneLevel( 7 );
//				mbd.setRemovalChance( 100.0d );
//				
//				mbd.getSoundEffects().add( mbeSound01.clone() );
//				mbd.getSoundEffects().add( mbeSound02.clone().setOffsetTicks( 30 ) );
//				mbd.getSoundEffects().add( mbeSound03.clone() );
//				
//				mbd.getVisualEffects().add( mbeExplode04.clone() );
//				mbd.getVisualEffects().add( mbeExplode02.clone().setOffsetTicks( 30 ) );
//				
//				mbd.setGlowing( true );
//				
//				mbd.setCooldownTicks( 60 );
//
//				getConfigData().getBombs().put( mbd.getName().toLowerCase(), mbd );
//			}
//			
//			
//			saveConfigJson();
//			
//			Output.get().logInfo( "Mine bombs: setup default values." );
//		}
//		else {
//			Output.get().logInfo( "Could not generate a mine bombs save file since at least one " +
//						"mine bomb already exists." );
//		}
//		
//	}

	
	public MineBombsConfigData getConfigData() {
		return configData;
	}
	public void setConfigData( MineBombsConfigData configData ) {
		this.configData = configData;
	}

	
	public MineBombData findBombByName( String bombName )
	{
		MineBombData results = null;
		
		String cleanedBombName = Text.stripColor( bombName );
		
		if ( cleanedBombName != null && !cleanedBombName.isEmpty() ) {
			for ( String bombKey : getConfigData().getBombs().keySet() )
			{
				MineBombData bomb = getConfigData().getBombs().get( bombKey );
				
				if ( bomb != null ) {
					String cBombName = Text.stripColor( bomb.getName() );

					if ( cBombName != null && 
							cBombName.equalsIgnoreCase( cleanedBombName ) ) {
						
						results = bomb;
					}
				}
				
				
			}
		}
		
		return results;
	}

}
