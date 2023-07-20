package tech.mcprison.prison.discord;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;

import tech.mcprison.prison.Prison;

public class PrisonSupportFiles {
	
	public static final String PSFN_KEY__NAME = "{$name}";
	public static final String PSFN_KEY__NUMBER = "{$number}";
	public static final String PRISON_SUPPORT_FILE_NAME__PATTERN = "prison_support_" + 
										PSFN_KEY__NAME + "_" + PSFN_KEY__NUMBER + ".htm";
	
	public static final String SECTION_CODE = "§";

	private File supportFile;

	private boolean colorMapping = true;
	
	public enum ColorMaps {
		black( "&0", "<span class=\"cc0\">", "</span>", ".cc0 { color: black; }"),
		DarkBlue( "&1", "<span class=\"cc1\">", "</span>", ".cc1 { color: darkblue; }"),
		DarkGreen( "&2", "<span class=\"cc2\">", "</span>", ".cc2 { color: darkgreen; }"),
		DarkCyan( "&3", "<span class=\"cc3\">", "</span>", ".cc3 { color: darkCyan; }"),
		DarkRed( "&4", "<span class=\"cc4\">", "</span>", ".cc4 { color: darkred; }"),
		DarkMagenta( "&5", "<span class=\"cc5\">", "</span>", ".cc5 { color: darkmagenta; }"),
		Orange( "&6", "<span class=\"cc6\">", "</span>", ".cc6 { color: orange; }"),
		LightGray( "&7", "<span class=\"cc7\">", "</span>", ".cc7 { color: lightgray; }"),
		DarkGray( "&8", "<span class=\"cc8\">", "</span>", ".cc8 { color: darkgray; }"),

		Blue( "&9", "<span class=\"cc9\">", "</span>", ".cc9 { color: blue; }"),
		Green( "&a", "<span class=\"cca\">", "</span>", ".cca { color: green; }"),
		Cyan( "&b", "<span class=\"ccb\">", "</span>", ".ccb { color: cyan; }"),
		Red( "&c", "<span class=\"color:Red\">", "</span>", ".ccc { color: red[; }"),
		Magenta( "&d", "<span class=\"ccd\">", "</span>", ".ccd { color: magenta; }"),
		Yellow( "&e", "<span class=\"cce\">", "</span>", ".cce { color: yellow; }"),
		White( "&f", "<span class=\"ccf\">", "</span>", ".ccf { color: white; }"),

		
		bold( "&l", "<b>", "</b>", null ),
		strike( "&m", "<s>", "</s>", null ),
		underline( "&n", "<u>", "</u>", null ),
		italic( "&o", "<i>", "</i>", null ),

		reset( "&r", "", "", null ),
		
		// Internal codes:
		colorMappingOff( "&-", "", "", null ),
		colorMappingOn( "&+", "", "", null )
		;
		
		private final String colorCode;
		private final String start;
		private final String end;
		private final String css;
		private ColorMaps( String colorCode, String start, String end, String css ) {
			this.colorCode = colorCode;
			this.start = start;
			this.end = end;
			this.css = css;
		}
		public String getColorCode() {
			return colorCode;
		}
		public String getStart() {
			return start;
		}
		public String getEnd() {
			return end;
		}
		public String getCss() {
			return css;
		}

		public static ColorMaps match( String line ) {
			ColorMaps results = null;
			
			for (ColorMaps cm : values() ) {
				
				String cc1 = cm.getColorCode();
				String cc2 = cc1.replace("&", SECTION_CODE);

//				char test = line.charAt(0);
//				char test2 = '\u0167';
//				
//				String t = "Test: " + test + test2;
//				
//				String cc2 = cc1.replace("&", "§");
//				String cc3 = cc1.replace("&", "\u0167"); // Section code: §, &#167; &#xA7; &sect;
//				String cc4 = cc1.replace("&", Character.toString(test2) ); 
//
//				char test3 = cc2.charAt(0);
				
//				Character.getType( cc2.charAt(0));
//				Character. ( cc2.charAt(0));
				
				if ( line.toLowerCase().startsWith( cc1 ) || line.startsWith( cc2 ) ) {
					results = cm;
					break;
				}
			}
			
			return results;
		}
	}
	
	
	public File setupSupportFile( String name ) {
		
		String nameId = name.replaceAll("\\s", "_");
		File file = createSupportFile( nameId );
		
		setSupportFile( file );
		
		return getSupportFile();
	}
	
	public void saveToSupportFile( StringBuilder text ) {
		
		File file = getSupportFile();
		
		// If an empty file, then delete it:
		if ( file.exists() && file.length() == 0 ) {
			file.delete();
		}
		
		if ( !file.exists() ) {
			saveSupportDataToFile( text );
		}
		else {
			appendSaveSupportDataToFile( text );
		}
		
	}
	
//	private void saveSupportDataToFile( StringBuilder text ) {
//		File file = getSupportFile();
//		
//		try {
//			Files.write( text.toString().getBytes(), file);
//		} 
//		catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	
	private void saveSupportDataToFile( StringBuilder text ) {
		File file = getSupportFile();
		
		try ( 
				BufferedWriter bw = new BufferedWriter( new FileWriter( file, false )); // create file	
				) {
			
			
			bw.write( getGlobalCss() );
			
			writeBufferedWriter( bw, text );
			
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void appendSaveSupportDataToFile( StringBuilder text ) {
		File file = getSupportFile();
		
		try ( 
			BufferedWriter bw = new BufferedWriter( new FileWriter( file, true )); // append	
			) {
			
			bw.write( "\n\n- = - = - = - = - = - = - = - = - = - = -\n\n");
			
			writeBufferedWriter( bw, text );
//			bw.write( text.toString() );
			
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	private void writeBufferedWriter( BufferedWriter bw, StringBuilder text ) {
		
		try (
				BufferedReader br = new BufferedReader( new StringReader( text.toString() ));
			) {
			
			String line = br.readLine();
			
			while ( line != null ) {
				
				String converted = convertColorCodes( line );
				
				bw.write(converted);
				
				line = br.readLine();
			}
			
		}
		catch ( Exception e ) {
			
		}
	}
	

	protected String convertColorCodes(String line) {
		StringBuilder sb = new StringBuilder();

		if ( isColorMapping() ) {
			
			StringBuilder sbEnd = new StringBuilder();
			
			for ( int i = 0; i < line.length(); i++ ) {
				
				int idx1 = line.indexOf(SECTION_CODE, i);
				int idx2 = line.indexOf("&", i);
				
				int idx = smallestButValid( idx1, idx2 );
				
				if ( idx == -1 ) {
					sb.append( line.substring(i) );
					break;
				}
				
				String left = line.substring(i, idx);
				sb.append( left );
				
				String right = line.substring(idx); 
				ColorMaps cm = ColorMaps.match( right );
				
				if ( cm == null ) {
					sb.append( line.substring(idx,idx+1));
					i = idx;
				}
				else if ( cm == ColorMaps.colorMappingOff ) {
					
					setColorMapping( false );
					sb.append( right.replace( cm.getColorCode(), "" ) );
					break;
				}
				else if ( cm == ColorMaps.colorMappingOn ) {

					setColorMapping( true );
					sb.append( right.replace( cm.getColorCode(), "" ) );
					break;
				}
				else if ( cm == ColorMaps.reset ) {
					sb.append( sbEnd );
					sbEnd.setLength(0);
					i = idx + 1;
				}
				else {
					sb.append( cm.getStart() );
					sbEnd.insert(0, cm.getEnd() );
					i = idx + 1;
				}
			}
			
			sb.append( sbEnd )
			.append( "<br />");
		}
		else {
			ColorMaps cm = ColorMaps.match( line );
			
			// ignore all other maps... since color mapping is off, we can only search for ON:
			if ( cm == ColorMaps.colorMappingOn ) {
				setColorMapping( true );
				
				// Return the line without the color code:
				sb.append( line.replace( cm.getColorCode(), "" ) )
					.append( "<br />");
			}
			else {
				sb.append( line )
					.append( "<br />");
				
			}
		}
		
		return sb.toString();
	}

	private int smallestButValid(int idx1, int idx2) {
		
		int results =  idx1 != -1 ? idx1 : -1;
		
		if ( results == -1 || idx2 != -1 && idx2 < results ) {
			results = idx2;
		}
		
		return results;
	}

	public String getFileStats( long addedContentSize ) {
		StringBuilder sb = new StringBuilder();
		
		DecimalFormat dFmt = new DecimalFormat( "#,##0.000" );
		
		double fileSize = getSupportFile().length() / 1024d;
		
		double newDataSize = addedContentSize / 1024d;
		
		sb.append( "    * " )
			.append( getSupportFile().getAbsolutePath() )
			.append( "  " )
			.append( dFmt.format(fileSize) )
			.append( " KB   Before HTML conversion: " )
			.append( dFmt.format(newDataSize) )
			.append( " KB");
		
		return sb.toString();
	}
	
	
	private File createSupportFile( File dir, String name ) {
		File file = null;
		
		DecimalFormat iFmt = new DecimalFormat( "0000" );
		
		for ( int i = 0; i < 1000; i++ ) {
			String fileName = PRISON_SUPPORT_FILE_NAME__PATTERN
						.replace( PSFN_KEY__NAME, name )
						.replace( PSFN_KEY__NUMBER, iFmt.format( i ) );
			
			file = new File( dir, fileName );
			if ( !file.exists() ) {
				break;
			}
		}
		return file;
	}
	
	private File createSupportFile( String name ) {
		File dir = new File( Prison.get().getDataFolder(), "backups" );
		
		File file = createSupportFile( dir, name );
		
//		File[] files = dir.listFiles( new FilenameFilter() {
//			public boolean accept( File dir, String fileName ) {
//				return fileName.startsWith("prison_support_") && 
//						fileName.endsWith(".md");
//			}
//		});
		
		return file;
	}

	private String getGlobalCss() {
		StringBuilder sb = new StringBuilder();
		
		sb.append( "<style>\n" )
		
			.append( "body {\n")
			.append( "  font-family: ui-monospaced, monospaced, \"Lucida Console\", \"Courier New\", \"Courier\";\n")
			.append( "  color: white;\n")
			.append( "  background-color: #494949;\n")
			.append( "  white-space: pre;\n")
			.append( "}\n");

		
		for (ColorMaps colorMap : ColorMaps.values()) {
			if ( colorMap.getCss() != null ) {
				
				sb.append( colorMap.getCss() ).append( "\n" );
			}
		}
		
		
		sb.append( "</style>" );
		
		return sb.toString();
	}
	
	public File getSupportFile() {
		return supportFile;
	}
	public void setSupportFile(File supportFile) {
		this.supportFile = supportFile;
	}

	public boolean isColorMapping() {
		return colorMapping;
	}
	public void setColorMapping(boolean colorMapping) {
		this.colorMapping = colorMapping;
	}

	
}
