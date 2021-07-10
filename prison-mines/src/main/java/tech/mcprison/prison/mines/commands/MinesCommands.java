/*
 * Prison is a Minecraft plugin for the prison game mode.
 * Copyright (C) 2017-2020 The Prison Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.mines.commands;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.chat.FancyMessage;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.commands.CommandPagedData;
import tech.mcprison.prison.commands.Wildcard;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.block.Block;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.data.MineData;
import tech.mcprison.prison.mines.data.MineData.MineNotificationMode;
import tech.mcprison.prison.mines.data.MineScheduler.MineResetActions;
import tech.mcprison.prison.mines.data.MineScheduler.MineResetType;
import tech.mcprison.prison.mines.data.PrisonSortableResults;
import tech.mcprison.prison.mines.features.MineBlockEvent;
import tech.mcprison.prison.mines.features.MineBlockEvent.BlockEventType;
import tech.mcprison.prison.mines.features.MineLinerBuilder;
import tech.mcprison.prison.mines.features.MineLinerBuilder.LinerPatterns;
import tech.mcprison.prison.mines.managers.MineManager;
import tech.mcprison.prison.mines.managers.MineManager.MineSortOrder;
import tech.mcprison.prison.mines.tasks.MineTeleportWarmUpTask;
import tech.mcprison.prison.modules.ModuleElementType;
import tech.mcprison.prison.output.BulletedListComponent;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.FancyMessageComponent;
import tech.mcprison.prison.output.LogLevel;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.output.RowComponent;
import tech.mcprison.prison.placeholders.PlaceholdersUtil;
import tech.mcprison.prison.selection.Selection;
import tech.mcprison.prison.tasks.PrisonCommandTask.TaskMode;
import tech.mcprison.prison.tasks.PrisonCommandTask;
import tech.mcprison.prison.tasks.PrisonTaskSubmitter;
import tech.mcprison.prison.util.Bounds;
import tech.mcprison.prison.util.Bounds.Edges;
import tech.mcprison.prison.util.Text;

/**
 * @author Dylan M. Perks
 */
public class MinesCommands
	extends MinesBlockCommands {
	
	public MinesCommands() {
		super( "MinesCommands" );
	}
    
    @Command(identifier = "mines command", 
    		onlyPlayers = false, permissions = "prison.commands")
    public void mineCommandSubcommands(CommandSender sender) {
    	sender.dispatchCommand( "mines command help" );
    }
    
    @Command(identifier = "mines set", 
    		onlyPlayers = false, permissions = "prison.commands")
    public void minesSetSubcommands(CommandSender sender) {
    	sender.dispatchCommand( "mines set help" );
    }
    
    
    /*
     * NOTE: Block commands from the class MinesBlockCommands need to have the "command" references here
     * so the prison command handler can pick them up properly.
     * 
     * 
     */
    
    @Command(identifier = "mines block", 
    		onlyPlayers = false, permissions = "prison.commands")
    public void mineBlockSubcommands(CommandSender sender) {
    	sender.dispatchCommand( "mines block help" );
    }
    
    @Command(identifier = "mines blockEvent", 
    		onlyPlayers = false, permissions = "prison.commands")
    public void mineBlockEventSubcommands(CommandSender sender) {
    	sender.dispatchCommand( "mines blockEvent help" );
    }
	
    @Override
    @Command(identifier = "mines block add", permissions = "mines.block", onlyPlayers = false, 
			description = "Adds a block to a mine.")
	public void addBlockCommand(CommandSender sender,
			@Arg(name = "mineName", description = "The name of the mine to add the block to.")
					String mineName, 
			@Arg(name = "block", description = "The block's name or ID.") 
					String block,
			@Arg(name = "chance", description = "The percent chance (out of 100) that this block will occur.")
					double chance) {
    	
    	super.addBlockCommand( sender, mineName, block, chance );
    }
    
    
    @Override
    @Command(identifier = "mines block set", permissions = "mines.block", onlyPlayers = false, 
			description = "Changes the percentage of a block in a mine.")
	public void setBlockCommand(CommandSender sender,
			@Arg(name = "mineName", description = "The name of the mine to edit.") 
					String mineName,
			@Arg(name = "block", description = "The block's name or ID.") 
					String block,
			@Arg(name = "chance", description = "The percent chance (out of 100) that this block will occur.") 
					double chance) {
	    	
    	super.setBlockCommand( sender, mineName, block, chance );
	}  
    
    @Override
    @Command(identifier = "mines block remove", permissions = "mines.block", 
    			onlyPlayers = false, description = "Deletes a block from a mine.")
    public void delBlockCommand(CommandSender sender,
	        @Arg(name = "mineName", description = "The name of the mine to edit.") String mineName,
	        @Arg(name = "block", def = "AIR", description = "The block's name") String block) {

    	super.delBlockCommand( sender, mineName, block );
    }
    
    @Command(identifier = "mines block search", permissions = "mines.block", 
					description = "Searches for a block to add to a mine.")
	public void searchBlockCommand(CommandSender sender,
			@Arg(name = "search", def = " ", description = "Any part of the block's name or ID.") String search,
			@Arg(name = "page", def = "1", description = "Page of search results (optional)") String page ) {
		
    	super.searchBlockCommand( sender, search, page, 
    			"mines block search", 
    			"mines block add",
    			"mine" );
    }
    
    @Command(identifier = "mines block searchAll", permissions = "mines.block", 
    		description = "Searches for a blocks and items. Items cannot be added to mines.")
    public void searchBlockAllCommand(CommandSender sender,
    		@Arg(name = "search", def = " ", description = "Any part of the block's, or item's name.") String search,
    		@Arg(name = "page", def = "1", description = "Page of search results (optional)") String page ) {
    	
    	super.searchBlockAllCommand( sender, search, page,
    			"mines block searchAll", 
    			"mines block add",
    			"mine" );
    }
    
    @Override
    @Command(identifier = "mines block list", permissions = "mines.block", 
    				description = "Searches for a block to add to a mine.")
    public void listBlockCommand(CommandSender sender,
    		@Arg(name = "mineName", description = "The name of the mine to view.") String mineName ) {

    	super.listBlockCommand( sender, mineName );
    }
    
    @Override
    
    @Command(identifier = "mines block constraint", permissions = "mines.block", 
							description = "Optionally enable constraints on a mine's block generation.")
	public void constraintsBlockCommand(CommandSender sender,
			@Arg(name = "mineName", description = "The name of the mine to view.") String mineName,
			@Arg(name = "blockNme", description = "The block's name") String blockName,
			@Arg(name = "contraint", description = "Constraint to apply " +
						"[min max excludeTop excludeBottom]",
					def = "max") String constraint,
			@Arg(name = "value", description = "The value to assign to this constraint. " +
					"A value of 0 will remove the constraint.") int value ) {

    	super.constraintsBlockCommand( sender, mineName, blockName, constraint, value );
    }
    

    
    
    
    @Command(identifier = "mines create", description = "Creates a new mine, or even a virtual mine.", 
    		onlyPlayers = false, permissions = "mines.create")
    public void createCommand(CommandSender sender,
    		@Arg(name = "virtual", description = "Create a virtual mine in name only; no physical location. " +
    				"This allows the mine to be predefined before specifying the coordinates. Use [virtual]. ", 
    				def = "") 
    					String virtualMine,
    		@Wildcard(join=true)
    		@Arg(name = "mineName", description = "The name of the new mine.", def = " ") String mineName
    		) {
    	boolean virtual = false;
    	
    	if ( virtualMine != null && virtualMine.trim().length() > 0 ) {
    		if ( "virtual".equalsIgnoreCase( virtualMine.trim()) ) {
    			virtual = true;
    		}
    		else {
    			// Combine virtualMine to the beginning of the mineName if it exists.  It was not
    			// intended to be the virtualMine parameter. Yes, adding a space will be an error, but
    			// they added it any way.
    			mineName = virtualMine + (mineName == null ? "" : " " + mineName.trim() ).trim();
    		}
    	}

        if ( mineName == null || mineName.contains( " " ) || mineName.trim().length() == 0 ) {
        	sendMessage( sender, "&3Names cannot contain spaces or be empty. &b[&d" + mineName + "&b]" );
    		return;
        }
        mineName = mineName.trim();

    	Player player = getPlayer( sender );
    	
    	if ( !virtual && (player == null || !player.isOnline())) {
    		sendMessage( sender, "&3You must be a player in the game to run this command." );
    		return;
    	}

    	PrisonMines pMines = PrisonMines.getInstance();
    	
    	if (PrisonMines.getInstance().getMine(mineName) != null) {
    		pMines.getMinesMessages().getLocalizable("mine_exists")
    										.sendTo(sender, LogLevel.ERROR);
    		return;
    	}

    	Selection selection = null;

    	// virtual mine will skip the setting of the boundaries, but it will make
    	// the mine unusable.
    	if ( !virtual ) {
    	
    		selection = Prison.get().getSelectionManager().getSelection(player);
    		if (!selection.isComplete()) {
    			pMines.getMinesMessages().getLocalizable("select_bounds")
    						.sendTo(sender, LogLevel.ERROR);
    			return;
    		}
    		
    		if (!selection.getMin().getWorld().getName()
    				.equalsIgnoreCase(selection.getMax().getWorld().getName())) {
    			pMines.getMinesMessages().getLocalizable("world_diff")
    						.sendTo(sender, LogLevel.ERROR);
    			return;
    		}
    	}
    	

        setLastMineReferenced(mineName);
        
        Mine mine = new Mine(mineName, selection);
        pMines.getMineManager().add(mine);
        
        if ( mine.isVirtual() ) {
        	sendMessage( sender, "&3Virtual mine created: use command " +
        			"&7/mines set area &3 to enable as a normal mine." );
        }
        else {
        	pMines.getMinesMessages().getLocalizable("mine_created").sendTo(sender);
        }
        
        if ( !virtual && sender != null && sender instanceof Player ) {
        	// Delete the selection:
        	Prison.get().getSelectionManager().clearSelection((Player) sender);
        }
    }
    
    private void sendMessage( CommandSender sender, String message ) {
    	if ( sender == null ) {
    		Output.get().logInfo( message );
    	}
    	else {
    		sender.sendMessage( message );
    	}
    }
    
    @Command(identifier = "mines rename", description = "Rename a mine.", 
    		onlyPlayers = false, permissions = "mines.rename")
    public void renameCommand(CommandSender sender,
    			@Arg(name = "mineName", description = "The existing name of the mine.", def = " ") String mineName,
    		@Wildcard(join=true)
    			@Arg(name = "newName", description = "The new name for the mine.", def = " ") String newName
    		) {
    	
        if (!performCheckMineExists(sender, mineName)) {
            return;
        }
        
    	if ( newName == null || newName.contains( " " ) || newName.trim().length() == 0 ) {
    		sender.sendMessage( "&3New mine name cannot contain spaces or be empty. &b[&d" + newName + "&b]" );
    		return;
    	}
    	newName = newName.trim();
    	
    	PrisonMines pMines = PrisonMines.getInstance();
    	
    	if ( pMines.getMine(newName) != null ) {
    		sender.sendMessage( "&3Invalid new mine name. Another mine has that name. &b[&d" + newName + "&b]" );
    		return;
    		
    	}
    	
    	Mine mine = pMines.getMine(mineName);

    	setLastMineReferenced(newName);
    	

    	pMines.getMineManager().rename(mine, newName);
    	
    	
    	sender.sendMessage( String.format( "&3Mine &d%s &3was successfully renamed to &d%s&3.", mineName, newName) );
    	
    	pMines.getMinesMessages().getLocalizable("mine_created").sendTo(sender);
    	
    }
    
    
    @Command(identifier = "mines set mineAccessByRank", 
    		description = "Enables player access to the mine based upon their Rank. This is the " +
    				"preferred way to grant access to a mine, but the mine must be linked to " +
    				"a Rank: See /mines set rank help. " +
    				"This feature bypasses the need to use permissions or WorldGuard regions when " +
    				"event priorities are configured correctly.", 
    		onlyPlayers = true, permissions = "mines.set")
    public void mineAccessByRankCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine") String mineName, 
        @Arg(name = "enable", description = "Enable the mineAccessByRank: [enable, disable]") String enable ) {

        if (!performCheckMineExists(sender, mineName)) {
            return;
        }

        PrisonMines pMines = PrisonMines.getInstance();
        Mine mine = pMines.getMine(mineName);

        if ( mine.getRank() == null ) {
        
        	sender.sendMessage( "&cThis mine must be linked to a Rank before you can enable this feature." );
        	return;
        }
        
        if ( enable == null || !"enable".equalsIgnoreCase( enable ) && !"disable".equalsIgnoreCase( enable ) ) {
        	
        	sender.sendMessage( "&cInvalid option. &7The parameter &3enable &7can only have the values of " +
        			"'enable' or 'disable'.  Please try again." );
        	return;
        }
        
        boolean accessEnabled = "enable".equalsIgnoreCase( enable );
        
        if ( mine.isMineAccessByRank() == accessEnabled ) {
        	sender.sendMessage( 
        			String.format( "&cInvalid setting. &7The mine's setting mineAccessByRank has not been " +
        			"changed.  The mine already is set to the value of: [%s]", enable ) );
        	return;
        }

        
        setLastMineReferenced(mineName);
        
        mine.setMineAccessByRank( accessEnabled );
        
        pMines.getMineManager().saveMine(mine);

        sender.sendMessage( 
        		String.format( "&7The mine's setting mineAccessByRank has been changed to: [%s]", enable ) );
        
    }

    
    @Command(identifier = "mines set tpAccessByRank", 
    		description = "Enables player teleport access to the mine based upon their Rank. This is the " +
    				"preferred way to grant access to a mine's spawn point, but the mine must be linked to " +
    				"a Rank: See /mines set rank help.", 
    		onlyPlayers = true, permissions = "mines.set")
    public void tpAccessByRankCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine") String mineName, 
        @Arg(name = "enable", description = "Enable the tpAccessByRank: [enable, disable]") String enable ) {

        if (!performCheckMineExists(sender, mineName)) {
            return;
        }

        PrisonMines pMines = PrisonMines.getInstance();
        Mine mine = pMines.getMine(mineName);

        if ( mine.getRank() == null ) {
        
        	sender.sendMessage( "&cThis mine must be linked to a Rank before you can enable this feature." );
        	return;
        }
        
        if ( enable == null || !"enable".equalsIgnoreCase( enable ) && !"disable".equalsIgnoreCase( enable ) ) {
        	
        	sender.sendMessage( "&cInvalid option. &7The parameter &3enable &7can only have the values of " +
        			"'enable' or 'disable'.  Please try again." );
        	return;
        }
        
        boolean accessEnabled = "enable".equalsIgnoreCase( enable );
        
        if ( mine.isTpAccessByRank() == accessEnabled ) {
        	sender.sendMessage( 
        			String.format( "&cInvalid setting. &7The mine's setting tpAccessByRank has not been " +
        			"changed.  The mine already is set to the value of: [%s]", enable ) );
        	return;
        }

        
        setLastMineReferenced(mineName);
        
        mine.setTpAccessByRank( accessEnabled );
        
        pMines.getMineManager().saveMine(mine);

        sender.sendMessage( 
        		String.format( "&7The mine's setting tpAccessByRank has been changed to: [%s]", enable ) );
        
    }


    @Command(identifier = "mines set spawn", description = "Set the mine's spawn to where you're standing.", 
    		onlyPlayers = true, permissions = "mines.set")
    public void spawnpointCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to edit.") String mineName) {

    	Player player = getPlayer( sender );
    	
    	if (player == null || !player.isOnline()) {
    		sender.sendMessage( "&3You must be a player in the game to run this command." );
    		return;
    	}
    	
        if (!performCheckMineExists(sender, mineName)) {
            return;
        }

        PrisonMines pMines = PrisonMines.getInstance();
        Mine mine = pMines.getMine(mineName);

        
        if ( mine.isVirtual() ) {
        	sender.sendMessage( "&cMine is a virtual mine&7. Use &a/mines set area &7to enable the mine." );
        	return;
        }
        
        if ( !mine.isEnabled() ) {
        	sender.sendMessage( "&cMine is disabled&7. Use &a/mines info &7for possible cause." );
        	return;
        }
        
        if (!mine.getWorld().isPresent()) {
            pMines.getMinesMessages().getLocalizable("missing_world")
                .sendTo(sender);
            return;
        }

        if (!((Player) sender).getLocation().getWorld().getName()
            .equalsIgnoreCase(
                mine.getWorldName())) {
            pMines.getMinesMessages().getLocalizable("spawnpoint_same_world")
                .sendTo(sender);
            return;
        }

        setLastMineReferenced(mineName);
        
        mine.setSpawn(((Player) sender).getLocation());
        pMines.getMineManager().saveMine(mine);
        pMines.getMinesMessages().getLocalizable("spawn_set").sendTo(sender);
    }


    @Command(identifier = "mines set tag", description = "Sets the mine's tag name.", 
    		onlyPlayers = true, permissions = "mines.set")
    public void tagCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to edit.") String mineName, 
        @Wildcard(join=true)
    		@Arg(name = "tag", description = "Tag value for the mine. Use [null] to remove.") String tag ) {

        if (!performCheckMineExists(sender, mineName)) {
            return;
        }

        if ( tag == null || tag.trim().length() == 0 ) {
        	sender.sendMessage( "&cTag name must be a valid value. To remove use a value of &anull&c." );
        	return;
        }
        
        if ( tag.equalsIgnoreCase( "null" ) ) {
        	tag = null;
        }

        PrisonMines pMines = PrisonMines.getInstance();
        Mine mine = pMines.getMine(mineName);

        if ( tag == null && mine.getTag() == null || 
        		mine.getTag() != null &&
        		mine.getTag().equalsIgnoreCase( tag )) {
        
        	sender.sendMessage( "&cThe new tag name is the same as what it was. No change was made." );
        	return;
        }
        
        mine.setTag( tag );

        setLastMineReferenced(mineName);
        
        pMines.getMineManager().saveMine(mine);

        if ( tag == null ) {
        	sender.sendMessage( 
        			String.format( "&cThe tag name was cleared for the mine %s.", 
        					mine.getTag() ) );
        }
        else {
        	sender.sendMessage( 
        			String.format( "&cThe tag name was changed to %s for the mine %s.", 
        					tag, mine.getTag() ) );
        }
        
    }

    
    @Command(identifier = "mines set sortOrder", description = "Sets the mine's sort order, or " +
    		"prevents a mine from being included in most listings. If more than one mine has the " +
    		"same sort order, then they will be sorted alphabetically within that sub-group.", 
    		onlyPlayers = true, permissions = "mines.set")
    public void sortOrderCommand(CommandSender sender,
    		@Arg(name = "mineName", description = "The name of the mine to edit.") String mineName, 
    		@Arg(name = "sortOrder", description = "The sort order for listing mines. A value " +
    				"of -1 or [supress] will prevent the mine from beign included in most listings.",
    				def = "0" ) String sortOrder ) {
    	
    	int order = 0;
    	
    	if (!performCheckMineExists(sender, mineName)) {
    		return;
    	}
    	
    	if ( sortOrder == null ) {
    		sortOrder = "0";
    	}
    	else if ( "suppress".equalsIgnoreCase( sortOrder.trim() ) ) {
    		sortOrder = "-1";
    	}
    	
		try {
			order = Integer.parseInt( sortOrder );
		}
		catch ( NumberFormatException e ) {
			sender.sendMessage( "Invalid sortOrder.  Use an integer value of [0-n], or " +
					"[-1, supress] to prevent the mine from being included in most listings." );
			return;
		}
		
		if ( order < -1 ) {
			order = -1;
		}
    	
    	PrisonMines pMines = PrisonMines.getInstance();
    	Mine mine = pMines.getMine(mineName);
    	
    	if ( order == mine.getSortOrder()) {
    		sender.sendMessage( "&cThe new sort order is the same as what it was. No change was made." );
    		return;
    	}
    	
    	mine.setSortOrder( order );
    	
    	setLastMineReferenced(mineName);
    	
    	pMines.getMineManager().saveMine(mine);
    	
    	String suppressedMessage = order == -1 ? "This mine will be suppressed from most listings." : "";
    	sender.sendMessage( 
    			String.format( "&cThe sort order was changed to %s for the mine %s. %s", 
    					Integer.toString( mine.getSortOrder() ), mine.getTag(),
    					suppressedMessage ) );
    	
    }
    

    @Command(identifier = "mines delete", permissions = "mines.delete", onlyPlayers = false, description = "Deletes a mine.")
    public void deleteCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to delete.") String mineName,
    	@Arg(name = "confirm", def = "", description = "Confirm that the mine should be deleted") String confirm) {
        if (!performCheckMineExists(sender, mineName)) {
            return;
        }
        
        setLastMineReferenced(mineName);
        
        // They have 1 minute to confirm.
        long now = System.currentTimeMillis();
        if ( getConfirmTimestamp() != null && ((now - getConfirmTimestamp()) < 1000 * 60 ) && 
        		confirm != null && "confirm".equalsIgnoreCase( confirm ))  {
        	setConfirmTimestamp( null );
        	
        	PrisonMines pMines = PrisonMines.getInstance();
        	
        	Mine mine = pMines.getMine(mineName);
        	
        	// should be able to delete disabled and virtual mines:
//            if ( !mine.isEnabled() ) {
//            	sender.sendMessage( "&cMine is disabled&7. Use &a/mines info &7for possible cause." );
//            	return;
//            }
        	
        	// Remove from the manager:
        	pMines.getMineManager().removeMine(mine);
        	
        	// Terminate the running task for mine resets. Will allow it to be garbage collected.
        	mine.terminateJob();
        	
        	setLastMineReferenced(null);
        	
        	pMines.getMinesMessages().getLocalizable("mine_deleted").sendTo(sender);
        	
        } else if ( getConfirmTimestamp() == null || ((now - getConfirmTimestamp()) >= 1000 * 60 ) ) {
        	setConfirmTimestamp( now );

        	ChatDisplay chatDisplay = new ChatDisplay("&cDelete " + mineName);
        	BulletedListComponent.BulletedListBuilder builder = new BulletedListComponent.BulletedListBuilder();
        	builder.add( new FancyMessage(
                    "&3Confirm the deletion of this mine" )
                    .suggest("/mines delete " + mineName + " cancel"));

        	builder.add( new FancyMessage(
        			"&3Click &eHERE&3 to display the command" )
        			.suggest("/mines delete " + mineName + " cancel"));
        	
        	builder.add( new FancyMessage(
        			"&3Enter: &7/mines delete " + mineName + " confirm" )
        			.suggest("/mines delete " + mineName + " cancel"));
        	
        	builder.add( new FancyMessage(
        			"&3Then change &ecancel&3 to &econfirm&3." )
        			.suggest("/mines delete " + mineName + " cancel"));
        	
        	builder.add( new FancyMessage("You have 1 minute to respond."));
        	
            chatDisplay.addComponent(builder.build());
            chatDisplay.send(sender);
            
        } else if (confirm != null && "cancel".equalsIgnoreCase( confirm )) {
        	setConfirmTimestamp( null );
        	
        	ChatDisplay display = new ChatDisplay("&cDelete " + mineName);
            display.addText("&8Delete canceled.");

            display.send( sender );
            
        } else {
	    	ChatDisplay display = new ChatDisplay("&cDelete " + mineName);
	    	display.addText("&8Delete confirmation failed. Try again.");
	    	
	    	display.send( sender );
	    }
        
    }

    @Command(identifier = "mines info", permissions = "mines.info", onlyPlayers = false, 
    				description = "Lists information about a mine. Page value of 'ALL' will show all of the " +
    						"information on page 1 and 2 (block list with block constraints), plus it will show " +
    						"all BlockEvents, and mine reset commands. ")
    public void infoCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to view.") String mineName,
        @Arg(name = "page", def = "1", 
        				description = "Page of search results (optional) [1-n, ALL]") String page 
    		) {
        if (!performCheckMineExists(sender, mineName)) {
            return;
        }

        
        setLastMineReferenced(mineName);
        
        PrisonMines pMines = PrisonMines.getInstance();
    	MineManager mMan = pMines.getMineManager();
        Mine m = pMines.getMine(mineName);
        
        
        CommandPagedData cmdPageData = null;
        
        
        if ( m.isUseNewBlockModel() ) {
        
        	cmdPageData = new CommandPagedData(
        			"/mines info " + m.getName(), m.getPrisonBlocks().size(),
        			1, page );
        }
        else {
        	
        	cmdPageData = new CommandPagedData(
        			"/mines info " + m.getName(), m.getBlocks().size(),
        			1, page );
        }
        
//        // Same page logic as in mines block search:
//    	int curPage = 1;
//    	int pageSize = 10;
//    	int pages = (m.getBlocks().size() / pageSize) + 1;
//    	try
//		{
//			curPage = Integer.parseInt(page);
//		}
//		catch ( NumberFormatException e )
//		{
//			// Ignore: Not an integer, will use the default value.
//		}
//    	curPage = ( curPage < 1 ? 1 : (curPage > pages ? pages : curPage ));
//    	int pageStart = (curPage - 1) * pageSize;
//    	int pageEnd = ((pageStart + pageSize) > m.getBlocks().size() ? m.getBlocks().size() : pageStart + pageSize);

    	

        DecimalFormat dFmt = new DecimalFormat("#,##0");
        DecimalFormat fFmt = new DecimalFormat("#,##0.00");
        
        ChatDisplay chatDisplay = new ChatDisplay("&bMine: &3" + m.getName());

        chatDisplay.addText("&7Server runtime: %s", Prison.get().getServerRuntimeFormatted() );;


        // Display Mine Info only:
        if ( cmdPageData.getCurPage() == 1 ) {
        	
        	if ( m.isVirtual() ) {
        		chatDisplay.addText("&cWarning!! This mine is &lVirtual&r&c!! &7Use &3/mines set area &7to enable." );
        	}
        	
        	if ( !m.isEnabled() ) {
        		chatDisplay.addText("&cWarning!! This mine is &lDISABLED&r&c!!" );
        	}
        	
        	
        	if ( m.isMineAccessByRank() && m.isTpAccessByRank() ) {
        		RowComponent row = new RowComponent();
        		row.addTextComponent( "&3Mine Access by Rank.   TP Access by Rank." );
        		chatDisplay.addComponent( row );
        	}
        	else if ( !m.isMineAccessByRank() && m.isTpAccessByRank() ) {
        		RowComponent row = new RowComponent();
        		row.addTextComponent( "&3TP Access by Rank." );
        		chatDisplay.addComponent( row );
        	}
        	if ( m.isMineAccessByRank() && !m.isTpAccessByRank() ) {
        		RowComponent row = new RowComponent();
        		row.addTextComponent( "&3Mine Access by Rank." );
        		chatDisplay.addComponent( row );
        	}
        	
        	if ( !m.isMineAccessByRank() ) {
        		RowComponent row = new RowComponent();
        		row.addTextComponent( "&3Mine Access Permission: &7%s   &3(Should use Access by Rank)", 
        				( m.getAccessPermission() == null ? "&2none" : m.getAccessPermission() ) );
        		chatDisplay.addComponent( row );
        	}
        	
        	
        	String noTagMessag = String.format( "&7(not set) &3Will default to mine name if used." );
        	chatDisplay.addText("&3Tag: &7%s", m.getTag() == null ? noTagMessag : m.getTag());
        	
        	if ( !m.isVirtual() ) {
        		String worldName = m.getWorld().isPresent() ? m.getWorld().get().getName() : "&cmissing";
        		chatDisplay.addText("&3World: &7%s", worldName);
        	}
        	
        	
        	if ( m.getRank() == null ) {
        		chatDisplay.addText( "&3No rank is linked to this mine." );
        	}
        	else {
        		chatDisplay.addText( "&3Rank: &7%s", m.getRank() );
        	}
        	
        	
        	if ( !m.isVirtual() ) {
        		String minCoords = m.getBounds().getMin().toBlockCoordinates();
        		String maxCoords = m.getBounds().getMax().toBlockCoordinates();
        		chatDisplay.addText("&3Bounds: &7%s &8to &7%s", minCoords, maxCoords);
        		Player player = getPlayer( sender );
        		
        		chatDisplay.addText("&3Center: &7%s   &3%s &7%s", 
        				m.getBounds().getCenter().toBlockCoordinates(), 
        				(player == null ? "" : "Distance:"),
        				(player == null ? "" : fFmt.format( m.getBounds().getDistance3d( player.getLocation() ) ))
        				);
        		
        		
        		String spawnPoint = m.getSpawn() != null ? m.getSpawn().toBlockCoordinates() : "&cnot set";
        		chatDisplay.addText("&3Spawnpoint: &7%s", spawnPoint);
        		
        		if ( cmdPageData.isShowAll() || mMan.isMineStats() ) {
        			RowComponent rowStats = new RowComponent();
        			rowStats.addTextComponent( "  -- &7 Stats :: " );
        			rowStats.addTextComponent( m.statsMessage() );
        			
        			chatDisplay.addComponent(rowStats);
        		}
        	}
        	

        	
//          chatDisplay.text("&3Size: &7%d&8x&7%d&8x&7%d", Math.round(m.getBounds().getWidth()),
//              Math.round(m.getBounds().getHeight()), Math.round(m.getBounds().getLength()));
          	
          	if ( !m.isVirtual() ) {
          		RowComponent row = new RowComponent();
          		row.addTextComponent( "&3Size: &7%d&8x&7%d&8x&7%d", Math.round(m.getBounds().getWidth()),
          				Math.round(m.getBounds().getHeight()), Math.round(m.getBounds().getLength()) );
          		
          		row.addTextComponent( "    &3Volume: &7%s &3Blocks", 
          				dFmt.format( Math.round(m.getBounds().getTotalBlockCount())) );
          		chatDisplay.addComponent( row );
          	}
          	
          	
          	if ( !m.isVirtual() ) {
          		RowComponent row = new RowComponent();
          		row.addTextComponent( "&3Blocks Remaining: &7%s  %s%% ",
          				dFmt.format( m.getRemainingBlockCount() ), 
          				fFmt.format( m.getPercentRemainingBlockCount() ) );
          		
          		chatDisplay.addComponent( row );
          	}
        	
          	
          	{
          		RowComponent row = new RowComponent();
          		row.addTextComponent( "&3Liner: &7%s", 
        				m.getLinerData().toInfoString() );
        		chatDisplay.addComponent( row );
          	}
        	
        	
          	
          	{
          		RowComponent row = new RowComponent();
          		row.addTextComponent( "&3Mine Command Count: &7%d", 
          				m.getResetCommands().size() );
          		chatDisplay.addComponent( row );
          	}
          	
          	
          	
          	{
          		RowComponent row = new RowComponent();
          		row.addTextComponent( "&3Mine BlockEvent Count: &7%d", 
          				m.getBlockEvents().size() );
          		chatDisplay.addComponent( row );
          	}
          	
          	
        	{
        		RowComponent row = new RowComponent();
        		double rtMinutes = m.getResetTime() / 60.0D;
        		row.addTextComponent( "&3Reset time: &7%s &3Secs (&7%.2f &3Mins)", 
        				Integer.toString(m.getResetTime()), rtMinutes );
        		chatDisplay.addComponent( row );
        	}
        	
        	{
        		RowComponent row = new RowComponent();
        		row.addTextComponent( "&3Mine Reset Count: &7%s ", 
        				dFmt.format(m.getResetCount()) );
        		
        		if ( m.isUsePagingOnReset() ) {
        			row.addTextComponent( "    &7-= &5Reset Paging Enabled &7=-" );
        		}
        		else {
        			row.addTextComponent( "    &7-= &3Reset Paging Disabled &7=-" );
        		}
        		
        		chatDisplay.addComponent( row );
        		
        		double resetTimeSeconds = m.getStatsResetTimeMS() / 1000.0;
        		if ( !m.isUsePagingOnReset() && resetTimeSeconds > 0.5 ) {
        			String resetTimeSec = PlaceholdersUtil.formattedTime( resetTimeSeconds );
        			chatDisplay.addText("&5  Warning: &3Reset time is &7%s&3, which is high. " +
        					"It is recommened that you try to enable &7/mines set resetPaging help",
        					resetTimeSec );
        		}
        	}
        	
        	if ( !m.isVirtual() ) {
        		RowComponent row = new RowComponent();
        		
        		long targetResetTime = m.getTargetResetTime();
        		double remaining = ( targetResetTime <= 0 ? 0d : 
        			(targetResetTime - System.currentTimeMillis()) / 1000d);
        		double rtMinutes = remaining / 60.0D;
        		
        		row.addTextComponent( "&3Time Remaining Until Reset: &7%s &3Secs (&7%.2f &3Mins)", 
        				dFmt.format( remaining ), rtMinutes );
        		chatDisplay.addComponent( row );
        	}
        	
        	{
        		RowComponent row = new RowComponent();
        		row.addTextComponent( "&3Notification Mode: &7%s &7%s", 
        				m.getNotificationMode().name(), 
        				( m.getNotificationMode() == MineNotificationMode.radius ? 
        						dFmt.format( m.getNotificationRadius() ) + " blocks" : "" ) );
        		chatDisplay.addComponent( row );
        	}
        	
        	{
        		RowComponent row = new RowComponent();
        		row.addTextComponent( "&3Notifications Filtered by Permissions: %s", 
        				( m.isUseNotificationPermission() ? "&2Enabled" : "&dDisabled" ) );
        		chatDisplay.addComponent( row );
        	}
        	
        	{
        		RowComponent row = new RowComponent();
        		row.addTextComponent( "&3Notification Permission: &7%s", 
        				m.getMineNotificationPermissionName() );
        		chatDisplay.addComponent( row );
        	}
        	
        	
        	

        	
        	{
        		RowComponent row = new RowComponent();
        		if ( m.isZeroBlockResetDisabled() ) {
        			row.addTextComponent( "&3Zero Blocks Reset Delay: &cDISABLED");
        		} else {
        			if ( m.getResetThresholdPercent() == 0 ) {
        				row.addTextComponent( "&3Zero Blocks Reset Delay: &7%s &3Seconds",
        						fFmt.format( m.getZeroBlockResetDelaySec() ));
        			}
        			else {
        				row.addTextComponent( "&7Threshold &3Reset Delay: &7%s &3Seconds",
        						fFmt.format( m.getZeroBlockResetDelaySec() ));
        			}
        		}
        		
        		chatDisplay.addComponent( row );
        	}
        	
        	
        	if ( !m.isVirtual() ) {
        		RowComponent row = new RowComponent();
        		if ( m.getResetThresholdPercent() == 0 ) {
        			row.addTextComponent( "&3Reset Threshold: &cDISABLED");
        		} else {
        			
        			double blocks =  m.getBounds().getTotalBlockCount() * 
        					m.getResetThresholdPercent() / 100.0d;
        			row.addTextComponent( "&3Reset Threshold: &7%s &3Percent (&7%s &3blocks)",
        					fFmt.format( m.getResetThresholdPercent() ),
        					dFmt.format( blocks ) );
        		}
        		
        		chatDisplay.addComponent( row );
        	}
        	
        	
        	if ( m.isSkipResetEnabled() ) {
        		RowComponent row = new RowComponent();
        		row.addTextComponent( "&3Skip Reset: &2Enabled&3: &3Threshold: &7%s  &3Skip Limit: &7%s",
        				fFmt.format( m.getSkipResetPercent() ), dFmt.format( m.getSkipResetBypassLimit() ));
        		chatDisplay.addComponent( row );
        		
        		if ( m.getSkipResetBypassCount() > 0 ) {
        			RowComponent row2 = new RowComponent();
        			row2.addTextComponent( "    &3Skipping Enabled: Skip Count: &7%s",
        					dFmt.format( m.getSkipResetBypassCount() ));
        			chatDisplay.addComponent( row2 );
        		}
        	} else {
        		RowComponent row = new RowComponent();
        		row.addTextComponent( "&3Skip Mine Reset if no Activity: &cnot set");
        		chatDisplay.addComponent( row );
        	}
        	
        	
        	
        	if ( m.isMineSweeperEnabled() ) {
        		
        		// stats for mine sweeper activity:
        		long mineSweeperAvgMs = ( m.getMineSweeperCount() == 0 ? 0 : m.getMineSweeperTotalMs() / m.getMineSweeperCount());
        		
        		String mineSweeperBlks = PlaceholdersUtil.formattedKmbtSISize(m.getMineSweeperBlocksChanged(), fFmt, " " );
        		
        		// Format with input requiring seconds:
        		String totalRunTime = PlaceholdersUtil.formattedTime( m.getMineSweeperTotalMs() / 1000d );
        		
        		RowComponent row = new RowComponent();
        		row.addTextComponent( "&3Mine Sweeper: &2Enabled&3: runs: %s  Avg ms: %s  Time: %s  Blks: %s ",
        						dFmt.format( m.getMineSweeperCount() ), dFmt.format( mineSweeperAvgMs ),
        						totalRunTime,
        						mineSweeperBlks );
        		chatDisplay.addComponent( row );
        		
        		if ( m.getStatsMineSweeperTaskMs().size() > 0 ) {
        			RowComponent row2 = new RowComponent();
        			row2.addTextComponent( "&3        %s ", m.statsMessageMineSweeper() );
        			chatDisplay.addComponent( row2 );
        		}

        	}
        	else {
        		RowComponent row = new RowComponent();
        		row.addTextComponent( "&3Mine Sweeper:  &cDisabled&3 ");
        		chatDisplay.addComponent( row );
        	}
        	
        	
        	
        	if ( m.getResetCommands() != null && m.getResetCommands().size() > 0 ) {
//        		RowComponent row = new RowComponent();
//        		row.addTextComponent( "&3Reset Commands: &7%s ",
//        				dFmt.format( m.getResetCommands().size() ) );

        		BulletedListComponent.BulletedListBuilder builder = new BulletedListComponent.BulletedListBuilder();

        		FancyMessage msg = new FancyMessage(String.format("&3Reset Commands: &7%s", 
        				dFmt.format( m.getResetCommands().size() )))
            			.suggest("/mines command list " + m.getName())
            			.tooltip("&7Click to list to view the reset commands.");
        		
        		builder.add(msg);
        		
        		chatDisplay.addComponent( builder.build() );
        	}

        	
        }

        {
        	chatDisplay.addText( "&3Block model: &7%s", 
        			( m.isUseNewBlockModel() ? "New" : "Old") );
        }
        
        int blockSize = 0;
        if ( cmdPageData.isShowAll() || cmdPageData.getCurPage() > 1 ) {
//        	if ( cmdPageData.isDebug() ) {
//        		chatDisplay.addText( "&3Block model: &7%s", 
//        				( m.isUseNewBlockModel() ? "New" : "Old") );
//        	}
        	chatDisplay.addText("&3Blocks:");
        	chatDisplay.addText("&8Click on a block's name to edit its chances of appearing.%s",
        			(m.isUseNewBlockModel() ? ".." : ""));
        	
        	BulletedListComponent list = getBlocksList(m, cmdPageData, true );
        	chatDisplay.addComponent(list);
        }

        if ( m.isUseNewBlockModel() ) {
        	blockSize =  m.getPrisonBlocks().size();
        }
        else {
        	blockSize = m.getBlocks().size();
        }
        
        String message = blockSize != 0 ? null : " &cNo Blocks Defined";
        cmdPageData.generatePagedCommandFooter( chatDisplay, message );
        
        chatDisplay.send(sender);
        
        // If show all, then include the mine's commands and blockEvents:
        // These are different commands, so they will be in different chatDisplay objects
        // so cannot weave them together:
        if ( cmdPageData.isShowAll() ) {
        	commandList( sender, m.getName() );
        	
        	blockEventList( sender, m.getName() );
        }
    }


	
    @Command(identifier = "mines reset", permissions = "mines.reset", description = "Resets a mine.")
    public void resetCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to reset, " +
        		"or '*all*' to reset all the mines, " +
        		"or '*cancel*' to cancel the resetting of all mines.") String mineName,
        @Wildcard(join=true)
    	@Arg(name = "options", description = "Optional settings [noCommands details] " +
    			"'noCommands' prevents the running of mine commands. " +
    			"'details' shows progress on reset *all*.", def = "") String options
    			) {
        
        // make sure not null and set to lower case:
        options = ( options == null ? "" : options.trim().toLowerCase());

        MineResetType resetType = MineResetType.FORCED;
        List<MineResetActions> resetActions = new ArrayList<>();
        
        
        if ( options.contains( "nocommands" )) {
        	options = options.replace( "nocommands", "" ).trim();
        	resetActions.add( MineResetActions.NO_COMMANDS );
        }
        
        // The value of chained is an internal value and should not be shown to users:
        if ( options.contains( "details" ) ) {
        	options = options.replace( "details", "" ).trim();
        	resetActions.add( MineResetActions.DETAILS );
        }
        
        // The value of chained is an internal value and should not be shown to users:
        if ( options.contains( "chained" ) ) {
        	options = options.replace( "chained", "" ).trim();
        	resetActions.add( MineResetActions.CHAINED_RESETS );
        }
        
        if ( !options.trim().isEmpty() ) {
        	sender.sendMessage( "&cInvalid value for &7options&c. " +
        			"&3The only valid options are: [&7noCommands details&3] or blanks. " +
        			"[&7" + options + "&3] mine = [&7" + mineName + "&3]" );
        	return;
        }

        PrisonMines pMines = PrisonMines.getInstance();
        
        if ( mineName != null && "*all*".equalsIgnoreCase( mineName ) ) {
        	pMines.resetAllMines( resetType, resetActions );
        	return;
        }
        
        if ( mineName != null && "*cancel*".equalsIgnoreCase( mineName ) ) {
        	pMines.cancelResetAllMines();
        	return;
        }
        

        if (!performCheckMineExists(sender, mineName)) {
            return;
        }

        setLastMineReferenced(mineName);
        
        
        Mine m = pMines.getMine(mineName);
        
        
        if ( m.isVirtual() ) {
        	sender.sendMessage( "&cInvalid option. This mine is a virtual mine&7. Use &a/mines set area &7to enable the mine." );
        	return;
        }
        

        if ( !m.isEnabled() ) {
        	sender.sendMessage( "&cMine is disabled&7. Use &a/mines info &7for possible cause." );
        	return;
        }

        try {
        	
        	m.manualReset( resetType, resetActions );
        } catch (Exception e) {
        	pMines.getMinesMessages().getLocalizable("mine_reset_fail")
                .sendTo(sender);
            Output.get().logError("Couldn't reset mine " + mineName, e);
        }

        pMines.getMinesMessages().getLocalizable("mine_reset").sendTo(sender);
    }


    @Command(identifier = "mines list", permissions = "mines.list", onlyPlayers = false)
    public void listCommand(CommandSender sender, 
    		@Arg(name = "sort", def = "sortOrder",
    			description = "Sort the list by either alpha or active [" + 
    					"sortOrder alpha active xSortOrder xAlpha xActive" +
    					"].  Most active mines are based upon blocks mined since server restart.") 
    				String sort,
            @Arg(name = "page", def = "1", 
            	description = "Page of search results (optional) [1-n, ALL]") String page 
    		) {
    	Player player = getPlayer( sender );
    	
    	MineSortOrder sortOrder = MineSortOrder.fromString( sort );
    	
    	// If sort was invalid, double check to see if it is a page number or ALL:
    	if ( sortOrder == MineSortOrder.invalid ) {
    		sortOrder = MineSortOrder.sortOrder;

    		if ( sort != null && "ALL".equalsIgnoreCase( sort )) {
    			// The user did not specify a sort order, but instead this is the page number
    			// so fix it for them:
    			page = "ALL";
    		}
    		else if ( sort != null ) {
    			try {
    				int test = Integer.parseInt( sort );
    				
    				// This is actually the page number so default to alpha sort:
    				page = Integer.toString( test );
    			}
    			catch ( NumberFormatException e ) {
    				// Oof... this isn't a page number, so report an error.
    				sender.sendMessage( "Invalid sort order.  Use a valid sort order " +
    						"or a page number such as [1-n, ALL]" );
    			}
    		}
    	}

    	ChatDisplay display = new ChatDisplay("Mines");
    	display.addText("&8Click a mine's name to see more information.");

    	
    	getMinesList( display, sortOrder, page, player );
    	
        
        
        display.send(sender);
    }

	public void getMinesList( ChatDisplay display, MineSortOrder sortOrder, String page, Player player )
	{
		PrisonMines pMines = PrisonMines.getInstance();
    	MineManager mMan = pMines.getMineManager();
    	
    	
    	// Get mines in the correct sorted order and suppress the mines if they should
    	PrisonSortableResults sortedMines = pMines.getMines( sortOrder );
    	
    	display.addText( "&3  Mines listed: &7%s   &3Mines suppressed: &7%s",
    					sortedMines.getSortedList().size(),
    					sortedMines.getSortedSuppressedList().size());
    	
    	if ( sortedMines.getSortedSuppressedList().size() > 0 ) {
    		display.addText( "&8To view suppressed mines sort by: %s", 
    				sortedMines.getSuppressedListSortTypes() );
    	}
    	
    	
//    	// Sort first by name, then blocks mined so final sort order will be:
//    	//   Most blocks mined, then alphabetical
//    	mineList.sort( (a, b) -> a.getName().compareToIgnoreCase( b.getName()) );
//
//    	// for now hold off on sorting by total blocks mined.
//    	if ( "active".equalsIgnoreCase( sort )) {
//    		mineList.sort( (a, b) -> Long.compare(b.getTotalBlocksMined(), a.getTotalBlocksMined()) );
//    	}
        
        CommandPagedData cmdPageData = new CommandPagedData(
        		"/mines list " + sortOrder.name(), sortedMines.getSortedList().size(),
        		0, page, 14 );
        
        BulletedListComponent list = 
        		getMinesLineItemList(sortedMines, player, cmdPageData, mMan.isMineStats());
    	
    	display.addComponent(list);
    	
    	cmdPageData.generatePagedCommandFooter( display );
    	
    	
	}


    private BulletedListComponent getMinesLineItemList( PrisonSortableResults sortedMines, Player player,
    		CommandPagedData cmdPageData, boolean isMineStatsEnabled )
	{
    	BulletedListComponent.BulletedListBuilder builder =
    			new BulletedListComponent.BulletedListBuilder();
    	    	
    	DecimalFormat dFmt = new DecimalFormat("#,##0");
    	DecimalFormat fFmt = new DecimalFormat("#,##0.00");
    	
    	int count = 0;
    	 
        for (Mine m : sortedMines.getSortedList()) {
        	
            if ( cmdPageData == null ||
            		count++ >= cmdPageData.getPageStart() && count <= cmdPageData.getPageEnd() ) {
            
            	RowComponent row = new RowComponent();
            	
            	//row.addTextComponent( m.getWorldName() + " " );
            	
            	if ( m.getSortOrder() < 1 ) {
//            		row.addTextComponent( "    " );

//            		row.addFancy( 
//            				new FancyMessage( String.format("&3(&b%s&3) ", 
//            						"X") )
//            				.tooltip("&7Sort order: Suppressed"));
            	}
            	else {
            		row.addFancy( 
            				new FancyMessage( String.format("&3(&b%s&3) ", 
            						Integer.toString( m.getSortOrder() )) )
            				.tooltip("&7Sort order."));
            	}
            	
            	
            	String name = m.getName();
            	if ( name.length() < 6 ) {
            		name += "      ".substring( 0, (6-name.length()) );
            	}
            	row.addFancy( 
            			new FancyMessage( String.format("&7%s ", name) )
            					.command("/mines info " + m.getName())
            					.tooltip("&7Mine " + m.getTag() + ": Click to view more info."));
            	
            	
            	if ( m.getTag() != null && m.getTag().trim().length() > 0 ) {
            		String tag = m.getTag();
            		String tagNoColor = Text.stripColor( tag );
            		if ( tagNoColor.length() < 6 ) {
            			tag += "      ".substring( 0, (6 - tagNoColor.length()) );
            		}
            		row.addTextComponent( "%s ", tag );
            	}
            	
            	
            	if ( !m.isVirtual() ) {
            		row.addFancy( 
            				new FancyMessage("&eTP ").command("/mines tp " + m.getName())
            				.tooltip("&7Click to TP to the mine"));
            	}
            	
            	
            	row.addTextComponent( "  &3(&2R: " );
            	
            	if ( !m.isVirtual() ) {
            		row.addFancy( 
            				new FancyMessage( 
            						String.format( "&7%s &3sec &3/ ", dFmt.format(m.getRemainingTimeSec())))
            				
            				.tooltip( "Estimated time in seconds before the mine resets" ) );
//            		row.addTextComponent( " sec &3(&b" );
            	}
            	
            	row.addFancy( 
            			new FancyMessage(
            					String.format( "&7%s &3sec )&b", dFmt.format(m.getResetTime()) ))
            			.tooltip( "Reset time in seconds" ) );
//            	row.addTextComponent( " sec&3)&b" );
            	
            	if ( !m.isVirtual() && player != null && 
            			m.getBounds().withinSameWorld( player.getLocation() ) ) {
            		
            		double distance = m.getBounds().getDistance3d(player.getLocation());
            		
//            		row.addTextComponent( "  &3Dist: &7");
            		row.addFancy( 
            				new FancyMessage(
            						String.format( "  &3Dist: &7%s", fFmt.format( distance )) ).
            				tooltip("Distance to the Mine") );
            		
            	}
            	
            	//builder.add(row.getFancy());
            	
            	
            	if ( !m.isVirtual() ) {
//            		RowComponent row2 = new RowComponent();
//            	row2.addTextComponent( "            &3Rem: " );
            		
            		// Right justify the total blocks mined, with 1000's separators:
            		String blocksMined = "          " + dFmt.format( m.getTotalBlocksMined() );
//            		String blocksMined = "                 " + dFmt.format( m.getTotalBlocksMined() );
            		blocksMined = blocksMined.substring( blocksMined.length() - 8);
            		
            		row.addFancy( 
            				new FancyMessage( String.format("  %s  &3Rem: ", blocksMined)).
            				tooltip( "Blocks mined" ) );
            		
            		row.addFancy( 
            				new FancyMessage(fFmt.format(m.getPercentRemainingBlockCount())).
            				tooltip( "Percent Blocks Remaining" ) );
            		
            		row.addTextComponent( "%%  &3RCnt: &7" );
            		
            		row.addFancy( 
            				new FancyMessage(dFmt.format(m.getResetCount())).
            				tooltip( "Times the mine was reset." ) );
            		
            		if ( !m.isVirtual() ) {
            			
            			row.addTextComponent( " &3 Vol: &7" );
            			row.addFancy( 
            					new FancyMessage(dFmt.format(m.getBounds().getTotalBlockCount())).
            					tooltip( "Volume in Blocks" ) );
            		}
            		
            		

                	
                	
                	boolean hasCmds = m.getResetCommands().size() > 0;
                	if ( hasCmds ) {
                		row.addFancy( 
                    			new FancyMessage( String.format(" &cCmds: &7%s  ", 
                    					Integer.toString( m.getResetCommands().size() )) )
                    					.command("/mines commands list " + m.getName())
                    					.tooltip("&7Click to view commands."));
                	}

                	
                	
                	boolean hasBlockEvents = m.getBlockEvents().size() > 0;
                	if ( hasBlockEvents ) {
                		row.addFancy( 
                				new FancyMessage( String.format(" &cbEvs: &7%s  ", 
                						Integer.toString( m.getBlockEvents().size() )) )
                				.command("/mines blockEvent list " + m.getName())
                				.tooltip("&7Click to view blockEvents."));
                	}
                	
                	
                	
                	if ( m.isVirtual() ) {
                		row.addFancy(  
                				new FancyMessage( "&cVIRTUAL " )
                				.command("/mines set area " + m.getName())
                				.tooltip("&7Click to set the mine's area to make it a real mine. "));
                	}
                	
                	
                	if ( !m.isEnabled() ) {
                		row.addFancy(  
                				new FancyMessage( "&cDISABLED!! " )
                				.command("/mines info " + m.getName())
                				.tooltip("&7Click to view possible reason why the mine is " +
                						"disabled. World may not exist? "));
                	}
                	
                	
                	if ( m.isUsePagingOnReset() ) {
                		row.addFancy( 
                				new FancyMessage("&5Paged ")
                				.tooltip("&7Paging Used during Mine Reset"));
                	}

      
            		
//       	 String noteMode = m.getNotificationMode().name() + 
//       			 ( m.getNotificationMode() == MineNotificationMode.radius ? 
//       					 " " + dFmt.format( m.getNotificationRadius() ) : "" );
//       	 row.addFancy( 
//       			 new FancyMessage(noteMode).tooltip( "Notification Mode" ) );
//       	 
//       	 row.addTextComponent( "&7 - &b" );
//
//       	 row.addFancy( 
//       			 new FancyMessage(m.getBounds().getDimensions()).tooltip( "Size of Mine" ) );
//       	 
//       	 row.addTextComponent( "&7 - &b");
            		
            		builder.add(row.getFancy());
            		
            	}
            	
            	
            	
            	if ( !m.isVirtual() && isMineStatsEnabled ) {
            		RowComponent rowStats = new RowComponent();
            		
            		rowStats.addTextComponent( "  -- &7 Stats :: " );
            		
            		rowStats.addTextComponent( m.statsMessage() );
            		
            		builder.add(rowStats.getFancy());
            	}
        	
            }
       }
       
//        display.addComponent(builder.build());

		return builder.build();
	}

	/**
     * <p>The following command will change the mine's time between resets. But it will
     * not be applied until after the next reset.
     * </p>
     * 
     * @param sender
     * @param mineName
     * @param time
     */
    @Command(identifier = "mines set skipReset", permissions = "mines.skipreset", 
    		description = "Set a mine to skip the reset if not enough blocks have been mined.")
    public void skipResetCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to edit.") String mineName,
        @Arg(name = "enabled", description = "Enable the skip reset processing: 'Enabled' or 'Disable'", 
        		def = "disabled") String enabled,
        @Arg(name = "percent", description = "Percent threshold before resetting.", def = "80" ) String percent,
        @Arg(name = "bypassLimit", description = "Limit number of skips before bypassing and performing a reset",
        		def = "50") String bypassLimit
    		) {
        
        if (performCheckMineExists(sender, mineName)) {
        	setLastMineReferenced(mineName);
        	
        	if ( enabled == null || !"enabled".equalsIgnoreCase( enabled ) && !"disabled".equalsIgnoreCase( enabled )) {
        		Output.get().sendWarn( sender,"&7Invalid &benabled&7 value. Must be either &benabled&7 or " +
        				"&bdisabled&7.  Was &b%s&7.", (enabled == null ? "&c-blank-" : enabled) );
        		return;
        	}

        	PrisonMines pMines = PrisonMines.getInstance();
        	Mine m = pMines.getMine(mineName);
            
//            if ( !m.isEnabled() ) {
//            	sender.sendMessage( "&cMine is disabled&7. Use &a/mines info &7for possible cause." );
//            	return;
//            }
            
        	boolean skipEnabled = "enabled".equalsIgnoreCase( enabled );
        	double skipPercent = 80.0d;
        	int skipBypassLimit = 50;
        	
        	try {
				skipPercent = Double.parseDouble( percent );
				if ( skipPercent < 0.0d ) {
					skipPercent = 0.0d;
				} else if ( skipPercent > 100.0d ) {
					skipPercent = 100.0d;
				}
			}
			catch ( NumberFormatException e1 ) {
				Output.get().sendWarn( sender,"&7Invalid percentage. Not a number. " +
						"Was &b%s&7.", (enabled == null ? "&c-blank-" : enabled) );
				return;
			}
        	
        	try {
				skipBypassLimit = Integer.parseInt( bypassLimit );
				if ( skipBypassLimit < 1 ) {
					skipBypassLimit = 1;
				} 
			}
			catch ( NumberFormatException e1 ) {
				Output.get().sendWarn( sender,"&7Invalid bypass limit. Not number. " +
						"Was &b%s&7.", (bypassLimit == null ? "-blank-" : bypassLimit) );
			}
        	
        	m.setSkipResetEnabled( skipEnabled );
        	m.setSkipResetPercent( skipPercent );
        	m.setSkipResetBypassLimit( skipBypassLimit );
        	
        	pMines.getMineManager().saveMine( m );
        	
        	// User's message:
        	String message = String.format( "&7mines skipreset for &b%s&7: &b%s&7  " +
					        			"threshold: &b%.2f&7 percent  bypassLimit: &b%d", 
					        			m.getTag(), (skipEnabled ? "enabled" : "disabled"),
					        			skipPercent, skipBypassLimit );
        	Output.get().sendInfo( sender, message );
        	
        	// Server Log message:
        	Player player = getPlayer( sender );
        	Output.get().logInfo( "%s :: Changed by: %s", message,
        								(player == null ? "console" : player.getDisplayName()) );
        } 
    }



    /**
     * <p>The following command will change the mine's time between resets. But it will
     * not be applied until after the next reset.
     * </p>
     * 
     * @param sender
     * @param mineName
     * @param time
     */
    @Command(identifier = "mines set resetTime", permissions = "mines.resettime", 
    		description = "Set a mine's time  to reset.")
    public void resetTimeCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to edit.") String mineName,
        @Arg(name = "time", description = "Time in seconds for the mine to auto reset." ) String time
        
    		) {
        
        if (performCheckMineExists(sender, mineName)) {
        	setLastMineReferenced(mineName);

        	try {
        		int resetTime = MineData.MINE_RESET__TIME_SEC__DEFAULT;

        		if ( time != null && time.trim().length() > 0 ) {
        			resetTime = Integer.parseInt( time );
        		}

				if ( resetTime < MineData.MINE_RESET__TIME_SEC__MINIMUM ) {
					Output.get().sendWarn( sender, 
							"&7Invalid resetTime value for &b%s&7. Must be an integer value of &b%d&7 or greater. [&b%d&7]",
							mineName, MineData.MINE_RESET__TIME_SEC__MINIMUM, resetTime );
				} else {
					PrisonMines pMines = PrisonMines.getInstance();
					Mine m = pMines.getMine(mineName);
			        
//			        if ( !m.isEnabled() ) {
//			        	sender.sendMessage( "&cMine is disabled&7. Use &a/mines info &7for possible cause." );
//			        	return;
//			        }
					
					m.setResetTime( resetTime );
					
        			pMines.getMineManager().saveMine( m );
        								
					// User's message:
					Output.get().sendInfo( sender, "&7mines set resettime: &b%s &7resetTime set to &b%d", m.getTag(), resetTime );
					
					// Server Log message:
					Player player = getPlayer( sender );
					Output.get().logInfo( "&bmines set resettime&7: &b%s &7set &b%s &7resetTime to &b%d", 
							(player == null ? "console" : player.getDisplayName()), m.getTag(), resetTime  );
				}
			}
			catch ( NumberFormatException e ) {
				Output.get().sendWarn( sender, 
						"&7Invalid resetTime value for &b%s&7. Must be an integer value of &b%d &7or greater. [&b%s&7]",
						mineName, MineData.MINE_RESET__TIME_SEC__MINIMUM, time );
			}
        } 
    }

    
    
    /**
     * <p>When a mine reaches zero blocks, a manual reset will be issued to run.  By default
     * it will have a 0 second delay before running, but this command controls how long of
     * a delay to use.
     * </p>
     * 
     * <p>Although the delay is in seconds, it should be known that the value will be multiplied
     * by 20 to convert it to ticks.  So any value less than 0.05 will be treated as zero and
     * effectively will be in 0.05 increments.  Give or take a tick should not matter, but 
     * beware if a player, or owner, complains that 0.17 is the same as 0.15.   
     * </p>
     * 
     * @param sender
     * @param mineName
     * @param time
     */
    @Command(identifier = "mines set zeroBlockResetDelay", permissions = "mines.zeroblockresetdelay", 
    		description = "Set a mine's delay before reset when it reaches zero blocks.")
    public void zeroBlockResetDelayCommand(CommandSender sender,
    		@Arg(name = "mineName", description = "The name of the mine to edit.") String mineName,
    		@Arg(name = "time/DISABLE", description = "Delay in seconds before resetting when the mine reaches " +
    				"zero blocks, or DISABLE." ) String time
    		
    		) {
    	
    	if (performCheckMineExists(sender, mineName)) {
    		setLastMineReferenced(mineName);
    		
    		try {
    			double resetTime = 
    						time != null && "disable".equalsIgnoreCase( time ) ? -1.0d : 
    						0.0d;
    			
    			if ( resetTime != -1.0d && time != null && time.trim().length() > 0 ) {
    				resetTime = Double.parseDouble( time );
    				
    				// Only displaying two decimal positions, since 0.01 is 10 ms. 
    				// Anything less than 0.01 is set to ZERO so it does not mess with anything unseen.
    				// Also any value less than 0.05 is basically zero since this value has to be
    				// converted to ticks.
    				if ( resetTime < 0.01d ) {
    					resetTime = 0.0d;
    				}
    			}
    			
    			PrisonMines pMines = PrisonMines.getInstance();
    			Mine m = pMines.getMine(mineName);
    	        
//    	        if ( !m.isEnabled() ) {
//    	        	sender.sendMessage( "&cMine is disabled&7. Use &a/mines info &7for possible cause." );
//    	        	return;
//    	        }
    			
    			m.setZeroBlockResetDelaySec( resetTime );
    			
    			pMines.getMineManager().saveMine( m );
    			
    			DecimalFormat dFmt = new DecimalFormat("#,##0.00");
    			// User's message:
    			if ( m.isZeroBlockResetDisabled() ) {
    				Output.get().sendInfo( sender, "&7Mine &b%s Zero Block Reset Delay: &cDISABLED", 
    						m.getTag(), dFmt.format( resetTime ) );
    				
    			} else {
    				Output.get().sendInfo( sender, "&7Mine &b%s Zero Block Reset Delay: &b%s &7sec", 
    						m.getTag(), dFmt.format( resetTime ) );
    				
    			}
    			
    			// Server Log message:
    			Player player = getPlayer( sender );
    			Output.get().logInfo( "&7Mine &b%s Zero Block Reset Delay: &b%s &7set it to &b%s &7sec",
    					(player == null ? "console" : player.getDisplayName()), 
    					m, dFmt.format( resetTime )  );
    		}
    		catch ( NumberFormatException e ) {
    			Output.get().sendWarn( sender, 
    					"&7Invalid zeroBlockResetDelay value for &b%s&7. Must be an double value of &b0.00 &7or " +
    					"greater. [&b%s&7]",
    					mineName, time );
    		}
    	} 
    }
    
    

	/**
     * <p>The following command will change the mine's time between resets. But it will
     * not be applied until after the next reset.
     * </p>
     * 
     * @param sender
     * @param mineName
     * @param time
     */
    @Command(identifier = "mines set resetThreshold", permissions = "mines.resetThreshold", 
    		description = "Triggers a mine reset once this threshold is crossed and the remaining " +
    				"block percentage is less than or equal to this value")
    public void resetThresholdPercentCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to edit.") String mineName,
        @Arg(name = "percent", description = "Threshold percent to trigger a reset.(0 is disabled)", 
        					def = "0" ) String percent
    		) {
        
        if (performCheckMineExists(sender, mineName)) {
        	setLastMineReferenced(mineName);
        	
        	PrisonMines pMines = PrisonMines.getInstance();
        	Mine m = pMines.getMine(mineName);
            
//            if ( !m.isEnabled() ) {
//            	sender.sendMessage( "&cMine is disabled&7. Use &a/mines info &7for possible cause." );
//            	return;
//            }
            
        	double thresholdPercent = 0.0d;
        	
        	try {
        		thresholdPercent = Double.parseDouble( percent );
				if ( thresholdPercent < 0.0d ) {
					thresholdPercent = 0.0d;
				} else if ( thresholdPercent > 100.0d ) {
					thresholdPercent = 100.0d;
				}
			}
			catch ( NumberFormatException e1 ) {
				Output.get().sendWarn( sender,"&7Invalid percentage. Not a number. " +
						"Was &b%s&7.", (percent == null ? "&c-blank-" : percent) );
				return;
			}
        	
        	
        	if ( thresholdPercent == m.getResetThresholdPercent() ) {
        		String msg = "The Reset Threshold Percent was not changed.";
        		Output.get().sendInfo( sender, msg );
        		return;
        	}
        	
        	m.setResetThresholdPercent( thresholdPercent );
        	
        	pMines.getMineManager().saveMine( m );
        	
        	double blocks = m.isVirtual() ? 0 :
        						m.getBounds().getTotalBlockCount() * 
									m.getResetThresholdPercent() / 100.0d;
        	
            DecimalFormat dFmt = new DecimalFormat("#,##0");
            DecimalFormat fFmt = new DecimalFormat("#,##0.00");
            
        	// User's message:
        	String message = String.format( "&7The Reset Threshold Percent for mine &b%s&7 was set to &b%s&7, " +
					        			"which is about &b%s &7blocks.", 
					        			m.getTag(), 
					        			fFmt.format( m.getResetThresholdPercent() ),
					        			dFmt.format( blocks ) );
        	Output.get().sendInfo( sender, message );
        	
        	// Server Log message:
        	Player player = getPlayer( sender );
        	Output.get().logInfo( "%s :: Changed by: %s", message,
        								(player == null ? "console" : player.getDisplayName()) );
        } 
    }



    @Command(identifier = "mines set notification", permissions = "mines.notification", 
    		description = "Set a mine's notification mode.")
    public void setNotificationCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to edit.") String mineName,
        @Arg(name = "mode", def="displayOptions", description = "The notification mode to use: disabled, within, radius") 
    					String mode,
        @Arg(name = "radius", def="0", description = "The distance from the center of the mine to notify players of a reset." ) 
    					String radius
        
    		) {
        
        if (performCheckMineExists(sender, mineName)) {
        	setLastMineReferenced(mineName);

        	PrisonMines pMines = PrisonMines.getInstance();
        	Mine m = pMines.getMine(mineName);
            
//            if ( !m.isEnabled() ) {
//            	sender.sendMessage( "&cMine is disabled&7. Use &a/mines info &7for possible cause." );
//            	return;
//            }
        	
        	MineNotificationMode noteMode = MineNotificationMode.fromString( mode, MineNotificationMode.displayOptions );
        	
        	if ( noteMode == MineNotificationMode.displayOptions ) {
        		sender.sendMessage( "&7Select a Mode: &bdisabled&7, &bwithin &7the mine, &bradius " +
        				"&7from center of mine." );
        	} else {
        		long noteRadius = 0L;
        		if ( noteMode == MineNotificationMode.radius ) {
        			if ( radius == null || radius.trim().length() == 0 ) {
        				noteRadius = MineData.MINE_RESET__BROADCAST_RADIUS_BLOCKS;
        			} else {
        				try {
        					noteRadius = Long.parseLong( radius );
        					
        					if ( noteRadius < 1 ) {
        						noteRadius = MineData.MINE_RESET__BROADCAST_RADIUS_BLOCKS;
        						DecimalFormat dFmt = new DecimalFormat("#,##0");
        						Output.get().sendWarn( sender, "&7Invalid radius value for &b%s&7. " +
            							"Must be an positive non-zero integer. Using the default value: &b%s &7[&b%s&7]",
            							mineName, dFmt.format(MineData.MINE_RESET__BROADCAST_RADIUS_BLOCKS), radius );
        					}
        				}
        				catch ( NumberFormatException e ) {
        					e.printStackTrace();
        					Output.get().sendWarn( sender, "&7Invalid notification radius for &b%s&7. " +
        							"Must be an positive non-zero integer. [&b%s&7]",
        							mineName, radius );
        				}
        			}
        		}
        		
        		if ( m.getNotificationMode() != noteMode || m.getNotificationRadius() != noteRadius ) {
        			m.setNotificationMode( noteMode );
        			m.setNotificationRadius( noteRadius );
        			
        			pMines.getMineManager().saveMine( m );
        			
        			DecimalFormat dFmt = new DecimalFormat("#,##0");
        			// message: notification mode changed
        			Output.get().sendInfo( sender, "&7Notification mode was changed for &b%s&7: &b%s %s",
        					mineName, m.getNotificationMode().name(), 
        					(m.getNotificationMode() == MineNotificationMode.radius ? 
        							dFmt.format( m.getNotificationRadius() ) : "" ));
        			
        		} else {
        			// message: notification mode did not change
        			Output.get().sendInfo( sender, "&7Notification mode was not changed for mine &b%s&7.", mineName );
        		}
        	}
        } 
    }


    @Command(identifier = "mines set notificationPerm", permissions = "mines.notification", 
    		description = "Enable or disable a mine's notification permission. If enabled, then players " +
    					"must have the mine's permission to get messages for reset. This filter " +
    					"can be combined with the other notification settings.", 
    		altPermissions = "mines.notification.[mineName]")
    public void setNotificationPermissionCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to edit.") String mineName,
        @Arg(name = "action", def="enable", description = "Enable or disable the permission filtering: [enable, disable]") 
    					String action
        
    		) {
        
        if (performCheckMineExists(sender, mineName)) {
        	setLastMineReferenced(mineName);

        	PrisonMines pMines = PrisonMines.getInstance();
        	Mine m = pMines.getMine(mineName);
            
//            if ( !m.isEnabled() ) {
//            	sender.sendMessage( "&cMine is disabled&7. Use &a/mines info &7for possible cause." );
//            	return;
//            }
            
            if ( !action.equalsIgnoreCase( "enable" ) && !action.equalsIgnoreCase( "disable" )) {
            	sender.sendMessage( "&7Invalid value for action: [enable, disable]" );
            	return;
            }
            
            if ( action.equalsIgnoreCase( "enable" ) && !m.isUseNotificationPermission() ) {
            	sender.sendMessage( 
            			String.format( "&7Notification Permission filter has been enabled. Using permission %s",
            					m.getMineNotificationPermissionName() ) );
            	m.setUseNotificationPermission( true );
            	pMines.getMineManager().saveMine( m );
            }
            else if ( action.equalsIgnoreCase( "disable" ) && m.isUseNotificationPermission() ) {
            	sender.sendMessage( "&7Notification Permission filter has been disabled." );
            	m.setUseNotificationPermission( false );
            	pMines.getMineManager().saveMine( m );
            }
            else {
            	
            	sender.sendMessage( "&7Notification Permission filter was not changed. Canceling." );
            }
            
            
        } 
    }




    @Command(identifier = "mines set accessPermission", permissions = "mines.set", 
    		description = "Mine access should be controlled by Ranks. Please see " +
    				"'/mines set mineAccessByRank help' for more information. As a secondary " +
    				"backup, Mines Access by Permissions will " +
    				"Enable or disable a mine's access with the use of a permission. " +
    				"If enabled, then players " +
    					"must have this permission to be able to mine. This may be able to " +
    					"replace the need to setting up WorldGuard regions, since this may " +
    					"serve the same purpose. This option defaults to disabled. " +
    					"This command can only use permissions. Permission groups will not work. ", 
    		altPermissions = "mines.notification.[mineName]")
    public void setMinePermissionCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to add a permission to.") String mineName,
        @Arg(name = "permission", def="enable", description = "Permission.  Suggested: `mines.<mineName>`: [none]") 
    					String permission
        
    		) {
        
        if (performCheckMineExists(sender, mineName)) {
        	setLastMineReferenced(mineName);

        	PrisonMines pMines = PrisonMines.getInstance();
        	Mine m = pMines.getMine(mineName);
            
        	if ( m.isMineAccessByRank() ) {

        		sender.sendMessage( "&3The use of Mine Access Permissions is not needed and is disabled " +
        				"because Mine Access is controlled by Ranks." );
        		
        		return;
        	}
        	
            
        	if ( permission == null || permission.equalsIgnoreCase( "none" ) ) {
            	m.setAccessPermission( null );
            	pMines.getMineManager().saveMine( m );

            	sender.sendMessage( 
            			String.format( "&7The Mine Access Permission has been disabled for %s.", 
            					m.getName() ));
            }
            else {
            	m.setAccessPermission( permission );
            	pMines.getMineManager().saveMine( m );
            	
            	sender.sendMessage( 
            			String.format( "&7The Mine Access Permission has been enable for %s and " +
            					"has a value of [%s].", m.getName(), permission ));
            }
            
            
        } 
    }


    @Command(identifier = "mines set rank", permissions = "mines.set", 
    		description = "Links a mine to a rank or removes the rank.")
    public void setMineRankCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine.") String mineName,
        @Arg(name = "rankName", description = "Then rank name to link to this mine. " +
        		"Use 'none' to remove the rank.") 
    					String rankName
        
    		) {
        
        if (performCheckMineExists(sender, mineName)) {
        	setLastMineReferenced(mineName);

        	PrisonMines pMines = PrisonMines.getInstance();
        	Mine m = pMines.getMine(mineName);
            
            
            if ( rankName == null || rankName.trim().length() == 0 ) {
            	sender.sendMessage( "&cRank name is required." );
            	return;
            }
            
            
            if ( m.getRank() != null ) {
            	// First unlink the preexisting mine and rank:
            	String removedRankName = m.getRank().getName();
            	
            	// unlinkModuleElement will do the saving
            	Prison.get().getPlatform().unlinkModuleElements( m, m.getRank() );
        		
        		sender.sendMessage( String.format( "&3Rank &7%s &3has been removed from mine &7%s", 
        				removedRankName, m.getTag() ));

            }
            
            if ( !"none".equalsIgnoreCase( rankName ) ) {
            	
            	boolean success = Prison.get().getPlatform().linkModuleElements( m, 
            			ModuleElementType.RANK, rankName );
            	
            	if ( !success ) {
            		sender.sendMessage( String.format( "&3Invalid Rank Name: &7%s", rankName ));
            	}
            	else {
            		sender.sendMessage( String.format( "&3Rank &7%s &3has been linked to mine &7%s", 
            				rankName, m.getTag() ));
            	}
            }
            
        } 
    }


/*
 * Remove this command since the same functionality exists in /mines set rank:
 * This will be removed shortly once the replacement is confirmed to work well.
 * 
 *   @Command(identifier = "mines set norank", permissions = "mines.set", 
 *   		description = "Unlinks a rank from a mine")
 *	public void setMineNoRankCommand(CommandSender sender,
 *   		@Arg(name = "mineName", description = "The name of the mine.") String mineName
 *   
 *   		) {
 *   	
 *   	if (performCheckMineExists(sender, mineName)) {
 *   		setLastMineReferenced(mineName);
 *   		
 *   		PrisonMines pMines = PrisonMines.getInstance();
 *   		Mine m = pMines.getMine(mineName);
 *   		
 *   		if ( m.getRank() == null ) {
 *   			sender.sendMessage( "&cThis mine has no ranks to unlink." );
 *   			return;
 *   		}
 *   		
 *   		ModuleElement rank = m.getRank();
 *   		
 *   		Prison.get().getPlatform().unlinkModuleElements( m, m.getRank() );
 *   		
 *   		
 *   		sender.sendMessage( String.format( "&3Rank &7%s &3has been removed from mine &7%s", 
 *   				rank.getName(), m.getName() ));
 *   		
 *   	} 
 *   }
 *   
 */
    

    @Command(identifier = "mines set area", permissions = "mines.set", 
    				description = "Set the area of a mine to your current selection or a 1x1 mine under your feet.")
    public void redefineCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to edit.") String mineName,
        @Arg(name = "source", description = "&3The source to use for setting the area. The &7wand&3 " +
        		"uses the area defined by the wand. &7Feet&3 defines a 1x1 mine under your feet" +
        		"which is useful in void worlds or when flying and can be enlarged with " +
        		"&7/mines set size help&3 . &2[&7wand feet&2]", 
        				def = "wand") String source,
        @Arg(name = "confirm", description = "If the mine is greater than 20k blocks you will have " +
        		"to confirm the area.", def = "---") String confirm
        ) {
    	
    	if (!performCheckMineExists(sender, mineName)) {
    		return;
    	}

        PrisonMines pMines = PrisonMines.getInstance();
        Mine m = pMines.getMine(mineName);
        
        Player player = getPlayer( sender );
        
//        if ( !m.isEnabled() ) {
//        	sender.sendMessage( "&cMine is disabled&7. Use &a/mines info &7for possible cause." );
//        	return;
//        }
        
        Selection selection = null;

        if ( source != null && "feet".equalsIgnoreCase( source ) ) {
        	selection = new Selection( player.getLocation(), player.getLocation());
        }
        else if ( source == null || "wand".equalsIgnoreCase( source ) ) {
        	selection = Prison.get().getSelectionManager().getSelection( player );
        }
        else {
        	sender.sendMessage( "&3Valid values for &2source &3are &7wand&3 and &7feet&3." );
        	return;
        }
        
        if (!selection.isComplete()) {
        	pMines.getMinesMessages().getLocalizable("select_bounds")
                .sendTo(sender);
            return;
        }

        if (!Objects.equals(selection.getMin().getWorld().getName(),
            selection.getMax().getWorld().getName())) {
            PrisonMines.getInstance().getMinesMessages().getLocalizable("world_diff")
                .sendTo(sender);
            return;
        }
        
        DecimalFormat dFmt = new DecimalFormat("#,##0");
        Bounds selectedBounds = selection.asBounds();
        
        if ( selectedBounds.getTotalBlockCount() > 20000 && 
        		(confirm == null || !"yes".equalsIgnoreCase( confirm ) )) {
        	String message = String.format( "&7Warning: This mine has a size of %s. If this is " +
        			"intentional, then please re-submit this command with a confirmation of 'yes' " +
        			"as a final parameter.  ", dFmt.format( selectedBounds.getTotalBlockCount() ) );
        	sender.sendMessage( message );
        	return;
        }

        // TODO check to see if they are the same boundaries, if not, don't change...
        
        setLastMineReferenced(mineName);
        
        boolean wasVirtual = m.isVirtual();
        
        // Setting the bounds when it's virtual will configure all the internals:
        
        m.setBounds(selectedBounds);
        
        if ( wasVirtual ) {
        	
        	
        	String message = String.format( "&3The mine &7%s &3 is no longer a virutal mine " +
        			"and has been enabled with an area of &7%s &3blocks.",
        			m.getTag(), dFmt.format( m.getBounds().getTotalBlockCount() ));
        	
        	sender.sendMessage( message );
        	Output.get().logInfo( message );
        }

        pMines.getMineManager().saveMine( m );
        
        pMines.getMinesMessages().getLocalizable("mine_redefined")
            .sendTo(sender);
        
        // Delete the selection:
        Prison.get().getSelectionManager().clearSelection((Player) sender);
        //pMines.getMineManager().clearCache();
        
        // adjustSize to zero to reset set all liners:
        m.adjustSize( Edges.walls, 0 );
    }

    

    @Command(identifier = "mines set tracer", permissions = "mines.set", 
    				description = "Clear the mine and set a tracer around the outside")
    public void setTracerCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to set the tracer in.") String mineName) {
    	
    	if (!performCheckMineExists(sender, mineName)) {
    		return;
    	}

        PrisonMines pMines = PrisonMines.getInstance();
        Mine mine = pMines.getMine(mineName);
        
        
        if ( mine.isVirtual() ) {
        	sender.sendMessage( "&cMine is a virtual mine&7. Use &a/mines set area &7to enable the mine." );
        	return;
        }

        mine.enableTracer();

    }

    
    
    @Command(identifier = "mines set size", permissions = "mines.set", description = "Change the size of the mine")
    public void setSizeCommand(CommandSender sender,
    		@Arg(name = "mineName", description = "The name of the mine to resize.") String mineName,
    		@Arg(name = "edge", description = "Edge to adjust [top, bottom, north, east, south, west, walls]", def = "walls") String edge, 
    		//@Arg(name = "adjustment", description = "How to adust the size [smaller, larger]", def = "larger") String adjustment,
    		@Arg(name = "amount", description = "amount to adjust, [-1, 0, 1]. Zero will refresh liner.", def = "1") int amount 
    		
    		) {
    	
    	if (!performCheckMineExists(sender, mineName)) {
    		return;
    	}
    	
    	Edges e = Edges.fromString( edge );
    	if ( e == null ) {
    		sender.sendMessage( "&cInvalid edge value. [top, bottom, north, east, south, west, walls]" );
    		return;
    	}
    	
    	if ( amount == 0 ) {
    		sender.sendMessage( "&cSize of mine will not be changed. Will refresh the liner." );
//    		return;
    	}

//    	if ( adjustment == null || "smaller".equalsIgnoreCase( adjustment ) || "larger".equalsIgnoreCase( adjustment ) ) {
//    		sender.sendMessage( "&cInvalid adjustment. [larger, smaller]" );
//    		return;
//    	}
    	
    	PrisonMines pMines = PrisonMines.getInstance();
    	Mine mine = pMines.getMine(mineName);
    	
    	if ( mine.isVirtual() ) {
    		sender.sendMessage( "&cMine is a virtual mine&7. Use &a/mines set area &7to enable the mine." );
    		return;
    	}
    	
    	
    	mine.adjustSize( e, amount );
    	
    	pMines.getMineManager().saveMine( mine );
    	
    	
    }
    
    
//    @Command(identifier = "mines set move", permissions = "mines.set", 
//    				description = "Move the location of the mine by a few blocks")
    public void moveMineCommand(CommandSender sender,
    		@Arg(name = "mineName", description = "The name of the mine to set the tracer in.") String mineName,
    		@Arg(name = "direction", def = "north",
    				description = "Direction to move mine [top, bottom, north, east, south, west, walls]" ) String direction, 
    		@Arg(name = "amount", description = "amount to move, [1, 2, 3, ...]", def = "1") int amount 
    		
    		) {
    	
    	if (!performCheckMineExists(sender, mineName)) {
    		return;
    	}
    	
    	Edges edge = Edges.fromString( direction );
    	if ( edge == null || edge == Edges.walls ) {
    		sender.sendMessage( "&cInvalid direction value. [top, bottom, north, east, south, west]" );
    		return;
    	}
    	
    	if ( amount < 1 ) {
    		sender.sendMessage( "&cInvalid amount. Must be 1 or more." );
    		return;
    	}
    	
    	
    	PrisonMines pMines = PrisonMines.getInstance();
    	Mine mine = pMines.getMine(mineName);
    	
    	if ( mine.isVirtual() ) {
    		sender.sendMessage( "&cMine is a virtual mine&7. Use &a/mines set area &7to enable the mine." );
    		return;
    	}
    	
    	
    	mine.moveMine( edge, amount );
    	
    	pMines.getMineManager().saveMine( mine );
    }
    
    @Command(identifier = "mines set liner", permissions = "mines.set", 
    			description = "Change the blocks that line the mine.")
    public void setLinerCommand(CommandSender sender,
    		@Arg(name = "mineName", description = "The name of the mine") String mineName,
    		@Arg(name = "edge", description = "Edge to use [top, bottom, north, east, south, west, walls]", def = "walls") String edge, 
    		//@Arg(name = "adjustment", description = "How to adust the size [smaller, larger]", def = "larger") String adjustment,
    		@Arg(name = "pattern", description = "pattern to use. '?' for a list of all patterns. " +
    				"'repair' will attempt to repair missing blocks outside of the liner. " +
    				"'remove' will remove the liner from the mine. 'removeAll' removes alll liners. [?]", 
    				def = "bright") String pattern,
    		@Arg(name = "force", description = "Force liner if air [force no]", def = "no") String force
    		
    		) {
    	
    	if (!performCheckMineExists(sender, mineName)) {
    		return;
    	}
    	
    	if ( pattern != null && "?".equals( pattern ) || edge != null && "?".equals( edge )) {
    		
    		sender.sendMessage( "&cAvailable Edges: &3[&7top bottom north east south west walls&3]" );
    		sender.sendMessage( "&3Available Patterns: [&7" + LinerPatterns.toStringAll() + "&3]" );
    		return;
    	}
    	
    	Edges e = Edges.fromString( edge );
    	if ( e == null ) {
    		sender.sendMessage( "&cInvalid edge value. &3[&7top bottom north east south west walls&3]" );
    		return;
    	}
    	
    	LinerPatterns linerPattern = LinerPatterns.fromString( pattern );
    	if ( linerPattern == null ) {
    		sender.sendMessage( "&cInvalid pattern.&3 Select one of these: [&7" + 
    							LinerPatterns.toStringAll() + "&3]" );
    		return;
    	}
    	
    	boolean isForced = false;
    	if ( force != null && !"force".equalsIgnoreCase( force ) && !"no".equalsIgnoreCase( force ) ) {
    		sender.sendMessage( 
    				String.format( "&3The valid values for &7force &3 are &7force&3 and &7no&3. " +
    						"Was &2[&7%s&2]", force ) );
    	}
    	else if ( "force".equalsIgnoreCase( force ) ) {
    		isForced = true;
    	}
    	
    	PrisonMines pMines = PrisonMines.getInstance();
    	Mine mine = pMines.getMine(mineName);
    	
//    	if ( mine.isVirtual() ) {
//    		sender.sendMessage( "&cMine is a virtual mine.&7 Use &a/mines set area &7to enable the mine." );
//    		return;
//    	}
    	
    	if ( linerPattern == LinerPatterns.removeAll ) {
    		
    		mine.getLinerData().removeAll();
    		sender.sendMessage( "&7All liners have been removed from mine " + mine.getName() );
    	}
    	else if ( linerPattern == LinerPatterns.remove ) {
    		mine.getLinerData().remove( e );
    		sender.sendMessage( "&7The liner for the " + e.name() + " has been removed from mine " + mine.getName() );
    	}
    	else {
    		mine.getLinerData().setLiner( e, linerPattern, isForced );
    		new MineLinerBuilder( mine, e, linerPattern, isForced );
    	}
    	
    	pMines.getMineManager().saveMine( mine );
    }
    
    

    @Command(identifier = "mines set resetpaging", permissions = "mines.resetpaging", 
    		description = "Enable paging during a mine reset.")
    public void setMineResetPagingCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to edit.") String mineName,
        @Arg(name = "paging", def="disabled", 
        		description = "Enable or disable paging [disable, enable]") 
    					String paging
    		) {
        
        if (performCheckMineExists(sender, mineName)) {
        	setLastMineReferenced(mineName);

        	PrisonMines pMines = PrisonMines.getInstance();
        	Mine m = pMines.getMine(mineName);
            
//            if ( !m.isEnabled() ) {
//            	sender.sendMessage( "&cMine is disabled&7. Use &a/mines info &7for possible cause." );
//            	return;
//            }
        	
            if  ( paging == null || !"disable".equalsIgnoreCase( paging ) && !"enable".equalsIgnoreCase( paging ) ) {
            	sender.sendMessage( "&cInvalid paging option&7. Use &adisable&7 or &aenable&7" );
            	return;
            }
            
            if ( "disable".equalsIgnoreCase( paging ) && m.isUsePagingOnReset() ) {
            	m.setUsePagingOnReset( false );
            	pMines.getMineManager().saveMine( m );
            	sender.sendMessage( String.format( "&7Mine Reset Paging has been disabled for mine %s.", m.getTag()) );
            }
            else if ( "enable".equalsIgnoreCase( paging ) && !m.isUsePagingOnReset() ) {
            	m.setUsePagingOnReset( true );
            	pMines.getMineManager().saveMine( m );
            	sender.sendMessage( String.format( "&7Mine Reset Paging has been enabled for mine %s.", m.getTag()) );
            }
            else {
            	sender.sendMessage( String.format( "&7Mine Reset Paging status has not changed for mine %s.", m.getTag()) );
            	
            }
        	
        } 
    }


    @Command(identifier = "mines set mineSweeper", permissions = "mines.set", 
    		description = "Enable the Mine Sweeper task that is used to update the block counts " +
    				"in the mine if there is another plugin that is breaking blocks and " +
    				"prison is unable to integrate with that process to get an accurate block " +
    				"update in real time. WARNING: This task should never be used unless it is a last " +
    				"resort. With each block that broke, prison will 'try' to submit this task, but " +
    				"it can only submit the task every 2 to 10 seconds, no matter how many blocks " +
    				"are broke or how quickly. The fewer blocks that remain in the mine, the shorter " +
    				"the submission delay will be. Enable '/mines stats' to monitor run time of " +
    				"the sweep, then use '/mines info' to view them.")
    public void setMineSweeperCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to edit.") String mineName,
        @Arg(name = "mineSweeper", def="disabled", 
        		description = "Enable or disable the mineSweeper tasks [disable, enable]") 
    					String mineSweeper
    		) {
        
        if (performCheckMineExists(sender, mineName)) {
        	setLastMineReferenced(mineName);

        	PrisonMines pMines = PrisonMines.getInstance();
        	Mine m = pMines.getMine(mineName);
            
            if  ( mineSweeper == null || !"disable".equalsIgnoreCase( mineSweeper ) && 
            									!"enable".equalsIgnoreCase( mineSweeper ) ) {
            	sender.sendMessage( "&cInvalid paging option&7. Use &adisable&7 or &aenable&7" );
            	return;
            }
            
            if ( "disable".equalsIgnoreCase( mineSweeper ) && m.isMineSweeperEnabled() ) {
            	m.setMineSweeperEnabled( false );
            	pMines.getMineManager().saveMine( m );
            	sender.sendMessage( String.format( "&7Mine Sweeper has been disabled for mine %s.", m.getTag()) );
            }
            else if ( "enable".equalsIgnoreCase( mineSweeper ) && !m.isMineSweeperEnabled() ) {
            	m.setMineSweeperEnabled( true );
            	pMines.getMineManager().saveMine( m );
            	sender.sendMessage( String.format( "&7Mine Sweeper has been enabled for mine %s.", m.getTag()) );
            }
            else {
            	sender.sendMessage( String.format( "&7Mine Sweeper status has not changed for mine %s.", m.getTag()) );
            	
            }
        	
        } 
    }

    

    @Command(identifier = "mines tp", description = "TP to the mine. Will default to the mine's " +
    		"spawn location if set, but can specify the target [spawn, mine]. OPs and console can " +
    		"TP other online players to a specified mine. Access for non-OPs can be setup through " +
    		"'/mines set tpAccessByRank help` is preferred over permissions.", 
    		aliases = "mtp",
    		altPermissions = {"access-by-rank", "mines.tp", "mines.tp.[mineName]"})
    public void mineTp(CommandSender sender,
        @Arg(name = "mineName", def="",
        		description = "The name of the mine to teleport to.") String mineName,
        
		@Arg(name = "player", def = "", description = "Player name to TP - " +
				"Only console or rank command can include this parameter") String playerName,
		@Arg(name = "target", def = "spawn", 
				description = "Selects either the mine's spawn location or the center of " +
						"the mine. [spawn, mine]")
    			String target
    		) {
    	
    	
    	if ( mineName != null && 
    			("spawn".equalsIgnoreCase( mineName ) || "mine".equalsIgnoreCase( mineName )) ) {
    		target = mineName;
    		// Since the value spawn and mine are the last parameters, then we know playerName and 
    		// mineName were not provided so set them to empty Strings:
    		playerName = "";
    		mineName = "";
    	}

    	
    	// If playerName was not specified, then it could contain the value of target, if so, then copy
    	// to the target variable and set playerName to an empty String.
    	if ( playerName != null && 
    			("spawn".equalsIgnoreCase( playerName ) || "mine".equalsIgnoreCase( playerName )) ) {
    		target = playerName;
    		playerName = "";
    	}

    	// Only valid values are mine and spawn, if anything other than these, set value to spawn:
    	if ( target == null || 
    			!("spawn".equalsIgnoreCase( target ) || "mine".equalsIgnoreCase( target )) ) {
    		target = "spawn";
    	}
    	//setLastMineReferenced(mineName);
    	
    	PrisonMines pMines = PrisonMines.getInstance();
    	Mine m = null;
    	
    	
    	if ( mineName == null || mineName.trim().isEmpty() ) {
    		// Need to find a "correct" mine to TP to.
    		
    		m = (Mine) Prison.get().getPlatform().getPlayerDefaultMine( sender );

    		if ( m == null ) {
    			
    			sender.sendMessage( "&cNo target mine found. " +
    									"&3Resubmit teleport request with a mine name." );
    			return;
    		}
    	}
    	else {
    		
    		// Load mine information first to confirm the mine exists and the parameter is correct:
    		if (!performCheckMineExists(sender, mineName)) {
    			return;
    		}
    		
    		
    		m = pMines.getMine(mineName);
    	}
    	
    	
    	
    	if ( m.isVirtual() ) {
    		sender.sendMessage( "&cInvalid option. This mine is a virtual mine&7. Use &a/mines set area &7to enable the mine." );
    		return;
    	}
    	
    	
    	Player player = getPlayer( sender );
    	
    	Player playerAlt = getOnlinePlayer( playerName );
    	
    	
    	if ( playerName != null && playerName.trim().length() > 0 && playerAlt == null) {
    		sender.sendMessage( "&3Specified player is not in the game so they cannot be teleported." );
    		return;
    	}
    	
    	
    	if ( (player == null || !player.isOnline()) && playerAlt != null && !playerAlt.isOnline() ) {
    		sender.sendMessage( "&3The player must be in the game." );
			return;
    	}
    	
    	
    	boolean isOp = sender.isOp();
    	
    	if ( isOp && playerAlt != null && playerAlt.isOnline() ) {
    		
    		// The person issuing the tp command is op and they are trying to TP another player
    		player = playerAlt;

    		// Console is trying to TP someone, the other checks do not apply:
    		teleportPlayer( playerAlt, m, target );
    		return;
    	}
    	else if ( (player == null || !player.isOnline()) && playerAlt != null && playerAlt.isOnline() ) {
    		
    		// If the sender is console or its being ran as a rank command, and the playerName is 
    		// a valid online player, then TP them:
    		
    		teleportPlayer( playerAlt, m, target );
    		return;
    	}
    	else if ( playerAlt != null && !player.getName().equalsIgnoreCase( playerAlt.getName()  ) ) {
    		
    		sender.sendMessage( "&3You cannot teleport other players to a mine. Ignoring parameter." );
    		return;
    	}
    	
    	// From here on down, cannot use playerAlt, so must use either sender or player:
    	
    	if ( mineName == null || mineName.trim().isEmpty() ) {
    		// Need to find a "correct" mine to TP to.
    		
    		m = (Mine) Prison.get().getPlatform().getPlayerDefaultMine( sender );

    		if ( m == null ) {
    			
    			sender.sendMessage( "&cNo target mine found. " +
    									"&3Resubmit teleport request with a mine name." );
    			return;
    		}
    	}
    	
        
        
    	// NOTE: Mine.hasTPAccess() checks for rank access and also if they have perms set.
        if ( !isOp && !m.hasTPAccess( player ) ) {
        	
        	Output.get().sendError(sender, "Sorry. You're unable to teleport there." );
        	return;
        	
        }
    	
//    	String minePermission = "mines.tp." + m.getName().toLowerCase();
//    	if ( !isOp &&
//    			!sender.hasPermission("mines.tp") && 
//    			!sender.hasPermission( minePermission ) ) {
//    		
//            Output.get()
//                .sendError(sender, "Sorry. You're unable to teleport there." );
//            return;
//        }
    	

    	
//        if ( !m.isEnabled() ) {
//        	sender.sendMessage( "&cMine is disabled&7. Use &a/mines info &7for possible cause." );
//        	return;
//        }
        
    	if ( sender.isPlayer() ) {
    		teleportPlayer( (Player) sender, m, target );
//    		m.teleportPlayerOut( (Player) sender, target );
    	} else {
    		sender.sendMessage(
    	            "&3Telport failed. Are you sure you're a Player?");
    	}
    }

    private void teleportPlayer( Player player, Mine mine, String target ) {
    	
    	if ( Prison.get().getPlatform().getConfigBooleanFalse( "prison-mines.tp-warmup.enabled" ) ) {
    		
    		// if warm up enabled:
    		double maxDistance = Prison.get().getPlatform().
    							getConfigDouble( "prison-mines.tp-warmup.movementMaxDistance", 1.0 );
    		long delayInTicks = Prison.get().getPlatform().
    							getConfigLong( "prison-mines.tp-warmup.delayInTicks", 20 );
    		
    		MineTeleportWarmUpTask mineTeleportWarmUp = new MineTeleportWarmUpTask( 
    							player, mine, target, maxDistance );
    		PrisonTaskSubmitter.runTaskLater( mineTeleportWarmUp, delayInTicks );
    	}
    	else {
    		
    		mine.teleportPlayerOut( player, target );
    		
    		// To "move" the player out of the mine, they are elevated by one block above the surface
    		// so need to remove the glass block if one is spawned under them.  If there is no glass
    		// block, then it will do nothing.
    		mine.submitTeleportGlassBlockRemoval();
    	}
    	


    }
    
    @Command(identifier = "mines stats", permissions = "mines.stats", description = "Toggle stats on all mines.")
    public void mineStats(CommandSender sender) {
    	
    	PrisonMines pMines = PrisonMines.getInstance();
    	MineManager mMan = pMines.getMineManager();
    	
    	// toggle the stats:
    	mMan.setMineStats( !mMan.isMineStats() );
    	
    	if ( mMan.isMineStats() ) {
    		sender.sendMessage(
    				"&3Mine Stats are now enabled. Use &7/mines list&3 to view stats on last mine reset. ");
    	} else {
    		sender.sendMessage( "&3Mine stats are now disabled." );
    	}
    }
   
    
    
    @Command(identifier = "mines whereami", permissions = "mines.whereami", 
    				description = "Identifies what mines you are in, or are the closest to." )
    public void mineWhereAmI(CommandSender sender) {
    	
    	Player player = getPlayer( sender );
    	
    	if (player == null || !player.isOnline()) {
    		sender.sendMessage( "&3You must be a player in the game to run this command." );
    		return;
    	}
    	
    	player.sendMessage( "&3Your coordinates are: &7" + player.getLocation().toBlockCoordinates() );

    	PrisonMines pMines = PrisonMines.getInstance();

    	
    	Mine lookingAtMine = null;
    	
    	List<Block> sightBlocks = player.getLineOfSightBlocks();
    	
//    	Block sightBlock = player.getLineOfSightBlock();
//    	Location sightLocation = sightBlock != null ? sightBlock.getLocation() : null;
    	
    	
    	List<Mine> inMine = new ArrayList<>();
    	TreeMap<Integer, Mine> nearMine = new TreeMap<>();
    	for ( Mine mine : pMines.getMineManager().getMines() ) {
    		
    		
    		// Check the first 10 blocks in the line of sight to see if any are in a mine.
    		// The reason why the first 10 are checked is to "look" through mine liners.
    		if ( lookingAtMine == null && sightBlocks.size() > 0 ) {
    			int cnt = 0;
    			for ( Block sightBlock : sightBlocks ) {
    				if ( mine.isInMineExact( sightBlock.getLocation() ) ) {
    					lookingAtMine = mine;
    					break;
    				}
					if ( cnt++ < 10 ) {
						break;
					}
				}
    		}
//    		if ( sightLocation != null && mine.isInMineExact( sightLocation )) {
//    			lookingAtMine = mine;
//    		}
    		
    		if ( !mine.isVirtual() && mine.getBounds().withinIncludeTopBottomOfMine( player.getLocation() ) ) {
    			inMine.add( mine );
    		}
    		
    		// This is checking for within a certain distance from any mine, so we just need to use
    		// some arbitrary distance as a max radius.  We do not want to use the individual values
    		// that have been set for each mine.
    		else if ( !mine.isVirtual() &&  mine.getBounds().within( player.getLocation(), 
    									MineData.MINE_RESET__BROADCAST_RADIUS_BLOCKS) ) {
    			Double distance = mine.getBounds().getDistance3d( player.getLocation() );
//    			Double distance = new Bounds( mine.getBounds().getCenter(), player.getLocation()).getDistance();
    			nearMine.put( distance.intValue(), mine );
    		}
    	}
    	
    	if ( lookingAtMine != null ) {
    		double distance = lookingAtMine.getBounds().getDistance3d( player.getLocation() );
    		DecimalFormat dFmt = new DecimalFormat("#,##0.0");
    		sender.sendMessage( String.format( "&3You are looking at mine &7%s &3which is &7%s &3blocks away.", 
    					lookingAtMine.getTag(), dFmt.format( distance ) ) );
    	}
    	
    	if ( inMine.size() > 0 ) {
    		// You are in the mines:
    		for ( Mine m : inMine ) {
    			sender.sendMessage( "&3You are in mine &7" + m.getTag() );
    		}
    	}
    	if ( nearMine.size() > 0 ) {
    		// You are near the mines:
    		int cnt = 0;
    		Set<Integer> distances = nearMine.keySet();
    		for ( Integer dist : distances ) {
				Mine m = nearMine.get( dist );
				sender.sendMessage( "&3You are &7" + dist + " &7blocks away from the center of mine &3" + m.getTag() );
				if ( ++cnt >= 5 ) {
					break;
				}
			}
    		
    	} 
    	else if ( inMine.size() == 0 ) {
    		// you are not near any mines:
    		sender.sendMessage( "&3Sorry, you are not within " + MineData.MINE_RESET__BROADCAST_RADIUS_BLOCKS + 
    				" blocks from any mine." );
    	}

    }

//	private Player getPlayer( CommandSender sender ) {
//		Optional<Player> player = Prison.get().getPlatform().getPlayer( sender.getName() );
//		return player.isPresent() ? player.get() : null;
//	}
    
	private Player getOnlinePlayer( String playerName ) {
		Player player = null;
		if ( playerName != null && !playerName.trim().isEmpty() ) {
			Optional<Player> oPlayer = Prison.get().getPlatform().getPlayer( playerName );
			player = oPlayer.isPresent() ? oPlayer.get() : null;
		}
		return player;
	}
	

    
    @Command(identifier = "mines wand", permissions = "mines.wand", 
    		description = "Receive a wand to select a mine area.", 
    		onlyPlayers = false )
    public void wandCommand(CommandSender sender) {
    	
    	Player player = getPlayer( sender );
    	
    	if (player == null || !player.isOnline()) {
    		sender.sendMessage( "&3You must be a player in the game to run this command." );
    		return;
    	}

        Prison.get().getSelectionManager().bestowSelectionTool(player);
        sender.sendMessage(
            "&3Here you go! &7Left click to select the first corner, and right click to select the other.");
    }
    
    

    

	@Command(identifier = "mines blockEvent list", description = "Lists the blockEvent commands for a mine.", 
    						onlyPlayers = false, permissions = "mines.set")
    public void blockEventList(CommandSender sender, 
    				@Arg(name = "mineName") String mineName) {
    	
    	
        if (!performCheckMineExists(sender, mineName)) {
            return;
        }

        
        setLastMineReferenced(mineName);
        
        PrisonMines pMines = PrisonMines.getInstance();
//    	MineManager mMan = pMines.getMineManager();
        Mine m = pMines.getMine(mineName);
        
        if (m.getBlockEvents() == null || m.getBlockEvents().size() == 0) {
            Output.get().sendInfo(sender, "The mine '%s' contains no BlockEvent commands.", m.getTag());
            return;
        }


        ChatDisplay display = new ChatDisplay("BlockEvent Commands for " + m.getTag());
        display.addText("&8Hover over values for more information and clickable actions.");

        generateBlockEventListing( m, display, true );
        
        display.addComponent(new FancyMessageComponent(
            new FancyMessage("&7[&a+&7] Add").suggest("/mines blockEvent add " + mineName + " [chance] [perm] [cmd] /")
                .tooltip("&7Add a new BockEvent command.")));
        display.send(sender);
    }

	private void generateBlockEventListing( Mine m, ChatDisplay display, boolean includeRemove ) {
		
		BulletedListComponent.BulletedListBuilder builder =
        					new BulletedListComponent.BulletedListBuilder();

        DecimalFormat dFmt = new DecimalFormat("0.00000");
        
        int rowNumber = 1;
        for (MineBlockEvent blockEvent : m.getBlockEvents()) {
        	
        	RowComponent row = new RowComponent();
        	
        	String chance = dFmt.format( blockEvent.getChance() );
        	
        	row.addTextComponent( " &3Row: &d%d  ", rowNumber++ );
        	
        	FancyMessage msgPercent = new FancyMessage( String.format( "&7%s%% ", chance ) )
        			.suggest( "/mines blockEvent percent " + m.getName() + " " + rowNumber + " [%]" )
        			.tooltip("Percent Chance - Click to Edit");
        	row.addFancy( msgPercent );
        	
        	FancyMessage msgPerm = new FancyMessage( String.format( "&3[&7%s&3] ", 
        													blockEvent.getPermission() ) )
        			.suggest( "/mines blockEvent permission " + m.getName() + " " + rowNumber + " [permisson]" )
        			.tooltip("Permission - Click to Edit");
        	row.addFancy( msgPerm );
        	
        	FancyMessage msgEventType = new FancyMessage( String.format( "&7%s", 
        													blockEvent.getEventType().name() ) )
        			.suggest( "/mines blockEvent eventType " + m.getName() + " " + rowNumber + " [eventType]" )
        			.tooltip("Event Type - Click to Edit");
        	row.addFancy( msgEventType );
        	
        	if ( blockEvent.getTriggered() != null ) {
        		
        		FancyMessage msgTriggered = new FancyMessage( String.format( "&3:&7%s", 
        				blockEvent.getTriggered() ) )
        				.suggest( "/mines blockEvent triggered " + m.getName() + " " + rowNumber + " [triggered]" )
        				.tooltip("Triggered - Click to Edit");
        		row.addFancy( msgTriggered );
        	}
        	
        	FancyMessage msgMode = new FancyMessage( String.format( " &3(&7%s&3) ", 
        			blockEvent.getTaskMode().name() ) )
        			.suggest( "/mines blockEvent mode " + m.getName() + " " + rowNumber + " [mode]" )
        			.tooltip("Event Task Mode - Click to Edit");
        	row.addFancy( msgMode );
        	
        	FancyMessage msgCommand = new FancyMessage( String.format( " &a'&7%s&a'", 
        			blockEvent.getCommand() ) )
        			//.command("/mines blockEvent remove " + mineName + " " + blockEvent.getCommand() )
        			.tooltip("Event Commands - You cannot change a command directly, " +
        					"delete it and then re-add it.");
        	row.addFancy( msgCommand );
        	
        	
//        	if ( blockEvent.getPrisonBlocks().size() > 0 ) {
//        		StringBuilder sb = new StringBuilder();
//        		
//        		for ( PrisonBlock block : blockEvent.getPrisonBlocks() ) {
//					if ( sb.length() > 0 ) {
//						sb.append( ", " );
//					}
//					sb.append( block.getBlockName() );
//				}
//        		if ( sb.length() > 0 ) {
//        			sb.insert( 0, "[" );
//        			sb.append( "]" );
//        			
//        			FancyMessage msgBlocks = new FancyMessage( sb.toString() )
//        					.tooltip( "Block filters. Click block list to add another." )
//        					.suggest( "/mines blockvent block add " + m.getName() + " " + 
//        								rowNumber + " block_name" );
//        			
//        			row.addFancy( msgBlocks );
//        		}
//        	}
        	
        	if ( includeRemove ) {
        		
        		FancyMessage msgRemove = new FancyMessage( " &4Remove&3" )
        				.suggest("/mines blockEvent remove " + m.getName() + " " + rowNumber )
        				.tooltip("Click to Delete this BlockEvent");
        		row.addFancy( msgRemove );
        	}
        	
	
            builder.add( row );
            
            
            
            String prisonBlocks = blockEvent.getPrisonBlockStrings();
            if ( !prisonBlocks.isEmpty() ) {
            	RowComponent row2 = new RowComponent();
            	
            	row2.addTextComponent( "                " );
            	
            	FancyMessage msgBlocks = new FancyMessage( String.format( " &bBlocks: &3[&7%s&3]", 
            			prisonBlocks ) )
            			.command("/mines blockEvent blocks " + m.getName() )
            			.tooltip("Event Blocks - Click to Edit");
            	row2.addFancy( msgBlocks );
            	
            	builder.add( row2 );
            }
            
        }

        display.addComponent(builder.build());
	}


	@Command(identifier = "mines blockEvent remove", description = "Removes a BlockEvent command from a mine.", 
    		onlyPlayers = false, permissions = "mines.set")
    public void blockEventRemove(CommandSender sender, 
    				@Arg(name = "mineName") String mineName,
    				@Arg(name = "row") Integer row) {
    	
        if ( row == null || row <= 0 ) {
        	sender.sendMessage( 
        			String.format("&7Please provide a valid row number greater than zero. " +
        					"Was row=[&b%d&7]",
        					(row == null ? "null" : row) ));
        	return;        	
        }
        
        if (!performCheckMineExists(sender, mineName)) {
            return;
        }
        
        setLastMineReferenced(mineName);
        
        PrisonMines pMines = PrisonMines.getInstance();
        Mine m = pMines.getMine(mineName);
        
        
        if (m.getBlockEvents() == null || m.getBlockEvents().size() == 0) {
            Output.get().sendInfo(sender, "The mine '%s' contains no BlockEvent commands.", m.getTag());
            return;
        }

        if ( row > m.getBlockEvents().size() ) {
        	sender.sendMessage( 
        			String.format("&7Please provide a valid row number no greater than &b%d&7. " +
        					"Was row=[&b%d&7]",
        					m.getBlockEvents().size(), (row == null ? "null" : row) ));
        	return;        	
        }
        
        MineBlockEvent blockEvent = m.getBlockEvents().get( row - 1 );
        
        
        if ( blockEvent != null && m.getBlockEventsRemove( blockEvent ) ) {
        	
        	pMines.getMineManager().saveMine( m );
            	
        	Output.get().sendInfo(sender, "Removed BlockEvent command '%s' from the mine '%s'.", 
        				blockEvent.getCommand(), m.getTag());
        } else {
        	Output.get().sendWarn(sender, 
        			String.format("The mine %s doesn't contain that BlockEvent command. Nothing was changed.", 
        						m.getTag()));
        }
        
        // Redisplay the event list:
        blockEventList( sender, mineName );
    }

	

	@Command(identifier = "mines blockEvent add", description = "Adds a BlockBreak command to a mine. " +
			"To send messages use {msg} or {broadcast} followed by the formatted message. " +
			"Can use placeholders {player} and {player_uid}. Use ; between multiple commands. " +
			"Example: 'token give {player} 1;{msg} &7You got &31 &7token!;tpa a'", 
    		onlyPlayers = false, permissions = "mines.set")
    public void blockEventAdd(CommandSender sender, 
    			@Arg(name = "mineName", description = "mine name, or 'placeholders' for a list of possible placeholders that " +
    					"you can use with blockEvents") String mineName,
    			@Arg(name = "percent", def = "100.0",
    					description = "Percent chance between 0.0000 and 100.0") Double chance,
    			@Arg(name = "permission", def = "none",
    					description = "Optional permission that the player must have, or [none] for no perm." 
    								) String perm,
//    			@Arg(name = "eventType", def = "eventTypeAll",
//    					description = "EventType to trigger BlockEvent: [eventTypeAll, eventBlockBreak, eventTEXplosion]"
//    								) String eventType,
//    			@Arg(name = "triggered", def = "none",
//    					description = "TE Explosion Triggered sources. Requires TokenEnchant v18.11.0 or newer. [none, ...]"
//    					) String triggered,
    			@Arg(name = "taskMode", description = "Processing task mode to run the task as console. " +
    								"Player runs as player. " +
    								"[inline, inlinePlayer, sync, syncPlayer]",
    					def = "inline") String mode,
    			@Arg(name = "command") @Wildcard String command) {
    	
		// Note: async is not an option since the bukkit dispatchCommand will run it as sync.
		//String mode = "sync";
		
        if ( mineName != null && "placeholders".equalsIgnoreCase( mineName ) ) {
        	
        	String placeholders = 
        			
        			PrisonCommandTask.CustomPlaceholders.listPlaceholders(
        					PrisonCommandTask.CommandEnvironment.all_commands ) + " " +
        			
        			PrisonCommandTask.CustomPlaceholders.listPlaceholders(
									PrisonCommandTask.CommandEnvironment.blockevent_commands );
        	
        	String message = String.format( "Valid Placeholders that can be used with blockEvents: [%s]", 
        							placeholders );
        	
        	Output.get().logInfo( message );
        	return;
        }

		
    	if (command.startsWith("/")) {
            command = command.replaceFirst("/", "");
        }

        if (!performCheckMineExists(sender, mineName)) {
            return;
        }
        
        if ( chance <= 0d || chance > 100.0d ) {
        	sender.sendMessage( 
        			String.format("&7Please provide a valid value for chance " +
        								"between 0.0000 and 100.0. Was state=[&b%d&7]",
        			chance ));
        	return;
        }
        
        TaskMode taskMode = TaskMode.fromString( mode );
        
        if ( mode == null || !taskMode.name().equalsIgnoreCase( mode ) ) {
        	sender.sendMessage( 
        			String.format("&7Task mode is defaulting to %s. " +
        					"[inline, inlinePlayer, sync, syncPlayer]  mode=[&b%s&7]",
        					taskMode.name(), mode ));
        	return;
        }
        
        if ( perm == null || perm.trim().length() == 0 || "none".equalsIgnoreCase( perm ) ) {
        	perm = "";
        }
        
        
//        BlockEventType eType = BlockEventType.fromString( eventType );
//        if ( !eType.name().equalsIgnoreCase( eventType ) ) {
//        	sender.sendMessage( 
//        			String.format("&7Notice: The supplied eventType does not match the list of valid " +
//        					"BlockEventTypes therefore defaulting to eventTypeAll. Valid eventTypes are: " +
//        					"[eventTypeAll, eventBlockBreak, eventTEXplosion]",
//        					eventType ));
//        }
        
//        if ( eType != BlockEventType.eventTEXplosion && triggered != null && !"none".equalsIgnoreCase( triggered ) ) {
//        	sender.sendMessage( "&7Notice: triggered is only valid exclusivly for eventTEXplosion. " +
//        			"Defaulting to none." );
//        	triggered = null;
//        }
//        if ( triggered != null && "none".equalsIgnoreCase( triggered ) ) {
//        	triggered = null;
//        }

        
        setLastMineReferenced(mineName);
        
        PrisonMines pMines = PrisonMines.getInstance();
//    	MineManager mMan = pMines.getMineManager();
        Mine m = pMines.getMine(mineName);
        
        if ( command == null || command.trim().length() == 0 ) {
        	sender.sendMessage( 
        			String.format( "&7Please provide a valid BlockEvent command: command=[%s]", command) );
        	return;
        }
        
        MineBlockEvent blockEvent = new MineBlockEvent( chance, perm, command, taskMode );
        m.getBlockEvents().add( blockEvent );

        pMines.getMineManager().saveMine( m );
        
        Output.get().sendInfo(sender, "&7Added BlockEvent command '&b%s&7' " +
        		"&7to the mine '&b%s&7' with " +
        		"the optional permission %s. Using the mode %s.", 
        		command, m.getTag(), 
        		perm == null || perm.trim().length() == 0 ? "&3none&7" : "'&3" + perm + "&7'",
        		mode );

		String.format("&7Notice: &3The default eventType has been set to &7all&3. If you need " +
				"to change it to something else, then use the command &7/mines blockEvent eventType help&3. " +
				"[all, blockBreak, TEXplosion] The event type is what causes the block to break. " +
				"Token Enchant's Explosion events are covered and can be focused with the " +
				"triggered parameter." );

        
//        if ( eType == BlockEventType.eventTEXplosion ) {
//        	sender.sendMessage( "&7Notice: &3Since the event type is for TokenEnchant's eventTEXplosion, " +
//        			"then you may set the value of &7triggered&7 with the command " +
//        			"&7/mines blockEvent triggered help&3." );
//        }

        
        // Redisplay the event list:
        blockEventList( sender, mineName );

    }


	@Command(identifier = "mines blockEvent percent", 
			description = "Edits the percentage amount", 
    		onlyPlayers = false, permissions = "mines.set")
    public void blockEventPercent(CommandSender sender, 
    			@Arg(name = "mineName") String mineName,
    			@Arg(name = "row") Integer row,
    			@Arg(name = "percent",
    					description = "Percent chance between 0.0000 and 100.0") Double chance) {
    	
        if (!performCheckMineExists(sender, mineName)) {
            return;
        }
        
        if ( row == null || row <= 0 ) {
        	sender.sendMessage( 
        			String.format("&7Please provide a valid row number greater than zero. " +
        					"Was row=[&b%d&7]",
        					(row == null ? "null" : row) ));
        	return;        	
        }
        
        if ( chance <= 0d || chance > 100.0d ) {
        	sender.sendMessage( 
        			String.format("&7Please provide a valid value for chance " +
        					"between 0.0000 and 100.0. Was state=[&b%d&7]",
        					chance ));
        	return;
        }
                
        setLastMineReferenced(mineName);
        
        PrisonMines pMines = PrisonMines.getInstance();
//    	MineManager mMan = pMines.getMineManager();
        Mine m = pMines.getMine(mineName);
        
        if ( row > m.getBlockEvents().size() ) {
        	sender.sendMessage( 
        			String.format("&7Please provide a valid row number no greater than &b%d&7. " +
        					"Was row=[&b%d&7]",
        					m.getBlockEvents().size(), (row == null ? "null" : row) ));
        	return;        	
        }
        
        MineBlockEvent blockEvent = m.getBlockEvents().get( row - 1 );
        
        double chanceOld = blockEvent.getChance();
        
        // Update percent:
        blockEvent.setChance( chance );

        // Save the mine:
        pMines.getMineManager().saveMine( m );
        
        DecimalFormat dFmt = new DecimalFormat("0.00000");
        Output.get().sendInfo(sender, "&7BlockEvent percentage &b%s&7 was changed for mine '&b%s&7'. " +
        		"Was &b%s&7. Command '&b%s&7'", 
        		dFmt.format( chance ), m.getTag(), 
        		dFmt.format( chanceOld ), blockEvent.getCommand() );
        
        // Redisplay the event list:
        blockEventList( sender, mineName );

    }


	@Command(identifier = "mines blockEvent permission", description = "Edits a BlockBreak permisson.", 
    		onlyPlayers = false, permissions = "mines.set")
    public void blockEventPermission(CommandSender sender, 
    			@Arg(name = "mineName") String mineName,
    			@Arg(name = "row") Integer row,
    			@Arg(name = "permission", def = "none",
    					description = "Optional permission that the player must have, or [none] for no perm." 
    								) String perm
    			) {
    	

        if (!performCheckMineExists(sender, mineName)) {
            return;
        }
        
        
        if ( row == null || row <= 0 ) {
        	sender.sendMessage( 
        			String.format("&7Please provide a valid row number greater than zero. " +
        					"Was row=[&b%d&7]",
        					(row == null ? "null" : row) ));
        	return;        	
        }
        
        
        if ( perm == null || perm.trim().length() == 0 || "none".equalsIgnoreCase( perm ) ) {
        	perm = "";
        }
        
        
        setLastMineReferenced(mineName);
        
        PrisonMines pMines = PrisonMines.getInstance();
//    	MineManager mMan = pMines.getMineManager();
        Mine m = pMines.getMine(mineName);
        
        if ( row > m.getBlockEvents().size() ) {
        	sender.sendMessage( 
        			String.format("&7Please provide a valid row number no greater than &b%d&7. " +
        					"Was row=[&b%d&7]",
        					m.getBlockEvents().size(), (row == null ? "null" : row) ));
        	return;        	
        }
        
        MineBlockEvent blockEvent = m.getBlockEvents().get( row - 1 );

        String permissionOld = blockEvent.getPermission();
        
        blockEvent.setPermission( perm );

        pMines.getMineManager().saveMine( m );
        
        
        Output.get().sendInfo(sender, "&7BlockEvent permission &b%s&7 was changed for mine '&b%s&7'. " +
        		"Was &b%s&7. Command '&b%s&7'", 
        		perm == null || perm.trim().length() == 0 ? "&3none&7" : "'&3" + perm + "&7'", 
        		m.getTag(), 
        		permissionOld == null || permissionOld.trim().length() == 0 ? "&3none&7" : 
        														"'&3" + permissionOld + "&7'", 
        		blockEvent.getCommand() );

        
        // Redisplay the event list:
        blockEventList( sender, mineName );

    }


	@Command(identifier = "mines blockEvent eventType", description = "Edits a BlockBreak EventType. " +
			"The 'all' event is the default and applies to all event types. The 'blockBreak' targets " +
			"only normal Bukkit BlockBreakEvents. The 'TEXplosion' enables Token Enchant's Block " +
			"Explosion events. While 'CEXplosion' is for Crazy Enchant's Block Explosion Events.", 
    		onlyPlayers = false, permissions = "mines.set")
    public void blockEventEventType(CommandSender sender, 
    			@Arg(name = "mineName") String mineName,
    			@Arg(name = "row") Integer row,
    			@Arg(name = "eventType", def = "all",
					description = "EventType to trigger BlockEvent: " +
										"[all, blockBreak, TEXplosion, CEXplosion]"
							) String eventType
    			) {
    	

        if (!performCheckMineExists(sender, mineName)) {
            return;
        }
        
        
        if ( row == null || row <= 0 ) {
        	sender.sendMessage( 
        			String.format("&7Please provide a valid row number greater than zero. " +
        					"Was row=[&b%d&7]",
        					(row == null ? "null" : row) ));
        	return;        	
        }
        
        
        BlockEventType eType = BlockEventType.fromString( eventType );
        if ( !eType.name().equalsIgnoreCase( eventType ) ) {
        	sender.sendMessage( 
        			String.format("&7Notice: The supplied eventType does not match the list of valid " +
        					"BlockEventTypes therefore defaulting to eventTypeAll. Valid eventTypes are: " +
        					"[%s]",
        					eventType, BlockEventType.getPrimaryEventTypes() ));
        }
        
        setLastMineReferenced(mineName);
        
        PrisonMines pMines = PrisonMines.getInstance();
//    	MineManager mMan = pMines.getMineManager();
        Mine m = pMines.getMine(mineName);
        
        if ( row > m.getBlockEvents().size() ) {
        	sender.sendMessage( 
        			String.format("&7Please provide a valid row number no greater than &b%d&7. " +
        					"Was row=[&b%d&7]",
        					m.getBlockEvents().size(), (row == null ? "null" : row) ));
        	return;        	
        }
        
        MineBlockEvent blockEvent = m.getBlockEvents().get( row - 1 );

        BlockEventType eTypeOld = blockEvent.getEventType();
        
        blockEvent.setEventType( eType );

        pMines.getMineManager().saveMine( m );
        
        
        Output.get().sendInfo(sender, "&7BlockEvent EventType &b%s&7 was changed for mine '&b%s&7'. " +
        		"Was &b%s&7. Command '&b%s&7'", 
        		eType.name(), m.getTag(), eTypeOld.name(), blockEvent.getCommand() );

        if ( eType == BlockEventType.TEXplosion ) {
        	sender.sendMessage( "&7Notice: &3Since the event type is for TokenEnchant's eventTEXplosion, " +
        			"then you may set the value of &7triggered&7 with the command " +
        			"&7/mines blockEvent triggered help&3." );
        }
        
        // Redisplay the event list:
        blockEventList( sender, mineName );

    }

	
	@Command(identifier = "mines blockEvent triggered", description = "Edits a BlockBreak triggered value.", 
			onlyPlayers = false, permissions = "mines.set")
	public void blockEventTriggered(CommandSender sender, 
			@Arg(name = "mineName") String mineName,
			@Arg(name = "row") Integer row,
			@Arg(name = "triggered", def = "none",
					description = "TE Explosion Triggered sources. Requires TokenEnchant v18.11.0 or newer. [none, ...]"
						) String triggered
			) {
		
		
		if (!performCheckMineExists(sender, mineName)) {
			return;
		}
		
		
		if ( row == null || row <= 0 ) {
			sender.sendMessage( 
					String.format("&7Please provide a valid row number greater than zero. " +
							"Was row=[&b%d&7]",
							(row == null ? "null" : row) ));
			return;        	
		}
		
		
		setLastMineReferenced(mineName);
		
		PrisonMines pMines = PrisonMines.getInstance();
//    	MineManager mMan = pMines.getMineManager();
		Mine m = pMines.getMine(mineName);
		

		
		if ( row > m.getBlockEvents().size() ) {
			sender.sendMessage( 
					String.format("&7Please provide a valid row number no greater than &b%d&7. " +
							"Was row=[&b%d&7]",
							m.getBlockEvents().size(), (row == null ? "null" : row) ));
			return;        	
		}
		
		MineBlockEvent blockEvent = m.getBlockEvents().get( row - 1 );

		
        if ( blockEvent.getEventType() != BlockEventType.TEXplosion && triggered != null && 
        		!"none".equalsIgnoreCase( triggered ) ) {
        	sender.sendMessage( "&7Notice: triggered is only valid exclusivly for eventTEXplosion. " +
        			"Defaulting to none." );
        	triggered = null;
        }
        if ( triggered != null && "none".equalsIgnoreCase( triggered ) ) {
        	triggered = null;
        }

        String oldTriggered = blockEvent.getTriggered();
		
		
		blockEvent.setTriggered( triggered );
		
		pMines.getMineManager().saveMine( m );
		
		
		Output.get().sendInfo(sender, "&7BlockEvent triggered &b%s&7 was changed for mine '&b%s&7'. " +
				"Was &b%s&7. Command '&b%s&7'", 
				(triggered == null ? "none" : triggered), 
				m.getTag(), 
				(oldTriggered == null ? "none" : oldTriggered), 
				blockEvent.getCommand() );
		
		
		// Redisplay the event list:
		blockEventList( sender, mineName );
		
	}
	

	@Command(identifier = "mines blockEvent taskMode", description = "Edits a BlockBreak task mode type: [inline, sync].", 
    		onlyPlayers = false, permissions = "mines.set")
    public void blockEventJobMode(CommandSender sender, 
    			@Arg(name = "mineName") String mineName,
    			@Arg(name = "row") Integer row,
    	        @Arg(name = "taskMode", description = "Processing task mode to run the task: " +
    	        		"[inline, inlinePlayer, sync, syncPlayer]",
    					def = "inline") String mode
    			) {

        if (!performCheckMineExists(sender, mineName)) {
            return;
        }
        
        
        if ( row == null || row <= 0 ) {
        	sender.sendMessage( 
        			String.format("&7Please provide a valid row number greater than zero. " +
        					"Was row=[&b%d&7]",
        					(row == null ? "null" : row) ));
        	return;        	
        }
        
        
        TaskMode taskMode = TaskMode.fromString( mode );
        
        if ( mode == null || !taskMode.name().equalsIgnoreCase( mode ) ) {
        	sender.sendMessage( 
        			String.format("&7Task mode is defaulting to %s. " +
        					"[inline, inlinePlayer, sync, syncPlayer]  mode=[&b%s&7]",
        					taskMode.name(), mode ));
        	return;
        }

//        if ( mode == null || !"sync".equalsIgnoreCase( mode ) && !"inline".equalsIgnoreCase( mode ) ) {
//        	sender.sendMessage( 
//        			String.format("&7Please provide a valid mode for running the commands. " +
//        					"[inline, sync]  mode=[&b%s&7]",
//        					mode ));
//        	return;
//        }
        
        
        setLastMineReferenced(mineName);
        
        PrisonMines pMines = PrisonMines.getInstance();
//    	MineManager mMan = pMines.getMineManager();
        Mine m = pMines.getMine(mineName);
        
        if ( row > m.getBlockEvents().size() ) {
        	sender.sendMessage( 
        			String.format("&7Please provide a valid row number no greater than &b%d&7. " +
        					"Was row=[&b%d&7]",
        					m.getBlockEvents().size(), (row == null ? "null" : row) ));
        	return;        	
        }
        
        MineBlockEvent blockEvent = m.getBlockEvents().get( row - 1 );

        TaskMode taskModeOld = blockEvent.getTaskMode();
        
        blockEvent.setTaskMode( taskMode );

        pMines.getMineManager().saveMine( m );
        
        
        Output.get().sendInfo(sender, "&7BlockEvent task mode &b%s&7 was changed for mine '&b%s&7'. " +
        		"Was &b%s&7. Command '&b%s&7'", 
        		taskMode, m.getTag(), taskModeOld.name(), blockEvent.getCommand() );

        
        // Redisplay the event list:
        blockEventList( sender, mineName );

    }



	@Command(identifier = "mines blockEvent block add", 
						description = "Adds a block filter to a BlockBreak task. " +
								"Omit the row number to display all of the available block events for" +
								"the selected mine. " +
								"Omit the block name to disaply a list of all the available blocks for " +
								"the selected mine.", 
						onlyPlayers = false, permissions = "mines.set")
    public void blockEventBlockAdd(CommandSender sender, 
    			@Arg(name = "mineName") String mineName,
    			@Arg(name = "rowBlockEvent", def = "0", description = "Row number of the blockEvent to " +
    					"add the block filter to. If not provided, or value of 0, then " +
    					"this command will display a list of all current blockEvents for " +
    					"this mine.") Integer rowBlockEvent,

    			// search is no longer needed nor is the wildcard join for blockName:
//    			@Arg(name = "search", description = "Optional keyword 'search' to search " +
//    					"based upon value of blockName. [search, none, <blank>]",
//    					def = "") String search,
//    			@Wildcard(join=true)
    	        @Arg(name = "rowBlockName", description = "Row number of the block to add, or " +
    	        		"if ommitted, then it will show a list of all of the blocks that " +
    	        		"are available within the selected mine.",
    					def = "") Integer rowBlockName
    			) {

        if (!performCheckMineExists(sender, mineName)) {
            return;
        }
        
        setLastMineReferenced(mineName);
        
        PrisonMines pMines = PrisonMines.getInstance();
//    	MineManager mMan = pMines.getMineManager();
        Mine m = pMines.getMine(mineName);
        
        
        String commandRoot = String.format( "" +
				"/mines blockEvent block add %s ", m.getName() );
        
        /// if row is less than 1, then we need to display a list of BlockEvents:
        if ( rowBlockEvent == null || rowBlockEvent <= 0 ) {

            ChatDisplay display = new ChatDisplay("Add blocks to a BlockEvent for " + m.getTag() );
            display.addText("&8Hover over values for more information and clickable actions.");

            // Generates a blockEvent listing for the given selected mine:
            generateBlockEventListing( m, display, false );
            
            
            display.addText( "&7Select a BlockEvent by row number to add a block" );
            
            // try to "suggest" reading this command: 
            // mines blockEvent block add [row] [search} [block]
        	FancyMessage msgAddBlock = new FancyMessage( String.format( 
        									"&7%s [rowBlockEvent] [rowBlockName]", 
        										commandRoot ) )
					.suggest( commandRoot + " [rowBlockEvent] [rowBlockName]" )
					.tooltip("Add block to blockEvent - Click to Add");
            
        	RowComponent rowFancy = new RowComponent();
        	rowFancy.addFancy( msgAddBlock );
        	display.addComponent( rowFancy );

            display.send( sender );
     
        	return;        	
        }
        
        
        if ( rowBlockEvent > m.getBlockEvents().size() ) {
        	sender.sendMessage( 
        			String.format("&7Please provide a valid row number no greater than &b%d&7. " +
        					"Was rowBlockEvent=[&b%d&7]",
        					m.getBlockEvents().size(), (rowBlockEvent == null ? "null" : rowBlockEvent) ));
        	return;        	
        }
        

        // We have the row number, so now get the BlockEvent:
        MineBlockEvent blockEvent = m.getBlockEvents().get( rowBlockEvent - 1 );

        if ( blockEvent != null ) {
        	
        	if ( rowBlockName == null || rowBlockName == 0 || 
        						rowBlockName > m.getBlockEvents().size() ) {
        		
        		String commandBlockEvent = String.format( "" +
        				"%s %d ", commandRoot, rowBlockEvent );

        		ChatDisplay display = new ChatDisplay("Add blocks to a BlockEvent for " + m.getTag() );
                display.addText("&8Select a block from this mine by using the block's row number:");
                display.addText("&8  " + commandBlockEvent + " [rowBlockName]");
                
                DecimalFormat dFmt = new DecimalFormat("0.00000");
                
        		// Display a list of blocks for the mine:
        		int blockRow = 0;
        		
        		// Old block model is not supported with blockEvent block filers:
        		if ( m.isUseNewBlockModel() ) {
        			
        			for ( PrisonBlock block : m.getPrisonBlocks() )
        			{
        	        	
        	        	RowComponent rowB = new RowComponent();
        	        	
        	        	rowB.addTextComponent( " &3Row: &d%d  ", ++blockRow );
        	        	
        	        	String message = String.format( "&7%s %s", 
        	        			block.getBlockName(), dFmt.format( block.getChance() ) );
        	        	
        	        	String command = String.format( "%s %d", commandBlockEvent, blockRow );
        	        	
        	        	FancyMessage msgAddBlock = new FancyMessage( message )
								.suggest( command )
								.tooltip("Add selected block to blockEvent - Click to Add");
        	        	
        	        	rowB.addFancy( msgAddBlock );
        	        	
        	        	display.addComponent( rowB );
        			}
        		}

        		display.send( sender );
        		return;
        	}
        	
        	
        	// Old block model is not supported with blockEvent block filers:
        	if ( m.isUseNewBlockModel() ) {
        		PrisonBlock block = m.getPrisonBlocks().get( rowBlockName - 1 );
        		
        		if ( block != null ) {

        			blockEvent.addPrisonBlock( block );
        			
        			pMines.getMineManager().saveMine( m );
        			
        			sender.sendMessage( "Block has been added to BlockEvent" );
        			
        			return;
        		}
        	}
        	
//        	PrisonBlockTypes prisonBlockTypes = Prison.get().getPlatform().getPrisonBlockTypes();
//        	PrisonBlock block = prisonBlockTypes.getBlockTypesByName( blockName );
 
        	
        }
        else {
        	sender.sendMessage( "BlockEvent was not found" );
        	// BlockEvent not found. Recheck the blockEven row number.
        }

        // Redisplay the event list:
 //       blockEventList( sender, mineName );

        sender.sendMessage( "BlockEvent was not completed correctly" );
    }
	
	


	@Command(identifier = "mines blockEvent block remove", 
						description = "Removes a block filter from a BlockBreak task. " +
								"Omit the row number to display all of the available block events for" +
								"the selected mine. " +
								"Omit the rowBlockName to disaply a list of all the blocks filters for " +
								"this task.", 
						onlyPlayers = false, permissions = "mines.set")
    public void blockEventBlockRemove(CommandSender sender, 
    			@Arg(name = "mineName") String mineName,
    			@Arg(name = "rowBlockEvent", def = "0", description = "Row number of the blockEvent to " +
    					"add the block filter to. If not provided, or value of 0, then " +
    					"this command will display a list of all current blockEvents for " +
    					"this mine.") Integer rowBlockEvent,

    			// search is no longer needed nor is the wildcard join for blockName:
//    			@Arg(name = "search", description = "Optional keyword 'search' to search " +
//    					"based upon value of blockName. [search, none, <blank>]",
//    					def = "") String search,
//    			@Wildcard(join=true)
    	        @Arg(name = "rowBlockName", description = "Name of block to add, or " +
    	        		"if ommitted, then it will show a list of all of the blocks that " +
    	        		"are available within the selected mine.",
    					def = "") Integer rowBlockName
    			) {

        if (!performCheckMineExists(sender, mineName)) {
            return;
        }
        
        setLastMineReferenced(mineName);

        
        PrisonMines pMines = PrisonMines.getInstance();
//    	MineManager mMan = pMines.getMineManager();
        Mine m = pMines.getMine(mineName);

        
        String commandRoot = String.format( "" +
				"/mines blockEvent block remove %s ", m.getName() );
        
        
        /// if row is less than 1, then we need to display a list of BlockEvents:
        if ( rowBlockEvent == null || rowBlockEvent <= 0 ) {
        	

            ChatDisplay display = new ChatDisplay("Remove block from a BlockEvent for " + m.getTag() );
            display.addText("&8Hover over values for more information and clickable actions.");

            // Generates a blockEvent listing for the given selected mine:
            generateBlockEventListing( m, display, false );
            
            
            display.addText( "&7Select a BlockEvent by row number to remove a block" );
            
            // try to "suggest" reading this command: 
            // mines blockEvent block add [row] [search} [block]
        	FancyMessage msgRemoveBlock = new FancyMessage( String.format( 
        									"&7%s [rowBlockEvent] [rowBlockName]", 
        										commandRoot ) )
					.suggest( commandRoot + " [rowBlockEvent] [rowBlockName]" )
					.tooltip("Remove a block from a blockEvent - Click to Remove");
            
        	RowComponent rowFancy = new RowComponent();
        	rowFancy.addFancy( msgRemoveBlock );
        	display.addComponent( rowFancy );
        	
            display.send( sender );
     
        	return;        	
        }
        
        
        if ( rowBlockEvent > m.getBlockEvents().size() ) {
        	sender.sendMessage( 
        			String.format("&7Please provide a valid row number no greater than &b%d&7. " +
        					"Was rowBlockEvent=[&b%d&7]",
        					m.getBlockEvents().size(), (rowBlockEvent == null ? "null" : rowBlockEvent) ));
        	return;        	
        }
        
        

        // We have the row number, so now get the BlockEvent:
        MineBlockEvent blockEvent = m.getBlockEvents().get( rowBlockEvent - 1 );

        if ( blockEvent != null ) {
        	
        	if ( rowBlockName == null || rowBlockName == 0 || 
        						rowBlockName > m.getBlockEvents().size() ) {
        		
        		String commandBlockEvent = String.format( "" +
        				"%s %d ", commandRoot, rowBlockEvent );

        		ChatDisplay display = new ChatDisplay("Remove a block from a BlockEvent for " + m.getTag() );
                display.addText("&8Select a block filter from this mine by using the block's row number:");
                display.addText("&8  " + commandBlockEvent + " [rowBlockName]");
                
//                DecimalFormat dFmt = new DecimalFormat("0.00000");
                
        		// Display a list of blocks for the mine:
        		int blockRow = 0;
        		
        		// Old block model is not supported with blockEvent block filers:
        		if ( m.isUseNewBlockModel() ) {
        			
        			for ( PrisonBlock block : blockEvent.getPrisonBlocks() )
        			{
        	        	
        	        	RowComponent rowB = new RowComponent();
        	        	
        	        	rowB.addTextComponent( " &3Row: &d%d  ", ++blockRow );
        	        	
        	        	String message = String.format( "&7%s", 
        	        			block.getBlockName() );
//        	        	String message = String.format( "&7%s %s", 
//        	        			block.getBlockName(), dFmt.format( block.getChance() ) );
        	        	
        	        	String command = String.format( "%s %d", commandBlockEvent, blockRow );
        	        	
        	        	FancyMessage msgAddBlock = new FancyMessage( message )
								.suggest( command )
								.tooltip("Remove a selected block from a blockEvent - Click to Remove");
        	        	
        	        	rowB.addFancy( msgAddBlock );
        	        	
        	        	display.addComponent( rowB );
        			}
        		}

        		display.send( sender );
        		return;
        	}
        	
        	
        	// Old block model is not supported with blockEvent block filers:
        	if ( m.isUseNewBlockModel() ) {
        		
        		if ( blockEvent.removePrisonBlock( rowBlockName ) ) {

        			pMines.getMineManager().saveMine( m );
        			
        			sender.sendMessage( "Block has been removed from the BlockEvent" );
        			
        			return;
        		}
        	}
        	
//        	PrisonBlockTypes prisonBlockTypes = Prison.get().getPlatform().getPrisonBlockTypes();
//        	PrisonBlock block = prisonBlockTypes.getBlockTypesByName( blockName );
        	
        }
        else {
        	sender.sendMessage( "BlockEvent was not found" );
        	// BlockEvent not found. Recheck the blockEven row number.
        	
        	return;
        }

        
        // Redisplay the event list:
 //       blockEventList( sender, mineName );

        sender.sendMessage( "BlockEvent was not completed correctly" );
    }
	

	
//	private String extractSearchValue( String page, String blockName ) {
//		String results = blockName;
//		if ( blockName.toLowerCase().startsWith( page.toLowerCase() ) ) {
//			results = blockName.substring( page.length() ).trim();
//		}
//		
//		return results;
//	}

//	private String extractPage( String blockName ) {
//		String page = "1";
//		
//		if ( blockName != null && blockName.toLowerCase().startsWith( "all " ) ) {
//			page = "all";
//		}
//		else if ( blockName!= null && blockName.contains( " " ) ) {
//					
//			try {
//				String pageStr = blockName.substring( 0, blockName.indexOf( " " ) );
//				
//				int pg = Integer.parseInt( pageStr );
//				
//				page = Integer.toString( pg );
//			}
//			catch ( NumberFormatException e ) {
//			}
//		}
//		
//		return page;
//	}


	
	
	


	@Command(identifier = "mines command list", description = "Lists the commands for a mine.", 
    						onlyPlayers = false, permissions = "mines.command")
    public void commandList(CommandSender sender, 
    				@Arg(name = "mineName") String mineName) {
    	
//    	if ( 1 < 2 ) {
//    		sender.sendMessage( "&cThis command is disabled&7. It will be enabled in the near future." );
//    		return;
//    	}
    	
        if (!performCheckMineExists(sender, mineName)) {
            return;
        }

        
        setLastMineReferenced(mineName);
        
        PrisonMines pMines = PrisonMines.getInstance();
//    	MineManager mMan = pMines.getMineManager();
        Mine m = pMines.getMine(mineName);
        
        if (m.getResetCommands() == null || m.getResetCommands().size() == 0) {
            Output.get().sendInfo(sender, "The mine '%s' contains no commands.", m.getTag());
            return;
        }


        ChatDisplay display = new ChatDisplay("ResetCommand for " + m.getName());
        display.addText("&8Click a command to remove it.");
        BulletedListComponent.BulletedListBuilder builder =
            new BulletedListComponent.BulletedListBuilder();

        int rowNumber = 1;
        for (String command : m.getResetCommands()) {
        	
        	
        	RowComponent row = new RowComponent();
        	
        	row.addTextComponent( " &3Row: &d%d  ", rowNumber++ );
        	
        	FancyMessage msg = new FancyMessage( "&a'&7" + command + "&a'" );
            row.addFancy( msg );
            
        	FancyMessage msgRemove = new FancyMessage( " &4Remove&3" )
        			.suggest("/mines command remove " + m.getName() + " " + rowNumber )
        			.tooltip("Click to Remove this Mine Command");
        	row.addFancy( msgRemove );
	
            builder.add( row );

        }

        display.addComponent(builder.build());
        display.addComponent(new FancyMessageComponent(
            new FancyMessage("&7[&a+&7] Add").suggest("/mines command add " + mineName + " /")
                .tooltip("&7Add a new command.")));
        display.send(sender);
    }


	@Command(identifier = "mines command remove", description = "Removes a command from a mine.", 
    		onlyPlayers = false, permissions = "mines.command")
    public void commandRemove(CommandSender sender, 
    				@Arg(name = "mineName") String mineName,
    				@Arg(name = "row", 
							description = "The row number of the command to remove.") 
							Integer row) {
    	
    	
        if ( row == null || row <= 0 ) {
        	sender.sendMessage( 
        			String.format("&7Please provide a valid row number greater than zero. " +
        					"Was row=[&b%d&7]",
        					(row == null ? "null" : row) ));
        	return;        	
        }

        if (!performCheckMineExists(sender, mineName)) {
            return;
        }
        
        setLastMineReferenced(mineName);
        
        PrisonMines pMines = PrisonMines.getInstance();
//    	MineManager mMan = pMines.getMineManager();
        Mine m = pMines.getMine(mineName);
        
//        if ( !m.isEnabled() ) {
//        	sender.sendMessage( "&cMine is disabled&7. Use &a/mines info &7for possible cause." );
//        	return;
//        }
        
        if (m.getResetCommands() == null || m.getResetCommands().size() == 0) {
            Output.get().sendInfo(sender, "The mine '%s' contains no commands.", m.getTag());
            return;
        }


        if (m.getResetCommands() == null) {
        	m.setResetCommands( new ArrayList<>() );
        }

        if ( row > m.getResetCommands().size() ) {
        	sender.sendMessage( 
        			String.format("&7Please provide a valid row number no greater than &b%d&7. " +
        					"Was row=[&b%d&7]",
        					m.getResetCommands().size(), (row == null ? "null" : row) ));
        	return;        	
        }


        String oldCommand = m.getResetCommands().remove( (int) row - 1);
        if ( oldCommand != null ) {
        	
        	pMines.getMineManager().saveMine( m );
            	
        	Output.get().sendInfo(sender, "Removed command '%s' from the mine '%s'.", 
        				oldCommand, m.getTag());
        } 
        else {
        	Output.get().sendWarn(sender, 
        			String.format("The mine %s doesn't contain that command. Nothing was changed.", 
        						m.getTag()));
        }
    }

	@Command(identifier = "mines command add", description = "Adds a command to a mine with NO placeholders.", 
    		onlyPlayers = false, permissions = "mines.command")
    public void commandAdd(CommandSender sender, 
    			@Arg(name = "mineName", description = "mine name, or 'placeholders' for a list of possible placeholders that " +
    					"you can use with blockEvents") String mineName,
    			@Arg(name = "state", def = "before", description = "State can be either before or after.") String state,
    			@Arg(name = "command") @Wildcard String command) {
  
		
		
        if ( mineName != null && "placeholders".equalsIgnoreCase( mineName ) ) {
        	
        	String placeholders = 
        			
        			PrisonCommandTask.CustomPlaceholders.listPlaceholders(
        					PrisonCommandTask.CommandEnvironment.all_commands ) + " " +
        			
        			PrisonCommandTask.CustomPlaceholders.listPlaceholders(
									PrisonCommandTask.CommandEnvironment.mine_commands );
        	
        	String message = String.format( "Valid Placeholders that can be used with mine commands: [%s]", 
        							placeholders );
        	
        	Output.get().logInfo( message );
        	return;
        }

    	if (command.startsWith("/")) {
            command = command.replaceFirst("/", "");
        }

        if (!performCheckMineExists(sender, mineName)) {
            return;
        }
        
        if ( state == null || !state.equalsIgnoreCase( "before" ) && !state.equalsIgnoreCase( "after" )) {
        	sender.sendMessage( 
        			String.format("&7Please provide a valid state: &bbefore&7 or &bafter&7. Was state=[&b%s&7]",
        			state ));
        	return;
        }
        
        setLastMineReferenced(mineName);
        
        PrisonMines pMines = PrisonMines.getInstance();
//    	MineManager mMan = pMines.getMineManager();
        Mine m = pMines.getMine(mineName);
        
        if ( command == null || command.trim().length() == 0 ) {
        	sender.sendMessage( 
        			String.format( "&7Please provide a valid command: command=[%s]", command) );
        	return;
        }
        
        String newComand = state + ": " + command;
        m.getResetCommands().add(newComand);

        pMines.getMineManager().saveMine( m );
        
        Output.get().sendInfo(sender, "&7Added command '&b%s&7' to the mine '&b%s&7'.", 
        		newComand, m.getTag());

    }
    
}
