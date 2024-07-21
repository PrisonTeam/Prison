package tech.mcprison.prison.bombs.animations;

import java.text.DecimalFormat;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.bombs.MineBombData;
import tech.mcprison.prison.bombs.MineBombs;
import tech.mcprison.prison.internal.ArmorStand;
import tech.mcprison.prison.internal.EulerAngle;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.util.BluesSemanticVersionComparator;
import tech.mcprison.prison.util.Location;
import tech.mcprison.prison.util.Text;

public abstract class BombAnimations {
	
	private int id;
	
	private BombAnimationsTask task;
	
	private MineBombData bomb;
	private PrisonBlock sBlock;
	private ItemStack item;
	
	private Location originalLocation;
	
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
		
		this.originalLocation = null;
		
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
		
		Location location =
				getBomb().getPlacedBombLocation() != null ?
						getBomb().getPlacedBombLocation() :
						getsBlock().getLocation();
				
		location.setY( location.getY() + 2.5 );
		
		setOriginalLocation( location );
		
		
		// NOTE: The direction the entity is facing is based upon yaw.  
		//       When using org.bukkit.Location.setDirection() it's using a vector
		//       to set yaw and pitch so it's looking at that vector point. 
		// So... I guess that means an entity cannot turn their head?
		//       For entities, or at least armorStands, there is an Euler angle 
		//       based getHeadPose() and setHeadPose() functions.
		location.setYaw( entityYaw );  
		
		
		// Not sure why 90 was being added.  I suspect 0 is straight up?
		location.setPitch( entityPitch  + 90 );
		
		
		
//		double startingAngle = ( 360d / entityYaw ) * twoPI;
//		location.setDirection( startingAngle );
		
		// eulerAngleX += startingAngle;
		
		EulerAngle arm = new EulerAngle( 
						eulerAngleX, 
						eulerAngleY, 
						eulerAngleZ );
		
//		EulerAngle arm = new EulerAngle( 
//				eulerAngleX + startingAngle, 
//				eulerAngleY + startingAngle, 
//				eulerAngleZ + startingAngle );
		
		
		// Spawn an invisible armor stand:
		armorStand = location.spawnArmorStand(
						getBomb().getItemType(),
//						(getItem() == null ? null : getBomb().getItemType()),
						getBomb().getName() );
		
//						MineBombs.MINE_BOMBS_NBT_KEY, getBomb().getName() );
		
		
//		armorStand = location.spawnArmorStand();
		
		if ( armorStand != null ) {
			
//			Output.get().logInfo( "### init mine bomb 1: id: " + getId() + "  " + 
//						armorStand.getLocation().toString() );
			
			// Need to teleport the armor stand to the actual location where it
			// landed or was thrown to, otherwise it will look like it's offset.
			//armorStand.teleport( getOriginalLocation() );
			
//			Output.get().logInfo( "### init mine bomb 2: id: " + getId() + "  " + 
//					armorStand.getLocation().toString() );

			// Sets visibility:false, arms:true, basePlate:false, canPickupItems:false, 
			// removeWhenFar:false, gravity:false, and itemInHand.
			// armorStand.setupArmorStand( getBomb().getItemType() );
//			armorStand.setupArmorStand( getItem() );
			
			armorStand.setCustomNameVisible( getId() == 0 && initializeCustomName() );
			
			armorStand.setNbtString( MineBombs.MINE_BOMBS_NBT_KEY, getBomb().getName() );
			
//			armorStand.setNbtString( MineBombs.MINE_BOMBS_NBT_THROWER_UUID, 
//					playerUUID );
			
			armorStand.setSmall( getBomb().isSmall() );
			
			armorStand.setRightArmPose(arm);
			
//			ItemStack itemInHand = armorStand.getItemInHand();
//			
//			int iihAmount = itemInHand.getAmount();
//			int iamount = getItem().getAmount();
//			
//			int count = iihAmount + iamount;
//			
//			armorStand.setArms( true );
//			armorStand.setBasePlate( false );
//			armorStand.setCanPickupItems( false );
//			
//			
//			armorStand.setItemInHand( getItem() );
			
//			armorStand.setRemoveWhenFarAway(false);
			
			if ( new BluesSemanticVersionComparator().compareMCVersionTo( "1.9.0" ) >= 0 ) {
				
				armorStand.setGlowing( getBomb().isGlowing() );
				
				// setGravity is invalid for spigot 1.8.8:
				armorStand.setGravity( getBomb().isGravity() );
			}
		}
		
//		if ( Output.get().isDebug() ) {
//			String msg = String.format( 
//					"### BombAnimation.initializeArmorStand : id: %s  %s  %s", 
//					Integer.toString(getId()),
//					bomb.getAnimationPattern().name(),
//					armorStand.getLocation().toString()
//					);
//			
//			Output.get().logInfo( msg );
//		}
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

	public Location getOriginalLocation() {
		return originalLocation;
	}
	public void setOriginalLocation(Location originalLocation) {
		this.originalLocation = originalLocation;
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
