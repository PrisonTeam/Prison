package tech.mcprison.prison.discord;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.util.Text;

/**
 * <p>This class provides a way to post text to https://paste.helpch.at/ so 
 * it can be used to help provide support when needed.
 * </p>
 * 
 * <p>The site paste.helpch.at is a hastebin site so it following the standard
 * hastebin protocols in uploading content.
 * </p>
 * 
 * <p>Sending a POST request to https://hasteUrl/documents, with the request body as the 
 * paste content it will return the id of the paste, which is then appended to the 
 * site's URL.  
 * </p>
 * 
 * 
 * <p>For example:</p> 
 * 
 * <p>Posting to: <pre>https://paste.helpch.at/documents</pre></p>
 * 
 * <p>Provides a result (as an example):</p>
 * <pre>{"key":"utozikecag"}</pre>
 * 
 * <p>Then use that to construct the actual URL to access the paste:</p>
 * <pre>https://paste.helpch.at/utozikecag</pre>
 * 
 *
 * <p>References:
 * </p>
 * 
 * https://github.com/kaimu-kun/hastebin.java/blob/master/src/me/kaimu/hastebin/Hastebin.java
 * 
 * https://www.spigotmc.org/threads/making-a-paste-in-pastebin-hastebin.366093/#post-3353670
 *  
 *  <pre>
	JsonObject object = GSON.fromJson(new InputStreamReader(connection.getInputStream(), Charsets.UTF_8), JsonObject.class);
	String url = "https://hastebin.com/" + object.get("key").getAsString();
	</pre>
 *
 *
 */
public class PrisonPasteChat {
	
	private static final String hastebinURL = "https://paste.helpch.at/";
	private static final String hastebinURLdocuments = "documents";
	private static final String hastebinURLraw = "raw/";
//	private static final String hastebinURL = "https://hastebin.com/documents";
	
	public static final int HASTEBIN_MAX_LENGTH = 390000;
	private static final String SUBMISSION_SIZE_PLACEHOLDER = "{submissionSizeInBytes-------}";
	
	private String supportName;
	private TreeMap<String, String> supportURLs;
	
	public PrisonPasteChat( String supportName, TreeMap<String, String> supportURLs ) {
		super();
		
		this.supportName = supportName;
		this.supportURLs = supportURLs;
	}
	
	public String post( String text ) {
		return post( text, false, false );
	}

	/**
	 * <p>This posts the text, but during the cleaning process it keeps the
	 * color codes.  This is good for configuration files.  This is bad for
	 * processed, and already translated output and log files.
	 * </p>
	 * 
	 * @param text
	 * @return
	 */
	public String postKeepColorCodes( String text ) {
		return post( text, false, true );
	}
	
	public String post( String text, boolean raw, boolean keepColorCodes ) {
		String results = null;
		
		try {
			
			String cleanedText = cleanText( text, keepColorCodes );
			
			cleanedText = addHeaders( cleanedText );
			
			results = postPaste( cleanedText, raw );
		}
		catch (Exception e) {
			Output.get().logInfo( "PrisonPasteChat: Failed to paste to paste.helpch.at. " +
					"You'll have to manually paste the data. [%s]", e.getMessage() );
		}
		
		return results;
	}

	/**
	 * <p>This function will clean up the text by removing a lot of the 
	 * junk color codes (the unicode fails) and also remove translated color
	 * codes that have already been converted.
	 * </p>
	 * 
	 * <p>The option keepcolorCodes will preserve the & color codes.  It does this
	 * by first converting them to an arbitrary placeholder, then the text is
	 * cleaned, then the temporary placeholders are converted back to &.
	 * </p>
	 * 
	 * @param text
	 * @param keepColorCodes
	 * @return
	 */
	protected String cleanText( String text, boolean keepColorCodes )
	{
		String cleanedText = text;
		
		if ( keepColorCodes ) {
			cleanedText = cleanedText.replaceAll("&", "^amp^");
		}
		
		cleanedText = Text.stripColor( cleanedText );

		
		cleanedText = 
				cleanedText.replaceAll( 
						"\\[m|\\[0;30;1m|\\[0;31;1m|\\[0;32;1m|\\[0;33;1m|\\[0;34;1m|\\[0;35;1m|" +
						"\\[0;36;1m|\\[0;37;1m|\\[0;38;1m|\\[0;39;1m|" +
						"\\[0;30;22m|\\[0;31;22m|\\[0;32;22m|\\[0;33;22m|\\[0;34;22m|\\[0;35;22m|" +
						"\\[0;36;22m|\\[0;37;22m|\\[0;38;22m|\\[0;39;22m" +
						"",
						"" );
		
		if ( keepColorCodes ) {
			cleanedText = cleanedText.replaceAll("\\^amp\\^", "&");
		}
		
		// Max length of 400,000 characters:  Trim and send first section only.
		if ( cleanedText.length() > HASTEBIN_MAX_LENGTH ) {
			cleanedText = cleanedText.substring( 0, HASTEBIN_MAX_LENGTH );
		}
		
		
//		// Injects the size back in to the text without changing the total length of the text:
//		int size = cleanedText.length();
//		DecimalFormat dFmt = new DecimalFormat("#,##0");
//		String sizeString = (dFmt.format( size ) + " bytes                        ")
//									.substring( 0, SUBMISSION_SIZE_PLACEHOLDER.length() );
//		
//		cleanedText = cleanedText.replace( SUBMISSION_SIZE_PLACEHOLDER, sizeString );
		
		return cleanedText;
	}
	
	private String addHeaders( String text ) {
		
		text = setupSupportPrefix() + text;
		
		// Injects the size back in to the text without changing the total length of the text:
		int size = text.length();
		
		DecimalFormat dFmt = new DecimalFormat("#,##0");
		String sizeString = (dFmt.format( size ) + " bytes                        ")
									.substring( 0, SUBMISSION_SIZE_PLACEHOLDER.length() );
		
		text = text.replace( SUBMISSION_SIZE_PLACEHOLDER, sizeString );
		
		return text;
	}
	
	private String setupSupportPrefix() {
		
		StringBuilder sb = new StringBuilder();
		
		SimpleDateFormat sdFmt = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		
		sb.append( "Support Name:     " ).append( supportName ).append( "\n" );
		sb.append( "Submission Date:  " ).append( sdFmt.format( new Date() ) ).append( "\n" );
		sb.append( "Submission Size:  " ).append( SUBMISSION_SIZE_PLACEHOLDER ).append( "\n" );
		
		sb.append( "\n" );
		
		Set<String> urlKeys = getSupportURLs().keySet();
		for ( String key : urlKeys ) {
			sb.append( padSpaces(key) ).append( getSupportURLs().get( key ) ).append( "\n" );
		}
		
		sb.append( "\n" );
		
		return sb.toString();
	}
	
	private String padSpaces( String keyName ) {
		return (keyName + "                     ").substring( 0,18 );
	}
	
	private String postPaste( String text, boolean raw ) 
			throws IOException {
		String results = null;
		String rawJson = null;
		
		byte[] postData = text.getBytes(StandardCharsets.UTF_8);
		int postDataLength = postData.length;

		String requestURL = hastebinURL + hastebinURLdocuments;
		URL url = new URL(requestURL);
		
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setInstanceFollowRedirects(false);
		
		conn.setRequestMethod("POST");
		conn.setRequestProperty("User-Agent", "Hastebin Java Api");
		conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
		conn.setUseCaches(false);

		try (
				DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			) {
			
			wr.write(postData);
			
			try (
					BufferedReader reader = new BufferedReader( 
									new InputStreamReader( conn.getInputStream()));
				) {
				
				rawJson = reader.readLine();
			}
		} 
		catch (IOException e) {
			Output.get().logError( 
					String.format( "Failure in sending paste. %s ", e.getMessage()) , e );
		}
		
		
		if ( rawJson != null ) {
			//Output.get().logInfo( "### rawJson : " + rawJson );
			// ### rawJson : {"key":"utozikecag"}
			
			Gson gson = new Gson();
			JsonObject object = gson.fromJson( rawJson, JsonObject.class );
			
			results = hastebinURL + 
					( raw ? hastebinURLraw : "" ) +
						object.get("key").getAsString();
					
		}
		
		return results;
	}
	
	public TreeMap<String, String> getSupportURLs() {
		return supportURLs;
	}
	public void setSupportURLs( TreeMap<String, String> supportURLs ) {
		this.supportURLs = supportURLs;
	}
	
}
