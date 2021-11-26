package tech.mcprison.prison.bombs;

import static org.junit.Assert.*;

import java.util.TreeSet;

import org.junit.Test;

public class MineBombEffectsDataTest
		extends MineBombEffectsData
{

	@Test
	public final void testCompare()
	{
		
		MineBombEffectsData mbef01 = new MineBombEffectsData("ABC", EffectState.explode, 0 );
		MineBombEffectsData mbef02 = new MineBombEffectsData("ABC", EffectState.placed, 0 );

		MineBombEffectsData mbef03 = new MineBombEffectsData("XYZ", EffectState.placed, 3 );

		assertEquals( 1, compare( mbef01, mbef02 ) );
		
		TreeSet<MineBombEffectsData> xParticleEffects = new TreeSet<>( new MineBombEffectsData() );
		
		xParticleEffects.add( mbef02 );
		xParticleEffects.add( mbef03 );
		xParticleEffects.add( mbef01 );
		
		// mbef02 should be sorted "first":
		assertEquals( mbef02, xParticleEffects.first() );
		
		// mbef01 should be sorted "last":
		assertEquals( mbef01, xParticleEffects.last() );
		
	}

}
