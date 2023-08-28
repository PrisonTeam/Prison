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

package tech.mcprison.prison.spigot;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.update.spiget.SpigetUpdate;
import org.inventivetalent.update.spiget.UpdateCallback;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.PrisonCommand;
import tech.mcprison.prison.PrisonCommand.RegisteredPluginsData;
import tech.mcprison.prison.alerts.Alerts;
import tech.mcprison.prison.backups.PrisonBackups;
import tech.mcprison.prison.integration.Integration;
import tech.mcprison.prison.integration.IntegrationType;
import tech.mcprison.prison.internal.block.PrisonBlockTypes;
import tech.mcprison.prison.localization.LocaleManager;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.managers.MineManager;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.modules.ModuleElementType;
import tech.mcprison.prison.modules.PluginEntity;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.LogLevel;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.commands.FailedRankCommands;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.managers.RankManager;
import tech.mcprison.prison.sellall.PrisonSellall;
import tech.mcprison.prison.sellall.commands.SellallCommands;
import tech.mcprison.prison.spigot.autofeatures.AutoManagerFeatures;
import tech.mcprison.prison.spigot.autofeatures.events.AutoManagerBlockBreakEvents;
import tech.mcprison.prison.spigot.backpacks.BackpacksListeners;
import tech.mcprison.prison.spigot.block.OnBlockBreakEventListener;
import tech.mcprison.prison.spigot.bstats.PrisonBStats;
import tech.mcprison.prison.spigot.commands.PrisonSpigotBackpackCommands;
import tech.mcprison.prison.spigot.commands.PrisonSpigotGUIBackPackCommands;
import tech.mcprison.prison.spigot.commands.PrisonSpigotGUICommands;
import tech.mcprison.prison.spigot.commands.PrisonSpigotGUISellAllCommands;
import tech.mcprison.prison.spigot.commands.PrisonSpigotMinesCommands;
import tech.mcprison.prison.spigot.commands.PrisonSpigotPrestigeCommands;
import tech.mcprison.prison.spigot.commands.PrisonSpigotRanksCommands;
import tech.mcprison.prison.spigot.commands.PrisonSpigotSellAllCommands;
import tech.mcprison.prison.spigot.compat.Compatibility;
import tech.mcprison.prison.spigot.compat.SpigotCompatibility;
import tech.mcprison.prison.spigot.configs.BackpacksConfig;
import tech.mcprison.prison.spigot.configs.GuiConfig;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.configs.SellAllConfig;
import tech.mcprison.prison.spigot.customblock.CustomItems;
import tech.mcprison.prison.spigot.economies.EssentialsEconomy;
import tech.mcprison.prison.spigot.economies.GemsEconomy;
import tech.mcprison.prison.spigot.economies.SaneEconomy;
import tech.mcprison.prison.spigot.economies.VaultEconomy;
import tech.mcprison.prison.spigot.gui.ListenersPrisonManager;
import tech.mcprison.prison.spigot.permissions.LuckPermissions;
import tech.mcprison.prison.spigot.permissions.LuckPerms5;
import tech.mcprison.prison.spigot.permissions.VaultPermissions;
import tech.mcprison.prison.spigot.placeholder.PlaceHolderAPIIntegration;
import tech.mcprison.prison.spigot.sellall.SellAllUtil;
import tech.mcprison.prison.spigot.slime.SlimeBlockFunEventListener;
import tech.mcprison.prison.spigot.spiget.BluesSpigetSemVerComparator;
import tech.mcprison.prison.spigot.tasks.PrisonInitialStartupTask;
import tech.mcprison.prison.spigot.tasks.SpigotPrisonDelayedStartupTask;
import tech.mcprison.prison.spigot.utils.PrisonUtilsMineBombs;
import tech.mcprison.prison.spigot.utils.PrisonUtilsModule;
import tech.mcprison.prison.util.Text;

/**
 * The plugin class for the Spigot implementation.
 *
 * @author Faizaan A. Datoo
 * @author GABRYCA
 */
public class SpigotPrison 
	extends JavaPlugin 
	implements PluginEntity {

	private static SpigotPrison config;

	Field commandMap;
    Field knownCommands;
    SpigotScheduler scheduler;
    Compatibility compatibility;
    boolean debug = false;

    private File dataDirectory;
//    private boolean doAlertAboutConvert = false;
    
    private AutoManagerFeatures autoFeatures = null;
//    private FileConfiguration autoFeaturesConfig = null;
    
    private OnBlockBreakEventListener blockBreakEventListeners;

    private MessagesConfig messagesConfig;
    private GuiConfig guiConfig;
    private SellAllConfig sellAllConfig;
    private BackpacksConfig backpacksConfig;

    private PrisonBlockTypes prisonBlockTypes;

    private static boolean isBackPacksEnabled = false;
    private static boolean isSellAllEnabled = false;
    
    
    private LocaleManager localeManager;
    private File moduleDataFolder;
    
    private List<Listener> registeredBlockListeners;
    
    
    private PrisonBStats prisonBStats;
//    private Metrics bStatsMetrics = null;
//    private PrisonMetrics bStatsMetrics = null;

    
    public static SpigotPrison getInstance(){
        return config;
    }

    public SpigotPrison() {
    	super();
    	
    	config = this;
    	
    	this.registeredBlockListeners = new ArrayList<>();
    	
    }

    @Override
    public void onLoad() {
    	
        // Startup bStats:  Not needed with the new PrisonMetrics class.
    	this.prisonBStats = new PrisonBStats( this );
    	prisonBStats.initMetricsOnLoad();

    	
    	/**
    	 * Old versions of prison MUST be upgraded with v3.0.x or even v3.1.1.
    	 * Upgrading from old versions of prison to v3.2.x is not supported.  
    	 * Please upgrade to an earlier release of v3.0.x then to v3.2.1.
    	
        // The meta file is used to see if the folder needs converting.
        // If the folder doesn't contain it, it's probably not a Prison 3 thing.
        File metaFile = new File(getDataFolder(), ".meta");
        if (getDataFolder().exists()) {
            if (!metaFile.exists()) {
                File old = getDataFolder();
                old.renameTo(new File(getDataFolder().getParent(), "Prison.old"));
                doAlertAboutConvert = true;
            }
        }
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
            try {
                metaFile.createNewFile();
            } catch (IOException e) {
                Output.get().logError( 
                        "Could not create .meta file, this will cause problems with the converter!");
            }
        }
    	 */
        
    }

    @Override
    public void onEnable() {
    	
    	
    	// Setup the config.yml file and set debug mode:
    	// config = this;
        this.saveDefaultConfig();
        this.debug = getConfig().getBoolean("debug", false);


        // Create the core directory structure if it is missing:
        initDataDir();

//        // Setup the localManager (when instantiating Prison) and set the default language:
//        Prison.get().getLocaleManager().setDefaultLocale(
//        		getConfig().getString("default-language", "en_US"));

        // Setup some of the key data structures and object required to be in place
        // prior to starting up:
        initCommandMap();
        this.scheduler = new SpigotScheduler(this);
        
        SpigotPlatform platform = new SpigotPlatform(this);
        

        // Show Prison's splash screen and setup the core components:
        Prison.get()
        		.init( platform, Bukkit.getVersion(), getDataFolder() );

        
        
        // If prison version is new, then make a copy of all config files that may change on startup:
        PrisonBackups backups = new PrisonBackups();
        backups.initialStartupVersionCheck();
        

        
        
        // Enable the spigot locale manager:
        getLocaleManager();
        
        if ( debug ) {
        	Output.get().setDebug( debug );
        }
        
        // Load the Text's language configs:
        Text.initialize();
        
        
        this.compatibility = SpigotCompatibility.getInstance();
//        initCompatibility();  Obsolete...
        
        
        initUpdater();
        
        
    	boolean delayedPrisonStartup = getConfig().getBoolean("delayedPrisonStartup.enabled", false);

    	
    	if ( !delayedPrisonStartup ) {
    		
    		// Check to see if CMI is an active plugin.  If it is, then let's enable the delayed startup.
    		// It should be noted that just because CMI is detected, it does not mean that the CMI Economy
    		// is being used.  Use a flexible startup which means it will start if any vault economy 
    		// is found.
    		RegisteredPluginsData cmiPlugin = platform.identifyRegisteredPlugin( "CMI" );
    		if ( cmiPlugin != null ) {
    			String cmiMessage = String.format( 
    					"CMI was detected and Prison's delayed startup has been enabled: %s - %s ", 
    					cmiPlugin.getPluginName(), cmiPlugin.getPluginVersion() );
    			Output.get().logInfo( cmiMessage );
    		
    			onEnableDelayedStartFlexible();
    		}
    		else {
    			
    			onEnableStartup();
    		}
    	}
    	else {
    		onEnableDelayedStart();
    	}
    	
    }
    
    
    protected void onEnableDelayedStartFlexible() {
    	
    	SpigotPrisonDelayedStartupTask delayedStartupTask = new SpigotPrisonDelayedStartupTask( this );
    	delayedStartupTask.setUseAnyVaultEconomy( true );
    	delayedStartupTask.submit();
    }
    
    protected void onEnableDelayedStart() {
    	
    	SpigotPrisonDelayedStartupTask delayedStartupTask = new SpigotPrisonDelayedStartupTask( this );
    	delayedStartupTask.submit();
    }
    
    
    public void onEnableFail() {
        // Register the failure /ranks command handler:
        
        FailedRankCommands failedRanksCommands = new FailedRankCommands();
//        rankManager.setFailedRanksCommands( failedRanksCommands );
        Prison.get().getCommandHandler().registerCommands( failedRanksCommands );
        

    }
    	
   public void onEnableStartup() {
	   

        
        // Manually register Listeners with Bukkit:
        Bukkit.getPluginManager().registerEvents(new ListenersPrisonManager(),this);

        
        boolean slimeFunEnabled1 = SpigotPrison.getInstance().getConfig().getBoolean("slime-fun");
		boolean slimeFunEnabled2 = SpigotPrison.getInstance().getConfig().getBoolean("slime-fun.enabled");
		
        if ( slimeFunEnabled1 || slimeFunEnabled2 ) {
        	Bukkit.getPluginManager().registerEvents(new SlimeBlockFunEventListener(), this);
        }
        
        Bukkit.getPluginManager().registerEvents(new SpigotListener(), this);

        try {
            isBackPacksEnabled = getConfig().getBoolean("backpacks");
        } catch (NullPointerException ignored){}

        if (isBackPacksEnabled){
            Bukkit.getPluginManager().registerEvents(new BackpacksListeners(), this);
        }

        initIntegrations();
        
        // Sellall set to disabled since it will be set to the correct value in enableModulesAndCommands():
        isSellAllEnabled = false;
        
        // This is the loader for modules and commands:
        enableModulesAndCommands();

        
//        // NOTE: Put all commands within the initModulesAndCommands() function.
//        initModulesAndCommands();
//        
//        applyDeferredIntegrationInitializations();
//        
//        
//        // After all the integrations have been loaded and the deferred tasks ran, 
//        // then run the deferred Module setups:
//        initDeferredModules();
        
        
        // The BlockBreakEvents must be registered after the mines and ranks modules have been enabled:
        // Auto features will prevent this if it's disabled.
        getBlockBreakEventListeners().registerAllBlockBreakEvents( this );
        
        
        // These stats are displayed within the initDeferredModules():
        //Prison.get().getPlatform().getPlaceholders().printPlaceholderStats();
        
        
        @SuppressWarnings("unused")
		PrisonCommand cmdVersion = Prison.get().getPrisonCommands();

//        if (doAlertAboutConvert) {
//            Alerts.getInstance().sendAlert(
//                    "&7An old installation of Prison has been detected. &3Type /prison convert to convert your old data automatically. &7If you already converted, delete the 'Prison.old' folder so that we stop nagging you.");
//        }

        // Finally print the version after loading the prison plugin:

//        // Store all loaded plugins within the PrisonCommand for later inclusion:
//        for ( Plugin plugin : Bukkit.getPluginManager().getPlugins() ) {
//        	String name = plugin.getName();
//        	String version = plugin.getDescription().getVersion();
//        	String value = "&7" + name + " &3(&a" + version + "&3)";
//        	cmdVersion.getRegisteredPlugins().add( value );
//		}

        
        ChatDisplay cdVersion = new ChatDisplay("A suppressed title");
        cdVersion.setShowTitle( false );
//		ChatDisplay cdVersion = cmdVersion.displayVersion("basic");
		

        // This generates the module listing, the autoFeatures overview, 
        // the integrations listings, and the plugins listings.
        // Used in the command: /prison version
		boolean isBasic = true;
		boolean showLaddersAndRanks = false;
        Prison.get().getPlatform().prisonVersionFeatures( cdVersion, isBasic, showLaddersAndRanks );


		cdVersion.toLog( LogLevel.INFO );
		
		// Provides a startup test of blocks available for the version of spigot that being used:
		if ( getConfig().getBoolean("prison-block-compatibility-report") ) {
			SpigotUtil.testAllPrisonBlockTypes();
		}

		
       // Force a backup if prison version is new:
       PrisonBackups backups = new PrisonBackups();
       backups.serverStartupVersionCheck();
	       

       // Reload guiConfigs since ranks and mines have now been loaded:
       guiConfig.initialize();
       
       
       
       // Setup mine bombs:
       PrisonUtilsMineBombs.getInstance().reloadPrisonMineBombs();
       

       // Enable Temp spigot commands:
       new SpigotCommand();
		
       // Startup bStats:
       prisonBStats.initMetricsOnEnable();
       
       
       
		Output.get().logInfo( "Prison - Finished loading." );
		
		
		if ( PrisonInitialStartupTask.isInitialStartup() ) {
			
			PrisonInitialStartupTask startupTask = new PrisonInitialStartupTask( this );
			startupTask.submit();
		}
    }

    @Override
    public void onDisable() {
    	if (this.scheduler != null ) {
    		this.scheduler.cancelAll();
    	}
    	
    	Prison.get().getPlatform().unregisterAllCommands();
    	
    	Prison.get().deinit();
    }

    
    

    /**
     * Lazy load LocalManager which ensures Prison is already loaded so 
     * can get the default language to use from the plugin configs.
     * 
     * Returns the {@link LocaleManager} for the plugin. This contains the global messages that Prison
     * uses to run its command library, and the like. {@link Module}s have their own {@link
     * LocaleManager}s, so that each module can have independent localization.
     *
     * @return The global locale manager instance.
     */
    public LocaleManager getLocaleManager() {
    		
    	if ( this.localeManager == null ) {
    		
    		this.localeManager = new LocaleManager(this, "lang/spigot");
    	}
        return localeManager;
    }

    /**
     * Returns this module's data folder, where all data can be stored.
     * It is located in the Prison data folder, and has the name of the module.
     * It is automatically generated.
     *
     * @return The {@link File} representing the data folder.
     */
    public File getModuleDataFolder() {
    	
    	if ( moduleDataFolder == null ) {
    		this.moduleDataFolder = Module.setupModuleDataFolder( "spigot" );
    	}
        return moduleDataFolder;
    }
    
    
    public OnBlockBreakEventListener getBlockBreakEventListeners() {
    	if ( blockBreakEventListeners == null ) {
            this.blockBreakEventListeners = new OnBlockBreakEventListener();
    	}
		return blockBreakEventListeners;
	}

	public FileConfiguration getGuiConfig() {
    	if (guiConfig == null) {
    		guiConfig = new GuiConfig();
    	}
        return guiConfig.getFileGuiConfig();
    }

    public FileConfiguration getSellAllConfig(){
        return sellAllConfig.getFileSellAllConfig();
    }

    public FileConfiguration updateSellAllConfig() {
        // Let this like this or it won't update when you do /Sellall etc and will need a server restart.
        sellAllConfig = new SellAllConfig();
        sellAllConfig.initialize();
        return sellAllConfig.getFileSellAllConfig();
    }

    public MessagesConfig getMessagesConfig() {
    	if (messagesConfig == null) {
    		messagesConfig = MessagesConfig.get();
    	}
    	
        return messagesConfig;
    }

    public FileConfiguration getBackpacksConfig() {
        if (backpacksConfig == null){
            backpacksConfig = new BackpacksConfig();
        }

        return backpacksConfig.getFileBackpacksConfig();
    }
    
    public AutoManagerFeatures getAutoFeatures() {
    	if ( autoFeatures == null ) {
    		// None of the event listeners have been registered so auto features has not 
    		// been setup.  The following line will allow it to be setup so the setting can be accessed.
    		// The proper way to access the settings are through... 
    		//     AutoFeaturesWrapper.getInstance().getAutoFeaturesConfig()
    		autoFeatures = new AutoManagerBlockBreakEvents();
    	}
    	
		return autoFeatures;
	}

	public SellAllUtil getSellAllUtil(){
        return SellAllUtil.get();
    }

    public boolean isSellAllEnabled(){
        return isSellAllEnabled;
    }

	public void setAutoFeatures( AutoManagerFeatures autoFeatures ) {
		this.autoFeatures = autoFeatures;
	}


	/**
     * Translate alternate color codes like & codes.
     * */
    public static String format(String format){

        // This might be enabled in the future
        // Check if version's higher than 1.16
        //if (new BluesSpigetSemVerComparator().compareMCVersionTo("1.16.0") > 0){
        //    Matcher matcher = hexPattern.matcher(format);
        //    while (matcher.find()) {
        //        String color = format.substring(matcher.start(), matcher.end());
        //        format = format.replace(color, ChatColor.valueOf(color) + "");
        //        matcher = hexPattern.matcher(format);
        //    }
        //}

        return format == null ? "" : ChatColor.translateAlternateColorCodes('&', format);
    }

    public static String stripColor(String format){
    	return Text.stripColor(format);
//    	format = format(format);
    	
//    	return format == null ? null : ChatColor.stripColor(format);
    }
    
//    /**
//     * <p>bStats reporting</p>
//     * 
//     * https://github.com/Bastian/bStats-Metrics/tree/master/base/src/main/java/org/bstats/charts
//     * 
//     */
//    private void initMetricsOnLoad() {
//    	if (!getConfig().getBoolean("send-metrics", true)) {
//    		return; // Don't check if they don't want it
//    	}
//    	
//    	int pluginId = 657;
//    	bStatsMetrics = new Metrics( this, pluginId );
////    	bStatsMetrics = new PrisonMetrics( this, pluginId );
//    	
////    	Metrics metrics = new Metrics( this, pluginId );
//    }
//    
//    private void initMetricsOnEnable() {
//        if (!getConfig().getBoolean("send-metrics", true)) {
//            return; // Don't check if they don't want it
//        }
//        
//        
//        if ( bStatsMetrics == null ) {
//        	int pluginId = 657;
//        	
//        	bStatsMetrics = new Metrics( this, pluginId );
////        	bStatsMetrics = new PrisonMetrics( this, pluginId );
//        }
//
//        // Report the modules being used
//        SimpleBarChart sbcModulesUsed = new SimpleBarChart("modules_used", () -> {
//            Map<String, Integer> valueMap = new HashMap<>();
//            for (Module m : PrisonAPI.getModuleManager().getModules()) {
//                valueMap.put(m.getName(), 1);
//            }
//            return valueMap;
//        });
//        bStatsMetrics.addCustomChart( sbcModulesUsed );
//
//        // Report the API level
//        SimplePie spApiLevel = 
//                new SimplePie("api_level", () -> 
//                	"API Level " + Prison.API_LEVEL + "." + Prison.API_LEVEL_MINOR );
//        bStatsMetrics.addCustomChart( spApiLevel );
//        
//        
//        Optional<Module> prisonMinesOpt = Prison.get().getModuleManager().getModule( PrisonMines.MODULE_NAME );
//        Optional<Module> prisonRanksOpt = Prison.get().getModuleManager().getModule( PrisonRanks.MODULE_NAME );
//        
//        int mineCount = prisonMinesOpt.map(module -> ((PrisonMines) module).getMineManager().getMines().size()).orElse(0);
//        int rankCount = prisonRanksOpt.map(module -> ((PrisonRanks) module).getRankCount()).orElse(0);
//        
//        int defaultRankCount = prisonRanksOpt.map(module -> ((PrisonRanks) module).getDefaultLadderRankCount()).orElse(0);
//        int prestigesRankCount = prisonRanksOpt.map(module -> ((PrisonRanks) module).getPrestigesLadderRankCount()).orElse(0);
//        int otherRankCount = rankCount - defaultRankCount - prestigesRankCount;
//        
//        int ladderCount = prisonRanksOpt.map(module -> ((PrisonRanks) module).getladderCount()).orElse(0);
//        int playerCount = prisonRanksOpt.map(module -> ((PrisonRanks) module).getPlayersCount()).orElse(0);
//        
//        
//        
//        DrilldownPie mlcPrisonRanksAndLadders = new DrilldownPie("mines_ranks_and_ladders", () -> {
//        	Map<String, Map<String, Integer>> map = new HashMap<>();
//        	
//        	Map<String, Integer> ranks = new HashMap<>();
//        	ranks.put( Integer.toString( mineCount ), 1 );
//        	map.put( "mines", ranks );
//        	
//        	Map<String, Integer> defRanks = new HashMap<>();
//        	defRanks.put( Integer.toString( rankCount ), 1 );
//        	map.put( "ranks", defRanks );
//    	
//        	Map<String, Integer> prestigesRanks = new HashMap<>();
//        	prestigesRanks.put( Integer.toString( ladderCount ), 1 );
//        	map.put( "ladders", prestigesRanks );
//        	
//        	Map<String, Integer> otherRanks = new HashMap<>();
//        	otherRanks.put( Integer.toString( playerCount ), 1 );
//        	map.put( "players", otherRanks );
//        	
//        	return map;
//        });
//        bStatsMetrics.addCustomChart( mlcPrisonRanksAndLadders );
//        
////        MultiLineChart mlcMinesRanksAndLadders = 
////        		new MultiLineChart("mines_ranks_and_ladders", new Callable<Map<String, Integer>>() {
////            @Override
////            public Map<String, Integer> call() throws Exception {
////                Map<String, Integer> valueMap = new HashMap<>();
////                valueMap.put("mines", mineCount);
////                valueMap.put("ranks", rankCount);
////                valueMap.put("ladders", ladderCount);
////                valueMap.put("players", playerCount);
////                return valueMap;
////            }
////        });
////        bStatsMetrics.addCustomChart( mlcMinesRanksAndLadders );
//        
//        
//        
//        DrilldownPie mlcPrisonPrisonRanks = new DrilldownPie("prison_ranks", () -> {
//        	Map<String, Map<String, Integer>> map = new HashMap<>();
//        	
//        	Map<String, Integer> ranks = new HashMap<>();
//        	ranks.put( Integer.toString( rankCount ), 1 );
//        	map.put( "ranks", ranks );
//        	
//        	Map<String, Integer> defRanks = new HashMap<>();
//        	defRanks.put( Integer.toString( defaultRankCount ), 1 );
//        	map.put( "defaultRanks", defRanks );
//    	
//        	Map<String, Integer> prestigesRanks = new HashMap<>();
//        	prestigesRanks.put( Integer.toString( prestigesRankCount ), 1 );
//        	map.put( "prestigesRanks", prestigesRanks );
//        	
//        	Map<String, Integer> otherRanks = new HashMap<>();
//        	otherRanks.put( Integer.toString( otherRankCount ), 1 );
//        	map.put( "otherRanks", otherRanks );
//        	
//        	return map;
//        });
//        bStatsMetrics.addCustomChart( mlcPrisonPrisonRanks );
//        
////        MultiLineChart mlcPrisonRanks = new MultiLineChart("prison_ranks", new Callable<Map<String, Integer>>() {
////        	@Override
////        	public Map<String, Integer> call() throws Exception {
////        		Map<String, Integer> valueMap = new HashMap<>();
////        		valueMap.put("ranks", rankCount);
////        		valueMap.put("defaultRanks", defaultRankCount);
////        		valueMap.put("prestigesRanks", prestigesRankCount);
////        		valueMap.put("otherRanks", otherRankCount);
////        		return valueMap;
////        	}
////        });
////        bStatsMetrics.addCustomChart( mlcPrisonRanks );
//        
//        
//        DrilldownPie mlcPrisonPrisonLadders = new DrilldownPie("prison_ladders", () -> {
//        	Map<String, Map<String, Integer>> map = new HashMap<>();
//        	
//    		
//    		PrisonRanks pRanks = (PrisonRanks) prisonRanksOpt.orElseGet( null );
//    		for ( RankLadder ladder : pRanks.getLadderManager().getLadders() ) {
//    	
//    			Map<String, Integer> entry = new HashMap<>();
//        		entry.put( Integer.toString( ladder.getRanks().size() ), 1 );
//        		
//        		map.put( ladder.getName(), entry );
//    		}
//        	
//        	return map;
//        });
//        bStatsMetrics.addCustomChart( mlcPrisonPrisonLadders );
//        
//        
////        MultiLineChart mlcPrisonladders = new MultiLineChart("prison_ladders", new Callable<Map<String, Integer>>() {
////        	@Override
////        	public Map<String, Integer> call() throws Exception {
////        		Map<String, Integer> valueMap = new HashMap<>();
////        		
////        		PrisonRanks pRanks = (PrisonRanks) prisonRanksOpt.orElseGet( null );
////        		for ( RankLadder ladder : pRanks.getLadderManager().getLadders() ) {
////        	
////        			valueMap.put( ladder.getName(), ladder.getRanks().size() );
////        		}
////        		
////        		return valueMap;
////        	}
////        });
////        bStatsMetrics.addCustomChart( mlcPrisonladders );
//
//        TreeMap<String, RegisteredPluginsData> plugins = Prison.get().getPrisonCommands().getRegisteredPluginData();
//
//        TreeMap<String, RegisteredPluginsData> pluginsAtoE = getSubsetOfPlugins(plugins, 'a', 'f', false );
//        TreeMap<String, RegisteredPluginsData> pluginsFtoM = getSubsetOfPlugins(plugins, 'f', 'n', false );
//        TreeMap<String, RegisteredPluginsData> pluginsNtoS = getSubsetOfPlugins(plugins, 'n', 't', false );
//        TreeMap<String, RegisteredPluginsData> pluginsTto9 = getSubsetOfPlugins(plugins, 't', 'z', true );
//        
//        DrilldownPie mlcPrisonPlugins = new DrilldownPie("plugins", () -> {
//        	Map<String, Map<String, Integer>> map = new HashMap<>();
//        	
//        	for (String pluginName : plugins.keySet() ) {
//        		RegisteredPluginsData pluginData = plugins.get( pluginName );
//				
//        		Map<String, Integer> entry = new HashMap<>();
//        		entry.put( pluginData.getPluginVersion(), 1 );
//        		
//        		map.put( pluginData.getPluginName(), entry );
//			}
//        	
//        	return map;
//        });
//        bStatsMetrics.addCustomChart( mlcPrisonPlugins );
//        
//        
//        DrilldownPie mlcPrisonPluginsAtoE = new DrilldownPie("plugins_a_to_e", () -> {
//        	Map<String, Map<String, Integer>> map = new HashMap<>();
//        	
//        	for (String pluginName : pluginsAtoE.keySet() ) {
//        		RegisteredPluginsData pluginData = pluginsAtoE.get( pluginName );
//        		
//        		Map<String, Integer> entry = new HashMap<>();
//        		entry.put( pluginData.getPluginVersion(), 1 );
//        		
//        		map.put( pluginData.getPluginName(), entry );
//        	}
//        	
//        	return map;
//        });
//        bStatsMetrics.addCustomChart( mlcPrisonPluginsAtoE );
//        
//        
//        DrilldownPie mlcPrisonPluginsFtoM = new DrilldownPie("plugins_f_to_m", () -> {
//        	Map<String, Map<String, Integer>> map = new HashMap<>();
//        	
//        	for (String pluginName : pluginsFtoM.keySet() ) {
//        		RegisteredPluginsData pluginData = pluginsFtoM.get( pluginName );
//        		
//        		Map<String, Integer> entry = new HashMap<>();
//        		entry.put( pluginData.getPluginVersion(), 1 );
//        		
//        		map.put( pluginData.getPluginName(), entry );
//        	}
//        	
//        	return map;
//        });
//        bStatsMetrics.addCustomChart( mlcPrisonPluginsFtoM );
//        
//        
//        DrilldownPie mlcPrisonPluginsNtoS = new DrilldownPie("plugins_n_to_s", () -> {
//        	Map<String, Map<String, Integer>> map = new HashMap<>();
//        	
//        	for (String pluginName : pluginsNtoS.keySet() ) {
//        		RegisteredPluginsData pluginData = pluginsNtoS.get( pluginName );
//        		
//        		Map<String, Integer> entry = new HashMap<>();
//        		entry.put( pluginData.getPluginVersion(), 1 );
//        		
//        		map.put( pluginData.getPluginName(), entry );
//        	}
//        	
//        	return map;
//        });
//        bStatsMetrics.addCustomChart( mlcPrisonPluginsNtoS );
//        
//        
//        DrilldownPie mlcPrisonPluginsTto9 = new DrilldownPie("plugins_t_to_z_plus_others", () -> {
//        	Map<String, Map<String, Integer>> map = new HashMap<>();
//        	
//        	for (String pluginName : pluginsTto9.keySet() ) {
//        		RegisteredPluginsData pluginData = pluginsTto9.get( pluginName );
//        		
//        		Map<String, Integer> entry = new HashMap<>();
//        		entry.put( pluginData.getPluginVersion(), 1 );
//        		
//        		map.put( pluginData.getPluginName(), entry );
//        	}
//        	
//        	return map;
//        });
//        bStatsMetrics.addCustomChart( mlcPrisonPluginsTto9 );
//        
//        
//        
//    }

//    private TreeMap<String, RegisteredPluginsData> getSubsetOfPlugins(
//    			TreeMap<String, RegisteredPluginsData> plugins,
//    			char rangeLow, char rangeHigh,
//    			boolean includeNonAlpha ) {
//    	TreeMap<String, RegisteredPluginsData> results = new TreeMap<>();
//    	
//    	Set<String> keys = plugins.keySet();
//    	for (String key : keys) {
//			char keyFirstChar = key.toLowerCase().charAt(0);
//			
//			if ( Character.isAlphabetic(keyFirstChar) ) {
//				
//				if ( Character.compare(keyFirstChar, rangeLow) >= 0 && Character.compare( keyFirstChar, rangeHigh) < 0 ) {
//					
//					results.put( key, plugins.get(key) );
//				}
//			}
//			else {
//				
//				// Add all non-alpha plugins to this result:
//				results.put( key, plugins.get(key) );
//			}
//		}
//    	
//		return results;
//	}

	/**
     * Checks to see if there is a newer version of prison that has been released.
     * It checks based upon what is deployed to spigotmc.org.
     */
	private void initUpdater() {
        if (!getConfig().getBoolean("check-updates")) {
            return; // Don't check if they don't want it
        }
        
//        String currentVersion = getDescription().getVersion();

        SpigetUpdate updater = new SpigetUpdate(this, Prison.SPIGOTMC_ORG_PROJECT_ID);
//        SpigetUpdate updater = new SpigetUpdate(this, 1223);
        
        BluesSpigetSemVerComparator aRealSemVerComparator = new BluesSpigetSemVerComparator();
        updater.setVersionComparator( aRealSemVerComparator );
//        updater.setVersionComparator(VersionComparator.EQUAL);

        updater.checkForUpdate(new UpdateCallback() {
            @Override
            public void updateAvailable(String newVersion, String downloadUrl,
                                        boolean hasDirectDownload) {
                Alerts.getInstance().sendAlert(
                        "&3%s is now available. &7Go to the &lSpigot&r&7 page to download the latest release with new features and fixes :)",
                        newVersion);
            }

            @Override
            public void upToDate() {
                // Plugin is up-to-date
            }
        });
    }

    private void initDataDir() {
        dataDirectory = new File(getDataFolder(), "data_storage");
        if (!dataDirectory.exists()) {
            dataDirectory.mkdir();
        }
    }

    private void initCommandMap() {
        try {
            commandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMap.setAccessible(true);
            knownCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommands.setAccessible(true);
        } catch (NoSuchFieldException e) {
            getLogger().severe(
                    "&c&lReflection error: &7Ensure that you're using the latest version of Spigot and Prison.");
            e.printStackTrace();
        }
    }


	private void initIntegrations() {

		String preventEconomyVaultUsageKey = "integrations.prevent-economy-vault-usage";
		boolean preventEconomyVaultUsage = Prison.get().getPlatform().getConfigBooleanFalse(preventEconomyVaultUsageKey);
		
		if ( !preventEconomyVaultUsage ) {
			registerIntegration(new VaultEconomy());
		}
		
        registerIntegration(new EssentialsEconomy());
        registerIntegration(new SaneEconomy());
        registerIntegration(new GemsEconomy());

        registerIntegration(new VaultPermissions());
        registerIntegration(new LuckPerms5());
        registerIntegration(new LuckPermissions());

//        registerIntegration(new MVdWPlaceholderIntegration());
        registerIntegration(new PlaceHolderAPIIntegration());
        
        registerIntegration(new CustomItems());

//        registerIntegration(new WorldGuard6Integration());
//        registerIntegration(new WorldGuard7Integration());
    }

	public boolean isIntegrationRegistered( Integration integration ) {
		
    	return isPluginRegistered( integration.getProviderName() );
	}
	
	protected boolean isPluginRegistered( String pluginName ) {
		
		return Bukkit.getPluginManager().isPluginEnabled( pluginName );
	}
	
	public boolean isPluginEnabled( String pluginName ) {
		
		Plugin plug = Bukkit.getPluginManager().getPlugin( pluginName );
		
		return plug != null && plug.isEnabled();
	}
	
	/**
	 * <p>This "tries" to reload the placeholder integrations, which may not
	 * always work, and can fail.  It's here to try to do something, but
	 * it may not work.  At least we tried.
	 * </p>
	 * 
	 */
	public void reloadIntegrationsPlaceholders() {
		
//		MVdWPlaceholderIntegration ph1 = new MVdWPlaceholderIntegration();
		PlaceHolderAPIIntegration ph2 = new PlaceHolderAPIIntegration();
		
//		registerIntegration(ph1);
		registerIntegration(ph2);
		
//		ph1.deferredInitialization();
		ph2.deferredInitialization();
	}
    
    public void registerIntegration(Integration integration) {

    	boolean isRegistered = isIntegrationRegistered( integration );
    	String version = ( isRegistered ? Bukkit.getPluginManager()
    									.getPlugin( integration.getProviderName() )
    											.getDescription().getVersion() : null );
    	
    	PrisonAPI.getIntegrationManager().register(integration, isRegistered, version );
    }

    
    private void enableModulesAndCommands() {
    	
        // NOTE: Put all commands within the initModulesAndCommands() function.
        initModulesAndCommands();
        
        applyDeferredIntegrationInitializations();
        
        
        // After all the integrations have been loaded and the deferred tasks ran, 
        // then run the deferred Module setups:
        initDeferredModules();

    }
    
    private void disableModulesAndCommands() {
    	
    	for ( Module module : Prison.get().getModuleManager().getModules() ) {
    		if ( module.isEnabled() ) {
    			module.disable();
    		}
    	}
    	
    	Prison.get().getCommandHandler().getAllRegisteredCommands();
    }
    
    public void resetModulesAndCommands() {
    	
    	disableModulesAndCommands();
    	
    	enableModulesAndCommands();
    }
    
    /**
     * This function registers all of the modules in prison.  It should also manage
     * the registration of "extra" commands that are outside of the modules, such
     * as gui related commands.
     * 
     */
    private void initModulesAndCommands() {

        YamlConfiguration modulesConf = loadConfig("modules.yml");
        
        boolean isMinesEnabled = false;
        boolean isRanksEnabled = false;

        // TODO: This business logic needs to be moved to the Module Manager:
        if (modulesConf.getBoolean("mines")) {
        	isMinesEnabled = true;
        	
            Prison.get().getModuleManager()
                    .registerModule(new PrisonMines(getDescription().getVersion()));

            // The GUI handler for mines... cannot be hooked up here:
//            Prison.get().getCommandHandler().registerCommands( new PrisonSpigotMinesCommands() );
            
        } else {
            Output.get().logInfo("&7Modules: &cPrison Mines are disabled and were not Loaded. ");
            Output.get().logInfo("&7  Prison Mines have been disabled in &2plugins/Prison/modules.yml&7.");
            Prison.get().getModuleManager().getDisabledModules().add( PrisonMines.MODULE_NAME );
        }

        if (modulesConf.getBoolean("ranks") ) {
        	PrisonRanks rankModule = new PrisonRanks(getDescription().getVersion() );
        	
        	// Register and enable Ranks:
            Prison.get().getModuleManager().registerModule( rankModule );

            if ( rankModule.isEnabled() && PrisonAPI.getIntegrationManager().hasForType(IntegrationType.ECONOMY) ) {
            	
            	isRanksEnabled = true;
            }
        } 
        else {
        	Output.get().logInfo("&3Modules: &cPrison Ranks, Ladders, and Players are disabled and were not Loaded. ");
        	Output.get().logInfo("&7  Prestiges cannot be enabled without ranks being enabled. ");
        	Output.get().logInfo("&7  Prison Ranks have been disabled in &2plugins/Prison/modules.yml&7.");
        	Prison.get().getModuleManager().getDisabledModules().add( PrisonRanks.MODULE_NAME );
        }
        
       
        // If the sellall module is defined in modules.yml, then use that setting, otherwise
        // use the sellall config setting within the config.yml file.
        String sellallModuleName = PrisonSellall.MODULE_NAME.toLowerCase();
        boolean isDefined = modulesConf.contains(sellallModuleName);
        
        // First check to see if the module is enabled (sellall):
        if ( isDefined && modulesConf.getBoolean(sellallModuleName) ||
        		// if not, then check to see if sellall is enabled within config.yml:
        		!isDefined && getConfig().contains("sellall") && getConfig().isBoolean("sellall") ) {
        		
        	PrisonSellall sellallModule = new PrisonSellall(getDescription().getVersion() );
        	
        	// Register and enable the sellall module:
            Prison.get().getModuleManager().registerModule( sellallModule );
            
            Prison.get().getCommandHandler().registerCommands( new SellallCommands() );
            

            isSellAllEnabled = true;
            
            
            // If sellall is enabled, then allow it to initialize.
//            if (isSellAllEnabled){
            	SellAllUtil.get();
//            }

            // Load sellAll if enabled
//            if (isSellAllEnabled){
            	Prison.get().getCommandHandler().registerCommands( new PrisonSpigotSellAllCommands() );
//            }

        } 
        else {
        	Output.get().logInfo("&3Modules: &cPrison sellall module is disabled and was not Loaded. ");
        	Prison.get().getModuleManager().getDisabledModules().add( PrisonSellall.MODULE_NAME );
        }
        

        
        // The following linkMinesAndRanks() function must be called only after the 
        // Module deferred tasks are ran.
//        // Try to load the mines and ranks that have the ModuleElement placeholders:
//        // Both the mine and ranks modules must be enabled.
//        if (modulesConf.getBoolean("mines") && modulesConf.getBoolean("ranks")) {
//        	linkMinesAndRanks();
//        }

        
        // Do not enable sellall if ranks is not loaded since it uses player ranks:
        if ( isRanksEnabled ) {
        	
        	// enable under GUI:
//        	Prison.get().getCommandHandler().registerCommands( new PrisonSpigotRanksCommands() );
        	
//        	// NOTE: If ranks module is enabled, then try to register prestiges commands if enabled:
//        	if ( isPrisonConfig( "prestiges") || isPrisonConfig( "prestige.enabled" ) ) {
//        		// Enable the setup of the prestige related commands only if prestiges is enabled:
//        		Prison.get().getCommandHandler().registerCommands( new PrisonSpigotPrestigeCommands() );
//        	}
        	
            
        }

        // Load backpacks commands if enabled
        if (isBackPacksEnabled){
        	Prison.get().getCommandHandler().registerCommands( new PrisonSpigotBackpackCommands() );
        }

        
        // The following will enable all of the GUI related commands.  It's important that they
        // cannot be enabled elsewhere, or at least the 'prison-gui-enabled' must control 
        // their registration:
        if ( Prison.get().getPlatform().getConfigBooleanFalse( "prison-gui-enabled" ) ) {

        	// Prison's primary GUI commands:
        	Prison.get().getCommandHandler().registerCommands( new PrisonSpigotGUICommands() );
        	
        	
        	if ( isMinesEnabled ) {
              // The GUI handler for mines... cannot be hooked up here:
              Prison.get().getCommandHandler().registerCommands( new PrisonSpigotMinesCommands() );
        	}
        	
        	
        	if ( isRanksEnabled ) {
        		Prison.get().getCommandHandler().registerCommands( new PrisonSpigotRanksCommands() );
        		
            	// NOTE: If ranks module is enabled, then try to register prestiges commands if enabled:
            	if ( isPrisonConfig( "prestiges") || isPrisonConfig( "prestige.enabled" ) ) {
            		// Enable the setup of the prestige related commands only if prestiges is enabled:
            		Prison.get().getCommandHandler().registerCommands( new PrisonSpigotPrestigeCommands() );
            	}
        	}
        	
        	
        	if ( isBackPacksEnabled ) {
        		Prison.get().getCommandHandler().registerCommands( new PrisonSpigotGUIBackPackCommands() );	
        	}
        	
        	
        	if ( isSellAllEnabled ) {
        		Prison.get().getCommandHandler().registerCommands( new PrisonSpigotGUISellAllCommands() );	
        	}
        }
        
//        // This registers the admin's /gui commands
//        // GUI commands were updated to prevent use of ranks commands when ranks module is not loaded.
//        if (getConfig().getString("prison-gui-enabled").equalsIgnoreCase("true")) {
//        }

        
        // Register prison utility commands:
        if (modulesConf.getBoolean("utils.enabled", true)) {
            Prison.get().getModuleManager()
                    .registerModule(new PrisonUtilsModule(getDescription().getVersion(), modulesConf));

//            Prison.get().getCommandHandler().registerCommands( new PrisonSpigotMinesCommands() );
            
        } else {
            Output.get().logInfo("&7Modules: &cPrison Utils are disabled and were not Loaded. ");
            Output.get().logInfo("&7  Prison Utils have been disabled in &2plugins/Prison/modules.yml&7.");
            Prison.get().getModuleManager().getDisabledModules().add( PrisonUtilsModule.MODULE_NAME );
        }
        
        
    }


    /**
     * This will initialize any process that has been setup in the modules that
     * needs to be ran after all of the integrations have been fully loaded and initialized.
     *  
     */
    private void initDeferredModules() {
    	
    	for ( Module module : Prison.get().getModuleManager().getModules() ) {
    		
    		module.deferredStartup();
    	}
    	
    	
    	// Reload placeholders:
    	Prison.get().getPlatform().getPlaceholders().reloadPlaceholders();
    	
    	
    	// Finally link mines and ranks if both are loaded:
    	linkMinesAndRanks();
    }
    
    
    /**
     * Try to link the mines and ranks that have the ModuleElement placeholders:
     * Both the mine and ranks modules must be enabled to try to link them all
     * together.
     */
    private void linkMinesAndRanks() {

    	
    	if (PrisonRanks.getInstance() != null && PrisonRanks.getInstance().isEnabled() && 
    			PrisonMines.getInstance() != null && PrisonMines.getInstance().isEnabled()) {

    		RankManager rm = PrisonRanks.getInstance().getRankManager();
    		MineManager mm = PrisonMines.getInstance().getMineManager();

    		// go through all mines and link them to the Ranks and link that
    		// rank back to the mine. 
    		// So just by linking mines, will also link all of the ranks too.
    		// It's important to understand the primary source is within the Mine 
    		// since a mine can only have one rank.
    		rm.getRanks();
    		mm.getMines();

    		int count = 0;
    		for (Mine mine : mm.getMines()) {
				if ( mine.getRank() == null && mine.getRankString() != null ) {
					String[] rParts = mine.getRankString().split( "," );
					
					if (rParts.length > 2) {
						ModuleElementType meType = ModuleElementType.fromString( rParts[0] );
						String rankName = rParts[1];
						
						if (meType == ModuleElementType.RANK) {
							Rank rank = rm.getRank(rankName);
							
							if (rank != null) {
								mine.setRank(rank);
								rank.getMines().add(mine);
								count++;
							}
						}
					}
				}
			}
    		Output.get().logInfo("A total of %s Mines and Ranks have been linked together.", Integer.toString(count));
    	}
	}

	private void applyDeferredIntegrationInitializations() {
    	for ( Integration deferredIntegration : PrisonAPI.getIntegrationManager().getDeferredIntegrations() ) {
    		
    		try {
    			if ( deferredIntegration.isRegistered() && deferredIntegration.hasIntegrated() ) {
    				
    				deferredIntegration.deferredInitialization();
    			}
    		}
    		catch ( Exception e ) {
    			

    			PrisonAPI.getIntegrationManager().removeIntegration( deferredIntegration );
    			
        		Output.get().logWarn( 
        				String.format( "Warning: An integration failed during deferred integration. " +
        				"Disabling the integration to protect Prison: %s %s %s[%s]", 
        				deferredIntegration.getKeyName(), deferredIntegration.getVersion(),
        				(deferredIntegration.getDebugInfo() == null ? 
        							"no debug info" : deferredIntegration.getDebugInfo()) ));
    			
    		}

    	}
    }
    
    public SpigotScheduler getScheduler() {
		return scheduler;
	}

	public Compatibility getCompatibility() {
    	return compatibility;
    }
    
    private File getBundledFile(String name) {
        getDataFolder().mkdirs();
        File file = new File(getDataFolder(), name);
        if (!file.exists()) {
            saveResource(name, false);
        }
        return file;
    }

    public YamlConfiguration loadConfig(String file) {
        return YamlConfiguration.loadConfiguration(getBundledFile(file));
    }
    
    public void saveConfig(String fileName, YamlConfiguration config ) {
    	if ( config != null ) {
    		File file = getBundledFile(fileName);
    		try {
				config.save( file );
			} 
    		catch (IOException e) {
    			String message = String.format( "Error saving config file: %s  [%s]", 
    					file.getAbsoluteFile(), e.getMessage() );
    			
    			Output.get().logError( message );
			}
    	}
    }

    File getDataDirectory() {
        return dataDirectory;
    }
    
    public boolean isPrisonConfig( String configId ) {

    	String config = SpigotPrison.getInstance().getConfig().getString( configId );
    	boolean results = config != null && config.equalsIgnoreCase( "true" );
    	return results;
    }
    
    /**
     * Setup hooks in to the valid prison block types.  This will be only the 
     * block types that have tested to be valid on the server that is running 
     * prison.  This provides full compatibility to the admins that if a block 
     * is listed, then it's usable.  No more guessing or finding out after the 
     * fact that a block that was used was invalid for their version of minecraft.
     */
	public PrisonBlockTypes getPrisonBlockTypes() {
		
		if ( prisonBlockTypes == null ) {
			synchronized ( PrisonBlockTypes.class ) {
				if ( prisonBlockTypes == null ) {
					
					// Setup hooks in to the valid prison block types, which must be done
					// after all the integrations have been initialized.  This will be only
					// the block types that have tested to be valid on the server that is
					// running prison.  This provides full compatibility to the admins that
					// if a block is listed, then it's usable.  No more guessing or finding
					// out after the fact that a block that was used was invalid for
					// their version of minecraft.
					PrisonBlockTypes pbt = new PrisonBlockTypes();
					pbt.addBlockTypes( SpigotUtil.getAllPlatformBlockTypes() );
					pbt.addBlockTypes( SpigotUtil.getAllCustomBlockTypes() );
					this.prisonBlockTypes = pbt;
					
				}
			}
		}
		
		return prisonBlockTypes;
	}

	public List<Listener> getRegisteredBlockListeners() {
		return registeredBlockListeners;
	}

}
