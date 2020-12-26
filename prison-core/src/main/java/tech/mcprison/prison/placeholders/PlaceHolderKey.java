package tech.mcprison.prison.placeholders;

import tech.mcprison.prison.placeholders.PlaceholderManager.PrisonPlaceHolders;

public class PlaceHolderKey {
	
	private String key;
	private PrisonPlaceHolders placeholder;
	private String data;
	private boolean primary = true;
	private String aliasName;
	
	public PlaceHolderKey( String key, PrisonPlaceHolders placeholder ) {
		this(key, placeholder, true);
	}
	public PlaceHolderKey( String key, PrisonPlaceHolders placeholder, boolean primary ) {
		this.key = key;
		this.placeholder = placeholder;
		this.primary = primary;
	}
	public PlaceHolderKey( String key, PrisonPlaceHolders placeholder, String data ) {
		this(key, placeholder);
		this.data = data;
	}
	public PlaceHolderKey( String key, PrisonPlaceHolders placeholder, String data, boolean primary ) {
		this(key, placeholder, primary);
		this.data = data;
	}

	public String getKey() {
		return key;
	}
	public void setKey( String key ) {
		this.key = key;
	}

	public PrisonPlaceHolders getPlaceholder() {
		return placeholder;
	}
	public void setPlaceholder( PrisonPlaceHolders placeholder ) {
		this.placeholder = placeholder;
	}

	public String getData() {
		return data;
	}
	public void setData( String data ) {
		this.data = data;
	}
	
	public boolean isPrimary() {
		return primary;
	}
	public void setPrimary( boolean primary ) {
		this.primary = primary;
	}
	
	public String getAliasName() {
		return aliasName;
	}
	public void setAliasName( String aliasName ) {
		this.aliasName = aliasName;
	}
}
