package tech.mcprison.prison;

import tech.mcprison.prison.internal.CommandSender;

public class PrisonCommandMessages
{
	protected void coreDebugTestLocaleseMsg( CommandSender sender ) {
		Prison.get().getLocaleManager()
				.getLocalizable( "core_prison_utf8_test" )
				.setFailSilently()
				.sendTo( sender );
	}
}
