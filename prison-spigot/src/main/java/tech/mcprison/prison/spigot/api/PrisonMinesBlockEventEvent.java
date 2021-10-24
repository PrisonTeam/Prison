package tech.mcprison.prison.spigot.api;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import tech.mcprison.prison.internal.block.MineTargetPrisonBlock;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.features.MineBlockEvent.BlockEventType;
import tech.mcprison.prison.spigot.block.SpigotBlock;

/**
 * <p>This is an event that prison calls before processing the BlockEvents that can 
 * be setup after a block(s) is(are) physically broken. This event is filtered by a 
 * defined random chance, an optional player permission, original BlockBreakType
 * (normal BlockBreakEvent, TokenEnchant explosions events, Crazy Enchant explosions 
 * events, etc).  
 * </p>
 * 
 * <p>This is called before applying the BlockBreak events. The block already was 
 * broken, the BlockBreak events are actions to take afterwards. If this event is
 * canceled, then it will not run any of the BlockBreak commands that are directly
 * tied to this entry.  Other BlockBreak events may run and cannot be canceled by
 * this event.  Note you can stack multiple command in one BlockBreak command by 
 * using semicolons.
 * </p>
 * 
 * <p>Please note, that the two prison events, PrisonMinesBlockEventEvent and 
 * PrisonMinesBlockBreakEvent are almost exactly the same, except for the class name.  
 * This is intentional since PrisonMinesBlockBreakEvent is fired before prison 
 * processes a block event, and PrisonMinesBlockEventEvent is fired before 
 * processing a BlockEvent that has passed it's primary filters.
 * </p>
 * 
 * <p>PrisonMinesBlockEventEvent also has a field called parameter.  It's a way to
 * target specific listeners if needed.  For example, to fire the PrisonMinesBlockEventEvent
 * you would use the placeholder {fireBlockEvent}.  But to pass a parameter, you 
 * would use {fireBlockEvent::rainbow-decay} as an example.  This is a sample 
 * prison event that you can actually include in your mines now.
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
public class PrisonMinesBlockEventEvent
//		extends BlockBreakEvent
{
	/**
	 * <p><b>Warning:</b> BlastUseEvent does not identify the block the player actually hit, so the dummyBlock
	 * is just a random first block from the explodedBlocks list and may not be the block
	 * that initiated the explosion event.  Such events are identified by 
	 * BlockEventType.CEXplosion.  Make sure you do not process the first block twice,
	 * which is also passed as the org.bukkit.block.Block and the SpigotBlock to prevent have to 
	 * pass nulls.  They are the exact same objects.  
	 * </p>
	 *
	 * Due to this behavior, since BlockBreakEvent's handlers is static, it 
	 * will "share" the handlers (listeners).  To prevent this unwanted behavior,
	 * since prison's BlockBreakEvent listeners will be called, this class
	 * defines and overrides the the handlers with it's own instance.
	 * 
	 */
private static final HandlerList handlers = new HandlerList();
	
	private Mine mine;
	private SpigotBlock spigotBlock;

	//private SpigotBlock overRideSpigotBlock;
	
	private List<SpigotBlock> explodedBlocks;
	
	private BlockEventType blockEventType;
	private String triggered;

	private String parameter;
	
	public PrisonMinesBlockEventEvent( Block theBlock, Player player, 
			Mine mine, SpigotBlock spigotBlock, 
			List<SpigotBlock> explodedBlocks, 
			BlockEventType blockEventType,
			String triggered, String parameter )
	{
//		super( theBlock, player );
//		
//		this.mine = mine;
//		this.spigotBlock = spigotBlock;
//		
//		this.explodedBlocks = explodedBlocks;
//		
//		this.blockEventType = blockEventType;
//		this.triggered = triggered;
//		
//		this.parameter = parameter;
		
		//this.overRideSpigotBlock = null;
		
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

	public List<SpigotBlock> getExplodedBlocks() {
		return explodedBlocks;
	}
	public void setExplodedBlocks( List<SpigotBlock> explodedBlocks ) {
		this.explodedBlocks = explodedBlocks;
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
	
	public String getParameter() {
		return parameter;
	}
	public void setParameter( String parameter ) {
		this.parameter = parameter;
	}

	
//	@Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

}
