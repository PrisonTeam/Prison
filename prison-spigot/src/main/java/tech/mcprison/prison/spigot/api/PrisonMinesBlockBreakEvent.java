package tech.mcprison.prison.spigot.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;

import tech.mcprison.prison.internal.block.MineTargetPrisonBlock;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.features.MineBlockEvent.BlockEventType;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.spigot.compat.SpigotCompatibility;
import tech.mcprison.prison.spigot.game.SpigotPlayer;

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
	
	private boolean cancelOriginalEvent = false;
	private boolean monitor = false;
	private boolean blockEventsOnly = false;
	
	
	// Normally the explosion will ONLY work if the center target block was non-AIR.
	// This setting allows the explosion to be processed even if it is air.
	private boolean forceIfAirBlock = false;
	
	
	private List<SpigotItemStack> bukkitDrops;
	
	private List<Block> unprocessedRawBlocks;
	

	public PrisonMinesBlockBreakEvent( Block theBlock, Player player, 
			SpigotBlock spigotBlock, SpigotPlayer spigotPlayer,
			boolean monitor, boolean blockEventsOnly,
			BlockEventType blockEventType, String triggered) {
		
		super( theBlock, player );

		this.spigotBlock = spigotBlock;
		this.spigotPlayer = spigotPlayer;
		
		this.itemInHand = SpigotCompatibility.getInstance().getPrisonItemInMainHand( player );
		
		this.monitor = monitor;
		this.blockEventsOnly = blockEventsOnly;
		
		this.blockEventType = blockEventType;
		this.triggered = triggered;
		
		this.explodedBlocks = new ArrayList<>();
		this.targetExplodedBlocks = new ArrayList<>();

		this.unprocessedRawBlocks = new ArrayList<>();
		
		this.bukkitDrops = new ArrayList<>();
		
	}
	
	public PrisonMinesBlockBreakEvent( Block theBlock, Player player, 
			Mine mine, SpigotBlock spigotBlock, 
			List<SpigotBlock> explodedBlocks, 
			BlockEventType blockEventType,
			String triggered )
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
		
		this.bukkitDrops = new ArrayList<>();
		
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
		
		SpigotBlock spigotBlock = new SpigotBlock(bukkitBlock);
		
		return getOriginalTargetBlock( spigotBlock );
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

	public boolean isMonitor() {
		return monitor;
	}
	public void setMonitor( boolean monitor ) {
		this.monitor = monitor;
	}
	
	public boolean isBlockEventsOnly() {
		return blockEventsOnly;
	}
	public void setBlockEventsOnly( boolean blockEventsOnly ) {
		this.blockEventsOnly = blockEventsOnly;
	}

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

	@Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

}
