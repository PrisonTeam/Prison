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
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.update.spiget.SpigetUpdate;
import org.inventivetalent.update.spiget.UpdateCallback;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.PrisonCommand;
import tech.mcprison.prison.alerts.Alerts;
import tech.mcprison.prison.integration.Integration;
import tech.mcprison.prison.internal.block.PrisonBlockTypes;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.managers.MineManager;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.modules.ModuleElementType;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.LogLevel;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.managers.RankManager;
import tech.mcprison.prison.spigot.autofeatures.AutoManager;
import tech.mcprison.prison.spigot.autofeatures.AutoManagerFeatures;
import tech.mcprison.prison.spigot.commands.PrisonSpigotBackpackCommands;
import tech.mcprison.prison.spigot.backpacks.BackpacksListeners;
import tech.mcprison.prison.spigot.block.OnBlockBreakEventListener;
import tech.mcprison.prison.spigot.commands.PrisonSpigotGUICommands;
import tech.mcprison.prison.spigot.commands.PrisonSpigotMinesCommands;
import tech.mcprison.prison.spigot.commands.PrisonSpigotPrestigeCommands;
import tech.mcprison.prison.spigot.commands.PrisonSpigotRanksCommands;
import tech.mcprison.prison.spigot.compat.Compatibility;
import tech.mcprison.prison.spigot.compat.Spigot113;
import tech.mcprison.prison.spigot.compat.Spigot18;
import tech.mcprison.prison.spigot.compat.Spigot19;
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
import tech.mcprison.prison.spigot.placeholder.MVdWPlaceholderIntegration;
import tech.mcprison.prison.spigot.placeholder.PlaceHolderAPIIntegration;
import tech.mcprison.prison.spigot.sellall.SellAllPrisonCommands;
import tech.mcprison.prison.spigot.slime.SlimeBlockFunEventListener;
import tech.mcprison.prison.spigot.spiget.BluesSpigetSemVerComparator;
import tech.mcprison.prison.spigot.utils.PrisonUtilsModule;

/**
 * The plugin class for the Spigot implementation.
 *
 * @author Faizaan A. Datoo
 * @author GABRYCA
 */
public class SpigotPrison extends JavaPlugin {

    Field commandMap;
    Field knownCommands;
    SpigotScheduler scheduler;
    Compatibility compatibility;
    boolean debug = false;

    private File dataDirectory;
//    private boolean doAlertAboutConvert = false;
    
    private AutoManagerFeatures autoFeatures = null;
//    private FileConfiguration autoFeaturesConfig = null;
    
    private MessagesConfig messagesConfig;
    private GuiConfig guiConfig;
    private SellAllConfig sellAllConfig;
    private BackpacksConfig backpacksConfig;

    private PrisonBlockTypes prisonBlockTypes;

    private static SpigotPrison config;
    private static boolean isBackPacksEnabled = false;

    public static SpigotPrison getInstance(){
        return config;
    }

//  ###Tab-Complete###
//  private TreeSet<String> registeredCommands = new TreeSet<>();
       

//    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onLoad() {
    	
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
    	config = this;
        this.saveDefaultConfig();
        debug = getConfig().getBoolean("debug", false);

        initDataDir();
        initCommandMap();
        initCompatibility();
        initUpdater();
        
        this.scheduler = new SpigotScheduler(this);

        Prison.get().init(new SpigotPlatform(this), Bukkit.getVersion());
        Prison.get().getLocaleManager().setDefaultLocale(getConfig().getString("default-language", "en_US"));
        
        // Manually register Listeners with Bukkit:
        Bukkit.getPluginManager().registerEvents(new ListenersPrisonManager(),this);
        Bukkit.getPluginManager().registerEvents(new AutoManager(), this);
        Bukkit.getPluginManager().registerEvents(new OnBlockBreakEventListener(), this);
        Bukkit.getPluginManager().registerEvents(new SlimeBlockFunEventListener(), this);
        Bukkit.getPluginManager().registerEvents(new SpigotListener(), this);

        try {
            isBackPacksEnabled = getInstance().getConfig().getString("backpacks").equalsIgnoreCase("true");
        } catch (NullPointerException ignored){}

        if (isBackPacksEnabled){
            Bukkit.getPluginManager().registerEvents(new BackpacksListeners(), this);
        }

        initIntegrations();
        
        // NOTE: Put all commands within the initModulesAndCommands() function.
        initModulesAndCommands();
        
        applyDeferredIntegrationInitializations();
        
        
        // After all the integrations have been loaded and the deferred tasks ran, 
        // then run the deferred Module setups:
        initDeferredModules();
        
        
        initMetrics();
        
        
        // These stats are displayed within the initDeferredModules():
        //Prison.get().getPlatform().getPlaceholders().printPlaceholderStats();
        
        
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

		ChatDisplay cdVersion = cmdVersion.displayVersion();
		cdVersion.toLog( LogLevel.INFO );
		
		// Provides a startup test of blocks available for the version of spigot that being used:
		if ( getConfig().getBoolean("prison-block-compatibility-report") ) {
			SpigotUtil.testAllPrisonBlockTypes();
		}
		
		Output.get().logInfo( "Prison - Finished loading." );
    }

    @Override
    public void onDisable() {
    	if (this.scheduler != null ) {
    		this.scheduler.cancelAll();
    	}
    	
    	Prison.get().getPlatform().unregisterAllCommands();
    	
    	Prison.get().deinit();
    }

    public FileConfiguration getGuiConfig() {
    	if (guiConfig == null) {
    		guiConfig = new GuiConfig();
    	}
        return guiConfig.getFileGuiConfig();
    }

    public FileConfiguration getSellAllConfig() {
        // Let this like this or it wont update when you do /Sellall etc and will need a server restart.
        sellAllConfig = new SellAllConfig();
        sellAllConfig.initialize();
        return sellAllConfig.getFileSellAllConfig();
    }

    public FileConfiguration getMessagesConfig() {
    	if (messagesConfig == null) {
    		messagesConfig = new MessagesConfig();
    	}
    	
        return messagesConfig.getFileGuiMessagesConfig();
    }

    public FileConfiguration getBackpacksConfig() {
        if (backpacksConfig == null){
            backpacksConfig = new BackpacksConfig();
        }

        return backpacksConfig.getFileBackpacksConfig();
    }
    
    public AutoManagerFeatures getAutoFeatures() {
		return autoFeatures;
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
    	format = format(format);
    	
    	return format == null ? null : ChatColor.stripColor(format);
    }
    
    private void initMetrics() {
        if (!getConfig().getBoolean("send-metrics", true)) {
            return; // Don't check if they don't want it
        }
        Metrics metrics = new Metrics(this);

        // Report the modules being used
        metrics.addCustomChart(new Metrics.SimpleBarChart("modules_used", () -> {
            Map<String, Integer> valueMap = new HashMap<>();
            for (Module m : PrisonAPI.getModuleManager().getModules()) {
                valueMap.put(m.getName(), 1);
            }
            return valueMap;
        }));

        // Report the API level
        metrics.addCustomChart(
                new Metrics.SimplePie("api_level", () -> "API Level " + Prison.API_LEVEL));
        
        Optional<Module> prisonMinesOpt = Prison.get().getModuleManager().getModule( PrisonMines.MODULE_NAME );
        Optional<Module> prisonRanksOpt = Prison.get().getModuleManager().getModule( PrisonRanks.MODULE_NAME );
        
        int mineCount = prisonMinesOpt.map(module -> ((PrisonMines) module).getMineManager().getMines().size()).orElse(0);
        int rankCount = prisonRanksOpt.map(module -> ((PrisonRanks) module).getRankCount()).orElse(0);
        int ladderCount = prisonRanksOpt.map(module -> ((PrisonRanks) module).getladderCount()).orElse(0);
        
        metrics.addCustomChart(new Metrics.MultiLineChart("mines_ranks_and_ladders", new Callable<Map<String, Integer>>() {
            @Override
            public Map<String, Integer> call() throws Exception {
                Map<String, Integer> valueMap = new HashMap<>();
                valueMap.put("mines", mineCount);
                valueMap.put("ranks", rankCount);
                valueMap.put("ladders", ladderCount);
                return valueMap;
            }
        }));
    }

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

    private void initCompatibility() {
    	
    	if ( new BluesSpigetSemVerComparator().compareMCVersionTo("1.9.0") < 0 ) {
            compatibility = new Spigot18();
        } 
    	else if ( new BluesSpigetSemVerComparator().compareMCVersionTo("1.13.0") < 0 ) {
            compatibility = new Spigot19();
        }
    	else {
    		compatibility = new Spigot113();
    	}

        getLogger().info("Using version adapter " + compatibility.getClass().getName());
    }

	private void initIntegrations() {

    	registerIntegration(new VaultEconomy());
        registerIntegration(new EssentialsEconomy());
        registerIntegration(new SaneEconomy());
        registerIntegration(new GemsEconomy());

        registerIntegration(new VaultPermissions());
        registerIntegration(new LuckPerms5());
        registerIntegration(new LuckPermissions());

        registerIntegration(new MVdWPlaceholderIntegration());
        registerIntegration(new PlaceHolderAPIIntegration());
        
        registerIntegration(new CustomItems());

//        registerIntegration(new WorldGuard6Integration());
//        registerIntegration(new WorldGuard7Integration());
    }

	
	/**
	 * <p>This "tries" to reload the placeholder integrations, which may not
	 * always work, and can fail.  It's here to try to do something, but
	 * it may not work.  At least we tried.
	 * </p>
	 * 
	 */
	public void reloadIntegrationsPlaceholders() {
		
		MVdWPlaceholderIntegration ph1 = new MVdWPlaceholderIntegration();
		PlaceHolderAPIIntegration ph2 = new PlaceHolderAPIIntegration();
		
		registerIntegration(ph1);
		registerIntegration(ph2);
		
		ph1.deferredInitialization();
		ph2.deferredInitialization();
	}
    
    private void registerIntegration(Integration integration) {

    	boolean isRegistered = Bukkit.getPluginManager().isPluginEnabled(integration.getProviderName());
    	String version = ( isRegistered ? Bukkit.getPluginManager()
    									.getPlugin( integration.getProviderName() )
    											.getDescription().getVersion() : null );
    	
    	PrisonAPI.getIntegrationManager().register(integration, isRegistered, version );
    }

    /**
     * This function registers all of the modules in prison.  It should also manage
     * the registration of "extra" commands that are outside of the modules, such
     * as gui related commands.
     * 
     */
    private void initModulesAndCommands() {

        YamlConfiguration modulesConf = loadConfig("modules.yml");

        // TODO: This business logic needs to be moved to the Module Manager:
        if (modulesConf.getBoolean("mines")) {
            Prison.get().getModuleManager()
                    .registerModule(new PrisonMines(getDescription().getVersion()));

            Prison.get().getCommandHandler().registerCommands( new PrisonSpigotMinesCommands() );
            
        } else {
            Output.get().logInfo("&7Modules: &cPrison Mines are disabled and were not Loaded. ");
            Output.get().logInfo("&7  Prison Mines have been disabled in &2plugins/Prison/modules.yml&7.");
            Prison.get().getModuleManager().getDisabledModules().add( PrisonMines.MODULE_NAME );
        }

        if (modulesConf.getBoolean("ranks")) {
            Prison.get().getModuleManager()
                    .registerModule(new PrisonRanks(getDescription().getVersion()));

            Prison.get().getCommandHandler().registerCommands( new PrisonSpigotRanksCommands() );
            
            // NOTE: If ranks module is enabled, then try to register prestiges commands if enabled:
            if ( isPrisonConfig( "prestiges") || isPrisonConfig( "prestige.enabled" ) ) {
            	// Enable the setup of the prestige related commands only if prestiges is enabled:
            	Prison.get().getCommandHandler().registerCommands( new PrisonSpigotPrestigeCommands() );
            }
            
        } else {
        	Output.get().logInfo("&3Modules: &cPrison Ranks, Ladders, and Players are disabled and were not Loaded. ");
        	Output.get().logInfo("&7  Prestiges cannot be enabled without ranks being enabled. ");
        	Output.get().logInfo("&7  Prison Ranks have been disabled in &2plugins/Prison/modules.yml&7.");
        	Prison.get().getModuleManager().getDisabledModules().add( PrisonRanks.MODULE_NAME );
        }
        
        
        // The following linkMinesAndRanks() function must be called only after the 
        // Module deferred tasks are ran.
//        // Try to load the mines and ranks that have the ModuleElement placeholders:
//        // Both the mine and ranks modules must be enabled.
//        if (modulesConf.getBoolean("mines") && modulesConf.getBoolean("ranks")) {
//        	linkMinesAndRanks();
//        }

        // Load sellAll if enabled
        if (SellAllPrisonCommands.isEnabled()){
            Prison.get().getCommandHandler().registerCommands(new SellAllPrisonCommands());
        }

        // Load backpacks commands if enabled
        if (isBackPacksEnabled){
            Prison.get().getCommandHandler().registerCommands(new PrisonSpigotBackpackCommands());
        }

        // This registers the admin's /gui commands
        if (getConfig().getString("prison-gui-enabled").equalsIgnoreCase("true")) {
            Prison.get().getCommandHandler().registerCommands(new PrisonSpigotGUICommands());
        }

        
        // Register prison utility commands:
        if (modulesConf.getBoolean("utils.enabled", true)) {
            Prison.get().getModuleManager()
                    .registerModule(new PrisonUtilsModule(getDescription().getVersion(), modulesConf));

            Prison.get().getCommandHandler().registerCommands( new PrisonSpigotMinesCommands() );
            
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
    		deferredIntegration.deferredInitialization();
    	}
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

    private YamlConfiguration loadConfig(String file) {
        return YamlConfiguration.loadConfiguration(getBundledFile(file));
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
}
