package tech.mcprison.prison.tasks;

import tech.mcprison.prison.tasks.PrisonCommandTask.CustomPlaceholders;

public class PrisonCommandTaskPlaceholderData {

	private CustomPlaceholders placeholder;
	private String value;
	
	public PrisonCommandTaskPlaceholderData( CustomPlaceholders placeholder, String value ) {
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
	public CustomPlaceholders getPlaceholder() {
		return placeholder;
	}
	public void setPlaceholder( CustomPlaceholders placeholder ) {
		this.placeholder = placeholder;
	}

	public String getValue() {
		return value;
	}
	public void setValue( String value ) {
		this.value = value;
	}
	
}
