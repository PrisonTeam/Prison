package tech.mcprison.prison.integration;

/**
 * <p>WorldGuard Integration is needed because there are two major versions of
 * WorldGuard and WorldEdit that needs to be supported.  This is required due
 * to the wide range of spigot versions that are supported.
 * </p>
 * 
 * <p>The two different versions WorldGuard, and WorldEdit, may have different
 * command sets and parameters, not to mention different class names and 
 * possibly different package names too.  So these integrations are to
 * provide a common interface in to any platform, and any version. Hopefully! lol
 * </p>
 * 
 * <p>Version 6.x is for minecraft versions 1.12.2 and below.</p>
 * 
 * <p>Version 7.x is for minecraft versions 1.13.0 and up.  
 * </p>
 * 
 * <p>It is stated that minecraft implementations for spigot have significant 
 * changes from one version to the next, so one version of WorldEdit and 
 * WorldGuard may not support any of the older versions.  Due to these restrictions
 * and warnings, carefully make sure you download the version of WorldGuard and
 * WorldEdit that matches the server you are running.  All v6.x are not the same, nor 
 * are all v7.x the same.  The best we can do is to make sure you are using the
 * correct major version for the version of minecraft that you are running on 
 * your server; the correct minor version is on you to get correct.
 * </p>
 *
 */
public class WorldGuardIntegration
	extends IntegrationCore {
	
	private boolean integrated = false;

	public WorldGuardIntegration( String keyName, String providerName ) {
		super( keyName, providerName, IntegrationType.WORLDGUARD );
	}

	public boolean isIntegrated() {
		return integrated;
	}
	public void setIntegrated( boolean integrated ) {
		this.integrated = integrated;
	}
	
	
}
