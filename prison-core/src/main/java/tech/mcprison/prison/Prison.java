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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import com.google.common.eventbus.EventBus;

import tech.mcprison.prison.alerts.Alerts;
import tech.mcprison.prison.commands.CommandHandler;
import tech.mcprison.prison.error.ErrorManager;
import tech.mcprison.prison.integration.IntegrationManager;
import tech.mcprison.prison.internal.platform.Platform;
import tech.mcprison.prison.localization.LocaleManager;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.modules.ModuleManager;
import tech.mcprison.prison.modules.PluginEntity;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.placeholders.PlaceholderManager;
import tech.mcprison.prison.placeholders.PlaceholdersUtil;
import tech.mcprison.prison.selection.SelectionManager;
import tech.mcprison.prison.store.Database;
import tech.mcprison.prison.troubleshoot.TroubleshootManager;
import tech.mcprison.prison.util.EventExceptionHandler;
import tech.mcprison.prison.util.PrisonTPS;

/**
 * Entry point for implementations. <p> An instance of Prison can be retrieved using the static
 * {@link Prison#get()} method, however in order to use the core libraries, you must call
 * {@link Prison#init(Platform)} with a valid {@link Platform} implementation.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class Prison 
	implements PluginEntity {

	/**
	 * <p>This is not exactly a Prison module, but it's name is used within
	 * the LocaleManager to correct the generation of an incorrect directory
	 * path for the locale language files for this "core" module. Basically
	 * this provides consistency in the location of all language files that
	 * the admins can easily edit and know where to find them.
	 * </p>
	 * 
	 */
	public static final String PSEDUO_MODLE_NAME = "core";
	
	public static final int SPIGOTMC_ORG_PROJECT_ID = 1223; //72740;
	
    // Singleton
	private static Prison instance = null;

    public static final int API_LEVEL = 3;
    
    private String minecraftVersion;
    private List<Integer> versionMajMin;
    
    private long serverStartupTime;

    // Fields
    private Platform platform;
    private File dataFolder;
    private ModuleManager moduleManager;
    private PrisonCommand prisonCommands;
    private CommandHandler commandHandler;
    private SelectionManager selectionManager;
    private EventBus eventBus;
    private LocaleManager localeManager;
//    private ItemManager itemManager;
    private ErrorManager errorManager;
    private TroubleshootManager troubleshootManager;
    private IntegrationManager integrationManager;
    
    private PlaceholderManager placeholderManager;
    
    private Database metaDatabase;
    
    
    private PrisonTPS prisonTPS;
    
    
    private Prison() {
    	super();
    	
    	this.serverStartupTime = System.currentTimeMillis();
    }

    /**
     * Gets the current instance of this class. <p> An instance will always be available after
     * the implementation invokes the {@link Prison#init(Platform)} method.
     *
     * @return an instance of Prison.
     */
    public static Prison get() {
        if (instance == null) {
            instance = new Prison();
        }
        return instance;
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
    		this.localeManager = new LocaleManager(this, "lang/core");
    	}
        return localeManager;
    }
    
    /**
     * Initializes prison-core. In the implementations, this should be called when the plugin is
     * enabled. After this is called, every getter in this class will return a value.
     * <p>
     * Note that modules <b>should not call this method</b>. This is solely for the implementations.
     */
    public boolean init(Platform platform, String minecraftVersion) {
        long startTime = System.currentTimeMillis();

        
        this.platform = platform;
        this.minecraftVersion = minecraftVersion;
        
        if (!initDataFolder()) {
        	Output.get().logInfo("&cFailure: &eInitializing the Prison Data Folders!" );
        	Output.get().logInfo("&e&k!=&d Prison Plugin Terminated! &e&k=!&7" );
        	return false;
        }
        
        
        this.prisonTPS = new PrisonTPS();
        this.prisonTPS.submitAsyncTPSTask();

        
        // Setup the LocalManager if it is not yet started:
        getLocaleManager();

        
        sendBanner();
        
        
        Output.get().logInfo("Enabling and starting...");

        // Initialize various parts of the API. The magic happens here :)
        initManagers();
        if (!initMetaDatabase()) {
        	Output.get().logInfo("&cFailure: &eInitializing the Prison Database!" );
        	Output.get().logInfo("&e&k!=&d Prison Plugin Terminated! &e&k=!&7" );
            return false;
        }
        Alerts.getInstance(); // init alerts

        this.prisonCommands = new PrisonCommand();
        this.commandHandler.registerCommands(prisonCommands);
        


        long stopTime = System.currentTimeMillis();
        
        Output.get()
                .logInfo("Enabled &3Prison v%s in %d milliseconds.", getPlatform().getPluginVersion(),
                        (stopTime - startTime));

        registerInbuiltTroubleshooters();

        if (getPlatform().shouldShowAlerts())
            scheduleAlertNagger();
        
        
        // Disabled for now. The integrations cannot properly support this yet.
//        List<String> integrations = Prison.get().getIntegrationManager().toStrings();
//        for ( String intgration : integrations ) {
//        	Output.get().logInfo( intgration );
//		}

        
        return true;
    }

    // Initialization steps

    private void sendBanner() {
    	
    	ChatDisplay display = new ChatDisplay("");
    	
    	display.addText("");
    	display.addText("&6 _____      _                 ");
    	display.addText("&6|  __ \\    (_)                ");
    	display.addText("&6| |__) | __ _ ___  ___  _ __  ");
    	display.addText("&6|  ___/ '__| / __|/ _ \\| '_ \\");
    	display.addText("&6| |   | |  | \\__ \\ (_) | | | |");
    	display.addText("&6|_|   |_|  |_|___/\\___/|_| |_|");
    	display.addText("");
    	display.addText("&7Loading Prison version: &3%s", PrisonAPI.getPluginVersion());
    	display.addText("&7Running on platform: &3%s", platform.getClass().getSimpleName());
    	display.addText("&7Minecraft version: &3%s", getMinecraftVersion());
    	// display.addText("&7Server runtime: %s", getServerRuntimeFormatted() );
    	display.addText("");
    	
    	displaySystemSettings( display );
    	displaySystemTPS( display );

    	display.addText("");
    	
    	display.sendtoOutputLogInfo();
    }
    
    public void displaySystemSettings( ChatDisplay display ) {
    	
        display.addText("&7Server runtime: %s", Prison.get().getServerRuntimeFormatted() );;
        
        Runtime runtime = Runtime.getRuntime();
        
        String javaVersion = System.getProperty("java.version");
        
        int processors = runtime.availableProcessors();
        long memoryMax = runtime.maxMemory();
        long memoryTotal = runtime.totalMemory();
        long memoryFree = runtime.freeMemory();
        long memoryUsed = memoryTotal - memoryFree;
        
        DecimalFormat dFmt = new DecimalFormat("#,##0.000");
        String memMax = PlaceholdersUtil.formattedIPrefixBinarySize( memoryMax, dFmt, " " );
        String memTotal = PlaceholdersUtil.formattedIPrefixBinarySize( memoryTotal, dFmt, " " );
        String memFree = PlaceholdersUtil.formattedIPrefixBinarySize( memoryFree, dFmt, " " );
        String memUsed = PlaceholdersUtil.formattedIPrefixBinarySize( memoryUsed, dFmt, " " );

        display.addText("&7Java Version: %s  Processor cores: %s ", 
        								javaVersion, Integer.toString( processors ) );
        display.addText("&7Memory Max: %s  Total: %s  Free: %s  Used: %s", 
        				memMax, memTotal, memFree, memUsed );
        
        
        File prisonFolder = Prison.get().getDataFolder();
        long diskSpaceTotal = prisonFolder.getTotalSpace();
        long diskSpaceUsable = prisonFolder.getUsableSpace();
        long diskSpaceFree = prisonFolder.getFreeSpace();
        long diskSpaceUsed = diskSpaceTotal - diskSpaceFree;
        
        String dsTotal = PlaceholdersUtil.formattedIPrefixBinarySize( diskSpaceTotal, dFmt, " " );
        String dsUsable = PlaceholdersUtil.formattedIPrefixBinarySize( diskSpaceUsable, dFmt, " " );
        String dsFree = PlaceholdersUtil.formattedIPrefixBinarySize( diskSpaceFree, dFmt, " " );
        String dsUsed = PlaceholdersUtil.formattedIPrefixBinarySize( diskSpaceUsed, dFmt, " " );
        
        display.addText("&7Total Server Disk Space: %s  Usable: %s  Free: %s  Used: %s", 
        		dsTotal, dsUsable, dsFree, dsUsed );
        
        
        getPrisonDiskSpaceUsage( display, prisonFolder, 
        		"&7Prison's File Count: %s  Folder Count: %s  Disk Space: %s  Other Objects: %s" );

    	
    }
    
    public void displaySystemSettings( LinkedHashMap<String, String> fields ) {
        Runtime runtime = Runtime.getRuntime();
        
        String javaVersion = System.getProperty("java.version");
        
        int processors = runtime.availableProcessors();
        long memoryMax = runtime.maxMemory();
        long memoryTotal = runtime.totalMemory();
        long memoryFree = runtime.freeMemory();
        long memoryUsed = memoryTotal - memoryFree;
        
        DecimalFormat dFmt = new DecimalFormat("#,##0.000");
        String memMax = PlaceholdersUtil.formattedIPrefixBinarySize( memoryMax, dFmt, " " );
        String memTotal = PlaceholdersUtil.formattedIPrefixBinarySize( memoryTotal, dFmt, " " );
        String memFree = PlaceholdersUtil.formattedIPrefixBinarySize( memoryFree, dFmt, " " );
        String memUsed = PlaceholdersUtil.formattedIPrefixBinarySize( memoryUsed, dFmt, " " );

        fields.put( "javaVersion", javaVersion );
        fields.put( "procCores", Integer.toString( processors ) );

        fields.put( "memMax", memMax );
        fields.put( "memTotal", memTotal );
        fields.put( "memFree", memFree );
        fields.put( "memUsed", memUsed );
        
        
//        File prisonFolder = Prison.get().getDataFolder();
//        long diskSpaceTotal = prisonFolder.getTotalSpace();
//        long diskSpaceUsable = prisonFolder.getUsableSpace();
//        long diskSpaceFree = prisonFolder.getFreeSpace();
//        long diskSpaceUsed = diskSpaceTotal - diskSpaceFree;
//        
//        String dsTotal = PlaceholdersUtil.formattedIPrefixBinarySize( diskSpaceTotal, dFmt, " " );
//        String dsUsable = PlaceholdersUtil.formattedIPrefixBinarySize( diskSpaceUsable, dFmt, " " );
//        String dsFree = PlaceholdersUtil.formattedIPrefixBinarySize( diskSpaceFree, dFmt, " " );
//        String dsUsed = PlaceholdersUtil.formattedIPrefixBinarySize( diskSpaceUsed, dFmt, " " );
//        
//        
//        fields.put( "DiskSpaceTotal", dsTotal );
//        fields.put( "DiskSpaceUsable", dsUsable );
//        fields.put( "DiskSpaceFree", dsFree );
//        fields.put( "DiskSpaceUsed", dsUsed );
        
        
//        getPrisonDiskSpaceUsage( fields );

        getSystemTPS( fields );

    }
    
    public void displaySystemTPS( ChatDisplay display ) {
    	
        DecimalFormat iFmt = new DecimalFormat("#,##0");
        PrisonTPS prisonTPS = Prison.get().getPrisonTPS();
        display.addText( "&7Prison TPS Average: %s  Min: %s  Max: %s%s   " +
        					"Interval: %s ticks  Samples: %s",
        						prisonTPS.getAverageTPSFormatted(),
        						prisonTPS.getTPSMinFormatted(),
        						( prisonTPS.getTpsMax() >= 100  ? ">" : ""),
        						prisonTPS.getTPSMaxFormatted(),
        						iFmt.format( PrisonTPS.SUBMIT_TICKS_INTERVAL ),
        						iFmt.format( prisonTPS.getTpsSamples() )
        		);
        
        
        String tpsHistory = prisonTPS.getLastFewTPS();
        if ( tpsHistory.length() > 0 ) {
        	
        	display.addText( "&7TPS History: %s", tpsHistory );
        }

    }
    public void getSystemTPS( LinkedHashMap<String, String> fields ) {
    	
    	//DecimalFormat iFmt = new DecimalFormat("#,##0");
    	PrisonTPS prisonTPS = Prison.get().getPrisonTPS();
    	
    	fields.put( "tps", prisonTPS.getAverageTPSFormatted() );
    	fields.put( "tpsMin", prisonTPS.getTPSMinFormatted() );
//    	fields.put( "tpsMax", ( prisonTPS.getTpsMax() >= 100  ? ">" : "") + prisonTPS.getTPSMaxFormatted() );
    	//fields.put( "tpsInterval", iFmt.format( PrisonTPS.SUBMIT_TICKS_INTERVAL ) );
    	//fields.put( "tpsSamples", iFmt.format( prisonTPS.getTpsSamples() ) );
    	
    	
//    	String tpsHistory = prisonTPS.getLastFewTPS();
//    	if ( tpsHistory.length() > 0 ) {
//    		
//    		fields.put( "tpsHistory", prisonTPS.getLastFewTPS() );
//    	}
    	
    }
    
    private void getPrisonDiskSpaceUsage( ChatDisplay display,
			File prisonFolder, String text ) {

		PrisonDiskStats diskStats = new PrisonDiskStats();
		
		// Increment folder count for prison's plugin folder:
		diskStats.incrementFolderCount();
		
		calculatePrisonDiskUsage( diskStats, prisonFolder );
		
		DecimalFormat dFmt = new DecimalFormat("#,##0.000");
		DecimalFormat iFmt = new DecimalFormat("#,##0");
		
		String prisonFileCount = iFmt.format( diskStats.getFileCount() );
		String prisonFolderCount = iFmt.format( diskStats.getFolderCount() );
		String prisonOtherObjectCount = iFmt.format( diskStats.getOtherObjectCount() );
		String prisonStorageSize = PlaceholdersUtil.formattedIPrefixBinarySize( 
						diskStats.getStorageSize(), dFmt, " " );
		
		display.addText( text, prisonFileCount, prisonFolderCount, prisonStorageSize, 
				prisonOtherObjectCount );
		
	}
    
    public void getPrisonDiskSpaceUsage( LinkedHashMap<String, String> fields ) {
        File prisonFolder = Prison.get().getDataFolder();
        
    	PrisonDiskStats diskStats = new PrisonDiskStats();
    	
    	// Increment folder count for prison's plugin folder:
    	diskStats.incrementFolderCount();
    	
    	calculatePrisonDiskUsage( diskStats, prisonFolder );
    	
    	DecimalFormat dFmt = new DecimalFormat("#,##0.000");
    	DecimalFormat iFmt = new DecimalFormat("#,##0");
    	
    	String prisonFileCount = iFmt.format( diskStats.getFileCount() );
//    	String prisonFolderCount = iFmt.format( diskStats.getFolderCount() );
    	//String prisonOtherObjectCount = iFmt.format( diskStats.getOtherObjectCount() );
    	String prisonStorageSize = PlaceholdersUtil.formattedIPrefixBinarySize( 
    			diskStats.getStorageSize(), dFmt, " " );
    	
    	fields.put( "prisonStorageFiles", prisonFileCount );
//    	fields.put( "prisonStorageFolders", prisonFolderCount );
    	fields.put( "prisonStorageSize", prisonStorageSize );
    	//fields.put( "prisonStorageObjects", prisonOtherObjectCount );
    	
    }

	
	private void calculatePrisonDiskUsage( PrisonDiskStats diskStats, File prisonFolder ) {
	
		File[] files = prisonFolder.listFiles();
		
		for ( File file : files ) {
			if ( file.isDirectory() ) {
				diskStats.incrementFolderCount();
				
				// recursively call for all directories:
				calculatePrisonDiskUsage( diskStats, file );
			}
			else if ( file.isFile() ) {
				diskStats.incrementFileCount();
				diskStats.addStorageSize( file.length() );
			}
			else {
				diskStats.incrementOtherObjectCount();
			}
		}
	}

    public class PrisonDiskStats {
    	
    	long fileCount = 0L;
    	long folderCount = 0L;
    	long otherObjectCount = 0L;
    	long storageSize = 0L;
    	
    	public PrisonDiskStats() {
    		super();
    	}

    	public void incrementFileCount() {
    		fileCount++;
    	}
		public long getFileCount() {
			return fileCount;
		}
		public void setFileCount( long fileCount ) {
			this.fileCount = fileCount;
		}

		public void incrementFolderCount() {
			folderCount++;
		}
		public long getFolderCount() {
			return folderCount;
		}
		public void setFolderCount( long folderCount ) {
			this.folderCount = folderCount;
		}

		public void incrementOtherObjectCount() {
			otherObjectCount++;
		}
		public long getOtherObjectCount() {
			return otherObjectCount;
		}
		public void setOtherObjectCount( long otherObjectCount ) {
			this.otherObjectCount = otherObjectCount;
		}

		public void addStorageSize( long fileSize ) {
			storageSize += fileSize;
		}
		public long getStorageSize() {
			return storageSize;
		}
		public void setStorageSize( long storageSize ) {
			this.storageSize = storageSize;
		}
    }

    private boolean initDataFolder() {
        // Creates the /Prison directory, for core configuration.
        this.dataFolder = getPlatform().getPluginDirectory();
        return this.dataFolder.exists() || this.dataFolder.mkdir();
    }

    private boolean initMetaDatabase() {
        Optional<Database> metaDatabaseOptional = getPlatform().getStorage().getDatabase("meta");
        if (!metaDatabaseOptional.isPresent()) {
            getPlatform().getStorage().createDatabase("meta");
            metaDatabaseOptional = getPlatform().getStorage().getDatabase("meta");

            if (!metaDatabaseOptional.isPresent()) {
                Output.get().logError(
                        "Could not create the meta database. This means that something is wrong with the data storage for the plugin.");
                Output.get().logError("The plugin will now disable.");
                return false;
            }
        }
        metaDatabase = metaDatabaseOptional.get();
        return true;
    }

    private void initManagers() {
        // Now we initialize the API
    	
    	// LocalManager must be initialized ASAP due to dependencies with
    	// Output components:
//        this.localeManager = new LocaleManager(this, "lang/core");
        this.errorManager = new ErrorManager(this);
        this.eventBus = new EventBus(new EventExceptionHandler());
        this.moduleManager = new ModuleManager();
        this.commandHandler = new CommandHandler();
        this.selectionManager = new SelectionManager();
        this.troubleshootManager = new TroubleshootManager();
        this.integrationManager = new IntegrationManager();
        this.placeholderManager = new PlaceholderManager();
        

//        try {
//            this.itemManager = new ItemManager();
//        } catch (Exception e) {
//            this.errorManager.throwError(new Error(
//                    "Error while loading items.csv. Try running /prison troubleshoot item_scan.")
//                    .appendStackTrace("when loading items.csv", e));
//            Output.get().logError("Try running /prison troubleshoot item_scan.");
//        }
    }

    private void registerInbuiltTroubleshooters() {
//        PrisonAPI.getTroubleshootManager().registerTroubleshooter(new ItemTroubleshooter());
    }

    private void scheduleAlertNagger() {
        // Nag the user with alerts every 5 minutes
        PrisonAPI.getScheduler().runTaskTimerAsync(() -> PrisonAPI.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission("prison.admin")
                        && Alerts.getInstance().getAlertsFor(player.getUUID()).size() > 0)
                .forEach(Alerts.getInstance()::showAlerts), 60 * 20 * 5, 60 * 20 * 5);
    }

    // End initialization steps

    /**
     * Finalizes and unregisters instances in prison-core. In the implementations, this should be
     * called when the plugin is disabled.
     */
    public void deinit() {
        moduleManager.unregisterAll();
    }

    // Getters

    public String getMinecraftVersion()
	{
		return minecraftVersion;
	}
    
    public List<Integer> getMVersionMajMin() {
    	if ( versionMajMin == null ) {
			
    		this.versionMajMin = new ArrayList<>();
    		
    		String v = Prison.get().getMinecraftVersion();
			String versionStr = v.substring( v.indexOf( "(MC:" ) + 4, v.lastIndexOf( ")" ) );
			String[] vMN = versionStr.split( "\\." );
			
			for ( int x = 0; x < vMN.length; x++ ) {
				String ver = vMN[x];
				
				try {
					this.versionMajMin.add( 
							Integer.parseInt( ver.trim() ) );
				}
				catch ( NumberFormatException e ) {
					// ignore... just break out:
					break;
				}
			}
			
//			Output.get().logInfo( "#### Prison.getMVersionMajMin() : " + 
//					( versionMajMin != null && versionMajMin.size() > 0 ? versionMajMin.get(0) : "?" ) + " " +
//					( versionMajMin != null && versionMajMin.size() > 1 ? versionMajMin.get(1) : "?" ) + " " +
//					( versionMajMin != null && versionMajMin.size() > 2 ? versionMajMin.get(2) : "?" )
//					);
    	}
    	return versionMajMin;
    }

	@Override
    public String getName() {
        return "PrisonCore";
    }

    /**
     * Returns the Platform in use, which contains methods for interacting with the Minecraft server
     * on whichever implementation is currently being used.
     *
     * @return the {@link Platform}.
     */
    public Platform getPlatform() {
        return platform;
    }

    /**
     * Returns the core data folder, which is located at "/plugins/Prison". This contains the
     * core config.json and messages.json files, as well as other global data.
     *
     * @return the {@link File}.
     */
    public File getDataFolder() {
        return dataFolder;
    }



    /**
     * Returns the core's {@link ErrorManager}. For modules, you should use your own module's error manager via {@link Module#getErrorManager()}.
     *
     * @return The core's error manager instance.
     */
    public ErrorManager getErrorManager() {
        return errorManager;
    }

    /**
     * Returns the event bus, where event listeners can be registered and events can be posted.
     *
     * @return The {@link EventBus}.
     */
    public EventBus getEventBus() {
        return eventBus;
    }

    /**
     * Returns the module manager, which stores instances of all registered {@link
     * Module}s and manages their state.
     *
     * @return The {@link ModuleManager}.
     */
    public ModuleManager getModuleManager() {
        return moduleManager;
    }
    

    public PrisonCommand getPrisonCommands() {
		return prisonCommands;
	}

	/**
     * Returns the command handler, where command methods can be registered using the {@link
     * CommandHandler#registerCommands(Object)} method.
     *
     * @return The {@link CommandHandler}.
     */
    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    /**
     * Returns the selection manager, which stores each player's current selection using Prison's
     * selection wand.
     *
     * @return The {@link SelectionManager}.
     */
    public SelectionManager getSelectionManager() {
        return selectionManager;
    }

//    /**
//     * Returns the item manager, which manages the "friendly" names of items
//     */
//    public ItemManager getItemManager() {
//        return itemManager;
//    }

    /**
     * Returns the meta database, which is used to store data from within the core.
     */
    public Database getMetaDatabase() {
        return metaDatabase;
    }

    /**
     * Returns the troubleshoot manager, which is used to register {@link tech.mcprison.prison.troubleshoot.Troubleshooter}s.
     */
    public TroubleshootManager getTroubleshootManager() {
        return troubleshootManager;
    }

    /**
     * Returns the integration manager, which returns {@link tech.mcprison.prison.integration.Integration}s.
     */
    public IntegrationManager getIntegrationManager() {
        return integrationManager;
    }

    
    /**
     * Returns the integration manager, which returns {@link tech.mcprison.prison.integration.Integration}s.
     */
    public PlaceholderManager getPlaceholderManager() {
    	return placeholderManager;
    }
    
    
    public long getServerStartupTime() {
		return serverStartupTime;
	}
	public void setServerStartupTime( long serverStartupTime ) {
		this.serverStartupTime = serverStartupTime;
	}

	public String getServerRuntimeFormatted() {
    	long currentTime = System.currentTimeMillis();
    	long runtimeMs = currentTime - getServerStartupTime();
    	return PlaceholdersUtil.formattedTime( runtimeMs / 1000 );
    }

	public PrisonTPS getPrisonTPS() {
		return prisonTPS;
	}
    
}
