package tech.mcprison.prison.commands.handlers;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.CommandArgument;
import tech.mcprison.prison.commands.TransformError;
import tech.mcprison.prison.internal.CommandSender;

public class DoubleClassArgumentHandler extends NumberArgumentHandler<Double> {

    public DoubleClassArgumentHandler() {
    }

    @Override 
    public Double transform(CommandSender sender, CommandArgument argument, String value)
    		throws TransformError {
    	Double results = null;
    
    	if ( value != null ) {
    		
    		value = value.replaceAll( "$|%", "" );
    		if ( value.trim().length() > 0 ) {
    			try {
    				results = Double.parseDouble(value);
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
