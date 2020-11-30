package tech.mcprison.prison.commands.handlers;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.CommandArgument;
import tech.mcprison.prison.commands.TransformError;
import tech.mcprison.prison.internal.CommandSender;

public class IntegerClassArgumentandler 
		extends NumberArgumentHandler<Integer> {


    public IntegerClassArgumentandler() {
    }

    @Override public Integer transform(CommandSender sender, CommandArgument argument, String value)
    		throws TransformError {
    	Integer results = null;
    
    	if ( value != null ) {
    		
    		//value = value.replaceAll("$|%", "");
    		if ( value.trim().length() > 0 ) {
    			try {
    				results = Integer.parseInt(value);
    			} catch (NumberFormatException e) {
    				throw new TransformError(
    						Prison.get().getLocaleManager().getLocalizable("numberParseError")
    						.withReplacements(value).localizeFor(sender));
    			}
    		}
    	}
    	return results;
    }
}
