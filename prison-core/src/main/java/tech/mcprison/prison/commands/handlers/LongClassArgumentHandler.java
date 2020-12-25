package tech.mcprison.prison.commands.handlers;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.CommandArgument;
import tech.mcprison.prison.commands.TransformError;
import tech.mcprison.prison.internal.CommandSender;

public class LongClassArgumentHandler 
		extends NumberArgumentHandler<Long> {

    public LongClassArgumentHandler() {
    }

    @Override 
    public Long transform(CommandSender sender, CommandArgument argument, String value)
    		throws TransformError {
    	Long results = null;
    	
    	if ( value != null ) {
    		try {
    			results = Long.parseLong(value);
    		} catch (NumberFormatException e) {
    			throw new TransformError(
    					Prison.get().getLocaleManager().getLocalizable("numberParseError")
    					.withReplacements(value).localizeFor(sender));
    		}
    	}
        
        return results;
    }


}
