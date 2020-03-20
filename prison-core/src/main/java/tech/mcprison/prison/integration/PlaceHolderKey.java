package tech.mcprison.prison.integration;

import tech.mcprison.prison.integration.IntegrationManager.PrisonPlaceHolders;

public class PlaceHolderKey {
	
	private String key;
	private PrisonPlaceHolders placeholder;
	private String data;
	
	public PlaceHolderKey( String key, PrisonPlaceHolders placeholder ) {
		this.key = key;
		this.placeholder = placeholder;
	}
	public PlaceHolderKey( String key, PrisonPlaceHolders placeholder, String data ) {
		this(key, placeholder);
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
}
