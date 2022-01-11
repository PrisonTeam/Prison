package tech.mcprison.prison.spigot.tasks;

import org.bukkit.Bukkit;

import net.milkbowl.vault.economy.Economy;
import tech.mcprison.prison.integration.Integration;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.economies.VaultEconomy;
import tech.mcprison.prison.tasks.PrisonRunnable;

public class SpigotPrisonDelayedStartupTask
	implements PrisonRunnable
{
	public static final int TICKS_PER_SECOND = 20;
	
	public static final long TASK_SUBMIT_DELAY_SECS = 5;
	public static final int MAX_ATTEMPTS = 6;
	
	private int attempts = 0;
	private long cooldownInTicks;
	private int maxAttempts;
	private String targetVaultEconomyName;
	
	private final SpigotPrison prison;
	private Economy econ = null;
	private String econName = null;
	
	public SpigotPrisonDelayedStartupTask( SpigotPrison prison ) {
		super();
		
		this.prison = prison;
		
		this.cooldownInTicks = prison.getConfig().getLong(
						"delayedPrisonStartup.cooldown-sec", 
								TASK_SUBMIT_DELAY_SECS ) * TICKS_PER_SECOND;

		this.maxAttempts = prison.getConfig().getInt(
										"delayedPrisonStartup.max-attempts", MAX_ATTEMPTS );
		
		this.targetVaultEconomyName = prison.getConfig().getString( 
										"delayedPrisonStartup.triggers.vault-economy-name" );
		
	}

	
	public void submit() {
		String vaultEconomyName = inspectVaultEconomy();
		
		if ( isVaultEconomyIntegrated() && 
				econ != null && econ.isEnabled() &&
				vaultEconomyName != null &&
				vaultEconomyName.equalsIgnoreCase( targetVaultEconomyName )
				 ) {
			
			// It's enabled now, so don't submit, just go ahead and startup prison:
			
			Output.get().logInfo( 
					String.format( "&7Prison Delayed Enablement: &3A Vault economy is available. " +
							"Skipping delayed start. Starting Prison now." ));
			
			prison.onEnableStartup();
		}
		else {
			
			submitTask();
		}
		
	}


	private void submitTask()
	{
		Output.get().logInfo( 
				String.format( "&cNOTICE: &7Prison Delayed Enablement: &3Prison startup has " +
						"been delayed.  " +
						"Waiting for a Vault Economy to be enabled.  " +
						"Attempts: %d of %d.  Submitting task...", 
						attempts, maxAttempts ));

		prison.getScheduler().runTaskLater( this, cooldownInTicks );
	}
	
	
	public boolean isVaultEconomyIntegrated() {
		
		Integration integration = new VaultEconomy();
		
		return prison.isIntegrationRegistered( integration ) && 
				prison.isPluginEnabled( integration.getProviderName() );
	}
	
	
    
    public String inspectVaultEconomy() {
    	String results = null;
    	
        if ( econName == null && Bukkit.getPluginManager().getPlugin("Vault") != null && 
        		Bukkit.getServicesManager().getRegistration(Economy.class) != null ) {
           
        	econ = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
        	
        	if ( econ != null ) {
        		econName = econ.getClass().getSimpleName();
        		results = econName;
        		
        		if ( prison.getConfig().isBoolean( "delayedPrisonStartup.inspect-vault" ) ) {
        			
        			String message = String.format(  
        					"Inspect Vault Economy: %s  Use '%s' with " +
        							"'delayedPrisonStartup.triggers-vault-economy-name' ", 
        					econ.isEnabled() ? "enabled" : "disabled",
        							econ.getClass().getSimpleName()
        					);
        			
        			Output.get().logInfo( message );
        		}
        	}
        }
        else if ( econName != null ) {
        	results = econName;
        }
        
        if ( results == null ) {
        	Output.get().logInfo( "Inspect Vault Economy: Failed." );
        }
        
        return results;
    }
	
	@Override
	public void run() {
		
		// First check to see if CMI is active, if it is then start Prison.
		// If this feature is enabled, then it is "assumed" that CMI will be
		// used through Vault, so all we need to see is if vault is loadable as
		// an integration.
		

		String vaultEconomyName = inspectVaultEconomy();
		
		if ( isVaultEconomyIntegrated() && 
				econ != null && econ.isEnabled() &&
				vaultEconomyName != null &&
				vaultEconomyName.equalsIgnoreCase( targetVaultEconomyName )
				 ) {

			Output.get().logInfo( 
					String.format( "&7Prison Delayed Enablement: &3The Vault Economy '%s' has been " +
							"detected after %d attempts. " +
							"Trying to start Prison now.", targetVaultEconomyName, attempts ));
			
			prison.onEnableStartup();
		}
		else {
			
			// Not ready yet.  Resubmit task to run again.
			if ( attempts++ < maxAttempts ) {
				Output.get().logWarn( 
						String.format( "&7Prison Delayed Enablement: &3Waiting for a " +
								"Vault economy to be enabled. " +
								"Attempts: %d of %d.  Resubmitting task to try again.", 
								attempts, maxAttempts ));
				
				submitTask();
			}
			else {
				Output.get().logError( 
						String.format( "&7Prison Delayed Enablement: &cFailed &cto find an " +
							"active Vault Economy Named '%s' after %d attempts. Cannot start Prison.", 
							targetVaultEconomyName, attempts ));
				
				prison.onEnableFail();
			}
		}
	}

}
