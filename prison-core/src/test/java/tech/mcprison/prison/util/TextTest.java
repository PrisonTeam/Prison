package tech.mcprison.prison.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TextTest
		extends Text
{

	@Test
	public void testStripColor() {
		//
//	    public static final Pattern STRIP_COLOR_PATTERN =
//	            Pattern.compile("(?i)" + COLOR_ + "#[A-Fa-f0-9]{6}|" + 
//	            						 COLOR_ + "[0-9A-FK-OR]");
	    
		assertEquals("This is a test", stripColor("This is a test"));
		assertEquals("This is a test", stripColor("This &7is a test"));
		assertEquals("This is a test", stripColor("This &7is &Ra test"));
		assertEquals("This is a test", stripColor("This &7is &ra test"));
		
		assertEquals("This is a test", stripColor("This &0&1&2&3&4&5&6&7&8&9" +
				"&a&A&B&b&c&C&d&D&e&E&f&F&k&K&l&L&m&M&n&N&o&O&r&Ris &Ra test"));
		
		assertEquals("This is a test", stripColor("&KTh&ris &7is &aa &At&Be&8s&9t"));
		
		assertEquals("This is a test", stripColor("This &#123456is &ra test"));
		assertEquals("This is a test", stripColor("This &#abCDeFis &ra test"));
		
		
	}

	public void testTranslateAmpColorCodes() {
		assertEquals("This is a test", translateColorCodes("This is a test", 'x'));
		assertEquals("This x7is a test", translateColorCodes("This &7is a test", 'x'));
		assertEquals("This x7is xRa test", translateColorCodes("This &7is &Ra test", 'x'));
		assertEquals("This x7is xra test", translateColorCodes("This &7is &ra test", 'x'));
		
		
		assertEquals("xKThxris x7is xaa xAtxBex8sx9t", 
							translateColorCodes("&KTh&ris &7is &aa &At&Be&8s&9t", 'x'));
		
		assertEquals("This x#123456is xra test", translateColorCodes("This &#123456is &ra test", 'x'));
		assertEquals("This x#abCDeFis &xra test", translateColorCodes("This &#abCDeFis &ra test", 'x'));

		
	}
}
