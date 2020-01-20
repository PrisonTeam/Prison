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
		
		if ( isRegistered()) {
			try {
				this.econWrapper = new VaultEconomyWrapper();
			}
			catch ( java.lang.NoClassDefFoundError | Exception e ) {
				// ignore this exception since it means the plugin was not loaded
			}
		}
	}
    
	@Override 
	public double getBalance(Player player) {
        if (econWrapper != null) {
        	return econWrapper.getBalance( player );
        } else {
        	return 0;
        }
    }

    @Override 
    public void setBalance(Player player, double amount) {
        if (econWrapper != null) {
        	econWrapper.setBalance( player, amount );
        }
    }

    @Override 
    public void addBalance(Player player, double amount) {
        if (econWrapper != null) {
        	econWrapper.addBalance( player, amount );
        }
    }

    @Override
    public void removeBalance(Player player, double amount) {
        if (econWrapper != null) {
        	econWrapper.removeBalance( player, amount );
        }
    }

    @Override 
    public boolean canAfford(Player player, double amount) {
        return econWrapper != null && econWrapper.canAfford( player, amount );
    }
    
    @Override
    public String getDisplayName()
    {
    	return (econWrapper == null ? "Vault Economy" : econWrapper.getName()) + " (Vault)";
    }
    
    @Override 
    public boolean hasIntegrated() {
        return econWrapper != null;
    }

	@Override
	public String getPluginSourceURL()
	{
		return "https://www.spigotmc.org/resources/vault.34315/";
	}
}
