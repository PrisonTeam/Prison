package tech.mcprison.prison.bombs;

import java.text.DecimalFormat;

import tech.mcprison.prison.Prison;

public class MineBombMessages {

	static public String mineBombsCoolDownMsg( int cooldownTicks ) {
		
		double cooldownSeconds = cooldownTicks / 20.0f;
		DecimalFormat dFmt = Prison.get().getDecimalFormat( "0.0" );
		
		return Prison.get().getLocaleManager()
				.getLocalizable( "core_minebombs__cooldown_delay" )
				.withReplacements( 
						dFmt.format(cooldownSeconds) )
				.localize();
	}
}
