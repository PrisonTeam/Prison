package tech.mcprison.prison.spigot.game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.cryptomorin.xseries.XEntityType;

import tech.mcprison.prison.internal.Entity;
import tech.mcprison.prison.internal.EntityType;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.util.Location;
import tech.mcprison.prison.util.Vector;

public class SpigotEntity
	extends SpigotCommandSender
	implements Entity {
		
	private org.bukkit.entity.Entity bukkitEntity;

	public SpigotEntity(org.bukkit.entity.Entity entity ) {
        super( entity );
        
        this.bukkitEntity = entity;
    }


	@Override
	public UUID getUniqueId() {
		return getBukkitEntity().getUniqueId();
	}

	@Override
	public String getCustomName() {
		return getBukkitEntity().getCustomName();
	}

	@Override
	public int getEntityId() {
		return getBukkitEntity().getEntityId();
	}

	@Override
	public boolean eject() {
		return getBukkitEntity().eject();
	}

	@Override
	public Location getLocation() {
		return new SpigotLocation( getBukkitEntity().getLocation() );
	}

	@Override
	public Location getLocation(Location loc) {
		return 
			new SpigotLocation( 
				getBukkitEntity().getLocation( ((SpigotLocation) loc).getBukkitLocation() ));
	}

	@Override
	public float getFallDistance() {
		return getBukkitEntity().getFallDistance();
	}

	@Override
	public int getFireTicks() {
		return getBukkitEntity().getFireTicks();
	}

	@Override
	public int getMaxFireTicks() {
		return getBukkitEntity().getMaxFireTicks();
	}

	/**
	 * The parameter of radius is actually a cubic-radius, 
	 * where the r is added to the location to produce a cube around the
	 * Entity or Player.
	 * 
	 * @param r Radius around the Entity or Player
	 * @param eType
	 * @return
	 */
	public List<Entity> getNearbyEntities( int r, EntityType eType ) {
		Location loc = getLocation();
		List<org.bukkit.entity.Entity> bEntities = getBukkitEntity()
				.getNearbyEntities( loc.getX() + r, loc.getY() + r, loc.getZ() + r);
		return convertEntities( bEntities, (SpigotEntityType) eType );
	}

	@Override
	public List<Entity> getNearbyEntities(double x, double y, double z) {
		List<org.bukkit.entity.Entity> bEntities = getBukkitEntity().getNearbyEntities(x, y, z);
		return convertEntities( bEntities, null );
	}

	/**
	 * Converts all of the bukkit entities in to the SpigotEntityType, and if
	 * a SpigotEntityType is provided, it will also filter the results.
	 * 
	 * @param bEntities
	 * @param seType
	 * @return
	 */
	private List<Entity> convertEntities(
				List<org.bukkit.entity.Entity> bEntities,
				SpigotEntityType seType ) {
		List<Entity> sEntities = new ArrayList<>();
		for (org.bukkit.entity.Entity entity : bEntities) {
			
			if ( seType == null ||
					seType.getxEType() == XEntityType.of(entity) ) {
				
				sEntities.add( new SpigotEntity( entity ));
			}
		}
		return sEntities;
	}



	@SuppressWarnings("deprecation")
	@Override
	public Entity getPassenger() {
		return new SpigotEntity( getBukkitEntity().getPassenger() );
	}

	@Override
	public int getTicksLived() {
		return getBukkitEntity().getTicksLived();
	}

	@Override
	public EntityType getType() {
		
		EntityType eType = new SpigotEntityType( getBukkitEntity().getType() );
		return eType;
	}

	@Override
	public Entity getVehicle() {
		return new SpigotEntity( getBukkitEntity().getVehicle() );
	}

	@Override
	public Vector getVelocity() {
		
		org.bukkit.util.Vector bVel = getBukkitEntity().getVelocity();
		Vector vec = new Vector( bVel.getX(), bVel.getY(), bVel.getZ() );
		
		return vec;
	}

	@Override
	public World getWorld() {
		return new SpigotWorld( getBukkitEntity().getWorld() );
	}

	@Override
	public boolean isCustomNameVisible() {
		return getBukkitEntity().isCustomNameVisible();
	}

	@Override
	public void setCustomName(String name) {
		getBukkitEntity().setCustomName(name);
	}

	@Override
	public void setCustomNameVisible(boolean flag) {
		getBukkitEntity().setCustomNameVisible(flag);
	}

	@Override
	public boolean isDead() {
		return getBukkitEntity().isDead();
	}

	@Override
	public boolean isEmpty() {
		return getBukkitEntity().isEmpty();
	}

	@Override
	public boolean isInsideVehicle() {
		return getBukkitEntity().isInsideVehicle();
	}

	@Override
	public boolean isOnGround() {
		return getBukkitEntity().isOnGround();
	}

	@Override
	public boolean isValid() {
		return getBukkitEntity().isValid();
	}

	@Override
	public boolean leaveVehicle() {
		return getBukkitEntity().leaveVehicle();
	}

	@Override
	public void remove() {
		getBukkitEntity().remove();
	}

	@Override
	public void setFallDistance(float distance) {
		getBukkitEntity().setFallDistance(distance);
	}

	@Override
	public void setFireTicks(int ticks) {
		getBukkitEntity().setFireTicks(ticks);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean setPassenger(Entity passenger) {
		return getBukkitEntity().setPassenger( ((SpigotEntity) passenger).getBukkitEntity() );
	}

	@Override
	public void setTicksLived(int value) {
		getBukkitEntity().setTicksLived(value);
	}

	@Override
	public void setVelocity(Vector velocity) {
		
		org.bukkit.util.Vector bVec = 
					new org.bukkit.util.Vector( 
							velocity.getX(), velocity.getY(), velocity.getZ() );
		
		getBukkitEntity().setVelocity( bVec );
	}

	@Override
	public boolean teleport(Entity destination) {
		return getBukkitEntity().teleport( ((SpigotEntity) destination).getBukkitEntity() );
	}

	@Override
	public boolean teleport(Location location) {
		
		org.bukkit.World world = ((SpigotWorld) location.getWorld()).getWrapper();
		org.bukkit.Location loc = new org.bukkit.Location( world,
						location.getX(), location.getY(), location.getZ(),
						location.getYaw(), location.getPitch());
		return getBukkitEntity().teleport( loc );
	}

	public org.bukkit.entity.Entity getBukkitEntity() {
		return bukkitEntity;
	}


	@Override
	public String getName() {
		return bukkitEntity.getName();
	}

	
	@Override
	public boolean isPlayer() {
		return false;
	}



//	@Override
//	public boolean doesSupportColors() {
//		return false;
//	}





}
