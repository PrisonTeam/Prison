package tech.mcprison.prison.autofeatures;

import java.util.ArrayList;

public class BlockConverterEventTrigger 
	extends BlockConverter {

	private ArrayList<BlockConverterOptionEventTrigger> options;
	
	public BlockConverterEventTrigger( String blockName, int keyQuantity ) {
		super( blockName, keyQuantity );
		
		this.options = new ArrayList<>();
	}

	public BlockConverterEventTrigger( String blockName, int keyQuantity, String mininumSpigotSemanticVersion ) {
		super( blockName, keyQuantity, mininumSpigotSemanticVersion );

		this.options = new ArrayList<>();
	}
	
	public BlockConverterEventTrigger( String blockName ) {
		this( blockName, 1 );
	}
	
	public ArrayList<BlockConverterOptionEventTrigger> getOptions() {
		return options;
	}
	public void setOptions(ArrayList<BlockConverterOptionEventTrigger> options) {
		this.options = options;
	}
}
