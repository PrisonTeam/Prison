package tech.mcprison.prison.internal;

public interface ArmorStand 
		extends Entity {

	
	//public ArmorStand spawn(Location location);
	
//	public void setupArmorStand(String itemType);
//	public void setupArmorStand( ItemStack item );

	public boolean isVisible();
	public void setVisible( boolean visible );
	
	public boolean getRemoveWhenFarAway();
	public void setRemoveWhenFarAway( boolean removeWhenFarAway );

	public ItemStack getItemInHand();
	public void setItemInHand( ItemStack itemm );
	
	
	public void setRightArmPose( EulerAngle arm );
	
	public boolean isGlowing();
	public void setGlowing( boolean glowing );
	
	public boolean hasGravity();
	public void setGravity( boolean gravity );

	public boolean hasArms();
	public void setArms( boolean arms );

	public boolean hasBasePlate();
	public void setBasePlate( boolean basePlate );

	public boolean getCanPickupItems();
	public void setCanPickupItems( boolean canPickupItems );
	
	public boolean isSmall();
	public void setSmall(boolean small);
	
	public void setInvulnerable(boolean b);

}
