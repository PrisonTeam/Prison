package tech.mcprison.prison.bombs.animations;

import java.text.DecimalFormat;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.bombs.MineBombData;
import tech.mcprison.prison.bombs.MineBombs;
import tech.mcprison.prison.internal.ArmorStand;
import tech.mcprison.prison.internal.EulerAngle;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.util.BluesSemanticVersionComparator;
import tech.mcprison.prison.util.Location;
import tech.mcprison.prison.util.Text;

public abstract class BombAnimations {
	
	private int id;
	
	private BombAnimationsTask task;
	
	private MineBombData bomb;
	private PrisonBlock sBlock;
	private ItemStack item;
	
	private String customName;
	private boolean isDyanmicTag = false;
	private String tagName;

//	long ageTicks = 0L;
	long terminateOnZeroTicks = 0L;
	
	private ArmorStand armorStand;
	
	private double eulerAngleX = 1.0;
	private double eulerAngleY = 0;
	private double eulerAngleZ = 0;
	
	private double twoPI;
	
	private float entityYaw;
	private float entityPitch;
	
	private DecimalFormat dFmt;
	
	
	public BombAnimations( MineBombData bomb, 
			PrisonBlock sBombBlock, 
			ItemStack item,
			BombAnimationsTask task,
			float entityYaw, 
			float entityPitch ) {
		super();
		
		// Used for "canceling" and detonating the task:
		this.task = task;
		
		setId( getTask().getAnimators().size() );
		
		this.bomb = bomb;
		this.sBlock = sBombBlock;
		this.item = item;
		
//		this.ageTicks = 0;
		this.terminateOnZeroTicks = getTaskLifeSpan();

		this.isDyanmicTag = bomb.getNameTag() != null &&
							bomb.getNameTag().contains( "{countdown}" );
		this.tagName = "";
		

		this.entityYaw = entityYaw;
		this.entityPitch = entityPitch;
		
		this.twoPI = Math.PI * 2;
		
		this.dFmt = Prison.get().getDecimalFormat( "0.0" );

		initialize();
	}
	
	protected void cancel() {
		task.cancel();
	}
	
	public void initialize() {
		
		Location location = getsBlock().getLocation();
		location.setY( location.getY() + 3 );
		
		location.setYaw( entityYaw );
		location.setPitch( entityPitch + 90 );
		
		
		double startingAngle = ( 360d / entityYaw ) * twoPI;
		
		// eulerAngleX += startingAngle;
		
		EulerAngle arm = new EulerAngle( 
						eulerAngleX + startingAngle, 
						eulerAngleY + startingAngle, 
						eulerAngleZ + startingAngle );
		
		
		armorStand = location.spawnArmorStand();
		
		if ( armorStand != null ) {
			
			armorStand.setVisible(false);
			
			armorStand.setNbtString( MineBombs.MINE_BOMBS_NBT_KEY, getBomb().getName() );
			
			armorStand.setRightArmPose(arm);
			armorStand.setItemInHand( getItem() );
			
			armorStand.setCustomNameVisible( getId() == 0 && initializeCustomName() );
			armorStand.setRemoveWhenFarAway(false);
			
			if ( new BluesSemanticVersionComparator().compareMCVersionTo( "1.9.0" ) >= 0 ) {
				
				armorStand.setGlowing( getBomb().isGlowing() );
				
				// setGravity is invalid for spigot 1.8.8:
				armorStand.setGravity( getBomb().isGravity() );
			}
		}
		
		if ( Output.get().isDebug() ) {
			String msg = String.format( 
					"### BombAnimation.initializeArmorStand : id: %s  %s ", 
					Integer.toString(getId()),
					bomb.getAnimationPattern().name()
					);
			
			Output.get().logInfo( msg );
		}
	}


	public abstract void stepAnimation();
	
	public void step() {
		stepAnimation();
		
		if ( eulerAngleX > twoPI ) {
			eulerAngleX -= twoPI;
		}
		else if ( eulerAngleX < -twoPI ) {
			eulerAngleX += twoPI;
		}
		if ( eulerAngleY > twoPI ) {
			eulerAngleY -= twoPI;
		}
		else if ( eulerAngleY < -twoPI ) {
			eulerAngleY += twoPI;
		}
		if ( eulerAngleZ > twoPI ) {
			eulerAngleZ -= twoPI;
		}
		else if ( eulerAngleZ < -twoPI ) {
			eulerAngleZ += twoPI;
		}
		
		// Track the time that this has lived:
		if ( --terminateOnZeroTicks == 0 || !armorStand.isValid() ) {
			
			armorStand.remove();
			
			cancel();
		}
		
		if ( getId() == 0 ) {
			
			updateCustomName();
		}
		
	}
	
	
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
	
		String tName = null;
		
		if ( getBomb().getNameTag() == null || 
				getBomb().getNameTag().trim().isEmpty() ) {

			tName = getBomb().getName();
		}
		else {
			
			tName = getBomb().getNameTag();
			if ( tName.contains( "{name}" ) ) {
				tName = tName.replace( "{name}", getBomb().getName() );
			}
		}
		
		if ( tName != null ) {
			
			tName = Text.translateAmpColorCodes( tName );
			
			setTagName( tName );
			setCustomName( tName );
			armorStand.setCustomName( tName );
			
			updateCustomName();

			results = true;
		}
		
		return results;
	}

	protected void updateCustomName()
	{
		if ( isDyanmicTag ) {
		
			double countdown = (terminateOnZeroTicks / 20.0d);
			String cName = getTagName().replace( "{countdown}", dFmt.format( countdown) );
			
			setCustomName( cName );
			
			armorStand.setCustomName( cName );
			
//			setTagName( tagName );
//			armorStand.setCustomName( tagName );
		}
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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

	public String getCustomName() {
		return customName;
	}
	public void setCustomName(String customName) {
		this.customName = customName;
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

	public ArmorStand getArmorStand() {
		return armorStand;
	}
	public void setArmorStand(ArmorStand armorStand) {
		this.armorStand = armorStand;
	}

	public double getEulerAngleX() {
		return eulerAngleX;
	}
	public void setEulerAngleX(double eulerAngleX) {
		this.eulerAngleX = eulerAngleX;
	}

	public double getEulerAngleY() {
		return eulerAngleY;
	}
	public void setEulerAngleY(double eulerAngleY) {
		this.eulerAngleY = eulerAngleY;
	}

	public double getEulerAngleZ() {
		return eulerAngleZ;
	}
	public void setEulerAngleZ(double eulerAngleZ) {
		this.eulerAngleZ = eulerAngleZ;
	}

	public double getTwoPI() {
		return twoPI;
	}
	public void setTwoPI(double twoPI) {
		this.twoPI = twoPI;
	}

	public float getEntityYaw() {
		return entityYaw;
	}
	public void setEntityYaw(float entityYaw) {
		this.entityYaw = entityYaw;
	}

	public float getEntityPitch() {
		return entityPitch;
	}
	public void setEntityPitch(float entityPitch) {
		this.entityPitch = entityPitch;
	}
	
}
