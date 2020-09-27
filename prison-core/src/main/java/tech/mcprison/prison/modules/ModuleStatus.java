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

package tech.mcprison.prison.modules;

/**
 * Stores data on the status of the module.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class ModuleStatus {

  /*
   * Enums
   */

    private Status status;

  /*
   * Fields & Constants
   */

    private String message;

    /**
     * Quickly set a module to the {@link Status#ENABLED} status, and set the message to "Enabled" in
     * green (a).
     */
    public void toEnabled() {
        setStatus(Status.ENABLED);
        setMessage("&aEnabled");
    }

    /*
     * Methods
     */

    /**
     * Quickly set a module to the {@link Status#DISABLED} status, and set the message to "Disabled"
     * in red (c).
     */
    public void toDisabled() {
        setStatus(Status.DISABLED);
        setMessage("&cDisabled");
    }

    /**
     * Quickly set a module to the {@link Status#FAILED} status.
     *
     * @param reason The message to set. Supports amp-prefixed color codes.
     */
    public void toFailed(String reason) {
        setStatus(Status.FAILED);
        setMessage(reason);
    }

    public Status getStatus() {
        return status;
    }

    /**
     * <p>Displays the associated test based upon the three combinations
     * of status codes.
     * </p>
     * 
     * @return
     */
    public String getStatusText() {
    	return (getStatus() == ModuleStatus.Status.ENABLED ? 
				"&2Enabled" : 
			(getStatus() == ModuleStatus.Status.FAILED ? 
					"&cFailed" : "&9&m-Disabled-" ));
    }


    /*
     * Getters & Setters
     */

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public void addMessage(String message) {
    	if ( this.message == null ) {
    		setMessage(message);
    	} else {
    		setMessage( getMessage() + ". " + message);
    	}
    }

    public enum Status {
        ENABLED, DISABLED, FAILED
    }

}
