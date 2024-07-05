package tech.mcprison.prison.internal;

public class EntityType {
	
	public static final EntityType ENTITY_TYPE_ARMOR_STAND = new EntityType( "ARMOR_STAND" );
	
	private String entityType;
	
	public EntityType( String entityType ) {
		super();
		
		this.entityType = entityType;
	}

	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	
}
