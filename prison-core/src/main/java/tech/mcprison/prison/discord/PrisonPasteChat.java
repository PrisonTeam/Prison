package tech.mcprison.prison.discord;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.util.Text;

/**
 * Hey, quick question.... 
 * Are there any APIs for https://paste.helpch.at/ to help insert content to a new paste?  
 * That way an app can create a paste and then provide the user with the URL?
 *
 * You can send a POST request to https://hasteUrl/documents, with the request body as the 
 * paste content it will return the id of the paste.
 * 
 * Ok, but could you explain what you mean by hasteUrl since that is not a valid url?  
 * I'm guessing it does not have anything to do with the helpch.at site?
 * 
 * The url of any hastebin.  paste.helpch.at is a hastebin
 * 
 * oh it is? I did not know that.  Ok... that makes sense.  Thanks!
 * 
 * it is indeed. 
 *
 *
 * https://github.com/kaimu-kun/hastebin.java/blob/master/src/me/kaimu/hastebin/Hastebin.java
 * 
 *
 *  https://www.spigotmc.org/threads/making-a-paste-in-pastebin-hastebin.366093/#post-3353670
 *  
 * JsonObject object = GSON.fromJson(new InputStreamReader(connection.getInputStream(), Charsets.UTF_8), JsonObject.class);
String url = "https://hastebin.com/" + object.get("key").getAsString();
 *
 *
 */
public class PrisonPasteChat {
	
	private static final String hastebinURL = "https://paste.helpch.at/";
	private static final String hastebinURLdocuments = "documents";
	private static final String hastebinURLraw = "raw/";
	
//	private static final String hastebinURL = "https://hastebin.com/documents";
	
	
	public PrisonPasteChat() {
		super();
		
	}
	
	public String post ( String text ) {
		return post( text, false );
	}

	public String post( String text, boolean raw ) {
		String results = null;
		
		try {
			
			String cleanedText = Text.stripColor( text );
			
			results = postPaste( cleanedText, raw );
		}
		catch (Exception e) {
			Output.get().logInfo( "PrisonPasteChat: Failed to paste to paste.helpch.at. " +
					"You'll have to manually paste the data. [%s]", e.getMessage() );
		}
		
		return results;
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
			
			Output.get().logInfo( "### rawJson : " + rawJson );
			
			Gson gson = new Gson();
			
			JsonObject object = gson.fromJson( rawJson, JsonObject.class );
//			PrisonPasteChatJson chatResults = gson.fromJson( rawJson, PrisonPasteChatJson.class );
			
			results = hastebinURL + 
					( raw ? hastebinURLraw : "" ) +
						object.get("key").getAsString();
					
		}
		
		return results;
	}
	
}
