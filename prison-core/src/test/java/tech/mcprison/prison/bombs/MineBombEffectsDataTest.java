package tech.mcprison.prison.bombs;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.junit.Test;

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

}
