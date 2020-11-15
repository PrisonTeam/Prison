package tech.mcprison.prison.commands;

public abstract class BaseCommands
{
	private String cmdGroup;
	
	public BaseCommands( String cmdGroup ) {
		this.cmdGroup = cmdGroup;
	}

	public String getCmdGroup() {
		return cmdGroup;
	}
	public void setCmdGroup( String cmdGroup ) {
		this.cmdGroup = cmdGroup;
	}
}
