package tech.mcprison.prison.commands;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class TabCompleterDataTest
		extends
		TabCompleaterData
{

	@Test
	public void test()
	{
		String testA = "/cmd a";
		String testB = "/cmd b";
		String testC = "/cmd mid c";
		String testD = "/cmd mid d";
		String testE = "/e";
		String testF = "/f";
		String testG = "/e g";
		
		TabCompleaterData tcd = new TabCompleaterData();
		
		assertEquals( 0, tcd.getData().size() );
		
		RegisteredCommand testRCA = RegisteredCommand.junitTest( testA );
		assertEquals( "/cmd a", testRCA.getUsage() );
		
		assertEquals( 0, tcd.getData().size() );

		RegisteredCommand testRCB = RegisteredCommand.junitTest( testB );
		RegisteredCommand testRCC = RegisteredCommand.junitTest( testC );
		RegisteredCommand testRCD = RegisteredCommand.junitTest( testD );
		RegisteredCommand testRCE = RegisteredCommand.junitTest( testE );
		RegisteredCommand testRCF = RegisteredCommand.junitTest( testF );
		RegisteredCommand testRCG = RegisteredCommand.junitTest( testG );
		
		tcd.add( testRCA );
		
		assertEquals( 1, tcd.getData().size() );
		assertEquals( 1, tcd.getData().get( "cmd" ).getData().size() );

		tcd.add( testRCB );
		
		assertEquals( 1, tcd.getData().size() );
		assertEquals( 2, tcd.getData().get( "cmd" ).getData().size() );
		
		tcd.add( testRCC );
		
		assertEquals( 1, tcd.getData().size() );
		assertEquals( 3, tcd.getData().get( "cmd" ).getData().size() );
		assertEquals( 1, tcd.getData().get( "cmd" ).getData().get( "mid" ).getData().size() );
		
		tcd.add( testRCD );
		
		assertEquals( 1, tcd.getData().size() );
		assertEquals( 3, tcd.getData().get( "cmd" ).getData().size() );
		assertEquals( 2, tcd.getData().get( "cmd" ).getData().get( "mid" ).getData().size() );
		
		tcd.add( testRCE );
		
		assertEquals( 2, tcd.getData().size() );
		
		tcd.add( testRCF );
		
		assertEquals( 3, tcd.getData().size() );
		
		tcd.add( testRCG );
		
		assertEquals( 3, tcd.getData().size() );
		assertEquals( 1, tcd.getData().get( "e" ).getData().size() );
		
		
		// Structure should be "good":
		
		// Now test pulling out results:
		List<String> results1 = tcd.check( "cmd", "" );
		
		assertEquals( 3, results1.size() );
		assertEquals( "a", results1.get(0) );
		assertEquals( "b", results1.get(1) );
		assertEquals( "mid", results1.get(2) );

		List<String> results2 = tcd.check( "cmd", "m" );
		
		assertEquals( 1, results2.size() );
		
		
		List<String> results3 = tcd.check( "cmd", "mi" );
		
		assertEquals( 1, results3.size() );

		List<String> results4 = tcd.check( "cmd", "mid" );
		
		assertEquals( 1, results4.size() );
		
		
		List<String> results5 = tcd.check( "cmd", "mid", "" );
		
		assertEquals( 2, results5.size() );
		assertEquals( "c", results5.get(0) );
		assertEquals( "d", results5.get(1) );
		
		List<String> results6 = tcd.check( "cmd", "mid", "c" );
		
		assertEquals( 1, results6.size() );
		assertEquals( "c", results6.get(0) );
		
		
		List<String> results7 = tcd.check( "cmd", "mid", "x" );
		
		assertEquals( 0, results7.size() );
		
		
		
	}

}
