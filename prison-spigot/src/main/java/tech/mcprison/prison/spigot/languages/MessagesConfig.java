package tech.mcprison.prison.spigot.languages;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;

/**
 * @author GABRYCA
 **/

public class MessagesConfig {

    // Initialize parameters and variables
    private FileConfiguration conf;
    private int changeCount = 0;

    public MessagesConfig() {

        initialize();
    }

    public void initialize() {

    	// Filepath
        File file = new File(SpigotPrison.getInstance().getDataFolder() +
                "/languages/" + SpigotPrison.getInstance().getConfig().getString("default-language") + ".yml");

    	if( !file.exists() ) {
    		try {
    			File parentDir = file.getParentFile();
    			parentDir.mkdirs();
    			
    			file.createNewFile();
    		} 
    		catch (IOException e) {
    			e.printStackTrace();
    		}
    	} 
    	
    	conf = YamlConfiguration.loadConfiguration(file);
    	
        // Everything's here (not anymore...)
        values();

        if ( changeCount > 0 ) {
        	try {
				conf.save(file);
				
				Output.get().logInfo( "&aThere were &b%d &anew values added for the language files " +
						"used by the GuiConfig.yml file located at &b%s", 
						changeCount, file.getAbsoluteFile() );
			}
			catch ( IOException e ) {

				Output.get().logInfo( "&4Failed to save &b%d &4new values for the language files " +
						"used by the GuiConfig.yml file located at &b%s&4. " +
						"&a %s", changeCount, file.getAbsoluteFile(), e.getMessage() );
			}
        }
    }

    private void dataConfig(String key, String value){
    	if (conf.getString(key) == null) {
    		conf.set(key, value);
    		changeCount++;
    	}
    }

    // All the strings should be here
    private void values(){
        dataConfig("Gui.Lore.ActivateWithinMode","&8Activate Within mode.");
        dataConfig("Gui.Lore.ActivateRadiusMode","&8Activate Radius mode.");
        dataConfig("Gui.Lore.AutoPickupGuiManager","&8AutoPickup Manager.");
        dataConfig("Gui.Lore.AutoSmeltGuiManager","&8AutoSmelt Manager.");
        dataConfig("Gui.Lore.AutoBlockGuiManager","&8AutoBlock Manager.");
        dataConfig("Gui.Lore.BlockType","&3BlockType: ");
        dataConfig("Gui.Lore.Blocks","&3Blocks:");
        dataConfig("Gui.Lore.Blocks2","&8Manage blocks of the Mine.");
        dataConfig("Gui.Lore.ClickToChoose","&8Click to choose.");
        dataConfig("Gui.Lore.ClickToConfirm","&8Click to confirm.");
        dataConfig("Gui.Lore.ClickToCancel","&8Click to cancel.");
        dataConfig("Gui.Lore.ClickToDecrease","&8Click to decrease.");
        dataConfig("Gui.Lore.ClickToIncrease","&8Click to increase.");
        dataConfig("Gui.Lore.ClickToManageRank","&8Manage this rank.");
        dataConfig("Gui.Lore.ClickToManageCommands","&8Manage RankUPCommands.");
        dataConfig("Gui.Lore.ClickToOpen","&8Click to open.");
        dataConfig("Gui.Lore.ClickToRename", "&8Click to rename.");
        dataConfig("Gui.Lore.ClickToTeleport","&8Click to teleport.");
        dataConfig("Gui.Lore.ClickToUse","&8Click to use.");
        dataConfig("Gui.Lore.ClickToRankup","&8Click to rankup");
        dataConfig("Gui.Lore.ClickToEditBlock", "&8Click to edit percentage.");
        dataConfig("Gui.Lore.ClickToClose", "&cClick to close the GUI.");
        dataConfig("Gui.Lore.ClickToPriorPage", "&aClick to see the prior page.");
        dataConfig("Gui.Lore.ClickToNextPage", "&aClick to see the next page.");
        dataConfig("Gui.Lore.ClickToStartBlockSetup", "&aClick to setup block.");
        dataConfig("Gui.Lore.ClickToAddBlock", "&aClick to add a block.");
        dataConfig("Gui.Lore.Chance","&3Chance: ");
        dataConfig("Gui.Lore.Command","&3Command: &7");
        dataConfig("Gui.Lore.ContainsTheRank","&3The Rank ");
        dataConfig("Gui.Lore.ContainsNoCommands"," &3contains no commands.");
        dataConfig("Gui.Lore.DisableNotifications","&8Disable notifications.");
        dataConfig("Gui.Lore.EnabledAll","&aAll features ON");
        dataConfig("Gui.Lore.DisabledAll","&aAll features OFF");
        dataConfig("Gui.Lore.FullSoundEnabled","&aFull Inv., notify with sound ON");
        dataConfig("Gui.Lore.FullSoundDisabled","&cFull Inv., notify with sound OFF");
        dataConfig("Gui.Lore.FullHologramEnabled","&aFull Inv., notify with hologram ON");
        dataConfig("Gui.Lore.FullHologramDisabled","&cFull Inv., notify with hologram OFF");
        dataConfig("Gui.Lore.Id","&3Rank id: &7");
        dataConfig("Gui.Lore.Info","&8&l|&3Info&8|");
        dataConfig("Gui.Lore.IfYouHaveEnoughMoney","&8If you have enough money");
        dataConfig("Gui.Lore.LadderThereAre","&8There're &3");
        dataConfig("Gui.Lore.LadderCommands"," &3Commands at ladder:");
        dataConfig("Gui.Lore.LeftClickToConfirm","&aLeft-Click to confirm.");
        dataConfig("Gui.Lore.LeftClickToOpen","&8Left Click to open.");
        dataConfig("Gui.Lore.LeftClickToReset","&aLeft Click to reset");
        dataConfig("Gui.Lore.ManageResetTime","&8Manage the reset time of Mine.");
        dataConfig("Gui.Lore.MinesButton","&8Mines GUI manager.");
        dataConfig("Gui.Lore.MineName", "&3Mine Name: &f");
        dataConfig("Gui.Lore.Name","&3Rank Name: &7");
        dataConfig("Gui.Lore.Notifications","&8Edit Mines notifications.");
        dataConfig("Gui.Lore.PlayersWithTheRank","&3Players at rank: &7");
        dataConfig("Gui.Lore.PrestigeWarning", "&3Prestige will reset: ");
        dataConfig("Gui.Lore.PrestigeWarning2", "&3 - &bRank");
        dataConfig("Gui.Lore.PrestigeWarning3", "&3 - &bBalance");
        dataConfig("Gui.Lore.Price","&3Price: &a$");
        dataConfig("Gui.Lore.Price2","&8Price: &a$");
        dataConfig("Gui.Lore.Price3","&3Rank Price: &a$");
        dataConfig("Gui.Lore.Percentage", "&8Percentage: ");
        dataConfig("Gui.Lore.PrisonTasksButton","&8Prison Tasks Manager.");
        dataConfig("Gui.Lore.ResetTime","&3Reset time(s): &7");
        dataConfig("Gui.Lore.Radius","&8Radius: ");
        dataConfig("Gui.Lore.RankupCommands","&8&l|&3RankUPCommands&8| &8&l- &3");
        dataConfig("Gui.Lore.Rankup","&aRankup");
        dataConfig("Gui.Lore.RanksButton","&8Ranks GUI manager.");
        dataConfig("Gui.Lore.ResetButton","&8Resets the mine.");
        dataConfig("Gui.Lore.RightClickToCancel","&cRight-Click to cancel.");
        dataConfig("Gui.Lore.RightClickToEnable","&aRight click to enable");
        dataConfig("Gui.Lore.RightClickToToggle","&cRight click to toggle");
        dataConfig("Gui.Lore.SpawnPoint","&3Spawnpoint: &7");
        dataConfig("Gui.Lore.StatusLockedMine","&8Status: &cLocked");
        dataConfig("Gui.Lore.StatusUnlockedMine","&8Status: &aUnlocked");
        dataConfig("Gui.Lore.SpawnPoint2","&8Set the mine spawn point.");
        dataConfig("Gui.Lore.SizeOfMine","&3Size of Mine: &7");
        dataConfig("Gui.Lore.Selected","&3Selected");
        dataConfig("Gui.Lore.ShiftAndRightClickToDelete","&cShift + Right click to delete.");
        dataConfig("Gui.Lore.ShiftAndRightClickToDisable","&cShift + Right click to disable");
        dataConfig("Gui.Lore.ShiftAndRightClickToToggle","&cShift + Right click to toggle");
        dataConfig("Gui.Lore.StatusEnabled","&8Enabled");
        dataConfig("Gui.Lore.StatusDisabled","&8Disabled");
        dataConfig("Gui.Lore.SkipReset1","&8Skip the reset if ");
        dataConfig("Gui.Lore.SkipReset2","&8not enough blocks ");
        dataConfig("Gui.Lore.SkipReset3","&8have been mined.");
        dataConfig("Gui.Lore.Tp","&8Tp to the mine.");
        dataConfig("Gui.Lore.Tag","&3Tag: &8");
        dataConfig("Gui.Lore.Tag2","&3Rank Tag: &7");
        dataConfig("Gui.Lore.Time","&8Time: ");
        dataConfig("Gui.Lore.Volume","&3Volume: &7");
        dataConfig("Gui.Lore.Value", "&3Value: &a$");
        dataConfig("Gui.Lore.World","&3World: &7");
        dataConfig("Gui.Lore.noRanksFoundSetup", "&3There aren't Ranks!");
        dataConfig("Gui.Lore.noRanksFoundSetup2", "&3If you want continue the setup.");
        dataConfig("Gui.Lore.noRanksFoundSetup3", "&3All Ranks and Mines from A to Z will be made");
        dataConfig("Gui.Lore.noRanksFoundSetup4", "&3With &adefault &3values!");
        dataConfig("Gui.Lore.noRanksFoundSetup5", "&3You can do the same by command:");
        dataConfig("Gui.Lore.noRanksFoundSetup6", "&1/ranks autoConfigure full <price=x> <mult=x>!");
        dataConfig("Gui.Lore.noRanksFoundSetup7", "&3Please replace the X with the starting price and");
        dataConfig("Gui.Lore.noRanksFoundSetup8", "&3multiplier, default price = 50000, multiplier = 1.5.");
        dataConfig("Gui.Message.CantGetRanksAdmin", "&3[PRISON WARN] &cCan't get Ranks, there might be no ranks or the Ranks module's disabled.");
        dataConfig("Gui.Message.NoSellAllItems", "&cSorry but there aren't SellAll Items to show.");
        dataConfig("Gui.Message.EmptyGui","&cSorry, the GUI looks empty.");
        dataConfig("Gui.Message.NoBlocksMine","&cSorry but there aren't blocks inside this Mine.");
        dataConfig("Gui.Message.NoMines", "&cSorry but there aren't Mines to show.");
        dataConfig("Gui.Message.NoRankupCommands", "&cSorry, but there aren't rankUpCommands for this ranks, please create one to use this GUI!");
        dataConfig("Gui.Message.NoLadders", "&cSorry but there aren't ladders to show.");
        dataConfig("Gui.Message.NoRanksPrestigesLadder", "&3[PRISON WARN] &cThere aren't ranks in the -prestiges- ladder!");
        dataConfig("Gui.Message.NoRanksFoundAdmin", "&cSorry, but before using this GUI you should create a Rank in this ladder!");
        dataConfig("Gui.Message.NoRanksFound", "&cSorry, but there aren't Ranks in the default or selected ladder!");
        dataConfig("Gui.Message.NoRanksFoundHelp1", "&cSorry, but there aren't Ranks in the default or selected ladder or the ladder &3[");
        dataConfig("Gui.Message.NoRanksFoundHelp2", "]&c isn't found!");
        dataConfig("Gui.Message.LadderPrestigesNotFound", "&3[PRISON WARN] &cLadder -prestiges- not found!");
        dataConfig("Gui.Message.TooManyBlocks","&cSorry, but there're too many Blocks and the max's 54 for the GUI");
        dataConfig("Gui.Message.TooManyLadders","&cSorry, but there're too many ladders and the max's 54 for the GUI");
        dataConfig("Gui.Message.TooManyMines","&cSorry, but there're too many mines and the max's 54 for the GUI");
        dataConfig("Gui.Message.TooManyRankupCommands","&cSorry, but there're too many RankupCommands and the max's 54 for the GUI");
        dataConfig("Gui.Message.TooManyRanks", "&cSorry, but there're too many ranks and the max's 54 for the GUI");
        dataConfig("Gui.Message.TooManySellAllItems", "&3[PRISON WARN] &cThere are too many items and the MAX for the GUI's 54!");
        dataConfig("Gui.Message.ZeroBlocksReset1","&8Set a mine's delay ");
        dataConfig("Gui.Message.ZeroBlocksReset2","&8before reset when it ");
        dataConfig("Gui.Message.ZeroBlocksReset3","&8reaches zero blocks.");
        dataConfig("Gui.Message.mineNameRename", "&7[&3Info&7] &3Please write the &6mineName &3you'd like to use and &6submit&3.");
        dataConfig("Gui.Message.mineNameRenameClose", "&7[&3Info&7] &3Input &cclose &3to cancel or wait &c30 seconds&3.");
        dataConfig("Gui.Message.OutOfTimeNoChanges", "&cYou ran out of time, nothing changed.");
        dataConfig("Gui.Message.rankTagRename", "&7[&3Info&7] &3Please write the &6tag &3you'd like to use and &6submit&3.");
        dataConfig("Gui.Message.rankTagRenameClose", "&7[&3Info&7] &3Input &cclose &3to cancel or wait &c30 seconds&3.");
        dataConfig("Setup.Message.MissingPermission", "&7[&cError&7] &cSorry but you don't have the permission [-prison.setup- or -prison.admin-]!");
        dataConfig("Setup.Message.WrongFormat", "&7[&cError&7] &cYou're missing the last argument -mines- or -ranks-, /<command> setup -mines- or -ranks- !");
        dataConfig("Setup.Message.WelcomeToRanksSetup", "&7[&3Info&7] &3Hi and welcome to the ranks setup, please wait until it'll be completed!");
        dataConfig("Setup.Message.SuccessRanksSetup", "&7[&3Info&7] &3The ranks setup got completed with &asuccess&3 and the ranks got added to the default ladder,\n please check the logs if something's missing!");
        dataConfig("Setup.Message.Aborted", "&7[&3Info&7] &3Prison Setup Cancelled.");
    }

    public FileConfiguration getFileGuiMessagesConfig(){
        return conf;
    }

}
