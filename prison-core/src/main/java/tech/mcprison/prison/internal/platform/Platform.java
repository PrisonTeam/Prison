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

package tech.mcprison.prison.internal.platform;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import tech.mcprison.prison.commands.PluginCommand;
import tech.mcprison.prison.file.YamlFileIO;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.PlayerUtil;
import tech.mcprison.prison.internal.Scheduler;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlockTypes;
import tech.mcprison.prison.internal.scoreboard.ScoreboardManager;
import tech.mcprison.prison.modules.ModuleElement;
import tech.mcprison.prison.modules.ModuleElementType;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.placeholders.Placeholders;
import tech.mcprison.prison.store.Storage;
import tech.mcprison.prison.util.Location;

/**
 * Represents an internal platform that Prison has been implemented for.
 * The internal platform is responsible for connecting Prison's APIs to the underlying server API.
 *
 * @author Faizaan A. Datoo
 * @author Camouflage100
 * @since API 1.0
 */
public interface Platform {

    /**
     * Returns the world with the specified name.
     */
    Optional<World> getWorld(String name);

    
    /**
     * <p>This function allows the PrisonCommand to get a list of any possible world
     * load failures.
     * </p>
     * 
     * @param display
     * @return
     */
    public void getWorldLoadErrors( ChatDisplay display );
    
    
    /**
     * Returns the player with the specified name.
     */
    Optional<Player> getPlayer(String name);

    /**
     * Returns the player with the specified UUID.
     */
    Optional<Player> getPlayer(UUID uuid);

    /**
     * Returns a list of all online players.
     */
    List<Player> getOnlinePlayers();
    
	public List<Player> getOfflinePlayers();
	
	
// NOTE: Disabling for now.  There is an internal failure within the Prison code base when trying 
//       to use this, so will revisit in the future.
    public Optional<Player> getOfflinePlayer(String name);
    
    public Optional<Player> getOfflinePlayer(UUID uuid);
    
    /**
     * Returns the plugin's version.
     */
    String getPluginVersion();

    /**
     * Returns the {@link File} representing the plugin's designated storage folder.
     * This directory must have already been created by the implementation.
     */
    File getPluginDirectory();

    /**
     * Registers a command with the server implementation.
     *
     * @param command The {@link PluginCommand} to register.
     */
    void registerCommand(PluginCommand command);

    /**
     * Unregisters a registered command.
     * This does not support command aliases, because those are currently not definable anyway.
     *
     * @param command The command to unregister, without the preceding '/'.
     */
    public void unregisterCommand(String command);

    
    public void unregisterAllCommands();

    
    /**
     * Returns a list of all registered commands.
     */
    List<PluginCommand> getCommands();

    /**
     * Runs a command as the console (i.e. with all privileges).
     *
     * @param cmd The command to run, without the '/'.
     */
    void dispatchCommand(String cmd);

    /**
     * Runs a command as the sender and with only the sender's privileges.
     * 
     * @param sender
     * @param cmd
     */
    public void dispatchCommand(CommandSender sender, String cmd);
    
    /**
     * Returns the {@link Scheduler}, which can be used to schedule tasks.
     */
    Scheduler getScheduler();

    /**
     * Creates a new {@link GUI} to show to players.
     *
     * @param title   The title of the GUI.
     * @param numRows The number of rows in the GUI; must be divisible by 9.
     */

    /**
     * If an iron door is open, this method closes it.
     * If an iron door is closed, this method opens it.
     *
     * @param doorLocation The {@link Location} of the door.
     */
    void toggleDoor(Location doorLocation);

    /**
     * Log a colored message to the console (if supported).
     *
     * @param message The message. May include color codes, amp-prefixed.
     * @param format  The objects inserted via {@link String#format(String, Object...)}.
     */
    public void log(String message, Object... format);

    public void logCore( String message );

    public void logPlain( String message );
    
    /**
     * Logs a debug message to the console if the user has debug messages enabled.
     *
     * @param message The message. May include color codes, amp-prefixed.
     * @param format  The The objects inserted via {@link String#format(String, Object...)}.
     */
    void debug(String message, Object... format);

    /**
     * Runs the converter for this platform.
     *
     * @return The output of the converter. It will be sent to whoever ran the converter system (e.g. usually a command sender).
     */
    default String runConverter() {
        return "This operation is unsupported on this platform.";
    }

    /**
     * Returns a map of capabilities and whether or not this internal has them.
     */
    Map<Capability, Boolean> getCapabilities();

    /**
     * Send a title to a player
     *
     * @param player   The player that you want to send the title to
     * @param title    The text of the title
     * @param subtitle The text of the subtitle
     * @param fade     The length of the fade
     */
    void showTitle(Player player, String title, String subtitle, int fade);

    /**
     * Send an actionbar to a player
     *
     * @param player   The player that you want to send the actionbar to
     * @param text     The text of the actionbar
     * @param duration The amount of time to show the action bar, in seconds. Set to -1 for no duration (i.e. vanilla standard duration of ~3 seconds).
     */
    void showActionBar(Player player, String text, int duration);

    /**
     * Returns the scoreboard manager.
     */
    ScoreboardManager getScoreboardManager();

    /**
     * Returns the storage manager.
     */
    Storage getStorage();

    /**
     * Retrieves the {@link PluginCommand} object for a command with a certain label.
     *
     * @param label The command's label.
     * @return The {@link PluginCommand}, or null if no command exists by that label.
     */
    default Optional<PluginCommand> getCommand(String label) {
        for (PluginCommand command : getCommands()) {
            if (command.getLabel().equalsIgnoreCase(label)) {
                return Optional.of(command);
            }
        }
        return Optional.empty();
    }


    /**
     * Returns true if the server should show alerts to in-game players, false otherwise.
     * This is a configuration option.kkjksdf;erljnkx.jcsmka.f.fdlwe;s.x. frrer5
     */
    boolean shouldShowAlerts();


    
    public void identifyRegisteredPlugins();

    
    
    public Placeholders getPlaceholders();
    
    
	
	public YamlFileIO getYamlFileIO( File yamlFile );
	

	public void reloadConfig();
	
	
	public String getConfigString( String key );
	
	public String getConfigString( String key, String defaultValue );
	
	
	/**
	 * <p>This returns the boolean value that is associated with the key.
	 * It has to match on true to return a true value.  If the key does
	 * not exist, then it returns a value of false. Default value is false.
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
	public boolean getConfigBooleanFalse( String key );
	

	/**
	 * <p>This is the only way to find out if the new block model is enabled.
	 * </p>
	 * 
	 * <p>This should be the only place within prison to use the actual String value of the
	 * permission.  This will allow for a simple change in the future.
	 * </p>
	 * 
	 * @return
	 */
	public boolean isUseNewPrisonBlockModel();
	
	
	/**
	 * <p>This returns the boolean value that is associated with the key.
	 * It has to match on true to return a true value, but if the key does
	 * not exist, then it returns a value of true.  Default value is true.
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
	public boolean getConfigBooleanTrue( String key );
	
	
	public int getConfigInt( String key, int defaultValue );
	
	public long getConfigLong( String key, long defaultValue );

	public double getConfigDouble( String key, double defaultValue );
	

    /**
     * Setup hooks in to the valid prison block types.  This will be only the 
     * block types that have tested to be valid on the server that is running 
     * prison.  This provides full compatibility to the admins that if a block 
     * is listed, then it's usable.  No more guessing or finding out after the 
     * fact that a block that was used was invalid for their version of minecraft.
     */
	public PrisonBlockTypes getPrisonBlockTypes();	
	
	public PrisonBlock getPrisonBlock( String blockName );
	
	
	public boolean linkModuleElements( ModuleElement sourceElement, ModuleElementType targetElementType, String name );
	
	
	public boolean unlinkModuleElements( ModuleElement elementA, ModuleElement elementB );


	public ModuleElement createModuleElement( CommandSender sender, ModuleElementType elementType, String name, 
						String tag, String accessPermission );

	
	public int getModuleElementCount( ModuleElementType elementType );
	
	public ModuleElement getModuleElement( ModuleElementType elementType, String elementName );
	
	
	public ModuleElement getPlayerDefaultMine( tech.mcprison.prison.internal.CommandSender sender );
	

	public boolean isMineAccessibleByRank( Player player, ModuleElement mine );
	
	
	public void autoCreateMineBlockAssignment( List<String> rankMineNames, boolean forceKeepBlocks );


	public void autoCreateMineLinerAssignment( List<String> rankMineNames, 
					boolean forceLinersBottom, boolean forceLinersWalls );
	
	
	public void autoCreateConfigureMines();
	
	
	/** 
	 * This function will return a list of text strings indicating features that are either enabled or disabled.
	 * 
	 * @return
	 */
	public List<String> getActiveFeatures();


	public String dumpEventListenersBlockBreakEvents();
	
	public String dumpEventListenersPlayerChatEvents();


	public void traceEventListenersBlockBreakEvents( CommandSender sender );

	
	public void testPlayerUtil( UUID uuid );
	

	public void saveResource( String string, boolean replace );


	public String getMinesListString();


	public String getRanksListString();

	
	public PlayerUtil getPlayerUtil( UUID playerUuid );

	
	/**
	 * <p>Some information on events...
	 * </p>
	 * 
	 * https://bukkit.fandom.com/wiki/Event_API_Reference
	 * 
	 * <p>When changing values of an event the changes of one with the higher priority will 
	 * override any changes done before by a listener with a lower priority so that in the 
	 * end the one with the highest priority can have the final say in the actually outcome. 
	 * <b>To achieve this priority order listeners are called from the ones with the 
	 * lowest to the ones with the highest priority. Any listener with the MONITOR 
	 * priority is called last.</b> 
	 * 
	 * </p>
	 * 
	 * @param eventType
	 * @param handlerList
	 */
	public List<String> dumpEventListenersList( String eventType, HandlerList handlerList );


	public ChatDisplay dumpEventListenersChatDisplay( String eventType, HandlerList handlerList );


	/**
	 * <p>This only reloads the event listeners that auto features uses.  This is called by
	 * the command "/prison reload autoFeatures".  
	 * </p>
	 * 
	 * <code>tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.reloadConfig()</code>
	 * 
	 */
	public void reloadAutoFeaturesEventListeners();


	
}
