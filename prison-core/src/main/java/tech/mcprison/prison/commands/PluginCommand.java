/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017-2020 The Prison Team
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

package tech.mcprison.prison.commands;

/**
 * @author Faizaan A. Datoo
 */
public class PluginCommand {

    private String label;
    private String labelRegistered;
    
    private String description, usage;

    public PluginCommand(String label, String description, String usage) {
        this.label = label;
        this.labelRegistered = null;
        
        this.description = description;
        this.usage = usage;
    }

    public String getLabel() {
        return label;
    }

    public String getLabelRegistered() {
    	return labelRegistered;
    }
    public void setLabelRegistered( String labelRegistered ) {
    	this.labelRegistered = labelRegistered;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsage() {
        return  getLabelRegistered() == null ? usage : "/" + getLabelRegistered();
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }
}
