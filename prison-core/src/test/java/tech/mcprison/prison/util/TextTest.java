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
		
		assertEquals("This is a test", stripColor("This #123456is &ra test"));
		assertEquals("This is a test", stripColor("This #abCDeFis &ra test"));
		
		
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
	
	@Test
	public void testTranslateColorCodesQuotedText() {
		
		// Test no translations:
		assertEquals("This is a test", replaceColorCodeWithx( 
												translateColorCodes("This is a test", '&')));
		
		// test simple translations:
		assertEquals("This x7is a test", replaceColorCodeWithx( 
												translateColorCodes("This &7is a test", '&')));
		assertEquals("This x7is xra test", replaceColorCodeWithx( 
												translateColorCodes("This &7is &Ra test", '&')));

		// Test without quotes:
		assertEquals("This x7is xra x1tx2ex3sx4t", replaceColorCodeWithx( 
												translateColorCodes("This &7is &Ra &1t&2e&3s&4t", '&')));

		
		// Test with quotes:
		assertEquals("This x7is xra &1t&2e&3s&4t", replaceColorCodeWithx( 
												translateColorCodes("This &7is &Ra \\Q&1t&2e&3s&4t\\E", '&')));
		
	}
	
	/**
	 * <p>Running the junit test above in the IDE works well using the actual character that is
	 * referred to with the char code 167.  But running through gradle it has an issue with translation
	 * on the character code set and is not always the same character. </p>
	 * 
	 * <p>So to provide consistency and eliminate the UTF junk, we are converting it to a lower
	 * case x so it can at least test the function and not the character encoding of gradle.
	 * <p>
	 * 
	 * @param text
	 * @return
	 */
	private String replaceColorCodeWithx( String text ) {
		
		return replaceColorCodeWithx( text, 'x' );
		
	}
	private String replaceColorCodeWithx( String text, char replace ) {
		
		char code = 167;
		
		return text.replace( code, replace );
		
	}
	
	@Test
	public void testHexColors() {
		
		// Test no translations:
		assertEquals("This is a test", translateHexColorCodes("This is a test", Text.COLOR_CHAR));
		
		// test simple translations:
		assertEquals("This &7is a test", translateHexColorCodes("This &7is a test", Text.COLOR_CHAR));
		
		// Test a hex color code:
		assertEquals("This &7is ^x^a^3^b^4^c^5 &Ra test", replaceColorCodeWithx(
										translateHexColorCodes("This &7is #a3b4c5 &Ra test", Text.COLOR_CHAR), '^' ));

//		// Test without quotes:
//		assertEquals("This x7is xra x1tx2ex3sx4t", replaceColorCodeWithx( 
//												translateColorCodes("This &7is &Ra &1t&2e&3s&4t", '&')));

		// Test the translation within main:
		assertEquals("This ^7is ^x^a^3^b^4^c^5 ^ra test", replaceColorCodeWithx(
										translateColorCodes("This &7is #a3b4c5 &Ra test", '&'), '^' ));

		// Test with complete quote:
		assertEquals("This ^7is ^x^a^3^b^4^c^5 ^ra test #123456 test", replaceColorCodeWithx(
				translateColorCodes("This &7is #a3b4c5 &Ra test \\Q#123456 test\\E", '&'), '^' ));
		
		
		// Test with the alt hex encoding that could be used with placeholder hex support:
		assertEquals("This ^7is ^x^a^3^b^4^c^5 ^ra test ^x^1^2^3^4^5^6 test", replaceColorCodeWithx(
				translateAmpColorCodesAltHexCode("This &7is #a3b4c5 &Ra test #123456 test"), '^' ));
		
	}
}
