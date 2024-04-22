package tech.mcprison.prison.spigot.economies;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import com.edwardbelt.edprison.EdPrison;
import com.edwardbelt.edprison.api.models.EconomyModel;
import com.edwardbelt.edprison.utils.EconomyUtils;

import tech.mcprison.prison.internal.Player;

public class EdPrisonEconomyWrapper {

	
	private EconomyModel economy;
	
	private List<String> allEconomies;
	
	
	public EdPrisonEconomyWrapper( String providerName ) {
		super();
		
		this.allEconomies = new ArrayList<>();
		
		Bukkit.getPluginManager().getPlugin( providerName );
		
		
		if( Bukkit.getPluginManager().isPluginEnabled( providerName )) {
		    EdPrison edPrisonPlugin = (EdPrison) Bukkit.getPluginManager().getPlugin( providerName );
		    EconomyModel edEconomy = edPrisonPlugin.getApi().getEconomyApi();

		    this.economy = edEconomy;

		    List<String> allEconos = EconomyUtils.getAllEconomies();
		    
		    this.allEconomies.addAll( allEconos );

		}
	}
	
	
	public boolean isEnabled() {
		return economy != null;
	}
	
	
	public boolean supportedCurrency( String currencyName ) {
		boolean supported = false;
		
		if ( isEnabled() && getAllEconomies().contains(currencyName) ) {
			supported = true;
		}
		
		return supported;
	}
	
	
	
//	public double getBalance(Player player) {
//        return getBalance(player, null);
//    }

	public double getBalance(Player player, String currencyName) {
		double results = 0;
		if ( isEnabled() && currencyName != null ) {
			results = economy.getEco(player.getUUID(), currencyName);
		}
		return results;
	}
	
	public void addBalance(Player player, double amount, String currencyName) {
		if ( isEnabled() && currencyName != null ) {
			
			economy.addEco(player.getUUID(), currencyName, amount);
			
		}
	}
	
	public void withdraw(Player player, double amount, String currencyName) {
		if ( isEnabled() && currencyName != null ) {

			economy.removeEco(player.getUUID(), currencyName, amount);
			
		}
	}

	public EconomyModel getEconomy() {
		return economy;
	}
	public void setEconomy(EconomyModel economy) {
		this.economy = economy;
	}

	public List<String> getAllEconomies() {
		return allEconomies;
	}
	public void setAllEconomies(List<String> allEconomies) {
		this.allEconomies = allEconomies;
	}
}
