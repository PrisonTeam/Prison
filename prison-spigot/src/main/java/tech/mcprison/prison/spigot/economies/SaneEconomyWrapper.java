package tech.mcprison.prison.spigot.economies;

import org.appledash.saneeconomy.economy.EconomyManager;
import org.appledash.saneeconomy.economy.economable.EconomablePlayer;
import org.bukkit.Bukkit;

import tech.mcprison.prison.internal.Player;

public class SaneEconomyWrapper
{
	private EconomyManager economyManager;
	
	public SaneEconomyWrapper(String providerName) {
		super();
		
		org.appledash.saneeconomy.SaneEconomy saneEconomy = 
				(org.appledash.saneeconomy.SaneEconomy) 
				Bukkit.getServer().getPluginManager().getPlugin(providerName);
		
		if(saneEconomy != null) {
			this.economyManager = saneEconomy.getEconomyManager();
		}
	}
	
    public double getBalance(Player player) {
        return economyManager.getBalance(toEconomablePlayer(player));
    }

    public void setBalance(Player player, double amount) {
        economyManager.setBalance(toEconomablePlayer(player), amount);
    }

    private EconomablePlayer toEconomablePlayer(Player player) {
        return new EconomablePlayer(Bukkit.getOfflinePlayer(player.getUUID()));
    }
}
