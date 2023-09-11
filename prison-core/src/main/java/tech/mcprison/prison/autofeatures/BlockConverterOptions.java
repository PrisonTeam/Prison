package tech.mcprison.prison.autofeatures;

public abstract class BlockConverterOptions {

	private final BlockConverterOptionType blockConverterOptionType;
	
	public BlockConverterOptions() {
		super();
		
		this.blockConverterOptionType = BlockConverterOptionType.no_options_set;
	}
	public BlockConverterOptions( BlockConverterOptionType optionType ) {
		super();
		
		this.blockConverterOptionType = optionType;
	}
	
	public enum BlockConverterOptionType {
		no_options_set,
		event_trigger;
	}

	public BlockConverterOptionType getBlockConverterOptionType() {
		return blockConverterOptionType;
	}
	
}
