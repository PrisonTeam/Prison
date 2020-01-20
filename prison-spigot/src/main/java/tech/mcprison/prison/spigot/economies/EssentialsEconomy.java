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

import java.math.MathContext;

import com.earth2me.essentials.api.Economy;

import tech.mcprison.prison.integration.EconomyIntegration;
import tech.mcprison.prison.internal.Player;

/**
 * Integrates with Essentials Economy.
 *
 * @author Faizaan A. Datoo
 */
public class EssentialsEconomy 
	extends EconomyIntegration {

	private EssEconomyWrapper wrapper = null;

    public EssentialsEconomy() {
    	super( "EssentialsX", "Essentials" );
    	
    }
	
	@Override
	public void integrate() {
		if ( isRegistered() // && classLoaded 
				) {
			try
			{
				@SuppressWarnings( "unused" )
				MathContext mathCtx = Economy.MATH_CONTEXT;

				wrapper = new EssEconomyWrapper();
			}
			catch ( java.lang.NoClassDefFoundError | Exception e )
			{
				e.printStackTrace();
			}
		}
	}

    @Override 
    public double getBalance(Player player) {
        return wrapper.getBalance(player);
    }

    @Override 
    public void setBalance(Player player, double amount) {
        wrapper.setBalance(player, amount);
    }

    @Override 
    public void addBalance(Player player, double amount) {
        setBalance(player, getBalance(player) + amount);
    }

    @Override 
    public void removeBalance(Player player, double amount) {
        setBalance(player, getBalance(player) - amount);
    }

    @Override 
    public boolean canAfford(Player player, double amount) {
        return getBalance(player) >= amount;
    }
    
    @Override 
    public boolean hasIntegrated() {
        return wrapper != null;
    }

	@Override
	public String getPluginSourceURL()
	{
		return "https://www.spigotmc.org/resources/essentialsx.9089/";
	}

}
