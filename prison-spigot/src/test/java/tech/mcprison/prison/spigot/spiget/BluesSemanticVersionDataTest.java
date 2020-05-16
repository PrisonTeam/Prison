package tech.mcprison.prison.spigot.spiget;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BluesSemanticVersionDataTest
{

	@Test
	public void test()
	{

		// It should be noted that a valid semantic version has at least
		// three segments:
		
		// Test the creation of a SemanticVersioningData object:
		BluesSemanticVersionData svData = new BluesSemanticVersionData("0.1.0");
		assertEquals("0.1.0 (valid) [0.1.0]", svData.toString());
		assertTrue(svData.isValid());
		assertNotNull(svData.getMajor());
		assertNotNull(svData.getMinor());
		assertNotNull(svData.getPatch());
		assertEquals( 0, (long) svData.getMajor() );
		assertEquals( 1, (long) svData.getMinor() );
		assertEquals( 0, (long) svData.getPatch() );
		assertNull( svData.getPrerelease() );
		assertNull( svData.getBuildmetadata() );
		
		svData = new BluesSemanticVersionData("0.1.1");
		assertEquals("0.1.1 (valid) [0.1.1]", svData.toString());
		assertTrue(svData.isValid());
		assertEquals( 0, (long) svData.getMajor() );
		assertEquals( 1, (long) svData.getMinor() );
		assertEquals( 1, (long) svData.getPatch() );
		assertNull( svData.getPrerelease() );
		assertNull( svData.getBuildmetadata() );
		
		svData = new BluesSemanticVersionData("0.1.2");
		assertEquals("0.1.2 (valid) [0.1.2]", svData.toString());
		assertTrue(svData.isValid());
		assertEquals( 0, (long) svData.getMajor() );
		assertEquals( 1, (long) svData.getMinor() );
		assertEquals( 2, (long) svData.getPatch() );
		assertNull( svData.getPrerelease() );
		assertNull( svData.getBuildmetadata() );
		
		svData = new BluesSemanticVersionData("0.1.3-alpha.1");
		assertEquals("0.1.3-alpha.1 (valid) [0.1.3-alpha.1]", svData.toString());
		assertTrue(svData.isValid());
		assertEquals( 0, (long) svData.getMajor() );
		assertEquals( 1, (long) svData.getMinor() );
		assertEquals( 3, (long) svData.getPatch() );
		assertEquals( "alpha.1", svData.getPrerelease() );
		assertNull( svData.getBuildmetadata() );
		
		svData = new BluesSemanticVersionData("3.5.3-alpha.3+build0038273");
		assertEquals("3.5.3-alpha.3+build0038273 (valid) [3.5.3-alpha.3+build0038273]", svData.toString());
		assertTrue(svData.isValid());
		assertEquals( 3, (long) svData.getMajor() );
		assertEquals( 5, (long) svData.getMinor() );
		assertEquals( 3, (long) svData.getPatch() );
		assertEquals( "alpha.3", svData.getPrerelease() );
		assertEquals( "build0038273", svData.getBuildmetadata() );
		
		
		
		// Test for a few invalid semVer types:
		svData = new BluesSemanticVersionData("3");
		assertEquals("<fail>.<fail>.<fail> (invalid) [3]", svData.toString());
		assertFalse(svData.isValid());
		
		svData = new BluesSemanticVersionData("3.2");
		assertEquals("<fail>.<fail>.<fail> (invalid) [3.2]", svData.toString());
		assertFalse(svData.isValid());
		
		svData = new BluesSemanticVersionData("3.1-alpha");
		assertEquals("<fail>.<fail>.<fail> (invalid) [3.1-alpha]", svData.toString());
		assertFalse(svData.isValid());
		
		svData = new BluesSemanticVersionData("v3.2.0");
		assertEquals("<fail>.<fail>.<fail> (invalid) [v3.2.0]", svData.toString());
		assertFalse(svData.isValid());
		
		svData = new BluesSemanticVersionData("03.2.0");
		assertFalse(svData.isValid());
		svData = new BluesSemanticVersionData("v3.2.0.1");
		assertFalse(svData.isValid());
		svData = new BluesSemanticVersionData("3A.2.0");
		assertFalse(svData.isValid());
		svData = new BluesSemanticVersionData("3.A.0");
		assertFalse(svData.isValid());
		svData = new BluesSemanticVersionData("3.2.a");
		assertFalse(svData.isValid());
		

		// Check the compareTo functionality:
		svData = new BluesSemanticVersionData("3.37.5");
		assertTrue(svData.isValid());
		
		BluesSemanticVersionData svDataCheck = new BluesSemanticVersionData("3.37.5");
		assertTrue(svDataCheck.isValid());
		
		// Should be equal:
		assertTrue(svDataCheck.compareTo( svData ) == 0 );

		
		svDataCheck = new BluesSemanticVersionData("3.37.6");
		assertTrue(svDataCheck.isValid());
		
		// svDataCheck should be greater than svData:
		assertTrue(svDataCheck.compareTo( svData ) > 0 );

		// Check that its transitive, in that svData is less than svDataCheck:
		assertTrue(svData.compareTo( svDataCheck ) < 0 );
		
		
		svDataCheck = new BluesSemanticVersionData("3.37.6-beta.1");
		assertTrue(svDataCheck.isValid());
		
		// svDataCheck should be greater than svData, and transitive too:
		assertTrue(svDataCheck.compareTo( svData ) > 0 );
		assertTrue(svData.compareTo( svDataCheck ) < 0 );

		
		
		svData = new BluesSemanticVersionData("3.37.6-beta.1");
		assertTrue(svData.isValid());
		svDataCheck = new BluesSemanticVersionData("3.37.6-beta.2");
		assertTrue(svDataCheck.isValid());
	
		// the beta.2 should be greater than beta.1, they should also be transitive too:
		assertTrue(svDataCheck.compareTo( svData ) > 0 );
		assertTrue(svData.compareTo( svDataCheck ) < 0 );
		
		
		svData = new BluesSemanticVersionData("3.37.6-beta.2");
		assertTrue(svData.isValid());
		svDataCheck = new BluesSemanticVersionData("3.37.6");
		assertTrue(svDataCheck.isValid());

		
		// the version with no prerelease should be greater than beta.2:
		assertTrue(svDataCheck.compareTo( svData ) > 0 );
		assertTrue(svData.compareTo( svDataCheck ) < 0 );
		
		
		svData = new BluesSemanticVersionData("3.37.6");
		assertTrue(svData.isValid());
		svDataCheck = new BluesSemanticVersionData("3.37.6+build20382");
		assertTrue(svDataCheck.isValid());

		// These should be identical since buildmeta should always be ignored:
		assertTrue(svDataCheck.compareTo( svData ) == 0 );
		assertTrue(svData.compareTo( svDataCheck ) == 0 );

		
		// Test the compareTo function:
		assertTrue( new BluesSpigetSemVerComparator().compareTo( "1.8.0", "1.9.0" ) < 0 );
		assertTrue( new BluesSpigetSemVerComparator().compareTo( "1.8.8", "1.9.0" ) < 0 );
		assertFalse( new BluesSpigetSemVerComparator().compareTo( "1.9.0", "1.9.0" ) < 0 );
		assertFalse( new BluesSpigetSemVerComparator().compareTo( "1.15.2", "1.9.0" ) < 0 );
		assertTrue( new BluesSpigetSemVerComparator().compareTo( "1.9.0", "1.9.0" ) == 0 );
		assertTrue( new BluesSpigetSemVerComparator().compareTo( "1.15.2", "1.9.0" ) > 0 );
		
	}

}
