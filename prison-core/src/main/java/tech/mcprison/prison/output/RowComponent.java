package tech.mcprison.prison.output;

import tech.mcprison.prison.chat.FancyMessage;
import tech.mcprison.prison.internal.CommandSender;

public class RowComponent
		extends DisplayComponent
{
	private FancyMessage fancy;
	
	public RowComponent()
	{
		this.fancy = new FancyMessage("");
	}
	
	public void addFancy(FancyMessage fancyMessage)
	{
		fancy.addFancy(fancyMessage);
	}
	
	public void addTextComponent(String text, Object... args)
	{
		TextComponent tComp = new TextComponent(text, args);
		FancyMessage fm = new FancyMessage(tComp.text);
		
		addFancy( fm );
	}
	
	public FancyMessage getFancy() {
		return fancy;
	}
	
	@Override
	public String text()
	{
		return fancy.toOldMessageFormat();
	}

	@Override
	public void send( CommandSender sender )
	{
		fancy.send( sender );
	}

}
