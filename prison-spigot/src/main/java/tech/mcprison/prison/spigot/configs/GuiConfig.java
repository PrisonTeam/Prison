package tech.mcprison.prison.spigot.configs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;

/**
 * @author GABRYCA
 * */
public class GuiConfig extends SpigotConfigComponents{

    // Declaring parameters and variables
    private FileConfiguration conf;
    private int changeCount = 0;

    // Check if the GuiConfig's enabled
    public GuiConfig() {
        // Will make ALWAYS the config even if GUIs are disabled
        initialize();
    }

    public void initialize() {

    	// Filepath
        File file = new File(SpigotPrison.getInstance().getDataFolder() + "/GuiConfig.yml");
    	fileMaker(file);
    	conf = YamlConfiguration.loadConfiguration(file);
    	
        // Values to write down into the config
        values();

        if (conf.getList("EditableLore.Ranks") == null){
            List<String> lore = new ArrayList<>();
            lore.add(" ");
            lore.add("&8-----------------------");
            lore.add("&3Ladder: &a{ladderName}");
            lore.add("&3Mines:  &a{linkedMines}");
            lore.add(" ");
            lore.add("&3Rank Price: &a${rankPrice}");
            lore.add(" ");
            lore.add("&7Players:    &3%prison_rank__player_count_{rankName}%");
            lore.add("&8-----------------------");
            lore.add("&7Total Cost: &3%prison_rank__player_cost_formatted_{rankName}%");
            lore.add("&7Multiplier: &3{rankMultiplier}");
            lore.add("&7Balance:    &3%prison_player_balance_default%");
            lore.add("&7Remaining:  &3%prison_rank__player_cost_remaining_formatted_{rankName}%");
            lore.add("%prison_rank__player_cost_bar_{rankName}%");
            lore.add("&8-----------------------");

            conf.set("EditableLore.Ranks", lore);
            changeCount++;
        }

        if ( conf.getList( "EditableLore.README-Updated-2022-12-22" ) == null ) {
        	 List<String> lore = new ArrayList<>();
             lore.add(" ");
             lore.add("&8-----------------------");
             lore.add("&7 WARNING!! DO NOT EDIT THESE!!");
             lore.add("&7  - THESE ARE JUST INFORMATIONAL NOTES AND WILL BE IGNORED BY PRISON.");
             lore.add(" ");
             lore.add("&7 PLEASE READ - IMPORTANT CHANGESE!!");
             lore.add("&7  - You can safely remove other 'EditableLore.README*' notes since they ");
             lore.add("&7      are now obsolete.");
             lore.add("&7  - Remove everything under 'EditableLore.Mines' and then reload the GUI: ");
             lore.add("&7      /prison reload gui ");
             lore.add("&7    The Mines LORE layout has been updated and this will help apply the ");
             lore.add("&7      new update to your configs");
             lore.add("&8-----------------------");
             lore.add("&7 There are three types of placeholders that will work with EditableLore:");
             lore.add("&7 1. GUI placeholders");
             lore.add("&7 2. '/prison placeholders list'");
             lore.add("&7 3. Any other placeholder through PlaceholderAPI");
             lore.add("&8-----------------------");
             lore.add("&7GUI Placeholders are only the following and they are evaluated first ");
             lore.add("&7so they can be nested in the prison placeholders: ");
             lore.add("&7  ");
             lore.add("&7 For Ranks ONLY: ");
             lore.add("&7   {rankName} {rankTag} {rankPrice} {rankMultiplier}");
             lore.add("&7   {ladderName} {linkedMines}");
             lore.add("&7   {linkedMines}");
             lore.add("&7  ");
             lore.add("&7 For Mines ONLY: ");
             lore.add("&7   {mineName} {mineTag} {mineSize} {mineVolume} ");
             lore.add("&7   {mineRemaining} {mineRemaingPercent} ");
             lore.add("&7   {clickToTeleport} {lockStatus} {playerCount} {linkedRank} ");
             lore.add("&8-----------------------");
             lore.add("&7Prison placeholders can include any that are within these placeholder ");
             lore.add("&7Groups: PLAYER, RANKS, RANKPLAYERS, MINES, STATSMINES, and STATSRANKS");
             lore.add("&8-----------------------");
             lore.add("&7To use dyanamic placeholders, you need to use a combination of these two");
             lore.add("&7types of placeholders. You use the GUI placeholders to inject the rank");
             lore.add("&7name, or mine name, in to the prison placeholders. When injecting the ");
             lore.add("&7names, do not use the tags since those will also inject formatting codes");
             lore.add("&7which will corrupt the placeholders.");
             lore.add("&7Cost:      &3%prison_rank__player_cost_formatted_{rankName}%");
             lore.add("&7Remaining: &3%prison_rank__player_cost_remaining_formatted_{rankName}%");
             lore.add("%prison_rank__player_cost_bar_{rankName}%");
             lore.add("&8-----------------------");
        	

             conf.set("EditableLore.README-Updated-2022-12-22", lore);
             changeCount++;
        }
        
        

        if (conf.getList("EditableLore.Rank.default.A") == null){
            List<String> lore = new ArrayList<>();
            lore.add(" ");
            lore.add("&8-----------------------");
            lore.add("&3The start of an amazing adventure.");
            lore.add("&8-----------------------");

            conf.set("EditableLore.Rank.default.A", lore);
            changeCount++;
        }
        
        if (conf.getList("EditableLore.Rank.default.Z") == null){
        	List<String> lore = new ArrayList<>();
        	lore.add(" ");
        	lore.add("&8-----------------------");
        	lore.add("&3The end of the most amazing adventure.");
        	lore.add("&3But... if you do '/prestige' you can start");
        	lore.add("&3start over and have even more fun!");
        	lore.add("&8-----------------------");
        	
        	conf.set("EditableLore.Rank.default.Z", lore);
        	changeCount++;
        }

        if (conf.getList("EditableLore.Rank.prestiges.p1") == null){
            List<String> lore = new ArrayList<>();
            lore.add(" ");
            lore.add("&8-----------------------");
            lore.add("&3This is a very prestigious rank!");
            lore.add("&8-----------------------");

            conf.set("EditableLore.Rank.prestiges.p1", lore);
            changeCount++;
        }

        if (conf.getList("EditableLore.Mines") == null){
            List<String> lore = new ArrayList<>();
            lore.add(" ");
            lore.add("&8-----------------------");
            lore.add("&3Mine:   &a{mineName} &r{mineTag}");
            lore.add("&3Rank:   &a{linkedRank}");
            lore.add(" ");
            lore.add("&3Size:   &a{mineSize}");
            lore.add("&3Volume: &a{mineVolume}");
            lore.add("&3Blocks Remaining: &a{mineRemaining} &a{mineRemainingPercent}%");
            lore.add("&3Players in Mine: &a{playerCount}");
            lore.add(" ");
            lore.add("  {clickToTeleport}");
            lore.add("  {lockStatus}");
            lore.add("&8-----------------------");
            
            conf.set("EditableLore.Mines", lore);
            changeCount++;
        }


        if (conf.getList("EditableLore.Mine.A") == null){
            List<String> lore = new ArrayList<>();
            lore.add(" ");
            lore.add("&8-----------------------");
            lore.add("&3Where new players start their awesome adventure!");
            lore.add("&8-----------------------");
            
            conf.set("EditableLore.Mine.A", lore);
            changeCount++;
        }
        
        if (conf.getList("EditableLore.Mine.Z") == null){
        	List<String> lore = new ArrayList<>();
        	lore.add(" ");
        	lore.add("&8-----------------------");
        	lore.add("&3Time to get ready to prestige, and restart the fun adventure!");
        	lore.add("&8-----------------------");
        	
        	conf.set("EditableLore.Mine.Z", lore);
        	changeCount++;
        }
        
        if ( conf.get( "Options.Mines.MaterialType" ) == null ) {
        	
        	if ( PrisonMines.getInstance() != null ) {

        		LinkedHashMap<String,String> map = new LinkedHashMap<>();
        		
        		map.put("NoMineAccess", XMaterial.REDSTONE_BLOCK.name() );
        				
        		for ( Mine mine : PrisonMines.getInstance().getMineManager().getMines() ) {
        			if ( mine.getPrisonBlocks().size() > 0 ) {
        				map.put( mine.getName(), mine.getPrisonBlocks().get(0).getBlockName() );
        			}
        		}
        		
        		conf.set("Options.Mines.MaterialType", map);
        		changeCount++;
        	}
        }
        else if ( conf.get( "Options.Mines.MaterialType.NoMineAccess" ) == null ) {
        	
        	String noMineAccess = XMaterial.REDSTONE_BLOCK.name();
        	
        	conf.set("Options.Mines.MaterialType.NoMineAccess", noMineAccess );
        	changeCount++;
        }
        
        // Count and save
        if (changeCount > 0) {
        	try {
				conf.save(file);
				Output.get().logInfo("&aThere were &b%d &anew values added to the GuiConfig.yml file located at &b%s", changeCount, file.getAbsoluteFile());
			}
			catch (IOException e) {
				Output.get().logInfo("&4Failed to save &b%d &4new values to the GuiConfig.yml file located at " + "&b%s&4. " + "&a %s", changeCount, file.getAbsoluteFile(), e.getMessage());
			}
        }

        conf = YamlConfiguration.loadConfiguration(file);
    }

    private void dataConfig(String key, Object value){
    	if (conf.getString(key) == null) {
    		conf.set(key, value);
    		changeCount++;
    	}
    }

    // All the strings of the config should be here
    private void values(){
        dataConfig("Options.Ranks.GUI_Enabled", true);
        dataConfig("Options.Ranks.Permission_GUI_Enabled", false);
        dataConfig("Options.Ranks.Permission_GUI","prison.gui.ranks");
        dataConfig("Options.Ranks.Item_gotten_rank","TRIPWIRE_HOOK");
        dataConfig("Options.Ranks.Item_not_gotten_rank","REDSTONE_BLOCK");
        dataConfig("Options.Ranks.Enchantment_effect_current_rank", true);
        dataConfig("Options.Ranks.Ladder","default");
        dataConfig("Options.Ranks.Number_of_Rank_Player_GUI",  false);
        dataConfig("Options.Mines.GUI_Enabled", true);
        dataConfig("Options.Mines.Permission_GUI_Enabled", false);
        dataConfig("Options.Mines.Permission_GUI","prison.gui.mines");
        dataConfig("Options.Mines.PermissionWarpPlugin","mines.tp.");
        dataConfig("Options.Mines.CommandWarpPlugin","mines tp");
        dataConfig("Options.Prestiges.GUI_Enabled", true);
        dataConfig("Options.Prestiges.Permission_GUI_Enabled", false);
        dataConfig("Options.Prestiges.Permission_GUI","prison.gui.prestiges");
        dataConfig("Options.Setup.EnabledGUI", true);
        dataConfig("Options.Titles.PlayerRanksGUI", "&3Player -> Ranks");
        dataConfig("Options.Titles.PlayerPrestigesGUI", "&3Player -> Prestiges");
        dataConfig("Options.Titles.PlayerMinesGUI", "&3Player -> Mines");
    }

    // Return method to call the config, you can use this or the global one in the main class
    public FileConfiguration getFileGuiConfig(){
        return conf;
    }
}
