package tech.mcprison.prison.bombs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.bombs.MineBombEffectsData.EffectState;
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
	
	
	@SuppressWarnings( "unused" )
	public void setupDefaultMineBombData()
	{
		if ( getConfigData().getBombs().size() == 0 ) {

//			XMaterial.WOODEN_PICKAXE;
//			XMaterial.STONE_PICKAXE;
//			XMaterial.IRON_PICKAXE;
//			XMaterial.GOLDEN_PICKAXE;
//			XMaterial.DIAMOND_PICKAXE;
//			XMaterial.NETHERITE_PICKAXE;

			MineBombEffectsData mbeSound01 = new MineBombEffectsData( "ENTITY_CREEPER_PRIMED", EffectState.placed, 0 );
			MineBombEffectsData mbeSound02 = new MineBombEffectsData( "CAT_HISS", EffectState.placed, 0 );
			
			MineBombEffectsData mbeSound03 = new MineBombEffectsData( "ENTITY_GENERIC_EXPLODE", EffectState.explode, 0 );
			MineBombEffectsData mbeSound04 = new MineBombEffectsData( "ENTITY_DRAGON_FIREBALL_EXPLODE", EffectState.explode, 0 );
			
			// Does not work with spigot 1.8.x:
			MineBombEffectsData mbeExplode01 = new MineBombEffectsData( "FIREWORKS_SPARK", EffectState.placed, 0 );
			// Does not work with spigot 1.8.x:
			MineBombEffectsData mbeExplode02 = new MineBombEffectsData( "BUBBLE_COLUMN_UP", EffectState.placed, 0 );
			MineBombEffectsData mbeExplode03 = new MineBombEffectsData( "ENCHANTMENT_TABLE", EffectState.placed, 0 );
			
//			MineBombEffectsData mbeExplode05 = new MineBombEffectsData( "END_ROD", EffectState.placed, 0 );
			MineBombEffectsData mbeExplode04 = new MineBombEffectsData( "FLAME", EffectState.placed, 0 );
			// Does not work with spigot 1.8.x:
			MineBombEffectsData mbeExplode08 = new MineBombEffectsData( "DRAGON_BREATH", EffectState.placed, 0 );

			MineBombEffectsData mbeExplode06a = new MineBombEffectsData( "SMOKE", EffectState.placed, 0 );
			// Does not work with spigot 1.8.x:
			MineBombEffectsData mbeExplode06 = new MineBombEffectsData( "SMOKE_NORMAL", EffectState.placed, 0 );
			// Does not work with spigot 1.8.x:
			MineBombEffectsData mbeExplode07 = new MineBombEffectsData( "SMOKE_LARGE", EffectState.placed, 0 );
			
			MineBombEffectsData mbeExplode10 = new MineBombEffectsData( "EXPLOSION_NORMAL", EffectState.explode, 0 );
			MineBombEffectsData mbeExplode11 = new MineBombEffectsData( "EXPLOSION_LARGE", EffectState.explode, 0 );
			// Does not work with spigot 1.8.x:
			MineBombEffectsData mbeExplode12 = new MineBombEffectsData( "EXPLOSION_HUGE", EffectState.explode, 0 );
			
			
			{
				MineBombData mbd = new MineBombData( 
						"SmallBomb", "brewing_stand", ExplosionShape.sphere.name(), 2, "Small Mine Bomb" );
				mbd.setToolInHandName( "DIAMOND_PICKAXE" );
				mbd.setToolInHandFortuneLevel( 0 );
				mbd.setDescription("A small mine bomb made with some chemicals and a brewing stand.");
				
				mbd.getSoundEffects().add( mbeSound01.clone() );
				mbd.getSoundEffects().add( mbeSound02.clone().setOffsetTicks( 30 ) );
				mbd.getSoundEffects().add( mbeSound03.clone() );
				
				mbd.getVisualEffects().add( mbeExplode04.clone() );
				mbd.getVisualEffects().add( mbeExplode03.clone().setOffsetTicks( 30 ) );
				
				mbd.getVisualEffects().add( mbeExplode06a.clone() );
				mbd.getVisualEffects().add( mbeExplode10.clone() );
				mbd.getVisualEffects().add( mbeExplode06.clone() );
				
				getConfigData().getBombs().put( mbd.getName().toLowerCase(), mbd );
				
			}
			
			{
				MineBombData mbd = new MineBombData( 
						"MediumBomb", "firework_rocket", ExplosionShape.sphere.name(), 5, "Medium Mine Bomb" );
				mbd.setDescription("A medium mine bomb made from leftover fireworks, " +
						"but supercharged with a strange green glowing liquid.");
				mbd.setToolInHandName( "DIAMOND_PICKAXE" );
				mbd.setToolInHandFortuneLevel( 3 );
				
				mbd.getSoundEffects().add( mbeSound01.clone() );
				mbd.getSoundEffects().add( mbeSound02.clone().setOffsetTicks( 30 ) );
				mbd.getSoundEffects().add( mbeSound03.clone() );
				
				mbd.getVisualEffects().add( mbeExplode04.clone() );
				mbd.getVisualEffects().add( mbeExplode03.clone().setOffsetTicks( 30 ) );
				
				mbd.getVisualEffects().add( mbeExplode10.clone() );
				mbd.getVisualEffects().add( mbeExplode06.clone() );

				mbd.setCooldownTicks( 60 );
				
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
				
				mbd.getSoundEffects().add( mbeSound01.clone() );
				mbd.getSoundEffects().add( mbeSound02.clone().setOffsetTicks( 30 ) );
				mbd.getSoundEffects().add( mbeSound03.clone().setVolumne( 2.0f ) );
				
				mbd.getVisualEffects().add( mbeExplode04.clone() );
				mbd.getVisualEffects().add( mbeExplode03.clone().setOffsetTicks( 30 ) );
				
				mbd.getVisualEffects().add( mbeExplode10.clone() );
				mbd.getVisualEffects().add( mbeExplode06.clone() );
				mbd.getVisualEffects().add( mbeExplode06a.clone() );

				mbd.setCooldownTicks( 60 );
				
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
				
				mbd.getSoundEffects().add( mbeSound01.clone() );
				mbd.getSoundEffects().add( mbeSound02.clone().setOffsetTicks( 10 ).setVolumne( 0.25f ).setPitch( 0.25f ) );
				mbd.getSoundEffects().add( mbeSound02.clone().setOffsetTicks( 20 ).setVolumne( 0.5f ).setPitch( 0.5f ) );
				mbd.getSoundEffects().add( mbeSound02.clone().setOffsetTicks( 30 ).setVolumne( 1.0f ).setPitch( 0.75f ) );
				mbd.getSoundEffects().add( mbeSound02.clone().setOffsetTicks( 40 ).setVolumne( 2.0f ).setPitch( 1.5f ) );
				mbd.getSoundEffects().add( mbeSound02.clone().setOffsetTicks( 50 ).setVolumne( 5.0f ).setPitch( 2.5f ) );
				
				mbd.getSoundEffects().add( mbeSound03.clone().setOffsetTicks( 0 ).setVolumne( 3.0f ) );
				mbd.getSoundEffects().add( mbeSound04.clone().setOffsetTicks( 5 ).setVolumne( 1.5f ) );
				mbd.getSoundEffects().add( mbeSound03.clone().setOffsetTicks( 10 ).setVolumne( 2.5f ) );
				mbd.getSoundEffects().add( mbeSound04.clone().setOffsetTicks( 15 ).setVolumne( 1.0f ) );
				mbd.getSoundEffects().add( mbeSound03.clone().setOffsetTicks( 20 ).setVolumne( 2.0f ) );
				mbd.getSoundEffects().add( mbeSound04.clone().setOffsetTicks( 25 ).setVolumne( 0.75f ) );
				mbd.getSoundEffects().add( mbeSound03.clone().setOffsetTicks( 30 ).setVolumne( 1.5f ) );
				mbd.getSoundEffects().add( mbeSound04.clone().setOffsetTicks( 35 ).setVolumne( 0.55f ) );
				mbd.getSoundEffects().add( mbeSound03.clone().setOffsetTicks( 40 ).setVolumne( 1.0f ) );
				mbd.getSoundEffects().add( mbeSound04.clone().setOffsetTicks( 45 ).setVolumne( 0.25f ) );
				mbd.getSoundEffects().add( mbeSound03.clone().setOffsetTicks( 50 ).setVolumne( 0.5f ) );
				mbd.getSoundEffects().add( mbeSound04.clone().setOffsetTicks( 55 ).setVolumne( 0.15f ) );
				
				
				mbd.getVisualEffects().add( mbeExplode06.clone() );
				mbd.getVisualEffects().add( mbeExplode06a.clone() );
				mbd.getVisualEffects().add( mbeExplode03.clone() );
				mbd.getVisualEffects().add( mbeExplode12.clone() );
				mbd.getVisualEffects().add( mbeExplode12.clone().setOffsetTicks( 30 ) );
				mbd.getVisualEffects().add( mbeExplode12.clone().setOffsetTicks( 60 ) );
				mbd.getVisualEffects().add( mbeExplode07.clone().setOffsetTicks( 60 ) );
				mbd.getVisualEffects().add( mbeExplode08.clone().setOffsetTicks( 90 ) );
				
				mbd.getVisualEffects().add( mbeExplode10.clone() );
				mbd.getVisualEffects().add( mbeExplode06.clone().setOffsetTicks( 20 ) );
				
				mbd.setAutosell( true );
				mbd.setGlowing( true );
				mbd.setAutosell( true );
				
				mbd.setCooldownTicks( 60 );
				mbd.setFuseDelayTicks( 3 * 20 ); // 3 seconds

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
				
				mbd.getSoundEffects().add( mbeSound01.clone() );
				mbd.getSoundEffects().add( mbeSound02.clone().setOffsetTicks( 30 ) );
				mbd.getSoundEffects().add( mbeSound03.clone() );
				
				mbd.getVisualEffects().add( mbeExplode01.clone() );
				mbd.getVisualEffects().add( mbeExplode02.clone().setOffsetTicks( 30 ) );
				mbd.getVisualEffects().add( mbeExplode03.clone().setOffsetTicks( 10 ) );
				mbd.getVisualEffects().add( mbeExplode04.clone() );
				
				mbd.getVisualEffects().add( mbeExplode10.clone() );
				mbd.getVisualEffects().add( mbeExplode06.clone() );
				mbd.getVisualEffects().add( mbeExplode06a.clone() );
				mbd.getVisualEffects().add( mbeExplode11.clone().setOffsetTicks( 05 ) );
				
				mbd.setCooldownTicks( 3 * 20 ); // 3 seconds
				mbd.setFuseDelayTicks( 2 * 20 ); // 2 seconds

				mbd.setGlowing( true );
				mbd.setGravity( false );
				
				mbd.setCooldownTicks( 60 );

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
				
				mbd.getSoundEffects().add( mbeSound01.clone() );
				mbd.getSoundEffects().add( mbeSound02.clone().setOffsetTicks( 30 ) );
				mbd.getSoundEffects().add( mbeSound03.clone() );
				
				mbd.getVisualEffects().add( mbeExplode04.clone() );
				mbd.getVisualEffects().add( mbeExplode02.clone().setOffsetTicks( 30 ) );
				
				mbd.setGlowing( true );
				
				mbd.setCooldownTicks( 60 );

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
