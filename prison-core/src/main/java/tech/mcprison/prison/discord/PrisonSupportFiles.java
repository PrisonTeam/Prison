package tech.mcprison.prison.discord;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.Prison;

public class PrisonSupportFiles {
	
	public static final String PSFN_KEY__NAME = "{$name}";
	public static final String PSFN_KEY__NUMBER = "{$number}";
	public static final String PRISON_SUPPORT_FILE_NAME__PATTERN = "prison_support_" + 
										PSFN_KEY__NAME + "_" + PSFN_KEY__NUMBER + ".html";
	
	public static final String SECTION_CODE = "§";

	private File supportFile;

	private boolean colorMapping = true;
	
	private PrisonSupportFileLinkage linkage;
	
	public enum ColorMaps {
		black( "&0", "<span class=\"cc0\">", "</span>" ),
		DarkBlue( "&1", "<span class=\"cc1\">", "</span>" ),
		DarkGreen( "&2", "<span class=\"cc2\">", "</span>" ),
		DarkCyan( "&3", "<span class=\"cc3\">", "</span>" ),
		DarkRed( "&4", "<span class=\"cc4\">", "</span>" ),
		DarkMagenta( "&5", "<span class=\"cc5\">", "</span>" ),
		Orange( "&6", "<span class=\"cc6\">", "</span>" ),
		LightGray( "&7", "<span class=\"cc7\">", "</span>" ),
		DarkGray( "&8", "<span class=\"cc8\">", "</span>" ),

		Blue( "&9", "<span class=\"cc9\">", "</span>" ),
		Green( "&a", "<span class=\"cca\">", "</span>" ),
		Cyan( "&b", "<span class=\"ccb\">", "</span>" ),
		Red( "&c", "<span class=\"ccc\">", "</span>" ),
		Magenta( "&d", "<span class=\"ccd\">", "</span>" ),
		Yellow( "&e", "<span class=\"cce\">", "</span>" ),
		White( "&f", "<span class=\"ccf\">", "</span>" ),

		
		bold( "&l", "<b>", "</b>" ),
		strike( "&m", "<s>", "</s>" ),
		underline( "&n", "<u>", "</u>" ),
		italic( "&o", "<i>", "</i>" ),

		reset( "&r", "", "" ),
		
		// Internal codes:
		colorMappingOff( "&-", "", "" ),
		colorMappingOn( "&+", "", "" )
		;
		
		private final String colorCode;
		private final String start;
		private final String end;
		private ColorMaps( String colorCode, String start, String end ) {
			this.colorCode = colorCode;
			this.start = start;
			this.end = end;
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
	
	public void saveToSupportFile( StringBuilder text, String supportName ) {
		
		File file = getSupportFile();
		
		// If an empty file, then delete it:
		if ( file.exists() && file.length() == 0 ) {
			file.delete();
		}
		
		if ( !file.exists() ) {
			saveSupportDataToFile( text, supportName );
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
	
	
	private void saveSupportDataToFile( StringBuilder text, String supportName ) {
		File file = getSupportFile();
		
		linkage = new PrisonSupportFileLinkage();
		
		try ( 
				BufferedWriter bw = new BufferedWriter( new FileWriter( file, false )); // create file	
				) {
			
			
			bw.write( getHtmlHead( supportName ) );
			bw.write( getHtmlBodyStart() );
			
			writeBufferedWriter( bw, text );
			
//			bw.write( getHtmlBodyEnd() );

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

	
	
	private List<String> extractAllHyperlinkPlaceholders( StringBuilder text ) {
		
		List<String> hlp = new ArrayList<>();
		
		try (
				BufferedReader br = new BufferedReader( new StringReader( text.toString() ));
			) {
			
			String line = br.readLine();
			
			while ( line != null ) {
				
				
				if ( line.startsWith( "||`" ) ) {
					
					linkage.addLinkage( line );
					hlp.add( line );
				}
				
				line = br.readLine();
			}
			
		}
		catch ( Exception e ) {
			
		}
		
		return hlp;
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
//				else if ( cm == ColorMaps.colorMappingOn ) {
//					
//					setColorMapping( true );
//					sb.append( right.replace( cm.getColorCode(), "" ) );
//					break;
//				}
//				else if ( !isColorMapping() ) {
//					i = idx + 1;
//				}
				else if ( cm == ColorMaps.colorMappingOff ) {
					
					setColorMapping( false );
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
			.append( "\n");
		}
		else {
			ColorMaps cm = ColorMaps.match( line );
			
			// ignore all other maps... since color mapping is off, we can only search for ON:
			if ( cm == ColorMaps.colorMappingOn ) {
				setColorMapping( true );
				
				// Return the line without the color code:
				sb.append( line.replace( cm.getColorCode(), "" ) )
					.append( "\n");
			}
			else {
				sb.append( line )
					.append( "\n");
				
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

	private String getHtmlHead( String supportName ) {
		StringBuilder sb = new StringBuilder();

		sb.append( "<!DOCTYPE html>\n" )
			.append( "<html>\n" )
			.append( "<head>\n" )
			.append( "<title>Prison Support: " ) 
			.append( 			supportName )
			.append( "</title>\n" )
			.append( "  <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.7.0/jquery.min.js\"></script>\n" )
			.append( getGlobalCss() )
			.append( "</head>\n" );
		
		return sb.toString();
	}
	
	private String getHtmlBodyStart() {
		StringBuilder sb = new StringBuilder();
		
		sb.append( "<body class=\"console\">\n");
		sb.append( "  <div class=\"buttons\">\n");
		sb.append( "    <button class=\"button\" onclick=\"$('body').attr('class', 'console')\">Console</button>");
		sb.append( "<button class=\"button\" onclick=\"$('body').attr('class', 'madog')\">Prison</button>\n");
		sb.append( "  </div>\n");
		
		return sb.toString();
	}
	
	@SuppressWarnings("unused")
	private String getHtmlBodyEnd() {
		StringBuilder sb = new StringBuilder();

		sb.append( "</body>\n" );
		sb.append( "</html>\n" );

		return sb.toString();
	}
	
	private String getGlobalCss() {
		StringBuilder sb = new StringBuilder();
		
		sb.append( "<style>\n" )
		
			.append( "body {\n")
			.append( "  font-family: ui-monospaced, monospaced, \"Lucida Console\", \"Courier New\", \"Courier\";\n")
			.append( "  white-space: pre;\n")
			.append( "}\n");
		
		
		sb.append( "body.console { color: white; background-color: #000000; }\n");
		sb.append( "body.console .cc0 { color: black; }\n");
		sb.append( "body.console .cc1 { color: #0037da; }\n");
		sb.append( "body.console .cc2 { color: #13a10e; }\n");
		sb.append( "body.console .cc3 { color: #3a96dd; }\n");
		sb.append( "body.console .cc4 { color: #c50f1f; }\n");
		sb.append( "body.console .cc5 { color: #881798; }\n");
		sb.append( "body.console .cc6 { color: #c19c00; }\n");
		sb.append( "body.console .cc7 { color: #cccccc; }\n");
		sb.append( "body.console .cc8 { color: #767676; }\n");
		sb.append( "body.console .cc9 { color: #3b78ff; }\n");
		sb.append( "body.console .cca { color: #16c60c; }\n");
		sb.append( "body.console .ccb { color: #61d6d6; }\n");
		sb.append( "body.console .ccc { color: #e74856; }\n");
		sb.append( "body.console .ccd { color: #b4009e; }\n");
		sb.append( "body.console .cce { color: #f9f1a5; }\n");
		sb.append( "body.console .ccf { color: #f2f2f2; }\n");
		
		
		sb.append( "body.madog { color: white; background-color: #171717; }\n");
		sb.append( "body.madog .cc0 { color: black; }\n");
		sb.append( "body.madog .cc1 { color: darkblue; }\n");
		sb.append( "body.madog .cc2 { color: #0F0; }\n");
		sb.append( "body.madog .cc3 { color: #ff8f00; }\n");
		sb.append( "body.madog .cc4 { color: darkred; }\n");
		sb.append( "body.madog .cc5 { color: #F0F; }\n");
		sb.append( "body.madog .cc6 { color: orange; }\n");
		sb.append( "body.madog .cc7 { color: #aaa6a6; }\n");
		sb.append( "body.madog .cc8 { color: darkgray; }\n");
		sb.append( "body.madog .cc9 { color: yellow; }\n");
		sb.append( "body.madog .cca { color: #00c3ff; }\n");
		sb.append( "body.madog .ccb { color: cyan; }\n");
		sb.append( "body.madog .ccc { color: red; }\n");
		sb.append( "body.madog .ccd { color: magenta; }\n");
		sb.append( "body.madog .cce { color: yellow; }\n");
		sb.append( "body.madog .ccf { color: white; }\n");
		
		//sb.append( "body.console \n");
		
		sb.append( ".buttons { \n");
		sb.append( "  position: fixed;\n" );
		sb.append( "  top: 0;\n" );
		sb.append( "  right: 0;\n" );
		sb.append( "  width: 150px;\n" );
		sb.append( "  height: 34px;\n" );
		sb.append( "} \n");
		sb.append( ".button { \n");
		sb.append( "  display: inline-block; \n");
		sb.append( "} \n");

		
//		for (ColorMaps colorMap : ColorMaps.values()) {
//			if ( colorMap.getCss() != null ) {
//				
//				sb.append( colorMap.getCss() ).append( "\n" );
//			}
//		}
		
		
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
