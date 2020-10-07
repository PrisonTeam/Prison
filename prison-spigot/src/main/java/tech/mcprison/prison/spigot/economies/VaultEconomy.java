/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.spigot.economies;

import tech.mcprison.prison.integration.EconomyIntegration;
import tech.mcprison.prison.internal.Player;

/**
 * @author Faizaan A. Datoo
 */
public class VaultEconomy 
	extends EconomyIntegration {

    private VaultEconomyWrapper econWrapper;

    public VaultEconomy() {
    	super( "VaultEcon", "Vault" );
    }

	@Override
	public void integrate() {
		addDebugInfo( "1" );
		
		if ( isRegistered()) {
			addDebugInfo( "2" );
			try {
				addDebugInfo( "3" );
				this.econWrapper = new VaultEconomyWrapper( getProviderName() );
				addDebugInfo( "4" );
			}
			catch ( java.lang.NoClassDefFoundError | Exception e ) {
				addDebugInfo( "5:Exception:" + e.getMessage() );
				// ignore this exception since it means the plugin was not loaded
			}
		}
		addDebugInfo( "6" );
	}
    
	@Override 
	public double getBalance(Player player) {
        if (hasIntegrated()) {
        	return econWrapper.getBalance( player );
        } else {
        	return 0;
        }
    }

    @Override 
    public void setBalance(Player player, double amount) {
        if (hasIntegrated()) {
        	econWrapper.setBalance( player, amount );
        }
    }

    @Override 
    public void addBalance(Player player, double amount) {
        if (hasIntegrated()) {
        	econWrapper.addBalance( player, amount );
        }
    }

    @Override
    public void removeBalance(Player player, double amount) {
        if (hasIntegrated()) {
        	econWrapper.removeBalance( player, amount );
        }
    }

    @Override 
    public boolean canAfford(Player player, double amount) {
        return hasIntegrated() && getBalance(player) >= amount;
    }
    
    @Override
    public String getDisplayName()
    {
    	return ( !hasIntegrated() ? "Vault Economy" : econWrapper.getName()) + " (Vault)";
    }
    
    /**
     * <p>Its important to not only ensure that the wrapper exists, but the contained 
     * economy object is actually enabled too.
     * </p>
     */
    @Override 
    public boolean hasIntegrated() {
        return econWrapper != null && econWrapper.isEnabled();
    }

	@Override
	public String getPluginSourceURL() {
		return "https://www.spigotmc.org/resources/vault.34315/";
	}
}
