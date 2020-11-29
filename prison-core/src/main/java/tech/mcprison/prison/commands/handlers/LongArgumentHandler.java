package tech.mcprison.prison.commands.handlers;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.CommandArgument;
import tech.mcprison.prison.commands.TransformError;
import tech.mcprison.prison.internal.CommandSender;

public class LongArgumentHandler 
			extends NumberArgumentHandler<Long> {

    public LongArgumentHandler() {
    }

    @Override 
    public Long transform(CommandSender sender, CommandArgument argument, String value)
        throws TransformError {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new TransformError(
                Prison.get().getLocaleManager().getLocalizable("numberParseError")
                    .withReplacements(value).localizeFor(sender));
        }
    }

}
