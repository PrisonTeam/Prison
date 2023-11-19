package tech.mcprison.prison.spigot.worldguard;

import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.platform.Platform;

public class WorldGuardData {

	private String worldGuardPrefix;
	
	private boolean enabled;
	private String name;
	
	private String permissionPrefix;
	private int priority;
	
	private boolean denyNonMembers;
	private String denyMessage;
	
	private List<String> flags;
	
	private int increaseX;
	private int increaseZ;
	private int increaseY;
	
	public WorldGuardData( String worldGuardPrefix ) {
		super();
		
		this.worldGuardPrefix = worldGuardPrefix;
		
		this.flags = new ArrayList<>();
		
		configure();
	}
	
	private void configure() {
		
		String wgPrefix = getWorldGuardPrefix();
		
		Platform platform = Prison.get().getPlatform();
		
		this.enabled = platform.getConfigBooleanFalse( wgPrefix + "enable" );
		
		if ( isEnabled() ) {
			
			this.name = platform.getConfigString( wgPrefix + "name-prefix" );
			
			this.permissionPrefix = platform.getConfigString( wgPrefix + "permission-prefix" );

			this.priority = platform.getConfigInt( wgPrefix + "priority", 10 );
			
			this.denyNonMembers = platform.getConfigBooleanFalse( wgPrefix + "deny-non-members" );
			
			this.denyMessage = platform.getConfigString( wgPrefix + "deny-message" );
		
			List<String> flagKeys = platform.getConfigHashKeys( wgPrefix + "flags" );
			
			for (String key : flagKeys) {
				boolean includeFlag = platform.getConfigBooleanFalse( wgPrefix + "flags" + "." + key );
				
				// Only flags that are enabled are stored:
				if ( includeFlag ) {
					flags.add( key );
				}
			}
			
			
			this.increaseX = platform.getConfigInt( wgPrefix + "increase-x", 0 );
			this.increaseZ = platform.getConfigInt( wgPrefix + "increase-z", 0 );
			this.increaseY = platform.getConfigInt( wgPrefix + "increase-y", 0 );
		
		}
		
	}

	public String getWorldGuardPrefix() {
		return worldGuardPrefix;
	}
	public void setWorldGuardPrefix(String worldGuardPrefix) {
		this.worldGuardPrefix = worldGuardPrefix;
	}

	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getPermissionPrefix() {
		return permissionPrefix;
	}
	public void setPermissionPrefix(String permissionPrefix) {
		this.permissionPrefix = permissionPrefix;
	}

	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean isDenyNonMembers() {
		return denyNonMembers;
	}
	public void setDenyNonMembers(boolean denyNonMembers) {
		this.denyNonMembers = denyNonMembers;
	}

	public String getDenyMessage() {
		return denyMessage;
	}
	public void setDenyMessage(String denyMessage) {
		this.denyMessage = denyMessage;
	}

	public List<String> getFlags() {
		return flags;
	}
	public void setFlags(List<String> flags) {
		this.flags = flags;
	}

	public int getIncreaseX() {
		return increaseX;
	}
	public void setIncreaseX(int increaseX) {
		this.increaseX = increaseX;
	}

	public int getIncreaseZ() {
		return increaseZ;
	}
	public void setIncreaseZ(int increaseZ) {
		this.increaseZ = increaseZ;
	}

	public int getIncreaseY() {
		return increaseY;
	}
	public void setIncreaseY(int increaseY) {
		this.increaseY = increaseY;
	}
	
}
