package tech.mcprison.prison.spigot.economies;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.spiget.BluesSpigetSemVerComparator;

/**
 * <p>Prison does not support banks since the only way to identify a bank is through
 * the player's name.  Not even sure if it was implemented correctly in the past.
 * If there is a need to reenable this code, then bank support can be added
 * back in the future, and at that time, it can be verified to be correct.
 * I honestly don't think it is correct the way it was being used before.
 * </p>
 *
 */
public class VaultEconomyWrapper
{
	private net.milkbowl.vault.economy.Economy economy = null;
	
	private String providerName;
	private String vaultVersion;
	
	private boolean preV1dot4 = false;
	
	public VaultEconomyWrapper(String providerName ) {
		super();
		
		this.providerName = providerName;
		
		RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> economyProvider =
				Bukkit.getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
			
			
			this.vaultVersion = 
					Bukkit.getPluginManager().getPlugin( getProviderName() )
								.getDescription().getVersion();
			
			this.preV1dot4 = ( new BluesSpigetSemVerComparator().compareTo( getVaultVersion(), 
									"1.4.0" ) < 0 );
			
			Output.get().logInfo( "### VaultEconomyWrapper : vaultVersion = " + getVaultVersion() + 
					"  is pre1_4= " + isPreV1_4() );
		}
	}
	
	
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName( String providerName ) {
		this.providerName = providerName;
	}

	public String getVaultVersion() {
		return vaultVersion;
	}
	public void setVaultVersion( String vaultVersion ) {
		this.vaultVersion = vaultVersion;
	}

	public boolean isPreV1_4() {
		return preV1dot4;
	}
	public void setPreV1_4( boolean preV1_4 ) {
		this.preV1dot4 = preV1_4;
	}


	public boolean isEnabled()
	{
		return economy != null && economy.isEnabled();
	}

	public String getName() {
		return economy == null ? "not enabled" : economy.getName();
	}
	
	private OfflinePlayer getOfflinePlayer(Player player) {
		
		return SpigotUtil.getBukkitOfflinePlayer( player.getUUID() );
	}
	
	
    @SuppressWarnings( "deprecation" )
	public double getBalance(Player player) {
    	double results = 0;
        if (economy != null) {
        	if ( isPreV1_4() ) {
        		results = economy.getBalance(player.getName());
        	}
        	else {
        		OfflinePlayer oPlayer = getOfflinePlayer( player );
        		if ( oPlayer == null ) {
        			Output.get().logInfo( "VaultEconomyWrapper.getBalance(): Error: " +
        					"Cannot get economy for player %s so returning a value of 0.", 
        					player.getName());
        			results = 0;
        		}
        		else {
        			results = economy.getBalance(oPlayer);
        		}
        		
        	}
//        	if ( economy.hasBankSupport() ) {
//
//        		EconomyResponse bnkBal = economy.bankBalance( player.getName() );
//        		if ( bnkBal.transactionSuccess() ) {
//        			results = bnkBal.balance;
//        		}
//        			
//        		
//        	} else {
//        	results = economy.getBalance(player.getName());
//        	}
        }
        
        return results;
    }

    @SuppressWarnings( "deprecation" )
	public boolean setBalance(Player player, double amount) {
    	boolean results = false;
        if (economy != null) {
        	
           	if ( isPreV1_4() ) {
           		economy.withdrawPlayer( player.getName(), getBalance( player ) );
           		economy.depositPlayer( player.getName(), amount );
           		results = true;
        	}
        	else {
        		OfflinePlayer oPlayer = getOfflinePlayer( player );
        		if ( oPlayer == null ) {
        			Output.get().logInfo( "VaultEconomyWrapper.setBalance(): Error: " +
        					"Cannot get economy for player %s so cannot set balance to %s.", 
        					player.getName(), Double.toString( amount ));
        		}
        		else {
        			economy.withdrawPlayer( oPlayer, getBalance( player ) );
        			economy.depositPlayer( oPlayer, amount );
        			results = true;
        		}
        	}
        	
//        	if ( economy.hasBankSupport() ) {
//        		economy.bankWithdraw(player.getName(), getBalance(player));
//        		economy.bankDeposit(player.getName(), amount);
//        	} else {
//        		economy.withdrawPlayer( player.getName(), getBalance( player ) );
//        		economy.depositPlayer( player.getName(), amount );
//        	}
        }
        return results;
    }

    @SuppressWarnings( "deprecation" )
	public boolean addBalance(Player player, double amount) {
    	boolean results = false;
        if (economy != null) {
        	if ( isPreV1_4() ) {
        		economy.depositPlayer( player.getName(), amount );
           		results = true;
        	}
        	else {
        		OfflinePlayer oPlayer = getOfflinePlayer( player );
        		if ( oPlayer == null ) {
        			Output.get().logInfo( "VaultEconomyWrapper.addBalance(): Error: " +
        					"Cannot get economy for player %s so cannot add a balance of %s.", 
        					player.getName(), Double.toString( amount ));
        		}
        		else {
        			economy.depositPlayer( oPlayer, amount );
        			results = true;
        		}
        	}
//        	if ( economy.hasBankSupport() ) {
//        		economy.bankDeposit(player.getName(), amount);
//        	} else {
//        		economy.depositPlayer( player.getName(), amount );
//        	}
        }
        return results;
    }

    @SuppressWarnings( "deprecation" )
	public boolean removeBalance(Player player, double amount) {
    	boolean results = false;
    	if (economy != null) {
    		if ( isPreV1_4() ) {
    			economy.withdrawPlayer( player.getName(), amount );
    			results = true;
    		}
    		else {
    			OfflinePlayer oPlayer = getOfflinePlayer( player );
    			if ( oPlayer == null ) {
    				Output.get().logInfo( "VaultEconomyWrapper.removeBalance(): Error: " +
    						"Cannot get economy for player %s so cannot remove a balance of %s.", 
    						player.getName(), Double.toString( amount ));
    			}
    			else {
    				economy.withdrawPlayer( oPlayer, amount );
    				results = true;
    			}
    		}

//        if (economy != null) {
//        	if ( economy.hasBankSupport() ) {
//        		economy.bankWithdraw(player.getName(), amount);
//        	} else {
//        		economy.withdrawPlayer( player.getName(), amount );
//        	}
//        }
    	}
    	return results;
    }

    @SuppressWarnings( "deprecation" )
	public boolean canAfford(Player player, double amount) {
    	boolean results = false;
    	if (economy != null) {
       		if ( isPreV1_4() ) {
       			results = economy.has(player.getName(), amount);
    		}
    		else {
    			OfflinePlayer oPlayer = getOfflinePlayer( player );
    			if ( oPlayer == null ) {
    				Output.get().logInfo( "VaultEconomyWrapper.canAfford(): Error: " +
    						"Cannot get economy for player %s so cannot tell if " +
    						"player can afford the amount of %s.", 
    						player.getName(), Double.toString( amount ));
    			}
    			else {
    				results = economy.has(oPlayer, amount);
    			}
    		}

//        	if ( economy.hasBankSupport() ) {
//        		results = economy.bankHas(player.getName(), amount).transactionSuccess();
//        	} else {
//        		results = economy.has(player.getName(), amount);
//        	}
    	}
        return results;
    }

}
