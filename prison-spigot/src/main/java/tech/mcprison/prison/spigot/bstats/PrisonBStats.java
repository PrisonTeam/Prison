package tech.mcprison.prison.spigot.bstats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.DrilldownPie;
import org.bstats.charts.SimpleBarChart;
import org.bstats.charts.SimplePie;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.PrisonCommand.RegisteredPluginsData;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig;
import tech.mcprison.prison.autofeatures.AutoFeaturesWrapper;
import tech.mcprison.prison.integration.Integration;
import tech.mcprison.prison.integration.IntegrationManager;
import tech.mcprison.prison.integration.IntegrationType;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.TopNPlayers;
import tech.mcprison.prison.spigot.SpigotPrison;

public class PrisonBStats {

	private SpigotPrison spigotPrison;
	
	private Metrics bStatsMetrics = null;
	
	private List<String> reportPrisonCore;
	private List<String> reportPermissions;
	private List<String> reportEconomy;
	private List<String> reportPlaceholders;
//	private List<String> reportVault;
	private List<String> reportEnchantments;
	private List<String> reportAdminTools;
	private List<String> reportConflicts;
	
	
	private TreeSet<String> pluginsUsed;
	
	
	public PrisonBStats( SpigotPrison spigotPrison ) {
		super();
		
		this.spigotPrison = spigotPrison;
		
		
		this.reportPrisonCore = new ArrayList<>();
		this.reportPermissions = new ArrayList<>();

		this.reportEconomy = new ArrayList<>();
		this.reportPlaceholders = new ArrayList<>();
//		this.reportVault = new ArrayList<>();
		this.reportEnchantments = new ArrayList<>();
		this.reportAdminTools = new ArrayList<>();
		this.reportConflicts = new ArrayList<>();
		
		this.pluginsUsed = new TreeSet<>();
		
		
		setupPluginReports();
	}
	


	/**
     * <p>bStats reporting</p>
     * 
     * https://github.com/Bastian/bStats-Metrics/tree/master/base/src/main/java/org/bstats/charts
     * 
     */
    public void initMetricsOnLoad() {
    	if (!spigotPrison.getConfig().getBoolean("send-metrics", true)) {
    		return; // Don't check if they don't want it
    	}
    	
    	int pluginId = 657;
    	setbStatsMetrics( new Metrics( spigotPrison, pluginId ) );;
//    	bStatsMetrics = new PrisonMetrics( this, pluginId );
    	
//    	Metrics metrics = new Metrics( this, pluginId );
    }
    
    public void initMetricsOnEnable() {
        if (!spigotPrison.getConfig().getBoolean("send-metrics", true)) {
            return; // Don't check if they don't want it
        }
        
        
        if ( getbStatsMetrics() == null ) {
        	int pluginId = 657;
        	
        	setbStatsMetrics( new Metrics( spigotPrison, pluginId ) );
//        	bStatsMetrics = new PrisonMetrics( this, pluginId );
        }

        // Report the modules being used
        SimpleBarChart sbcModulesUsed = new SimpleBarChart("modules_used", () -> {
            Map<String, Integer> valueMap = new HashMap<>();
            for (Module m : PrisonAPI.getModuleManager().getModules()) {
                valueMap.put(m.getName(), 1);
            }
            return valueMap;
        });
        getbStatsMetrics().addCustomChart( sbcModulesUsed );

        // Report the API level
        SimplePie spApiLevel = 
                new SimplePie("api_level", () -> 
                	"API Level " + Prison.API_LEVEL + "." + Prison.API_LEVEL_MINOR );
        getbStatsMetrics().addCustomChart( spApiLevel );
        
        
        PrisonMines prisonMines = (PrisonMines) Prison.get().getModuleManager().getModule( PrisonMines.MODULE_NAME );
        PrisonRanks prisonRanks = (PrisonRanks) Prison.get().getModuleManager().getModule( PrisonRanks.MODULE_NAME );
        
        
        // delete: "prison_ranks"
        // delete: "mines_ranks_and_ladders"
//        "prison_default_rank_counts"
//        "prison_prestiges_rank_counts"
//        "prison_other_rank_counts"
//        "prison_total_rank_counts"
        
//        "prison_total_ladder_counts"
//        "prison_total_mine_counts"
        
//        "prison_total_active_player_counts"
//        "prison_total_archived_player_counts"
//        "prison_total_player_counts"
        
//        "prison_languages"
        
        // Note that the functions that provide the values to these arrow functions must get the values from within them
        // or they will never update their values when bstats runs them during their intervals!!
        
        getbStatsMetrics().addCustomChart( new SimplePie( "prison_default_rank_counts", () -> {
        	
        	int defaultRankCount = prisonRanks.getDefaultLadderRankCount();
        	
//        	int defaultRankCount = prisonRanksOpt.map(module -> ((PrisonRanks) module).getDefaultLadderRankCount()).orElse(0);
        	return Integer.toString( defaultRankCount );
        }) );
        
        getbStatsMetrics().addCustomChart( new SimplePie( "prison_prestiges_rank_counts", () -> {
        	int prestigesRankCount = prisonRanks.getPrestigesLadderRankCount();
        	return Integer.toString( prestigesRankCount );
        }) );
        
        getbStatsMetrics().addCustomChart( new SimplePie( "prison_other_rank_counts", () -> {
        	int rankCountTotal = prisonRanks.getRankCount();
        	int defaultRankCount = prisonRanks.getDefaultLadderRankCount();
        	int prestigesRankCount = prisonRanks.getPrestigesLadderRankCount();
        	
        	int otherRankCount = rankCountTotal - defaultRankCount - prestigesRankCount;
        	return Integer.toString( otherRankCount );
        }) );
        
        getbStatsMetrics().addCustomChart( new SimplePie( "prison_total_rank_counts", () -> {
        	int rankCountTotal = prisonRanks.getRankCount();
        	return Integer.toString( rankCountTotal );
        }) );
        
        getbStatsMetrics().addCustomChart( new SimplePie( "prison_total_ladder_counts", () -> {
        	int ladderCount = prisonRanks.getladderCount();
        	return Integer.toString( ladderCount );
        }) );
        
        getbStatsMetrics().addCustomChart( new SimplePie( "prison_total_mine_counts", () -> {

        	int mineCount = prisonMines.getMineManager().getMines().size();
        	return Integer.toString( mineCount );
        }) );
        
        getbStatsMetrics().addCustomChart( new SimplePie( "prison_total_player_counts", () -> {
        	
        	int playerCount = prisonRanks.getPlayersCount();
        	return Integer.toString( playerCount );
        }) );
        
        getbStatsMetrics().addCustomChart( new SimplePie( "prison_total_active_player_counts", () -> {

        	int playerCountActive = PrisonRanks.getInstance().getPlayerManager() == null ?
        			-1 : 
        			TopNPlayers.getInstance().getTopNSize();
        	return Integer.toString( playerCountActive );
        }) );
        
        getbStatsMetrics().addCustomChart( new SimplePie( "prison_total_archived_player_counts", () -> {

        	int playerCountArchived = PrisonRanks.getInstance().getPlayerManager() == null ?
        			-1 : 
        			TopNPlayers.getInstance().getArchivedSize();
        	return Integer.toString( playerCountArchived );
        }) );
        
        getbStatsMetrics().addCustomChart( new SimplePie( "prison_languages", () -> {
        	return Prison.get().getPlatform().getConfigString( "default-language", "en_US" );
        }) );
        
        
        
        
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
//        getbStatsMetrics().addCustomChart( mlcPrisonRanksAndLadders );
        
//        MultiLineChart mlcMinesRanksAndLadders = 
//        		new MultiLineChart("mines_ranks_and_ladders", new Callable<Map<String, Integer>>() {
//            @Override
//            public Map<String, Integer> call() throws Exception {
//                Map<String, Integer> valueMap = new HashMap<>();
//                valueMap.put("mines", mineCount);
//                valueMap.put("ranks", rankCount);
//                valueMap.put("ladders", ladderCount);
//                valueMap.put("players", playerCount);
//                return valueMap;
//            }
//        });
//        bStatsMetrics.addCustomChart( mlcMinesRanksAndLadders );
        
        
        
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
//        getbStatsMetrics().addCustomChart( mlcPrisonPrisonRanks );
        
//        MultiLineChart mlcPrisonRanks = new MultiLineChart("prison_ranks", new Callable<Map<String, Integer>>() {
//        	@Override
//        	public Map<String, Integer> call() throws Exception {
//        		Map<String, Integer> valueMap = new HashMap<>();
//        		valueMap.put("ranks", rankCount);
//        		valueMap.put("defaultRanks", defaultRankCount);
//        		valueMap.put("prestigesRanks", prestigesRankCount);
//        		valueMap.put("otherRanks", otherRankCount);
//        		return valueMap;
//        	}
//        });
//        bStatsMetrics.addCustomChart( mlcPrisonRanks );
        
        
        // remove "prison_ladders" since the many are duplicated and we should not have personal ladder names:
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
//        getbStatsMetrics().addCustomChart( mlcPrisonPrisonLadders );
        
        
//        MultiLineChart mlcPrisonladders = new MultiLineChart("prison_ladders", new Callable<Map<String, Integer>>() {
//        	@Override
//        	public Map<String, Integer> call() throws Exception {
//        		Map<String, Integer> valueMap = new HashMap<>();
//        		
//        		PrisonRanks pRanks = (PrisonRanks) prisonRanksOpt.orElseGet( null );
//        		for ( RankLadder ladder : pRanks.getLadderManager().getLadders() ) {
//        	
//        			valueMap.put( ladder.getName(), ladder.getRanks().size() );
//        		}
//        		
//        		return valueMap;
//        	}
//        });
//        bStatsMetrics.addCustomChart( mlcPrisonladders );

        TreeMap<String, RegisteredPluginsData> plugins = Prison.get().getPrisonCommands().getRegisteredPluginData();
        
        
        createNewBstatReport( "core_prison_plugins", reportPrisonCore, plugins, pluginsUsed );
        createNewBstatReport( "permission_plugins", reportPermissions, plugins, pluginsUsed );
        createNewBstatReport( "economy_plugins", reportEconomy, plugins, pluginsUsed );
        
        createNewBstatReport( "placeholder_plugins", reportPlaceholders, plugins, pluginsUsed );
        createNewBstatReport( "enchantment_plugins", reportEnchantments, plugins, pluginsUsed );
        createNewBstatReport( "admin_tools_plugins", reportAdminTools, plugins, pluginsUsed );
        
        createNewBstatReport( "potential_conflicts_plugins", reportConflicts, plugins, pluginsUsed );

        
		//this.reportVault = new ArrayList<>(); "prison_integrated_vault_plugins"


//        TreeMap<String, RegisteredPluginsData> pluginsAtoE = getSubsetOfPlugins(plugins, 'a', 'f', false, pluginsUsed );
//        TreeMap<String, RegisteredPluginsData> pluginsFtoM = getSubsetOfPlugins(plugins, 'f', 'n', false, pluginsUsed );
//        TreeMap<String, RegisteredPluginsData> pluginsNtoS = getSubsetOfPlugins(plugins, 'n', 't', false, pluginsUsed );
//        TreeMap<String, RegisteredPluginsData> pluginsTto9 = getSubsetOfPlugins(plugins, 't', 'z', true, pluginsUsed );
        
        // Remove "plugins" - Too much info on one report
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
//        getbStatsMetrics().addCustomChart( mlcPrisonPlugins );
        
        
        
        DrilldownPie mlcPrisonVaultPlugins = new DrilldownPie("prison_vault_plugins", () -> {
        	Map<String, Map<String, Integer>> map = new HashMap<>();
     

            IntegrationManager im = Prison.get().getIntegrationManager();
            
            Set<IntegrationType> inTypeKeys = im.getIntegrations().keySet();
            for (IntegrationType inTypeKey  : inTypeKeys ) {
            	List<Integration> integrations = im.getIntegrations().get( inTypeKey );
            	
            	for (Integration integration : integrations) {
    				
            		if ( integration.getDisplayName().toLowerCase().contains( "(vault)") ) {
            			
            			Map<String, Integer> entry = new HashMap<>();
                		entry.put( integration.getDisplayName(), 1 );
                		
                		map.put( integration.getType().name(), entry );
            		}
            		
    			}
    		}
        	
        	return map;
        });
        getbStatsMetrics().addCustomChart( mlcPrisonVaultPlugins );
        
        
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
//        getbStatsMetrics().addCustomChart( mlcPrisonPluginsAtoE );
        
        
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
//        getbStatsMetrics().addCustomChart( mlcPrisonPluginsFtoM );
        
        
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
//        getbStatsMetrics().addCustomChart( mlcPrisonPluginsNtoS );
        
        
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
//        getbStatsMetrics().addCustomChart( mlcPrisonPluginsTto9 );
        
        
        
        AutoFeaturesFileConfig afConfig = AutoFeaturesWrapper.getInstance().getAutoFeaturesConfig();
        TreeMap<String, String> autofeaturesBstats = afConfig.getBstatsDetails();
        
        DrilldownPie mlcPrisonAutofeatures = new DrilldownPie("autofeatures", () -> {
        	Map<String, Map<String, Integer>> map = new HashMap<>();
        	
        	for (String feature : autofeaturesBstats.keySet() ) {
        		String detail = autofeaturesBstats.get( feature );
        		
        		Map<String, Integer> entry = new HashMap<>();
        		entry.put( detail, 1 );
        		
        		map.put( feature, entry );
        	}
        	
        	return map;
        });
        getbStatsMetrics().addCustomChart( mlcPrisonAutofeatures );
        
        
        
    }
	
//    /**
//     * <p>This function will split up a list of active plugins in to sub-groups.
//     * This is controlled by the <b>rangeLow</b> through <b>rangeHigh</b> parameters.
//     * The parameter <b>includeNonAlpha</b> will include all other plugins where their
//     * names do not begin with an alpha character; this is a catch-all to prevent plugins
//     * from being omitted.
//     * </p>
//     * 
//     * <p>The parameter <b>pluginsUsed</b> is a set plugins that have already been 
//     * included in other reports so therefore should be omitted from these reports.
//     * </p>
//     * 
//     * @param plugins
//     * @param rangeLow
//     * @param rangeHigh
//     * @param includeNonAlpha
//     * @param pluginsUsed
//     * @return
//     */
//    private TreeMap<String, RegisteredPluginsData> getSubsetOfPlugins(
//			TreeMap<String, RegisteredPluginsData> plugins,
//			char rangeLow, char rangeHigh,
//			boolean includeNonAlpha, 
//			TreeSet<String> pluginsUsed ) {
//    	
//		TreeMap<String, RegisteredPluginsData> results = new TreeMap<>();
//		
//		Set<String> keys = plugins.keySet();
//		for (String key : keys) {
//			
//			if ( !pluginsUsed.contains( key ) ) {
//				
//				char keyFirstChar = key.toLowerCase().charAt(0);
//				
//				if ( Character.isAlphabetic(keyFirstChar) ) {
//					
//					if ( Character.compare(keyFirstChar, rangeLow) >= 0 && Character.compare( keyFirstChar, rangeHigh) < 0 ) {
//						
//						results.put( key, plugins.get(key) );
//					}
//				}
//				else {
//					
//					// Add all non-alpha plugins to this result:
//					results.put( key, plugins.get(key) );
//				}
//			}
//			
//		}
//		
//		return results;
//	}
    
    
    /**
     * <p>Most of the new reports falls in to specific formats for report plugins.  
     * This function uses the pattern to generate the report of interest.
     * </p>
     * 
     * @param reportName
     * @param pluginsInThisReport
     * @param plugins
     * @param pluginsUsed
     */
    private void createNewBstatReport( String reportName, List<String> pluginsInThisReport, 
    		TreeMap<String, RegisteredPluginsData> plugins, TreeSet<String> pluginsUsed ) {
    	
        
        DrilldownPie report = new DrilldownPie( reportName, () -> {
        	Map<String, Map<String, Integer>> map = new HashMap<>();
        	
        	for (String pluginName : pluginsInThisReport ) {
        		
        		pluginsUsed.add( pluginName );
        		
        		RegisteredPluginsData pluginData = plugins.get( pluginName );
        		Map<String, Integer> entry = new HashMap<>();
        		
        		String detailNameVersion = null;
        		
        		if ( pluginData != null ) {
        			
        			detailNameVersion = pluginName + ": " + pluginData.getPluginVersion();
        		}
        		else {
        			detailNameVersion = pluginName + ": Not Installed"; 
        		}
        		
        		entry.put( detailNameVersion, 1 );
        		
        		map.put( pluginName, entry );
        	}
        	
        	return map;
        });
        getbStatsMetrics().addCustomChart( report );

        
    }
    
    private void setupPluginReports() {

		reportPrisonCore.add( "Prison" );
		reportPrisonCore.add( "Essentials" );
		reportPrisonCore.add( "Vault" );
		reportPrisonCore.add( "ProtocolLib" );
		reportPrisonCore.add( "Minepacks" );
		reportPrisonCore.add( "CustomItems" );
		
		reportPermissions.add( "GroupManager" );
		reportPermissions.add( "GroupManagerX" );
		reportPermissions.add( "LuckPerms" );
		reportPermissions.add( "LPC" );
		reportPermissions.add( "PermissionsEx" );
		
		
		reportEconomy.add( "Essentials" );
		reportEconomy.add( "Economy_CMI" );
		reportEconomy.add( "CMIEInjector" );
		reportEconomy.add( "GemsEconomy" );
		reportEconomy.add( "SDFEconomy" );
		reportEconomy.add( "SaneEconomy" );
//		reportEconomy.add( "Tokens" );
		reportEconomy.add( "Ultimate_Economy" );
		reportEconomy.add( "XConomy" );
		
		
		reportPlaceholders.add( "PlaceholderAPI" );
		reportPlaceholders.add( "MVdWPlaceholderAPI" );
		
		reportPlaceholders.add( "AnimatedScoreboard" );
		reportPlaceholders.add( "DecentHolograms" );
		reportPlaceholders.add( "DeluxeMenus" );
		reportPlaceholders.add( "EconomyShopGUI" );
		reportPlaceholders.add( "EssentialsChat" );
		reportPlaceholders.add( "HolographicDisplays" );
		reportPlaceholders.add( "HolographicExtension" );
		reportPlaceholders.add( "RealScoreboard" );
		reportPlaceholders.add( "Scoreboard-revision" );
		reportPlaceholders.add( "RealScoreboard" );
		reportPlaceholders.add( "TAB" );
		reportPlaceholders.add( "TabList" );

		reportPlaceholders.add( "ajLeaderBoards" );
		reportPlaceholders.add( "FeatherBoard" );
		
		
		
		reportEnchantments.add( "AdvancedEnchantmens" );
		reportEnchantments.add( "CrazyEnchantments" );
		reportEnchantments.add( "TokenEnchant" );
		reportEnchantments.add( "TimTheEnchanter" );
		reportEnchantments.add( "Zenchantments" );
		reportEnchantments.add( "PrisonEnchants" );
		reportEnchantments.add( "RevEnchants" );

		reportEnchantments.add( "mcMMO" );
		
		
		
		reportAdminTools.add( "Citizens" );
		reportAdminTools.add( "CoreProtect" );
		reportAdminTools.add( "CMI" );
		reportAdminTools.add( "CMILib" );
		reportAdminTools.add( "EssentialsSpawn" );
		reportAdminTools.add( "FastAsyncWorldEdit" );
		reportAdminTools.add( "Multiverse-Core" );
		reportAdminTools.add( "Multiverse" );
		reportAdminTools.add( "Multiworld" );
		reportAdminTools.add( "MyCommand" );
		reportAdminTools.add( "NBTAPI" );
		reportAdminTools.add( "PlayerKits" );
		reportAdminTools.add( "PlotSquared" );
		reportAdminTools.add( "SkinsRestorer" );
		reportAdminTools.add( "Skript" );
		reportAdminTools.add( "ViaBackwards" );
		reportAdminTools.add( "ViaRewind" );
		reportAdminTools.add( "ViaVersion" );
		reportAdminTools.add( "VoidGen" );
		reportAdminTools.add( "WorldEdit" );
		reportAdminTools.add( "WorldGuard" );
		reportAdminTools.add( "WorldGuardExtraFlags" );

		reportAdminTools.add( "ConsoleSpamFix" );
		reportAdminTools.add( "GriefPrevention" );
		
		
		
		reportConflicts.add( "AutoSell" );
		reportConflicts.add( "CataMines" );
		reportConflicts.add( "DeluxeMines" );
		reportConflicts.add( "EZPrestige" );
		reportConflicts.add( "EZRanksPro" );
		reportConflicts.add( "JetsPrisonCells" );
		reportConflicts.add( "MineCrates" );
		reportConflicts.add( "MineBomb" );
		reportConflicts.add( "MineBuddy" );
		reportConflicts.add( "MineChess" );
		reportConflicts.add( "MineResetLite" );
		reportConflicts.add( "MineResetLitePlus" );
		reportConflicts.add( "NonSquareMines" );
		reportConflicts.add( "PlugMan" ); // Just to get an idea of how many may be causing issues
		reportConflicts.add( "PrisonControl" );
		reportConflicts.add( "PrisonGames" );
		reportConflicts.add( "PrivateMines" );
		reportConflicts.add( "Rankup" );
		reportConflicts.add( "Tokens" );

	}
    
	public Metrics getbStatsMetrics() {
		return bStatsMetrics;
	}

	public void setbStatsMetrics(Metrics bStatsMetrics) {
		this.bStatsMetrics = bStatsMetrics;
	}
}
