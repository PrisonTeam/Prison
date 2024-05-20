package tech.mcprison.prison.spigot.spiget;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.alerts.Alerts;
import tech.mcprison.prison.output.Output;

public class PrisonSpigetUpdateCallback
		implements org.inventivetalent.update.spiget.UpdateCallback {

	@Override
	public void upToDate() {
		
		String currentVersion = Prison.get().getPlatform().getPluginVersion();
		
		String msg = String.format(
				"&dPrison is up to date!  " +
				"&bCurrent version: &6%s.  " +
				"&7Visit our discord server to get help and find " +
				"pre-release versions. ",
						currentVersion
				);
		
		Output.get().logInfo( msg );
		
		
	}

	@Override
	public void updateAvailable(String newVersion, String downloadUrl, 
			boolean hasDirectDownload ) {

		String currentVersion = Prison.get().getPlatform().getPluginVersion();
		
		String msg = String.format(
				"&dA new version of Prison is now available!  " +
				"&bCurrent version: &6%s  &bNew Version: &6%s&b.  " +
				"&7Go to &lSpigotmc.org&r&7 to download the " +
				"latest release with new features and fixes. :)",
				currentVersion,
                newVersion,
                downloadUrl
				);
		
		Output.get().logInfo( msg );
		
		Alerts.getInstance().sendAlert( msg );
		
	}

}
