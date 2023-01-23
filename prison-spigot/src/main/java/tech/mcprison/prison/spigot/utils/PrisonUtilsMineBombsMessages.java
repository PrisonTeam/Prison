package tech.mcprison.prison.spigot.utils;

import java.text.DecimalFormat;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.spigot.SpigotPrison;

public abstract class PrisonUtilsMineBombsMessages 
	extends PrisonUtilsMineBombsTasks
{
	public PrisonUtilsMineBombsMessages() {
		super();
	}

	protected void exampleMsg( CommandSender sender, String mineName ) {
		SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_minebombs__" )
				.withReplacements( 
						mineName )
				.sendTo( sender );
	}
	
	protected String exampleReturnMsg( String mineTagName ) {
		return SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_minebombs__" )
				.withReplacements( 
						mineTagName )
				.localize();
	}
	

	protected void mineBombsCoolDownMsg( CommandSender sender, double cooldownTicks ) {
		
		double cooldownSeconds = cooldownTicks / 20.0f;
		DecimalFormat dFmt = Prison.get().getDecimalFormat( "0.0" );
		
		SpigotPrison.getInstance().getLocaleManager()
				.getLocalizable( "spigot_minebombs__cooldown_delay" )
				.withReplacements( 
						dFmt.format(cooldownSeconds) )
				.sendTo( sender );
	}
	
}
