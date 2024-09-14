package tech.mcprison.prison.spigot.api;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.bombs.MineBombData;
import tech.mcprison.prison.internal.block.MineTargetPrisonBlock;
import tech.mcprison.prison.internal.block.PrisonBlock.PrisonBlockType;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.features.MineBlockEvent.BlockEventType;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.block.BlockBreakPriority;
import tech.mcprison.prison.spigot.block.OnBlockBreakMines.MinesEventResults;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.spigot.compat.SpigotCompatibility;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.sellall.SellAllUtil;
import tech.mcprison.prison.spigot.utils.tasks.PlayerAutoRankupTask;

/**
 * <p>This is an event that prison calls before processing the blocks that
 * are within the mines.  It is cancelable.
 * </p>
 *  
 * <p>Please note, that the two prison events, PrisonMinesBlockEventEvent and 
 * PrisonMinesBlockBreakEvent are exactly the same, except for the class name.  
 * This is intentional since PrisonMinesBlockBreakEvent is fired before prison 
 * processes a block event, and PrisonMinesBlockEventEvent is fired before 
 * processing a BlockEvent that has passed it's primary filters.
 * </p>
 * 
 * <p><b>Warning:</b> BlastUseEvent does not identify the block the player actually hit, so the dummyBlock
 * is just a random first block from the explodedBlocks list and may not be the block
 * that initiated the explosion event.  Such events are identified by 
 * BlockEventType.CEXplosion.  Make sure you do not process the first block twice,
 * which is also passed as the org.bukkit.block.Block and the SpigotBlock to prevent have to 
 * pass nulls.  They are the exact same objects.  
 * </p>
 *
 */
public class PrisonMinesBlockBreakEvent
		extends BlockBreakEvent
{
	/**
	 * Warning: Because this class extends from the BlockBreakEvent, it also
	 *          uses the same handlers listing, which means when this event
	 *          fires, it will fire all BlockBreakEvent listeners too.  
	 *          For prison, it means an a recursive loop that will result in
	 *          numerous stack overflow exceptions.  This prevents that from
	 *          happening.  Plus the BlockBreakEvent handler specifically checks
	 *          for this class and exits.
	 *          
	 * Due to this behavior, since BlockBreakEvent's handlers is static, it 
	 * will "share" the handlers (listeners).  To prevent this unwanted behavior,
	 * since prison's BlockBreakEvent listeners will be called, this class
	 * defines and overrides the the handlers with it's own instance.
	 * 
	 */
	private static final HandlerList handlers = new HandlerList();
	
	private Mine mine;
	
	private SpigotPlayer spigotPlayer;
	private SpigotItemStack itemInHand;

	private SpigotBlock spigotBlock;
	private MineTargetPrisonBlock targetBlock;

	
	//private SpigotBlock overRideSpigotBlock;
	
	private List<SpigotBlock> explodedBlocks;
	
	// The targetBlocks are the blocks that were used to reset the mine with.
	// They identify what the blocks were supposed to be.
	private List<MineTargetPrisonBlock> targetExplodedBlocks;
	
	private BlockEventType blockEventType;
	private String triggered;
	
	
	private MineBombData mineBomb;
	
	
	// If this is set during the validation process, and the validation fails, then this it will 
	// force the canceling of the original block event.
	private boolean cancelOriginalEvent = false;

	
	// This will control if durability is calculated, but only if in the auto features configs
	// has the AutoFeatures.isCalculateDurabilityEnabled set to true.  If it is set to false 
	// then this setting will do nothing.
	private boolean calculateDurability = true;

	
	// The use of forceAutoSell will sell the drops before they are added to the 
	// player's inventory.  This would actually be much faster to process than
	// going through the normal sellall functionality since that will have to process
	// all of the player's inventory slots, including their backpacks.
	private boolean forceAutoSell = false;
	
	
	// If forceBlockRemoval is true, then it will remove the block even if the block
	// break priority is MONITOR. This setting is only used (so far) with the
	// BlockConverters Event Triggers to bypass prison processing, but will allow prison
	// to remove the block and then treat it like a MONITOR event so it gets counted.
	private boolean forceBlockRemoval = false;
	
	
	private BlockBreakPriority bbPriority;
	
//	@Deprecated
//	private boolean monitor = false;
//	// replace with BlockBreakPriority
//	
//	
//	// blockEventsOnly was intended to be able to run the block events when 
//	// the the AutoManager is disabled.  But now, as of 2021-11-23, if 
//	// AutoManager is disabled, then nothing related to auto features, 
//	// including block events will be active.
//	@Deprecated
//	private boolean blockEventsOnly = false;
//	// replace with BlockBreakPriority bbPriority
	
	
	
	// Normally the explosion will ONLY work if the center target block was non-AIR.
	// This setting allows the explosion to be processed even if it is air.
	private boolean forceIfAirBlock = false;
	
	
	private List<SpigotItemStack> bukkitDrops;
	
	private int preventedDrops;
	
	private List<Block> unprocessedRawBlocks;
	
	
	private boolean applyToPlayersBlockCount;
	
	
	private StringBuilder debugInfo;
	private boolean forceDebugLogging;

	
	public PrisonMinesBlockBreakEvent( 
				MinesEventResults eventResults,
//				Block theBlock, Player player, 
//				Mine mine,
//				SpigotBlock spigotBlock, SpigotPlayer spigotPlayer,
//				BlockBreakPriority bbPriority,
//				boolean monitor, boolean blockEventsOnly,
				BlockEventType blockEventType, 
				String triggered,
				StringBuilder debugInfo ) {
		
		super( eventResults.getBlock(), eventResults.getSpigotPlayer().getWrapper() );
//		super( theBlock, player );

		this.mine = eventResults.getMine();
//		this.mine = mine;
		
		// Need to wrap in a Prison block so it can be used with the mines:
//		SpigotBlock sBlock = SpigotBlock.getSpigotBlock( theBlock );
//		SpigotPlayer sPlayer = new SpigotPlayer( player );

		this.spigotBlock = eventResults.getSpigotBlock();
		this.spigotPlayer = eventResults.getSpigotPlayer();
//		this.spigotBlock = sBlock;
//		this.spigotPlayer = sPlayer;
		
		this.itemInHand = SpigotCompatibility.getInstance()
				.getPrisonItemInMainHand( eventResults.getSpigotPlayer().getWrapper() );
		
		this.bbPriority = eventResults.getBbPriority();
//		this.bbPriority = bbPriority;
//		this.monitor = monitor;
//		this.blockEventsOnly = blockEventsOnly;
		
		this.blockEventType = blockEventType;
		this.triggered = triggered;
		
		this.mineBomb = null;
		
		this.explodedBlocks = new ArrayList<>();
		this.targetExplodedBlocks = new ArrayList<>();

		this.unprocessedRawBlocks = new ArrayList<>();
		
		this.bukkitDrops = new ArrayList<>();
		
		this.preventedDrops = 0;
		
		this.debugInfo = new StringBuilder();
		setDebugColorCodeDebug();
		this.debugInfo.append( debugInfo );
		
		this.forceDebugLogging = false;
		
		this.applyToPlayersBlockCount = true;
		
	}
	
	public PrisonMinesBlockBreakEvent( Block theBlock, Player player, 
			Mine mine, SpigotBlock spigotBlock, 
			List<SpigotBlock> explodedBlocks, 
			BlockEventType blockEventType,
			String triggered,
			StringBuilder debugInfo
			)
	{
		super( theBlock, player );
		
		this.spigotBlock = spigotBlock;
		this.spigotPlayer = new SpigotPlayer( player );
		
		this.itemInHand = SpigotCompatibility.getInstance().getPrisonItemInMainHand( player );

		this.mine = mine;
		
		this.explodedBlocks = explodedBlocks;
		
		this.blockEventType = blockEventType;
		this.targetExplodedBlocks = new ArrayList<>();

		this.triggered = triggered;
		
		this.mineBomb = null;
		
		this.bukkitDrops = new ArrayList<>();
		
		this.preventedDrops = 0;
		
		this.debugInfo = debugInfo;
		
		this.applyToPlayersBlockCount = true;
		
	}

	/**
	 * <p>Prison tracks what each block in the mine should be when it resets the mines.
	 * This allows prison to identify if a block has already been broken, and also
	 * allows prison to quickly get the stats on that kind of a block.
	 * </p>
	 * 
	 * <p>Prison tracks the block stats based upon the list of blocks that will be
	 * generated within the mine.  There is only one instance of that block for a 
	 * given mine, and its stats will be saved periodically.  To get an instance
	 * of that block to inspect the stats directly, you can use this function
	 * by passing it a SpigotBlock object which will identify the blocks actual 
	 * location within the world and it's x, y, and z coordinates.  If the
	 * block exists within a mine, then it will have a corresponding MineTargetPrisonBlock
	 * object.  Use <pre>targetBlock.getPrisonBlock()</pre> to get that block
	 * which is of type <pre>PrisonBlockStatusData</pre> which is identical for both
	 * the old prison block model and the new prison block model.
	 * </p>
	 * 
	 * <p>You should not make any changes to any objects you get from the prison 
	 * resources without understanding how they are being used.  The risks can
	 * vary, but most operations may not be thread safe especially if a component 
	 * was never designed to be accessed concurrently through multiple threads.
	 * You can do what you need to do, but keep in mind that if intermittent 
	 * failures start to be observed, then that could be a sign of a thread
	 * safety issue.  Prison does not employ thread locking or synchronization
	 * due to the additional overhead and potential resource starving and lockouts 
	 * that may happen if not used properly.  If this remains an issue for you,
	 * or you think it may become an issue, then please contact Blue (myself) and
	 * we can figure out a safe and performant way to address your needs.
	 * </p>
	 * 
	 * @param spigotBlock
	 * @return
	 */
	public MineTargetPrisonBlock getOriginalTargetBlock( SpigotBlock spigotBlock ) {
		
		MineTargetPrisonBlock targetBlock = mine.getTargetPrisonBlock( spigotBlock );
		
		return targetBlock;
	}
	
	public MineTargetPrisonBlock getOriginalTargetBlock( Block bukkitBlock ) {
		
		SpigotBlock spigotBlock = SpigotBlock.getSpigotBlock(bukkitBlock);
		
		return getOriginalTargetBlock( spigotBlock );
	}
	
	
	public TreeMap<String, Integer> getTargetBlockCounts() {
		TreeMap<String, Integer> results = new TreeMap<>();
		
		if ( getTargetBlock() != null ) {
			results.put( 
					getTargetBlock().getPrisonBlock().getBlockType() == PrisonBlockType.minecraft ?
							getTargetBlock().getPrisonBlock().getBlockName() : 
							getTargetBlock().getPrisonBlock().getBlockType() + ":" + getTargetBlock().getPrisonBlock().getBlockName()
					, 1 );
		}
		
		for ( MineTargetPrisonBlock targetBlock : getTargetExplodedBlocks() )
		{
			String blockName =
					targetBlock.getPrisonBlock().getBlockType() == PrisonBlockType.minecraft ?
							targetBlock.getPrisonBlock().getBlockName() : 
								targetBlock.getPrisonBlock().getBlockType() + ":" + targetBlock.getPrisonBlock().getBlockName();
							
							// targetBlock.getPrisonBlock().getBlockName();
			
			int count = 1 + ( results.containsKey( blockName ) ? results.get( blockName ) : 0) ;
			results.put( blockName, count );
		}
		
		return results;
	}
	
	

	/**
	 * <p>This will perform a Prison's sellall on a player's inventory.
	 * This tracks how many nanoseconds it took to run and will log this
	 * information if Prison is in debug mode.  This info will be appended
	 * to the debug info for the block break handling.
	 * </p>
	 * 
	 * @param description
	 */
	public void performSellAllOnPlayerInventoryLogged( String description ) {

		debugInfo.append( performSellAllOnPlayerInventoryString(description) );
		
//		final long nanoStart = System.nanoTime();
//		boolean success = SellAllUtil.get().sellAllSell( getPlayer(), 
//								false, false, false, true, true, false);
//		final long nanoStop = System.nanoTime();
//		double milliTime = (nanoStop - nanoStart) / 1000000d;
//		
//		DecimalFormat dFmt = Prison.get().getDecimalFormat("#,##0.00");
//		debugInfo.append( "(" )
//				.append( description)
//				.append( ": " + (success ? "success" : "failed"))
//				.append( " ms: " + dFmt.format( milliTime ) + ") ");
		
		PlayerAutoRankupTask.autoSubmitPlayerRankupTask( getSpigotPlayer(), debugInfo );
		
	}
	public String performSellAllOnPlayerInventoryString( String description ) {
		StringBuilder sb = new StringBuilder();
		
		if ( SpigotPrison.getInstance().isSellAllEnabled() ) {
			
			final long nanoStart = System.nanoTime();
			
			boolean isUsingSign = false;
			boolean completelySilent = false;
			boolean notifyPlayerEarned = false;
			boolean notifyPlayerDelay = false;
			boolean notifyPlayerEarningDelay = true;
			boolean playSoundOnSellAll = false;
			boolean notifyNothingToSell = false;
			
			boolean success = SellAllUtil.get().sellAllSell( getPlayer(), 
					isUsingSign, completelySilent, 
					notifyPlayerEarned, notifyPlayerDelay, 
					notifyPlayerEarningDelay, playSoundOnSellAll, 
					notifyNothingToSell, null );
			final long nanoStop = System.nanoTime();
			double milliTime = (nanoStop - nanoStart) / 1000000d;
			
			DecimalFormat dFmt = Prison.get().getDecimalFormat("#,##0.00");
			
			sb.append( "(" )
				.append( description)
				.append( ": " + (success ? "success" : "failed"))
				.append( " ms: " + dFmt.format( milliTime ) + ") ");
		}
		
		return sb.toString();
	}
	
	
	public Mine getMine() {
		return mine;
	}
	public void setMine( Mine mine ) {
		this.mine = mine;
	}

	public SpigotBlock getSpigotBlock() {
		return spigotBlock;
	}
	public void setSpigotBlock( SpigotBlock spigotBlock ) {
		this.spigotBlock = spigotBlock;
	}

	public MineTargetPrisonBlock getTargetBlock() {
		return targetBlock;
	}
	public void setTargetBlock( MineTargetPrisonBlock targetBlock ) {
		this.targetBlock = targetBlock;
	}

	public SpigotPlayer getSpigotPlayer() {
		return spigotPlayer;
	}
	public void setSpigotPlayer( SpigotPlayer spigotPlayer ) {
		this.spigotPlayer = spigotPlayer;
	}

	public SpigotItemStack getItemInHand() {
		return itemInHand;
	}
	public void setItemInHand( SpigotItemStack itemInHand ) {
		this.itemInHand = itemInHand;
	}

	public List<SpigotBlock> getExplodedBlocks() {
		return explodedBlocks;
	}
	public void setExplodedBlocks( List<SpigotBlock> explodedBlocks ) {
		this.explodedBlocks = explodedBlocks;
	}

	public List<MineTargetPrisonBlock> getTargetExplodedBlocks() {
		return targetExplodedBlocks;
	}
	public void setTargetExplodedBlocks( List<MineTargetPrisonBlock> targetBlocks ) {
		this.targetExplodedBlocks = targetBlocks;
	}

	public List<SpigotItemStack> getBukkitDrops() {
		return bukkitDrops;
	}
	public void setBukkitDrops( List<SpigotItemStack> drops ) {
		this.bukkitDrops = drops;
	}

	public int getPreventedDrops() {
		return preventedDrops;
	}
	public void setPreventedDrops(int preventedDrops) {
		this.preventedDrops = preventedDrops;
	}

	public BlockEventType getBlockEventType() {
		return blockEventType;
	}
	public void setBlockEventType( BlockEventType blockEventType ) {
		this.blockEventType = blockEventType;
	}

	public String getTriggered() {
		return triggered;
	}
	public void setTriggered( String triggered ) {
		this.triggered = triggered;
	}

//	public SpigotBlock getOverRideSpigotBlock() {
//		return overRideSpigotBlock;
//	}
//	public void setOverRideSpigotBlock( SpigotBlock overRideSpigotBlock ) {
//		this.overRideSpigotBlock = overRideSpigotBlock;
//	}
	
	public boolean isCancelOriginalEvent() {
		return cancelOriginalEvent;
	}
	public void setCancelOriginalEvent( boolean cancelOriginalEvent ) {
		this.cancelOriginalEvent = cancelOriginalEvent;
	}

	public boolean isCalculateDurability() {
		return calculateDurability;
	}
	public void setCalculateDurability( boolean calculateDurability ) {
		this.calculateDurability = calculateDurability;
	}

	public boolean isForceAutoSell() {
		return forceAutoSell;
	}
	public void setForceAutoSell( boolean forceAutoSell ) {
		this.forceAutoSell = forceAutoSell;
	}
	
	public boolean isForceBlockRemoval() {
		return forceBlockRemoval;
	}
	public void setForceBlockRemoval(boolean forceBlockRemoval) {
		this.forceBlockRemoval = forceBlockRemoval;
	}

	public BlockBreakPriority getBbPriority() {
		return bbPriority;
	}
	public void setBbPriority( BlockBreakPriority bbPriority ) {
		this.bbPriority = bbPriority;
	}

//	public boolean isMonitor() {
//		return monitor;
//	}
//	public void setMonitor( boolean monitor ) {
//		this.monitor = monitor;
//	}
//	
//	public boolean isBlockEventsOnly() {
//		return blockEventsOnly;
//	}
//	public void setBlockEventsOnly( boolean blockEventsOnly ) {
//		this.blockEventsOnly = blockEventsOnly;
//	}

	public List<Block> getUnprocessedRawBlocks() {
		return unprocessedRawBlocks;
	}
	public void setUnprocessedRawBlocks( List<Block> unprocessedRawBlocks ) {
		this.unprocessedRawBlocks = unprocessedRawBlocks;
	}

	public boolean isForceIfAirBlock() {
		return forceIfAirBlock;
	}
	public void setForceIfAirBlock( boolean forceIfAirBlock ) {
		this.forceIfAirBlock = forceIfAirBlock;
	}

	public boolean isApplyToPlayersBlockCount() {
		return applyToPlayersBlockCount;
	}
	public void setApplyToPlayersBlockCount(boolean applyToPlayersBlockCount) {
		this.applyToPlayersBlockCount = applyToPlayersBlockCount;
	}

	@Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
	public MineBombData getMineBomb() {
		return mineBomb;
	}
	public void setMineBomb( MineBombData mineBomb ) {
		this.mineBomb = mineBomb;
	}

	public StringBuilder getDebugInfo() {
		return debugInfo;
	}
	public void setDebugInfo(StringBuilder debugInfo) {
		this.debugInfo = debugInfo;
	}

	public boolean isForceDebugLogging() {
		return forceDebugLogging;
	}
	public void setForceDebugLogging(boolean forceDebugLogging) {
		this.forceDebugLogging = forceDebugLogging;
	}

	public void setDebugColorCodeInfo() {
		getDebugInfo().append( Output.get().getColorCodeInfo() );
	}
	
	public void setDebugColorCodeWarning() {
		getDebugInfo().append( Output.get().getColorCodeWarning() );
	}
	
	public void setDebugColorCodeError() {
		getDebugInfo().append( Output.get().getColorCodeError() );
	}
	
	public void setDebugColorCodeDebug() {
		getDebugInfo().append( Output.get().getColorCodeDebug() );
	}
}
