package tech.mcprison.prison.util;

import static org.junit.Assert.*;

import org.junit.Test;

import tech.mcprison.prison.placeholders.PlaceholdersUtil;

public class PlaceholdersUtilTest
		extends
		PlaceholdersUtil
{

	@Test
	public void test()
	{
		
		assertEquals( "0.00", formattedMetricSISize(0) );
		assertEquals( "1.00", formattedMetricSISize(1) );
		assertEquals( "123.00", formattedMetricSISize(123) );
		
		assertEquals( "1.23 k", formattedMetricSISize(1234) );
		assertEquals( "12.35 k", formattedMetricSISize(12345) );
		assertEquals( "123.46 k", formattedMetricSISize(123456) );
		
		assertEquals( "1.23 M", formattedMetricSISize(1234567) );
		assertEquals( "12.35 M", formattedMetricSISize(12345678) );
		assertEquals( "123.46 M", formattedMetricSISize(123456789) );
		
		assertEquals( "1.23 G", formattedMetricSISize(1234567890.0d) );
		assertEquals( "12.35 G", formattedMetricSISize(12345678901.0d) );
		assertEquals( "123.46 G", formattedMetricSISize(123456789012.0d) );
		
		assertEquals( "1.23 T", formattedMetricSISize(1234567890123.0d) );
		assertEquals( "12.35 T", formattedMetricSISize(12345678901234.0d) );
		assertEquals( "123.46 T", formattedMetricSISize(123456789012345.0d) );
		
		assertEquals( "1.23 P", formattedMetricSISize(1234567890123456.0d) );
		assertEquals( "12.35 P", formattedMetricSISize(12345678901234567.0d) );
		assertEquals( "123.46 P", formattedMetricSISize(123456789012345678.0d) );
		
		assertEquals( "1.23 E", formattedMetricSISize(1234567890123456789.0d) );
		assertEquals( "12.35 E", formattedMetricSISize(12345678901234567890.0d) );
		assertEquals( "123.46 E", formattedMetricSISize(123456789012345678901.0d) );
		
		assertEquals( "1.23 Z", formattedMetricSISize(1234567890123456789012.0d) );
		assertEquals( "12.35 Z", formattedMetricSISize(12345678901234567890123.0d) );
		assertEquals( "123.46 Z", formattedMetricSISize(123456789012345678901234.0d) );
		
		assertEquals( "1.23 Y", formattedMetricSISize(1234567890123456789012345.0d) );
		assertEquals( "12.35 Y", formattedMetricSISize(12345678901234567890123456.0d) );
		assertEquals( "123.46 Y", formattedMetricSISize(123456789012345678901234567.0d) );
		
		assertEquals( "1,234.57 Y", formattedMetricSISize(1234567890123456789012345678.0d) );
		assertEquals( "12,345.68 Y", formattedMetricSISize(12345678901234567890123456789.0d) );
		assertEquals( "123,456.79 Y", formattedMetricSISize(123456789012345678901234567890.0d) );
		
	}
	
	
	@Test
	public void testPrefixBinary() {
		
		assertEquals( "0.00", formattedPrefixBinarySize(0) );
		assertEquals( "1.00", formattedPrefixBinarySize(1) );
		assertEquals( "123.00", formattedPrefixBinarySize(123) );
		
		assertEquals( "1.21 KB", formattedPrefixBinarySize(1234) );
		assertEquals( "12.06 KB", formattedPrefixBinarySize(12345) );
		assertEquals( "120.56 KB", formattedPrefixBinarySize(123456) );
		
		assertEquals( "1.18 MB", formattedPrefixBinarySize(1234567) );
		assertEquals( "11.77 MB", formattedPrefixBinarySize(12345678) );
		assertEquals( "117.74 MB", formattedPrefixBinarySize(123456789) );
		
		assertEquals( "1.15 GB", formattedPrefixBinarySize(1234567890.0d) );
		assertEquals( "11.50 GB", formattedPrefixBinarySize(12345678901.0d) );
		assertEquals( "114.98 GB", formattedPrefixBinarySize(123456789012.0d) );
		
		assertEquals( "1.12 TB", formattedPrefixBinarySize(1234567890123.0d) );
		assertEquals( "11.23 TB", formattedPrefixBinarySize(12345678901234.0d) );
		assertEquals( "112.28 TB", formattedPrefixBinarySize(123456789012345.0d) );
		
		assertEquals( "1.10 PB", formattedPrefixBinarySize(1234567890123456.0d) );
		assertEquals( "10.97 PB", formattedPrefixBinarySize(12345678901234567.0d) );
		assertEquals( "109.65 PB", formattedPrefixBinarySize(123456789012345678.0d) );
		
		assertEquals( "1.07 EB", formattedPrefixBinarySize(1234567890123456789.0d) );
		assertEquals( "10.71 EB", formattedPrefixBinarySize(12345678901234567890.0d) );
		assertEquals( "107.08 EB", formattedPrefixBinarySize(123456789012345678901.0d) );
		
		
		assertEquals( "1.05 ZB", formattedPrefixBinarySize(1234567890123456789012.0d) );
		assertEquals( "10.46 ZB", formattedPrefixBinarySize(12345678901234567890123.0d) );
		assertEquals( "104.57 ZB", formattedPrefixBinarySize(123456789012345678901234.0d) );
		
		assertEquals( "1.02 YB", formattedPrefixBinarySize(1234567890123456789012345.0d) );
		assertEquals( "10.21 YB", formattedPrefixBinarySize(12345678901234567890123456.0d) );
		assertEquals( "102.12 YB", formattedPrefixBinarySize(123456789012345678901234567.0d) );
		
		assertEquals( "1,021.21 YB", formattedPrefixBinarySize(1234567890123456789012345678.0d) );
		assertEquals( "10,212.11 YB", formattedPrefixBinarySize(12345678901234567890123456789.0d) );
		assertEquals( "102,121.06 YB", formattedPrefixBinarySize(123456789012345678901234567890.0d) );

	}

}
