package tech.mcprison.prison.spigot.economies;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.EconomyResponse;
import tech.mcprison.prison.internal.Player;

public class VaultEconomyWrapper
{
	private net.milkbowl.vault.economy.Economy economy = null;
	
	public VaultEconomyWrapper() {
		super();
		
		RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> economyProvider =
				Bukkit.getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}
	}
	
	public boolean isEnabled()
	{
		return economy != null && economy.isEnabled();
	}

	public String getName() {
		return economy == null ? "not enabled" : economy.getName();
	}
	
    @SuppressWarnings( "deprecation" )
	public double getBalance(Player player) {
    	double results = 0;
        if (economy != null) {
        	if ( economy.hasBankSupport() ) {
        		EconomyResponse bnkBal = economy.bankBalance( player.getName() );
        		if ( bnkBal.transactionSuccess() ) {
        			results = bnkBal.balance;
        		}
        	} else {
        		results = economy.getBalance(player.getName());
        	}
        }
        
        return results;
    }

    @SuppressWarnings( "deprecation" )
	public void setBalance(Player player, double amount) {
        if (economy != null) {
        	if ( economy.hasBankSupport() ) {
        		economy.bankWithdraw(player.getName(), getBalance(player));
        		economy.bankDeposit(player.getName(), amount);
        	} else {
        		economy.withdrawPlayer( player.getName(), getBalance( player ) );
        		economy.depositPlayer( player.getName(), amount );
        	}
        }
    }

    @SuppressWarnings( "deprecation" )
	public void addBalance(Player player, double amount) {
        if (economy != null) {
        	if ( economy.hasBankSupport() ) {
        		economy.bankDeposit(player.getName(), amount);
        	} else {
        		economy.depositPlayer( player.getName(), amount );
        	}
        }
    }

    @SuppressWarnings( "deprecation" )
	public void removeBalance(Player player, double amount) {
        if (economy != null) {
        	if ( economy.hasBankSupport() ) {
        		economy.bankWithdraw(player.getName(), amount);
        	} else {
        		economy.withdrawPlayer( player.getName(), amount );
        	}
        }
    }

    @SuppressWarnings( "deprecation" )
	public boolean canAfford(Player player, double amount) {
    	boolean results = false;
    	if (economy != null) {
        	if ( economy.hasBankSupport() ) {
        		results = economy.bankHas(player.getName(), amount).transactionSuccess();
        	} else {
        		results = economy.has(player.getName(), amount);
        	}
    	}
        return results;
    }

}
