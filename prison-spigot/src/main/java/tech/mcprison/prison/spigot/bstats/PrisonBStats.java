package tech.mcprison.prison.spigot.bstats;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.RankLadder;
import tech.mcprison.prison.spigot.SpigotPrison;

public class PrisonBStats {

	private SpigotPrison spigotPrison;
	
	private Metrics bStatsMetrics = null;
	
	public PrisonBStats( SpigotPrison spigotPrison ) {
		super();
		
		this.spigotPrison = spigotPrison;
		
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
        
        
        Optional<Module> prisonMinesOpt = Prison.get().getModuleManager().getModule( PrisonMines.MODULE_NAME );
        Optional<Module> prisonRanksOpt = Prison.get().getModuleManager().getModule( PrisonRanks.MODULE_NAME );
        
        int mineCount = prisonMinesOpt.map(module -> ((PrisonMines) module).getMineManager().getMines().size()).orElse(0);
        int rankCount = prisonRanksOpt.map(module -> ((PrisonRanks) module).getRankCount()).orElse(0);
        
        int defaultRankCount = prisonRanksOpt.map(module -> ((PrisonRanks) module).getDefaultLadderRankCount()).orElse(0);
        int prestigesRankCount = prisonRanksOpt.map(module -> ((PrisonRanks) module).getPrestigesLadderRankCount()).orElse(0);
        int otherRankCount = rankCount - defaultRankCount - prestigesRankCount;
        
        int ladderCount = prisonRanksOpt.map(module -> ((PrisonRanks) module).getladderCount()).orElse(0);
        int playerCount = prisonRanksOpt.map(module -> ((PrisonRanks) module).getPlayersCount()).orElse(0);
        
        
        
        DrilldownPie mlcPrisonRanksAndLadders = new DrilldownPie("mines_ranks_and_ladders", () -> {
        	Map<String, Map<String, Integer>> map = new HashMap<>();
        	
        	Map<String, Integer> ranks = new HashMap<>();
        	ranks.put( Integer.toString( mineCount ), 1 );
        	map.put( "mines", ranks );
        	
        	Map<String, Integer> defRanks = new HashMap<>();
        	defRanks.put( Integer.toString( rankCount ), 1 );
        	map.put( "ranks", defRanks );
    	
        	Map<String, Integer> prestigesRanks = new HashMap<>();
        	prestigesRanks.put( Integer.toString( ladderCount ), 1 );
        	map.put( "ladders", prestigesRanks );
        	
        	Map<String, Integer> otherRanks = new HashMap<>();
        	otherRanks.put( Integer.toString( playerCount ), 1 );
        	map.put( "players", otherRanks );
        	
        	return map;
        });
        getbStatsMetrics().addCustomChart( mlcPrisonRanksAndLadders );
        
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
        
        
        
        DrilldownPie mlcPrisonPrisonRanks = new DrilldownPie("prison_ranks", () -> {
        	Map<String, Map<String, Integer>> map = new HashMap<>();
        	
        	Map<String, Integer> ranks = new HashMap<>();
        	ranks.put( Integer.toString( rankCount ), 1 );
        	map.put( "ranks", ranks );
        	
        	Map<String, Integer> defRanks = new HashMap<>();
        	defRanks.put( Integer.toString( defaultRankCount ), 1 );
        	map.put( "defaultRanks", defRanks );
    	
        	Map<String, Integer> prestigesRanks = new HashMap<>();
        	prestigesRanks.put( Integer.toString( prestigesRankCount ), 1 );
        	map.put( "prestigesRanks", prestigesRanks );
        	
        	Map<String, Integer> otherRanks = new HashMap<>();
        	otherRanks.put( Integer.toString( otherRankCount ), 1 );
        	map.put( "otherRanks", otherRanks );
        	
        	return map;
        });
        getbStatsMetrics().addCustomChart( mlcPrisonPrisonRanks );
        
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
        
        
        DrilldownPie mlcPrisonPrisonLadders = new DrilldownPie("prison_ladders", () -> {
        	Map<String, Map<String, Integer>> map = new HashMap<>();
        	
    		
    		PrisonRanks pRanks = (PrisonRanks) prisonRanksOpt.orElseGet( null );
    		for ( RankLadder ladder : pRanks.getLadderManager().getLadders() ) {
    	
    			Map<String, Integer> entry = new HashMap<>();
        		entry.put( Integer.toString( ladder.getRanks().size() ), 1 );
        		
        		map.put( ladder.getName(), entry );
    		}
        	
        	return map;
        });
        getbStatsMetrics().addCustomChart( mlcPrisonPrisonLadders );
        
        
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

        TreeMap<String, RegisteredPluginsData> pluginsAtoE = getSubsetOfPlugins(plugins, 'a', 'f', false );
        TreeMap<String, RegisteredPluginsData> pluginsFtoM = getSubsetOfPlugins(plugins, 'f', 'n', false );
        TreeMap<String, RegisteredPluginsData> pluginsNtoS = getSubsetOfPlugins(plugins, 'n', 't', false );
        TreeMap<String, RegisteredPluginsData> pluginsTto9 = getSubsetOfPlugins(plugins, 't', 'z', true );
        
        DrilldownPie mlcPrisonPlugins = new DrilldownPie("plugins", () -> {
        	Map<String, Map<String, Integer>> map = new HashMap<>();
        	
        	for (String pluginName : plugins.keySet() ) {
        		RegisteredPluginsData pluginData = plugins.get( pluginName );
				
        		Map<String, Integer> entry = new HashMap<>();
        		entry.put( pluginData.getPluginVersion(), 1 );
        		
        		map.put( pluginData.getPluginName(), entry );
			}
        	
        	return map;
        });
        getbStatsMetrics().addCustomChart( mlcPrisonPlugins );
        
        
        DrilldownPie mlcPrisonPluginsAtoE = new DrilldownPie("plugins_a_to_e", () -> {
        	Map<String, Map<String, Integer>> map = new HashMap<>();
        	
        	for (String pluginName : pluginsAtoE.keySet() ) {
        		RegisteredPluginsData pluginData = pluginsAtoE.get( pluginName );
        		
        		Map<String, Integer> entry = new HashMap<>();
        		entry.put( pluginData.getPluginVersion(), 1 );
        		
        		map.put( pluginData.getPluginName(), entry );
        	}
        	
        	return map;
        });
        getbStatsMetrics().addCustomChart( mlcPrisonPluginsAtoE );
        
        
        DrilldownPie mlcPrisonPluginsFtoM = new DrilldownPie("plugins_f_to_m", () -> {
        	Map<String, Map<String, Integer>> map = new HashMap<>();
        	
        	for (String pluginName : pluginsFtoM.keySet() ) {
        		RegisteredPluginsData pluginData = pluginsFtoM.get( pluginName );
        		
        		Map<String, Integer> entry = new HashMap<>();
        		entry.put( pluginData.getPluginVersion(), 1 );
        		
        		map.put( pluginData.getPluginName(), entry );
        	}
        	
        	return map;
        });
        getbStatsMetrics().addCustomChart( mlcPrisonPluginsFtoM );
        
        
        DrilldownPie mlcPrisonPluginsNtoS = new DrilldownPie("plugins_n_to_s", () -> {
        	Map<String, Map<String, Integer>> map = new HashMap<>();
        	
        	for (String pluginName : pluginsNtoS.keySet() ) {
        		RegisteredPluginsData pluginData = pluginsNtoS.get( pluginName );
        		
        		Map<String, Integer> entry = new HashMap<>();
        		entry.put( pluginData.getPluginVersion(), 1 );
        		
        		map.put( pluginData.getPluginName(), entry );
        	}
        	
        	return map;
        });
        getbStatsMetrics().addCustomChart( mlcPrisonPluginsNtoS );
        
        
        DrilldownPie mlcPrisonPluginsTto9 = new DrilldownPie("plugins_t_to_z_plus_others", () -> {
        	Map<String, Map<String, Integer>> map = new HashMap<>();
        	
        	for (String pluginName : pluginsTto9.keySet() ) {
        		RegisteredPluginsData pluginData = pluginsTto9.get( pluginName );
        		
        		Map<String, Integer> entry = new HashMap<>();
        		entry.put( pluginData.getPluginVersion(), 1 );
        		
        		map.put( pluginData.getPluginName(), entry );
        	}
        	
        	return map;
        });
        getbStatsMetrics().addCustomChart( mlcPrisonPluginsTto9 );
        
        
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
	
    private TreeMap<String, RegisteredPluginsData> getSubsetOfPlugins(
			TreeMap<String, RegisteredPluginsData> plugins,
			char rangeLow, char rangeHigh,
			boolean includeNonAlpha ) {
	TreeMap<String, RegisteredPluginsData> results = new TreeMap<>();
	
	Set<String> keys = plugins.keySet();
	for (String key : keys) {
		char keyFirstChar = key.toLowerCase().charAt(0);
		
		if ( Character.isAlphabetic(keyFirstChar) ) {
			
			if ( Character.compare(keyFirstChar, rangeLow) >= 0 && Character.compare( keyFirstChar, rangeHigh) < 0 ) {
				
				results.put( key, plugins.get(key) );
			}
		}
		else {
			
			// Add all non-alpha plugins to this result:
			results.put( key, plugins.get(key) );
		}
	}
	
	return results;
}
    
	public Metrics getbStatsMetrics() {
		return bStatsMetrics;
	}

	public void setbStatsMetrics(Metrics bStatsMetrics) {
		this.bStatsMetrics = bStatsMetrics;
	}
}
