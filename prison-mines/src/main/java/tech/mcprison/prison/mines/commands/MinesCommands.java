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

import org.apache.commons.lang3.StringUtils;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.chat.FancyMessage;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.BaseCommands;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.commands.CommandPagedData;
import tech.mcprison.prison.commands.Wildcard;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Block;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.data.MineData;
import tech.mcprison.prison.mines.data.MineData.MineNotificationMode;
import tech.mcprison.prison.mines.data.MineLinerBuilder;
import tech.mcprison.prison.mines.data.MineLinerBuilder.LinerPatterns;
import tech.mcprison.prison.mines.data.PrisonSortableResults;
import tech.mcprison.prison.mines.managers.MineManager;
import tech.mcprison.prison.mines.managers.MineManager.MineSortOrder;
import tech.mcprison.prison.modules.ModuleElement;
import tech.mcprison.prison.modules.ModuleElementType;
import tech.mcprison.prison.output.BulletedListComponent;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.FancyMessageComponent;
import tech.mcprison.prison.output.LogLevel;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.output.RowComponent;
import tech.mcprison.prison.selection.Selection;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.Bounds.Edges;
import tech.mcprison.prison.util.MaterialType;
import tech.mcprison.prison.util.Text;

/**
 * @author Dylan M. Perks
 */
public class MinesCommands
	extends BaseCommands {
	
	public MinesCommands() {
		super( "MinesCommands" );
	}
	
	private Long confirmTimestamp;
	
	private String lastMineReferenced;
	private Long lastMineReferencedTimestamp;
	

    private boolean performCheckMineExists(CommandSender sender, String mineName) {
    	mineName = Text.stripColor( mineName );
        if (PrisonMines.getInstance().getMine(mineName) == null) {
            PrisonMines.getInstance().getMinesMessages().getLocalizable("mine_does_not_exist")
                .sendTo(sender);
            return false;
        }
        return true;
    }

    @Command(identifier = "mines create", description = "Creates a new mine, or even a virtual mine.", 
    		onlyPlayers = false, permissions = "mines.create")
    public void createCommand(CommandSender sender,
    		@Arg(name = "virtual", description = "Create a virtual mine in name only; no physical location. " +
    				"This allows the mine to be predefined before specifying the coordinates. Use [virtual]. ", def = "") 
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
        					mine.getName() ) );
        }
        else {
        	sender.sendMessage( 
        			String.format( "&cThe tag name was changed to %s for the mine %s.", 
        					tag, mine.getName() ) );
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
    					Integer.toString( mine.getSortOrder() ), mine.getName(),
    					suppressedMessage ) );
    	
    }
    
    
    @Command(identifier = "mines block add", permissions = "mines.block", onlyPlayers = false, 
    						description = "Adds a block to a mine.")
    public void addBlockCommand(CommandSender sender,
    			@Arg(name = "mineName", description = "The name of the mine to add the block to.")
            			String mineName, 
            	@Arg(name = "block", description = "The block's name or ID.") 
    					String block,
            	@Arg(name = "chance", description = "The percent chance (out of 100) that this block will occur.")
    					double chance) {
        if (!performCheckMineExists(sender, mineName)) {
            return;
        }

        PrisonMines pMines = PrisonMines.getInstance();
        
        setLastMineReferenced(mineName);
        
        Mine m = pMines.getMine(mineName);
        
        // should be able manage blocks even if disabled or virtual:
//        if ( !m.isEnabled() ) {
//        	sender.sendMessage( "&cMine is disabled&7. Use &a/mines info &7for possible cause." );
//        	return;
//        }
        
        if ( Prison.get().getPlatform().getConfigBooleanFalse( "use-new-prison-block-model" ) ) {
        	
        	block = block == null ? null : block.trim().toLowerCase();
        	PrisonBlock prisonBlock = null;
        	
        	if ( block != null && Prison.get().getPrisonBlockTypes().getBlockTypesByName().containsKey( block ) ) {
        		prisonBlock = Prison.get().getPrisonBlockTypes().getBlockTypesByName().get( block );
        	}
        	
        	if ( prisonBlock == null ) {
        		pMines.getMinesMessages().getLocalizable("not_a_block").
        					withReplacements(block).sendTo(sender);
        		return;
        	}
        	
        	
//        	if (m.isInMine(prisonBlock)) {
//        		pMines.getMinesMessages().getLocalizable("block_already_added").
//        					sendTo(sender);
//        		return;
//        	}
//        	
        	updateMinePrisonBlock( sender, m, prisonBlock, chance, pMines );
        	
        	
        

        }
        else {
        	
        	BlockType blockType = BlockType.getBlock(block);
        	
        	if (blockType == null || blockType.getMaterialType() != MaterialType.BLOCK ) {
        		pMines.getMinesMessages().getLocalizable("not_a_block")
        										.withReplacements(block).sendTo(sender);
        		return;
        	}
        	
        	if (m.isInMine(blockType)) {
        		pMines.getMinesMessages().getLocalizable("block_already_added")
        		.sendTo(sender);
        		return;
        	}
        	
        	if ( chance <= 0 ) {
        		sender.sendMessage( "The percent chance must have a value greater than zero." );
        		return;
        	}
        	
        	
        	BlockPercentTotal percentTotal = calculatePercentage( chance, blockType, m );
        	
        	if ( percentTotal.getTotalChance() > 100.0d) {
        		pMines.getMinesMessages().getLocalizable("mine_full")
        		.sendTo(sender, LogLevel.ERROR);
        		return;
        	}
        	
        	// This is an add block function so if we get this far, add it:
        	if ( percentTotal.getOldBlock() == null ) {
        		// add the block since it does not exist in the mine:
        		m.getBlocks().add( new Block( blockType, chance) );
        	} 
        	else if ( chance <= 0 ) {
        		// block exists in mine, but chance is set to zero so remove it:
        		m.getBlocks().remove( percentTotal.getOldBlock() );
        	}
        	else {
        		// update the block chance. The block in percentTotal comes from this mine
        		// so just update the chance:
        		percentTotal.getOldBlock().setChance( chance );
        	}

        	pMines.getMineManager().saveMine( m );
        	
        	pMines.getMinesMessages().getLocalizable("block_added")
        		.withReplacements(block, mineName).sendTo(sender);
        }

        getBlocksList(m, null).send(sender);

        //pMines.getMineManager().clearCache();
    }

	private void updateMinePrisonBlock( CommandSender sender, Mine m, PrisonBlock prisonBlock, double chance, PrisonMines pMines )
	{
		PrisonBlock existingPrisonBlock = m.getPrisonBlock( prisonBlock );

		if ( chance <= 0 ) {
			if ( existingPrisonBlock == null ) {
				sender.sendMessage( "The percent chance must have a value greater than zero." );
			}
			else {
				// Delete the block since it exists and the chance was set to zero:
				deleteBlock( sender, pMines, m, existingPrisonBlock );
			}
			return;
		}
		

		
		BlockPercentTotal percentTotal = calculatePercentage( chance, prisonBlock, m );
		
		if ( percentTotal.getTotalChance() > 100.0d) {
			pMines.getMinesMessages().getLocalizable("mine_full").
						sendTo(sender, LogLevel.ERROR);
			return;
		}
		
		if ( existingPrisonBlock != null ) {
			
			if ( chance <= 0 ) {
				// remove the block since it has zero chance
				m.getPrisonBlocks().remove( existingPrisonBlock );
			}
			else {
				// update chance for the prisonBlock. This block is
				// still in the mine, so just update the chance.
				existingPrisonBlock.setChance( chance );
			}
			
			pMines.getMineManager().saveMine( m );

			pMines.getMinesMessages().getLocalizable("block_set")
						.withReplacements( existingPrisonBlock.getBlockName(), m.getName()).sendTo(sender);
		}
		else {
			prisonBlock.setChance( chance );
			m.getPrisonBlocks().add( prisonBlock );

			pMines.getMineManager().saveMine( m );

			pMines.getMinesMessages().getLocalizable("block_added")
						.withReplacements(prisonBlock.getBlockName(), m.getName()).sendTo(sender);
		}
	}

    @Command(identifier = "mines block set", permissions = "mines.block", onlyPlayers = false, 
    					description = "Changes the percentage of a block in a mine.")
    public void setBlockCommand(CommandSender sender,
    			@Arg(name = "mineName", description = "The name of the mine to edit.") 
    					String mineName,
    			@Arg(name = "block", description = "The block's name or ID.") 
    					String block,
    			@Arg(name = "chance", description = "The percent chance (out of 100) that this block will occur.") 
    					double chance) {
    	
        if (!performCheckMineExists(sender, mineName)) {
            return;
        }

        setLastMineReferenced(mineName);
        
        PrisonMines pMines = PrisonMines.getInstance();
        Mine m = pMines.getMine(mineName);

        // you should be able to configure virtual and disabled mines
//        if ( !m.isEnabled() ) {
//        	sender.sendMessage( "&cMine is disabled&7. Use &a/mines info &7for possible cause." );
//        	return;
//        }
        
        if ( Prison.get().getPlatform().getConfigBooleanFalse( "use-new-prison-block-model" ) ) {
        
        	
        	block = block == null ? null : block.trim().toLowerCase();
        	PrisonBlock prisonBlock = null;
        	
        	if ( block != null && Prison.get().getPrisonBlockTypes().getBlockTypesByName().containsKey( block ) ) {
        		prisonBlock = Prison.get().getPrisonBlockTypes().getBlockTypesByName().get( block );
        	}
        	
        	
        	
        	// Change behavior: If trying to change a block that is not in the mine, then instead add it:
        	if (!m.isInMine(prisonBlock)) {
        		addBlockCommand( sender, mineName, block, chance );
//        	pMines.getMinesMessages().getLocalizable("block_not_removed")
//                .sendTo(sender);
        		return;
        	}
        	
        	updateMinePrisonBlock( sender, m, prisonBlock, chance, pMines );
        	

//        	// If it's 0, just delete it!
//        	if (chance <= 0.0d) {
//        		deleteBlock( sender, pMines, m, prisonBlock );
////            delBlockCommand(sender, mine, block);
//        		return;
//        	}
//        	
//        	
//        	double totalChance = chance;
//        	PrisonBlock blockToUpdate = null;
//        	for ( PrisonBlock blk : m.getPrisonBlocks() ) {
//				if ( blk.getBlockName().equalsIgnoreCase( prisonBlock.getBlockName() ) ) {
//					totalChance -= blk.getChance();
//					blockToUpdate = blk;
//				}
//				else {
//					totalChance += blk.getChance();
//				}
//			}
//        	
//        	if (totalChance > 100.0d) {
//        		pMines.getMinesMessages().getLocalizable("mine_full").
//        					sendTo(sender, LogLevel.ERROR);
//        		return;
//        	}
//        	
//        	blockToUpdate.setChance( chance );
//        	
//        	// total chance is not being calculated correctly...
//        	
//        	final double[] totalComp = {chance};
//        	m.getPrisonBlocks().forEach(block1 -> {
//        		totalComp[0] -= block1.getChance();
//        	});
//
//        	if (totalComp[0] > 100.0d) {
//        		pMines.getMinesMessages().getLocalizable("mine_full")
//        		.sendTo(sender, Localizable.Level.ERROR);
//        		return;
//        	}
//        	
//        	for (PrisonBlock blockObject : m.getPrisonBlocks()) {
//        		if (blockObject.getBlockName().equalsIgnoreCase( prisonBlock.getBlockName() )) {
//        			blockObject.setChance(chance);
//        		}
//        	}
        
        }
        else {
        	
        	BlockType blockType = BlockType.getBlock(block);
        	if (blockType == null) {
        		pMines.getMinesMessages().getLocalizable("not_a_block").
        								withReplacements(block).sendTo(sender);
        		return;
        	}
        	
        	// Change behavior: If trying to change a block that is not in the mine, then instead add it:
        	if (!m.isInMine(blockType)) {
        		addBlockCommand( sender, mineName, block, chance );
//        	pMines.getMinesMessages().getLocalizable("block_not_removed")
//                .sendTo(sender);
        		return;
        	}
        	
        	// If it's 0, just delete it! If the block is not in the mine, then nothing will happen.
        	if (chance <= 0.0d) {
        		deleteBlock( sender, pMines, m, blockType );
//            delBlockCommand(sender, mine, block);
        		return;
        	}
        	
        	
        	BlockPercentTotal percentTotal = calculatePercentage( chance, blockType, m );
        	
        	
        	if ( percentTotal.getTotalChance() > 100.0d) {
        		pMines.getMinesMessages().getLocalizable("mine_full").
        					sendTo(sender, LogLevel.ERROR);
        		return;
        	}
        	
        	// Block would have been added or deleted above, so if it gets here, then 
        	// just update the block that's in the mine, which is stored in the percentTotal
        	// result object:
        	percentTotal.getOldBlock().setChance( chance );
        	
        	
        	pMines.getMineManager().saveMine( m );
        	
        	pMines.getMinesMessages().getLocalizable("block_set")
        	.withReplacements(block, mineName).sendTo(sender);
        }
        
        
        getBlocksList(m, null).send(sender);

        //pMines.getMineManager().clearCache();

    }
    
    
    private BlockPercentTotal calculatePercentage( double chance, BlockType blockType, Mine m ) {
    	BlockPercentTotal results = new BlockPercentTotal();
    	results.addChance( chance );

    	for ( Block block : m.getBlocks() ) {
			if ( block.getType() == blockType ) {
				// do not replace the block's chance since this may fail
				results.setOldBlock( block );
			}
			else {
				results.addChance( block.getChance() );
			}
		}
    	
    	return results;
    }
    
    private BlockPercentTotal calculatePercentage( double chance, PrisonBlock prisonBlock, Mine m ) {
    	BlockPercentTotal results = new BlockPercentTotal();
    	results.addChance( chance );
    	
    	for ( PrisonBlock block : m.getPrisonBlocks() ) {
    		if ( block.equals( prisonBlock ) ) {
    			// do not replace the block's chance since this may fail
    			results.setPrisonBlock( block );
    		}
    		else {
    			results.addChance( block.getChance() );
    		}
    	}
    	
    	if ( results.getPrisonBlock() == null ) {
    		prisonBlock.setChance( chance );
    		results.setPrisonBlock( prisonBlock );
    	}
    	return results;
    }
    
    protected class BlockPercentTotal {
    	private double totalChance = 0d;
    	private Block oldBlock = null;
    	private PrisonBlock prisonBlock = null;
    	
    	public BlockPercentTotal() {
    	}

    	public void addChance( double chance ) {
    		this.totalChance += chance;
    	}
		public double getTotalChance() {
			return totalChance;
		}
		public void setTotalChance( double totalChance ) {
			this.totalChance = totalChance;
		}

		public Block getOldBlock() {
			return oldBlock;
		}
		public void setOldBlock( Block oldBlock ) {
			this.oldBlock = oldBlock;
		}

		public PrisonBlock getPrisonBlock() {
			return prisonBlock;
		}
		public void setPrisonBlock( PrisonBlock prisonBlock ) {
			this.prisonBlock = prisonBlock;
		}
		
    }
    

    @Command(identifier = "mines block remove", permissions = "mines.block", onlyPlayers = false, description = "Deletes a block from a mine.")
    public void delBlockCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to edit.") String mineName,
        @Arg(name = "block", def = "AIR", description = "The block's name or ID.") String block) {

        if (!performCheckMineExists(sender, mineName)) {
            return;
        }

        setLastMineReferenced(mineName);
        
        PrisonMines pMines = PrisonMines.getInstance();
        Mine m = pMines.getMine(mineName);
        
        
        if ( Prison.get().getPlatform().getConfigBooleanFalse( "use-new-prison-block-model" ) ) {
        
        	
        	block = block == null ? null : block.trim().toLowerCase();
        	PrisonBlock prisonBlock = null;
        	
        	if ( block != null && Prison.get().getPrisonBlockTypes().getBlockTypesByName().containsKey( block ) ) {
        		prisonBlock = Prison.get().getPrisonBlockTypes().getBlockTypesByName().get( block );
        	}
        	
        	// Cannot delete a block if it does not exist:
//        	if (!m.isInMine(prisonBlock)) {
//        		return;
//        	}
        	
        	// make sure the deleteBlock is deleting the actual block stored in the mine:
        	PrisonBlock preexistingPrisonBlock = m.getPrisonBlock( prisonBlock );
        	
        	if ( preexistingPrisonBlock != null ) {
        		
        		deleteBlock( sender, pMines, m, preexistingPrisonBlock );
        	}
        	
        }
        else {
        	
        	BlockType blockType = BlockType.getBlock(block);
        	if (blockType == null) {
        		pMines.getMinesMessages().getLocalizable("not_a_block")
        		.withReplacements(block).sendTo(sender);
        		return;
        	}
        	
        	if (!m.isInMine(blockType)) {
        		pMines.getMinesMessages().getLocalizable("block_not_removed")
        		.sendTo(sender);
        		return;
        	}
        	
        	deleteBlock( sender, pMines, m, blockType );
        }
        
        getBlocksList(m, null).send(sender);
    }

    /**
     * Delete only the first occurrence of a block with the given BlockType.
     * 
     * @param sender
     * @param pMines
     * @param m
     * @param prisonBlock
     */
	private void deleteBlock( CommandSender sender, PrisonMines pMines, Mine m, PrisonBlock prisonBlock )
	{
		PrisonBlock rBlock = null;
		for ( PrisonBlock block : m.getPrisonBlocks() ) {
			if (block.getBlockName().equalsIgnoreCase( prisonBlock.getBlockName() )) {
				rBlock = block;
				break;
    		}
		}
		if ( m.getPrisonBlocks().remove( rBlock )) {
			pMines.getMineManager().saveMine( m );
			
			pMines.getMinesMessages().getLocalizable("block_deleted").
						withReplacements(prisonBlock.getBlockName(), m.getName()).sendTo(sender);
		}
	}
	/**
	 * Delete only the first occurrence of a block with the given BlockType.
	 * 
	 * @param sender
	 * @param pMines
	 * @param m
	 * @param blockType
	 */
	private void deleteBlock( CommandSender sender, PrisonMines pMines, Mine m, BlockType blockType )
	{
		Block rBlock = null;
		for ( Block block : m.getBlocks() ) {
			if ( block.getType() ==  blockType ) {
				rBlock = block;
				break;
			}
		}
		if ( m.getBlocks().remove( rBlock )) {
			pMines.getMineManager().saveMine( m );
			
			pMines.getMinesMessages().getLocalizable("block_deleted")
			.withReplacements(blockType.name(), m.getName()).sendTo(sender);
		}
	}

    @Command(identifier = "mines block search", permissions = "mines.block", 
    				description = "Searches for a block to add to a mine.")
    public void searchBlockCommand(CommandSender sender,
        @Arg(name = "search", def = " ", description = "Any part of the block's name or ID.") String search,
        @Arg(name = "page", def = "1", description = "Page of search results (optional)") String page ) {

    	PrisonMines pMines = PrisonMines.getInstance();
    	if (search == null)
    	{
    		pMines.getMinesMessages().getLocalizable("block_search_blank").sendTo(sender);
    	}
    	
    	ChatDisplay display = null;
    	
        if ( Prison.get().getPlatform().getConfigBooleanFalse( "use-new-prison-block-model" ) ) {
            
        	display = prisonBlockSearchBuilder(search, page);
        }
        else {
        	
        	display = blockSearchBuilder(search, page);
        }
        
        display.send(sender);

        //pMines.getMineManager().clearCache();
    }
    
    private ChatDisplay prisonBlockSearchBuilder(String search, String page)
    {
    	List<PrisonBlock> blocks = new ArrayList<>();
    	
    	for ( PrisonBlock pBlock : Prison.get().getPrisonBlockTypes().getBlockTypes() ) {
    		if ( pBlock.isBlock() && pBlock.getBlockName().contains( search.toLowerCase()  )) {
    			blocks.add( pBlock );
    		}
    	}
    	
    	CommandPagedData cmdPageData = new CommandPagedData(
    			"/mines block search " + search, blocks.size(),
    			0, page );
    	
    	// Same page logic as in mines info
//    	int curPage = 1;
//    	int pageSize = 10;
//    	int pages = (blocks.size() / pageSize) + 1;
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
//    	int pageEnd = ((pageStart + pageSize) > blocks.size() ? blocks.size() : pageStart + pageSize);
    	
    	
    	ChatDisplay display = new ChatDisplay("Block Search (" + blocks.size() + ")");
    	display.text("&8Click a block to add it to a mine.");
    	
    	BulletedListComponent.BulletedListBuilder builder =
    			new BulletedListComponent.BulletedListBuilder();
    	for ( int i = cmdPageData.getPageStart(); i < cmdPageData.getPageEnd(); i++ )
    	{
    		PrisonBlock block = blocks.get(i);
    		FancyMessage msg =
    				new FancyMessage(
    						String.format("&7%s %s", 
    								Integer.toString(i), block.getBlockName()
//    								block.getAltName(),
    								))
    				.suggest("/mines block add " + getLastMineReferenced() + 
    									" " + block.getBlockName() + " %")
    				.tooltip("&7Click to add block to a mine.");
    		builder.add(msg);
    	}
    	display.addComponent(builder.build());
    	
    	// This command plus parameters used:
//        String pageCmd = "/mines block search " + search;
    	
    	cmdPageData.generatePagedCommandFooter( display );
    	
    	return display;
    }

	private ChatDisplay blockSearchBuilder(String search, String page)
	{
		List<BlockType> blocks = new ArrayList<>();
    	for (BlockType block : BlockType.values())
		{
			if ( block.getMaterialType() == MaterialType.BLOCK && 
					(block.getId().contains(search.toLowerCase()) || 
					block.name().toLowerCase().contains(search.toLowerCase())) )
			{
				blocks.add(block);
			}
		}
    	
        
        CommandPagedData cmdPageData = new CommandPagedData(
        		"/mines block search " + search, blocks.size(),
        		0, page );
    	
    	// Same page logic as in mines info
//    	int curPage = 1;
//    	int pageSize = 10;
//    	int pages = (blocks.size() / pageSize) + 1;
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
//    	int pageEnd = ((pageStart + pageSize) > blocks.size() ? blocks.size() : pageStart + pageSize);

    	
        ChatDisplay display = new ChatDisplay("Block Search (" + blocks.size() + ")");
        display.text("&8Click a block to add it to a mine.");
        
        BulletedListComponent.BulletedListBuilder builder =
        						new BulletedListComponent.BulletedListBuilder();
        for ( int i = cmdPageData.getPageStart(); i < cmdPageData.getPageEnd(); i++ )
        {
        	BlockType block = blocks.get(i);
            FancyMessage msg =
                    new FancyMessage(
                    		String.format("&7%s %s  (%s)%s", 
                    				Integer.toString(i), block.name(), 
                    				block.getId().replace("minecraft:", ""),
                    				(block.getMaterialVersion() == null ? "" : 
                    					"(" + block.getMaterialVersion() + ")")))
                    .suggest("/mines block add " + getLastMineReferenced() + " " + block.name() + " %")
                        .tooltip("&7Click to add block to a mine.");
                builder.add(msg);
        }
        display.addComponent(builder.build());
        
        // This command plus parameters used:
//        String pageCmd = "/mines block search " + search;
        
        cmdPageData.generatePagedCommandFooter( display );
        
		return display;
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
            display.text("&8Delete canceled.");

            display.send( sender );
            
        } else {
	    	ChatDisplay display = new ChatDisplay("&cDelete " + mineName);
	    	display.text("&8Delete confirmation failed. Try again.");
	    	
	    	display.send( sender );
	    }
        
    }

    @Command(identifier = "mines info", permissions = "mines.info", onlyPlayers = false, 
    				description = "Lists information about a mine.")
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
        
        
        if ( Prison.get().getPlatform().getConfigBooleanFalse( "use-new-prison-block-model" ) ) {
        
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

        // Display Mine Info only:
        if ( cmdPageData.getCurPage() == 1 ) {
        	
        	if ( m.isVirtual() ) {
        		chatDisplay.text("&cWarning!! This mine is &lVirtual&r&c!! &7Use &3/mines set area &7to enable." );
        	}
        	
        	if ( !m.isEnabled() ) {
        		chatDisplay.text("&cWarning!! This mine is &lDISABLED&r&c!!" );
        	}
        	
        	
        	String noTagMessag = String.format( "&7(not set) &3Will default to mine name if used." );
        	chatDisplay.text("&3Tag: &7%s", m.getTag() == null ? noTagMessag : m.getTag());
        	
        	if ( !m.isVirtual() ) {
        		String worldName = m.getWorld().isPresent() ? m.getWorld().get().getName() : "&cmissing";
        		chatDisplay.text("&3World: &7%s", worldName);
        	}
        	
        	
        	if ( m.getRank() == null ) {
        		chatDisplay.text( "&3No rank is linked to this mine." );
        	}
        	else {
        		chatDisplay.text( "&3Rank: &7%s", m.getRank() );
        	}
        	
        	
        	if ( !m.isVirtual() ) {
        		String minCoords = m.getBounds().getMin().toBlockCoordinates();
        		String maxCoords = m.getBounds().getMax().toBlockCoordinates();
        		chatDisplay.text("&3Bounds: &7%s &8to &7%s", minCoords, maxCoords);
        		Player player = getPlayer( sender );
        		
        		chatDisplay.text("&3Center: &7%s   &3%s &7%s", 
        				m.getBounds().getCenter().toBlockCoordinates(), 
        				(player == null ? "" : "Distance:"),
        				(player == null ? "" : fFmt.format( m.getBounds().getDistance3d( player.getLocation() ) ))
        				);
        		
        		
        		String spawnPoint = m.getSpawn() != null ? m.getSpawn().toBlockCoordinates() : "&cnot set";
        		chatDisplay.text("&3Spawnpoint: &7%s", spawnPoint);
        		
        		if ( mMan.isMineStats() ) {
        			RowComponent rowStats = new RowComponent();
        			rowStats.addTextComponent( "  -- &7 Stats :: " );
        			rowStats.addTextComponent( m.statsMessage() );
        			
        			chatDisplay.addComponent(rowStats);
        		}
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
        	
        	
        	
        	
//        chatDisplay.text("&3Size: &7%d&8x&7%d&8x&7%d", Math.round(m.getBounds().getWidth()),
//            Math.round(m.getBounds().getHeight()), Math.round(m.getBounds().getLength()));
        	
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
        		row.addTextComponent( "&3Skip Reset &2Enabled&3: &3Threshold: &7%s  &3Skip Limit: &7%s",
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
        
        if ( cmdPageData.isShowAll() || cmdPageData.getCurPage() > 1 ) {
        	chatDisplay.text("&3Blocks:");
        	chatDisplay.text("&8Click on a block's name to edit its chances of appearing.");
        	BulletedListComponent list = getBlocksList(m, cmdPageData);
        	
        	chatDisplay.addComponent(list);
        }
        
        int blockSize = 0;
        
        if ( Prison.get().getPlatform().getConfigBooleanFalse( "use-new-prison-block-model" ) ) {
        	blockSize = m.getPrisonBlocks().size();
        }
        else {
        	blockSize = m.getBlocks().size();
        }
        
        String message = blockSize != 0 ? null : " &cNo Blocks Defined";
        cmdPageData.generatePagedCommandFooter( chatDisplay, message );

        chatDisplay.send(sender);
    }

    private BulletedListComponent getBlocksList(Mine m, CommandPagedData cmdPageData) {
        BulletedListComponent.BulletedListBuilder builder = new BulletedListComponent.BulletedListBuilder();

        DecimalFormat dFmt = new DecimalFormat("##0.00");
        double totalChance = 0.0d;
        int count = 0;
        
        if ( Prison.get().getPlatform().getConfigBooleanFalse( "use-new-prison-block-model" ) ) {

        	for (PrisonBlock block : m.getPrisonBlocks()) {
        		double chance = Math.round(block.getChance() * 100.0d) / 100.0d;
        		totalChance += chance;
        		
        		if ( cmdPageData == null ||
        				count++ >= cmdPageData.getPageStart() && count <= cmdPageData.getPageEnd() ) {
        			String blockName = block.getBlockName().replaceAll("_", " ").toLowerCase();
        			String percent = dFmt.format(chance) + "%";
        			FancyMessage msg = new FancyMessage(String.format("&7%s - %s  (%s)", 
        					percent, block.getBlockName(), blockName))
        					.suggest("/mines block set " + m.getName() + " " + block.getBlockName() + " %")
        					.tooltip("&7Click to edit the block's chance.");
        			builder.add(msg);
        			
        		}
        	}
        }
        else {
        	
        	for (Block block : m.getBlocks()) {
        		double chance = Math.round(block.getChance() * 100.0d) / 100.0d;
        		totalChance += chance;
        		
        		if ( cmdPageData == null ||
        				count++ >= cmdPageData.getPageStart() && count <= cmdPageData.getPageEnd() ) {
        			String blockName =
        					StringUtils.capitalize(block.getType().name().replaceAll("_", " ").toLowerCase());
        			String percent = dFmt.format(chance) + "%";
        			FancyMessage msg = new FancyMessage(String.format("&7%s - %s  (%s)", 
        					percent, block.getType().name(), blockName))
        					.suggest("/mines block set " + m.getName() + " " + block.getType().name() + " %")
        					.tooltip("&7Click to edit the block's chance.");
        			builder.add(msg);
        			
        		}
        	}
        }

        if (totalChance < 100.0d) {
            builder.add("&e%s - Air", dFmt.format(100.0d - totalChance) + "%");
        }

        return builder.build();
    }

    @Command(identifier = "mines reset", permissions = "mines.reset", description = "Resets a mine.")
    public void resetCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to reset.") String mineName) {

        if (!performCheckMineExists(sender, mineName)) {
            return;
        }

        setLastMineReferenced(mineName);
        
        PrisonMines pMines = PrisonMines.getInstance();
        
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
        	m.manualReset();
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
        ChatDisplay display = new ChatDisplay("Mines");
        display.text("&8Click a mine's name to see more information.");
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

        PrisonMines pMines = PrisonMines.getInstance();
    	MineManager mMan = pMines.getMineManager();
    	
    	
    	// Get mines in the correct sorted order and suppress the mines if they should
    	PrisonSortableResults sortedMines = pMines.getMines( sortOrder );
    	
    	display.text( "&3  Mines listed: &7%s   &3Mines suppressed: &7%s",
    					sortedMines.getSortedList().size(),
    					sortedMines.getSortedSuppressedList().size());
    	
    	if ( sortedMines.getSortedSuppressedList().size() > 0 ) {
    		display.text( "&8To view suppressed mines sort by: %s", 
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
        		0, page, 7 );
        
        BulletedListComponent list = 
        		getMinesLineItemList(sortedMines, player, cmdPageData, mMan.isMineStats());
    	
    	display.addComponent(list);
    	
        
        cmdPageData.generatePagedCommandFooter( display );
        
        display.send(sender);
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
            	
            	if ( m.getSortOrder() < 0 ) {
            		row.addFancy( 
            				new FancyMessage( String.format("&3(&b%s&3) ", 
            						"X") )
            				.tooltip("&7Sort order: Suppressed"));
            	}
            	else {
            		row.addFancy( 
            				new FancyMessage( String.format("&3(&b%s&3) ", 
            						Integer.toString( m.getSortOrder() )) )
            				.tooltip("&7Sort order."));
            	}
            	
            	
            	row.addFancy( 
            			new FancyMessage( String.format("&3Mine: &7%s ", m.getName()) )
            					.command("/mines info " + m.getName())
            					.tooltip("&7Click to view info."));
            	
            	if ( m.getTag() != null && m.getTag().trim().length() > 0 ) {
            		row.addTextComponent( "%s ", m.getTag() );
            	}
            	
            	boolean hasCmds = m.getResetCommands().size() > 0;
            	if ( hasCmds ) {
            		row.addFancy( 
                			new FancyMessage( String.format(" &cCmds: &7%s  ", 
                					Integer.toString( m.getResetCommands().size() )) )
                					.command("/mines commands list " + m.getName())
                					.tooltip("&7Click to view commands."));
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
            	
            	if ( !m.isVirtual() ) {
            		row.addFancy( 
            				new FancyMessage("&eTP ").command("/mines tp " + m.getName())
            				.tooltip("&7Click to TP to the mine"));
            	}
            	
            	
            	if ( m.isUsePagingOnReset() ) {
            		row.addFancy( 
            				new FancyMessage("&5Pgd ")
            				.tooltip("&7Paging Used during Mine Reset"));
            	}

            	
            	row.addTextComponent( "  &3Reset: &7" );
            	
            	if ( !m.isVirtual() ) {
            		row.addFancy( 
            				new FancyMessage(dFmt.format(m.getRemainingTimeSec()))
            				.tooltip( "Estimated time in seconds before the mine resets" ) );
            		row.addTextComponent( " sec &3(&b" );
            	}
            	
            	row.addFancy( 
            			new FancyMessage(dFmt.format(m.getResetTime()))
            			.tooltip( "Reset time in seconds" ) );
            	row.addTextComponent( " sec&3)&b" );
            	
            	if ( !m.isVirtual() && player != null && 
            			m.getBounds().withinSameWorld( player.getLocation() ) ) {
            		
            		row.addTextComponent( "  &3Dist: &7");
            		row.addFancy( 
            				new FancyMessage( fFmt.format(m.getBounds().getDistance3d(player.getLocation()))).
            				tooltip("Distance to the Mine") );
            		
            	}
            	
            	builder.add(row.getFancy());
            	
            	
            	if ( !m.isVirtual() ) {
            		RowComponent row2 = new RowComponent();
//            	row2.addTextComponent( "            &3Rem: " );
            		
            		// Right justify the total blocks mined, with 1000's separators:
            		String blocksMined = "           " + dFmt.format( m.getTotalBlocksMined() );
            		blocksMined = blocksMined.substring( blocksMined.length() - 10);
            		
            		row2.addFancy( 
            				new FancyMessage( String.format("  %s  &3Rem: ", blocksMined)).
            				tooltip( "Blocks mined" ) );
            		
            		row2.addFancy( 
            				new FancyMessage(fFmt.format(m.getPercentRemainingBlockCount())).
            				tooltip( "Percent Blocks Remaining" ) );
            		
            		row2.addTextComponent( "%%  &3RCnt: &7" );
            		
            		row2.addFancy( 
            				new FancyMessage(dFmt.format(m.getResetCount())).
            				tooltip( "Times the mine was reset." ) );
            		
            		if ( !m.isVirtual() ) {
            			
            			row2.addTextComponent( " &3 Vol: &7" );
            			row2.addFancy( 
            					new FancyMessage(dFmt.format(m.getBounds().getTotalBlockCount())).
            					tooltip( "Volume in Blocks" ) );
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
            		
            		builder.add(row2.getFancy());
            		
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
					        			m.getName(), (skipEnabled ? "enabled" : "disabled"),
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
					Output.get().sendInfo( sender, "&7mines set resettime: &b%s &7resetTime set to &b%d", m.getName(), resetTime );
					
					// Server Log message:
					Player player = getPlayer( sender );
					Output.get().logInfo( "&bmines set resettime&7: &b%s &7set &b%s &7resetTime to &b%d", 
							(player == null ? "console" : player.getDisplayName()), m.getName(), resetTime  );
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
    						m.getName(), dFmt.format( resetTime ) );
    				
    			} else {
    				Output.get().sendInfo( sender, "&7Mine &b%s Zero Block Reset Delay: &b%s &7sec", 
    						m.getName(), dFmt.format( resetTime ) );
    				
    			}
    			
    			// Server Log message:
    			Player player = getPlayer( sender );
    			Output.get().logInfo( "&7Mine &b%s Zero Block Reset Delay: &b%s &7set it to &b%s &7sec",
    					(player == null ? "console" : player.getDisplayName()), 
    					m.getName(), dFmt.format( resetTime )  );
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
					        			m.getName(), 
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


    @Command(identifier = "mines set rank", permissions = "mines.set", 
    		description = "Links a mine to a rank.")
    public void setMineRankCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine.") String mineName,
        @Arg(name = "rankName", description = "Then rank name to link to this mine.") 
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
            	Prison.get().getPlatform().unlinkModuleElements( m, m.getRank() );
            }
            
            boolean success = Prison.get().getPlatform().linkModuleElements( m, 
            						ModuleElementType.RANK, rankName );
            
            if ( !success ) {
            	sender.sendMessage( String.format( "&3Invalid Rank Name: &7%s", rankName ));
            }
            else {
            	sender.sendMessage( String.format( "&3Rank &7%s &3has been linked to mine &7%s", 
            						rankName, m.getName() ));
            }
        } 
    }


    
    @Command(identifier = "mines set norank", permissions = "mines.set", 
    		description = "Unlinks a rank from a mine")
    public void setMineNoRankCommand(CommandSender sender,
    		@Arg(name = "mineName", description = "The name of the mine.") String mineName
    
    		) {
    	
    	if (performCheckMineExists(sender, mineName)) {
    		setLastMineReferenced(mineName);
    		
    		PrisonMines pMines = PrisonMines.getInstance();
    		Mine m = pMines.getMine(mineName);
    		
    		if ( m.getRank() == null ) {
    			sender.sendMessage( "&cThis mine has no ranks to unlink." );
    			return;
    		}
    		
    		ModuleElement rank = m.getRank();
    		
    		Prison.get().getPlatform().unlinkModuleElements( m, m.getRank() );
    		
    		
    		sender.sendMessage( String.format( "&3Rank &7%s &3has been removed from mine &7%s", 
    				rank.getName(), m.getName() ));
    		
    	} 
    }
    
    

    @Command(identifier = "mines set area", permissions = "mines.set", 
    				description = "Set the area of a mine to your current selection or a 1x1 mine under your feet.")
    public void redefineCommand(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to edit.") String mineName,
        @Arg(name = "source", description = "&3The source to use for setting the area. The &7wand&3 " +
        		"uses the area defined by the wand. &7Feet&3 defines a 1x1 mine under your feet" +
        		"which is useful in void worlds or when flying and can be enlarged with " +
        		"&7/mines set size help&3 . &2[&7wand feet&2]", 
        				def = "wand") String source
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

        // TODO check to see if they are the same boundaries, if not, don't change...
        
        setLastMineReferenced(mineName);
        
        boolean wasVirtual = m.isVirtual();
        
        // Setting the bounds when it's virtual will configure all the internals:
        
        m.setBounds(selection.asBounds());
        
        if ( wasVirtual ) {
        	
        	
        	DecimalFormat dFmt = new DecimalFormat("#,##0");
        	String message = String.format( "&3The mine &7%s &3 is no longer a virutal mine " +
        			"and has been enabled with an area of &7%s &3blocks.",
        			m.getName(), dFmt.format( m.getBounds().getTotalBlockCount() ));
        	
        	sender.sendMessage( message );
        	Output.get().logInfo( message );
        }

        pMines.getMineManager().saveMine( m );
        
        pMines.getMinesMessages().getLocalizable("mine_redefined")
            .sendTo(sender);
        
        // Delete the selection:
        Prison.get().getSelectionManager().clearSelection((Player) sender);
        //pMines.getMineManager().clearCache();
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
    		@Arg(name = "mineName", description = "The name of the mine to set the tracer in.") String mineName,
    		@Arg(name = "edge", description = "Edge to adjust [top, bottom, north, east, south, west, walls]", def = "walls") String edge, 
    		//@Arg(name = "adjustment", description = "How to adust the size [smaller, larger]", def = "larger") String adjustment,
    		@Arg(name = "amount", description = "amount to adjust, [-1, 1]", def = "1") int amount 
    		
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
    		sender.sendMessage( "&cInvalid amount. Cannot be zero." );
    		return;
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
    		@Arg(name = "pattern", description = "pattern to use [?]", def = "bright") String pattern,
    		@Arg(name = "force", description = "Force liner if air [force no]", def = "no") String force
    		
    		) {
    	
    	if (!performCheckMineExists(sender, mineName)) {
    		return;
    	}
    	
    	Edges e = Edges.fromString( edge );
    	if ( e == null ) {
    		sender.sendMessage( "&cInvalid edge value. &3[&7top bottom north east south west walls&3]" );
    		return;
    	}
    	
    	if ( pattern != null && "?".equals( pattern )) {
    		sender.sendMessage( "&3Available Patterns: [&7" + 
    				LinerPatterns.toStringAll() + "&3]" );
    		
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
    	
    	if ( mine.isVirtual() ) {
    		sender.sendMessage( "&cMine is a virtual mine.&7 Use &a/mines set area &7to enable the mine." );
    		return;
    	}
    	
    	
    	new MineLinerBuilder( mine, e, linerPattern, isForced );
    	
    	// NOTE: The mine itself was not changed, so nothing to save:
    	
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
            	sender.sendMessage( String.format( "&7Mine Reset Paging has been disabled for mine %s.", m.getName()) );
            }
            else if ( "enable".equalsIgnoreCase( paging ) && !m.isUsePagingOnReset() ) {
            	m.setUsePagingOnReset( true );
            	pMines.getMineManager().saveMine( m );
            	sender.sendMessage( String.format( "&7Mine Reset Paging has been enabled for mine %s.", m.getName()) );
            }
        	
        } 
    }

    

    @Command(identifier = "mines tp", description = "TP to the mine.", 
    		aliases = "mtp",
    		altPermissions = {"mines.tp", "mines.tp.[mineName]"})
    public void mineTp(CommandSender sender,
        @Arg(name = "mineName", description = "The name of the mine to teleport to.") String mineName,
        
		@Arg(name = "player", def = "", description = "Player name to TP - " +
				"Only console or rank command can include this parameter") String playerName

    		) {
    	
    	Player player = getPlayer( sender );

    	Player playerAlt = getOnlinePlayer( playerName );
    	
    	if ( sender.isOp() && playerAlt != null && playerAlt.isOnline() ) {
    		player = playerAlt;
    	}
    	else if ( player == null || !player.isOnline()) {

    		if ( playerName != null && playerName.trim().length() > 0 && playerAlt == null) {
    			sender.sendMessage( "&3Specified player is not in the game so they cannot be teleported." );
    		}
    		
    		// If the sender is console or its being ran as a rank command, and the playerName is 
    		// a valid online player, then use that player as the active player issuing the command:
    		if ( playerAlt != null && playerAlt.isOnline() ) {
    			player = playerAlt;
    		}
    		else {
    			sender.sendMessage( "&3You must be a player in the game to run this command." );
    			return;
    		}
    		
    	}
    	else if ( playerAlt != null && !player.getName().equalsIgnoreCase( playerAlt.getName()  ) ) {
    		sender.sendMessage( "&3You cannot teleport other players to a mine. Ignoring parameter." );
    	}

    	// Load mine information first to confirm the mine exists and the parameter is correct:
    	if (!performCheckMineExists(sender, mineName)) {
    		return;
    	}

    	setLastMineReferenced(mineName);
    	
    	PrisonMines pMines = PrisonMines.getInstance();
    	Mine m = pMines.getMine(mineName);
    	
    	
        if ( m.isVirtual() ) {
        	sender.sendMessage( "&cInvalid option. This mine is a virtual mine&7. Use &a/mines set area &7to enable the mine." );
        	return;
        }
        
    	
    	String minePermission = "mines.tp." + m.getName().toLowerCase();
    	if ( !sender.isOp() &&
    			!sender.hasPermission("mines.tp") && 
    			!sender.hasPermission( minePermission ) ) {
                Output.get()
                    .sendError(sender, "Sorry. You're unable to teleport there." );
                return;
            }
    	

//        if ( !m.isEnabled() ) {
//        	sender.sendMessage( "&cMine is disabled&7. Use &a/mines info &7for possible cause." );
//        	return;
//        }
        
    	if ( sender instanceof Player ) {
    		m.teleportPlayerOut( (Player) sender );
    	} else {
    		sender.sendMessage(
    	            "&3Telport failed. Are you sure you're a Player?");
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

    	List<Mine> inMine = new ArrayList<>();
    	TreeMap<Integer, Mine> nearMine = new TreeMap<>();
    	for ( Mine mine : pMines.getMineManager().getMines() ) {
    		if ( !mine.isVirtual() && mine.getBounds().within( player.getLocation() ) ) {
    			inMine.add( mine );
    		}
    		
    		// This is checking for within a certain distance from any mine, so we just need to use
    		// some arbitrary distance as a max radius.  We do not want to use the individual values
    		// that have been set for each mine.
    		else if ( !mine.isVirtual() &&  mine.getBounds().within( player.getLocation(), MineData.MINE_RESET__BROADCAST_RADIUS_BLOCKS) ) {
    			Double distance = mine.getBounds().getDistance3d( player.getLocation() );
//    			Double distance = new Bounds( mine.getBounds().getCenter(), player.getLocation()).getDistance();
    			nearMine.put( distance.intValue(), mine );
    		}
    	}
    	
    	if ( inMine.size() > 0 ) {
    		// You are in the mines:
    		for ( Mine m : inMine ) {
    			sender.sendMessage( "&3You are in mine &7" + m.getName() );
    		}
    	}
    	if ( nearMine.size() > 0 ) {
    		// You are near the mines:
    		int cnt = 0;
    		Set<Integer> distances = nearMine.keySet();
    		for ( Integer dist : distances ) {
				Mine m = nearMine.get( dist );
				sender.sendMessage( "&3You are &7" + dist + " &7blocks away from the center of mine &3" + m.getName() );
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

	private Player getPlayer( CommandSender sender ) {
		Optional<Player> player = Prison.get().getPlatform().getPlayer( sender.getName() );
		return player.isPresent() ? player.get() : null;
	}
    
	private Player getOnlinePlayer( String playerName ) {
		Player player = null;
		if ( playerName != null ) {
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
    
    
    @Command(identifier = "mines playerInventory", permissions = "mines.set", 
    		description = "For listing what's in a player's inventory by dumping it to console.", 
    		onlyPlayers = false )
    public void playerInventoryCommand(CommandSender sender) {
    	
    	Player player = getPlayer( sender );
    	
    	if (player == null || !player.isOnline()) {
    		sender.sendMessage( "&3You must be a player in the game to run this command." );
    		return;
    	}
    	
    	player.printDebugInventoryInformationToConsole();
    }



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
            Output.get().sendInfo(sender, "The mine '%s' contains no commands.", m.getName());
            return;
        }


        ChatDisplay display = new ChatDisplay("ResetCommand for " + m.getName());
        display.text("&8Click a command to remove it.");
        BulletedListComponent.BulletedListBuilder builder =
            new BulletedListComponent.BulletedListBuilder();

        for (String command : m.getResetCommands()) {
            FancyMessage msg = new FancyMessage( "&a'&7" + command + "&a'" )
                .command("/mines command remove " + mineName + " " + command)
                .tooltip("Click to remove.");
            builder.add(msg);
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
    				@Arg(name = "command", 
    					description = "Exact command to remove including the 'before: ' and 'after: ' states.") 
    						@Wildcard String command) {
    	
//    	if ( 1 < 2 ) {
//    		sender.sendMessage( "&cThis command is disabled&7. It will be enabled in the near future." );
//    		return;
//    	}
    	
        if (command.startsWith("/")) {
            command = command.replaceFirst("/", "");
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
            Output.get().sendInfo(sender, "The mine '%s' contains no commands.", m.getName());
            return;
        }

        if ( m.getResetCommands().remove(command) ) {
        	
        	pMines.getMineManager().saveMine( m );
            	
        	Output.get().sendInfo(sender, "Removed command '%s' from the mine '%s'.", 
        				command, m.getName());
        } else {
        	Output.get().sendWarn(sender, 
        			String.format("The mine %s doesn't contain that command. Nothing was changed.", 
        						m.getName()));
        }
    }

	@Command(identifier = "mines command add", description = "Adds a command to a mine with NO placeholders.", 
    		onlyPlayers = false, permissions = "mines.command")
    public void commandAdd(CommandSender sender, 
    			@Arg(name = "mineName") String mineName,
    			@Arg(name = "state", def = "before", description = "State can be either before or after.") String state,
    			@Arg(name = "command") @Wildcard String command) {
    	

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
        		newComand, m.getName());

    }
    
	public Long getConfirmTimestamp()
	{
		return confirmTimestamp;
	}
	public void setConfirmTimestamp( Long confirmTimestamp )
	{
		this.confirmTimestamp = confirmTimestamp;
	}

	/**
	 * <p>This function will return the last mine reference to be used to fill in the
	 * <code>&lt;mine&gt;</code> reference within these commands.  After 30 minutes of 
	 * the last reference, this value will be reset to null and this function will then
	 * return the default mine place holder of <code>&lt;mine&gt;</code>.
	 * </p>
	 * 
	 * @return last mine referenced, or <code>&lt;mine&gt;</code>
	 */
	public String getLastMineReferenced()
	{
		if ( getLastMineReferencedTimestamp() != null &&
				System.currentTimeMillis() - getLastMineReferencedTimestamp() > (30 * 60 * 1000))
		{
			setLastMineReferenced( null );
		}
		return (lastMineReferenced == null ? "<mine>" : lastMineReferenced);
	}
	public void setLastMineReferenced( String lastMineReferenced )
	{
		lastMineReferenced( System.currentTimeMillis() );
		this.lastMineReferenced = lastMineReferenced;
	}

	public Long getLastMineReferencedTimestamp()
	{
		return lastMineReferencedTimestamp;
	}
	public void lastMineReferenced( Long lastMineReferencedTimestamp )
	{
		this.lastMineReferencedTimestamp = lastMineReferencedTimestamp;
	}

	
}
