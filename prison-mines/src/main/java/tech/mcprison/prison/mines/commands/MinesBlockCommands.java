package tech.mcprison.prison.mines.commands;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.chat.FancyMessage;
import tech.mcprison.prison.commands.CommandPagedData;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlockStatusData;
import tech.mcprison.prison.internal.block.PrisonBlockTypes;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.BlockOld;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.output.BulletedListComponent;
import tech.mcprison.prison.output.ChatDisplay;
import tech.mcprison.prison.output.LogLevel;
import tech.mcprison.prison.output.RowComponent;
import tech.mcprison.prison.placeholders.PlaceholdersUtil;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.MaterialType;

public class MinesBlockCommands
	extends MinesCoreCommands
{
	
	public MinesBlockCommands( String cmdGroup ) {
		super( cmdGroup );
	}


    
    public void addBlockCommand(CommandSender sender,
            			String mineName, 
    					String block,
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
        

        
        if ( m.isUseNewBlockModel() ) {
        	
        	block = block == null ? null : block.trim().toLowerCase();
        	PrisonBlock prisonBlock = null;
        	
        	PrisonBlockTypes prisonBlockTypes = Prison.get().getPlatform().getPrisonBlockTypes();
        	
        	if ( block != null && prisonBlockTypes.getBlockTypesByName().containsKey( block ) ) {
        		prisonBlock = prisonBlockTypes.getBlockTypesByName().get( block );
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
        		m.getBlocks().add( new BlockOld( blockType, chance, 0) );
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

        getBlocksList(m, null, true ).send(sender);

        //pMines.getMineManager().clearCache();
    }

	private void updateMinePrisonBlock( CommandSender sender, Mine m, PrisonBlock prisonBlock, 
											double chance, PrisonMines pMines )
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
				m.removePrisonBlock( existingPrisonBlock );
			}
			else {
				// update chance for the prisonBlock. This block is
				// still in the mine, so just update the chance.
				existingPrisonBlock.setChance( chance );
			}
			
			pMines.getMineManager().saveMine( m );

			pMines.getMinesMessages().getLocalizable("block_set")
						.withReplacements( existingPrisonBlock.getBlockName(), m.getTag()).sendTo(sender);
		}
		else {
			prisonBlock.setChance( chance );
			m.addPrisonBlock( prisonBlock );

			pMines.getMineManager().saveMine( m );

			pMines.getMinesMessages().getLocalizable("block_added")
						.withReplacements(prisonBlock.getBlockName(), m.getTag()).sendTo(sender);
		}
	}

	protected BulletedListComponent getBlocksList( Mine m, CommandPagedData cmdPageData, boolean includeTotals )
	{

		BulletedListComponent.BulletedListBuilder builder = new BulletedListComponent.BulletedListBuilder();

		DecimalFormat iFmt = new DecimalFormat( "#,##0" );
		DecimalFormat dFmt = new DecimalFormat( "#,##0.00" );
		double totalChance = 0.0d;
		int count = 0;

		PrisonBlock totals = new PrisonBlock( "Totals" );

		if ( m.isUseNewBlockModel() )
		{

			for ( PrisonBlock block : m.getPrisonBlocks() )
			{
				double chance = Math.round( block.getChance() * 100.0d ) / 100.0d;
				totalChance += chance;

				totals.addStats( block );

				if ( cmdPageData == null || count++ >= cmdPageData.getPageStart() && count <= cmdPageData.getPageEnd() )
				{

					addBlockStats( m, block, iFmt, dFmt, builder );

				}
			}
		}
		if ( !m.isUseNewBlockModel() || !m.isUseNewBlockModel() && cmdPageData != null && cmdPageData.isDebug() )
		{

			for ( BlockOld block : m.getBlocks() )
			{
				double chance = Math.round( block.getChance() * 100.0d ) / 100.0d;
				totalChance += chance;

				totals.addStats( block );

				if ( cmdPageData == null || count++ >= cmdPageData.getPageStart() && count <= cmdPageData.getPageEnd() )
				{

					addBlockStats( m, block, iFmt, dFmt, builder );

				}
			}
		}

		if ( totalChance < 100.0d )
		{
			builder.add( "&e%s - Air", dFmt.format( 100.0d - totalChance ) + "%" );
		}

		if ( includeTotals )
		{
			addBlockStats( m, totals, iFmt, dFmt, builder );
		}

		return builder.build();
	}
	
	private void addBlockStats( Mine mine, PrisonBlockStatusData block, DecimalFormat iFmt, DecimalFormat dFmt,
			BulletedListComponent.BulletedListBuilder builder )
	{
		RowComponent row = new RowComponent();

		boolean totals = block.getBlockName().equalsIgnoreCase( "totals" );

		if ( totals )
		{
			String text = "                   &dTotals:  ";
			row.addTextComponent( text );
		}
		else
		{

			String percent = dFmt.format( block.getChance() ) + "%";

			String text = String.format( "&7%s - %s", percent, block.getBlockName() );
			// Minor padding after the name and chance:
			if ( text.length() < 30 )
			{
				text += "                              ".substring( text.length() );
			}
			FancyMessage msg = new FancyMessage( text )
					.suggest( "/mines block set " + mine.getName() + " " + block.getBlockName() + " %" )
					.tooltip( "&7Click to edit the block's chance." );
			row.addFancy( msg );
		}

		String text1 = formatStringPadRight( (totals ? "      &b%s" : "  &3Pl: &7%s"), 16,
				iFmt.format( block.getResetBlockCount() ) );
		FancyMessage msg1 = new FancyMessage( text1 ).tooltip( "&7Number of blocks of this type &3Pl&7aced in this mine." );
		row.addFancy( msg1 );

		String text2 = formatStringPadRight( (totals ? "    &b%s" : "  &3Rm: &7%s"), 16,
				iFmt.format( block.getResetBlockCount() - block.getBlockCountUnsaved() ) );
		FancyMessage msg2 = new FancyMessage( text2 ).tooltip( "&7Number of blocks of this type &3R&7e&3e&7aining." );
		row.addFancy( msg2 );

		FancyMessage msg3 = new FancyMessage( String.format( (totals ? " &b%s" : "  &3T: &7%s"),
				PlaceholdersUtil.formattedKmbtSISize( 1.0d * block.getBlockCountTotal(), dFmt, "" ) ) )
						.tooltip( "&3T&7otal blocks of this type that have been mined." );
		row.addFancy( msg3 );

		FancyMessage msg4 = new FancyMessage( String.format( (totals ? "      &b%s" : "  &3S: &7%s"),
				PlaceholdersUtil.formattedKmbtSISize( 1.0d * block.getBlockCountTotal(), dFmt, "" ) ) )
						.tooltip( "&7Blocks of this type that have been mined since the server was &3S&7tarted." );
		row.addFancy( msg4 );

		builder.add( row );

		if ( block.getConstraintMin() > 0 || block.getConstraintMax() > 0 || block.getConstraintExcludeTopLayers() > 0 ||
				block.getConstraintExcludeBottomLayers() > 0 )
		{

			RowComponent row2 = new RowComponent();

			row2.addTextComponent( "        &3Constraints:  " );

			String text6 = formatStringPadRight( "&2Min: &6%s", 16,
					(block.getConstraintMin() == 0 ? "none" : iFmt.format( block.getConstraintMin() )) );
			FancyMessage msg6 = new FancyMessage( text6 ).tooltip( "&7During a mine reset, the min constraint will try to " +
					"be the minimum number of blocks of this type to be added " + "to the mine." );
			row2.addFancy( msg6 );

			String text7 = formatStringPadRight( "  &2Max: &6%s", 1,
					(block.getConstraintMax() == 0 ? "none" : iFmt.format( block.getConstraintMax() )) );
			FancyMessage msg7 = new FancyMessage( text7 ).tooltip( "&7During a mine reset, the max constraint will try to " +
					"be the maximum number of blocks of this type to be added " + "to the mine." );
			row2.addFancy( msg7 );

			String text8 = formatStringPadRight( "  &2ExcTop: &6%s", 1,
					(block.getConstraintExcludeTopLayers() == 0 ? "none" : iFmt.format( block.getConstraintExcludeTopLayers() )) );
			FancyMessage msg8 = new FancyMessage( text8 )
					.tooltip( "&7During a mine reset, the Exclude from the Top-n Layers constraint will " +
							"prevent the blocks of this type from being added at the specified layer and " + "above." );
			row2.addFancy( msg8 );

			String text9 = formatStringPadRight( "  &2ExcBottom: &6%s", 1, (block.getConstraintExcludeBottomLayers() == 0 ? "none"
					: iFmt.format( block.getConstraintExcludeBottomLayers() )) );
			FancyMessage msg9 = new FancyMessage( text9 )
					.tooltip( "&7During a mine reset, the Exclude from the Bottom-n Layers constraint will " +
							"prevent the blocks of this type from being added at the specified layer and " + "below." );
			row2.addFancy( msg9 );

			builder.add( row2 );
		}

	}
	

	public void setBlockCommand(CommandSender sender,
    					String mineName,
    					String block,
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
        

        if ( m.isUseNewBlockModel() ) {
        
        	
        	block = block == null ? null : block.trim().toLowerCase();
        	PrisonBlock prisonBlock = null;
        	
        	PrisonBlockTypes prisonBlockTypes = Prison.get().getPlatform().getPrisonBlockTypes();
        	
        	if ( block != null && prisonBlockTypes.getBlockTypesByName().containsKey( block ) ) {
        		prisonBlock = prisonBlockTypes.getBlockTypesByName().get( block );
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
        
        
        getBlocksList(m, null, true ).send(sender);

        //pMines.getMineManager().clearCache();

    }

    
    private BlockPercentTotal calculatePercentage( double chance, BlockType blockType, Mine m ) {
    	BlockPercentTotal results = new BlockPercentTotal();
    	results.addChance( chance );

    	for ( BlockOld block : m.getBlocks() ) {
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
    	private BlockOld oldBlock = null;
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

		public BlockOld getOldBlock() {
			return oldBlock;
		}
		public void setOldBlock( BlockOld oldBlock ) {
			this.oldBlock = oldBlock;
		}

		public PrisonBlock getPrisonBlock() {
			return prisonBlock;
		}
		public void setPrisonBlock( PrisonBlock prisonBlock ) {
			this.prisonBlock = prisonBlock;
		}
		
    }
    

    public void delBlockCommand(CommandSender sender,
        String mineName,
        String block) {

        if (!performCheckMineExists(sender, mineName)) {
            return;
        }

        setLastMineReferenced(mineName);
        
        PrisonMines pMines = PrisonMines.getInstance();
        Mine m = pMines.getMine(mineName);
        
        
        if ( m.isUseNewBlockModel() ) {
        
        	
        	block = block == null ? null : block.trim().toLowerCase();
        	PrisonBlock prisonBlock = null;
        	
        	
        	PrisonBlockTypes prisonBlockTypes = Prison.get().getPlatform().getPrisonBlockTypes();
        	
        	if ( block != null && prisonBlockTypes.getBlockTypesByName().containsKey( block ) ) {
        		prisonBlock = prisonBlockTypes.getBlockTypesByName().get( block );
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
        
        getBlocksList(m, null, true).send(sender);
    }

    /**
     * Delete only the first occurrence of a block with the given BlockType.
     * 
     * @param sender
     * @param pMines
     * @param m
     * @param prisonBlock
     */
	private void deleteBlock( CommandSender sender, PrisonMines pMines, Mine m, PrisonBlock prisonBlock ) {
		if ( m.removePrisonBlock( prisonBlock ) ) {
			pMines.getMineManager().saveMine( m );
			
			pMines.getMinesMessages().getLocalizable("block_deleted").
						withReplacements(prisonBlock.getBlockName(), m.getTag()).sendTo(sender);
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
		BlockOld rBlock = null;
		for ( BlockOld block : m.getBlocks() ) {
			if ( block.getType() ==  blockType ) {
				rBlock = block;
				break;
			}
		}
		if ( m.getBlocks().remove( rBlock )) {
			pMines.getMineManager().saveMine( m );
			
			pMines.getMinesMessages().getLocalizable("block_deleted")
			.withReplacements(blockType.name(), m.getTag()).sendTo(sender);
		}
	}

    public void searchBlockCommand(CommandSender sender,
        String search,
        String page ) {

    	PrisonMines pMines = PrisonMines.getInstance();
    	if (search == null)
    	{
    		pMines.getMinesMessages().getLocalizable("block_search_blank").sendTo(sender);
    	}
    	
    	ChatDisplay display = null;
    	
        if ( Prison.get().getPlatform().isUseNewPrisonBlockModel() ) {
            
        	display = prisonBlockSearchBuilder(search, page, true, "mines block search");
        }
        else {
        	
        	display = blockSearchBuilder(search, page, true, "mines block search");
        }
        
        display.send(sender);

        //pMines.getMineManager().clearCache();
    }
    
    public void searchBlockAllCommand(CommandSender sender,
    		String search,
    		String page ) {
    	
    	PrisonMines pMines = PrisonMines.getInstance();
    	if (search == null)
    	{
    		pMines.getMinesMessages().getLocalizable("block_search_blank").sendTo(sender);
    	}
    	
    	ChatDisplay display = null;
    	
    	if ( Prison.get().getPlatform().isUseNewPrisonBlockModel() ) {
    		
    		display = prisonBlockSearchBuilder(search, page, false, "mines block searchAll");
    	}
    	else {
    		
    		display = blockSearchBuilder(search, page, false, "mines block searchAll");
    	}
    	
    	display.send(sender);
    	
    	//pMines.getMineManager().clearCache();
    }
    
    private ChatDisplay prisonBlockSearchBuilder(String search, String page, 
    							boolean restrictToBlocks, String command )
    {
    	
    	PrisonBlockTypes prisonBlockTypes = Prison.get().getPlatform().getPrisonBlockTypes();
    	List<PrisonBlock> blocks = prisonBlockTypes.getBlockTypes( search, restrictToBlocks );
    	
    	CommandPagedData cmdPageData = new CommandPagedData(
    			"/" + command + " " + search, blocks.size(),
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
    	display.addText("&8Click a block to add it to a mine.");
    	
    	BulletedListComponent.BulletedListBuilder builder =
    			new BulletedListComponent.BulletedListBuilder();
    	for ( int i = cmdPageData.getPageStart(); i < cmdPageData.getPageEnd(); i++ )
    	{
    		PrisonBlock block = blocks.get(i);
    		FancyMessage msg =
    				new FancyMessage(
    						String.format("&7%s %s (%s)", 
    								Integer.toString(i), block.getBlockNameSearch(),
    								(block.isBlock() ? "block" : "item")
//    								block.getAltName(),
    								))
    				.suggest("/mines block add " + getLastMineReferenced() + 
    									" " + block.getBlockNameSearch() + " %")
    				.tooltip("&7Click to add block to a mine.");
    		builder.add(msg);
    	}
    	display.addComponent(builder.build());
    	
    	// This command plus parameters used:
//        String pageCmd = "/mines block search " + search;
    	
    	cmdPageData.generatePagedCommandFooter( display );
    	
    	return display;
    }

	private ChatDisplay blockSearchBuilder(String search, String page, 
					boolean restrictToBlocks, String command)
	{
		List<BlockType> blocks = new ArrayList<>();
    	for (BlockType block : BlockType.values())
		{
			if ( (!restrictToBlocks || restrictToBlocks && block.getMaterialType() == MaterialType.BLOCK) && 
					(block.getId().contains(search.toLowerCase()) || 
					block.name().toLowerCase().contains(search.toLowerCase())) )
			{
				blocks.add(block);
			}
		}
    	
        
        CommandPagedData cmdPageData = new CommandPagedData(
        		"/" + command + " " + search, blocks.size(),
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
        display.addText("&8Click a block to add it to a mine.");
        
        BulletedListComponent.BulletedListBuilder builder =
        						new BulletedListComponent.BulletedListBuilder();
        for ( int i = cmdPageData.getPageStart(); i < cmdPageData.getPageEnd(); i++ )
        {
        	BlockType block = blocks.get(i);
            FancyMessage msg =
                    new FancyMessage(
                    		String.format("&7%s %s  (%s)%s (%s)", 
                    				Integer.toString(i), block.name(), 
                    				block.getId().replace("minecraft:", ""),
                    				(block.getMaterialVersion() == null ? "" : 
                    					"(" + block.getMaterialVersion() + ")"),
                    				(block.isBlock() ? "block": "item"))
                    		)
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

	

    public void listBlockCommand(CommandSender sender,
    		String mineName ) {

        setLastMineReferenced(mineName);
        
        PrisonMines pMines = PrisonMines.getInstance();
        Mine m = pMines.getMine(mineName);
        
        DecimalFormat dFmt = new DecimalFormat("#,##0");
        DecimalFormat fFmt = new DecimalFormat("#,##0.00");
        
        
        ChatDisplay chatDisplay = new ChatDisplay("&bMine: &3" + m.getName());

      	
      	if ( !m.isVirtual() ) {
      		RowComponent row = new RowComponent();
      		row.addTextComponent( "&3Blocks Remaining: &7%s  %s%% ",
      				dFmt.format( m.getRemainingBlockCount() ), 
      				fFmt.format( m.getPercentRemainingBlockCount() ) );
      		
      		chatDisplay.addComponent( row );
      	}
        

      	int blockSize = 0;


        chatDisplay.addText("&3Blocks:");
        chatDisplay.addText("&8Click on a block's name to edit its chances of appearing.%s",
        		(m.isUseNewBlockModel() ? ".." : ""));
        
        BulletedListComponent list = getBlocksList(m, null, true );
        chatDisplay.addComponent(list);

        if ( m.isUseNewBlockModel() ) {
        	blockSize =  m.getPrisonBlocks().size();
        }
        else {
        	blockSize = m.getBlocks().size();
        }
        
        if ( blockSize == 0 ) {
        	String message = blockSize != 0 ? null : " &cNo Blocks Defined";
        	chatDisplay.addText( message );
        }
        
        chatDisplay.send(sender);
    	
    }
    
    

    public void constraintsBlockCommand(CommandSender sender,
    		String mineName,
    		String blockName,
    		String constraint,
    		int value ) {

        setLastMineReferenced(mineName);
        
        PrisonMines pMines = PrisonMines.getInstance();
        Mine m = pMines.getMine(mineName);

        if ( m == null ) {
        	sender.sendMessage( 
        			String.format( "&7The specified mine named &3%s &7 does not exist. " +
        					"Please try again.", 
        					(mineName == null ? "null" : mineName) ));
        	return;
        }
        
        if ( constraint == null || 
        		!"max".equalsIgnoreCase( constraint ) && !"min".equalsIgnoreCase( constraint ) && 
        		!"excludeTop".equalsIgnoreCase( constraint ) && !"excludeBottom".equalsIgnoreCase( constraint ) ) {
        	sender.sendMessage( 
        			String.format( "Valid contraint values are [min max excludeTop excludeBottom]. " +
        					"ExcludeTop and ExcludeBottom are expressed in the number of layers. " +
        					"Was [%s]", 
        			(constraint == null ? "null" : constraint) ));
        	listBlockCommand(sender, m.getTag() );
        	return;
        }

        if ( blockName == null || !m.hasBlock( blockName ) ) {
        	sender.sendMessage( 
        			String.format( "&7The block name &3%s &7 does not exist in the specified mine. " +
        					"Please try again.", 
        					(blockName == null ? "null" : blockName) ));
        	listBlockCommand(sender, m.getTag() );
        	return;
        }

        if ( value < 0 ) {
        	sender.sendMessage( 
        			String.format( "&7The specified value cannot be less than zero. [%s]  " +
        					"Please try again.", 
        					Integer.toString( value ) ));
        	listBlockCommand(sender, m.getTag() );
        	return;
        }
        
        if ( m.getBounds() != null && value > m.getBounds().getTotalBlockCount() ) {
        	sender.sendMessage( 
        			String.format( "&7The specified value cannot be more than the total number " +
        					"of blocks in the mine. value = [%s]  total blocks = [%s]  " +
        					"Please try again.", 
        					Integer.toString( value ), 
        					Integer.toString( m.getBounds().getTotalBlockCount() ) ));
        	listBlockCommand(sender, m.getTag() );
        	return;
        }
        
        
        
    	PrisonBlockStatusData block = null;
    	
    	if ( m.isUseNewBlockModel() ) {
    		block = m.getPrisonBlock( blockName );
    	}
    	else {
    		block = m.getBlockOld( blockName );
    	}
        

    	if ( "min".equalsIgnoreCase( constraint ) ) {
    		if ( block.getConstraintMax() != 0 && value > block.getConstraintMax() ) {
            	sender.sendMessage( 
            			String.format( "&7The specified value for the min constraint cannot " +
            					"be more than the max constraint value.  value = [%s]  max= %s  " +
            					"Please try again.", 
            					Integer.toString( value ), 
            					Integer.toString( block.getConstraintMax() ) ));
            	listBlockCommand(sender, m.getTag() );
            	return;

    		}
    		block.setConstraintMin( value );
    	}
    	if ( "max".equalsIgnoreCase( constraint ) ) {
    		if ( block.getConstraintMin() != 0 && value < block.getConstraintMin() ) {
    			sender.sendMessage( 
    					String.format( "&7The specified value for the max constraint cannot " +
    							"be less than the min constraint value.  value = [%s]  min= %s  " +
    							"Please try again.", 
    							Integer.toString( value ), 
    							Integer.toString( block.getConstraintMin() ) ));
    			listBlockCommand(sender, m.getTag() );
    			return;
    			
    		}
    		block.setConstraintMax( value );
    	}
    	if ( "excludeTop".equalsIgnoreCase( constraint ) ) {
    		if ( block.getConstraintExcludeBottomLayers() != 0 &&
    				value > block.getConstraintExcludeBottomLayers() ) {
    			sender.sendMessage( 
    					String.format( "&7The specified value for the ExcludeTop layers constraint cannot " +
    							"be more than the ExcludeBottom layers constraint value.  " +
    							"value = [%s]  ExcludeBottom layers= %s  " +
    							"Please try again.", 
    							Integer.toString( value ), 
    							Integer.toString( block.getConstraintExcludeBottomLayers() ) ));
    			listBlockCommand(sender, m.getTag() );
    			return;
    			
    		}
    		block.setConstraintExcludeTopLayers( value );
    	}
    	if ( "excludeBottom".equalsIgnoreCase( constraint ) ) {
    		if ( block.getConstraintExcludeTopLayers() != 0 &&
    						value < block.getConstraintExcludeTopLayers() ) {
    			sender.sendMessage( 
    					String.format( "&7The specified value for the ExcludeBottom layers constraint cannot " +
    							"be less than the ExcludeTop layers constraint value.  " +
    							"value = [%s]  ExcludeTop layers= %s  " +
    							"Please try again.", 
    							Integer.toString( value ), 
    							Integer.toString( block.getConstraintExcludeTopLayers() ) ));
    			listBlockCommand(sender, m.getTag() );
    			return;
    			
    		}
    		block.setConstraintExcludeBottomLayers( value );
    	}
        
        
        
        pMines.getMineManager().saveMine( m );
        
        
        String message = String.format( "&7Mine &3%s&7's constraint for &3%s &7has been set to &3%s.", 
        		m.getTag(), constraint,
        		(value == 0 ? "disabled" : Integer.toString( value ) ));
        
        
        sender.sendMessage( message );
        
        
    }
	

	
}
