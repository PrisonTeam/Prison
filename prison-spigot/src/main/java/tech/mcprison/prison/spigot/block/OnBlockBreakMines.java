package tech.mcprison.prison.spigot.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.integration.CustomBlockIntegration;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.block.MineTargetPrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlock.PrisonBlockType;
import tech.mcprison.prison.internal.block.PrisonBlockStatusData;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.api.PrisonMinesBlockBreakEvent;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.utils.BlockUtils;

public class OnBlockBreakMines
	extends OnBlockBreakEventCoreMessages
{
	private PrisonMines prisonMineManager;
	private boolean mineModuleDisabled = false;

	public OnBlockBreakMines() {
		super();
		
		this.prisonMineManager = null;
		
	}
	
	public class MinesEventResults {
		private boolean cancelEvent = false;
		private boolean ignoreEvent = false;
		private Mine mine = null;
		
		public MinesEventResults() {
			super();
		}

		
		public Mine getMine() {
			return mine;
		}
		public void setMine(Mine mine) {
			this.mine = mine;
		}

		public boolean isCancelEvent() {
			return cancelEvent;
		}
		public void setCancelEvent( boolean cancelEvent ) {
			this.cancelEvent = cancelEvent;
		}

		public boolean isIgnoreEvent() {
			return ignoreEvent;
		}
		public void setIgnoreEvent( boolean ignoreEvent ) {
			this.ignoreEvent = ignoreEvent;
		}
	}
	
	public Mine findMine( Player player, SpigotBlock sBlock, List<Block> altBlocksSource, PrisonMinesBlockBreakEvent pmEvent )
	{
		return findMine( player.getUniqueId(), sBlock, altBlocksSource, pmEvent );
	}
	
	public Mine findMine( UUID playerUUID, SpigotBlock sBlock, List<Block> altBlocksSource, PrisonMinesBlockBreakEvent pmEvent )
	{
		Long playerUUIDLSB = Long.valueOf( playerUUID.getLeastSignificantBits() );

		// Get the cached mine, if it exists:
		Mine mine = getPlayerCache().get( playerUUIDLSB );

		if ( mine == null || sBlock != null && !mine.isInMineExact( sBlock.getLocation() ) )
		{
			// Look for the correct mine to use.
			// Set mine to null so if cannot find the right one it will return a
			// null:
			mine = findMineLocation( sBlock );

			// Thanks to CrazyEnchant, where they do not identify the block the
			// player breaks, we
			// have to go through all of the unprecessedRawBlocks to see if any
			// are within a mine.
			// If we find a block that's in a mine, then use that block as the
			// primary block.
			if ( mine == null && altBlocksSource != null )
			{

				for ( Block bBlock : altBlocksSource )
				{
					SpigotBlock sBlockAltBlock = SpigotBlock.getSpigotBlock( bBlock );
					mine = findMineLocation( sBlockAltBlock );
					if ( mine != null )
					{

						if ( pmEvent != null )
						{
							pmEvent.setSpigotBlock( sBlockAltBlock );
						}

						break;
					}
				}
			}

			// Store the mine in the player cache if not null:
			if ( mine != null )
			{
				getPlayerCache().put( playerUUIDLSB, mine );
			}
		}

		return mine;
	}
	
	
	// NOTE: The following two commented out functions need to be copied and actived in 
	//       in the class that is using these functions for these two events:
	
//	protected boolean ignoreMinesBlockBreakEvent( Cancellable event, Player player, Block block ) {
//		
//		MinesEventResults eventResults = ignoreMinesBlockBreakEvent( player, block );
//		
//		if ( eventResults.isCancelEvent() ) {
//			event.setCancelled( eventResults.isCancelEvent() );
//		}
//		return eventResults.isIgnoreEvent();
//	}
//
//	protected boolean processMinesBlockBreakEvent( PEExplosionEvent event, Player player, Block block ) {
//		
//		MinesEventResults eventResults = ignoreMinesBlockBreakEvent( player, block );
//		
//		if ( eventResults.isCancelEvent() ) {
//			event.setCancelled( eventResults.isCancelEvent() );
//		}
//		return eventResults.isIgnoreEvent();
//	}

		
	/**
	 * <p> If the event is canceled, it still needs to be processed because of the MONITOR events: 
	 * An event will be "canceled" and "ignored" if the block 
	 * BlockUtils.isUnbreakable(), or if the mine is activly resetting.
	 * The event will also be ignored if the block is outside of a mine
	 * or if the targetBlock has been set to ignore all block events which 
	 * means the block has already been processed.
	 * </p>
	 * 
	 * @param player
	 * @param block
	 * @return
	 */
	protected MinesEventResults ignoreMinesBlockBreakEvent( Player player, Block block ) {
		
		MinesEventResults results = new MinesEventResults();
		
		SpigotBlock sBlock = SpigotBlock.getSpigotBlock( block );
		if ( BlockUtils.getInstance().isUnbreakable( sBlock ) ) {
			results.setCancelEvent( true );
			results.setIgnoreEvent( true );
		}
		
		Mine mine = findMine( player, sBlock,  null, null ); 
		results.setMine( mine );
		
		if ( mine == null ) {
			// Prison is unable to process blocks outside of mines right now, so exit:
			results.setIgnoreEvent( true );
		}
		else {
			
			// If not minable, then display message and exit.
			if ( !mine.getMineStateMutex().isMinable() ) {
				
				SpigotPlayer sPlayer = new SpigotPlayer( player );
				sPlayer.setActionBar( mineIsBeingResetMsg( mine.getTag() ) );
				results.setCancelEvent( true );
				results.setIgnoreEvent( true );
			}
			MineTargetPrisonBlock targetBlock = mine.getTargetPrisonBlock( sBlock );
			
			// If ignore all block events, then exit this function without logging anything:
			if ( targetBlock != null && targetBlock.isIgnoreAllBlockEvents() ) {
				
				// Do not cancel the event... let other plugins deal with it... prison does not care about this block.
				//event.setCancelled( true );
				results.setIgnoreEvent( true );
			}
		}
		
		return results;
	}
	
//	/**
//	 * <p>Warning... this is a temp copy of the real function and will be removed
//	 * if PEExplosionEvent adds the interface Cancellable.
//	 * </p>
//	 * 
//	 * @param event
//	 * @param player
//	 * @param block
//	 * @return
//	 */
//	protected boolean processMinesBlockBreakEvent( PEExplosionEvent event, Player player, Block block ) {
//		boolean processEvent = true;
//		
//		SpigotBlock sBlock = new SpigotBlock( block );
//		if ( BlockUtils.getInstance().isUnbreakable( sBlock ) ) {
//			event.setCancelled( true );
//			processEvent = false;
//		}
//		
//		Mine mine = findMine( player, sBlock,  null, null ); 
//		
//		if ( mine == null  ) {
//			// Prison is unable to process blocks outside of mines right now, so exit:
//			processEvent = false;
//		}
//		
//		// If not minable, then display message and exit.
//		if ( !mine.getMineStateMutex().isMinable() ) {
//			
//			SpigotPlayer sPlayer = new SpigotPlayer( player );
//			sPlayer.setActionBar( "Mine " + mine.getTag() + " is being reset... please wait." );
//			event.setCancelled( true );
//			processEvent = false;
//		}
//		MineTargetPrisonBlock targetBlock = mine.getTargetPrisonBlock( sBlock );
//		
//		// If ignore all block events, then exit this function without logging anything:
//		if ( targetBlock.isIgnoreAllBlockEvents() ) {
//			event.setCancelled( true );
//			processEvent = false;
//		}
//
//		
//		return processEvent;
//	}
	
	/**
	 * <p>If mine is not null, then it will check for a zero-block reset (reset-threshold).
	 * </p>
	 * 
	 * @param mine
	 */
	public void checkZeroBlockReset( Mine mine ) {
		if ( mine != null ) {
			
			// Checks to see if the mine ran out of blocks, and if it did, then
			// it will reset the mine:
			mine.checkZeroBlockReset();
		}
	}
	
	
	/**
	 * <p>If mine is not null, then it will perform a mine sweeper 
	 * for the mine, if it is enabled.
	 * </p>
	 * 
	 * @param mine
	 */
	public void checkMineSweeper( Mine mine ) {
		if ( mine != null ) {
			
			// submit a mine sweeper task.  It will only run if it is enabled and another 
			// mine sweeper task has not been submitted.
			mine.submitMineSweeperTask();
		}
	}
	
	
	
	/**
	 * <p>Checks only if the names match.  Does not check locations within any worlds.
	 * </p>
	 * @param pbTargetBlock
	 * @param pbBlockHit
	 * @return
	 */
	public boolean isBlockAMatch( MineTargetPrisonBlock targetBlock, PrisonBlock pbBlockHit )
	{
		boolean results = false;
		if ( targetBlock != null ) {
			PrisonBlockStatusData pbTargetBlock = targetBlock.getPrisonBlock();
			
			if ( pbTargetBlock != null && pbBlockHit != null ) {
				
				if ( pbTargetBlock.getBlockType() == PrisonBlockType.CustomItems ) {
					// The pbBlockHit will never match the pbTargetBlock.. must check the actual block:
					
					List<CustomBlockIntegration> cbIntegrations = 
							PrisonAPI.getIntegrationManager().getCustomBlockIntegrations();
					
					for ( CustomBlockIntegration customBlock : cbIntegrations )
					{
						String cbId = customBlock.getCustomBlockId( pbBlockHit );
						
						if ( cbId != null ) {
							
							if ( pbTargetBlock.getBlockName().equalsIgnoreCase( cbId ) ) {
								
								pbBlockHit.setBlockType( customBlock.getBlockType() );
								pbBlockHit.setBlockName( cbId );
								
								results = true;
								break;
							}
						}
					}
					
				}
				else {
					
					results = pbTargetBlock.equals( pbBlockHit );
				}
			}
		}
		
		return results;
	}
	

	/**
	 * <p>This function is only called once it has been confirmed that the block is the correct one, that
	 * it matches the the targetBlock.  All validation on if the block is correct, or not, has been 
	 * removed since this is not the place for the checks.
	 * </p>
	 * 
	 * <p>The function isBlockAMatch() should be used prior to calling this function.
	 * </p>
	 * 
	 * @param bukkitDrops
	 * @param targetBlock
	 * @param itemInHand
	 * @param sBlockMined
	 * @return
	 */
	public boolean collectBukkitDrops( List<SpigotItemStack> bukkitDrops, MineTargetPrisonBlock targetBlock,
			SpigotItemStack itemInHand, SpigotBlock sBlockMined, SpigotPlayer player )
	{
		boolean results = false;

		if ( targetBlock != null && targetBlock.getPrisonBlock().getBlockType() == PrisonBlockType.CustomItems ) {
			
			List<CustomBlockIntegration> cbIntegrations = 
					PrisonAPI.getIntegrationManager().getCustomBlockIntegrations();
			
			for ( CustomBlockIntegration customBlock : cbIntegrations )
			{
				List<? extends ItemStack> drops = customBlock.getDrops( player, sBlockMined, itemInHand );
				
				for ( ItemStack drop : drops )
				{
					bukkitDrops.add( (SpigotItemStack) drop );
					results = true;
				}
			}
			
		}
		
		
		
		// if ( sBlockMined == null && targetBlock.getMinedBlock() != null ) {
		// sBlockMined = (SpigotBlock) targetBlock.getMinedBlock();
		// }
		// SpigotBlock sBlock = (SpigotBlock) targetBlock.getMinedBlock();

		// If in the mine, then need a targetBlock, otherwise if it's null then get drops anyway:
		if ( !results && sBlockMined != null 
//				&& ( targetBlock == null ||
//					targetBlock.getPrisonBlock().equals( sBlockMined.getPrisonBlock() )
//				   ) 
				)
		{
			

			getSpigotDrops( bukkitDrops, sBlockMined, itemInHand );
			
			//// This clears the drops for the given block, so if the event is
			//// not canceled, it will
			//// not result in duplicate drops.
			// if ( isBoolean( AutoFeatures.cancelAllBlockEventBlockDrops ) ) {
			// sBlock.clearDrops();
			// }
			
			results = true;

		}
//		else if ( !results && sBlockMined != null )
//		{
//			Output.get().logWarn( "collectBukkitDrops: block was changed and not what was expected.  " + "Block: " +
//					sBlockMined.getBlockName() + "  expecting: " + 
//					(targetBlock == null ? "(nothing)" : targetBlock.getPrisonBlock().getBlockName()) );
//		}

		return results;
	}

	/**
	 * <p>This gets the bukkit drops...
	 * </p>
	 * 
	 * @param bukkitDrops
	 * @param sBlockMined
	 * @param itemInHand
	 */
	private void getSpigotDrops( List<SpigotItemStack> bukkitDrops, SpigotBlock sBlockMined, SpigotItemStack itemInHand )
	{
		List<SpigotItemStack> drops = SpigotUtil.getDrops( sBlockMined, itemInHand );
		
		bukkitDrops.addAll( drops );
	}
	
	
	public void clearBukkitDrops( List<SpigotItemStack> bukkitDrops, MineTargetPrisonBlock targetBlock )
	{

		SpigotBlock sBlock = (SpigotBlock) targetBlock.getMinedBlock();
		sBlock.clearDrops();

	}

	
	/**
	 * <p>The List of drops must have only one ItemStack per block type (name).
	 * This function combines multiple occurrences together and adds up their 
	 * counts to properly represent the total quantity in the original drops collection
	 * that had duplicate entries.
	 * </p>
	 * 
	 * @param List of SpigotItemStack drops with duplicate entries
	 * @return List of SpigotItemStack drops without duplicates
	 */
	public List<SpigotItemStack> mergeDrops( List<SpigotItemStack> drops )
	{
		if ( drops.size() <= 1 ) {
			return drops;
		}
		TreeMap<String,SpigotItemStack> results = new TreeMap<>();

		boolean changed = false;
		for ( SpigotItemStack drop : drops ) {
			String key = drop.getName();
			if ( !results.containsKey( key ) ) {
				results.put( key, drop );
			}
			else {
				SpigotItemStack sItemStack = results.get( key );
				
				sItemStack.setAmount( sItemStack.getAmount() + drop.getAmount() );
				changed = true;
			}
		}
		
		return changed ?  new ArrayList<>( results.values() ) : drops;
	}

	
	private Mine findMineLocation( SpigotBlock block ) {
		return getPrisonMineManager() == null || block == null || block.getLocation() == null ? 
				null : getPrisonMineManager().findMineLocationExact( block.getLocation() );
	}
	

	private TreeMap<Long, Mine> getPlayerCache() {
		return getPrisonMineManager() == null ? 
				new TreeMap<Long, Mine>() :
				getPrisonMineManager().getPlayerCache();
	}

	private PrisonMines getPrisonMineManager() {
		if ( prisonMineManager == null && !isMineModuleDisabled() ) {
			Optional<Module> mmOptional = Prison.get().getModuleManager().getModule( PrisonMines.MODULE_NAME );
			if ( mmOptional.isPresent() && mmOptional.get().isEnabled() ) {
				PrisonMines prisonMines = (PrisonMines) mmOptional.get();
				this.prisonMineManager = prisonMines;
			} else {
				setMineModuleDisabled( true );
			}
		}
		return prisonMineManager;
	}
	
	private boolean isMineModuleDisabled() {
		return mineModuleDisabled;
	}
	private void setMineModuleDisabled( boolean mineModuleDisabled ) {
		this.mineModuleDisabled = mineModuleDisabled;
	}
}
