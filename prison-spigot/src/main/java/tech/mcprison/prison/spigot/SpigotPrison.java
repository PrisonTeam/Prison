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

package tech.mcprison.prison.spigot;

import java.io.File;
import java.io.IOException;
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
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.LogLevel;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.spigot.autoFeatures.AutoFeaturesFileConfig;
import tech.mcprison.prison.spigot.autoFeatures.AutoManager;
import tech.mcprison.prison.spigot.block.OnBlockBreakEventListener;
import tech.mcprison.prison.spigot.commands.PrestigesCommands;
import tech.mcprison.prison.spigot.commands.PrisonGuiCommands;
import tech.mcprison.prison.spigot.commands.PrestigesPrestigeCommand;
import tech.mcprison.prison.spigot.commands.PrisonSpigotCommands;
import tech.mcprison.prison.spigot.compat.Compatibility;
import tech.mcprison.prison.spigot.compat.Spigot18;
import tech.mcprison.prison.spigot.compat.Spigot19;
import tech.mcprison.prison.spigot.economies.EssentialsEconomy;
import tech.mcprison.prison.spigot.economies.GemsEconomy;
import tech.mcprison.prison.spigot.economies.SaneEconomy;
import tech.mcprison.prison.spigot.economies.VaultEconomy;
import tech.mcprison.prison.spigot.gui.GUIListener;
import tech.mcprison.prison.spigot.gui.GuiConfig;
import tech.mcprison.prison.spigot.gui.ListenersPrisonManagerGUI;
import tech.mcprison.prison.spigot.permissions.LuckPermissions;
import tech.mcprison.prison.spigot.permissions.LuckPerms5;
import tech.mcprison.prison.spigot.permissions.VaultPermissions;
import tech.mcprison.prison.spigot.placeholder.MVdWPlaceholderIntegration;
import tech.mcprison.prison.spigot.placeholder.PlaceHolderAPIIntegration;
import tech.mcprison.prison.spigot.player.SlimeBlockFunEventListener;
import tech.mcprison.prison.spigot.spiget.BluesSpigetSemVerComparator;

/**
 * The plugin class for the Spigot implementation.
 *
 * @author Faizaan A. Datoo
 */
public class SpigotPrison extends JavaPlugin {

    Field commandMap;
    Field knownCommands;
    SpigotScheduler scheduler;
    Compatibility compatibility;
    boolean debug = false;

    private File dataDirectory;
    private boolean doAlertAboutConvert = false;
    
    private FileConfiguration autoFeaturesConfig = null;

    private static SpigotPrison config;

    public static SpigotPrison getInstance(){
        return config;
    }

//  ###Tab-Complete###
//  private TreeSet<String> registeredCommands = new TreeSet<>();
       

//    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onLoad() {
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
        new GuiConfig();

        GUIListener.get().init(this);
        Bukkit.getPluginManager().registerEvents(new ListenersPrisonManagerGUI(),this);
        Bukkit.getPluginManager().registerEvents(new AutoManager(), this);
        Bukkit.getPluginManager().registerEvents(new OnBlockBreakEventListener(), this);
        Bukkit.getPluginManager().registerEvents(new SlimeBlockFunEventListener(), this);



        getCommand("prestige").setExecutor(new PrestigesPrestigeCommand());
        getCommand("prestiges").setExecutor(new PrestigesCommands());
        getCommand("prisonmanager").setExecutor(new PrisonSpigotCommands());
        
        new SpigotListener(this).init();
        getAutoFeaturesConfig();

        Prison.get().getCommandHandler().registerCommands(new PrisonGuiCommands());
        
        initIntegrations();
        initModules();

        applyDeferredIntegrationInitializations();
        
        extractCommandsForAutoComplete();
        
        initMetrics();

        if (doAlertAboutConvert) {
            Alerts.getInstance().sendAlert(
                    "&7An old installation of Prison has been detected. &3Type /prison convert to convert your old data automatically. &7If you already converted, delete the 'Prison.old' folder so that we stop nagging you.");
        }
        
        
        // Finally print the version after loading the prison plugin:
        PrisonCommand cmdVersion = Prison.get().getPrisonCommands();
        
//        // Store all loaded plugins within the PrisonCommand for later inclusion:
//        for ( Plugin plugin : Bukkit.getPluginManager().getPlugins() ) {
//        	String name = plugin.getName();
//        	String version = plugin.getDescription().getVersion();
//        	String value = "&7" + name + " &3(&a" + version + "&3)";
//        	cmdVersion.getRegisteredPlugins().add( value );
//		}

		ChatDisplay cdVersion = cmdVersion.displayVersion();
		cdVersion.toLog( LogLevel.INFO );
		
		Output.get().logInfo( "Prison - Finished loading." );
    }

    @Override
    public void onDisable() {
        this.scheduler.cancelAll();
        Prison.get().deinit();
    }

    public static FileConfiguration getGuiConfig(){
        GuiConfig messages = new GuiConfig();
        return messages.getFileGuiConfig();
    }

    /**
     * <p>This lazy loading of the FileConfiguration for the AutoFeatures will ensure
     * the file is loaded from the file system only one time and only when it is first
     * used.  This ensures that if it is never used, it is never loaded in to memory.
     * </p>
     * 
     * @return
     */
    public FileConfiguration getAutoFeaturesConfig() {
    	if ( this.autoFeaturesConfig == null ) {
    		AutoFeaturesFileConfig afc = new AutoFeaturesFileConfig();
    		this.autoFeaturesConfig = afc.getConfig();
    	}
        return autoFeaturesConfig;
    }
    
    
    public boolean saveAutoFeaturesConfig() {
    	boolean success = false;
    	FileConfiguration afConfig = getAutoFeaturesConfig();
  
    	if ( afConfig != null ) {
    		AutoFeaturesFileConfig afc = new AutoFeaturesFileConfig();
    		success = afc.saveConf(afConfig);
    	}
    	return success;
    }
    
    

    public static String format(String format){
        return ChatColor.translateAlternateColorCodes('&', format);
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
        
        int mineCount = !prisonMinesOpt.isPresent() ? 0 : ((PrisonMines) prisonMinesOpt.get()).getMineManager().getMines().size();
        int rankCount = !prisonRanksOpt.isPresent() ? 0 : ((PrisonRanks) prisonRanksOpt.get()).getRankCount();
        int ladderCount = !prisonRanksOpt.isPresent() ? 0 : ((PrisonRanks) prisonRanksOpt.get()).getladderCount();
        
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
        String[] version = Bukkit.getVersion().split("\\.");
        int minorVersionInt = 9;
        try {
            minorVersionInt = Integer.parseInt(version[1]);
        } catch (NumberFormatException e) {
            try {
                minorVersionInt =
                        Integer.parseInt(version[1].substring(0, version[1].indexOf(')')));
            } catch (Exception ex) {
                Output.get().logError(
                        "Unable to determine server version. Assuming spigot 1.9 or greater.");
            }
        }

        if (minorVersionInt <= 8) {
            compatibility = new Spigot18();
        } else {
            compatibility = new Spigot19();
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
    }
    
    private void registerIntegration(Integration integration) {
    	integration.setRegistered( Bukkit.getPluginManager().isPluginEnabled(integration.getProviderName()) );

    	integration.integrate();
		
    	PrisonAPI.getIntegrationManager().register(integration);
    }

    private void initModules() {
        YamlConfiguration modulesConf = loadConfig("modules.yml");

        if (modulesConf.getBoolean("mines")) {
            Prison.get().getModuleManager()
                    .registerModule(new PrisonMines(getDescription().getVersion()));
        } else {
            Output.get().logInfo("Not loading mines because it's disabled in modules.yml.");
        }

        if (modulesConf.getBoolean("ranks")) {
            Prison.get().getModuleManager()
                    .registerModule(new PrisonRanks(getDescription().getVersion()));
        } else {
            Output.get().logInfo("Not loading ranks because it's disabled in modules.yml");
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
    

    /**
     * <p>This function will register any missing "command" and will
     * set the usable onTabComplete to the one within this class, 
     * that follows this function.  
     * </p>
     * 
     */
    private void extractCommandsForAutoComplete() {
/*
 * ###Tab-Complete### (search for other occurrences of this tag)
 * 
 * The following works up to a certain point, but is disabled until 
 * a full solution can be implemented.
 * 
		List<String> commandKeys = Prison.get().getCommandHandler().getRootCommandKeys();
    	
    	registeredCommands.clear();
    	registeredCommands.addAll( commandKeys );
    	
    	// commands are already broken down to elements with roots: Keep the following
    	// just in case we need to expand with other uses:
    	for ( String cmdKey : commandKeys ) {
    		
    		Output.get().logInfo( "SpigotPrison.extractCommandsForAutoComplete: Command: %s", cmdKey );
    		
    		Optional<tech.mcprison.prison.commands.PluginCommand> registeredCommand = Prison.get().getPlatform().getCommand(cmdKey);
    		if ( !registeredCommand.isPresent() ) {
    			tech.mcprison.prison.commands.PluginCommand  rootPcommand = new tech.mcprison.prison.commands.PluginCommand(cmdKey, "--", "/" + cmdKey);
    			Prison.get().getPlatform().registerCommand(rootPcommand);
    		}
    		
    		PluginCommand pCommand = this.getCommand(cmdKey);
    		if ( pCommand != null ) {
    			pCommand.setTabCompleter(this);
    		} else {
    			Output.get().logInfo( "SpigotPrison.extractCommandsForAutoComplete: " +
				"## Error not found ## Command: %s ", cmdKey );
    		}
		}
 */
    	
	}
/*
 * ###Tab-Complete###
 * 
 * This function is disabled until tab complete can be fully implemented.
 * 
 * @see org.bukkit.plugin.java.JavaPlugin#onTabComplete(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
 *
 *  // Not being used...
	@Override
	public List<String> onTabComplete( CommandSender sender, Command command, String alias, String[] args )
	{
		List<String> results = new ArrayList<>();
		Output.get().logInfo( "SpigotPrison.onTabComplete: Command: %s :: %s", command.getLabel(), command.getName() );

		// Map<String, Map<String, Object>> cmds = getDescription().getCommands();
		
//		registeredCommands
		
		return results;
	}
 */
    
}
