package tech.mcprison.prison.output;

import tech.mcprison.prison.internal.CommandSender;

public class SupportHyperLinkComponent
	extends DisplayComponent {

    protected String text;

    public SupportHyperLinkComponent(String text, Object... args) {
        this.text = Output.stringFormat(text, args);
    }

    @Override public String text() {
        return text;
    }

    @Override public void send(CommandSender sender) {
        
    	// Do not send this message to the player... it is only for support documents:
    	//sender.sendMessage(text());
    }
}
