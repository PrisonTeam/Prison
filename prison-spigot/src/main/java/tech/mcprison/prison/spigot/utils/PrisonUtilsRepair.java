package tech.mcprison.prison.spigot.utils;

import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.commands.Wildcard;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.spigot.compat.Compatibility;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.inventory.SpigotPlayerInventory;

public class PrisonUtilsRepair
	extends PrisonUtils
{

	private boolean enableRepairAll = false;
	private boolean enableRepairHand = false;

	
	public enum RepairOptions {
		
		repairInHand,
		repairAll,
		
		repairArmour,
		repairEnchanted
		;
		
		public static RepairOptions fromString( String option ) {
			RepairOptions results = null;
			
			for ( RepairOptions rOp : values() )
			{
				if ( rOp.name().equalsIgnoreCase( option ) ) {
					results = rOp;
					break;
				}
			}
			
			return results;
		}
		
	}
	
	public PrisonUtilsRepair() {
		super();
		
		
	}
	

	/**
	 * <p>There is no initialization needed for these commands.
	 * <p>
	 * 
	 * <p>This function must return a value of true to indicate that this 
	 * set of commands are enabled.  If it is set to false, then these
	 * commands will not be registered when prison is loaded.
	 * </p>
	 * 
	 * @return
	 */
	@Override
	protected Boolean initialize()
	{
		return true;
	}
	
	
	@Command(identifier = "prison utils repairAll", 
				description = "Repair all items in the player's inventory.",
			onlyPlayers = false, 
			permissions = "prison.utils.repair.all", 
			altPermissions = "prison.utils.repair.all.others")
	public void utilRepairAll(CommandSender sender, 
			@Arg(name = "playerName", description = "Player name") String playerName,
			
			@Wildcard(join=true)
			@Arg(name = "options", description = "Options [repairArmour, repairEnchanted]", 
					def = "") String options ) {
		
		if ( !isEnableRepairAll() ) {
			
			Output.get().logInfo( "Prison's utils command repairAll is disabled in modules.yml." );
		}
		else {

			List<RepairOptions> repairOptions = getOptions( RepairOptions.repairAll, options, playerName );
			
			
			SpigotPlayer player = checkPlayerPerms( sender, playerName, 
					"prison.utils.repair.all", 
					"prison.utils.repair.all.others" );

			// Player cannot be null.  If it is null, then there was a failure.
			if ( player != null ) {
				
				utilRepair( player, playerName, repairOptions );
			}
		}
	}
	
	
	
	
	@Command(identifier = "prison utils repair", 
				description = "Repair the item in the player's hand.",
			onlyPlayers = false, 
			permissions = "prison.utils.repair.inhand", 
			altPermissions = "prison.utils.repair.inhand.others")
	public void utilRepairInHand(CommandSender sender, 
			@Arg(name = "playerName", description = "Player name") String playerName,
			
			@Wildcard(join=true)
			@Arg(name = "options", description = "Options [repairArmour, repairEnchanted]", 
					def = "") String options ) {
		
		
		if ( !isEnableRepairHand() ) {
			
			Output.get().logInfo( "Prison's utils command repair (repairInHand) is disabled in modules.yml." );
		}
		else {

			List<RepairOptions> repairOptions = getOptions( RepairOptions.repairInHand, options, playerName );
			
			
			SpigotPlayer player = checkPlayerPerms( sender, playerName, 
					"prison.utils.repair.inhand", 
					"prison.utils.repair.inhand.others" );

			// Player cannot be null.  If it is null, then there was a failure.
			if ( player != null ) {
				
				utilRepair( player, playerName, repairOptions );
			}
			else {
				
				Output.get().logInfo( "Prison's utils command repair: Player cannot be loaded." );
			}
		}
	}
	

	private void utilRepair( SpigotPlayer player, String playerName, List<RepairOptions> repairOptions ) {
		
    	final List<String> repaired = new ArrayList<>();
    	
    	if ( player != null && player.getWrapper() != null && player.getWrapper().getInventory() != null ) {
    		
    		SpigotPlayerInventory inventory = new SpigotPlayerInventory( player.getWrapper().getInventory() );
    		
    		if ( repairOptions.contains( RepairOptions.repairAll ) ) {
    			
    			repairItems( inventory.getItems(), player, repaired, repairOptions);
    		}
    		else if ( repairOptions.contains( RepairOptions.repairInHand ) ) {
    			List<ItemStack> itemStacks = new ArrayList<>();
    			if ( inventory.getItemInLeftHand() != null ) {
    				itemStacks.add( inventory.getItemInLeftHand() );
    			}
    			if ( inventory.getItemInRightHand() != null ) {
    				itemStacks.add( inventory.getItemInRightHand() );
    			}
    			
    			if ( itemStacks.size() > 0 ) {
    				repairItems( itemStacks, player, repaired, repairOptions);
    			}
    		}
    		
    		if ( repairOptions.contains( RepairOptions.repairArmour ) ) {
    			
    			repairItems( inventory.getArmorContents(), player, repaired, repairOptions);
    		}
    		
    	}
    	 
        if (repaired.isEmpty()) {
        	 if ( player.getWrapper() != null ) {
        		 player.sendMessage( String.format( "&3Nothing was repaired.") );
        	 }
        	 else {
        		 Output.get().logInfo( "&3Nothing was repaired." );
        	 }
        	 
        } 
        else {

        	 // At least one item was repaired so update the player's inventory:
        	 player.updateInventory();
        	 
    		 player.sendMessage( 
    				 String.format( "&3Repaired &7%d &3items: %s", 
    				 repaired.size(), String.join(", ", repaired)) );
        		 
         }
		
	}
	
	/**
	 * <p>This function parses the options parameter to return a listing of options to 
	 * apply to the repair. It adds the primaryOption that is provided to the List of 
	 * returned options.  And it also strips out repairInHand and repairAll so they cannot
	 * be passed as parameters to prevent users from bypassing the controls.
	 * </p>
	 *
	 * @param primaryOption The primary option for the command: repairInHand or repairAll.  
	 * 						Will be added to the returned options.
	 * @param options The String that will be parsed to extract options from.
	 * 
	 */
	private List<RepairOptions> getOptions( RepairOptions primaryOption, String options, String playerName ) {
		
		// If playerName is not null or blank, then add to options since it "may" be an option if
		// playerName was not specified.  Do not have to verify if it's a valid player's name since
		// the parsing to RepairOptions will drop all non valid RepairOptions.
		if ( playerName != null && !playerName.trim().isEmpty() ) {
			options += " " + playerName;
		}
		
    	List<RepairOptions> repairOptions = new ArrayList<>();
    	for ( String rOpt : options.split( " " ) ) {
    		RepairOptions repairOption = RepairOptions.fromString( rOpt );
    		if ( repairOption != null ) {
    			repairOptions.add( repairOption );
    		}
    	}
    	
    	repairOptions.remove( RepairOptions.repairInHand );
    	repairOptions.remove( RepairOptions.repairAll );
    	
    	if ( primaryOption != null ) {
    		repairOptions.add( 0, primaryOption );
    	}
    	
    	return repairOptions;
	}
	
	/**
	 * <P>Will perform the repair.  Skips the attempt if it's a block, 
	 * does not have a durability, or it's durability is zero (nothing to repair).
	 * </p>
	 * 
	 * @param item
	 * @throws Exception
	 */
    private boolean repairItem( SpigotItemStack item, 
    		List<String> repaired, List<RepairOptions> repairOptions ) 
    		{
    	boolean results = false;
    	
    	Compatibility compat = SpigotPrison.getInstance().getCompatibility();
    	
    	if ( item != null && !item.isBlock() && !item.isAir() ) {
    		
    		if ( compat.getDurabilityMax( item ) > 0 &&   // has durability if > 0
    				compat.getDurability( item ) > 0 &&   // has wear if > 0
    				( item.getEnchantments().isEmpty() || 
    						!item.getEnchantments().isEmpty() && 
    						!repairOptions.contains( RepairOptions.repairEnchanted )
    						) ) {
    			
    			results = compat.setDurability( item, 0 );
    			if ( results ) {
    				final String itemName = item.getName();
    				repaired.add(itemName.replace('_', ' '));
    			}
    		}
    	}
    	
        
        return results;
    }
    

    private void repairItems( List<ItemStack> itemStacks, SpigotPlayer player, 
    					List<String> repaired, List<RepairOptions> repairOptions ) {
    	
        for (final ItemStack is : itemStacks) {
        	SpigotItemStack item = (SpigotItemStack) is;
        	
        	repairItem( item, repaired, repairOptions );
        }
    }
    private void repairItems( ItemStack[] itemStacks, SpigotPlayer player, 
    		List<String> repaired, List<RepairOptions> repairOptions ) {
    	
    	for (final ItemStack is : itemStacks) {
    		SpigotItemStack item = (SpigotItemStack) is;
    		
    		repairItem( item, repaired, repairOptions );
    	}
    }

	public boolean isEnableRepairAll() {
		return enableRepairAll;
	}
	public void setEnableRepairAll( boolean enableRepairAll ) {
		this.enableRepairAll = enableRepairAll;
	}

	public boolean isEnableRepairHand() {
		return enableRepairHand;
	}
	public void setEnableRepairHand( boolean enableRepairHand ) {
		this.enableRepairHand = enableRepairHand;
	}

}
