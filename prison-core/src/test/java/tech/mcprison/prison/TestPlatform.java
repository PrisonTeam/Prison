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

package tech.mcprison.prison;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

import tech.mcprison.prison.PrisonCommand.RegisteredPluginsData;
import tech.mcprison.prison.commands.PluginCommand;
import tech.mcprison.prison.file.FileStorage;
import tech.mcprison.prison.file.YamlFileIO;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.PlayerUtil;
import tech.mcprison.prison.internal.Scheduler;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlockTypes;
import tech.mcprison.prison.internal.platform.Capability;
import tech.mcprison.prison.internal.platform.HandlerList;
import tech.mcprison.prison.internal.platform.Platform;
import tech.mcprison.prison.internal.scoreboard.ScoreboardManager;
import tech.mcprison.prison.modules.ModuleElement;
import tech.mcprison.prison.modules.ModuleElementType;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.placeholders.PlaceholderManager.PlaceholderFlags;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.placeholders.Placeholders;
import tech.mcprison.prison.store.Storage;
import tech.mcprison.prison.util.ChatColor;
import tech.mcprison.prison.util.Location;

/**
 * @author Faizaan A. Datoo
 */
public class TestPlatform implements Platform {

    private File pluginDirectory;
    private boolean suppressOutput;

    public TestPlatform(File pluginDirectory, boolean suppressOutput) {
        this.pluginDirectory = pluginDirectory;
        this.suppressOutput = suppressOutput;
    }

    @Override 
    public Optional<World> getWorld(String name) {
        return Optional.of(new TestWorld(name));
    }

    @Override 
    public void getWorldLoadErrors( ChatDisplay display ) {
    	
    }
    
    @Override 
    public Optional<Player> getPlayer(String name) {
        return null;
    }

    @Override 
    public Optional<Player> getPlayer(UUID uuid) {
        return null;
    }

    @Override 
    public List<Player> getOnlinePlayers() {
        return new ArrayList<>();
    }
    @Override
    public List<Player> getOfflinePlayers() {
    	List<Player> players = new ArrayList<>();
    	return players;
    }
    
    @Override
	public Optional<Player> getOfflinePlayer( String name ) {
		return null;
	}

	@Override
	public Optional<Player> getOfflinePlayer( UUID uuid ) {
		return null;
	}

	@Override 
	public String getPluginVersion() {
        return "Tests";
    }

    @Override 
    public File getPluginDirectory() {
        return pluginDirectory;
    }

    @Override
    public boolean shouldShowAlerts() {
        return false;
    }

    @Override 
    public void registerCommand(PluginCommand command) {

    }

    @Override 
    public void unregisterCommand(String command) {

    }
    
    @Override
    public void unregisterAllCommands() {
    	
    }

    @Override 
    public List<PluginCommand> getCommands() {
        return Collections.emptyList();
    }

    @Override 
    public void dispatchCommand(String cmd) {
    }

    @Override
	public void dispatchCommand( CommandSender sender, String cmd ) {
	}

	@Override 
	public Scheduler getScheduler() {
        return new TestScheduler();
    }

    @Override 
    public void toggleDoor(Location doorLocation) {
    }

    @Override 
    public void log(String message, Object... format) {
        if (suppressOutput) {
            return;
        }
        System.out.println(ChatColor.stripColor(String.format(message, format)));
    }

    @Override
    public void logCore( String message ) {
    	if (suppressOutput) {
    		return;
    	}
    	System.out.println(ChatColor.stripColor(message));
    }
    
    @Override
    public void logPlain( String message ) {
    	System.out.println(message);
    }
    
    @Override 
    public void debug(String message, Object... format) {
        log(message, format);
    }

    @Override 
    public Map<Capability, Boolean> getCapabilities() {
        return null;
    }

    @Override 
    public void showTitle(Player player, String title, String subtitle, int fade) {

    }

    @Override 
    public void showActionBar(Player player, String text, int duration) {

    }

    @Override 
    public ScoreboardManager getScoreboardManager() {
        return null;
    }

    @Override 
    public Storage getStorage() {
        return new FileStorage(getPluginDirectory());
    }

    @Override
	public void identifyRegisteredPlugins() {
		 PrisonCommand cmdVersion = Prison.get().getPrisonCommands();
		 
		 // reset so it will reload cleanly:
		 cmdVersion.getRegisteredPluginData().clear();
	
		 RegisteredPluginsData rpd1 = cmdVersion.addRegisteredPlugin( "TestPlugin", "3.2.1-alpha.13" );
		 RegisteredPluginsData rpd2 = cmdVersion.addRegisteredPlugin( "AnotherPlugin", "0.1.1" );
		 
		 cmdVersion.addPluginDetails( rpd1.getPluginName(), rpd1.getPluginVersion(), "lol", new ArrayList<>() );
		 cmdVersion.addPluginDetails( rpd2.getPluginName(), rpd2.getPluginVersion(), "crime", new ArrayList<>() );
		 cmdVersion.addPluginDetails( rpd2.getPluginName(), rpd2.getPluginVersion(), "justice", new ArrayList<>() );
    }

    
    public Map<PlaceholderFlags, Integer> getPlaceholderDetailCounts() {
    	Map<PlaceholderFlags, Integer> placeholderDetails = new TreeMap<>();
    	
    	return placeholderDetails;
    }
    
    public int getPlaceholderCount() {
    	return 0;
    }
    
    
    public int getPlaceholderRegistrationCount() {
    	return 0;
    }
 
    
    @Override
    public Placeholders getPlaceholders() {
    	return null;
    }
    
	
	@Override
	public YamlFileIO getYamlFileIO( File yamlFile ) {
		return null;
	}

	@Override
	public void reloadConfig() {
	}
	
	@Override
	public String getConfigString( String key ) {
		return null;
	}
	
	@Override
	public String getConfigString( String key, String defaultValue ) {
		return defaultValue;
	}
	
	@Override
	public boolean getConfigBooleanFalse( String key ) {
		return false;
	}
	
	@Override
	public boolean getConfigBooleanTrue( String key ) {
		return false;
	}
	
	@Override
	public int getConfigInt( String key, int defaultValue ) {
		return defaultValue;
	}
	
	@Override
	public long getConfigLong( String key, long defaultValue ) {
		return defaultValue;
	}
	
	@Override
	public double getConfigDouble( String key, double defaultValue ) {
		return defaultValue;
	}

	@Override
	public List<?> getConfigStringArray( String key ) {
		return new ArrayList<String>();
	}

	@Override
	public boolean isWorldExcluded( String worldName ) {
		return false;
	}
	
	@Override
    public TreeSet<String> getExcludedWorlds()
	{
		return new TreeSet<>();
	}
	
	@Override
	public PrisonBlockTypes getPrisonBlockTypes() {
		return null;
	}
	
	@Override
	public PrisonBlock getPrisonBlock( String blockName ) {
		return null;
	}
	
	@Override
	public ItemStack getItemStack( PrisonBlock prisonBlock, int blockQuantity ) {
		return null;
	}
	
	@Override
	public boolean linkModuleElements( ModuleElement sourceElement, 
					ModuleElementType targetElementType, String name ) {
		return false;
	}

	@Override
	public boolean unlinkModuleElements( ModuleElement elementA, ModuleElement elementB ) {
		return false;
	}
	
	@Override
	public ModuleElement createModuleElement( CommandSender sender, ModuleElementType elementType, String name, 
					String tag, String accessPermission ) {
		return null;
	}
	
	@Override
	public int getModuleElementCount( ModuleElementType elementType ) {
		return 0;
	}
	
	@Override
	public ModuleElement getModuleElement( ModuleElementType elementType, String elementName ) {
		return null;
	}
	
	@Override
	public ModuleElement getPlayerDefaultMine( tech.mcprison.prison.internal.CommandSender sender ) {
		return null;
	}

	@Override
	public boolean isMineAccessibleByRank( Player player, ModuleElement mine ) {
		return false;
	}
	
	@Override
	public void autoCreateMineBlockAssignment( List<String> rankMineNames, boolean forceKeepBlocks ) {
		
	}
	
	@Override
	public void autoCreateMineLinerAssignment( List<String> rankMineNames, 
					boolean forceLinersBottom, boolean forceLinersWalls ) {
		
	}
	
	@Override
	public void autoCreateConfigureMines() {
		
	}
	
	@Override
	public List<String> getActiveFeatures( boolean showLaddersAndRanks ) {
		List<String> results = new ArrayList<>();
		return results;
	}
	
	
	@Override
	public void prisonVersionFeatures( ChatDisplay display, boolean isBasic, boolean showLaddersAndRanks ) {
		
	}
	
	
	@Override
	public String dumpEventListenersBlockBreakEvents() {
		return "";
	}
	
	@Override
	public String dumpEventListenersPlayerChatEvents() {
		return "";
	}
	
	@Override
	public void traceEventListenersBlockBreakEvents( CommandSender sender ) {
		
	}
	
	@Override
	public String dumpEventListenersPlayerInteractEvents() {
		return "";
	}
	
	@Override
	public void testPlayerUtil( UUID uuid ) {
		
	}
	
	@Override
	public void saveResource( String string, boolean replace ) {
		
	}
	
	@Override
	public String getMinesListString() {
		return "";
	}
	
	@Override
	public String getRanksListString() {
		return "";
	}

	public boolean isSuppressOutput()
	{
		return suppressOutput;
	}

	public void setSuppressOutput( boolean suppressOutput )
	{
		this.suppressOutput = suppressOutput;
	}

	public void setPluginDirectory( File pluginDirectory )
	{
		this.pluginDirectory = pluginDirectory;
	}

	@Override
	public PlayerUtil getPlayerUtil( UUID playerUuid ) {
		return null;
	}
	
	@Override
	public PlayerUtil getPlayerUtil( Player player ) {
		return null;
	}
	
	@Override
	public List<String> dumpEventListenersList( String eventType, HandlerList handlerList )
	{
		return new ArrayList<>();
	}

	@Override
	public ChatDisplay dumpEventListenersChatDisplay( String eventType, HandlerList handlerList )
	{
		ChatDisplay chatDisplay = new ChatDisplay( "Test Event Listing" );
		return chatDisplay;
	}
	
	/**
	 * <p>This only reloads the event listeners that auto features uses.  This is called by
	 * the command "/prison reload autoFeatures".  
	 * </p>
	 * 
	 * <code>tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.reloadConfig()</code>
	 * 
	 */
	@Override
	public void reloadAutoFeaturesEventListeners() {
		
	}

	@Override
	public void setTitle( Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut )
	{
		
	}

	@Override
	public void setActionBar( Player player, String actionBar )
	{
		
	}
	
	@Override
	public int compareServerVerisonTo( String comparisonVersion ) {
		return 0;
	}
	
	@Override
	public void checkPlayerDefaultRank( RankPlayer rPlayer ) {
		
	}
	
	@Override
	public void listAllMines(CommandSender sender, Player player) {
		
	}
	
	@Override
	public void sellall( RankPlayer rankPlayer ) {
		
	}
}
