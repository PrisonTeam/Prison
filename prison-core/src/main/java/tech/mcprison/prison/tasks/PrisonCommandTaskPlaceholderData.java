package tech.mcprison.prison.tasks;

public class PrisonCommandTaskPlaceholderData {

	private String placeholder;
	private String value;
	
	public PrisonCommandTaskPlaceholderData( String placeholder, String value ) {
		super();
		
		this.placeholder = placeholder;
		this.value = value;
	}

	public String getPlaceholder() {
		return placeholder;
	}
	public void setPlaceholder( String placeholder ) {
		this.placeholder = placeholder;
	}

	public String getValue() {
		return value;
	}
	public void setValue( String value ) {
		this.value = value;
	}
	
}
