package tech.mcprison.prison.internal;

import java.util.List;
import java.util.UUID;

import tech.mcprison.prison.util.Location;
import tech.mcprison.prison.util.Vector;

public interface Entity
		extends CommandSender 
{


    /** Returns a unique and persistent id for this entity */
    public UUID getUniqueId();
    

    /** Gets the custom name on a mob. */
    public String getCustomName();
    
    /** Returns a unique id for this entity	 */
    public int getEntityId();
    
    /** Eject any passenger. */
    public boolean eject();
    
    /** Gets the entity's current position */
    public Location getLocation();
    
    /** Stores the entity's current position in the provided Location object. */
    public Location getLocation(Location loc);

    
    /** Returns the distance this entity has fallen */
    public float getFallDistance();
    
    /** Returns the entity's current fire ticks (ticks before the entity stops being on fire). */
    public int getFireTicks();
    
    /** Retrieve the last EntityDamageEvent inflicted on this entity. */
//    public EntityDamageEvent getLastDamageCause();
    
    /** Returns the entity's maximum fire ticks. */
    public int getMaxFireTicks();
    
    /** Returns a list of entities within a bounding box centered around this entity */
    public List<Entity> getNearbyEntities(double x, double y, double z);
    
    /** Gets the primary passenger of a vehicle. */
    public Entity getPassenger();
    
    /** Gets the Server that contains this Entity */
    //public Server getServer();
    
    /** Gets the amount of ticks this entity has lived for. */
    public int getTicksLived();
    
    /** Get the type of the entity. */
    public EntityType getType();
    
    /** Get the vehicle that this player is inside. */
    public Entity getVehicle();
    
    /** Gets this entity's current velocity */
    public Vector getVelocity();
    
    /** Gets the current world this entity resides in */
    public World getWorld();
    
    /** Gets whether or not the mob's custom name is displayed client side. */
    public boolean isCustomNameVisible();
    
    /** Sets a custom name on a mob. */
    public void setCustomName(String name);
    
    /** Sets whether or not to display the mob's custom name client side. */
    public void setCustomNameVisible(boolean flag);
    
    
    /** Returns true if this entity has been marked for removal. */
    public boolean isDead();
    
    /** Check if a vehicle has passengers. */
    public boolean isEmpty();
    
    /** Returns whether this entity is inside a vehicle. */
    public boolean isInsideVehicle();
    
    /** Returns true if the entity is supported by a block. */
    public boolean isOnGround();
    
    /** Returns false if the entity has died or been despawned for some other reason. */
    public boolean isValid();
    
    /** Leave the current vehicle. */
    public boolean leaveVehicle();
    
    /** Performs the specified EntityEffect for this entity. */
    //public void playEffect(EntityEffect type);
    
    /** Mark the entity's removal. */
    public void remove();
    
    /** Sets the fall distance for this entity */
    public void setFallDistance(float distance);
    
    /** Sets the entity's current fire ticks (ticks before the entity stops being on fire).  */
    public void setFireTicks(int ticks);
    
    /** Record the last EntityDamageEvent inflicted on this entity */
    //public void setLastDamageCause(EntityDamageEvent event);
    
    /** Set the passenger of a vehicle. */
    public boolean setPassenger(Entity passenger);
    
    /** Sets the amount of ticks this entity has lived for. */
    public void setTicksLived(int value);
    
    /** Sets this entity's velocity */
    public void setVelocity(Vector velocity);
    
    //public Entity.Spigot 	spigot();
    
    /**  Teleports this entity to the target Entity. */
    boolean teleport(Entity destination);
   
    /** Teleports this entity to the target Entity. */
    //public boolean teleport(Entity destination, PlayerTeleportEvent.TeleportCause cause);
    
    /** Teleports this entity to the given location. */
    public boolean teleport(Location location);
    
    /** Teleports this entity to the given location. */
   // public boolean 	teleport(Location location, PlayerTeleportEvent.TeleportCause cause);
    
    
}
