package tech.mcprison.prison.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BluesSpigetSemVerComparatorTest
		//extends BluesSpigetSemVerComparator
{

	@Test
	public void test()
	{
		BluesSemanticVersionComparator bsvc = new BluesSemanticVersionComparator();
		
		// Test a few comparisons that should work easily:
		
		// Two valid semVers, but at same version should return a false:
		assertFalse( bsvc.performComparisons( "1.0.1", "1.0.1" ) );

		assertFalse( bsvc.performComparisons( "1.0.2", "1.0.1" ) );

		// Newer version available, should return true:
		assertTrue( bsvc.performComparisons( "1.0.1", "1.0.2" ) );
		
		// Newer version available, should return true:
		assertTrue( bsvc.performComparisons( "1.4.1", "1.5.0" ) );
		
		// Newer version available, should return true:
		assertTrue( bsvc.performComparisons( "1.4.61", "1.5.0" ) );
		
		
		
		
		
		// First test a SINGLE digit number: no decimals:
		
		// Neither are valid semVersion so should return false:
		assertFalse( bsvc.performComparisons( "1", "1" ) );

		// Neither are valid semVersion so should return false:
		assertFalse( bsvc.performComparisons( "0", "1" ) );
		
		// Test for a larger gap:
		assertFalse( bsvc.performComparisons( "1", "7" ) );
		
		// Test for one of them being a double digit:
		assertFalse( bsvc.performComparisons( "1", "17" ) );
		
		// Test for one of them being a double digit, but older version's digit a non-one:
		assertFalse( bsvc.performComparisons( "8", "17" ) );
		
		// These should all be false since they are all equal:
		assertFalse( bsvc.performComparisons( "1.0", "1.0" ) );
		assertFalse( bsvc.performComparisons( "1.0.1", "1.0.1" ) );
		
		// The following is an invalid semVer so it should return a false:
		assertFalse( bsvc.performComparisons( "2.37.902.181.0.2.4", "2.37.902.181.0.2.4" ) );
		assertFalse( bsvc.performComparisons( "2.37.902.181.0.2.4", "2.37.902.181.0.2.5" ) );
		
		
		// This should be false, where current is newer than "new".  
		// This situation would happen if the developer is running a newer version than 
		// what has been published:
		
		// equals... so should be false:
		assertFalse( bsvc.performComparisons( "2.37.902-alpha.181-beta.0+build2.5", 
										 "2.37.902-alpha.181-beta.0+build2.5" ) );

		// buildmeta should be ignored so does not matter. Should be false:
		assertFalse( bsvc.performComparisons( "2.37.902-alpha.181-beta.0+build2.5", 
										 "2.37.902-alpha.181-beta.0+build2.7.34" ) );

		// should be true:
		assertTrue( bsvc.performComparisons( "2.37.902-alpha.181-beta.0+build2.5", 
										"2.37.902-alpha.181-beta.1+build2.7.34" ) );

		// Newer version has invalid semVer, but its corrected to be valid: return true:
		assertTrue( bsvc.performComparisons( "1.2.3", "1.3" ) );
		
		
		// Current version has invalid semVer, but newer has valid. Always true:
		assertTrue( bsvc.performComparisons( "1.2", "1.2.1" ) );
		
		// 1.501 is valid so this test will fail since 1.2.1 is less:
		assertFalse( bsvc.performComparisons( "1.501", "1.2.1" ) );
		assertTrue( bsvc.performComparisons( "9999901", "1.2.1" ) );
		
		
		// Test situations with prerelease tagging:
		
		// First test that these are equal and returns a value of false:
		assertFalse( bsvc.performComparisons( "1.2.3-alpha.1", 
										 "1.2.3-alpha.1" ) );
		
		// Newer has alpha.2 and should return true:
		assertTrue( bsvc.performComparisons( "1.2.3-alpha.1", 
										 "1.2.3-alpha.2" ) );
		// current has alpha.2 and should return false:
		assertFalse( bsvc.performComparisons( "1.2.3-alpha.2", 
										 "1.2.3-alpha.1" ) );
		
		// Current has alpha.1 and newer has full release, should return true:
		assertTrue( bsvc.performComparisons( "1.2.3-alpha.1", 
										 "1.2.3" ) );
		// Newer has alpha.1 and should return false since full release is ranked
		// higher than prerelease:
		assertFalse( bsvc.performComparisons( "1.2.3", 
										 "1.2.3-alpha.1" ) );
		
		
		assertTrue( bsvc.performComparisons( "1.16.5", "1.17" ) );
		assertTrue( bsvc.performComparisons( "1.16.5", "1.17.0" ) );
	}
	
	@Test
	public void test02() {
		BluesSemanticVersionComparator bsvc = new BluesSemanticVersionComparator();
		
		String paperTest = "Minecraft version: git-Paper-21 (MC: 1.15)";
		
		assertEquals( "1.15", bsvc.getBukkitVersion( paperTest ) );
		
	}
}
