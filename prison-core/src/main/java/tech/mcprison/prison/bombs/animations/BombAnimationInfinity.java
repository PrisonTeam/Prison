package tech.mcprison.prison.bombs.animations;

import tech.mcprison.prison.bombs.MineBombData;
import tech.mcprison.prison.bombs.MineBombs;
import tech.mcprison.prison.internal.ArmorStand;
import tech.mcprison.prison.internal.EulerAngle;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.util.BluesSemanticVersionComparator;
import tech.mcprison.prison.util.Location;

public class BombAnimationInfinity
		extends BombAnimations {

	
	private double eulerAngleX = 1.0;
	private double eulerAngleY = 0;
	private double eulerAngleZ = 0;
	
	private double twoPI;
	
	private ArmorStand armorStand;

	private float entityYaw;
	private float entityPitch;
	
	private int id;
	
	
	public BombAnimationInfinity( MineBombData bomb, 
									PrisonBlock sBombBlock, ItemStack item,
									BombAnimationsTask task,
									float entityYaw, float entityPitch ) {
		super( bomb, sBombBlock, item, task );
			
		this.entityYaw = entityYaw;
		this.entityPitch = entityPitch;
		
		initialize();
	}


	
	public void initialize() {
		
		this.twoPI = Math.PI * 2;
		
		setId( getTask().getAnimators().size() );
		
		initializeArmorStand();
	}

	private void initializeArmorStand() {
		
		Location location = getsBlock().getLocation();
		location.setY( location.getY() + 2.5 );
		
		location.setYaw( entityYaw );
		location.setPitch( entityPitch + 90 );
		
		
		double startingAngle = ( 360d / entityYaw ) * twoPI;
		
//		eulerAngleX += startingAngle;
		
		EulerAngle arm = new EulerAngle( eulerAngleX + startingAngle, eulerAngleY + startingAngle, eulerAngleZ + startingAngle );
		
		
		armorStand = location.spawnArmorStand();
		
		armorStand.setNbtString( MineBombs.MINE_BOMBS_NBT_KEY, getBomb().getName() );
		
		if ( armorStand != null ) {
			
			armorStand.setVisible(false);
			armorStand.setRemoveWhenFarAway(false);
			armorStand.setItemInHand( getItem() );
			armorStand.setRightArmPose(arm);
			
			
			if ( initializeCustomName() ) {
				
				//updateArmorStandCustomName();
				armorStand.setCustomName( getTagName() );
				armorStand.setCustomNameVisible(true);
			}
			else {
				
				armorStand.setCustomNameVisible(false);
			}
			
			if ( new BluesSemanticVersionComparator().compareMCVersionTo( "1.9.0" ) >= 0 ) {
				
				armorStand.setGlowing( getBomb().isGlowing() );
				
				// setGravity is invalid for spigot 1.8.8:
				armorStand.setGravity( getBomb().isGravity() );
			}
		}
		
		if ( Output.get().isDebug() ) {
			String msg = String.format( 
					"### BombAnimationInfinity.initializeArmorStand : id: %s ", 
					Integer.toString(getId())
					
					);
			Output.get().logInfo( msg );
		}
	}




	@Override
	public void step()
	{
		
		double speed = 0.35;
		
		eulerAngleX += speed;
		eulerAngleY += speed / 3;
		eulerAngleZ += speed / 5;
		
		
		EulerAngle arm = new EulerAngle( eulerAngleX, eulerAngleY, eulerAngleZ );
		
		armorStand.setRightArmPose(arm);
		
		
		if ( eulerAngleX > twoPI ) {
			eulerAngleX -= twoPI;
		}
		if ( eulerAngleY > twoPI ) {
			eulerAngleY -= twoPI;
		}
		if ( eulerAngleZ > twoPI ) {
			eulerAngleZ -= twoPI;
		}
		

		// Track the time that this has lived:
		if ( --terminateOnZeroTicks == 0 || !armorStand.isValid() ) {
			
			armorStand.remove();
			
			cancel();
		}
		
		updateCustomName();
		
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
