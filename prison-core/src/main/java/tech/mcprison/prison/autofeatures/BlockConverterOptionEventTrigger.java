package tech.mcprison.prison.autofeatures;

/**
 * <p>The event trigger uses the block type that this is associated with to
 * identify when it should apply.
 * </p>
 * 
 */
public class BlockConverterOptionEventTrigger 
//	extends BlockConverterOptions 
	{

//	private String blockTriggerName;
	
	private String eventPluginName;
	
	private String description;
	
	private String eventPluginPriority;
	
	private String eventPluginClassName;
	
	private boolean allowPrisonToProccessDrops;
	
	private boolean removeBlockWithoutDrops;
	
	private boolean ignoreBlockInExplosionEvents;
	
	/**
	 * This is to be used as a transient cache of working with
	 * the external event.
	 */
	private transient Object externalResource;
	
	public BlockConverterOptionEventTrigger() {
		super();
//		super( BlockConverterOptionType.event_trigger );
	}

	public String getEventPluginName() {
		return eventPluginName;
	}
	public void setEventPluginName(String eventPluginName) {
		this.eventPluginName = eventPluginName;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getEventPluginPriority() {
		return eventPluginPriority;
	}
	public void setEventPluginPriority(String eventPluginPriority) {
		this.eventPluginPriority = eventPluginPriority;
	}

	public String getEventPluginClassName() {
		return eventPluginClassName;
	}
	public void setEventPluginClassName(String eventPluginClassName) {
		this.eventPluginClassName = eventPluginClassName;
	}

	public boolean isAllowPrisonToProccessDrops() {
		return allowPrisonToProccessDrops;
	}
	public void setAllowPrisonToProccessDrops(boolean allowPrisonToProccessDrops) {
		this.allowPrisonToProccessDrops = allowPrisonToProccessDrops;
	}

	public boolean isRemoveBlockWithoutDrops() {
		return removeBlockWithoutDrops;
	}
	public void setRemoveBlockWithoutDrops(boolean removeBlockWithoutDrops) {
		this.removeBlockWithoutDrops = removeBlockWithoutDrops;
	}

	public boolean isIgnoreBlockInExplosionEvents() {
		return ignoreBlockInExplosionEvents;
	}
	public void setIgnoreBlockInExplosionEvents(boolean ignoreBlockInExplosionEvents) {
		this.ignoreBlockInExplosionEvents = ignoreBlockInExplosionEvents;
	}

	public Object getExternalResource() {
		return externalResource;
	}
	public void setExternalResource(Object externalResource) {
		this.externalResource = externalResource;
	}

}
