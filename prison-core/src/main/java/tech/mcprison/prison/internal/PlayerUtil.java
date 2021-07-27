package tech.mcprison.prison.internal;

import java.util.UUID;

public abstract class PlayerUtil
{
	
	private UUID playerUuid;
	
	
	public enum ItemType {
		pickaxe,
		axe,
		shovel,
		hoe,
		
		sword,
		bow,
		crossbow,
		shears,
		
		boots,
		chestplate,
		helmet,
		horse_armor,
		leggings,
		shield;
		
		public static ItemType fromString( String value ) {
			ItemType results = null;
			
			if ( value != null && !value.isEmpty() ) {
				value = value.trim().toLowerCase();
				
				for ( ItemType iType : values() )
				{
					if ( value.contains( iType.name() )) {
						results = iType;
						break;
					}
				}
			}
			return results;
		}
	}

	public enum ItemMaterial {
		stone,
		wooden,
		iron,
		golden,
		diamond,
		netherite,
		chainmail,
		leather,
		turtle;
		
		public static ItemMaterial fromString( String value ) {
			ItemMaterial results = null;
			
			if ( value != null && !value.isEmpty() ) {
				value = value.trim().toLowerCase();
				
				for ( ItemMaterial iTMaterial : values() )
				{
					if ( value.contains( iTMaterial.name() )) {
						results = iTMaterial;
						break;
					}
				}
			}
			return results;
		}
	}
	
	public PlayerUtil( UUID playerUuid ) {
		super();
		
		this.playerUuid = playerUuid;
		
	}
	
	public abstract boolean isActive();
	
	public abstract double getMaxHealth();
	
	public abstract void setMaxHealth( double maxHealth );
	
	public abstract int getMaximumAir();
	
	public abstract int getRemainingAir();
	
	public abstract int getFoodLevel();
	
	public abstract double getExhaustion();
	
	public abstract double getExp();
	
	public abstract int getLevel();
	
	public abstract double getWalkSpeed();
	
	public abstract ItemStack getPrisonItemStack();
	
	public abstract String getItemInHandName();
	
	public abstract String getItemInHandDisplayName();
	
	public abstract String getItemInHandItemType();
	
	public abstract String getItemInHandItemMaterial();
	
	public abstract int getItemInHandDurability();
	
	public abstract int getItemInHandDurabilityMax();
	
	public abstract int getItemInHandEnchantmentFortune();
	
	public abstract int getItemInHandEnchantmentEfficency();
	
	public abstract int getItemInHandEnchantmentSilkTouch();
	
	public abstract int getItemInHandEnchantmentMending();
	
	public abstract int getItemInHandEnchantmentLuck();

	public UUID getPlayerUuid() {
		return playerUuid;
	}
	public void setPlayerUuid( UUID playerUuid ) {
		this.playerUuid = playerUuid;
	}
}
