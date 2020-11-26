package tech.mcprison.prison.spigot.configs;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;

/**
 * @author GABRYCA
 **/

public class MessagesConfig extends SpigotConfigComponents{

    // Initialize parameters and variables
    private FileConfiguration conf;
    private int changeCount = 0;

    public MessagesConfig() {
        initialize();
    }

    public void initialize() {

    	// Filepath
        File file = new File(SpigotPrison.getInstance().getDataFolder() + "/module_conf/lang/" + SpigotPrison.getInstance().getConfig().getString("default-language") + ".yml");

        // Check if the config exists
        fileMaker(file);

        // Get the config
    	conf = YamlConfiguration.loadConfiguration(file);
    	
        // Call method
        values();

        if (changeCount > 0) {
        	try {
				conf.save(file);
				Output.get().logInfo("&aThere were &b%d &anew values added for the language files " + "used by the GuiConfig.yml file located at &b%s", changeCount, file.getAbsoluteFile());
			}
			catch (IOException e) {
				Output.get().logInfo("&4Failed to save &b%d &4new values for the language files " + "used by the GuiConfig.yml file located at &b%s&4. " + "&a %s", changeCount, file.getAbsoluteFile(), e.getMessage());
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
        dataConfig("Lore.ActivateWithinMode","&8Activate Within mode.");
        dataConfig("Lore.ActivateRadiusMode","&8Activate Radius mode.");
        dataConfig("Lore.AutoPickupGuiManager","&8AutoPickup Manager.");
        dataConfig("Lore.AutoSmeltGuiManager","&8AutoSmelt Manager.");
        dataConfig("Lore.AutoBlockGuiManager","&8AutoBlock Manager.");
        dataConfig("Lore.BlockType","&3BlockType: ");
        dataConfig("Lore.Blocks","&3Blocks:");
        dataConfig("Lore.Blocks2","&8Manage blocks of the Mine.");
        dataConfig("Lore.ClickToChoose","&8Click to choose.");
        dataConfig("Lore.ClickToConfirm","&8Click to confirm.");
        dataConfig("Lore.ClickToCancel","&8Click to cancel.");
        dataConfig("Lore.ClickToDecrease","&8Click to decrease.");
        dataConfig("Lore.ClickToIncrease","&8Click to increase.");
        dataConfig("Lore.ClickToManageRank","&8Manage this rank.");
        dataConfig("Lore.ClickToManageCommands","&8Manage RankUPCommands.");
        dataConfig("Lore.ClickToOpen","&8Click to open.");
        dataConfig("Lore.ClickToRename", "&8Click to rename.");
        dataConfig("Lore.ClickToTeleport","&8Click to teleport.");
        dataConfig("Lore.ClickToUse","&8Click to use.");
        dataConfig("Lore.ClickToRankup","&8Click to rankup");
        dataConfig("Lore.ClickToEditBlock", "&8Click to edit percentage.");
        dataConfig("Lore.ClickToClose", "&cClick to close the GUI.");
        dataConfig("Lore.ClickToPriorPage", "&aClick to see the prior page.");
        dataConfig("Lore.ClickToNextPage", "&aClick to see the next page.");
        dataConfig("Lore.ClickToStartBlockSetup", "&aClick to setup block.");
        dataConfig("Lore.ClickToAddBlock", "&aClick to add a block.");
        dataConfig("Lore.Chance","&3Chance: ");
        dataConfig("Lore.Command","&3Command: &7");
        dataConfig("Lore.ContainsTheRank","&3The Rank ");
        dataConfig("Lore.ContainsNoCommands"," &3contains no commands.");
        dataConfig("Lore.DisableNotifications","&8Disable notifications.");
        dataConfig("Lore.EnabledAll","&aAll features ON");
        dataConfig("Lore.DisabledAll","&aAll features OFF");
        dataConfig("Lore.FullSoundEnabled","&aFull Inv., notify with sound ON");
        dataConfig("Lore.FullSoundDisabled","&cFull Inv., notify with sound OFF");
        dataConfig("Lore.FullHologramEnabled","&aFull Inv., notify with hologram ON");
        dataConfig("Lore.FullHologramDisabled","&cFull Inv., notify with hologram OFF");
        dataConfig("Lore.Id","&3Rank id: &7");
        dataConfig("Lore.Info","&8&l|&3Info&8|");
        dataConfig("Lore.IfYouHaveEnoughMoney","&8If you have enough money");
        dataConfig("Lore.LadderThereAre","&8There're &3");
        dataConfig("Lore.LadderCommands"," &3Commands at ladder:");
        dataConfig("Lore.LeftClickToConfirm","&aLeft-Click to confirm.");
        dataConfig("Lore.LeftClickToOpen","&8Left Click to open.");
        dataConfig("Lore.LeftClickToReset","&aLeft Click to reset");
        dataConfig("Lore.ManageResetTime","&8Manage the reset time of Mine.");
        dataConfig("Lore.MinesButton","&8Mines GUI manager.");
        dataConfig("Lore.MineName", "&3Mine Name: &f");
        dataConfig("Lore.Name","&3Rank Name: &7");
        dataConfig("Lore.Notifications","&8Edit Mines notifications.");
        dataConfig("Lore.PlayersWithTheRank","&3Players at rank: &7");
        dataConfig("Lore.PrestigeWarning", "&3Prestige will reset: ");
        dataConfig("Lore.PrestigeWarning2", "&3 - &bRank");
        dataConfig("Lore.PrestigeWarning3", "&3 - &bBalance");
        dataConfig("Lore.Price","&3Price: &a$");
        dataConfig("Lore.Price2","&8Price: &a$");
        dataConfig("Lore.Price3","&3Rank Price: &a$");
        dataConfig("Lore.Percentage", "&8Percentage: ");
        dataConfig("Lore.PrisonTasksButton","&8Prison Tasks Manager.");
        dataConfig("Lore.ResetTime","&3Reset time(s): &7");
        dataConfig("Lore.Radius","&8Radius: ");
        dataConfig("Lore.RankupCommands","&8&l|&3RankUPCommands&8| &8&l- &3");
        dataConfig("Lore.Rankup","&aRankup");
        dataConfig("Lore.RanksButton","&8Ranks GUI manager.");
        dataConfig("Lore.ResetButton","&8Resets the mine.");
        dataConfig("Lore.RightClickToCancel","&cRight-Click to cancel.");
        dataConfig("Lore.RightClickToEnable","&aRight click to enable");
        dataConfig("Lore.RightClickToToggle","&cRight click to toggle");
        dataConfig("Lore.SpawnPoint","&3Spawnpoint: &7");
        dataConfig("Lore.StatusLockedMine","&8Status: &cLocked");
        dataConfig("Lore.StatusUnlockedMine","&8Status: &aUnlocked");
        dataConfig("Lore.SpawnPoint2","&8Set the mine spawn point.");
        dataConfig("Lore.SizeOfMine","&3Size of Mine: &7");
        dataConfig("Lore.Selected","&3Selected");
        dataConfig("Lore.ShiftAndRightClickToDelete","&cShift + Right click to delete.");
        dataConfig("Lore.ShiftAndRightClickToDisable","&cShift + Right click to disable");
        dataConfig("Lore.ShiftAndRightClickToToggle","&cShift + Right click to toggle");
        dataConfig("Lore.StatusEnabled","&8Enabled");
        dataConfig("Lore.StatusDisabled","&8Disabled");
        dataConfig("Lore.SkipReset1","&8Skip the reset if ");
        dataConfig("Lore.SkipReset2","&8not enough blocks ");
        dataConfig("Lore.SkipReset3","&8have been mined.");
        dataConfig("Lore.Tp","&8Tp to the mine.");
        dataConfig("Lore.Tag","&3Tag: &8");
        dataConfig("Lore.Tag2","&3Rank Tag: &7");
        dataConfig("Lore.Time","&8Time: ");
        dataConfig("Lore.Volume","&3Volume: &7");
        dataConfig("Lore.Value", "&3Value: &a$");
        dataConfig("Lore.World","&3World: &7");
        dataConfig("Lore.noRanksFoundSetup", "&3There aren't Ranks!");
        dataConfig("Lore.noRanksFoundSetup2", "&3If you want continue the setup.");
        dataConfig("Lore.noRanksFoundSetup3", "&3All Ranks and Mines from A to Z will be made");
        dataConfig("Lore.noRanksFoundSetup4", "&3With &adefault &3values!");
        dataConfig("Lore.noRanksFoundSetup5", "&3You can do the same by command:");
        dataConfig("Lore.noRanksFoundSetup6", "&1/ranks autoConfigure full <price=x> <mult=x>!");
        dataConfig("Lore.noRanksFoundSetup7", "&3Please replace the X with the starting price and");
        dataConfig("Lore.noRanksFoundSetup8", "&3multiplier, default price = 50000, multiplier = 1.5.");
        dataConfig("Message.CantGetRanksAdmin", "&7[&3PRISON WARN&7] &cCan't get Ranks, there might be no ranks or the Ranks module's disabled.");
        dataConfig("Message.CantRunGUIFromConsole", "&7[&3PRISON INFO&7] You cannot run the GUI from the console.");
        dataConfig("Message.DefaultLadderEmpty", "&7[&3PRISON ERROR&7] &cThe default ladder has no rank.");
        dataConfig("Message.NoSellAllItems", "&7[&3PRISON WARN&7] &cSorry but there aren't SellAll Items to show.");
        dataConfig("Message.EmptyGui","&7[&3PRISON WARN&7] &cSorry, the GUI looks empty.");
        dataConfig("Message.NoBlocksMine","&7[&3PRISON WARN&7] &cSorry but there aren't blocks inside this Mine.");
        dataConfig("Message.NoMines", "&7[&3PRISON WARN&7] &cSorry but there aren't Mines to show.");
        dataConfig("Message.NoRankupCommands", "&7[&3PRISON WARN&7] &cSorry, but there aren't rankUpCommands for this ranks, please create one to use this GUI!");
        dataConfig("Message.NoLadders", "&7[&3PRISON WARN&7] &cSorry but there aren't ladders to show.");
        dataConfig("Message.NoRanksPrestigesLadder", "&3[PRISON WARN] &cThere aren't ranks in the -prestiges- ladder!");
        dataConfig("Message.NoRanksFoundAdmin", "&7[&3PRISON WARN&7] &cSorry, but before using this GUI you should create a Rank in this ladder!");
        dataConfig("Message.NoRanksFound", "&7[&3PRISON WARN&7] &cSorry, but there aren't Ranks in the default or selected ladder!");
        dataConfig("Message.NoRanksFoundHelp1", "&7[&3PRISON WARN&7] &cSorry, but there aren't Ranks in the default or selected ladder or the ladder &3[");
        dataConfig("Message.NoRanksFoundHelp2", "]&c isn't found!");
        dataConfig("Message.LadderPrestigesNotFound", "&7[&3PRISON WARN&7] &cLadder -prestiges- not found!");
        dataConfig("Message.TooManyBlocks","&7[&3PRISON WARN&7] &cSorry, but there're too many Blocks and the max's 54 for the GUI");
        dataConfig("Message.TooManyLadders","&7[&3PRISON WARN&7] &cSorry, but there're too many ladders and the max's 54 for the GUI");
        dataConfig("Message.TooManyMines","&7[&3PRISON WARN&7] &cSorry, but there're too many mines and the max's 54 for the GUI");
        dataConfig("Message.TooManyRankupCommands","&7[&3PRISON WARN&7] &cSorry, but there're too many RankupCommands and the max's 54 for the GUI");
        dataConfig("Message.TooManyRanks", "&7[&3PRISON WARN&7] &cSorry, but there're too many ranks and the max's 54 for the GUI");
        dataConfig("Message.TooManySellAllItems", "&7[&3PRISON WARN&7] &cThere are too many items and the MAX for the GUI's 54!");
        dataConfig("Message.ZeroBlocksReset1","&8Set a mine's delay ");
        dataConfig("Message.ZeroBlocksReset2","&8before reset when it ");
        dataConfig("Message.ZeroBlocksReset3","&8reaches zero blocks.");
        dataConfig("Message.mineNameRename", "&7[&3PRISON INFO&7] &3Please write the &6mineName &3you'd like to use and &6submit&3.");
        dataConfig("Message.mineNameRenameClose", "&7[&3PRISON INFO&7] &3Input &cclose &3to cancel or wait &c30 seconds&3.");
        dataConfig("Message.mineNameRenameClosed", "&7[&3PRISON INFO&7] &cRename mine closed, nothing got changed!");
        dataConfig("Message.mineOrGuiDisabled", "&7[&3PRISON INFO&7] &cGUI and/or GUI Mines is not enabled. Check GuiConfig.yml.");
        dataConfig("Message.mineMissingGuiPermission", "&7[&3PRISON INFO&7] &cYou lack the permissions to use GUI mines");
        dataConfig("Message.OutOfTimeNoChanges", "&7[&3PRISON&7] &cYou ran out of time, nothing changed.");
        dataConfig("Message.PrestigeCancelled", "&7[&3PRISON INFO&7] &cPrestige cancelled!");
        dataConfig("Message.PrestigeCancelledWrongKeyword", "&7[&3PRISON INFO&7] &cPrestige cancelled, you didn't type the word: confirm");
        dataConfig("Message.PrestigeRanOutOfTime", "&7[&3PRISON INFO&7] &cYou ran out of time, prestige cancelled.");
        dataConfig("Message.PrestigesDisabledDefault", "&7[&3PRISON INFO&7] &cPrestiges are disabled by default, please edit it in your config.yml!");
        dataConfig("Message.ConfirmPrestige", "&7[&3PRISON INFO&7] &aConfirm&3: Type the word &aconfirm &3 to confirm");
        dataConfig("Message.CancelPrestige", "&7[&3PRISON INFO&7] &cCancel&3: Type the word &ccancel &3to cancel, &cyou've 30 seconds.");
        dataConfig("Message.PrestigesAreDisabled", "&7[&3PRISON INFO&7] &cPrestiges are disabled. Check config.yml.");
        dataConfig("Message.GuiOrPrestigesDisabled", "&7[&3PRISON INFO&7] &cGUI and/or GUI Prestiges is not enabled. Check GuiConfig.yml.");
        dataConfig("Message.CantFindPrestiges", "&7[&3PRISON ERROR&7] &cThe prestige ladder has no prestiges!");
        dataConfig("Message.missingGuiPrestigesPermission", "&7[&3PRISON INFO&7] &cYou lack the permissions to use GUI prestiges");
        dataConfig("Message.rankTagRename", "&7[&3PRISON INFO&7] &3Please write the &6tag &3you'd like to use and &6submit&3.");
        dataConfig("Message.rankTagRenameClose", "&7[&3PRISON INFO&7] &3Input &cclose &3to cancel or wait &c30 seconds&3.");
        dataConfig("Message.rankTagRenameClosed", "&7[&3PRISON INFO&7] &cRename tag closed, nothing got changed!");
        dataConfig("Message.rankGuiDisabledOrAllGuiDisabled", "&7[&3PRISON INFO&7] &cGUI and/or GUI ranks is not enabled. Check GuiConfig.yml (%s %s)");
        dataConfig("Message.rankGuiMissingPermission", "&7[&3PRISON INFO&7] &cYou lack the permissions to use GUI ranks");
        dataConfig("Message.SellAllIsDisabled", "&7[&3PRISON ERROR&7] &cSorry but the SellAll Feature's disabled in the config.yml");
        dataConfig("Message.SellAllEditedWithSuccess", "] edited with success!");
        dataConfig("Message.SellAllSubCommandNotFound", "&7[&3PRISON WARN&7] &cSub-command not found, check with /sellall for a list!");
        dataConfig("Message.SellAllMultipliersAreDisabled", "&7[&3PRISON WARN&7] &cMultipliers are disabled in the SellAll config!");
        dataConfig("Message.SellAllMultiplierWrongFormat", "&7[&3PRISON WARN&7] &cWrong format, please use /sellall multiplier add/delete <Prestige> <Multiplier>");
        dataConfig("Message.SellAllMissingPermission", "&7[&3PRISON WARN&7] &cSorry, but you don't have the permission [");
        dataConfig("Message.SellAllRanksDisabled", "&7[&3PRISON ERROR&7] &cThe Ranks module's disabled or not found!");
        dataConfig("Message.SellAllPrestigeLadderNotFound", "&7[&3PRISON WARN&7] &cCan't find the prestiges ladder, they might be disabled in the config.yml!");
        dataConfig("Message.SellAllCantFindPrestigeOrRank", "&7[&3PRISON WARN&7] &cCan't find the Prestige/Rank: ");
        dataConfig("Message.SellAllRankNotFoundInPrestigeLadder", "&7[&3PRISON WARN&7] &cThe -prestiges- ladder doesn't contain the Rank: ");
        dataConfig("Message.SellAllMultiplierNotANumber", "&7[&3PRISON WARN&7] &cSorry but the multiplier isn't a number [/sellall multiplier add ");
        dataConfig("Message.SellAllMultiplierNotNumber2", " Here-> ");
        dataConfig("Message.SellAllConfigSaveFail", "&7[&3PRISON ERROR&7] &cSorry, something went wrong while saving the config!");
        dataConfig("Message.SellAllMultiplierEditSaveSuccess", "&7[&3PRISON&7] &aMultiplier got added or edited with success!");
        dataConfig("Message.SellAllMultiplierFormat", "&7[&3PRISON WARN&7] &cPlease use this format: /sellall multiplier delete <Prestige>");
        dataConfig("Message.SellAllCantFindMultiplier", "&7[&3PRISON WARN&7] &cCan't find the Multiplier of the prestige ");
        dataConfig("Message.SellAllCantFindMultiplier2", " in the sellallconfig.yml");
        dataConfig("Message.SellAllMultiplierDeleteSuccess", "&7[&3PRISON WARN&7] &aMultiplier deleted with success!");
        dataConfig("Message.SellAllWrongFormatCommand", "&7[&3PRISON WARN&7] &cWrong format, try /sellall for a list of commands.");
        dataConfig("Message.SellAllPleaseAddItem", "&7[&3PRISON WARN&7] &cPlease add an ITEM_ID [example: /sellall add COAL_ORE <price>]");
        dataConfig("Message.SellAllAddPrice", "&7[&3PRISON WARN&7] &cPlease add a price or value for the item [example: /sellall add COAL_ORE 100]");
        dataConfig("Message.SellAllWrongID", "&7[&3PRISON WARN&7] &cSorry but the ITEM_ID's wrong, please check it [/sellall");
        dataConfig("Message.SellAllValueNotNumber", "&7[&3PRISON WARN&7] &cSorry but the value isn't a number [/sellall");
        dataConfig("Message.SellAllMissingID", "&7[&3PRISON WARN&7] &cPlease add an ITEM_ID [example: /sellall delete COAL_ORE]");
        dataConfig("Message.SellAllTagWarn", "&7[&3PRISON WARN&7] &c");
        dataConfig("Message.SellAllNotFoundStringConfig", " not found in the config or got already deleted");
        dataConfig("Message.SellAllPrisonTag", "&7[&3PRISON&7]&a ");
        dataConfig("Message.SellAllDeletedSuccess", " Deleted with success!");
        dataConfig("Message.SellAllCommandEditSuccess", "] added with success!");
        dataConfig("Message.SellAllYouArentPlayer", "&7[&3PRISON ERROR&7]&c You aren't a player");
        dataConfig("Message.SellAllNothingToSell", "&7[&3PRISON&7]&c You have nothing to sell!");
        dataConfig("Message.SellAllYouGotMoney", "&7[&3PRISON&7]&a You got $");
        dataConfig("Message.SellAllGUIDisabled", "&7[&3PRISON ERROR&7] Sorry but the GUI's disabled in the SellAllConfig.yml");
        dataConfig("Setup.Message.MissingPermission", "&7[&3PRISON ERROR&7] &cSorry but you don't have the permission [-prison.setup- or -prison.admin-]!");
        dataConfig("Setup.Message.WrongFormat", "&7[&3PRISON ERROR&7] &cYou're missing the last argument -mines- or -ranks-, /<command> setup -mines- or -ranks- !");
        dataConfig("Setup.Message.WelcomeToRanksSetup", "&7[&3PRISON INFO&7] &3Hi and welcome to the ranks setup, please wait until it'll be completed!");
        dataConfig("Setup.Message.SuccessRanksSetup", "&7[&3PRISON INFO&7] &3The ranks setup got completed with &asuccess&3 and the ranks got added to the default ladder,\n please check the logs if something's missing!");
        dataConfig("Setup.Message.Aborted", "&7[&3PRISON INFO&7] &3Prison Setup Cancelled.");
    }

    public FileConfiguration getFileGuiMessagesConfig(){
        return conf;
    }
}
