package tech.mcprison.prison.spigot.api;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.features.MineTargetPrisonBlock;
import tech.mcprison.prison.mines.features.MineBlockEvent.BlockEventType;
import tech.mcprison.prison.spigot.block.SpigotBlock;

/**
 * <p>This is an event that prison calls before processing the blocks that
 * are within the mines.  It is cancelable.
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

	private Mine mine;
	private SpigotBlock spigotBlock;

	//private SpigotBlock overRideSpigotBlock;
	
	private List<SpigotBlock> explodedBlocks;
	
	private BlockEventType blockEventType;
	private String triggered;

	public PrisonMinesBlockBreakEvent( Block theBlock, Player player, 
			Mine mine, SpigotBlock spigotBlock, 
			List<SpigotBlock> explodedBlocks, 
			BlockEventType blockEventType,
			String triggered )
	{
		super( theBlock, player );
		
		this.mine = mine;
		this.spigotBlock = spigotBlock;
		
		this.explodedBlocks = explodedBlocks;
		
		this.blockEventType = blockEventType;
		this.triggered = triggered;
		
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

}
