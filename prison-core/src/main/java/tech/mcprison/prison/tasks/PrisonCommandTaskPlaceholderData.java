package tech.mcprison.prison.tasks;

import tech.mcprison.prison.tasks.PrisonCommandTaskData.BlockEventCustomPlaceholders;

public class PrisonCommandTaskPlaceholderData {

	private BlockEventCustomPlaceholders placeholder;
	private String value;
	
	public PrisonCommandTaskPlaceholderData( BlockEventCustomPlaceholders placeholder, String value ) {
		super();
		
		this.placeholder = placeholder;
		this.value = value;
	}
	
	public boolean contains( String text ) {
		return text != null && text.contains( placeholder.getPlaceholder() );
	}

	public String replace( String text ) {
		String results = text;
		
		if ( text != null ) {
			results = text.replace( placeholder.getPlaceholder(), getValue() );
		}
		
		return results;
	}
	public BlockEventCustomPlaceholders getPlaceholder() {
		return placeholder;
	}
	public void setPlaceholder( BlockEventCustomPlaceholders placeholder ) {
		this.placeholder = placeholder;
	}

	public String getValue() {
		return value;
	}
	public void setValue( String value ) {
		this.value = value;
	}
	
}
