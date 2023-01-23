package tech.mcprison.prison.commands;

import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.Output;

public class CommandAccessResults {
	
	private CommandSender sender;

	private String command;
	private String perm;
	
	private boolean access = true;
	
	private CommandAccessRejectionReason rejectionReason;
	
	public enum CommandAccessRejectionReason {
		UNKNOWN__NOT_CHECKED,
		ACCESS_PERMITTED,

		REJECTION__COMMAND_PERMISSIONS,
		REJECTION__COMMAND_ALT_PERMISSIONS,
		REJECTION__CONFIG_YAML;
	}

	public CommandAccessResults( CommandSender sender ) {
		super();
		
		this.sender = sender;
		this.perm = "";
		
		this.access = true;
		this.rejectionReason = CommandAccessRejectionReason.UNKNOWN__NOT_CHECKED;
	}

	public void setAccessPermitted() {
		setAccess( true );
		setRejectionReason( CommandAccessRejectionReason.ACCESS_PERMITTED);
	}
	public void rejectCommandPermission( String command, String perm ) {
		setCommand( command );
		setPerm( perm );
		setAccess( false );
		setRejectionReason( CommandAccessRejectionReason.REJECTION__COMMAND_PERMISSIONS);
	}
	public void rejectCommandAltPermission( String command, String perm ) {
		setCommand( command );
		setPerm( perm );
		setAccess( false );
		setRejectionReason( CommandAccessRejectionReason.REJECTION__COMMAND_ALT_PERMISSIONS);
	}
	public void rejectConfigYaml( String command, String perm ) {
		setCommand( command );
		setPerm( perm );
		setAccess( false );
		setRejectionReason( CommandAccessRejectionReason.REJECTION__CONFIG_YAML);
	}
	
	public void debugAccess() {
		if ( Output.get().isDebug() ) {
			String msg = String.format( "Command Access for %s: %b [%s]  Perm: [%s] Reason: %s]", 
					getSender().getName(),
					isAccess(),
					getCommand() == null ? "" : getCommand(),
					getPerm() == null ? "" : getPerm(),
					getRejectionReason().name()
					);
			
			Output.get().logInfo( msg );
		}
	}
	
	public CommandSender getSender() {
		return sender;
	}
	public void setSender(CommandSender sender) {
		this.sender = sender;
	}

	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}

	public String getPerm() {
		return perm;
	}
	public void setPerm(String perm) {
		this.perm = perm;
	}

	public boolean isAccess() {
		return access;
	}
	public void setAccess(boolean access) {
		this.access = access;
	}

	public CommandAccessRejectionReason getRejectionReason() {
		return rejectionReason;
	}
	public void setRejectionReason(CommandAccessRejectionReason rejectionReason) {
		this.rejectionReason = rejectionReason;
	}
}
