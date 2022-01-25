package tech.mcprison.prison.spigot.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.block.MineTargetPrisonBlock;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.api.PrisonMinesBlockBreakEvent;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.utils.BlockUtils;

public class OnBlockBreakMines
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
		
		public MinesEventResults() {
			super();
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

		
	protected MinesEventResults ignoreMinesBlockBreakEvent( Player player, Block block ) {
		MinesEventResults results = new MinesEventResults();
		
		SpigotBlock sBlock = SpigotBlock.getSpigotBlock( block );
		if ( BlockUtils.getInstance().isUnbreakable( sBlock ) ) {
			results.setCancelEvent( true );
			results.setIgnoreEvent( true );
		}
		
		Mine mine = findMine( player, sBlock,  null, null ); 
		
		if ( mine == null ) {
			// Prison is unable to process blocks outside of mines right now, so exit:
			results.setIgnoreEvent( true );
		}
		else {
			
			// If not minable, then display message and exit.
			if ( !mine.getMineStateMutex().isMinable() ) {
				
				SpigotPlayer sPlayer = new SpigotPlayer( player );
				sPlayer.setActionBar( "Mine " + mine.getTag() + " is being reset... please wait." );
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
	
	public void checkZeroBlockReset( Mine mine ) {
		if ( mine != null ) {
			
			// submit a mine sweeper task.  It will only run if it is enabled and another 
			// mine sweeper task has not been submitted.
			mine.submitMineSweeperTask();
			
			// Checks to see if the mine ran out of blocks, and if it did, then
			// it will reset the mine:
			mine.checkZeroBlockReset();
		}
	}
	

	public boolean collectBukkitDrops( List<SpigotItemStack> bukkitDrops, MineTargetPrisonBlock targetBlock,
			SpigotItemStack itemInHand, SpigotBlock sBlockMined )
	{
		boolean results = false;

		// if ( sBlockMined == null && targetBlock.getMinedBlock() != null ) {
		// sBlockMined = (SpigotBlock) targetBlock.getMinedBlock();
		// }
		// SpigotBlock sBlock = (SpigotBlock) targetBlock.getMinedBlock();

		// If in the mine, then need a targetBlock, otherwise if it's null then get drops anyway:
		if ( sBlockMined != null && 
				( targetBlock == null ||
					targetBlock.getPrisonBlock().equals( sBlockMined.getPrisonBlock() )
				   ) )
		{

			List<SpigotItemStack> drops = SpigotUtil.getDrops( sBlockMined, itemInHand );

			bukkitDrops.addAll( drops );

			//// This clears the drops for the given block, so if the event is
			//// not canceled, it will
			//// not result in duplicate drops.
			// if ( isBoolean( AutoFeatures.cancelAllBlockEventBlockDrops ) ) {
			// sBlock.clearDrops();
			// }

			results = true;

		}
		else if ( sBlockMined != null )
		{
			Output.get().logWarn( "collectBukkitDrops: block was changed and not what was expected.  " + "Block: " +
					sBlockMined.getBlockName() + "  expecting: " + 
					(targetBlock == null ? "(nothing)" : targetBlock.getPrisonBlock().getBlockName()) );
		}

		return results;
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
		TreeMap<String,SpigotItemStack> results = new TreeMap<>();

		for ( SpigotItemStack drop : drops ) {
			String key = drop.getName();
			if ( !results.containsKey( key ) ) {
				results.put( key, drop );
			}
			else {
				SpigotItemStack sItemStack = results.get( key );
				
				sItemStack.setAmount( sItemStack.getAmount() + drop.getAmount() );
			}
		}
		
		return new ArrayList<>( results.values() );
	}

	
	private Mine findMineLocation( SpigotBlock block ) {
		return getPrisonMineManager() == null ? 
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
