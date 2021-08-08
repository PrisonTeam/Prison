package tech.mcprison.prison.util;

import java.util.ArrayList;
import java.util.List;

public enum JumboTextFont {

	A("A", 5, "  A  ", " A A ", "AAAAA", "A   A", "A   A" ),
	B("B", 5, "BBBB ", "B   B", "BBBB ", "B   B", "BBBB " ),
	C("C", 5, " CCC ", "C   C", "C    ", "C   C", " CCC " ),
	D("D", 5, "DDDD ", "D   D", "D   D", "D   D", "DDDD " ),
	E("E", 5, "EEEEE", "E    ", "EEEE ", "E    ", "EEEEE" ),
	F("F", 5, "FFFFF", "F    ", "FFF  ", "F    ", "F    " ),
	G("G", 5, " GGG ", "G    ", "G GGG", "G   G", " GGG " ),
	H("H", 5, "H   H", "H   H", "HHHHH", "H   H", "H   H" ),
	I("I", 3, "III", " I ", " I ", " I ", "III" ),
	J("J", 5, "JJJJJ", "   J ", "   J ", "J  J ", "JJJ  " ),
	
	K("K", 5, "K   K", "K  K ", "KK   ", "K  K ", "K   K" ),
	L("L", 4, "L   ", "L   ", "L   ", "L   ", "LLLL" ),
	M("M", 5, "M   M", "MM MM", "M M M", "M   M", "M   M" ),
	N("N", 5, "N   N", "NN  N", "N N N", "N  NN", "N   N" ),
	O("O", 5, " OOO ", "O   O", "O   O", "O   O", " OOO " ),
	P("P", 5, "PPPP ", "P   P", "PPPP ", "P    ", "P    "),
	Q("Q", 5, " QQQ ", "Q   Q", "Q \\ Q", "Q  \\Q", " QQ \\" ),
	R("R", 5, "RRRR ", "R   R", "RRRR ", "R  R ", "R   R" ),
	S("S", 5, " SSS ", "S    ", " SSS ", "    S", " SSS " ),
	T("T", 5, "TTTTT", "  T  ", "  T  ", "  T  ", "  T  " ),
	
	U("U", 5, "U   U", "U   U", "U   U", "U   U", " UUU " ),
	V("V", 5, "V   V", "V   V", "V   V", " V V ", "  V  " ),
	W("W", 5, "W   W", "W   W", "W W W", "WW WW", "W   W" ),
	X("X", 5, "X   X", " X X ", "  X  ", " X X ", "X   X" ),
	Y("Y", 5, "Y   Y", " Y Y ", "  Y  ", "  Y  ", "  Y  " ),
	Z("Z", 5, "ZZZZZ", "   Z ", "  Z  ", " Z   ", "ZZZZZ" ),
	
	n1("1", 3, " 1 ", "11 ", " 1 ", " 1 ", "111" ),
	n2("2", 5, " 222 ", "2   2", "   2 ", "  2  ", "22222" ),
	n3("3", 5, " 3333", "    3", "  33 ", "    3", "33333" ),
	n4("4", 5, "   4 ", " 4 4 ", "44444", "   4 ", "   4 " ),
	n5("5", 5, "55555", "5    ", "5555 ", "    5", "5555 " ),
	n6("6", 5, " 666 ", "6    ", "6666 ", "6   6", " 666 " ),
	n7("7", 5, "77777", "   7 ", "  7  ", " 7   ", "7    " ),
	n8("8", 5, " 888 ", "8   8", " 888 ", "8   8", " 888 " ),
	n9("9", 5, " 999 ", "9   9", " 9999", "    9", " 999 " ),
	n0("0", 5, " 000 ", "0   0", "0   0", "0   0", " 000 " ),
	
	space(" ", 2, "  ", "  ", "  ", "  ", "  " ),
	comma(",", 2, "  ", "  ", "##", " #", "# " ),
	period(".", 2, "  ", "  ", "  ", "##", "##" ),
	exclaim("!", 1, "!", "!", "!", " ", "*" ),
	question("?", 5, " ??? ", "?   ?", "   ? ", "     ", "   * " ),
	slash("/", 5, "    /", "   / ", "  /  ", " /   ", "/    " ),
	backslash("\\", 5, "\\    ", " \\   ", "  \\  ", "   \\ ", "    \\" )
	;
	
	
	private final String letter;
	private final int width;
	private final List<String> fontRows;
	private JumboTextFont( String letter, int width, String... font) {
		this.letter = letter;
		this.width = width;
		this.fontRows = new ArrayList<>();
		
		for ( String line : font )
		{
//			line = (line.length() != 5 ? (line + "     ").substring( 0, 5 ) : line );
			fontRows.add( line );
		}
	}
	
	
	public String getLetter() {
		return letter;
	}
	public int getWidth() {
		return width;
	}
	public List<String> getFontRows() {
		return fontRows;
	}
	
	public static void makeJumboFontText( String text, StringBuilder sb ) {
		
		// Temp until lowercase is added:
		text = text.toUpperCase();
		
		List<StringBuilder> texts = JumboTextFont.textLines( text );
		
		for ( StringBuilder sbLine : texts ) {
			sb.append( sbLine ).append( "\n" );
		}

	}
	
	private static List<StringBuilder> textLines( String word ) {
		List<JumboTextFont> jtfs = JumboTextFont.buildJumboText( word );
		
		int fontHeight = 5;
		
		List<StringBuilder> texts = new ArrayList<>();
		for ( int i = 0; i < fontHeight; i++ ) {
			texts.add( new StringBuilder() );
		}
		
		for ( JumboTextFont jumboTextFont : jtfs ) {
			
			for ( int i = 0; i < 5; i++ ) {
				String frow = jumboTextFont.getFontRows().get( i );
				texts.get( i ).append( frow ).append( "  " );
			}
			
		}

		return texts;
	}
	
	private static List<JumboTextFont> buildJumboText( String word ) {
		List<JumboTextFont> results = new ArrayList<>();
		
		for ( String letter : word.split("|") )
		{
			JumboTextFont jtf = findMatch( letter );
			if ( jtf != null ) {
				results.add( jtf );
			}
		}
		
		return results;
	}
	
	private static JumboTextFont findMatch( String letter ) {
		JumboTextFont results = null;
		
		if ( letter != null ) {
			
			for ( JumboTextFont jtf : values() ) {
				if ( jtf.getLetter().equals( letter ) ) {
					results = jtf;
					break;
				}
			}
		}
		
		return results;
	}
	
}
