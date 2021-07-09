package tech.mcprison.prison.discord;

import static org.junit.Assert.*;

import org.junit.Test;

public class PrisonPasteChatTest
		extends
		PrisonPasteChat
{

	public PrisonPasteChatTest()
	{
		super( "jUnitTextSupportName" );
	}

	@Test
	public final void test()
	{
		assertEquals( "", cleanText( "", true ) );
		
		assertEquals( "test", cleanText( "test", true ) );
		assertEquals( "test &", cleanText( "test &", true ) );
		assertEquals( "test &7test&3 test", cleanText( "test &7test&3 test", true ) );
	}

}
