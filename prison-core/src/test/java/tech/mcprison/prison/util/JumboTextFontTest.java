package tech.mcprison.prison.util;

import org.junit.Test;

public class JumboTextFontTest
{

	@Test
	public final void test()
	{
		testPrint( "AaBbCcDd" );
		
		testPrint( "ABCDEFGHIJ" );

		testPrint( "KLMNOPQRST" );
		
		testPrint( "UVWXYZ!?" );

		testPrint( "1234567890" );
		
		testPrint( "ABC Aa, Bb. Cd!" );
		
		testPrint( "abcdefghij");
		
		testPrint( "klmnopqrst" );
		
		testPrint( "uvwxyz" );
		
		testPrint( "aA/_-+=_\\Bb" );

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
