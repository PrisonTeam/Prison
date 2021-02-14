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

package tech.mcprison.prison.internal;

/**
 * Represents any entity that may send commands and receive output.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public interface CommandSender 
					extends PlayerPermissions {

//	/**
//	 * Returns the UUID for CommandSender if they are a bukkit Player.
//	 * 
//	 * @return
//	 */
//	public UUID getUUID();
	
    /**
     * Returns the name of the command sender.
     */
    String getName();

    /**
     * Force a commandSender to send a command
     */
    void dispatchCommand(String command);

    /**
     * Returns true if the command sender can show colors to the viewer.
     * This is not always the case, especially when using command blocks and online consoles.
     */
    boolean doesSupportColors();

    /**
     * Sends a message to the command sender.
     * If the command sender supports colors, color codes will automatically be parsed and shown.
     * If not, color codes will simply be stripped from the message.
     *
     * @param message The message to send. May include color codes, amp-prefixed.
     */
    void sendMessage(String message);

    /**
     * Sends multiple messages to the command sender.
     * Each message will be shown on its own line.
     *
     * @param messages The array containing each message.
     * @see #sendMessage(String)
     */
    void sendMessage(String[] messages);

    /**
     * Send a raw JSON message to the sender.
     *
     * @param json The JSON message. Must be in proper format.
     */
    void sendRaw(String json);
    
    
//    public boolean isOp();
//    
//    public void recalculatePermissions();
//    
//    /**
//     * Returns true if the command sender has access to the permission specified.
//     *
//     * @param perm The permission to check.
//     */
//    boolean hasPermission(String perm);

    
    public boolean isPlayer();

}
