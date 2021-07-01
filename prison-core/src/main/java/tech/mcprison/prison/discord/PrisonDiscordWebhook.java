package tech.mcprison.prison.discord;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.discord.DiscordWebhook.EmbedObject;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.output.Output.DebugTarget;
import tech.mcprison.prison.util.Text;

/**
 * <p>Please note, that there are max limits for what can be sent.  
 * There is a max of 25 fields, plus a max size limit of 6000 characters
 * on a number of combined fields. Therefore need to be conservative on 
 * what is sent.  Failure to comply to these limits will result in the
 * request being rejected with a 400 exception.
 * </p>
 * https://discord.com/developers/docs/resources/channel#embed-limits
 * 
 *
 */
public class PrisonDiscordWebhook
{
	public static final String prisonWebhook = "https://discord.com/api/webhooks/";
	
	public final String img = "https://cdn.discordapp.com/emojis/460524143205548033.png?v=1";
	
	
	// Discord userName of person submitting webhook: 
	private String userName;
	private String serverName;
	private String serverUrl;
	
	private LinkedHashMap<String, String> fields;
	
	public PrisonDiscordWebhook() {
		
		this.fields = new LinkedHashMap<>();
		
	}
	
	public boolean setup() {
		boolean results = true;
		
		setUserName( "TestWebHook" );
		setServerName( "-ServerName-" );
		setServerUrl( "https://prison.jar-mc.com/" );
		
		if ( getFields().size() == 0 ) {
			
			getFields().put( "prisonVersion", Prison.get().getPlatform().getPluginVersion() );
			getFields().put( "platform", Prison.get().getPlatform().getClass().getSimpleName() );
			getFields().put( "minecraftVersion", Prison.get().getMinecraftVersion() );
			
			Prison.get().displaySystemSettings( getFields() );
		}
		
		return results;
	}
	
	public void send( CommandSender sender, String title, String message, boolean addPrisonStats ) {
		
		int totalSize = 0;
		
		DiscordWebhook webhook = new DiscordWebhook( prisonWebhook + getPwhu() );
	    webhook.setContent( message );
	    webhook.setUsername( getUserName() );
	    webhook.setAvatarUrl( img );
	    webhook.setTts(false); // text-to-speach
	    
	    
	    if ( addPrisonStats ) {
	    	totalSize += addPrisonStats( title, webhook );
	    }

	    
	    if ( Output.get().isDebug( DebugTarget.support ) ) {
	    	Output.get().logDebug( DebugTarget.support, 
	    			"Prison Webhook debug: total fields: %d  total chars: %d  ", 
	    			getFields().size(), totalSize );
	    	
	    	if ( getFields().size() > 25 || totalSize > 6000 ) {
	    		Output.get().logDebug( DebugTarget.support, 
	    				"Prison Webhook debug: total fields: Failure. Max number of " +
	    				"fields is 25. Max number of characters is 6000.  Please reduce " +
	    				"these to bring in to complience and resubmit. " );
	    	}
	    	
	    }
	    
	    String results = submitWebhook( webhook );
	    
	    sender.sendMessage( results );
	    
//	    webhook.addEmbed(new DiscordWebhook.EmbedObject()
//	            .setTitle( title )
//	            .setDescription("Prison's discord help webhook.")
//	            .setColor(Color.RED)
//	            .addField("1st Field", "Inline", true)
//	    		.addField("2nd Field", "Inline", true)
//	    		.addField("3rd Field", "No-Inline", false)
////	    		.setThumbnail("https://kryptongta.com/images/kryptonlogo.png")
//	    		.setFooter("Footer text", "Submitted by " + getUserName() )
////	    		.setImage("https://kryptongta.com/images/kryptontitle2.png")
////	    		.setAuthor("Author Name", "https://kryptongta.com", "https://kryptongta.com/images/kryptonlogowide.png")
//	    		.setUrl( getServerUrl()));
	    
//	    webhook.addEmbed(new DiscordWebhook.EmbedObject()
//	    .setDescription("Just another added embed object!"));
//	    webhook.execute(); //Handle exception
	    
	    
	}
	
	
	private int addPrisonStats( String title, DiscordWebhook webhook ) {
		
		int totalSize = 0;
		
		String desc = "Prison's discord help webhook.";
		String footer = "Footer -Submitted by " + getUserName();
		String footerIcon = img;
		
	    EmbedObject webhookObject = new DiscordWebhook.EmbedObject()
		        .setTitle( title )
		        .setDescription( desc )
		        .setColor(Color.RED)
				.setThumbnail( img )
				.setFooter( footer, footerIcon )
				.setImage( img )
		//		.setAuthor("Author Name", "https://kryptongta.com", "https://kryptongta.com/images/kryptonlogowide.png")
				.setUrl( getServerUrl() );
	    
	    totalSize += title.length() + desc.length() + footer.length() + footerIcon.length();
	    
	    Set<String> keys = getFields().keySet();
	    int count = 0;
	    for ( String key : keys ) {
			String value = getFields().get( key );
			
			webhookObject.addField( key, value, ++count > 3 );
			
			totalSize += key.length() + value.length();
		}
	    
	    webhook.addEmbed( webhookObject );
	    
	    return totalSize;
	}

	private String submitWebhook( DiscordWebhook webhook ) {
		String message = null;
		
		try {
			webhook.execute();
			
			message = "Support information was successfully sent.";
		}
		catch ( IOException e ) {
			
			message = "Support information was unable to be sent. " +
					"Please check the console for more information." ;

			Output.get().logInfo( "%s Error: [%s]", message, e.getMessage() );
		}
		
		return message;
	}
	
	public void sample() {
    	
//    	PrisonDiscordWebhook prisonWebhook = new PrisonDiscordWebhook();
//    	
//    	if ( prisonWebhook.setup() ) {
//    		
//    		ChatDisplay display = displayVersion("ALL");
//    		StringBuilder text = display.toStringBuilder();
//    		List<String> textParts = extractChunks( text, 2000 );
//    		
////    		if ( Output.get().isDebug( DebugTarget.support ) ) {
//    			Output.get().logInfo( 
//    					"Prison support submit version: submit size: %d ", text.length() );
////    		}
////    		
//    		// Able to send:
//
//    		// NOTE: Max message size of 2000 characters:
//    		int i = 0;
//    		for ( String textPart : textParts )
//			{
//				Output.get().logInfo( "### debug - text part %d  length = %d", i, textPart.length() );
//				
//    			prisonWebhook.send( sender, "Prison's Discord Webhook Version", 
//    					textPart, false );
//    			
//			}
//    		
//    		
////    		sender.sendMessage( "Prison's Discord Support:  It appears like Prison is able to " +
////    				"automatically submit support information for you. Please get confirmation from a " +
////    				"staff member prior to submitting any additional information." );
//    	}
//    	else {
//    		sender.sendMessage( "Prison's Discord Support:  Sorry. Unable to automatically submit support " +
//    				"information for you. You will have to manually provide the requested information." );
//    	}
    	
	}
	
	public void sample2() {
	    
//	    @Command(identifier = "prison support submit testMessage", 
//	    		description = "For Prison support purposes only.  Use when instructed to do so. " +
//	    				"This will provide a simple test to confirm if the Prison plugin can " +
//	    				"help submit support information for you. A Prison Discord staff member will " +
//	    				"need to request that you submit this test, and they will confirm if it was " +
//	    				"success or not.", 
//	    				onlyPlayers = false, permissions = "prison.debug" )
//	    public void supportSubmitTestMessage(CommandSender sender,
//	    		@Arg(name = "option", description = "Do not use this option.", def = "" ) String option
//	    		) {
//	    	
//	    	PrisonDiscordWebhook prisonWebhook = new PrisonDiscordWebhook();
//	    	
//	    	if ( prisonWebhook.setup() ) {
//	    		
//	    		// Able to send:
//	    		prisonWebhook.send( sender, "Prison's Discord Webhook Test", 
//	    				"This is a test of the Prison's Discord Webhook.  If this is received, then " +
//	    				"the server admin who requested help can submit additional support information. " +
//	    				"\\n\\nPlease inform the requestor that they can submit additional information. " +
//	    				"This confirms that their hosting services allows outgoing http transactions.",
//	    				!(option != null && "false".equalsIgnoreCase( option )) );
//	    		
////	    		sender.sendMessage( "Prison's Discord Support:  It appears like Prison is able to " +
////	    				"automatically submit support information for you. Please get confirmation from a " +
////	    				"staff member prior to submitting any additional information." );
//	    	}
//	    	else {
//	    		sender.sendMessage( "Prison's Discord Support:  Sorry. Unable to automatically submit support " +
//	    				"information for you. You will have to manually provide the requested information." );
//	    	}
//	    	
//	    }
	    
	
	}
	
	
	public List<String> extractChunks( StringBuilder text, int maxLength )
	{
		List<String> results = new ArrayList<>();

		int start = 0;
		int end = text.length();

		if ( (end - start) > maxLength ) {
			end = text.lastIndexOf( "\\n", start + maxLength );
		}

		while ( end != -1 ) {

			String str = text.substring( start, end );
			if ( str != null ) {
				str = Text.stripColor( str );
			}
			results.add( str );

			if ( end < text.length() ) {
				start = end + 1;

				if ( start + maxLength > text.length() ) {
					end = text.length();
				}
				else {
					end = text.lastIndexOf( "\\n", start + maxLength );
					if ( end < start ) {
						end = start + maxLength;
					}
				}
			}
			else {
				end = -1;
			}

		}

		return results;
	}
	
	
	private String getPwhu() {
		
		return "859531792649551892";
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName( String userName ) {
		this.userName = userName;
	}

	public String getServerName() {
		return serverName;
	}
	public void setServerName( String serverName ) {
		this.serverName = serverName;
	}

	public String getServerUrl() {
		return serverUrl;
	}
	public void setServerUrl( String serverUrl ) {
		this.serverUrl = serverUrl;
	}

	public java.util.LinkedHashMap<String, String> getFields() {
		return fields;
	}
	public void setFields( java.util.LinkedHashMap<String, String> fields ) {
		this.fields = fields;
	}
	
}
