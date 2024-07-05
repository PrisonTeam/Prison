package tech.mcprison.prison.internal;

public interface ArmorStand 
		extends Entity {

	
	//public ArmorStand spawn(Location location);
	

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
	
	
}
