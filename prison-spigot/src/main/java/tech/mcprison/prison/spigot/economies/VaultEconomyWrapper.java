package tech.mcprison.prison.spigot.economies;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

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
	
	public String getName() {
		return economy.getName();
	}
	
    @SuppressWarnings( "deprecation" )
	public double getBalance(Player player) {
        if (economy == null) {
            return 0;
        }
        return economy.getBalance(player.getName());
    }

    public void setBalance(Player player, double amount) {
        if (economy == null) {
            return;
        }
        economy.bankWithdraw(player.getName(), getBalance(player));
        economy.bankDeposit(player.getName(), amount);
    }

    public void addBalance(Player player, double amount) {
        if (economy == null) {
            return;
        }
        economy.bankDeposit(player.getName(), amount);
    }

    public void removeBalance(Player player, double amount) {
        if (economy == null) {
            return;
        }
        economy.bankWithdraw(player.getName(), amount);
    }

    public boolean canAfford(Player player, double amount) {
        return economy != null && economy.bankHas(player.getName(), amount).transactionSuccess();
    }

}
