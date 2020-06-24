package tech.mcprison.prison.autofeatures;

import static org.junit.Assert.*;

import org.junit.Test;

public class ValueNodeFactoryTest
		extends
		ValueNodeFactory
{

	@Test
	public void testNormalValues()
	{
		{
			ValueNode vn = getValueNode( null );
			assertNotNull( vn );
			assertTrue( vn.isNullNode() );
			assertTrue( vn instanceof NullNode );
			assertTrue( ((NullNode) vn).isNull() );
		}
		
		{
			ValueNode vn = getValueNode( "true" );
			assertNotNull( vn );
			assertTrue( vn.isBooleanNode() );
			assertTrue( vn instanceof BooleanNode );
			assertTrue( ((BooleanNode) vn).getValue() );
		}
		
		{
			ValueNode vn = getValueNode( "false" );
			assertNotNull( vn );
			assertTrue( vn.isBooleanNode() );
			assertTrue( vn instanceof BooleanNode );
			assertFalse( ((BooleanNode) vn).getValue() );
		}
		
		{
			ValueNode vn = getValueNode( "1" );
			assertNotNull( vn );
			assertTrue( vn.isLongNode() );
			assertTrue( vn instanceof LongNode );
			assertEquals( 1L, ((LongNode) vn).getValue() );
		}
		
		{
			ValueNode vn = getValueNode( "123456789" );
			assertNotNull( vn );
			assertTrue( vn.isLongNode() );
			assertTrue( vn instanceof LongNode );
			assertEquals( 123456789L, ((LongNode) vn).getValue() );
		}
		
		{
			ValueNode vn = getValueNode( "-123456789" );
			assertNotNull( vn );
			assertTrue( vn.isLongNode() );
			assertTrue( vn instanceof LongNode );
			assertEquals( -123456789L, ((LongNode) vn).getValue() );
		}
		
		{
			ValueNode vn = getValueNode( "0.0" );
			assertNotNull( vn );
			assertTrue( vn.isDoubleNode() );
			assertTrue( vn instanceof DoubleNode );
			assertEquals( 0d, ((DoubleNode) vn).getValue(), 0.01 );
		}
		
		{
			ValueNode vn = getValueNode( "0.001" );
			assertNotNull( vn );
			assertTrue( vn.isDoubleNode() );
			assertTrue( vn instanceof DoubleNode );
			assertEquals( 0.001d, ((DoubleNode) vn).getValue(), 0.0001 );
		}
		
		{
			ValueNode vn = getValueNode( "1234567.8901" );
			assertNotNull( vn );
			assertTrue( vn.isDoubleNode() );
			assertTrue( vn instanceof DoubleNode );
			assertEquals( 1234567.8901d, ((DoubleNode) vn).getValue(), 0.001 );
		}
		
		{
			ValueNode vn = getValueNode( "-1234567.8901" );
			assertNotNull( vn );
			assertTrue( vn.isDoubleNode() );
			assertTrue( vn instanceof DoubleNode );
			assertEquals( -1234567.8901d, ((DoubleNode) vn).getValue(), 0.001 );
		}
		
		{
			// NOTE: Cannot parse scientific notation:
			ValueNode vn = getValueNode( "123.45E2" );
			assertNotNull( vn );
			assertFalse( vn.isDoubleNode() );
			assertTrue( vn.isTextNode() );
			assertTrue( vn instanceof TextNode );
//			assertEquals( 12345.0d, ((DoubleNode) vn).getValue(), 0.01 );
		}

		
		{
			ValueNode vn = getValueNode( "" );
			assertNotNull( vn );
			assertTrue( vn.isTextNode() );
			assertTrue( vn instanceof TextNode );
			assertEquals( "", ((TextNode) vn).getValue() );
		}
		
		{
			// Note: Leading and trailing spaces are removed.
			ValueNode vn = getValueNode( " test " );
			assertNotNull( vn );
			assertTrue( vn.isTextNode() );
			assertTrue( vn instanceof TextNode );
			assertEquals( "test", ((TextNode) vn).getValue() );
		}
		
		{
			ValueNode vn = getValueNode( "test 123" );
			assertNotNull( vn );
			assertTrue( vn.isTextNode() );
			assertTrue( vn instanceof TextNode );
			assertEquals( "test 123", ((TextNode) vn).getValue() );
		}
		
		
		
		
	}
	
	/**
	 * The purpose of these tests are to try to test the edge cases of what may not
	 * be normally encountered.
	 */
	@Test
	public void testStrangeValues()
	{
		// null is null, so nothing else to test with NullNode.
		
		{
			ValueNode vn = getValueNode( " true" );
			assertNotNull( vn );
			assertTrue( vn.isBooleanNode() );
			assertTrue( vn instanceof BooleanNode );
			assertTrue( ((BooleanNode) vn).getValue() );
		}
		
		{
			ValueNode vn = getValueNode( "  false   " );
			assertNotNull( vn );
			assertTrue( vn.isBooleanNode() );
			assertTrue( vn instanceof BooleanNode );
			assertFalse( ((BooleanNode) vn).getValue() );
		}
		
		{
			ValueNode vn = getValueNode( "   1   " );
			assertNotNull( vn );
			assertTrue( vn.isLongNode() );
			assertTrue( vn instanceof LongNode );
			assertEquals( 1L, ((LongNode) vn).getValue() );
		}
		
		{
			// Expected fail: No space between number and sign
			ValueNode vn = getValueNode( " - 123456789 " );
			assertNotNull( vn );
			assertFalse( vn.isLongNode() );
			assertTrue( vn.isTextNode() );
//			assertTrue( vn instanceof LongNode );
//			assertEquals( 123456789L, ((LongNode) vn).getValue() );
		}
		
		{
			// Expected fail: No double signs
			ValueNode vn = getValueNode( "-123456789-" );
			assertNotNull( vn );
			assertFalse( vn.isLongNode() );
			assertTrue( vn.isTextNode() );
//			assertTrue( vn instanceof LongNode );
//			assertEquals( -123456789L, ((LongNode) vn).getValue() );
		}
		
		{
			ValueNode vn = getValueNode( "0.0" );
			assertNotNull( vn );
			assertTrue( vn.isDoubleNode() );
			assertTrue( vn instanceof DoubleNode );
			assertEquals( 0d, ((DoubleNode) vn).getValue(), 0.01 );
		}
		
		{
			ValueNode vn = getValueNode( "0.001" );
			assertNotNull( vn );
			assertTrue( vn.isDoubleNode() );
			assertTrue( vn instanceof DoubleNode );
			assertEquals( 0.001d, ((DoubleNode) vn).getValue(), 0.0001 );
		}
		
		{
			ValueNode vn = getValueNode( "1234567.8901" );
			assertNotNull( vn );
			assertTrue( vn.isDoubleNode() );
			assertTrue( vn instanceof DoubleNode );
			assertEquals( 1234567.8901d, ((DoubleNode) vn).getValue(), 0.001 );
		}
		
		{
			ValueNode vn = getValueNode( "-1234567.8901" );
			assertNotNull( vn );
			assertTrue( vn.isDoubleNode() );
			assertTrue( vn instanceof DoubleNode );
			assertEquals( -1234567.8901d, ((DoubleNode) vn).getValue(), 0.001 );
		}
		
		{
			// NOTE: Cannot parse scientific notation:
			ValueNode vn = getValueNode( "123.45E2" );
			assertNotNull( vn );
			assertFalse( vn.isDoubleNode() );
			assertTrue( vn.isTextNode() );
			assertTrue( vn instanceof TextNode );
//			assertEquals( 12345.0d, ((DoubleNode) vn).getValue(), 0.01 );
		}

		
		{
			ValueNode vn = getValueNode( "" );
			assertNotNull( vn );
			assertTrue( vn.isTextNode() );
			assertTrue( vn instanceof TextNode );
			assertEquals( "", ((TextNode) vn).getValue() );
		}
		
		{
			ValueNode vn = getValueNode( " test\n " );
			assertNotNull( vn );
			assertTrue( vn.isTextNode() );
			assertTrue( vn instanceof TextNode );
			assertEquals( "test", ((TextNode) vn).getValue() );
		}
		
		{
			ValueNode vn = getValueNode( "test 123  " );
			assertNotNull( vn );
			assertTrue( vn.isTextNode() );
			assertTrue( vn instanceof TextNode );
			assertEquals( "test 123", ((TextNode) vn).getValue() );
		}
		
		
		
		
	}

}
