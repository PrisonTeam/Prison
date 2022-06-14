package tech.mcprison.prison.placeholders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.TestPlatform;

public class PlaceholderIdentifierTest {

	public PlaceholderIdentifierTest() {
		super();
	}

	/**
	 * <p>Test to confirm the PlaceholderIdentifier.getIdentifier() is correct.
	 * </p>
	 */
	@Test
	public void testPlaceholders() {
		
		PlaceholderIdentifier t1 = new PlaceholderIdentifier(null);
		
		assertNotNull( t1.getIdentifier() );
		assertEquals( "", t1.getIdentifier() );
		assertEquals( "", t1.getOriginalIdentifier() );
		
		PlaceholderIdentifier t2 = new PlaceholderIdentifier( "" );
		
		assertNotNull( t2.getIdentifier() );
		assertEquals( "", t2.getIdentifier() );
		assertEquals( "", t2.getOriginalIdentifier() );
		
		PlaceholderIdentifier t3 = new PlaceholderIdentifier( "prison_simple" );
		
		assertNotNull( t3.getIdentifier() );
		assertEquals( "prison_simple", t3.getIdentifier() );
		assertNull( t3.getEscapeLeft() );
		assertNull( t3.getEscapeRight() );
		assertEquals( "prison_simple", t3.getOriginalIdentifier() );
		
		PlaceholderIdentifier t4 = new PlaceholderIdentifier( "simple" );
		
		assertNotNull( t4.getIdentifier() );
		assertEquals( "prison_simple", t4.getIdentifier() );
		assertEquals( "simple", t4.getOriginalIdentifier() );
		
		PlaceholderIdentifier t5 = new PlaceholderIdentifier( "{prison_simple}" );
		
		assertNotNull( t5.getIdentifier() );
		assertEquals( "prison_simple", t5.getIdentifier() );
		assertEquals( "{", t5.getEscapeLeft() );
		assertEquals( "}", t5.getEscapeRight() );
		assertEquals( "{prison_simple}", t5.getOriginalIdentifier() );
		
		PlaceholderIdentifier t6 = new PlaceholderIdentifier( "{simple}" );
		
		assertNotNull( t6.getIdentifier() );
		assertEquals( "prison_simple", t6.getIdentifier() );
		assertEquals( "{", t6.getEscapeLeft() );
		assertEquals( "}", t6.getEscapeRight() );
		assertEquals( "{simple}", t6.getOriginalIdentifier() );
		
		PlaceholderIdentifier t7 = new PlaceholderIdentifier( "%prison_simple%" );
		
		assertNotNull( t7.getIdentifier() );
		assertEquals( "prison_simple", t7.getIdentifier() );
		assertEquals( "%", t7.getEscapeLeft() );
		assertEquals( "%", t7.getEscapeRight() );
		assertEquals( "%prison_simple%", t7.getOriginalIdentifier() );
		
		// test to validate an invalid escape character will result in no mapping:
		PlaceholderIdentifier t8 = new PlaceholderIdentifier( "#prison_simple#" );
		
		assertNotNull( t8.getIdentifier() );
		assertEquals( "prison_simple", t8.getIdentifier() );
		assertNotNull( t8.getEscapeLeft() );
		assertNotNull( t8.getEscapeRight() );
		assertEquals( "#", t8.getEscapeLeft() );
		assertEquals( "#", t8.getEscapeRight() );
		assertEquals( "#prison_simple#", t8.getOriginalIdentifier() );
		
		// It should be noted that "any" punction could be used, but is not a good idea.
		// It should also be noted that the left and right escape characters do not have
		// to match, but that is not prison's problem if the placeholder plugin sends 
		// prison an unmatched placeholder escape characters.
		PlaceholderIdentifier t9 = new PlaceholderIdentifier( "]prison_simple^" );
		
		assertNotNull( t9.getIdentifier() );
		assertEquals( "prison_simple", t9.getIdentifier() );
		assertNotNull( t9.getEscapeLeft() );
		assertNotNull( t9.getEscapeRight() );
		assertEquals( "]", t9.getEscapeLeft() );
		assertEquals( "^", t9.getEscapeRight() );
		assertEquals( "]prison_simple^", t9.getOriginalIdentifier() );
		
	}
	
	
	/**
	 * <p>This tests for the proper extraction of the placeholder attributes.
	 * The first three are duplicates from the prior unit test above, so only need
	 * to check for attributes.
	 * </p>
	 */
	@Test
	public void testPlaceholderAttributes() {
		
		// Setup the test Prison platform:
		Prison.get().setupJUnitInstance( new TestPlatform( null, true) );
		
		PlaceholderIdentifier t1 = new PlaceholderIdentifier(null);
		assertEquals( 0, t1.getAttributes().size() );
		
		PlaceholderIdentifier t2 = new PlaceholderIdentifier( "" );
		assertEquals( 0, t2.getAttributes().size() );
		
		PlaceholderIdentifier t3 = new PlaceholderIdentifier( "prison_simple" );
		assertEquals( 0, t3.getAttributes().size() );
		
		PlaceholderIdentifier t4 = new PlaceholderIdentifier( "prison_simple::nFormat" );
		assertEquals( 1, t4.getAttributes().size() );
		

		// failure because it's not a double colon:
		PlaceholderIdentifier t5 = new PlaceholderIdentifier( "prison_simple:nFormat" );
		assertEquals( 0, t5.getAttributes().size() );
		
		
		// Test with 3 placeholder attributes
		PlaceholderIdentifier t6 = new PlaceholderIdentifier( "prison_simple::nFormat::bar::text" );
		assertEquals( 3, t6.getAttributes().size() );
		
		
		// Test with 3 placeholder attributes between "junk"
		PlaceholderIdentifier t7 = new PlaceholderIdentifier( "prison_simple::junk1::nFormat::junk2::bar::junk3::text:junk4" );
		assertEquals( 3, t7.getAttributes().size() );
		
		
	}
	
	/**
	 * <p>This tests for the proper extraction of the placeholder sequence.
	 * The first four are duplicates from the prior unit tests above, so only need
	 * to check for sequences not being found.
	 * </p>
	 */
	@Test
	public void testPlaceholderSequences() {
		
		
		PlaceholderIdentifier t1 = new PlaceholderIdentifier(null);
		assertFalse( t1.hasSequence() );
		assertEquals( -1, t1.getSequence() );
		
		PlaceholderIdentifier t2 = new PlaceholderIdentifier( "" );
		assertFalse( t2.hasSequence() );
		assertEquals( -1, t2.getSequence() );
		
		PlaceholderIdentifier t3 = new PlaceholderIdentifier( "prison_simple" );
		assertFalse( t3.hasSequence() );
		assertEquals( -1, t3.getSequence() );
		
		PlaceholderIdentifier t4 = new PlaceholderIdentifier( "prison_simple::nFormat" );
		assertFalse( t4.hasSequence() );
		assertEquals( -1, t4.getSequence() );
		
		
		// NOTE: Sequences never are at the end of a placeholder so this is an invalid placeholder:
		PlaceholderIdentifier t5 = new PlaceholderIdentifier( "prison_simple_001" );
		assertFalse( t5.hasSequence() );
		assertEquals( -1, t5.getSequence() );

		
		PlaceholderIdentifier t6 = new PlaceholderIdentifier( "prison_simple_001_test" );
		assertTrue( t6.hasSequence() );
		assertEquals( 1, t6.getSequence() );
		
		PlaceholderIdentifier t7 = new PlaceholderIdentifier( "prison_simple_12345_test::nFormat::text" );
		assertTrue( t7.hasSequence() );
		assertEquals( 12345, t7.getSequence() );
		assertEquals( 2, t7.getAttributes().size() );
		
		PlaceholderIdentifier t8 = new PlaceholderIdentifier( "prison_simple_3_test::nFormat:junk::text" );
		assertTrue( t8.hasSequence() );
		assertEquals( 3, t8.getSequence() );
		assertEquals( 2, t8.getAttributes().size() );
		
		
	}
	

}