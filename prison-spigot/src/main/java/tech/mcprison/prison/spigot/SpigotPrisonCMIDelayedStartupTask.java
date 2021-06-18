package tech.mcprison.prison.spigot;

import tech.mcprison.prison.integration.Integration;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.economies.VaultEconomy;
import tech.mcprison.prison.tasks.PrisonRunnable;

public class SpigotPrisonCMIDelayedStartupTask
	implements PrisonRunnable
{
	public static final int TICKS_PER_SECOND = 20;
	
	public static final long TASK_SUBMIT_DELAY_TICKS = 5 * TICKS_PER_SECOND;
	public static final int MAX_ATTEMPTS = 6;
	
	private int attempts = 0;
	
	private final SpigotPrison prison;
	
	public SpigotPrisonCMIDelayedStartupTask( SpigotPrison prison ) {
		super();
		
		this.prison = prison;
		
	}

	
	public void submit() {
		
		if ( isVaultEconomyIntegrated() ) {
			// It's enabled now, so don't submit, just go ahead and startup prison:
			
			Output.get().logInfo( 
					String.format( "&7Prison Delayed Enablement: &3A Vault economy is available. " +
							"Skipping delayed start. Starting Prison now." ));
			
			prison.onEnableStartup();
		}
		else {
			
			Output.get().logInfo( 
					String.format( "&cNOTICE: &7Prison Delayed Enablement: &3Prison startup has " +
							"been delayed.  " +
							"Waiting for a Vault Economy to be enabled.  " +
							"Attempts: %d of %d.  Submitting task...", 
							attempts, MAX_ATTEMPTS ));

			prison.getScheduler().runTaskLater( this, TASK_SUBMIT_DELAY_TICKS );
		}
		
	}
	
	
	public boolean isVaultEconomyIntegrated() {
		
		Integration integration = new VaultEconomy();
		return prison.isIntegrationRegistered( integration );
	}
	
	@Override
	public void run() {
		
		// First check to see if CMI is active, if it is then start Prison.
		// If this feature is enabled, then it is "assumed" that CMI will be
		// used through Vault, so all we need to see is if vault is loadable as
		// an integration.
		

		if ( isVaultEconomyIntegrated() ) {

			Output.get().logInfo( 
					String.format( "&7Prison Delayed Enablement: &3A Vault economy has been " +
							"detected after %d attempts. " +
							"Trying to start Prison now.", attempts ));
			
			prison.onEnableStartup();
		}
		else {
			
			// Not ready yet.  Resubmit task to run again.
			if ( attempts++ < MAX_ATTEMPTS ) {
				Output.get().logWarn( 
						String.format( "&7Prison Delayed Enablement: &3Waiting for a " +
								"Vault economy to be enabled. " +
								"Attempts: %d of %d.  Resubmitting task to try again.", 
								attempts, MAX_ATTEMPTS ));
				
				submit();
			}
			else {
				Output.get().logError( 
						String.format( "&7Prison Delayed Enablement: &cFailed &cto find an " +
							"active economy through vault after %d attempts. Cannot start Prison.", 
							attempts ));
			}
		}
	}

}
