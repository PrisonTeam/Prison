package tech.mcprison.prison.discord;

import static org.junit.Assert.*;

import org.junit.Test;

public class PrisonSupportFilesTest 
		extends PrisonSupportFiles 
{

	@Test
	public void test() {
		
		String t1 = "This is a test";
		String r1 = "This is a test\n";
		
		assertEquals(r1, convertColorCodes(t1) );
		
		
		String t2 = "&3This is a test";
		String r2 = "<span class=\"cc3\">This is a test</span>\n";
		
		assertEquals(r2, convertColorCodes(t2) );
		
		
		String t3 = "&3This is a &1test";
		String r3 = "<span class=\"cc3\">This is a "
				+ "<span class=\"cc1\">test</span></span>\n";
		
		assertEquals(r3, convertColorCodes(t3) );
		
		
		String t4 = "&3This is a &1test & sample";
		String r4 = "<span class=\"cc3\">This is a "
				+ "<span class=\"cc1\">test & sample</span></span>\n";
		
		assertEquals(r4, convertColorCodes(t4) );
		
		
		String t5 = "&3This is a &1test & a&r fun &1sample";
		String r5 = "<span class=\"cc3\">This is a "
				+ "<span class=\"cc1\">test & a</span></span>"
				+ " fun <span class=\"cc1\">sample</span>\n";
		
		assertEquals(r5, convertColorCodes(t5) );
		
	}

}
