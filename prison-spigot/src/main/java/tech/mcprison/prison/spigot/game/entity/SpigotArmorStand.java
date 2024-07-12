package tech.mcprison.prison.spigot.game.entity;

import tech.mcprison.prison.internal.Entity;
import tech.mcprison.prison.internal.EulerAngle;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.spigot.block.SpigotItemStack;

public class SpigotArmorStand 
	extends SpigotEntity
	implements tech.mcprison.prison.internal.ArmorStand
{
	
	private org.bukkit.entity.ArmorStand bArmorStand;
	
	public SpigotArmorStand( org.bukkit.entity.ArmorStand bArmorStand ) {
		super( bArmorStand );
		
		bArmorStand.setVisible( false );
		
		this.bArmorStand = bArmorStand;
	}

//	public static ArmorStand spawn( tech.mcprison.prison.util.Location location ) {
//		tech.mcprison.prison.internal.Entity e = location.spawnEntity( SpigotEntityType.ENTITY_TYPE_ARMOR_STAND );
//		SpigotEntity sEntity = (SpigotEntity) e;
//		
//		org.bukkit.entity.ArmorStand armorStand = (org.bukkit.entity.ArmorStand) sEntity.getBukkitEntity();
//		SpigotArmorStand sArmorStand = new SpigotArmorStand( armorStand );
//		
//		return sArmorStand;
//	}

	public SpigotArmorStand(Entity entity) {
		this( (org.bukkit.entity.ArmorStand) ((SpigotEntity) entity).getBukkitEntity() );
		
		org.bukkit.entity.Entity bEntity = ((SpigotEntity) entity).getBukkitEntity(); 
		if ( bEntity instanceof org.bukkit.entity.ArmorStand ) {
			this.bArmorStand = (org.bukkit.entity.ArmorStand) bEntity;
			this.bArmorStand.setVisible( false );
		}
	}

	@Override
	public boolean isVisible() {
		return bArmorStand.isVisible();
	}

	@Override
	public void setVisible( boolean visible ) {
		bArmorStand.setVisible( visible );
	}
	
	@Override
	public void setCustomNameVisible( boolean visible ) {
		bArmorStand.setCustomNameVisible( visible );
	}
	
	@Override
	public void setCustomName( String customName ) {
		
		bArmorStand.setCustomName( customName );
	}

	@Override
	public boolean getRemoveWhenFarAway() {
		return bArmorStand.getRemoveWhenFarAway();
	}

	@Override
	public void setRemoveWhenFarAway(boolean removeWhenFarAway) {
		bArmorStand.setRemoveWhenFarAway( removeWhenFarAway );
	}

	@Override
	public ItemStack getItemInHand() {
		return new SpigotItemStack( bArmorStand.getItemInHand() );
	}

	@Override
	public void setItemInHand(ItemStack item) {
		SpigotItemStack sItemStack = (SpigotItemStack) item;
		bArmorStand.setItemInHand( sItemStack.getBukkitStack() );
	}

	@Override
	public void setRightArmPose(EulerAngle arm) {
		
		org.bukkit.util.EulerAngle eAngle = new org.bukkit.util.EulerAngle(
				arm.getX(), arm.getY(), arm.getZ() );

		bArmorStand.setRightArmPose(eAngle);
	}

	@Override
	public boolean isGlowing() {
		return bArmorStand.isGlowing();
	}

	@Override
	public void setGlowing(boolean glowing) {
		bArmorStand.setGlowing(glowing);
	}

	@Override
	public boolean hasGravity() {
		return bArmorStand.hasGravity();
	}

	@Override
	public void setGravity(boolean gravity) {
		bArmorStand.setGravity(gravity);
	}
	
	
}
