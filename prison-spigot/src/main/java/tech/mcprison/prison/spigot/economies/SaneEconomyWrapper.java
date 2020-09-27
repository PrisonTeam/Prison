package tech.mcprison.prison.spigot.economies;

import org.appledash.saneeconomy.economy.EconomyManager;
import org.appledash.saneeconomy.economy.economable.EconomablePlayer;
import org.bukkit.Bukkit;

import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.output.Output;

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
    	double result = 0;
        
    	try {
    		result = economyManager.getBalance(toEconomablePlayer(player));
    	}
	    catch ( Exception e ) {
	    	Output.get().logError( "Failed to get SaneEconomy balance. " +
	    			"Using API v0.15.0. You may need to downgrade. ", e );
	    }

        return result;
    }

    public void setBalance(Player player, double amount) {
    	try {
    		economyManager.setBalance(toEconomablePlayer(player), amount);
    	}
	    catch ( Exception e ) {
	    	Output.get().logError( "Failed to set SaneEconomy balance. " +
	    			"Using API v0.15.0. You may need to downgrade. ", e );
	    }
    }

    private EconomablePlayer toEconomablePlayer(Player player) {
        return new EconomablePlayer(Bukkit.getOfflinePlayer(player.getUUID()));
    }
}
