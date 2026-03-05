package tech.mcprison.prison.wip.internal.heads;

import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.wip.internal.heads.MinecraftHeadsCache.MinecraftHeadsCategory;

public class MinecraftHeadData {

	private MinecraftHeadsCategory category;
	private String name;
	private String uuid;
	private String value;
	private String tags;
	
	private boolean used;
	
	private transient List<String> tagList;
	
	public MinecraftHeadData() {
		super();
		
		this.used = false;
		
		this.tagList = new ArrayList<>();
	}

	public MinecraftHeadsCategory getCategory() {
		return category;
	}
	public void setCategory(MinecraftHeadsCategory category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}

	public List<String> getTagList() {
		return tagList;
	}
	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}

	public boolean isUsed() {
		return used;
	}
	public void setUsed(boolean used) {
		this.used = used;
	}
}
