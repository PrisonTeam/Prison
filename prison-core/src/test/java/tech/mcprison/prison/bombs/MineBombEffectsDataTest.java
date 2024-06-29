package tech.mcprison.prison.bombs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.TestPlatform;
import tech.mcprison.prison.internal.platform.Platform;

public class MineBombEffectsDataTest
		extends MineBombEffectsData
{

	@Test
	public final void testCompare()
	{
		
		MineBombEffectsData mbef01 = new MineBombEffectsData("ABC", EffectState.explode, 0 );
		mbef01.setEffectType( EffectType.sounds );
		MineBombEffectsData mbef02 = new MineBombEffectsData("XYZ", EffectState.placed, 3 );
		mbef02.setEffectType( EffectType.sounds );

		MineBombEffectsData mbef03 = new MineBombEffectsData("BDF", EffectState.finished, 14 );
		mbef03.setEffectType( EffectType.sounds );
		MineBombEffectsData mbef04 = new MineBombEffectsData("DEF", EffectState.finished, 0 );
		mbef04.setEffectType( EffectType.sounds );

		MineBombEffectsData mbef05 = new MineBombEffectsData("ABC", EffectState.placed, 0 );
		mbef05.setEffectType( EffectType.sounds );

		
		assertEquals( 1, compare( mbef01, mbef02 ) );
		
		TreeSet<MineBombEffectsData> xParticleEffects = new TreeSet<>( new MineBombEffectsData() );
		
		xParticleEffects.add( mbef01 );
		xParticleEffects.add( mbef02 );
		xParticleEffects.add( mbef03 );
		xParticleEffects.add( mbef04 );
		xParticleEffects.add( mbef05 );
		
		List<MineBombEffectsData> testList = new ArrayList<>( xParticleEffects );
		
		// mbef05 should be sorted "first":
		assertEquals( mbef05, xParticleEffects.first() );
		
		// mbef03 should be sorted "last":
		assertEquals( mbef03, xParticleEffects.last() );
		
		
		assertEquals( 5, testList.size() );
		
		assertEquals( mbef05, testList.get( 0 ) );
		assertEquals( mbef02, testList.get( 1 ) );
		assertEquals( mbef01, testList.get( 2 ) );
		assertEquals( mbef04, testList.get( 3 ) );
		assertEquals( mbef03, testList.get( 4 ) );
		
	}
	
	@Test
	public final void testJson()
	{
		
		Platform platform = new TestPlatform( new File("tempDir"), true );
		Prison.get().setPlatform( platform );
		
		
		MineBombs mBombs = new MineBombs();
		
		// First build the default bombs:
		MineBombDefaultConfigSettings defConfigs = new MineBombDefaultConfigSettings();
		defConfigs.setupDefaultMineBombData(mBombs);

		String json = mBombs.toJson();
		
		MineBombs mBombsTwo = MineBombs.fromJson(json);
		
		assertNotNull( mBombs );
		assertNotNull( mBombsTwo );

		assertEquals( 0, mBombs.compareTo(mBombsTwo ));
		
//		assertEquals( mBombs, mBombsTwo );
	}
	
	
	@Test
	public final void testClone()
	{
		
		Platform platform = new TestPlatform( new File("tempDir"), true );
		Prison.get().setPlatform( platform );
		
		
		MineBombs mBombs = new MineBombs();
		
		// First build the default bombs:
		MineBombDefaultConfigSettings defConfigs = new MineBombDefaultConfigSettings();
		defConfigs.setupDefaultMineBombData(mBombs);
		
		MineBombs mBombsTwo = new MineBombs();
		
		Set<String> keys = mBombs.getConfigData().getBombs().keySet();
		for (String key : keys) {
			MineBombData mBomb = mBombs.getConfigData().getBombs().get(key);
			
			mBombsTwo.getConfigData().getBombs().put(
					key, mBomb.clone() );
			
		}
		
		assertNotNull( mBombs );
		assertNotNull( mBombsTwo );
		
		assertEquals( 0, mBombs.compareTo(mBombsTwo ));
		
//		assertEquals( mBombs, mBombsTwo );
	}
	

}
