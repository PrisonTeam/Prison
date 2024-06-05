package tech.mcprison.prison.spigot;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.autofeatures.AutoFeaturesWrapper;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.sellall.PrisonSellall;
import tech.mcprison.prison.spigot.customblock.PrisonItemsAdder;
import tech.mcprison.prison.spigot.sellall.SellAllUtil;

public class SpigotCommand {
	
	public SpigotCommand() {
		super();
		
		// Register these commands:
		Prison.get().getCommandHandler().registerCommands( this );
	}
	
    

    @Command(identifier = "prison support troubleshoot autosell", 
    		description = "Prison support troubleshooting: autosell. "
    				+ "This command can be ran at any time. It does not make "
    				+ "any changes to any configs. This will help identify if "
    				+ "autosell is properly configured, and if not, then it "
    				+ "will suggest changes.", 
    		aliases = "prison reload players",
    				onlyPlayers = false, permissions = "ranks.set")
    public void supportRanksCmd(CommandSender sender ){
    	
    	List<String> msgs = new ArrayList<>();
    	
    	List<String> perms = new ArrayList<>();
    	perms.add( "prison.admin" );
    	perms.add( "prison.sellall.delay" );
    	perms.add( "prison.autosell.edit" );
    	perms.add( "prison.sellall.toggle" );
    	perms.add( "" );
    	perms.add( "" );

    	msgs.add( "&3Prison support troubleshoot autosell:" );
    	
    	msgs.add( "  The following information is intended to help confirmm if " );
    	msgs.add( "  autosell is enabled, and if it isn't then it will identify " );
    	msgs.add( "  what settings need to be changed to enable it." );
    	
    	boolean sellall = SpigotPrison.getInstance().isSellAllEnabled();
    	
    	if ( !sellall ) {
    		msgs.add( "&cWarning: sellall is disabled. It needs to be turned on." );
 
    		YamlConfiguration modulesConf = SpigotPrison.getInstance().loadConfig("modules.yml");
    		String sellallModuleName = PrisonSellall.MODULE_NAME.toLowerCase();
            boolean isSellallModuleDefined = modulesConf.contains(sellallModuleName);
    		
            if ( isSellallModuleDefined ) {
            	
            	msgs.add( "  The sellall module is enabled." );
            	msgs.add( "&c  But sellall is disabled." );
            } 
            else {
            	msgs.add( "&c  The sellall module is not enabled; please enable." );
            	msgs.add( "&c    Enable by editing file: '&6plugins/Prison/modules.yml&c'" );
            	msgs.add( "&c    Ensure '&6sellall: true&c' is set to '&6true&c' " );
            	msgs.add( "&c    Use '&2/prison reload sellall&c' to load the autosell settings." );
            	
            }
            perms.add( "" );
    		
            msgs.add( "  Note: In the 'plugins/Prison/config.yml' there is an older setting " );
            msgs.add( "    'sellall: true' which is no longer used.  Do not add it, or enable it, " );
            msgs.add( "    since it will be ignored." );

            perms.add( "" );
            
    	}
    	else {
    		SellAllUtil saUtils = SellAllUtil.get();
    		
    		// Options.Sell_Permission_Enabled: 'false'
    		// Options.Sell_Permission: prison.admin
    		// Options.Full_Inv_AutoSell: true
    		// Options.Full_Inv_AutoSell_Notification: 'true'
    		// Options.Full_Inv_AutoSell_perUserToggleable: true
    		// Options.Full_Inv_AutoSell_perUserToggleable_Need_Perm: 'false'
    		// Options.Full_Inv_AutoSell_PerUserToggleable_Permission: prison.sellall.toggle
    		// Options.Sell_Per_Block_Permission_Enabled: 'false'
    		// Options.Sell_Per_Block_Permission: prison.sellall.
    		// Options.SellAll_ignoreCustomNames: false

    		boolean saPermsEnabled = saUtils.isSellAllSellPermissionEnabled;
    		String saPermsStr = saUtils.permissionSellAllSell;
    		boolean saFullInvAutosell = saUtils.isAutoSellEnabled;
    		boolean saUserToggle = saUtils.isAutoSellPerUserToggleable;
    		boolean saUserTogglePermEnable = saUtils.isAutoSellPerUserToggleablePermEnabled;
    		String saUserTogglePermStr = saUtils.permissionAutoSellPerUserToggleable;

    		
    		msgs.add( "  SellAll is enabled." );
    		
    		msgs.add( "    Perms enabled: " + Boolean.toString( saPermsEnabled ));
    		msgs.add( "      'Options.Sell_Permission_Enabled: " + Boolean.toString( saPermsEnabled ) + "'" );
    		if ( saPermsEnabled ) {
    			
    			msgs.add( "    Perm: 'Options.Sell_Permission: " + saPermsStr + "'" );
    		}
    		msgs.add( "    SellAll autosell on full inventory: " + Boolean.toString( saFullInvAutosell ) );
    		msgs.add( "      'Options.Full_Inv_AutoSell: " + Boolean.toString( saFullInvAutosell ) + "'" );
    		
    		msgs.add( "    SellAll user controlled autosell toggle enabled: " + 
    							Boolean.toString( saUserToggle ) );
    		msgs.add( "      'Options.Full_Inv_AutoSell_perUserToggleable: " + Boolean.toString( saUserToggle ) + "'" );
    		msgs.add( "    SellAll user toggle perms enabled: " + Boolean.toString( saUserTogglePermEnable ) );
    		if ( saUserTogglePermEnable ) {
    			
    			msgs.add( "      'Options.Full_Inv_AutoSell_PerUserToggleable_Permission: " + saUserTogglePermStr + "'" );
    		}
    		msgs.add( "    " );
    		
    		if ( !saFullInvAutosell ) {
    			
    			msgs.add( "&c  To get auto sell to work, you must enable the SellAll autosell on full inventory." );
    			msgs.add( "&c  See the above setting on what needs to be changed in the autosell configs." );
    		}
    		
    		msgs.add( "    " );
    		
    		
    		AutoFeaturesWrapper afWrap = AutoFeaturesWrapper.getInstance();
    		
    		boolean afAutoManagerEnabled = afWrap.isBoolean( AutoFeatures.isAutoManagerEnabled );
    		
    		if ( !afAutoManagerEnabled ) {
    			
    			msgs.add( "&c  AutoManager is disabled. This needs to be enabled." );
    			msgs.add( "&c    Config file: '&6plugins/Prison/autoFeaturesConfig.yml&c'" );
    			msgs.add( "&c    Set '&6autoManager.isAutoManagerEnabled: true&c' to a value of '&6true&c'." );
    			msgs.add( "&c    Use '&2/prison reload autofeatures&c' to reload the auto features settings." );
    			msgs.add( "  " );
    		}
    		else {
    			
    			boolean afAutoSellIfInventoryIsFull = afWrap.isBoolean( AutoFeatures.isAutoSellIfInventoryIsFull );
    			boolean afAutoSellPerBlock = afWrap.isBoolean( AutoFeatures.isAutoSellPerBlockBreakEnabled );
    			
    			String afAutoSellPerBlockPerm = afWrap.getMessage( AutoFeatures.permissionAutoSellPerBlockBreakEnabled );
    			boolean afAutoSellPerBlockPermIsEnabled = 
    					!afAutoSellPerBlockPerm.equalsIgnoreCase( "disable" ) &&
    					!afAutoSellPerBlockPerm.equalsIgnoreCase( "disabled" ) &&
    					!afAutoSellPerBlockPerm.equalsIgnoreCase( "false" )
    					;

    			boolean afAutoSellForceCheck = afWrap.isBoolean( AutoFeatures.isForceSellAllOnInventoryWhenBukkitBlockBreakEventFires );
    			boolean afAutoSellForceDelayedCheck = afWrap.isBoolean( AutoFeatures.isEnabledDelayedSellAllOnInventoryWhenBukkitBlockBreakEventFires );
    			long afAutoSellForceDelayedCheckTicks = afWrap.getInteger( AutoFeatures.isEnabledDelayedSellAllOnInventoryDelayInTicks );
    			
    			boolean afAutoSellForceDebugLoggin = afWrap.isBoolean( AutoFeatures.isAutoSellLeftoversForceDebugLogging );
    			
    			msgs.add( "  AutoManager is enabled. This is required to use autosell features." );
    			msgs.add( "    AutoSell on full inventory: " + Boolean.toString( afAutoSellIfInventoryIsFull ) );
    			msgs.add( "      Note: This setting applies only to the processing of block breakage events.  Other " );
    			msgs.add( "            ways blocks get in to the player's inventory will not trigger this autosell." );
    			if ( !afAutoSellIfInventoryIsFull ) {
    				
    				msgs.add( "        To enable set: 'options.inventory.isAutoSellIfInventoryIsFull: true' to a value of true."   );
    			}
    			msgs.add( "    AutoSell per block breakage: " + Boolean.toString( afAutoSellPerBlock ) );
    			if ( !afAutoSellPerBlock ) {
    				
    				msgs.add( "        To enable set: 'options.inventory.isAutoSellPerBlockBreakEnabled: true' to a value of true."   );
    			}
    			msgs.add( "      Per block perms enabled: " + afAutoSellPerBlockPerm + "  (" + 
    							(afAutoSellPerBlockPermIsEnabled ? "is enabled" : "is disabled") +
    						")");
    			msgs.add( "      Perm: 'options.inventory.permissionAutoSellPerBlockBreakEnabled: '" + afAutoSellPerBlockPerm + "'" );
    			msgs.add( "           To disable perms, use either 'disable' or 'false' instead of an actual perm." );
    				
    			msgs.add( "  " );
    			
    			
    			msgs.add( "    MineBombs: Since sellall is enabled, the minebomb's autosell feature can be use." );
    			msgs.add( "               MineBomb's autosell does not require autosell to be enabled since it can " );
    			msgs.add( "               force an autosell. Autosell is useful for when the mine bomb's drops would " );
    			msgs.add( "               be far too large for a player's inventory to handle, and may even cause server " );
    			msgs.add( "               lag." );
    			msgs.add( "  " );
    			
    			
    			if ( afAutoSellPerBlock && afAutoSellIfInventoryIsFull ) {
    				
    				msgs.add( "Autosell is enabled in autoFeatures for both per block and full inventory. " );
    				msgs.add( "Since per block autosell is enabled, Prison will never use the full inventory autosell " );
    				msgs.add( "through autoFeatures. " );
    				msgs.add( "Per block autosell will sell the items that were just mined, and will not place those items" );
    				msgs.add( "in the player's inventory prior to selling them. Per block autosell will only sell what is " );
    				msgs.add( "being mined, and will not sell anything that is already in the player's inventory." );
    				msgs.add( "  " );
    			}
    			else if ( afAutoSellPerBlock ) {
    				
    				msgs.add( "Autosell is enabled in autoFeatures for just per block autosell.  You do not need to " );
    				msgs.add( "enable full inventory autosell since it will never be used anyway; per block autosell  " );
    				msgs.add( "overrides the full inventory autosell within the autoFeatures.  " );
    				msgs.add( "Per block autosell will sell the items that were just mined, and will not place those items" );
    				msgs.add( "in the player's inventory prior to selling them. Per block autosell will only sell what is " );
    				msgs.add( "being mined, and will not sell anything that is already in the player's inventory." );
    				msgs.add( "  " );
    			}
    			else if ( afAutoSellIfInventoryIsFull ) {
    				
    				msgs.add( "Autosell is enabled in autoFeatures only when handling block break events, and a player's  " );
    				msgs.add( "inventory becomes full. Other actions outside of handling block break events will not trigger " );
    				msgs.add( "an autosell event even though the player's inventory may fill up." );
    				msgs.add( "  " );
    			}
    			else {
    				
    				msgs.add( "Autosell has not been enabled in autoFeatures. " );
    				msgs.add( "Please see the above settings to enable it. " );
    				msgs.add( "  " );
    			}

    			msgs.add( "  " );
    			msgs.add( "Additonal auto sell capabilities that can be enabled within Prison's auto features:  " );
    			msgs.add( "  " );

    			msgs.add( "The player can turn off autosell by using the `/sellall autoSellToggle` command, if that " );
    			msgs.add( "been enabled.  See settings above. " );
    			
    			msgs.add( "  " );
    			msgs.add( "&cPlease note that if autosell is working for other players, but some are complaining " );
    			msgs.add( "&cthat it is not working for them, then please have them check their toggle status:  " );
    			msgs.add( "&c`&2/sellall autoSellToggle&c`  " );
    			msgs.add( "  " );
    			
    			
    			msgs.add( "  " );
    			msgs.add( "Note: The following property is not enabled: 'options.inventory.isAutoSellIfInventoryIsFullForBLOCKEVENTSPriority: '" );
    			msgs.add( "      Since it is not enabled, examples on how to use it is moot and will not be provided." );
    			msgs.add( "  " );
    			msgs.add( "Prison is very flexible because the configuration of many servers can be very complex and involve " );
    			msgs.add( "the use of many different plugins, somme of which may conflict with Prison, or it is preferred by " );
    			msgs.add( "admins to use another plugin to handle Prison's functions, such as handling the block breakage. " );
    			msgs.add( "Therefore, acknowledging the complexity of the plugin mix, Prison has a couple of autosell " );
    			msgs.add( "features that can help handle some edge cases.  These features generally are not used, but they " );
    			msgs.add( "can solve some complex issues.  Thease features are listed below..." );
    			msgs.add( "" );
    			msgs.add( "  Check player's inventory for full inventory autosell: " +
    							(afAutoSellForceCheck ? "is enabled" : "is disabled" )
    							);
    			msgs.add( "    Setting: 'options.inventory.isForceSellAllOnInventoryWhenBukkitBlockBreakEventFires: " + 
    										Boolean.toString(afAutoSellForceCheck)+ " '" );
    			msgs.add( "    Prison's autoFeature's autosell does not inspect the player's inventory since it is selling " );
    			msgs.add( "    the blocks that are being mined. Bypassing the player's inventory save a lot of processing " );
    			msgs.add( "    overhead that could contribute to lag. Therefore this option, which only is tied to " );
    			msgs.add( "    the 'org.bukkit.BlockBreakEvent' handling, can check the player's inventory to see if it is " );
    			msgs.add( "    full so a sellall can be fired. This is useful if you have to use another plugins block " );
    			msgs.add( "    handling but you're using prison's sellall. This could be used in conjunction with a " );
    			msgs.add( "    event priority of MONITOR, BLOCKEVENTS, ACESS, ACCESSBLOCKEVENTS, and ACCESSMONITOR." );
    			msgs.add( "    NOTE: This can be applied without enabling autosell directly." );
    			msgs.add( "    " );
    			msgs.add( "  Delayed player inventory autosell: " +
								(afAutoSellForceDelayedCheck ? "is enabled" : "is disabled" )
								);
    			msgs.add( "    Delayed for: " + Long.toString( afAutoSellForceDelayedCheckTicks) + " ticks." );
    			msgs.add( "    Setting: 'options.inventory.isEnabledDelayedSellAllOnInventoryWhenBukkitBlockBreakEventFires: " + 
    										Boolean.toString( afAutoSellForceDelayedCheck ) + " '" );
    			msgs.add( "    Setting: 'options.inventory.isEnabledDelayedSellAllOnInventoryDelayInTicks: " + 
    										Long.toString( afAutoSellForceDelayedCheckTicks ) + " '" );
    			msgs.add( "    This option is similar to the check player's inventory for full inventory with autosell, except " );
    			msgs.add( "    that each time this task runs, it will sell everything in the player's inventory that is sellable." );
    			msgs.add( "    This process is the same as if the player used `/sellall sell` but on a delay." );
    			msgs.add( "    " );
    			
    			msgs.add( "    How this setting is different, is that it will submit a task to run in 'n' ticks to check " );
    			msgs.add( "    the player's inventory and then performm an autosell if needed. " );
    			msgs.add( "    This is very useful for when another plugin is handling the block breaks, and the " );
    			msgs.add( "    blocks are not yet placed in the player's inventory by the time prison is handling the " );
    			msgs.add( "    MONITOR (or other) priorities." );
    			msgs.add( "    Valid tick values are 0 and higher, with 2 ticks being the default.  Setting this value to " );
    			msgs.add( "    one second (20 ticks) or higher is reasonable, and can reduce server load, but the player will " );
    			msgs.add( "    see a slight delay from when their inventoy becommes full and when it is sold. " );
    			msgs.add( "    This submits a player task to check their inventory in the future, as defined by " );
    			msgs.add( "    the number of ticks. While this submitted task is waiting to run, or is running, " );
    			msgs.add( "    addtional mining by the player will not submit more of these tasks. One task per player " );
    			msgs.add( "    can be submitted at time, since the task will handle all prior mining activiies." );
    			msgs.add( "    " );
    			msgs.add( "    " );
    			
    			
    			msgs.add( "  Force debug logging on autosell overflow conditions: " +
									(afAutoSellForceDebugLoggin ? "is enabled" : "is disabled" ) );
    			msgs.add( "    Setting: 'options.inventory.isAutoSellLeftoversForceDebugLogging: " + 
									Boolean.toString( afAutoSellForceDebugLoggin ) + " '" );
    			msgs.add( "    It would be a VERY rare condition for blocks not autoselling, and could be a sign that the " );
    			msgs.add( "    block has not been setup in the Prison sellall shop." );
    			msgs.add( "    Therefore, if this situation is identified, where there are leftover blocks that have not been " );
    			msgs.add( "    auto sold, then this will trigger a block break debug logging even for that transaction so it's " );
    			msgs.add( "    added to the console. This will make it easier to track down configuration issues with sellall." );
    			msgs.add( "    The block break debuging informmation is being shown in full since it can help provide " );
    			msgs.add( "    more information which would help address other possible issues." );
    			msgs.add( "    This only works when prison debug mode is turned off, and it will only log just that one" );
    			msgs.add( "    transaction (it will not turn on prison debug mode)." );
    			msgs.add( "    This condition will be logged every time the issue happens, which can result in many logged messages." );
    			
    			msgs.add( "    " );
				
    		}
    		msgs.add( "  " );
    		
    		
    	}
    	
    	//msgs.add( "" );
    	
    	
    	sender.sendMessage(msgs);
    }
    


    @Command(identifier = "prison support test itemsAdder", 
    		description = "Initial test of accessing ItemsAdder.", 
    		onlyPlayers = false, permissions = "prison.admin" )
    public void testItemAdderCommand(CommandSender sender ) {
    	

    	PrisonItemsAdder pia = new PrisonItemsAdder();
    	
    	
    	Output.get().logInfo( "Prison Support: Starting to access ItemsAdder:" );
    	Output.get().logInfo( "  This is just a preliminary test just to identify if prison can access the "
    			+ "ItemsAddr list of custom blocks.  Once this can be verified, along with the format that "
    			+ "they are using, then Prison can be setup to utilize those items as custom blocks within "
    			+ "Prison.  Please copy and past these results to the discord server to the attention "
    			+ "of Blue." );
    	Output.get().logInfo( "  Will list all custom blocks: ItemsAdder.getAllItems() with only isBlock():" );
    	
    	pia.integrate();
    	

    	if ( pia.hasIntegrated() ) {
    		
    		pia.testCustomBlockRegistry();
    	}
    	else {
    		Output.get().logInfo( "Warning: Prison has not been able to establish a connection to "
    				+ "ItemsAdder.  Make sure it has been installed and is loading successfully." );
    	}
    	
    	
    	Output.get().logInfo( "Prison Support: Compleated tests with access to ItemsAdder:" );
    }
}
