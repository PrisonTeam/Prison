package tech.mcprison.prison.util;

import org.junit.Test;

public class JumboTextFontTest
{

	@Test
	public final void test()
	{
		testPrint( "ABC, CDEFG." );
		
		testPrint( "HIJKL? ABC." );

		testPrint( "MNOPQRST" );
		
		testPrint( "UVWXYZ!?" );

		testPrint( "1234567890" );
		
		testPrint( "ABC ?/!\\,? CBA" );

	}

	public void testPrint( String text ) {
		
		StringBuilder sb = new StringBuilder();
		
		JumboTextFont.makeJumboFontText( text, sb );
		
		System.out.println( text );
		System.out.println();
		System.out.println( sb.toString() );
		System.out.println();

	}
}
