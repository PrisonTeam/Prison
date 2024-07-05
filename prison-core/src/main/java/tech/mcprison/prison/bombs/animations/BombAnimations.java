package tech.mcprison.prison.bombs.animations;

import java.text.DecimalFormat;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.bombs.MineBombData;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.util.Text;

public abstract class BombAnimations {
	
	private BombAnimationsTask task;
	
	private MineBombData bomb;
	private PrisonBlock sBlock;
	private ItemStack item;
	
	
	private boolean isDyanmicTag = false;
	private String tagName;

//	long ageTicks = 0L;
	long terminateOnZeroTicks = 0L;
	
	private DecimalFormat dFmt;
	
	
	public BombAnimations( MineBombData bomb, 
			PrisonBlock sBombBlock, 
			ItemStack item,
			BombAnimationsTask task ) {
		super();
		
		// Used for "canceling" the task:
		this.task = task;
		
		this.bomb = bomb;
		this.sBlock = sBombBlock;
		this.item = item;
		
//		this.ageTicks = 0;
		this.terminateOnZeroTicks = getTaskLifeSpan();

		this.isDyanmicTag = bomb.getNameTag().contains( "{countdown}" );
		this.tagName = "";
		
		this.dFmt = Prison.get().getDecimalFormat( "0.0" );
		
		// initialize();
	}
	
	protected void cancel() {
		task.cancel();
	}
	
	public abstract void initialize();
	
	
	public abstract void step();
	
	
	/**
	 * <p>This will calculate how long the placed item needs to 
	 * exist before removal, and this task will remove itself.
	 * While this item is placed, this task will run every 2 
	 * ticks and will spin the item in 3d space.
	 * </p>
	 * 
	 * <p>Removal is based upon the fuseDelayTicks which will take it to
	 * the explosion, then scanning the final effects to find how long
	 * the last one will be submitted for.  Then add 15 ticks.
	 * </p>
	 * 
	 * <p>At this time, not 100% sure if this item or armor stand
	 * will be used to "place" the effects.  Probably not.  If it's not
	 * needed, then this can be removed when the explosions start.
	 * </p>
	 * 
	 * @return
	 */
	protected long getTaskLifeSpan()
	{
		int removeInTicks = bomb.getFuseDelayTicks() + bomb.getItemRemovalDelayTicks();
		return removeInTicks;
	}
	
	
	protected boolean initializeCustomName() {
		boolean results = false;
	
		if ( getBomb().getNameTag() != null && 
				!getBomb().getNameTag().trim().isEmpty() ) {
			
			String tagName = getBomb().getNameTag();
			if ( tagName.contains( "{name}" ) ) {
				tagName = tagName.replace( "{name}", getBomb().getName() );
			}
			setTagName( Text.translateAmpColorCodes( tagName ) );
		}

		return results;
	}

	protected void updateCustomName()
	{
		if ( isDyanmicTag ) {
		
			double countdown = (terminateOnZeroTicks / 20.0d);
			String tagName = this.tagName.replace( "{countdown}", dFmt.format( countdown) );
			
			setTagName( tagName );
//			armorStand.setCustomName( tagName );
		}
	}


	public BombAnimationsTask getTask() {
		return task;
	}
	public void setTask(BombAnimationsTask task) {
		this.task = task;
	}

	public MineBombData getBomb() {
		return bomb;
	}
	public void setBomb(MineBombData bomb) {
		this.bomb = bomb;
	}

	public PrisonBlock getsBlock() {
		return sBlock;
	}
	public void setsBlock(PrisonBlock sBlock) {
		this.sBlock = sBlock;
	}

	public ItemStack getItem() {
		return item;
	}
	public void setItem(ItemStack item) {
		this.item = item;
	}

	public boolean isDyanmicTag() {
		return isDyanmicTag;
	}
	public void setDyanmicTag(boolean isDyanmicTag) {
		this.isDyanmicTag = isDyanmicTag;
	}

	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public long getTerminateOnZeroTicks() {
		return terminateOnZeroTicks;
	}
	public void setTerminateOnZeroTicks(long terminateOnZeroTicks) {
		this.terminateOnZeroTicks = terminateOnZeroTicks;
	}
	
}
