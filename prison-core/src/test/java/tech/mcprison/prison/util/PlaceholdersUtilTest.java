package tech.mcprison.prison.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class PlaceholdersUtilTest
		extends
		PlaceholdersUtil
{

	@Test
	public void test()
	{
		
		assertEquals( "0.00", formattedSize(0) );
		assertEquals( "1.00", formattedSize(1) );
		assertEquals( "123.00", formattedSize(123) );
		
		assertEquals( "1.23 k", formattedSize(1234) );
		assertEquals( "12.35 k", formattedSize(12345) );
		assertEquals( "123.46 k", formattedSize(123456) );
		
		assertEquals( "1.23 M", formattedSize(1234567) );
		assertEquals( "12.35 M", formattedSize(12345678) );
		assertEquals( "123.46 M", formattedSize(123456789) );
		
		assertEquals( "1.23 G", formattedSize(1234567890.0d) );
		assertEquals( "12.35 G", formattedSize(12345678901.0d) );
		assertEquals( "123.46 G", formattedSize(123456789012.0d) );
		
		assertEquals( "1.23 T", formattedSize(1234567890123.0d) );
		assertEquals( "12.35 T", formattedSize(12345678901234.0d) );
		assertEquals( "123.46 T", formattedSize(123456789012345.0d) );
		
		assertEquals( "1.23 P", formattedSize(1234567890123456.0d) );
		assertEquals( "12.35 P", formattedSize(12345678901234567.0d) );
		assertEquals( "123.46 P", formattedSize(123456789012345678.0d) );
		
		assertEquals( "1.23 E", formattedSize(1234567890123456789.0d) );
		assertEquals( "12.35 E", formattedSize(12345678901234567890.0d) );
		assertEquals( "123.46 E", formattedSize(123456789012345678901.0d) );
		
		assertEquals( "1.23 Z", formattedSize(1234567890123456789012.0d) );
		assertEquals( "12.35 Z", formattedSize(12345678901234567890123.0d) );
		assertEquals( "123.46 Z", formattedSize(123456789012345678901234.0d) );
		
		assertEquals( "1.23 Y", formattedSize(1234567890123456789012345.0d) );
		assertEquals( "12.35 Y", formattedSize(12345678901234567890123456.0d) );
		assertEquals( "123.46 Y", formattedSize(123456789012345678901234567.0d) );
		
		assertEquals( "1,234.57 Y", formattedSize(1234567890123456789012345678.0d) );
		assertEquals( "12,345.68 Y", formattedSize(12345678901234567890123456789.0d) );
		assertEquals( "123,456.79 Y", formattedSize(123456789012345678901234567890.0d) );
		
	}

}
